package swasolutions.com.wdpos.adaptadores.adaptadoresVistas;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.vo.clases_objeto.Abono;

/**
 * Created by sebas on 22/09/2017.
 */

public class AbonosVistaAdapter extends RecyclerView.Adapter<AbonosVistaAdapter.AbonosVistaViewHolder>{

    private List<Abono> abonos;
    private Context context;


    public AbonosVistaAdapter(List<Abono> abonos, Context context) {
        this.abonos = abonos;
        this.context = context;
    }

    @Override
    public AbonosVistaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_abonos,parent,false);
        return new AbonosVistaAdapter.AbonosVistaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AbonosVistaViewHolder holder, int position) {


        holder.idVenta.setText(""+abonos.get(position).getId());
        holder.estado.setText(""+abonos.get(position).getEstadoVenta());
        holder.pago.setText(""+abonos.get(position).getPagado());
        holder.fecha.setText(""+abonos.get(position).getFecha());

    }

    @Override
    public int getItemCount() {
        return abonos.size();
    }

    static class AbonosVistaViewHolder extends RecyclerView.ViewHolder{


        CardView cardView;

        TextView idVenta;
        TextView estado;
        TextView pago;
        TextView fecha;

        public AbonosVistaViewHolder(View itemView) {
            super(itemView);

            cardView= (CardView) itemView.findViewById(R.id.cardViewVistaAbonos);
            idVenta= (TextView) itemView.findViewById(R.id.txtIdVenta_CardViewAbonos);
            estado= (TextView) itemView.findViewById(R.id.txtEstado_CardViewAbonos);
            pago= (TextView) itemView.findViewById(R.id.txtPago_CardViewAbonos);
            fecha= (TextView) itemView.findViewById(R.id.txtFecha_cardviewAbonos);
        }
    }
}
