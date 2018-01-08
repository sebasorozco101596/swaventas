package swasolutions.com.wdpos.vo.server;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import swasolutions.com.wdpos.actividades.paneles.PanelActivity;
import swasolutions.com.wdpos.base_de_datos.DeudasBD;
import swasolutions.com.wdpos.vo.clases_objeto.Deuda;

/**
 * Created by sebas on 23/06/2017.
 */

public class Deudas {

    /**
     * Variables necessary for the operation of logic.
     */
    private Context context;
    private ArrayList<Deuda> list= new ArrayList<>();
    private  String URLDeudas;
    private String link;

    private DeudasBD bdDeudas;

    /**
     * Class builder
     * @param context
     */
    public Deudas(Context context,String link){

        this.context=context;
        this.link=link;

        URLDeudas= link+"/app_movil/cliente/cargarDeudas.php";
        bdDeudas= new DeudasBD(context,"BDMessages",null,1);
    }

    /**
     * Method that makes a request to the server to get the friends of a specific user
     * @return
     */
    public void obtenerDeudas(){

        //Request to the server of the user's friends.
        final JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET,URLDeudas, (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int count=0;

                        //Each user is scrolled and added to the friends list.
                        while(count<response.length()){

                            try {
                                JSONObject jsonObject= response.getJSONObject(count);
                                int id = jsonObject.getInt("id");
                                String idCliente=jsonObject.getString("customerId");
                                //idCliente = idCliente.replaceFirst ("^0*", "");
                                String total= jsonObject.getString("total");
                                String fecha= jsonObject.getString("fecha");
                                String comprador= jsonObject.getString("comprador");
                                String referencia= jsonObject.getString("referencia");
                                String pagado= jsonObject.getString("pagado");

                                Deuda deuda= new Deuda(id,idCliente,fecha,referencia,comprador,total,pagado);

                                bdDeudas.agregarDeuda(id,fecha,referencia,comprador,idCliente,total,pagado);

                                Log.d("locc","entre: "+pagado+" id: "+id+ "idCliente: "+ idCliente+ " total:" + total);
                                list.add(deuda);
                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        PanelActivity.mostrarSnockBar("proceso terminado",2);
                        bdDeudas.close();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {





            }
        });
        bdDeudas.close();
        MySingleton.getInstance(context).addToRequestQue(jsonArrayRequest);
    }


}
