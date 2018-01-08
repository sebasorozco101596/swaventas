package swasolutions.com.wdpos.adaptadores.adaptadoresvistas;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.vo.clases_objeto.ClienteCompleto;

/**
 * Created by sebas on 30/12/2017.
 */

public class ClientesNuevosVistaAdapter extends  RecyclerView.Adapter<ClientesNuevosVistaAdapter.ClientesViewHolder>{

    private List<ClienteCompleto> clientes;
    private Context context;
    private String TIPO;
    private String NICKNAME;
    private String ID;

    /**
     * Builder class
     * @param clientes
     */
    public ClientesNuevosVistaAdapter(List<ClienteCompleto> clientes, Context context,String nickname,
                           String id){

        this.clientes=clientes;
        this.context=context;
        this.NICKNAME=nickname;
        this.ID=id;



    }



    @Override
    public ClientesNuevosVistaAdapter.ClientesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_clientes,parent,false);
        return new ClientesNuevosVistaAdapter.ClientesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ClientesNuevosVistaAdapter.ClientesViewHolder holder, final int position) {

        holder.id.setText(""+clientes.get(position).getCedula());

        holder.nombre.setText(clientes.get(position).getNombre());
        holder.compania.setText(clientes.get(position).getNombre());
        holder.direccion.setText(clientes.get(position).getDireccion());
        holder.telefono.setText(clientes.get(position).getTelefono());

        holder.cardView.getBackground().setAlpha(0);


        holder.optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu =  new PopupMenu(context,holder.optionMenu);
                popupMenu.inflate(R.menu.menu_vista_cliente);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getItemId()){
                            case R.id.accion_subir_cliente:





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
}
