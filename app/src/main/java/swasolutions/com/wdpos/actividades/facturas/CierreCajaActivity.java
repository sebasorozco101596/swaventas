package swasolutions.com.wdpos.actividades.facturas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zj.btsdk.BluetoothService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;
import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.base_de_datos.AbonosBD;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.ClientesCompletoBD;
import swasolutions.com.wdpos.base_de_datos.DevolucionesBD;
import swasolutions.com.wdpos.base_de_datos.GastosBD;
import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.base_de_datos.ProductosVentaBD;
import swasolutions.com.wdpos.base_de_datos.VentasBD;
import swasolutions.com.wdpos.impresion.DeviceListActivity;
import swasolutions.com.wdpos.vo.clases_objeto.Abono;
import swasolutions.com.wdpos.vo.clases_objeto.Cliente;
import swasolutions.com.wdpos.vo.clases_objeto.ClienteCompleto;
import swasolutions.com.wdpos.vo.clases_objeto.Devolucion;
import swasolutions.com.wdpos.vo.clases_objeto.Gasto;
import swasolutions.com.wdpos.vo.clases_objeto.ProductoVenta;
import swasolutions.com.wdpos.vo.clases_objeto.Venta;
import swasolutions.com.wdpos.vo.clases_objeto.VentaCompleta;
import swasolutions.com.wdpos.vo.server.Clientes;
import swasolutions.com.wdpos.vo.server.MySingleton;
import swasolutions.com.wdpos.vo.server.SubirServidor;

import static android.provider.ContactsContract.DisplayNameSources.NICKNAME;


public class CierreCajaActivity extends AppCompatActivity {

    private static final int MI_PERMISO_NETWORK = 1;
    private static final int MI_PERMISO_LEER = 2 ;
    private static final int MI_PERMISO_ESCRIBIR = 3;
    private TextView txtCantidadProductos,txtTotalVentas,
            txtDineroRecibidoVentas,txtDineroRecibidoAbonos,txtTotalGastos,txtDineroEntregar;

    private TextView txtFecha,txtVendedor,txtNumeroClientesNuevos,txtProductosDevolver;

    private TextView txtTitulo,txtDireccion,txtTelefono;

    /**
     * Datos necesarios para la impresion
     */

    private static final String TAG = "CierreCajaActivity";
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private String URLClientes;

    private JsonObjectRequest requestCliente;


    private Button btnSearch;
    private Button btnSend;
    private Button btnVolver;
    private Button btnImprimirDetallado;
    private Button btnSubirClientes;
    private Button btnEliminarCliRepetidos;
    private BluetoothService mService = null;
    private BluetoothDevice con_dev = null;
    private Context context;

    private String msg = "";
    private String msgProductos= "";
    private String msgAbonos = "";
    private String msgGastos = "";
    private String msgDatos= "";
    private String header= "";
    private String DIVIDER = "--------------------------------";
    private String DIVIDER_DOUBLE = "================================";
    private String BREAK = "\r\n";

    /**
     * Datos que llegaran desde el panel
     */

    private int TOTAL_VENTAS;
    private int DINERO_VENTAS;
    private int DINERO_ABONOS;
    private int TOTAL_GASTOS;
    private int DINERO_ENTREGAR;

    /**
     * Listas que se llenaran con datos de la base de datos local
     */

    private ArrayList<Abono> abonos;
    private ArrayList<Venta> ventas;
    private ArrayList<ProductoVenta> productosVentas;
    private ArrayList<Gasto> gastos;
    private ArrayList<VentaCompleta> ventasCompleto;
    private ArrayList<Devolucion> devoluciones;
    private ArrayList<String> clientesRepetidos;
    public String link;
    public String VENDEDOR;
    public String ID;
    public String CICLO;


    private int contador;
    /**
     * Bases de datos locales
     */

    public static GastosBD bdGastos;
    public static AbonosBD bdAbonos;
    public static ProductosVentaBD bdProductosVenta;
    public static VentasBD bdVentas;
    public static ClientesCompletoBD bdClientesCompleto;
    public static GruposVendedorBD bdGruposVendedor;

