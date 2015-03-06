package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.ComprobanteVentaDetalle;

/**
 * Created by Usuario on 15/12/2014.
 */

public class DBAdapter_Temp_Autorizacion_Cobro {

    public static final String temp_autorizacion_cobro = "_id";

    public static final String temp_id_agente= "temp_id_Agente";
    public static final String temp_id_autorizacion_cobro= "temp_id_autorizacion_cobro";
    public static final String temp_id_motivo_solicitud = "temp_id_Motivo_Solicitud";
    public static final String temp_id_estado_solicitud = "temp_id_Estado_Solicitud";
    public static final String temp_referencia = "temp_Referencia";
    public static final String temp_montoCredito = "temp_MontoCredito";
    public static final String temp_establec = "temp_Establecimiento";
    public static final String temp_vigencia_credito = "temp_Vigencia_Credito";
    public static final String temp_fechaLimite = "temp_fecha_limite";
    public static final String estado_sincronizacion = "estado_sincronizacion";
    public static final String temp_id_comprobante = "estado_id_comprobante";
    public static final String TAG = "Temp_autorizacion_cobros";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    public static final String SQLITE_TABLE_Temp_Autorizacion_Cobro = "m_temp_autorizacion_cobro";
    private final Context mCtx;
    public static final String CREATE_TABLE_TEMP_Autorizacion_Cobro =
            "create table "+SQLITE_TABLE_Temp_Autorizacion_Cobro+"("
                    +temp_autorizacion_cobro+" integer primary key autoincrement,"
                    +temp_id_autorizacion_cobro+" integer ,"
                    +temp_id_agente+" integer,"
                    +temp_id_motivo_solicitud+" integer,"
                    +temp_id_estado_solicitud+" integer,"
                    +temp_referencia+" integer,"
                    +temp_montoCredito+" real,"
                    +temp_establec+" integer,"
                    +temp_id_comprobante+" integer,"
                    +temp_vigencia_credito+" real,"
                    +estado_sincronizacion+" integer ," +
                    temp_fechaLimite+" text );";

    public static final String DELETE_TABLE_AUTORIZACION_COBRO = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Temp_Autorizacion_Cobro;

    public DBAdapter_Temp_Autorizacion_Cobro(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter_Temp_Autorizacion_Cobro open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public Cursor consultarfecha(int idAutorizacion){
        Cursor c = mDb.rawQuery("select * from "+SQLITE_TABLE_Temp_Autorizacion_Cobro+" where "+temp_autorizacion_cobro+"='"+idAutorizacion+"'",null);

        return c;

    }


    public long createTempAutorizacionPago(int idAgente, int motivoSolicitud, int estadoSolicitud, int referencia, double aPagar,
            double pagado,int idEstablec,int sincronizacion, int idComprobante){

        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_id_agente,idAgente);
        initialValues.put(temp_establec,idEstablec);
        initialValues.put(temp_id_motivo_solicitud,motivoSolicitud);
        initialValues.put(temp_id_estado_solicitud,estadoSolicitud);
        initialValues.put(temp_referencia,referencia);
        initialValues.put(temp_montoCredito,aPagar);
        initialValues.put(temp_vigencia_credito,pagado);
        initialValues.put(estado_sincronizacion,sincronizacion);
        initialValues.put(temp_id_comprobante,idComprobante);
        initialValues.put(temp_id_autorizacion_cobro,incrementable());
        return mDb.insert(SQLITE_TABLE_Temp_Autorizacion_Cobro, null, initialValues);
    }
    private int incrementable (){
        Cursor cr = mDb.rawQuery("select * from "+SQLITE_TABLE_Temp_Autorizacion_Cobro+";",null);

        return cr.getCount()+1;
    }
    public Cursor listarAutorizaciones(int idEstablec){

        Cursor cr = mDb.rawQuery("Select * from "+SQLITE_TABLE_Temp_Autorizacion_Cobro+" where "+temp_establec+"='"+idEstablec+"';",null);

        return cr;
    }
    public Cursor filterExport() {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Temp_Autorizacion_Cobro, new String[] {
                        temp_autorizacion_cobro,temp_id_agente,
                        temp_establec, temp_id_motivo_solicitud, temp_id_estado_solicitud,
                        temp_referencia, temp_montoCredito, temp_vigencia_credito, estado_sincronizacion, temp_id_comprobante, temp_id_autorizacion_cobro
                },
                Constants._SINCRONIZAR + " = " + Constants._CREADO + " AND " + temp_id_estado_solicitud + " = '1'" , null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean existeAutorizacionCobro(int idAutorizacionCobro) {
        boolean exists = false;
        Cursor mCursor = mDb.query(true, SQLITE_TABLE_Temp_Autorizacion_Cobro, new String[] {
                        temp_autorizacion_cobro,temp_id_agente,
                        temp_establec, temp_id_motivo_solicitud, temp_id_estado_solicitud,
                        temp_referencia, temp_montoCredito, temp_vigencia_credito, estado_sincronizacion, temp_id_comprobante,temp_fechaLimite
                },
                 temp_id_autorizacion_cobro+ " = " + idAutorizacionCobro, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            exists = true;
        }
        if (mCursor.getCount()==0){
            exists = false;
        }
        Log.d("EXISTE AUTORIZACION COBRO ", ""+exists);
        return exists;
    }

    public boolean updateAutorizacionAprobado(String id,String idDetalleCobro){
        DbAdapter_Comprob_Venta_Detalle adapterVenta= new DbAdapter_Comprob_Venta_Detalle(mCtx);
        adapterVenta.open();
        boolean estado = false;
        try {
            mDb.execSQL(" update "+SQLITE_TABLE_Temp_Autorizacion_Cobro+" set "+temp_id_estado_solicitud+"='5',"+Constants._SINCRONIZAR+"="+Constants._ACTUALIZADO+" where "+temp_autorizacion_cobro+"='"+id+"'; ");
            mDb.execSQL(" update "+DbAdapter_Comprob_Cobro.SQLITE_TABLE_Comprob_Cobro+" set "+DbAdapter_Comprob_Cobro.CC_estado_cobro+"='1',"+Constants._SINCRONIZAR+"="+Constants._CREADO+" where "+DbAdapter_Comprob_Cobro.CC_id_cob_historial+"='"+idDetalleCobro+"'; ");
            Cursor cursor = mDb.rawQuery("select * from "+DbAdapter_Comprob_Cobro.SQLITE_TABLE_Comprob_Cobro+" where "+DbAdapter_Comprob_Cobro.CC_id_cob_historial+"='"+idDetalleCobro+"'; ",null);
            cursor.moveToFirst();
           // adapterVenta.createComprobVentaDetalle(intidComprobante,intidProducto,StringnombreProducto,intcantidad,intimporte,doublecostoVenta,doubleprecioUnitario,"","",0);
            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }
        public int  updateAutorizacionCobro(int idAutorizacionCobro, int estadoSolicitud, int idEstablecimiento,String fechaLimite){

        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_id_estado_solicitud, estadoSolicitud );
        initialValues.put(Constants._SINCRONIZAR,Constants._IMPORTADO);
        initialValues.put(temp_fechaLimite,fechaLimite);
        mDb.execSQL("update "+DbAdapter_Comprob_Cobro.SQLITE_TABLE_Comprob_Cobro+" set "+DbAdapter_Comprob_Cobro.CC_estado_prologa+"='2' where "+DbAdapter_Comprob_Cobro.CC_id_autorizacion+"='"+idAutorizacionCobro+"'");

        return mDb.update(SQLITE_TABLE_Temp_Autorizacion_Cobro, initialValues,
                temp_id_autorizacion_cobro+"=? AND " + temp_establec + " = ?",new String[]{""+idAutorizacionCobro, ""+idEstablecimiento});

    }
    public int  updateAutorizacionCobro_Au(int idAutorizacionCobro, int estadoSolicitud, int idEstablecimiento,String fechaLimite){

        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_id_estado_solicitud, estadoSolicitud );
        initialValues.put(Constants._SINCRONIZAR,Constants._ACTUALIZADO);
        initialValues.put(temp_fechaLimite,fechaLimite);

        return mDb.update(SQLITE_TABLE_Temp_Autorizacion_Cobro, initialValues,
                temp_id_autorizacion_cobro+"=? AND " + temp_establec + " = ?",new String[]{""+idAutorizacionCobro, ""+idEstablecimiento});

    }
    private String fechaFormat (String fecha){

        SimpleDateFormat sm = new SimpleDateFormat("MM-dd-yyyy");

        String strDate = sm.format(fecha);
        return strDate;
    }

