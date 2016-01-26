package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import union.union_vr1.Conexion.DbHelper;

/**
 * Created by Usuario on 15/12/2014.
 */

public class DBAdapter_Cliente_Ruta {

    public static final String cliente_ruta_id = "_id";
    public static final String cliente_ruta_remotoId = "cliente_ruta_remotoId";
    public static final String cliente_ruta_nombres = "cliente_ruta_nombres";
    public static final String cliente_ruta_apPaterno = "cliente_ruta_apPaterno";
    public static final String cliente_ruta_apMaterno = "cliente_ruta_apMaterno";
    public static final String cliente_ruta_docIdentidad = "cliente_ruta_docIdentidad";
    public static final String cliente_ruta_tipo_docIdentidad = "cliente_ruta_tipo_docIdentidad";
    public static final String cliente_ruta_tipo_PerIdentidad = "cliente_ruta_tipo_PerIdentidad";
    public static final String cliente_ruta_celular = "cliente_ruta_celular";
    public static final String cliente_ruta_email = "cliente_ruta_email";
    public static final String cliente_ruta_codigoERP = "cliente_ruta_codigoERP";
    public static final String cliente_ruta_empresaId = "cliente_ruta_empresaId";
    public static final String cliente_ruta_agenteId = "cliente_ruta_agenteId";
    public static final String cliente_ruta_rutaId = "cliente_ruta_rutaId";
    public static final String cliente_ruta_dia_semana="cliente_ruta_dia_semana";
    public static final String cliente_ruta_establecimiento="cliente_ruta_establecimiento";
    public static final String cliente_ruta_direccion = "cliente_ruta_direccion";

    public static final String TAG = "M_CLIENTE_RUTA";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_CLIENTE_RUTA = "m_cliente_ruta"; //
    private final Context mCtx;

    public static final String CREATE_TABLE_CLIENTE_RUTA =
            "create table " + SQLITE_TABLE_CLIENTE_RUTA + " ("
                    + cliente_ruta_id + " integer primary key autoincrement,"
                    + cliente_ruta_remotoId + " integer,"
                    + cliente_ruta_nombres + " text,"
                    + cliente_ruta_apPaterno + " text,"
                    + cliente_ruta_apMaterno + " text,"
                    + cliente_ruta_docIdentidad + " text,"
                    + cliente_ruta_tipo_docIdentidad + " integer,"
                    + cliente_ruta_tipo_PerIdentidad + " integer,"
                    + cliente_ruta_celular + " text,"
                    + cliente_ruta_email + " text,"
                    + cliente_ruta_codigoERP + " text,"
                    + cliente_ruta_empresaId + " integer,"
                    + cliente_ruta_agenteId + " integer,"
                    + cliente_ruta_rutaId + " integer,"
                    + cliente_ruta_dia_semana+ " text,"
                    + cliente_ruta_establecimiento+ " text,"
                    + cliente_ruta_direccion + " text,"
                    + Constants._SINCRONIZAR + " integer);";

    public static final String DELETE_TABLE_CLIENTE_RUTA = "DROP TABLE IF EXISTS " + SQLITE_TABLE_CLIENTE_RUTA;

