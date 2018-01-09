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

import swasolutions.com.wdpos.base_de_datos.WarehouseBD;
import swasolutions.com.wdpos.vo.clases_objeto.Warehouse;

/**
 * Created by sebas on 2/11/2017.
 */

public class Warehouses {

    /**
     * Variables necessary for the operation of logic.
     */
    private Context context;
    private ArrayList<Warehouse> list= new ArrayList<>();
    private String URLWarehouses;

    private WarehouseBD bdWarehouse;

    /**
     * Class builder
     * @param context
     */
    public Warehouses(Context context,String link){

        this.context=context;

        URLWarehouses= link+"/app_movil/vendedor/cargarWarehouses.php";
        bdWarehouse= new WarehouseBD(context,"WarehouseBD",null,1);
    }

    /**
     * Method that makes a request to the server to get the friends of a specific user
     * @return
     */
    public void obtenerWarehouses(){


        //Request to the server of the user's friends.
        final JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET,URLWarehouses, (String) null,
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
                                String phone = jsonObject.getString("phone");

                                Warehouse warehouse= new Warehouse(id,codigo,name,phone);

                                Log.d("warehouses",name);
                                bdWarehouse.agregarWarehouse(id,codigo,name,phone);

                                list.add(warehouse);
                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        bdWarehouse.close();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        bdWarehouse.close();
        MySingleton.getInstance(context).addToRequestQue(jsonArrayRequest);
    }


}
