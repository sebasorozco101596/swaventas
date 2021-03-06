package swasolutions.com.wdpos.logica;

import android.annotation.SuppressLint;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.clientes.ClientesActivity;
import swasolutions.com.wdpos.actividades.facturas.CierreCajaActivity;
import swasolutions.com.wdpos.actividades.paneles.PanelConfiguracionActivity;
import swasolutions.com.wdpos.actividades.paneles.PanelEliminacionActivity;
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
import swasolutions.com.wdpos.base_de_datos.CategoriasBD;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.ClientesCompletoBD;
import swasolutions.com.wdpos.base_de_datos.CreditoBD;
import swasolutions.com.wdpos.base_de_datos.DeudasBD;
import swasolutions.com.wdpos.base_de_datos.DevolucionesBD;
import swasolutions.com.wdpos.base_de_datos.GastosBD;
import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.base_de_datos.PedidosBD;
import swasolutions.com.wdpos.base_de_datos.PreciosGrupoBD;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosVentaBD;
import swasolutions.com.wdpos.base_de_datos.UnidadesBD;
import swasolutions.com.wdpos.base_de_datos.VentasBD;
import swasolutions.com.wdpos.base_de_datos.WarehouseBD;
import swasolutions.com.wdpos.vo.clases_objeto.Cliente;
import swasolutions.com.wdpos.vo.server.MySingleton;

/**
 * Created by sebas on 29/12/2017.
 */

public class Logica {

    public static String centrarCadena(String cadena) {


        String espaciosInicio="";
        String espaciosFinal="";
        String cadenaFinal;

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

        String linea1= cadena;
        String linea2="";
        String resultado;

        if(cadena.length()>32){

            for(int j=32;j>1;j--){

                if(linea1.charAt(j)==' '){
                    linea1= cadena.substring(0,j);
                    linea2= cadena.substring(j, cadena.length());
                    break;
                }
            }
            String BREAK = "\r\n";
            resultado= centrarCadena(linea1)+ BREAK +centrarCadena(linea2);

            return resultado;

        }else{

            resultado= centrarCadena(cadena);

            return resultado;

        }
    }

    public String contarEspaciosInicio(String cadena,int inicio) {


        String espacios="";
        if(cadena.length()<inicio){
            int falta= (cadena.length()-inicio)*(-1);
            for(int i=0;i<falta;i++){
                espacios+=" ";
            }
        }

        return espacios;

    }

    public void verificarContrasenia(final Context context, final String ID, final String tipo,
                                     final String NICKNAME){


        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        @SuppressLint("InflateParams") View mView =  LayoutInflater.from(context).inflate(R.layout.dialog_contrasenia,null);
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
                        CategoriasBD bdCategorias= new CategoriasBD(context,null,1);
                        ClientesBD bdClientes= new ClientesBD(context,null,1);
                        ClientesCompletoBD bdClientesCompleto= new ClientesCompletoBD(context,null,1);
                        CreditoBD bdCredito= new CreditoBD(context,null,1);
                        DeudasBD bdDeudas= new DeudasBD(context,null,1);
                        DevolucionesBD bdDevoluciones= new DevolucionesBD(context,null,1);
                        GruposVendedorBD bdGruposVendedor = new GruposVendedorBD(context,null,1);
                        GastosBD bdGastos= new GastosBD(context,null,1);
                        PedidosBD bdPedidos= new PedidosBD(context,null,1);
                        ProductosBD bdProductos= new ProductosBD(context,null,1);
                        PreciosGrupoBD bdPreciosGrupo= new PreciosGrupoBD(context,null,1);
                        VentasBD bdVentas= new VentasBD(context,null,1);
                        ProductosVentaBD bdProductosVenta= new ProductosVentaBD(context,null,1);
                        WarehouseBD bdWarehouses= new WarehouseBD(context,null,1);
                        UnidadesBD bdUnidades= new UnidadesBD(context,null,1);

                        bdAbonos.eliminarAbonos();
                        bdCarrito.eliminarProductosCarrito();
                        bdClientes.eliminarTodosClientes();
                        bdClientesCompleto.eliminarClientes();
                        bdCategorias.eliminarCategorias();
                        bdCredito.eliminarCreditos();
                        bdDeudas.eliminarTodasLasDeudas();
                        bdDevoluciones.eliminarDevoluciones();
                        bdGruposVendedor.eliminarGruposVendedor();
                        bdGastos.eliminarGastos();
                        bdPedidos.eliminarPedidos();
                        bdProductos.eliminarTodosProductos();
                        bdPreciosGrupo.eliminarPreciosGrupo();
                        bdVentas.eliminarVentas();
                        bdProductosVenta.eliminarProductosVenta();
                        bdWarehouses.eliminarWarehouses();
                        bdUnidades.eliminarUnidades();

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
                        ClientesBD bdClientes= new ClientesBD(context,null,1);
                        bdClientesCompleto.eliminarClientes();
                        bdClientesCompleto.close();

                        bdClientes.eliminarTodosClientes();
                        bdClientes.close();

                        Toast.makeText(context,"Clientes eliminados correctamente",
                                Toast.LENGTH_SHORT).show();
                    }


