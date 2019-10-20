package it.yboschetto.bot1.objects;




public class Mercury extends SolarObject{


    public Mercury() {
        super();
        N = Utility.rev(48.3313 + 3.24587E-5 * JD);
        i = Utility.rev(7.0047 + 5.00E-8 * JD);
        w = Utility.rev(29.1241 + 1.01444E-5 * JD);
        a = 0.387098;
        e = Utility.rev(0.205635 + 5.59E-10 * JD);
        M = Utility.rev(168.6562 + 4.0923344368 * JD);

        setName("Mercury");
    }

}
