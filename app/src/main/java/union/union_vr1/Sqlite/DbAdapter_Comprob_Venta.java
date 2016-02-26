package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.ComprobanteVenta;
import union.union_vr1.Servicios.ServiceExport;

public class DbAdapter_Comprob_Venta {

    public static final String CV_id_comprob = "_id";

    //PROCEDIMIENTOS NOS DA ESTOS CAMPOS
    public static final String CV_id_comp = "cv_in_id_comp";
    public static final String CV_id_establec = "cv_in_id_establec";
    public static final String CV_id_tipo_doc = "cv_in_id_tipo_doc";
    public static final String CV_id_forma_pago = "cv_in_id_forma_pago";
    public static final String CV_id_tipo_venta = "cv_in_id_tipo_venta";
    public static final String CV_codigo_erp = "cv_te_codigo_erp";
    public static final String CV_serie = "cv_te_serie";
    public static final String CV_num_doc = "cv_in_num_doc";
    public static final String CV_base_imp = "cv_re_base_imp";
    public static final String CV_igv = "cv_re_igv";
    public static final String CV_total = "cv_re_total";
    public static final String CV_id_agente = "cv_in_id_agente";
    public static final String CV_id_tipo_pago = "cv_in_id_tipo_pago";
    public static final String estado_sincronizacion = "estado_sincronizacion";

    //FALTA OBTENER ESTAS COLUMNAS
    public static final String CV_fecha_doc = "cv_te_fecha_doc";
    public static final String CV_hora_doc = "cv_te_hora_doc";
        //ACTUALIZAMOS ESTE CAMPO[CUANDO UN CLIENTE NOS DICE QUE QUIERE DEVOLVER UN PRODUCTO]
    public static final String CV_estado_comp = "cv_in_estado_comp";
    public static final String CV_liquidacion = "id_liquidacion";
    public static final String CV_direccion_id = "cv_direccion_id";

    //ID DE LA BASE DE DATOS SQL SERVER
    public static final String CV_id_SQL_SERVER_comprob = "id_sql_server_comprob";
    public static final String CV_sincronizacion_sha1 = "sincronizacion_sha1";



    public static final String CV_estado_conexion = "cv_in_estado_conexion";
    public static final String CV_estado_sincronizacion_anulacion = "cv_sinc_anulado";

    public static final String CV_SHA1 = "cv_sha1";


    public static final String TAG = "Comprob_Venta";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "ProdUnion.sqlite";
    private static final String SQLITE_TABLE_Comprob_Venta = "m_comprob_venta";
    private final Context mCtx;

    public static final String CREATE_TABLE_COMPROB_VENTA =
            "create table "+SQLITE_TABLE_Comprob_Venta+" ("
                    +CV_id_comprob+" integer primary key autoincrement,"
                    +CV_id_establec+" integer,"
                    +CV_id_tipo_doc+" integer,"
                    +CV_id_forma_pago+" integer,"
                    +CV_id_tipo_pago+" integer,"
                    +CV_id_tipo_venta+" integer,"
                    +CV_codigo_erp+" text,"
                    +CV_serie+" text,"
                    +CV_num_doc+" integer,"
                    +CV_direccion_id+" integer,"
                    +CV_base_imp+" real,"
                    +CV_igv+" real,"
                    +CV_total+" real,"
                    +CV_fecha_doc+" text,"
                    +CV_SHA1+" text,"
                    +CV_hora_doc+" text,"
                    +CV_estado_comp+" integer,"
                    +CV_estado_conexion+" integer,"
                    +CV_id_agente+" integer, "
                    +CV_liquidacion+" integer, "
                    +CV_id_SQL_SERVER_comprob+" integer, "
                    +CV_sincronizacion_sha1+" integer, "
                    +CV_estado_sincronizacion_anulacion+" integer, "
                    +estado_sincronizacion+" integer);";

    public static final String DELETE_TABLE_COMPROB_VENTA = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Comprob_Venta;

