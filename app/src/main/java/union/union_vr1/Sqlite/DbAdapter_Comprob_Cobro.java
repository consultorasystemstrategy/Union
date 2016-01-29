package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.ComprobanteCobro;
import union.union_vr1.Utils.Utils;

public class DbAdapter_Comprob_Cobro {

    //LLENAMOS  TODOS ESTOS CAMPOS AL REALIZAR UNA VENTA AL CRÉDITO

    //ESTOS CAMPOS OBTENEMOS DEL PROCEDIMIENTO
    public static final String CC_id_cob_historial = "_id";
    public static final String CC_id_comprobante_cobro = "cc_in_id_comprobante_cobro";
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
    public static final String CC_estado_prorroga = "cc_estado_prologa";
    public static final String CC_liquidacion = "id_liquidacion";
    public static final String CC_estado_flex = "cc_estado_flex";
    public static final String CC_id_flex = "CC_id_flex";


    public static final String TAG = "Comprob_Cobro";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "ProdUnion.sqlite";
    public static final String SQLITE_TABLE_Comprob_Cobro = "m_comprob_cobro";
    private final Context mCtx;

    public static final String CREATE_TABLE_COMPROB_COBRO =
            "create table if not exists " + SQLITE_TABLE_Comprob_Cobro + " ("
                    + CC_id_cob_historial + " integer primary key autoincrement,"
                    + CC_id_comprobante_cobro + " text,"
                    + CC_id_establec + " integer,"
                    + CC_id_comprob + " integer,"
                    + CC_id_plan_pago + " integer,"
                    + CC_id_plan_pago_detalle + " integer,"
                    + CC_desc_tipo_doc + " text,"
                    + CC_doc + " text,"
                    + CC_fecha_programada + " text,"
                    + CC_monto_a_pagar + " real,"
                    + CC_fecha_cobro + " text,"
                    + CC_hora_cobro + " text,"
                    + CC_monto_cobrado + " real,"
                    + CC_estado_cobro + " integer,"
                    + CC_id_agente + " integer,"
                    + CC_id_flex + " integer,"
                    + CC_id_forma_cobro + " integer,"
                    + CC_estado_flex + " integer,"
                    + CC_lugar_registro + " text, "
                    + estado_sincronizacion + " integer,"
                    + CC_id_autorizacion + " integer,"
                    + CC_liquidacion + " integer, "

                    + CC_estado_prorroga + " text );";

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

