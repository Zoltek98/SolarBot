package it.yboschetto.bot1.objects;

public class Neptune extends SolarObject {

    public Neptune() {
        super();

        N = Utility.rev(131.7806 + 3.0173E-5 * JD);
        i = Utility.rev(1.7700 - 2.55E-7 * JD);
        w = Utility.rev(272.8461 - 6.027E-6 * JD);
        a = Utility.rev(30.05826 + 3.313E-8 * JD);
        e = Utility.rev(0.008606 + 2.15E-9 * JD);
        M = Utility.rev(260.2471 + 0.005995147 * JD);

        setName("Nettuno");

    }
}
