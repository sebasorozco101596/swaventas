package swasolutions.com.wdpos.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.apartados.ApartadosActivity;
import swasolutions.com.wdpos.actividades.clientes.DeudasClienteActivity;
import swasolutions.com.wdpos.actividades.clientes.EditarClienteActivity;
import swasolutions.com.wdpos.actividades.facturas.FacturaVentaActivity;
import swasolutions.com.wdpos.actividades.pedidos.PedidosActivity;
import swasolutions.com.wdpos.actividades.ventas.VentasActivity;
import swasolutions.com.wdpos.base_de_datos.CreditoBD;
import swasolutions.com.wdpos.vo.clases_objeto.Cliente;

/**
 * Created by sebas on 24/06/2017.
 */

public class ClientesAdapter extends  RecyclerView.Adapter<ClientesAdapter.ClientesViewHolder>{

    private List<Cliente> clientes;
    private Context context;
    private String TIPO;
    private int TOTAL;
    private String NICKNAME;
    private String IDVENDEDOR;


    public static CreditoBD bdCredito;
    public boolean isActivatedRadioButton;


    /**
     * Builder class
     * @param clientes
     */
    public ClientesAdapter(List<Cliente> clientes, Context context,String tipo,int total,String nickname,
                           String id){

        this.clientes=clientes;
        this.context=context;
        this.TIPO=tipo;
        this.TOTAL=total;
        this.NICKNAME=nickname;
        this.IDVENDEDOR =id;

        bdCredito= new CreditoBD(context,null,1);


    }



