package it.yboschetto.bot1.objects;

import javax.rmi.CORBA.Util;
import java.util.Calendar;
import java.util.Date;

import static it.yboschetto.bot1.objects.Utility.*;


public class SolarObject {

	private String name;
    protected double N, i, a, e,w,M, JD,oblecl,RA,DEC;
    protected double E0, E1, x, y, r, v, ecl, lon, x2, y2, z, sup, AO, xorr, yorr, zorr, xorr2,xgmer,ygmer,zgmer;
    protected double xeclip;
    protected double yeclip;
    protected double zeclip;
    private double lonmer;
    private double latmer;
    private double SIDTIME;
    protected double azimuth;
    protected double altitude;
    protected double longitude;
    protected double latitude;
    private Calendar now;
    private Sun sun;

    public SolarObject(){

        sun = new Sun();
        sun.calcolatePoistion();
        this.JD = Utility.getD();
        this.oblecl = Utility.getEcl();
        E1 = 0;
    }
    public SolarObject(int i){

    }

    public double[] calculatePosition(){

        Utility.calculateJulianDate(Calendar.getInstance());
       // E1 = E0 - (E0 - (DEG) * e * Math.sin(E0 * RAD) - M) / (1 - e * Math.cos(E0 * RAD));

        E1 = M + DEG * e * Math.sin(M * RAD) * (1+ e * Math.cos(M * RAD));
        while (E1 - E0 > 0.005)
        {
            E0 = E1;
            E1 = E0 - (E0 - (DEG) * e * Math.sin(E0 * Utility.RAD) - M) / (1 - e * Math.cos(E0 * Utility.RAD));
        }
        x = a * (Math.cos(E1 * RAD) - e);
        y = a * Math.sqrt(1 - e * e) * Math.sin(E1 * RAD);
        r = Math.sqrt(x * x + y * y);
        v = Math.atan2(y, x) * DEG;

        xeclip = r * (Math.cos(N * RAD) * Math.cos((v + w) * RAD) - Math.sin(N * RAD) * Math.sin((v + w) * RAD) * Math.cos(i * RAD));
        yeclip = r * (Math.sin(N * RAD) * Math.cos((v + w) * RAD) + Math.cos(N * RAD) * Math.sin((v + w) * RAD) * Math.cos(i * RAD));
        zeclip = r * Math.sin((v + w) * RAD) * Math.sin(i * RAD);
        lonmer = Math.atan2(yeclip, xeclip) * DEG;
        latmer = Math.atan2(zeclip, Math.sqrt(xeclip * xeclip + yeclip * yeclip)) * DEG;
        latmer = Utility.rev(latmer);
        xgmer = sun.getXequat()+ xeclip;
        ygmer = sun.getYequat() + yeclip;
        zgmer = sun.getZequat() + zeclip;

        double xequat,yequat,zequat;

        xequat = xgmer;
        yequat = ygmer * Math.cos(oblecl * RAD) - zgmer * Math.sin(oblecl * RAD);
        zequat = ygmer * Math.sin(oblecl * RAD) + zgmer * Math.cos(oblecl * RAD);

        RA = Math.atan2(yequat, xequat) * DEG;
        DEC = Math.atan2(zequat, Math.sqrt(xequat * xequat + yequat * yequat)) * DEG;

        RA = Utility.rev24(RA / 15);

        double[] coordinates = {RA, DEC};

        return coordinates;
    }
    
    public double[] calculateAzimuthalPoistion(double lat,double longitude) {

       /* calculatePosition();
    	this.latitude = latitude;
        this.longitude = longitude;
       
        temposid=sun.getSIDTIME();
        AO = (temposid - RA / 15) * 15;
        xorr = Math.cos(AO * RAD) * Math.cos(DEC * RAD);
        yorr = Math.sin(AO * RAD) * Math.cos(DEC * RAD);
        zorr = Math.sin(DEC * RAD);
        xorr2= xorr;
        xorr = xorr * Math.sin(latitude * RAD) - zorr * Math.cos(latitude * RAD);
        yorr = yorr;
        zorr = xorr2 * Math.cos(latitude * RAD) + zorr * Math.sin(latitude * RAD);
        azimuth = Math.atan2(yorr, xorr) * DEG + 180;
        altitude = Math.atan2(zorr, Math.sqrt(xorr * xorr + yorr * yorr)) * DEG;
        */
        this.longitude=longitude;
        this.latitude= lat;
        calculatePosition();
        double GMST0, UT, UTC = 2;

        GMST0 = (sun.getL() + 180) / 15;
        UT = rev24(Utility.getHour() - UTC);
        SIDTIME = rev24(GMST0 + UT + longitude/15 );

        double HA;

        HA = Utility.rev((SIDTIME - RA) * 15);

        double x, y, z;

        x = Math.cos(HA * RAD) * Math.cos(DEC * RAD);
        y = Math.sin(HA * RAD) * Math.cos(DEC * RAD);
        z = Math.sin(DEC * RAD);

        double xhor, yhor, zhor;

        xhor = x * Math.sin(lat * RAD) - z * Math.cos(lat * RAD);
        yhor = y;
        zhor = x * Math.cos(lat * RAD) + z * Math.sin(lat * RAD);



        azimuth = Utility.rev(Math.atan2(yhor, xhor) * DEG + 180);
        altitude = Math.asin(zhor) * DEG;

        double[] coordinates = {azimuth, altitude};
        return coordinates;

    }

