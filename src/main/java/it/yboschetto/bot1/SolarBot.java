package it.yboschetto.bot1;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.vdurmont.emoji.EmojiParser;

import it.yboschetto.bot1.objects.*;
import it.yboschetto.bot1.objects.GenericObject;

/**
 * 
 * @author bosyu
 * @version 0.7.1b
 * SolarBot 
 *
 */

//@Component
public class SolarBot extends TelegramLongPollingBot {

	ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
	List<KeyboardRow> keyboard = new ArrayList<>();
	List<GenericObject> visibleObjectList = new ArrayList<GenericObject>();
	KeyboardRow row = new KeyboardRow();
	KeyboardRow row2 = new KeyboardRow();
	KeyboardRow row3 = new KeyboardRow();
	KeyboardRow row4 = new KeyboardRow();
	String message_text;
	String answer = "NaN";
	String username, commands, city = "";
	String alba, tramonto;
	Date adesso;
	Double ecl;
	Sun sole ;
	Mercury mercurio;
	Venus venere;
	Moon luna;
	Mars marte;
	Jupiter giove;
	Saturn saturno;
	Uranus urano;
	Neptune nettuno;
	double d;
	double GMT=2;

	double latitude = 0, longitude = 0;

	@Value("${comands.list}")
	private String[] comandi = {"/comandi - Mostra la lista dei comandi ","/AlbaTramonto - Mostra alba e tramonto di oggi","/ListaVisibili - Mostra tutti gli oggetti attulmente visibili e la loro posizione","/Oggetti - Apre una tastiera da cui e' possbile accedere ai dati dei vari oggetti"};
	
	@Value("${telegram.bot.username}")
	private String BOTNAME;
	
	 private String token_test = "875502005:AAH7wBx1hsmmTio04RI4EgX9ZrDnY9QOl8g";
	 private String token_prod = "814488899:AAE7n6sUnclziB26FMtuw8HOmy8EEVH0x0c";
	 
	private String TOKEN = token_test;
	
	@Value("${log.file.path}")
	private String PATH;

