package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.Agente;

public class DbAdapter_Agente {

    //LOS PROCEDIMIENTOS TRAEN ESTOS CAMPOS
    public static final String AG_id_agente = "_id";
    public static final String AG_id_agente_venta = "ag_in_id_agente_venta";
    public static final String AG_id_empresa = "ag_in_id_empresa";
    public static final String AG_id_usuario = "ag_in_id_usuario";
    public static final String AG_nombre_agente = "ag_te_nombre_agente";
    public static final String AG_nom_usuario = "ag_te_nom_usuario";
    public static final String AG_pass_usuario = "ag_te_pass_usuario";
    public static final String AG_liquidacion = "ag_in_liquidacion";
    public static final String AG_km_inicial = "ag_re_km_inicial";
    public static final String AG_km_final = "ag_re_km_final";
    public static final String AG_nombre_ruta = "ag_te_nombre_ruta";
    public static final String AG_nro_bodegas = "ag_in_nro_bodegas";
    public static final String AG_serie_boleta = "ag_te_serie_boleta";
    public static final String AG_serie_factura = "ag_te_serie_factura";
    public static final String AG_correlativo_boleta = "ag_in_correlativo_boleta";
    public static final String AG_correlativo_factura = "ag_in_correlativo_factura";
    public static final String estado_sincronizacion = "estado_sincronizacion";
    public static final String AG_fecha = "fecha";

    //FALTA TRAER ESTOS CAMPOS
    public static final String AG_correlativo_rrpp = "ag_in_correlativo_rrpp";
    public static final String AG_serie_rrpp = "ag_te_serie_rrpp";



    public static final String TAG = "Agente";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "ProdUnion.sqlite";
    private static final String SQLITE_TABLE_Agente = "m_agente";
    private final Context mCtx;

    public static final String CREATE_TABLE_AGENTE =
            "create table if not exists "+SQLITE_TABLE_Agente+" ("
                    +AG_id_agente+" integer primary key autoincrement,"
                    +AG_id_agente_venta+" integer,"
                    +AG_id_empresa+" integer,"
                    +AG_id_usuario+" integer,"
                    +AG_nombre_agente+" text,"
                    +AG_nom_usuario+" text,"
                    +AG_pass_usuario+" text,"
                    +AG_liquidacion+" integer,"
                    +AG_km_inicial+" real,"
                    +AG_km_final+" real,"
                    +AG_nombre_ruta+" text,"
                    +AG_nro_bodegas+" integer,"
                    +AG_serie_boleta+" text,"
                    +AG_serie_factura+" text,"
                    +AG_serie_rrpp+" text,"
                    +AG_correlativo_boleta+" integer,"
                    +AG_correlativo_factura+" integer,"
                    +AG_correlativo_rrpp+" integer, "
                    +AG_fecha+" text, "
                    +estado_sincronizacion+ " integer);";

    public static final String DELETE_TABLE_AGENTE = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Agente;

