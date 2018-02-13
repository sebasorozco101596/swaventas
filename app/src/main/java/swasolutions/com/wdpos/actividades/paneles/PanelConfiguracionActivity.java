package swasolutions.com.wdpos.actividades.paneles;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.clientes.ConfiguracionGruposClienteActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.SharedPreferences;
import swasolutions.com.wdpos.actividades.terminales.TerminalesActivity;
import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.base_de_datos.PreciosGrupoBD;
import swasolutions.com.wdpos.logica.Logica;
import swasolutions.com.wdpos.vo.clases_objeto.GrupoVendedor;

public class PanelConfiguracionActivity extends AppCompatActivity {

    private Logica logica;
    private String link;
    private ArrayList<Integer> precios;
    private  ArrayList<String> stringGruposVendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_configuracion);

        precios= new ArrayList<>();
        logica= new Logica();
        link=ConfiguracionActivity.getLinkHosting(PanelConfiguracionActivity.this);

        Button btnTerminales= (Button) findViewById(R.id.btnTerminales_panelConfiguracion);
        Button btnNotas = (Button) findViewById(R.id.btnNotas_panelConfiguracion);
        Button btnAjustes = (Button) findViewById(R.id.btnAjustes_panelConfiguracion);
        Button btnGruposClientes= (Button) findViewById(R.id.btnGruposClientes_panelConfiguracion);
        Button btnEliminarTodo= (Button) findViewById(R.id.btnEliminarTodo_panelConfiguracion);
        Button btnPrecios= (Button) findViewById(R.id.btnPrecios_panelConfiguracion);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbarPanelConfiguracion);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Switch aSwitchImpresion= (Switch) findViewById(R.id.SwitchImprimir_panelConfiguracion);

        if(SharedPreferences.getPreferenciaImpresion(getApplicationContext())){
            aSwitchImpresion.setChecked(true);
        }else{
            aSwitchImpresion.setChecked(false);
        }

        aSwitchImpresion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.guardarPreferenciaImpresion(getApplicationContext(),true);
                }else{
                    SharedPreferences.guardarPreferenciaImpresion(getApplicationContext(),false);
                }
            }
        });


        btnPrecios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                precios.clear();

                final GruposVendedorBD bdGruposVendedor= new GruposVendedorBD(getApplicationContext(),null,1);;
                final PreciosGrupoBD bdPreciosGrupo= new PreciosGrupoBD(getApplicationContext(),null,1);;
                ArrayList<GrupoVendedor> gruposVendedor;
                gruposVendedor= bdGruposVendedor.gruposVendedor();

                stringGruposVendedor= new ArrayList<>();
                stringGruposVendedor.add("Seleccione el grupo");

                for(int i=0;i<gruposVendedor.size();i++){
                    stringGruposVendedor.add(gruposVendedor.get(i).getName());
                }


                AlertDialog.Builder builder= new AlertDialog.Builder(PanelConfiguracionActivity.this);
                @SuppressLint("InflateParams") View mView =  LayoutInflater.
                        from(PanelConfiguracionActivity.this).inflate(R.layout.dialog_precios,null);

                final CheckBox precio1= (CheckBox) mView.findViewById(R.id.cbPrecio1_precios);
                final CheckBox precio2= (CheckBox) mView.findViewById(R.id.cbPrecio2_precios);
                final CheckBox precio3= (CheckBox) mView.findViewById(R.id.cbPrecio3_precios);
                final CheckBox precio4= (CheckBox) mView.findViewById(R.id.cbPrecio4_precios);
                final CheckBox precio5= (CheckBox) mView.findViewById(R.id.cbPrecio5_precios);
                final CheckBox precio6= (CheckBox) mView.findViewById(R.id.cbPrecio6_precios);
                final Spinner grupos= (Spinner) mView.findViewById(R.id.spinnerGrupos_precios);


                ArrayAdapter<CharSequence> adapterGrupo =
                        new ArrayAdapter(PanelConfiguracionActivity.this,android.R.layout.simple_spinner_item,stringGruposVendedor);
                grupos.setAdapter(adapterGrupo);

                grupos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        precio1.setChecked(false);
                        precio2.setChecked(false);
                        precio3.setChecked(false);
                        precio4.setChecked(false);
                        precio5.setChecked(false);
                        precio6.setChecked(false);
                        precios.clear();
                        if(!("Seleccione el grupo".equals(grupos.getSelectedItem().toString())) &&
                                bdPreciosGrupo.existeGrupo(bdGruposVendedor.obtenerId(grupos.getSelectedItem().toString()))){
                            int idGrupo=bdGruposVendedor.obtenerId(grupos.getSelectedItem().toString());

                            if(bdPreciosGrupo.precios(idGrupo).get(0)==1 || bdPreciosGrupo.precios(idGrupo).get(1)==1){
                                precio1.setChecked(true);
                            }
                            if(bdPreciosGrupo.precios(idGrupo).get(0)==2 || bdPreciosGrupo.precios(idGrupo).get(1)==2){
                                precio2.setChecked(true);
                            }
                            if(bdPreciosGrupo.precios(idGrupo).get(0)==3 || bdPreciosGrupo.precios(idGrupo).get(1)==3){
                                precio3.setChecked(true);
                            }
                            if(bdPreciosGrupo.precios(idGrupo).get(0)==4 || bdPreciosGrupo.precios(idGrupo).get(1)==4){
                                precio4.setChecked(true);
                            }
                            if(bdPreciosGrupo.precios(idGrupo).get(0)==5 || bdPreciosGrupo.precios(idGrupo).get(1)==5){
                                precio5.setChecked(true);
                            }
                            if(bdPreciosGrupo.precios(idGrupo).get(0)==6 || bdPreciosGrupo.precios(idGrupo).get(1)==6){
                                precio6.setChecked(true);
                            }

                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                Button btnGuardarPrecios= (Button) mView.findViewById(R.id.btnGuardarPrecios_dialogPrecios);
                builder.setView(mView);

                precio1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            precios.add(1);
                        }else{
                            precios.remove(encontrarPosicion(1,precios));
                        }
                    }
                });

                precio2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            precios.add(2);
                            Toast.makeText(getApplicationContext(),"Seleccionado",Toast.LENGTH_SHORT).show();
                        }else{
                            precios.remove(encontrarPosicion(2,precios));
                            Toast.makeText(getApplicationContext(),"Desseleccionado "+precios.size(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                precio3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            precios.add(3);
                        }else{
                            precios.remove(encontrarPosicion(3,precios));
                            Toast.makeText(getApplicationContext(),"Desseleccionado "+precios.size(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                precio4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            precios.add(4);
                        }else{
                            precios.remove(encontrarPosicion(4,precios));
                            Toast.makeText(getApplicationContext(),"Desseleccionado "+precios.size(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                precio5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            precios.add(5);
                        }else{
                            precios.remove(encontrarPosicion(5,precios));
                            Toast.makeText(getApplicationContext(),"Desseleccionado "+precios.size(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                precio6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            precios.add(6);
                        }else{
                            precios.remove(encontrarPosicion(6,precios));
                            Toast.makeText(getApplicationContext(),"Desseleccionado "+precios.size(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                final AlertDialog alertDialog= builder.create();
                btnGuardarPrecios.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!("Seleccione el grupo".equals(grupos.getSelectedItem().toString()))){
                            if(precios.size()==2){
                                guardarPreferenciasPrecios(precios,bdGruposVendedor.obtenerId(grupos.getSelectedItem().toString()));
                                Toast.makeText(getApplicationContext(),"Precios guardados correctamente",
                                        Toast.LENGTH_SHORT).show();
                            }else if(precios.size()==1 || precios.size()==0){
                                Toast.makeText(getApplicationContext(),"Se deben seleccionar minimo 2 precios",
                                        Toast.LENGTH_SHORT).show();
                            }else if(precios.size()>2){
                                Toast.makeText(getApplicationContext(),"Solo se deben seleccionar 2 precios",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Debe seleccionar algun grupo",Toast.LENGTH_SHORT).show();
                        }

                    alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });

        btnTerminales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentTerminales = new Intent(getApplicationContext(), TerminalesActivity.class);
                startActivity(intentTerminales);
            }
        });

        btnGruposClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getApplicationContext(), ConfiguracionGruposClienteActivity.class);
                startActivity(intent);

            }
        });

        btnNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PanelNotasActivity.class);
                startActivity(intent);

            }
        });

        btnAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logica.verificarCodigoSecreto(PanelConfiguracionActivity.this,link,"btnAjustes");


            }
        });

        btnEliminarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logica.verificarCodigoSecreto(PanelConfiguracionActivity.this,link,"btnEliminar");


            }
        });

    }

    private int encontrarPosicion(int i,ArrayList<Integer> precios) {

        int numero=0;

        for(int j=0;j<precios.size();j++){
            if(precios.get(j)==i){
                numero=j;
            }
        }
        return numero;
    }

    private void guardarPreferenciasPrecios(ArrayList<Integer> precios,int grupo) {

        PreciosGrupoBD preciosGrupoBD= new PreciosGrupoBD(getApplicationContext(),null,1);

        if(!preciosGrupoBD.existeGrupo(grupo)){
            preciosGrupoBD.agregarPrecioGrupo(grupo,precios.get(0),precios.get(1));
        }else{
            preciosGrupoBD.actualizarPrecioGrupo(grupo, precios.get(0),precios.get(1));
        }
        precios.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