	@Override
	public void onUpdateReceived(Update update) {

		message_text = update.getMessage().getText();
		username = update.getMessage().getChat().getUserName();
		long chat_id = update.getMessage().getChatId();
		SendMessage message = new SendMessage().setChatId(chat_id);
		
		
		if (update.hasMessage() && update.getMessage().hasText()) {

			String user_first_name = update.getMessage().getChat().getFirstName();
			String user_last_name = update.getMessage().getChat().getLastName();
			long user_id = update.getMessage().getChat().getId();

			init();
			initPlanets();
			
			if (message_text.equals("/start")) {
				clear();
				setFirstCommand();
				keyboardMarkup.setKeyboard(keyboard);
				keyboardMarkup.setResizeKeyboard(true);
				
				message.setReplyMarkup(keyboardMarkup);
			
				answer=Messages.askPosition()+EmojiParser.parseToUnicode("\n\nEcco la lista dei comandi : \n:point_down::point_down::point_down:")+getCommands();
				
			}
			else if(latitude==0 && longitude==0 && !message_text.equals(EmojiParser.parseToUnicode(":back: Torna indietro")) ) {			//Controllo lat e long  prima di tutto il resto 
															
				answer=Messages.utonto();						//Non ha immesso la posizione 
			}

			else if (message_text.equals("/comandi") || message_text.equals("Lista comandi")) {

				clear();

				setDefaultCommand();
				keyboardMarkup.setKeyboard(keyboard);
				keyboardMarkup.setResizeKeyboard(false);
				message.setReplyMarkup(keyboardMarkup);
				commands = getCommands();
				answer = Messages.showCommands(commands);
			} else if (message_text.equals("Lista Visibili") || message_text.equals("/ListaVisibili")) {

				// Check oggetti visibili
				if (checkVisibles()) {

					answer = Messages.planetsVisible(visibleObjectList);

				} else {
					answer = Messages.noPlanets(sole.getTramonto(sole));
				}
			} else if (message_text.equals("Alba e Tramonto") || message_text.equals("/AlbaTramonto")) {

				// Chiamata getAlba e getTramonto
				alba = sole.getAlba(sole);
				tramonto = sole.getTramonto(sole);
				answer=Messages.showSunRiseSet(alba,tramonto);
			} else if (message_text.equals("Oggetti") || message_text.equals("/Oggetti")) {
				clear();
				row.add("Sole");
				row.add("Mercurio");
				row.add("Venere");
				row2.add("Luna");
				row2.add("Marte");
				row2.add("Giove");
				row3.add("Saturno");
				row3.add("Urano");
				row3.add("Nettuno");
				row4.add(EmojiParser.parseToUnicode(":back: Torna indietro"));
				keyboard.add(row);
				keyboard.add(row2);
				keyboard.add(row3);
				keyboard.add(row4);

				keyboardMarkup.setKeyboard(keyboard);
				message.setReplyMarkup(keyboardMarkup);
				answer=Messages.showPlanetaryList();
				

			} else if (message_text.equals(EmojiParser.parseToUnicode(":back: Torna indietro"))) {
				clear();
				setDefaultCommand();
				keyboardMarkup.setKeyboard(keyboard);
				message.setReplyMarkup(keyboardMarkup);
				answer=message_text;
			} else if(message_text.equals("Sole")) {
				answer=Messages.sunMessage(
						
						sole.getAlba(sole),
						sole.getTramonto(sole),
						Utility.getDirezione(sole.getAzimuth()),
						sole.getAltitude(),
						sole.getDateTramonto(sole));		
				
			} else if (message_text.equals("Mercurio")) {
				answer = Messages.planetMessage("Mercurio", mercurio.getTramonto(sole), mercurio.getAlba(sole),
						sole.getDateTramonto(sole), mercurio.isVisible(), Utility.getDirezione(mercurio.getAzimuth()),
						mercurio.getAltitude());
			} else if (message_text.equals("Venere")) {
				answer = Messages.planetMessage("Venere", venere.getTramonto(sole), venere.getAlba(sole), sole.getDateTramonto(sole),
						venere.isVisible(), Utility.getDirezione(venere.getAzimuth()), venere.getAltitude());
			} else if (message_text.equals("Luna")) {
				answer = Messages.moonMessage(luna.getTramonto(sole), luna.getAlba(sole),
						luna.isVisible(), Utility.getDirezione(luna.getAzimuth()), luna.getAltitude(),luna.getLunarPhase(),luna.getEmoji());
			} else if (message_text.equals("Marte")) {
				answer = Messages.planetMessage("Marte", marte.getTramonto(sole), marte.getAlba(sole), sole.getDateTramonto(sole),
						marte.isVisible(), Utility.getDirezione(marte.getAzimuth()), marte.getAltitude());
			} else if (message_text.equals("Giove")) {
				answer = Messages.planetMessage("Giove", giove.getTramonto(sole), giove.getAlba(sole), sole.getDateTramonto(sole),
						giove.isVisible(), Utility.getDirezione(giove.getAzimuth()), giove.getAltitude());
			} else if (message_text.equals("Saturno")) {
				answer = Messages.planetMessage("Saturno", saturno.getTramonto(sole), saturno.getAlba(sole), sole.getDateTramonto(sole),
						saturno.isVisible(), Utility.getDirezione(saturno.getAzimuth()), saturno.getAltitude());
			} else if (message_text.equals("Urano")) {
				answer = Messages.planetMessage("Urano", urano.getTramonto(sole), urano.getAlba(sole), sole.getDateTramonto(sole),
						urano.isVisible(), Utility.getDirezione(urano.getAzimuth()), urano.getAltitude());
			} else if (message_text.equals("Nettuno")) {
				answer = Messages.planetMessage("Nettuno", nettuno.getTramonto(sole), nettuno.getAlba(sole), sole.getDateTramonto(sole),
						nettuno.isVisible(), Utility.getDirezione(nettuno.getAzimuth()), nettuno.getAltitude());
			} else if(message_text.equals("42")) {
				// Inizio EasterEgg
				
				SendPhoto msg;
				try {
					msg = new SendPhoto()
							.setChatId(chat_id)
							.setPhoto("42",new FileInputStream("img/42.jpg"))
							.setCaption("42");
					
					execute(msg);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				answer="Il senso della vita";
			} else if(message_text.contains("dio") || message_text.contains("Dio")) {
				answer=Messages.quiet();
			}
			
			//Fine easter egg
			

			log(user_first_name, user_last_name, Long.toString(user_id), message_text, answer);

		} else if (update.hasMessage() && update.getMessage().getLocation() != null) {
			latitude = update.getMessage().getLocation().getLatitude();
			longitude = update.getMessage().getLocation().getLongitude();
			
			init();
			initPlanets();
			commands = getCommands();
			answer = Messages.locationUpdated(latitude, longitude);
				
			clear();
			
			setDefaultCommand();
			keyboardMarkup.setKeyboard(keyboard);
			keyboardMarkup.setResizeKeyboard(false);
			message.setReplyMarkup(keyboardMarkup);
			
		}
		else {
			answer=Messages.commandNotFound(commands);
		}

		System.out.println("\n ----------------------------\nLatitudine : "+latitude+"\nLongitudine : "+longitude);
		try {
			//System.out.println(System.getProperty("user.dir"));
			message.setText(answer); 
			execute(message);

		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

	}

	public String getCommands() {

		String commandString = "";
		for (int i = 0; i < comandi.length; i++) {
			commandString += "\n" + comandi[i];
		}

		return commandString;
	}

	@Override
	public String getBotUsername() {
		return BOTNAME;
	}

	@Override 
	public String getBotToken() {
		return TOKEN;
	}

	private void log(String first_name, String last_name, String user_id, String txt, String bot_answer) {
		System.out.println("\n ----------------------------");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		System.out
				.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
		System.out.println("Bot answer: \n Text - " + bot_answer);
	
	}

	private void clear() {
		keyboard.clear();
		row.clear();
		row2.clear();
		row3.clear();
		row4.clear();
	}

	private void setDefaultCommand() {
		// row.add("Lista Visibili");
		KeyboardButton key = new KeyboardButton();
		key.setRequestLocation(true);
		key.setText("Imposta posizione");
		row.add("Lista Visibili");
		row.add("Alba e Tramonto");
		row.add("Oggetti");
		keyboard.add(row);
		row2.add("Lista comandi");
		row2.add(key);
		keyboard.add(row2);
	}

	public void setFirstCommand() {
		// row.add("Lista Visibili");
		KeyboardButton key = new KeyboardButton();
		key.setRequestLocation(true);
		key.setText(EmojiParser.parseToUnicode(":pushpin: Imposta posizione"));
		
		row.add(key);
		keyboard.add(row);
		
	}

	public boolean checkVisibles() {

		visibleObjectList.clear();
		if (adesso.after(sole.getDateTramonto(sole)) || adesso.before(sole.getDateAlba(sole))) {
			// Se è notte
			if (mercurio.isVisible()) {
				visibleObjectList.add(new GenericObject("Mercurio", "", "", 1, mercurio.getAzimuth()));
			}
			if (venere.isVisible()) {
				visibleObjectList.add(new GenericObject("Venere", "", "", 2, venere.getAzimuth()));
			}
			if (luna.isVisible()) {
				visibleObjectList.add(new GenericObject("Luna", "", "", 3, luna.getAzimuth()));
			}
			if (marte.isVisible()) {
				visibleObjectList.add(new GenericObject("Marte", "", "", 4, marte.getAzimuth()));
			}
			if (giove.isVisible()) {
				visibleObjectList.add(new GenericObject("Giove", "", "", 5, giove.getAzimuth()));
			}
			if (saturno.isVisible()) {
				visibleObjectList.add(new GenericObject("Saturno", "", "", 6, saturno.getAzimuth()));
			}
			if (urano.isVisible()) {
				visibleObjectList.add(new GenericObject("Urano", "", "", 7, urano.getAzimuth()));
			}
			if (nettuno.isVisible()) {
				visibleObjectList.add(new GenericObject("Nettuno", "", "", 8, nettuno.getAzimuth()));
			}
			return true;
		}
		// else non è notte
		return false;
	}

	public void init() {
		adesso = new Date();
		d = 367 * (1900 + adesso.getYear()) - 7 * ((1900 + adesso.getYear()) + ((1 + adesso.getMonth()) + 9) / 12) / 4
				+ 275 * (1 + adesso.getMonth()) / 9 + adesso.getDate() +1721013.5+(adesso.getHours()-GMT)/24  ;
		ecl = 23.4393 - 3.563E-7 * d;

		sole = new Sun();
		sole.calculateAzimuthalPoistion(latitude, longitude);
				

	}

	public void initPlanets() {

		mercurio = new Mercury();
		mercurio.calculateAzimuthalPoistion(latitude, longitude);
		venere = new Venus();
		venere.calculateAzimuthalPoistion(latitude, longitude);
		luna = new Moon();
		luna.calculateAzimuthalPoistion(latitude, longitude);
		marte = new Mars();
		marte.calculateAzimuthalPoistion(latitude, longitude);
		giove = new Jupiter();
		giove.calculateAzimuthalPoistion(latitude, longitude);
		saturno = new Saturn();
		saturno.calculateAzimuthalPoistion(latitude, longitude);
		urano = new Uranus();
		urano.calculateAzimuthalPoistion(latitude, longitude);
		nettuno = new Neptune();
		nettuno.calculateAzimuthalPoistion(latitude, longitude);
	}


}
