package swasolutions.com.wdpos.actividades.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.adaptadores.adaptadoresVistas.ProductosVentaVistaAdapter;
import swasolutions.com.wdpos.base_de_datos.ProductosVentaBD;
import swasolutions.com.wdpos.vo.clases_objeto.ProductoVenta;

public class ProductosVistaActivity extends AppCompatActivity {


    private ArrayList<ProductoVenta> productoVentas;

    private RecyclerView recyclerViewProductos;
    private ProductosVentaVistaAdapter adapter;

    private Toolbar toolbarFiltrado;

    private ProductosVentaBD bdProductos;



    private int IDVENTA;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_vista);

        productoVentas= new ArrayList<>();

        toolbarFiltrado= (Toolbar) findViewById(R.id.toolbarVistaProductos);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();



        if(bundle!=null) {
            IDVENTA = bundle.getInt("key_idVenta");
        }


        bdProductos= new ProductosVentaBD(getApplicationContext(),"ProductosVentaBD",null,1);

        productoVentas= bdProductos.productosVenta(IDVENTA);

        bdProductos.close();

        //Toast.makeText(getApplicationContext(),""+productoVentas.size(),Toast.LENGTH_SHORT).show();

        toolbarFiltrado.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerViewProductos = (RecyclerView) findViewById(R.id.recyclerViewVistaProductos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerViewProductos.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new ProductosVentaVistaAdapter(productoVentas,this);
        recyclerViewProductos.setAdapter(adapter);

    }
}