    public DbAdapter_Comprob_Venta(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Comprob_Venta open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createComprobVenta(
            int id_establec, int id_tipo_doc, int id_forma_pago, int id_tipo_pago, int id_tipo_venta,
            String codigo_erp, String serie, int num_doc, double base_imp, double igv, double total,
            String fecha_doc, String hora_doc, int estado_comp, int estado_conexion, int id_agente, int estadoSincronizacion, int liquidacion, int direccion_id){

        ContentValues initialValues = new ContentValues();
        initialValues.put(CV_id_establec,id_establec);
        initialValues.put(CV_id_tipo_doc,id_tipo_doc);
        initialValues.put(CV_id_forma_pago,id_forma_pago);
        initialValues.put(CV_id_tipo_pago,id_tipo_pago);
        initialValues.put(CV_id_tipo_venta,id_tipo_venta);
        initialValues.put(CV_codigo_erp,codigo_erp);
        initialValues.put(CV_serie,serie);
        initialValues.put(CV_num_doc,num_doc);
        initialValues.put(CV_base_imp,base_imp);
        initialValues.put(CV_igv,igv);
        initialValues.put(CV_total,total);
        initialValues.put(CV_fecha_doc,fecha_doc);
        initialValues.put(CV_hora_doc,hora_doc);
        initialValues.put(CV_estado_comp,estado_comp);
        initialValues.put(CV_estado_conexion,estado_conexion);
        initialValues.put(CV_id_agente,id_agente);
        initialValues.put(CV_liquidacion,liquidacion);
        initialValues.put(CV_direccion_id,direccion_id);
        initialValues.put(CV_estado_sincronizacion_anulacion, Constants._CREADO);
        initialValues.put(Constants._SINCRONIZAR, estadoSincronizacion);


        return mDb.insert(SQLITE_TABLE_Comprob_Venta, null, initialValues);
    }

    public long createComprobVentas(ComprobanteVenta comprobante, int id_agente){

        ContentValues initialValues = new ContentValues();
        initialValues.put(CV_id_establec,comprobante.getIdEstablecimiento());
        initialValues.put(CV_id_tipo_doc, comprobante.getIdTipoDocumento());
        initialValues.put(CV_id_forma_pago,comprobante.getIdFormaPago());
        initialValues.put(CV_id_tipo_venta,comprobante.getIdTipoVenta());
        initialValues.put(CV_codigo_erp,comprobante.getCodigoErp());
        initialValues.put(CV_serie,comprobante.getSerie());
        initialValues.put(CV_num_doc,comprobante.getNumeroDocumento());
        initialValues.put(CV_base_imp,comprobante.getBaseImponible());
        initialValues.put(CV_igv,comprobante.getIgv());
        initialValues.put(CV_total,comprobante.getTotal());
        initialValues.put(CV_fecha_doc, comprobante.getFechaDoc());
        initialValues.put(CV_hora_doc,comprobante.getHoraDoc());
        initialValues.put(CV_estado_comp, comprobante.getEstadoComprobante());
        initialValues.put(CV_estado_conexion, comprobante.getEstadoConexion());
        initialValues.put(CV_id_agente, id_agente);

        return mDb.insert(SQLITE_TABLE_Comprob_Venta, null, initialValues);
    }

    public void updateComprobantes(ComprobanteVenta comprobante, int id_agente){

        ContentValues initialValues = new ContentValues();
        initialValues.put(CV_id_establec,comprobante.getIdEstablecimiento());
        initialValues.put(CV_id_tipo_doc, comprobante.getIdTipoDocumento());
        initialValues.put(CV_id_forma_pago,comprobante.getIdFormaPago());
        initialValues.put(CV_id_tipo_venta,comprobante.getIdTipoVenta());
        initialValues.put(CV_codigo_erp,comprobante.getCodigoErp());
        initialValues.put(CV_serie,comprobante.getSerie());
        initialValues.put(CV_num_doc,comprobante.getNumeroDocumento());
        initialValues.put(CV_base_imp,comprobante.getBaseImponible());
        initialValues.put(CV_igv,comprobante.getIgv());
        initialValues.put(CV_total,comprobante.getTotal());
        initialValues.put(CV_fecha_doc, comprobante.getFechaDoc());
        initialValues.put(CV_hora_doc,comprobante.getHoraDoc());
        initialValues.put(CV_estado_comp, comprobante.getEstadoComprobante());
        initialValues.put(CV_estado_conexion, comprobante.getEstadoConexion());
        initialValues.put(CV_id_agente,id_agente);

        mDb.update(SQLITE_TABLE_Comprob_Venta, initialValues,
                CV_id_establec + "=?", new String[]{"" + comprobante.getIdEstablecimiento()});
    }

    public int updateSHA1(int _id, String SHA1){

        ContentValues initialValues = new ContentValues();
        initialValues.put(CV_SHA1,SHA1);
        initialValues.put(CV_sincronizacion_sha1, Constants._ACTUALIZADO);

        return mDb.update(SQLITE_TABLE_Comprob_Venta, initialValues,
                CV_id_comprob+"=?",new String[]{""+_id});
    }

/*    public Cursor filterToExportSHA1(int liquidacion) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta, new String[] {CV_id_comprob,

                        CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                        CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente, CV_SHA1, CV_sincronizacion_sha1, CV_id_SQL_SERVER_comprob
                },
                CV_liquidacion + " = '"+liquidacion+"' AND "+ CV_sincronizacion_sha1 + " != '"+Constants._EXPORTADO+"' AND "+CV_SHA1 + " != '' AND "+CV_id_SQL_SERVER_comprob + " != '';", null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }*/
//export
    public Cursor filterToExportSHA1(int liquidacion){
        Cursor cr = mDb.rawQuery("SELECT CV.*, EC.* FROM \n" +
                "m_comprob_venta CV \n" +
                "INNER JOIN m_exportacion_comprobantes EC \n" +
                "ON (CV._id = EC._id_sqlite) \n" +
                "WHERE CV.id_liquidacion = ? \n" +
                "AND CV.sincronizacion_sha1 != ? \n" +
                "AND CV.cv_sha1 !='' \n" +
                "AND EC._id_sid !=''", new String[]{"" + liquidacion, ""+Constants._EXPORTADO});
        return cr;
    }
    public Cursor fetchSHA1(int liquidacion) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta, new String[]{CV_id_comprob,

                        CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                        CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion,
                        CV_id_agente, CV_SHA1, CV_sincronizacion_sha1, CV_id_SQL_SERVER_comprob, CV_id_tipo_pago
                },
                CV_liquidacion + " = ? ", new String[]{"" + liquidacion}, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



    public boolean existeComprobVentaById(int id) {
        boolean exists = false;
        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta, new String[] {CV_id_comprob,

                        CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                        CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente, CV_id_tipo_pago
                },
                CV_id_establec + " = " + id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
            exists = true;
        }
        if (mCursor.getCount()==0){
            exists = false;
        }
        return exists;
    }

