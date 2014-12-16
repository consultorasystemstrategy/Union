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

/**
 * Created by CCIE on 04/12/2014.
 */
public class DbAdapter_Resumen_Caja {

    public static final String RC_resumenCajaId = "_id";
    public static final String RC_id_Agente = "id_Agente";
    public static final String RC_descripcionComprobante = "rc_te_descripcion";
    public static final String RC_id_tipo_GI = "rc_te_tipo_gi";
    public static final String RC_cantidad = "rc_in_cantidad";
    public static final String RC_vendido = "rc_re_vendido";
    public static final String RC_pagado = "rc_re_pagado";
    public static final String RC_cobrado = "rc_re_cobrado";
    public static final String RC_fecha_reporte = "rc_re_fecha_reporte";


    public static final String TAG = "Resumen_Caja";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String SQLITE_TABLE_Resumen_Caja = "m_resumen_caja";
    private final Context mCtx;

    public static final String CREATE_TABLE_RESUMEN_CAJA =
            "CREATE TABLE IF NOT EXISTS "+SQLITE_TABLE_Resumen_Caja+" ("
                    +RC_resumenCajaId+ " integer primary key autoincrement,"
                    +RC_id_Agente+" integer,"
                    +RC_descripcionComprobante+ " text,"
                    +RC_id_tipo_GI+" integer,"
                    +RC_cantidad+ " integer,"
                    +RC_vendido+ " real,"
                    +RC_pagado+ " real,"
                    +RC_cobrado+ " real,"
                    +RC_fecha_reporte+" text);";


    public static final String DELETE_TABLE_RESUMEN_CAJA = "DROP TABLE IF EXISTS + " + SQLITE_TABLE_Resumen_Caja;

    public DbAdapter_Resumen_Caja(Context context){this.mCtx=context;}

    public DbAdapter_Resumen_Caja open() throws SQLException{
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if(mDbHelper!=null){
            mDbHelper.close();
        }
    }

    public long createResumenCaja(String descripcion,int idAgente ,int tipo_gi,int cantidad, Double vendido, Double pagado, Double cobrado,String fecha){

        ContentValues initialValues = new ContentValues();
        initialValues.put(RC_id_Agente,idAgente);
        initialValues.put(RC_descripcionComprobante,descripcion);
        initialValues.put(RC_id_tipo_GI,tipo_gi);
        initialValues.put(RC_cantidad,cantidad);
        initialValues.put(RC_vendido,vendido);
        initialValues.put(RC_pagado,pagado);
        initialValues.put(RC_cobrado,cobrado);
        initialValues.put(RC_fecha_reporte,fecha);

        return mDb.insert(SQLITE_TABLE_Resumen_Caja,null,initialValues);
    }

    public boolean deleteAllResumenCaja(){
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Resumen_Caja,null,null);
        Log.w(TAG,Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public Cursor fetchAllResumenCaja(){
        Cursor mCursor = mDb.query(SQLITE_TABLE_Resumen_Caja,new String[]{RC_resumenCajaId,
                        RC_descripcionComprobante,RC_cantidad,RC_vendido,RC_pagado,RC_cobrado},
                null,null,null,null,null);
        if(mCursor!=null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }



    public void insertSomeResumenCaja(){


    }

}