    public double calculateNoonPosition(){

        // E1 = E0 - (E0 - (DEG) * e * Math.sin(E0 * RAD) - M) / (1 - e * Math.cos(E0 * RAD));

        E1 = M + DEG * e * Math.sin(M * RAD) * (1+ e * Math.cos(M * RAD));
        while (E1 - E0 > 0.005)
        {
            E0 = E1;
            E1 = E0 - (E0 - (DEG) * e * Math.sin(E0 * Utility.RAD) - M) / (1 - e * Math.cos(E0 * Utility.RAD));
        }
        x = a * (Math.cos(E1 * RAD) - e);
        y = a * Math.sqrt(1 - e * e) * Math.sin(E1 * RAD);
        r = Math.sqrt(x * x + y * y);
        v = Math.atan2(y, x) * DEG;

        xeclip = r * (Math.cos(N * RAD) * Math.cos((v + w) * RAD) - Math.sin(N * RAD) * Math.sin((v + w) * RAD) * Math.cos(i * RAD));
        yeclip = r * (Math.sin(N * RAD) * Math.cos((v + w) * RAD) + Math.cos(N * RAD) * Math.sin((v + w) * RAD) * Math.cos(i * RAD));
        zeclip = r * Math.sin((v + w) * RAD) * Math.sin(i * RAD);
        lonmer = Math.atan2(yeclip, xeclip) * DEG;
        latmer = Math.atan2(zeclip, Math.sqrt(xeclip * xeclip + yeclip * yeclip)) * DEG;
        latmer = Utility.rev(latmer);
        xgmer = sun.getXequat()+ xeclip;
        ygmer = sun.getYequat() + yeclip;
        zgmer = sun.getZequat() + zeclip;

        double xequat,yequat,zequat;

        xequat = xgmer;
        yequat = ygmer * Math.cos(oblecl * RAD) - zgmer * Math.sin(oblecl * RAD);
        zequat = ygmer * Math.sin(oblecl * RAD) + zgmer * Math.cos(oblecl * RAD);

        double noonRA;
        noonRA = Math.atan2(yequat, xequat) * DEG;
       // DEC = Math.atan2(zequat, Math.sqrt(xequat * xequat + yequat * yequat)) * DEG;

       // RA = Utility.rev24(RA / 15);


        return noonRA;
    }

    public Boolean isVisible()
    {
        Boolean vis=true;
        if (altitude<0)
        {
            vis=false;
        }
        return vis;

    }
    
    
	public String getName() {
		return name;
	}
	public String getDirezione() {
		return Utility.getDirezione(azimuth);
	}

	public String getAlba(Sun sun) {

        double GMST0,LST,UTt, LHA, sinH, h;

        h = -0.833;
        double noonRA = calculateNoonPosition();

        GMST0 = sun.getL() + 180;

        double UT_Sole_al_sud = Utility.rev24((noonRA - GMST0 - longitude) / 15);

        double cosLHA = (Math.sin(h * Utility.RAD) - Math.sin(latitude * Utility.RAD) * Math.sin(DEC * Utility.RAD)) / (Math.cos(latitude * Utility.RAD) * Math.cos(DEC * Utility.RAD));

        LHA = (Math.acos(cosLHA) * DEG) / 15;
        double hAlba = UT_Sole_al_sud - LHA + Utility.GMT;
        return Utility.format(Utility.rev24(hAlba));
	}

	public String getTramonto(Sun sun) {

        double GMST0,LST,UTt, LHA, sinH, h;

        h = -0.833;
        double noonRA = calculateNoonPosition();

        GMST0 = sun.getL() + 180;

        double UT_Sole_al_sud = Utility.rev24((noonRA - GMST0 - longitude) / 15);

        double cosLHA = (Math.sin(h * Utility.RAD) - Math.sin(latitude * Utility.RAD) * Math.sin(DEC * Utility.RAD)) / (Math.cos(latitude * Utility.RAD) * Math.cos(DEC * Utility.RAD));

        LHA = (Math.acos(cosLHA) * DEG) / 15;
        double hAlba = UT_Sole_al_sud + LHA + Utility.GMT;
        return Utility.format(Utility.rev24(hAlba));

	}
	public Date getDateAlba(Sun sun1) {
		return Utility.formatDate(getAlba(sun1));
	}
	public Date getDateTramonto(Sun sun1) {
		return Utility.formatDate(getTramonto(sun1));
	}



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

        return Utility.addZero(d) + "° " + Utility.addZero(Math.abs(m)) + "' " + Utility.addZero(Math.abs(s)) + "\"";
    }

    public String getStringAzimuth(){
        double d, m, s;

        d = azimuth;
        m = (d - (int) d) * 60;
        s = (d - (int) d - m / 60) * 3600;

        return Utility.addZero(d) + "° " + Utility.addZero(m) + "' " + Utility.addZero(s) + "\"";

    }
    public String getStringAltitude(){
        double d, m, s;

        d = altitude;
        m = (d - (int) d) * 60;
        s = (d - (int) d - m / 60) * 3600;

        return Utility.addZero(d) + "° " + Utility.addZero(m) + "' " + Utility.addZero(s) + "\"";

    }

	public double getAzimuth() {
		return azimuth;
	}
	public double getAltitude() {
		return altitude;
	}

	public void setName(String name) {
		this.name = name;
	}

    public double getRA() {
        return RA;
    }

    public double getDEC() {
        return DEC;
    }
}

