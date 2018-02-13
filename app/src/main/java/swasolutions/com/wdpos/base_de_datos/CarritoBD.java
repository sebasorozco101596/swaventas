package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.ProductoCarrito;

/**
 * Created by sebas on 24/06/2017.
 */

public class CarritoBD extends SQLiteOpenHelper{


    private  String creacionCarrito= "CREATE TABLE IF NOT EXISTS `Carrito` ( `idProducto` INTEGER NOT NULL," +
            " `nombreProducto` TEXT NOT NULL, `precioProducto` INTEGER NOT NULL,`precioProducto2` INTEGER NOT NULL," +
            " `cantidad` INTEGER,`codigoProducto` TEXT NOT NULL, PRIMARY KEY(`idProducto`) )";

    public CarritoBD(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "CarritoBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(creacionCarrito);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("entre on upgrade", "onUpgrade: entre");
    }

    public void agregarProductoCarrito(int id,String nombre,int precio,int precio2,Context context,
                                       String codigoProducto){

        SQLiteDatabase database= this.getWritableDatabase();

        String creacionCarrito= "CREATE TABLE IF NOT EXISTS `Carrito` ( `idProducto` INTEGER NOT NULL," +
                " `nombreProducto` TEXT NOT NULL, `precioProducto` INTEGER NOT NULL,`precioProducto2` INTEGER NOT NULL," +
                " `cantidad` INTEGER,`codigoProducto` TEXT NOT NULL, PRIMARY KEY(`idProducto`) )";
        database.execSQL(creacionCarrito);

        String queryId= "Select c.idProducto,c.cantidad FROM Carrito c";
        Cursor register=database.rawQuery(queryId,null);
        int encontro=0;
        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int idProducto=register.getInt(0);
                int cantidad=register.getInt(1);
                int nuevaCantidad= cantidad+1;


                if(id==idProducto){
                    encontro=1;
                    Toast.makeText(context,"Producto agregado",Toast.LENGTH_SHORT).show();
                    String queryUpdate = "UPDATE Carrito " +
                            "SET cantidad="+nuevaCantidad+" where idProducto="+id;
                    database.execSQL(queryUpdate);
                }

            }while (register.moveToNext());


            if(encontro==0){

                Toast.makeText(context,"Producto agregado",Toast.LENGTH_SHORT).show();
                String queryAgregar= "INSERT INTO Carrito (idProducto,nombreProducto,precioProducto," +
                        "precioProducto2,cantidad,codigoProducto) VALUES ("+ id +",'"+nombre+"',"+
                        precio+","+precio2+","+1+",'"+ codigoProducto+"');";
                database.execSQL(queryAgregar);
            }

        }else if(!register.moveToNext()){
            Toast.makeText(context,"Producto agregado",Toast.LENGTH_SHORT).show();
            String queryAgregar= "INSERT INTO Carrito (idProducto,nombreProducto,precioProducto," +
                    "precioProducto2,cantidad,codigoProducto) VALUES ("+ id +",'"+nombre+"',"+
                    precio+","+precio2+","+1+",'"+ codigoProducto+"');";
            database.execSQL(queryAgregar);
        }
    }

    public void actualizarProductoCarrito(int id,String nombre,int precio,int precio2,
                                          Context context,String codigoProducto){

        SQLiteDatabase database= this.getWritableDatabase();

        String creacionCarrito= "CREATE TABLE IF NOT EXISTS `Carrito` ( `idProducto` INTEGER NOT NULL," +
                " `nombreProducto` TEXT NOT NULL, `precioProducto` INTEGER NOT NULL,`precioProducto2` INTEGER NOT NULL," +
                " `cantidad` INTEGER,`codigoProducto` TEXT NOT NULL, PRIMARY KEY(`idProducto`) )";
        database.execSQL(creacionCarrito);


        String queryId= "Select c.idProducto FROM Carrito c";
        Cursor register=database.rawQuery(queryId,null);
        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int idProducto=register.getInt(0);


                if(id==idProducto){
                    Toast.makeText(context,"Producto alterado",Toast.LENGTH_SHORT).show();
                    String queryUpdate = "UPDATE Carrito " +
                            "SET precioProducto="+precio+",precioProducto2="+precio2+" where idProducto="+id;
                    database.execSQL(queryUpdate);
                }

            }while (register.moveToNext());

        }
    }

    public ArrayList<ProductoCarrito> cargarProductosCarrito() {

        //List where all messages will be saved
        ArrayList<ProductoCarrito> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();



        //Query that gets messages by group
        String query= "SELECT c.idProducto,c.nombreProducto,c.precioProducto,c.precioProducto2," +
                "c.cantidad,c.codigoProducto FROM Carrito c";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String nombre=register.getString(1);
                int precio= register.getInt(2);
                int precio2=register.getInt(3);
                int cantidad= register.getInt(4);
                String codigoProducto= register.getString(5);

                lista.add(new ProductoCarrito(id,nombre,precio,precio2,cantidad,codigoProducto));
            }while (register.moveToNext());
        }

        return lista;
    }

    public void eliminarProductosCarrito(){
        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Carrito;";
        database.execSQL(queryDelete);
    }

    public void eliminarProductoCarrito(int id, int cantidad) {

        SQLiteDatabase database= this.getWritableDatabase();
        int nuevaCantidad= cantidad-1;

        if(nuevaCantidad==0){
            String eliminarProducto= "Delete From Carrito where idProducto='"+
                    id+"'";
            Log.d("entreEliminar","entre");
            database.execSQL(eliminarProducto);


        }else{
            String actualizadProducto= "Update Carrito set cantidad='"+nuevaCantidad+"' where idProducto='"+
                    id+"'";
            database.execSQL(actualizadProducto);
        }



    }
}
