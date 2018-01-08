package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.Unidad;

/**
 * Created by sebas on 16/12/2017.
 */

public class UnidadesBD extends SQLiteOpenHelper {

    private String creacionBD="CREATE TABLE `Unidades` " +
            "( `id` INTEGER NOT NULL PRIMARY KEY, `code` TEXT NOT NULL" +
            ", `name` TEXT NOT NULL)";

    public UnidadesBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "UnidadesBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creacionBD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void agregarUnidad(int id, String codigo, String name) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsertWarehouse= "INSERT INTO Unidades (id,code,name)"+
                "VALUES ("+ id +",'"+codigo+"','"+name+"');";
        database.execSQL(queryInsertWarehouse);

    }

    public ArrayList<Unidad> unidades() {

        //List where all messages will be saved
        ArrayList<Unidad> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();

        //Query that gets messages by group
        String query= "SELECT w.id,w.code,w.name FROM Unidades w";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String code=register.getString(1);
                String name= register.getString(2);


                lista.add(new Unidad(id,code,name));
            }while (register.moveToNext());
        }

        return lista;
    }

    public void eliminarUnidades() {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Unidades;";
        database.execSQL(queryDelete);

    }

    public int obtenerId(String nombreUnidad) {

        int id=0;

        String sql= "SELECT id FROM Unidades where name='"+nombreUnidad+"';";

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

        String sql= "SELECT name FROM Unidades where id="+id+";";

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
