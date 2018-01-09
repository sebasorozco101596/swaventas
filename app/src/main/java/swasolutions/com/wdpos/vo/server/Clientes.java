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
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.vo.clases_objeto.Cliente;

/**
 * Created by sebas on 23/06/2017.
 */

public class Clientes {

    /**
     * Variables necessary for the operation of logic.
     */
    private Context context;
    private ArrayList<Cliente> list= new ArrayList<>();
    private String URLClientes;

    private ClientesBD bdClientes;

    /**
     * Class builder
     * @param context
     */
    public Clientes(Context context,String link){

        this.context=context;

        URLClientes= link+"/app_movil/cliente/clientes.php";
        bdClientes= new ClientesBD(context,null,1);
    }

    /**
     * Method that makes a request to the server to get the friends of a specific user
     * @return
     */
    public void obtenerClientes(){

        //Request to the server of the user's friends.
        final JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET,URLClientes, (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int count=0;

                        //Each user is scrolled and added to the friends list.
                        while(count<response.length()){

                            try {
                                JSONObject jsonObject= response.getJSONObject(count);

                                String cedula = jsonObject.getString("cedula");

                                //cedula = cedula.replaceFirst ("^0*", "");

                                String name= jsonObject.getString("name");
                                String compania= jsonObject.getString("company");
                                String direccion= jsonObject.getString("address");
                                String telefono= jsonObject.getString("phone");
                                String id = jsonObject.getString("id");
                                int group_id= jsonObject.getInt("customer_group_id");

                                Cliente cliente= new Cliente(cedula,name,compania,direccion,telefono,id,group_id);

                                bdClientes.agregarCliente(cedula,name,compania,direccion,telefono,id,context,group_id);


                                Log.d("cliente","entre "+name+" id: "+id + " cedula "+cedula);
                                list.add(cliente);
                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        PanelActivity.mostrarSnockBar("proceso terminado",2);
                        bdClientes.close();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        MySingleton.getInstance(context).addToRequestQue(jsonArrayRequest);
    }


}
