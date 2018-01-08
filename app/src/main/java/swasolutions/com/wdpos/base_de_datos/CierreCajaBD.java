package swasolutions.com.wdpos.base_de_datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sebas on 16/07/2017.
 */

public class CierreCajaBD extends SQLiteOpenHelper {

    String creacionBD= "CREATE TABLE `CierreCaja` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            " `fecha` TEXT NOT NULL, `vendedor` TEXT NOT NULL," +
            " `cantidadProductos` INTEGER NOT NULL, `totalVentas` INTEGER NOT NULL," +
            " `dineroRecibidoVentas` INTEGER NOT NULL, `dineroRecibidoAbonos` INTEGER NOT NULL," +
            " `totalGastos` INTEGER NOT NULL, `dineroEntregar` INTEGER NOT NULL )";

    public CierreCajaBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "CierreCajaBD", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(creacionBD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void agregarCierreCaja(String fecha,String vendedor,int cantidadProductos,int totalVentas,
                                  int totalDineroVentas,int totalDineroAbonos,int totalGastos,
                                  int dineroEntregar) {

        SQLiteDatabase database= this.getWritableDatabase();
        String queryInsertar= "INSERT INTO CierreCaja (id,fecha,vendedor,cantidadProductos," +
                "totalVentas,dineroRecibidoVentas,dineroRecibidoAbonos,totalGastos,dineroEntregar)"+
                "VALUES ("+ null +",'"+fecha+"','"+vendedor+"',"+cantidadProductos+","+totalVentas+
                ","+totalDineroVentas+","+totalDineroAbonos+","+totalGastos+","+dineroEntregar+");";
        database.execSQL(queryInsertar);


    }
}
