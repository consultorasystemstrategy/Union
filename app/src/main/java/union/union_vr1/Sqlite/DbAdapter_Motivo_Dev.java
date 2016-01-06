package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import union.union_vr1.Conexion.DbHelper;

/**
 * Created by Usuario on 31/12/2015.
 */
public class DbAdapter_Motivo_Dev {

    public static final String motivo_dev_id = "_id";
    public static final String motivo_dev_devId = "motivo_dev_devId";
    public static final String motivo_dev_descripcion = "motivo_dev_descripcion";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_MOT_DEV = "table_motivo_dev";
    private final Context mCtx;

    public static final String CREATE_TABLE_MOT_DEV =
            "create table if not exists "+ SQLITE_TABLE_MOT_DEV +" ("
                    + motivo_dev_id +" integer primary key,"
                    + motivo_dev_devId +" integer ,"
                    + motivo_dev_descripcion +" text );";

    public static final String DELETE_TABLE_MOT_DEV= "DROP TABLE IF EXISTS " + SQLITE_TABLE_MOT_DEV;

    public DbAdapter_Motivo_Dev(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Motivo_Dev open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createMotDev(int id,String descripcion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(motivo_dev_devId, id);
        initialValues.put(motivo_dev_descripcion, descripcion);
        return mDb.insert(SQLITE_TABLE_MOT_DEV, null, initialValues);
    }


    public Cursor fetchMotDev() {
        Cursor cursor=null;

             cursor = mDb.rawQuery("select * from "+ SQLITE_TABLE_MOT_DEV +" order by "+ motivo_dev_id +" asc",null);


        return cursor;
    }


    public boolean deleteMotDev() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_MOT_DEV, null ,null);
        return doneDelete > 0;

    }
}
