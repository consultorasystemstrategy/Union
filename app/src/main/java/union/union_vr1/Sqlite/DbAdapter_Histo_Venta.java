package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;

public class DbAdapter_Histo_Venta {

    public static final String HV_id_histoventa = "_id";
    public static final String HV_id_comprob = "hv_in_id_comprob";
    public static final String HV_id_establec = "hv_in_id_establec";
    public static final String HV_orden = "hv_in_orden";
    public static final String HV_serie = "hv_te_serie";
    public static final String HV_num_doc = "hv_in_num_doc";
    public static final String HV_total = "hv_re_total";
    public static final String HV_fecha_doc = "hv_te_fecha_doc";
    public static final String HV_hora_doc = "hv_te_hora_doc";
    public static final String HV_estado_comp = "hv_in_estado_comp";
    public static final String HV_estado_conexion = "hv_in_estado_conexion";
    public static final String HV_id_agente = "hv_in_id_agente";
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
                    +HV_id_histoventa+" integer primary key autoincrement,"
                    +HV_id_comprob+" integer,"
                    +HV_id_establec+" integer,"
                    +HV_orden+" integer,"
                    +HV_serie+" text,"
                    +HV_num_doc+" integer,"
                    +HV_total+" real,"
                    +HV_fecha_doc+" text,"
                    +HV_hora_doc+" text,"
                    +HV_estado_comp+" integer,"
                    +HV_estado_conexion+" integer,"
                    +HV_id_agente+" integer, " +
                    estado_sincronizacion+" integer);";

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
            int id_comprob, int id_establec, int orden, String serie, int num_doc, double total,
            String fecha_doc, String hora_doc, int estado_comp, int estado_conexion, int id_agente){

        ContentValues initialValues = new ContentValues();
        initialValues.put(HV_id_comprob,id_comprob);
        initialValues.put(HV_id_establec,id_establec);
        initialValues.put(HV_orden,orden);
        initialValues.put(HV_serie,serie);
        initialValues.put(HV_num_doc,num_doc);
        initialValues.put(HV_total,total);
        initialValues.put(HV_fecha_doc,fecha_doc);
        initialValues.put(HV_hora_doc,hora_doc);
        initialValues.put(HV_estado_comp,estado_comp);
        initialValues.put(HV_estado_conexion,estado_conexion);
        initialValues.put(HV_id_agente,id_agente);

        return mDb.insert(SQLITE_TABLE_Histo_Venta, null, initialValues);
    }

    public boolean deleteAllHistoVenta() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Histo_Venta, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchHistoVentaByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Histo_Venta, new String[] {HV_id_histoventa,
                            HV_id_comprob, HV_id_establec, HV_orden, HV_serie, HV_num_doc,
                            HV_total, HV_fecha_doc, HV_hora_doc, HV_estado_comp},
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

    }

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

    public void insertSomeHistoVenta() {

        createHistoVenta( 1, 1, 1, "1A", 1, 10, "2014-11-12", "08:10:00", 0, 0, 1);
        createHistoVenta( 2, 1, 2, "2A", 2, 20, "2014-11-12", "08:10:00", 0, 0, 1);
        createHistoVenta( 3, 2, 1, "3A", 3, 30, "2014-11-12", "08:10:00", 0, 0, 1);
        createHistoVenta( 4, 2, 2, "4A", 4, 10, "2014-11-12", "08:10:00", 0, 0, 1);
        createHistoVenta( 5, 3, 1, "5A", 5, 10, "2014-11-12", "08:10:00", 0, 0, 1);

    }

}