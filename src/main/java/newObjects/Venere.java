package newObjects;

import java.util.Date;

import it.yboschetto.bot1.objects.Sole;

public class Venere extends SolarObject{

	public Venere(Date now) {
		super(now);
		N = Sole.rev(76.6799 + 2.46590E-5 * d);
        i = Sole.rev(3.3946 + 2.75E-8 * d);
        w = Sole.rev(54.8910 + 1.38374E-5 * d);
        a = 0.723330;
        e = Sole.rev(0.006773 - 1.302E-9 * d);
        M = Sole.rev(48.0052 + 1.6021302244 * d);
	}

}
