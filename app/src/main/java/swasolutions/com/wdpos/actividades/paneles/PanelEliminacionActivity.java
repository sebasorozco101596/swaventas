package swasolutions.com.wdpos.actividades.paneles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.logica.Logica;

public class PanelEliminacionActivity extends AppCompatActivity {

    private Logica logica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_eliminacion);
        logica= new Logica();

        Button btnEliminarCliNuevos= (Button) findViewById(R.id.btnEliminarCliNuevos_panelEliminacion);
        Button btnEliminarTodo= (Button) findViewById(R.id.btnEliminarTodo_panelEliminacion);

        btnEliminarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logica.verificarContrasenia(PanelEliminacionActivity.this,null,
                        "eliminarTodo",null);
            }
        });

        btnEliminarCliNuevos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logica.verificarContrasenia(PanelEliminacionActivity.this,null,
                        "eliminarClientesNuevos",null);
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
