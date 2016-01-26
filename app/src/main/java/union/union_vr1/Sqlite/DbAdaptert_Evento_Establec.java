package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.EventoEstablecimiento;

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
    public static final String EE_dias_credito = "ee_in_dias_credito";
    public static final String EE_id_agente = "ee_in_id_agente";
    public static final String EE_id_liquidacion = "ee_in_id_liquidacion";
    public static final String estado_sincronizacion = "estado_sincronizacion";
    public static final String EE_codigo_barras = "ee_in_codigo_barras";
    public static final String EE_direccion = "ee_in_direccion";
    public static final String EE_direccion_principal = "ee_in_direccion_principal";
    public static final String EE_time_atencion = "ee_in_time_atencion";
    public static final String EE_Latitud = "ee_in_latitud";
    public static final String EE_Longitud = "ee_in_longitud";
    public static  final String EE_id_actualizar = "EE_id_actualizar";
    //Estado creado
    public static  final String EE_estado_autorizado = "ee_in_estado_autorizado";


    //FALTA OBTENER ESTE CAMPO
    public static final String EE_monto_credito = "ee_re_monto_credito";

    //ESTOS CAMPOS LOS RELLENAMOS NOSOTROS[ACTUALIZAMOS]
    public static final String EE_id_estado_no_atencion = "ee_in_id_estado_no_atencion";
    public static final String EE_estado_no_atencion_comentario ="ee_in_estado_no_atencion_comentario";


    //OBTIENE DE MÁS 2 CAMPOS[SON LOS PARÁMETROS PARA OBTENER LOS ESTABLECIMIENTOS POR RUTA]
    //CAJA LIQUIDACIÓN
    //FECHA

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
                    +EE_codigo_barras+" text,"
                    +EE_direccion+" text,"
                    +EE_direccion_principal+" text,"
                    +EE_nom_cliente+" text,"
                    +EE_time_atencion+" text,"
                    +EE_doc_cliente+" text,"
                    +EE_orden+" integer,"
                    +EE_surtido_stock_ant+" integer,"
                    +EE_surtido_venta_ant+" integer,"
                    +EE_monto_credito+" real,"
                    +EE_dias_credito+" integer,"
                    +EE_id_estado_no_atencion+" integer,"
                    +EE_estado_no_atencion_comentario+" text,"
                    +EE_id_agente+" integer, "
                    +EE_id_liquidacion+" integer, "
                    +EE_Latitud+" text, "//lat
                    +EE_Longitud+" text, "//long
                    +EE_estado_autorizado+" integer, "
                    +EE_id_actualizar+" integer, " +
                    estado_sincronizacion+" integer);";

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
            int estado_no_atencion, int id_agente, int id_liquidacion) {

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
        initialValues.put(EE_id_estado_no_atencion,estado_no_atencion);
        initialValues.put(EE_id_agente,id_agente);
        initialValues.put(EE_id_liquidacion,id_liquidacion);


        return mDb.insert(SQLITE_TABLE_Evento_Establec, null, initialValues);
    }

    public long createEstablecimientos(EventoEstablecimiento establecimiento, int id_agente, int id_liquidacion,int idEditar) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_establec, establecimiento.getIdEstablecimiento());
        initialValues.put(EE_id_cat_est,establecimiento.getIdCategoriaEstablecimiento());
        initialValues.put(EE_id_tipo_doc_cliente,establecimiento.getTipoDocCliente());
        initialValues.put(EE_id_estado_atencion, establecimiento.getEstadoAtencion());
        initialValues.put(EE_nom_establec,establecimiento.getNombreEstablecimiento());
        initialValues.put(EE_nom_cliente,establecimiento.getNombreCliente());
        initialValues.put(EE_doc_cliente,establecimiento.getDocCliente());
        initialValues.put(EE_orden,establecimiento.getOrden());
        initialValues.put(EE_surtido_stock_ant,establecimiento.getSurtidoStockAnterior());
        initialValues.put(EE_surtido_venta_ant,establecimiento.getSurtidoVentaAnterior());
        initialValues.put(EE_monto_credito,establecimiento.getMontoCredito());
        initialValues.put(EE_dias_credito,establecimiento.getDiasCredito());
        initialValues.put(EE_id_estado_no_atencion,establecimiento.getIdEstadoNoAtencion());
        initialValues.put(EE_id_agente,id_agente);
        initialValues.put(EE_id_liquidacion,id_liquidacion);
        initialValues.put(EE_codigo_barras,establecimiento.getCodigoBarras());
        initialValues.put(EE_direccion,establecimiento.getDireccion());
        initialValues.put(EE_direccion_principal, establecimiento.getDireccionPrincipal());
        initialValues.put(EE_estado_autorizado,establecimiento.getEstadoAutorizado());
        initialValues.put(EE_Latitud,establecimiento.getLatitud());
        initialValues.put(EE_Longitud,establecimiento.getLongitud());
        initialValues.put(EE_id_actualizar,idEditar);
        return mDb.insert(SQLITE_TABLE_Evento_Establec, null, initialValues);
    }
    public int updateEstablecimientosEditar(EventoEstablecimiento establecimiento){

        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_cat_est,establecimiento.getIdCategoriaEstablecimiento());
        initialValues.put(EE_id_tipo_doc_cliente,establecimiento.getTipoDocCliente());
        initialValues.put(EE_nom_establec,establecimiento.getNombreEstablecimiento());
        initialValues.put(EE_nom_cliente,establecimiento.getNombreCliente());
        initialValues.put(EE_doc_cliente,establecimiento.getDocCliente());
        initialValues.put(EE_direccion,establecimiento.getDireccion());
        initialValues.put(EE_direccion_principal,establecimiento.getDireccionPrincipal());

        initialValues.put(EE_Latitud,establecimiento.getLatitud());
        initialValues.put(EE_Longitud, establecimiento.getLongitud());


        return mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_establec + "=?", new String[]{"" + establecimiento.getIdEstablecimiento()});
    }
    public void updateEstablecimientos(EventoEstablecimiento establecimiento, int id_agente, int id_liquidacion){

        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_establec,establecimiento.getIdEstablecimiento());
        initialValues.put(EE_id_cat_est,establecimiento.getIdCategoriaEstablecimiento());
        initialValues.put(EE_id_tipo_doc_cliente,establecimiento.getTipoDocCliente());
        initialValues.put(EE_id_estado_atencion, establecimiento.getEstadoAtencion());
        initialValues.put(EE_nom_establec,establecimiento.getNombreEstablecimiento());
        initialValues.put(EE_nom_cliente,establecimiento.getNombreCliente());
        initialValues.put(EE_doc_cliente,establecimiento.getDocCliente());
        initialValues.put(EE_orden,establecimiento.getOrden());
        initialValues.put(EE_surtido_stock_ant,establecimiento.getSurtidoStockAnterior());
        initialValues.put(EE_surtido_venta_ant,establecimiento.getSurtidoVentaAnterior());
        initialValues.put(EE_monto_credito,establecimiento.getMontoCredito());
        initialValues.put(EE_dias_credito,establecimiento.getDiasCredito());
        initialValues.put(EE_id_estado_no_atencion,establecimiento.getIdEstadoNoAtencion());
        initialValues.put(EE_id_agente,id_agente);
        initialValues.put(EE_id_liquidacion,id_liquidacion);
        initialValues.put(EE_codigo_barras,establecimiento.getCodigoBarras());
        initialValues.put(EE_direccion,establecimiento.getDireccion());
        initialValues.put(EE_direccion_principal,establecimiento.getDireccionPrincipal());
        initialValues.put(EE_estado_autorizado,establecimiento.getEstadoAutorizado());

        initialValues.put(EE_Latitud,establecimiento.getLatitud());
        initialValues.put(EE_Longitud,establecimiento.getLongitud());


        mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_establec + "=?", new String[]{"" + establecimiento.getIdEstablecimiento()});
    }
    public boolean existeEstablecsById(int idEstablec) throws SQLException {
        boolean exists = false;
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                        EE_id_establec, EE_id_cat_est, EE_nom_establec, EE_nom_cliente, EE_orden,
                        EE_id_estado_atencion,EE_monto_credito,EE_dias_credito, EE_time_atencion, EE_direccion_principal},
                EE_id_establec + " = " + idEstablec, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            exists = true;
        }
        if (mCursor.getCount()==0){
            exists= false;
        }
        return exists;

    }
    public void updateEstadoNoAtendido(String id, int estado, int estadoNoAtendido, String comentario, String time){
        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_estado_atencion, estado);
        initialValues.put(EE_id_estado_no_atencion, estadoNoAtendido);
        initialValues.put(EE_estado_no_atencion_comentario, comentario);
        initialValues.put(EE_time_atencion, time);
        initialValues.put(Constants._SINCRONIZAR, Constants._ACTUALIZADO);

        mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_establec+"=?",new String[]{id});
    }
    public void updateEstadoEstablecs(String id, int estado, String time){
        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_estado_atencion,estado);
        initialValues.put(EE_time_atencion, time);
        initialValues.put(Constants._SINCRONIZAR, Constants._ACTUALIZADO);

        mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_establec+"=?",new String[]{id});
    }
    public void updateEstablecs(String id, int estado){
        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_estado_atencion,estado);

        mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_establec+"=?",new String[]{id});
    }
    public int updateEstablecsCredito(int idEstablecimiento, Double montoCredito, int diasCredito){

        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_monto_credito, montoCredito);
        initialValues.put(EE_dias_credito, diasCredito);
        initialValues.put(Constants._SINCRONIZAR, Constants._ACTUALIZADO);

        return mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_establec+"=?",new String[]{""+idEstablecimiento});
    }

    public int updateEstablecsEstadoIdUpd(int idEstablecimiento){

        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_estado_autorizado, "6");
        return mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_establec+"=?",new String[]{""+idEstablecimiento});
    }


    public int updateEstablecsEstadoId(int idEstablecimiento){

        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_estado_autorizado, "6");
        return mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_establec+"=?",new String[]{""+idEstablecimiento});
    }

    public int updateEstabLatLong(int idEstablecimiento,String lat , String lon){

        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_Latitud, lat);
        initialValues.put(EE_Longitud, lon);
        return mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_establec+"=?",new String[]{""+idEstablecimiento});
    }

    public int updateEstaIdRemoto(int idEstablecimiento,String id){

        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_establec, id);
        return mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_actualizar+"=?",new String[]{""+idEstablecimiento});
    }

    public int updateEstabEstado(int idEstablecimiento,String estado){

        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_estado_autorizado, estado);

        return mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_actualizar+"=?",new String[]{""+idEstablecimiento});
    }

    public int updateEstabEstadoAtencion(int idEstablecimiento, int estadoAtencion){

        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_estado_atencion,estadoAtencion );
        return mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_actualizar+"=?",new String[]{""+idEstablecimiento});
    }

    public int updateIDSIDEstablecimiento(int idEstablecimientoint, int id_SID_establecimiento){

        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_establec, id_SID_establecimiento);

        return mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_actualizar+"=?",new String[]{""+idEstablecimientoint});
    }



    public int ordern (){
        Cursor cr = mDb.rawQuery("select max("+EE_orden+")+1 as 'ORDEN' from "+SQLITE_TABLE_Evento_Establec+"", null);
        cr.moveToFirst();
        return  cr.getInt(cr.getColumnIndexOrThrow("ORDEN"));
    }



    public void changeEstadoToExport(String[] ids, int estadoSincronizacion){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR,estadoSincronizacion);

        String signosInterrogacion = "";
        for (int i=0; i<ids.length; i++){
            if (i==ids.length-1)
            {
                signosInterrogacion+= "?";
            }else {
                signosInterrogacion+= "? OR ";
            }

        }

        Log.d("SIGNOS INTERROGACIÓN", signosInterrogacion);
        int cantidadRegistros = mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_evt_establec + "= " + signosInterrogacion, ids);


        Log.d("REGISTROS ACTUALIZADO ", "" + cantidadRegistros);
    }


    public void updateEstablecs1(String id, int aten, int naten){
        ContentValues initialValues = new ContentValues();
        initialValues.put(EE_id_estado_atencion,aten);
        initialValues.put(EE_id_estado_no_atencion, naten);
        mDb.update(SQLITE_TABLE_Evento_Establec, initialValues,
                EE_id_establec + "=?", new String[]{id});
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
                            EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_id_estado_atencion,
                            EE_id_agente, EE_time_atencion},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                            EE_id_establec, EE_id_cat_est, EE_id_tipo_doc_cliente, EE_id_estado_atencion,
                            EE_nom_establec, EE_nom_cliente, EE_doc_cliente, EE_orden, EE_surtido_stock_ant,
                            EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_id_estado_no_atencion,
                            EE_id_agente, EE_time_atencion},
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
                        EE_id_establec, EE_id_cat_est, EE_id_tipo_doc_cliente, EE_id_estado_atencion,
                        EE_nom_establec, EE_nom_cliente, EE_doc_cliente, EE_orden, EE_surtido_stock_ant,
                        EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_id_estado_no_atencion,
                        EE_id_agente, EE_time_atencion, EE_direccion_principal},
                EE_id_establec + " = " + inputText, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    public Cursor fetchEstablecsByBarcode(String barcode, int liquidacion) throws SQLException {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                        EE_id_establec, EE_id_cat_est, EE_id_tipo_doc_cliente, EE_id_estado_atencion,
                        EE_nom_establec, EE_nom_cliente, EE_doc_cliente, EE_orden, EE_surtido_stock_ant,
                        EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_id_estado_no_atencion,
                        EE_id_agente,EE_codigo_barras, EE_time_atencion, EE_direccion_principal},
                EE_codigo_barras + " LIKE '%" + barcode+"%' AND "+EE_id_liquidacion + " = " +liquidacion, null,
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
                        EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_id_agente, EE_time_atencion},
                EE_id_establec + " = " + inputText, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor fectchLiq(int liqui){
        return mDb.rawQuery("select * from "+SQLITE_TABLE_Evento_Establec+"  where "+EE_id_liquidacion+"='"+liqui+"'",null);
    }
    public int getIdParent(String documento){
        Cursor cursor = mDb.rawQuery("select * from "+SQLITE_TABLE_Evento_Establec+"  where "+EE_doc_cliente+"='"+documento+"'",null);
cursor.moveToFirst();

        return cursor.getInt(cursor.getColumnIndexOrThrow(EE_id_evt_establec));
    }

    public Cursor fetchAllEstablecs() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                        EE_id_establec, EE_id_cat_est, EE_id_tipo_doc_cliente, EE_id_estado_atencion, EE_id_estado_no_atencion,
                        EE_nom_establec, EE_nom_cliente, EE_doc_cliente, EE_orden, EE_surtido_stock_ant,
                        EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_id_agente, EE_time_atencion, EE_direccion_principal},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public String getNameCliente(int establec){
        Cursor cursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Evento_Establec + " where " + EE_id_establec + "='" + establec + "'", null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndexOrThrow(EE_nom_cliente));
    }

    public Cursor filterExportUpdated() {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                        EE_id_establec, EE_id_cat_est, EE_id_tipo_doc_cliente, EE_id_estado_atencion, EE_id_estado_no_atencion,
                        EE_nom_establec, EE_nom_cliente, EE_doc_cliente, EE_orden, EE_surtido_stock_ant,
                        EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_id_agente, EE_time_atencion, EE_direccion_principal},
                Constants._SINCRONIZAR + " = " + Constants._ACTUALIZADO, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllEstablecsX() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                        EE_id_establec, EE_id_cat_est, EE_id_tipo_doc_cliente, EE_id_estado_atencion,
                        EE_nom_establec, EE_nom_cliente, EE_doc_cliente, EE_orden, EE_surtido_stock_ant,
                        EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_id_agente, EE_time_atencion},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public Cursor
    fetchAllEstablecsXX() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Evento_Establec, new String[] {EE_id_evt_establec,
                        EE_id_establec, EE_id_cat_est, EE_id_tipo_doc_cliente, EE_id_estado_atencion,
                        EE_nom_establec, EE_nom_cliente, EE_doc_cliente, EE_orden, EE_surtido_stock_ant,
                        EE_surtido_venta_ant, EE_monto_credito, EE_dias_credito, EE_id_estado_no_atencion,
                        EE_id_agente, EE_direccion_principal},
                null, null, null, null,EE_id_estado_atencion + ", " + EE_orden);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor listarEstablecimientos(int idLiquidacion){
        Cursor mCursor = mDb.rawQuery("SELECT ee.*,  SUM(cc.cc_re_monto_a_pagar) as cc_re_monto_a_pagar\n" +
                "FROM m_evento_establec ee\n" +
                "LEFT OUTER JOIN m_comprob_cobro cc\n" +
                "ON ee.ee_in_id_establec = cc.cc_in_id_establec \n" +
                "WHERE cc_in_estado_cobro = 1 \n" +
                "AND ee_in_id_liquidacion = '" + idLiquidacion + "' \n" +
                "GROUP BY  ee.ee_in_id_establec, cc_in_estado_cobro\n" +
                "UNION \n" +
                "SELECT ee.*,  SUM(cc.cc_re_monto_a_pagar) as cc_re_monto_a_pagar\n" +
                "FROM m_evento_establec ee\n" +
                "LEFT OUTER JOIN m_comprob_cobro cc\n" +
                "ON ee.ee_in_id_establec = cc.cc_in_id_establec \n" +
                "WHERE  ee_in_id_liquidacion = '" + idLiquidacion + "' AND cc_in_estado_cobro IS NULL\n" +
                "GROUP BY  ee.ee_in_id_establec, cc_in_estado_cobro\n" +
                "UNION\n" +
                "select ee.*,  SUM(0) as cc_re_monto_a_pagar\n" +
                "from m_comprob_cobro cc,\n" +
                "m_evento_establec ee \n" +
                "where ee.ee_in_id_establec = cc.cc_in_id_establec \n" +
                "AND ee_in_id_liquidacion = '" + idLiquidacion + "'\n" +
                "GROUP BY  ee.ee_in_id_establec \n" +
                "having sum(cc.cc_in_estado_cobro)=0 " +
                "ORDER BY ee_in_id_estado_atencion ASC", null);
        return mCursor;
    }

    public Cursor listarEstablecimientosByID(int idEstablecimiento, int idLiquidacion){
        Cursor mCursor = mDb.rawQuery("SELECT *,  (SELECT SUM(cc_re_monto_a_pagar) AS cc_re_monto_a_pagar \n" +
                "FROM m_comprob_cobro \n" +
                "WHERE cc_in_id_establec = '"+idEstablecimiento+"' \n" +
                "AND cc_in_estado_cobro = 1  ) AS cc_re_monto_a_pagar \n" +
                "FROM m_evento_establec \n" +
                "WHERE ee_in_id_establec = '"+idEstablecimiento+"' \n" +
                "AND ee_in_id_liquidacion = '"+idLiquidacion+"' ;",null);
        return mCursor;
    }

    public Cursor listarEstablecimientosPorNombre(String nombreCliente, int idLiquidacion){
        Cursor mCursor = mDb.rawQuery("SELECT ee.*,  SUM(cc.cc_re_monto_a_pagar) as cc_re_monto_a_pagar\n" +
                "FROM m_evento_establec ee\n" +
                "LEFT OUTER JOIN m_comprob_cobro cc\n" +
                "ON ee.ee_in_id_establec = cc.cc_in_id_establec \n" +
                "WHERE ee_te_nom_cliente LIKE '%"+nombreCliente+"%'   \n" +
                "AND ee_in_id_liquidacion = '"+idLiquidacion+"' AND cc_in_estado_cobro = 1\n" +
                "GROUP BY  ee.ee_in_id_establec\n" +
                "UNION\n" +
                "SELECT ee.*,  SUM(cc.cc_re_monto_a_pagar) as cc_re_monto_a_pagar\n" +
                "FROM m_evento_establec ee\n" +
                "LEFT OUTER JOIN m_comprob_cobro cc\n" +
                "ON ee.ee_in_id_establec = cc.cc_in_id_establec \n" +
                "WHERE ee_te_nom_cliente LIKE '%"+nombreCliente+"%'   \n" +
                "AND ee_in_id_liquidacion = '"+idLiquidacion+"' AND cc_in_estado_cobro IS NULL\n" +
                "GROUP BY  ee.ee_in_id_establec\n" +
                "UNION\n" +
                "select ee.*,sum(0) as  cc_re_monto_a_pagar\n" +
                "from m_comprob_cobro cc,m_evento_establec ee \n" +
                "where ee.ee_in_id_establec = cc.cc_in_id_establec \n" +
                "AND ee_te_nom_cliente LIKE '%"+nombreCliente+"%'   \n" +
                "AND ee_in_id_liquidacion = '"+idLiquidacion+"'\n" +
                "GROUP BY  ee.ee_in_id_establec \n" +
                "having sum(cc.cc_in_estado_cobro)=0 " +
                "ORDER BY ee_in_id_estado_atencion ASC",null);

        return mCursor;
    }

    public boolean getLatLongStatus(int establecimiento){
        boolean estado=true;
        Cursor cr = mDb.rawQuery("select * from "+SQLITE_TABLE_Evento_Establec+" where "+EE_id_establec+" = '"+establecimiento+"' ",null);
        while(cr.moveToFirst()){
            String lat,lon;
            lat = cr.getString(cr.getColumnIndexOrThrow(EE_Latitud));
            lon = cr.getString(cr.getColumnIndexOrThrow(EE_Longitud));

            Log.d("EXPORTLATLONG",""+lat+"--"+lon);

            if(lat.equals("") || lon.equals("") || lat ==null || lon ==null){
                estado = false;
            }
        }
        return estado;
    }


}