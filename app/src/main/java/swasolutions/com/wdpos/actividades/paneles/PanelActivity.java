package swasolutions.com.wdpos.actividades.paneles;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.clientes.ClientesActivity;
import swasolutions.com.wdpos.actividades.clientes.ImprimirClientesActivity;
import swasolutions.com.wdpos.actividades.clientes.RegistroClienteActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.actividades.ventas.VentasActivity;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.DeudasBD;
import swasolutions.com.wdpos.base_de_datos.GastosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.logica.Logica;
import swasolutions.com.wdpos.vo.server.Clientes;
import swasolutions.com.wdpos.vo.server.Deudas;
import swasolutions.com.wdpos.vo.server.Productos;

public class PanelActivity extends AppCompatActivity implements View.OnClickListener{

    private Logica logica;

    private ProductosBD bdProductos;
    private ClientesBD bdClientes;
    private DeudasBD bdDeudas;
    private GastosBD bdGastos;



    private String link;

    private String NICKNAME;
    private String ID;

    public static View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        TextView txtVersion= (TextView) findViewById(R.id.txtVersion_panel);
        txtVersion.setText("91");

        logica= new Logica();

        view = findViewById(android.R.id.content);
        /**
         * Inicilizacion de los botonoes iniciales
         */
        Button btnVender= (Button) findViewById(R.id.btnVender_panel);
        Button btnBuscarCLiente= (Button) findViewById(R.id.btnBuscarCLiente_panel);
        Button btnGastos= (Button) findViewById(R.id.btnGastos_panel);
        Button btnCierreCaja= (Button) findViewById(R.id.btnCierreCaja_panel);
        Button btnAgregarCliente= (Button) findViewById(R.id.btnRegistrarCliente_panel);
        Button btnPedidos= (Button) findViewById(R.id.btnPedido_panel);
        Button btnConfiguracion = (Button) findViewById(R.id.btnConfiguracion_panel);
        Button btnSubirProducto= (Button) findViewById(R.id.btnSubirProducto_Panel);
        Button  btnAgregarCredito= (Button) findViewById(R.id.btnAgregarCredito_Panel);
        Button btnDevolucion= (Button) findViewById(R.id.btnDevolucion_panel);
        Button btnClientesNuevos= (Button) findViewById(R.id.btnVistaClientes_panel);
        Button btnEditarCliente= (Button) findViewById(R.id.btnEditarCliente_panel);
        Button btnVistaVentas = (Button) findViewById(R.id.btnVistaVentas_panel);
        Button btnVistaAbonos= (Button) findViewById(R.id.btnVistaAbonos_panel);
        Button btnVistaGastos= (Button) findViewById(R.id.btnVistaGastos_panel);
        Button btnImprimirClientes= (Button) findViewById(R.id.btnImprimirClientes_panel);

        btnVender.setOnClickListener(this);
        btnBuscarCLiente.setOnClickListener(this);
        btnGastos.setOnClickListener(this);
        btnCierreCaja.setOnClickListener(this);
        btnAgregarCliente.setOnClickListener(this);
        btnPedidos.setOnClickListener(this);
        btnConfiguracion.setOnClickListener(this);
        btnSubirProducto.setOnClickListener(this);
        btnAgregarCredito.setOnClickListener(this);
        btnDevolucion.setOnClickListener(this);
        btnClientesNuevos.setOnClickListener(this);
        btnEditarCliente.setOnClickListener(this);
        btnVistaAbonos.setOnClickListener(this);
        btnVistaVentas.setOnClickListener(this);
        btnVistaGastos.setOnClickListener(this);
        btnImprimirClientes.setOnClickListener(this);


