package swasolutions.com.wdpos.actividades.clientes;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.adaptadores.DeudasAdapter;
import swasolutions.com.wdpos.base_de_datos.DeudasBD;
import swasolutions.com.wdpos.vo.clases_objeto.Deuda;

public class DeudasClienteActivity extends AppCompatActivity {

    private ArrayList<Deuda> deudas;

    private RecyclerView recyclerView;
    private DeudasAdapter adapter;

    private TextView txtTotalDeudas;

    public static DeudasBD bdDeudas;
    public static SQLiteDatabase sqLiteDatabase;

    public String CEDULA_CLIENTE;

    private String NICKNAME;
    private String IDVENDEDOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deudas_cliente);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);


        deudas= new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if(bundle!=null) {
            CEDULA_CLIENTE = bundle.getString("key_cedula_cliente");
            NICKNAME = bundle.getString("key_nickname");
            IDVENDEDOR= bundle.getString("key_idVendedor");
        }

        bdDeudas= new DeudasBD(getApplicationContext(),"DeudasBD",null,1);
        sqLiteDatabase= bdDeudas.getWritableDatabase();

        Log.d("iddddddddddddddd",""+ CEDULA_CLIENTE);
        deudas= bdDeudas.obtenerDeudas(""+ CEDULA_CLIENTE);

        txtTotalDeudas = (TextView) findViewById(R.id.txtTotalDeudas_DeudasCliente);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbarDeudas);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewDeudas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        Collections.reverse(deudas);

        //The adapter is instantiated to add a cardview for each object
        adapter = new DeudasAdapter(deudas,this, CEDULA_CLIENTE,NICKNAME,IDVENDEDOR);
        recyclerView.setAdapter(adapter);

        txtTotalDeudas.setText(""+bdDeudas.totalDeudas(CEDULA_CLIENTE));

    }
}
