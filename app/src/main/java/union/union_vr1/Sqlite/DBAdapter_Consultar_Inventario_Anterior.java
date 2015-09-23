package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.Conexion.DbHelper;

/**
 * Created by Usuario on 15/12/2014.
 */

public class DBAdapter_Consultar_Inventario_Anterior {

    public static final String _id = "_id";
    public static final String temp_in_id_producto= "temp_in_id_producto";
    public static final String temp_in_nombre = "temp_in_nombre";
    public static final String temp_in_fecha = "temp_te_fecha";
    public static final String temp_te_cantidad = "temp_te_cantidad";



    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_Temp_Consultar_Inventario = "m_temp_consultar_inventario";
    private final Context mCtx;

    public static final String CREATE_TABLE_TEMP_CONSULTAR_Inventario =
            "create table "+SQLITE_TABLE_Temp_Consultar_Inventario+" ("
                    +_id+" integer primary key autoincrement,"
                    +temp_in_id_producto+" text,"
                    +temp_in_nombre+" text,"
                    +temp_in_fecha+" text,"
                    +temp_te_cantidad+" text);";

    public static final String DELETE_TABLE_TEMP_CONSULTAR_Inventario = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Temp_Consultar_Inventario;

    public DBAdapter_Consultar_Inventario_Anterior(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter_Consultar_Inventario_Anterior open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTempConsultarInventario(String id,String nombre,String cantidad,String fecha){

        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_in_id_producto,id);
        initialValues.put(temp_in_nombre,nombre);
        initialValues.put(temp_te_cantidad,cantidad);
        initialValues.put(temp_in_fecha,fecha);
        return mDb.insert(SQLITE_TABLE_Temp_Consultar_Inventario, null, initialValues);
    }
    public Cursor getConsultarInventario (String fecha){
        Cursor cr = mDb.rawQuery("select  "+temp_in_id_producto+", "+temp_in_nombre+", "+temp_te_cantidad+"  from "+SQLITE_TABLE_Temp_Consultar_Inventario+" where "+temp_in_fecha+" = '"+fecha+"';",null);
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
