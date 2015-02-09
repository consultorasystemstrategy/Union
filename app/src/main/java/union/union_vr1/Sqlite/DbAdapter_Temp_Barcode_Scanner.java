package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;

/**
 * Created by Usuario on 06/02/2015.
 */
public class DbAdapter_Temp_Barcode_Scanner {

    public static final String temp_id_scanner = "_id";
    public static final String temp_id_establecimiento = "temp_id_establecimiento";
    public static final String TAG = "Temp Scanner BArcode";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_Temp_scanner = "m_Temp_scanner";
    private final Context mCtx;

    public static final String CREATE_TABLE_Temp_scanner =
            "create table if not exists "+SQLITE_TABLE_Temp_scanner+" ("
                    +temp_id_scanner+" integer primary key autoincrement,"
                    +temp_id_establecimiento+" integer);";

    public static final String DELETE_TABLE_Temp_scanner = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Temp_scanner;

    public DbAdapter_Temp_Barcode_Scanner(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Temp_Barcode_Scanner open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTempScanner(
            int idEstablecimiento) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_id_establecimiento, idEstablecimiento);

        return mDb.insert(SQLITE_TABLE_Temp_scanner, null, initialValues);
    }



    public boolean deleteAll() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Temp_scanner, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }
    public Cursor fetchAll() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Temp_scanner, new String[] {temp_id_scanner,
                        temp_id_establecimiento},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}
