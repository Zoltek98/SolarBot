package it.yboschetto.bot1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.vdurmont.emoji.EmojiParser;

import it.yboschetto.bot1.objects.Direzione;
import it.yboschetto.bot1.objects.GenericObject;
import it.yboschetto.bot1.objects.Giove;
import it.yboschetto.bot1.objects.Luna;
import it.yboschetto.bot1.objects.Marte;
import it.yboschetto.bot1.objects.Mercurio;
import it.yboschetto.bot1.objects.Nettuno;
import it.yboschetto.bot1.objects.Saturno;
import it.yboschetto.bot1.objects.Sole;
import it.yboschetto.bot1.objects.Urano;
import it.yboschetto.bot1.objects.Venere;

@Component
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
	Sole sole;
	Mercurio mercurio;
	Venere venere;
	Luna luna;
	Marte marte;
	Giove giove;
	Saturno saturno;
	Urano urano;
	Nettuno nettuno;
	int d;

	double latitude = 0, longitude = 0;

	@Value("${comands.list}")
	private String[] comandi;

	@Override
	public void onUpdateReceived(Update update) {

		message_text = update.getMessage().getText();
		username = update.getMessage().getChat().getUserName();
		long chat_id = update.getMessage().getChatId();
		SendMessage message = new SendMessage().setChatId(chat_id);

		if (update.hasMessage() && update.getMessage().hasText()) {

			String user_first_name = update.getMessage().getChat().getFirstName();
			String user_last_name = update.getMessage().getChat().getLastName();
			String user_username = update.getMessage().getChat().getUserName();
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
				
				message.setText(answer);
			}
			else if(latitude==0 && longitude==0) {//Controllo lat e long  prima di tutto il resto 
				//Non ha immesso la posizione 
				answer=Messages.utonto();
				message.setText(answer);
			}

			else if (message_text.equals("/comandi") || message_text.equals("Lista comandi")) {

				clear();

				setDefaultCommand();
				keyboardMarkup.setKeyboard(keyboard);
				keyboardMarkup.setResizeKeyboard(false);
				message.setReplyMarkup(keyboardMarkup);
				commands = getCommands();
				answer = EmojiParser.parseToUnicode(
						"Ecco la lista dei comandi : \n:point_down::point_down::point_down: " + commands);
				message.setText(answer);
			} else if (message_text.equals("Lista Visibili") || message_text.equals("/ListaVisibili")) {

				// Check oggetti visibili, per ora non fa niente
				if (checkVisibles()) {

					answer = Messages.planetsVisible(visibleObjectList);

				} else {
					answer = Messages.noPlanets(sole.getTramonto());
				}
				message.setText(answer);
			} else if (message_text.equals("Alba e Tramonto") || message_text.equals("/AlbaTramonto")) {

				// Chiamata getAlba e getTramonto
				alba = sole.getAlba();
				tramonto = sole.getTramonto();
				answer = EmojiParser
						.parseToUnicode(":sunrise: Alba: " + alba + "\n:city_sunset: Tramonto: " + tramonto);
				message.setText(answer);
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
				answer = EmojiParser.parseToUnicode(
						"Ecco la lista degli oggetti planetari : \n:point_down::point_down::point_down:");
				message.setText(answer);

			} else if (message_text.equals(EmojiParser.parseToUnicode(":back: Torna indietro"))) {
				clear();
				setDefaultCommand();
				keyboardMarkup.setKeyboard(keyboard);
				message.setReplyMarkup(keyboardMarkup);
				message.setText(message_text);
			} else if (message_text.equals("Mercurio")) {
				answer = Messages.planetMessage("Mercurio", mercurio.getTramonto(), mercurio.getAlba(),
						sole.getTramonto(), mercurio.Visibile(), Direzione.getDirezione(mercurio.getAzimuth()),
						mercurio.getAltitude());
				message.setText(answer);
			} else if (message_text.equals("Venere")) {
				answer = Messages.planetMessage("Venere", venere.getTramonto(), venere.getAlba(), sole.getTramonto(),
						venere.Visibile(), Direzione.getDirezione(venere.getAzimuth()), venere.getAltitude());
				message.setText(answer);
			} else if (message_text.equals("Luna")) {
				answer = Messages.planetMessage("Luna", luna.getTramonto(), luna.getAlba(), sole.getTramonto(),
						luna.Visibile(), Direzione.getDirezione(luna.getAzimuth()), luna.getAltitude());
				message.setText(answer);
			} else if (message_text.equals("Marte")) {
				answer = Messages.planetMessage("Marte", marte.getTramonto(), marte.getAlba(), sole.getTramonto(),
						marte.Visibile(), Direzione.getDirezione(marte.getAzimuth()), marte.getAltitude());
				message.setText(answer);
			} else if (message_text.equals("Giove")) {
				answer = Messages.planetMessage("Giove", giove.getTramonto(), giove.getAlba(), sole.getTramonto(),
						giove.Visibile(), Direzione.getDirezione(giove.getAzimuth()), giove.getAltitude());
				message.setText(answer);
			} else if (message_text.equals("Saturno")) {
				answer = Messages.planetMessage("Saturno", saturno.getTramonto(), saturno.getAlba(), sole.getTramonto(),
						saturno.Visibile(), Direzione.getDirezione(saturno.getAzimuth()), saturno.getAltitude());
				message.setText(answer);
			} else if (message_text.equals("Urano")) {
				answer = Messages.planetMessage("Urano", urano.getTramonto(), urano.getAlba(), sole.getTramonto(),
						urano.Visibile(), Direzione.getDirezione(urano.getAzimuth()), urano.getAltitude());
				message.setText(answer);
			} else if (message_text.equals("Nettuno")) {
				answer = Messages.planetMessage("Nettuno", nettuno.getTramonto(), nettuno.getAlba(), sole.getTramonto(),
						nettuno.Visibile(), Direzione.getDirezione(nettuno.getAzimuth()), nettuno.getAltitude());
				message.setText(answer);
			}

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
			
			message.setText(answer);
		}

		try {

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
		return "SolarBot";
	}

	@Override 
	public String getBotToken() {

		return "814488899:AAE7n6sUnclziB26FMtuw8HOmy8EEVH0x0c";
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
		keyboard.add(row2);
		keyboard.add(row);
		keyboard.add(row3);
		
	}

	public boolean checkVisibles() {

		double hTramonto = Double.parseDouble(sole.getTramonto().substring(0, 2));
		double mTramonto = Double.parseDouble(sole.getTramonto().substring(3));
		visibleObjectList.clear();
		if (adesso.getHours() > hTramonto || (adesso.getHours() == hTramonto && adesso.getMinutes() > mTramonto)) {
			// Se è notte
			if (mercurio.Visibile()) {
				visibleObjectList.add(new GenericObject("Mercurio", "", "", 1, mercurio.getAzimuth()));
			}
			if (venere.Visibile()) {
				visibleObjectList.add(new GenericObject("Venere", "", "", 1, venere.getAzimuth()));
			}
			if (luna.Visibile()) {
				visibleObjectList.add(new GenericObject("Luna", "", "", 1, luna.getAzimuth()));
			}
			if (marte.Visibile()) {
				visibleObjectList.add(new GenericObject("Marte", "", "", 1, marte.getAzimuth()));
			}
			if (giove.Visibile()) {
				visibleObjectList.add(new GenericObject("Giove", "", "", 1, giove.getAzimuth()));
			}
			if (saturno.Visibile()) {
				visibleObjectList.add(new GenericObject("Saturno", "", "", 1, saturno.getAzimuth()));
			}
			if (urano.Visibile()) {
				visibleObjectList.add(new GenericObject("Urano", "", "", 1, urano.getAzimuth()));
			}
			if (nettuno.Visibile()) {
				visibleObjectList.add(new GenericObject("Nettuno", "", "", 1, nettuno.getAzimuth()));
			}
			return true;
		}
		// else non è notte
		return false;
	}

	public void init() {
		adesso = new Date();
		d = 367 * (1900 + adesso.getYear()) - 7 * ((1900 + adesso.getYear()) + ((1 + adesso.getMonth()) + 9) / 12) / 4
				+ 275 * (1 + adesso.getMonth()) / 9 + adesso.getDate() - 730530;
		ecl = 23.4393 - 3.563E-7 * d;

		sole = new Sole(d, ecl, adesso);
		sole.Posizione(latitude, longitude);

	}

	public void initPlanets() {

		mercurio = new Mercurio(d, ecl, adesso);
		mercurio.Posizione(latitude, longitude);
		venere = new Venere(d, ecl, adesso);
		venere.Posizione(latitude, longitude);
		luna = new Luna(d, ecl, adesso);
		luna.Posizione(latitude, longitude);
		marte = new Marte(d, ecl, adesso);
		marte.Posizione(latitude, longitude);
		giove = new Giove(d, ecl, adesso);
		giove.Posizione(latitude, longitude);
		saturno = new Saturno(d, ecl, adesso);
		saturno.Posizione(latitude, longitude);
		urano = new Urano(d, ecl, adesso);
		urano.Posizione(latitude, longitude);
		nettuno = new Nettuno(d, ecl, adesso);
		nettuno.Posizione(latitude, longitude);
	}

}
