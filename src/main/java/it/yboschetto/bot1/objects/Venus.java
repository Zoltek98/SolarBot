package it.yboschetto.bot1.objects;


public class Venus extends SolarObject{

	public Venus(){

	    super();
		N = Utility.rev(76.6799 + 2.46590E-5 * JD);
        i = Utility.rev(3.3946 + 2.75E-8 * JD);
        w = Utility.rev(54.8910 + 1.38374E-5 * JD);
        a = 0.723330;
        e = Utility.rev(0.006773 - 1.302E-9 * JD);
        M = Utility.rev(48.0052 + 1.6021302244 * JD);

        setName("Venere");
	}

}
