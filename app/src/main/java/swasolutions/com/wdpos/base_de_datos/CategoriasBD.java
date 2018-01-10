package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.Categoria;

/**
 * Created by sebas on 16/12/2017.
 */

public class CategoriasBD extends SQLiteOpenHelper {

    private String creacionBD="CREATE TABLE `Categorias` " +
            "( `id` INTEGER NOT NULL PRIMARY KEY, `code` TEXT NOT NULL" +
            ", `name` TEXT NOT NULL)";

    public CategoriasBD(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "CategoriasBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creacionBD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("entre on upgrade", "onUpgrade: entre");
    }

    public void agregarCategoria(int id, String codigo, String name) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsertWarehouse= "INSERT INTO Categorias (id,code,name)"+
                "VALUES ("+ id +",'"+codigo+"','"+name+"');";
        database.execSQL(queryInsertWarehouse);

    }

    public ArrayList<Categoria> categorias() {

        //List where all messages will be saved
        ArrayList<Categoria> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();

        //Query that gets messages by group
        String query= "SELECT w.id,w.code,w.name FROM Categorias w";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String code=register.getString(1);
                String name= register.getString(2);


                lista.add(new Categoria(id,code,name));
            }while (register.moveToNext());
        }

        return lista;
    }

    public void eliminarCategorias() {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Categorias;";
        database.execSQL(queryDelete);

    }

    public int obtenerId(String nombreCategoria) {

        int id=0;

        String sql= "SELECT id FROM Categorias where name='"+nombreCategoria+"';";

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

        String sql= "SELECT name FROM Categorias where id="+id+";";

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
