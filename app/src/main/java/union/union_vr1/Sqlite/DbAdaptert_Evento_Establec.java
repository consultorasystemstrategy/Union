package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import union.union_vr1.Conexion.DbHelper;

public class DbAdaptert_Evento_Establec {

    public static final String EE_id_evt_establec = "_id";
    public static final String EE_id_establec = "ee_in_id_establec";
    public static final String EE_id_cat_est = "ee_in_id_cat_est";
    public static final String EE_id_tipo_doc_cliente = "ee_in_id_tipo_doc_cliente";
    public static final String EE_id_estado_atencion = "ee_in_id_estado_atencion";
    public static final String EE_nom_establec = "ee_te_nom_establec";
    public static final String EE_nom_cliente = "ee_te_nom_cliente";
    public static final String EE_doc_cliente = "ee_te_doc_cliente";
    public static final String EE_orden = "ee_in_orden";
    public static final String EE_surtido_stock_ant = "ee_in_surtido_stock_ant";
    public static final String EE_surtido_venta_ant = "ee_in_surtido_venta_ant";
    public static final String EE_monto_credito = "ee_re_monto_credito";
    public static final String EE_dias_credito = "ee_in_dias_credito";
    public static final String EE_estado_no_atencion = "ee_in_estado_no_atencion";
    public static final String EE_id_agente = "ee_in_id_agente";

    public static final String TAG = "Evento_Establec";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "ProdUnion.sqlite";
    private static final String SQLITE_TABLE_Evento_Establec = "m_evento_establec";
    private final Context mCtx;

    public static final String CREATE_TABLE_EVENTO_ESTABLEC =
            "create table if not exists "+SQLITE_TABLE_Evento_Establec+" ("
                    +EE_id_evt_establec+" integer primary key autoincrement,"
                    +EE_id_establec+" integer,"
                    +EE_id_cat_est+" integer,"
                    +EE_id_tipo_doc_cliente+" integer,"
                    +EE_id_estado_atencion+" integer,"
                    +EE_nom_establec+" text,"
                    +EE_nom_cliente+" text,"
                    +EE_doc_cliente+" text,"
                    +EE_orden+" integer,"
                    +EE_surtido_stock_ant+" integer,"
                    +EE_surtido_venta_ant+" integer,"
                    +EE_monto_credito+" real,"
                    +EE_dias_credito+" integer,"
                    +EE_estado_no_atencion+" integer,"
                    +EE_id_agente+" integer);";

    public static final String DELETE_TABLE_EVENTO_ESTABLEC = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Evento_Establec;

    public DbAdaptert_Evento_Establec(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdaptert_Evento_Establec open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createEstablecs(
            int id_establec, int id_cat_est, int id_tipo_doc_cliente, int id_estado_atencion,
            String nom_establec, String nom_cliente, String doc_cliente, int orden,
            int surtido_stock_ant, int surtido_venta_ant, double monto_credito, int dias_credito,
            int estado_no_atencion, int id_agente) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_establec,id_establec);
        initialValues.put(EE_id_cat_est,id_cat_est);
        initialValues.put(EE_id_tipo_doc_cliente,id_tipo_doc_cliente);
        initialValues.put(EE_id_estado_atencion,id_estado_atencion);
        initialValues.put(EE_nom_establec,nom_establec);
        initialValues.put(EE_nom_cliente,nom_cliente);
        initialValues.put(EE_doc_cliente,doc_cliente);
        initialValues.put(EE_orden,orden);
        initialValues.put(EE_surtido_stock_ant,surtido_stock_ant);
        initialValues.put(EE_surtido_venta_ant,surtido_venta_ant);
        initialValues.put(EE_monto_credito,monto_credito);
        initialValues.put(EE_dias_credito,dias_credito);
        initialValues.put(EE_estado_no_atencion,estado_no_atencion);
        initialValues.put(EE_id_agente,id_agente);

        return mDb.insert(SQLITE_TABLE_Evento_Establec, null, initialValues);
    }

    public void updateEstablecs(String id, int valor){
        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_estado_atencion,valor);

        mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_establec+"=?",new String[]{id});
    }

    public void updateEstablecs1(String id, int aten, int naten){
        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_estado_atencion,aten);
        initialValues.put(EE_estado_no_atencion,naten);
        mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_establec+"=?",new String[]{id});
    }

    public boolean deleteAllEstablecs() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Evento_Establec, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchEstablecsByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                            EE_id_establec, EE_id_cat_est, EE_id_tipo_doc_cliente, EE_id_estado_atencion,
                            EE_nom_establec, EE_nom_cliente, EE_doc_cliente, EE_orden, EE_surtido_stock_ant,
                            EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_estado_no_atencion,
                            EE_id_agente},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                            EE_id_establec, EE_id_cat_est, EE_id_tipo_doc_cliente, EE_id_estado_atencion,
                            EE_nom_establec, EE_nom_cliente, EE_doc_cliente, EE_orden, EE_surtido_stock_ant,
                            EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_estado_no_atencion,
                            EE_id_agente},
                    EE_nom_establec + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchEstablecsById(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                        EE_id_establec, EE_id_cat_est, EE_nom_establec, EE_nom_cliente, EE_orden,
                        EE_id_estado_atencion,EE_monto_credito,EE_dias_credito},
                EE_id_establec + " = " + inputText, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchEstablecsByIdX(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                        EE_id_establec, EE_id_cat_est, EE_id_tipo_doc_cliente, EE_id_estado_atencion,
                        EE_nom_establec, EE_nom_cliente, EE_doc_cliente, EE_orden, EE_surtido_stock_ant,
                        EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_id_agente},
                EE_id_establec + " = " + inputText, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllEstablecs() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                        EE_id_establec, EE_nom_establec, EE_nom_cliente, EE_doc_cliente},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllEstablecsX() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                        EE_id_establec, EE_id_cat_est, EE_id_tipo_doc_cliente, EE_id_estado_atencion,
                        EE_nom_establec, EE_nom_cliente, EE_doc_cliente, EE_orden, EE_surtido_stock_ant,
                        EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_id_agente},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public Cursor fetchAllEstablecsXX() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                        EE_id_establec, EE_id_cat_est, EE_id_tipo_doc_cliente, EE_id_estado_atencion,
                        EE_nom_establec, EE_nom_cliente, EE_doc_cliente, EE_orden, EE_surtido_stock_ant,
                        EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_estado_no_atencion,
                        EE_id_agente},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeEstablecs() {

        createEstablecs(1, 1, 3, 1, "TIENDA NAs", "JUAN", "10001", 1, 50, 4, 50.5, 31, 1, 1);
        createEstablecs(2, 1, 4, 2, "TIENDA NBs", "JUAN", "10001", 1, 11, 21, 60.5, 31, 2, 1);
        createEstablecs(3, 2, 5, 3, "GRIFO NAs",  "JUAN", "10001", 1, 11, 21, 70.5, 31, 3, 1);
        createEstablecs(4, 2, 6, 1, "GRIFO NBs",  "JUAN", "10001", 1, 11, 21, 80.5, 31, 4, 1);
        createEstablecs(5, 3, 7, 2, "PERSON NAs", "JUAN", "10001", 1, 11, 21, 90.5, 31, 1, 1);

    }

}