package it.yboschetto.bot1.objects;

import static it.yboschetto.bot1.objects.Utility.rev;

import java.util.Calendar;

public class Moon extends SolarObject {
	 private final double CYCLE = 29.53;
	 private String emoji = "";

	    public Moon() {
	        N = 125.1228 - 0.0529538083 * JD;
	        i = 5.1454;
	        w = 318.0634 + 0.1643573223 * JD;
	        a = 60.2666;
	        e = 0.054900;
	        M = 115.3654 + 13.0649929509 * JD;
	        N = rev(N);
	        w = rev(w);
	        M = rev(M);
	    }

	    public double getNumericPhase(){
	        // ultima luna piena 28 settembre 20.26
	        double lastMoon,now,diffMoon, newMoons,result;
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(2019,Calendar.SEPTEMBER,28,20,26,0);
	        Utility.calculateJulianDate(calendar);

	        // #Schifo
	        lastMoon = Utility.getD();
	        Utility.calculateJulianDate(Calendar.getInstance());
	        now = Utility.getD();

	        diffMoon = now - lastMoon;
	        newMoons = diffMoon / CYCLE;

	        result = (newMoons - (int)newMoons)*CYCLE;

	        return result;

	    }

	    public String getLunarPhase(){
	        double phaseDouble = getNumericPhase();
	        String phase = "";
	        if(phaseDouble>27.6 || phaseDouble < 1.6 ){
	            phase = "Luna nuova";
	            emoji = ":new_moon:";
	        }
	        else if(phaseDouble>1.6 || phaseDouble < 5.4 ){
	            phase = "Luna crescente";
	            emoji = ":waning_crescent_moon:";
	        }
	        else if(phaseDouble>5.4 || phaseDouble < 8.6 ){
	            phase = "Primo quarto";
	            emoji = ":last_quarter_moon:";
	        }
	        else if(phaseDouble>8.6 || phaseDouble < 12.4 ){
	            phase = "Gibbosa crescente";
	            emoji = ":waning_gibbous_moon:";
	        }
	        else if(phaseDouble>12.4 || phaseDouble < 16.6 ){
	            phase = "Luna piena";
	            emoji = ":full_moon:";
	        }
	        else if(phaseDouble>16.6 || phaseDouble < 20.4 ){
	            phase = "Gibbosa calante";
	            emoji = ":waxing_gibbous_moon:";
	        }
	        else if(phaseDouble>20.4|| phaseDouble <  23.6){
	            phase = "Ultimo quarto";
	            emoji = ":first_quarter_moon:";
	        }
	        else if(phaseDouble>23.6 || phaseDouble < 27.6 ){
	            phase = "Luna calante";
	            emoji = ":waxing_crescent_moon:";
	        }

	        return phase;
	    }
	    
	    public String getEmoji() {
	    	return emoji;
	    }

}
