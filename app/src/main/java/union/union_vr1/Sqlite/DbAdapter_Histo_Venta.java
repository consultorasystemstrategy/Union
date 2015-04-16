package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;

public class DbAdapter_Histo_Venta {

    public static final String HV_id = "_id";
    public static final String HV_id_histo = "hv_in_id_histo";
    public static final String HV_id_agente = "hv_in_agente";
    public static final String HV_subtotal= "hv_in_subtotal";
    public static final String HV_fecha= "hv_in_fecha";
    public static final String estado_sincronizacion = "estado_sincronizacion";
    public static final String HV_id_establecimiento= "hv_id_establecimiento";

    public static final String TAG = "Histo_Venta";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "ProdUnion.sqlite";
    private static final String SQLITE_TABLE_Histo_Venta = "m_histo_venta";
    private final Context mCtx;

    public static final String CREATE_TABLE_HISTO_VENTA =
            "create table "+SQLITE_TABLE_Histo_Venta+" ("
                    +HV_id+" integer primary key autoincrement,"
                    +HV_id_histo+" text,"
                    +HV_id_agente+" integer,"
                    +HV_subtotal+" real,"
                    +HV_fecha+" text ,"
                    +estado_sincronizacion+" integer,"
                    +HV_id_establecimiento+" integer );";

    public static final String DELETE_TABLE_HISTO_VENTA = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Histo_Venta;

    public DbAdapter_Histo_Venta(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Histo_Venta open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createHistoVenta(
            String id_histo, int id_agente, double subtotal,String fecha,int estado,String establecimiento){

        ContentValues initialValues = new ContentValues();
        initialValues.put(HV_id_histo,id_histo);
        initialValues.put(HV_id_agente,id_agente);
        initialValues.put(HV_subtotal,subtotal);
        initialValues.put(HV_fecha,fecha);
        initialValues.put(HV_id_establecimiento,establecimiento);
        initialValues.put(estado_sincronizacion,estado);


        return mDb.insert(SQLITE_TABLE_Histo_Venta, null, initialValues);
    }

    public int updateHistoVenta(String _id, String idGuia){
        ContentValues initialValues = new ContentValues();
        initialValues.put(HV_id_histo,idGuia);

        return mDb.update(SQLITE_TABLE_Histo_Venta, initialValues,
                HV_id_histo+" LIKE '%"+_id+"%'",null);
    }
    public Cursor filterExport() {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Histo_Venta, new String[] {HV_id,
                        HV_id_histo, HV_id_agente, HV_subtotal,HV_fecha, estado_sincronizacion},
                Constants._SINCRONIZAR + " = " + Constants._CREADO + " OR " + Constants._SINCRONIZAR + " = " + Constants._ACTUALIZADO, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void changeEstadoToExport(String[] idsInformeGasto, int estadoSincronizacion){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR,estadoSincronizacion);

        String signosInterrogacion = "";
        for (int i=0; i<idsInformeGasto.length; i++){
            if (i==idsInformeGasto.length-1)
            {
                signosInterrogacion+= "?";
            }else {
                signosInterrogacion+= "? OR ";
            }

        }

        Log.d("SIGNOS INTERROGACIÓN", signosInterrogacion);
        int cantidadRegistros = mDb.update(SQLITE_TABLE_Histo_Venta, initialValues,
                HV_id+"= "+ signosInterrogacion,idsInformeGasto);


        Log.d("REGISTROS ACTUALIZADO ", ""+cantidadRegistros);
    }

    public Cursor filterExportUpdated() {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Histo_Venta, new String[] {HV_id,
                        HV_id_histo, HV_id_agente, HV_subtotal,HV_fecha, estado_sincronizacion},
                Constants._SINCRONIZAR + " = " + Constants._ACTUALIZADO, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}