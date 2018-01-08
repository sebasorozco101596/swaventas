package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.Cliente;

/**
 * Created by sebas on 23/06/2017.
 */

public class ClientesBD extends SQLiteOpenHelper {

    String creacionBD="CREATE TABLE `Clientes` " +
            "( `cedula` TEXT NOT NULL PRIMARY KEY, `nombre` TEXT NOT NULL," +
            " `compania` TEXT NOT NULL, `direccion` TEXT, `telefono` TEXT,`id` TEXT NOT NULL," +
            "`group_id` INTEGER NOT NULL)";


    public ClientesBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "ClientesBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(creacionBD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void agregarCliente(String cedula, String name, String compania, String direccion,
                               String telefono,String id,Context context,int group_id) {

        SQLiteDatabase database= this.getWritableDatabase();
                String queryInsertarCliente= "INSERT INTO Clientes (cedula,nombre,compania,direccion,telefono,id" +
                        ",group_id) VALUES ('"+ cedula +"','"+name+"','"+compania+"','"+
                        direccion+"','"+telefono+"','"+id+"',"+group_id+");";
                database.execSQL(queryInsertarCliente);

    }
    public void eliminarTodosClientes(){

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete= "DELETE FROM Clientes;";
        database.execSQL(queryDelete);

    }

    public ArrayList<Cliente> cargarClientes() {

        //List where all messages will be saved
        ArrayList<Cliente> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();



        //Query that gets messages by group
        String query= "SELECT c.cedula,c.nombre,c.compania,c.direccion,c.telefono,c.id" +
                ",c.group_id FROM Clientes c";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                String cedula=register.getString(0);
                String nombre=register.getString(1);
                String compania= register.getString(2);
                String direccion=register.getString(3);
                String telefono= register.getString(4);
                String id=register.getString(5);
                int group_id= register.getInt(6);

                Log.d("entre","entre"+nombre);

                lista.add(new Cliente(cedula,nombre,compania,direccion,telefono,id,group_id));
            }while (register.moveToNext());
        }

        return lista;
    }

    public int contarFilas(){

        int contador=1;

        String sql= "SELECT COUNT(*) FROM Clientes";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){

            cursor.moveToFirst();
            contador= cursor.getInt(0);

        }

        cursor.close();
        return contador;

    }

    public boolean existeCliente(String cedula) {

        boolean bandera=false;

        String sql= "SELECT * FROM Clientes where cedula='"+cedula+"';";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount() >0){
            bandera= true;
        }
        cursor.close();

        return bandera;

    }

    public String buscarCliente(String cedulacliente) {

        String id="";

        String sql= "SELECT id FROM Clientes where cedula='"+cedulacliente+"';";

        Cursor cursor= getReadableDatabase().rawQuery(sql,null);
        SQLiteDatabase database= this.getWritableDatabase();

        Cursor register= database.rawQuery(sql,null);

        if (register.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                id = register.getString(0);
            } while(register.moveToNext());
        }

        register.close();


        return id;
    }

    public String obtenerCedula(String idCliente) {

        String cedula="";

        String sql= "SELECT cedula FROM Clientes where id='"+idCliente+"';";

        SQLiteDatabase database= this.getWritableDatabase();

        Cursor register= database.rawQuery(sql,null);

        if (register.moveToFirst()) {

            cedula = register.getString(0);
        }

        register.close();

        return cedula;

    }

    public void actualizarCliente(int grupo, String nombre, String direccion, String telefono,
                                  String cedula) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryActualizar= "UPDATE Clientes SET group_id="+grupo+", nombre='"+nombre+"'," +
                "direccion='"+direccion+"', telefono= '"+telefono+"' where cedula='"+cedula+"';";
        database.execSQL(queryActualizar);

    }
}
