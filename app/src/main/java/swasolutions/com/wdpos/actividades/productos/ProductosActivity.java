package swasolutions.com.wdpos.actividades.productos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.adaptadores.ProductosAdapter;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.vo.clases_objeto.Producto;

public class ProductosActivity extends AppCompatActivity {


    private ArrayList<Producto> productos;
    private ProductosAdapter adapter;
    private ProductosBD bdProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        productos = new ArrayList<>();

        bdProductos = new ProductosBD(getApplicationContext(), null, 1);

        productos = bdProductos.fillMessages();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewProductos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new ProductosAdapter(productos,this,"editar","11",1);
        recyclerView.setAdapter(adapter);
    }
}
