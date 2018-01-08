package swasolutions.com.wdpos.actividades.facturas;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;

import java.io.File;
import java.io.FileOutputStream;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.clientes.DeudasClienteActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.SharedPreferences;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.base_de_datos.AbonosBD;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.CreditoBD;
import swasolutions.com.wdpos.base_de_datos.DeudasBD;
import swasolutions.com.wdpos.base_de_datos.VentasBD;
import swasolutions.com.wdpos.impresion.DeviceListActivity;

public class FacturaAbonoActivity extends AppCompatActivity {

    private TextView txtNombreCliente,txtNombreVendedor,txtId,txtFecha,txtTotal,txtDeuda,txtPago,
            txtTotalDeudas;

    private TextView txtTitulo,txtDireccion,txtTelefono;

    private int TOTAL;
    private int PAGADO;
    private int ID;
    private String IDCLIENTE;
    private int PAGADOTOTAL;
    private int contador;

    private String IDVENDEDOR;
    private String NICKNAME;

    private String FECHA;
    private String REFERENCIA;
    private String COMPRADOR;

    private String TOTALDEUDAS;

    private int CREDITO;

    private static final int MI_PERMISO_NETWORK = 1 ;
    private static final int MI_PERMISO_LEER = 2;
    private static final int MI_PERMISO_ESCRIBIR = 3;

    public static AbonosBD bdAbonos;
    public static DeudasBD bdDeudas;
    public static VentasBD bdVentas;
    public static CreditoBD bdCredito;
    public static ClientesBD bdClientes;
    public static SQLiteDatabase sqLiteDatabase;

    /**
     * Imprimir factura
     */
    private static final String TAG = "FacturaAbonoActivity";
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    private Button btnSearch;
    private Button btnSend;
    private Button btnVolver;
    private Button btnSendCopia;
    private BluetoothService mService = null;
    private BluetoothDevice con_dev = null;
    private Context context;

    private String msg = "";
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
                            btnSendCopia.setEnabled(true);
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
                    btnSendCopia.setEnabled(false);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    Toast.makeText(context, "No se puede conectar el dispositivo",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura_abono);

        contador=1;

        context = this;

        mService = new BluetoothService(this, mHandler);

        if (!mService.isAvailable()) {
            Toast.makeText(this, "Bluetooth no está disponible", Toast.LENGTH_LONG).show();
            finish();
        }



    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mService.isBTopen()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        txtTitulo= (TextView) findViewById(R.id.txtNombreTienda_facturaAbono);
        txtDireccion= (TextView) findViewById(R.id.txtDireccion_facturaAbono);
        txtTelefono= (TextView) findViewById(R.id.txtTelefono_facturaAbono);

        txtNombreCliente= (TextView) findViewById(R.id.txtNombreCliente_FacturaAbono);
        txtNombreVendedor= (TextView) findViewById(R.id.txtVendedor_facturaAbono);
        txtId = (TextView) findViewById(R.id.txtIdVenta_facturaAbono);
        txtFecha= (TextView) findViewById(R.id.txtFecha_facturaAbono);
        txtTotal= (TextView) findViewById(R.id.txtTotal_facturaAbono);
        txtDeuda= (TextView) findViewById(R.id.txtTotalDebido_FacturaAbono);
        txtPago= (TextView) findViewById(R.id.txtTotalPagado_FacturaAbono);
        btnSearch= (Button) findViewById(R.id.btnSearch_abono);
        btnSend= (Button) findViewById(R.id.btnSend_abono);
        btnSendCopia= (Button) findViewById(R.id.btnSendCopia_facturaAbono);
        btnVolver= (Button) findViewById(R.id.btnVolver_facturaAbono);
        txtTotalDeudas= (TextView) findViewById(R.id.txtTotalDeudas_FacturaAbono);

        btnSearch.setOnClickListener(new ClickEvent());
        btnSend.setOnClickListener(new ClickEvent());
        btnSendCopia.setOnClickListener(new ClickEvent());
        btnVolver.setOnClickListener(new ClickEvent());

        btnSendCopia.setEnabled(false);
        btnSend.setEnabled(false);


