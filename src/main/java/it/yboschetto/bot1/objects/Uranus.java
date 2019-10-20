package it.yboschetto.bot1.objects;

public class Uranus extends SolarObject{
    
    public Uranus() {
        super();
        N =  Utility.rev(74.0005 + 1.3978E-5* JD);
        i =   Utility.rev(0.7733 + 1.9E-8 * JD);
        w =  Utility.rev(96.6612 + 3.0565E-5 * JD);
        a = Utility.rev(19.18171 - 1.55E-8* JD);
        e = Utility.rev(0.047318 + 7.45E-9 * JD);
        M = Utility.rev(142.5905 + 0.011725806 * JD);
        setName("Urano");
    }
}
