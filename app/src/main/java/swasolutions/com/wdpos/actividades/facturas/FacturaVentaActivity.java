package swasolutions.com.wdpos.actividades.facturas;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.ArrayList;
import java.util.List;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.SharedPreferences;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.adaptadores.FacturaAdapter;
import swasolutions.com.wdpos.base_de_datos.CarritoBD;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.ClientesCompletoBD;
import swasolutions.com.wdpos.base_de_datos.CreditoBD;
import swasolutions.com.wdpos.base_de_datos.DeudasBD;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosVentaBD;
import swasolutions.com.wdpos.base_de_datos.VentasBD;
import swasolutions.com.wdpos.impresion.DeviceListActivity;
import swasolutions.com.wdpos.vo.clases_objeto.ProductoCarrito;

public class FacturaVentaActivity extends AppCompatActivity{


    /**
     * Productos de la factura
     */
    private ArrayList<ProductoCarrito> productos;

    private static TextView txtTotalFactura;

    private TextView txtNombreCliente,txtFecha,txtIdVenta,txtPagado,txtDeuda,txtEstado;
    private Button btnVolver;

    private TextView txtTitulo,txtDireccion,txtTelefono;

    private String NOMBRE;
    private int PAGO;
    private int CREDITO;
    private String CEDULACLIENTE;
    private String TIPOPAGO;


    /**
     * Atributos de la base de datos de ventas y productos de venta.
     */
    private VentasBD bdVentas;
    private CarritoBD bdFactura;
    private ProductosVentaBD bdProductosVenta;
    private DeudasBD bdDeudas;
    private ProductosBD bdProductos;
    private ClientesBD bdClientes;
    private ClientesCompletoBD bdClientesCompletos;
    private CreditoBD bdCredito;

    /**
     * Datos necesarios para la impresion
     */

    private static final String TAG = "FacturaVentaActivity";
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    private Button btnSearch;
    private Button btnSend;
    private Button btnSendCopia;
    private BluetoothService mService = null;
    private BluetoothDevice con_dev = null;
    private Context context;
    private int contador;

