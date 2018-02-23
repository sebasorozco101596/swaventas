package swasolutions.com.wdpos.vo.server;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import swasolutions.com.wdpos.base_de_datos.AbonosBD;
import swasolutions.com.wdpos.base_de_datos.DevolucionesBD;
import swasolutions.com.wdpos.base_de_datos.GastosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosVentaBD;
import swasolutions.com.wdpos.base_de_datos.VentasBD;
import swasolutions.com.wdpos.vo.clases_objeto.Abono;
import swasolutions.com.wdpos.vo.clases_objeto.Devolucion;
import swasolutions.com.wdpos.vo.clases_objeto.Gasto;
import swasolutions.com.wdpos.vo.clases_objeto.ProductoVenta;
import swasolutions.com.wdpos.vo.clases_objeto.Venta;

/**
 * Created by sebas on 9/07/2017.
 */

public class SubirServidor implements Runnable {

    private ArrayList<Abono> abonos;
    private ArrayList<Venta> ventas;
    private ArrayList<Devolucion> devoluciones;
    private ArrayList<Gasto> gastos;

    private GastosBD bdGastos;
    private AbonosBD bdAbonos;
    private ProductosVentaBD bdProductosVenta;
    private VentasBD bdVentas;
    private DevolucionesBD bdDevoluciones;
    private ProductosBD bdProductos;

    private Context context;
    private int tipo;

    private String id;
    private int warehouse_id;

    private String URLAbonos;
    private String URLVentas;
    private String URLGastos;
    private String URLDevoluciones;



    public SubirServidor(ArrayList<Abono> abonos, ArrayList<Venta> ventas,
                         ArrayList<Gasto> gastos,ArrayList<Devolucion> devoluciones,
                         Context context, int tipo, String link, String id, int warehouse_id){
        this.abonos=abonos;
        this.ventas=ventas;
        this.gastos=gastos;
        this.devoluciones=devoluciones;
        this.context= context;
        this.tipo=tipo;
        this.id=id;
        this.warehouse_id=warehouse_id;
        URLAbonos = link+"/app_movil/vendedor/actualizarDeuda.php";
        URLVentas = link+"/app_movil/vendedor/subirVenta.php";
        URLGastos = link+"/app_movil/vendedor/subirGastos.php";
        URLDevoluciones = link+"/app_movil/vendedor/subirDevoluciones.php";


        /**
         * Instancia de las bases de datos locales
         */

        bdGastos= new GastosBD(context,null,1);
        bdAbonos= new AbonosBD(context,null,1);
        bdVentas= new VentasBD(context,null,1);
        bdProductos= new ProductosBD(context,null,1);
        bdDevoluciones= new DevolucionesBD(context,null,1);
        bdProductosVenta= new ProductosVentaBD(context,null,1);


    }



