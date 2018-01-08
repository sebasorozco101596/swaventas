package swasolutions.com.wdpos.actividades.vistas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.adaptadores.adaptadoresVistas.ClientesNuevosVistaAdapter;
import swasolutions.com.wdpos.base_de_datos.ClientesCompletoBD;
import swasolutions.com.wdpos.vo.clases_objeto.ClienteCompleto;

public class ClientesNuevosVistaActivity extends AppCompatActivity {

    private ArrayList<ClienteCompleto> clientes;

    private RecyclerView recyclerView;
    private ClientesNuevosVistaAdapter adapter;

    private Toolbar toolbarFiltrado;

    public static ClientesCompletoBD bdCliente;

    private String TIPO;
    private String NICKNAME;
    private String ID;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_nuevos_vista);

        clientes= new ArrayList<>();
        context= ClientesNuevosVistaActivity.this;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if(bundle!=null) {
            NICKNAME = bundle.getString("key_nickname");
            ID= bundle.getString("key_id");
        }

        toolbarFiltrado= (Toolbar) findViewById(R.id.toolbarVistaClientes);
        setSupportActionBar(toolbarFiltrado);

        bdCliente= new ClientesCompletoBD(getApplicationContext(),"ClientesBD",null,1);

        clientes= bdCliente.clientesVista();

        bdCliente.close();


        toolbarFiltrado.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewVistaClientes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new ClientesNuevosVistaAdapter(clientes,this,ID,NICKNAME);
        recyclerView.setAdapter(adapter);


    }
}
