package swasolutions.com.wdpos.actividades.sharedpreferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.base_de_datos.CategoriasBD;
import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.base_de_datos.UnidadesBD;
import swasolutions.com.wdpos.base_de_datos.WarehouseBD;
import swasolutions.com.wdpos.vo.clases_objeto.Warehouse;
import swasolutions.com.wdpos.vo.server.Categorias;
import swasolutions.com.wdpos.vo.server.GruposVendedor;
import swasolutions.com.wdpos.vo.server.Unidades;
import swasolutions.com.wdpos.vo.server.Warehouses;

public class ConfiguracionActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText txtLinkHosting,txtPing;
    private EditText txtNombreTienda,txtDireccionTienda,txtTelefonoTienda;
    private Spinner spinnerWarehouses;
    private ArrayList<String> Stringwarehouses;

    private WarehouseBD bdWarehouses;

    private static final String STRING_PREFERENCE= "solutions.swa.com.wdpos";

    private static final String PREFERENCE_HOSTING= "state.edit.hosting";
    private static final String PREFERENCE_HOSTING2= "state.edit.hosting.2";
    private static final String PREFERENCE_WAREHOUSE_ID= "state.edit.warehouse.id";
    private static final String PREFERENCE_PING= "state.edit.ping";
    private static final String PREFERENCE_TIENDA= "state.edit.tienda";

    private static final String PREFERENCE_LINK_HOSTING= "state.edit.link.hosting";
    private static final String PREFERENCE_NOMBRE_TIENDA= "state.edit.nombre.tienda";
    private static final String PREFERENCE_DIRECCION_TIENDA= "state.edit.direccion.tienda";
    private static final String PREFERENCE_TELEFONO_TIENDA= "state.edit.telefono.tienda";

    //ArrayAdapter para conectar el Spinner a nuestros recursos strings.xml
    protected ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        ArrayList<Warehouse> warehouses= new ArrayList<>();
        Stringwarehouses = new ArrayList<>();

        txtLinkHosting= (EditText) findViewById(R.id.txtLinkHosting);
        Button btnAgregarHosting= (Button) findViewById(R.id.btnGuardarDatosHosting);
        Button btnVolver= (Button) findViewById(R.id.btnVolver_configuracion);
        Button btnAgregarHosting2 = (Button) findViewById(R.id.btnGuardarPinYWare);
        spinnerWarehouses= (Spinner) findViewById(R.id.warehousesSpinner);
        btnAgregarHosting.setOnClickListener(this);
        btnVolver.setOnClickListener(this);
        btnAgregarHosting2.setOnClickListener(this);

        bdWarehouses = new WarehouseBD(getApplicationContext(), null, 1);
        warehouses= bdWarehouses.warehouses();


        Stringwarehouses.add("Seleccione");

        for(int i=0;i<warehouses.size();i++){
            Stringwarehouses.add(warehouses.get(i).getName());
        }

        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter(this,android.R.layout.simple_spinner_item,Stringwarehouses);
        spinnerWarehouses.setAdapter(adapter);

        txtNombreTienda= (EditText) findViewById(R.id.txtNombreTienda);
        txtDireccionTienda= (EditText) findViewById(R.id.txtDireccionTienda);
        txtTelefonoTienda= (EditText) findViewById(R.id.txtTelefonoTienda);
        Button btnAgregarDatosTienda= (Button) findViewById(R.id.btnGuardarDatosTienda);
        txtPing = (EditText) findViewById(R.id.txtPing_Configuracion);
        btnAgregarDatosTienda.setOnClickListener(this);

        if(getPreferenciaHosting(ConfiguracionActivity.this).equals("guardadas")){
            txtLinkHosting.setText(getLinkHosting(ConfiguracionActivity.this).toString());
        }

        if(getPreferenciaHosting2(ConfiguracionActivity.this).equals("guardadas")){
            spinnerWarehouses.setSelection(encontrarPosicion());
            txtPing.setText(""+getPreferenciaPing(ConfiguracionActivity.this));
        }

        if(getPreferenciaTienda(ConfiguracionActivity.this).equals("guardadas")){
            txtNombreTienda.setText(getNombreTienda(ConfiguracionActivity.this).toString());
            txtDireccionTienda.setText(getDireccionTienda(ConfiguracionActivity.this).toString());
            txtTelefonoTienda.setText(getTelefonoTienda(ConfiguracionActivity.this).toString());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private int encontrarPosicion() {

        int number= 0;
        for(int i=0; i<Stringwarehouses.size();i++){

            if(Stringwarehouses.get(i).
                    equals(bdWarehouses.obtenerNombre(ConfiguracionActivity.getPreferenciaWarehouseID(ConfiguracionActivity.this)))){
                number = i;
            }
        }
        return number;

    }

    /*
    private void obtenerLista() {

        Stringwarehouses.add("Seleccione");

        for(int i=0;i<warehouses.size();i++){
            Stringwarehouses.add(warehouses.get(i).getName());
            Log.d("warehousesString",warehouses.get(i).getName());
        }

    }
    */

    private void obtenerDatosExternos() {

        bdWarehouses = new WarehouseBD(getApplicationContext(), null, 1);
        bdWarehouses.eliminarWarehouses();

        GruposVendedorBD bdGruposVendedor = new GruposVendedorBD(getApplicationContext(),null,1);
        bdGruposVendedor.eliminarGruposVendedor();

        CategoriasBD bdCategorias= new CategoriasBD(getApplicationContext(),null,1);
        bdCategorias.eliminarCategorias();

        UnidadesBD bdUnidades= new UnidadesBD(getApplicationContext(),null,1);
        bdUnidades.eliminarUnidades();



        Warehouses warehouses= new Warehouses(getApplicationContext(),
                ConfiguracionActivity.getLinkHosting(ConfiguracionActivity.this).toString());
        warehouses.obtenerWarehouses();

        GruposVendedor gruposVendedor= new GruposVendedor(getApplicationContext(),
                ConfiguracionActivity.getLinkHosting(ConfiguracionActivity.this).toString());
        gruposVendedor.obtenerGruposVendedor();

        Categorias categorias= new Categorias(getApplicationContext(),
                ConfiguracionActivity.getLinkHosting(ConfiguracionActivity.this).toString());
        categorias.obtenerCategorias();

        Unidades unidades= new Unidades(getApplicationContext(),
                ConfiguracionActivity.getLinkHosting(ConfiguracionActivity.this).toString());
        unidades.obtenerUnidades();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnGuardarDatosHosting:
                if (txtLinkHosting.getText().toString().length() <= 0) {
                    txtLinkHosting.setError("Introduzca el link del hosting!");
                }else if(txtLinkHosting.getText().length()<=10){
                    txtLinkHosting.setError("No estas escribiendo bien el link, revisalo!");
                } else if(txtLinkHosting.getText().length()>=11){

                    String inicio= txtLinkHosting.getText().toString().substring(0,7);
                    Log.d("inicio",inicio);
                    if(!inicio.equalsIgnoreCase("http://")){
                        txtLinkHosting.setError("No olvide ingresar \"http://\" al inicio!");
                    }else{

                        guardarLinkHosting();
                        obtenerDatosExternos();
                        guardarPreferenciasHosting();
                        Toast.makeText(getApplicationContext(),"Dato guardado",Toast.LENGTH_SHORT).show();

                        finish();
                        Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);

                    }

                }
                break;

            case R.id.btnGuardarPinYWare:

                if(txtPing.toString().length()<=0){
                    txtPing.setError("Introduzca el id del warehouse");
                }else if(ConfiguracionActivity.getLinkHosting(ConfiguracionActivity.this).toString().length()<=0){
                    Toast.makeText(getApplicationContext(),"Primero guarde el link",Toast.LENGTH_SHORT).show();
                }else{
                    guardarWarehouseId();
                    guardarPing();
                    guardarPreferenciasHosting2();

                    Toast.makeText(getApplicationContext(),"Datos guardados",Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.btnGuardarDatosTienda:
                if (txtNombreTienda.getText().toString().length() <= 0) {
                    txtNombreTienda.setError("Introduzca el nombre de la tienda!");
                }else if (txtDireccionTienda.getText().toString().length() <= 0) {
                    txtDireccionTienda.setError("Introduzca la direccion de la tienda!");
                }else if (txtTelefonoTienda.getText().toString().length() <= 0) {
                    txtTelefonoTienda.setError("Introduzca el telefono de la tienda!");
                }else{
                    guardarNombreTienda();
                    guardarDireccionTienda();
                    guardarTelefonoTienda();
                    guardarPreferenciasTienda();
                    Toast.makeText(getApplicationContext(),"Datos guardados",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnVolver_configuracion:

                Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }

    }


    private void guardarPreferenciasHosting2() {

        SharedPreferences sharedPreferences= ConfiguracionActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_HOSTING2,"guardadas");
        edit.commit();
    }


    private void guardarPreferenciasHosting(){
        SharedPreferences sharedPreferences= ConfiguracionActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_HOSTING,"guardadas");
        edit.commit();
    }

    private void guardarPreferenciasTienda(){
        SharedPreferences sharedPreferences= ConfiguracionActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_TIENDA,"guardadas");
        edit.commit();
    }


    private void guardarLinkHosting(){
        SharedPreferences sharedPreferences= ConfiguracionActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_LINK_HOSTING,txtLinkHosting.getText().toString().toLowerCase());
        edit.commit();
    }

    private void guardarPing(){
        SharedPreferences sharedPreferences= ConfiguracionActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_PING,txtPing.getText().toString().toLowerCase());
        edit.commit();
    }

    private void guardarWarehouseId() {

        SharedPreferences sharedPreferences= ConfiguracionActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        int id= bdWarehouses.obtenerId(spinnerWarehouses.getSelectedItem().toString());
        edit.putInt(PREFERENCE_WAREHOUSE_ID,id);
        edit.commit();

    }

    private void guardarNombreTienda(){
        SharedPreferences sharedPreferences= ConfiguracionActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_NOMBRE_TIENDA,txtNombreTienda.getText().toString().toUpperCase());
        edit.commit();
    }

    private void guardarDireccionTienda(){
        SharedPreferences sharedPreferences= ConfiguracionActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_DIRECCION_TIENDA,txtDireccionTienda.getText().toString());
        edit.commit();
    }

    private void guardarTelefonoTienda(){
        SharedPreferences sharedPreferences= ConfiguracionActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_TELEFONO_TIENDA,txtTelefonoTienda.getText().toString());
        edit.commit();
    }


    public static String getPreferenciaHosting(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_HOSTING,""));
        return channel;
    }

    public static String getPreferenciaHosting2(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_HOSTING2,""));
        return channel;
    }

    public static int getPreferenciaWarehouseID(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        int channel = (sharedPreferences.getInt(PREFERENCE_WAREHOUSE_ID,0));
        return channel;
    }

    public static int getPreferenciaPing(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_PING,""));
        return Integer.parseInt(channel);
    }

    public static String getPreferenciaTienda(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_TIENDA,""));
        return channel;
    }

    public static void eliminarPreferencias(Context context){

        SharedPreferences settings = context.getSharedPreferences(
                STRING_PREFERENCE, Context.MODE_PRIVATE);
        settings.edit().clear().commit();

    }


    public static String getLinkHosting(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_LINK_HOSTING,""));
        return channel.toString();
    }

    public static String getNombreTienda(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_NOMBRE_TIENDA,""));
        return channel.toString();
    }

    public static String getDireccionTienda(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_DIRECCION_TIENDA,""));
        return channel.toString();
    }

    public static String getTelefonoTienda(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_TELEFONO_TIENDA,""));
        return channel.toString();
    }


}
