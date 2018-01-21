package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.Venta;

/**
 * Created by sebas on 23/06/2017.
 */

public class VentasBD extends SQLiteOpenHelper{

    //colocar el not null al credito

    private String creacionBD="CREATE TABLE `Ventas` ( `id` INTEGER PRIMARY KEY, " +
            "`fecha` TEXT NOT NULL, `referencia` TEXT NOT NULL, `idVendedor` INTEGER NOT NULL, " +
            "`cedulaCliente` TEXT NOT NULL, " +
            "`cliente` TEXT NOT NULL, `total` INTEGER NOT NULL, `estadoVenta` TEXT NOT NULL," +
            " `pagadoCliente` INTEGER NOT NULL,`cantidadProductos` INTEGER NOT NULL DEFAULT 0," +
            "`idCliente` TEXT NOT NULL,`existe` INTEGER NOT NULL,`nota` TEXT,`credito` INTEGER NOT NULL)";


    //,`nota` TEXT,`credito` INTEGER NOT NULL

    //,`credito` INTEGER NOT NULL

    public VentasBD(Context context, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, "VentasBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(creacionBD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("entre on upgrade", "onUpgrade: entre");
    }
    public void agregarVenta(int id, String fecha, String referencia, int idVendedor,String cedulaCliente,
                             String cliente,int total,String estadoVenta,int pagadoCliente,int cantidad,
                            String idCliente,Context context,int existe,String nota,int credito){

        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsertarVenta= "INSERT INTO Ventas (id,fecha,referencia,idVendedor,cedulaCliente," +
                "cliente,total,estadoVenta,pagadoCliente,cantidadProductos,idCliente,existe,nota,credito)" +
                " VALUES ("+ id +",'"+fecha+"','"+referencia+"',"+ idVendedor+",'"+cedulaCliente+
                "','"+cliente+"',"+total+",'"+
                estadoVenta+"',"+pagadoCliente+","+cantidad+",'"+idCliente+"','"+existe+"','"+
                nota+"',"+credito+");";

        //Toast.makeText(context,""+pagadoCliente,Toast.LENGTH_SHORT).show();
        database.execSQL(queryInsertarVenta);

    }

    public int contarFilas(){

        int contador=1;

        String sql= "SELECT COUNT(*) FROM Ventas";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            contador= cursor.getInt(0);

        }

        cursor.close();
        return contador;

    }

    public boolean existeRegistro(String referencia){

        boolean bandera=false;

        String sql= "SELECT * FROM Ventas where referencia='"+referencia+"'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            bandera= true;
        }
        cursor.close();

        return bandera;
    }

    public int encontrarVenta(String referencia){

        int pago=0;

        String sql= "SELECT id FROM Ventas where referencia='"+referencia+"'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            cursor.moveToFirst();
            pago= cursor.getInt(0);
        }

        cursor.close();
        return pago;

    }


    public ArrayList<Venta> ventas() {

        //List where all messages will be saved
        ArrayList<Venta> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();



        //Query that gets messages by group
        String query= "SELECT id,fecha,referencia,idVendedor,cedulaCliente,cliente,total,estadoVenta," +
                "pagadoCliente,cantidadProductos,idCliente,existe,nota,credito FROM Ventas";

        //,nota,credito

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id = register.getInt(0);
                String fecha = register.getString(1);
                String referencia = register.getString(2);
                int idVendedor = register.getInt(3);
                String cedulaCliente = register.getString(4);
                String cliente = register.getString(5);
                int total = register.getInt(6);
                String estadoVenta = register.getString(7);
                int pagadoCliente = register.getInt(8);
                int cantidadProductos= register.getInt(9);
                String idCliente= register.getString(10);
                int existe= register.getInt(11);
                String nota= register.getString(12);
                int credito= register.getInt(13);



                lista.add(new Venta(id,fecha,referencia,idVendedor,cliente,cedulaCliente,total,estadoVenta,pagadoCliente,
                        cantidadProductos,idCliente,existe,nota,credito));

                //,nota,credito
            }while (register.moveToNext());
        }

        return lista;
    }

    public void eliminarVentas(){
        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Ventas;";
        database.execSQL(queryDelete);
    }

    public int totalVentasRecibido(){

        int contador=1;

        //SELECT SUM(pagadoCliente-credito) FROM Ventas
        String sql= "SELECT SUM(pagadoCliente-credito) FROM Ventas";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            contador= cursor.getInt(0);

        }

        cursor.close();
        return contador;

    }

    public int totalVentas(){

        int contador=1;

        String sql= "SELECT SUM(total) FROM Ventas";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            contador= cursor.getInt(0);

        }

        cursor.close();
        return contador;

    }

    public int cantidadProductos(){

        int contador=1;

        String sql= "SELECT SUM(cantidadProductos) FROM Ventas";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            contador= cursor.getInt(0);

        }

        cursor.close();
        return contador;

    }

    public boolean existenVentas() {

        boolean bandera=false;

        String sql= "SELECT * FROM Ventas ";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            bandera= true;
        }
        cursor.close();

        return bandera;

    }

    public void eliminarVenta(int id) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Ventas WHERE id='"+id+"';";
        database.execSQL(queryDelete);

    }

    public void actualizarVenta(int id, int pagado,Context context,String estadoVenta,int credito) {

        SQLiteDatabase database= this.getWritableDatabase();
        int nuevoPagado=encontrarPago(id)+pagado;
        int nuevoCredito= encontrarCredito(id)+ credito;
        Log.d("Actualizando",""+nuevoPagado);

        if("paid".equals(estadoVenta)){
            String queryUpdate= "UPDATE Ventas SET pagadoCliente='"+nuevoPagado+"',estadoVenta='"+
                    estadoVenta+ "',credito='"+nuevoCredito+"' where id='"+id+"'";
            database.execSQL(queryUpdate);
        }else{
            String queryUpdate= "UPDATE Ventas SET pagadoCliente='"+nuevoPagado+"'" +
                    ",credito='"+nuevoCredito+"' where id='"+id+"'";
            database.execSQL(queryUpdate);
        }

    }

    private int encontrarCredito(int id) {

        int pago=0;

        String sql= "SELECT credito FROM Ventas where id='"+id+"'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            pago= cursor.getInt(0);

        }

        cursor.close();
        return pago;

    }

    public int encontrarPago(int id){

        int pago=0;

        String sql= "SELECT pagadoCliente FROM Ventas where id='"+id+"'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            pago= cursor.getInt(0);

        }

        cursor.close();
        return pago;

    }

    public String obtenerCedulaCliente(String nroFactura) {

        String cedula="";

        String sql= "SELECT cedulaCliente FROM Ventas where id='"+nroFactura+"'";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            cedula= cursor.getString(0);

        }

        cursor.close();
        return cedula;

    }
}
