package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.ComprobanteCobro;

public class DbAdapter_Comprob_Cobro {

    //LLENAMOS  TODOS ESTOS CAMPOS AL REALIZAR UNA VENTA AL CRÉDITO

    //ESTOS CAMPOS OBTENEMOS DEL PROCEDIMIENTO
    public static final String CC_id_cob_historial = "_id";
    public static final String CC_id_comprobante_cobro  = "cc_in_id_comprobante_cobro";
    public static final String CC_id_establec = "cc_in_id_establec";
    public static final String CC_id_comprob = "cc_in_id_comprob";
    public static final String CC_id_plan_pago = "cc_in_id_plan_pago";
    public static final String CC_id_plan_pago_detalle = "cc_in_id_plan_pago_detalle";
    public static final String CC_desc_tipo_doc = "cc_te_desc_tipo_doc";
    public static final String CC_doc = "cc_te_doc";
    public static final String CC_fecha_programada = "cc_te_fecha_programada";
    public static final String CC_monto_a_pagar = "cc_re_monto_a_pagar";
    public static final String estado_sincronizacion = "estado_sincronizacion";


    //ESTOS CAMPOS ACTUALIZAMOS AL REALIZAR UN COBRO
    public static final String CC_estado_cobro = "cc_in_estado_cobro";
    public static final String CC_fecha_cobro = "cc_te_fecha_cobro";
    public static final String CC_hora_cobro = "cc_te_hora_cobro";
    public static final String CC_monto_cobrado = "cc_re_monto_cobrado";
    public static final String CC_id_agente = "cc_in_id_agente";
    public static final String CC_id_forma_cobro = "cc_id_forma_cobro";
    public static final String CC_lugar_registro = "cc_lugar_registro";
    public static final String CC_id_autorizacion = "cc_idAutorizacion";
    public static final String CC_estado_prologa = "cc_estado_prologa";

    public static final String TAG = "Comprob_Cobro";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "ProdUnion.sqlite";
    public  static final String SQLITE_TABLE_Comprob_Cobro = "m_comprob_cobro";
    private final Context mCtx;

    public static final String CREATE_TABLE_COMPROB_COBRO =
            "create table if not exists "+SQLITE_TABLE_Comprob_Cobro+" ("
                    +CC_id_cob_historial+" integer primary key autoincrement,"
                    +CC_id_comprobante_cobro+" text,"
                    +CC_id_establec+" integer,"
                    +CC_id_comprob+" integer,"
                    +CC_id_plan_pago+" integer,"
                    +CC_id_plan_pago_detalle+" integer,"
                    +CC_desc_tipo_doc+" text,"
                    +CC_doc+" text,"
                    +CC_fecha_programada+" text,"
                    +CC_monto_a_pagar+" real,"
                    +CC_fecha_cobro+" text,"
                    +CC_hora_cobro+" text,"
                    +CC_monto_cobrado+" real,"
                    +CC_estado_cobro+" integer,"
                    +CC_id_agente+" integer,"
                    +CC_id_forma_cobro+" integer,"
                    +CC_lugar_registro+" text, "
                    +estado_sincronizacion+" integer,"
                    +CC_id_autorizacion+" integer,"
                    +CC_estado_prologa+" text );";

    public static final String DELETE_TABLE_COMPROB_COBRO = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Comprob_Cobro;

