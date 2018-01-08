package swasolutions.com.wdpos.actividades.devoluciones;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.ventas.VentasActivity;
import swasolutions.com.wdpos.adaptadores.ProductosDevolucionAdapter;
import swasolutions.com.wdpos.base_de_datos.CreditoBD;
import swasolutions.com.wdpos.base_de_datos.DevolucionesBD;
import swasolutions.com.wdpos.vo.clases_objeto.Devolucion;

public class DevolucionActivity extends AppCompatActivity implements View.OnClickListener{

    private String CEDULA;

    private Button btnCancelarDevolucion,btnProductos,btnCompletarDevolucion;
    private static TextView txtTotal,txtFecha;


    private ArrayList<Devolucion> productosDevolucion;

    private RecyclerView recyclerView;
    private ProductosDevolucionAdapter adapter;

    public static CreditoBD bdCredito;
    public static DevolucionesBD bdDevoluciones;
    public static SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);

        productosDevolucion= new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            CEDULA = bundle.getString("key_cedula");
        }

        bdDevoluciones= new DevolucionesBD(getApplicationContext(),"DevolucionesBD",null,1);
        bdCredito= new CreditoBD(getApplicationContext(),null,null,1);


        productosDevolucion= bdDevoluciones.devoluciones(CEDULA);


        btnCancelarDevolucion= (Button) findViewById(R.id.btnCancelarDevo_devolucion);
        btnProductos= (Button) findViewById(R.id.btnProductos_devolucion);
        btnCompletarDevolucion= (Button) findViewById(R.id.btnCompletarDevo_devolucion);
        txtTotal= (TextView) findViewById(R.id.txtPrecioTotal_devolucion);
        txtFecha= (TextView) findViewById(R.id.txtFecha_devolucion);

        btnCompletarDevolucion.setOnClickListener(this);
        btnProductos.setOnClickListener(this);
        btnCancelarDevolucion.setOnClickListener(this);

        String date = (DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date()).toString());
        txtFecha.setText(date);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewDevoluciones);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new ProductosDevolucionAdapter(productosDevolucion,this);
        recyclerView.setAdapter(adapter);



        txtTotal.setText(""+calcularTotal(productosDevolucion));



    }

    private int calcularTotal(ArrayList<Devolucion> productosDevolucion) {

        int total=0;

        for(int i=0;i<productosDevolucion.size();i++){
            total+=productosDevolucion.get(i).getValor();
        }

        return total;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancelarDevo_devolucion:

                /**
                 * We will create a personalized alert, we will add
                 * buttons and also their actions, we give the
                 * user a description of what will
                 * happen if he deletes his account, and the user
                 * will choose whether to delete it or not.
                 */

                AlertDialog.Builder builder= new AlertDialog.Builder(DevolucionActivity.this);
                builder.setIcon(R.drawable.logo);
                builder.setTitle(R.string.tittle_warning_drop_devolucion);
                builder.setMessage(R.string.detail_devoluciones);
                builder.setCancelable(false);

                /**
                 * Action of the button
                 */
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        bdDevoluciones.eliminarDevoluciones(CEDULA);
                        Toast.makeText(getApplicationContext(),"Devolucion cancelada",Toast.LENGTH_LONG).show();
                        finish();
                        bdDevoluciones.close();
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

                break;

            case R.id.btnProductos_devolucion:

                Intent intentDevolucion= new Intent(getApplicationContext(),VentasActivity.class);
                intentDevolucion.putExtra("key_id",CEDULA);
                intentDevolucion.putExtra("key_cedula",CEDULA);
                intentDevolucion.putExtra("key_tipo","devolucion");
                startActivity(intentDevolucion);
                break;

            case R.id.btnCompletarDevo_devolucion:

                bdCredito.agregarCredito(CEDULA,
                        Integer.parseInt(txtTotal.getText().toString()),getApplicationContext());
                for(int i=0;i<productosDevolucion.size();i++){
                    bdDevoluciones.actualizarDevolucion(productosDevolucion.get(i).getId());
                }
                finish();
                break;
        }
    }
}
