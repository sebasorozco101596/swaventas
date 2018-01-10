package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.Producto;

/**
 * Created by sebas on 23/06/2017.
 */

public class ProductosBD extends SQLiteOpenHelper {

    private String creacionBD="CREATE TABLE `Productos` " +
            "( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `nombre` TEXT NOT NULL" +
            ", `precio` INTEGER NOT NULL, `codigoProducto` TEXT NOT NULL, `cantidad` INTEGER NOT NULL )";

    public ProductosBD(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "ProductosBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(creacionBD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("entre on upgrade", "onUpgrade: entre");
    }

    public ArrayList<Producto> fillMessages() {

        //List where all messages will be saved
        ArrayList<Producto> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();



        //Query that gets messages by group
        String query= "SELECT p.id,p.nombre,p.precio,p.codigoProducto,p.cantidad FROM Productos p";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String nombre=register.getString(1);
                String precio= register.getString(2);
                String codigoProducto= register.getString(3);
                String cantidad= register.getString(4);

                Log.d("entre","entre"+nombre);

                lista.add(new Producto(id,nombre,precio,codigoProducto,cantidad));
            }while (register.moveToNext());
        }

        return lista;
    }


    public void agregarProducto(int id,String nombre,String precio,String codigoProducto,String cantidad){

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "INSERT INTO Productos (id,nombre,precio,codigoProducto,cantidad)"+
                "VALUES ("+ id +",'"+nombre+"','"+precio+"','"+codigoProducto+"','"+cantidad+"');";
        database.execSQL(queryDelete);

    }

    public void eliminarTodosProductos(){

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Productos;";
        database.execSQL(queryDelete);

    }

    public void actualizarProducto(int id,int cantidad){

        int cantidadProducto= encontrarCantidadProducto(id);
        SQLiteDatabase database= this.getWritableDatabase();
        String queryUpdate= "update Productos set cantidad= "+(cantidadProducto-cantidad)+
                " where id ="+id+";";
        database.execSQL(queryUpdate);

    }

    public int encontrarCantidadProducto(int id){

        int cantidad=0;

        String sql= "SELECT cantidad FROM Productos where id='"+id+"'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            cantidad= cursor.getInt(0);

        }

        cursor.close();
        return cantidad;

    }

    public int idProducto(String codigoProducto) {

        int id=1;

        String sql= "SELECT id FROM Productos where codigoProducto='"+codigoProducto+"'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            id= cursor.getInt(0);

        }

        cursor.close();
        return id;

    }
}
