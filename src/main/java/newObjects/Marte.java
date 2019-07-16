package newObjects;

import java.util.Date;

import it.yboschetto.bot1.objects.Sole;

public class Marte extends SolarObject{

	public Marte(Date now) {
		super(now);
        N =  Sole.rev(49.5574 + 2.11081E-5 * d);
        i = Sole.rev(1.8497 - 1.78E-8 * d);
        w = Sole.rev(286.5016 + 2.92961E-5 * d);
        a = 1.523678731494108E+00;
        e = Sole.rev(0.093405 + 2.516E-9 * d);
        M = Sole.rev(18.6021 + 0.5240207766 * d);
	}

}
