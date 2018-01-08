package swasolutions.com.wdpos.logica;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.clientes.ClientesActivity;
import swasolutions.com.wdpos.actividades.facturas.CierreCajaActivity;
import swasolutions.com.wdpos.actividades.paneles.PanelConfiguracionActivity;
import swasolutions.com.wdpos.actividades.productos.CrearProductoActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.vistas.AbonosVistaActivity;
import swasolutions.com.wdpos.actividades.vistas.ClientesNuevosVistaActivity;
import swasolutions.com.wdpos.actividades.vistas.GastosVistaActivity;
import swasolutions.com.wdpos.actividades.vistas.VistaVentasActivity;
import swasolutions.com.wdpos.base_de_datos.AbonosBD;
import swasolutions.com.wdpos.base_de_datos.GastosBD;
import swasolutions.com.wdpos.base_de_datos.VentasBD;

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

    public static boolean verificarContrasenia(final Context context, final String ID,final String tipo,
                                               final String NICKNAME){

        final boolean[] bandera = {false};

        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        View mView =  LayoutInflater.from(context).inflate(R.layout.dialog_contrasenia,null);
        final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
        Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
        builder.setView(mView);
        final AlertDialog alertDialog= builder.create();
        btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if(txtContrasenia.getText().length() <=0){
                    txtContrasenia.setError("Campo vacio");
                }else if(Integer.parseInt(txtContrasenia.getText().toString()) !=
                        ConfiguracionActivity.getPreferenciaPing(context)){
                    txtContrasenia.setError("Ping incorrecto");
                } else{

                    if("editarCliente".equals(tipo)){
                        Intent intent= new Intent(context,ClientesActivity.class);
                        intent.putExtra("key_tipo","edicion");
                        intent.putExtra("key_id",ID);
                        context.startActivity(intent);
                    }else if("cierreCaja".equals(tipo)){

                        GastosBD bdGastos= new GastosBD(context,"GastosBD",null,1);
                        AbonosBD bdAbonos= new AbonosBD(context,null,1);
                        VentasBD bdVentas= new VentasBD(context,"VentasBD",null,1);

                        int cantidadProductos= bdVentas.cantidadProductos();
                        int totalVentas= bdVentas.totalVentas();
                        int dineroRecibidoVentas=bdVentas.totalVentasRecibido();
                        int dineroRecibidoAbonos= bdAbonos.totalAbonos();
                        int totalGastos=bdGastos.totalGastos();
                        int dineroEntregar= (dineroRecibidoVentas+dineroRecibidoAbonos)-totalGastos;

                        Intent intent = new Intent(context, CierreCajaActivity.class);
                        intent.putExtra("key_vendedor",NICKNAME);
                        intent.putExtra("key_id",ID);
                        intent.putExtra("key_cantidadProductos",cantidadProductos);
                        intent.putExtra("key_totalVentas",totalVentas);
                        intent.putExtra("key_dineroRecibidoVentas",dineroRecibidoVentas);
                        intent.putExtra("key_dineroRecibidoAbonos",dineroRecibidoAbonos);
                        intent.putExtra("key_totalGastos",totalGastos);
                        intent.putExtra("key_dineroEntregar",dineroEntregar);
                        intent.putExtra("key_ciclo","0");

                        context.startActivity(intent);

                        bdGastos.close();
                        bdAbonos.close();
                        bdVentas.close();

                    }else if("vistaClientes".equals(tipo)){
                        Intent intent= new Intent(context,ClientesNuevosVistaActivity.class);
                        context.startActivity(intent);
                    }else if("devolucion".equals(tipo)){
                        Intent intent= new Intent(context,ClientesActivity.class);
                        intent.putExtra("key_tipo","devolucion");
                        intent.putExtra("key_id",ID);
                        context.startActivity(intent);
                    }else if("crearProducto".equals(tipo)){
                        Intent intentProductos= new Intent(context, CrearProductoActivity.class);
                        context.startActivity(intentProductos);
                    }else if("agregarCredito".equals(tipo)){
                        Intent intent= new Intent(context,ClientesActivity.class);
                        intent.putExtra("key_tipo","credito");
                        intent.putExtra("key_id",ID);
                        context.startActivity(intent);
                    }else if("configuracion".equals(tipo)){
                        Intent intentPanelConfiguracion = new Intent(context,PanelConfiguracionActivity.class);
                        context.startActivity(intentPanelConfiguracion);
                    }else if("vistaVentas".equals(tipo)){
                        Intent intent = new Intent(context, VistaVentasActivity.class);
                        context.startActivity(intent);
                    }else if("vistaAbonos".equals(tipo)){
                        Intent intent = new Intent(context, AbonosVistaActivity.class);
                        context.startActivity(intent);
                    }else if("vistaGastos".equals(tipo)){
                        Intent intent = new Intent(context, GastosVistaActivity.class);
                        context.startActivity(intent);
                    }


                    alertDialog.dismiss();

                }
            }
        });

        alertDialog.show();

        return bandera[0];
    }

    public static boolean soloNumeros(String name) {
        Pattern patron = Pattern.compile("^[0-9]+$");
        return !patron.matcher(name).matches() || name.length() > 25;
    }
}
