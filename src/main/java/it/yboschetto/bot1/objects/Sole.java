package it.yboschetto.bot1.objects;/*
									* To change this license header, choose License Headers in Project Properties.
									* To change this template file, choose Tools | Templates
									* and open the template in the editor.
									*/

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author bosyu
 */

import java.util.Date;

public class Sole {
	private double w, a, e, M, L, E, x, y, r, v, ecl, lon, x2, y2, z, xequat, yequat, zequat, min, sec;
	private double pi = 3.14159265358979323846;
	private double GMST0, tempo, temposid, AO, xorr, xorr2, yorr, zorr, azimuth, altitude, dec, AR;
	private Date adesso;
	private String localUTa_stringa = "", localUTt_string = "";
	private double sup1, N1, N2, sup2, N3, N, lngOra, t_alba, t_tramonto, M2, L2, RA_prova, Lquadrant, RAquadrant,
			sinDec, cosDec, zenith = 90.833333, cosH, H_alba, H_tramonto, Ta, Tt, UTa, UTt, localUTa, localUtt;
	private double longitudine, latitudine, LHA, LST, sinH, h, UT_Sole_al_sud, cosLHA, hAlba, hTramonto;

	public Sole(double d, double ecl, Date ora) {
		this.ecl = ecl;
		adesso = ora;
		w = rev(282.9404 + 4.70935E-5 * d);
		a = 1.000000;// UA
		e = rev(0.016709 - 1.151E-9 * d);
		M = rev(356.0470 + 0.9856002585 * d);
		L = w + M;
		E = M + (180 / pi) * e * Math.sin(M * pi / 180) * (1 + e * Math.cos(M * pi / 180));
		x = Math.cos(E * pi / 180) - e;
		y = Math.sin(E * pi / 180) * Math.sqrt(1 - e * e);
		r = Math.sqrt(x * x + y * y);
		v = Math.atan2(y, x);
		v = v * 180 / pi;
	}

	public void Posizione(double latitudine, double longitudine) {
		this.latitudine = latitudine;
		this.longitudine = longitudine;

		adesso = new Date();
		lon = v + w;
		lon = rev(lon);
		x2 = r * Math.cos(lon * pi / 180);
		y2 = r * Math.sin(lon * pi / 180);
		z = 0;
		xequat = x2;
		yequat = y2 * Math.cos(ecl * pi / 180);
		zequat = y2 * Math.sin(ecl * pi / 180);
		dec = Math.atan2(zequat, Math.sqrt(xequat * xequat + yequat * yequat)) * 180 / pi;
		AR = Math.atan2(yequat, xequat) * 180 / pi;
		GMST0 = L / 15 + 12;
		tempo = adesso.getHours();
		min = adesso.getMinutes();
		min /= 60;
		sec = adesso.getSeconds();
		sec /= 3600;
		tempo = tempo + min + sec;
		temposid = GMST0 + tempo - 1 + longitudine / 15;
		AO = (temposid - AR / 15) * 15;
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

	public String Visibile() {
		String vis = "si";
		if (altitude < 0) {
			vis = "no";
		}
		return vis;

	}

	public double getM() {
		return M;
	}

	public double getL() {
		return L;
	}

	public double getXequat() {
		return xequat;
	}

	public double getYequat() {
		return yequat;
	}

	public double getZequat() {
		return zequat;
	}

	public double getTemposid() {
		return temposid;
	}

	public double getAzimuth() {
		return azimuth;
	}

	public double getAltitude() {
		return altitude;
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

	public String getAlba() {
		String formattata = "";

		// Assegno adesso a mezzogiorno per calcolare GMST0 a metà giornata ed avere i
		// calcoli gisuti
		adesso.setHours(12);
		adesso.setMinutes(29);
		GMST0 = L + 180;
		L = M + w;
		UTt = Double.parseDouble((adesso.getHours() - 2) + "." + (adesso.getMinutes() * 5 / 3));
		LST = GMST0 + UTt * 15 + longitudine;
		LHA = LST - AR;
		sinH = Math.sin(latitudine * pi / 180) * Math.sin(dec * pi / 180) * Math.cos(latitudine * pi / 180)
				* Math.cos(dec * pi / 180) * Math.cos(LHA * pi / 180);

		h = Math.atan(sinH * 180 / pi);

		AR = LST;
		// AR=(AR%24);
		UT_Sole_al_sud = (AR - GMST0 - longitudine) / 15;

		cosLHA = (Math.sin(-0.833 * pi / 180) - Math.sin(latitudine * pi / 180) * Math.sin(dec * pi / 180))
				/ (Math.cos(latitudine * pi / 180) * Math.cos(dec * pi / 180));
		if (true) {
			LHA = (Math.acos(cosLHA) * 180 / pi) / 15;
			hAlba = UT_Sole_al_sud - LHA + 2;
			formattata = format(hAlba);
		}
		return formattata;

	}

	public String getTramonto() {
		adesso.setHours(12);
		adesso.setMinutes(14);
		String formattata = "";
		GMST0 = L + 180;
		L = M + w;
		UTt = Double.parseDouble((adesso.getHours() - 2) + "." + (adesso.getMinutes() * 5 / 3));
		LST = GMST0 + UTt * 15 + longitudine;
		LHA = LST - AR;
		sinH = Math.sin(latitudine * pi / 180) * Math.sin(dec * pi / 180) * Math.cos(latitudine * pi / 180)
				* Math.cos(dec * pi / 180) * Math.cos(LHA * pi / 180);

		h = Math.atan(sinH * 180 / pi);

		AR = LST;
		// AR=(AR%24);
		UT_Sole_al_sud = (AR - GMST0 - longitudine) / 15;

		cosLHA = (Math.sin(-0.833 * pi / 180) - Math.sin(latitudine * pi / 180) * Math.sin(dec * pi / 180))
				/ (Math.cos(latitudine * pi / 180) * Math.cos(dec * pi / 180));

		if (true) {
			LHA = (Math.acos(cosLHA) * 180 / pi) / 15;
			hTramonto = UT_Sole_al_sud + LHA + 4;
			// hAlba=(hAlba%24);
			formattata = format(hTramonto);
		}

		return formattata;

	}

	public String format(double orario) {

		String orario_formattato = "";
		String hh = String.valueOf(orario).substring(0, String.valueOf(orario).indexOf('.'));
		double mm;
		String sMm = String.valueOf(orario).substring(String.valueOf(orario).indexOf('.') + 1,
				String.valueOf(orario).indexOf('.') + 3);
		mm = Double.parseDouble(sMm);

		mm = mm * 3 / 5;

		sMm = String.valueOf(mm).substring(0, 2);
		if (sMm.contains(".")) {
			sMm = "0" + sMm.charAt(0);
		}
		orario_formattato = hh + ":" + sMm;
		return orario_formattato;
	}

	public Date getDateTramonto() {
		String tramonto = getTramonto();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = new Date();

		date.setHours(Integer.parseInt(tramonto.substring(0,2)));
		date.setMinutes(Integer.parseInt(tramonto.substring(0,2)));
	
		return date;

	}

	public Date getDateAlba() {
		String alba = getAlba();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		try {
			date = sdf.parse(alba);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;

	}
}
