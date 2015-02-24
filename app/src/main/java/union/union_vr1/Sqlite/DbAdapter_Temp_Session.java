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
public class DbAdapter_Temp_Session {

    public static final String session_id_session = "_id";
    public static final String session_id_variable = "temp_id_variable";
    public static final String session_valor = "temp_id_valor";
    public static final String TAG = "Temp SESSION";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_Temp_session = "m_temp_session";
    private final Context mCtx;

    public static final String CREATE_TABLE_Temp_Session =
            "create table if not exists "+SQLITE_TABLE_Temp_session+" ("
                    +session_id_session+" integer primary key autoincrement,"
                    +session_id_variable+" integer, "
                    +session_valor + " integer);";

    public static final String DELETE_TABLE_Temp_session = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Temp_session;

    public DbAdapter_Temp_Session(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Temp_Session open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTempSession(int idVariable, int valor) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(session_id_variable, idVariable);
        initialValues.put(session_valor, valor);

        return mDb.insert(SQLITE_TABLE_Temp_session, null, initialValues);
    }



    public boolean deleteVariable(int idVariable) {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Temp_session, session_id_variable + " = ? " , new String[]{
                ""+idVariable
        });
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }
    public int fetchVarible(int idVariable) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Temp_session, new String[] {session_id_session,
                        session_id_variable, session_valor},
                session_id_variable + " = ? " ,
                new String[]{
                        ""+idVariable
                },
                null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.getCount()==0){
            Log.d("SESSION", "valor 0");
            return 0;
        }

        return mCursor.getInt(mCursor.getColumnIndexOrThrow(session_valor));
    }

    public boolean deleteAll() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Temp_session, null ,null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }
}
