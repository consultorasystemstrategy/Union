package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.TipoGasto;

/**
 * Created by Usuario on 22/12/2015.
 */
public class DbAdapter_Exportacion_Comprobantes {


    public static final String EC_id = "_id";
    public static final String EC_id_sqlite = "_id_sqlite";
    public static final String EC_id_sid = "_id_sid";
    public static final String EC_id_flex = "_id_flex";
    public static final String EC_liquidacion = "liquidacion";

    public static final String EC_estado_sincronizacion = Constants._SINCRONIZAR;

    public static final String TAG = DbAdapter_Exportacion_Comprobantes.class.getSimpleName();
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //


    public static final String SQLITE_TABLE_EXPORTACION_COMPROBANTES= "m_exportacion_comprobantes";
    private final Context mCtx;

    public static final String CREATE_TABLE_EXPORTACION_COMPROBANTES =
            "create table if not exists "+SQLITE_TABLE_EXPORTACION_COMPROBANTES+" ("
                    +EC_id+" integer primary key autoincrement,"
                    +EC_id_sqlite+" integer,"
                    +EC_id_sid+" integer, "
                    +EC_id_flex+" integer, "
                    +EC_liquidacion+" integer, "
                    +EC_estado_sincronizacion+" integer);";

    public static final String DELETE_TABLE_EXPORTACION_COMPROBANTES = "DROP TABLE IF EXISTS " + SQLITE_TABLE_EXPORTACION_COMPROBANTES;

    public DbAdapter_Exportacion_Comprobantes(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Exportacion_Comprobantes open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createRegistroExportacion(long id_sqlite, int id_sid, int estado_sincronizacion, int liquidacion) {

        long _id_mapeo = -1;
        /**
         * SE CREA UN REGISTRO QUE MAPEA LOS ID DE LOS COMPROBANTES ENTRE LOS SISTEMAS
         * _ID_SQLITE
         * _ID_SID
         * _ID_FLEX
         * */
        ContentValues initialValues = new ContentValues();
        initialValues.put(EC_id_sqlite,id_sqlite);
        initialValues.put(EC_id_sid,id_sid);
        initialValues.put(EC_id_flex,Constants._FLEX_ID_DEFECTO);
        initialValues.put(EC_estado_sincronizacion, estado_sincronizacion);
        initialValues.put(EC_liquidacion, liquidacion);



        _id_mapeo = mDb.insert(SQLITE_TABLE_EXPORTACION_COMPROBANTES, null, initialValues);
        Log.d(TAG, "_ID MAPEO : "+ _id_mapeo);
        return _id_mapeo;
    }
    public int updateRegistroExportacion(long id_sqlite, int id_sid, int estado_sincronizacion, int liquidacion) {

        int updated = -1;
        /**
         * SE CREA UN REGISTRO QUE MAPEA LOS ID DE LOS COMPROBANTES ENTRE LOS SISTEMAS
         * _ID_SQLITE
         * _ID_SID
         * _ID_FLEX
         * */
        ContentValues initialValues = new ContentValues();
        initialValues.put(EC_id_sqlite,id_sqlite);
        initialValues.put(EC_id_sid,id_sid);
        initialValues.put(EC_id_flex,Constants._FLEX_ID_DEFECTO);
        initialValues.put(EC_estado_sincronizacion, estado_sincronizacion);
        initialValues.put(EC_liquidacion, liquidacion);



        updated = mDb.update(SQLITE_TABLE_EXPORTACION_COMPROBANTES, initialValues, EC_id_sqlite + " = ?", new String[]{"" + id_sqlite});
        Log.d(TAG, "UPDATED : "+ updated);
        return updated;
    }


    public boolean existeRegistroExport(long _id_sqlite) throws SQLException {
        boolean exists = false;
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_EXPORTACION_COMPROBANTES, new String[] {EC_id,
                        EC_id_sqlite, EC_id_sid, EC_id_flex,
                        EC_estado_sincronizacion, EC_liquidacion
                },
                EC_id_sqlite + " = " + _id_sqlite, null,
                null, null, null, null);

        if (mCursor.getCount() >0) {
            mCursor.moveToFirst();
            exists = true;
        }else{
            exists = false;
        }

        return exists;

    }

    public Cursor filterExport(int liquidacion) {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_EXPORTACION_COMPROBANTES, new String[] {EC_id,
                        EC_id_sqlite, EC_id_sid, EC_id_flex,
                        EC_estado_sincronizacion, EC_liquidacion
                },
                EC_estado_sincronizacion + " = " + Constants._CREADO + " AND "+EC_liquidacion + " = "+liquidacion + " AND " + EC_id_sid + " > 0", null,
                null, null, null, null);
        if (mCursor.getCount() > 0) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public String fetchIDSID_bysqlite(int _id_sqlite) {

        String sid_id = "SIN EXPORTAR";
        Cursor mCursor = mDb.query(true, SQLITE_TABLE_EXPORTACION_COMPROBANTES, new String[] {EC_id,
                        EC_id_sqlite, EC_id_sid, EC_id_flex,
                        EC_estado_sincronizacion, EC_liquidacion
                },
                EC_id_sqlite + " = ?", new String[]{""+_id_sqlite}, null, null, null, null);

        if (mCursor.getCount() >0) {
            mCursor.moveToFirst();
            sid_id = mCursor.getString(mCursor.getColumnIndexOrThrow(EC_id_sid));
        }
        return sid_id;
    }

    public Cursor fetchAll() {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_EXPORTACION_COMPROBANTES, new String[] {EC_id,
                        EC_id_sqlite, EC_id_sid, EC_id_flex,
                        EC_estado_sincronizacion, EC_liquidacion
                },
                null, null,
                null, null, null, null);
        if (mCursor.getCount() > 0) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public int changeEstadoToExport(String[] _ids, int estadoSincronizacion){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR, estadoSincronizacion);

        String signosInterrogacion = "";
        for (int i=0; i<_ids.length; i++){
            if (i==_ids.length-1)
            {
                signosInterrogacion+= "?";
            }else {
                signosInterrogacion+= "? OR ";
            }

        }

        Log.d("SIGNOS INTERROGACIÓN", signosInterrogacion);
        int cantidadRegistros = mDb.update(SQLITE_TABLE_EXPORTACION_COMPROBANTES, initialValues,
                EC_id+"= "+ signosInterrogacion,_ids);


        Log.d("REGISTROS ACTUALIZADO ", ""+cantidadRegistros);
        return cantidadRegistros;
    }

    public int changeEstado(int estado, int liquidacion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR, estado);

        return mDb.update(SQLITE_TABLE_EXPORTACION_COMPROBANTES, initialValues, EC_liquidacion  +" = ?",  new String[]{""+liquidacion} );
    }
    public int changeEstadoToNoExportOne(int id_sid) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR, Constants._CREADO);

        return mDb.update(SQLITE_TABLE_EXPORTACION_COMPROBANTES, initialValues, EC_id_sid + "=?", new String[]{""+id_sid} );
    }

}
