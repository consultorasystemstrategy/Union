package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import union.union_vr1.Conexion.DbHelper;

/**
 * Created by Usuario on 31/12/2015.
 */
public class DbAdapter_Tipo_Doc_Identidad {

    public static final String motivo_id = "_id";
    public static final String motivo_DevolucionId = "TdiTipoDocIdentidadId";
    public static final String motivo_Descripcion = "TdiVDescripcion";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_MOTIVO_DEVOLUCION = "m_temp_spinner_data";
    private final Context mCtx;

    public static final String CREATE_TABLE_MOTIVO_DEVOLUCION =
            "create table if not exists "+SQLITE_TABLE_MOTIVO_DEVOLUCION+" ("
                    +motivo_id+" integer primary key,"
                    +motivo_DevolucionId+" integer ,"
                    +motivo_Descripcion+" text );";

    public static final String DELETE_TABLE_MOTIVO_DEVOLUCION= "DROP TABLE IF EXISTS " + SQLITE_TABLE_MOTIVO_DEVOLUCION;

    public DbAdapter_Tipo_Doc_Identidad(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Tipo_Doc_Identidad open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createMotivoDevolu(int id,String descripcion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(motivo_DevolucionId, id);
        initialValues.put(motivo_Descripcion, descripcion);
        return mDb.insert(SQLITE_TABLE_MOTIVO_DEVOLUCION, null, initialValues);
    }


    public Cursor fetchMotivoDevolu() {
        Cursor cursor = mDb.rawQuery("select * from "+SQLITE_TABLE_MOTIVO_DEVOLUCION+" order by "+motivo_DevolucionId+" asc",null);
        return cursor;
    }


    public boolean deleteAllMotivoDevolu() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_MOTIVO_DEVOLUCION, null ,null);
        return doneDelete > 0;

    }
}
