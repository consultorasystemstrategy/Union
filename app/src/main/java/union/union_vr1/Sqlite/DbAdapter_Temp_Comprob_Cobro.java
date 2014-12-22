package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;

public class DbAdapter_Temp_Comprob_Cobro {

    public static final String temp_id_cob_historial = "_id";
    public static final String temp_id_establec = "temp_in_id_establec";
    public static final String temp_id_comprob = "temp_in_id_comprob";
    public static final String temp_id_plan_pago = "temp_in_id_plan_pago";
    public static final String temp_id_plan_pago_detalle = "temp_in_id_plan_pago_detalle";
    public static final String temp_desc_tipo_doc = "temp_te_desc_tipo_doc";
    public static final String temp_doc = "temp_te_doc";
    public static final String temp_fecha_programada = "temp_te_fecha_programada";
    public static final String temp_monto_a_pagar = "temp_re_monto_a_pagar";
    public static final String temp_fecha_cobro = "temp_te_fecha_cobro";
    public static final String temp_hora_cobro = "temp_te_hora_cobro";
    public static final String temp_monto_cobrado = "temp_re_monto_cobrado";
    public static final String temp_estado_cobro = "temp_in_estado_cobro";
    public static final String temp_id_agente = "temp_in_id_agente";
    public static final String temp_id_forma_cobro = "temp_id_forma_cobro";
    public static final String temp_lugar_registro = "temp_lugar_registro";

    public static final String TAG = "Comprob_Cobro";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "ProdUnion.sqlite";
    private static final String SQLITE_TABLE_TEMP_Comprob_Cobro = "m_temp_comprob_cobro";
    private final Context mCtx;

    public static final String CREATE_TABLE_TEMP_COMPROB_COBRO =
            "create table if not exists "+SQLITE_TABLE_TEMP_Comprob_Cobro+" ("
                    +temp_id_cob_historial+" integer primary key autoincrement,"
                    +temp_id_establec+" integer,"
                    +temp_id_comprob+" integer,"
                    +temp_id_plan_pago+" integer,"
                    +temp_id_plan_pago_detalle+" integer,"
                    +temp_desc_tipo_doc+" text,"
                    +temp_doc+" text,"
                    +temp_fecha_programada+" text,"
                    +temp_monto_a_pagar+" real,"
                    +temp_fecha_cobro+" text,"
                    +temp_hora_cobro+" text,"
                    +temp_monto_cobrado+" real,"
                    +temp_estado_cobro+" integer,"
                    +temp_id_agente+" integer,"
                    +temp_id_forma_cobro+" integer,"
                    +temp_lugar_registro+" text);";

    public static final String DELETE_TABLE_TEMP_COMPROB_COBRO = "DROP TABLE IF EXISTS " + SQLITE_TABLE_TEMP_Comprob_Cobro;

