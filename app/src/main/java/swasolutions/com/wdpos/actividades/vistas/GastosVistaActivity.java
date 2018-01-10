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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos_vista);


        Toolbar toolbarGastos = (Toolbar) findViewById(R.id.toolbarVistaGastos);

        GastosBD bdGastos= new GastosBD(getApplicationContext(),null,1);

        ArrayList<Gasto> gastos= bdGastos.gastos();

        bdGastos.close();

        //Toast.makeText(getApplicationContext(),""+gastos.size(),Toast.LENGTH_SHORT).show();

        toolbarGastos.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView recyclerViewGastos = (RecyclerView) findViewById(R.id.recyclerViewGastos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerViewGastos.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        GastosVistaAdapter adapter = new GastosVistaAdapter(gastos);
        recyclerViewGastos.setAdapter(adapter);


    }
}
