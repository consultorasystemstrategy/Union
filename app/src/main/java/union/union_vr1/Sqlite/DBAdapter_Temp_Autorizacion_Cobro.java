package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;

/**
 * Created by Usuario on 15/12/2014.
 */

public class DBAdapter_Temp_Autorizacion_Cobro {

    public static final String temp_autorizacion_cobro = "_id";
    public static final String temp_id_agente= "temp_id_Agente";
    public static final String temp_id_motivo_solicitud = "temp_id_Motivo_Solicitud";
    public static final String temp_id_estado_solicitud = "temp_id_Estado_Solicitud";
    public static final String temp_referencia = "temp_Referencia";
    public static final String temp_montoCredito = "temp_MontoCredito";
    public static final String temp_establec = "temp_Establecimiento";
    public static final String temp_vigencia_credito = "temp_Vigencia_Credito";
    public static final String estado_sincronizacion = "estado_sincronizacion";
    public static final String TAG = "Temp_Venta_Detalle";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_Temp_Autorizacion_Cobro = "m_temp_autorizacion_cobro";
    private final Context mCtx;
    public static final String CREATE_TABLE_TEMP_Autorizacion_Cobro =
            "create table "+SQLITE_TABLE_Temp_Autorizacion_Cobro+"("
                    +temp_autorizacion_cobro+" integer primary key autoincrement,"
                    +temp_id_agente+" integer,"
                    +temp_id_motivo_solicitud+" integer,"
                    +temp_id_estado_solicitud+" integer,"
                    +temp_referencia+" integer,"
                    +temp_montoCredito+" real,"
                    +temp_vigencia_credito+" text,"
                    +estado_sincronizacion+" integer );";

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

    public long createTempAutorizacionPago(int idAgente, int motivoSolicitud, int estadoSolicitud, int referencia, double montoCreditoPagado,
            double creditoPagar,int idEstablec,int sincronizacion){

        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_id_agente,idAgente);
        initialValues.put(temp_establec,idEstablec);
        initialValues.put(temp_id_motivo_solicitud,motivoSolicitud);
        initialValues.put(temp_id_estado_solicitud,estadoSolicitud);
        initialValues.put(temp_referencia,referencia);
        initialValues.put(temp_montoCredito,montoCreditoPagado);
        initialValues.put(temp_vigencia_credito,creditoPagar);
        return mDb.insert(SQLITE_TABLE_Temp_Autorizacion_Cobro, null, initialValues);
    }

}
