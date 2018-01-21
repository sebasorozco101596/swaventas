package swasolutions.com.wdpos.actividades.clientes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.adaptadores.ClientesAdapter;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.logica.Logica;
import swasolutions.com.wdpos.vo.clases_objeto.Cliente;

public class ClientesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<Cliente> clientes;

    private ClientesAdapter adapter;

    public static ClientesBD bdCliente;
    public static GruposVendedorBD bdGruposVendedor;

    private String TIPO;
    private int TOTAL;
    private String NICKNAME;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Logica logica= new Logica();

        clientes= new ArrayList<>();
        Context context= ClientesActivity.this;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if(bundle!=null) {
            TIPO = bundle.getString("key_tipo");

            if("venta".equals(TIPO)){
                TOTAL= bundle.getInt("key_total");
            }

            NICKNAME = bundle.getString("key_nickname");
            ID= bundle.getString("key_id");

        }

        Toolbar toolbarFiltrado= (Toolbar) findViewById(R.id.toolbarCliente);
        setSupportActionBar(toolbarFiltrado);

        bdCliente= new ClientesBD(getApplicationContext(),null,1);
        bdGruposVendedor= new GruposVendedorBD(getApplicationContext(),null,1);

        clientes= bdCliente.cargarClientes();

        clientes= logica.filtrarClientes(clientes,context);

        bdCliente.close();


        toolbarFiltrado.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewClientes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new ClientesAdapter(clientes,this,TIPO,TOTAL,ID,NICKNAME);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filtrado,menu);


        MenuItem menuItem= menu.findItem(R.id.accion_buscar);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("Nombre del cliente/id");
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
        ArrayList<Cliente> nuevaLista= new ArrayList<>();
        for(Cliente cliente:clientes){


            String nombre= cliente.getName().toLowerCase();
            String cedula= ""+cliente.getCedula();


            if(nombre.contains(newTextToLower) || cedula.contains(newTextToLower) ) {
                nuevaLista.add(cliente);

                Log.d("entrecli",newTextToLower);
            }

        }

        adapter.setFilter(nuevaLista);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        finish();
    }
}
