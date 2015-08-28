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
 * Created by Usuario on 15/12/2014.
 */

public class DBAdapter_Temp_Inventario {

    public static final String _id = "_id";
    public static final String temp_in_id_guia = "temp_in_id_guia";
    public static final String temp_in_id_estado = "temp_in_id_estado";
    public static final String temp_te_nom_fecha = "temp_te_nom_fecha";

    public static final String TAG = "Temp_Venta_Detalle";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_Temp_Venta_Inventario = "m_temp_venta_inventario_guia";
    private final Context mCtx;

    public static final String CREATE_TABLE_TEMP_VENTA_Inventario =
            "create table "+SQLITE_TABLE_Temp_Venta_Inventario+" ("
                    +_id+" integer primary key autoincrement,"
                    +temp_in_id_guia+" text,"
                    +temp_in_id_estado+" integer,"
                    +temp_te_nom_fecha+" text);";

    public static final String DELETE_TABLE_TEMP_VENTA_DETALLE = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Temp_Venta_Inventario;

    public DBAdapter_Temp_Inventario(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter_Temp_Inventario open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTempInventario(String idguia,int estado){

        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_in_id_guia,idguia);
        initialValues.put(temp_in_id_estado,estado);
        initialValues.put(temp_te_nom_fecha,getDatePhone());
        return mDb.insert(SQLITE_TABLE_Temp_Venta_Inventario, null, initialValues);
    }
    public Cursor getAllIvnetario (){
        Cursor cr = mDb.rawQuery("select * from "+SQLITE_TABLE_Temp_Venta_Inventario+" where "+temp_te_nom_fecha+" = '"+getDatePhone()+"'",null);
        if (cr != null) {
            cr.moveToFirst();
        }
        return cr;

    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }

}
