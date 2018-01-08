package swasolutions.com.wdpos.actividades.paneles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.clientes.ConfiguracionGruposClienteActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.actividades.terminales.TerminalesActivity;

public class PanelConfiguracionActivity extends AppCompatActivity {

    private Button btnTerminales, btnNotas,btnAjustes,btnGruposClientes,btnEliminarTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_configuracion);


        btnTerminales= (Button) findViewById(R.id.btnTerminales_panelConfiguracion);
        btnNotas = (Button) findViewById(R.id.btnNotas_panelConfiguracion);
        btnAjustes = (Button) findViewById(R.id.btnAjustes_panelConfiguracion);
        btnGruposClientes= (Button) findViewById(R.id.btnGruposClientes_panelConfiguracion);
        btnEliminarTodo= (Button) findViewById(R.id.btnEliminarTodo_panelConfiguracion);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbarPanelConfiguracion);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

                Intent intent = new Intent(getApplicationContext(),ConfiguracionActivity.class);
                startActivity(intent);

            }
        });

        btnEliminarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent= new Intent(getApplicationContext(),PanelEliminacionActivity.class);
                startActivity(intent);
            }
        });

    }
}