    public DbAdapter_Agente(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Agente open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createAgentes(
            int id_agente_venta, int id_empresa, int id_usuario,
            String nombre_agente, String nom_usuario, String pass_usuario, int liquidacion,
            double km_inicial, double km_final, String nombre_ruta, int nro_bodegas,
            String serie_boleta, String serie_factura, String serie_rrpp, int correlativo_boleta,
            int correlativo_factura, int correlativo_rrpp, String fecha) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(AG_id_agente_venta,id_agente_venta);
        initialValues.put(AG_id_empresa,id_empresa);
        initialValues.put(AG_id_usuario,id_usuario);
        initialValues.put(AG_nombre_agente,nombre_agente);
        initialValues.put(AG_nom_usuario,nom_usuario);
        initialValues.put(AG_pass_usuario,pass_usuario);
        initialValues.put(AG_liquidacion,liquidacion);
        initialValues.put(AG_km_inicial,km_inicial);
        initialValues.put(AG_km_final,km_final);
        initialValues.put(AG_nombre_ruta,nombre_ruta);
        initialValues.put(AG_nro_bodegas,nro_bodegas);
        initialValues.put(AG_serie_boleta,serie_boleta);
        initialValues.put(AG_serie_factura,serie_factura);
        initialValues.put(AG_serie_rrpp,serie_rrpp);
        initialValues.put(AG_correlativo_boleta,correlativo_boleta);
        initialValues.put(AG_correlativo_factura,correlativo_factura);
        initialValues.put(AG_correlativo_rrpp,correlativo_rrpp);
        initialValues.put(AG_fecha, fecha);

        return mDb.insert(SQLITE_TABLE_Agente, null, initialValues);
    }
    public long createAgente(Agente agente, String fecha) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(AG_id_agente_venta,agente.getIdAgenteVenta());
        initialValues.put(AG_id_empresa,agente.getIdEmpresa());
        initialValues.put(AG_id_usuario,agente.getIdUsuario());
        initialValues.put(AG_nombre_agente,agente.getNombreAgente());
        initialValues.put(AG_nom_usuario,agente.getNombreUsuario());
        initialValues.put(AG_pass_usuario,agente.getPassUsuario());
        initialValues.put(AG_liquidacion,agente.getLiquidacion());
        initialValues.put(AG_km_inicial,agente.getKmInicial());
        initialValues.put(AG_km_final,agente.getKmFinal());
        initialValues.put(AG_nombre_ruta,agente.getNombreRuta());
        initialValues.put(AG_nro_bodegas,agente.getNroBodegas());
        initialValues.put(AG_serie_boleta,agente.getSerieBoleta());
        initialValues.put(AG_serie_factura,agente.getSerieFactura());
        initialValues.put(AG_serie_rrpp, agente.getSerieRrpp());
        initialValues.put(AG_correlativo_boleta,agente.getCorrelativoBoleta());
        initialValues.put(AG_correlativo_factura,agente.getCorrelativoFactura());
        initialValues.put(AG_correlativo_rrpp, agente.getCorrelativoRrpp());
        initialValues.put(AG_correlativo_rrpp, agente.getCorrelativoRrpp());
        initialValues.put(AG_fecha, fecha);


