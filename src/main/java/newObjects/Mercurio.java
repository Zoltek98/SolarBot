package newObjects;

import java.util.Date;

import it.yboschetto.bot1.objects.Sole;

public class Mercurio extends SolarObject{

	public Mercurio(Date now) {
		super(now);
		N = Sole.rev(48.3313 + 3.24587E-5 * d);
        i = Sole.rev(7.0047 + 5.00E-8 * d);
        w = Sole.rev(29.1241 + 1.01444E-5 * d);
        a = 0.387098;
        e = Sole.rev(0.205635 + 5.59E-10 * d);
        M = Sole.rev(168.6562 + 4.0923344368 * d);		
	}

}
