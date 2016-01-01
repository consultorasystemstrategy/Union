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
public class DbAdapter_Tipo_Persona {

    public static final String tipo_Persona_Id = "_id";
    public static final String tipo_Persona_PersonaId = "TperlTipoPersonaId";
    public static final String tipo_Persona_Descripcion = "TperVDescripcion";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_TIPO_PERSONA = "table_tipo_persona";
    private final Context mCtx;

    public static final String CREATE_TABLE_TIPO_PERSONA =
            "create table if not exists "+SQLITE_TABLE_TIPO_PERSONA+" ("
                    + tipo_Persona_Id +" integer primary key,"
                    + tipo_Persona_PersonaId +" integer ,"
                    + tipo_Persona_Descripcion +" text );";

    public static final String DELETE_TABLE_TIPO_PERSONA= "DROP TABLE IF EXISTS " + SQLITE_TABLE_TIPO_PERSONA;

    public DbAdapter_Tipo_Persona(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Tipo_Persona open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTipoPersona(int id,String descripcion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(tipo_Persona_PersonaId, id);
        initialValues.put(tipo_Persona_Descripcion, descripcion);
        return mDb.insert(SQLITE_TABLE_TIPO_PERSONA, null, initialValues);
    }


    public Cursor fetchTipoPersona() {
        Cursor cursor = mDb.rawQuery("select * from "+SQLITE_TABLE_TIPO_PERSONA+" order by "+ tipo_Persona_Id +" asc",null);
        return cursor;
    }


    public boolean deleteAllTipoPersona() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_TIPO_PERSONA, null ,null);
        return doneDelete > 0;

    }
}