                    alertDialog.dismiss();

                }
            }
        });

        alertDialog.show();

    }


    public void verificarCodigoSecreto(final Context context, final String link, final String tipo){


        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        @SuppressLint("InflateParams") View mView =  LayoutInflater.from(context).inflate(R.layout.dialog_contrasenia,null);
        final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
        Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
        builder.setView(mView);
        final AlertDialog alertDialog= builder.create();
        btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtContrasenia.getText().length() <=0){
                    txtContrasenia.setError("Campo vacio");
                }else{

                    verificarContraseniaSecreta(context,link,txtContrasenia.getText().toString(),
                            getMacAddr(),tipo);

                    alertDialog.dismiss();

                }
            }
        });

        alertDialog.show();

    }

    private void verificarContraseniaSecreta(final Context context, final String link, final String secret_id,
                                             final String macAddr, final String tipo) {

        String url= "http://wds.grupowebdo.com/app_movil/macaddress/verificarCodigoSecreto.php";
        StringRequest requestLogin = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success user")) {

                        if("btnEliminar".equals(tipo)){
                            Intent intent= new Intent(context,PanelEliminacionActivity.class);
                            context.startActivity(intent);
                        }else if("btnAjustes".equals(tipo)){
                            Intent intent = new Intent(context,ConfiguracionActivity.class);
                            context.startActivity(intent);
                        }

                    } else {

                        Toast.makeText(context, "Error: " + jsonObject.getString("error") +
                                " solicite su codigo secreto con el administrador", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("link", link);
                hashMap.put("mac_address",macAddr.toLowerCase());
                hashMap.put("secret_id",secret_id);

                return hashMap;
            }
        };

        MySingleton.getInstance(context).addToRequestQue(requestLogin);


    }

    /**
     * Metodo que verifica si la cadena que esta entrando solo admite numeros
     * @param name nombre que se verificara si tiene numeros
     * @return boolean
     */
    public static boolean soloNumeros(String name) {
        Pattern patron = Pattern.compile("^[0-9]+$");
        return patron.matcher(name).matches() && name.length() < 25;
    }

    public static boolean isConnectedWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isConnectedMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public boolean verificarConexion(Context context) {
        return isConnectedWifi(context) || isConnectedMobile(context);
    }

    public ArrayList<Cliente> filtrarClientes(ArrayList<Cliente> clientes,Context context) {

        ArrayList<Cliente> clientesNew= new ArrayList<>();

        if(SharedPreferences.getPreferenciaTodosGrupos(context).equals("no")){

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

    public String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString().toLowerCase();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }
}