        link= ConfiguracionActivity.getLinkHosting(PanelActivity.this);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbarChats);
        setSupportActionBar(toolbar);

        /**
         * Se reciben los datos enviados desde el login.
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null) {
            NICKNAME = bundle.getString("key_nickname");
            ID= bundle.getString("key_id");

            Toast.makeText(getApplicationContext(),"Bienvenido: " +NICKNAME,Toast.LENGTH_SHORT).show();
            Log.d("nickname",NICKNAME);
        }

        /**
         * Instancia de las bases de datos locales
         */

        bdGastos= new GastosBD(getApplicationContext(),null,1);

        //------------

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    /**
     * The chats menu is displayed
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menupanel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Actions are handled by pressing the menu buttons
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_actualizarProductos:


                if(logica.verificarConexion(PanelActivity.this)) {


                    bdProductos = new ProductosBD(getApplicationContext(), null, 1);
                    bdProductos.eliminarTodosProductos();


                    int warehouseId= ConfiguracionActivity.getPreferenciaWarehouseID(PanelActivity.this);

                    Productos productos = new Productos(getApplicationContext(), link,warehouseId);
                    productos.obtenerProductos();


                    String mensaje= "actualizando productos, espere un momento";
                    mostrarSnockBar(mensaje,10);

                }else{
                    Toast.makeText(getApplicationContext(),"Verifique la conexión a internet",
                            Toast.LENGTH_SHORT).show();
                }

                return true;

            case R.id.accion_actualizarClientes:

                if(logica.verificarConexion(PanelActivity.this)) {
                    bdClientes = new ClientesBD(getApplicationContext(), null, 1);
                    bdClientes.eliminarTodosClientes();

                    Clientes clientes = new Clientes(getApplicationContext(), link);
                    clientes.obtenerClientes();

                    Snackbar.make(findViewById(android.R.id.content), "actualizando clientes," +
                            " espere un momento", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Verifique la conexión a internet",
                            Toast.LENGTH_SHORT).show();
                }

                return true;

            case R.id.accion_actualizarDeudasCliente:

                if(logica.verificarConexion(PanelActivity.this)) {
                    bdDeudas = new DeudasBD(getApplicationContext(), null, 1);
                    bdDeudas.eliminarTodasLasDeudas();

                    Deudas deudas = new Deudas(getApplicationContext(), link);
                    deudas.obtenerDeudas();


                    Snackbar.make(findViewById(android.R.id.content), "actualizando deudas, espere un momento", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }else{
                    Toast.makeText(getApplicationContext(),"Verifique la conexión a internet",
                            Toast.LENGTH_SHORT).show();
                }


                return true;

            case R.id.accion_configuracion:

                Intent intent= new Intent(getApplicationContext(),ConfiguracionActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void mostrarSnockBar(String mensaje,int duracion){

        Snackbar.make(view.findViewById(android.R.id.content),mensaje,duracion)
                .setAction("Action", null).show();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnVender_panel:

                Intent intent= new Intent(getApplicationContext(),VentasActivity.class);
                intent.putExtra("key_nickname",NICKNAME);
                intent.putExtra("key_id",ID);
                intent.putExtra("key_tipo","venta");
                startActivity(intent);
                break;

            case R.id.btnBuscarCLiente_panel:

                Intent intentAbonos= new Intent(getApplicationContext(),ClientesActivity.class);
                intentAbonos.putExtra("key_tipo","pago");
                intentAbonos.putExtra("key_id",ID);
                startActivity(intentAbonos);

                break;

            case R.id.btnGastos_panel:
                AlertDialog.Builder builder= new AlertDialog.Builder(PanelActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_gastos,null);
                final EditText txtDinero= (EditText) mView.findViewById(R.id.txtPrecio_DialogoGasto);
                final EditText txtDescripcion= (EditText) mView.findViewById(R.id.txtDescripcion_DialogoGasto);
                Button btnPago= (Button) mView.findViewById(R.id.btnGuardarGasto_dialogo);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnPago.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int gasto=0;

                        if(logica.soloNumeros(txtDinero.getText().toString())){
                            gasto= Integer.parseInt(txtDinero.getText().toString());
                        }else if(!logica.soloNumeros(txtDinero.getText().toString())){
                            txtDinero.setError("Solo se admiten numeros");
                            return;
                        }

                        if (txtDinero.getText().toString().length() <= 0) {
                            txtDinero.setError("Digite el valor del gasto !");
                        }else  if (txtDescripcion.getText().toString().length() <= 0) {
                            txtDinero.setError("Digite la descripcion del gasto!");
                        }else if(gasto<0){
                            txtDinero.setError("Valor negativo");
                        } else {
                            String descripcion= txtDescripcion.getText().toString();
                            String date = (DateFormat.format("yyyy/MM/dd HH:mm:ss", new java.util.Date()).toString());
                            String referencia= date+"/gasto/"+ LoginActivity.getId(PanelActivity.this);
                            int dinero= Integer.parseInt(txtDinero.getText().toString());
                            bdGastos.agregarGasto(date,referencia,dinero,
                                    LoginActivity.getUserName(PanelActivity.this),descripcion);
                            Toast.makeText(getApplicationContext(),"Gasto Guardado",Toast.LENGTH_SHORT).show();

                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();
                break;

            case R.id.btnCierreCaja_panel:
                logica.verificarContrasenia(PanelActivity.this,ID,"cierreCaja",NICKNAME);
                break;

            case R.id.btnPedido_panel:
                Intent intentPedidos= new Intent(getApplicationContext(),PanelPedidoActivity.class);
                startActivity(intentPedidos);
                break;

            case R.id.btnRegistrarCliente_panel:
                Intent intentAddCliente = new Intent(getApplicationContext(),RegistroClienteActivity.class);
                startActivity(intentAddCliente);
                break;
            case R.id.btnDevolucion_panel:
                logica.verificarContrasenia(PanelActivity.this,ID,"devolucion",null);
                break;

            case R.id.btnConfiguracion_panel:
                logica.verificarContrasenia(PanelActivity.this,ID,"configuracion",null);
                break;
            case R.id.btnSubirProducto_Panel:
                logica.verificarContrasenia(PanelActivity.this,ID,"crearProducto",null);
                break;
            case R.id.btnAgregarCredito_Panel:
                logica.verificarContrasenia(PanelActivity.this,ID,"agregarCredito",null);
                break;
            case R.id.btnEditarCliente_panel:
                logica.verificarContrasenia(PanelActivity.this,ID,"editarCliente",null);
                break;

            case R.id.btnVistaVentas_panel:
                logica.verificarContrasenia(PanelActivity.this,ID,"vistaVentas",null);
                break;

            case R.id.btnVistaAbonos_panel:
                logica.verificarContrasenia(PanelActivity.this,ID,"vistaAbonos",null);
                break;

            case R.id.btnVistaGastos_panel:
                logica.verificarContrasenia(PanelActivity.this,ID,"vistaGastos",null);
                break;

            case R.id.btnVistaClientes_panel:
                logica.verificarContrasenia(PanelActivity.this,ID,"vistaClientes",null);
                break;
            case R.id.btnImprimirClientes_panel:
                Intent intentClientes= new Intent(getApplicationContext(),ImprimirClientesActivity.class);
                intentClientes.putExtra("key_id",ID);
                startActivity(intentClientes);
                break;
            default:
                Toast.makeText(getApplicationContext(),"No presiono nada",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
