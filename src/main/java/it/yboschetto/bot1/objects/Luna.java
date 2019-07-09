package it.yboschetto.bot1.objects; /**
 * @author bosyu
 */

import java.util.Date;

public class Luna {
    private double N, i, w, a, e, M, L, E0, E1, x, y, r, v, ecl, lon, x2, y2, z, d, sup, ar, decluna, AO_luna, xorr_luna, yorr_luna, zorr_luna, xorr2;
    private double xeclip, yeclip, zeclip, lonluna, latluna, Ll, D, F, lontot, lattot, distanzatot, temposid, azimuth, altitude;
    private double pi = 3.14159265358979323846;
    private Date adesso;
    private double lo1, lo2, lo3, lo4, lo5, lo6, lo7, lo8, lo9, lo10, lo11, lo12;
    private double la1, la2, la3, la4, la5;
    private double d1, d2;
    private double sup1, sup2, sup3, supsup;//variabili di supporto altrimenti senza nome
    private double latitudine, longitudine, LHA, LST, GMST0, UTt, sinH, h, hAlba, UT_Sole_al_sud, cosLHA, hTramonto;

    public Luna(double d, double ecl, Date ora) {
        this.d = d;
        N = 125.1228 - 0.0529538083 * d;
        i = 5.1454;
        w = 318.0634 + 0.1643573223 * d;
        a = 60.2666;
        e = 0.054900;
        M = 115.3654 + 13.0649929509 * d;
        N = rev(N);
        w = rev(w);
        M = rev(M);
        this.ecl = ecl;
        adesso = ora;
    }

    public void Posizione(double latitudine, double longitudine) {
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        E0 = M + (180 / pi) * e * Math.sin(M * pi / 180) * (1 - e * Math.cos(M * pi / 180));
        E1 = E0 - (E0 - (180 / pi) * e * Math.sin(E0 * pi / 180) - M) / (1 - e * Math.cos(E0 * pi / 180));
        while (E0 - E1 > 0.005) {
            E0 = E1;
            E1 = E0 - (E0 - (180 / pi) * e * Math.sin(E0 * pi / 180) - M) / (1 - e * Math.cos(E0 * pi / 180));
        }
        x = a * (Math.cos(E1 * pi / 180) - e);
        y = a * Math.sqrt(1 - e * e) * Math.sin(E1 * pi / 180);
        r = Math.sqrt(x * x + y * y);
        v = Math.atan2(y, x) * 180 / pi;
        xeclip = r * (Math.cos(N * pi / 180) * Math.cos((v + w) * pi / 180) - Math.sin(N * pi / 180) * Math.sin((v + w) * pi / 180) * Math.cos(i * pi / 180));
        yeclip = r * (Math.sin(N * pi / 180) * Math.cos((v + w) * pi / 180) + Math.cos(N * pi / 180) * Math.sin((v + w) * pi / 180) * Math.cos(i * pi / 180));
        zeclip = r * Math.sin((v + w) * pi / 180) * Math.sin(i * pi / 180);
        lonluna = Math.atan2(yeclip, xeclip) * 180 / pi;
        if (lonluna < 0) {
            lonluna += 360;
        }
        latluna = Math.atan2(zeclip, Math.sqrt(xeclip * xeclip + yeclip * yeclip)) * 180 / pi;

        //Perturbazioni Luna
        Ll = rev(N + w + M);
        Sole s1 = new Sole(d, ecl, adesso);
        s1.Posizione(latitudine, longitudine);
        sup = s1.getL();
        D = rev(Ll - sup);
        F = rev(Ll - N);
        sup = s1.getM();
        //Perturbazioni longitudine
        lo1 = -1.274 * Math.sin((M - 2 * D) * pi / 180);
        lo2 = 0.658 * Math.sin(2 * D * pi / 180);
        lo3 = -0.186 * Math.sin(sup * pi / 180);
        lo4 = -0.059 * Math.sin((2 * M - 2 * D) * pi / 180);
        lo5 = -0.057 * Math.sin((M - 2 * D + sup) * pi / 180);
        lo6 = 0.053 * Math.sin((M + 2 * D) * pi / 180);
        lo7 = 0.046 * Math.sin((2 * D - sup) * pi / 180);
        lo8 = 0.041 * Math.sin((M - sup) * pi / 180);
        lo9 = -0.035 * Math.sin(D * pi / 180);
        lo10 = -0.031 * Math.sin((M + sup) * pi / 180);
        lo11 = -0.015 * Math.sin((2 * F - 2 * D) * pi / 180);
        lo12 = 0.011 * Math.sin((M - 4 * D) * pi / 180);
        //Perturbazioni latitudine
        la1 = -0.173 * Math.sin((F - 2 * D) * pi / 180);
        la2 = -0.055 * Math.sin((M - F - 2 * D) * pi / 180);
        la3 = -0.046 * Math.sin((M + F - 2 * D) * pi / 180);
        la4 = 0.033 * Math.sin((F + 2 * D) * pi / 180);
        la5 = 0.017 * Math.sin((2 * M + F) * pi / 180);
        //Perturbazioni nella distanza
        d1 = -0.58 * Math.cos((M - 2 * D) * pi / 180);
        d2 = -0.46 * Math.cos(2 * D * pi / 180);
        lontot = lo1 + lo2 + lo3 + lo4 + lo5 + lo6 + lo7 + lo8 + lo9 + lo10 + lo11 + lo12;
        lattot = la1 + la2 + la3 + la4 + la5;
        distanzatot = d1 + d2;
        lonluna += lontot;
        latluna += lattot;
        r += distanzatot;
        sup1 = Math.cos(lonluna * pi / 180) * Math.cos(latluna * pi / 180);
        sup2 = Math.sin(lonluna * pi / 180) * Math.cos(latluna * pi / 180);
        sup3 = Math.sin(latluna * pi / 180);
        supsup = sup2;
        sup1 = sup1 * Math.cos(ecl * pi / 180) + sup3 * Math.sin(ecl * pi / 180);
        sup2 = sup2 * Math.cos(ecl * pi / 180) - sup3 * Math.sin(ecl * pi / 180);
        sup3 = supsup * Math.sin(ecl * pi / 180) + sup3 * Math.cos(ecl * pi / 180);
        ar = rev(Math.atan2(sup2, sup1) * 180 / pi);
        decluna = Math.atan2(sup3, Math.sqrt(sup1 * sup1 + sup2 * sup2)) * 180 / pi;
        temposid = s1.getTemposid();
        AO_luna = (temposid - ar / 15) * 15;
        xorr_luna = Math.cos(AO_luna * pi / 180) * Math.cos(decluna * pi / 180);
        yorr_luna = Math.sin(AO_luna * pi / 180) * Math.cos(decluna * pi / 180);
        zorr_luna = Math.sin(decluna * pi / 180);
        xorr2 = xorr_luna;
        //ora uso lat=60° poi cambiare con quella di zane'
        xorr_luna = xorr_luna * Math.sin(latitudine * pi / 180) - zorr_luna * Math.cos(latitudine * pi / 180);
        yorr_luna = yorr_luna;
        zorr_luna = xorr2 * Math.cos(latitudine * pi / 180) + zorr_luna * Math.sin(latitudine * pi / 180);
        azimuth = Math.atan2(yorr_luna, xorr_luna) * 180 / pi + 180;
        altitude = Math.atan2(zorr_luna, Math.sqrt(xorr_luna * xorr_luna + yorr_luna * yorr_luna)) * 180 / pi;
    }