    public static DevolucionesBD bdDevoluciones;
    public static ClientesBD bdClientes;



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
                            btnImprimirDetallado.setEnabled(true);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d(TAG, "Bluetooth está conectando");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d(TAG, "Estado Bluetooth escuchar o ninguno");
                            break;
                        default:
                            Log.d(TAG, "handleMessage: entre");
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
                    Log.d(TAG, "handleMessage: entre");
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cierre_caja);

        context = this;
        contador=0;

        clientesRepetidos= new ArrayList<>();
        devoluciones= new ArrayList<>();


        mService = new BluetoothService(this, mHandler);

        if (!mService.isAvailable()) {
            Toast.makeText(this, "Bluetooth no está disponible", Toast.LENGTH_LONG).show();
            finish();
        }


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null) {
            TOTAL_VENTAS= bundle.getInt("key_totalVentas");
            DINERO_VENTAS= bundle.getInt("key_dineroRecibidoVentas");
            DINERO_ABONOS= bundle.getInt("key_dineroRecibidoAbonos");
            TOTAL_GASTOS= bundle.getInt("key_totalGastos");
            DINERO_ENTREGAR= bundle.getInt("key_dineroEntregar");
            CICLO= bundle.getString("key_ciclo");
        }

        if("1".equals(CICLO)){
            if(bdAbonos.existenAbonos() || bdGastos.existenGastos() || bdVentas.existenVentas()
                    || bdProductosVenta.existenProductos() ){
                if(bdAbonos.existenAbonos()){
                    Toast.makeText(getApplicationContext(),"Existen abonos",Toast.LENGTH_SHORT).show();
                }else if(bdGastos.existenGastos()){
                    Toast.makeText(getApplicationContext(),"Existen gastos",Toast.LENGTH_SHORT).show();
                }else if(bdVentas.existenVentas()){
                    Toast.makeText(getApplicationContext(),"Existen ventas",Toast.LENGTH_SHORT).show();
                }else if(bdProductosVenta.existenProductos()){
                    Toast.makeText(getApplicationContext(),"Existen productos",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Cierre de caja completo",Toast.LENGTH_SHORT).show();
            }

        }else if("2".equals(CICLO) && bdClientesCompleto.clientes().size()>0){
                Toast.makeText(getApplicationContext(),"Aun existen clientes",Toast.LENGTH_SHORT).show();
        }
    }

    private void solicitarPermisos() {

        if (ActivityCompat.checkSelfPermission(CierreCajaActivity.this,
                Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(CierreCajaActivity.this,
                    Manifest.permission.ACCESS_NETWORK_STATE)) {

                new SweetAlertDialog(CierreCajaActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Atencion!")
                        .setContentText("debes otorgar permisos")
                        .setConfirmText("solicitar permiso")
                        .setCancelText("cancelar")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                ActivityCompat.requestPermissions(CierreCajaActivity.this,
                                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                                        MI_PERMISO_NETWORK);
                            }
                        }).show();
            } else {

                ActivityCompat.requestPermissions(CierreCajaActivity.this,
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                        MI_PERMISO_NETWORK);
            }
            return;

        }else if(ActivityCompat.checkSelfPermission(CierreCajaActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){


            if (ActivityCompat.shouldShowRequestPermissionRationale(CierreCajaActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                new SweetAlertDialog(CierreCajaActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Atencion!")
                        .setContentText("debes otorgar permisos")
                        .setConfirmText("solicitar permiso")
                        .setCancelText("cancelar")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                ActivityCompat.requestPermissions(CierreCajaActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        MI_PERMISO_LEER);
                            }
                        }).show();
            } else {

                ActivityCompat.requestPermissions(CierreCajaActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MI_PERMISO_LEER);
            }
            return;

        }else if(ActivityCompat.checkSelfPermission(CierreCajaActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){


            if (ActivityCompat.shouldShowRequestPermissionRationale(CierreCajaActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                new SweetAlertDialog(CierreCajaActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Atencion!")
                        .setContentText("debes otorgar permisos")
                        .setConfirmText("solicitar permiso")
                        .setCancelText("cancelar")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                ActivityCompat.requestPermissions(CierreCajaActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MI_PERMISO_ESCRIBIR);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(CierreCajaActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MI_PERMISO_ESCRIBIR);
            }
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mService.isBTopen()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }



        ventasCompleto = new ArrayList<>();

        btnSearch = (Button) findViewById(R.id.btnSearch_cierreCaja);
        btnSend = (Button) findViewById(R.id.btnSend_cierreCaja);
        btnVolver= (Button) findViewById(R.id.btnVolver_cierreCaja);
        btnSubirClientes= (Button) findViewById(R.id.btnSubirClientes_cierreCaja);
        btnImprimirDetallado= (Button) findViewById(R.id.btnImprimirDetallado_cierreCaja);
        btnEliminarCliRepetidos= (Button) findViewById(R.id.btnEliminarCliRepetidos_cierreCaja);

        txtTitulo= (TextView) findViewById(R.id.txtNombreTienda_cierreCaja);
        txtDireccion= (TextView) findViewById(R.id.txtDireccion_cierreCaja);
        txtTelefono= (TextView) findViewById(R.id.txtTelefono_cierreCaja);

        txtFecha= (TextView) findViewById(R.id.txtFecha_cierreCaja);
        txtVendedor= (TextView) findViewById(R.id.txtVendedor_cierreCaja);

        txtCantidadProductos= (TextView) findViewById(R.id.txtCantidadProductos_cierreCaja);
        txtTotalVentas= (TextView) findViewById(R.id.txtTotalVentas_cierreCaja);
        txtDineroRecibidoVentas= (TextView) findViewById(R.id.txtDineroVentas_cierreCaja);
        txtDineroRecibidoAbonos= (TextView) findViewById(R.id.txtDineroAbonos_CierreCaja);
        txtTotalGastos= (TextView) findViewById(R.id.txtTotalGastos_cierreCaja);
        txtDineroEntregar= (TextView) findViewById(R.id.txtEfectivoEntregar_cierreCaja);

        txtNumeroClientesNuevos= (TextView) findViewById(R.id.txtClientesNuevos_cierreCaja);
        txtProductosDevolver= (TextView) findViewById(R.id.txtProductosDevolver_cierreCaja);



        /**
         * Instancia de las bases de datos locales
         */

        bdGastos= new GastosBD(getApplicationContext(),null,1);
        bdAbonos= new AbonosBD(getApplicationContext(),null,1);
        bdVentas= new VentasBD(getApplicationContext(),null,1);
        bdProductosVenta= new ProductosVentaBD(getApplicationContext(),null,1);
        bdGruposVendedor = new GruposVendedorBD(getApplicationContext(),null,1);
        bdClientesCompleto= new ClientesCompletoBD(getApplicationContext(),null,1);
        bdDevoluciones= new DevolucionesBD(getApplicationContext(),null,1);
        bdClientes = new ClientesBD(getApplicationContext(), null, 1);
        //------------

        if(bdClientesCompleto.clientes().size() !=0){
            btnSubirClientes.setEnabled(true);
        }else{
            btnSubirClientes.setEnabled(false);
        }


        devoluciones=bdDevoluciones.devolucionesCierre();

        link= ConfiguracionActivity.getLinkHosting(CierreCajaActivity.this);
        VENDEDOR= LoginActivity.getUserName(CierreCajaActivity.this);
        ID= LoginActivity.getId(CierreCajaActivity.this);


        txtProductosDevolver.setText(""+bdDevoluciones.contarDevoluciones());


        btnSearch.setOnClickListener(new ClickEvent());
        btnSend.setOnClickListener(new ClickEvent());
        btnVolver.setOnClickListener(new ClickEvent());
        btnImprimirDetallado.setOnClickListener(new ClickEvent());
        btnSubirClientes.setOnClickListener(new ClickEvent());
        btnEliminarCliRepetidos.setOnClickListener(new ClickEvent());
        btnSend.setEnabled(false);
        btnImprimirDetallado.setEnabled(false);
        btnEliminarCliRepetidos.setEnabled(false);

        if(validarClientesRepetidos(bdClientes.cargarClientes(),bdClientesCompleto.clientes())){
            btnEliminarCliRepetidos.setEnabled(true);
        }



        txtTitulo.setText(ConfiguracionActivity.getNombreTienda(CierreCajaActivity.this));
        txtDireccion.setText(ConfiguracionActivity.getDireccionTienda(CierreCajaActivity.this));
        txtTelefono.setText(ConfiguracionActivity.getTelefonoTienda(CierreCajaActivity.this));

        String date = (DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date()).toString());
        txtFecha.setText(date);
        txtVendedor.setText(VENDEDOR);

        //""+CANTIDAD_PRODUCTOS

        txtCantidadProductos.setText(""+bdProductosVenta.cantidadProductosVentas());
        txtTotalVentas.setText(""+TOTAL_VENTAS);
        txtDineroRecibidoVentas.setText(""+DINERO_VENTAS);
        txtDineroRecibidoAbonos.setText(""+DINERO_ABONOS);
        txtTotalGastos.setText(""+TOTAL_GASTOS);
        txtDineroEntregar.setText(""+DINERO_ENTREGAR);

        txtNumeroClientesNuevos.setText(""+bdClientesCompleto.clientes().size());
    }

    private boolean validarClientesRepetidos(ArrayList<Cliente> clientes, ArrayList<ClienteCompleto> clientesNuevos) {

        boolean bandera=false;

        for(int i=0;i<clientesNuevos.size();i++){
            for(int j=0;j<clientes.size();j++){
                if(clientesNuevos.get(i).getCedula().toString().equals(clientes.get(j).getCedula().toString())
                        && !clientesNuevos.get(i).getNombre().toString().equals(clientes.get(j).getName().toString())){
                    bandera=true;
                    clientesRepetidos.add(clientesNuevos.get(i).getId());
                }
            }
        }

        return bandera;

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

    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v.equals(btnSearch)) {
                Intent serverIntent = new Intent(context, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

                //btnSend.setEnabled(true);
                    //bdVentas.eliminarVentas();
                    //bdProductosVenta.eliminarProductosVenta();
                //btnImprimirDetallado.setEnabled(true);


                ventas= bdVentas.ventas();
                abonos= bdAbonos.abonos();
                gastos=bdGastos.gastos();
                productosVentas= bdProductosVenta.productosVenta();
                ventasCompleto= encontrarVentas(ventas,productosVentas);


            }else if(v.equals(btnSubirClientes)){

                URLClientes = link + "/app_movil/vendedor/registrarClienteTest.php";
                //Log.d("registroCliente", "" + clientes.size());

                int numeroClientes= bdClientesCompleto.clientes().size();

                if(isOnlineNet() && isNetDisponible()){
                    if(numeroClientes>0){

                        AlertDialog.Builder builder= new AlertDialog.Builder(CierreCajaActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.dialog_carga,null);

                        EjAsincTask2 ejAsincTask2= new EjAsincTask2();
                        ejAsincTask2.execute();


                        builder.setView(mView);
                        final AlertDialog alertDialog= builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


                        JSONObject jsonObjectClientes;
                        final JSONArray jsonArray= new JSONArray();

                        ArrayList<ClienteCompleto> clientes= bdClientesCompleto.clientes();
                        for(int j = 0; j<clientes.size(); j++) {

                            final int finalI = j;

                            HashMap<String, String> hashMapCliente = new HashMap<String, String>();
                            hashMapCliente.put("id", ""+clientes.get(finalI).getId());
                            hashMapCliente.put("customer_group_name", "" + clientes.get(finalI).getGrupo());
                            hashMapCliente.put("name", "" + clientes.get(finalI).getNombre());
                            hashMapCliente.put("vat_no", "" + clientes.get(finalI).getCedula());
                            hashMapCliente.put("address", "" + clientes.get(finalI).getDireccion());
                            hashMapCliente.put("city", "" + clientes.get(finalI).getCiudad());
                            hashMapCliente.put("state", "" + clientes.get(finalI).getEstado());
                            hashMapCliente.put("country", "" + clientes.get(finalI).getPais());
                            hashMapCliente.put("phone", "" + clientes.get(finalI).getTelefono());
                            hashMapCliente.put("customer_group_id",""+bdGruposVendedor.
                                    obtenerId(clientes.get(finalI).getGrupo()));
                            hashMapCliente.put("tipo",""+clientes.get(finalI).getTipo());

                            JSONObject jsonObjectProductos= new JSONObject(hashMapCliente);

                            jsonArray.put(jsonObjectProductos);
                        }

                        jsonObjectClientes= new JSONObject();
                        try {
                            jsonObjectClientes.put("clientes",jsonArray);
                            Log.d("ClientesSubir",jsonObjectClientes.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        requestCliente = new JsonObjectRequest(Request.Method.POST,URLClientes,jsonObjectClientes, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.names().get(0).equals("success")) {
                                        bdClientesCompleto.eliminarClientes();
                                        contador++;

                                        if(isNetDisponible() && isOnlineNet()) {
                                            bdClientes.eliminarTodosClientes();
                                            Clientes clientes = new Clientes(getApplicationContext(), link);
                                            clientes.obtenerClientes();

                                        }else{
                                            Toast.makeText(getApplicationContext(),"Verifique la conexión a internet",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }else if(response.names().get(0).equals("error")){
                                        Toast.makeText(getApplicationContext(),response.getString("error"),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.d("errorVenta",error.toString());
                            }
                        });
                        MySingleton.getInstance(context).addToRequestQue(requestCliente);
                    }
                } else{
                    Toast.makeText(getApplicationContext(),"Verifique su conexion a internet",
                            Toast.LENGTH_SHORT).show();
                }


            }else if(v.equals(btnEliminarCliRepetidos)){

                AlertDialog.Builder builder= new AlertDialog.Builder(CierreCajaActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_carga,null);

                EjAsincTask2 ejAsincTask2= new EjAsincTask2();
                ejAsincTask2.execute();


                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

                for(int i=0;i<clientesRepetidos.size();i++){
                    bdClientesCompleto.eliminarAbono(clientesRepetidos.get(i));
                }



            }else if(v.equals(btnVolver)){

                {

                    /**
                     * We will create a personalized alert, we will add
                     * buttons and also their actions, we give the
                     * user a description of what will
                     * happen if he deletes his account, and the user
                     * will choose whether to delete it or not.
                     */

                    AlertDialog.Builder builder= new AlertDialog.Builder(CierreCajaActivity.this);
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


            }

            else if (v.equals(btnSend)) {
                if(contador==0){

                    AlertDialog.Builder builder= new AlertDialog.Builder(CierreCajaActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_carga,null);
                    builder.setView(mView);
                    final AlertDialog alertDialog= builder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                    btnSend.setEnabled(false);
                    btnSearch.setEnabled(false);
                    btnVolver.setEnabled(false);

                    solicitarPermisos();

                    int numeroclientes= bdClientesCompleto.clientes().size();

                    if(numeroclientes==0){

                        if(isNetDisponible() && isOnlineNet()){

                            if(numeroclientes==0 && (TOTAL_GASTOS>0 || TOTAL_VENTAS>0 || DINERO_ABONOS>0 ||
                                    devoluciones.size()>0)){

                                ventas=organizarVentas(ventas);

                                //cantidad productos,total ventas, dinero recibido, dinero abonos, gastos

                                if(TOTAL_GASTOS>0 || TOTAL_VENTAS>0 || DINERO_ABONOS >0 || devoluciones.size()>0){


                                    /**
                                     * Logica que sube todos los datos al servidor
                                     */
                                    ExecutorService executor = Executors.newFixedThreadPool(4);

                                    int warehouseId= ConfiguracionActivity.getPreferenciaWarehouseID(CierreCajaActivity.this);
                                    Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

                                    for(int i=1;i<=4;i++){

                                        // Log.d("ERROR",""+i);
                                        Runnable workerVentas = new SubirServidor(abonos,ventasCompleto,gastos,
                                                devoluciones, getApplicationContext(),i,link,ID,cache,
                                                warehouseId);
                                        //The thread in the thread pool runs.
                                        executor.execute(workerVentas);
                                    }

                                    executor.shutdown();

                                    while (!executor.isTerminated()) {
                                    }


                                    header += centrarCadena(txtTitulo.getText().toString().toUpperCase())+ BREAK;
                                    header += alinearLineas(txtDireccion.getText().toString()) + BREAK;
                                    header += centrarCadena("Republica dominicana")+ BREAK;
                                    header += centrarCadena("Tel: "+txtTelefono.getText().toString())+BREAK ;

                                    header += DIVIDER_DOUBLE + BREAK;


                                    msg += "Fecha:"+ txtFecha.getText().toString()+ BREAK;
                                    msg += "Vendedor:" + txtVendedor.getText().toString() + BREAK;
                                    msg += "Cantidad productos:" +txtCantidadProductos.getText().toString() +BREAK;
                                    msg += "Total por ventas:" +txtTotalVentas.getText().toString()+BREAK;

                                    msg += "Dinero recibido ventas:" + txtDineroRecibidoVentas.getText().toString() + BREAK;
                                    msg += "Dinero recibido abonos: "+ txtDineroRecibidoAbonos.getText().toString() + BREAK;
                                    msg += "Total gastos:" + txtTotalGastos.getText().toString() + BREAK+BREAK;
                                    msg += "Efectivo a entregar:" + txtDineroEntregar.getText().toString() + BREAK;

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

                                    EjAsincTask ejAsincTask= new EjAsincTask();
                                    ejAsincTask.execute();

                                    takeScreenshot();

                                    contador=1;

                                }else{
                                    Toast.makeText(getApplicationContext(),"No tiene datos para cerrar caja."
                                            ,Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                    btnSearch.setEnabled(true);
                                    btnVolver.setEnabled(true);
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"No tiene datos para cerrar caja."
                                        ,Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                btnSearch.setEnabled(true);
                                btnVolver.setEnabled(true);
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Verifique su conexion a internet",
                                    Toast.LENGTH_LONG).show();

                            btnSend.setEnabled(true);
                            btnSearch.setEnabled(true);
                            btnVolver.setEnabled(true);

                            alertDialog.dismiss();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Suba primero los clientes nuevos al servidor",
                                Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        btnSearch.setEnabled(true);
                        btnVolver.setEnabled(true);
                    }


                }
                else if(contador >0){

                    Toast.makeText(getApplicationContext(),"Ya imprimio una vez la factura",Toast.LENGTH_SHORT).show();

                }
            }else if(v.equals(btnImprimirDetallado)){

                AlertDialog.Builder builder= new AlertDialog.Builder(CierreCajaActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();

                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(CierreCajaActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            Toast.makeText(getApplicationContext(),"Cierre de caja detallado",Toast.LENGTH_SHORT).show();

                            header += centrarCadena(txtTitulo.getText().toString().toUpperCase())+ BREAK;
                            header += alinearLineas(txtDireccion.getText().toString()) + BREAK;
                            header += centrarCadena("Republica dominicana")+ BREAK;
                            header += centrarCadena("Tel: "+txtTelefono.getText().toString())+BREAK ;

                            header += DIVIDER_DOUBLE;

                            msg += "Fecha:"+ txtFecha.getText().toString()+ BREAK;
                            msg += "Vendedor:" + txtVendedor.getText().toString() + BREAK;

                            msgProductos += centrarCadena("Productos") +BREAK+ DIVIDER_DOUBLE+BREAK;
                            msgProductos+= "noFac" + " , " + "ced" + " , " + "codPro" + " , " +
                                    "valor" + BREAK;
                            msgProductos+= DIVIDER + BREAK;

                            for(int i=0;i<productosVentas.size();i++){

                                String nroFactura= ""+productosVentas.get(i).getIdVenta();
                                String codProducto= "" + productosVentas.get(i).getCodigoProducto();
                                String valor = "" + (productosVentas.get(i).getPrecioUnitario()*
                                        productosVentas.get(i).getCantidad());
                                String cedula= bdVentas.obtenerCedulaCliente(nroFactura);
                                msgProductos += nroFactura+" , " +cedula + " , " + codProducto+ " , " + valor + BREAK;
                            }

                            msgProductos += "Cantidad productos:" +txtCantidadProductos.getText().toString() +BREAK;
                            msgProductos += "Total por ventas:" +txtTotalVentas.getText().toString()+BREAK;
                            msgProductos += "Dinero recibido ventas:" + txtDineroRecibidoVentas.getText().toString() + BREAK;

                            msgProductos+= DIVIDER_DOUBLE;

                            msgAbonos += centrarCadena("Abonos") + BREAK+DIVIDER_DOUBLE + BREAK;
                            msgAbonos += "nroFactura" + " , " + "cedula" + " , " + "valor" + BREAK;
                            msgAbonos += DIVIDER + BREAK;

                            for(int i=0;i<abonos.size();i++){

                                String referencia= ""+abonos.get(i).getId();
                                String valor = ""+abonos.get(i).getPagoPayment();
                                String cedula= abonos.get(i).getCedCliente();
                                msgAbonos += referencia + " , "+ cedula+ " , " + valor + BREAK;

                            }

                            msgAbonos+= DIVIDER_DOUBLE;

                            msgGastos += centrarCadena("Gastos") + BREAK+ DIVIDER_DOUBLE + BREAK ;
                            msgGastos += "vendedor" + " , " + "descripcion" + " , " + "valor" + BREAK;
                            msgGastos += DIVIDER + BREAK;

                            for(int i=0;i<gastos.size();i++){

                                String vendedor = ""+gastos.get(i).getVendedor();
                                String descripcion= ""+ gastos.get(i).getDescripcion();
                                String valor= ""+ gastos.get(i).getDineroGastado();
                                msgGastos+= vendedor+" , " + descripcion + " , " + valor + BREAK;
                            }

                            msgGastos+= DIVIDER_DOUBLE;

                            msgDatos += "Dinero recibido ventas:" + txtDineroRecibidoVentas.getText().toString() + BREAK;
                            msgDatos += "Dinero recibido abonos: "+ txtDineroRecibidoAbonos.getText().toString() + BREAK;
                            msgDatos += "Total gastos:" + txtTotalGastos.getText().toString() + BREAK+BREAK;
                            msgDatos += "Efectivo a entregar:" + txtDineroEntregar.getText().toString() + BREAK;


                            byte[] cmd = new byte[3];
                            cmd[0] = 0x1b;
                            cmd[1] = 0x21;
                            cmd[2] |= 0x10;
                            mService.write(cmd);
                            mService.sendMessage(header, "GBK");

                            cmd[2] &= 0xEF;
                            mService.write(cmd);
                            mService.sendMessage(msg, "GBK");

                            cmd[2] &= 0xEF;
                            mService.write(cmd);
                            mService.sendMessage(msgProductos, "GBK");

                            cmd[2] &= 0xEF;
                            mService.write(cmd);
                            mService.sendMessage(msgAbonos, "GBK");

                            cmd[2] &= 0xEF;
                            mService.write(cmd);
                            mService.sendMessage(msgGastos, "GBK");

                            cmd[2] &= 0xEF;
                            mService.write(cmd);
                            mService.sendMessage(msgDatos, "GBK");

                            Log.d("facturaCierre", header + "\n" +msg + "\n" + msgProductos + "\n" + msgAbonos + "\n" + msgGastos
                                    + "\n" + msgDatos);

                            header = "";
                            msg = "";
                            msgProductos = "";
                            msgAbonos = "";
                            msgGastos = "";
                            msgDatos = "";

                            alertDialog.dismiss();

                        }
                    }
                });


                alertDialog.show();






            }
        }
    }

    private  ArrayList<Venta> organizarVentas(ArrayList<Venta> ventas) {

        ArrayList<Venta> ventasNew= ventas;

        for(int i=0;i<ventasNew.size();i++){
            if(ventas.get(i).getExiste()==2){
                ventasNew.get(i).setIdCliente(bdClientes.buscarCliente(ventasNew.get(i).getCedulaCliente()));
                ventasNew.get(i).setExiste(1);
                ventasNew.set(i,ventasNew.get(i));
            }
        }

        return ventasNew;

    }

    private ArrayList<VentaCompleta> encontrarVentas(ArrayList<Venta> ventas, ArrayList<ProductoVenta> productosVentas) {

        ArrayList<VentaCompleta> ventasCompl= new ArrayList<>();
        ArrayList<ProductoVenta> productos= new ArrayList<>();

        for(int i=0;i<ventas.size();i++){

            int id= ventas.get(i).getId();
            Venta venta= ventas.get(i);
            productos.clear();

            for(int j=0;j<productosVentas.size();j++){

                if(productosVentas.get(j).getIdVenta()==id){
                    productos.add(productosVentas.get(j));
                }

            }
            VentaCompleta ventaCompleta= new VentaCompleta(venta,productos);
            ventasCompl.add(ventaCompleta);

            // Log.d("pruebitaa","entre aca: "+ventaCompleta.getProductos().size() + ""+ ventaCompleta.getVenta().getCliente());

        }


        return ventasCompl;


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

    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private void segundo(){
        try {

            Thread.sleep(1000);

        }catch (InterruptedException e){

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

            String mPath = Environment.getExternalStorageDirectory().toString() + "/WDPOS/"+ "cierre_"+
                    VENDEDOR+"_"+txtFecha.getText().toString()+".png";


            // create bitmap screen capture
            btnSend.setVisibility(View.INVISIBLE);
            btnVolver.setVisibility(View.INVISIBLE);
            btnSearch.setVisibility(View.INVISIBLE);


            View v1 = findViewById(R.id.linearCierreCaja);
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
    private class EjAsincTask extends AsyncTask<Void,Integer,Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            int contador = 15;

            for (int i = 1; i <= contador; i++) {
                segundo();

                if (isCancelled()) {
                    break;
                }

            }

            return true;

        }




        @Override
        protected void onPostExecute(Boolean booleana) {
            super.onPostExecute(booleana);

            if(booleana){
                finish();
                int cantidadProductos= bdVentas.cantidadProductos();
                int totalVentas= bdVentas.totalVentas();
                int dineroRecibidoVentas=bdVentas.totalVentasRecibido();
                int dineroRecibidoAbonos= bdAbonos.totalAbonos();
                int totalGastos=bdGastos.totalGastos();
                int dineroEntregar= (dineroRecibidoVentas+dineroRecibidoAbonos)-totalGastos;

                Intent intent = new Intent(getApplicationContext(), CierreCajaActivity.class);

                intent.putExtra("key_vendedor",NICKNAME);
                intent.putExtra("key_id",ID);
                intent.putExtra("key_cantidadProductos",cantidadProductos);
                intent.putExtra("key_totalVentas",totalVentas);
                intent.putExtra("key_dineroRecibidoVentas",dineroRecibidoVentas);
                intent.putExtra("key_dineroRecibidoAbonos",dineroRecibidoAbonos);
                intent.putExtra("key_totalGastos",totalGastos);
                intent.putExtra("key_dineroEntregar",dineroEntregar);
                intent.putExtra("key_ciclo","1");


                startActivity(intent);



                btnSend.setEnabled(true);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            Toast.makeText(getApplicationContext(),"tarea cancelada",Toast.LENGTH_SHORT).show();
        }
    }



    private class EjAsincTask2 extends AsyncTask<Void,Integer,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            for (int i = 1; i <= 5; i++) {
                segundo();

                if (isCancelled()) {
                    break;
                }

            }

            return true;

        }




        @Override
        protected void onPostExecute(Boolean booleana) {
            super.onPostExecute(booleana);

            if(booleana){
                finish();
                int cantidadProductos= bdVentas.cantidadProductos();
                int totalVentas= bdVentas.totalVentas();
                int dineroRecibidoVentas=bdVentas.totalVentasRecibido();
                int dineroRecibidoAbonos= bdAbonos.totalAbonos();
                int totalGastos=bdGastos.totalGastos();
                int dineroEntregar= (dineroRecibidoVentas+dineroRecibidoAbonos)-totalGastos;

                Intent intent = new Intent(getApplicationContext(), CierreCajaActivity.class);

                intent.putExtra("key_vendedor",NICKNAME);
                intent.putExtra("key_id",ID);
                intent.putExtra("key_cantidadProductos",cantidadProductos);
                intent.putExtra("key_totalVentas",totalVentas);
                intent.putExtra("key_dineroRecibidoVentas",dineroRecibidoVentas);
                intent.putExtra("key_dineroRecibidoAbonos",dineroRecibidoAbonos);
                intent.putExtra("key_totalGastos",totalGastos);
                intent.putExtra("key_dineroEntregar",dineroEntregar);
                intent.putExtra("key_ciclo","2");


                startActivity(intent);



                btnSend.setEnabled(true);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            Toast.makeText(getApplicationContext(),"tarea cancelada",Toast.LENGTH_SHORT).show();
        }
    }

}
