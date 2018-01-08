package swasolutions.com.wdpos.actividades.vistas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.adaptadores.adaptadoresvistas.GastosVistaAdapter;
import swasolutions.com.wdpos.base_de_datos.GastosBD;
import swasolutions.com.wdpos.vo.clases_objeto.Gasto;

public class GastosVistaActivity extends AppCompatActivity {

    private ArrayList<Gasto> gastos;

    private RecyclerView recyclerViewGastos;
    private GastosVistaAdapter adapter;

    private Toolbar toolbarGastos;

    private GastosBD bdGastos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos_vista);

        gastos= new ArrayList<>();

        toolbarGastos = (Toolbar) findViewById(R.id.toolbarVistaGastos);

        bdGastos= new GastosBD(getApplicationContext(),"GastosBD",null,1);

        gastos= bdGastos.gastos();

        bdGastos.close();

        //Toast.makeText(getApplicationContext(),""+gastos.size(),Toast.LENGTH_SHORT).show();

        toolbarGastos.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerViewGastos = (RecyclerView) findViewById(R.id.recyclerViewGastos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerViewGastos.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new GastosVistaAdapter(gastos,this);
        recyclerViewGastos.setAdapter(adapter);


    }
}
