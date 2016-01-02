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
public class DbAdapter_Categoria_Establecimiento {

    public static final String cat_Establec_Id = "_id";
    public static final String cat_Establec_EstablecId = "CateICatEstablecimientoId";
    public static final String cat_Establec_Descripcion = "CateVDescripcion";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_CAT_ESTABLECIMIENTO = "categoria_establecimiento";
    private final Context mCtx;

    public static final String CREATE_TABLE_CAT_ESTABLECIMIENTO=
            "create table if not exists "+ SQLITE_TABLE_CAT_ESTABLECIMIENTO +" ("
                    + cat_Establec_Id +" integer primary key,"
                    + cat_Establec_EstablecId +" integer ,"
                    + cat_Establec_Descripcion +" text );";

    public static final String DELETE_TABLE_CATEGORIA_ESTABLECIMIENTO= "DROP TABLE IF EXISTS " + SQLITE_TABLE_CAT_ESTABLECIMIENTO;

    public DbAdapter_Categoria_Establecimiento(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Categoria_Establecimiento open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createCatEstablecimiento(int id,String descripcion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(cat_Establec_EstablecId, id);
        initialValues.put(cat_Establec_Descripcion, descripcion);
        return mDb.insert(SQLITE_TABLE_CAT_ESTABLECIMIENTO, null, initialValues);
    }


    public Cursor fetchCatEstablecimiento() {
        Cursor cursor = mDb.rawQuery("select * from "+ SQLITE_TABLE_CAT_ESTABLECIMIENTO +" order by "+ cat_Establec_Id +" asc",null);
        return cursor;
    }


    public boolean deleteCatEstablecimiento() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_CAT_ESTABLECIMIENTO, null ,null);
        return doneDelete > 0;

    }
}
