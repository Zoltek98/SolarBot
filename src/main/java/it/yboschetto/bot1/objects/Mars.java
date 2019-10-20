package it.yboschetto.bot1.objects;

import java.util.Calendar;
import java.util.Date;

public class Mars extends SolarObject{

	public Mars() {
		super();
        N =  Utility.rev(49.5574 + 2.11081E-5 * JD);
        i = Utility.rev(1.8497 - 1.78E-8 * JD);
        w = Utility.rev(286.5016 + 2.92961E-5 * JD);
        a = 1.523678731494108E+00;
        e = Utility.rev(0.093405 + 2.516E-9 * JD);
        M = Utility.rev(18.6021 + 0.5240207766 * JD);
        setName("Marte");
	}

}
