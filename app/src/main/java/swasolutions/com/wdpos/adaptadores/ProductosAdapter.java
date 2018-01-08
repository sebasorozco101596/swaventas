package swasolutions.com.wdpos.adaptadores;

import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.base_de_datos.CarritoBD;
import swasolutions.com.wdpos.base_de_datos.DevolucionesBD;
import swasolutions.com.wdpos.vo.clases_objeto.Producto;

/**
 * Created by sebas on 24/06/2017.
 */

public class ProductosAdapter extends  RecyclerView.Adapter<ProductosAdapter.ProductosViewHolder>{

    private List<Producto> productos;
    private Context context;
    private String tipo;
    private String CEDULA;

    private CarritoBD carritoBD;
    private DevolucionesBD devolucionesBD;

    /**
     * Builder class
     * @param productos
     */
    public ProductosAdapter(List<Producto> productos,Context context,String tipo,String CEDULA){

        this.productos=productos;
        this.context=context;
        this.tipo=tipo;
        this.CEDULA=CEDULA;

    }


    @Override
    public ProductosAdapter.ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_producto,parent,false);
        return new ProductosAdapter.ProductosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductosAdapter.ProductosViewHolder holder, final int position) {

            holder.nombre.setText(productos.get(position).getNombre().toLowerCase());
            holder.precio.setText(productos.get(position).getPrecio());
            holder.id.setText(""+productos.get(position).getId());
            holder.cantidad.setText(""+productos.get(position).getCantidad());
            holder.cardView.getBackground().setAlpha(0);




        holder.optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu =  new PopupMenu(context,holder.optionMenu);
                Menu menu= popupMenu.getMenu();
                popupMenu.inflate(R.menu.menuagregarcarrito);

                if(tipo.equals("venta")){
                    MenuItem menuVenta= menu.findItem(R.id.accion_agregarDevolucion);
                    menuVenta.setVisible(false);
                }else if(tipo.equals("devolucion")){
                    MenuItem menuPagar= menu.findItem(R.id.accion_agregarCarrito);
                    menuPagar.setVisible(false);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.accion_agregarCarrito:

                                carritoBD= new CarritoBD(context,"CarritoBD",null,1);

                                int id= Integer.parseInt(holder.id.getText().toString());
                                String nombre= holder.nombre.getText().toString();
                                int precio= Integer.parseInt(holder.precio.getText().toString());
                                String codigoProducto= productos.get(position).getCodigoProducto();

                                carritoBD.agregarProductoCarrito(id,nombre,precio,context,codigoProducto);

                                carritoBD.close();

                                break;

                            case R.id.accion_agregarDevolucion:


                                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                                View mView =  LayoutInflater.from(context).inflate(R.layout.dialog_tipo_devolucion,null);
                                builder.setView(mView);
                                final AlertDialog alertDialog= builder.create();
                                final RadioButton rdbNormal= (RadioButton) mView.findViewById(R.id.rdbNormal_dialogDevolucion);
                                final RadioButton rdbDeteriorado= (RadioButton) mView.findViewById(R.id.rdbDeteriorado_dialogDevolucion);
                                Button btnCompletar= (Button) mView.findViewById(R.id.btnAgregarDevolucion_dialogDevolucion);


                                btnCompletar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        devolucionesBD= new DevolucionesBD(context,"DevolucionesBD",null,1);

                                        int precioD= Integer.parseInt(holder.precio.getText().toString());
                                        String nombreD= holder.nombre.getText().toString();
                                        String codigoProductoD= productos.get(position).getCodigoProducto();

                                        if(rdbNormal.isChecked()){
                                            tipo= "normal";
                                        }else if(rdbDeteriorado.isChecked()){
                                            tipo= "deterioro";
                                        }

                                        devolucionesBD.agregarDevolucion(CEDULA,precioD,codigoProductoD,nombreD,context,tipo);

                                        devolucionesBD.close();

                                        alertDialog.dismiss();

                                    }
                                });


                                alertDialog.show();



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
        return productos.size();
    }




    static class ProductosViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        TextView id;
        TextView nombre;
        TextView precio;
        TextView cantidad;
        TextView optionMenu;

        public ProductosViewHolder(View itemView) {
            super(itemView);

            cardView= (CardView) itemView.findViewById(R.id.cardViewGroups);
            id= (TextView) itemView.findViewById(R.id.txtId_CardViewProductos);
            nombre= (TextView) itemView.findViewById(R.id.txtNombre_CardViewProductos);
            precio= (TextView) itemView.findViewById(R.id.txtPrecio_CardViewProductos);
            optionMenu= (TextView) itemView.findViewById(R.id.txtOptionMenu);
            cantidad= (TextView) itemView.findViewById(R.id.txtCantidad_CardViewProductos);

        }
    }

    public void setFilter(ArrayList<Producto> nuevaListaProductos) {

        productos= new ArrayList<>();
        productos.addAll(nuevaListaProductos );
        notifyDataSetChanged();

    }

}
