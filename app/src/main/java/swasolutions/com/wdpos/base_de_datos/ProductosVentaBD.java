package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.ProductoVenta;

/**
 * Created by sebas on 2/07/2017.
 */

public class ProductosVentaBD extends SQLiteOpenHelper {



    private String sqlCreacion= "CREATE TABLE `ProductosVenta` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            " `idVenta` INTEGER NOT NULL, `idProducto` INTEGER NOT NULL, " +
            "`codigoProducto` TEXT NOT NULL, `nombreProducto` TEXT NOT NULL," +
            " `precioUnidad` INTEGER NOT NULL, `cantidad` INTEGER NOT NULL, `idVendedor` INTEGER NOT NULL )";

    public ProductosVentaBD(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "ProductosVentaBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlCreacion);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void crearProductoVenta(int idVenta,int idProducto,String codigoProducto,String nombreProducto,
                                   int precioUnitario,int cantidad,int idVendedor){


        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "INSERT INTO ProductosVenta (id,idVenta,idProducto,codigoProducto,nombreProducto" +
                ",precioUnidad,cantidad,idVendedor) VALUES ("+ null +","+idVenta+","+idProducto+",'"+
                codigoProducto+"','"+nombreProducto+"',"+precioUnitario+","+cantidad+","+idVendedor+");";

        Log.d("producto",nombreProducto+"esto mas"+idVenta);
        database.execSQL(queryDelete);

    }

    public int cantidadProductosVentas(){

        //List where all messages will be saved
        SQLiteDatabase database= this.getWritableDatabase();


        int cantidad=0;
        //Query that gets messages by group
        String query= "SELECT cantidad FROM ProductosVenta ";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                int cantidadProductos= register.getInt(0);
                cantidad = cantidad + cantidadProductos;
               }while (register.moveToNext());
        }
        return cantidad;
    }

    public ArrayList<ProductoVenta> productosVenta() {

        //List where all messages will be saved
        ArrayList<ProductoVenta> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();



        //Query that gets messages by group
        String query= "SELECT id,idVenta,idProducto,codigoProducto,nombreProducto,precioUnidad,cantidad,idVendedor FROM ProductosVenta ";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                int idVenta=register.getInt(1);
                int idProducto= register.getInt(2);
                String codigoProducto= register.getString(3);
                String nombreProducto= register.getString(4);
                int precioUnidad= register.getInt(5);
                int cantidad= register.getInt(6);
                int idVendedor = register.getInt(7);


                lista.add(new ProductoVenta(id,idVenta,idProducto,codigoProducto,nombreProducto,precioUnidad,cantidad,idVendedor));
            }while (register.moveToNext());
        }

        return lista;
    }

    public ArrayList<ProductoVenta> productosVenta(int idVentaH) {

        //List where all messages will be saved
        ArrayList<ProductoVenta> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();



        //Query that gets messages by group
        String query= "SELECT id,idVenta,idProducto,codigoProducto,nombreProducto,precioUnidad," +
                "cantidad,idVendedor FROM ProductosVenta WHERE idVenta="+idVentaH;

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                int idVenta=register.getInt(1);
                int idProducto= register.getInt(2);
                String codigoProducto= register.getString(3);
                String nombreProducto= register.getString(4);
                int precioUnidad= register.getInt(5);
                int cantidad= register.getInt(6);
                int idVendedor = register.getInt(7);


                lista.add(new ProductoVenta(id,idVenta,idProducto,codigoProducto,nombreProducto,precioUnidad,cantidad,idVendedor));
            }while (register.moveToNext());
        }

        return lista;
    }

    public void eliminarProductosVenta(){
        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM ProductosVenta;";
        database.execSQL(queryDelete);
    }

    public boolean existenProductos() {

        boolean bandera=false;

        String sql= "SELECT * FROM ProductosVenta ";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            bandera= true;
        }
        cursor.close();

        return bandera;

    }

    public void eliminarProductoVenta(int id) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM ProductosVenta WHERE idVenta='"+id+"';";
        database.execSQL(queryDelete);

    }

}
