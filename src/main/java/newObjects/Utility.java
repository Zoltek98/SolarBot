package newObjects;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

	private static double d,ecl;
	public final static double GMT=2;
	public final static double PI = 3.14159265358979323846;
	
	public static String getDirezione(double azimuth){
        String direzione="";

        if(azimuth>=348.75 || azimuth<11.25){
            direzione="N";
        }
        else if(azimuth>=11.25 && azimuth <33.75){
            direzione="NNE";
        }
        else if(azimuth>=33.75 && azimuth <56.25){
            direzione="NE";
        }
        else if(azimuth>=56.25 && azimuth <78.75){
            direzione="ENE";
        }
        else if(azimuth>=78.75 && azimuth <101.25){
            direzione="E";
        }
        else if(azimuth>=101.25 && azimuth <123.75){
            direzione="ESE";
        }
        else if(azimuth>=123.75 && azimuth <146.25){
            direzione="SE";
        }
        else if(azimuth>=146.25 && azimuth <168.75){
            direzione="SSE";
        }
        else if(azimuth>=168.75 && azimuth <191.25){
            direzione="S";
        }
        else if(azimuth>=191.25 && azimuth <213.75){
            direzione="SSO";
        }
        else if(azimuth>=213.75 && azimuth <236.25){
            direzione="SO";
        }
        else if(azimuth>=236.25 && azimuth <258.75){
            direzione="OSO";
        }
        else if(azimuth>=258.75 && azimuth <281.25){
            direzione="O";
        }
        else if(azimuth>=281.25 && azimuth <303.75){
            direzione="ONO";
        }
        else if(azimuth>=303.75 && azimuth <326.25){
            direzione="NO";
        }
        else if(azimuth>=326.25 && azimuth <348.75){
            direzione="NNO";
        }



        return direzione;
    }
	
	public static void calculateJulianDate(Date adesso) {
		d = 367 * (1900 + adesso.getYear()) - 7 * ((1900 + adesso.getYear()) + ((1 + adesso.getMonth()) + 9) / 12) / 4
				+ 275 * (1 + adesso.getMonth()) / 9 + adesso.getDate() +1721013.5+(adesso.getHours()-GMT)/24  ;
		ecl = 23.4393 - 3.563E-7 * d;
	}

	public static double getD() {
		return d;
	}
	
	public static double getEcl() {
		return ecl;
	}
	
	public static double rev(double n) {
		
		if (n > 360) {
			while (n > 360) {
				n -= 360;
			}
		}
		if (n < 0) {
			while (n < 0) {
				n += 360;
			}
		}
		return n;
		
	}
	
	public static String format(double orario) {    //   #Poverate

        String orario_formattato = "";
        String hh = String.valueOf(orario).substring(0, String.valueOf(orario).indexOf('.'));
        double mm;
        String sMm = String.valueOf(orario).substring(String.valueOf(orario).indexOf('.') + 1, String.valueOf(orario).indexOf('.') + 3);
        mm = Double.parseDouble(sMm);

        mm = mm * 3 / 5;

        sMm = String.valueOf(mm).substring(0, 2);
        if (sMm.contains(".")) {
            sMm = "0" + sMm.charAt(0);
        }
        if(hh.length()==1){
            hh=0+hh;
        }
        orario_formattato = hh + ":" + sMm;
        return orario_formattato;
    }
	
	public static Date formatDate(String hour) {// #AltraPoverata

		Date date = new Date();

		date.setHours(Integer.parseInt(hour.substring(0,2)));
		date.setMinutes(Integer.parseInt(hour.substring(0,2)));
	
		return date;
	}
	
	public static double getGMT() {
		return GMT;
	}
}
