package it.yboschetto.bot1.objects;

public class Direzione {

    public static String getDirezione(double azimuth){
        String direzione="";

        if(azimuth>=348.75 || azimuth<11.25){
            direzione="N";
        }
        else if(azimuth>=11.25 && azimuth <33.75){
            direzione="NNE";
        }
        else if(azimuth>=33.75 && azimuth <56.25){
            direzione="NE";
        }
        else if(azimuth>=56.25 && azimuth <78.75){
            direzione="ENE";
        }
        else if(azimuth>=78.75 && azimuth <101.25){
            direzione="E";
        }
        else if(azimuth>=101.25 && azimuth <123.75){
            direzione="ESE";
        }
        else if(azimuth>=123.75 && azimuth <146.25){
            direzione="SE";
        }
        else if(azimuth>=146.25 && azimuth <168.75){
            direzione="SSE";
        }
        else if(azimuth>=168.75 && azimuth <191.25){
            direzione="S";
        }
        else if(azimuth>=191.25 && azimuth <213.75){
            direzione="SSO";
        }
        else if(azimuth>=213.75 && azimuth <236.25){
            direzione="SO";
        }
        else if(azimuth>=236.25 && azimuth <258.75){
            direzione="OSO";
        }
        else if(azimuth>=258.75 && azimuth <281.25){
            direzione="O";
        }
        else if(azimuth>=281.25 && azimuth <303.75){
            direzione="ONO";
        }
        else if(azimuth>=303.75 && azimuth <326.25){
            direzione="NO";
        }
        else if(azimuth>=326.25 && azimuth <348.75){
            direzione="NNO";
        }



        return direzione;
    }
}
