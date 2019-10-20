package it.yboschetto.bot1.objects;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static it.yboschetto.bot1.objects.Utility.*;

public class Sun extends SolarObject{

	private double RA, DEC, L, hour, oblecl, SIDTIME, xequat, yequat, zequat, JD,y2,z2;
    public Sun(){
        super(0);
        Utility.calculateJulianDate(Calendar.getInstance());
        JD = Utility.getD();
        oblecl = Utility.getEcl();
        setName("Sole");

    }
    public double[] calcolatePoistion() {

        Utility.calculateJulianDate(Calendar.getInstance());
        double w, a, e, M, E;


        w = 282.9404 + 4.70935E-5 * JD;
        a = 1.0000000;
        e = 0.016709 - 1.151E-9 * JD;
        M = rev(356.0470 + 0.9856002585 * JD);

        L = rev(w + M);

        E = M + DEG * e * Math.sin(M * RAD) * (1 + e * Math.cos(M * RAD));

        double x, y;

        x = Math.cos(E * RAD) - e;
        y = Math.sin(E * RAD) * Math.sqrt(1 - e * e);

        double r, v;

        r = Math.sqrt(x * x + y * y);
        v = Math.atan2(y, x) * DEG;

        double lon;

        lon = rev(v + w);

        double x2;

        x2 = r * Math.cos(lon * RAD);
        y2 = r * Math.sin(lon * RAD);
        z2 = 0.0;


        xequat = x2;
        yequat = y2 * Math.cos(oblecl * RAD);
        zequat = y2 * Math.sin(oblecl * RAD);


        RA = Math.atan2(yequat, xequat) * DEG;
        DEC = Math.atan2(zequat, Math.sqrt(xequat * xequat + yequat * yequat)) * DEG;

        RA = Utility.rev24(RA / 15);

        double[] coordinates = {RA, DEC};

        return coordinates;
    }

    public double[] calcolateNoonPoistion() {


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,12);
        calendar.set(Calendar.MINUTE,0);
        Utility.calculateJulianDate(calendar);

        double noonJD;
        noonJD = Utility.getD();
        oblecl = Utility.getEcl();

        double w, a, e, M, E;


        w = 282.9404 + 4.70935E-5 * noonJD;
        a = 1.0000000;
        e = 0.016709 - 1.151E-9 * noonJD;
        M = rev(356.0470 + 0.9856002585 * noonJD);

        L = rev(w + M);

        E = M + DEG * e * Math.sin(M * RAD) * (1 + e * Math.cos(M * RAD));

        double x, y;

        x = Math.cos(E * RAD) - e;
        y = Math.sin(E * RAD) * Math.sqrt(1 - e * e);

        double r, v;

        r = Math.sqrt(x * x + y * y);
        v = Math.atan2(y, x) * DEG;

        double lon;

        lon = rev(v + w);

        double x2;

        x2 = r * Math.cos(lon * RAD);
        y2 = r * Math.sin(lon * RAD);
        z2 = 0.0;


        xequat = x2;
        yequat = y2 * Math.cos(oblecl * RAD);
        zequat = y2 * Math.sin(oblecl * RAD);

        double noonRA,noonDEC;
        noonRA = Math.atan2(yequat, xequat) * DEG;
        noonDEC = Math.atan2(zequat, Math.sqrt(xequat * xequat + yequat * yequat)) * DEG;

       // noonRA = Utility.rev24(noonRA / 15);

        double [] coordinates = {noonRA,noonDEC};

        return coordinates;
    }

    @Override
    public double[] calculateAzimuthalPoistion(double lat, double longitude) {

        this.longitude=longitude;
        this.latitude= lat;
        calcolatePoistion();
        double GMST0, UT, UTC = 2;

        GMST0 = (L + 180) / 15;
        UT = rev24(Utility.getHour() - UTC);
        SIDTIME = rev24(GMST0 + UT + longitude / 15);

        double HA;

        HA = (SIDTIME - RA) * 15;

        double x, y, z;

        x = Math.cos(HA * RAD) * Math.cos(DEC * RAD);
        y = Math.sin(HA * RAD) * Math.cos(DEC * RAD);
        z = Math.sin(DEC * RAD);

        double xhor, yhor, zhor;

        xhor = x * Math.sin(lat * RAD) - z * Math.cos(lat * RAD);
        yhor = y;
        zhor = x * Math.cos(lat * RAD) + z * Math.sin(lat * RAD);




        azimuth = rev(Math.atan2(yhor, xhor) * DEG + 180);
        altitude = Math.asin(zhor) * DEG;

        double[] coordinates = {azimuth, altitude};

        return coordinates;
    }

    /*public static String getStringSunRise() {

        double GMST0 = L + 180;
        double LST;
    }*/


    public String getStringRA() {
        int h = (int) RA;
        double m = ((RA - h) * 100) * 3 / 5;
        return Utility.addZero(h) + " h " + Utility.addZero(m) + " m 00 s";
    }

    public String getStringDEC() {
        double d, m, s;

        d = DEC;
        m = (d - (int) d) * 60;
        s = (d - (int) d - m / 60) * 3600;

        return Utility.addZero(d) + "° " + Utility.addZero(m) + "' " + Utility.addZero(s) + "\"";
    }

    public double getRA() {
        return RA;
    }

    public double getDEC() {
        return DEC;
    }

    public double getSIDTIME() {
        return SIDTIME;
    }

    public double getXequat() {
        return xequat;
    }

    public double getYequat() {
        return y2;
    }

    public double getZequat() {
        return z2;
    }
    public double getL() {
        return L;
    }

    @Override
    public String getAlba(Sun sun) {

        //sun.calcolatePoistion();
        double GMST0,LST,UTt, LHA, sinH, h;

        h = -0.833;
        double[] noon = calcolateNoonPoistion();

        GMST0 = sun.getL() + 180;

        double UT_Sole_al_sud = Utility.rev24((noon[0] - GMST0 - longitude) / 15);

        double cosLHA = (Math.sin(h * Utility.RAD) - Math.sin(latitude * Utility.RAD) * Math.sin(noon[1] * Utility.RAD)) / (Math.cos(latitude * Utility.RAD) * Math.cos(noon[1] * Utility.RAD));

        LHA = (Math.acos(cosLHA) * DEG) / 15;
        double hAlba = UT_Sole_al_sud - LHA + Utility.GMT;
        return Utility.format(hAlba);
    }

    @Override
    public String getTramonto(Sun sun) {


        //sun.calcolatePoistion();
        double GMST0,LST,UTt, LHA, sinH, h;

        h = -0.833;
        double[] noon = calcolateNoonPoistion();

        GMST0 = sun.getL() + 180;

        double UT_Sole_al_sud = Utility.rev24((noon[0] - GMST0 - longitude) / 15);

        double cosLHA = (Math.sin(h * Utility.RAD) - Math.sin(latitude * Utility.RAD) * Math.sin(noon[1] * Utility.RAD)) / (Math.cos(latitude * Utility.RAD) * Math.cos(noon[1] * Utility.RAD));

        LHA = (Math.acos(cosLHA) * DEG) / 15;
        double hAlba = UT_Sole_al_sud + LHA + Utility.GMT;
        return Utility.format(hAlba);
    }

}
