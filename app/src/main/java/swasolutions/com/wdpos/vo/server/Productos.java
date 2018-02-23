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
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.vo.clases_objeto.Producto;

/**
 * Created by sebas on 23/06/2017.
 */

public class Productos {

    /**
     * Variables necessary for the operation of logic.
     */
    private Context context;
    private ArrayList<Producto> list= new ArrayList<>();
    private String URLProductos;
    int warehouseId;

    private ProductosBD bdProductos;

    /**
     * Class builder
     * @param context
     */
    public Productos(Context context,String link,int warehouseId){

        this.context=context;
        this.warehouseId=warehouseId;

        URLProductos= link+"/app_movil/vendedor/productos.php";
        bdProductos= new ProductosBD(context,null,1);
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
                                int unit= jsonObject.getInt("unit");
                                String costString= jsonObject.getString("cost");
                                String price1String= jsonObject.getString("price");
                                int price2= jsonObject.getInt("price2");
                                int price3= jsonObject.getInt("price3");
                                int price4= jsonObject.getInt("price4");
                                int price5= jsonObject.getInt("price5");
                                int price6= jsonObject.getInt("price6");
                                int categoryId= jsonObject.getInt("category_id");
                                String codigo= jsonObject.getString("code");
                                String type= jsonObject.getString("type");
                                String cantidad = jsonObject.getString("quantity");

                                Log.d("quantityyyyyyy",cantidad);

                                int id= Integer.parseInt(idString);

                                price1String.replaceAll(".",",");
                                costString.replaceAll(".",",");
                                cantidad.replaceAll(".",",");

                                //int price= Integer.parseInt(priceString);

                                Producto producto= new Producto(id,name,unit,costString,price1String,price2,price3,
                                        price4,price5,price6,categoryId,type,codigo,cantidad,1);

                                bdProductos.agregarProducto(id,name,unit,costString,price1String,price2,price3,
                                        price4,price5,price6,categoryId,type,codigo,cantidad);

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
