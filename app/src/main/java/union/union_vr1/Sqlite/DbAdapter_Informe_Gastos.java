package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;

public class DbAdapter_Informe_Gastos {

    public static final String GA_id_gasto = "_id";
    public static final String GA_id_tipo_gasto = "ga_in_id_tipo_gasto";
    public static final String GA_id_proced_gasto = "ga_in_id_proced_gasto";
    public static final String GA_id_tipo_doc = "ga_in_id_tipo_doc";
    public static final String GA_nom_tipo_gasto = "ga_te_nom_tipo_gasto";
    public static final String GA_subtotal = "ga_re_subtotal";
    public static final String GA_igv = "ga_re_igv";
    public static final String GA_total = "ga_re_total";
    public static final String GA_fecha = "ga_te_fecha";
    public static final String GA_hora = "ga_te_hora";
    public static final String GA_estado = "ga_int_estado";
    public static final String GA_referencia = "ga_referencia";
    public static final String GA_id_agente = "ga_in_id_agente";
    public static final String TAG = "Informe_Gastos";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_Informe_Gastos = "m_informe_gastos";
    private final Context mCtx;

    public static final String CREATE_TABLE_INFORME_GASTOS =
            "create table "+SQLITE_TABLE_Informe_Gastos+" ("
                    +GA_id_gasto+" integer primary key autoincrement,"
                    +GA_id_tipo_gasto+" integer,"
                    +GA_id_proced_gasto+" integer,"
                    +GA_id_tipo_doc+" integer,"
                    +GA_nom_tipo_gasto+" text,"
                    +GA_subtotal+" real,"
                    +GA_igv+" real,"
                    +GA_total+" real,"
                    +GA_fecha+" text,"
                    +GA_hora+" text,"
                    +GA_estado+" integer,"
                    +GA_referencia+" text,"
                    +GA_id_agente+" integer,"
                    +Constants._SINCRONIZAR+" text);";

    public static final String DELETE_TABLE_INFORME_GASTOS = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Informe_Gastos;

    public DbAdapter_Informe_Gastos(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Informe_Gastos open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createInformeGastos(
            int id_tipo_gasto, int id_procedencia_gasto, int id_tipo_doc, String nom_tipo_gasto,
            double subtotal, double igv, double total, String fecha, String hora, int estado, String referencia,
            int id_agente, int estadoSincronizado) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(GA_id_tipo_gasto,id_tipo_gasto);
        initialValues.put(GA_id_proced_gasto,id_procedencia_gasto);
        initialValues.put(GA_id_tipo_doc,id_tipo_doc);
        initialValues.put(GA_nom_tipo_gasto,nom_tipo_gasto);
        initialValues.put(GA_subtotal,subtotal);
        initialValues.put(GA_igv,igv);
        initialValues.put(GA_total,total);
        initialValues.put(GA_fecha,fecha);
        initialValues.put(GA_hora,hora);
        initialValues.put(GA_estado,estado);
        initialValues.put(GA_referencia, referencia);
        initialValues.put(GA_id_agente,id_agente);
        initialValues.put(Constants._SINCRONIZAR, estadoSincronizado);
        return mDb.insert(SQLITE_TABLE_Informe_Gastos, null, initialValues);
    }