        return mDb.insert(SQLITE_TABLE_Agente, null, initialValues);
    }


    public void updateAgente(Agente agente, String fecha){

        ContentValues initialValues = new ContentValues();
        initialValues.put(AG_id_agente_venta,agente.getIdAgenteVenta());
        initialValues.put(AG_id_empresa,agente.getIdEmpresa());
        initialValues.put(AG_id_usuario,agente.getIdUsuario());
        initialValues.put(AG_nombre_agente,agente.getNombreAgente());
        initialValues.put(AG_nom_usuario,agente.getNombreUsuario());
        initialValues.put(AG_pass_usuario,agente.getPassUsuario());
        initialValues.put(AG_liquidacion,agente.getLiquidacion());
        initialValues.put(AG_km_inicial,agente.getKmInicial());
        initialValues.put(AG_km_final,agente.getKmFinal());
        initialValues.put(AG_nombre_ruta,agente.getNombreRuta());
        initialValues.put(AG_nro_bodegas,agente.getNroBodegas());
        initialValues.put(AG_serie_boleta,agente.getSerieBoleta());
        initialValues.put(AG_serie_factura,agente.getSerieFactura());
        initialValues.put(AG_serie_rrpp, agente.getSerieRrpp());
        initialValues.put(AG_correlativo_boleta,agente.getCorrelativoBoleta());
        initialValues.put(AG_correlativo_factura,agente.getCorrelativoFactura());
        initialValues.put(AG_correlativo_rrpp, agente.getCorrelativoRrpp());
        initialValues.put(AG_fecha, fecha);

        mDb.update(SQLITE_TABLE_Agente, initialValues,
                AG_id_agente_venta+"=?",new String[]{""+agente.getIdAgenteVenta()});
    }
    public boolean existeAgentesById(int idAgenteVenta) throws SQLException {
        boolean exists = false;
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Agente, new String[] {AG_id_agente,
                        AG_id_agente_venta, AG_nombre_agente, AG_nombre_ruta, AG_nro_bodegas, AG_fecha},
                AG_id_agente_venta + " = " + idAgenteVenta, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            exists = true;
        }

        if (mCursor.getCount()==0){
            exists=false;
        }
        return exists;

    }
    public void updateAgentesBO(String idagen, String seribo, String corrbo){
        ContentValues initialValues = new ContentValues();
        initialValues.put(AG_serie_boleta,seribo);
        initialValues.put(AG_correlativo_boleta,corrbo);
        mDb.update(SQLITE_TABLE_Agente, initialValues,
                AG_id_agente_venta+"=?",new String[]{idagen});
    }

    public void updateAgentesFA(String idagen, String serifa, String corrfa){
        ContentValues initialValues = new ContentValues();
        initialValues.put(AG_serie_factura,serifa);
        initialValues.put(AG_correlativo_factura,corrfa);
        mDb.update(SQLITE_TABLE_Agente, initialValues,
                AG_id_agente_venta+"=?",new String[]{idagen});
    }

    public void updateAgentesRP(String idagen, String serirp, String corrrp){
        ContentValues initialValues = new ContentValues();
        initialValues.put(AG_serie_rrpp,serirp);
        initialValues.put(AG_correlativo_rrpp,corrrp);
        mDb.update(SQLITE_TABLE_Agente, initialValues,
                AG_id_agente_venta+"=?",new String[]{idagen});
    }

    public boolean deleteAllAgentes() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Agente, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchAgentesByIds(int  idAgenteVenta, int idLiquidacion) throws SQLException {
        Log.w(TAG, ""+idAgenteVenta);
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Agente, new String[] {AG_id_agente,
                        AG_id_agente_venta,AG_id_empresa, AG_id_usuario, AG_nombre_agente,AG_nom_usuario,
                        AG_pass_usuario, AG_liquidacion, AG_km_inicial, AG_km_final,  AG_nombre_ruta, AG_nro_bodegas, AG_fecha},
                AG_id_agente_venta + " = '" + idAgenteVenta+"' AND "+AG_liquidacion + " = '" + idLiquidacion + "'", null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }



    public String getSerieFacturaByIdAgente(int idAgente, int idLiquidacion) throws SQLException {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Agente, new String[] {AG_id_agente,
                        AG_id_agente_venta, AG_nombre_agente, AG_nombre_ruta, AG_nro_bodegas, AG_serie_boleta, AG_serie_factura, AG_serie_rrpp, AG_fecha},
                AG_id_agente_venta + " = '" + idAgente+"' AND "+AG_liquidacion + " = '" + idLiquidacion + "'", null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.getCount()==0){
            return null;
        }


        return mCursor.getString(mCursor.getColumnIndex(AG_serie_factura));

    }

    public Cursor login(String usuario, String password) throws SQLException {

        boolean existeUser = false;
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Agente, new String[] {AG_id_agente,
                        AG_id_agente_venta,AG_id_empresa, AG_id_usuario, AG_nombre_agente,AG_nom_usuario,
                        AG_pass_usuario, AG_liquidacion, AG_km_inicial, AG_km_final,  AG_nombre_ruta, AG_nro_bodegas, AG_fecha},
                AG_nom_usuario + " = '" + usuario + "' AND "+ AG_pass_usuario + " = '" + password+"'", null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            existeUser=true;
        }


        return mCursor;

    }


    public String getSerieBoletaByIdAgente(int idAgente, int idLiquidacion) throws SQLException {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Agente, new String[] {AG_id_agente,
                        AG_id_agente_venta, AG_nombre_agente, AG_nombre_ruta, AG_nro_bodegas, AG_serie_boleta, AG_serie_factura, AG_serie_rrpp, AG_fecha},
                AG_id_agente_venta + " = '" + idAgente+"' AND "+AG_liquidacion + " = '" + idLiquidacion + "'", null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.getCount()==0){
            return null;
        }


        return mCursor.getString(mCursor.getColumnIndex(AG_serie_boleta));

    }


    public Cursor fetchAgentesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Agente, new String[] {AG_id_agente,
                            AG_id_agente_venta, AG_nombre_agente, AG_nombre_ruta, AG_nro_bodegas, AG_fecha},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Agente, new String[] {AG_id_agente,
                            AG_id_agente_venta, AG_nombre_agente, AG_nombre_ruta, AG_nro_bodegas, AG_fecha},
                    AG_nombre_agente + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllAgentes() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Agente, new String[] {AG_id_agente,
                        AG_id_agente_venta, AG_nombre_agente, AG_nombre_ruta, AG_nro_bodegas, AG_fecha},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllAgentesVenta() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Agente, new String[] {AG_id_agente,
                        AG_id_agente_venta, AG_serie_boleta, AG_serie_factura, AG_serie_rrpp,
                        AG_correlativo_boleta, AG_correlativo_factura, AG_correlativo_rrpp, AG_fecha},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public void updateAgente(String id_agente_venta,String id_usuario,String id_empresa,String nombre_usuario,String nombre_agente,String pass_usuario){
        ContentValues initialValues = new ContentValues();
        initialValues.put(AG_id_agente_venta,id_agente_venta);
        initialValues.put(AG_id_usuario,id_usuario);
        initialValues.put(AG_id_empresa,id_empresa);
        initialValues.put(AG_nombre_agente,nombre_agente);
        initialValues.put(AG_nom_usuario,nombre_usuario);
        initialValues.put(AG_pass_usuario,pass_usuario);
        mDb.update(SQLITE_TABLE_Agente, initialValues,null,null);
    }

    public void updateAgenteBefore(String id_agente_venta, String id_liquidacion, String km_inicial,
                                   String km_final, String nombre_ruta,String nro_bodegas,
                                   String serie_boleta, String serie_factura, String serie_rrpp,
                                   String correlativo_boleta, String correlativo_factura,
                                   String correlativo_rrpp){
        ContentValues initialValues = new ContentValues();
        initialValues.put(AG_liquidacion,id_liquidacion);
        initialValues.put(AG_km_inicial,km_inicial);
        initialValues.put(AG_km_final,km_final);
        initialValues.put(AG_nombre_ruta,nombre_ruta);
        initialValues.put(AG_nro_bodegas,nro_bodegas);
        initialValues.put(AG_serie_boleta,serie_boleta);
        initialValues.put(AG_serie_factura,serie_factura);
        initialValues.put(AG_serie_rrpp,serie_rrpp);
        initialValues.put(AG_correlativo_boleta,correlativo_boleta);
        initialValues.put(AG_correlativo_factura,correlativo_factura);
        initialValues.put(AG_correlativo_rrpp,correlativo_rrpp);
        mDb.update(SQLITE_TABLE_Agente, initialValues,
                AG_id_agente_venta+"=?",new String[]{id_agente_venta});
    }
    public String getNameAgente(){
        Cursor cr = mDb.rawQuery("select * from "+SQLITE_TABLE_Agente+"",null);
        cr.moveToFirst();
        return cr.getString(cr.getColumnIndexOrThrow(AG_nombre_agente));
    }
    public int getIdUsuario(){
        Cursor cr = mDb.rawQuery("select * from "+SQLITE_TABLE_Agente+"",null);
        cr.moveToFirst();
//
     return cr.getInt(cr.getColumnIndexOrThrow(AG_id_usuario));

    }


    private String M_id_agente_venta;
    private String M_liquidacion;
    private String M_km_inicial;
    private String M_km_final;
    private String M_nombre_ruta;
    private String M_nro_bodegas;
    private String M_serie_boleta;
    private String M_serie_factura;
    private String M_serie_rrpp;
    private String M_correlativo_boleta;
    private String M_correlativo_factura;
    private String M_correlativo_rrpp;



}