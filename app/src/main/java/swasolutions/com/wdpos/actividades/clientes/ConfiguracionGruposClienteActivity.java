package swasolutions.com.wdpos.actividades.clientes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.sharedpreferences.SharedPreferences;
import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.vo.clases_objeto.GrupoVendedor;

public class ConfiguracionGruposClienteActivity extends AppCompatActivity implements View.OnClickListener{

    private GruposVendedorBD bdGruposVendedor;
    public static SQLiteDatabase sqLiteDatabase;

    private Button btnRegistrarGrupo1,btnRegistrarGrupo2,btnRegistrarGrupo3,btnRegistrarGrupo4
            ,btnRegistrarGrupo5, btnRegistrarGrupo6;
    private Spinner spinnerGrupo1,spinnerGrupo2,spinnerGrupo3,spinnerGrupo4,spinnerGrupo5
            ,spinnerGrupo6;

    private  ArrayList<String> stringGruposVendedor;
    private ArrayList<GrupoVendedor> gruposVendedor;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_grupos_cliente);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        view = findViewById(android.R.id.content);

        gruposVendedor= new ArrayList<>();
        Context context= ConfiguracionGruposClienteActivity.this;

        btnRegistrarGrupo1= (Button) findViewById(R.id.btnGuardarDatosGrupo1_confGrupo);
        btnRegistrarGrupo2= (Button) findViewById(R.id.btnGuardarDatosGrupo2_confGrupo);
        btnRegistrarGrupo3= (Button) findViewById(R.id.btnGuardarDatosGrupo3_confGrupo);
        btnRegistrarGrupo4= (Button) findViewById(R.id.btnGuardarDatosGrupo4_confGrupo);
        btnRegistrarGrupo5= (Button) findViewById(R.id.btnGuardarDatosGrupo5_confGrupo);
        btnRegistrarGrupo6= (Button) findViewById(R.id.btnGuardarDatosGrupo6_confGrupo);

        Switch aSwitchTodos= (Switch) findViewById(R.id.SwitchTodosLosGrupos_confGrupos);
        aSwitchTodos.setTextOn("SI"); // displayed text of the Switch whenever it is in checked or on state
        aSwitchTodos.setTextOff("NO");

        if(SharedPreferences.getPreferenciaTodosGrupos(ConfiguracionGruposClienteActivity.this).toString()
                .length()==0 || SharedPreferences.getPreferenciaTodosGrupos(ConfiguracionGruposClienteActivity.this).toString()
                == null){

            SharedPreferences.guardarPreferenciaGrupoTodos(ConfiguracionGruposClienteActivity.this,"no");

        } else if(SharedPreferences.getPreferenciaTodosGrupos(ConfiguracionGruposClienteActivity.this).toString()
                .equals("si")){
            aSwitchTodos.setChecked(true);
        }else if(SharedPreferences.getPreferenciaTodosGrupos(ConfiguracionGruposClienteActivity.this).toString()
                .equals("no")){
            aSwitchTodos.setChecked(false);
        }


        btnRegistrarGrupo1.setOnClickListener(this);
        btnRegistrarGrupo2.setOnClickListener(this);
        btnRegistrarGrupo3.setOnClickListener(this);
        btnRegistrarGrupo4.setOnClickListener(this);
        btnRegistrarGrupo5.setOnClickListener(this);
        btnRegistrarGrupo6.setOnClickListener(this);

        spinnerGrupo1= (Spinner) findViewById(R.id.spinnerGrupo1_confGrupo);
        spinnerGrupo2= (Spinner) findViewById(R.id.spinnerGrupo2_confGrupo);
        spinnerGrupo3= (Spinner) findViewById(R.id.spinnerGrupo3_confGrupo);
        spinnerGrupo4= (Spinner) findViewById(R.id.spinnerGrupo4_confGrupo);
        spinnerGrupo5= (Spinner) findViewById(R.id.spinnerGrupo5_confGrupo);
        spinnerGrupo6= (Spinner) findViewById(R.id.spinnerGrupo6_confGrupo);

        aSwitchTodos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    SharedPreferences.guardarPreferenciaGrupoTodos(ConfiguracionGruposClienteActivity.this,
                            "si");
                    btnRegistrarGrupo1.setEnabled(false);
                    btnRegistrarGrupo2.setEnabled(false);
                    btnRegistrarGrupo3.setEnabled(false);
                    btnRegistrarGrupo4.setEnabled(false);
                    btnRegistrarGrupo5.setEnabled(false);
                    btnRegistrarGrupo6.setEnabled(false);

                    spinnerGrupo1.setEnabled(false);
                    spinnerGrupo2.setEnabled(false);
                    spinnerGrupo3.setEnabled(false);
                    spinnerGrupo4.setEnabled(false);
                    spinnerGrupo5.setEnabled(false);
                    spinnerGrupo6.setEnabled(false);

                }else{
                    SharedPreferences.guardarPreferenciaGrupoTodos(ConfiguracionGruposClienteActivity.this,
                            "no");

                    btnRegistrarGrupo1.setEnabled(true);
                    btnRegistrarGrupo2.setEnabled(true);
                    btnRegistrarGrupo3.setEnabled(true);
                    btnRegistrarGrupo4.setEnabled(true);
                    btnRegistrarGrupo5.setEnabled(true);
                    btnRegistrarGrupo6.setEnabled(true);

                    spinnerGrupo1.setEnabled(true);
                    spinnerGrupo2.setEnabled(true);
                    spinnerGrupo3.setEnabled(true);
                    spinnerGrupo4.setEnabled(true);
                    spinnerGrupo5.setEnabled(true);
                    spinnerGrupo6.setEnabled(true);
                }
            }
        });

        if(aSwitchTodos.isChecked()){

            btnRegistrarGrupo1.setEnabled(false);
            btnRegistrarGrupo2.setEnabled(false);
            btnRegistrarGrupo3.setEnabled(false);
            btnRegistrarGrupo4.setEnabled(false);
            btnRegistrarGrupo5.setEnabled(false);
            btnRegistrarGrupo6.setEnabled(false);

            spinnerGrupo1.setEnabled(false);
            spinnerGrupo2.setEnabled(false);
            spinnerGrupo3.setEnabled(false);
            spinnerGrupo4.setEnabled(false);
            spinnerGrupo5.setEnabled(false);
            spinnerGrupo6.setEnabled(false);

        }else{

            btnRegistrarGrupo1.setEnabled(true);
            btnRegistrarGrupo2.setEnabled(true);
            btnRegistrarGrupo3.setEnabled(true);
            btnRegistrarGrupo4.setEnabled(true);
            btnRegistrarGrupo5.setEnabled(true);
            btnRegistrarGrupo6.setEnabled(true);

            spinnerGrupo1.setEnabled(true);
            spinnerGrupo2.setEnabled(true);
            spinnerGrupo3.setEnabled(true);
            spinnerGrupo4.setEnabled(true);
            spinnerGrupo5.setEnabled(true);
            spinnerGrupo6.setEnabled(true);

        }

        bdGruposVendedor= new GruposVendedorBD(getApplicationContext(),null,1);

        gruposVendedor= bdGruposVendedor.gruposVendedor();
        obtenerLista();

        ArrayAdapter<CharSequence> adapterGrupo =
                new ArrayAdapter(this,android.R.layout.simple_spinner_item,stringGruposVendedor);
        spinnerGrupo1.setAdapter(adapterGrupo);
        spinnerGrupo2.setAdapter(adapterGrupo);
        spinnerGrupo3.setAdapter(adapterGrupo);
        spinnerGrupo4.setAdapter(adapterGrupo);
        spinnerGrupo5.setAdapter(adapterGrupo);
        spinnerGrupo6.setAdapter(adapterGrupo);

        spinnerGrupo1.setSelection(encontrarPosicion(SharedPreferences.getPreferenciaGrupo1(context)));
        spinnerGrupo2.setSelection(encontrarPosicion(SharedPreferences.getPreferenciaGrupo2(context)));
        spinnerGrupo3.setSelection(encontrarPosicion(SharedPreferences.getPreferenciaGrupo3(context)));
        spinnerGrupo4.setSelection(encontrarPosicion(SharedPreferences.getPreferenciaGrupo4(context)));
        spinnerGrupo5.setSelection(encontrarPosicion(SharedPreferences.getPreferenciaGrupo5(context)));
        spinnerGrupo6.setSelection(encontrarPosicion(SharedPreferences.getPreferenciaGrupo6(context)));



    }

    private void obtenerLista() {

        stringGruposVendedor= new ArrayList<>();
        stringGruposVendedor.add("Seleccione el grupo");

        for(int i=0;i<gruposVendedor.size();i++){
            stringGruposVendedor.add(gruposVendedor.get(i).getName());
        }

    }

    private int encontrarPosicion(int numero) {

        int number= 0;
        for(int i=0; i<stringGruposVendedor.size();i++){

            if(stringGruposVendedor.get(i).
                    equals(bdGruposVendedor.obtenerNombre(numero))){
                        number = i;
            }
        }
        return number;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.btnGuardarDatosGrupo1_confGrupo:
                if(!spinnerGrupo1.getSelectedItem().toString().equals("Seleccione el grupo")){
                    SharedPreferences.guardarPreferenciaGrupo1(ConfiguracionGruposClienteActivity.this,
                            bdGruposVendedor.obtenerId(spinnerGrupo1.getSelectedItem().toString()));

                    Snackbar.make(view.findViewById(android.R.id.content),"Dato guardado",Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Seleccione un grupo",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnGuardarDatosGrupo2_confGrupo:
                if(!spinnerGrupo2.getSelectedItem().toString().equals("Seleccione el grupo")){
                    SharedPreferences.guardarPreferenciaGrupo2(ConfiguracionGruposClienteActivity.this,
                            bdGruposVendedor.obtenerId(spinnerGrupo2.getSelectedItem().toString()));
                    Snackbar.make(view.findViewById(android.R.id.content),"Dato guardado",Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Seleccione un grupo",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnGuardarDatosGrupo3_confGrupo:
                if(!spinnerGrupo3.getSelectedItem().toString().equals("Seleccione el grupo")){
                    SharedPreferences.guardarPreferenciaGrupo3(ConfiguracionGruposClienteActivity.this,
                            bdGruposVendedor.obtenerId(spinnerGrupo3.getSelectedItem().toString()));
                    Snackbar.make(view.findViewById(android.R.id.content),"Dato guardado",Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Seleccione un grupo",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnGuardarDatosGrupo4_confGrupo:
                if(!spinnerGrupo4.getSelectedItem().toString().equals("Seleccione el grupo")){
                    SharedPreferences.guardarPreferenciaGrupo4(ConfiguracionGruposClienteActivity.this,
                            bdGruposVendedor.obtenerId(spinnerGrupo4.getSelectedItem().toString()));
                    Snackbar.make(view.findViewById(android.R.id.content),"Dato guardado",Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Seleccione un grupo",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnGuardarDatosGrupo5_confGrupo:
                if(!spinnerGrupo5.getSelectedItem().toString().equals("Seleccione el grupo")){
                    SharedPreferences.guardarPreferenciaGrupo5(ConfiguracionGruposClienteActivity.this,
                            bdGruposVendedor.obtenerId(spinnerGrupo5.getSelectedItem().toString()));
                    Snackbar.make(view.findViewById(android.R.id.content),"Dato guardado",Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Seleccione un grupo",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnGuardarDatosGrupo6_confGrupo:
                if(!spinnerGrupo6.getSelectedItem().toString().equals("Seleccione el grupo")){
                    SharedPreferences.guardarPreferenciaGrupo6(ConfiguracionGruposClienteActivity.this,
                            bdGruposVendedor.obtenerId(spinnerGrupo6.getSelectedItem().toString()));
                    Snackbar.make(view.findViewById(android.R.id.content),"Dato guardado",Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Seleccione un grupo",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }

    }
}
