package newObjects;

import java.util.Date;

import it.yboschetto.bot1.objects.Sole;


public class SolarObject {

	private String name;
    protected double N, i, a, e,w,M, d;
    private double E0, E1, x, y, r, v, ecl, lon, x2, y2, z, sup, ar, dec, AO, xorr, yorr, zorr, xorr2,xgmer,ygmer,zgmer;
    private double xeclip, yeclip, zeclip, lonmer, latmer, temposid, azimuth, altitude;
    private double GMST0,LHA,L,UTt,LST,sinH,h,UT_Sole_al_sud,cosLHA,hAlba,hTramonto,latitude,longitude;
    private Date now;
    
    public SolarObject(Date now) {
    	
		Utility.calculateJulianDate(now);
		this.d=Utility.getD();
		this.ecl=Utility.getEcl();
		this.now=now;
    }
    
    public void calculatePoistion(double latitude,double longitude) {
    	this.latitude = latitude;
        this.longitude = longitude;
        E0 = M + (180 / Utility.PI) * e * Math.sin(M * Utility.PI / 180) * (1 - e * Math.cos(M * Utility.PI / 180));
        E1 = E0 - (E0 - (180 / Utility.PI) * e * Math.sin(E0 * Utility.PI / 180) - M) / (1 - e * Math.cos(E0 * Utility.PI / 180));
        while (E0 - E1 > 0.005)
        {
            E0 = E1;
            E1 = E0 - (E0 - (180 / Utility.PI) * e * Math.sin(E0 * Utility.PI / 180) - M) / (1 - e * Math.cos(E0 * Utility.PI / 180));
        }
        x = a * (Math.cos(E1 * Utility.PI / 180) - e);
        y = a * Math.sqrt(1 - e * e) * Math.sin(E1 * Utility.PI / 180);
        r = Math.sqrt(x * x + y * y);
        v = Math.atan2(y, x) * 180 / Utility.PI;
        Sole sole = new Sole(d, ecl, now);
        sole.Posizione(latitude, longitude);
        xeclip = r * (Math.cos(N * Utility.PI / 180) * Math.cos((v + w) * Utility.PI / 180) - Math.sin(N * Utility.PI / 180) * Math.sin((v + w) * Utility.PI / 180) * Math.cos(i * Utility.PI / 180));
        yeclip = r * (Math.sin(N * Utility.PI / 180) * Math.cos((v + w) * Utility.PI / 180) + Math.cos(N * Utility.PI / 180) * Math.sin((v + w) * Utility.PI / 180) * Math.cos(i * Utility.PI / 180));
        zeclip = r * Math.sin((v + w) * Utility.PI / 180) * Math.sin(i * Utility.PI / 180);
        lonmer = Math.atan2(yeclip, xeclip) * 180 / Utility.PI;
        latmer = Math.atan2(zeclip, Math.sqrt(xeclip * xeclip + yeclip * yeclip)) * 180 / Utility.PI;
        latmer = Sole.rev(latmer);
        xgmer = sole.getXequat()+ xeclip;
        ygmer = sole.getYequat() + yeclip;
        zgmer = sole.getZequat() + zeclip;
        ar = Utility.rev(Math.atan2(ygmer, xgmer) * 180 / Utility.PI);
        dec = Math.atan2(zgmer, Math.sqrt(xgmer * xgmer + ygmer * ygmer)) * 180 / Utility.PI;
        temposid=sole.getTemposid();
        AO = (temposid - ar / 15) * 15;
        xorr = Math.cos(AO * Utility.PI / 180) * Math.cos(dec * Utility.PI / 180);
        yorr = Math.sin(AO * Utility.PI / 180) * Math.cos(dec * Utility.PI / 180);
        zorr = Math.sin(dec * Utility.PI / 180);
        xorr2= xorr;
        xorr = xorr * Math.sin(latitude * Utility.PI / 180) - zorr * Math.cos(latitude * Utility.PI / 180);
        yorr = yorr;
        zorr = xorr2 * Math.cos(latitude * Utility.PI / 180) + zorr * Math.sin(latitude * Utility.PI / 180);
        azimuth = Math.atan2(yorr, xorr) * 180 / Utility.PI + 180;
        altitude = Math.atan2(zorr, Math.sqrt(xorr * xorr + yorr * yorr)) * 180 / Utility.PI;
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
	public String getAlba() {
		GMST0 = L + 180;
        L = M + w;
        UTt = Double.parseDouble((int)(now.getHours() - Utility.GMT) + "." + (int)(now.getMinutes() * 5 / 3));
        LST = GMST0 + UTt * 15.04107 + longitude;
        LHA = LST - ar;
        sinH = Math.sin(latitude * Utility.PI / 180) * Math.sin(dec * Utility.PI / 180) * Math.cos(latitude * Utility.PI / 180) * Math.cos(dec * Utility.PI / 180) * Math.cos(LHA * Utility.PI / 180);

        h = Math.atan(sinH * 180 / Utility.PI);


        ar = LST;
        //AR=(AR%24);
        UT_Sole_al_sud = (ar - GMST0 - longitude) / 15.04107;

        cosLHA = (Math.sin(-0.833 * Utility.PI / 180) - Math.sin(latitude * Utility.PI / 180) * Math.sin(dec * Utility.PI / 180)) / (Math.cos(latitude * Utility.PI / 180) * Math.cos(dec * Utility.PI / 180));

        LHA = (Math.acos(cosLHA) * 180 / Utility.PI) / 15.04107;
        hAlba = UT_Sole_al_sud - LHA + Utility.GMT;
		return Utility.format(hAlba);
	}
	public String getTramonto() {
		
		now.setHours(12);
        now.setMinutes(0);
        String formattata = "";
        GMST0 = L + 180;
        L = M + w;
        UTt = Double.parseDouble((int)(now.getHours() - Utility.GMT) + "." + (int)(now.getMinutes() * 5.0 / 3));
        LST = GMST0 + UTt * 15.04107 + longitude;
        LHA = LST - ar;
        sinH = Math.sin(latitude * Utility.PI / 180) * Math.sin(dec * Utility.PI / 180) * Math.cos(latitude * Utility.PI / 180) * Math.cos(dec * Utility.PI / 180) * Math.cos(LHA * Utility.PI / 180);

        h = Math.atan(sinH * 180 / Utility.PI);


        ar = LST;
        //AR=(AR%24);
        UT_Sole_al_sud = (ar - GMST0 - longitude) / 15.04107;

        cosLHA = (Math.sin(-0.833 * Utility.PI / 180) - Math.sin(latitude * Utility.PI / 180) * Math.sin(dec * Utility.PI / 180)) / (Math.cos(latitude * Utility.PI / 180) * Math.cos(dec * Utility.PI / 180));

        LHA = (Math.acos(cosLHA) * 180 / Utility.PI) / 15.04107;
        hTramonto = UT_Sole_al_sud + LHA + Utility.GMT;
        
        return Utility.format(hTramonto);
	}
	public double getAzimuth() {
		return azimuth;
	}
	public double getAltitude() {
		return altitude;
	}
	public Date getDateAlba() {
		return Utility.formatDate(getAlba());
	}
	public Date getDateTramonto() {
		return Utility.formatDate(getTramonto());
	}
	public void setName(String name) {
		this.name = name;
	}
    
	
}
