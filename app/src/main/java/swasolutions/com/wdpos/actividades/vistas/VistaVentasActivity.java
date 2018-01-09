package swasolutions.com.wdpos.actividades.vistas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.adaptadores.adaptadoresvistas.VentasVistaAdapter;
import swasolutions.com.wdpos.base_de_datos.VentasBD;
import swasolutions.com.wdpos.vo.clases_objeto.Venta;

public class VistaVentasActivity extends AppCompatActivity {


    private ArrayList<Venta> ventas;

    private RecyclerView recyclerViewVentas;
    private VentasVistaAdapter adapter;

    private Toolbar toolbarFiltrado;

    private VentasBD bdVentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_ventas);


        ventas= new ArrayList<>();

        toolbarFiltrado= (Toolbar) findViewById(R.id.toolbarVistaVentas);

        bdVentas= new VentasBD(getApplicationContext(),null,1);

        ventas= bdVentas.ventas();

        bdVentas.close();

        //Toast.makeText(getApplicationContext(),""+ventas.size(),Toast.LENGTH_SHORT).show();

        toolbarFiltrado.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerViewVentas = (RecyclerView) findViewById(R.id.recyclerViewVentas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerViewVentas.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new VentasVistaAdapter(ventas,this);
        recyclerViewVentas.setAdapter(adapter);

    }
}
