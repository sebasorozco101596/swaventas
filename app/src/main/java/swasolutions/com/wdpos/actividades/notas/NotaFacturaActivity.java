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

public class NotaFacturaActivity extends AppCompatActivity {

    private EditText txtNota;
    private Button btnGuardarNota;
    private Context context;

    public static View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_factura);

        txtNota = (EditText) findViewById(R.id.txtNotaFactura_NotaFactura);
        btnGuardarNota = (Button) findViewById(R.id.btnGuardarNotaFactura_NotaFactura);
        context= NotaFacturaActivity.this;

        txtNota.setText(SharedPreferences.getPreferenciaNotaFactura(context).toString());

        view = findViewById(android.R.id.content);


        btnGuardarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nota = txtNota.getText().toString();
                SharedPreferences.guardarPreferenciaNotaFactura(context,nota);

                Snackbar.make(view.findViewById(android.R.id.content),"Nota guardada",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }
}
