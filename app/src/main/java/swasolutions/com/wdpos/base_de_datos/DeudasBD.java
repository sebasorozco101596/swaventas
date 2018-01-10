package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.Deuda;

/**
 * Created by sebas on 26/06/2017.
 */

public class DeudasBD extends SQLiteOpenHelper{


    private String creacionBD="CREATE TABLE `Deudas` " +
            "( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "`fecha` TEXT NOT NULL,`referencia` TEXT NOT NULL,`comprador` TEXT NOT NULL," +
            "`idCliente` TEXT NOT NULL," +
            " `total` INTEGER NOT NULL, `pagado` INTEGER NOT NULL )";

    public DeudasBD(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "DeudasBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(creacionBD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("entre on upgrade", "onUpgrade: entre");
    }

    public ArrayList<Deuda> obtenerDeudas(String idCliente) {

        //List where all messages will be saved
        ArrayList<Deuda> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();

        //Query that gets messages by group
        String query= "SELECT id,fecha,referencia,comprador,idCliente,total,pagado FROM Deudas where idCliente='"+idCliente
                +"' AND (total<>pagado)";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String fecha= register.getString(1);
                String referencia= register.getString(2);
                String comprador= register.getString(3);
                String idClientee= register.getString(4);
                String total=register.getString(5);
                int pagado= register.getInt(6);

                Log.d("entre","entre"+total);

                lista.add(new Deuda(id,idClientee,fecha,referencia,comprador,total,""+pagado));
            }while (register.moveToNext());
        }

        return lista;
    }

    public void agregarDeuda(int id,String fecha,String referencia,String comprador,
                             String idCliente, String total, String pagado) {


        SQLiteDatabase database= this.getWritableDatabase();

        if(id<0){
            String queryInsertar= "INSERT INTO Deudas (id,fecha,referencia,comprador,idCliente,total,pagado)"+
                    "VALUES ("+ null+",'"+fecha+"','"+referencia+"','"+comprador+"','"+ idCliente +"','"+total+"','"+pagado+"');";
            database.execSQL(queryInsertar);
        }else{
            String queryInsertar= "INSERT INTO Deudas (id,fecha,referencia,comprador,idCliente,total,pagado)"+
                    "VALUES ("+ id +",'"+fecha+"','"+referencia+"','"+comprador+"','"+ idCliente +"','"+total+"','"+pagado+"');";
            database.execSQL(queryInsertar);
        }



    }


    public void eliminarTodasLasDeudas() {
        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Deudas;";
        database.execSQL(queryDelete);
    }


    public void actualizarDeuda(int id, int pagado) {

        SQLiteDatabase database= this.getWritableDatabase();
        int nuevoPagado=encontrarPago(id)+pagado;
        Log.d("Actualizando",""+nuevoPagado);
        String queryInsertar= "UPDATE Deudas SET pagado='"+nuevoPagado+"' where id='"+id+"'";
        database.execSQL(queryInsertar);

    }

    public int contarFilas(){

        int contador=1;

        String sql= "SELECT COUNT(*) FROM Deudas";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            contador= cursor.getInt(0);

        }

        cursor.close();
        return contador;

    }

    public int encontrarPago(int id){

        int pago=0;

        String sql= "SELECT pagado FROM Deudas where id='"+id+"'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            pago= cursor.getInt(0);

        }

        cursor.close();
        return pago;

    }

    public int totalDeudas(String idCliente) {

        int contador=1;

        String sql= "SELECT SUM(total-pagado) FROM Deudas where idCliente='"+idCliente+"'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            contador= cursor.getInt(0);

        }

        cursor.close();
        return contador;



    }
}
