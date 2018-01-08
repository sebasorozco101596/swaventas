package swasolutions.com.wdpos.adaptadores;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.ventas.CarritoActivity;
import swasolutions.com.wdpos.base_de_datos.CarritoBD;
import swasolutions.com.wdpos.vo.clases_objeto.ProductoCarrito;

/**
 * Created by sebas on 24/06/2017.
 */

public class ProductosCarritoAdapter extends  RecyclerView.Adapter<ProductosCarritoAdapter.ProductosCarritoViewHolder>{

    private List<ProductoCarrito> productos;
    private Context context;
    private int total;
    public String tipo;

    /**
     * Builder class
     * @param productos
     */
    public ProductosCarritoAdapter(List<ProductoCarrito> productos,Context context,int total,String tipo){

        this.productos=productos;
        this.context=context;
        this.total=total;
        this.tipo=tipo;

    }


    @Override
    public ProductosCarritoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_productos_carrito, parent,false);
        return new ProductosCarritoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ProductosCarritoViewHolder holder, final int position) {

        if(tipo.equals("administrator") || tipo.equals("owner")){
            holder.precio.setEnabled(true);
            holder.btnCambiarPrecio.setVisibility(View.VISIBLE);
        }

        holder.precio.setEnabled(false);
        holder.precio.setEnabled(true);

        holder.nombre.setText(productos.get(position).getNombre().toLowerCase());
        holder.precio.setText(""+productos.get(position).getPrecio());
        holder.id.setText(""+productos.get(position).getId());
        holder.cantidad.setText(""+productos.get(position).getCantidad());
        holder.cardView.getBackground().setAlpha(0);

        int precio = Integer.parseInt(holder.precio.getText().toString());
        productos.get(position).setPrecio(precio);


        holder.agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CarritoBD carritoBD= new CarritoBD(context,"CarritoBD",null,1);

                int id= Integer.parseInt(holder.id.getText().toString());
                int precio= Integer.parseInt(holder.precio.getText().toString());
                int cantidad= Integer.parseInt(holder.cantidad.getText().toString());
                String codigoProducto= productos.get(position).getCodigoProducto();

                carritoBD.agregarProductoCarrito(id,holder.nombre.getText().toString(),precio,context,codigoProducto);

                holder.cantidad.setText(""+(cantidad+1));

                productos= carritoBD.cargarProductosCarrito();
                CarritoActivity.calcularTotal(productos);


            }
        });


        holder.btnCambiarPrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mView = li.inflate(R.layout.dialog_contrasenia,null);
                final EditText txtContrasenia= (EditText) mView.findViewById(R.id.txtContrasenia_DialogoContrasenia);
                Button btnEnviarContrasenia= (Button) mView.findViewById(R.id.btnEnviarContrasenia_contrasenia);
                builder.setView(mView);
                final AlertDialog alertDialog= builder.create();


                btnEnviarContrasenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(Integer.parseInt(txtContrasenia.getText().toString()) ==
                                ConfiguracionActivity.getPreferenciaPing(context))){
                            txtContrasenia.setError("Ping incorrecto");
                        } else {

                            if(soloNumeros(holder.precio.getText().toString())){

                                //Toast.makeText(context,holder.precio.getText().toString(),Toast.LENGTH_SHORT).show();

                                CarritoBD carritoBD= new CarritoBD(context,"CarritoBD",null,1);

                                int id= Integer.parseInt(holder.id.getText().toString());
                                int precio= Integer.parseInt(holder.precio.getText().toString());
                                int cantidad= Integer.parseInt(holder.cantidad.getText().toString());
                                String codigoProducto= productos.get(position).getCodigoProducto();

                                carritoBD.actualizarProductoCarrito(id,holder.nombre.getText().toString(),precio,context,codigoProducto);

                                holder.cantidad.setText(""+(cantidad));

                                productos= carritoBD.cargarProductosCarrito();
                                CarritoActivity.calcularTotal(productos);
                            }else{
                                holder.precio.setError("Solo se aceptan numeros");
                            }

                            alertDialog.dismiss();

                        }
                    }
                });


                alertDialog.show();






            }
        });

        holder.quitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CarritoBD carritoBD= new CarritoBD(context,"CarritoBD",null,1);

                int id= Integer.parseInt(holder.id.getText().toString());
                int cantidad= Integer.parseInt(holder.cantidad.getText().toString());

                if((cantidad-1)==0){

                    carritoBD.eliminarProductoCarrito(id,cantidad);
                    productos.remove(position);
                    notifyItemRemoved(position);
                    productos= carritoBD.cargarProductosCarrito();
                    CarritoActivity.calcularTotal(productos);

                }else {
                    carritoBD.eliminarProductoCarrito(id,cantidad);
                    holder.cantidad.setText(""+(cantidad-1));
                    productos= carritoBD.cargarProductosCarrito();
                    CarritoActivity.calcularTotal(productos);

                }
            }
        });

    }

    private boolean soloNumeros(String s) {

        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }



    static class ProductosCarritoViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        TextView id;
        TextView nombre;
        EditText precio;
        TextView cantidad;
        TextView agregar;
        TextView quitar;
        Button btnCambiarPrecio;

        public ProductosCarritoViewHolder(View itemView) {
            super(itemView);

            cardView= (CardView) itemView.findViewById(R.id.cardViewProductosCarro);
            id= (TextView) itemView.findViewById(R.id.txtId_CardViewProductosCarro);
            nombre= (TextView) itemView.findViewById(R.id.txtNombre_CardViewProductosCarro);
            precio= (EditText) itemView.findViewById(R.id.txtPrecio_CardViewProductosCarro);
            cantidad= (TextView) itemView.findViewById(R.id.txtCantidad_cardViewProductosCarro);
            agregar = (TextView) itemView.findViewById(R.id.txtAgregarProducto_carrito);
            quitar = (TextView) itemView.findViewById(R.id.txtEliminarProducto_carrito);
            btnCambiarPrecio = (Button) itemView.findViewById(R.id.btnCambiarPrecii_productosCarrito);

        }
    }

}
