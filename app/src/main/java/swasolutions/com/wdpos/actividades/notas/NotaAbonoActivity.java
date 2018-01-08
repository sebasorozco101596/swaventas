package swasolutions.com.wdpos.actividades.notas;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.sharedpreferences.SharedPreferences;

public class NotaAbonoActivity extends AppCompatActivity {

    EditText txtNota;
    Button btnGuardarNota;
    Context context;

    public static View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_abono);

        txtNota = (EditText) findViewById(R.id.txtNotasAbono_NotaAbonos);
        btnGuardarNota = (Button) findViewById(R.id.btnGuardarNotaAbono_NotaAbono);
        context= NotaAbonoActivity.this;

        txtNota.setText(SharedPreferences.getPreferenciaNotaAbono(context).toString());

        view = findViewById(android.R.id.content);


        btnGuardarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nota = txtNota.getText().toString();
                SharedPreferences.guardarPreferenciaNotaAbono(context,nota);

                Snackbar.make(view.findViewById(android.R.id.content),"Nota guardada",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }
}
