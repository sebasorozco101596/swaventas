package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.Warehouse;

/**
 * Created by sebas on 2/11/2017.
 */

public class WarehouseBD extends SQLiteOpenHelper {

    private String creacionBD="CREATE TABLE `Warehouses` " +
            "( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `code` TEXT NOT NULL" +
            ", `name` TEXT NOT NULL, `phone` TEXT NOT NULL)";

    public WarehouseBD(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "WarehousesBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creacionBD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("entre on upgrade", "onUpgrade: entre");
    }


    public void agregarWarehouse(int id, String codigo, String name, String phone) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsertWarehouse= "INSERT INTO Warehouses (id,code,name,phone)"+
                "VALUES ("+ id +",'"+codigo+"','"+name+"','"+phone+"');";
        database.execSQL(queryInsertWarehouse);

    }

    public ArrayList<Warehouse> warehouses() {

        //List where all messages will be saved
        ArrayList<Warehouse> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();

        //Query that gets messages by group
        String query= "SELECT w.id,w.code,w.name,w.phone FROM Warehouses w";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String code=register.getString(1);
                String name= register.getString(2);
                String phone= register.getString(3);


                lista.add(new Warehouse(id,code,name,phone));
            }while (register.moveToNext());
        }

        return lista;
    }

    public void eliminarWarehouses() {
        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Warehouses;";
        database.execSQL(queryDelete);
    }

    public int obtenerId(String nombreWarehouse) {

        int id=0;

        String sql= "SELECT id FROM Warehouses where name='"+nombreWarehouse+"';";

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

        String sql= "SELECT name FROM Warehouses where id="+id+";";

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