    public static double rev(double n) {
        if (n > 360) {
            while (n > 360) {
                n -= 360;
            }
        }
        if (n < 0) {
            while (n < 0) {
                n += 360;
            }
        }
        return n;
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
        adesso.setMinutes(27);
        GMST0 = L + 180;
        L = M + w;
        UTt = Double.parseDouble((adesso.getHours() - 1) + "." + (adesso.getMinutes() * 5 / 3));
        LST = GMST0 + UTt * 15 + longitudine;
        LHA = LST - ar;
        sinH = Math.sin(latitudine * pi / 180) * Math.sin(decluna * pi / 180) * Math.cos(latitudine * pi / 180) * Math.cos(decluna * pi / 180) * Math.cos(LHA * pi / 180);

        h = Math.atan(sinH * 180 / pi);


        ar = LST;
        //AR=(AR%24);
        UT_Sole_al_sud = (ar - GMST0 - longitudine) / 15;

        cosLHA = (Math.sin(-0.583 * pi / 180) - Math.sin(latitudine * pi / 180) * Math.sin(decluna * pi / 180)) / (Math.cos(latitudine * pi / 180) * Math.cos(decluna * pi / 180));

        LHA = (Math.acos(cosLHA) * 180 / pi) / 15;
        hAlba = UT_Sole_al_sud - LHA + 1;
        formattata = format(hAlba);

        return formattata;

    }

    public String getTramonto() {
        adesso.setHours(12);
        adesso.setMinutes(30);
        String formattata = "";
        GMST0 = L + 180;
        L = M + w;
        UTt = Double.parseDouble((adesso.getHours() - 1) + "." + (adesso.getMinutes() * 5 / 3));
        LST = GMST0 + UTt * 15 + longitudine;
        LHA = LST - ar;
        sinH = Math.sin(latitudine * pi / 180) * Math.sin(decluna * pi / 180) * Math.cos(latitudine * pi / 180) * Math.cos(decluna * pi / 180) * Math.cos(LHA * pi / 180);

        h = Math.atan(sinH * 180 / pi);


        ar = LST;
        //AR=(AR%24);
        UT_Sole_al_sud = (ar - GMST0 - longitudine) / 15;

        cosLHA = (Math.sin(-0.583 * pi / 180) - Math.sin(latitudine * pi / 180) * Math.sin(decluna * pi / 180)) / (Math.cos(latitudine * pi / 180) * Math.cos(decluna * pi / 180));

        LHA = (Math.acos(cosLHA) * 180 / pi) / 15;
        hTramonto = UT_Sole_al_sud + LHA + 1;
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