        bdAbonos= new AbonosBD(getApplicationContext(),"AbonosBD",null,1);
        bdDeudas=new DeudasBD(getApplicationContext(),"DeudasBD",null,1);
        bdVentas=new VentasBD(getApplicationContext(),"VentasBD",null,1);
        bdCredito= new CreditoBD(context,null,null,1);
        bdClientes= new ClientesBD(context,null,null,1);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if(bundle!=null) {
            TOTAL= Integer.parseInt(bundle.getString("key_total"));
            PAGADO= Integer.parseInt(bundle.getString("key_pagado"));
            PAGADOTOTAL= Integer.parseInt(bundle.getString("key_pagadoTotal"));
            //Toast.makeText(getApplicationContext(),""+PAGADOTOTAL,Toast.LENGTH_SHORT).show();

            FECHA= bundle.getString("key_fecha");
            REFERENCIA= bundle.getString("key_referencia");
            COMPRADOR= bundle.getString("key_comprador");
            ID=bundle.getInt("key_id");
            IDCLIENTE=bundle.getString("key_idCliente");

            NICKNAME = bundle.getString("key_nickname");
            IDVENDEDOR= bundle.getString("key_idVendedor");

            CREDITO= bundle.getInt("key_credito");

            int deudas= (bdDeudas.totalDeudas(IDCLIENTE));

            TOTALDEUDAS= ""+(deudas-PAGADO);

        }


        txtTitulo.setText(ConfiguracionActivity.getNombreTienda(FacturaAbonoActivity.this));
        txtDireccion.setText(ConfiguracionActivity.getDireccionTienda(FacturaAbonoActivity.this));
        txtTelefono.setText(ConfiguracionActivity.getTelefonoTienda(FacturaAbonoActivity.this));
        txtTotalDeudas.setText(TOTALDEUDAS);

        txtPago.setText(""+PAGADO);
        txtDeuda.setText(""+(TOTAL-PAGADO));
        txtTotal.setText(""+TOTAL);

        txtNombreVendedor.setText(NICKNAME);
        txtFecha.setText(FECHA);
        txtId.setText(REFERENCIA);
        txtNombreCliente.setText(COMPRADOR);

        txtNombreVendedor.setText(LoginActivity.getUserName(FacturaAbonoActivity.this));

