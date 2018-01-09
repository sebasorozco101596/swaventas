package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.Gasto;

/**
 * Created by sebas on 23/06/2017.
 */

public class GastosBD extends SQLiteOpenHelper{


    private String creacionBD= "CREATE TABLE `Gastos` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            " `fecha` TEXT NOT NULL, `referencia` TEXT NOT NULL, `dinero` INTEGER NOT NULL," +
            " `vendedor` TEXT NOT NULL,`descripcion` TEXT NOT NULL )";

    public GastosBD(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "GastosBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creacionBD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void agregarGasto(String fecha,String referencia,int dinero,String vendedor,String descripcion){

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "INSERT INTO Gastos (id,fecha,referencia,dinero,vendedor,descripcion) " +
                "VALUES ("+ null +",'"+fecha+"','"+referencia+"',"+
                dinero+",'"+vendedor+"','"+descripcion+"');";
        database.execSQL(queryDelete);

    }

    public ArrayList<Gasto> gastos() {

        //List where all messages will be saved
        ArrayList<Gasto> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();



        //Query that gets messages by group
        String query= "SELECT id,fecha,referencia,dinero,vendedor,descripcion FROM Gastos ";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String fecha= register.getString(1);
                String referencia= register.getString(2);
                int dinero=register.getInt(3);
                String vendedor= register.getString(4);
                String descripcion= register.getString(5);

                lista.add(new Gasto(id,fecha,referencia,dinero,vendedor,descripcion));
            }while (register.moveToNext());
        }

        return lista;
    }

    public void eliminarGastos(){
        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Gastos;";
        database.execSQL(queryDelete);
    }

    public int totalGastos(){

        int contador=1;

        String sql= "SELECT SUM(dinero) FROM Gastos";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            contador= cursor.getInt(0);

        }

        cursor.close();
        return contador;

    }

    public boolean existenGastos() {

        boolean bandera=false;

        String sql= "SELECT * FROM Gastos ";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            bandera= true;
        }
        cursor.close();

        return bandera;

    }

    public void eliminarGasto(int id) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Gastos WHERE id='"+id+"';";
        database.execSQL(queryDelete);

    }
}
