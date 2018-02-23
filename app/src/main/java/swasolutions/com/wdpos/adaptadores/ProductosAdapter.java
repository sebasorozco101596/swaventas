package swasolutions.com.wdpos.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import swasolutions.com.wdpos.actividades.productos.EditarProductosActivity;
import swasolutions.com.wdpos.base_de_datos.CarritoBD;
import swasolutions.com.wdpos.base_de_datos.DevolucionesBD;
import swasolutions.com.wdpos.base_de_datos.PreciosGrupoBD;
import swasolutions.com.wdpos.vo.clases_objeto.Producto;

/*
 * Created by sebas on 24/06/2017.
 */

public class ProductosAdapter extends  RecyclerView.Adapter<ProductosAdapter.ProductosViewHolder>{

    private List<Producto> productos;
    private Context context;
    private String tipo;
    private String CEDULA;
    private int ID_GRUPO;

    /**
     * Builder class
     * @param productos productos que se mostraran
     */
    public ProductosAdapter(List<Producto> productos,Context context,String tipo,String CEDULA,int ID_GRUPO){

        this.productos=productos;
        this.context=context;
        this.tipo=tipo;
        this.CEDULA=CEDULA;
        this.ID_GRUPO=ID_GRUPO;
    }


    @Override
    public ProductosAdapter.ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_producto,parent,false);
        return new ProductosAdapter.ProductosViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ProductosAdapter.ProductosViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            holder.nombre.setText(productos.get(position).getNombre().toLowerCase());

            PreciosGrupoBD bdPrecios= new PreciosGrupoBD(context,null,1);

            ArrayList<Integer> preciosFijos= new ArrayList<>();
            preciosFijos.add(Integer.parseInt(productos.get(position).getPrecio1()));
            preciosFijos.add(productos.get(position).getPrecio2());
            preciosFijos.add(productos.get(position).getPrecio3());
            preciosFijos.add(productos.get(position).getPrecio4());
            preciosFijos.add(productos.get(position).getPrecio5());
            preciosFijos.add(productos.get(position).getPrecio6());

            ArrayList<Integer> precios= bdPrecios.precios(ID_GRUPO);

            if(bdPrecios.existeGrupo(ID_GRUPO)){
                holder.precio.setText(""+preciosFijos.get(precios.get(0)-1));
                holder.precio2.setText(""+preciosFijos.get(precios.get(1)-1));
            }else if(!bdPrecios.existeGrupo(ID_GRUPO)){
                holder.precio.setText(productos.get(position).getPrecio1());
                holder.precio2.setText(""+productos.get(position).getPrecio2());
            }
            holder.id.setText(""+productos.get(position).getId());
            holder.cantidad.setText(""+productos.get(position).getCantidad());
            holder.cardView.getBackground().setAlpha(0);

            preciosFijos.clear();


        holder.optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu =  new PopupMenu(context,holder.optionMenu);
                Menu menu= popupMenu.getMenu();
                popupMenu.inflate(R.menu.menuagregarcarrito);

                if("venta".equals(tipo)){
                    MenuItem menuVenta= menu.findItem(R.id.accion_agregarDevolucion);
                    menuVenta.setVisible(false);
                    MenuItem menuEditarProducto= menu.findItem(R.id.accion_editarProducto);
                    menuEditarProducto.setVisible(false);
                }else if("devolucion".equals(tipo)){
                    MenuItem menuPagar= menu.findItem(R.id.accion_agregarCarrito);
                    menuPagar.setVisible(false);
                    MenuItem menuEditarProducto= menu.findItem(R.id.accion_editarProducto);
                    menuEditarProducto.setVisible(false);
                }else if("editar".equals(tipo)){
                    MenuItem menuVenta= menu.findItem(R.id.accion_agregarDevolucion);
                    menuVenta.setVisible(false);
                    MenuItem menuPagar= menu.findItem(R.id.accion_agregarCarrito);
                    menuPagar.setVisible(false);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.accion_editarProducto:

                                Intent intentEditar= new Intent(context, EditarProductosActivity.class);
                                intentEditar.putExtra("key_idProducto",productos.get(position).getId());
                                intentEditar.putExtra("key_nombre",productos.get(position).getNombre());
                                intentEditar.putExtra("key_unit",productos.get(position).getUnit());
                                intentEditar.putExtra("key_cost",productos.get(position).getCost());
                                intentEditar.putExtra("key_price",productos.get(position).getPrecio());
                                intentEditar.putExtra("key_price2",productos.get(position).getPrecio2());
                                intentEditar.putExtra("key_price3",productos.get(position).getPrecio3());
                                intentEditar.putExtra("key_price4",productos.get(position).getPrecio4());
                                intentEditar.putExtra("key_price5",productos.get(position).getPrecio5());
                                intentEditar.putExtra("key_price6",productos.get(position).getPrecio6());
                                intentEditar.putExtra("key_categoryId",productos.get(position).getCategoryId());
                                intentEditar.putExtra("key_code",productos.get(position).getCodigoProducto());
                                intentEditar.putExtra("key_type",productos.get(position).getType());
                                context.startActivity(intentEditar);

                                break;

                            case R.id.accion_agregarCarrito:

                                CarritoBD carritoBD= new CarritoBD(context,null,1);

                                int id= Integer.parseInt(holder.id.getText().toString());
                                String nombre= holder.nombre.getText().toString();
                                int precio= Integer.parseInt(holder.precio.getText().toString());
                                int precio2= Integer.parseInt(holder.precio2.getText().toString());
                                String codigoProducto= productos.get(position).getCodigoProducto();

                                carritoBD.agregarProductoCarrito(id,nombre,precio,precio2,context,codigoProducto);

                                carritoBD.close();

                                break;

                            case R.id.accion_agregarDevolucion:


                                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                                @SuppressLint("InflateParams") View mView =  LayoutInflater.from(context).inflate(R.layout.dialog_tipo_devolucion,null);
                                builder.setView(mView);
                                final AlertDialog alertDialog= builder.create();
                                final RadioButton rdbNormal= (RadioButton) mView.findViewById(R.id.rdbNormal_dialogDevolucion);
                                final RadioButton rdbDeteriorado= (RadioButton) mView.findViewById(R.id.rdbDeteriorado_dialogDevolucion);
                                Button btnCompletar= (Button) mView.findViewById(R.id.btnAgregarDevolucion_dialogDevolucion);


                                btnCompletar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        DevolucionesBD devolucionesBD= new DevolucionesBD(context,null,1);

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

        private CardView cardView;

        private TextView id;
        private TextView nombre;
        private TextView precio;
        private TextView precio2;
        private TextView cantidad;
        private TextView optionMenu;

        private ProductosViewHolder(View itemView) {
            super(itemView);

            cardView= (CardView) itemView.findViewById(R.id.cardViewGroups);
            id= (TextView) itemView.findViewById(R.id.txtId_CardViewProductos);
            nombre= (TextView) itemView.findViewById(R.id.txtNombre_CardViewProductos);
            precio= (TextView) itemView.findViewById(R.id.txtPrecio_CardViewProductos);
            precio2= (TextView) itemView.findViewById(R.id.txtPrecio2_CardViewProductos);
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
