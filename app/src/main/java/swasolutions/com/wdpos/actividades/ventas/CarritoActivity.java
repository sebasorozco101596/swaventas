package swasolutions.com.wdpos.actividades.ventas;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.clientes.ClientesActivity;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.adaptadores.ProductosCarritoAdapter;
import swasolutions.com.wdpos.base_de_datos.CarritoBD;
import swasolutions.com.wdpos.vo.clases_objeto.ProductoCarrito;

public class CarritoActivity extends AppCompatActivity {

    private Button btnCancelarVenta,btnProductos,btnVender;
    private static TextView txtTotal;


    private ArrayList<ProductoCarrito> productosCarrito;

    private RecyclerView recyclerView;
    private ProductosCarritoAdapter adapter;

    public static CarritoBD bdCarrito;
    public static SQLiteDatabase sqLiteDatabase;

    private String NICKNAME;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);



        productosCarrito= new ArrayList<>();

        txtTotal= (TextView) findViewById(R.id.txtPrecioTotal_Carrito);
        btnCancelarVenta= (Button) findViewById(R.id.btnCancelarVenta_carrito);
        btnProductos= (Button) findViewById(R.id.btnProductos_carrito);
        btnVender= (Button) findViewById(R.id.btnVender_carrito);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            NICKNAME = bundle.getString("key_nickname");
            ID = bundle.getString("key_id");
        }


        bdCarrito= new CarritoBD(getApplicationContext(),"CarritoBD",null,1);
        sqLiteDatabase= bdCarrito.getWritableDatabase();

        productosCarrito= bdCarrito.cargarProductosCarrito();

        bdCarrito.close();

        final int total= calcularTotal(productosCarrito);

        txtTotal.setText(""+total);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCarroVenta);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new ProductosCarritoAdapter(productosCarrito,this,total, LoginActivity.getUserType(this));
        recyclerView.setAdapter(adapter);


        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),VentasActivity.class);
                intent.putExtra("key_tipo","venta");
                intent.putExtra("key_nickname",NICKNAME);
                intent.putExtra("key_id",ID);
                startActivity(intent);
                finish();
            }
        });


        btnCancelarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {

                    /**
                     * We will create a personalized alert, we will add
                     * buttons and also their actions, we give the
                     * user a description of what will
                     * happen if he deletes his account, and the user
                     * will choose whether to delete it or not.
                     */

                    AlertDialog.Builder builder= new AlertDialog.Builder(CarritoActivity.this);
                    builder.setIcon(R.drawable.logo);
                    builder.setTitle(R.string.tittle_warning_drop_account);
                    builder.setMessage(R.string.detail_drop_account);
                    builder.setCancelable(false);

                    /**
                     * Action of the button
                     */
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            bdCarrito.eliminarProductosCarrito();
                            Toast.makeText(getApplicationContext(),"Venta cancelada",Toast.LENGTH_LONG).show();
                            finish();
                            bdCarrito.close();
                        }
                    });
                    /**
                     * Action of the button
                     */
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog= builder.create();
                    alertDialog.show();


                }

            }
        });

        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (productosCarrito.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Agregue productos antes de vender",
                            Toast.LENGTH_SHORT).show();
                } else {

                    /**
                     * We will create a personalized alert, we will add
                     * buttons and also their actions, we give the
                     * user a description of what will
                     * happen if he deletes his account, and the user
                     * will choose whether to delete it or not.
                     */

                    AlertDialog.Builder builder = new AlertDialog.Builder(CarritoActivity.this);
                    builder.setIcon(R.drawable.logo);
                    builder.setTitle(R.string.tituloVender);
                    builder.setMessage(R.string.mensajeVender);
                    builder.setCancelable(false);

                    /**
                     * Action of the button
                     */
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(getApplicationContext(), ClientesActivity.class);
                            intent.putExtra("key_tipo", "venta");
                            int totalPagar = Integer.parseInt(txtTotal.getText().toString());
                            intent.putExtra("key_total", totalPagar);
                            startActivity(intent);
                            finish();
                        }
                    });
                    /**
                     * Action of the button
                     */
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                }


            }
        });

    }

    public static int calcularTotal(List<ProductoCarrito> productos){

        int total=0;

        for(int i =0;i<productos.size();i++){

            total+= (productos.get(i).getPrecio())*(productos.get(i).getCantidad());

        }


        txtTotal.setText(""+total);
        return total;
    }
}
