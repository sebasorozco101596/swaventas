package swasolutions.com.wdpos.actividades.paneles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.SharedPreferences;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.base_de_datos.AbonosBD;
import swasolutions.com.wdpos.base_de_datos.CarritoBD;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.ClientesCompletoBD;
import swasolutions.com.wdpos.base_de_datos.DeudasBD;
import swasolutions.com.wdpos.base_de_datos.GastosBD;
import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.base_de_datos.PedidosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosVentaBD;
import swasolutions.com.wdpos.base_de_datos.VentasBD;
import swasolutions.com.wdpos.base_de_datos.WarehouseBD;

public class PanelEliminacionActivity extends AppCompatActivity {

    private Button btnEliminarCliNuevos,btnEliminarTodo;

    public static AbonosBD bdAbonos;
    public static CarritoBD bdCarrito;
    public static ClientesBD bdClientes;
    public static ClientesCompletoBD bdClientesCompleto;
    public static DeudasBD bdDeudas;
    public static GastosBD bdGastos;
    public static GruposVendedorBD bdGruposVendedor;
    public static PedidosBD bdPedidos;
    public static ProductosBD bdProductos;
    public static ProductosVentaBD bdProductosVenta;
    public static VentasBD bdVentas;
    public static WarehouseBD bdWarehouses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_eliminacion);

        btnEliminarCliNuevos= (Button) findViewById(R.id.btnEliminarCliNuevos_panelEliminacion);
        btnEliminarTodo= (Button) findViewById(R.id.btnEliminarTodo_panelEliminacion);

        btnEliminarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(PanelEliminacionActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(PanelEliminacionActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            bdAbonos= new AbonosBD(getApplicationContext(),"AbonosBD",null,1);
                            bdCarrito= new CarritoBD(getApplicationContext(),"CarritoBD",null,1);
                            bdClientes= new ClientesBD(getApplicationContext(),"BDMessages",null,1);
                            bdClientesCompleto= new ClientesCompletoBD(getApplicationContext(),"ClientesCompletoBD",null,1);
                            bdDeudas= new DeudasBD(getApplicationContext(),"DeudasBD",null,1);
                            bdGruposVendedor = new GruposVendedorBD(getApplicationContext(),"GruposVendedorBD",null,1);
                            bdGastos= new GastosBD(getApplicationContext(),"GastosBD",null,1);
                            bdPedidos= new PedidosBD(getApplicationContext(),"PedidosBD",null,1);
                            bdProductos= new ProductosBD(getApplicationContext(),"ProductosBD",null,1);
                            bdVentas= new VentasBD(getApplicationContext(),"VentasBD",null,1);
                            bdProductosVenta= new ProductosVentaBD(getApplicationContext(),"ProductosVentaBD",null,1);
                            bdWarehouses= new WarehouseBD(getApplicationContext(),"WarehouusesBD",null,1);


                            bdAbonos.eliminarAbonos();
                            bdCarrito.eliminarProductosCarrito();
                            bdClientes.eliminarTodosClientes();
                            bdClientesCompleto.eliminarClientes();
                            bdDeudas.eliminarTodasLasDeudas();
                            bdGruposVendedor.eliminarGruposVendedor();
                            bdGastos.eliminarGastos();
                            bdPedidos.eliminarPedidos();
                            bdProductos.eliminarTodosProductos();
                            bdVentas.eliminarVentas();
                            bdProductosVenta.eliminarProductosVenta();
                            bdWarehouses.eliminarWarehouses();

                            bdAbonos.close();
                            bdCarrito.close();
                            bdClientes.close();
                            bdClientesCompleto.close();
                            bdDeudas.close();
                            bdGruposVendedor.close();
                            bdGastos.close();
                            bdPedidos.close();
                            bdProductos.close();
                            bdVentas.close();
                            bdProductosVenta.close();
                            bdWarehouses.close();




                            ConfiguracionActivity.eliminarPreferencias(PanelEliminacionActivity.this);
                            LoginActivity.eliminarPreferencias(PanelEliminacionActivity.this);
                            SharedPreferences.eliminarPreferencias(PanelEliminacionActivity.this);


                            Intent intent= new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            alertDialog.dismiss();

                        }
                    }
                });
                alertDialog.show();

            }
        });

        btnEliminarCliNuevos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder= new AlertDialog.Builder(PanelEliminacionActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(PanelEliminacionActivity.this))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {
                            bdClientesCompleto= new ClientesCompletoBD(getApplicationContext(),
                                    "ClientesCompletoBD",null,1);
                            bdClientesCompleto.eliminarClientes();
                            bdClientesCompleto.close();

                            Toast.makeText(getApplicationContext(),"Clientes eliminados correctamente",
                                    Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();

            }
        });
    }
}
