package swasolutions.com.wdpos.adaptadores.adaptadoresvistas;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.vo.clases_objeto.ProductoVenta;

/**
 * Created by sebas on 22/09/2017.
 */

public class ProductosVentaVistaAdapter  extends  RecyclerView.Adapter<ProductosVentaVistaAdapter.ProductosVentaVistaViewHolder> {

    private List<ProductoVenta> productos;
    private Context context;


    /**
     * Builder class
     * @param productos
     */
    public ProductosVentaVistaAdapter(List<ProductoVenta> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }




    @Override
    public ProductosVentaVistaAdapter.ProductosVentaVistaViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                       int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_productos_venta,parent,false);
        return new ProductosVentaVistaAdapter.ProductosVentaVistaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductosVentaVistaAdapter.ProductosVentaVistaViewHolder holder,
                                 final int position) {

        holder.id.setText(""+productos.get(position).getIdVenta());
        holder.nombre.setText(productos.get(position).getNombre());
        holder.precio.setText(""+productos.get(position).getPrecioUnitario());
        holder.cantidad.setText(""+productos.get(position).getCantidad());

        holder.cardView.getBackground().setAlpha(0);


    }

    @Override
    public int getItemCount() {
        return productos.size();
    }



    static class ProductosVentaVistaViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        TextView id;
        TextView nombre;
        TextView precio;
        TextView cantidad;

        public ProductosVentaVistaViewHolder(View itemView) {
            super(itemView);

            cardView= (CardView) itemView.findViewById(R.id.cardViewProductosVenta);
            id= (TextView) itemView.findViewById(R.id.txtId_CardViewProductosVenta);
            nombre= (TextView) itemView.findViewById(R.id.txtNombre_CardViewProductosVenta);
            precio= (TextView) itemView.findViewById(R.id.txtPrecio_CardViewProductosCarroVentas);
            cantidad= (TextView) itemView.findViewById(R.id.txtCantidad_CardViewProductosVenta);

        }
    }
}
