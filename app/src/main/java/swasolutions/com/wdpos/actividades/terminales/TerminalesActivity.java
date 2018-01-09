package swasolutions.com.wdpos.actividades.terminales;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.adaptadores.TerminalesAdapter;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.base_de_datos.WarehouseBD;
import swasolutions.com.wdpos.impresion.DeviceListActivity;
import swasolutions.com.wdpos.logica.Logica;
import swasolutions.com.wdpos.vo.clases_objeto.Producto;

public class TerminalesActivity extends AppCompatActivity {


    private ArrayList<Producto> productos;

    private Toolbar toolbarFiltrado;
    private TextView txtNroTerminal;

    private RecyclerView recyclerView;
    private TerminalesAdapter adapter;


    /*
    Datos impresion
     */

    private Logica logica;

    private static final String TAG = "TerminalesActivity";
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    private Button btnSearch;
    private Button btnSend;

    private BluetoothService mService = null;
    private BluetoothDevice con_dev = null;
    private Context context;

    private String fecha;

    private String msg = "";
    private String msgProductos= "";
    private String msgDatos= "";
    private String header= "";
    private String DIVIDER = "--------------------------------";
    private String DIVIDER_DOUBLE = "================================";
    private String BREAK = "\r\n";

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(context, "Conectarse correctamente",
                                    Toast.LENGTH_SHORT).show();
                            btnSend.setEnabled(true);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d(TAG, "Bluetooth está conectando");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d(TAG, "Estado Bluetooth escuchar o ninguno");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Toast.makeText(context, "Se ha perdido la conexión del dispositivo",
                            Toast.LENGTH_SHORT).show();
                    btnSend.setEnabled(false);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    Toast.makeText(context, "No se puede conectar el dispositivo",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.d(TAG, "handleMessage: no entro");
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminales);



        context = this;

        mService = new BluetoothService(this, mHandler);

        if (!mService.isAvailable()) {
            Toast.makeText(this, "Bluetooth no está disponible", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mService.isBTopen()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        productos= new ArrayList<>();
        logica= new Logica();

        fecha = (DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date()).toString());

        ProductosBD bdProductos= new ProductosBD(getApplicationContext(),null,1);
        WarehouseBD bdWarehouses = new WarehouseBD(getApplicationContext(), null, 1);

        productos= bdProductos.fillMessages();
        toolbarFiltrado= (Toolbar) findViewById(R.id.toolbarVistaProductosTerminales);
        setSupportActionBar(toolbarFiltrado);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewVistaProductosTerminales);
        txtNroTerminal = (TextView) findViewById(R.id.txtNroTerminal_terminales);

        String nombreWarhouse = bdWarehouses.obtenerNombre(ConfiguracionActivity.getPreferenciaWarehouseID(TerminalesActivity.this));
        txtNroTerminal.setText(nombreWarhouse);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new TerminalesAdapter(productos);
        recyclerView.setAdapter(adapter);

        toolbarFiltrado.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnSearch= (Button) findViewById(R.id.btnSearch_Terminales);
        btnSend= (Button) findViewById(R.id.btnSend_Terminales);

        btnSearch.setOnClickListener(new ClickEvent());
        btnSend.setOnClickListener(new ClickEvent());
        btnSend.setEnabled(false);
    }


    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v == btnSearch) {
                Intent serverIntent = new Intent(context, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                //btnSend.setEnabled(true);
                //btnSendCopia.setEnabled(true);

            }else if (v == btnSend) {

                String titulo= ConfiguracionActivity.getNombreTienda(TerminalesActivity.this);
                String direccion= ConfiguracionActivity.getDireccionTienda(TerminalesActivity.this);
                String telefono= ConfiguracionActivity.getTelefonoTienda(TerminalesActivity.this);
                header += logica.centrarCadena(titulo.toUpperCase()) + BREAK;
                header += logica.alinearLineas(direccion) + BREAK;
                header += logica.centrarCadena("Republica dominicana") + BREAK;
                header += logica.centrarCadena("Tel: " + telefono) + BREAK;

                header += DIVIDER_DOUBLE;

                msgDatos += "Fecha: " + fecha + BREAK;
                msgDatos += "Vendedor: " + LoginActivity.getUserName(TerminalesActivity.this) + BREAK;
                msgDatos += "Terminal: " + txtNroTerminal.getText().toString() + BREAK;
                msgDatos += DIVIDER + BREAK;



                byte[] cmd = new byte[3];
                cmd[0] = 0x1b;
                cmd[1] = 0x21;
                cmd[2] |= 0x10;
                mService.write(cmd);
                mService.sendMessage(header, "GBK");

                cmd[2] &= 0xEF;
                mService.write(cmd);
                mService.sendMessage(msgDatos, "GBK");

                for (int i = 0; i < productos.size(); i++) {

                    if (productos.get(i).getNombre().length() > 24) {

                        String nombre = productos.get(i).getNombre();
                        String linea1 = nombre;
                        String linea2 = "";

                        for (int j = 24; j > 1; j--) {
                            if (linea1.charAt(j) == ' ') {
                                linea1 = nombre.substring(0, j);
                                linea2 = nombre.substring(j, nombre.length());
                                break;
                            }
                        }

                        String cadenaInicio = "" + productos.get(i).getCantidad();
                        msgProductos += "" + productos.get(i).getCantidad() + logica.contarEspaciosInicio(cadenaInicio,6) +
                                linea1 +BREAK;
                        msgProductos += logica.contarEspaciosInicio(cadenaInicio,6) + linea2 + BREAK;


                    } else {
                        String cadenaInicio = "" + productos.get(i).getCantidad();
                        msgProductos += "" + productos.get(i).getCantidad() +  logica.contarEspaciosInicio(cadenaInicio,6) +
                                productos.get(i).getNombre().toString() + BREAK;
                    }
                }
                //cmd[2] |= 0x01;
                cmd[2] &= 0xEF;
                mService.write(cmd);
                mService.sendMessage(msgProductos, "GBK");

                Log.d("terminalesss", header + "\n" + msgDatos + "\n" + msgProductos + "\n" + msg);

                msg = "";
                msgProductos = "";
                msgDatos = "";
                header = "";

                Toast.makeText(getApplicationContext(),"Impresion completa",Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;


    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
                }else {
                    finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    con_dev = mService.getDevByMac(address);
                    mService.connect(con_dev);
                }
                break;
        }
    }
}
