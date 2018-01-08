package swasolutions.com.wdpos.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Pattern;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.facturas.FacturaAbonoActivity;
import swasolutions.com.wdpos.base_de_datos.CreditoBD;
import swasolutions.com.wdpos.vo.clases_objeto.Deuda;

/**
 * Created by sebas on 24/06/2017.
 */

public class DeudasAdapter extends  RecyclerView.Adapter<DeudasAdapter.DeudasViewHolder>{

    private List<Deuda> deudas;
    private Context context;
    private String ID;
    private String IDVENDEDOR;
    private String NICKNAME;

    private CreditoBD bdCredito;
    private boolean isActivatedRadioButton;

    /**
     * Builder class
     * @param deudas
     */
    public DeudasAdapter(List<Deuda> deudas, Context context,String id,String nickname,String idVendedor){

        this.deudas=deudas;
        this.context=context;
        this.ID=id;
        this.NICKNAME=nickname;
        this.IDVENDEDOR=idVendedor;

        bdCredito= new CreditoBD(context,null,null,1);

    }


    @Override
    public DeudasAdapter.DeudasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_deudas,parent,false);
        return new DeudasAdapter.DeudasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DeudasAdapter.DeudasViewHolder holder, final int position) {

        holder.id.setText(""+deudas.get(position).getId());
        holder.total.setText(deudas.get(position).getTotal());
        holder.fecha.setText(deudas.get(position).getFecha());
        holder.referencia.setText(deudas.get(position).getReferencia());
        holder.comprador.setText(deudas.get(position).getComprador());
        holder.pagado.setText(deudas.get(position).getPagado());
        holder.cardView.getBackground().setAlpha(0);

        int deuda= Integer.parseInt(String.valueOf(holder.total.getText()))-
                Integer.parseInt(String.valueOf(holder.pagado.getText()));

        holder.deuda.setText(""+deuda);


        holder.optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                PopupMenu popupMenu =  new PopupMenu(context,holder.optionMenu);
                popupMenu.inflate(R.menu.menudeudas);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getItemId()){
                            case R.id.accion_abonarDeuda:

                                final AlertDialog.Builder builder= new AlertDialog.Builder(context);
                                //LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View mView =  LayoutInflater.from(context).inflate(R.layout.dialog_pago,null);

                                final EditText txtPago= (EditText) mView.findViewById(R.id.txtPago_DialogoPago);
                                final TextView txtTotal= (TextView) mView.findViewById(R.id.txtTotalVenta_dialogo);
                                Button btnPago= (Button) mView.findViewById(R.id.btnPagar_dialogo);
                                final LinearLayout linearCredito= (LinearLayout) mView.findViewById(R.id.linearUsarCredito_DialogoPago);
                                final EditText txtValorCredito= (EditText) mView.findViewById(R.id.txtValorCredito_DialogoPago);
                                final CheckBox checkBoxCredito= (CheckBox) mView.findViewById(R.id.checkbox_usarCredito_diaPago);
                                int totalD= Integer.parseInt(holder.total.getText().toString());
                                final int pagadoD= Integer.parseInt(holder.pagado.getText().toString());

                                builder.setView(mView);
                                final AlertDialog alertDialog= builder.create();

                                txtTotal.setText(""+(totalD-pagadoD));

                                Log.d("idcliente", ""+deudas.get(position).getIdCliente());
                                String cedula= deudas.get(position).getIdCliente();

                                if(bdCredito.buscarCliente(cedula)){
                                    linearCredito.setVisibility(View.VISIBLE);
                                }

                                isActivatedRadioButton=checkBoxCredito.isChecked();

                                checkBoxCredito.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(isActivatedRadioButton){
                                            checkBoxCredito.setChecked(false);
                                            txtValorCredito.setVisibility(View.INVISIBLE);
                                        }

                                        isActivatedRadioButton= checkBoxCredito.isChecked();

                                        if(isActivatedRadioButton){
                                            txtValorCredito.setVisibility(View.VISIBLE);
                                        }

                                    }
                                });

                                final int credito= bdCredito.buscarValor(cedula);

                                txtValorCredito.setText(""+credito);

                                btnPago.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int pagar=0;
                                        int total= Integer.parseInt(txtTotal.getText().toString());

                                        if(soloNumeros(txtPago.getText().toString())){
                                            pagar= Integer.parseInt(txtPago.getText().toString());
                                        }else if(!soloNumeros(txtPago.getText().toString())){
                                            txtPago.setError("Solo se admiten numeros");
                                            return;
                                        }if (txtPago.getText().toString().length() <= 0) {
                                            txtPago.setError("Digite el pago !");
                                        } else if(pagar>total){
                                            txtPago.setError("Excede el precio total de la venta!");
                                        }else if(pagar<0){
                                            txtPago.setError("Valor negativo");
                                        }else if(Integer.parseInt(txtValorCredito.getText().toString())>credito){
                                            txtValorCredito.setError("No se puede pasar de: " + credito);
                                        }else if(checkBoxCredito.isChecked() && txtValorCredito.getText().toString().length()<=0){
                                            txtValorCredito.setError("Digite algun valor");
                                        }else if(Integer.parseInt(txtValorCredito.getText().toString())<0){
                                            txtValorCredito.setError("Valor negativo");
                                        }else if(checkBoxCredito.isChecked() &&
                                                ((Integer.parseInt(txtPago.getText().toString()) +
                                                Integer.parseInt(txtValorCredito.getText().toString()))>
                                                Integer.parseInt(txtTotal.getText().toString()))){
                                            txtPago.setError("El valor se excede del total a pagar");
                                        }

                                        else {

                                            Intent intent= new Intent(context,FacturaAbonoActivity.class);

                                            int credito=0;

                                            if(checkBoxCredito.isChecked()){
                                                credito=Integer.parseInt(txtValorCredito.getText().toString());
                                            }

                                            int pago= Integer.parseInt(txtPago.getText().toString()) + credito;

                                            intent.putExtra("key_fecha",holder.fecha.getText().toString());
                                            intent.putExtra("key_referencia",holder.referencia.getText().toString());
                                            intent.putExtra("key_comprador",holder.comprador.getText().toString());
                                            intent.putExtra("key_total",""+total);
                                            intent.putExtra("key_pagado",""+pago);
                                            intent.putExtra("key_id",deudas.get(position).getId());
                                            intent.putExtra("key_idCliente",ID);
                                            intent.putExtra("key_vendedor",NICKNAME);
                                            intent.putExtra("key_idVendedor",IDVENDEDOR); //ACA AGREGUE
                                            intent.putExtra("key_pagadoTotal",""+pagadoD);
                                            intent.putExtra("key_credito",credito);


                                            alertDialog.dismiss();
                                            context.startActivity(intent);

                                            ((Activity)context).finish();


                                        }
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

    private boolean soloNumeros(String name) {
        Pattern patron = Pattern.compile("^[0-9]+$");
        if (!patron.matcher(name).matches() || name.length() > 25) {
            return false;
        }

        return true;
    }

    @Override
    public int getItemCount() {
        return deudas.size();
    }




    static class DeudasViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        TextView id;
        TextView total;
        TextView pagado;
        TextView referencia;
        TextView fecha;
        TextView comprador;
        TextView optionMenu;
        TextView deuda;

        public DeudasViewHolder(View itemView) {
            super(itemView);

            cardView= (CardView) itemView.findViewById(R.id.cardViewDeudas);
            id= (TextView) itemView.findViewById(R.id.txtId_CardViewDeudas);
            total= (TextView) itemView.findViewById(R.id.txtTotal_CardViewDeudas);
            fecha= (TextView) itemView.findViewById(R.id.txtFecha_CardViewDeudas);
            referencia= (TextView) itemView.findViewById(R.id.txtReferencia_CardViewDeudas);
            comprador= (TextView) itemView.findViewById(R.id.txtCliente_CardViewDeudas);
            pagado= (TextView) itemView.findViewById(R.id.txtPagado_CardViewDeudas);
            optionMenu= (TextView) itemView.findViewById(R.id.txtOptionMenuDeudas);
            deuda= (TextView) itemView.findViewById(R.id.txtDeuda_CardViewDeudas);

        }
    }

}
