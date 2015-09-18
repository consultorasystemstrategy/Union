package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.Agente;

public class DbAdapter_Agente_Login {

    //LOS PROCEDIMIENTOS TRAEN ESTOS CAMPOS
    public static final String AG_id_agente = "_id";
    public static final String AG_id_agente_venta = "ag_in_id_agente_venta_login";
    public static final String AG_nombre_agente = "ag_te_nombre_agente_login";
    public static final String AG_serie_boleta = "ag_te_serie_boleta_login";
    public static final String AG_serie_factura = "ag_te_serie_factura_login";
    public static final String AG_correlativo_boleta = "ag_in_correlativo_boleta_login";
    public static final String AG_correlativo_factura = "ag_in_correlativo_factura_login";
    public static final String estado_sincronizacion = "estado_sincronizacion_login";
    public static final String LOGIN_usuario= "login_usuario";
    public static final String LOGIN_clave = "login_clave";
    public static final String AG_fecha = "fecha";
    public static final String AG_LIQUIDACION = "LIQUIDACION";

    //FALTA TRAER ESTOS CAMPOS
    public static final String AG_correlativo_rrpp = "ag_in_correlativo_rrpp_login";
    public static final String AG_serie_rrpp = "ag_te_serie_rrpp_login";



    public static final String TAG = "Agente_Login";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "ProdUnion.sqlite";
    private static final String SQLITE_TABLE_Agente = "m_agente_login";
    private final Context mCtx;

    public static final String CREATE_TABLE_AGENTE =
            "create table if not exists "+SQLITE_TABLE_Agente+" ("
                    +AG_id_agente+" integer primary key autoincrement,"
                    +AG_id_agente_venta+" integer,"
                    +AG_nombre_agente+" text,"
                    +AG_serie_boleta+" text,"
                    +AG_serie_factura+" text,"
                    +AG_serie_rrpp+" text,"
                    +AG_correlativo_boleta+" text,"
                    +AG_correlativo_factura+" text,"
                    +AG_correlativo_rrpp+" text, "
                    +LOGIN_usuario+" text, "
                    +LOGIN_clave+" text, "
                    +AG_fecha+" text, "
                    +AG_LIQUIDACION+" integer, "
                    +estado_sincronizacion+ " integer);";

    public static final String DELETE_TABLE_AGENTE_LOGIN = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Agente;

    public DbAdapter_Agente_Login(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Agente_Login open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    public long createAgente(Agente agente, String fecha,String usuario,String clave) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(AG_id_agente_venta,agente.getIdAgenteVenta());
        initialValues.put(AG_nombre_agente,agente.getNombreAgente());
        initialValues.put(AG_serie_boleta,agente.getSerieBoleta());
        initialValues.put(AG_serie_factura,agente.getSerieFactura());
        initialValues.put(AG_serie_rrpp, agente.getSerieRrpp());
        initialValues.put(AG_correlativo_boleta,agente.getCorrelativoBoleta());
        initialValues.put(AG_correlativo_factura,agente.getCorrelativoFactura());
        initialValues.put(AG_correlativo_rrpp, agente.getCorrelativoRrpp());
        initialValues.put(LOGIN_usuario, usuario);
        initialValues.put(LOGIN_clave, clave);
        initialValues.put(AG_LIQUIDACION, agente.getLiquidacion());
        initialValues.put(AG_fecha, fecha);


        return mDb.insert(SQLITE_TABLE_Agente, null, initialValues);
    }


    public void updateAgente(Agente agente, String fecha,String usuario,String clave){

        ContentValues initialValues = new ContentValues();
        initialValues.put(AG_id_agente_venta,agente.getIdAgenteVenta());
        initialValues.put(AG_nombre_agente,agente.getNombreAgente());
        initialValues.put(AG_serie_boleta,agente.getSerieBoleta());
        initialValues.put(AG_serie_factura,agente.getSerieFactura());
        initialValues.put(AG_serie_rrpp, agente.getSerieRrpp());
        initialValues.put(AG_correlativo_boleta,agente.getCorrelativoBoleta());
        initialValues.put(AG_correlativo_factura,agente.getCorrelativoFactura());
        initialValues.put(AG_correlativo_rrpp, agente.getCorrelativoRrpp());
        initialValues.put(LOGIN_usuario, usuario);
        initialValues.put(AG_LIQUIDACION, agente.getLiquidacion());
        initialValues.put(LOGIN_clave, clave);
        initialValues.put(AG_fecha, fecha);

        mDb.update(SQLITE_TABLE_Agente, initialValues,
                AG_id_agente_venta+"=?",new String[]{""+agente.getIdAgenteVenta()});
    }

    public boolean existeAgentesById(int idAgenteVenta) throws SQLException {
        boolean exists = false;
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Agente, new String[] {AG_id_agente,
                        AG_id_agente_venta, AG_nombre_agente,  AG_fecha},
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



    public Cursor fetchAllAgentesVentaLogin(String fecha) {

        Cursor mCursor = mDb.rawQuery("select * from "+SQLITE_TABLE_Agente +" where "+AG_fecha+"='"+fecha+"'",null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public int fetchLiquidacion(String fecha){
        int valor=-1;
        Cursor cursor = mDb.rawQuery("select * from "+SQLITE_TABLE_Agente+" where "+AG_fecha+" = '"+fecha+"' ;",null);
        if(cursor.moveToFirst()){
            valor = cursor.getInt(cursor.getColumnIndexOrThrow(AG_LIQUIDACION));
        }
        return  valor;
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