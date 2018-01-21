package swasolutions.com.wdpos.actividades.paneles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.notas.NotaAbonoActivity;
import swasolutions.com.wdpos.actividades.notas.NotaFacturaActivity;

public class PanelNotasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_notas);

        Button btnNotaFactura = (Button) findViewById(R.id.btnNotasVentas_PanelNotas);
        Button btnNotaAbono = (Button) findViewById(R.id.btnNotaAbonos_PanelNotas);

        btnNotaFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),NotaFacturaActivity.class);
                startActivity(intent);

            }
        });

        btnNotaAbono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),NotaAbonoActivity.class);
                startActivity(intent);

            }
        });

    }
}
