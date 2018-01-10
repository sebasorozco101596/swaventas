package swasolutions.com.wdpos.actividades.vendedores;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import swasolutions.com.wdpos.R;
import swasolutions.com.wdpos.actividades.paneles.PanelActivity;
import swasolutions.com.wdpos.actividades.sharedpreferences.ConfiguracionActivity;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView txtTitulo;
    private EditText txtEmail;

    private StringRequest requestLogin;
    private RequestQueue requestQueue;

    private String URLUser;

    private String tipoUsuario;

    private String idVendedor;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;

    private CheckBox rdbNotCloseSesion;

    private boolean isActivatedRadioButton;


    //Attributes in charge of maintaining the active session
    private static final String STRING_PREFERENCE= "solutions.swa.com.swausers";
    private static final String PREFERENCE_STATE_SESSION= "state.button.session";
    private static final String PREFERENCE_USERNAME= "state.edit.username";
    private static final String PREFERENCE_USERNAME_TYPE= "state.edit.username.type";
    private static final String PREFERENCE_IDVENDEDOR= "state.edit.id.vendedor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R
                .layout.activity_login);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        String link= ConfiguracionActivity.getLinkHosting(LoginActivity.this);
        URLUser = link+"/app_movil/vendedor/login_usuario.php";


        if(getStateButton()){
            Intent intentCo = new Intent(LoginActivity.this, PanelActivity.class);
            intentCo.putExtra("key_nickname",getUserName(LoginActivity.this).toString());
            intentCo.putExtra("key_id",getId(LoginActivity.this).toString());

            startActivity(intentCo);
            finish();
        }

        pedirPermitos();

        int permissionCheck = ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionCheck<0){
            pedirPermitos();
        }

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbar);
        btnLogin= (Button) findViewById(R.id.btnIniciarSesion);
        txtTitulo= (TextView) findViewById(R.id.txtTituloApp);
        rdbNotCloseSesion= (CheckBox) findViewById(R.id.rdbNoCerrarSesion);
        txtEmail= (EditText) findViewById(R.id.txtEmail_Login);

        requestQueue = Volley.newRequestQueue(this);

        isActivatedRadioButton=rdbNotCloseSesion.isChecked();

        rdbNotCloseSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isActivatedRadioButton){

                    rdbNotCloseSesion.setChecked(false);

                }

                isActivatedRadioButton= rdbNotCloseSesion.isChecked();

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ConfiguracionActivity.getNombreTienda(LoginActivity.this).length()>0 &&
                        ConfiguracionActivity.getDireccionTienda(LoginActivity.this).length()>0 &&
                        ConfiguracionActivity.getTelefonoTienda(LoginActivity.this).length()>0 &&
                        ConfiguracionActivity.getLinkHosting(LoginActivity.this).length()>0){


                    if (txtEmail.getText().toString().length() <= 0) {
                        txtEmail.setError("Please Enter Email !");
                    }else {

                        requestLogin = new StringRequest(Request.Method.POST, URLUser, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject jsonObject = new JSONObject(response);

                                    if (jsonObject.names().get(0).equals("success user")) {

                                        btnLogin.setEnabled(false);

                                        String cadena= jsonObject.getString("success user");

                                        String campos[] = cadena.split(",");

                                        String id= campos[0];
                                        //String email= campos[1];
                                        tipoUsuario= campos [2];

                                        idVendedor=id;

                                        Intent intent = new Intent(getApplicationContext(),PanelActivity.class);
                                        saveUserName();
                                        saveStateRadioButton();
                                        saveID();
                                        saveUserType();
                                        intent.putExtra("key_nickname",txtEmail.getText().toString());
                                        intent.putExtra("key_id",id);
                                        startActivity(intent);
                                        finish();

                                    } else {

                                        btnLogin.setEnabled(true);
                                        Toast.makeText(getApplicationContext(), "Error" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(),"Verifique su conexión a internet",Toast.LENGTH_SHORT).show();
                                Log.d("error",""+error);
                                finish();
                                Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                                btnLogin.setEnabled(true);

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("email", txtEmail.getText().toString().toLowerCase());

                                return hashMap;
                            }
                        };

                        requestQueue.add(requestLogin);

                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Llene todos los campos correctamente",
                            Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(getApplicationContext(),ConfiguracionActivity.class);
                    startActivity(intent);
                }



            }
        });

    }




    /**
     * Method that saves the state of the button, in case the user wants to keep the session active.
     */
    public void saveStateRadioButton(){
        SharedPreferences sharedPreferences= LoginActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(PREFERENCE_STATE_SESSION,rdbNotCloseSesion.isChecked()).apply();
    }

    /**
     * Method that obtains the state of the button. When you log in again.
     * @return
     */
    private boolean getStateButton(){
        SharedPreferences sharedPreferences= LoginActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFERENCE_STATE_SESSION,false);
    }

    /**
     * Method that changes the state of the button, to perform a logout.
     * @param context
     * @param state
     */
    public static void changeStateButton(Context context, boolean state){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(PREFERENCE_STATE_SESSION,state).apply();
    }

    /**
     * The user's nickname is saved.
     */
    private void saveUserName(){
        SharedPreferences sharedPreferences= LoginActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_USERNAME,txtEmail.getText().toString());
        edit.commit();
    }

    /**
     * The user's nickname is saved.
     */
    private void saveUserType(){
        SharedPreferences sharedPreferences= LoginActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_USERNAME_TYPE,tipoUsuario);
        edit.commit();
    }

    /**
     * The user's nickname is saved.
     */
    private void saveID(){
        SharedPreferences sharedPreferences= LoginActivity.this.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_IDVENDEDOR,idVendedor);
        edit.commit();
    }

    /**
     * Method that obtains the nickname of the user.
     * @return
     */
    public static String getUserName(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_USERNAME,""));
        return channel;
    }

    /**
     * Method that obtains the nickname of the user.
     * @return
     */
    public static String getUserType(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_USERNAME_TYPE,""));
        return channel;
    }

    /**
     * Method that obtains the nickname of the user.
     * @return
     */
    public static String getId(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_IDVENDEDOR,""));
        return channel;
    }

    /**
     * The chats menu is displayed
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menulogin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Actions are handled by pressing the menu buttons
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_configuracion:

                finish();

                Intent intent = new Intent(getApplicationContext(), ConfiguracionActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if(ConfiguracionActivity.getPreferenciaTienda(LoginActivity.this).equals("guardadas")&&
                ConfiguracionActivity.getNombreTienda(LoginActivity.this).length()>=0){
            txtTitulo.setText(ConfiguracionActivity.getNombreTienda(LoginActivity.this));
        }

    }

    public void pedirPermitos(){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Log.d("LoginActivity", "onRequestPermissionsResult: existen los permisos");

                } else {

                    pedirPermitos();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            default:
                Log.d("aqui", "onRequestPermissionsResult: entre default");
                break;

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static void eliminarPreferencias(Context context) {

        SharedPreferences settings = context.getSharedPreferences(
                STRING_PREFERENCE, Context.MODE_PRIVATE);
        settings.edit().clear().commit();

    }
}
