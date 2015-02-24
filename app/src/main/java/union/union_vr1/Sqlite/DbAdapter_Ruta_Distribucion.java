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
 * Created by Usuario on 18/02/2015.
 */
public class DbAdapter_Ruta_Distribucion {

    public static final String RD_id_ = "_id";
    public static final String RD_id_ruta_distribucion = "id_ruta_distribucion";
    public static final String RD_id_ruta = "id_ruta";
    public static final String RD_nombre_ruta = "nombre_ruta";
    public static final String RD_dia_semana = "dia_semana";
    public static final String RD_numero_establecimientos = "numero_establecimientos";
    public static final String RD_id_agente = "id_agente";
    public static final String TAG = "Ruta_Distribución";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_RUTA_DISTRIBUCION = "m_ruta_distribucion";
    private final Context mCtx;

    public static final String CREATE_TABLE_RUTA_DISTRIBUCION =
            "create table if not exists "+SQLITE_TABLE_RUTA_DISTRIBUCION+" ("
                    +RD_id_+" integer primary key autoincrement,"
                    +RD_id_ruta_distribucion+" integer,"
                    +RD_id_ruta+" integer, " +
                    RD_nombre_ruta+" text, " +
                    RD_dia_semana+" text, " +
                    RD_numero_establecimientos + " integer," +
                    RD_id_agente + " text, " +
                    Constants._SINCRONIZAR + " integer);";

    public static final String DELETE_TABLE_RUTA_DISTRIBUCION = "DROP TABLE IF EXISTS " + SQLITE_TABLE_RUTA_DISTRIBUCION;

    public DbAdapter_Ruta_Distribucion(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Ruta_Distribucion open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createRutaDistribucion( int id, int idRuta, String nombreRuta, String diaSemana, int numeroEstablecimientos, int idAgente) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(RD_id_ruta_distribucion,id);
        initialValues.put(RD_id_ruta,idRuta);
        initialValues.put(RD_nombre_ruta,nombreRuta);
        initialValues.put(RD_dia_semana, diaSemana);
        initialValues.put(RD_numero_establecimientos, numeroEstablecimientos);
        initialValues.put(RD_id_agente, idAgente);
        initialValues.put(Constants._SINCRONIZAR, Constants._IMPORTADO);


        return mDb.insert(SQLITE_TABLE_RUTA_DISTRIBUCION, null, initialValues);
    }

    public boolean delleteAllRutaByIdAgente(int idAgente) {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_RUTA_DISTRIBUCION, RD_id_agente + " =  ? " , new String[]{""+idAgente} );
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchRutaDistribucionByIdAgente(int idAgente) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_RUTA_DISTRIBUCION, new String[] {RD_id_,
                        RD_id_ruta_distribucion, RD_id_ruta,RD_nombre_ruta , RD_dia_semana, RD_numero_establecimientos},
                RD_id_agente + " =  ? " , new String[]{""+idAgente}, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
