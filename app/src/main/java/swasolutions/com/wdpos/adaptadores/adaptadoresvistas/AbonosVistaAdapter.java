package swasolutions.com.wdpos.adaptadores.adaptadoresvistas;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.base_de_datos.AbonosBD;
import swasolutions.com.wdpos.base_de_datos.DeudasBD;
import swasolutions.com.wdpos.vo.clases_objeto.Abono;

/**
 * Created by sebas on 22/09/2017.
 */

public class AbonosVistaAdapter extends RecyclerView.Adapter<AbonosVistaAdapter.AbonosVistaViewHolder>{

    private ArrayList<Abono> abonos;
    private Context context;

    public static AbonosBD bdAbonos;
    public static DeudasBD bdDeudas;

    public AbonosVistaAdapter(ArrayList<Abono> abonos, Context context) {
        this.abonos = abonos;
        this.context= context;

        bdAbonos= new AbonosBD(context,null,1);
        bdDeudas= new DeudasBD(context,null,1);
    }

    @Override
    public AbonosVistaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_abonos,parent,false);
        return new AbonosVistaAdapter.AbonosVistaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AbonosVistaViewHolder holder, final int position) {


        holder.idVenta.setText(""+abonos.get(position).getId());
        holder.estado.setText(""+abonos.get(position).getEstadoVenta());
        holder.pago.setText(""+abonos.get(position).getPagado());
        holder.fecha.setText(""+abonos.get(position).getFecha());

        holder.txtOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final PopupMenu popupMenu =  new PopupMenu(context,holder.txtOptions);
                popupMenu.inflate(R.menu.menuvistas_abonos);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getItemId()){
                            case R.id.accion_eliminar_abono:

                                bdAbonos.eliminarAbono(abonos.get(position).getId());
                                bdDeudas.actualizarDeuda(bdDeudas.buscarId(abonos.get(position).getReferencia()),
                                        Integer.parseInt(holder.pago.getText().toString())*(-1));

                                abonos.remove(position);
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
        return abonos.size();
    }

    static class AbonosVistaViewHolder extends RecyclerView.ViewHolder{
        
        private TextView idVenta;
        private TextView estado;
        private TextView pago;
        private TextView fecha;

        TextView txtOptions;

        private AbonosVistaViewHolder(View itemView) {
            super(itemView);

            idVenta= (TextView) itemView.findViewById(R.id.txtIdVenta_CardViewAbonos);
            estado= (TextView) itemView.findViewById(R.id.txtEstado_CardViewAbonos);
            pago= (TextView) itemView.findViewById(R.id.txtPago_CardViewAbonos);
            fecha= (TextView) itemView.findViewById(R.id.txtFecha_cardviewAbonos);

            txtOptions= (TextView) itemView.findViewById(R.id.txtOptionMenuVistaAbonos);
        }
    }
}
