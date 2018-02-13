package swasolutions.com.wdpos.actividades.ventas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.devoluciones.DevolucionActivity;
import swasolutions.com.wdpos.adaptadores.ProductosAdapter;
import swasolutions.com.wdpos.base_de_datos.CarritoBD;
import swasolutions.com.wdpos.base_de_datos.DevolucionesBD;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.vo.clases_objeto.Producto;

public class VentasActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ArrayList<Producto> productos;

    private ProductosAdapter adapter;

    private String IDVENDEDOR;
    private String TIPO;
    private String CEDULA;
    private String NOMBRE;
    private int ID_GRUPO;

    private ProductosBD bdProductos;
    private CarritoBD bdCarrito;
    private DevolucionesBD bdDevoluciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        productos = new ArrayList<>();

        bdProductos = new ProductosBD(getApplicationContext(), null, 1);
        bdCarrito = new CarritoBD(getApplicationContext(), null, 1);
        bdDevoluciones = new DevolucionesBD(getApplicationContext(), null, 1);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if (bundle != null) {
            IDVENDEDOR = bundle.getString("key_idVendedor");
            TIPO = bundle.getString("key_tipo");
            CEDULA= bundle.getString("key_cedula");
            NOMBRE= bundle.getString("key_nombre");
            ID_GRUPO= bundle.getInt("key_grupo");
        }


        productos = bdProductos.fillMessages();
        Button btnCancelarVenta = (Button) findViewById(R.id.btnCancelarVenta_venta);
        Button btnCarrito = (Button) findViewById(R.id.btnCarroVenta);


        if("devolucion".equals(TIPO)) {
            btnCarrito.setText("Devoluciones");
            btnCancelarVenta.setText("Cancelar devoluciones");
        }

        Toolbar toolbarFiltrado= (Toolbar) findViewById(R.id.toolbarVentas);
        setSupportActionBar(toolbarFiltrado);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewProductos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new ProductosAdapter(productos,this,TIPO,CEDULA,ID_GRUPO);
        recyclerView.setAdapter(adapter);

        toolbarFiltrado.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnCancelarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(TIPO.equals("devolucion")){

                    /**
                     * We will create a personalized alert, we will add
                     * buttons and also their actions, we give the
                     * user a description of what will
                     * happen if he deletes his account, and the user
                     * will choose whether to delete it or not.
                     */

                    AlertDialog.Builder builder= new AlertDialog.Builder(VentasActivity.this);
                    builder.setIcon(R.drawable.logo);
                    builder.setTitle(R.string.tittle_warning_drop_devolucion);
                    builder.setMessage(R.string.detail_devoluciones);
                    builder.setCancelable(false);
                    /**
                     * Action of the button
                     */
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            bdDevoluciones.eliminarDevoluciones(CEDULA);
                            Toast.makeText(getApplicationContext(),"Devolucion cancelada",Toast.LENGTH_LONG).show();
                            finish();
                            bdDevoluciones.close();
                            bdCarrito.close();
                            bdProductos.close();

                        }
                    });
                    /**
                     * Action of the button
                     */
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog= builder.create();
                    alertDialog.show();

                }else{
                    /**
                     * We will create a personalized alert, we will add
                     * buttons and also their actions, we give the
                     * user a description of what will
                     * happen if he deletes his account, and the user
                     * will choose whether to delete it or not.
                     */

                    AlertDialog.Builder builder= new AlertDialog.Builder(VentasActivity.this);
                    builder.setIcon(R.drawable.logo);
                    builder.setTitle(R.string.tittle_warning_drop_account);
                    builder.setMessage(R.string.detail_drop_account);
                    builder.setCancelable(false);

                    /**
                     * Action of the button
                     */
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            bdCarrito.eliminarProductosCarrito();
                            Toast.makeText(getApplicationContext(),"Venta Cancelada",Toast.LENGTH_LONG).show();
                            finish();
                            bdDevoluciones.close();
                            bdCarrito.close();
                            bdProductos.close();
                        }
                    });
                    /**
                     * Action of the button
                     */
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog= builder.create();
                    alertDialog.show();
                }

            }
        });

        btnCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("venta".equals(TIPO)){
                    Intent intent= new Intent(getApplicationContext(), CarritoActivity.class);
                    intent.putExtra("key_id", IDVENDEDOR);
                    intent.putExtra("key_cedula",CEDULA);
                    intent.putExtra("key_nombre",NOMBRE);
                    startActivity(intent);
                    finish();
                }else if("devolucion".equals(TIPO)){
                    Intent intentDevoluciones= new Intent(getApplicationContext(), DevolucionActivity.class);
                    intentDevoluciones.putExtra("key_cedula",CEDULA);
                    startActivity(intentDevoluciones);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filtrado,menu);
        MenuItem menuItem= menu.findItem(R.id.accion_buscar);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("Nombre del producto/id");
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String newTextToLower = newText.toLowerCase();
        ArrayList<Producto> nuevaLista= new ArrayList<>();
        for(Producto producto:productos){


            String nombre= producto.getNombre().toLowerCase();
            String id= ""+producto.getId();


            if(nombre.contains(newTextToLower) || id.contains(newTextToLower) ) {
                nuevaLista.add(producto);
                Log.d("entrecli",newTextToLower);
            }

        }

        adapter.setFilter(nuevaLista);
        return true;
    }
}
