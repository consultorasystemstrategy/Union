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
 * Created by kike on 15/01/2016.
 */
public class DBAdapter_Trans_Detallado {




    public static final String _id = "_id";
    public static final String pro_v_id = "temp_in_id_guia";
    public static final String pro_v_nombre = "temp_in_id_nombre";
    public static final String guitran_v_numguia_flex = "temp_in_id_numero_flex";
    public static final String pro_guitra_usuarioid= "temp_in_id_estado";
    public static final String guitran_v_cantidad_nm = "temp_in_id_cantidad_nm";
    public static final String guitran_fecha_producto = "temp_in_id_fecha_producto";



    public static final String TAG = "Temp_Transferencia_Detalle";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_Temp_Trans_Detalle = "m_temp_trans_detalle_guia";
    private final Context mCtx;

    public static final String CREATE_TABLE_TEMP_Trans_Detalle =
            "create table "+SQLITE_TABLE_Temp_Trans_Detalle+" ("
                    +_id+" integer primary key autoincrement,"
                    +pro_v_id+" text,"
                    +pro_v_nombre+" text,"
                    +guitran_v_numguia_flex+" text,"
                    +pro_guitra_usuarioid+" text,"
                    +guitran_v_cantidad_nm+" text,"
                    +guitran_fecha_producto+" text,"
                    + Constants._SINCRONIZAR + " integer);";

    public static final String DELETE_TABLE_TEMP_TRANS_DETALLE = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Temp_Trans_Detalle;

    public DBAdapter_Trans_Detallado(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter_Trans_Detallado open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public void truncateClienteRuta() {
        mDb.execSQL("delete from " + SQLITE_TABLE_Temp_Trans_Detalle);
    }
    public long createTransDetallado(String idguia, String productovnombre, String numguiafelx, String usuarioid,String cantidadnum ,String fechad, int estado_sincronizacion ){

        ContentValues initialValues = new ContentValues();
        initialValues.put(pro_v_id,idguia);
        initialValues.put(pro_v_nombre,productovnombre);
        initialValues.put(guitran_v_numguia_flex,numguiafelx);
        initialValues.put(pro_guitra_usuarioid,usuarioid);
        initialValues.put(guitran_v_cantidad_nm,cantidadnum);
        initialValues.put(guitran_fecha_producto,fechad);
        initialValues.put(Constants._SINCRONIZAR, estado_sincronizacion);
        return mDb.insert(SQLITE_TABLE_Temp_Trans_Detalle, null, initialValues);

    }
    public Cursor getAllTrans (){
        Cursor cr = mDb.rawQuery("select * from "+SQLITE_TABLE_Temp_Trans_Detalle+" '",null);
        if (cr != null) {
            cr.moveToFirst();
        }
        return cr;
    }
    public Cursor listarporid(String dia) {
        Cursor mCursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Temp_Trans_Detalle + " WHERE " + guitran_v_numguia_flex + "='"+dia+"'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllStock(String codigo) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Temp_Trans_Detalle, new String[]{_id,
                        pro_v_id, pro_guitra_usuarioid, pro_v_nombre,guitran_v_cantidad_nm},
                guitran_v_numguia_flex + " = ?", new String[]{"" + codigo}, null, null, pro_v_nombre + " ASC");

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