    public boolean deleteAllComprobVenta() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Comprob_Venta, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public void updateComprobante(int id, int anulado, int estadoSincronizacion){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CV_estado_comp, anulado);
        initialValues.put(CV_estado_sincronizacion_anulacion, estadoSincronizacion);
        mDb.update(SQLITE_TABLE_Comprob_Venta, initialValues,
                CV_id_comprob + "=?", new String[]{"" + id});
    }
    public int updateSerie(int id, String serie, int num_doc, int estadoSincronizacion){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CV_serie, serie);
        initialValues.put(CV_num_doc, num_doc);
        initialValues.put(Constants._SINCRONIZAR, Constants._ACTUALIZADO);
        return mDb.update(SQLITE_TABLE_Comprob_Venta, initialValues,
                CV_id_comprob + "=?", new String[]{"" + id});
    }


    public int updateComprobanteIDReal(long id, int idReal){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CV_id_SQL_SERVER_comprob, idReal);

        return mDb.update(SQLITE_TABLE_Comprob_Venta, initialValues,
                CV_id_comprob+"=?",new String[]{""+id});

    }

    public int changeEstadoToExport(String[] idComprobante, int estadoSincronizacion){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR, estadoSincronizacion);

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
        int cantidadRegistros = mDb.update(SQLITE_TABLE_Comprob_Venta, initialValues,
                CV_id_comprob+"= "+ signosInterrogacion,idComprobante);


        Log.d(ServiceExport.TAG, "REGISTROS EXPORTADOS CV " + cantidadRegistros);
        return cantidadRegistros;
    }
    public int changeEstadoAnuladoToExport(String[] idComprobante, int estadoSincronizacion){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CV_estado_sincronizacion_anulacion, estadoSincronizacion);

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
        int cantidadRegistros = mDb.update(SQLITE_TABLE_Comprob_Venta, initialValues,
                CV_id_comprob+"= "+ signosInterrogacion,idComprobante);


        Log.d(ServiceExport.TAG, "REGISTROS EXPORTADOS CV " + cantidadRegistros);
        return cantidadRegistros;
    }


    public int changeEstadoSHA1ToExport(String[] idComprobante, int estadoSincronizacionSHA1){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CV_sincronizacion_sha1,estadoSincronizacionSHA1);

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
        int cantidadRegistros = mDb.update(SQLITE_TABLE_Comprob_Venta, initialValues,
                CV_id_comprob+"= "+ signosInterrogacion,idComprobante);


        Log.d("REGISTROS EXPORTADOS ", ""+cantidadRegistros);
        return cantidadRegistros;
    }

    public int updateComprobanteMontos(long id, Double total, Double igv, Double base_imponible){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CV_total,total);
        initialValues.put(CV_igv, igv);
        initialValues.put(CV_base_imp, base_imponible);


        return mDb.update(SQLITE_TABLE_Comprob_Venta, initialValues,
                CV_id_comprob+"=?",new String[]{""+id});
    }



    public Cursor fetchComprobVentaByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta,  new String[] {CV_id_comprob,

                            CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                            CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                            CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente, CV_id_tipo_pago
                    },
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Venta,new String[] {CV_id_comprob,

                            CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                            CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                            CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente, CV_id_tipo_pago
                    },
                    CV_num_doc + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    public Cursor filterExport() {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Venta,new String[] {CV_id_comprob,

                        CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                        CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion,
                        CV_id_agente, CV_id_tipo_pago, CV_direccion_id
                },
                Constants._SINCRONIZAR + " = " + Constants._CREADO + " OR " + Constants._SINCRONIZAR + " = " + Constants._ACTUALIZADO, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor filterExportAnulado() {
        Cursor mCursor = null;
        mCursor = mDb.rawQuery("SELECT CV.*, EC.* \n" +
                "FROM m_comprob_venta CV \n" +
                "INNER JOIN m_exportacion_comprobantes EC \n" +
                "ON (CV._id = EC._id_sqlite) \n"+
                "WHERE CV."+CV_estado_comp+" = ? AND CV."+CV_estado_sincronizacion_anulacion +" = ? AND EC."+DbAdapter_Exportacion_Comprobantes.EC_id_sid + " > 0", new String[]{""+Constants._CV_ANULADO,""+Constants._ACTUALIZADO});
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public Cursor fetchAllComprobVenta(int idEstablecimiento, int liquidacion) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta, new String[] {CV_id_comprob,

                        CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                        CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente, CV_id_tipo_pago
                },
                CV_id_establec + " = ? AND "+CV_liquidacion + " = ?", new String[]{""+idEstablecimiento, ""+liquidacion}, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor fetchAllComprobVentaByName(int idEstablecimiento, int liquidacion, String name) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta, new String[] {CV_id_comprob,

                        CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                        CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente, CV_id_tipo_pago
                },
                CV_id_establec + " = ? AND "+CV_liquidacion + " = ? AND "+CV_codigo_erp +" LIKE '%"+name+"%'", new String[]{""+idEstablecimiento, ""+liquidacion}, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public Cursor fetchAllComprobVentaByEstable(String id) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta, new String[] {CV_id_comprob,

                        CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                        CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente, CV_id_tipo_pago
                },
                CV_id_establec + " = " + id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor fetchAllComprobVentaById(int id) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta, new String[] {CV_id_comprob,

                        CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                        CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente, CV_id_tipo_pago
                },
                CV_id_comprob + " = " + id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getTotalVentaByIdEstablecimientoAndLiquidacion(int idEstablecimiento, int liquidacion){
        Cursor cr = mDb.rawQuery("SELECT SUM(cv_re_total) AS total\n" +
                "FROM m_comprob_venta\n" +
                "WHERE cv_in_id_establec = ? \n" +
                "AND id_liquidacion  = ? " +
                "AND cv_in_estado_comp = '1'; ", new String[]{""+idEstablecimiento,""+liquidacion});
        return cr;
    }

    public Cursor getVentaCabecerabyID(int idComprobante){
        Cursor cr = mDb.rawQuery("SELECT CV.*, EE.*, A.* \n" +
                "FROM m_comprob_venta CV \n" +
                "INNER JOIN m_evento_establec EE \n" +
                "ON (CV.cv_in_id_establec = EE.ee_in_id_establec) \n" +
                "INNER JOIN m_agente A \n" +
                "ON (CV.cv_in_id_agente = A.ag_in_id_agente_venta) \n" +
                "WHERE CV._id = ?", new String[]{""+idComprobante});
        return cr;
    }



}