package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.ModalidadCredito;
import union.union_vr1.Objects.TipoGasto;

/**
 * Created by Usuario on 12/01/2016.
 */
public class DbAdapter_ModalidadCredito {


    public static final String TM_id = "_id";
    public static final String TM_id_modalidad_credito = "tm_id_modalidad";
    public static final String TM_dias_credito = "tm_dias_credito";
    public static final String TM_descripcion = "tm_descripcion";
    public static final String estado_sincronizacion = "estado_sincronizacion";

    public static final String TAG = DbAdapter_ModalidadCredito.class.getSimpleName();
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_Modalidad_credito = "m_modalidad_credito";
    private final Context mCtx;

    public static final String CREATE_TABLE_MODALIDAD_CREDITO =
            "create table if not exists "+SQLITE_TABLE_Modalidad_credito+" ("
                    +TM_id+" integer primary key autoincrement,"
                    +TM_id_modalidad_credito+" integer,"
                    +TM_dias_credito+" text, "
                    +TM_descripcion+" text, " +
                    estado_sincronizacion+" integer);";

    public static final String DELETE_TABLE_MODALIDAD_CREDITO = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Modalidad_credito;

    public DbAdapter_ModalidadCredito(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_ModalidadCredito open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createModalidadCreditos(ModalidadCredito modalidadCredito) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(TM_id_modalidad_credito,modalidadCredito.getId());
        initialValues.put(TM_dias_credito,modalidadCredito.getDiasCredito());
        initialValues.put(TM_descripcion,modalidadCredito.getDescripcion());
        initialValues.put(estado_sincronizacion, modalidadCredito.getEstadoSincronizacion());

        return mDb.insert(SQLITE_TABLE_Modalidad_credito, null, initialValues);
    }

    public int  updateModalidadCredito(ModalidadCredito modalidadCredito) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(TM_id_modalidad_credito,modalidadCredito.getId());
        initialValues.put(TM_dias_credito,modalidadCredito.getDiasCredito());
        initialValues.put(TM_descripcion,modalidadCredito.getDescripcion());
        initialValues.put(estado_sincronizacion, Constants._ACTUALIZADO);

        return mDb.update(SQLITE_TABLE_Modalidad_credito, initialValues, TM_id + "=?", new String[]{"" + modalidadCredito.getId()});

    }




    public boolean deleteAllModalidadCreditos() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Modalidad_credito, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public boolean existeModalidadCreditos(int idModalidadCredito) throws SQLException {

        boolean exists = false;
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Modalidad_credito, new String[]{TM_id,
                        TM_id_modalidad_credito, TM_dias_credito, TM_descripcion, estado_sincronizacion},
                TM_id_modalidad_credito + " = " + idModalidadCredito, null,
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
    public Cursor fetchModalidadCreditoByID(long _id) throws SQLException {

        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Modalidad_credito, new String[] {TM_id,
                        TM_id_modalidad_credito, TM_dias_credito, TM_descripcion, estado_sincronizacion},
                TM_id + " = " + _id  , null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



    public Cursor fetchAllModalidadCreditos() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Modalidad_credito, new String[] {TM_id,
                        TM_id_modalidad_credito, TM_dias_credito, TM_descripcion, estado_sincronizacion},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
