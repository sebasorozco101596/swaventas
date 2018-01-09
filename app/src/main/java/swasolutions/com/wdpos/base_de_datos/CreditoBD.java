package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by sebas on 19/12/2017.
 */

public class CreditoBD extends SQLiteOpenHelper {

    private String creacionBD="CREATE TABLE `Creditos` " +
            "( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `cedula` TEXT NOT NULL" +
            ", `valor` INTEGER NOT NULL)";

    public CreditoBD(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "CreditoBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(creacionBD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void agregarCredito(String cedula, int valor,Context context) {

        SQLiteDatabase database= this.getWritableDatabase();

        if(buscarCliente(cedula)){
            int valorAntiguo= buscarValor(cedula);
            String queryInsert= "UPDATE Creditos set valor="+(valor+valorAntiguo)+" where cedula=" +
                    "'"+cedula+"';";
            database.execSQL(queryInsert);

            Toast.makeText(context,"Credito agregado correctamente",Toast.LENGTH_SHORT).show();

        }else{
            String queryInsert= "INSERT INTO Creditos (id,cedula,valor)"+
                    "VALUES ("+ null +",'"+cedula+"',"+valor+");";
            database.execSQL(queryInsert);
            Toast.makeText(context,"Credito agregado correctamente",Toast.LENGTH_SHORT).show();
        }
    }

    public int buscarValor(String cedula) {

        int valor=0;

        String sql= "SELECT valor FROM Creditos where cedula="+cedula+";";

        SQLiteDatabase database= this.getWritableDatabase();

        Cursor register= database.rawQuery(sql,null);

        if (register.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                valor = register.getInt(0);
            } while(register.moveToNext());
        }

        register.close();

        return  valor;

    }

    public boolean buscarCliente(String cedula) {

        boolean existe=false;

        String sql= "SELECT * FROM Creditos where cedula='"+cedula+"';";

        SQLiteDatabase database= this.getWritableDatabase();

        Cursor register= database.rawQuery(sql,null);

        if(register.getCount() >0){
            existe= true;
        }

        register.close();

        return existe;

    }

    public void descontarCredito(String cedula, int valor,Context context) {

        SQLiteDatabase database= this.getWritableDatabase();

            int valorAntiguo= buscarValor(cedula);

            if((valorAntiguo-valor)==0){
                String queryDelete= "Delete FROM Creditos where cedula="+cedula+";";
                database.execSQL(queryDelete);
                Toast.makeText(context,"Credito descontado correctamente",Toast.LENGTH_SHORT).show();
            }else{
                String queryInsert= "UPDATE Creditos set valor="+(valorAntiguo-valor)+" where cedula=" +
                        "'"+cedula+"';";
                database.execSQL(queryInsert);
                Toast.makeText(context,"Credito descontado correctamente",Toast.LENGTH_SHORT).show();
            }
    }



}
