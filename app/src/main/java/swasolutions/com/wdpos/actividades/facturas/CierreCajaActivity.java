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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.zj.btsdk.BluetoothService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;
import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.SharedPreferences;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.base_de_datos.AbonosBD;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.ClientesCompletoBD;
import swasolutions.com.wdpos.base_de_datos.DevolucionesBD;
import swasolutions.com.wdpos.base_de_datos.GastosBD;
import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosVentaBD;
import swasolutions.com.wdpos.base_de_datos.VentasBD;
import swasolutions.com.wdpos.impresion.DeviceListActivity;
import swasolutions.com.wdpos.logica.Logica;
import swasolutions.com.wdpos.vo.clases_objeto.Abono;
import swasolutions.com.wdpos.vo.clases_objeto.Cliente;
import swasolutions.com.wdpos.vo.clases_objeto.ClienteCompleto;
import swasolutions.com.wdpos.vo.clases_objeto.Devolucion;
import swasolutions.com.wdpos.vo.clases_objeto.Gasto;
import swasolutions.com.wdpos.vo.clases_objeto.Producto;
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

    private TextView txtFecha,txtVendedor;

    private TextView txtTitulo,txtDireccion,txtTelefono;
    private Logica logica;

    /**
     * Datos necesarios para la impresion
     */

    private static final String TAG = "CierreCajaActivity";
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;


    private Button btnSearch;
    private Button btnSend;
    private Button btnVolver;
    private Button btnImprimirDetallado;
    private Button btnSubirClientes;
    private Button btnEliminarCliRepetidos;
    private Button btnEditarProductos;
    private BluetoothService mService = null;
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
    public static ProductosBD bdProductos;

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

        logica= new Logica();

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
        }
    }

    @SuppressLint("SetTextI18n")
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
        btnEditarProductos= (Button) findViewById(R.id.btnEditarProductos_cierreCaja);

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


        TextView txtNumeroClientesNuevos= (TextView) findViewById(R.id.txtClientesNuevos_cierreCaja);
        TextView txtProductosDevolver= (TextView) findViewById(R.id.txtProductosDevolver_cierreCaja);
        TextView txtProductosEditados= (TextView) findViewById(R.id.txtProductosEditados_cierreCaja);



        /*
         * Instancia de las bases de datos locales
         */

        bdGastos= new GastosBD(getApplicationContext(),null,1);
        bdAbonos= new AbonosBD(getApplicationContext(),null,1);
        bdVentas= new VentasBD(getApplicationContext(),null,1);
        bdProductosVenta= new ProductosVentaBD(getApplicationContext(),null,1);
        bdProductos= new ProductosBD(getApplicationContext(),null,1);
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

        txtProductosEditados.setText(""+bdProductos.productosEditados());

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
        btnEditarProductos.setOnClickListener(new ClickEvent());
        btnSend.setEnabled(false);
        btnEditarProductos.setEnabled(false);
        btnImprimirDetallado.setEnabled(false);
        btnEliminarCliRepetidos.setEnabled(false);

        if(!(SharedPreferences.getPreferenciaImpresion(CierreCajaActivity.this))){
            btnSend.setEnabled(true);
            btnImprimirDetallado.setEnabled(true);
        }

        /*
        if(validarClientesRepetidos(bdClientes.cargarClientes(),bdClientesCompleto.clientes())){
            btnEliminarCliRepetidos.setEnabled(true);
        }
        */

        int productosEditados= Integer.parseInt(txtProductosEditados.getText().toString());

        if(productosEditados>0){
            btnEditarProductos.setEnabled(true);
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


        ventas= bdVentas.ventas();
        abonos= bdAbonos.abonos();
        gastos=bdGastos.gastos();
        productosVentas= bdProductosVenta.productosVenta();
        //ventasCompleto= encontrarVentas(ventas,productosVentas);

    }


    private void validarClientesRepetidos(ArrayList<Cliente> clientes, ArrayList<ClienteCompleto> clientesNuevos) {

        for(int i=0;i<clientesNuevos.size();i++){
            for(int j=0;j<clientes.size();j++){
                if(clientesNuevos.get(i).getCedula().equals(clientes.get(j).getCedula())){
                    clientesRepetidos.add(clientesNuevos.get(i).getId());
                    Log.d("ClientesRepetidos", "validarClientesRepetidos: "+clientesNuevos.get(i).getId());
                }
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

    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v.equals(btnSearch)) {
                Intent serverIntent = new Intent(context, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

            }else if(v.equals(btnSubirClientes)){

                String URLClientes = link + "/app_movil/vendedor/registrarClienteTest.php";
                //Log.d("registroCliente", "" + clientes.size());

                int numeroClientes= bdClientesCompleto.clientes().size();

                if(logica.verificarConexion(CierreCajaActivity.this)){
                    if(numeroClientes>0){

                        AlertDialog.Builder builder= new AlertDialog.Builder(CierreCajaActivity.this);
                        @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.dialog_carga,null);

                        final EjAsincTask2 ejAsincTask2= new EjAsincTask2();
                        ejAsincTask2.execute();


                        builder.setView(mView);
                        final AlertDialog alertDialog= builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


                        JSONObject jsonObjectClientes;
                        final JSONArray jsonArray= new JSONArray();

                        ArrayList<ClienteCompleto> clientes= bdClientesCompleto.clientes();
                        for(int j = 0; j<clientes.size(); j++) {

                            HashMap<String, String> hashMapCliente = new HashMap<>();
                            hashMapCliente.put("id", ""+clientes.get(j).getId());
                            hashMapCliente.put("customer_group_name", "" + clientes.get(j).getGrupo());
                            hashMapCliente.put("name", "" + clientes.get(j).getNombre());
                            hashMapCliente.put("vat_no", "" + clientes.get(j).getCedula());
                            hashMapCliente.put("address", "" + clientes.get(j).getDireccion());
                            hashMapCliente.put("city", "" + clientes.get(j).getCiudad());
                            hashMapCliente.put("state", "" + clientes.get(j).getEstado());
                            hashMapCliente.put("country", "" + clientes.get(j).getPais());
                            hashMapCliente.put("phone", "" + clientes.get(j).getTelefono());
                            hashMapCliente.put("customer_group_id",""+bdGruposVendedor.
                                    obtenerId(clientes.get(j).getGrupo()));
                            hashMapCliente.put("tipo",""+clientes.get(j).getTipo());

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

                        JsonObjectRequest requestCliente = new JsonObjectRequest(Request.Method.POST,URLClientes,jsonObjectClientes, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.names().get(0).equals("success")) {

                                        Toast.makeText(getApplicationContext(),response.getString("success"),Toast.LENGTH_SHORT).show();

                                        bdClientesCompleto.eliminarClientes();
                                        contador++;

                                        if(logica.verificarConexion(CierreCajaActivity.this)){
                                            bdClientes.eliminarTodosClientes();
                                            Clientes clientes = new Clientes(getApplicationContext(), link);
                                            clientes.obtenerClientes();
                                        }else{
                                            Toast.makeText(getApplicationContext(),"Verifique la conexión a internet",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }else if(response.names().get(0).equals("error")){
                                        Toast.makeText(getApplicationContext(),response.getString("error")+
                                                        " Espera un momento.....", Toast.LENGTH_SHORT).show();

                                        bdClientes.eliminarTodosClientes();
                                        Clientes clientes = new Clientes(getApplicationContext(), link);
                                        clientes.obtenerClientes();

                                        ejAsincTask2.cancel(true);
                                        alertDialog.dismiss();

                                        btnEliminarCliRepetidos.setEnabled(true);
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


            }else if(v.equals(btnEditarProductos)){

                if(logica.verificarConexion(CierreCajaActivity.this)){

                    String URL_SUBIR= link+"/app_movil/vendedor/editarProductos.php";

                    AlertDialog.Builder builder= new AlertDialog.Builder(CierreCajaActivity.this);
                    @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.dialog_carga,null);

                    final EjAsincTask2 ejAsincTask2= new EjAsincTask2();
                    ejAsincTask2.execute();

                    builder.setView(mView);
                    final AlertDialog alertDialog= builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();

                    JSONObject jsonObjectProductos;
                    final JSONArray jsonArray= new JSONArray();

                    ArrayList<Producto> productos= bdProductos.listaProductosEditados();
                    for(int j = 0; j<productos.size(); j++) {

                        HashMap<String, String> hashMapCliente = new HashMap<>();
                        hashMapCliente.put("id", ""+productos.get(j).getId());
                        hashMapCliente.put("code", ""+ productos.get(j).getCodigoProducto());
                        hashMapCliente.put("nombre",""+  productos.get(j).getNombre());
                        hashMapCliente.put("unit", "" + productos.get(j).getUnit());
                        hashMapCliente.put("cost",""+  productos.get(j).getCost());
                        hashMapCliente.put("price", ""+ productos.get(j).getPrecio1());
                        hashMapCliente.put("price2", "" + productos.get(j).getPrecio2());
                        hashMapCliente.put("price3", "" + productos.get(j).getPrecio3());
                        hashMapCliente.put("price4", "" + productos.get(j).getPrecio4());
                        hashMapCliente.put("price5", "" + productos.get(j).getPrecio5());
                        hashMapCliente.put("price6", "" + productos.get(j).getPrecio6());
                        hashMapCliente.put("categoryId",""+ productos.get(j).getCategoryId());
                        hashMapCliente.put("type", ""+ productos.get(j).getType());

                        JSONObject jsonObjectProducto= new JSONObject(hashMapCliente);

                        jsonArray.put(jsonObjectProducto);
                    }

                    jsonObjectProductos= new JSONObject();
                    try {
                        jsonObjectProductos.put("productos",jsonArray);
                        Log.d("ClientesSubir",jsonObjectProductos.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("Ventasssssssss",jsonObjectProductos.toString());

                    JsonObjectRequest requestProductos = new JsonObjectRequest(Request.Method.POST,URL_SUBIR,jsonObjectProductos, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.names().get(0).equals("success")) {

                                    Toast.makeText(getApplicationContext(), "Productos editados correctamente..",
                                            Toast.LENGTH_LONG).show();

                                    bdProductos.actualizarProductosEditados();

                                } else {
                                    Toast.makeText(context, "Error" + response.getString("error"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("errorVenta",error.toString());


                            Toast.makeText(getApplicationContext(), "Error productos no fueron editados..",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    MySingleton.getInstance(context).addToRequestQue(requestProductos);

                }

            }else if(v.equals(btnEliminarCliRepetidos)){


                AlertDialog.Builder builder= new AlertDialog.Builder(CierreCajaActivity.this);
                @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.dialog_carga,null);

                EjAsincTask2 ejAsincTask2= new EjAsincTask2();
                ejAsincTask2.execute();

                validarClientesRepetidos(bdClientes.cargarClientes(),bdClientesCompleto.clientes());

                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

                for(int i=0;i<clientesRepetidos.size();i++){

                    String cedula=bdClientesCompleto.buscarCedula(clientesRepetidos.get(i));

                    bdVentas.actualizarIdCliente(clientesRepetidos.get(i),bdClientes.buscarCliente(cedula),
                            bdClientes.buscarNombre(cedula));
                    bdClientesCompleto.eliminarAbono(clientesRepetidos.get(i));
                }

                ventas= bdVentas.ventas();





            }else if(v.equals(btnVolver)){

                /*
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

                /*
                 * Action of the button
                 */
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                /*
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

            else if (v.equals(btnSend)) {
                if(contador==0){

                    AlertDialog.Builder builder= new AlertDialog.Builder(CierreCajaActivity.this);
                    @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.dialog_carga,null);
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

                        if(logica.verificarConexion(CierreCajaActivity.this)){

                            if(bdClientes.cargarClientes().size()==0){

                                alertDialog.dismiss();

                                Clientes clientes= new Clientes(context,link);
                                clientes.obtenerClientes();


                                Snackbar.make(findViewById(android.R.id.content), "actualizando clientes," +
                                        " espere un momento", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                btnSearch.setEnabled(true);
                                btnSend.setEnabled(true);
                                btnImprimirDetallado.setEnabled(true);
                                btnVolver.setEnabled(true);

                            }else{
                                if(TOTAL_GASTOS > 0 || TOTAL_VENTAS > 0 || DINERO_ABONOS > 0 || devoluciones.size() > 0){

                                    ventas=organizarVentas(ventas);

                                    //cantidad productos,total ventas, dinero recibido, dinero abonos, gastos

                                    if(TOTAL_GASTOS>0 || TOTAL_VENTAS>0 || DINERO_ABONOS >0 || devoluciones.size()>0){


                                        String URLVerificar = "http://wds.grupowebdo.com/app_movil/macaddress/verificarMacAddress.php";
                                        StringRequest requestLogin = new StringRequest(Request.Method.POST, URLVerificar, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {

                                                    JSONObject jsonObject = new JSONObject(response);

                                                    if (jsonObject.names().get(0).equals("success")) {

                                                /*
                                                 * Logica que sube todos los datos al servidor
                                                 */
                                                        ExecutorService executor = Executors.newFixedThreadPool(4);

                                                        int warehouseId= ConfiguracionActivity.getPreferenciaWarehouseID(CierreCajaActivity.this);
                                                        //Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

                                                        for(int i=1;i<=4;i++){

                                                            // Log.d("ERROR",""+i);
                                                            Runnable workerVentas = new SubirServidor(abonos,ventas,gastos,
                                                                    devoluciones, getApplicationContext(),i,link,ID,
                                                                    warehouseId);
                                                            //The thread in the thread pool runs.
                                                            executor.execute(workerVentas);
                                                        }

                                                        executor.shutdown();

                                                        while (true) {
                                                            if (executor.isTerminated()) break;
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
                                                        mService.sendMessage(header, "UTF-8");


                                                        cmd[2] &= 0xEF;
                                                        mService.write(cmd);
                                                        mService.sendMessage(msg, "UTF-8");

                                                        header="";
                                                        msg="";

                                                        EjAsincTask ejAsincTask= new EjAsincTask();
                                                        ejAsincTask.execute();

                                                        takeScreenshot();

                                                        contador=1;

                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Error: " + "Dispositivo no registrado", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                Toast.makeText(getApplicationContext(), "Verifique su conexión a internet", Toast.LENGTH_SHORT).show();
                                                Log.d("error", "" + error);
                                                alertDialog.dismiss();

                                            }
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                HashMap<String, String> hashMap = new HashMap<String, String>();

                                                Logica logica= new Logica();

                                                hashMap.put("link", link);
                                                hashMap.put("mac_address",logica.getMacAddr());
                                                return hashMap;
                                            }
                                        };

                                        MySingleton.getInstance(getApplicationContext()).addToRequestQue(requestLogin);

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
                @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();

                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(Integer.parseInt(txtContrasenia.getText().toString()) !=
                                ConfiguracionActivity.getPreferenciaPing(CierreCajaActivity.this)){
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
                            mService.sendMessage(header, "UTF-8");

                            cmd[2] &= 0xEF;
                            mService.write(cmd);
                            mService.sendMessage(msg, "UTF-8");

                            cmd[2] &= 0xEF;
                            mService.write(cmd);
                            mService.sendMessage(msgProductos, "UTF-8");

                            cmd[2] &= 0xEF;
                            mService.write(cmd);
                            mService.sendMessage(msgAbonos, "UTF-8");

                            cmd[2] &= 0xEF;
                            mService.write(cmd);
                            mService.sendMessage(msgGastos, "UTF-8");

                            cmd[2] &= 0xEF;
                            mService.write(cmd);
                            mService.sendMessage(msgDatos, "UTF-8");

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

        for(int i = 0; i< ventas.size(); i++){
            if(ventas.get(i).getExiste()==2){
                ventas.get(i).setIdCliente(bdClientes.buscarCliente(ventas.get(i).getCedulaCliente()));
                ventas.get(i).setExiste(1);
                ventas.set(i, ventas.get(i));
            }
        }
        return ventas;

    }

    /*
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

    */

    private String centrarCadena(String cadena) {


        String espaciosInicio="";
        String espaciosFinal="";
        String cadenaFinal;

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

        String linea1= cadena;
        String linea2="";
        String resultado;

        if(cadena.length()>32){



            for(int j=32;j>1;j--){

                if(linea1.charAt(j)==' '){
                    linea1= cadena.substring(0,j);
                    linea2= cadena.substring(j, cadena.length());
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
                    BluetoothDevice con_dev = mService.getDevByMac(address);
                    mService.connect(con_dev);
                }
                break;
        }
    }


    private void segundo(){
        try {

            Thread.sleep(1000);

        }catch (InterruptedException e){
            Log.d(TAG, "segundo: se pauso");
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
    @SuppressLint("StaticFieldLeak")
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

    @SuppressLint("StaticFieldLeak")
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
