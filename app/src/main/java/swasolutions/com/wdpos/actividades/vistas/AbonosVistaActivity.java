package swasolutions.com.wdpos.actividades.vistas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.adaptadores.adaptadoresvistas.AbonosVistaAdapter;
import swasolutions.com.wdpos.base_de_datos.AbonosBD;
import swasolutions.com.wdpos.vo.clases_objeto.Abono;

public class AbonosVistaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonos_vista);

        ArrayList<Abono> abonos= new ArrayList<>();

        Toolbar toolbarAbonos = (Toolbar) findViewById(R.id.toolbarVistaAbonos);

        AbonosBD bdAbonos= new AbonosBD(getApplicationContext(),null,1);

        abonos= bdAbonos.abonosVista();

        bdAbonos.close();

        //Toast.makeText(getApplicationContext(),""+abonos.size(),Toast.LENGTH_SHORT).show();

        toolbarAbonos.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView recyclerViewAbonos = (RecyclerView) findViewById(R.id.recyclerViewAbonos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        recyclerViewAbonos.setLayoutManager(linearLayoutManager);

        //The adapter is instantiated to add a cardview for each object
        AbonosVistaAdapter adapter = new AbonosVistaAdapter(abonos,AbonosVistaActivity.this);
        recyclerViewAbonos.setAdapter(adapter);
    }
}
