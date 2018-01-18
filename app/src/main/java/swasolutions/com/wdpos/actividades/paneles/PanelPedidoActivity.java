package swasolutions.com.wdpos.actividades.paneles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.clientes.ClientesActivity;
import swasolutions.com.wdpos.base_de_datos.PedidosBD;
import swasolutions.com.wdpos.impresion.DeviceListActivity;
import swasolutions.com.wdpos.logica.Logica;
import swasolutions.com.wdpos.vo.clases_objeto.Pedido;

import static android.os.Build.ID;

public class PanelPedidoActivity extends AppCompatActivity {


    /**
     * Imprimir factura
     */
    private static final String TAG = "PanelPedidoActivity";
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    private Button btnSearch;
    private Button btnSend;
    private Button btnVolver;
    private Button btnAgregarPedido;
    private BluetoothService mService = null;
    private Context context;
    private Logica logica;

    private  String msg = "";
    private  String header= "";
    private String DIVIDER_DOUBLE = "================================";
    private String BREAK = "\r\n";

    private ArrayList<Pedido> pedidos;

    /*
    Bases de datos
     */
    public static PedidosBD bdPedidos;
    public static SQLiteDatabase sqLiteDatabase;


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
                            Log.d(TAG, "handleMessage: panel pedido");
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
                    Log.d(TAG, "handleMessage: panel pedido");
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_pedido);

        context = this;
        logica= new Logica();
        mService = new BluetoothService(this, mHandler);

        if (!mService.isAvailable()) {
            Toast.makeText(this, "Bluetooth no está disponible", Toast.LENGTH_LONG).show();
            finish();
        }

        /*
        Se instancian las bases de datos a usar
         */
        bdPedidos= new PedidosBD(getApplicationContext(),null,1);
        sqLiteDatabase= bdPedidos.getWritableDatabase();

        pedidos= bdPedidos.pedidos();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mService.isBTopen()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        btnSearch= (Button) findViewById(R.id.btnSearch_PanelPedidos);
        btnSend= (Button) findViewById(R.id.btnSend_PanelPedidos);
        btnVolver= (Button) findViewById(R.id.btnVolver_PanelPedidos);
        btnAgregarPedido= (Button) findViewById(R.id.btnCrearPedido_panelPedidos);

        btnSearch.setOnClickListener(new ClickEvent());
        btnSend.setOnClickListener(new ClickEvent());
        btnVolver.setOnClickListener(new ClickEvent());
        btnAgregarPedido.setOnClickListener(new ClickEvent());

        btnSend.setEnabled(false);
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


        finish();
    }


    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v.equals(btnSearch)) {
                Intent serverIntent = new Intent(context, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                //btnSend.setEnabled(true);
            } else if (v.equals(btnSend)) {

                String date = (DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date()).toString());

                header += logica.centrarCadena("Pedidos")+ BREAK;
                header += "Fecha:"+ date + BREAK;
                header += DIVIDER_DOUBLE;

                for (int i = 0; i < pedidos.size(); i++) {
                    String cliente= pedidos.get(i).getCliente();
                    String pedido = pedidos.get(i).getPedido();

                    msg += "Cliente: "+cliente + BREAK;
                    msg += ""+pedido +BREAK+DIVIDER_DOUBLE+BREAK;
                }
                Toast.makeText(getApplicationContext(),"Imprimiendo..",Toast.LENGTH_SHORT).show();

                byte[] cmd = new byte[3];
                cmd[0] = 0x1b;
                cmd[1] = 0x21;
                cmd[2] |= 0x10;
                mService.write(cmd);
                mService.sendMessage(header, "UTF-8");

                cmd[2] &= 0xEF;
                mService.write(cmd);
                mService.sendMessage(msg, "UTF-8");

                header="";
                msg="";

                Log.d("ImpresionPedidos",header+ "\n"+msg);

                bdPedidos.eliminarPedidos();
            }else if(v.equals(btnVolver)) {

                {

                    /**
                     * We will create a personalized alert, we will add
                     * buttons and also their actions, we give the
                     * user a description of what will
                     * happen if he deletes his account, and the user
                     * will choose whether to delete it or not.
                     */

                    AlertDialog.Builder builder = new AlertDialog.Builder(PanelPedidoActivity.this);
                    builder.setIcon(R.drawable.logo);
                    builder.setTitle("Esta seguro?");
                    builder.setMessage("Si vuelve al panel sin antes imprimir no se agregara la venta");
                    builder.setCancelable(false);

                    /**
                     * Action of the button
                     */
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

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


            }else if(v.equals(btnAgregarPedido)){

                Intent intent= new Intent(getApplicationContext(),ClientesActivity.class);
                intent.putExtra("key_tipo","pedido");
                intent.putExtra("key_id",ID);
                startActivity(intent);

            }
        }
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
                    BluetoothDevice con_dev = mService.getDevByMac(address);
                    mService.connect(con_dev);
                }
                break;
            default:
                Log.d(TAG, "onActivityResult: impresion");
                break;
        }
    }
}
