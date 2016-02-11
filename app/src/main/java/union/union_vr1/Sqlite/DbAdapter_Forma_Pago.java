package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.FormaPago;

/**
 * Created by Usuario on 10/02/2016.
 */
public class DbAdapter_Forma_Pago {


    public static final String FP_id = "_id";
    public static final String FP_id_forma_pago= "_id_forma_pago";
    public static final String FP_detalle = "_detalle";
    public static final String FP_selected= "_selected";
    public static final String FP_liquidacion= "_liquidacion";
    public static final String FP_fecha = "_fecha";

    public static final String FP_estado_sincronizacion = Constants._SINCRONIZAR;

    public static final String TAG = DbAdapter_Forma_Pago.class.getSimpleName();
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //


    private static final String SQLITE_TABLE_M_FORMA_PAGO= "m_forma_pago";
    private final Context mCtx;

    public static final String CREATE_TABLE_M_FORMA_PAGO =
            "create table if not exists "+SQLITE_TABLE_M_FORMA_PAGO+" ("
                    +FP_id+" integer primary key autoincrement,"
                    +FP_id_forma_pago+" integer,"
                    +FP_detalle+" text, "
                    +FP_selected+" integer, "
                    +FP_liquidacion+" integer, "
                    +FP_fecha+" text, "
                    +FP_estado_sincronizacion+" integer);";

    public static final String DELETE_TABLE_M_FORMA_PAGO = "DROP TABLE IF EXISTS " + SQLITE_TABLE_M_FORMA_PAGO;

    public DbAdapter_Forma_Pago(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Forma_Pago open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    public long createFormaPago(FormaPago formaPAgo) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(FP_id_forma_pago,formaPAgo.get_id_forma_pago());
        initialValues.put(FP_detalle,formaPAgo.getDetalle());
        initialValues.put(FP_selected,formaPAgo.getSelected());
        initialValues.put(FP_fecha,formaPAgo.getFecha());
        initialValues.put(FP_liquidacion,formaPAgo.getLiquidacion());
        initialValues.put(FP_estado_sincronizacion, formaPAgo.getEstadoSincronizacion());

        return mDb.insert(SQLITE_TABLE_M_FORMA_PAGO, null, initialValues);
    }

    public int  updateFormaPago(FormaPago formaPAgo) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(FP_id_forma_pago,formaPAgo.get_id_forma_pago());
        initialValues.put(FP_detalle,formaPAgo.getDetalle());
        initialValues.put(FP_selected,formaPAgo.getSelected());
        initialValues.put(FP_fecha,formaPAgo.getFecha());
        initialValues.put(FP_liquidacion,formaPAgo.getLiquidacion());
        initialValues.put(FP_estado_sincronizacion, Constants._ACTUALIZADO);

        return mDb.update(SQLITE_TABLE_M_FORMA_PAGO, initialValues, FP_id_forma_pago + "=?", new String[]{"" + formaPAgo.get_id_forma_pago()});

    }




    public int deleteAllFormaPagos() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_M_FORMA_PAGO, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete ;

    }

    public boolean existeFormaPagos(int idFormaPago) throws SQLException {

        boolean exists = false;
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_M_FORMA_PAGO, new String[]{FP_id_forma_pago,
                        FP_id, FP_detalle, FP_selected, FP_fecha, FP_liquidacion, FP_estado_sincronizacion},
                FP_id_forma_pago + " = " + idFormaPago, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            exists = true;
        }
        if (mCursor.getCount()==0){
            exists = false;
        }
        return exists;
    }
    public Cursor fetchFormaPagoByID(long _id) throws SQLException {

        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_M_FORMA_PAGO, new String[]{FP_id_forma_pago,
                        FP_id, FP_detalle, FP_selected, FP_fecha, FP_liquidacion, FP_estado_sincronizacion},
                FP_id + " = " + _id, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



    public Cursor fetchAllFormaPagos() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_M_FORMA_PAGO, new String[]{FP_id_forma_pago,
                        FP_id, FP_detalle, FP_selected, FP_fecha, FP_liquidacion, FP_estado_sincronizacion},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