    public boolean deleteAllInformeGastos() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Informe_Gastos, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }
    public boolean deleteGastoById(int id_gasto) {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Informe_Gastos, GA_id_gasto +" = ?" , new String[]{""+id_gasto});
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchGastosById(int id_gasto){

        Cursor cursor = mDb.query(
                SQLITE_TABLE_Informe_Gastos,
                new String[] {GA_id_gasto,
                        GA_nom_tipo_gasto, GA_subtotal, GA_igv, GA_total,GA_id_tipo_doc},
                GA_id_gasto + "=?",
                new String[]{""+id_gasto},
                null,
                null,
                null
        );
        if (cursor.getCount()>0){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updateGastosById(int id, double total,double sub_total, double igv){
        ContentValues contentValues = new ContentValues();
        contentValues.put(GA_total, total);
        contentValues.put(GA_subtotal, sub_total);
        contentValues.put(GA_igv, igv);

        return  mDb.update(SQLITE_TABLE_Informe_Gastos,
                contentValues,
                GA_id_gasto + "=?",
                new String[]{""+id}
                );
    }


    public Cursor fetchInformeGastosByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Informe_Gastos, new String[] {GA_id_gasto,
                            GA_nom_tipo_gasto, GA_subtotal, GA_igv, GA_total},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Informe_Gastos, new String[] {GA_id_gasto,
                            GA_nom_tipo_gasto, GA_subtotal, GA_igv, GA_total},
                    GA_nom_tipo_gasto + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor filterExport() {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Informe_Gastos, new String[] {GA_id_gasto,
                        GA_id_tipo_gasto, GA_id_proced_gasto, GA_id_tipo_doc,
                        GA_nom_tipo_gasto, GA_subtotal, GA_igv, GA_total, GA_fecha,
                        GA_hora, GA_referencia, GA_id_agente
                },
                Constants._SINCRONIZAR + " = " + Constants._CREADO + " OR " + Constants._SINCRONIZAR + " = " + Constants._ACTUALIZADO, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllInformeGastos(String fecha) {

        /*Cursor mCursor = mDb.query(SQLITE_TABLE_Informe_Gastos, new String[] {GA_id_gasto,GA_id_tipo_gasto,
                        GA_nom_tipo_gasto, GA_subtotal, GA_igv, GA_total, GA_fecha, GA_referencia},
                null, null, null, null, GA_id_gasto+" DESC");
        */
        Cursor mCursor = mDb.rawQuery("SELECT m_informe_gastos._id, m_tipo_gasto.tg_te_nom_tipo_gasto,m_informe_gastos.ga_re_total, m_informe_gastos.ga_re_subtotal, m_informe_gastos.ga_re_igv,m_informe_gastos.ga_referencia FROM m_informe_gastos, m_tipo_gasto " +
                "WHERE m_informe_gastos.ga_in_id_tipo_gasto = m_tipo_gasto.tg_in_id_tgasto " +
                "AND ga_te_fecha LIKE '%"+fecha+"%' " +
                " ORDER BY m_informe_gastos._id DESC ",null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor resumenInformeGastos(String fecha) {


        Cursor mCursor = mDb.rawQuery("SELECT ig._id, tg_te_nom_tipo_gasto, \n" +
                "\tROUND(SUM(CASE WHEN ga_in_id_proced_gasto = '2'  THEN ga_re_total END),1) AS RUTA,\n" +
                "\tROUND(SUM(CASE WHEN ga_in_id_proced_gasto = '1' THEN  ga_re_total END),1) AS PLANTA \n" +
                "FROM m_informe_gastos ig \n" +
                "INNER JOIN m_tipo_gasto tg \n" +
                "ON ga_in_id_tipo_gasto = tg_in_id_tgasto \n" +
                "AND ga_te_fecha LIKE '%"+fecha+"%' \n" +
                "group by tg_te_nom_tipo_gasto ",null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor resumenTipoIngresos(int liquidacion) {


        Cursor mCursor = mDb.rawQuery("SELECT ROUND(SUM(cv_re_total),1) AS total, FP.*, CV.* \n" +
                "FROM m_comprob_venta CV \n" +
                "INNER JOIN m_forma_pago FP \n" +
                "ON CV.cv_in_id_tipo_pago = FP._id_forma_pago \n" +
                "WHERE CV.id_liquidacion = '"+liquidacion+"' AND CV.cv_in_estado_comp != 0 \n" +
                "group by FP._id_forma_pago ",null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Double  getTotalVentaByTipoIngreso(int liquidacion, int idTipoIngreso) {


        Double total = 0.0;
        Cursor mCursor = mDb.rawQuery("SELECT ROUND(SUM(cv_re_total),1) AS total, * \n" +
                "FROM m_comprob_venta  \n" +
                "WHERE id_liquidacion = '"+liquidacion+"' AND cv_in_estado_comp != 0 AND cv_in_id_tipo_pago = '"+idTipoIngreso+"'\n" +
                "group by  cv_in_id_tipo_pago; ",null);

        if (mCursor.getCount()>0){
            mCursor.moveToFirst();
             total = mCursor.getDouble(mCursor.getColumnIndexOrThrow("total"));
        }
        return total;
    }

    public Double  getTotalCobroByTipoIngreso(int liquidacion, int idTipoIngreso) {


        Double total = 0.0;
        Cursor mCursor = mDb.rawQuery("SELECT ROUND(SUM("+DbAdapter_Impresion_Cobros.Imprimir_monto+"),1) AS total, * \n" +
                "FROM "+DbAdapter_Impresion_Cobros.SQLITE_TABLE_IMPRIMIR_COBRO+"  \n" +
                "WHERE "+DbAdapter_Impresion_Cobros.Imprimir_liquidacion+" = '"+liquidacion+"' AND  "+DbAdapter_Impresion_Cobros.Imprimir_id_tipo_pago+" = '"+idTipoIngreso+"'\n" +
                "group by "+DbAdapter_Impresion_Cobros.Imprimir_id_tipo_pago+"; ",null);

        if (mCursor.getCount()>0){
            mCursor.moveToFirst();
            total = mCursor.getDouble(mCursor.getColumnIndexOrThrow("total"));
        }
        return total;
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
        int cantidadRegistros = mDb.update(SQLITE_TABLE_Informe_Gastos, initialValues,
                GA_id_gasto+"= "+ signosInterrogacion,idsInformeGasto);


        Log.d("REGISTROS ACTUALIZADO ", ""+cantidadRegistros);
    }
}