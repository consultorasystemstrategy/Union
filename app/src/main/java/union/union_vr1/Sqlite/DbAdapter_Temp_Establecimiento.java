package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import union.union_vr1.Conexion.DbHelper;

/**
 * Created by Usuario on 06/02/2015.
 */
public class DbAdapter_Temp_Establecimiento {

    public static final String establec_id = "_id";
    public static final String establec_id_remoto = "establec_id_remoto";
    public static final String establec_descripcion = "establec_descripcion";
    public static final String establec_direccion_fiscal = "establec_direccion_fiscal";
    public static final String establec_tipo_persona = "establec_tipo_persona";
    public static final String establec_nombres = "establec_nombres";
    public static final String establec_apPaterno = "establec_apPaterno";
    public static final String establec_apMaterno = "establec_apMaterno";
    public static final String establec_tipo_documento = "establec_tipo_documento";
    public static final String establec_nro_documento = "establec_nro_documento";
    public static final String establec_celular = "establec_celular";
    public static final String establec_correo = "establec_correo";
    public static final String establec_tipo_establecimiento = "establec_tipo_establecimiento";
    public static final String establec_descripcion_establecimiento = "establec_descripcion_establecimiento";


    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_Temp_Establec = "m_temp_establec_data";
    private final Context mCtx;

    public static final String CREATE_TABLE_TEMP_ESTABLEC =
            "create table if not exists " + SQLITE_TABLE_Temp_Establec + " ("
                    + establec_id + " integer primary key,"
                    + establec_id_remoto + " integer, "
                    + establec_descripcion + " integer, "
                    + establec_direccion_fiscal + " text, "
                    + establec_tipo_persona + " text, "
                    + establec_nombres + " text, "
                    + establec_apPaterno + " text, "
                    + establec_apMaterno + " text, "
                    + establec_tipo_documento + " text, "
                    + establec_nro_documento + " text, "
                    + establec_celular + " text, "
                    + establec_correo + " text, "
                    + establec_tipo_establecimiento + " text, "
                    + Constants._SINCRONIZAR + " integer, "
                    + establec_descripcion_establecimiento + " text );";

    public static final String DELETE_TABLE_Temp_Establec = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Temp_Establec;

    public DbAdapter_Temp_Establecimiento(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Temp_Establecimiento open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTempEstablec(
            int idRemoto,
            String direccion_descripcion,
            String direccion_fiscal,
            String tipo_persona,
            String nombres,
            String apPaterno,
            String apMaterno,
            String tipo_documento,
            String nro_documento,
            String celular,
            String correo,
            String tipo_establecimiento,
            String descripcion_establecimiento,
            int estado_actualizacion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(establec_id_remoto, idRemoto);
        initialValues.put(establec_descripcion, direccion_descripcion);
        initialValues.put(establec_direccion_fiscal, direccion_fiscal);
        initialValues.put(establec_tipo_persona, tipo_persona);
        initialValues.put(establec_nombres, nombres);
        initialValues.put(establec_apPaterno, apPaterno);
        initialValues.put(establec_apMaterno, apMaterno);
        initialValues.put(establec_tipo_documento, tipo_documento);
        initialValues.put(establec_nro_documento, nro_documento);
        initialValues.put(establec_celular, celular);
        initialValues.put(establec_correo, correo);
        initialValues.put(establec_tipo_establecimiento, tipo_establecimiento);
        initialValues.put(establec_descripcion_establecimiento, descripcion_establecimiento);
        initialValues.put(Constants._SINCRONIZAR, estado_actualizacion);
        return mDb.insert(SQLITE_TABLE_Temp_Establec, null, initialValues);
    }

    public long updateTempEstablec(String id,
                                   int idRemoto,
                                   String direccion_descripcion,
                                   String direccion_fiscal,
                                   String tipo_persona,
                                   String nombres,
                                   String apPaterno,
                                   String apMaterno,
                                   String tipo_documento,
                                   String nro_documento,
                                   String celular,
                                   String correo,
                                   String tipo_establecimiento,
                                   String descripcion_establecimiento,
                                   int estado_actualizacion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(establec_id_remoto, idRemoto);
        initialValues.put(establec_descripcion, direccion_descripcion);
        initialValues.put(establec_direccion_fiscal, direccion_fiscal);
        initialValues.put(establec_tipo_persona, tipo_persona);
        initialValues.put(establec_nombres, nombres);
        initialValues.put(establec_apPaterno, apPaterno);
        initialValues.put(establec_apMaterno, apMaterno);
        initialValues.put(establec_tipo_documento, tipo_documento);
        initialValues.put(establec_nro_documento, nro_documento);
        initialValues.put(establec_celular, celular);
        initialValues.put(establec_correo, correo);
        initialValues.put(establec_tipo_establecimiento, tipo_establecimiento);
        initialValues.put(establec_descripcion_establecimiento, descripcion_establecimiento);
        initialValues.put(Constants._SINCRONIZAR, estado_actualizacion);
        return mDb.update(SQLITE_TABLE_Temp_Establec, initialValues,
                establec_id + "=?", new String[]{id});
    }

    public Cursor fetchTemEstablec() {
        Cursor cursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Temp_Establec + " order by " + establec_id + " asc", null);
        return cursor;
    }

    public Cursor fetchTemEstablec(int id) {
        Cursor cursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Temp_Establec + " where  " + establec_id + "='" + id + "'", null);
        return cursor;
    }


    public boolean deleteAll() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Temp_Establec, null, null);
        return doneDelete > 0;

    }
    public long updateTempEstablecById(int id,
                                   int estado_actualizacion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR, estado_actualizacion);
        return mDb.update(SQLITE_TABLE_Temp_Establec, initialValues,
                establec_id + "=?", new String[]{id+""});
    }
}
