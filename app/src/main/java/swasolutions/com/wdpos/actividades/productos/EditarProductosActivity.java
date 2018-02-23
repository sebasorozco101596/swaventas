package swasolutions.com.wdpos.actividades.productos;

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
import swasolutions.com.wdpos.base_de_datos.CategoriasBD;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.base_de_datos.UnidadesBD;
import swasolutions.com.wdpos.vo.clases_objeto.Categoria;
import swasolutions.com.wdpos.vo.clases_objeto.Producto;
import swasolutions.com.wdpos.vo.clases_objeto.Unidad;

public class EditarProductosActivity extends AppCompatActivity {

    private int ID;
    private String NOMBRE;
    private int UNIT;
    private String COST;
    private String PRICE1;
    private int PRICE2;
    private int PRICE3;
    private int PRICE4;
    private int PRICE5;
    private int PRICE6;
    private int CATEGORYID;
    private String CODE;
    private String TYPE;

    private CategoriasBD bdCategorias;
    private UnidadesBD bdUnidades;
    private ProductosBD bdProductos;

    private EditText txtCodigo;
    private EditText txtNombre;
    private Spinner spinnerTipo;
    private Spinner spinnerUnidad;
    private Spinner spinnerCategoria;
    private EditText txtCosto;
    private EditText txtPrecio;
    private EditText txtPrecio2;
    private EditText txtPrecio3;
    private EditText txtPrecio4;
    private EditText txtPrecio5;
    private EditText txtPrecio6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_productos);

        ArrayList<String> stringCategorias= new ArrayList<>();
        ArrayList<String> stringUnidades= new ArrayList<>();
        ArrayList<String> stringTipos= new ArrayList<>();
        bdCategorias= new CategoriasBD(getApplicationContext(),null,1);
        bdUnidades= new UnidadesBD(getApplicationContext(),null,1);
        bdProductos= new ProductosBD(getApplicationContext(),null,1);

        ArrayList<Categoria> categorias= bdCategorias.categorias();
        ArrayList<Unidad> unidades = bdUnidades.unidades();

        /**
         * Se reciben los datos enviados desde el adaptador.
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null) {
            ID= bundle.getInt("key_idProducto");
            NOMBRE=bundle.getString("key_nombre");
            UNIT= bundle.getInt("key_unit");
            COST= bundle.getString("key_cost");
            PRICE1= bundle.getString("key_price");
            PRICE2= bundle.getInt("key_price2");
            PRICE3= bundle.getInt("key_price3");
            PRICE4= bundle.getInt("key_price4");
            PRICE5= bundle.getInt("key_price5");
            PRICE6= bundle.getInt("key_price6");
            CATEGORYID= bundle.getInt("key_categoryId");
            CODE= bundle.getString("key_code");
            TYPE= bundle.getString("key_type");
        }

        txtCodigo=(EditText) findViewById(R.id.txtCodigo_editarProducto);
        txtNombre= (EditText) findViewById(R.id.txtNombre_editarProducto);
        spinnerTipo= (Spinner) findViewById(R.id.spinnerTipo_editarProducto);
        spinnerUnidad= (Spinner) findViewById(R.id.spinnerUnidad_editarProducto);
        spinnerCategoria= (Spinner) findViewById(R.id.spinnerCategoria_editarProducto);
        txtCosto= (EditText) findViewById(R.id.txtCosto_editarProducto);
        txtPrecio= (EditText) findViewById(R.id.txtPrecio_editarProducto);
        txtPrecio2= (EditText) findViewById(R.id.txtPrecio2_editarProducto);
        txtPrecio3= (EditText) findViewById(R.id.txtPrecio3_editarProducto);
        txtPrecio4=(EditText) findViewById(R.id.txtPrecio4_editarProducto);
        txtPrecio5=(EditText) findViewById(R.id.txtPrecio5_editarProducto);
        txtPrecio6=(EditText) findViewById(R.id.txtPrecio6_editarProducto);
        Button btnEditar = (Button) findViewById(R.id.btnEditarProducto_editarProducto);

        txtCodigo.setText(CODE);
        txtNombre.setText(NOMBRE);
        txtCosto.setText(COST);
        txtPrecio.setText(""+PRICE1);
        txtPrecio2.setText(""+PRICE2);
        txtPrecio3.setText(""+PRICE3);
        txtPrecio4.setText(""+PRICE4);
        txtPrecio5.setText(""+PRICE5);
        txtPrecio6.setText(""+PRICE6);

        stringCategorias.add("Seleccione una categoria");
        stringUnidades.add("Seleccione una unidad");

        stringTipos.add("Seleccione un tipo");
        stringTipos.add("standard");
        stringTipos.add("combo");
        stringTipos.add("digital");
        stringTipos.add("servicio");

        for(int i=0;i<categorias.size();i++){
            stringCategorias.add(categorias.get(i).getNombre());
        }

        for(int i=0;i<unidades.size();i++){
            stringUnidades.add(unidades.get(i).getNombre());
        }

        ArrayAdapter adapterCategorias =
                new ArrayAdapter(this,android.R.layout.simple_spinner_item,stringCategorias);
        spinnerCategoria.setAdapter(adapterCategorias);


        ArrayAdapter<CharSequence> adapterUnidades =
                new ArrayAdapter(this,android.R.layout.simple_spinner_item,stringUnidades);
        spinnerUnidad.setAdapter(adapterUnidades);

        ArrayAdapter<CharSequence> adapterTipos =
                new ArrayAdapter(this,android.R.layout.simple_spinner_item,stringTipos);
        spinnerTipo.setAdapter(adapterTipos);

        spinnerCategoria.setSelection(encontrarCategoria(CATEGORYID,stringCategorias));
        spinnerTipo.setSelection(encontrarTipo(TYPE));
        spinnerUnidad.setSelection(encontrarUnidad(UNIT,stringUnidades));


        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= txtCodigo.getText().toString();
                if(bdProductos.existeCodigo(code,CODE)){
                    txtCodigo.setError("Ese codigo ya existe en otro producto");
                }else if(verificarCampos()){
                    String nombre= txtNombre.getText().toString();
                    int unit= bdUnidades.obtenerId(spinnerUnidad.getSelectedItem().toString());
                    String cost= txtCosto.getText().toString();
                    String precio1= txtPrecio.getText().toString();
                    int precio2= Integer.parseInt(txtPrecio2.getText().toString());
                    int precio3= Integer.parseInt(txtPrecio3.getText().toString());
                    int precio4= Integer.parseInt(txtPrecio4.getText().toString());
                    int precio5= Integer.parseInt(txtPrecio5.getText().toString());
                    int precio6= Integer.parseInt(txtPrecio6.getText().toString());
                    int categoryId= bdCategorias.obtenerId(spinnerCategoria.getSelectedItem().toString());
                    String type= spinnerTipo.getSelectedItem().toString();

                    Producto producto= new Producto(ID,nombre,unit,cost,precio1,precio2,precio3,precio4,
                            precio5,precio6,categoryId,type,code,"2",1);
                    bdProductos.editarProducto(producto,EditarProductosActivity.this);

                }


            }
        });


    }

    private int encontrarCategoria(int categoryid,ArrayList<String> categorias) {
        int number= 0;
        for(int i=0; i<categorias.size();i++){

            if(categorias.get(i).equals(bdCategorias.obtenerNombre(categoryid))){
                number = i;
            }
        }
        return number;
    }

    private int encontrarTipo(String type) {
        if("standard".equals(type)){
            return 1;
        }else if("combo".equals(type)){
            return 2;
        }else if("digital".equals(type)){
            return 3;
        }else if("servicio".equals(type)){
            return 4;
        }else{
            return 0;
        }
    }

    private int encontrarUnidad(int unitId,ArrayList<String> unidades) {
        int number= 0;
        for(int i=0; i<unidades.size();i++){

            if(unidades.get(i).equals(bdUnidades.obtenerNombre(unitId))){
                number = i;
            }
        }
        return number;
    }

    private boolean verificarCampos() {

        boolean bandera=false;

        if(txtNombre.getText().length()==0){
            txtNombre.setError("Diligencie el nombre");
        }else if(spinnerTipo.getSelectedItem().toString().equals("Seleccione un tipo")){
            Toast.makeText(getApplicationContext(),"Seleccione algun tipo",Toast.LENGTH_SHORT).show();
        }else if(txtCodigo.getText().length()==0){
            txtCodigo.setError("Diligencie el slug");
        }else if(spinnerUnidad.getSelectedItem().toString().equals("Seleccione una unidad")){
            Toast.makeText(getApplicationContext(),"Seleccione alguna unidad",Toast.LENGTH_SHORT).show();
        }else if(spinnerCategoria.getSelectedItem().toString().equals("Seleccione una categoria")){
            Toast.makeText(getApplicationContext(),"Seleccione alguna categoria",Toast.LENGTH_SHORT).show();
        }else if(txtCosto.getText().length()==0){
            txtCosto.setError("Diligencie el costo");
        }else if(txtPrecio.getText().length()==0){
            txtPrecio.setError("Diligencie el precio");
        }else if(txtPrecio2.getText().length()==0){
            txtPrecio2.setError("Diligencie el precio");
        }else if(txtPrecio3.getText().length()==0){
            txtPrecio3.setError("Diligencie el precio");
        }else if(txtPrecio4.getText().length()==0){
            txtPrecio4.setError("Diligencie el precio");
        }else if(txtPrecio5.getText().length()==0){
            txtPrecio5.setError("Diligencie el precio");
        }else if(txtPrecio6.getText().length()==0){
            txtPrecio6.setError("Diligencie el precio");
        }else{
            bandera= true;
        }
        return bandera;
    }
}
