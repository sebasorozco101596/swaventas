package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.Devolucion;

/**
 * Created by sebas on 21/12/2017.
 */

public class DevolucionesBD extends SQLiteOpenHelper {

    private String creacionBD="CREATE TABLE `Devoluciones` " +
            "( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `cedula` TEXT NOT NULL" +
            ", `valor` INTEGER NOT NULL,`codigo_producto` TEXT NOT NULL,`nombre_producto` TEXT NOT NULL," +
            "`estado` TEXT NOT NULL,`tipo` TEXT NOT NULL)";

    public DevolucionesBD(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "DevolucionesBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creacionBD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void agregarDevolucion(String cedula, int valor,String codigoProducto,String nombreProducto,
                                  Context context,String tipo) {

        SQLiteDatabase database= this.getWritableDatabase();

        String queryInsert= "INSERT INTO Devoluciones (id,cedula,valor,codigo_producto,nombre_producto,estado,"+
                "tipo) VALUES ("+ null +",'"+cedula+"',"+valor+",'"+codigoProducto+"','"+nombreProducto+"'" +
                ",'incompleto','"+tipo+"');";
        database.execSQL(queryInsert);
        Toast.makeText(context,"Devolucion agregada correctamente",Toast.LENGTH_SHORT).show();

    }

    public void eliminarDevoluciones(){
        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsert= "DELETE FROM devoluciones;";
        database.execSQL(queryInsert);
    }

    public void eliminarDevoluciones(String cedula){
        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsert= "DELETE FROM devoluciones where cedula='"+cedula+"' and" +
                "estado= 'incompleto';";
        database.execSQL(queryInsert);
    }

    public ArrayList<Devolucion> devoluciones(String cc) {

        //List where all messages will be saved
        ArrayList<Devolucion> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();

        //Query that gets messages by group
        String query= "SELECT id,cedula,valor,codigo_producto,nombre_producto FROM Devoluciones" +
                " where estado= 'incompleto'" +
                " and cedula='"+cc+"';";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String cedula=register.getString(1);
                int valor= register.getInt(2);
                String codigoProducto= register.getString(3);
                String nombreProducto= register.getString(4);


                lista.add(new Devolucion(id,cedula,valor,codigoProducto,nombreProducto));
            }while (register.moveToNext());
        }

        return lista;
    }

    public ArrayList<Devolucion> devolucionesCierre() {

        //List where all messages will be saved
        ArrayList<Devolucion> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();

        //Query that gets messages by group
        String query= "SELECT id,cedula,valor,codigo_producto,nombre_producto FROM Devoluciones" +
                " where estado= 'completo' and tipo= 'normal';";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String cedula=register.getString(1);
                int valor= register.getInt(2);
                String codigoProducto= register.getString(3);
                String nombreProducto= register.getString(4);


                lista.add(new Devolucion(id,cedula,valor,codigoProducto,nombreProducto));
            }while (register.moveToNext());
        }

        return lista;
    }

    public void actualizarDevolucion(int id){

        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsert= "UPDATE devoluciones set estado='completo' where id="+id+";";
        database.execSQL(queryInsert);

    }

    public int contarDevoluciones() {

        int contador=0;

        String sql= "SELECT COUNT(*) FROM Devoluciones where tipo='normal' and estado='completo'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            contador= cursor.getInt(0);

        }

        cursor.close();
        return contador;

    }

    public void completarDevoluciones() {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsert= "UPDATE devoluciones set estado='subido' where estado='completo';";
        database.execSQL(queryInsert);

    }
}