    public Cursor getComprobanteCobroById(String idcomprobante) {
        Log.d("DATOS DEL CURSOR", "" + idcomprobante);
        Cursor cursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Comprob_Cobro + " where " + CC_id_comprobante_cobro + "='" + idcomprobante + "'", null);
        return cursor;
    }


    public long createComprobCobros(
            int id_establec, int id_comprob, int id_plan_pago, int id_plan_pago_detalle,
            String desc_tipo_doc, String doc, String fecha_programada, double monto_a_pagar,
            String fecha_cobro, String hora_cobro, double monto_cobrado, int estado_cobro,
            int id_agente, int id_forma_cobro, String lugar_registro, int liquidacion, String idComprobanteCobro, int estadoProrroga) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_id_comprobante_cobro, idComprobanteCobro);
        initialValues.put(CC_id_establec, id_establec);
        initialValues.put(CC_id_comprob, id_comprob);
        initialValues.put(CC_id_plan_pago, id_plan_pago);
        initialValues.put(CC_id_plan_pago_detalle, id_plan_pago_detalle);
        initialValues.put(CC_desc_tipo_doc, desc_tipo_doc);
        initialValues.put(CC_doc, doc);
        initialValues.put(CC_fecha_programada, fecha_programada);
        initialValues.put(CC_monto_a_pagar, monto_a_pagar);
        initialValues.put(CC_fecha_cobro, fecha_cobro);
        initialValues.put(CC_hora_cobro, hora_cobro);
        initialValues.put(CC_monto_cobrado, monto_cobrado);
        initialValues.put(CC_estado_cobro, estado_cobro);
        initialValues.put(CC_id_agente, id_agente);
        initialValues.put(CC_id_forma_cobro, id_forma_cobro);
        initialValues.put(CC_lugar_registro, lugar_registro);
        initialValues.put(CC_liquidacion, liquidacion);
        initialValues.put(CC_estado_prorroga, estadoProrroga);
        initialValues.put(Constants._SINCRONIZAR, Constants._CREADO);
        return mDb.insert(SQLITE_TABLE_Comprob_Cobro, null, initialValues);
    }

    public String getIdComrobanteCobro(String idEstablecimiento) {
        String idComprobanteCobro = "";
        Cursor cr = mDb.rawQuery("select max(cc_in_id_comprobante_cobro) as idComprobanteCobro from m_comprob_cobro where cc_in_id_establec = '" + idEstablecimiento + "'; ", null);
        cr.moveToFirst();
        String datos = cr.getString(cr.getColumnIndexOrThrow("idComprobanteCobro"));

        try {
            String[] arrayDatos = datos.split("-");
            int numero = Integer.parseInt(arrayDatos[2]);
            numero = numero + 1;
            idComprobanteCobro = idEstablecimiento + "-" + getDatePhone() + "-" + numero;
            Log.d("IDJAJAJA", "" + CC_id_comprob + "---" + arrayDatos[2] + "--" + idComprobanteCobro);

        } catch (NullPointerException e) {
            Log.e("NULLPOINT", "" + e.getMessage());
            idComprobanteCobro = idEstablecimiento + "-" + getDatePhone() + "-" + 1;
        }

        return idComprobanteCobro;
    }

    public String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }

    public boolean updateCobro(String idCobro, double valorP, double valorR) {
        boolean estado = false;
        try {
            mDb.execSQL("update " + SQLITE_TABLE_Comprob_Cobro + " set " + CC_monto_cobrado + "='" + valorP + "', " + CC_monto_a_pagar + "='" + valorR + "'," + Constants._SINCRONIZAR + "=" + Constants._ACTUALIZADO + " where " + CC_id_cob_historial + "='" + idCobro + "'");
            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }

    public long createComprobCobro(ComprobanteCobro comprobanteCobro) {

        ContentValues initialValues = new ContentValues();

        initialValues.put(CC_id_comprobante_cobro, comprobanteCobro.getIdComprobanteCobro());
        initialValues.put(CC_id_establec, comprobanteCobro.getIdEstablecimiento());
        initialValues.put(CC_id_comprob, comprobanteCobro.getIdComprobante());
        initialValues.put(CC_id_plan_pago, comprobanteCobro.getIdPlanPago());
        initialValues.put(CC_id_plan_pago_detalle, comprobanteCobro.getIdPlanPagoDetalle());
        initialValues.put(CC_desc_tipo_doc, comprobanteCobro.getDescTipoDoc());
        initialValues.put(CC_doc, comprobanteCobro.getDoc());
        initialValues.put(CC_fecha_programada, comprobanteCobro.getFechaProgramada());
        initialValues.put(CC_monto_a_pagar, comprobanteCobro.getMontoPagar());
        initialValues.put(CC_fecha_cobro, comprobanteCobro.getFechaCobro());
        initialValues.put(CC_hora_cobro, comprobanteCobro.getHoraCobro());
        initialValues.put(CC_monto_cobrado, comprobanteCobro.getMontoCobrado());
        initialValues.put(CC_estado_cobro, comprobanteCobro.getEstadoCobro());
        initialValues.put(CC_id_agente, comprobanteCobro.getIdAgente());
        initialValues.put(CC_id_forma_cobro, comprobanteCobro.getIdFormaCobro());
        initialValues.put(CC_lugar_registro, comprobanteCobro.getLugarRegistro());
        return mDb.insert(SQLITE_TABLE_Comprob_Cobro, null, initialValues);
    }

    public void updateEstadoEnviado(int idComprobanteCobro, int estado, String idAutorizacion) {
        mDb.execSQL("update " + SQLITE_TABLE_Comprob_Cobro + " set " + CC_estado_prorroga + "='" + estado + "'," + CC_id_autorizacion + "='" + idAutorizacion + "' where " + CC_id_comprobante_cobro + "='" + idComprobanteCobro + "';");
    }

    public int verProceso(String idComprobanteCobro) {
        int e = 0;
        Cursor cr = mDb.rawQuery("select * from " + SQLITE_TABLE_Comprob_Cobro+ " where " +CC_id_cob_historial + "='" + idComprobanteCobro + "';", null);
        if (cr.moveToFirst()) {
            e = cr.getInt(cr.getColumnIndexOrThrow(CC_estado_prorroga));
        }

        return e;
    }

    private String getTimePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }

    public int updateComprobCobrosExport(String id) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(estado_sincronizacion, Constants._ACTUALIZADO);
        return mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_comprobante_cobro + " = ?",
                new String[]{
                        "" + id
                });
    }
    public int updateComprobCobrosSN(String idcomprobante,String fechaProgramada,double montopagado,double  montodeuda ) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_fecha_programada,fechaProgramada);
        initialValues.put(CC_monto_a_pagar, montodeuda);
        //
        initialValues.put(CC_estado_prorroga,Constants.COBRO_PARCIAL);
        initialValues.put(CC_fecha_cobro, getDatePhone());
        initialValues.put(CC_hora_cobro, getTimePhone());
        initialValues.put(CC_monto_cobrado, montopagado);
        //initialValues.put(CC_estado_cobro, 2);
        //
        initialValues.put(estado_sincronizacion, Constants._ACTUALIZADO);


        return mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_comprobante_cobro + " = ?",
                new String[]{
                        "" + idcomprobante
                });
    }

    public int updateComprobCobrosAutorizacion(int estado, double montopagado, String idcomprobante) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_fecha_cobro, getDatePhone());
        initialValues.put(CC_hora_cobro, getTimePhone());
        initialValues.put(CC_monto_cobrado, montopagado);
        initialValues.put(CC_estado_cobro, estado);
        initialValues.put(estado_sincronizacion, Constants._ACTUALIZADO);


        return mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_comprobante_cobro + " = ?",
                new String[]{
                        "" + idcomprobante
                });
    }

    public void updateComprobCobros(ComprobanteCobro comprobanteCobro) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_id_comprobante_cobro, comprobanteCobro.getIdComprobanteCobro());
        initialValues.put(CC_id_establec, comprobanteCobro.getIdEstablecimiento());
        initialValues.put(CC_id_comprob, comprobanteCobro.getIdComprobante());
        initialValues.put(CC_id_plan_pago, comprobanteCobro.getIdPlanPago());
        initialValues.put(CC_id_plan_pago_detalle, comprobanteCobro.getIdPlanPagoDetalle());
        initialValues.put(CC_desc_tipo_doc, comprobanteCobro.getDescTipoDoc());
        initialValues.put(CC_doc, comprobanteCobro.getDoc());
        initialValues.put(CC_fecha_programada, comprobanteCobro.getFechaProgramada());
        initialValues.put(CC_monto_a_pagar, comprobanteCobro.getMontoPagar());
        initialValues.put(CC_fecha_cobro, comprobanteCobro.getFechaCobro());
        initialValues.put(CC_hora_cobro, comprobanteCobro.getHoraCobro());
        initialValues.put(CC_monto_cobrado, comprobanteCobro.getMontoCobrado());
        initialValues.put(CC_estado_cobro, comprobanteCobro.getEstadoCobro());
        initialValues.put(CC_id_agente, comprobanteCobro.getIdAgente());
        initialValues.put(CC_id_forma_cobro, comprobanteCobro.getIdFormaCobro());
        initialValues.put(CC_lugar_registro, comprobanteCobro.getLugarRegistro());


        mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_comprobante_cobro + " = ?",
                new String[]{
                        "" + comprobanteCobro.getIdComprobanteCobro()
                });
    }


    public boolean existeComprobCobro(String comprobanteCobro) throws SQLException {
        boolean exists = false;
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Cobro, new String[]{CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_desc_tipo_doc, CC_doc, CC_fecha_programada,
                        CC_monto_a_pagar, CC_estado_cobro, CC_id_comprobante_cobro},
                CC_id_comprobante_cobro + " = '" + comprobanteCobro + "'",
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            Log.e("ID", "" + mCursor.getString(mCursor.getColumnIndexOrThrow(CC_id_comprobante_cobro)) + "---" + comprobanteCobro);
            exists = true;
        }
        if (mCursor.getCount() == 0) {
            exists = false;
        }
        return exists;

    }

    public void updateComprobCobros(String id, double valor) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_monto_a_pagar, valor);

        String[] columnas = new String[]{CC_monto_a_pagar};
        mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_plan_pago_detalle + "=? AND " + CC_id_comprob + "=?", new String[]{id, "0"});
    }

    public int updateComprobCobros_Auto(String id, double valor, String fecha) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_monto_cobrado, valor);
        initialValues.put(CC_fecha_programada, fecha);
        initialValues.put(Constants._SINCRONIZAR, Constants._ACTUALIZADO);

        int i = mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_comprobante_cobro + "=?", new String[]{id});
        return i;
    }

    public int changeEstadoToExportToFlex(String idComprobante, int estado) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR, estado);
        return mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_cob_historial + "=?", new String[]{idComprobante});
    }
    public int changeEstadoToExportParcial(String idComprobante, int estado) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_estado_prorroga, Constants.COBRO_PARCIAL_EXPORTADO);
        return mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_cob_historial + "=?", new String[]{idComprobante});
    }

    public int changeEstadoToExportToFlexForId(String idComprobante, int estado, int idFlex) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_estado_flex, estado);
        return mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_cob_historial + "=?", new String[]{idComprobante});
    }

    public void changeEstadoToExport(String[] idComprobante, int estadoSincronizacion) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR, estadoSincronizacion);

        String signosInterrogacion = "";
        for (int i = 0; i < idComprobante.length; i++) {
            if (i == idComprobante.length - 1) {
                signosInterrogacion += "?";
            } else {
                signosInterrogacion += "? OR ";
            }

        }

        Log.d("SIGNOS INTERROGACIÓN", signosInterrogacion);
        int cantidadRegistros = mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_cob_historial + "= " + signosInterrogacion, idComprobante);


        Log.d("REGISTROS EXPORTADOS ", "" + cantidadRegistros);
    }

    public long updateComprobCobrosCan(String id, String fecha, String hora, double valor, String estado) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_monto_cobrado, valor);
        initialValues.put(CC_fecha_cobro, fecha);
        initialValues.put(CC_hora_cobro, hora);
        initialValues.put(CC_estado_cobro, estado);
        initialValues.put(Constants._SINCRONIZAR, Constants._ACTUALIZADO);
        return mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_cob_historial + "=?", new String[]{id});

    }

    public void updateComprobCobrosMan(String id, String fecha, String hora, double valor, String estado) {


        mDb.execSQL("update " + SQLITE_TABLE_Comprob_Cobro + " set " + CC_monto_a_pagar + "=" + valor + ", " + CC_fecha_programada + "='" + fecha + "'," + CC_hora_cobro + "='" + hora + "', " + CC_estado_cobro + "='" + estado + "'," + CC_monto_cobrado + "=0, " + Constants._SINCRONIZAR + " = " + Constants._ACTUALIZADO + " where " + CC_id_cob_historial + "='" + id + "'");

    }

    public int updateComprobCobrosCan2(String id, String fecha, String hora, double valor, String estado) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_monto_cobrado, valor);
        initialValues.put(CC_fecha_cobro, fecha);
        initialValues.put(CC_hora_cobro, hora);
        initialValues.put(CC_estado_cobro, estado);
        initialValues.put(Constants._SINCRONIZAR, Constants._ACTUALIZADO);
        String[] columnas = new String[]{CC_monto_a_pagar};
        int insert = mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_cob_historial + "=?", new String[]{id});
        return insert;
    }

    public boolean deleteAllComprobCobros() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Comprob_Cobro, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor filterExportAndFetchById(int id_comprobante_venta, int idPlan) throws SQLException {

        Cursor mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Cobro, new String[]{

                        CC_id_comprobante_cobro, CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_id_plan_pago, CC_id_plan_pago_detalle,
                        CC_desc_tipo_doc, CC_doc, CC_fecha_programada, CC_monto_a_pagar,
                        CC_fecha_cobro, CC_monto_cobrado, CC_estado_cobro, Constants._SINCRONIZAR},
                CC_id_comprob + " =  ? " + " AND " + Constants._SINCRONIZAR + " = ?",
                new String[]{
                        "" + id_comprobante_venta,
                        "" + Constants._CREADO
                }
                ,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            ContentValues initialValues = new ContentValues();
            initialValues.put(CC_id_plan_pago, idPlan);
            initialValues.put(Constants._SINCRONIZAR, Constants._ACTUALIZADO);
            String[] columnas = new String[]{CC_monto_a_pagar};
            int insert = mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                    CC_id_comprob + "=?", new String[]{id_comprobante_venta + ""});


        }
        if (mCursor.getCount() == 0) {
            Log.d("FILTER EXPORT PLAN PAGO ", "NULL");
        }
        return mCursor;

    }

    public int updatePlanPagoDetalleCobros(int id_comprobante_venta, int idPlanPagoDetalle) throws SQLException {
        int insert = 0;


        ContentValues initialValues = new ContentValues();
        initialValues.put(CC_id_plan_pago_detalle, idPlanPagoDetalle);
        initialValues.put(Constants._SINCRONIZAR, Constants._ACTUALIZADO);
        insert = mDb.update(SQLITE_TABLE_Comprob_Cobro, initialValues,
                CC_id_comprob + "=?", new String[]{id_comprobante_venta + ""});


        return insert;

    }
    public Cursor filterExportUpdatedAndEstadoCobroParcial() throws SQLException {

        Cursor mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Cobro, new String[]{

                        CC_id_comprobante_cobro, CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_id_plan_pago, CC_id_plan_pago_detalle,
                        CC_desc_tipo_doc, CC_doc, CC_fecha_programada, CC_monto_a_pagar,
                        CC_fecha_cobro, CC_monto_cobrado, CC_estado_cobro, Constants._SINCRONIZAR},
                Constants._SINCRONIZAR + " = ? AND " + CC_estado_prorroga + " = '"+Constants.COBRO_PARCIAL+"'",
                new String[]{
                        "" + Constants._ACTUALIZADO,
                }
                ,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.getCount() == 0) {
            Log.d("FILTER EXPORT PLAN PAGO ", "NULL");
        }
        return mCursor;

    }

    public Cursor filterExportUpdatedAndEstadoCobro() throws SQLException {

        Cursor mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Cobro, new String[]{

                        CC_id_comprobante_cobro, CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_id_plan_pago, CC_id_plan_pago_detalle,
                        CC_desc_tipo_doc, CC_doc, CC_fecha_programada, CC_monto_a_pagar,
                        CC_fecha_cobro, CC_monto_cobrado, CC_estado_cobro, Constants._SINCRONIZAR},
                Constants._SINCRONIZAR + " = ? AND " + CC_estado_cobro + " = '0'",
                new String[]{
                        "" + Constants._ACTUALIZADO,
                }
                ,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.getCount() == 0) {
            Log.d("FILTER EXPORT PLAN PAGO ", "NULL");
        }
        return mCursor;

    }


    public Cursor fetchComprobCobrosByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null || inputText.length() == 0) {
            mCursor = mDb.query(SQLITE_TABLE_Comprob_Cobro, new String[]{CC_id_cob_historial,
                            CC_id_establec, CC_id_comprob, CC_desc_tipo_doc, CC_doc, CC_fecha_programada,
                            CC_monto_a_pagar, CC_estado_cobro},
                    null, null, null, null, null);

        } else {
            mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Cobro, new String[]{CC_id_cob_historial,
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

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Cobro, new String[]{

                        CC_id_comprobante_cobro, CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_id_plan_pago, CC_id_plan_pago_detalle,
                        CC_desc_tipo_doc, CC_doc, CC_fecha_programada, CC_monto_a_pagar,
                        CC_fecha_cobro, CC_monto_cobrado, CC_estado_cobro, Constants._SINCRONIZAR},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchComprobCobrosByIdComprobante(int id_comprob) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Cobro, new String[]{

                        CC_id_comprobante_cobro, CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_id_plan_pago, CC_id_plan_pago_detalle,
                        CC_desc_tipo_doc, CC_doc, CC_fecha_programada, CC_monto_a_pagar,
                        CC_fecha_cobro, CC_monto_cobrado, CC_estado_cobro, Constants._SINCRONIZAR},
                CC_id_comprob + " = " + id_comprob, null, null, null, CC_fecha_programada + " ASC");

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public int fetchComprobCobrosById(String id_comprob) {
        int id = 0;
        Cursor mCursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Comprob_Cobro + " where _id='" + id_comprob + "'", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
            id = mCursor.getInt(mCursor.getColumnIndexOrThrow(CC_id_comprobante_cobro));
        }
        return id;
    }

    public Cursor fetchNormal() {
        return mDb.rawQuery("select * from " + SQLITE_TABLE_Comprob_Cobro + " where " + CC_estado_flex + "='" + Constants.POR_EXPORTAR_FLEX + "'", null);
    }

    public Cursor filterExport() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Cobro, new String[]{


                        CC_id_comprobante_cobro, CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_id_plan_pago, CC_id_plan_pago_detalle,
                        CC_desc_tipo_doc, CC_doc, CC_fecha_programada, CC_monto_a_pagar,
                        CC_fecha_cobro, CC_monto_cobrado, CC_estado_cobro},
                Constants._SINCRONIZAR + " = " + Constants._CREADO, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.getCount() == 0) {
            Log.d("FILTER EXPORT CC", "NO HAY NINGÚN DATO FILTRADO POR COLUMNA SINCRONIZAR ACTUALIZADO");
        }


        return mCursor;
    }

    /*
    public Cursor fetchAllComprobCobrosByEst(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Cobro, new String[]{CC_id_comprobante_cobro, CC_id_cob_historial,
                        CC_id_establec, CC_id_comprob, CC_id_plan_pago, CC_id_plan_pago_detalle,
                        CC_desc_tipo_doc, CC_doc, CC_fecha_programada, CC_monto_a_pagar,
                        CC_fecha_cobro, CC_monto_cobrado, CC_estado_cobro, , CC_id_autorizacion},
                CC_id_establec + " = " + inputText + " and cc_in_estado_cobro ='1' and cc_in_id_comprobante_cobro is not null  order by cc_te_fecha_programada desc", null,
                null, null, null, null);
        if (mCursor != null) {

            mCursor.moveToFirst();
        }
        return mCursor;

    }*/
    public Cursor fetchAllComprobCobrosByEst(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        String query = "SELECT me.ee_te_nom_establec,mc.* FROM m_comprob_cobro mc,m_evento_establec me where mc.cc_in_id_establec =me.ee_in_id_establec and  " + CC_id_establec + " = " + inputText + " and cc_in_estado_cobro !='0' and cc_in_id_comprobante_cobro is not null  order by cc_te_fecha_programada desc";
        mCursor = mDb.rawQuery(query, null);
        if (mCursor != null) {

            mCursor.moveToFirst();
        }
        Log.d("QUERY", "" + query);
        return mCursor;

    }

    public Cursor listaComprobantes(int establex) {

        Cursor mCursor = mDb.rawQuery("SELECT cc_te_fecha_programada  FROM   m_comprob_cobro where cc_in_id_establec=" + establex + " and  cc_in_estado_cobro !='0'  order by cc_te_fecha_programada asc", null);
        return mCursor;
    }

    public Cursor listarEstablecCobros() {
        Cursor mCursor = mDb.rawQuery("SELECT mc._id, mc.cc_te_doc,me.ee_te_nom_cliente,mc.cc_te_fecha_programada, mc.cc_re_monto_a_pagar,me.ee_te_nom_establec ,mc.cc_in_id_establec from m_comprob_cobro mc,m_evento_establec me where mc.cc_in_id_establec=me.ee_in_id_establec  order by mc.cc_te_fecha_programada asc", null);
        return mCursor;
    }

    public Cursor listarComprobantesToCobros(int agente) {

        Cursor mCursor = mDb.rawQuery(" SELECT me.ee_te_nom_establec,me.ee_te_nom_cliente,mc.* FROM m_comprob_cobro mc,m_evento_establec me where mc.cc_in_id_establec =me.ee_in_id_establec and  cc_in_estado_cobro !='0' and cc_in_id_comprobante_cobro is not null  order by cc_te_fecha_programada desc ", null);
        return mCursor;
    }

    public Cursor listarComprobantesToCobrosMante(String idEstablec) {
        Cursor mCursor = mDb.rawQuery("SELECT mc._id as _id,1 as tipo, mc.cc_te_doc as cc_te_doc,me.ee_te_nom_cliente as ee_te_nom_cliente ,mc.cc_te_fecha_cobro as cc_te_fecha_cobro,mc.cc_te_hora_cobro as cc_te_hora_cobro, mc.cc_re_monto_cobrado as cc_re_monto_cobrado, case when mc.cc_in_estado_cobro=0 then 'Cobrado' else 'Anulado' end as estado  from m_comprob_cobro mc,m_evento_establec me where mc.cc_in_id_establec=me.ee_in_id_establec and mc.cc_te_hora_cobro !=\"\" and me.ee_in_id_establec='" + idEstablec + "'" +
                " union select _id as _id,2 as tipo, " + DbAdapter_Cobros_Manuales.CM_Numero + " as cc_te_doc, " + DbAdapter_Cobros_Manuales.CM_Nombre_Cliente + " as ee_te_nom_cliente, " + DbAdapter_Cobros_Manuales.CM_Fecha + " as cc_te_fecha_cobro," +
                " " + DbAdapter_Cobros_Manuales.CM_Hora + " as cc_te_hora_cobro, " + DbAdapter_Cobros_Manuales.CM_Importe + " as cc_re_monto_cobrado, " + DbAdapter_Cobros_Manuales.CM_Estado + " as estado from " + DbAdapter_Cobros_Manuales.SQLITE_TABLE_Cobros_Manuales + " where " + DbAdapter_Cobros_Manuales.CM_Id_Establecimiento + "='" + idEstablec + "' and " + Constants._SINCRONIZAR + "='" + Constants._EXPORTADO + "' ", null);
        return mCursor;
    }

    public Cursor printCabecera(String comprobante) {
        Cursor cr = mDb.rawQuery("select mc._id,mc.cc_in_id_comprobante_cobro,mc.cc_te_doc,me.ee_te_nom_cliente,ma.ag_te_nombre_agente from m_comprob_cobro mc,m_evento_establec me,m_agente ma" +
                " where mc.cc_in_id_establec=me.ee_in_id_establec and mc.cc_in_id_agente = ma.ag_in_id_agente_venta and mc._id='" + comprobante + "' ", null);
        return cr;
    }
}