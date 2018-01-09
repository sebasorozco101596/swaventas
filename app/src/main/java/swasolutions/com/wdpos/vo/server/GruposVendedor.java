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

import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.vo.clases_objeto.GrupoVendedor;

/**
 * Created by sebas on 2/11/2017.
 */

public class GruposVendedor {
    /**
     * Variables necessary for the operation of logic.
     */
    private Context context;
    private ArrayList<GrupoVendedor> list= new ArrayList<>();
    private String URLGruposClientes;

    private GruposVendedorBD bdGruposVendedor;

    /**
     * Class builder
     * @param context
     */
    public GruposVendedor(Context context,String link){

        this.context=context;

        URLGruposClientes= link+"/app_movil/vendedor/cargarGruposVendedor.php";
        bdGruposVendedor = new GruposVendedorBD(context,null,1);
    }

    /**
     * Method that makes a request to the server to get the friends of a specific user
     * @return
     */
    public void obtenerGruposVendedor(){


        //Request to the server of the user's friends.
        final JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET,URLGruposClientes, (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int count=0;

                        //Each user is scrolled and added to the friends list.
                        while(count<response.length()){

                            try {
                                JSONObject jsonObject= response.getJSONObject(count);
                                int id = jsonObject.getInt("id");
                                String name= jsonObject.getString("name");
                                int percent= jsonObject.getInt("percent");

                                GrupoVendedor grupoVendedor= new GrupoVendedor(id,name,percent);

                                bdGruposVendedor.agregarGrupoVendedor(id,name,percent);

                                list.add(grupoVendedor);
                                Log.d("gruposVendedor",name);
                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        bdGruposVendedor.close();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        bdGruposVendedor.close();
        MySingleton.getInstance(context).addToRequestQue(jsonArrayRequest);
    }


}