    public void changeEstadoToExport(String[] id, int estadoSincronizacion){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR,estadoSincronizacion);

        String signosInterrogacion = "";
        for (int i=0; i<id.length; i++){
            if (i==id.length-1)
            {
                signosInterrogacion+= "?";
            }else {
                signosInterrogacion+= "? OR ";
            }

        }

        Log.d("SIGNOS INTERROGACIÓN", signosInterrogacion);
        int cantidadRegistros = mDb.update(SQLITE_TABLE_Temp_Autorizacion_Cobro, initialValues,
                temp_autorizacion_cobro+"= "+ signosInterrogacion,id);


        Log.d("REGISTROS EXPORTADOS ", ""+cantidadRegistros);
    }
    public boolean anularAutorizacion(String id,String idDetalleCobro, String idComprobante){
        boolean estado = false;
        try {
            mDb.execSQL(" update "+SQLITE_TABLE_Temp_Autorizacion_Cobro+" set "+temp_id_estado_solicitud+"='4',"+Constants._SINCRONIZAR+"="+Constants._ACTUALIZADO+" where "+temp_autorizacion_cobro+"='"+id+"'; ");
            mDb.execSQL(" update "+DbAdapter_Comprob_Cobro.SQLITE_TABLE_Comprob_Cobro+" set "+DbAdapter_Comprob_Cobro.CC_estado_cobro+"='2',"+Constants._SINCRONIZAR+"="+Constants._CREADO+" where "+DbAdapter_Comprob_Cobro.CC_id_cob_historial+"='"+idDetalleCobro+"'; ");
            mDb.execSQL(" update "+DbAdapter_Comprob_Cobro.SQLITE_TABLE_Comprob_Cobro+" set "+DbAdapter_Comprob_Cobro.CC_estado_cobro+"='1',"+DbAdapter_Comprob_Cobro.CC_monto_a_pagar+"='"+calcularPago(idComprobante)+"', "+DbAdapter_Comprob_Cobro.CC_monto_cobrado+"='0.0',"+Constants._SINCRONIZAR+"="+Constants._CREADO+" where "+DbAdapter_Comprob_Cobro.CC_id_cob_historial+"='"+idComprobante+"'; ");
            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }
    private Double calcularPago(String idComprobante){
        double pago=0.0;
        Cursor cr = mDb.rawQuery("select * from "+DbAdapter_Comprob_Cobro.SQLITE_TABLE_Comprob_Cobro+" where  "+DbAdapter_Comprob_Cobro.CC_id_cob_historial+"='"+idComprobante+"'",null);
        cr.moveToFirst();
        pago=cr.getDouble(cr.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_monto_a_pagar))+cr.getDouble(cr.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_monto_cobrado));
        return pago;
    }


}
