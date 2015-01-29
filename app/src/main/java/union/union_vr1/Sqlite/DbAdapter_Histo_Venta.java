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
                    +estado_sincronizacion+" integer);";

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
            String id_histo, int id_agente, double subtotal,String fecha,int estado){

        ContentValues initialValues = new ContentValues();
        initialValues.put(HV_id_histo,id_histo);
        initialValues.put(HV_id_agente,id_agente);
        initialValues.put(HV_subtotal,subtotal);
        initialValues.put(HV_fecha,fecha);
        initialValues.put(estado_sincronizacion,estado);


        return mDb.insert(SQLITE_TABLE_Histo_Venta, null, initialValues);
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
/*
    public boolean deleteAllHistoVenta() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Histo_Venta, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }*/
/*
    public Cursor fetchHistoVentaByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Histo_Venta, new String[] {HV_id_histoventa,
                            HV_id_comprob, HV_id_establec, HV_orden},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Histo_Venta, new String[] {HV_id_histoventa,
                            HV_id_comprob, HV_id_establec, HV_orden, HV_serie, HV_num_doc,
                            HV_total, HV_fecha_doc, HV_hora_doc, HV_estado_comp},
                    HV_num_doc + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }*/
/*
    public Cursor fetchAllHistoVenta() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Histo_Venta, new String[] {HV_id_histoventa,
                        HV_id_comprob, HV_id_establec, HV_orden, HV_serie, HV_num_doc,
                        HV_total, HV_fecha_doc, HV_hora_doc, HV_estado_comp},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllHistoVentaByEstable(String id) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Histo_Venta, new String[] {HV_id_histoventa,
                        HV_id_comprob, HV_id_establec, HV_orden, HV_serie, HV_num_doc,
                        HV_total, HV_fecha_doc, HV_hora_doc, HV_estado_comp},
                HV_id_establec + " = " + id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
*//*
    public void insertSomeHistoVenta() {

        createHistoVenta( 1, 1, 1, "1A", 1, 10, "2014-11-12", "08:10:00", 0, 0, 1);
        createHistoVenta( 2, 1, 2, "2A", 2, 20, "2014-11-12", "08:10:00", 0, 0, 1);
        createHistoVenta( 3, 2, 1, "3A", 3, 30, "2014-11-12", "08:10:00", 0, 0, 1);
        createHistoVenta( 4, 2, 2, "4A", 4, 10, "2014-11-12", "08:10:00", 0, 0, 1);
        createHistoVenta( 5, 3, 1, "5A", 5, 10, "2014-11-12", "08:10:00", 0, 0, 1);

    }*/

}