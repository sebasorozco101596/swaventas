package swasolutions.com.wdpos.actividades.ventas;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.apartados.ApartadosActivity;
import swasolutions.com.wdpos.actividades.facturas.FacturaVentaActivity;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.adaptadores.ProductosCarritoAdapter;
import swasolutions.com.wdpos.base_de_datos.CarritoBD;
import swasolutions.com.wdpos.base_de_datos.CreditoBD;
import swasolutions.com.wdpos.logica.Logica;
import swasolutions.com.wdpos.vo.clases_objeto.ProductoCarrito;

public class CarritoActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static TextView txtTotalCredito,txtTotalContado;


    private ArrayList<ProductoCarrito> productosCarrito;

    private CarritoBD bdCarrito;
    private String ID;
    private String NOMBRE;
    private Logica logica;

    private CreditoBD bdCredito;
    private boolean isActivatedRadioButton;

    private String CEDULA;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        logica= new Logica();

        productosCarrito= new ArrayList<>();

        txtTotalCredito = (TextView) findViewById(R.id.txtPrecioTotalCredito_Carrito);
        txtTotalContado = (TextView) findViewById(R.id.txtPrecioTotalContado_Carrito);
        Button btnCancelarVenta= (Button) findViewById(R.id.btnCancelarVenta_carrito);
        Button btnProductos= (Button) findViewById(R.id.btnProductos_carrito);
        Button btnVender= (Button) findViewById(R.id.btnVender_carrito);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            ID = bundle.getString("key_id");
            NOMBRE = bundle.getString("key_nombre");
            CEDULA= bundle.getString("key_cedula");
        }

        bdCarrito= new CarritoBD(getApplicationContext(),null,1);
        bdCredito= new CreditoBD(getApplicationContext(),null,1);

        productosCarrito= bdCarrito.cargarProductosCarrito();

        bdCarrito.close();

        final int totalCredito= calcularTotalCredito(productosCarrito);
        final int totalContado= calcularTotalContado(productosCarrito);

        txtTotalCredito.setText(""+totalCredito);
        txtTotalContado.setText(""+totalContado);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCarroVenta);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        ProductosCarritoAdapter adapter = new ProductosCarritoAdapter(productosCarrito,
                this, LoginActivity.getUserType(this));
        recyclerView.setAdapter(adapter);


        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),VentasActivity.class);
                intent.putExtra("key_tipo","venta");
                intent.putExtra("key_id",ID);
                intent.putExtra("key_nombre",NOMBRE);
                intent.putExtra("key_cedula",CEDULA);
                startActivity(intent);
                finish();
            }
        });


        btnCancelarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {

                    /*
                     * We will create a personalized alert, we will add
                     * buttons and also their actions, we give the
                     * user a description of what will
                     * happen if he deletes his account, and the user
                     * will choose whether to delete it or not.
                     */

                    AlertDialog.Builder builder= new AlertDialog.Builder(CarritoActivity.this);
                    builder.setIcon(R.drawable.logo);
                    builder.setTitle(R.string.tittle_warning_drop_account);
                    builder.setMessage(R.string.detail_drop_account);
                    builder.setCancelable(false);

                    /*
                     * Action of the button
                     */
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            bdCarrito.eliminarProductosCarrito();
                            Toast.makeText(getApplicationContext(),"Venta cancelada",Toast.LENGTH_LONG).show();
                            finish();
                            bdCarrito.close();
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

            }
        });

        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (productosCarrito.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Agregue productos antes de vender",
                            Toast.LENGTH_SHORT).show();
                } else {

                    /*
                     * We will create a personalized alert, we will add
                     * buttons and also their actions, we give the
                     * user a description of what will
                     * happen if he deletes his account, and the user
                     * will choose whether to delete it or not.
                     */

                    AlertDialog.Builder builder = new AlertDialog.Builder(CarritoActivity.this);
                    builder.setIcon(R.drawable.logo);
                    builder.setTitle(R.string.tituloVender);
                    builder.setMessage(R.string.mensajeVender);
                    builder.setCancelable(false);

                    /*
                     * Action of the button
                     */
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            /*
                            Intent intent = new Intent(getApplicationContext(), ClientesActivity.class);
                            intent.putExtra("key_tipo", "venta");
                            int totalPagar = Integer.parseInt(txtTotalCredito.getText().toString());
                            intent.putExtra("key_total", totalPagar);
                            startActivity(intent);
                            finish();
                            */

                            AlertDialog.Builder builder= new AlertDialog.Builder(CarritoActivity.this);
                            View mView =  LayoutInflater.from(CarritoActivity.this).inflate(R.layout.dialog_pago,null);
                            builder.setView(mView);
                            final AlertDialog alertDialog= builder.create();
                            final EditText txtPago= (EditText) mView.findViewById(R.id.txtPago_DialogoPago);
                            final RadioGroup rdbGroup= (RadioGroup) mView.findViewById(R.id.groupRButtonTipoPago_Pago);
                            final RadioButton rdbContado= (RadioButton) mView.findViewById(R.id.rdbCotado_pagar);
                            final RadioButton rdbCredito= (RadioButton) mView.findViewById(R.id.rdbCredito_pagar);
                            final RadioButton rdbApartados= (RadioButton) mView.findViewById(R.id.rdbApartar_pagar);
                            final LinearLayout linearCredito= (LinearLayout) mView.findViewById(R.id.linearUsarCredito_DialogoPago);
                            final EditText txtValorCredito= (EditText) mView.findViewById(R.id.txtValorCredito_DialogoPago);
                            final CheckBox checkBoxCredito= (CheckBox) mView.findViewById(R.id.checkbox_usarCredito_diaPago);
                            final TextView txtTotalCredit= (TextView) mView.findViewById(R.id.txtTotalVentaCredito_dialogo);
                            final TextView txtTotalContad= (TextView) mView.findViewById(R.id.txtTotalVentaContado_dialogo);
                            Button btnPago= (Button) mView.findViewById(R.id.btnPagar_dialogo);

                            rdbGroup.setVisibility(View.VISIBLE);
                            txtTotalCredit.setText(""+txtTotalCredito.getText().toString());
                            txtTotalContad.setText(""+txtTotalContado.getText().toString());

                            if(bdCredito.buscarCliente(CEDULA)){
                                linearCredito.setVisibility(View.VISIBLE);
                            }


                            isActivatedRadioButton=checkBoxCredito.isChecked();


                            checkBoxCredito.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if(isActivatedRadioButton){
                                        checkBoxCredito.setChecked(false);
                                        txtValorCredito.setVisibility(View.INVISIBLE);
                                    }

                                    isActivatedRadioButton= checkBoxCredito.isChecked();

                                    if(isActivatedRadioButton){
                                        txtValorCredito.setVisibility(View.VISIBLE);
                                    }

                                }
                            });

                            final int credito= bdCredito.buscarValor(CEDULA);

                            txtValorCredito.setText(""+credito);

                            btnPago.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int pagar=0;

                                    String tipo="";

                                    if(rdbContado.isChecked()){
                                        tipo= "Contado";
                                    }else if(rdbCredito.isChecked()){
                                        tipo= "Credito";
                                    }else if(rdbApartados.isChecked()){
                                        tipo="Apartado";
                                    }

                                    if(logica.soloNumeros(txtPago.getText().toString())){
                                        pagar= Integer.parseInt(txtPago.getText().toString());
                                    }else if(!logica.soloNumeros(txtPago.getText().toString())){
                                        txtPago.setError("Solo se admiten numeros");
                                        return;
                                    }if (txtPago.getText().toString().length() <= 0) {
                                        txtPago.setError("Digite el pago !");
                                    } else if(pagar>Integer.parseInt(txtTotalCredito.getText().toString())
                                            && ("Credito".equals(tipo) ||"Apartado".equals(tipo)) ){
                                        txtPago.setError("Excede el precio total de la venta!");
                                    }else if(pagar>Integer.parseInt(txtTotalContado.getText().toString())
                                            && "Contado".equals(tipo)){
                                        txtPago.setError("Excede el precio total de la venta!");
                                    }else if(pagar<0){
                                        txtPago.setError("Valor negativo");
                                    }else if(Integer.parseInt(txtValorCredito.getText().toString())>credito){
                                        txtValorCredito.setError("No se puede pasar de: " + credito);
                                    }else if(checkBoxCredito.isChecked() && txtValorCredito.getText().toString().length()<=0){
                                        txtValorCredito.setError("Digite algun valor");
                                    }else if(Integer.parseInt(txtValorCredito.getText().toString())<0){
                                        txtValorCredito.setError("Valor negativo");
                                    }else if(checkBoxCredito.isChecked() &&
                                            ((Integer.parseInt(txtPago.getText().toString()) +
                                                    Integer.parseInt(txtValorCredito.getText().toString()))>
                                                    Integer.parseInt(txtTotalCredito.getText().toString()))){
                                        txtPago.setError("El valor se excede del total a pagar");
                                    }
                                    else {

                                        if("Apartado".equals(tipo)){
                                            Intent intent= new Intent(getApplicationContext(),ApartadosActivity.class);

                                            int credito=0;

                                            int pago= Integer.parseInt(txtPago.getText().toString()) + credito;

                                            String cedula= CEDULA;
                                            intent.putExtra("key_nombreCliente",NOMBRE);
                                            intent.putExtra("key_pago",""+pago);
                                            intent.putExtra("key_cedula",cedula);
                                            intent.putExtra("key_tipoPago", tipo);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Intent intent= new Intent(getApplicationContext(),FacturaVentaActivity.class);

                                            int credito=0;

                                            if(checkBoxCredito.isChecked()){
                                                credito=Integer.parseInt(txtValorCredito.getText().toString());
                                            }

                                            int pago= Integer.parseInt(txtPago.getText().toString()) + credito;

                                            String cedula= CEDULA;
                                            Toast.makeText(getApplicationContext(),CEDULA,Toast.LENGTH_SHORT).show();
                                            intent.putExtra("key_nombreCliente",NOMBRE);
                                            intent.putExtra("key_pago",""+pago);
                                            intent.putExtra("key_cedula",cedula);
                                            intent.putExtra("key_credito",credito);
                                            intent.putExtra("key_tipoPago", tipo);
                                            startActivity(intent);
                                            finish();
                                        }
                                        alertDialog.dismiss();



                                    }
                                }
                            });


                            alertDialog.show();








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

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                }


            }
        });

    }

    @SuppressLint("SetTextI18n")
    public static int calcularTotalContado(List<ProductoCarrito> productosCarrito) {

        int total=0;

        for(int i =0;i<productosCarrito.size();i++){

            if(productosCarrito.get(i).getPrecio()>=productosCarrito.get(i).getPrecio2()){
                total+= (productosCarrito.get(i).getPrecio2())*(productosCarrito.get(i).getCantidad());
            }else{
                total+= (productosCarrito.get(i).getPrecio())*(productosCarrito.get(i).getCantidad());
            }
        }


        txtTotalContado.setText(""+total);
        return total;


    }

    @SuppressLint("SetTextI18n")
    public static int calcularTotalCredito(List<ProductoCarrito> productos){

        int total=0;

        for(int i =0;i<productos.size();i++){

            if(productos.get(i).getPrecio()>=productos.get(i).getPrecio2()){
                total+= (productos.get(i).getPrecio())*(productos.get(i).getCantidad());
            }else{
                total+= (productos.get(i).getPrecio2())*(productos.get(i).getCantidad());
            }
        }


        txtTotalCredito.setText(""+total);
        return total;
    }
}
