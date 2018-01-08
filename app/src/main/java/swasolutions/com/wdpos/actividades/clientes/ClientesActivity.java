package swasolutions.com.wdpos.actividades.clientes;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import swasolutions.com.wdpos.actividades.sharedpreferences.SharedPreferences;
import swasolutions.com.wdpos.adaptadores.ClientesAdapter;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.vo.clases_objeto.Cliente;

public class ClientesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<Cliente> clientes;

    private RecyclerView recyclerView;
    private ClientesAdapter adapter;

    private Toolbar toolbarFiltrado;

    public static ClientesBD bdCliente;
    public static GruposVendedorBD bdGruposVendedor;
    public static SQLiteDatabase sqLiteDatabase;

    private String TIPO;
    private int TOTAL;
    private String NICKNAME;
    private String ID;

    private Context context;
    private ArrayList<Integer> grupos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        clientes= new ArrayList<>();
        context= ClientesActivity.this;
        grupos= new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if(bundle!=null) {
            TIPO = bundle.getString("key_tipo");

            if(TIPO.equals("venta")){
                TOTAL= bundle.getInt("key_total");
            }

            NICKNAME = bundle.getString("key_nickname");
            ID= bundle.getString("key_id");

        }

        toolbarFiltrado= (Toolbar) findViewById(R.id.toolbarCliente);
        setSupportActionBar(toolbarFiltrado);

        bdCliente= new ClientesBD(getApplicationContext(),"ClientesBD",null,1);
        bdGruposVendedor= new GruposVendedorBD(getApplicationContext(),"GruposVendedorBD",null,1);
        sqLiteDatabase= bdCliente.getWritableDatabase();

        clientes= bdCliente.cargarClientes();

        clientes=filtrarClientes(clientes);

        bdCliente.close();


        toolbarFiltrado.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewClientes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new ClientesAdapter(clientes,this,TIPO,TOTAL,ID,NICKNAME);
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<Cliente> filtrarClientes(ArrayList<Cliente> clientes) {

        ArrayList<Cliente> clientesNew= new ArrayList<>();

        if(SharedPreferences.getPreferenciaTodosGrupos(context).toString().equals("no")){
            grupos = encontrarGrupos();

            int group_1=SharedPreferences.getPreferenciaGrupo1(context);
            int group_2=SharedPreferences.getPreferenciaGrupo2(context);
            int group_3=SharedPreferences.getPreferenciaGrupo3(context);
            int group_4=SharedPreferences.getPreferenciaGrupo4(context);
            int group_5=SharedPreferences.getPreferenciaGrupo5(context);
            int group_6=SharedPreferences.getPreferenciaGrupo6(context);

            for(int i=0;i<clientes.size();i++){
                int group_id= clientes.get(i).getGroup_id();
                if(group_id==group_1 || group_id==group_2 || group_id==group_3 || group_id==group_4
                        || group_id==group_5 || group_id==group_6){
                    clientesNew.add(clientes.get(i));
                }
            }

        }else{
            clientesNew= clientes;
        }

        return clientesNew;

    }

    private ArrayList<Integer> encontrarGrupos() {

        ArrayList<Integer> listGrupos= new ArrayList<>();

        listGrupos.add(SharedPreferences.getPreferenciaGrupo1(context));
        listGrupos.add(SharedPreferences.getPreferenciaGrupo2(context));
        listGrupos.add(SharedPreferences.getPreferenciaGrupo3(context));
        listGrupos.add(SharedPreferences.getPreferenciaGrupo4(context));
        listGrupos.add(SharedPreferences.getPreferenciaGrupo5(context));
        listGrupos.add(SharedPreferences.getPreferenciaGrupo6(context));

        return listGrupos;
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

        newText = newText.toLowerCase();
        ArrayList<Cliente> nuevaLista= new ArrayList<>();
        for(Cliente cliente:clientes){


            String nombre= cliente.getName().toLowerCase();
            String cedula= ""+cliente.getCedula();


            if(nombre.contains(newText) || cedula.contains(newText) ) {
                nuevaLista.add(cliente);

                Log.d("entrecli",newText);
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
