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
import swasolutions.com.wdpos.vo.clases_objeto.ProductoCarrito;

/**
 * Created by sebas on 24/06/2017.
 */

public class FacturaAdapter extends  RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>{

    private List<ProductoCarrito> productos;
    private Context context;

    /**
     * Builder class
     * @param productos
     */
    public FacturaAdapter(List<ProductoCarrito> productos,Context context){

        this.productos=productos;
        this.context=context;
    }


    @Override
    public FacturaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_productos_factura, parent,false);
        return new FacturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FacturaViewHolder holder, int position) {

        holder.nombre.setText(productos.get(position).getNombre().toLowerCase());
        holder.total.setText(""+(productos.get(position).getPrecio()*productos.get(position).getCantidad()));
        holder.cantidad.setText(""+productos.get(position).getCantidad());
        holder.cardView.getBackground().setAlpha(0);


    }

    @Override
    public int getItemCount() {
        return productos.size();
    }



    static class FacturaViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView nombre;
        private TextView total;
        private TextView cantidad;

        public FacturaViewHolder(View itemView) {
            super(itemView);

            cardView= (CardView) itemView.findViewById(R.id.cardViewProductosFactura);
            nombre= (TextView) itemView.findViewById(R.id.txtNombre_CardViewProductosFactura);
            cantidad= (TextView) itemView.findViewById(R.id.txtCantidad_CardViewProductosFactura);
            total = (TextView) itemView.findViewById(R.id.txtPrecio_CardViewFactura);

        }
    }

}
