package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.ClienteCompleto;

/**
 * Created by sebas on 17/08/2017.
 */

public class ClientesCompletoBD extends SQLiteOpenHelper {

    private String creacionBD="CREATE TABLE `ClientesCompleto` " +
            "( `id` INTEGER PRIMARY KEY AUTOINCREMENT,`grupo` TEXT NOT NULL, `nombre` TEXT NOT NULL," +
            " `cedula` TEXT NOT NULL, `direccion` TEXT NOT NULL,`ciudad` TEXT NOT NULL ,`estado` TEXT NOT NULL," +
            "`pais` TEXT NOT NULL,`telefono` TEXT NOT NULL,`tipo` INTEGER NOT NULL)";

    //,`tipo` INTEGER NOT NULL
    public ClientesCompletoBD(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "ClientesCompletoBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(creacionBD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void agregarCliente(String id, String grupo, String nombre, String cedula, String direccion,
                               String ciudad, String estado, String pais,
                               String telefono,int tipo) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsertarCliente= "INSERT INTO ClientesCompleto (id,grupo,nombre,cedula,direccion,ciudad," +
                "estado,pais,telefono,tipo) VALUES ("+null+",'"+grupo+"','"+nombre+"','"+
                cedula+"','"+direccion +"','"+ciudad+"','"+estado+"','"+pais+"','"+
                telefono+"',"+tipo+");";
        database.execSQL(queryInsertarCliente);

    }

    public ArrayList<ClienteCompleto> clientes() {

        //List where all messages will be saved
        ArrayList<ClienteCompleto> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();



        //Query that gets messages by group
        String query= "SELECT c.id,c.grupo,c.nombre,c.cedula,c.direccion,c.ciudad,c.estado," +
                "c.pais,c.telefono,c.tipo FROM ClientesCompleto c";

        //,c.tipo

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                String id=register.getString(0);
                String grupo=register.getString(1);
                String nombre= register.getString(2);
                String cedula=register.getString(3);
                String direccion= register.getString(4);
                String ciudad= register.getString(5);
                String estado= register.getString(6);
                String pais= register.getString(7);
                String telefono= register.getString(8);
                int tipo= register.getInt(9);

                Log.d("entre","entre"+nombre);

                lista.add(new ClienteCompleto(id,grupo,nombre,cedula,direccion,ciudad,estado,
                        pais,telefono,tipo));

                //,tipo
            }while (register.moveToNext());
        }

        return lista;
    }
    public ArrayList<ClienteCompleto> clientesVista() {

        //List where all messages will be saved
        ArrayList<ClienteCompleto> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();

        //Query that gets messages by group
        String query= "SELECT c.id,c.grupo,c.nombre,c.cedula,c.direccion,c.ciudad,c.estado," +
                "c.pais,c.telefono,c.tipo FROM ClientesCompleto c where tipo=1";

        //,c.tipo
        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                String id=register.getString(0);
                String grupo=register.getString(1);
                String nombre= register.getString(2);
                String cedula=register.getString(3);
                String direccion= register.getString(4);
                String ciudad= register.getString(5);
                String estado= register.getString(6);
                String pais= register.getString(7);
                String telefono= register.getString(8);
                int tipo= register.getInt(9);

                Log.d("entre","entre"+nombre);

                lista.add(new ClienteCompleto(id,grupo,nombre,cedula,direccion,ciudad,estado,
                        pais,telefono,tipo));

                //,tipo
            }while (register.moveToNext());
        }

        return lista;
    }

    public void eliminarAbono(String id) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM ClientesCompleto where id='"+id+"';";
        database.execSQL(queryDelete);

    }

    public boolean existeCliente(String id) {

        boolean bandera=false;

        String sql= "SELECT * FROM ClientesCompleto where id='"+id+"';";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            bandera= true;
        }
        cursor.close();

        return bandera;

    }

    public boolean existeClienteCedula(String cedula) {

        boolean bandera=false;

        String sql= "SELECT * FROM ClientesCompleto where cedula='"+cedula+"';";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            bandera= true;
        }
        cursor.close();

        return bandera;

    }


    public boolean existenClientes(){

        boolean bandera=false;

        String sql= "SELECT * FROM ClientesCompleto;";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            bandera= true;
        }
        cursor.close();

        return bandera;

    }

    public void eliminarClientes() {
        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM ClientesCompleto";
        database.execSQL(queryDelete);


    }
}
