package swasolutions.com.wdpos.actividades.apartados;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.facturas.FacturaVentaActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.SharedPreferences;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.adaptadores.FacturaAdapter;
import swasolutions.com.wdpos.base_de_datos.CarritoBD;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.CreditoBD;
import swasolutions.com.wdpos.base_de_datos.ProductosVentaBD;
import swasolutions.com.wdpos.base_de_datos.VentasBD;
import swasolutions.com.wdpos.impresion.DeviceListActivity;
import swasolutions.com.wdpos.vo.clases_objeto.ProductoCarrito;

public class ApartadosActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<ProductoCarrito> productos;


    private Button btnSend;
    private  BluetoothService mService = null;

    private EditText txtNota;

    public static CarritoBD bdFactura;
    public static ClientesBD bdClientes;
    public static VentasBD bdVentas;
    public static CreditoBD bdCredito;
    public static ProductosVentaBD bdProductosVenta;

    static final String TAG = "ApartadosActivity";
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    private String NOMBRE;
    private int PAGO;
    private int CREDITO;
    private String CEDULACLIENTE;
    private String TIPOPAGO;

    private Context context;
    private String fecha;
    private String referencia;
    private String idCliente;
    private int total;
    public int deuda;
    private int cantidadProductos;
    private int existe;
    private int id;
    private int idVendedor;
    private String estadoVenta;
    private String nota;

    private int contador;


    private  String msg = "";
    private String msgDatos= "";
    private String header= "";
    private String msgProductos= "";
    private String DIVIDER = "--------------------------------";
    private String DIVIDER_DOUBLE = "================================";
    private String BREAK = "\r\n";
    private String SPACE4 = "     ";

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
                            Log.d(TAG, "handleMessage: Apartados activity");
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
                    Toast.makeText(getApplicationContext(),"No presiono nada",Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartados);

        productos= new ArrayList<>();
        context= ApartadosActivity.this;
        contador=0;

        mService = new BluetoothService(this, mHandler);

        if (!mService.isAvailable()) {
            Toast.makeText(this, "Bluetooth no está disponible", Toast.LENGTH_LONG).show();
            finish();
        }


        txtNota= (EditText) findViewById(R.id.txtNotaApartado_apartado);
        Button btnSearch= (Button) findViewById(R.id.btnSearch_apartados);
        Button btnVolver= (Button) findViewById(R.id.btnVolver_apartados);
        btnSend= (Button) findViewById(R.id.btnSend_apartados);
        btnSend.setEnabled(false);

        btnSearch.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnVolver.setOnClickListener(this);

        if(!(SharedPreferences.getPreferenciaImpresion(ApartadosActivity.this))){
            btnSend.setEnabled(true);
        }

        bdFactura= new CarritoBD(getApplicationContext(),null,1);
        bdClientes= new ClientesBD(getApplicationContext(),null,1);
        bdVentas= new VentasBD(getApplicationContext(),null,1);
        bdCredito= new CreditoBD(context,null,1);
        bdProductosVenta= new ProductosVentaBD(getApplicationContext(),null,1);

        productos= bdFactura.cargarProductosCarrito();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewApartado);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        FacturaAdapter adapter = new FacturaAdapter(productos);
        recyclerView.setAdapter(adapter);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null) {
            NOMBRE = bundle.getString("key_nombreCliente");
            PAGO= Integer.parseInt(bundle.getString("key_pago"));
            CEDULACLIENTE = bundle.getString("key_cedula");
            TIPOPAGO= bundle.getString("key_tipoPago");
            CREDITO= bundle.getInt("key_credito");
        }


        fecha = (DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date()).toString());
        referencia= fecha+ "/"+ NOMBRE;
        idCliente= bdClientes.buscarCliente(CEDULACLIENTE);
        total= calcularTotal(productos);
        deuda= (total-PAGO);
        cantidadProductos= FacturaVentaActivity.calcularCantidadProductos(productos);
        existe=3; //es 3 ya que asi llegara al servidor para saber que es un apartado
        id=bdVentas.contarFilas();
        idVendedor= Integer.parseInt(LoginActivity.getId(ApartadosActivity.this));
        estadoVenta= "partial";



    }

    private int calcularTotal(ArrayList<ProductoCarrito> productos) {

        int total=0;

        for(int i =0;i<productos.size();i++){

            total+= (productos.get(i).getPrecio())*(productos.get(i).getCantidad());

        }
        return total;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;

        if(contador==1){
            bdFactura.eliminarProductosCarrito();
        }

        bdVentas.close();
        bdFactura.close();
        bdClientes.close();
        bdProductosVenta.close();
    }

    @Override
    public void finish() {
        super.finish();
        bdVentas.close();
        bdFactura.close();
        bdClientes.close();
        bdProductosVenta.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bdVentas.close();
        bdFactura.close();
        bdClientes.close();
        bdProductosVenta.close();
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
                Log.d(TAG, "onActivityResult: entre");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnSearch_apartados:

                Intent serverIntent = new Intent(context, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                //btnSend.setEnabled(true);
                //btnSendCopia.setEnabled(true);

                break;
            case R.id.btnVolver_apartados:

                /**
                 * We will create a personalized alert, we will add
                 * buttons and also their actions, we give the
                 * user a description of what will
                 * happen if he deletes his account, and the user
                 * will choose whether to delete it or not.
                 */

                AlertDialog.Builder builder= new AlertDialog.Builder(ApartadosActivity.this);
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

                break;
            case R.id.btnSend_apartados:

                /*
        Codigo que ira dentro de la accion del boton luego se imprimira
         */

                if(contador==0){
                    nota= txtNota.getText().toString();

                    bdVentas.agregarVenta(id,fecha,referencia,idVendedor,CEDULACLIENTE,NOMBRE,total,
                            estadoVenta,PAGO,cantidadProductos,idCliente,ApartadosActivity.this,existe,
                            nota,CREDITO);

                    if(CREDITO>0){
                        if(bdCredito.buscarCliente(CEDULACLIENTE)){
                            bdCredito.descontarCredito(CEDULACLIENTE,CREDITO,context);
                        }
                    }

                    for(int j=0;j<productos.size();j++){

                        int idProducto= productos.get(j).getId();
                        String codigoProducto= productos.get(j).getCodigoProducto();
                        String nombreProducto= productos.get(j).getNombre();
                        int precioUnitario= productos.get(j).getPrecio();
                        int cantidad= productos.get(j).getCantidad();
                        bdProductosVenta.crearProductoVenta(id,idProducto,codigoProducto,nombreProducto,precioUnitario,
                                cantidad,idVendedor);
                    }

                    Toast.makeText(getApplicationContext(),"Venta agregada correctamente",Toast.LENGTH_SHORT).show();

                    contador=1;

                    imprimir();
                }else{

                    /**
                     * We will create a personalized alert, we will add
                     * buttons and also their actions, we give the
                     * user a description of what will
                     * happen if he deletes his account, and the user
                     * will choose whether to delete it or not.
                     */

                    AlertDialog.Builder builder2= new AlertDialog.Builder(ApartadosActivity.this);
                    builder2.setIcon(R.drawable.logo);
                    builder2.setTitle("Esta seguro?");
                    builder2.setMessage("Desea volver a imprimir ya guardo la venta");
                    builder2.setCancelable(false);

                    /**
                     * Action of the button
                     */
                    builder2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            imprimir();


                        }
                    });
                    /**
                     * Action of the button
                     */
                    builder2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog2= builder2.create();
                    alertDialog2.show();

                }


                break;




        }
    }

    private void imprimir() {

        String titulo= ConfiguracionActivity.getNombreTienda(ApartadosActivity.this);
        String direccion= ConfiguracionActivity.getDireccionTienda(ApartadosActivity.this);
        String telefono= ConfiguracionActivity.getTelefonoTienda(ApartadosActivity.this);
        header += centrarCadena(titulo.toUpperCase()) + BREAK;
        header += alinearLineas(direccion) + BREAK;
        header += centrarCadena("Republica dominicana") + BREAK;
        header += centrarCadena("Tel: " + telefono) + BREAK;

        header += DIVIDER_DOUBLE + BREAK;


        msgDatos += "Fecha:" + fecha + BREAK;
        msgDatos += "Id venta:" + referencia + BREAK;
        msgDatos += "Vendedor:" + LoginActivity.getUserName(ApartadosActivity.this) + BREAK;
        msgDatos += "Cliente:" + NOMBRE + BREAK;
        msgDatos += "Estado:" + TIPOPAGO + BREAK;
        msgDatos += DIVIDER + BREAK;
        msgDatos += "cantidad" + SPACE4 + "Producto" + SPACE4 + "Total" + BREAK;
        msgDatos += DIVIDER + BREAK;


        if(nota.length()>0) {
            msg += "Notas:" + BREAK + nota +BREAK;
        }

        msg += "Total:" + total + BREAK;
        msg += "Total Pagado: " + PAGO + BREAK;
        msg += "Total a deber: " + (total-PAGO) + BREAK + BREAK;





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

            if (productos.get(i).getNombre().length() > 17) {

                String nombre = productos.get(i).getNombre();
                String linea1 = nombre;
                String linea2 = "";

                for (int j = 17; j > 1; j--) {
                    if (linea1.charAt(j) == ' ') {
                        linea1 = nombre.substring(0, j);
                        linea2 = nombre.substring(j, nombre.length());
                        break;
                    }
                }

                String cadenaInicio = "" + productos.get(i).getCantidad();
                String cadenaMedia = cadenaInicio + linea1;
                msgProductos += "" + productos.get(i).getCantidad() + contarEspaciosInicio(cadenaInicio) +
                        linea1 + contarEspacios(cadenaMedia) +
                        (productos.get(i).getPrecio() * productos.get(i).getCantidad()) + BREAK;
                msgProductos += contarEspaciosInicio(cadenaInicio) + linea2 + BREAK;


            } else {

                String cadenaInicio = "" + productos.get(i).getCantidad();
                String cadenaMedia = cadenaInicio + productos.get(i).getNombre().toString();
                msgProductos += "" + productos.get(i).getCantidad() + contarEspaciosInicio(cadenaInicio) +
                        productos.get(i).getNombre().toString() + contarEspacios(cadenaMedia) +
                        (productos.get(i).getPrecio() * productos.get(i).getCantidad()) + BREAK;

            }


        }
        //cmd[2] |= 0x01;
        cmd[2] &= 0xEF;
        mService.write(cmd);
        mService.sendMessage(msgProductos, "GBK");

        String raya = DIVIDER_DOUBLE + BREAK;
        cmd[2] &= 0xEF;
        mService.write(cmd);
        mService.sendMessage(raya, "GBK");

        cmd[2] &= 0xEF;
        mService.write(cmd);
        mService.sendMessage(msg, "GBK");


        Log.d("apartadosss", header + "\n" + msgDatos + "\n" + msgProductos + "\n" + msg);



        msg = "";
        msgProductos = "";
        msgDatos = "";
        header = "";

        Toast.makeText(getApplicationContext(),"Impresion completa",Toast.LENGTH_SHORT).show();

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

    private String contarEspacios(String cadena) {


        String espacios="";
        if(cadena.length()<23){

            int falta= (cadena.length()-24)*(-1);
            for(int i=0;i<falta;i++){
                espacios+=" ";
            }

        }

        return espacios;

    }

    private String contarEspaciosInicio(String cadena) {


        String espacios="";
        if(cadena.length()<4){
            int falta= (cadena.length()-4)*(-1);
            for(int i=0;i<falta;i++){
                espacios+=" ";
            }
        }

        return espacios;

    }
}
