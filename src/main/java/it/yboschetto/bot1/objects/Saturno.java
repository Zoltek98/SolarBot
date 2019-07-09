package it.yboschetto.bot1.objects;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bosyu
 */
import java.util.Date;

public class Saturno {
      double N, i, w, a, e, M, L, E0, E1, x, y, r, v, ecl, lon, x2, y2, z, d, sup, ar, dec, AO, xorr, yorr, zorr, xorr2, xgsat, ygsat, zgsat;
        double xeclip, yeclip, zeclip, lonsat, latsat, temposid, azimuth, altitude;
        double pi = 3.14159265358979323846;
        Date adesso;
    private double latitudine, longitudine, LHA, LST, GMST0, UTt, sinH, h, hAlba, UT_Sole_al_sud, cosLHA, hTramonto;
        public Saturno(double d, double ecl, Date ora)
        {
            this.ecl = ecl;
            this.d = d;
            adesso = ora;
            N = Sole.rev(113.6634 + 2.38980E-5 * d);
            i = Sole.rev(2.4886 - 1.081E-7 * d);
            w = Sole.rev(339.3939 + 2.97661E-5 * d);
            a = 9.55475;
            e = Sole.rev(0.055546 - 9.499E-9 * d);
            M = Sole.rev(316.9670 + 0.0334442282 * d);
        }
    public void Posizione(double latitudine, double longitudine)
    {

        this.latitudine=latitudine;
        this.longitudine=longitudine;

        E0 = M + (180 / pi) * e * Math.sin(M * pi / 180) * (1 - e * Math.cos(M * pi / 180));
        E1 = E0 - (E0 - (180 / pi) * e * Math.sin(E0 * pi / 180) - M) / (1 - e * Math.cos(E0 * pi / 180));
        while (E0 - E1 > 0.005)
        {
            E0 = E1;
            E1 = E0 - (E0 - (180 / pi) * e * Math.sin(E0 * pi / 180) - M) / (1 - e * Math.cos(E0 * pi / 180));
        }
        x = a * (Math.cos(E1 * pi / 180) - e);
        y = a * Math.sqrt(1 - e * e) * Math.sin(E1 * pi / 180);
        r = Math.sqrt(x * x + y * y);
        v = Math.atan2(y, x) * 180 / pi;
        Sole sole = new Sole(d, ecl, adesso);
        sole.Posizione(latitudine, longitudine);
        xeclip = r * (Math.cos(N * pi / 180) * Math.cos((v + w) * pi / 180) - Math.sin(N * pi / 180) * Math.sin((v + w) * pi / 180) * Math.cos(i * pi / 180));
        yeclip = r * (Math.sin(N * pi / 180) * Math.cos((v + w) * pi / 180) + Math.cos(N * pi / 180) * Math.sin((v + w) * pi / 180) * Math.cos(i * pi / 180));
        zeclip = r * Math.sin((v + w) * pi / 180) * Math.sin(i * pi / 180);
        lonsat= Math.atan2(yeclip, xeclip) * 180 / pi;
        latsat = Math.atan2(zeclip, Math.sqrt(xeclip * xeclip + yeclip * yeclip)) * 180 / pi;
        latsat = Sole.rev(latsat);
        /*Saturno saturno = new Saturno(d, ecl, adesso);
        double Ms = saturno.getM();
        sup1=-0.332 * Math.sin((2*M - 5*Ms - 67.6)*pi/180 );
        sup2=-0.056 * Math.sin((2*M - 2*Ms + 21 )*pi/180);
        sup3=+0.042 * Math.sin((3*M - 5*Ms + 21 )*pi/180);
        sup4=-0.036 * Math.sin((M - 2*Ms)*pi/180);
        sup5=+0.022 * Math.cos((M - Ms)*pi/180);
        sup6=+0.023 * Math.sin((2*M - 3*Ms + 52)*pi/180 );
        sup7 = -0.016 * Math.sin((M - 5 * Ms - 69) * pi / 180);
        longio = longio + sup1 + sup2 + sup3 + sup4 + sup5 + sup6 + sup7;
         */
        xgsat = sole.getXequat() + xeclip;
        ygsat = sole.getYequat() + yeclip;
        zgsat = sole.getZequat() + zeclip;
        ar = Sole.rev(Math.atan2(ygsat, ygsat) * 180 / pi);
        dec = Math.atan2(zgsat, Math.sqrt(xgsat * xgsat + ygsat * ygsat)) * 180 / pi;
        temposid = sole.getTemposid();
        AO = (temposid - ar / 15) * 15;
        xorr = Math.cos(AO * pi / 180) * Math.cos(dec * pi / 180);
        yorr = Math.sin(AO * pi / 180) * Math.cos(dec * pi / 180);
        zorr = Math.sin(dec * pi / 180);
        xorr2 = xorr;
        xorr = xorr * Math.sin(latitudine * pi / 180) - zorr * Math.cos(latitudine * pi / 180);
        yorr = yorr;
        zorr = xorr2 * Math.cos(latitudine * pi / 180) + zorr * Math.sin(latitudine * pi / 180);
        azimuth = Math.atan2(yorr, xorr) * 180 / pi + 180;
        altitude = Math.atan2(zorr, Math.sqrt(xorr * xorr + yorr * yorr)) * 180 / pi;
    }
    public double getM() {
        return M;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public double getAltitude() {
        return altitude;
    }
    public Boolean Visibile()
    {
        Boolean vis=true;
        if (altitude<0)
        {
            vis=false;
        }
        return vis;

    }
    public String getAlba() {
        String formattata = "";

        //Assegno adesso a mezzogiorno per calcolare GMST0 a metà giornata ed avere i calcoli gisuti
        adesso.setHours(12);
        adesso.setMinutes(12);
        GMST0 = L + 180;
        L = M + w;
        UTt = Double.parseDouble((adesso.getHours() - 1) + "." + (adesso.getMinutes() * 5 / 3));
        LST = GMST0 + UTt * 15.04107 + longitudine;
        LHA = LST - ar;
        sinH = Math.sin(latitudine * pi / 180) * Math.sin(dec * pi / 180) * Math.cos(latitudine * pi / 180) * Math.cos(dec * pi / 180) * Math.cos(LHA * pi / 180);

        h = Math.atan(sinH * 180 / pi);


        ar = LST;
        //AR=(AR%24);
        UT_Sole_al_sud = (ar - GMST0 - longitudine) / 15.04107;

        cosLHA = (Math.sin(-0.833 * pi / 180) - Math.sin(latitudine * pi / 180) * Math.sin(dec * pi / 180)) / (Math.cos(latitudine * pi / 180) * Math.cos(dec * pi / 180));

            LHA = (Math.acos(cosLHA) * 180 / pi) / 15.04107;
            hAlba = UT_Sole_al_sud - LHA + 2;
            formattata = format(hAlba);

        return formattata;

    }

    public String getTramonto() {
        adesso.setHours(12);
        adesso.setMinutes(48);
        String formattata = "";
        GMST0 = L + 180;
        L = M + w;
        UTt = Double.parseDouble((adesso.getHours() - 1) + "." + (adesso.getMinutes() * 5 / 3));
        LST = GMST0 + UTt * 15.04107 + longitudine;
        LHA = LST - ar;
        sinH = Math.sin(latitudine * pi / 180) * Math.sin(dec * pi / 180) * Math.cos(latitudine * pi / 180) * Math.cos(dec * pi / 180) * Math.cos(LHA * pi / 180);

        h = Math.atan(sinH * 180 / pi);


        ar = LST;
        //AR=(AR%24);
        UT_Sole_al_sud = (ar - GMST0 - longitudine) / 15.04107;

        cosLHA = (Math.sin(-0.833 * pi / 180) - Math.sin(latitudine * pi / 180) * Math.sin(dec * pi / 180)) / (Math.cos(latitudine * pi / 180) * Math.cos(dec * pi / 180));

            LHA = (Math.acos(cosLHA) * 180 / pi) / 15.04107;
            hTramonto = UT_Sole_al_sud + LHA + 2;
            //hAlba=(hAlba%24);
            formattata = format(hTramonto);



        return formattata;

    }

    public String format(double orario) {

        String orario_formattato = "";
        String hh = String.valueOf(orario).substring(0, String.valueOf(orario).indexOf('.'));
        double mm;
        String sMm = String.valueOf(orario).substring(String.valueOf(orario).indexOf('.') + 1, String.valueOf(orario).indexOf('.') + 3);
        mm = Double.parseDouble(sMm);

        mm = mm * 3 / 5;

        sMm = String.valueOf(mm).substring(0, 2);
        if (sMm.contains(".")) {
            sMm = "0" + sMm.charAt(0);
        }
        if(hh.length()==1){
            hh=0+hh;
        }
        orario_formattato = hh + ":" + sMm;
        return orario_formattato;
    }

}