    public DbAdapter_Comprob_Cobro(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Comprob_Cobro open() throws SQLException {
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
        initialValues.put(CC_id_establec,id_establec);
        initialValues.put(CC_id_comprob,id_comprob);
        initialValues.put(CC_id_plan_pago,id_plan_pago);
        initialValues.put(CC_id_plan_pago_detalle,id_plan_pago_detalle);
        initialValues.put(CC_desc_tipo_doc,desc_tipo_doc);
        initialValues.put(CC_doc,doc);
        initialValues.put(CC_fecha_programada,fecha_programada);
        initialValues.put(CC_monto_a_pagar,monto_a_pagar);
        initialValues.put(CC_fecha_cobro,fecha_cobro);
        initialValues.put(CC_hora_cobro,hora_cobro);
        initialValues.put(CC_monto_cobrado,monto_cobrado);
        initialValues.put(CC_estado_cobro,estado_cobro);
        initialValues.put(CC_id_agente,id_agente);
        initialValues.put(CC_id_forma_cobro,id_forma_cobro);
        initialValues.put(CC_lugar_registro,lugar_registro);
        initialValues.put(Constants._SINCRONIZAR, Constants._CREADO);
        return mDb.insert(SQLITE_TABLE_Comprob_Cobro, null, initialValues);
    }
    public boolean updateCobro(String idCobro,double valorP,double valorR){
        boolean estado = false;
        try {
            mDb.execSQL("update "+SQLITE_TABLE_Comprob_Cobro+" set "+CC_monto_cobrado+"='"+valorP+"', "+CC_monto_a_pagar+"='"+valorR+"',"+Constants._SINCRONIZAR+"="+Constants._ACTUALIZADO+" where "+CC_id_cob_historial+"='"+idCobro+"'");
            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }

    public long createComprobCobro(ComprobanteCobro comprobanteCobro) {

        ContentValues initialValues = new ContentValues();

        initialValues.put(CC_id_comprobante_cobro,comprobanteCobro.getIdComprobanteCobro());
        initialValues.put(CC_id_establec,comprobanteCobro.getIdEstablecimiento());
        initialValues.put(CC_id_comprob,comprobanteCobro.getIdComprobante());
        initialValues.put(CC_id_plan_pago,comprobanteCobro.getIdPlanPago());
        initialValues.put(CC_id_plan_pago_detalle,comprobanteCobro.getIdPlanPagoDetalle());
        initialValues.put(CC_desc_tipo_doc,comprobanteCobro.getDescTipoDoc());
        initialValues.put(CC_doc, comprobanteCobro.getDoc());
        initialValues.put(CC_fecha_programada,comprobanteCobro.getFechaProgramada());
        initialValues.put(CC_monto_a_pagar,comprobanteCobro.getMontoPagar());
        initialValues.put(CC_fecha_cobro, comprobanteCobro.getFechaCobro());
        initialValues.put(CC_hora_cobro,comprobanteCobro.getHoraCobro());
        initialValues.put(CC_monto_cobrado, comprobanteCobro.getMontoCobrado());
        initialValues.put(CC_estado_cobro,comprobanteCobro.getEstadoCobro());
        initialValues.put(CC_id_agente,comprobanteCobro.getIdAgente());
        initialValues.put(CC_id_forma_cobro, comprobanteCobro.getIdFormaCobro());
        initialValues.put(CC_lugar_registro, comprobanteCobro.getLugarRegistro());
        return mDb.insert(SQLITE_TABLE_Comprob_Cobro, null, initialValues);
    }
    public void updateEstadoEnviado(int idComprobanteCobro, int estado,String idAutorizacion){
        mDb.execSQL("update "+SQLITE_TABLE_Comprob_Cobro+" set "+CC_estado_prologa+"='"+estado+"',"+CC_id_autorizacion+"='"+idAutorizacion+"' where "+CC_id_comprobante_cobro+"='"+idComprobanteCobro+"';");
    }

    public void updateComprobCobros(ComprobanteCobro comprobanteCobro){

        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_id_comprobante_cobro,comprobanteCobro.getIdComprobanteCobro());
        initialValues.put(CC_id_establec,comprobanteCobro.getIdEstablecimiento());
        initialValues.put(CC_id_comprob,comprobanteCobro.getIdComprobante());
        initialValues.put(CC_id_plan_pago,comprobanteCobro.getIdPlanPago());
        initialValues.put(CC_id_plan_pago_detalle,comprobanteCobro.getIdPlanPagoDetalle());
        initialValues.put(CC_desc_tipo_doc,comprobanteCobro.getDescTipoDoc());
        initialValues.put(CC_doc, comprobanteCobro.getDoc());
        initialValues.put(CC_fecha_programada,comprobanteCobro.getFechaProgramada());
        initialValues.put(CC_monto_a_pagar,comprobanteCobro.getMontoPagar());
        initialValues.put(CC_fecha_cobro, comprobanteCobro.getFechaCobro());
        initialValues.put(CC_hora_cobro,comprobanteCobro.getHoraCobro());
        initialValues.put(CC_monto_cobrado, comprobanteCobro.getMontoCobrado());
        initialValues.put(CC_estado_cobro,comprobanteCobro.getEstadoCobro());
        initialValues.put(CC_id_agente,comprobanteCobro.getIdAgente());
        initialValues.put(CC_id_forma_cobro, comprobanteCobro.getIdFormaCobro());
        initialValues.put(CC_lugar_registro, comprobanteCobro.getLugarRegistro());


        mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_comprobante_cobro + " = ?",
                new String[]{
                        ""+comprobanteCobro.getIdComprobanteCobro()
                });
    }


