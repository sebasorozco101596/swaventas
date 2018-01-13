package swasolutions.com.wdpos.actividades.clientes;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.adaptadores.ClientesAdapter;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.impresion.DeviceListActivity;
import swasolutions.com.wdpos.logica.Logica;
import swasolutions.com.wdpos.vo.clases_objeto.Cliente;

public class ImprimirClientesActivity extends AppCompatActivity {

    private ArrayList<Cliente> clientes;

    private RecyclerView recyclerView;
    private ClientesAdapter adapter;

    private Toolbar toolbarFiltrado;

    public static ClientesBD bdCliente;

    private String NICKNAME;
    private String ID;

    private Context context;

    private Logica logica;

    private static final String TAG = "TerminalesActivity";
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    private Button btnSearch;
    private Button btnSend;

    private BluetoothService mService = null;
    private BluetoothDevice con_dev = null;

    private String msg = "";
    private String msgProductos= "";
    private String msgDatos= "";
    private String header= "";
    private String DIVIDER = "--------------------------------";
    private String DIVIDER_DOUBLE = "================================";
    private String BREAK = "\r\n";

    @SuppressLint("HandlerLeak")
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
                        default:
                            Log.d(TAG, "handleMessage: no entro");
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
    protected void onStart() {
        super.onStart();

        if (!mService.isBTopen()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        logica= new Logica();

        clientes= new ArrayList<>();
        context= ImprimirClientesActivity.this;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if(bundle!=null) {
            NICKNAME = bundle.getString("key_nickname");
            ID= bundle.getString("key_id");
        }

        toolbarFiltrado= (Toolbar) findViewById(R.id.toolbarImprimirClientes);
        setSupportActionBar(toolbarFiltrado);

        bdCliente= new ClientesBD(getApplicationContext(),null,1);

        clientes= bdCliente.cargarClientes();

        clientes= logica.filtrarClientes(clientes,context);

        bdCliente.close();


        toolbarFiltrado.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewImprimirClientes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        adapter = new ClientesAdapter(clientes,this,"imprimirClientes",0,ID,NICKNAME);
        recyclerView.setAdapter(adapter);


        btnSearch= (Button) findViewById(R.id.btnSearch_Clientes);
        btnSend= (Button) findViewById(R.id.btnSend_Clientes);

        btnSearch.setOnClickListener(new ClickEvent());
        btnSend.setOnClickListener(new ClickEvent());
        btnSend.setEnabled(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprimir_clientes);

        mService = new BluetoothService(this, mHandler);

        if (!mService.isAvailable()) {
            Toast.makeText(this, "Bluetooth no está disponible", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v.equals(btnSearch)) {
                Intent serverIntent = new Intent(context, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                btnSend.setEnabled(true);
                //btnSendCopia.setEnabled(true);

            }else if (v.equals(btnSend)) {

                String titulo= ConfiguracionActivity.getNombreTienda(context);
                String direccion= ConfiguracionActivity.getDireccionTienda(context);
                String telefono= ConfiguracionActivity.getTelefonoTienda(context);
                header += logica.centrarCadena(titulo.toUpperCase()) + BREAK;
                header += logica.alinearLineas(direccion) + BREAK;
                header += logica.centrarCadena("Republica dominicana") + BREAK;
                header += logica.centrarCadena("Tel: " + telefono) + BREAK;

                header += DIVIDER_DOUBLE;

                String fecha = (DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date()).toString());

                msgDatos += "Fecha: " + fecha + BREAK;
                msgDatos += "Vendedor: " + LoginActivity.getUserName(context) + BREAK;
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

                for (int i = 0; i < clientes.size(); i++) {

                    if (clientes.get(i).getName().length() > 24) {

                        String nombre = clientes.get(i).getName();
                        String linea1 = nombre;
                        String linea2 = "";

                        for (int j = 24; j > 1; j--) {
                            if (linea1.charAt(j) == ' ') {
                                linea1 = nombre.substring(0, j);
                                linea2 = nombre.substring(j, nombre.length());
                                break;
                            }
                        }

                        String cadenaInicio = "" + clientes.get(i).getCedula();
                        msgProductos += "" + clientes.get(i).getCedula() + logica.contarEspaciosInicio(cadenaInicio,13) +
                                linea1 +BREAK;
                        msgProductos += logica.contarEspaciosInicio(cadenaInicio,6) + linea2 + BREAK;

                    } else {
                        String cadenaInicio = "" + clientes.get(i).getCedula();
                        msgProductos += "" + clientes.get(i).getCedula() +  logica.contarEspaciosInicio(cadenaInicio,13) +
                                clientes.get(i).getName() + BREAK;
                    }
                }
                //cmd[2] |= 0x01;
                cmd[2] &= 0xEF;
                mService.write(cmd);
                mService.sendMessage(msgProductos, "GBK");

                Log.d("clientes", header + "\n" + msgDatos + "\n" + msgProductos + "\n" + msg);

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
            default:
                Log.d(TAG, "onActivityResult: terminales activity");
                break;
        }
    }
}
