package swasolutions.com.wdpos.actividades.paneles;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.util.regex.Pattern;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.clientes.ClientesActivity;
import swasolutions.com.wdpos.actividades.clientes.RegistroClienteActivity;
import swasolutions.com.wdpos.actividades.facturas.CierreCajaActivity;
import swasolutions.com.wdpos.actividades.productos.CrearProductoActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.actividades.ventas.VentasActivity;
import swasolutions.com.wdpos.actividades.vistas.AbonosVistaActivity;
import swasolutions.com.wdpos.actividades.vistas.ClientesNuevosVistaActivity;
import swasolutions.com.wdpos.actividades.vistas.GastosVistaActivity;
import swasolutions.com.wdpos.actividades.vistas.VistaVentasActivity;
import swasolutions.com.wdpos.base_de_datos.AbonosBD;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.DeudasBD;
import swasolutions.com.wdpos.base_de_datos.GastosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosVentaBD;
import swasolutions.com.wdpos.base_de_datos.VentasBD;
import swasolutions.com.wdpos.vo.server.Clientes;
import swasolutions.com.wdpos.vo.server.Deudas;
import swasolutions.com.wdpos.vo.server.Productos;

public class PanelActivity extends AppCompatActivity {

    private Button btnVender,btnBuscarCLiente,btnGastos,btnCierreCaja,btnConfiguracion,
            btnAgregarCliente;
    private Button btnVistaVentas,btnVistaAbonos,btnVistaGastos,btnDevolucion,btnEditarCliente;
    private Button btnPedidos,btnSubirProducto,btnAgregarCredito,btnClientesNuevos;
    private TextView txtVersion;

    public static ProductosBD bdProductos;
    public static ClientesBD bdClientes;
    public static DeudasBD bdDeudas;
    public static GastosBD bdGastos;
    public static AbonosBD bdAbonos;
    public static ProductosVentaBD bdProductosVenta;
    public static VentasBD bdVentas;


    public static SQLiteDatabase sqLiteDatabase;

    public String link;

    private String NICKNAME;
    private String ID;

    public static View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        txtVersion= (TextView) findViewById(R.id.txtVersion_panel);
        txtVersion.setText("89");

        view = findViewById(android.R.id.content);
        /**
         * Inicilizacion de los botonoes iniciales
         */
        btnVender= (Button) findViewById(R.id.btnVender_panel);
        btnBuscarCLiente= (Button) findViewById(R.id.btnBuscarCLiente_panel);
        btnGastos= (Button) findViewById(R.id.btnGastos_panel);
        btnCierreCaja= (Button) findViewById(R.id.btnCierreCaja_panel);
        btnAgregarCliente= (Button) findViewById(R.id.btnRegistrarCliente_panel);
        btnPedidos= (Button) findViewById(R.id.btnPedido_panel);
        btnConfiguracion = (Button) findViewById(R.id.btnConfiguracion_panel);
        btnSubirProducto= (Button) findViewById(R.id.btnSubirProducto_Panel);
        btnAgregarCredito= (Button) findViewById(R.id.btnAgregarCredito_Panel);
        btnDevolucion= (Button) findViewById(R.id.btnDevolucion_panel);
        btnClientesNuevos= (Button) findViewById(R.id.btnVistaClientes_panel);
        btnEditarCliente= (Button) findViewById(R.id.btnEditarCliente_panel);

        /**
         * Inicializacion de los botones referentes a los productos, ventas, abonos y gastos
         * agregados en el momento de usar la aplicacion.
         */
        btnVistaVentas = (Button) findViewById(R.id.btnVistaVentas_panel);
        btnVistaAbonos= (Button) findViewById(R.id.btnVistaAbonos_panel);
        btnVistaGastos= (Button) findViewById(R.id.btnVistaGastos_panel);


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

        bdGastos= new GastosBD(getApplicationContext(),"GastosBD",null,1);
        sqLiteDatabase= bdGastos.getWritableDatabase();

        bdAbonos= new AbonosBD(getApplicationContext(),"AbonosBD",null,1);
        sqLiteDatabase= bdAbonos.getWritableDatabase();

        bdVentas= new VentasBD(getApplicationContext(),"VentasBD",null,1);
        sqLiteDatabase= bdVentas.getWritableDatabase();

        bdProductosVenta= new ProductosVentaBD(getApplicationContext(),"ProductosVentaBD",null,1);
        sqLiteDatabase=  bdProductosVenta.getWritableDatabase();

