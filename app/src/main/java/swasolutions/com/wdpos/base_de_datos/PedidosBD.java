package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

import swasolutions.com.wdpos.vo.clases_objeto.Pedido;

/**
 * Created by sebas on 21/10/2017.
 */

public class PedidosBD extends SQLiteOpenHelper {

    private String queryCreate= "CREATE TABLE Pedidos ( `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            " `pedido` TEXT NOT NULL, `cliente` TEXT NOT NULL)";

    public PedidosBD(Context context, SQLiteDatabase.CursorFactory factory,
                     int version) {
        super(context, "PedidosBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(queryCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void crearPedido(String pedido,String cliente,Context context){

        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsertarPedido= "INSERT INTO Pedidos (id,pedido,cliente)"+
                "VALUES ("+ null +",'"+pedido+"','"+cliente+"');";
        Toast.makeText(context,"Pedido agregado satisfactoriamente",Toast.LENGTH_SHORT).show();


        database.execSQL(queryInsertarPedido);

    }

    public ArrayList<Pedido> pedidos(){

        //List where all messages will be saved
        ArrayList<Pedido> lista= new ArrayList<>();
        SQLiteDatabase database= this.getWritableDatabase();

        //Query that gets messages by group
        String query= "SELECT id,pedido,cliente FROM Pedidos ";

        Cursor register= database.rawQuery(query,null);

        if(register.moveToFirst()){
            do {
                /**
                 * Attributes of each message
                 */
                int id= register.getInt(0);
                String pedido= register.getString(1);
                String cliente= register.getString(2);


                lista.add(new Pedido(id,pedido,cliente));
            }while (register.moveToNext());
        }

        return lista;
    }

    public void eliminarPedidos(){

        SQLiteDatabase database= this.getWritableDatabase();
        String queryDelete = "Delete from Pedidos";
        database.execSQL(queryDelete);
    }
}
