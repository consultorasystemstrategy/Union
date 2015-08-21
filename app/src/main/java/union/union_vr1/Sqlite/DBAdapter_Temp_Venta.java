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

public class DBAdapter_Temp_Venta {

    public static final String temp_venta_detalle = "_id";
    public static final String temp_id_comprob = "temp_in_id_comprob";
    public static final String temp_id_producto = "temp_in_id_producto";
    public static final String temp_nom_producto = "temp_te_nom_producto";
    public static final String temp_cantidad = "temp_in_cantidad";
    public static final String temp_precio_unit = "temp_re_precio_unit";
    public static final String temp_importe = "temp_re_importe";
    public static final String temp_procedencia = "temp_re_procedencia";
    public static final String temp_codigo_producto = "temp_te_codigo_producto";


    public static final String temp_prom_anterior = "temp_te_prom_anterior";
    public static final String temp_devuelto = "temp_te_devuelto";

    public static final String temp_valor_unidad = "temp_in_valor_unidad";
    public static final String temp_costo_venta = "temp_re_costo_venta";

    public static final String TAG = "Temp_Venta_Detalle";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_Temp_Venta_Detalle = "m_temp_venta_detalle";
    private final Context mCtx;

    public static final String CREATE_TABLE_TEMP_VENTA_DETALLE =
            "create table "+SQLITE_TABLE_Temp_Venta_Detalle+" ("
                    +temp_venta_detalle+" integer primary key autoincrement,"
                    +temp_id_comprob+" integer,"
                    +temp_id_producto+" integer,"
                    +temp_nom_producto+" text,"
                    +temp_cantidad+" integer,"
                    +temp_importe+" real,"
                    +temp_costo_venta+" real,"
                    +temp_precio_unit+" real,"
                    +temp_prom_anterior+" text,"
                    +temp_devuelto+" text,"
                    +temp_codigo_producto+" text,"
                    +temp_procedencia+" integer,"
                    +temp_valor_unidad+" integer);";

    public static final String DELETE_TABLE_TEMP_VENTA_DETALLE = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Temp_Venta_Detalle;

    public DBAdapter_Temp_Venta(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter_Temp_Venta open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTempVentaDetalle(
            int id_comprob, int id_producto, String nom_producto, int cantidad, double importe,
            double precio_unit, String prom_anterior, String devuelto, int procedencia, int valorUnidad, String codigoProducto){

        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_id_comprob,id_comprob);
        initialValues.put(temp_id_producto,id_producto);
        initialValues.put(temp_nom_producto,nom_producto);
        initialValues.put(temp_cantidad,cantidad);
        initialValues.put(temp_importe,importe);
        initialValues.put(temp_precio_unit,precio_unit);
        initialValues.put(temp_prom_anterior,prom_anterior);
        initialValues.put(temp_devuelto,devuelto);
        initialValues.put(temp_procedencia, procedencia);
        initialValues.put(temp_valor_unidad, valorUnidad);
        initialValues.put(temp_codigo_producto, codigoProducto);


        return mDb.insert(SQLITE_TABLE_Temp_Venta_Detalle, null, initialValues);
    }

    public void updateTempVentaDetalle1(String idorig, String iddest, String vals){
        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_id_comprob,iddest);

        mDb.update(SQLITE_TABLE_Temp_Venta_Detalle, initialValues,
                temp_venta_detalle+"=?",new String[]{idorig});
    }

    public void updateTempVentaDetalleCantidad(long id_temp_venta_detalle, int cantidad, double total){
        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_cantidad, cantidad);
        initialValues.put(temp_importe, total);

        mDb.update(SQLITE_TABLE_Temp_Venta_Detalle, initialValues,
                temp_venta_detalle+"=?",new String[]{""+id_temp_venta_detalle});
    }

    public boolean  deleteAllTempVentaDetalle() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Temp_Venta_Detalle, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public boolean deleteTempVentaDetalleById(long id_temp_venta_detalle) {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Temp_Venta_Detalle, temp_venta_detalle+"=?", new String[]{""+id_temp_venta_detalle});
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchAllTempVentaDetalle() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Temp_Venta_Detalle, new String[] {temp_venta_detalle,
                temp_id_comprob,temp_id_producto, temp_nom_producto, temp_cantidad, temp_precio_unit,temp_valor_unidad, temp_importe,
                temp_prom_anterior, temp_devuelto, temp_codigo_producto},null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllTempVentaDetalleByProcedencia(int procedencia) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Temp_Venta_Detalle, new String[] {temp_venta_detalle,
                temp_id_comprob,temp_id_producto, temp_nom_producto, temp_cantidad, temp_precio_unit,temp_precio_unit, temp_importe,
                temp_prom_anterior, temp_devuelto, temp_codigo_producto},temp_procedencia + " = ?", new String[]{""+procedencia}, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllTempVentaDetalleByID(long id) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Temp_Venta_Detalle, new String[] {temp_venta_detalle,
                temp_id_comprob,temp_id_producto, temp_nom_producto, temp_cantidad, temp_precio_unit,temp_precio_unit, temp_importe,
                temp_prom_anterior, temp_devuelto, temp_codigo_producto},temp_venta_detalle + " = ?", new String[]{""+id}, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchTempVentaByIDProducto(int id) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Temp_Venta_Detalle, new String[] {temp_venta_detalle,
                temp_id_comprob,temp_id_producto, temp_nom_producto, temp_cantidad, temp_precio_unit,temp_precio_unit, temp_importe,
                temp_prom_anterior, temp_devuelto, temp_codigo_producto},temp_id_producto + " = ?", new String[]{""+id}, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}
