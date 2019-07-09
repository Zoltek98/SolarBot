package it.yboschetto.bot1.objects;

import java.util.Date;

public class GenericObject {

    String name;
    String alba;
    String tramonto;
    String direzione;
    Date now;
    int id;
    double azimuth;

    public GenericObject(String name, String alba, String tramonto,int id,double azimuth) {
        this.name = name;
        this.alba = alba;
        this.tramonto = tramonto;
        now=new Date();
        this.id=id;
        this.azimuth=azimuth;
    }

    public String getName() {
        return name;
    }

    public String getAlba() {
        return alba;
    }

    public String getTramonto() {
        return tramonto;
    }

    public int getId() {
        return id;
    }

    public int getProgress(){

        int progress=0;
        int hAlba,hTramonto,hNow=0;
        hTramonto=Integer.parseInt(alba.substring(0,2));
        hAlba=Integer.parseInt(tramonto.substring(0,2));
        int durataTransito=0;
        if(hAlba>8 && (hTramonto>=0 && hTramonto<8)) {
            durataTransito=(24-hAlba)+hTramonto;
            if(now.getHours()>8) {//siamo nel giorno prima,vicini all'alba del pianeta
                hNow= now.getHours()-hAlba;
            }
            else{//post mezzanotte prima dell'alba fel sole
                hNow=(24-hAlba)+now.getHours();
            }
        }
        else{
           durataTransito = hTramonto - hAlba;
           hNow=now.getHours()-hAlba+2;
        }


       progress=(hNow*100/durataTransito);

        return progress;
    }

    public String getDirezione(){
        return Direzione.getDirezione(azimuth);
    }
}
