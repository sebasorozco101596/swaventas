package swasolutions.com.wdpos.logica;

/**
 * Created by sebas on 29/12/2017.
 */

public class Logica {

    public static String BREAK = "\r\n";

    public static String centrarCadena(String cadena) {


        String espaciosInicio="";
        String espaciosFinal="";
        String cadenaFinal="";

        if(cadena.length()<32){

            int falta= ((((cadena.length()-32)*(-1))/2)-1);
            for(int i=0;i<falta;i++){
                espaciosInicio+=" ";
                espaciosFinal+=" ";
            }
            cadenaFinal=espaciosInicio+cadena+espaciosFinal;

        }else{
            cadenaFinal=cadena;
        }

        return cadenaFinal;

    }

    public static String alinearLineas(String cadena){

        String nombre= cadena;
        String linea1= nombre;
        String linea2="";
        String resultado="";

        if(cadena.length()>32){

            for(int j=32;j>1;j--){

                if(linea1.charAt(j)==' '){
                    linea1= nombre.substring(0,j);
                    linea2= nombre.substring(j,nombre.length());
                    break;
                }
            }
            resultado= centrarCadena(linea1)+BREAK+centrarCadena(linea2);

            return resultado;

        }else{

            resultado= centrarCadena(cadena);

            return resultado;

        }
    }

    public static String contarEspacios(String cadena) {


        String espacios="";
        if(cadena.length()<23){

            int falta= (cadena.length()-24)*(-1);
            for(int i=0;i<falta;i++){
                espacios+=" ";
            }

        }

        return espacios;

    }

    public static String contarEspaciosInicio(String cadena,int inicio) {


        String espacios="";
        if(cadena.length()<inicio){
            int falta= (cadena.length()-inicio)*(-1);
            for(int i=0;i<falta;i++){
                espacios+=" ";
            }
        }

        return espacios;

    }
}
