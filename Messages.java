package it.yboschetto.bot1;

import java.util.Date;
import java.util.List;

import com.vdurmont.emoji.EmojiParser;

import it.yboschetto.bot1.objects.GenericObject;

public class Messages {

	public static String planetMessage(String name,String alba,String tramonto,String tramontoSole,boolean visibile,String direzione,double azimuth) {
		String message="";
		double hTramontoSole,mTramontoSole;
		hTramontoSole=Double.parseDouble(tramontoSole.substring(0, 2));
		mTramontoSole=Double.parseDouble(tramontoSole.substring(3));
		Date nowDate=new Date();
		if(nowDate.getHours()>hTramontoSole || (nowDate.getHours()==hTramontoSole && nowDate.getMinutes()>mTramontoSole)) {//se siamo dopo il tramonto
			message=name+" :\n"+
					((visibile)? ":telescope: " :":no_entry_sign: ")+"Attualmente "+((visibile)? "" :"non")+" visibile\n"+
					":arrow_up_small: Sorge alle "+alba+"\n"+
					":arrow_down_small: Tramonta alle "+tramonto+
					((visibile)?"\n:globe_with_meridians: In direzione "+direzione+"\n:point_up: A "+(int)azimuth+"° sull'orrizzonte":"");
		
		}
		else {//siamo prima del tramonto
			message=name+" :\n"+
					((visibile)? ":sunny: " :":no_entry_sign: ")+"Attualmente "+((visibile)? "sarebbe visibile \n						ma il sole non è ancora tramontato" :"non visibile")+" \n"+
					":arrow_up_small: Sorge alle "+alba+"\n"+
					":arrow_down_small: Tramonta alle "+tramonto;
		}
		
		
		
		return EmojiParser.parseToUnicode(message);
	}
	
	public static String noPlanets(String tramonto) {
		String message="";
		
		message="Goditi il sole fino alle "+tramonto+" :sunglasses:\n"+"Nessun'altro oggetto è visible";
		return EmojiParser.parseToUnicode(message);
	}
	
	public static String planetsVisible(List<GenericObject> list) {
		String message=":telescope: Alza gli occhi al cielo, puoi vedere:\n\n";
		
		for(int i=0;i<list.size();i++) {
			message+=String.format(":point_right: "+list.get(i).getName()+" in direzione %-28s\n",list.get(i).getDirezione());
		}
		return EmojiParser.parseToUnicode(message);
	}
	
	public static String welcome(String username,String commands) {
		String message="Benvenuto "+username+ " in SolarBot\n"+
					   "Ecco la lista dei comandi :	"+
					    commands;
		
		return EmojiParser.parseToUnicode(message);
	}
	
	public static String locationUpdated(double latitude,double longitude) {
		String message=":globe_with_meridians: Nuova posizione impostata a\n"+
					   ":triangular_flag_on_post: Latitudine:  "+latitude+"°\n"+
					   ":triangular_flag_on_post: Longitudine: "+longitude+"°\n";
		
		return EmojiParser.parseToUnicode(message);
	}
	
}
