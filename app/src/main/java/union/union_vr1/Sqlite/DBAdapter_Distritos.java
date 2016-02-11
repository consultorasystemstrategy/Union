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

public class DBAdapter_Distritos {

    public static final String DIS_id = "_id";
    public static final String Dis_IdDistrito = "Dis_IdDistrito";
    public static final String Dis_descripcion = "Dis_descripcion";

    public static final String TAG = DBAdapter_Distritos.class.getSimpleName();

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_DISTRITOS = "m_distritos"; //
    private final Context mCtx;

    public static final String CREATE_TABLE_DISTRITOS =
            "create table " + SQLITE_TABLE_DISTRITOS + " ("
                    + DIS_id + " integer primary key autoincrement,"
                    + Dis_IdDistrito + " integer,"
                    + Dis_descripcion + " text)";

    public static final String DELETE_TABLE_DISTRITOS = "DROP TABLE IF EXISTS " + SQLITE_TABLE_DISTRITOS;

    public DBAdapter_Distritos(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter_Distritos open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }



    public long createDistritos(int id , String descripcion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(Dis_IdDistrito, id);
        initialValues.put(Dis_descripcion, descripcion);
        return mDb.insert(SQLITE_TABLE_DISTRITOS, null, initialValues);
    }

    public Cursor  listarDistritos(){
        return mDb.rawQuery("select * from " + SQLITE_TABLE_DISTRITOS + "", null);
    }
    public Cursor  listarDistritosLikesss(String id){
        return mDb.rawQuery("select * from " + SQLITE_TABLE_DISTRITOS + " order by "+ Dis_IdDistrito +"='"+id+"' desc", null);
    }
    public Cursor  listarDistritosLike(String input){
        return mDb.rawQuery("select * from "+SQLITE_TABLE_DISTRITOS+" where "+Dis_descripcion+" like '%"+input+"%'",null);
    }

    public String  listarDistritosName(String id){
        Cursor cr  = mDb.rawQuery("select * from " + SQLITE_TABLE_DISTRITOS + " WHERE "+ Dis_IdDistrito +"='"+id+"'", null);
        cr.moveToFirst();

        return cr.getString(cr.getColumnIndexOrThrow(Dis_descripcion));
    }



}