    /**
     * Method running the thread
     */
    @Override
    public void run() {
        Log.d("ERROR",""+tipo);

        if(tipo==1){//ABONOS


            if(abonos.size()>0){

                Log.d("entreeeeeee", "1");

                for(int i = 0; i<abonos.size(); i++){

                    Log.d("entreeeeeee", ""+abonos.get(i).getPagado());
                    final int finalI = i;
                    StringRequest requestAbonos = new StringRequest(Request.Method.POST, URLAbonos, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.names().get(0).equals("success")) {

                                    Log.d("agregoAbono","Se agrego el abono");
                                    bdAbonos.eliminarAbono(abonos.get(finalI).getId());
                                } else {
                                    Toast.makeText(context, "Error" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            //Area where the code should be added for when a message is not received
                            //by a user.

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("estadoPago", abonos.get(finalI).getEstadoVenta());
                            hashMap.put("idVendedor",""+ id);
                            hashMap.put("pagado",""+ abonos.get(finalI).getPagado());
                            hashMap.put("idVenta", ""+abonos.get(finalI).getId());
                            hashMap.put("pagoPayment", ""+abonos.get(finalI).getPagoPayment());
                            Log.d("pagoPayment",""+abonos.get(finalI).getPagoPayment());
                            return hashMap;
                        }
                    };

                    MySingleton.getInstance(context).addToRequestQue(requestAbonos);
                    //requestQueue.add(requestAbonos);

                }

            }

        }else if(tipo==2){//VENTAS


            if(ventas.size()>0){

                Log.d("entreeeeeee", "2");

                JSONObject jsonObjectVenta;
                JSONObject jsonObjectVentas;
                final JSONArray jsonArrayVentas= new JSONArray();
                for(int i = 0; i<ventas.size(); i++){

                    final JSONArray jsonArray= new JSONArray();

                    Log.d("entreee", "producto: "+ ventas.get(i).getId());
                    //Toast.makeText(context,""+ventas.get(i).getVenta().getIdCliente(),Toast.LENGTH_SHORT).show();

                    final ArrayList<ProductoVenta> productosVenta= bdProductosVenta.productosVenta(ventas.get(i).getId());

                    for(int j = 0; j<productosVenta.size(); j++) {

                        final int finalI = j;

                        HashMap<String, String> hashMapProductos = new HashMap<String, String>();
                        hashMapProductos.put("idVenta", "1");
                        hashMapProductos.put("idProducto", "" + productosVenta.get(finalI).getIdProducto());
                        hashMapProductos.put("codigoProducto", productosVenta.get(finalI).getCodigoProducto());
                        hashMapProductos.put("nombreProducto", productosVenta.get(finalI).getNombre());
                        hashMapProductos.put("precio", "" + productosVenta.get(finalI).getPrecioUnitario());
                        hashMapProductos.put("cantidad", "" + productosVenta.get(finalI).getCantidad());
                        hashMapProductos.put("subtotal", "" + (productosVenta.get(finalI).getCantidad() *
                                productosVenta.get(finalI).getPrecioUnitario()));

                        JSONObject jsonObjectProductos= new JSONObject(hashMapProductos);

                        jsonArray.put(jsonObjectProductos);
                    }

                    HashMap<String, String> hashMapVenta = new HashMap<>();
                    hashMapVenta.put("id", ""+ ventas.get(i).getIdVendedor()*10000+ventas.get(i).getId());
                    hashMapVenta.put("fecha",""+ ventas.get(i).getFecha());
                    hashMapVenta.put("referencia",ventas.get(i).getReferencia()+"v2.1.3");
                    hashMapVenta.put("cedulaCliente", ""+ventas.get(i).getCedulaCliente());
                    hashMapVenta.put("nombreCliente", ventas.get(i).getCliente());
                    hashMapVenta.put("total", ""+ventas.get(i).getTotal());
                    hashMapVenta.put("estadoVenta", ventas.get(i).getEstadoVenta());
                    hashMapVenta.put("idVendedor", ""+ventas.get(i).getIdVendedor());
                    hashMapVenta.put("cantidadProductos", ""+ventas.get(i).getCantidadProductos());
                    hashMapVenta.put("pagado", ""+ventas.get(i).getPagadoPorCliente());
                    hashMapVenta.put("idCliente",""+ventas.get(i).getIdCliente());
                    hashMapVenta.put("existe",""+ventas.get(i).getExiste());
                    hashMapVenta.put("warehouse_id",""+warehouse_id);
                    hashMapVenta.put("nota",ventas.get(i).getNota());
                    hashMapVenta.put("ventalocal_id",""+ventas.get(i).getId());

                    jsonObjectVenta= new JSONObject(hashMapVenta);
                    try {
                        jsonObjectVenta.put("productos",jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArrayVentas.put(jsonObjectVenta);
                }

                jsonObjectVentas= new JSONObject();
                try {
                    jsonObjectVentas.put("ventas",jsonArrayVentas);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Ventasssssssss",jsonObjectVentas.toString());


                JsonObjectRequest requestVentas = new JsonObjectRequest(Request.Method.POST,URLVentas,jsonObjectVentas, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.names().get(0).equals("success")) {

                                String ventas = response.getString("success");
                                ventas= ventas.substring(0,ventas.length()-1);

                                ArrayList<String> userList = new ArrayList<>(Arrays.asList(ventas.split(",")));

                                //Toast.makeText(context,userList.toString(),Toast.LENGTH_SHORT).show();

                                for(int i=0;i<userList.size();i++){
                                    bdVentas.eliminarVenta(Integer.parseInt(userList.get(i)));
                                }

                                bdProductosVenta.eliminarProductosVenta();

                            } else {
                                Toast.makeText(context, "Error" + response.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("errorVenta",error.toString());
                    }
                });
                MySingleton.getInstance(context).addToRequestQue(requestVentas);


            }

        }else if(tipo==3) {//GASTOS

            if(gastos.size()>0) {
                Log.d("entreeeeeee", "3");

                for (int i = 0; i < gastos.size(); i++) {

                    Log.d("entreeeeeee", "3" + i);
                    final int finalI = i;
                    StringRequest requestGastos = new StringRequest(Request.Method.POST, URLGastos, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.names().get(0).equals("success")) {

                                    Log.d("ABONOS", "Se agrego el gasto");

                                    bdGastos.eliminarGasto(gastos.get(finalI).getId());


                                } else {
                                    Toast.makeText(context, "Error" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            //Area where the code should be added for when a message is not received
                            //by a user.

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("referencia", gastos.get(finalI).getReferencia());
                            hashMap.put("valor", "" + gastos.get(finalI).getDineroGastado());
                            hashMap.put("descripcion", gastos.get(finalI).getDescripcion());
                            hashMap.put("vendedor", gastos.get(finalI).getVendedor());
                            return hashMap;
                        }
                    };

                    MySingleton.getInstance(context).addToRequestQue(requestGastos);
                    //requestQueue.add(requestGastos);


                }

            }

        }else if(tipo==4){//DEVOLUCIONES

            if(devoluciones.size()>0){

                JSONObject jsonObjectDevolucion;
                final JSONArray jsonArray= new JSONArray();

                for(int j = 0; j<devoluciones.size(); j++) {

                    final int finalI = j;

                    HashMap<String, String> hashMapDevoluciones = new HashMap<String, String>();
                    hashMapDevoluciones.put("warehouse_id", ""+warehouse_id);
                    hashMapDevoluciones.put("idProducto", "" + bdProductos.idProducto(devoluciones.get(finalI).getCodigoProducto()));

                    JSONObject jsonObjectDevoluciones= new JSONObject(hashMapDevoluciones);

                    jsonArray.put(jsonObjectDevoluciones);
                }

                jsonObjectDevolucion= new JSONObject();
                try {
                    jsonObjectDevolucion.put("devoluciones",jsonArray);
                    Log.d("DevolucionesSubir",jsonObjectDevolucion.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest requestDevoluciones = new JsonObjectRequest(Request.Method.POST,URLDevoluciones,
                        jsonObjectDevolucion, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.names().get(0).equals("success")) {
                                bdDevoluciones.completarDevoluciones();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("errorVenta",error.toString());
                    }
                });
                MySingleton.getInstance(context).addToRequestQue(requestDevoluciones);
            }
        }


    }

}
