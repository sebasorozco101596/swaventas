package swasolutions.com.wdpos.actividades.pedidos;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.base_de_datos.PedidosBD;

public class PedidosActivity extends AppCompatActivity {


    /*
    Componentes
     */
    private TextView txtCliente;
    private EditText txtPedido;
    private Button btnAgregarPedido;
    private Toolbar toolbarPedidos;

    /*
    Variables normales
     */
    private String CLIENTE;
    private Context context;

    /*
    Bases de datos
     */
    public static PedidosBD bdPedidos;
    public static SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null) {
            CLIENTE = bundle.getString("key_cliente");
            context= PedidosActivity.this;
        }

        /*
        Instanciamos los componentes de la actividad
         */
        txtCliente= (TextView) findViewById(R.id.txtNombreCliente_pedidos);
        txtPedido= (EditText) findViewById(R.id.txtPedido_pedidos);
        btnAgregarPedido= (Button) findViewById(R.id.btnGuardarPedido_pedidos);
        toolbarPedidos= (Toolbar) findViewById(R.id.toolbarPedidos_pedidos);
        setSupportActionBar(toolbarPedidos);

        /*
        Se instancian las bases de datos a usar
         */
        bdPedidos= new PedidosBD(getApplicationContext(),null,1);
        sqLiteDatabase= bdPedidos.getWritableDatabase();


        /*
        Se cambia el valor del txtCliente por el nombre del cliente recibido desde el
        adaptador de cliente
         */
        txtCliente.setText(CLIENTE);

        /**
         * Logica al presionar los botones de la actividad
         */
        btnAgregarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pedido= txtPedido.getText().toString();
                bdPedidos.crearPedido(pedido,CLIENTE,context);
            }
        });
    }
}
