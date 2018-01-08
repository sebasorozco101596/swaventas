package swasolutions.com.wdpos.actividades.clientes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.ClientesCompletoBD;
import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.vo.clases_objeto.GrupoVendedor;

public class EditarClienteActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText txtNombre,txtDireccion,txtTelefono;
    private Button btnEditar;
    private Spinner spinnerGrupos;


    private GruposVendedorBD bdGruposVendedor;
    private ClientesBD bdClientes;
    private ClientesCompletoBD bdClientesCompleto;

    private ArrayList<GrupoVendedor> gruposVendedor;
    private  ArrayList<String> stringGruposVendedor;

    private String CEDULA;
    private int GROUP_ID;
    private String NOMBRE;
    private String DIRECCION;
    private String TELEFONO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cliente);

        gruposVendedor= new ArrayList<>();

        /**
         * Se reciben los datos enviados desde el cliente.
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null) {
            CEDULA = bundle.getString("key_cedula");
            NOMBRE= bundle.getString("key_nombre");
            GROUP_ID= bundle.getInt("key_grupo_id");
            DIRECCION= bundle.getString("key_direccion");
            TELEFONO= bundle.getString("key_telefono");
        }

        txtNombre= (EditText) this.findViewById(R.id.txtNombre_edicion);
        txtDireccion= (EditText) this.findViewById(R.id.txtDirecion_edicion);
        txtTelefono= (EditText) this.findViewById(R.id.txtTelefono_edicion);
        spinnerGrupos= (Spinner) findViewById(R.id.spinnerGruposVendedor_edicion);
        btnEditar= (Button) this.findViewById(R.id.btnRegistrar_edicion);
        btnEditar.setOnClickListener(this);


        txtDireccion.setText(DIRECCION);
        txtNombre.setText(NOMBRE);
        txtTelefono.setText(TELEFONO);

        bdClientes= new ClientesBD(getApplicationContext(),"ClientesBD",null,1);
        bdClientesCompleto= new ClientesCompletoBD(getApplicationContext(),"ClientesCompletoBD",null,1);
        bdGruposVendedor= new GruposVendedorBD(getApplicationContext(),"GruposVendedorBD",null,1);

        gruposVendedor= bdGruposVendedor.gruposVendedor();
        obtenerLista();

        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter(this,android.R.layout.simple_spinner_item,stringGruposVendedor);
        spinnerGrupos.setAdapter(adapter);

        spinnerGrupos.setSelection(encontrarPosicion(bdGruposVendedor.obtenerNombre(GROUP_ID)));
    }

    private int encontrarPosicion(String grupo) {

        int posicion=0;

        for(int i=0;i<gruposVendedor.size();i++){
            if(gruposVendedor.get(i).getName().toString().equals(grupo)){
                posicion=i+1;
            }
        }

        return posicion;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnRegistrar_edicion:

                int id=bdClientes.contarFilas()*10 +1;
                String grupo = spinnerGrupos.getSelectedItem().toString();
                String nombre= txtNombre.getText().toString();
                String direccion= txtDireccion.getText().toString();
                String telefono= txtTelefono.getText().toString();

                String idd= "" + id;

                bdClientesCompleto.agregarCliente(idd,grupo,nombre,CEDULA,direccion,"vacio","vacio"
                        , "vacio",telefono,2);

                //actualizar el cliente que ya estaba en la base de datos de clientes
                bdClientes.actualizarCliente(bdGruposVendedor.obtenerId(grupo),nombre,direccion,telefono,CEDULA);


                Toast.makeText(getApplicationContext(),"Cliente editado correctamente",Toast.LENGTH_SHORT).show();

                break;

        }

    }

    private void obtenerLista() {

        stringGruposVendedor= new ArrayList<>();
        stringGruposVendedor.add("Seleccione el grupo");

        for(int i=0;i<gruposVendedor.size();i++){
            stringGruposVendedor.add(gruposVendedor.get(i).getName());
        }

    }
}