    public DbAdapter_Temp_Comprob_Cobro(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Temp_Comprob_Cobro open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createComprobCobros(
            int id_establec, int id_comprob, int id_plan_pago, int id_plan_pago_detalle,
            String desc_tipo_doc, String doc, String fecha_programada, double monto_a_pagar,
            String fecha_cobro, String hora_cobro, double monto_cobrado, int estado_cobro,
            int id_agente,int id_forma_cobro, String lugar_registro) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_id_establec,id_establec);
        initialValues.put(temp_id_comprob,id_comprob);
        initialValues.put(temp_id_plan_pago,id_plan_pago);
        initialValues.put(temp_id_plan_pago_detalle,id_plan_pago_detalle);
        initialValues.put(temp_desc_tipo_doc,desc_tipo_doc);
        initialValues.put(temp_doc,doc);
        initialValues.put(temp_fecha_programada,fecha_programada);
        initialValues.put(temp_monto_a_pagar,monto_a_pagar);
        initialValues.put(temp_fecha_cobro,fecha_cobro);
        initialValues.put(temp_hora_cobro,hora_cobro);
        initialValues.put(temp_monto_cobrado,monto_cobrado);
        initialValues.put(temp_estado_cobro,estado_cobro);
        initialValues.put(temp_id_agente,id_agente);
        initialValues.put(temp_id_forma_cobro,id_forma_cobro);
        initialValues.put(temp_lugar_registro,lugar_registro);
        return mDb.insert(SQLITE_TABLE_TEMP_Comprob_Cobro, null, initialValues);
    }


    public void updateComprobCobros(String id, double valor){
        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_monto_a_pagar,valor);

        String[] columnas = new String[]{temp_monto_a_pagar};
        mDb.update(SQLITE_TABLE_TEMP_Comprob_Cobro, initialValues,
                temp_id_plan_pago_detalle+"=? AND "+temp_id_comprob+"=?",new String[]{id,"0"});
    }

    public void updateComprobCobrosCan(String id, String fecha, String hora, double valor, String estado){
        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_monto_cobrado,valor);
        initialValues.put(temp_fecha_cobro,fecha);
        initialValues.put(temp_hora_cobro,hora);
        initialValues.put(temp_estado_cobro,estado);
        String[] columnas = new String[]{temp_monto_a_pagar};
        mDb.update(SQLITE_TABLE_TEMP_Comprob_Cobro, initialValues,
                temp_id_cob_historial+"=?",new String[]{id});

    }
    public void updateComprobCobrosMan(String id, String fecha, String hora, double valor, String estado){



        mDb.execSQL("update "+SQLITE_TABLE_TEMP_Comprob_Cobro+" set "+temp_monto_a_pagar+"="+valor+", "+temp_fecha_programada+"='"+fecha+"',"+temp_hora_cobro+"='"+hora+"', "+temp_estado_cobro+"='"+estado+"',"+temp_monto_cobrado+"=0 where "+temp_id_cob_historial+"='"+id+"'");

    }
    public int updateComprobCobrosCan2(String id, String fecha, String hora, double valor, String estado){
        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_monto_cobrado,valor);
        initialValues.put(temp_fecha_cobro,fecha);
        initialValues.put(temp_hora_cobro,hora);
        initialValues.put(temp_estado_cobro,estado);
        String[] columnas = new String[]{temp_monto_a_pagar};
        int insert = mDb.update(SQLITE_TABLE_TEMP_Comprob_Cobro, initialValues,
                temp_id_cob_historial+"=?",new String[]{id});
        return insert;
    }

    public boolean deleteAllComprobCobros() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_TEMP_Comprob_Cobro, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchComprobCobrosByIds(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_TEMP_Comprob_Cobro, new String[] {temp_id_cob_historial,
                        temp_id_establec, temp_id_comprob, temp_desc_tipo_doc, temp_doc, temp_fecha_programada,
                        temp_monto_a_pagar, temp_estado_cobro},
                temp_id_establec + " = " + inputText, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchComprobCobrosByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_TEMP_Comprob_Cobro, new String[] {temp_id_cob_historial,
                            temp_id_establec, temp_id_comprob, temp_desc_tipo_doc, temp_doc, temp_fecha_programada,
                            temp_monto_a_pagar, temp_estado_cobro},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_TEMP_Comprob_Cobro, new String[] {temp_id_cob_historial,
                            temp_id_establec, temp_id_comprob, temp_desc_tipo_doc, temp_doc, temp_fecha_programada,
                            temp_monto_a_pagar, temp_estado_cobro},
                    temp_fecha_programada + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllComprobCobros() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_TEMP_Comprob_Cobro, new String[] {temp_id_cob_historial,
                        temp_id_establec, temp_id_comprob, temp_id_plan_pago, temp_id_plan_pago_detalle,
                        temp_desc_tipo_doc, temp_doc, temp_fecha_programada, temp_monto_a_pagar,
                        temp_fecha_cobro, temp_monto_cobrado, temp_estado_cobro},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllComprobCobrosByEst(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_TEMP_Comprob_Cobro, new String[] {temp_id_cob_historial,
                        temp_id_establec, temp_id_comprob, temp_id_plan_pago, temp_id_plan_pago_detalle,
                        temp_desc_tipo_doc, temp_doc, temp_fecha_programada, temp_monto_a_pagar,
                        temp_fecha_cobro, temp_monto_cobrado, temp_estado_cobro},
                temp_id_establec + " = " + inputText +" and temp_in_estado_cobro ='0'  order by temp_te_fecha_programada asc", null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    public Cursor listaComprobantes(int establex) {

        Cursor mCursor = mDb.rawQuery("SELECT temp_te_fecha_programada  FROM   m_comprob_cobro where temp_in_id_establec="+establex+" and  temp_in_estado_cobro ='0'  order by temp_te_fecha_programada asc",null);
        return mCursor;
    }
    public Cursor listarComprobantesToCobros(){
        Cursor mCursor = mDb.rawQuery("SELECT mc._id, mc.temp_te_doc,me.ee_te_nom_cliente,mc.temp_te_fecha_programada, mc.temp_re_monto_a_pagar,me.ee_te_nom_establec ,mc.temp_in_id_establec from m_comprob_cobro mc,m_evento_establec me where mc.temp_in_id_establec=me.ee_in_id_establec and mc.temp_in_estado_cobro='0' order by mc.temp_te_fecha_programada asc",null);
        return mCursor;
    }
    public Cursor listarComprobantesToCobrosMante(String idEstablec){
        Cursor mCursor = mDb.rawQuery("SELECT mc._id, mc.temp_te_doc,me.ee_te_nom_cliente,me.ee_te_nom_establec,mc.temp_te_fecha_cobro,mc.temp_te_hora_cobro, mc.temp_re_monto_cobrado, case when mc.temp_in_estado_cobro=0 then 'Anulado' else 'Cobrado' end as estado ,mc.temp_in_id_establec,mc.temp_te_fecha_programada from m_comprob_cobro mc,m_evento_establec me where mc.temp_in_id_establec=me.ee_in_id_establec and mc.temp_te_hora_cobro !=\"\" and me.ee_in_id_establec='"+idEstablec+"' order by mc.temp_te_fecha_programada asc",null);
        return mCursor;
    }


    public void insertSomeComprobCobros() {

        createComprobCobros(1, 1, 1, 1, "FACTURA", "FAC-0001", "2014-12-12", 1000, "",
                "", 0, 0, 1,1,"historial");
        createComprobCobros(1, 1, 1, 2, "FACTURA", "FAC-0001", "2014-12-19", 1000, "",
                "", 0, 0, 1,1,"historial");
        createComprobCobros(1, 1, 1, 2, "FACTURA", "FAC-0001", "2014-12-19", 500, "",
                "", 0, 0, 1,1,"historial");
        createComprobCobros(1, 1, 1, 2, "FACTURA", "FAC-0001", "2014-12-01", 200, "",
                "", 0, 0, 1,1,"historial");
        createComprobCobros(2, 2, 2, 2, "FACTURA", "FAC-0002", "2014-12-11", 200, "",
                "", 0, 0, 1,1,"historial");
    }

}