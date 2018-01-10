package swasolutions.com.wdpos.actividades.vistas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.adaptadores.adaptadoresvistas.ClientesNuevosVistaAdapter;
import swasolutions.com.wdpos.base_de_datos.ClientesCompletoBD;
import swasolutions.com.wdpos.vo.clases_objeto.ClienteCompleto;

public class ClientesNuevosVistaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_nuevos_vista);

        ArrayList<ClienteCompleto> clientes= new ArrayList<>();

        Toolbar toolbarFiltrado= (Toolbar) findViewById(R.id.toolbarVistaClientes);
        setSupportActionBar(toolbarFiltrado);

        ClientesCompletoBD bdCliente= new ClientesCompletoBD(getApplicationContext(),null,1);

        clientes= bdCliente.clientesVista();

        bdCliente.close();


        toolbarFiltrado.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewVistaClientes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        ClientesNuevosVistaAdapter adapter = new ClientesNuevosVistaAdapter(clientes,this);
        recyclerView.setAdapter(adapter);


    }
}
