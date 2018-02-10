package swasolutions.com.wdpos.actividades.paneles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.clientes.ConfiguracionGruposClienteActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.SharedPreferences;
import swasolutions.com.wdpos.actividades.terminales.TerminalesActivity;
import swasolutions.com.wdpos.logica.Logica;

public class PanelConfiguracionActivity extends AppCompatActivity {

    private Logica logica;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_configuracion);

        logica= new Logica();
        link=ConfiguracionActivity.getLinkHosting(PanelConfiguracionActivity.this);

        Button btnTerminales= (Button) findViewById(R.id.btnTerminales_panelConfiguracion);
        Button btnNotas = (Button) findViewById(R.id.btnNotas_panelConfiguracion);
        Button btnAjustes = (Button) findViewById(R.id.btnAjustes_panelConfiguracion);
        Button btnGruposClientes= (Button) findViewById(R.id.btnGruposClientes_panelConfiguracion);
        Button btnEliminarTodo= (Button) findViewById(R.id.btnEliminarTodo_panelConfiguracion);

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
