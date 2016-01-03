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
    public static final String establec_usuario_accion = "establec_usuario_accion";
    public static final String establec_telefono_fijo = "establec_telefono_fijo";
    public static final String establec_celular_one = "establec_celular_one";
    public static final String establec_celular_two = "establec_celular_two";
    public static final String establec_latitud = "establec_latitud";
    public static final String establec_longitud = "establec_longitud";
    public static final String establec_descripcion = "establec_descripcion";
    public static final String establec_direccion_fiscal = "establec_direccion_fiscal";
    public static final String establec_tipo_persona = "establec_tipo_persona";
    public static final String establec_nombres = "establec_nombres";
    public static final String establec_apPaterno = "establec_apPaterno";
    public static final String establec_apMaterno = "establec_apMaterno";
    public static final String establec_tipo_documento = "establec_tipo_documento";
    public static final String establec_nro_documento = "establec_nro_documento";
    public static final String establec_estado_guardado = "establec_estado_guardado";
    public static final String establec_categoria_estable="establec_categoria_estable";
    public static final String establec_correo = "establec_correo";
    public static final String establec_tipo_establecimiento = "establec_tipo_establecimiento";
    public static final String establec_descripcion_establecimiento = "establec_descripcion_establecimiento";
    public static final String establec_editado = "establec_editado";


    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_Temp_Establec = "m_temp_establec_data";
    private final Context mCtx;

    public static final String CREATE_TABLE_TEMP_ESTABLEC =
            "create table if not exists " + SQLITE_TABLE_Temp_Establec + " ("
                    + establec_id + " integer primary key,"
                    + establec_id_remoto + " integer, "
                    + establec_usuario_accion + " integer, "
                    + establec_telefono_fijo + " text, "
                    + establec_celular_one + " text, "
                    + establec_celular_two + " text, "
                    + establec_editado + " integer, "
                    + establec_latitud + " text, "
                    + establec_longitud + " text, "
                    + establec_descripcion + " text, "
                    + establec_direccion_fiscal + " text, "
                    + establec_tipo_persona + " text, "
                    + establec_nombres + " text, "
                    + establec_apPaterno + " text, "
                    + establec_apMaterno + " text, "
                    + establec_categoria_estable + " text, "
                    + establec_tipo_documento + " text, "
                    + establec_nro_documento + " text, "
                    + establec_estado_guardado + " text, "
                    + establec_correo + " text, "
                    + establec_tipo_establecimiento + " text, "
                    + establec_descripcion_establecimiento + " text,"
                    + Constants._SINCRONIZAR + " integer );";

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
            int _id_remoto,
            String _usuario_accion,
            String _telefono_fijo,
            String _celular_one,
            String _celular_two,
            double _latitud,
            double _longitud,
            String _descripcion_direccion,
            String _direccion_fiscal,
            int _tipo_persona,
            String _nombres,
            String _apPaterno,
            String _apMaterno,
            int _tipo_documento,
            int _nro_documento,
            int _estado_guardado,
            String _correo,
            int _tipo_establecimiento,
            String _descripcion_establecimiento,int _categoria_establec,
            int _ESTADO,int _estadoEditado
    ) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(establec_id_remoto, _id_remoto);
        initialValues.put(establec_usuario_accion, _usuario_accion);
        initialValues.put(establec_telefono_fijo, _telefono_fijo);
        initialValues.put(establec_celular_one, _celular_one);
        initialValues.put(establec_celular_two, _celular_two);
        initialValues.put(establec_latitud, _latitud);
        initialValues.put(establec_longitud, _longitud);
        initialValues.put(establec_descripcion, _descripcion_direccion);
        initialValues.put(establec_direccion_fiscal, _direccion_fiscal);
        initialValues.put(establec_tipo_persona, _tipo_persona);
        initialValues.put(establec_nombres, _nombres);
        initialValues.put(establec_apPaterno, _apPaterno);
        initialValues.put(establec_apMaterno, _apMaterno);
        initialValues.put(establec_tipo_documento, _tipo_documento);
        initialValues.put(establec_nro_documento, _nro_documento);
        initialValues.put(establec_estado_guardado, _estado_guardado);
        initialValues.put(establec_correo, _correo);
        initialValues.put(establec_categoria_estable, _categoria_establec);
        initialValues.put(establec_tipo_establecimiento, _tipo_establecimiento);
        initialValues.put(establec_descripcion_establecimiento, _descripcion_establecimiento);
        initialValues.put(establec_editado, _estadoEditado);
        initialValues.put(Constants._SINCRONIZAR, _ESTADO);

        return mDb.insert(SQLITE_TABLE_Temp_Establec, null, initialValues);
    }

    public long updateTempEstablec(
            String idRemoto,
                                   String _usuario_accion,
                                   String _telefono_fijo,
                                   String _celular_one,
                                   int _celular_two,
                                   double _latitud,
                                   double _longitud,
                                   String _descripcion,
                                   String _direccion_fiscal,
                                   int _tipo_persona,
                                   String _nombres,
                                   String _apPaterno,
                                   String _apMaterno,
                                   int _tipo_documento,
                                   int _nro_documento,
                                   int _estado_guardado,
                                   String _correo,
                                   int _tipo_establecimiento,
                                   String _descripcion_establecimiento,int catEstablec,
                                   int _ESTADO, int estadoEditado) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(establec_usuario_accion, _usuario_accion);
        initialValues.put(establec_telefono_fijo, _telefono_fijo);
        initialValues.put(establec_celular_one, _celular_one);
        initialValues.put(establec_celular_two, _celular_two);
        initialValues.put(establec_latitud, _latitud);
        initialValues.put(establec_longitud, _longitud);
        initialValues.put(establec_descripcion, _descripcion);
        initialValues.put(establec_direccion_fiscal, _direccion_fiscal);
        initialValues.put(establec_tipo_persona, _tipo_persona);
        initialValues.put(establec_nombres, _nombres);
        initialValues.put(establec_apPaterno, _apPaterno);
        initialValues.put(establec_apMaterno, _apMaterno);
        initialValues.put(establec_tipo_documento, _tipo_documento);
        initialValues.put(establec_nro_documento, _nro_documento);
        initialValues.put(establec_estado_guardado, _estado_guardado);
        initialValues.put(establec_correo, _correo);
        initialValues.put(establec_tipo_establecimiento, _tipo_establecimiento);
        initialValues.put(establec_descripcion_establecimiento, _descripcion_establecimiento);
        initialValues.put(establec_editado, estadoEditado);
        return mDb.update(SQLITE_TABLE_Temp_Establec, initialValues,
                establec_id_remoto + "=?", new String[]{idRemoto + ""});
    }
    public long update(int _id_remoto,int estado){

        ContentValues initialValues = new ContentValues();
        initialValues.put(establec_editado, estado);
        return mDb.update(SQLITE_TABLE_Temp_Establec, initialValues,
                establec_id_remoto + "=?", new String[]{_id_remoto+""});

    }

    public Cursor fetchTemEstablec() {
        Cursor cursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Temp_Establec + " order by " + establec_id + " asc", null);
        return cursor;
    }

    public Cursor fetchTemEstablecById(String id) {
        Cursor cursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Temp_Establec + " where  " + establec_id_remoto + "='" + id + "'", null);
        return cursor;
    }


    public boolean deleteAll() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Temp_Establec, null, null);
        return doneDelete > 0;

    }

    public long updateTempEstablecById(int id,int idRemoto,
                                       int estado_actualizacion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(establec_id_remoto, idRemoto);
        initialValues.put(Constants._SINCRONIZAR, estado_actualizacion);
        return mDb.update(SQLITE_TABLE_Temp_Establec, initialValues,
                establec_id + "=?", new String[]{id + ""});
    }
}
