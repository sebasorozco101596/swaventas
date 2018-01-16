package swasolutions.com.wdpos.actividades.sharedpreferences;

import android.content.Context;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sebas on 28/10/2017.
 */

public class SharedPreferences {


    private static final String PREFERENCE_NOTA_ABONO= "state.edit.nota.abono";
    private static final String PREFERENCE_NOTA_FACTURA= "state.edit.nota.factura";
    private static final String PREFERENCE_NUMERO_INTENTOS_FACTURA= "state.edit.numero.intentos.factura";

    private static final String PREFERENCE_GRUPO_1= "state.edit.grupo.1";
    private static final String PREFERENCE_GRUPO_2= "state.edit.grupo.2";
    private static final String PREFERENCE_GRUPO_3= "state.edit.grupo.3";
    private static final String PREFERENCE_GRUPO_4= "state.edit.grupo.4";
    private static final String PREFERENCE_GRUPO_5= "state.edit.grupo.5";
    private static final String PREFERENCE_GRUPO_6= "state.edit.grupo.6";
    private static final String PREFERENCE_GRUPO_TODOS= "state.edit.grupo.todos";

    private static final String PREFERENCE_IMPRESION="state.edit.impresion";


    /**
     * String donde se guardara el paquete de la aplicacion
     */
    private static final String STRING_PREFERENCE= "solutions.swa.com.wdpos";

    public static void guardarPreferenciaNotaAbono(Context context,String nota){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        android.content.SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_NOTA_ABONO,nota);
        edit.commit();
    }

    public static void guardarPreferenciaNumeroIntentos(Context context,int numeroIntentos){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        android.content.SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putInt(PREFERENCE_NUMERO_INTENTOS_FACTURA,numeroIntentos);
        edit.commit();
    }

    public static void guardarPreferenciaNotaFactura(Context context,String nota){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        android.content.SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_NOTA_FACTURA,nota);
        edit.commit();
    }

    public static void guardarPreferenciaGrupo1(Context context,int grupo){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        android.content.SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putInt(PREFERENCE_GRUPO_1,grupo);
        edit.commit();
    }

    public static void guardarPreferenciaGrupo2(Context context,int grupo){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        android.content.SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putInt(PREFERENCE_GRUPO_2,grupo);
        edit.commit();
    }

    public static void guardarPreferenciaGrupo3(Context context,int grupo){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        android.content.SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putInt(PREFERENCE_GRUPO_3,grupo);
        edit.commit();
    }

    public static void guardarPreferenciaGrupo4(Context context,int grupo){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        android.content.SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putInt(PREFERENCE_GRUPO_4,grupo);
        edit.commit();
    }

    public static void guardarPreferenciaGrupo5(Context context,int grupo){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        android.content.SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putInt(PREFERENCE_GRUPO_5,grupo);
        edit.commit();
    }

    public static void guardarPreferenciaGrupo6(Context context,int grupo){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        android.content.SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putInt(PREFERENCE_GRUPO_6,grupo);
        edit.commit();
    }

    public static void guardarPreferenciaGrupoTodos(Context context,String estado){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        android.content.SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(PREFERENCE_GRUPO_TODOS,estado);
        edit.commit();
    }

    public static void guardarPreferenciaImpresion(Context context,boolean estado){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(PREFERENCE_IMPRESION,estado).apply();
    }

    public static boolean getPreferenciaImpresion(Context context){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFERENCE_IMPRESION,false);
    }

    public static String getPreferenciaNotaAbono(Context context){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_NOTA_ABONO,""));
        return channel;
    }

    public static String getPreferenciaNotaFactura(Context context){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_NOTA_FACTURA,""));
        return channel;
    }

    public static int getPreferenciaNumeroIntentosFactura(Context context){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        int channel = (sharedPreferences.getInt(PREFERENCE_NUMERO_INTENTOS_FACTURA,0));
        return channel;
    }

    public static int getPreferenciaGrupo1(Context context){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        int channel = (sharedPreferences.getInt(PREFERENCE_GRUPO_1,0));
        return channel;
    }

    public static int getPreferenciaGrupo2(Context context){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        int channel = (sharedPreferences.getInt(PREFERENCE_GRUPO_2,0));
        return channel;
    }
    public static int getPreferenciaGrupo3(Context context){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        int channel = (sharedPreferences.getInt(PREFERENCE_GRUPO_3,0));
        return channel;
    }
    public static int getPreferenciaGrupo4(Context context){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        int channel = (sharedPreferences.getInt(PREFERENCE_GRUPO_4,0));
        return channel;
    }
    public static int getPreferenciaGrupo5(Context context){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        int channel = (sharedPreferences.getInt(PREFERENCE_GRUPO_5,0));
        return channel;
    }
    public static int getPreferenciaGrupo6(Context context){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        int channel = (sharedPreferences.getInt(PREFERENCE_GRUPO_6,0));
        return channel;
    }

    public static String getPreferenciaTodosGrupos(Context context){
        android.content.SharedPreferences sharedPreferences= context.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        String channel = (sharedPreferences.getString(PREFERENCE_GRUPO_TODOS,""));
        return channel;
    }


    public static void eliminarPreferencias(Context context) {

        android.content.SharedPreferences settings = context.getSharedPreferences(
                STRING_PREFERENCE, MODE_PRIVATE);
        settings.edit().clear().commit();

    }
}
