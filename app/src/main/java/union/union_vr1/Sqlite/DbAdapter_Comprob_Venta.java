package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.ComprobanteVenta;

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
    public static final String estado_sincronizacion = "estado_sincronizacion";

    //FALTA OBTENER ESTAS COLUMNAS
    public static final String CV_fecha_doc = "cv_te_fecha_doc";
    public static final String CV_hora_doc = "cv_te_hora_doc";
        //ACTUALIZAMOS ESTE CAMPO[CUANDO UN CLIENTE NOS DICE QUE QUIERE DEVOLVER UN PRODUCTO]
    public static final String CV_estado_comp = "cv_in_estado_comp";

    public static final String CV_estado_conexion = "cv_in_estado_conexion";


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
                    +CV_id_tipo_venta+" integer,"
                    +CV_codigo_erp+" text,"
                    +CV_serie+" text,"
                    +CV_num_doc+" integer,"
                    +CV_base_imp+" real,"
                    +CV_igv+" real,"
                    +CV_total+" real,"
                    +CV_fecha_doc+" text,"
                    +CV_hora_doc+" text,"
                    +CV_estado_comp+" integer,"
                    +CV_estado_conexion+" integer,"
                    +CV_id_agente+" integer, "
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
            int id_establec, int id_tipo_doc, int id_forma_pago, int id_tipo_venta,
            String codigo_erp, String serie, int num_doc, double base_imp, double igv, double total,
            String fecha_doc, String hora_doc, int estado_comp, int estado_conexion, int id_agente, int estadoSincronizacion){

        ContentValues initialValues = new ContentValues();
        initialValues.put(CV_id_establec,id_establec);
        initialValues.put(CV_id_tipo_doc,id_tipo_doc);
        initialValues.put(CV_id_forma_pago,id_forma_pago);
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
        initialValues.put(CV_id_agente,id_agente);

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
                CV_id_establec+"=?",new String[]{""+comprobante.getIdEstablecimiento()});
    }

    public boolean existeComprobVentaById(int id) {
        boolean exists = false;
        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta, new String[] {CV_id_comprob,

                        CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                        CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente
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

    public void updateComprobante(int id, int estadoSincronizacion){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR,estadoSincronizacion);

        mDb.update(SQLITE_TABLE_Comprob_Venta, initialValues,
                CV_id_comprob+"=?",new String[]{""+id});
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
        int cantidadRegistros = mDb.update(SQLITE_TABLE_Comprob_Venta, initialValues,
                CV_id_comprob+"= "+ signosInterrogacion,idComprobante);


        Log.d("REGISTROS EXPORTADOS ", ""+cantidadRegistros);
    }

    public void updateComprobanteMontos(long id, Double total, Double igv, Double base_imponible){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CV_total,total);
        initialValues.put(CV_total,igv);
        initialValues.put(CV_total,base_imponible);



        mDb.update(SQLITE_TABLE_Comprob_Venta, initialValues,
                CV_id_comprob+"=?",new String[]{""+id});
    }



    public Cursor fetchComprobVentaByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta,  new String[] {CV_id_comprob,

                            CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                            CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                            CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente
                    },
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Venta,new String[] {CV_id_comprob,

                            CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                            CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                            CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente
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
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente
                },
                Constants._SINCRONIZAR + " = " + Constants._CREADO + " OR " + Constants._SINCRONIZAR + " = " + Constants._ACTUALIZADO, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllComprobVenta() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta, new String[] {CV_id_comprob,

                        CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                        CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente
                },
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public Cursor fetchAllComprobVentaByEstable(String id) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta, new String[] {CV_id_comprob,

                        CV_id_establec, CV_id_tipo_doc, CV_id_forma_pago, CV_id_tipo_venta,
                        CV_codigo_erp, CV_serie, CV_num_doc, CV_base_imp, CV_igv, CV_total,
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente
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
                        CV_fecha_doc, CV_hora_doc, CV_estado_comp, CV_estado_conexion, CV_id_agente
                },
                CV_id_comprob + " = " + id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

/*
    public void insertSomeComprobVenta() {

        createComprobVenta( 1, 1, 1, 1, "0001", "1A", 1, 10, 0, 10, "14/01/2015", "08:10:00", 1, 0, 1);
        createComprobVenta( 2, 1, 1, 1, "0002", "2A", 2, 20, 0, 20, "14/01/2015", "08:10:00", 0, 0, 1);
        createComprobVenta( 3, 1, 1, 1, "0003", "3A", 3, 30, 0, 30, "14/01/2015", "08:10:00", 1, 0, 1);
        createComprobVenta( 4, 2, 2, 1, "0004", "4A", 4, 10, 0, 40, "14/01/2015", "08:10:00", 1, 0, 1);
        createComprobVenta( 5, 2, 2, 1, "0005", "5A", 5, 10, 0, 50, "14/01/2015", "08:10:00", 0, 0, 1);

    }*/

}