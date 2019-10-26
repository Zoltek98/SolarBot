package it.yboschetto.bot1.objects;

import static it.yboschetto.bot1.objects.Utility.DEG;
import static it.yboschetto.bot1.objects.Utility.RAD;

public class Pluto extends SolarObject {

    private double S,P, lonecl, latecl,r;

    public Pluto(){
        super();

        S = 50.03 + 0.033459652 * JD;
        P  =  238.95  +  0.003968789 * JD;

        lonecl = 238.9508  +  0.00400703 * JD
                - 19.799 * Math.sin(P * RAD)     + 19.848 * Math.cos(P * RAD)
                + 0.897 * Math.sin(2*P * RAD)    - 4.956 * Math.cos(2*P * RAD)
                + 0.610 * Math.sin(3*P * RAD)    + 1.211 * Math.cos(3*P * RAD)
                - 0.341 * Math.sin(4*P * RAD)    - 0.190 * Math.cos(4*P * RAD)
                + 0.128 * Math.sin(5*P * RAD)    - 0.034 * Math.cos(5*P * RAD)
                - 0.038 * Math.sin(6*P * RAD)    + 0.031 * Math.cos(6*P * RAD)
                + 0.020 * Math.sin((S-P) * RAD)    - 0.010 * Math.cos((S-P) * RAD);

        latecl =  -3.9082
                - 5.453 * Math.sin(P * RAD)     - 14.975 * Math.cos(P * RAD)
                + 3.527 * Math.sin(2*P * RAD)    + 1.673 * Math.cos(2*P * RAD)
                - 1.051 * Math.sin(3*P * RAD)    + 0.328 * Math.cos(3*P * RAD)
                + 0.179 * Math.sin(4*P * RAD)    - 0.292 * Math.cos(4*P * RAD)
                + 0.019 * Math.sin(5*P * RAD)    + 0.100 * Math.cos(5*P * RAD)
                - 0.031 * Math.sin(6*P * RAD)    - 0.026 * Math.cos(6*P * RAD)
                + 0.011 * Math.cos((S-P) * RAD);

        r     =  40.72
                + 6.68 * Math.sin(P * RAD)       + 6.90 * Math.cos(P * RAD)
                - 1.18 * Math.sin(2*P * RAD)     - 0.03 * Math.cos(2*P * RAD)
                + 0.15 * Math.sin(3*P * RAD)     - 0.14 * Math.cos(3*P * RAD);
    }

    @Override
    public double[] calculatePosition() {

        //latecl = Utility.rev(latecl);
        xgmer = r * Math.cos(lonecl * RAD) * Math.cos(latecl * RAD);
        ygmer = r * Math.sin(lonecl * RAD) * Math.cos(latecl * RAD);
        zgmer = r * Math.sin(latecl * RAD);

        double xs,ys;

        xs = sun.getR() * Math.cos(sun.getLon() * RAD);
        ys = sun.getR() * Math.sin(sun.getLon() * RAD);

        double xg,yg,zg;

        xg = xgmer + xs;
        yg = ygmer + ys;
        zg = zgmer;

        double xequat,yequat,zequat;

        xequat = xg;
        yequat = yg * Math.cos(oblecl * RAD) - zg * Math.sin(oblecl * RAD);
        zequat = yg * Math.sin(oblecl * RAD) + zg * Math.cos(oblecl * RAD);

        RA = Math.atan2(yequat, xequat) * DEG;
        DEC = Math.atan2(zequat, Math.sqrt(xequat * xequat + yequat * yequat)) * DEG;

        RA = Utility.rev24(RA / 15);

        double[] coordinates = {RA, DEC};

        return coordinates;
    }
}
