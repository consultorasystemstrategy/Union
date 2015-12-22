package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.TipoGasto;

public class DbAdapter_Tipo_Gasto {

    public static final String TG_id_tipo_gasto = "_id";
    public static final String TG_id_tgasto = "tg_in_id_tgasto";
    public static final String TG_nom_tipo_gasto = "tg_te_nom_tipo_gasto";
    public static final String estado_sincronizacion = "estado_sincronizacion";

    public static final String TAG = "Tipo_Gasto";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_Tipo_Gasto = "m_tipo_gasto";
    private final Context mCtx;

    public static final String CREATE_TABLE_TIPO_GASTO =
            "create table if not exists "+SQLITE_TABLE_Tipo_Gasto+" ("
                    +TG_id_tipo_gasto+" integer primary key autoincrement,"
                    +TG_id_tgasto+" integer,"
                    +TG_nom_tipo_gasto+" text, " +
                    estado_sincronizacion+" integer);";

    public static final String DELETE_TABLE_TIPO_GASTO = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Tipo_Gasto;

    public DbAdapter_Tipo_Gasto(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Tipo_Gasto open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTipoGastos(
            int id_tgasto, String nom_tipo_gasto) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(TG_id_tgasto,id_tgasto);
        initialValues.put(TG_nom_tipo_gasto,nom_tipo_gasto);

        return mDb.insert(SQLITE_TABLE_Tipo_Gasto, null, initialValues);
    }

    public long createTipoGasto(TipoGasto tipoGasto) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(TG_id_tgasto,tipoGasto.getIdTipoGasto());
        initialValues.put(TG_nom_tipo_gasto,tipoGasto.getNombreTipoGasto());
        initialValues.put(Constants._SINCRONIZAR,tipoGasto.getEstadoSincronizacion());

        return mDb.insert(SQLITE_TABLE_Tipo_Gasto, null, initialValues);
    }
    public int  updateTipoGasto(TipoGasto tipoGasto) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(TG_id_tgasto,tipoGasto.getIdTipoGasto());
        initialValues.put(TG_nom_tipo_gasto,tipoGasto.getNombreTipoGasto());
        initialValues.put(Constants._SINCRONIZAR,tipoGasto.getEstadoSincronizacion());

        return mDb.update(SQLITE_TABLE_Tipo_Gasto, initialValues, TG_id_tgasto + "=?", new String[]{""+tipoGasto.getIdTipoGasto()} );

    }




    public boolean deleteAllTipoGastos() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Tipo_Gasto, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchTipoGastosByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Tipo_Gasto, new String[] {TG_id_tipo_gasto,
                            TG_id_tgasto, TG_nom_tipo_gasto},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Tipo_Gasto, new String[] {TG_id_tipo_gasto,
                            TG_id_tgasto, TG_nom_tipo_gasto},
                    TG_nom_tipo_gasto + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    public boolean existeTipoGastos(int idTipoGasto) throws SQLException {

        boolean exists = false;
        Cursor mCursor = null;
            mCursor = mDb.query(true, SQLITE_TABLE_Tipo_Gasto, new String[] {TG_id_tipo_gasto,
                            TG_id_tgasto, TG_nom_tipo_gasto},
                    TG_id_tgasto + " = " + idTipoGasto  , null,
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

    public Cursor fetchAllTipoGastos() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Tipo_Gasto, new String[] {TG_id_tipo_gasto,
                        TG_id_tgasto, TG_nom_tipo_gasto},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeTipoGastos() {

        //Creando algunos tipos de gastos
        createTipoGastos(1, "Combustible");
        createTipoGastos(2, "Comida");
        createTipoGastos(3, "Departamento");
        createTipoGastos(4, "Viaje");
        createTipoGastos(5, "Nuevo");

    }

}