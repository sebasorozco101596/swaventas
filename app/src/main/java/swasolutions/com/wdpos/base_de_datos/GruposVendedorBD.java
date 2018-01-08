package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.GrupoVendedor;

/**
 * Created by sebas on 3/11/2017.
 */

public class GruposVendedorBD extends SQLiteOpenHelper {

    String creacionBD="CREATE TABLE `GruposVendedor` " +
            "( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
            ", `name` TEXT NOT NULL, `percent` INTEGER NOT NULL)";

    public GruposVendedorBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "GruposVendedorBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(creacionBD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<GrupoVendedor> gruposVendedor() {

        //List where all messages will be saved
        ArrayList<GrupoVendedor> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();

        //Query that gets messages by group
        String query= "SELECT w.id,w.name,w.percent FROM GruposVendedor w";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String name= register.getString(1);
                int percent= register.getInt(2);


                lista.add(new GrupoVendedor(id,name,percent));
            }while (register.moveToNext());
        }

        return lista;
    }

    public void agregarGrupoVendedor(int id, String name, int percent) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsertWarehouse= "INSERT INTO GruposVendedor (id,name,percent)"+
                "VALUES ("+ id +",'"+name+"',"+percent+");";
        database.execSQL(queryInsertWarehouse);

    }

    public void eliminarGruposVendedor() {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM GruposVendedor;";
        database.execSQL(queryDelete);

    }

    public int obtenerId(String nombreGrupo) {

        int id=0;

        String sql= "SELECT id FROM GruposVendedor where name='"+nombreGrupo+"';";

        SQLiteDatabase database= this.getWritableDatabase();

        Cursor register= database.rawQuery(sql,null);

        if (register.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                id = register.getInt(0);
            } while(register.moveToNext());
        }

        register.close();


        return id;
    }

    public String obtenerNombre(int id) {

        String name="";

        String sql= "SELECT name FROM GruposVendedor where id="+id+";";

        SQLiteDatabase database= this.getWritableDatabase();

        Cursor register= database.rawQuery(sql,null);

        if (register.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                name = register.getString(0);
            } while(register.moveToNext());
        }

        register.close();

        return name;
    }
}
