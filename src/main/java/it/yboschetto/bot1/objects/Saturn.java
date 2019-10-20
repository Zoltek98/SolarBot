package it.yboschetto.bot1.objects;

public class Saturn extends SolarObject {
    
    public Saturn() {
        N = Utility.rev(113.6634 + 2.38980E-5 * JD);
        i = Utility.rev(2.4886 - 1.081E-7 * JD);
        w = Utility.rev(339.3939 + 2.97661E-5 * JD);
        a = 9.55475;
        e = Utility.rev(0.055546 - 9.499E-9 * JD);
        M = Utility.rev(316.9670 + 0.0334442282 * JD);
    }

    public double getM(){
        return M;
    }
}
