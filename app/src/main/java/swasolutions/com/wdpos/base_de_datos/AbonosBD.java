package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.Abono;

/**
 * Created by sebas on 9/07/2017.
 */

public class AbonosBD extends SQLiteOpenHelper {

    private String creacionBD= "CREATE TABLE `Abonos` ( `id` INTEGER, `estadoVenta` TEXT," +
            " `pago` INTEGER NOT NULL, `idVendedor` INTEGER NOT NULL, `fecha` TEXT NOT NULL,`referencia` TEXT NOT NULL," +
            " `pagoCierre` INTEGER NOT NULL, cedulaCliente TEXT NOT NULL,`credito` INTEGER NOT NULL, PRIMARY KEY(`id`))";

    public AbonosBD(Context context,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "AbonosBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(creacionBD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void crearAbono(int id,String estadoVenta,int pago,int idVendedor,String fecha,
                           String referencia,int pagoCierre,String cedula,int credito,Context context){

        SQLiteDatabase database= this.getWritableDatabase();
        String queryCrearAbono= "INSERT INTO Abonos (id,estadoVenta,pago,idVendedor,fecha,referencia," +
                "pagoCierre,cedulaCliente,credito) " +
                "VALUES ("+ id +",'"+estadoVenta+"',"+pago+","+idVendedor+",'"+fecha+"','"+referencia+"',"+pagoCierre+
                ",'"+cedula+"',"+credito+");";
        Toast.makeText(context,"Abono creado",Toast.LENGTH_SHORT).show();
        database.execSQL(queryCrearAbono);

    }

    public boolean existeRegistro(String referencia){

        boolean bandera=false;

        String sql= "SELECT * FROM Abonos where referencia='"+referencia+"'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            bandera= true;
        }
        cursor.close();

        return bandera;
    }

    public void actualizarAbono(String referencia,int pagado){

        SQLiteDatabase database= this.getWritableDatabase();
        int nuevoPagado=encontrarPago(referencia)+pagado;
        Log.d("Actualizando",""+nuevoPagado);
        String queryInsertar= "UPDATE Abonos SET pago='"+nuevoPagado+"' where referencia='"+referencia+"'";
        Log.d("pagadoooooo",""+nuevoPagado);
        database.execSQL(queryInsertar);

    }

    public int encontrarPago(String referencia){

        int pago=0;

        String sql= "SELECT pago FROM Abonos where referencia='"+referencia+"'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            pago= cursor.getInt(0);

        }

        cursor.close();
        return pago;

    }


    public ArrayList<Abono> abonos() {

        //List where all messages will be saved
        ArrayList<Abono> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();



        //Query that gets messages by group
        String query= "SELECT id,estadoVenta,pago,idVendedor,fecha,referencia,pagoCierre," +
                "cedulaCliente,credito FROM Abonos ";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String estadoVenta= register.getString(1);
                int pago=register.getInt(2);
                int idVendedor=register.getInt(3);
                String fecha= register.getString(4);
                String referencia= register.getString(5);
                int pagoCierre = register.getInt(6);
                String cedCliente= register.getString(7);
                int credito=register.getInt(8);

                lista.add(new Abono(id,estadoVenta,pago,idVendedor,fecha,referencia,pagoCierre
                ,cedCliente,credito));
            }while (register.moveToNext());
        }

        return lista;
    }

    public ArrayList<Abono> abonosVista() {

        //List where all messages will be saved
        ArrayList<Abono> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();



        //Query that gets messages by group
        String query= "SELECT id,estadoVenta,pagoCierre,idVendedor,fecha,referencia,pagoCierre," +
                "cedulaCliente FROM Abonos ";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id=register.getInt(0);
                String estadoVenta= register.getString(1);
                int pago=register.getInt(2);
                int idVendedor=register.getInt(3);
                String fecha= register.getString(4);
                String referencia= register.getString(5);
                int pagoCierre = register.getInt(6);
                String cedCliente= register.getString(7);


                lista.add(new Abono(id,estadoVenta,pago,idVendedor,fecha,referencia,pagoCierre,
                        cedCliente));
            }while (register.moveToNext());
        }

        return lista;
    }

    public int totalAbonos(){

        int contador=0;

        String sql= "SELECT SUM(pagoCierre-credito) FROM Abonos";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            contador= cursor.getInt(0);

        }

        cursor.close();
        return contador;

    }
    public void eliminarAbonos() {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Abonos;";
        database.execSQL(queryDelete);
    }

    public boolean existenAbonos(){

            boolean bandera=false;

            String sql= "SELECT * FROM Abonos ";

            Cursor cursor= getReadableDatabase().rawQuery(sql,null);

            if(cursor.getCount() >0){
                bandera= true;
            }
            cursor.close();

            return bandera;

    }

    public void eliminarAbono(int id) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Abonos WHERE id='"+id+"';";
        database.execSQL(queryDelete);

    }
}