        //------------

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnEditarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(PanelActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(PanelActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            Intent intent= new Intent(getApplicationContext(),ClientesActivity.class);
                            intent.putExtra("key_tipo","edicion");
                            intent.putExtra("key_id",ID);
                            startActivity(intent);

                            alertDialog.dismiss();

                        }
                    }
                });

                alertDialog.show();

            }
        });

        btnClientesNuevos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(PanelActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(PanelActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            Intent intent= new Intent(getApplicationContext(),ClientesNuevosVistaActivity.class);
                            intent.putExtra("key_nickname",NICKNAME);
                            intent.putExtra("key_id",ID);
                            startActivity(intent);

                            alertDialog.dismiss();

                        }
                    }
                });

                alertDialog.show();



            }
        });

        btnDevolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder= new AlertDialog.Builder(PanelActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(PanelActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            Intent intent= new Intent(getApplicationContext(),ClientesActivity.class);
                            intent.putExtra("key_tipo","devolucion");
                            intent.putExtra("key_id",ID);
                            startActivity(intent);

                            alertDialog.dismiss();

                        }
                    }
                });

                alertDialog.show();



            }
        });

        btnSubirProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(PanelActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(PanelActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            Intent intentProductos= new Intent(getApplicationContext(), CrearProductoActivity.class);
                            startActivity(intentProductos);

                            alertDialog.dismiss();

                        }
                    }
                });

                alertDialog.show();
            }
        });

        btnAgregarCredito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(PanelActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(PanelActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            Intent intent= new Intent(getApplicationContext(),ClientesActivity.class);
                            intent.putExtra("key_tipo","credito");
                            intent.putExtra("key_id",ID);
                            startActivity(intent);

                            alertDialog.dismiss();

                        }
                    }
                });

                alertDialog.show();


            }
        });

        btnConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(PanelActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(PanelActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            Intent intentPanelConfiguracion = new Intent(getApplicationContext(),PanelConfiguracionActivity.class);
                            startActivity(intentPanelConfiguracion);

                            alertDialog.dismiss();

                        }
                    }
                });


                alertDialog.show();


            }
        });

        btnAgregarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),RegistroClienteActivity.class);
                startActivity(intent);

            }
        });



        btnCierreCaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(PanelActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(PanelActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {


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
                            intent.putExtra("key_ciclo","0");

                            startActivity(intent);

                            alertDialog.dismiss();

                        }
                    }
                });


                alertDialog.show();




            }
        });

        btnGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                        if(soloNumeros(txtDinero.getText().toString())){
                            gasto= Integer.parseInt(txtDinero.getText().toString());
                        }else if(!soloNumeros(txtDinero.getText().toString())){
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


            }
        });

        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getApplicationContext(),VentasActivity.class);
                intent.putExtra("key_nickname",NICKNAME);
                intent.putExtra("key_id",ID);
                intent.putExtra("key_tipo","venta");
                startActivity(intent);

            }
        });

        btnBuscarCLiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getApplicationContext(),ClientesActivity.class);
                intent.putExtra("key_tipo","pago");
                intent.putExtra("key_id",ID);
                startActivity(intent);
            }
        });

        btnPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),PanelPedidoActivity.class);
                startActivity(intent);
            }
        });


        btnVistaVentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(PanelActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(PanelActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            Intent intent = new Intent(getApplicationContext(), VistaVentasActivity.class);
                            startActivity(intent);

                            alertDialog.dismiss();

                        }
                    }
                });


                alertDialog.show();

            }
        });


        btnVistaAbonos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(PanelActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(PanelActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            Intent intent = new Intent(getApplicationContext(), AbonosVistaActivity.class);
                            startActivity(intent);

                            alertDialog.dismiss();

                        }
                    }
                });


                alertDialog.show();
            }
        });


        btnVistaGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(PanelActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(PanelActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            Intent intent = new Intent(getApplicationContext(), GastosVistaActivity.class);
                            startActivity(intent);

                            alertDialog.dismiss();

                        }
                    }
                });


                alertDialog.show();
            }
        });






    }

    private boolean soloNumeros(String name) {
        Pattern patron = Pattern.compile("^[0-9]+$");
        if (!patron.matcher(name).matches() || name.length() > 25) {
            return false;
        } else {
        }

        return true;
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


                if(isNetDisponible() && isOnlineNet()) {


                    bdProductos = new ProductosBD(getApplicationContext(), "BDMessages", null, 1);
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

                if(isNetDisponible() && isOnlineNet()) {
                    bdClientes = new ClientesBD(getApplicationContext(), "BDClientes", null, 1);
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

                if(isNetDisponible() && isOnlineNet()) {
                    bdDeudas = new DeudasBD(getApplicationContext(), "BDDeudas", null, 1);
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

    public static void mostrarSnockBar(String mensaje,int duracion){


        Snackbar.make(view.findViewById(android.R.id.content),mensaje,duracion)
                .setAction("Action", null).show();

    }

}
