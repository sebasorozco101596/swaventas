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
import swasolutions.com.wdpos.vo.clases_objeto.Devolucion;

/**
 * Created by sebas on 21/12/2017.
 */

public class ProductosDevolucionAdapter extends  RecyclerView.Adapter<ProductosDevolucionAdapter.ProductosDevolucionViewHolder>{

    private List<Devolucion> devoluciones;

    public ProductosDevolucionAdapter(List<Devolucion> devoluciones, Context context) {
        this.devoluciones = devoluciones;
    }

    @Override
    public ProductosDevolucionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_devolucion, parent,false);
        return new ProductosDevolucionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductosDevolucionViewHolder holder, int position) {

        holder.codigo.setText(devoluciones.get(position).getCodigoProducto());
        holder.nombre.setText(devoluciones.get(position).getNombreProducto());
        holder.precio.setText(""+devoluciones.get(position).getValor());

    }

    @Override
    public int getItemCount() {
        return devoluciones.size();
    }

    static class ProductosDevolucionViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        TextView codigo;
        TextView nombre;
        TextView precio;

        public ProductosDevolucionViewHolder(View itemView) {
            super(itemView);

            cardView= (CardView) itemView.findViewById(R.id.cardViewDevolucion);
            nombre= (TextView) itemView.findViewById(R.id.txtNombre_CardViewDevolucion);
            precio= (TextView) itemView.findViewById(R.id.txtPrecio_CardViewDevolucion);
            codigo= (TextView) itemView.findViewById(R.id.txtCode_CardViewDevolucion);

        }
    }

}
