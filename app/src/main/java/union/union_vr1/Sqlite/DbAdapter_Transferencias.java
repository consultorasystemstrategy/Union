package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.Transferencias;


public class DbAdapter_Transferencias {


    public static final String T_id = "_id";
    public static final String T_id_liquidacion = "_id_liquidacion";
    public static final String T_id_transferencia = "id_transferencia";
    public static final String T_codigo = "codigo";
    public static final String T_producto = "producto";
    public static final String T_cantidad = "cantidad";
    public static final String T_descripcion_transferencia = "descripcion_transferencia";
    public static final String T_almacen_id = "almacen_id";


    public static final String TAG = DbAdapter_Transferencias.class.getSimpleName();
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_TRANSFERENCIAS= "m_transferencias";
    private final Context mCtx;

    public static final String CREATE_TABLE_TRANSFERENCIAS =
            "create table if not exists "+SQLITE_TABLE_TRANSFERENCIAS+" ("
                    +T_id+" integer primary key autoincrement,"
                    +T_id_liquidacion+" integer, "
                    +T_id_transferencia+" text,"
                    +T_codigo+" text, "
                    +T_producto+" text, "
                    +T_cantidad+" integer, "
                    +T_descripcion_transferencia+" text, "
                    +T_almacen_id+" integer);";

    public static final String DELETE_TABLE_TRANSFERENCIAS = "DROP TABLE IF EXISTS " + SQLITE_TABLE_TRANSFERENCIAS;

    public DbAdapter_Transferencias(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Transferencias open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTransferencias(Transferencias transferencias, int liquidacion) {

        long _id_transferencias = -1;
        /**
         * SE CREA UN REGISTRO DE TRANSFERENCIAS
         * T_id_transferencia
         * T_codigo
         * T_producto
         * T_cantidad
         * T_descripcion_transferencia
         * T_almacen_id
         * */
        ContentValues initialValues = new ContentValues();
        initialValues.put(T_id_transferencia,transferencias.getIdTransferencia());
        initialValues.put(T_id_liquidacion,liquidacion);
        initialValues.put(T_codigo,transferencias.getCodigo());
        initialValues.put(T_producto, transferencias.getProducto());
        initialValues.put(T_cantidad, transferencias.getCantidad());
        initialValues.put(T_descripcion_transferencia, transferencias.getDescripcionTransferencia());
        initialValues.put(T_almacen_id, transferencias.getAlmacenId());

        _id_transferencias = mDb.insert(SQLITE_TABLE_TRANSFERENCIAS, null, initialValues);
        Log.d(TAG, "_ID TRANSFERENCIAS : " + _id_transferencias);
        return _id_transferencias;
    }

    public int updateTransferencia(Transferencias transferencias, int liquidacion) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(T_codigo,transferencias.getCodigo());
        initialValues.put(T_id_liquidacion,liquidacion);
        initialValues.put(T_producto, transferencias.getProducto());
        initialValues.put(T_cantidad, transferencias.getCantidad());
        initialValues.put(T_descripcion_transferencia, transferencias.getDescripcionTransferencia());
        initialValues.put(T_almacen_id, transferencias.getAlmacenId());

        return mDb.update(SQLITE_TABLE_TRANSFERENCIAS, initialValues,
                T_id_transferencia + "=?", new String[]{"" + transferencias.getIdTransferencia()});
    }

    public Cursor getTransferencias(int liquidacion) {
        Cursor mCursor = null;
        mCursor = mDb.query(SQLITE_TABLE_TRANSFERENCIAS, new String[] {T_id,
                        T_id_transferencia, T_codigo, T_producto,
                        T_cantidad, T_descripcion_transferencia, T_almacen_id, T_id_liquidacion
                },
                T_id_liquidacion + " = " + liquidacion, null,
                null, null, T_almacen_id + " ASC", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean existeTransferencia(String id_transferencia) {
        boolean exists = false;
        Cursor mCursor = mDb.query(SQLITE_TABLE_TRANSFERENCIAS, new String[] {T_id,
                        T_id_transferencia, T_codigo, T_producto,
                        T_cantidad, T_descripcion_transferencia, T_almacen_id, T_id_liquidacion
                },
                T_id_transferencia + " = " + id_transferencia, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
            exists = true;
        }
        if (mCursor.getCount() == 0) {
            exists = false;
        }
        return exists;
    }

}