    public boolean  existeComprobCobro(String comprobanteCobro) throws SQLException {
        boolean exists = false;
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Cobro, new String[] {CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_desc_tipo_doc, CC_doc, CC_fecha_programada,
                        CC_monto_a_pagar, CC_estado_cobro},
                CC_id_comprobante_cobro + " = '" + comprobanteCobro+"'" ,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            exists = true;
        }
        if (mCursor.getCount()==0){
            exists= false;
        }
        return exists;

    }

    public void updateComprobCobros(String id, double valor){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_monto_a_pagar,valor);

        String[] columnas = new String[]{CC_monto_a_pagar};
        mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_plan_pago_detalle+"=? AND "+CC_id_comprob+"=?",new String[]{id,"0"});
    }

    public void changeEstadoToExport(String[] idComprobante, int estadoSincronizacion){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR,estadoSincronizacion);

        String signosInterrogacion = "";
        for (int i=0; i<idComprobante.length; i++){
            if (i==idComprobante.length-1)
            {
                signosInterrogacion+= "?";
            }else {
                signosInterrogacion+= "? OR ";
            }

        }

        Log.d("SIGNOS INTERROGACIÓN", signosInterrogacion);
        int cantidadRegistros = mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_cob_historial+"= "+ signosInterrogacion,idComprobante);


        Log.d("REGISTROS EXPORTADOS ", ""+cantidadRegistros);
    }

    public void updateComprobCobrosCan(String id, String fecha, String hora, double valor, String estado){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_monto_cobrado,valor);
        initialValues.put(CC_fecha_cobro,fecha);
        initialValues.put(CC_hora_cobro,hora);
        initialValues.put(CC_estado_cobro,estado);
        initialValues.put(Constants._SINCRONIZAR,Constants._ACTUALIZADO);
        String[] columnas = new String[]{CC_monto_a_pagar};
        mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_cob_historial+"=?",new String[]{id});

    }
    public void updateComprobCobrosMan(String id, String fecha, String hora, double valor, String estado){



        mDb.execSQL("update "+SQLITE_TABLE_Comprob_Cobro+" set "+CC_monto_a_pagar+"="+valor+", "+CC_fecha_programada+"='"+fecha+"',"+CC_hora_cobro+"='"+hora+"', "+CC_estado_cobro+"='"+estado+"',"+CC_monto_cobrado+"=0, "+ Constants._SINCRONIZAR + " = "+Constants._ACTUALIZADO+" where "+CC_id_cob_historial+"='"+id+"'");

    }
    public int updateComprobCobrosCan2(String id, String fecha, String hora, double valor, String estado){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_monto_cobrado,valor);
        initialValues.put(CC_fecha_cobro,fecha);
        initialValues.put(CC_hora_cobro,hora);
        initialValues.put(CC_estado_cobro,estado);
        initialValues.put(Constants._SINCRONIZAR,Constants._ACTUALIZADO);
        String[] columnas = new String[]{CC_monto_a_pagar};
        int insert = mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_cob_historial+"=?",new String[]{id});
        return insert;
    }

    public boolean deleteAllComprobCobros() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Comprob_Cobro, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor filterExportAndFetchById(int id_comprobante_venta) throws SQLException {

        Cursor mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Cobro, new String[] {

                        CC_id_comprobante_cobro,CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_id_plan_pago, CC_id_plan_pago_detalle,
                        CC_desc_tipo_doc, CC_doc, CC_fecha_programada, CC_monto_a_pagar,
                        CC_fecha_cobro, CC_monto_cobrado, CC_estado_cobro, Constants._SINCRONIZAR},
                CC_id_comprob+ " =  ? "+ " AND "+ Constants._SINCRONIZAR + " = ?",
                new String[]{
                        ""+id_comprobante_venta,
                        ""+Constants._CREADO
                }
                ,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if(mCursor.getCount()==0){
            Log.d("FILTER EXPORT PLAN PAGO ", "NULL");
        }
        return mCursor;

    }

    public Cursor filterExportUpdatedAndEstadoCobro() throws SQLException {

        Cursor mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Cobro, new String[] {

                        CC_id_comprobante_cobro,CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_id_plan_pago, CC_id_plan_pago_detalle,
                        CC_desc_tipo_doc, CC_doc, CC_fecha_programada, CC_monto_a_pagar,
                        CC_fecha_cobro, CC_monto_cobrado, CC_estado_cobro, Constants._SINCRONIZAR},
                 Constants._SINCRONIZAR + " = ? AND " +CC_estado_cobro + " = '0'",
                new String[]{
                        ""+Constants._ACTUALIZADO,
                }
                ,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if(mCursor.getCount()==0){
            Log.d("FILTER EXPORT PLAN PAGO ", "NULL");
        }
        return mCursor;

    }


    public Cursor fetchComprobCobrosByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Comprob_Cobro, new String[] {CC_id_cob_historial,
                            CC_id_establec, CC_id_comprob, CC_desc_tipo_doc, CC_doc, CC_fecha_programada,
                            CC_monto_a_pagar, CC_estado_cobro},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Cobro, new String[] {CC_id_cob_historial,
                            CC_id_establec, CC_id_comprob, CC_desc_tipo_doc, CC_doc, CC_fecha_programada,
                            CC_monto_a_pagar, CC_estado_cobro},
                    CC_fecha_programada + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllComprobCobros() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Cobro, new String[] {

                        CC_id_comprobante_cobro,CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_id_plan_pago, CC_id_plan_pago_detalle,
                        CC_desc_tipo_doc, CC_doc, CC_fecha_programada, CC_monto_a_pagar,
                        CC_fecha_cobro, CC_monto_cobrado, CC_estado_cobro, Constants._SINCRONIZAR},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor filterExport() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Cobro, new String[] {


                        CC_id_comprobante_cobro,CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_id_plan_pago, CC_id_plan_pago_detalle,
                        CC_desc_tipo_doc, CC_doc, CC_fecha_programada, CC_monto_a_pagar,
                        CC_fecha_cobro, CC_monto_cobrado, CC_estado_cobro},
                Constants._SINCRONIZAR + " = " + Constants._ACTUALIZADO, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }if (mCursor.getCount()==0){
            Log.d("FILTER EXPORT CC","NO HAY NINGÚN DATO FILTRADO POR COLUMNA SINCRONIZAR ACTUALIZADO");
        }


        return mCursor;
    }

    public Cursor fetchAllComprobCobrosByEst(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Cobro, new String[] {CC_id_comprobante_cobro , CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_id_plan_pago, CC_id_plan_pago_detalle,
                        CC_desc_tipo_doc, CC_doc, CC_fecha_programada, CC_monto_a_pagar,
                        CC_fecha_cobro, CC_monto_cobrado, CC_estado_cobro,CC_estado_prologa,CC_id_autorizacion},
                CC_id_establec + " = " + inputText +" and cc_in_estado_cobro ='1'  order by cc_te_fecha_programada desc", null,
                null, null, null, null);
        if (mCursor != null) {

            mCursor.moveToFirst();
        }
        return mCursor;

    }
    public Cursor listaComprobantes(int establex) {

        Cursor mCursor = mDb.rawQuery("SELECT cc_te_fecha_programada  FROM   m_comprob_cobro where cc_in_id_establec="+establex+" and  cc_in_estado_cobro ='1'  order by cc_te_fecha_programada asc",null);
        return mCursor;
    }
    public Cursor listarEstablecCobros(){
        Cursor mCursor = mDb.rawQuery("SELECT mc._id, mc.cc_te_doc,me.ee_te_nom_cliente,mc.cc_te_fecha_programada, mc.cc_re_monto_a_pagar,me.ee_te_nom_establec ,mc.cc_in_id_establec from m_comprob_cobro mc,m_evento_establec me where mc.cc_in_id_establec=me.ee_in_id_establec  order by mc.cc_te_fecha_programada asc",null);
        return mCursor;
    }
    public Cursor listarComprobantesToCobros(){
        Cursor mCursor = mDb.rawQuery("SELECT mc._id, mc.cc_te_doc,me.ee_te_nom_cliente,mc.cc_te_fecha_programada, mc.cc_re_monto_a_pagar,me.ee_te_nom_establec ,mc.cc_in_id_establec from m_comprob_cobro mc,m_evento_establec me where mc.cc_in_id_establec=me.ee_in_id_establec and mc.cc_in_estado_cobro='1' order by mc.cc_te_fecha_programada asc",null);
        return mCursor;
    }
    public Cursor listarComprobantesToCobrosMante(String idEstablec){
        Cursor mCursor = mDb.rawQuery("SELECT mc._id, mc.cc_te_doc,me.ee_te_nom_cliente,me.ee_te_nom_establec,mc.cc_te_fecha_cobro,mc.cc_te_hora_cobro, mc.cc_re_monto_cobrado, case when mc.cc_in_estado_cobro=0 then 'Cobrado' else 'Anulado' end as estado ,mc.cc_in_id_establec,mc.cc_te_fecha_programada from m_comprob_cobro mc,m_evento_establec me where mc.cc_in_id_establec=me.ee_in_id_establec and mc.cc_te_hora_cobro !=\"\" and me.ee_in_id_establec='"+idEstablec+"' order by mc.cc_te_fecha_programada asc",null);
        return mCursor;
    }


    public void insertSomeComprobCobros() {

        createComprobCobros(1, 1, 1, 1, "FACTURA", "FAC-0001", "14/01/2015", 1000, "",
                "", 1, 1, 1,1,"historial");
        createComprobCobros(1, 1, 1, 2, "FACTURA", "FAC-0001", "14/01/2015", 1000, "",
                "", 1, 1, 1,1,"historial");
        createComprobCobros(1, 1, 1, 2, "FACTURA", "FAC-0001", "14/01/2015", 500, "",
                "", 1, 1, 1,1,"historial");
        createComprobCobros(1, 1, 1, 2, "BOLETA", "FAC-0001", "14/01/2015", 200, "",
                "", 1, 1, 1,1,"historial");
        createComprobCobros(2, 2, 2, 2, "BOLETA", "FAC-0002", "14/01/2015", 200, "",
                "", 1, 1, 1,1,"historial");
    }

}