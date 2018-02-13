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
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.clientes.DeudasClienteActivity;
import swasolutions.com.wdpos.actividades.clientes.EditarClienteActivity;
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

    private CreditoBD bdCredito;


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

                if("venta".equals(TIPO)){
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
                    MenuItem menuVender= menu.findItem(R.id.accion_vender);
                    menuVender.setVisible(false);
                }else if("pago".equals(TIPO)){
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
                    MenuItem menuVender= menu.findItem(R.id.accion_vender);
                    menuVender.setVisible(false);
                }else if("pedido".equals(TIPO)){
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
                    MenuItem menuVender= menu.findItem(R.id.accion_vender);
                    menuVender.setVisible(false);
                }else if("credito".equals(TIPO)){
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
                    MenuItem menuVender= menu.findItem(R.id.accion_vender);
                    menuVender.setVisible(false);
                }else if("devolucion".equals(TIPO)){
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
                    MenuItem menuVender= menu.findItem(R.id.accion_vender);
                    menuVender.setVisible(false);
                }else if("edicion".equals(TIPO)){
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
                    MenuItem menuVender= menu.findItem(R.id.accion_vender);
                    menuVender.setVisible(false);
                }else if("vender".equals(TIPO)){
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
                    MenuItem menuEdicion= menu.findItem(R.id.accion_editar_cliente);
                    menuEdicion.setVisible(false);
                }


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.accion_pagar:

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
                                intentEdicion.putExtra("key_grupo_id",clientes.get(position).getGroupId());
                                intentEdicion.putExtra("key_tipo","edicion");
                                context.startActivity(intentEdicion);

                                break;

                            case R.id.accion_vender:
                                Intent intentVender= new Intent(context,VentasActivity.class);
                                intentVender.putExtra("key_id",IDVENDEDOR);
                                intentVender.putExtra("key_tipo","venta");
                                intentVender.putExtra("key_grupo",clientes.get(position).getGroupId());
                                intentVender.putExtra("key_nombre",clientes.get(position).getName());
                                intentVender.putExtra("key_cedula",clientes.get(position).getCedula());
                                context.startActivity(intentVender);

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

    @Override
    public int getItemCount() {
        return clientes.size();
    }



    static class ClientesViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        private TextView id;
        private TextView nombre;
        private TextView compania;
        private TextView direccion;
        private TextView telefono;
        private TextView optionMenu;

        private ClientesViewHolder(View itemView) {
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
