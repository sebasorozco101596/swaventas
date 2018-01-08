package swasolutions.com.wdpos.vo.server;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.vo.clases_objeto.Producto;

/**
 * Created by sebas on 23/06/2017.
 */

public class Productos {

    /**
     * Variables necessary for the operation of logic.
     */
    Context context;
    ArrayList<Producto> list= new ArrayList<>();
    private String URLProductos;
    private String link;
    int warehouseId;

    public static ProductosBD bdProductos;
    public static SQLiteDatabase sqLiteDatabase;

    /**
     * Class builder
     * @param context
     */
    public Productos(Context context,String link,int warehouseId){

        this.context=context;
        this.link=link;
        this.warehouseId=warehouseId;

        URLProductos= link+"/app_movil/vendedor/productos.php";
        bdProductos= new ProductosBD(context,"BDMessages",null,1);
        sqLiteDatabase= bdProductos.getWritableDatabase();
    }

    /**
     * Method that makes a request to the server to get the friends of a specific user
     * @return
     */
    public void obtenerProductos(){

        String linkGet = URLProductos+"?warehouse_id="+warehouseId;

        //Request to the server of the user's friends.
        final JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET,linkGet, (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int count=0;

                        //Each user is scrolled and added to the friends list.
                        while(count<response.length()){

                            try {
                                JSONObject jsonObject= response.getJSONObject(count);
                                String idString = jsonObject.getString("id");
                                String name= jsonObject.getString("name");
                                String priceString= jsonObject.getString("price");
                                String codigo= jsonObject.getString("code");
                                String cantidad = jsonObject.getString("quantity");

                                Log.d("quantityyyyyyy",cantidad);

                                int id= Integer.parseInt(idString);


                                priceString.replaceAll(".",",");
                                cantidad.replaceAll(".",",");

                                //int price= Integer.parseInt(priceString);

                                Producto producto= new Producto(id,name,priceString,codigo,cantidad);

                                bdProductos.agregarProducto(id,name,priceString,codigo,cantidad);

                                Log.d("locc","entre"+name+"id: "+id);
                                list.add(producto);
                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        PanelActivity.mostrarSnockBar("proceso terminado",2);
                        bdProductos.close();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        bdProductos.close();
        MySingleton.getInstance(context).addToRequestQue(jsonArrayRequest);
    }


}