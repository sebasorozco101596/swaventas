package swasolutions.com.wdpos.adaptadores.adaptadoresvistas;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.vistas.ProductosVistaActivity;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.base_de_datos.ProductosVentaBD;
import swasolutions.com.wdpos.base_de_datos.VentasBD;
import swasolutions.com.wdpos.vo.clases_objeto.ProductoVenta;
import swasolutions.com.wdpos.vo.clases_objeto.Venta;

/**
 * Created by sebas on 22/09/2017.
 */

public class VentasVistaAdapter extends  RecyclerView.Adapter<VentasVistaAdapter.VentasVistaViewHolder> {

    private List<Venta> ventas;
    private Context context;


    public static ProductosVentaBD bdProductosVenta;
    public static VentasBD bdVentas;
    public static ProductosBD bdProductos;


    public VentasVistaAdapter(List<Venta> ventas, Context context) {


        this.ventas=ventas;
        this.context=context;

        bdVentas= new VentasBD(context,null,1);
        bdProductosVenta= new ProductosVentaBD(context,null,1);
        bdProductos= new ProductosBD(context,null,1);


    }

    @Override
    public VentasVistaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_ventas,parent,false);
        return new VentasVistaAdapter.VentasVistaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VentasVistaViewHolder holder, final int position) {

        //
        holder.idVenta.setText(""+ventas.get(position).getId());
        holder.fecha.setText(ventas.get(position).getFecha());
        holder.cliente.setText(ventas.get(position).getCliente());
        holder.tipo.setText("contado");
        holder.total.setText(""+ventas.get(position).getTotal());
        holder.totalDebido.setText(""+ (ventas.get(position).getTotal()-ventas.get(position).getPagadoPorCliente()));
        holder.totalPagado.setText(""+ventas.get(position).getPagadoPorCliente());

        holder.txtOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final PopupMenu popupMenu =  new PopupMenu(context,holder.txtOptions);
                popupMenu.inflate(R.menu.menuvistas);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getItemId()){
                            case R.id.accion_mostrar:

                                Intent intent= new Intent(context,ProductosVistaActivity.class);
                                intent.putExtra("key_idVenta",ventas.get(position).getId());
                                context.startActivity(intent);

                                break;
                            case R.id.accion_eliminar_venta:

                                ArrayList<ProductoVenta> productoVentas;

                                bdVentas.eliminarVenta(ventas.get(position).getId());

                                productoVentas= bdProductosVenta.productosVenta(position);

                                for(int i=0;i<productoVentas.size();i++){
                                    bdProductos.actualizarProducto(productoVentas.get(i).getIdProducto(),
                                            (productoVentas.get(i).getCantidad() * (-1)));
                                }

                                bdProductosVenta.eliminarProductoVenta(ventas.get(position).getId());
                                ventas.remove(position);
                                notifyItemRemoved(position);



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
        return ventas.size();
    }

    static class VentasVistaViewHolder extends RecyclerView.ViewHolder{


        private  TextView idVenta;
        private  TextView fecha;
        private TextView cliente;
        private TextView tipo;
        private TextView total;
        private TextView totalPagado;
        private TextView totalDebido;

        TextView txtOptions;

        public VentasVistaViewHolder(View itemView) {
            super(itemView);

            idVenta= (TextView) itemView.findViewById(R.id.txtIdVenta_CardViewVentas);
            fecha= (TextView) itemView.findViewById(R.id.txtFecha_CardViewVentas);
            cliente= (TextView) itemView.findViewById(R.id.txtCliente_CardViewVentas);
            tipo= (TextView) itemView.findViewById(R.id.txtTipo_CardViewVentas);
            total= (TextView) itemView.findViewById(R.id.txtTotal_cardViewVentas);
            totalPagado= (TextView) itemView.findViewById(R.id.txtTotalPagado_CardViewVentas);
            totalDebido= (TextView) itemView.findViewById(R.id.txtTotalDebido_cardViewVentas);
            txtOptions= (TextView) itemView.findViewById(R.id.txtOptionMenuVistaVentas);
        }
    }
}
