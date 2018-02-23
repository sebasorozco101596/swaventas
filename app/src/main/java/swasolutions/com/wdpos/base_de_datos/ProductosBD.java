package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.Producto;

/**
 * Created by sebas on 23/06/2017.
 */

public class ProductosBD extends SQLiteOpenHelper {

    private String creacionBD="CREATE TABLE `Productos` " +
            "( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `nombre` TEXT NOT NULL" +
            ",`unit` INTEGER NOT NULL, `cost` INTEGER NOT NULL," +
            " `precio1` INTEGER NOT NULL,`precio2` INTEGER NOT NULL,`precio3` INTEGER NOT NULL," +
            "`precio4` INTEGER NOT NULL,`precio5` INTEGER NOT NULL,`precio6` INTEGER NOT NULL," +
            "`categoryId` INTEGER NOT NULL,`type` TEXT NOT NULL," +
            " `codigoProducto` TEXT NOT NULL, `cantidad` INTEGER NOT NULL,`estado` INTEGER NOT NULL)";

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
        String query= "SELECT p.id,p.nombre,p.unit,p.cost,p.precio1,p.precio2,p.precio3,p.precio4,p.precio5," +
                "p.precio6,p.categoryId,p.type,p.codigoProducto,p.cantidad,p.estado FROM Productos p";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String nombre=register.getString(1);
                int unit=register.getInt(2);
                String cost=register.getString(3);
                String precio1= register.getString(4);
                int precio2= register.getInt(5);
                int precio3= register.getInt(6);
                int precio4= register.getInt(7);
                int precio5= register.getInt(8);
                int precio6= register.getInt(9);
                int categoryId= register.getInt(10);
                String type= register.getString(11);
                String codigoProducto= register.getString(12);
                String cantidad= register.getString(13);
                int estado= register.getInt(14);

                Log.d("entre","entre"+nombre);

                lista.add(new Producto(id,nombre,unit,cost,precio1,precio2,precio3,precio4,
                precio5,precio6,categoryId,type,codigoProducto,cantidad,estado));
            }while (register.moveToNext());
        }
        return lista;
    }


    public void agregarProducto(int id,String nombre,int unit,String cost,String precio,int precio2,
                                int precio3,int precio4,int precio5,int precio6,int categoryId,
                                String type,String codigoProducto,String cantidad){

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "INSERT INTO Productos (id,nombre,unit,cost,precio1,precio2,precio3," +
                "precio4,precio5,precio6,categoryId,type,codigoProducto,cantidad,estado) VALUES ("+ id +
                ",'"+nombre+"',"+unit+",'"+cost+"','"+precio+"'," + precio2+","+precio3+","+precio4+
                ","+precio5+","+ precio6+","+categoryId+",'"+type+"','"+codigoProducto+"','"+cantidad+"'," +
                "1);";
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

    public void editarProducto(Producto producto,Context context) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryUpdate= "update Productos set codigoProducto='"+producto.getCodigoProducto()+
                "',nombre= '"+producto.getNombre()+"',unit="+
                producto.getUnit()+",cost="+producto.getCost()+",precio1="+producto.getPrecio1()+
                ",precio2="+producto.getPrecio2()+",precio3="+producto.getPrecio3()+",precio4="+
                producto.getPrecio4()+",precio5="+producto.getPrecio5()+",precio6="+
                producto.getPrecio6()+",categoryId="+producto.getCategoryId()+",type='"+producto.getType()+
                "',estado=2 where id ="+producto.getId()+";";
        database.execSQL(queryUpdate);

        Toast.makeText(context,"Producto editado correctamente"+producto.getId(),
                Toast.LENGTH_SHORT).show();

    }

    public boolean existeCodigo(String code,String codigoAnterior) {

        boolean bandera=false;

        String sql= "SELECT * FROM Productos where codigoProducto='"+code+"' and codigoProducto <> '"+
                codigoAnterior+"'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            bandera= true;
        }
        cursor.close();

        return bandera;

    }

    public int productosEditados() {

        int id=0;

        String sql= "SELECT count(*) FROM Productos where estado=2";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            cursor.moveToFirst();
            id= cursor.getInt(0);
        }

        cursor.close();
        return id;
    }

    public ArrayList<Producto> listaProductosEditados() {

        //List where all messages will be saved
        ArrayList<Producto> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();



        //Query that gets messages by group
        String query= "SELECT p.id,p.nombre,p.unit,p.cost,p.precio1,p.precio2,p.precio3,p.precio4,p.precio5," +
                "p.precio6,p.categoryId,p.type,p.codigoProducto,p.cantidad,p.estado FROM Productos p" +
                " where estado=2";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String nombre=register.getString(1);
                int unit=register.getInt(2);
                String cost=register.getString(3);
                String precio1= register.getString(4);
                int precio2= register.getInt(5);
                int precio3= register.getInt(6);
                int precio4= register.getInt(7);
                int precio5= register.getInt(8);
                int precio6= register.getInt(9);
                int categoryId= register.getInt(10);
                String type= register.getString(11);
                String codigoProducto= register.getString(12);
                String cantidad= register.getString(13);
                int estado= register.getInt(14);

                Log.d("entre","entre"+nombre);

                lista.add(new Producto(id,nombre,unit,cost,precio1,precio2,precio3,precio4,
                        precio5,precio6,categoryId,type,codigoProducto,cantidad,estado));
            }while (register.moveToNext());
        }
        return lista;

    }

    public void actualizarProductosEditados() {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryUpdate= "update Productos set estado=1  where estado=2";
        database.execSQL(queryUpdate);

    }
}
