package swasolutions.com.wdpos.adaptadores.adaptadoresvistas;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.vo.clases_objeto.Gasto;

/**
 * Created by sebas on 22/09/2017.
 */

public class GastosVistaAdapter extends RecyclerView.Adapter<GastosVistaAdapter.GastosVistaViewHolder>{


    private List<Gasto> gastos;

    public GastosVistaAdapter(List<Gasto> gastos) {
        this.gastos = gastos;
    }

    @Override
    public GastosVistaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_gastos,parent,false);
        return new GastosVistaAdapter.GastosVistaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GastosVistaViewHolder holder, int position) {

        holder.fecha.setText(""+gastos.get(position).getFecha());
        holder.referencia.setText(""+gastos.get(position).getReferencia());
        holder.dinero.setText(""+gastos.get(position).getDineroGastado());
        holder.descripcion.setText(""+gastos.get(position).getDescripcion());

    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    static class GastosVistaViewHolder extends RecyclerView.ViewHolder{



        private TextView fecha;
        private TextView referencia;
        private TextView dinero;
        private TextView descripcion;

        private GastosVistaViewHolder(View itemView) {
            super(itemView);

            fecha= (TextView) itemView.findViewById(R.id.txtFecha_CardViewGastos);
            referencia= (TextView) itemView.findViewById(R.id.txtReferencia_CardViewGastos);
            dinero= (TextView) itemView.findViewById(R.id.txtDinero_CardViewGastos);
            descripcion= (TextView) itemView.findViewById(R.id.txtDescripcion_cardviewGasto);
        }
    }


}
