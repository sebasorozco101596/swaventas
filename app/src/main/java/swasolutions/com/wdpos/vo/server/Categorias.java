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

import swasolutions.com.wdpos.base_de_datos.CategoriasBD;
import swasolutions.com.wdpos.vo.clases_objeto.Categoria;

/**
 * Created by sebas on 16/12/2017.
 */

public class Categorias {

    /**
     * Variables necessary for the operation of logic.
     */
    private Context context;
    private ArrayList<Categoria> list= new ArrayList<>();
    private String URLCategorias;

    private CategoriasBD bdCategorias;

    /**
     * Class builder
     * @param context
     */
    public Categorias(Context context,String link){

        this.context=context;

        URLCategorias= link+"/app_movil/vendedor/cargarCategorias.php";
        bdCategorias= new CategoriasBD(context,null,1);
    }

    /**
     * Method that makes a request to the server to get the friends of a specific user
     * @return
     */
    public void obtenerCategorias(){


        //Request to the server of the user's friends.
        final JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET,URLCategorias, (String) null,
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
                                String codigo= jsonObject.getString("code");

                                Categoria categoria= new Categoria(id,codigo,name);

                                Log.d("warehouses",name);
                                bdCategorias.agregarCategoria(id,codigo,name);

                                list.add(categoria);
                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        bdCategorias.close();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        bdCategorias.close();
        MySingleton.getInstance(context).addToRequestQue(jsonArrayRequest);
    }
}
