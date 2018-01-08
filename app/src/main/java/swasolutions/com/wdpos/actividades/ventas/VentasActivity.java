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

    private Button btnCancelarVenta,btnCarrito;
    private ArrayList<Producto> productos;

    private Toolbar toolbarFiltrado;

    private RecyclerView recyclerView;
    private ProductosAdapter adapter;

    private String NICKNAME;
    private String IDVENDEDOR;
    private String TIPO;
    private String CEDULA;

    private ProductosBD bdProductos;
    private CarritoBD bdCarrito;
    private DevolucionesBD bdDevoluciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        productos = new ArrayList<>();

        bdProductos = new ProductosBD(getApplicationContext(), "ProductosBD", null, 1);
        bdCarrito = new CarritoBD(getApplicationContext(), "CarritoBD0", null, 1);
        bdDevoluciones = new DevolucionesBD(getApplicationContext(), "DevolucionesBD", null, 1);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if (bundle != null) {
            NICKNAME = bundle.getString("key_nickname");
            IDVENDEDOR = bundle.getString("key_idVendedor");
            TIPO = bundle.getString("key_tipo");
            CEDULA= bundle.getString("key_cedula");
        }


        productos = bdProductos.fillMessages();
        btnCancelarVenta = (Button) findViewById(R.id.btnCancelarVenta_venta);
        btnCarrito = (Button) findViewById(R.id.btnCarroVenta);


        if(TIPO.equals("devolucion")) {
            btnCarrito.setText("Devoluciones");
            btnCancelarVenta.setText("Cancelar devoluciones");
        }

        toolbarFiltrado= (Toolbar) findViewById(R.id.toolbarVentas);
        setSupportActionBar(toolbarFiltrado);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewProductos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new ProductosAdapter(productos,this,TIPO,CEDULA);
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

                if(TIPO.equals("venta")){
                    Intent intent= new Intent(getApplicationContext(), CarritoActivity.class);
                    intent.putExtra("key_nickname",NICKNAME);
                    intent.putExtra("key_id", IDVENDEDOR);
                    startActivity(intent);
                    finish();
                }else if(TIPO.equals("devolucion")){
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

        newText = newText.toLowerCase();
        ArrayList<Producto> nuevaLista= new ArrayList<>();
        for(Producto producto:productos){


            String nombre= producto.getNombre().toLowerCase();
            String id= ""+producto.getId();


            if(nombre.contains(newText) || id.contains(newText) ) {
                nuevaLista.add(producto);
                Log.d("entrecli",newText);
            }

        }

        adapter.setFilter(nuevaLista);
        return true;
    }
}
