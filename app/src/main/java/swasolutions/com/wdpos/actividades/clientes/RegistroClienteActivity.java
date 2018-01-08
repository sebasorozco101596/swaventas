package swasolutions.com.wdpos.actividades.clientes;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.vendedores.LoginActivity;
import swasolutions.com.wdpos.base_de_datos.ClientesBD;
import swasolutions.com.wdpos.base_de_datos.ClientesCompletoBD;
import swasolutions.com.wdpos.base_de_datos.GruposVendedorBD;
import swasolutions.com.wdpos.vo.clases_objeto.GrupoVendedor;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class RegistroClienteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtNombre,txtCedula,txtDireccion,txtCiudad,txtEstado,
    txtPais,txtTelefono;
    private Button btnRegistrar;
    private Spinner spinnerGrupos;

    private ImageView imgPerfil;
    private ImageView imgPerfilAtras;

    private ClientesBD bdClientes;
    private ClientesCompletoBD bdClientesCompleto;

    private GruposVendedorBD bdGruposVendedor;
    public static SQLiteDatabase sqLiteDatabase;

    private ArrayList<String> stringGruposVendedor;
    private ArrayList<GrupoVendedor> gruposVendedor;

    /*
    Constantes que se usaran para el funcionamiento de la camara
     */

    private static String APP_DIRECTORY = "WDPOS/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PicturesApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int PHOTO_CODE_BACK= 201;
    private final int SELECT_PICTURE = 300;

    String mPath;

    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        view = findViewById(android.R.id.content);
        gruposVendedor= new ArrayList<>();

        float number= 400f;

        txtNombre= (EditText) this.findViewById(R.id.txtNombre_registro);
        txtCedula= (EditText) this.findViewById(R.id.txtCedula_registro);
        txtDireccion= (EditText) this.findViewById(R.id.txtDirecion_registro);
        txtCiudad= (EditText) this.findViewById(R.id.txtCiudad_registro);
        txtEstado= (EditText) this.findViewById(R.id.txtEstado_registro);
        txtPais= (EditText) this.findViewById(R.id.txtPais_registro);
        txtTelefono= (EditText) this.findViewById(R.id.txtTelefono_registro);
        spinnerGrupos= (Spinner) findViewById(R.id.spinnerGruposVendedor);
        btnRegistrar= (Button) this.findViewById(R.id.btnRegistrar_registro);
        imgPerfil= (ImageView) findViewById(R.id.imagePerfilAdelante_registroCliente);
        imgPerfilAtras = (ImageView) findViewById(R.id.imagePerfilAtras_registroCliente);
        imgPerfilAtras.setOnClickListener(this);
        imgPerfil.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);


        //imgPerfil= (ImageView) findViewById(R.id.imagePerfil_registroCliente);

        if(mayRequestStoragePermission()) {
            imgPerfil.setEnabled(true);
            imgPerfilAtras.setEnabled(true);
        }
        else {
            imgPerfil.setEnabled(false);
            imgPerfilAtras.setEnabled(false);
        }

        bdClientes= new ClientesBD(getApplicationContext(),"ClientesBD",null,1);
        bdClientesCompleto= new ClientesCompletoBD(getApplicationContext(),"ClientesCompletoBD",null,1);
        bdGruposVendedor= new GruposVendedorBD(getApplicationContext(),"GruposVendedorBD",null,1);
        sqLiteDatabase= bdClientes.getWritableDatabase();

        gruposVendedor= bdGruposVendedor.gruposVendedor();
        obtenerLista();

        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter(this,android.R.layout.simple_spinner_item,stringGruposVendedor);
        spinnerGrupos.setAdapter(adapter);


    }

    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(view, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    /*
    private void showOptions() {
        final CharSequence[] option = {"Tomar foto frontal","Tomar foto ", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegistroClienteActivity.this);
        builder.setTitle("Eleige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == "Tomar foto frontal"){
                    //openCamera();
                }else if(option[which] == "Elegir de galeria"){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }
    */

    private void openCamera(String lado) {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            String imageName = txtCedula.getText().toString()+"_"+lado+".jpg";
            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;


            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            if(lado=="Adelante")
            startActivityForResult(intent, PHOTO_CODE);
            else if(lado=="Atras")
                startActivityForResult(intent, PHOTO_CODE_BACK);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    imgPerfil.setRotation(90);
                    imgPerfil.setImageBitmap(bitmap);
                    break;

                case PHOTO_CODE_BACK:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmapAtras = BitmapFactory.decodeFile(mPath);
                    imgPerfilAtras.setRotation(90);
                    imgPerfilAtras.setImageBitmap(bitmapAtras);
                    break;

                case SELECT_PICTURE:
                    Uri path = data.getData();
                    imgPerfil.setImageURI(path);

                    File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
                    boolean isDirectoryCreated = file.exists();

                    if(!isDirectoryCreated)
                        isDirectoryCreated = file.mkdirs();

                    if(isDirectoryCreated) {
                        String imageName = txtNombre.getText().toString() + "_" + txtCedula.getText().toString() + ".jpg";
                        mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                                + File.separator + imageName;
                        File newFile = new File(mPath);
                        Uri.fromFile(newFile);
                    }
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(RegistroClienteActivity.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                imgPerfil.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistroClienteActivity.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }



    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.btnRegistrar_registro:

                if(verificarCampos()){

                    int idVendedor = Integer.parseInt(LoginActivity.getId(RegistroClienteActivity.this));
                    int id=bdClientes.contarFilas()*idVendedor +1;
                    String nombre= txtNombre.getText().toString();
                    String direccion= txtDireccion.getText().toString();
                    String telefono= txtTelefono.getText().toString();
                    String cedula= txtCedula.getText().toString();
                    String pais= txtPais.getText().toString();
                    String ciudad= txtCiudad.getText().toString();
                    String grupo = spinnerGrupos.getSelectedItem().toString();
                    String estado= txtEstado.getText().toString();

                    String idd= "" + id;
                    //cedula = cedula.replaceFirst ("^0*", "");

                    if(!bdClientes.existeCliente(cedula)){
                        bdClientes.agregarCliente(cedula,nombre,nombre,direccion,telefono,
                                "1",RegistroClienteActivity.this,bdGruposVendedor.obtenerId(grupo));
                        bdClientesCompleto.agregarCliente(idd,grupo,nombre,cedula,direccion,ciudad,estado
                                , pais,telefono,1);

                        Toast.makeText(getApplicationContext(),"Cliente: "+nombre+" fue registrado exitosamente"
                                ,Toast.LENGTH_SHORT).show();

                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Cliente ya existe",Toast.LENGTH_SHORT).show();
                    }

                }

                break;

            case R.id.imagePerfilAtras_registroCliente:

                if(verificarCampos()) openCamera("Adelante");

                break;

            case R.id.imagePerfilAdelante_registroCliente:

                if(verificarCampos()) openCamera("Atras");
                break;
            default:
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bdClientes.close();
        bdClientesCompleto.close();
    }

    private void obtenerLista() {

        stringGruposVendedor= new ArrayList<>();
        stringGruposVendedor.add("Seleccione el grupo");

        for(int i=0;i<gruposVendedor.size();i++){
            stringGruposVendedor.add(gruposVendedor.get(i).getName());
        }

    }

    public boolean verificarCampos(){

        boolean bandera= false;
        if(spinnerGrupos.getSelectedItem().toString().equals("Seleccione el grupo")){
            Toast.makeText(getApplicationContext(),"Seleccione un grupo",Toast.LENGTH_SHORT).show();
        }else if(txtNombre.getText().length()==0){
            txtNombre.setError("Diligencie el nombre");
        }else if(txtCedula.getText().length()==0){
            txtCedula.setError("Diligencie la cedula");
        }else if(txtDireccion.getText().length()==0){
            txtDireccion.setError("Diligencie la direccion");
        }else if(txtCiudad.getText().length()==0){
            txtCiudad.setError("Diligencie la ciudad");
        }else if(txtEstado.getText().length()==0){
            txtEstado.setError("Diligencie el estado");
        }else if(txtPais.getText().length()==0){
            txtPais.setError("Diligencie el pais");
        }else if(txtTelefono.getText().length()==0){
            txtTelefono.setError("Diligencie el telefono");
        }else{

            bandera= true;

        }

        return  bandera;
    }
}
