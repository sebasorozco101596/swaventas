package swasolutions.com.wdpos.logica;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.clientes.ClientesActivity;
import swasolutions.com.wdpos.actividades.facturas.CierreCajaActivity;
import swasolutions.com.wdpos.actividades.paneles.PanelConfiguracionActivity;
import swasolutions.com.wdpos.actividades.productos.CrearProductoActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.SharedPreferences;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.actividades.vistas.AbonosVistaActivity;
import swasolutions.com.wdpos.actividades.vistas.ClientesNuevosVistaActivity;
import swasolutions.com.wdpos.actividades.vistas.GastosVistaActivity;
import swasolutions.com.wdpos.actividades.vistas.VistaVentasActivity;
import swasolutions.com.wdpos.base_de_datos.AbonosBD;
import swasolutions.com.wdpos.base_de_datos.CarritoBD;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.ClientesCompletoBD;
import swasolutions.com.wdpos.base_de_datos.DeudasBD;
import swasolutions.com.wdpos.base_de_datos.GastosBD;
import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.base_de_datos.PedidosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosVentaBD;
import swasolutions.com.wdpos.base_de_datos.VentasBD;
import swasolutions.com.wdpos.base_de_datos.WarehouseBD;
import swasolutions.com.wdpos.vo.clases_objeto.Cliente;

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

                        GastosBD bdGastos= new GastosBD(context,null,1);
                        AbonosBD bdAbonos= new AbonosBD(context,null,1);
                        VentasBD bdVentas= new VentasBD(context,null,1);

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
                    }else if("eliminarTodo".equals(tipo)){
                        AbonosBD bdAbonos= new AbonosBD(context,null,1);
                        CarritoBD bdCarrito= new CarritoBD(context,null,1);
                        ClientesBD bdClientes= new ClientesBD(context,null,1);
                        ClientesCompletoBD bdClientesCompleto= new ClientesCompletoBD(context,null,1);
                        DeudasBD bdDeudas= new DeudasBD(context,null,1);
                        GruposVendedorBD bdGruposVendedor = new GruposVendedorBD(context,null,1);
                        GastosBD bdGastos= new GastosBD(context,null,1);
                        PedidosBD bdPedidos= new PedidosBD(context,null,1);
                        ProductosBD bdProductos= new ProductosBD(context,null,1);
                        VentasBD bdVentas= new VentasBD(context,null,1);
                        ProductosVentaBD bdProductosVenta= new ProductosVentaBD(context,null,1);
                        WarehouseBD bdWarehouses= new WarehouseBD(context,null,1);

                        bdAbonos.eliminarAbonos();
                        bdCarrito.eliminarProductosCarrito();
                        bdClientes.eliminarTodosClientes();
                        bdClientesCompleto.eliminarClientes();
                        bdDeudas.eliminarTodasLasDeudas();
                        bdGruposVendedor.eliminarGruposVendedor();
                        bdGastos.eliminarGastos();
                        bdPedidos.eliminarPedidos();
                        bdProductos.eliminarTodosProductos();
                        bdVentas.eliminarVentas();
                        bdProductosVenta.eliminarProductosVenta();
                        bdWarehouses.eliminarWarehouses();

                        bdAbonos.close();
                        bdCarrito.close();
                        bdClientes.close();
                        bdClientesCompleto.close();
                        bdDeudas.close();
                        bdGruposVendedor.close();
                        bdGastos.close();
                        bdPedidos.close();
                        bdProductos.close();
                        bdVentas.close();
                        bdProductosVenta.close();
                        bdWarehouses.close();

                        ConfiguracionActivity.eliminarPreferencias(context);
                        LoginActivity.eliminarPreferencias(context);
                        SharedPreferences.eliminarPreferencias(context);


                        Intent intent= new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }else if("eliminarClientesNuevos".equals(tipo)){
                        ClientesCompletoBD bdClientesCompleto= new ClientesCompletoBD(context,null,1);
                        bdClientesCompleto.eliminarClientes();
                        bdClientesCompleto.close();

                        Toast.makeText(context,"Clientes eliminados correctamente",
                                Toast.LENGTH_SHORT).show();
                    }


                    alertDialog.dismiss();

                }
            }
        });

        alertDialog.show();

        return bandera[0];
    }

    /**
     * Metodo que verifica si la cadena que esta entrando solo admite numeros
     * @param name
     * @return
     */
    public static boolean soloNumeros(String name) {
        Pattern patron = Pattern.compile("^[0-9]+$");
        return patron.matcher(name).matches() && name.length() < 25;
    }

    private static boolean isNetDisponible(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }

    private static Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public boolean verificarConexion(Context context) {
        return isNetDisponible(context) && isOnlineNet();
    }

    public static ArrayList<Cliente> filtrarClientes(ArrayList<Cliente> clientes,Context context) {

        ArrayList<Cliente> clientesNew= new ArrayList<>();

        if(SharedPreferences.getPreferenciaTodosGrupos(context).toString().equals("no")){

            int group_1=SharedPreferences.getPreferenciaGrupo1(context);
            int group_2=SharedPreferences.getPreferenciaGrupo2(context);
            int group_3=SharedPreferences.getPreferenciaGrupo3(context);
            int group_4=SharedPreferences.getPreferenciaGrupo4(context);
            int group_5=SharedPreferences.getPreferenciaGrupo5(context);
            int group_6=SharedPreferences.getPreferenciaGrupo6(context);

            for(int i=0;i<clientes.size();i++){
                int group_id= clientes.get(i).getGroupId();
                if(group_id==group_1 || group_id==group_2 || group_id==group_3 || group_id==group_4
                        || group_id==group_5 || group_id==group_6){
                    clientesNew.add(clientes.get(i));
                }
            }

        }else{
            clientesNew= clientes;
        }

        return clientesNew;

    }
}