    public DBAdapter_Cliente_Ruta(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter_Cliente_Ruta open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public void truncateClienteRuta() {
        mDb.execSQL("delete from " + SQLITE_TABLE_CLIENTE_RUTA);
    }


    public long createClienteRuta(
            int idRemoto, String nombres, String apPaterno, String apMaterno, String docIdentidad,
            int tipo_docIdentidad, int tipo_PerIdentidad, String celular, String email, String codigoERP, int empresaId, int rutaId,String diaSemana,String establecimiento,String direccion,  int estado_sincronizacion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(cliente_ruta_remotoId, idRemoto);
        initialValues.put(cliente_ruta_nombres, nombres);
        initialValues.put(cliente_ruta_apPaterno, apPaterno);
        initialValues.put(cliente_ruta_apMaterno, apMaterno);
        initialValues.put(cliente_ruta_docIdentidad, docIdentidad);
        initialValues.put(cliente_ruta_tipo_docIdentidad, tipo_docIdentidad);
        initialValues.put(cliente_ruta_tipo_PerIdentidad, tipo_PerIdentidad);
        initialValues.put(cliente_ruta_celular, celular);
        initialValues.put(cliente_ruta_email, email);
        initialValues.put(cliente_ruta_codigoERP, codigoERP);
        initialValues.put(cliente_ruta_empresaId, empresaId);
        initialValues.put(cliente_ruta_rutaId, rutaId);
        initialValues.put(cliente_ruta_dia_semana,diaSemana);
        initialValues.put(cliente_ruta_establecimiento,establecimiento);
        initialValues.put(cliente_ruta_direccion,direccion);
        initialValues.put(Constants._SINCRONIZAR, estado_sincronizacion);
        return mDb.insert(SQLITE_TABLE_CLIENTE_RUTA, null, initialValues);
    }

    public int updateClienteRuta(int idRemoto, String nombres, String apPaterno, String apMaterno, String docIdentidad,
                                 int tipo_docIdentidad, int tipo_PerIdentidad, int celular, String email, String codigoERP, int empresaId, int rutaId, int estado_sincronizacion) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(cliente_ruta_nombres, nombres);
        initialValues.put(cliente_ruta_apPaterno, apPaterno);
        initialValues.put(cliente_ruta_apMaterno, apMaterno);
        initialValues.put(cliente_ruta_docIdentidad, docIdentidad);
        initialValues.put(cliente_ruta_tipo_docIdentidad, tipo_docIdentidad);
        initialValues.put(cliente_ruta_tipo_PerIdentidad, tipo_PerIdentidad);
        initialValues.put(cliente_ruta_celular, celular);
        initialValues.put(cliente_ruta_email, email);
        initialValues.put(cliente_ruta_codigoERP, codigoERP);
        initialValues.put(cliente_ruta_empresaId, empresaId);
        initialValues.put(cliente_ruta_rutaId, rutaId);
        initialValues.put(Constants._SINCRONIZAR, estado_sincronizacion);

        return mDb.update(SQLITE_TABLE_CLIENTE_RUTA, initialValues,
                cliente_ruta_remotoId + "=?", new String[]{"" + idRemoto});
    }

    public int updateEstadoClienteRuta(int idRemoto, int estado) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR, estado);
        return mDb.update(SQLITE_TABLE_CLIENTE_RUTA, initialValues,
                cliente_ruta_remotoId + "=?", new String[]{"" + idRemoto});
    }

    public Cursor listarDocumentoClientexRuta(String numero, int tipoDoc) {

        Cursor mCursor = mDb.rawQuery("select * from " + SQLITE_TABLE_CLIENTE_RUTA + " WHERE " + cliente_ruta_docIdentidad + " like '%" + numero + "%' and " + cliente_ruta_tipo_docIdentidad + "='" + tipoDoc + "'; ", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor listarDocumentoClientexRutaByNum(String numero) {

        Cursor mCursor = mDb.rawQuery("select * from " + SQLITE_TABLE_CLIENTE_RUTA + " WHERE " + cliente_ruta_docIdentidad + " = '" + numero + "' ", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public Boolean existeClienteRuta(String numero, int tipoDoc) {
        Boolean aBoolean = false;
        Cursor mCursor = mDb.rawQuery("select * from " + SQLITE_TABLE_CLIENTE_RUTA + " WHERE " + cliente_ruta_docIdentidad + " like '" + numero + "%' and " + cliente_ruta_tipo_docIdentidad + "='" + tipoDoc + "'; ", null);

        if (mCursor.moveToFirst()) {
            aBoolean =true;
        }
        return aBoolean;
    }
    public Cursor listarPorDia(String dia) {
        Cursor mCursor = mDb.rawQuery("select * from " + SQLITE_TABLE_CLIENTE_RUTA + " WHERE " + cliente_ruta_dia_semana + "='"+dia+"'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
