package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;

/**
 * Created by Usuario on 15/12/2014.
 */

public class DBAdapter_Temp_Canjes_Devoluciones {

    public static final String temp_id_canjes_devoluciones = "_id";
    public static final String temp_id_comprob = "temp_in_id_comprob";
    public static final String temp_id_producto = "temp_in_id_producto";
    public static final String temp_nom_producto = "temp_te_nom_producto";
    public static final String temp_codigo_producto = "temp_te_codigo_producto";
    public static final String temp_cantidad = "temp_in_cantidad";
    public static final String temp_precio_unit = "temp_re_precio_unit";
    public static final String temp_importe = "temp_importe";
    public static final String temp_lote = "temp_lote";
    public static final String temp_fecha_emision = "temp_fecha_emision";
    public static final String temp_cliente = "temp_cliente";
    public static final String temp_id_establecimiento = "temp_id_establecimiento";
    public static final String temp_id_documento = "temp_id_documento";
    public static final String temp_id_motivo = "temp_id_motivo";
    public static final String temp_estado_producto = "temp_id_estado_producto";

    public static final String temp_estado = "temp_estado";


    public static final String TAG = "Temp_Venta_Detalle";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_Temp_Canjes_Devoluciones = "m_temp_canjes_devoluciones";
    private final Context mCtx;

    public static final String CREATE_TABLE_TEMP_CANJES_DEVOLUCIONES =
            "create table " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " ("
                    + temp_id_canjes_devoluciones + " integer primary key autoincrement,"
                    + temp_id_comprob+" text,"
                    + temp_id_producto+" text,"
                    + temp_nom_producto+" text,"
                    + temp_codigo_producto+" text,"
                    + temp_cantidad+" text,"
                    + temp_precio_unit+ " text,"
                    + temp_importe+" text,"
                    + temp_lote+" text,"
                    + temp_fecha_emision+" text,"
                    + temp_cliente+" text,"
                    + temp_id_establecimiento+" text,"
                    + temp_id_documento+" text,"
                    + temp_id_motivo+" text,"
                    + temp_estado_producto+" text,"
                    + temp_estado + " integer);";


    public static final String DELETE_TABLE_TEMP_CANJES_DEVOLUCIONES = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Temp_Canjes_Devoluciones;

    public DBAdapter_Temp_Canjes_Devoluciones(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter_Temp_Canjes_Devoluciones open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTempCanjesDevoluciones(String id_comprob, String id_producto, String nom_producto, String codigo_producto, String cantidad, String precio_unit, String importe, String lote, String fecha_emision, String cliente,
                                       String id_establecimiento, String id_documento, String id_motivo, String estado_producto, String estado) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_id_comprob, id_comprob);
        initialValues.put(temp_id_producto, id_producto);
        initialValues.put(temp_nom_producto, nom_producto);
        initialValues.put(temp_codigo_producto, codigo_producto);
        initialValues.put(temp_cantidad, cantidad);
        initialValues.put(temp_precio_unit, precio_unit);
        initialValues.put(temp_importe, importe);
        initialValues.put(temp_lote, lote);
        initialValues.put(temp_fecha_emision, fecha_emision);
        initialValues.put(temp_cliente, cliente);
        initialValues.put(temp_id_establecimiento, id_establecimiento);
        initialValues.put(temp_id_documento, id_documento);
        initialValues.put(temp_id_motivo, id_motivo);
        initialValues.put(temp_estado_producto, estado_producto);
        initialValues.put(temp_estado, estado);

        return mDb.insert(SQLITE_TABLE_Temp_Canjes_Devoluciones, null, initialValues);
    }
    public Cursor listarCanjes(){
        Cursor cursor = mDb.rawQuery("select * from "+SQLITE_TABLE_Temp_Canjes_Devoluciones+" where "+temp_id_motivo+"='2';",null);
        return  cursor;
    }
    public Cursor listarDevoluciones(){
        Cursor cursor = mDb.rawQuery("select * from "+SQLITE_TABLE_Temp_Canjes_Devoluciones+" where "+temp_id_motivo+"='1';",null);
        return  cursor;
    }

    public void updateTempCanjesDevoluciones2(String idorig, String iddest, String vals) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_id_comprob, iddest);

        mDb.update(SQLITE_TABLE_Temp_Canjes_Devoluciones, initialValues,
                temp_id_canjes_devoluciones + "=?", new String[]{idorig});
    }

    public void updateTempCanjesDevoluciones(long id_temp_venta_detalle, int cantidad, double total) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_cantidad, cantidad);
        initialValues.put(temp_importe, total);

        mDb.update(SQLITE_TABLE_Temp_Canjes_Devoluciones, initialValues,
                temp_id_canjes_devoluciones + "=?", new String[]{"" + id_temp_venta_detalle});


    }
    public int truncateCanjesDevoluciones(){
        return mDb.delete(SQLITE_TABLE_Temp_Canjes_Devoluciones,null,null);
    }
    public int deleteCanjesDevoluciones(int id){
        return mDb.delete(SQLITE_TABLE_Temp_Canjes_Devoluciones, temp_id_canjes_devoluciones + " = ?", new String[] { id+"" });
    }



}