    private String msg = "";
    private String msgDatos= "";
    private String header= "";
    private String raya="";
    private String msgProductos= "";
    private String DIVIDER = "--------------------------------";
    private String DIVIDER_DOUBLE = "================================";
    private String BREAK = "\r\n";
    private String SPACE4 = "     ";

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
                        default:
                            Log.d(TAG, "handleMessage: dentro");
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
                default:
                    Log.d(TAG, "handleMessage: facturaVenta");
                    break;
            }
        }

    };

    /**
     * On create metodo principal de la actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);
        context = this;

        mService = new BluetoothService(this, mHandler);

        if (!mService.isAvailable()) {
            Toast.makeText(this, "Bluetooth no está disponible", Toast.LENGTH_LONG).show();
            finish();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onStart() {
        super.onStart();

        if (!mService.isBTopen()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        txtTitulo= (TextView) findViewById(R.id.txtNombreTienda_factura);
        txtDireccion= (TextView) findViewById(R.id.txtDireccion_factura);
        txtTelefono= (TextView) findViewById(R.id.txtTelefono_factura);
        TextView txtVendedor= (TextView) findViewById(R.id.txtVendedor_factura);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSendCopia= (Button) findViewById(R.id.btnSendCopia_factura);
        btnVolver= (Button) findViewById(R.id.btnVolver_factura);

        txtTotalFactura = (TextView) findViewById(R.id.txtTotal_factura);
        txtNombreCliente= (TextView) findViewById(R.id.txtNombreCliente_Factura);
        txtFecha= (TextView) findViewById(R.id.txtFecha_factura);
        txtIdVenta= (TextView) findViewById(R.id.txtIdVenta_factura);
        txtPagado= (TextView) findViewById(R.id.txtTotalPagado_Factura);
        txtDeuda = (TextView) findViewById(R.id.txtTotalDebido_Factura);
        txtEstado = (TextView) findViewById(R.id.txtEstado_Factura);

        btnSearch.setOnClickListener(new ClickEvent());
        btnSend.setOnClickListener(new ClickEvent());
        btnVolver.setOnClickListener(new ClickEvent());
        btnSend.setEnabled(false);
        btnSendCopia.setOnClickListener(new ClickEvent());
        btnSendCopia.setEnabled(false);

        if(!(SharedPreferences.getPreferenciaImpresion(FacturaVentaActivity.this))){
            btnSend.setEnabled(true);
            btnSendCopia.setEnabled(true);
        }

        productos= new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if(bundle!=null) {
            NOMBRE = bundle.getString("key_nombreCliente");
            PAGO= Integer.parseInt(bundle.getString("key_pago"));
            CEDULACLIENTE = bundle.getString("key_cedula");
            TIPOPAGO= bundle.getString("key_tipoPago");
            CREDITO= bundle.getInt("key_credito");

            //Toast.makeText(getApplicationContext(),""+TIPOPAGO,Toast.LENGTH_SHORT).show();
        }


        txtEstado.setText(TIPOPAGO);



        /**
         * Inicializacion de las variables de base de datos.
         */
        bdFactura= new CarritoBD(getApplicationContext(),null,1);
        bdVentas= new VentasBD(getApplicationContext(),null,1);
        bdProductosVenta= new ProductosVentaBD(getApplicationContext(),null,1);
        bdDeudas= new DeudasBD(getApplicationContext(),null,1);
        bdClientes= new ClientesBD(getApplicationContext(),null,1);
        bdClientesCompletos= new ClientesCompletoBD(getApplicationContext(),null,1);
        bdProductos= new ProductosBD(getApplicationContext(),null,1);
        bdCredito= new CreditoBD(context,null,1);

        //---------------

        productos= bdFactura.cargarProductosCarrito();

        int total=calcularTotal(productos,TIPOPAGO);

        txtPagado.setText(""+PAGO);
        txtDeuda.setText(""+(total-PAGO));

        txtVendedor.setText(LoginActivity.getUserName(FacturaVentaActivity.this));
        txtNombreCliente.setText(NOMBRE);

        txtTitulo.setText(ConfiguracionActivity.getNombreTienda(FacturaVentaActivity.this));
        txtDireccion.setText(ConfiguracionActivity.getDireccionTienda(FacturaVentaActivity.this));
        txtTelefono.setText(ConfiguracionActivity.getTelefonoTienda(FacturaVentaActivity.this));

        String date = (DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date()).toString());

        String id= date+ "/"+ NOMBRE;

        txtFecha.setText(date);
        txtIdVenta.setText(id);

        bdFactura.close();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFactura);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        FacturaAdapter adapter = new FacturaAdapter(productos,TIPOPAGO);
        recyclerView.setAdapter(adapter);
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
    }

    public static int calcularCantidadProductos(ArrayList<ProductoCarrito> productos) {

        int cantidad=0;

        for(int i=0;i<productos.size();i++){
            cantidad+= productos.get(i).getCantidad();
        }

        return cantidad;

    }


    public int calcularTotal(List<ProductoCarrito> productos, String tipo){

        int total=0;

        for(int i =0;i<productos.size();i++){

            if("Credito".equals(tipo)){
                if(productos.get(i).getPrecio()>=productos.get(i).getPrecio2()){
                    total+= (productos.get(i).getPrecio())*(productos.get(i).getCantidad());
                }else if(productos.get(i).getPrecio2()>=productos.get(i).getPrecio()){
                    total+= (productos.get(i).getPrecio2())*(productos.get(i).getCantidad());
                }
            }else if("Contado".equals(tipo)){

                Toast.makeText(getApplicationContext(),""+productos.size(),Toast.LENGTH_SHORT).show();
                if(productos.get(i).getPrecio()>=productos.get(i).getPrecio2()){
                    total+= (productos.get(i).getPrecio2())*(productos.get(i).getCantidad());
                }else if(productos.get(i).getPrecio2()>=productos.get(i).getPrecio()){
                    total+= (productos.get(i).getPrecio())*(productos.get(i).getCantidad());
                }
                Toast.makeText(getApplicationContext(),""+total,Toast.LENGTH_SHORT).show();
            }


        }

        txtTotalFactura.setText(""+total);
        return total;
    }

    @Override
    public void finish() {
        super.finish();
        bdVentas.close();
        bdFactura.close();
        SharedPreferences.guardarPreferenciaNumeroIntentos(FacturaVentaActivity.this,0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bdVentas.close();
        bdFactura.close();
    }

    class ClickEvent implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.FROYO)
        public void onClick(View v) {
            if (v.equals(btnSearch)) {
                Intent serverIntent = new Intent(context, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

                //btnSendCopia.setEnabled(true);

            }else if(v.equals(btnVolver)){
                {
                    /**
                     * We will create a personalized alert, we will add
                     * buttons and also their actions, we give the
                     * user a description of what will
                     * happen if he deletes his account, and the user
                     * will choose whether to delete it or not.
                     */

                    AlertDialog.Builder builder= new AlertDialog.Builder(FacturaVentaActivity.this);
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

                if(!bdVentas.existeRegistro(txtIdVenta.getText().toString())){



                    //for(int j=1;j<clientes.size();j++){

                    int id=bdVentas.contarFilas();
                    String cedulaCliente= CEDULACLIENTE;
                    String fecha= txtFecha.getText().toString();
                    String referencia= txtIdVenta.getText().toString();

                    /**
                     * Trozo de codigo que asegura si un cliente es nuevo o ya estaba
                     * si es 1 ya estaba agregado si es 2 fue agregado recientemente
                     */
                    int existe=1;
                    if(bdClientesCompletos.existeClienteCedula(cedulaCliente)){
                        existe=2;
                    }

                    String idCliente="";
                    String cliente= txtNombreCliente.getText().toString();
                    if(existe==1){
                        idCliente=bdClientes.buscarCliente(cedulaCliente);
                    }else{
                        idCliente=bdClientesCompletos.buscarCliente(cedulaCliente);
                    }

                    /*
                    String idCliente= clientes.get(j).getId();
                    String cedulaCliente= clientes.get(j).getCedula();
                    String cliente=clientes.get(j).getName();
                    */

                    int total= Integer.parseInt(txtTotalFactura.getText().toString());
                    int pagado= Integer.parseInt(txtPagado.getText().toString());
                    int cantidadProductos= calcularCantidadProductos(productos);
                    int idVendedor= Integer.parseInt(LoginActivity.getId(FacturaVentaActivity.this));

                    String estadoVenta= "partial";
                    if(txtDeuda.getText().equals("0")){
                        estadoVenta="paid";
                    }

                    if(Integer.parseInt(txtDeuda.getText().toString())>0){
                        String totalString = ""+total;
                        String pagadoString = ""+pagado;
                        bdDeudas.agregarDeuda(-1,fecha,referencia,cliente,cedulaCliente,totalString,
                                pagadoString);
                        Log.d("deudaaaaaaaa",fecha+","+referencia+","+cliente+","+cedulaCliente);
                    }

                    //for(int i=0;i<100;i++){
                        bdVentas.agregarVenta(id+1,fecha,referencia,idVendedor,cedulaCliente,cliente,total,
                                estadoVenta,pagado,cantidadProductos,idCliente,FacturaVentaActivity.this,existe,
                                null,CREDITO);

                    if(CREDITO>0 && bdCredito.buscarCliente(cedulaCliente)){
                            bdCredito.descontarCredito(cedulaCliente,CREDITO,context);
                    }



                        for(int j=0;j<productos.size();j++){

                            int idProducto= productos.get(j).getId();
                            String codigoProducto= productos.get(j).getCodigoProducto();
                            String nombreProducto= productos.get(j).getNombre();

                            int precioUnitario= 0;
                            if("Credito".equals(TIPOPAGO)){
                                if(productos.get(j).getPrecio()>=productos.get(j).getPrecio2()){
                                    precioUnitario=productos.get(j).getPrecio();
                                }else if(productos.get(j).getPrecio2()>=productos.get(j).getPrecio()){
                                    precioUnitario=productos.get(j).getPrecio2();
                                }
                            }else if("Contado".equals(TIPOPAGO)){
                                if(productos.get(j).getPrecio()>=productos.get(j).getPrecio2()){
                                    precioUnitario=productos.get(j).getPrecio2();
                                }else if(productos.get(j).getPrecio2()>=productos.get(j).getPrecio()){
                                    precioUnitario=productos.get(j).getPrecio();
                                }
                            }

                            int cantidad= productos.get(j).getCantidad();
                            bdProductosVenta.crearProductoVenta(id+1,idProducto,codigoProducto,nombreProducto,precioUnitario,
                                    cantidad,idVendedor);
                            bdProductos.actualizarProducto(idProducto,cantidad);
                        }
                    //}

                    Toast.makeText(getApplicationContext(),"venta guardada",Toast.LENGTH_SHORT).show();

                    header += centrarCadena(txtTitulo.getText().toString().toUpperCase()) + BREAK;
                    header += alinearLineas(txtDireccion.getText().toString()) + BREAK;
                    header += centrarCadena("Republica dominicana") + BREAK;
                    header += centrarCadena("Tel: " + txtTelefono.getText().toString()) + BREAK;

                    header += DIVIDER_DOUBLE + BREAK;


                    msgDatos += "Fecha:" + txtFecha.getText().toString() + BREAK;
                    msgDatos += "Id venta:" + txtIdVenta.getText().toString() + BREAK;
                    msgDatos += "Vendedor:" + LoginActivity.getUserName(FacturaVentaActivity.this) + BREAK;
                    msgDatos += "Cliente:" + txtNombreCliente.getText().toString() + BREAK;
                    msgDatos += "Estado:" + txtEstado.getText().toString() + BREAK;
                    msgDatos += DIVIDER + BREAK;
                    msgDatos += "cantidad" + SPACE4 + "Producto" + SPACE4 + "Total" + BREAK;
                    msgDatos += DIVIDER + BREAK;


                    msg += "Total:" + txtTotalFactura.getText().toString() + BREAK;
                    msg += "Total Pagado:" + txtPagado.getText().toString() + BREAK;
                    msg += "Total a deber: " + txtDeuda.getText().toString() + BREAK + BREAK;


                    String nota= SharedPreferences.getPreferenciaNotaFactura(FacturaVentaActivity.this).toString();
                    if(nota.length()>0) {
                        msg += "Notas:" + BREAK + nota +BREAK;
                    }


                    byte[] cmd = new byte[3];
                    cmd[0] = 0x1b;
                    cmd[1] = 0x21;
                    cmd[2] |= 0x10;
                    mService.write(cmd);
                    mService.sendMessage(header, "UTF-8");

                    cmd[2] &= 0xEF;
                    mService.write(cmd);
                    mService.sendMessage(msgDatos, "UTF-8");

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
                    mService.sendMessage(msgProductos, "UTF-8");

                    raya = DIVIDER_DOUBLE + BREAK;
                    cmd[2] &= 0xEF;
                    mService.write(cmd);
                    mService.sendMessage(raya, "UTF-8");

                    cmd[2] &= 0xEF;
                    mService.write(cmd);
                    mService.sendMessage(msg, "UTF-8");
                    takeScreenshot();

                    contador=1;

                    SharedPreferences.guardarPreferenciaNumeroIntentos(FacturaVentaActivity.this,1);

                    Log.d("facturaVenta", header + "\n" + msgDatos + "\n" + msgProductos + "\n" + msg);



                    msg = "";
                    msgProductos = "";
                    msgDatos = "";
                    header = "";

                }else{
                    Toast.makeText(getApplicationContext(),"venta ya existente",Toast.LENGTH_SHORT).show();
                }


            }else if(v.equals(btnSendCopia)){
                if(SharedPreferences.getPreferenciaNumeroIntentosFactura(FacturaVentaActivity.this) >0){


                    AlertDialog.Builder builder= new AlertDialog.Builder(FacturaVentaActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                    final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                    Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                    builder.setView(mView);
                    final AlertDialog alertDialog= builder.create();


                    btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        if(Integer.parseInt(txtContrasenia.getText().toString()) !=
                                ConfiguracionActivity.getPreferenciaPing(FacturaVentaActivity.this)){
                                txtContrasenia.setError("Ping incorrecto");
                            } else {

                                header="";
                                msgDatos="";
                                msg="";
                                msgProductos="";

                                header += centrarCadena(txtTitulo.getText().toString().toUpperCase())+ BREAK;
                                header += alinearLineas(txtDireccion.getText().toString()) + BREAK;
                                header += centrarCadena("Republica dominicana")+ BREAK;
                                header += centrarCadena("Tel: "+txtTelefono.getText().toString())+BREAK ;

                                header += DIVIDER_DOUBLE + BREAK;

                                msgDatos += "Fecha:"+ txtFecha.getText().toString()+ BREAK;
                                msgDatos += "Id venta:" + txtIdVenta.getText().toString() + BREAK;
                                msgDatos += "Vendedor:" +"vendedor" +BREAK;
                                msgDatos += "Cliente:" +txtNombreCliente.getText().toString()+BREAK;
                                msgDatos += "Estado:" +txtEstado.getText().toString()+BREAK;

                                msgDatos += DIVIDER + BREAK;
                                msgDatos += "cantidad" + SPACE4 + "Producto" +SPACE4+ "Total" +BREAK;
                                msgDatos += DIVIDER + BREAK;


                                msg += "Total factura:" + txtTotalFactura.getText().toString() + BREAK;
                                msg += "Total pagado:" + txtPagado.getText().toString() + BREAK;
                                msg += "Total a deber: "+ txtDeuda.getText().toString() + BREAK + BREAK;

                                String nota= SharedPreferences.getPreferenciaNotaFactura(FacturaVentaActivity.this).toString();
                                if(nota.length()>0) {
                                    msg += "Notas:" + BREAK + nota +BREAK;
                                }

                                msg += DIVIDER + BREAK;

                                msg+= "Recibido por"+BREAK+BREAK+BREAK;
                                msg+= "_______________________________"+BREAK+BREAK;

                                byte[] cmd = new byte[3];
                                cmd[0] = 0x1b;
                                cmd[1] = 0x21;
                                cmd[2] |= 0x10;
                                mService.write(cmd);
                                mService.sendMessage(header, "UTF-8");

                                cmd[2] &= 0xEF;
                                mService.write(cmd);
                                mService.sendMessage(msgDatos, "UTF-8");

                                for(int i=0;i<productos.size();i++){

                                    if(productos.get(i).getNombre().length()>17){

                                        String nombre= productos.get(i).getNombre();
                                        String linea1= nombre;
                                        String linea2="";

                                        for(int j=17;j>1;j--){

                                            if(linea1.charAt(j)==' '){
                                                linea1= nombre.substring(0,j);
                                                linea2= nombre.substring(j,nombre.length());
                                                break;
                                            }
                                        }

                                        String cadenaInicio=""+productos.get(i).getCantidad();
                                        String cadenaMedia=cadenaInicio+linea1;
                                        msgProductos += ""+productos.get(i).getCantidad()+contarEspaciosInicio(cadenaInicio) +
                                                linea1+contarEspacios(cadenaMedia)+
                                                (productos.get(i).getPrecio()*productos.get(i).getCantidad()) + BREAK;
                                        msgProductos+= contarEspaciosInicio(cadenaInicio)+linea2+ BREAK;



                                    }else{

                                        String cadenaInicio=""+productos.get(i).getCantidad();
                                        String cadenaMedia=cadenaInicio+productos.get(i).getNombre().toString();
                                        msgProductos += ""+productos.get(i).getCantidad()+contarEspaciosInicio(cadenaInicio) +
                                                productos.get(i).getNombre().toString()+contarEspacios(cadenaMedia) +
                                                (productos.get(i).getPrecio()*productos.get(i).getCantidad()) + BREAK;

                                    }

                                }
                                cmd[2] &= 0xEF ;
                                mService.write(cmd);
                                mService.sendMessage(msgProductos, "UTF-8");

                                raya = DIVIDER_DOUBLE + BREAK;
                                cmd[2] &= 0xEF  ;
                                mService.write(cmd);
                                mService.sendMessage(raya, "UTF-8");

                                cmd[2] &= 0xEF;
                                mService.write(cmd);
                                mService.sendMessage(msg, "UTF-8");

                                Log.d("ventaFactura",header+"\n"+msgDatos+"\n"+msgProductos+"\n"+msg);

                                msg = "";
                                msgProductos = "";
                                msgDatos = "";
                                header = "";

                                alertDialog.dismiss();

                                Toast.makeText(getApplicationContext(),"Copia impresa correcamente",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    alertDialog.show();



                }else{
                    Toast.makeText(getApplicationContext(),"Primero imprima la factura",Toast.LENGTH_SHORT);
                }
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
                Log.d(TAG, "onActivityResult: Factura venta activity result");
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
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

            String mPath = Environment.getExternalStorageDirectory().toString() + "/WDPOS/"+ "venta_"+
                    NOMBRE+"_"+txtFecha.getText().toString()+".png";

            // create bitmap screen capture
            btnSend.setVisibility(View.INVISIBLE);
            btnVolver.setVisibility(View.INVISIBLE);
            btnSendCopia.setVisibility(View.INVISIBLE);
            btnSearch.setVisibility(View.INVISIBLE);


            View v1 = findViewById(R.id.activity_factura_venta);
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
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
        bdClientes.close();
        bdClientesCompletos.close();
        bdDeudas.close();
        bdFactura.close();
        bdProductosVenta.close();
        bdProductos.close();
    }
}
