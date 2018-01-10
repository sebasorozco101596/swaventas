package swasolutions.com.wdpos.actividades.productos;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;
import swasolutions.com.wdpos.base_de_datos.CategoriasBD;
import swasolutions.com.wdpos.base_de_datos.ProductosBD;
import swasolutions.com.wdpos.base_de_datos.UnidadesBD;
import swasolutions.com.wdpos.vo.clases_objeto.Categoria;
import swasolutions.com.wdpos.vo.clases_objeto.Unidad;
import swasolutions.com.wdpos.vo.server.MySingleton;
import swasolutions.com.wdpos.vo.server.Productos;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CrearProductoActivity extends AppCompatActivity implements View.OnClickListener{

    private Bitmap bitmap;
    private String URL_SUBIR;

    private static String APP_DIRECTORY = "WDPOS/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "ProductosApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;


    private CategoriasBD bdCategorias;
    private ProductosBD bdProductos;

    private UnidadesBD bdUnidades;


    private  String mPath;

    private View view;
    private String link;

    private ImageView imgPerfil;
    private EditText txtSlug;
    private EditText txtNombre;
    private Spinner spinnerTipo;
    private Spinner spinnerUnidad;
    private Spinner spinnerCategoria;
    private EditText txtCosto;
    private EditText txtPrecio;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ArrayList<String> stringCategorias= new ArrayList<>();
        ArrayList<String> stringUnidades= new ArrayList<>();
        ArrayList<String> stringTipos= new ArrayList<>();
        bdCategorias= new CategoriasBD(getApplicationContext(),null,1);
        bdProductos= new ProductosBD(getApplicationContext(),null,1);
        bdUnidades= new UnidadesBD(getApplicationContext(),null,1);

        ArrayList<Categoria> categorias= bdCategorias.categorias();
        ArrayList<Unidad> unidades = bdUnidades.unidades();

        link= ConfiguracionActivity.getLinkHosting(CrearProductoActivity.this);
        bitmap=null;


        view = findViewById(android.R.id.content);
        context= CrearProductoActivity.this;

        txtSlug= (EditText) findViewById(R.id.txtSlug_crearProducto);
        txtNombre= (EditText) findViewById(R.id.txtNombre_crearProducto);
        spinnerTipo= (Spinner) findViewById(R.id.spinnerTipo_crearProducto);
        spinnerUnidad= (Spinner) findViewById(R.id.spinnerUnidad_crearProducto);
        spinnerCategoria= (Spinner) findViewById(R.id.spinnerCategoria_crearProducto);
        txtCosto= (EditText) findViewById(R.id.txtCosto_crearProducto);
        txtPrecio= (EditText) findViewById(R.id.txtPrecio_crearProducto);
        imgPerfil= (ImageView) findViewById(R.id.imageProducto_crearProducto);
        Button btnCrearProducto= (Button) findViewById(R.id.btnCrearProducto_crearProducto);
        imgPerfil.setOnClickListener(this);
        btnCrearProducto.setOnClickListener(this);


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








        URL_SUBIR= ConfiguracionActivity.getLinkHosting(CrearProductoActivity.this)+
                "/app_movil/vendedor/subirProducto.php";

        if(mayRequestStoragePermission()) {
            imgPerfil.setEnabled(true);
        }
        else {
            imgPerfil.setEnabled(false);
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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
                    bitmap = BitmapFactory.decodeFile(mPath);
                    imgPerfil.setRotation(90);
                    imgPerfil.setImageBitmap(bitmap);

                    bitmap= redimensionarImagenMaximo(bitmap,400,700);
                    break;
            }
        }
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

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            String imageName = txtSlug.getText().toString()+".jpg";
            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;


            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(CrearProductoActivity.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                imgPerfil.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CrearProductoActivity.this);
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

        switch (v.getId()){
            case R.id.imageProducto_crearProducto:
//
                if(verificarCampos()) openCamera();

                break;
            case R.id.btnCrearProducto_crearProducto:


                if(isNetDisponible() && isOnlineNet()) {

                    if(verificarCampos()){
                        final ProgressDialog loading = ProgressDialog.show(this,"Subiendo...",
                                "Espere por favor...",false,false);
                        StringRequest requestProducto = new StringRequest(Request.Method.POST, URL_SUBIR, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Disimissing the progress dialog

                                try {
                                    JSONObject jsonObject= new JSONObject(response);
                                    loading.dismiss();
                                    //Showing toast message of the response
                                    if(jsonObject.names().get(0).equals("success")){
                                        Toast.makeText(getApplicationContext(), "Producto subido correctamente",
                                                Toast.LENGTH_LONG).show();

                                        bdProductos.eliminarTodosProductos();


                                        int warehouseId= ConfiguracionActivity.getPreferenciaWarehouseID(CrearProductoActivity.this);

                                        Productos productos = new Productos(getApplicationContext(), link,warehouseId);
                                        productos.obtenerProductos();



                                        txtCosto.setText("");
                                        txtNombre.setText("");
                                        txtPrecio.setText("");
                                        txtSlug.setText("");
                                        spinnerCategoria.setSelection(0);
                                        spinnerTipo.setSelection(0);
                                        spinnerUnidad.setSelection(0);
                                        imgPerfil.setImageDrawable(getDrawable(R.drawable.camara));

                                    }else{
                                        loading.dismiss();
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                //Dismissing the progress dialog
                                loading.dismiss();

                                Toast.makeText(getApplicationContext(), "Producto subido correctamente..",
                                        Toast.LENGTH_LONG).show();

                                bdProductos.eliminarTodosProductos();


                                int warehouseId= ConfiguracionActivity.getPreferenciaWarehouseID(CrearProductoActivity.this);

                                Productos productos = new Productos(getApplicationContext(), link,warehouseId);
                                productos.obtenerProductos();


                                txtCosto.setText("");
                                txtNombre.setText("");
                                txtPrecio.setText("");
                                txtSlug.setText("");
                                spinnerCategoria.setSelection(0);
                                spinnerTipo.setSelection(0);
                                spinnerUnidad.setSelection(0);
                                imgPerfil.setImageDrawable(getDrawable(R.drawable.camara));
                                Log.d("error",error.toString());
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                String image ="null";
                                if(bitmap!=null){
                                    image=getStringImage(bitmap);
                                }

                                hashMap.put("codigo", generarAlfaNumericoAleatorio()+"MOV");
                                hashMap.put("nombre", txtNombre.getText().toString());
                                hashMap.put("tipo", spinnerTipo.getSelectedItem().toString());
                                hashMap.put("slug", txtSlug.getText().toString());
                                hashMap.put("unidades", ""+bdUnidades.obtenerId(spinnerUnidad.getSelectedItem().toString()));
                                hashMap.put("categoria",""+ bdCategorias.obtenerId(spinnerCategoria.getSelectedItem().toString()));
                                hashMap.put("costo", ""+txtCosto.getText().toString());
                                hashMap.put("precio", ""+txtPrecio.getText().toString());
                                hashMap.put("image", image);

                                Log.d("Subida",hashMap.toString());

                                return hashMap;
                            }
                        };

                        MySingleton.getInstance(context).addToRequestQue(requestProducto);

                    }




                }else{
                    Toast.makeText(getApplicationContext(),"Verifique la conexión a internet",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    public void mostrarSnockBar(String mensaje,int duracion){


        Snackbar.make(view.findViewById(android.R.id.content),mensaje,duracion)
                .setAction("Action", null).show();

    }

    private boolean verificarCampos() {

        boolean bandera=false;

        if(txtNombre.getText().length()==0){
            txtNombre.setError("Diligencie el nombre");
        }else if(spinnerTipo.getSelectedItem().toString().equals("Seleccione un tipo")){
            Toast.makeText(getApplicationContext(),"Seleccione algun tipo",Toast.LENGTH_SHORT).show();
        }else if(txtSlug.getText().length()==0){
            txtSlug.setError("Diligencie el slug");
        }else if(spinnerUnidad.getSelectedItem().toString().equals("Seleccione una unidad")){
            Toast.makeText(getApplicationContext(),"Seleccione alguna unidad",Toast.LENGTH_SHORT).show();
        }else if(spinnerCategoria.getSelectedItem().toString().equals("Seleccione una categoria")){
            Toast.makeText(getApplicationContext(),"Seleccione alguna categoria",Toast.LENGTH_SHORT).show();
        }else if(txtCosto.getText().length()==0){
            txtCosto.setError("Diligencie el costo");
        }else if(txtPrecio.getText().length()==0){
            txtPrecio.setError("Diligencie el precio");
        }else{
            bandera= true;
        }
        return bandera;
    }

    /**
     * Redimensionar un Bitmap. By TutorialAndroid.com
     * @return Bitmap
     */
    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

    public String generarAlfaNumericoAleatorio(){

        char[] elementos={'0','1','2','3','4','5','6','7','8','9','a',
                'b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t',
                'u','v','w','x','y','z','A', 'B','C','D','E','F','G','H','I','J','K','L','M',
                'N','O','P','Q','R','S','T',
                'U','V','W','X','Y','Z'};

        //char[] conjunto = new char[10];
        String pass="";

        for(int i=0;i<4;i++){
            int el = (int)(Math.random()*62);
            //conjunto[i] =  elementos[el];
            pass += elementos[el];
        }

        return pass;
    }

    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
