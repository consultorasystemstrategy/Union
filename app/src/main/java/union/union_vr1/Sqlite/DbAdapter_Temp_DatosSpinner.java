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
public class DbAdapter_Temp_DatosSpinner {

    public static final String spinner_id_session = "_id";
    public static final String spinner_variable = "temp_id_variable";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_Temp_data_spinner = "m_temp_spinner_data";
    private final Context mCtx;

    public static final String CREATE_TABLE_DATOS_SPINNER =
            "create table if not exists "+SQLITE_TABLE_Temp_data_spinner+" ("
                    +spinner_id_session+" integer primary key,"
                    +spinner_variable+" text );";

    public static final String DELETE_TABLE_Temp_data_spinner= "DROP TABLE IF EXISTS " + SQLITE_TABLE_Temp_data_spinner;

    public DbAdapter_Temp_DatosSpinner(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Temp_DatosSpinner open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTempSpinner(String valor,int id) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(spinner_variable, valor);
        initialValues.put(spinner_id_session, id);
        return mDb.insert(SQLITE_TABLE_Temp_data_spinner, null, initialValues);
    }
    public long updateTempSpinner(String valor) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(spinner_variable, valor);
        return  mDb.update(SQLITE_TABLE_Temp_data_spinner, initialValues,
                spinner_id_session+"=?",new String[]{valor});
    }

    public Cursor fetchTemSpinner() {
        Cursor cursor = mDb.rawQuery("select * from "+SQLITE_TABLE_Temp_data_spinner+" order by "+spinner_id_session+" asc",null);
        return cursor;
    }
    public Cursor fetchTemSpinnerTipo(int id) {
        Cursor cursor = mDb.rawQuery("select * from "+SQLITE_TABLE_Temp_data_spinner+" where  "+spinner_id_session+"='"+id+"'",null);
        return cursor;
    }


    public boolean deleteAll() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Temp_data_spinner, null ,null);
        return doneDelete > 0;

    }
}