    @Override
    public ClientesAdapter.ClientesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_clientes,parent,false);
        return new ClientesAdapter.ClientesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ClientesAdapter.ClientesViewHolder holder, final int position) {

        holder.id.setText(clientes.get(position).getCedula());

        holder.nombre.setText(clientes.get(position).getName());
        holder.compania.setText(clientes.get(position).getCompania());
        holder.direccion.setText(clientes.get(position).getDireccion());
        holder.telefono.setText(clientes.get(position).getTelefono());

        holder.cardView.getBackground().setAlpha(0);


        holder.optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu =  new PopupMenu(context,holder.optionMenu);
                Menu menu= popupMenu.getMenu();
                popupMenu.inflate(R.menu.menucliente);

                if(TIPO.equals("venta")){
                    MenuItem menuVenta= menu.findItem(R.id.accion_deudas);
                    menuVenta.setVisible(false);
                    MenuItem menuPedidos= menu.findItem(R.id.accion_pedidos);
                    menuPedidos.setVisible(false);
                    MenuItem menuCredito= menu.findItem(R.id.accion_agregar_credito);
                    menuCredito.setVisible(false);
                    MenuItem menuDevolucion= menu.findItem(R.id.accion_agregar_devolucion);
                    menuDevolucion.setVisible(false);
                    MenuItem menuEdicion= menu.findItem(R.id.accion_editar_cliente);
                    menuEdicion.setVisible(false);
                }else if(TIPO.equals("pago")){
                    MenuItem menuPagar= menu.findItem(R.id.accion_pagar);
                    menuPagar.setVisible(false);
                    MenuItem menuPedidos= menu.findItem(R.id.accion_pedidos);
                    menuPedidos.setVisible(false);
                    MenuItem menuCredito= menu.findItem(R.id.accion_agregar_credito);
                    menuCredito.setVisible(false);
                    MenuItem menuDevolucion= menu.findItem(R.id.accion_agregar_devolucion);
                    menuDevolucion.setVisible(false);
                    MenuItem menuEdicion= menu.findItem(R.id.accion_editar_cliente);
                    menuEdicion.setVisible(false);
                }else if(TIPO.equals("pedido")){
                    MenuItem menuPagar= menu.findItem(R.id.accion_pagar);
                    menuPagar.setVisible(false);
                    MenuItem menuVenta= menu.findItem(R.id.accion_deudas);
                    menuVenta.setVisible(false);
                    MenuItem menuCredito= menu.findItem(R.id.accion_agregar_credito);
                    menuCredito.setVisible(false);
                    MenuItem menuDevolucion= menu.findItem(R.id.accion_agregar_devolucion);
                    menuDevolucion.setVisible(false);
                    MenuItem menuEdicion= menu.findItem(R.id.accion_editar_cliente);
                    menuEdicion.setVisible(false);
                }else if(TIPO.equals("credito")){
                    MenuItem menuPagar= menu.findItem(R.id.accion_pagar);
                    menuPagar.setVisible(false);
                    MenuItem menuVenta= menu.findItem(R.id.accion_deudas);
                    menuVenta.setVisible(false);
                    MenuItem menuPedidos= menu.findItem(R.id.accion_pedidos);
                    menuPedidos.setVisible(false);
                    MenuItem menuDevolucion= menu.findItem(R.id.accion_agregar_devolucion);
                    menuDevolucion.setVisible(false);
                    MenuItem menuEdicion= menu.findItem(R.id.accion_editar_cliente);
                    menuEdicion.setVisible(false);
                }else if(TIPO.equals("devolucion")){
                    MenuItem menuPagar= menu.findItem(R.id.accion_pagar);
                    menuPagar.setVisible(false);
                    MenuItem menuVenta= menu.findItem(R.id.accion_deudas);
                    menuVenta.setVisible(false);
                    MenuItem menuPedidos= menu.findItem(R.id.accion_pedidos);
                    menuPedidos.setVisible(false);
                    MenuItem menuCredito= menu.findItem(R.id.accion_agregar_credito);
                    menuCredito.setVisible(false);
                    MenuItem menuEdicion= menu.findItem(R.id.accion_editar_cliente);
                    menuEdicion.setVisible(false);
                }else if(TIPO.equals("edicion")){
                    MenuItem menuPagar= menu.findItem(R.id.accion_pagar);
                    menuPagar.setVisible(false);
                    MenuItem menuVenta= menu.findItem(R.id.accion_deudas);
                    menuVenta.setVisible(false);
                    MenuItem menuPedidos= menu.findItem(R.id.accion_pedidos);
                    menuPedidos.setVisible(false);
                    MenuItem menuCredito= menu.findItem(R.id.accion_agregar_credito);
                    menuCredito.setVisible(false);
                    MenuItem menuDevolucion= menu.findItem(R.id.accion_agregar_devolucion);
                    menuDevolucion.setVisible(false);
                }


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.accion_pagar:

                                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                                    View mView =  LayoutInflater.from(context).inflate(R.layout.dialog_pago,null);
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
                                    final TextView txtTotal= (TextView) mView.findViewById(R.id.txtTotalVenta_dialogo);
                                    Button btnPago= (Button) mView.findViewById(R.id.btnPagar_dialogo);

                                    rdbGroup.setVisibility(View.VISIBLE);
                                    txtTotal.setText(""+TOTAL);

                                    if(bdCredito.buscarCliente(clientes.get(position).getCedula())){
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

                                    final int credito= bdCredito.buscarValor(clientes.get(position).getCedula());

                                    txtValorCredito.setText(""+credito);

                                btnPago.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int pagar=0;

                                            if(soloNumeros(txtPago.getText().toString())){
                                                pagar= Integer.parseInt(txtPago.getText().toString());
                                            }else if(!soloNumeros(txtPago.getText().toString())){
                                                txtPago.setError("Solo se admiten numeros");
                                                return;
                                            }

                                            if (txtPago.getText().toString().length() <= 0) {
                                                txtPago.setError("Digite el pago !");
                                            } else if(pagar>TOTAL){
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
                                                            Integer.parseInt(txtTotal.getText().toString()))){
                                                txtPago.setError("El valor se excede del total a pagar");
                                            }
                                            else {
                                                String tipo="";

                                                if(rdbContado.isChecked()){
                                                    tipo= "Contado";
                                                }else if(rdbCredito.isChecked()){
                                                    tipo= "Credito";
                                                }else if(rdbApartados.isChecked()){
                                                    tipo="Apartado";
                                                }

                                                if(tipo.equals("Apartado")){
                                                    Intent intent= new Intent(context,ApartadosActivity.class);

                                                    int credito=0;

                                                    int pago= Integer.parseInt(txtPago.getText().toString()) + credito;

                                                    String cedula= clientes.get(position).getCedula();
                                                    intent.putExtra("key_nombreCliente",clientes.get(position).getName());
                                                    intent.putExtra("key_pago",""+pago);
                                                    intent.putExtra("key_cedula",cedula);
                                                    intent.putExtra("key_tipoPago", tipo);
                                                    context.startActivity(intent);
                                                }else{
                                                    Intent intent= new Intent(context,FacturaVentaActivity.class);

                                                    int credito=0;

                                                    if(checkBoxCredito.isChecked()){
                                                        credito=Integer.parseInt(txtValorCredito.getText().toString());
                                                    }

                                                    int pago= Integer.parseInt(txtPago.getText().toString()) + credito;

                                                    String cedula= clientes.get(position).getCedula();
                                                    intent.putExtra("key_nombreCliente",clientes.get(position).getName());
                                                    intent.putExtra("key_pago",""+pago);
                                                    intent.putExtra("key_cedula",cedula);
                                                    intent.putExtra("key_credito",credito);
                                                    intent.putExtra("key_tipoPago", tipo);
                                                    context.startActivity(intent);
                                                }
                                                alertDialog.dismiss();



                                            }
                                        }
                                    });


                                    alertDialog.show();



                                break;
                            case R.id.accion_deudas:
                                Intent intent = new Intent(context, DeudasClienteActivity.class);

                                String cedula= clientes.get(position).getCedula();
                                intent.putExtra("key_cedula_cliente",cedula);
                                intent.putExtra("key_vendedor",NICKNAME);
                                intent.putExtra("key_idVendedor", IDVENDEDOR);
                                context.startActivity(intent);

                                break;

                            case R.id.accion_pedidos:

                                Intent intentPedidos = new Intent(context, PedidosActivity.class);
                                intentPedidos.putExtra("key_cliente",clientes.get(position).getName());
                                context.startActivity(intentPedidos);

                                break;

                            case R.id.accion_agregar_credito:


                                AlertDialog.Builder builderAgg= new AlertDialog.Builder(context);
                                View mViewAgg =  LayoutInflater.from(context).inflate(R.layout.dialog_agregar_credito,null);
                                builderAgg.setView(mViewAgg);
                                final EditText txtValor= (EditText) mViewAgg.findViewById(R.id.txtValor_AggCredito);
                                Button btnAgregarCredito= (Button) mViewAgg.findViewById(R.id.btnAgregarCredito_AggCredito);
                                builderAgg.setView(mViewAgg);
                                final AlertDialog alertDialogAgg= builderAgg.create();


                                btnAgregarCredito.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(txtValor.getText().toString().length()<=0){
                                            txtValor.setError("Agregue algun valor");
                                        } else {
                                            bdCredito.agregarCredito(clientes.get(position).getCedula(),
                                                    Integer.parseInt(txtValor.getText().toString()),context);

                                            alertDialogAgg.dismiss();
                                        }
                                    }
                                });


                                alertDialogAgg.show();

                                break;
                            case R.id.accion_agregar_devolucion:

                                Intent intentDevolucion= new Intent(context,VentasActivity.class);
                                intentDevolucion.putExtra("key_nickname",NICKNAME);
                                intentDevolucion.putExtra("key_idVendedor", IDVENDEDOR);
                                intentDevolucion.putExtra("key_cedula",clientes.get(position).getCedula());
                                intentDevolucion.putExtra("key_tipo","devolucion");
                                context.startActivity(intentDevolucion);

                                break;

                            case R.id.accion_editar_cliente:

                                Intent intentEdicion= new Intent(context,EditarClienteActivity.class);
                                intentEdicion.putExtra("key_cedula",clientes.get(position).getCedula());
                                intentEdicion.putExtra("key_nombre",clientes.get(position).getName());
                                intentEdicion.putExtra("key_telefono",clientes.get(position).getTelefono());
                                intentEdicion.putExtra("key_direccion",clientes.get(position).getDireccion());
                                intentEdicion.putExtra("key_grupo_id",clientes.get(position).getGroup_id());
                                intentEdicion.putExtra("key_tipo","edicion");
                                context.startActivity(intentEdicion);

                                break;
                            default:
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();

            }
        });





    }

    private boolean soloNumeros(String name) {
        Pattern patron = Pattern.compile("^[0-9]+$");
        if (!patron.matcher(name).matches() || name.length() > 25) {
            return false;
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }



    static class ClientesViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        TextView id;
        TextView nombre;
        TextView compania;
        TextView direccion;
        TextView telefono;
        TextView optionMenu;

        public ClientesViewHolder(View itemView) {
            super(itemView);

            cardView= (CardView) itemView.findViewById(R.id.cardViewClientes);
            id= (TextView) itemView.findViewById(R.id.txtId_CardViewClientes);
            nombre= (TextView) itemView.findViewById(R.id.txtNombre_CardViewClientes);
            compania= (TextView) itemView.findViewById(R.id.txtCompania_CardViewClientes);
            direccion= (TextView) itemView.findViewById(R.id.txtDireccion_CardViewClientes);
            telefono= (TextView) itemView.findViewById(R.id.txtTelefono_CardViewClientes);
            optionMenu= (TextView) itemView.findViewById(R.id.txtOptionMenuClientes);

        }
    }

    public void setFilter(ArrayList<Cliente> nuevaListaClientes){

        clientes= new ArrayList<>();
        clientes.addAll(nuevaListaClientes );
        notifyDataSetChanged();

    }

}
