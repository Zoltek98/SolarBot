package it.yboschetto.bot1.objects;

import javax.rmi.CORBA.Util;

import static it.yboschetto.bot1.objects.Utility.DEG;
import static it.yboschetto.bot1.objects.Utility.RAD;

public class Jupiter extends SolarObject {

    double longio, latgio, sup1, sup2, sup3, sup4, sup5, sup6, sup7, xggio, yggio, zggio;

    public Jupiter() {

        N = Utility.rev(100.4542 + 2.76854E-5 * JD);
        i = Utility.rev(1.3030 - 1.557E-7 * JD);
        w = Utility.rev(273.8777 + 1.64505E-5 * JD);
        a = 5.20256;
        e = Utility.rev(0.048498 + 4.469E-9 * JD);
        M = Utility.rev(19.8950 + 0.0830853001 * JD);
    }

    @Override
    public double[] calculatePosition() {
        E0 = M + (DEG) * e * Math.sin(M * RAD) * (1 - e * Math.cos(M * RAD));
        E1 = E0 - (E0 - (DEG) * e * Math.sin(E0 * RAD) - M) / (1 - e * Math.cos(E0 * RAD));
        while (E0 - E1 > 0.005) {
            E0 = E1;
            E1 = E0 - (E0 - (DEG) * e * Math.sin(E0 * RAD) - M) / (1 - e * Math.cos(E0 * RAD));
        }
        x = a * (Math.cos(E1 * RAD) - e);
        y = a * Math.sqrt(1 - e * e) * Math.sin(E1 * RAD);
        r = Math.sqrt(x * x + y * y);
        v = Math.atan2(y, x) * DEG;
        Sun sun = new Sun();
        sun.calcolatePoistion();
        //sole.Posizione(latitudine, longitudine);
        xeclip = r * (Math.cos(N * RAD) * Math.cos((v + w) * RAD) - Math.sin(N * RAD) * Math.sin((v + w) * RAD) * Math.cos(i * RAD));
        yeclip = r * (Math.sin(N * RAD) * Math.cos((v + w) * RAD) + Math.cos(N * RAD) * Math.sin((v + w) * RAD) * Math.cos(i * RAD));
        zeclip = r * Math.sin((v + w) * RAD) * Math.sin(i * RAD);
        longio = Math.atan2(yeclip, xeclip) * DEG;
        latgio = Math.atan2(zeclip, Math.sqrt(xeclip * xeclip + yeclip * yeclip)) * DEG;
        latgio = Utility.rev(latgio);
        Saturn saturn = new Saturn();
        double Ms = saturn.getM();
        sup1 = -0.332 * Math.sin((2 * M - 5 * Ms - 67.6) * RAD);
        sup2 = -0.056 * Math.sin((2 * M - 2 * Ms + 21) * RAD);
        sup3 = +0.042 * Math.sin((3 * M - 5 * Ms + 21) * RAD);
        sup4 = -0.036 * Math.sin((M - 2 * Ms) * RAD);
        sup5 = +0.022 * Math.cos((M - Ms) * RAD);
        sup6 = +0.023 * Math.sin((2 * M - 3 * Ms + 52) * RAD);
        sup7 = -0.016 * Math.sin((M - 5 * Ms - 69) * RAD);
        longio = longio + sup1 + sup2 + sup3 + sup4 + sup5 + sup6 + sup7;

        xggio = sun.getXequat() + xeclip;
        yggio = sun.getYequat() + yeclip;
        zggio = sun.getZequat() + zeclip;

        double xequat,yequat,zequat;

        xequat = xggio;
        yequat = yggio * Math.cos(oblecl * RAD) - zggio * Math.sin(oblecl * RAD);
        zequat = yggio * Math.sin(oblecl * RAD) + zggio * Math.cos(oblecl * RAD);


        RA = Math.atan2(yequat, xequat) * DEG;
        DEC = Math.atan2(zequat, Math.sqrt(xequat * xequat + yequat * yequat)) * DEG;


        RA = Utility.rev24(RA/15);

        double[] coordinates = {RA, DEC};

        return coordinates;
    }

}
