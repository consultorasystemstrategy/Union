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
public class DbAdapter_Tipo_Establecimiento {

    public static final String tipo_Establecimiento_Id = "_id";
    public static final String tipo_Establecimiento_EstablecimientoId = "TieITipoEstId";
    public static final String tipo_Establecimiento_Descripcion = "TieVDescripcion";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_TIPO_ESTABLECIMIENTO = "tipo_establecimiento";
    private final Context mCtx;

    public static final String CREATE_TABLE_TIPO_ESTABLECIMIENTO =
            "create table if not exists "+ SQLITE_TABLE_TIPO_ESTABLECIMIENTO +" ("
                    +tipo_Establecimiento_Id+" integer primary key,"
                    +tipo_Establecimiento_EstablecimientoId+" integer ,"
                    +tipo_Establecimiento_Descripcion+" text );";

    public static final String DELETE_TABLE_TIPO_ESTABLECIMIENTO= "DROP TABLE IF EXISTS " + SQLITE_TABLE_TIPO_ESTABLECIMIENTO;

    public DbAdapter_Tipo_Establecimiento(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Tipo_Establecimiento open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long creatTipoEstablec(int id,String descripcion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(tipo_Establecimiento_EstablecimientoId, id);
        initialValues.put(tipo_Establecimiento_Descripcion, descripcion);
        return mDb.insert(SQLITE_TABLE_TIPO_ESTABLECIMIENTO, null, initialValues);
    }


    public Cursor fetchTipoEstablec() {
        Cursor cursor = mDb.rawQuery(" select -1 as _id,-1 as TieITipoEstId ,'Seleccione tipo' as TieVDescripcion union  select "+tipo_Establecimiento_Id+","+tipo_Establecimiento_EstablecimientoId+","+tipo_Establecimiento_Descripcion+" from "+ SQLITE_TABLE_TIPO_ESTABLECIMIENTO +" order by "+tipo_Establecimiento_Id+" asc",null);
        return cursor;
    }



    public Cursor fetchTipoEstablecById(int id) {
        Cursor cursor = mDb.rawQuery("select * from "+ SQLITE_TABLE_TIPO_ESTABLECIMIENTO +" order by "+tipo_Establecimiento_EstablecimientoId+"='"+id+"' desc",null);
        return cursor;
    }


    public boolean deleteAllTipoEstablec() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_TIPO_ESTABLECIMIENTO, null ,null);
        return doneDelete > 0;

    }
}
