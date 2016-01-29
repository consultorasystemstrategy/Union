package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DecimalFormat;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.Precio;

public class DbAdapter_Impresion_Cobros {



    public static final String Imprimir_id= "_id";
    public static final String Imprimir_id_detalle = "Imprimir_id_detalle";
    public static final String Imprimir_id_establecimiento = "Imprimir_id_establecimiento";
    public static final String Imprimir_monto = "Imprimir_monto";
    public static final String Imprimir_tipo = "Imprimir_tipo";
    public static final String Imprimir_fecha = "Imprimir_fecha";
    public static final String Imprimir_cliente = "Imprimir_cliente";
    public static final String Imprimir_documento = "Imprimir_documento";
    public static final String Imprimir_fechaHora = "Imprimir_fechaHora";



    public static final String TAG = "Precio";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    public static final String SQLITE_TABLE_IMPRIMIR_COBRO = "imprimir_cobro";
    private final Context mCtx;

    public static final String CREATE_TABLE_IMPRIMIR_COBRO =
            "create table "+SQLITE_TABLE_IMPRIMIR_COBRO+" ("
                    +Imprimir_id+" integer primary key autoincrement,"
                    +Imprimir_id_detalle+" integer,"
                    +Imprimir_id_establecimiento+" integer,"
                    +Imprimir_monto+" text,"
                    +Imprimir_fecha+" text,"
                    +Imprimir_cliente+" text,"
                    +Imprimir_documento+" text,"
                    +Imprimir_fechaHora+" text,"
                    +Imprimir_tipo+" integer);"
            ;

    public static final String DELETE_TABLE_IMPRIMIR_COBRO = "DROP TABLE IF EXISTS " + SQLITE_TABLE_IMPRIMIR_COBRO;

    public DbAdapter_Impresion_Cobros(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Impresion_Cobros open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    public long createImprimir(int id_detalle,int idEstablecimiento,double monto,int tipo,String fecha,String cliente,String documento, String fechaHora){

        ContentValues initialValues = new ContentValues();
        initialValues.put(Imprimir_id_detalle,id_detalle);
        initialValues.put(Imprimir_monto,monto);
        initialValues.put(Imprimir_fecha,fecha);
        initialValues.put(Imprimir_cliente,cliente);
        initialValues.put(Imprimir_documento,documento);
        initialValues.put(Imprimir_fechaHora,fechaHora);
        initialValues.put(Imprimir_id_establecimiento,idEstablecimiento);
        initialValues.put(Imprimir_tipo,tipo);


        return mDb.insert(SQLITE_TABLE_IMPRIMIR_COBRO, null, initialValues);
    }

    public Cursor listarImprimirCobros(String fecha,int idEsta){

        Cursor cr = mDb.rawQuery("select * from "+SQLITE_TABLE_IMPRIMIR_COBRO+" where "+Imprimir_fecha+"='"+fecha+"' and "+Imprimir_id_establecimiento+"='"+idEsta+"' ",null);

        return cr;
    }

}