        String date = (DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date()).toString());
        txtFecha.setText(date);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;

        bdDeudas.close();
        bdAbonos.close();
    }


    @Override
    public void finish() {
        super.finish();
        bdDeudas.close();
        bdAbonos.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent intent= new Intent(getApplicationContext(),DeudasClienteActivity.class);
        Log.d("idddd",""+ID);
        intent.putExtra("key_id",IDCLIENTE);
        startActivity(intent);

        bdDeudas.close();
        bdAbonos.close();
    }


    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v == btnSearch) {
                Intent serverIntent = new Intent(context, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                //btnSend.setEnabled(true);
                //btnSendCopia.setEnabled(true);
            } else if (v == btnSend) {

                btnSend.setEnabled(false);

                if(!bdAbonos.existeRegistro(txtId.getText().toString())){
                    String fecha= txtFecha.getText().toString();
                    String referencia= txtId.getText().toString();
                    int pagado= Integer.parseInt(txtPago.getText().toString());

                    String estadoVenta= "partial";
                    if(txtDeuda.getText().equals("0")){
                        estadoVenta="paid";
                    }

                    int idVendedor= Integer.parseInt(LoginActivity.getId(FacturaAbonoActivity.this));

                    Log.d("referenciaaaaaaa",referencia);
                    if(bdVentas.existeRegistro(referencia)){
                        bdDeudas.actualizarDeuda(ID, pagado);
                        bdVentas.actualizarVenta(bdVentas.encontrarVenta(referencia),pagado,context,
                                estadoVenta,CREDITO);


                        Log.d("creditooooo", ""+CREDITO);
                        if(CREDITO>0){
                            bdCredito.descontarCredito(IDCLIENTE,CREDITO,context);
                        }

                    }else {
                        if(CREDITO>0){
                            bdCredito.descontarCredito(IDCLIENTE,CREDITO,context);
                        }

                        bdAbonos.crearAbono(ID, estadoVenta, pagado + PAGADOTOTAL,
                                idVendedor, fecha, referencia, pagado,IDCLIENTE,CREDITO,context);
                        bdDeudas.actualizarDeuda(ID, pagado);
                    }
                    contador=5;

                    header += centrarCadena(txtTitulo.getText().toString().toUpperCase())+ BREAK;
                    header += alinearLineas(txtDireccion.getText().toString()) + BREAK;
                    header += centrarCadena("Republica dominicana")+ BREAK;
                    header += centrarCadena("Tel: "+txtTelefono.getText().toString())+BREAK ;

                    header += DIVIDER_DOUBLE + BREAK;


                    msg += "Fecha:"+ txtFecha.getText().toString()+ BREAK;
                    msg += "Id venta:" + txtId.getText().toString() + BREAK;
                    msg += "Vendedor:" +LoginActivity.getUserName(FacturaAbonoActivity.this) +BREAK;
                    msg += "Cliente:" +txtNombreCliente.getText().toString()+BREAK;

                    msg += "Total:" + txtTotal.getText().toString() + BREAK;
                    msg += "Total Pagado:" + txtPago.getText().toString() + BREAK;
                    msg += "Total a deber: "+ txtDeuda.getText().toString() + BREAK;
                    msg += "Total deudas: " + TOTALDEUDAS + BREAK + BREAK;


                    String nota= SharedPreferences.getPreferenciaNotaAbono(FacturaAbonoActivity.this).toString();
                    if(nota.length()>0) {
                        msg += "Notas:" + BREAK + nota +BREAK;
                    }


                    byte[] cmd = new byte[3];
                    cmd[0] = 0x1b;
                    cmd[1] = 0x21;
                    cmd[2] |= 0x10;
                    mService.write(cmd);
                    mService.sendMessage(header, "GBK");
                    cmd[2] &= 0xEF;
                    mService.write(cmd);
                    mService.sendMessage(msg, "GBK");

                    header="";
                    msg="";

                    takeScreenshot();

                    Toast.makeText(getApplicationContext(),"Abono guardado",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Abono ya existente",Toast.LENGTH_SHORT).show();
                }

                Log.d("lineaaaaaaaa", header + "\n" + msg);

                header = "";
                msg = "";



            }else if(v== btnVolver){

                {

                    /**
                     * We will create a personalized alert, we will add
                     * buttons and also their actions, we give the
                     * user a description of what will
                     * happen if he deletes his account, and the user
                     * will choose whether to delete it or not.
                     */

                    AlertDialog.Builder builder= new AlertDialog.Builder(FacturaAbonoActivity.this);
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

                    AlertDialog alertDialog= builder.create();
                    alertDialog.show();

                }


            }else if(v == btnSendCopia){

                AlertDialog.Builder builder= new AlertDialog.Builder(FacturaAbonoActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(FacturaAbonoActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            header="";
                            msg="";

                            header += centrarCadena(txtTitulo.getText().toString().toUpperCase())+ BREAK;
                            header += alinearLineas(txtDireccion.getText().toString()) + BREAK;
                            header += centrarCadena("Republica dominicana")+ BREAK;
                            header += centrarCadena("Tel: "+txtTelefono.getText().toString())+BREAK ;
                            header += DIVIDER_DOUBLE + BREAK;


                            msg += "Fecha:"+ txtFecha.getText().toString()+ BREAK;
                            msg += "Id venta:" + txtId.getText().toString() + BREAK;
                            msg += "Vendedor:" +LoginActivity.getUserName(FacturaAbonoActivity.this) +BREAK;
                            msg += "Cliente:" +txtNombreCliente.getText().toString()+BREAK;

                            msg += "Total:" + txtTotal.getText().toString() + BREAK;
                            msg += "Total Pagado:" + txtPago.getText().toString() + BREAK;
                            msg += "Total a deber: "+ txtDeuda.getText().toString() + BREAK;
                            msg += "Total deudas: " + ""+(bdDeudas.totalDeudas(IDCLIENTE)-PAGADO) + BREAK + BREAK;

                            String nota= SharedPreferences.getPreferenciaNotaAbono(FacturaAbonoActivity.this).toString();
                            if(nota.length()>0) {
                                msg += "Notas:" + BREAK + nota +BREAK;
                            }

                            msg += DIVIDER + BREAK;

                            msg+= "Recibido por"+BREAK+BREAK+BREAK;
                            msg+= "___________________________________"+BREAK+BREAK;

                            byte[] cmd = new byte[3];
                            cmd[0] = 0x1b;
                            cmd[1] = 0x21;
                            cmd[2] |= 0x10;
                            mService.write(cmd);
                            mService.sendMessage(header, "GBK");
                            cmd[2] &= 0xEF;
                            mService.write(cmd);
                            mService.sendMessage(msg, "GBK");

                            Toast.makeText(getApplicationContext(),"Copia correcta",Toast.LENGTH_SHORT).show();
                            Log.d("abonoFactura",header+ "\n" + msg);

                            header="";
                            msg="";

                            alertDialog.dismiss();

                        }
                    }
                });


                alertDialog.show();
            }
        }
    }

    private String centrarCadena(String cadena) {


        String espaciosInicio="";
        String espaciosFinal="";
        String cadenaFinal="";

        if(cadena.length()<32){

            int falta= ((((cadena.length()-32)*(-1))/2)-1);
            for(int i=0;i<falta;i++){
                espaciosInicio+=" ";
                espaciosFinal+=" ";
            }
            cadenaFinal=espaciosInicio+cadena+espaciosFinal;

        }else{
            cadenaFinal=cadena;
        }

        return cadenaFinal;

    }

    private String alinearLineas(String cadena){

        String nombre= cadena;
        String linea1= nombre;
        String linea2="";
        String resultado="";

        if(cadena.length()>32){



            for(int j=32;j>1;j--){

                if(linea1.charAt(j)==' '){
                    linea1= nombre.substring(0,j);
                    linea2= nombre.substring(j,nombre.length());
                    break;
                }
            }
            resultado= centrarCadena(linea1)+BREAK+centrarCadena(linea2);

            return resultado;

        }else{

            resultado= centrarCadena(cadena);

            return resultado;

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
                    con_dev = mService.getDevByMac(address);
                    mService.connect(con_dev);
                }
                break;
        }
    }

    private void takeScreenshot() {
        try {
            // image naming and path  to include sd card  appending name you choose for file
            File f = new File(Environment.getExternalStorageDirectory() + "/WDPOS");

            // Comprobamos si la carpeta está ya creada
            // Si la carpeta no está creada, la creamos.
            if(!f.isDirectory()) {
                String newFolder = "/WDPOS"; //cualquierCarpeta es el nombre de la Carpeta que vamos a crear
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File myNewFolder = new File(extStorageDirectory + newFolder);
                myNewFolder.mkdir(); //creamos la carpeta
            }else{
                Log.d("error","La carpeta ya estaba creada");
            }

            String mPath = Environment.getExternalStorageDirectory().toString() + "/WDPOS/"+ "abono_"+
                    IDCLIENTE+"_"+IDVENDEDOR+"_"+txtFecha.getText().toString()+".png";

            // create bitmap screen capture
            btnSend.setVisibility(View.INVISIBLE);
            btnVolver.setVisibility(View.INVISIBLE);
            btnSearch.setVisibility(View.INVISIBLE);
            btnSendCopia.setVisibility(View.INVISIBLE);


            View v1 = findViewById(R.id.activity_factura);
            v1.setDrawingCacheEnabled(true);
            v1.layout(0, 0, v1.getMeasuredWidth(), v1.getMeasuredHeight());
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            btnSend.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.VISIBLE);
            btnVolver.setVisibility(View.VISIBLE);
            btnSendCopia.setVisibility(View.VISIBLE);

            MediaScannerConnection.scanFile(this,
                    new String[]{imageFile.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"entre error",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
        bdAbonos.close();
        bdDeudas.close();
        bdVentas.close();
    }
}
