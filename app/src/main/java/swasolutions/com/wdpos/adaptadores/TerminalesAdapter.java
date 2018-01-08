package swasolutions.com.wdpos.adaptadores;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.vo.clases_objeto.Producto;

/**
 * Created by sebas on 31/10/2017.
 */

public class TerminalesAdapter extends  RecyclerView.Adapter<TerminalesAdapter.TerminalesVistaViewHolder> {

    private List<Producto> productos;
    private Context context;


    /**
     * Builder class
     * @param productos
     */
    public TerminalesAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }




    @Override
    public TerminalesAdapter.TerminalesVistaViewHolder onCreateViewHolder(ViewGroup
    parent,
    int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_productos_venta,parent,false);
        return new TerminalesAdapter.TerminalesVistaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TerminalesAdapter.TerminalesVistaViewHolder holder,
    final int position) {

        holder.id.setText(""+productos.get(position).getId());
        holder.nombre.setText(productos.get(position).getNombre());
        holder.precio.setText(""+productos.get(position).getPrecio());
        holder.cantidad.setText(""+productos.get(position).getCantidad());
        holder.cardView.getBackground().setAlpha(0);


    }

    @Override
    public int getItemCount() {
        return productos.size();
    }



    static class TerminalesVistaViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        TextView id;
        TextView nombre;
        TextView precio;
        TextView cantidad;

        public TerminalesVistaViewHolder(View itemView) {
            super(itemView);

            cardView= (CardView) itemView.findViewById(R.id.cardViewProductosVenta);
            id= (TextView) itemView.findViewById(R.id.txtId_CardViewProductosVenta);
            nombre= (TextView) itemView.findViewById(R.id.txtNombre_CardViewProductosVenta);
            precio= (TextView) itemView.findViewById(R.id.txtPrecio_CardViewProductosCarroVentas);
            cantidad= (TextView) itemView.findViewById(R.id.txtCantidad_CardViewProductosVenta);

        }
    }
}
