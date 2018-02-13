package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by sebas on 10/02/2018.
 */

public class PreciosGrupoBD extends SQLiteOpenHelper {

    private  String creacionBD="CREATE TABLE `PreciosGrupo` " +
            "( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
            ", `idGrupo` INTEGER NOT NULL, `precio_uno` INTEGER NOT NULL, `precio_dos` INTEGER NOT NULL)";

    public PreciosGrupoBD(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "PreciosGrupoBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(creacionBD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("entre on upgrade", "onUpgrade: entre");
    }

    public void agregarPrecioGrupo(int idGrupo, int precio1, int precio2) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsertWarehouse= "INSERT INTO PreciosGrupo (id,idGrupo,precio_uno,precio_dos)"+
                "VALUES ("+ null +","+idGrupo+","+precio1+","+precio2+");";
        database.execSQL(queryInsertWarehouse);

    }

    public void eliminarPreciosGrupo() {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM PreciosGrupo;";
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

    public boolean existeGrupo(int grupo) {

        boolean bandera=false;

        String sql= "SELECT * FROM PreciosGrupo where idGrupo='"+grupo+"' ";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            bandera= true;
        }
        cursor.close();

        return bandera;
    }

    public void actualizarPrecioGrupo(int grupo, int precio1, int precio2) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsertWarehouse= "UPDATE PreciosGrupo SET precio_uno="+precio1+", precio_dos="
                +precio2+" where idGrupo="+grupo+";";
        database.execSQL(queryInsertWarehouse);

    }

    public ArrayList<Integer> precios(int idGrupo) {

        //List where all messages will be saved
        ArrayList<Integer> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();


        //Query that gets messages by group
        String query= "SELECT precio_uno,precio_dos FROM PreciosGrupo where idGrupo="+idGrupo+";";

        //,nota,credito

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int precio1 = register.getInt(0);
                int precio2 = register.getInt(1);

                lista.add(precio1);
                lista.add(precio2);

                //,nota,credito
            }while (register.moveToNext());
        }

        return lista;

    }
}
