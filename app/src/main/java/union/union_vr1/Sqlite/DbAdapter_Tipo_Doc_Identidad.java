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
public class DbAdapter_Tipo_Doc_Identidad {

    public static final String tipo_Doc_Identidad = "_id";
    public static final String tipo_Doc_IdentidadId = "TdiTipoDocIdentidadId";
    public static final String tipo_Doc_Descripcion = "TdiVDescripcion";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_TIPO_DOC_IDENTIDAD = "tipo_doc_identidad";
    private final Context mCtx;

    public static final String CREATE_TABLE_TIPO_DOC_IDENTIDAD =
            "create table if not exists "+ SQLITE_TABLE_TIPO_DOC_IDENTIDAD +" ("
                    + tipo_Doc_Identidad +" integer primary key,"
                    + tipo_Doc_IdentidadId +" integer ,"
                    + tipo_Doc_Descripcion +" text );";

    public static final String DELETE_TABLE_TIPO_DOC_IDENTIDAD= "DROP TABLE IF EXISTS " + SQLITE_TABLE_TIPO_DOC_IDENTIDAD;

    public DbAdapter_Tipo_Doc_Identidad(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Tipo_Doc_Identidad open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTipoDocIden(int id,String descripcion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(tipo_Doc_IdentidadId, id);
        initialValues.put(tipo_Doc_Descripcion, descripcion);
        return mDb.insert(SQLITE_TABLE_TIPO_DOC_IDENTIDAD, null, initialValues);
    }


    public Cursor fetchTipoDocIden(int id) {
        Cursor cursor=null;
        if(id==-1){
            cursor = mDb.rawQuery("select * from "+ SQLITE_TABLE_TIPO_DOC_IDENTIDAD +" order by "+ tipo_Doc_IdentidadId +" asc",null);

        }else{
            cursor = mDb.rawQuery("select * from "+ SQLITE_TABLE_TIPO_DOC_IDENTIDAD +" where "+ tipo_Doc_IdentidadId +" ='"+id+"'",null);

        }

        return cursor;
    }


    public boolean deleteAllTipoDocIden() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_TIPO_DOC_IDENTIDAD, null ,null);
        return doneDelete > 0;

    }
}
