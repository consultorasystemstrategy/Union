package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DecimalFormat;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.Precio;

public class DbAdapter_Cobros_Manuales {


    public static final String CM_id_Cobros_Manuales = "_id";
    public static final String CM_Categoria_Movimiento = "vint_CategoriaMovimientoId";
    public static final String CM_Importe = "vdou_Importe";
    public static final String CM_Fecha_Hora = "vstr_FechaHora";
    public static final String CM_Referencia = "vstr_Referencia";
    public static final String CM_Usuario = "vint_UsuarioId";
    public static final String CM_Serie = "vstr_Serie";
    public static final String CM_Numero = "vint_Numero";

    public static final String TAG = "Cobros Manuales";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    public static final String SQLITE_TABLE_Cobros_Manuales = "m_cobros_manuales";
    private final Context mCtx;

    public static final String CREATE_TABLE_COBROS_MANUALES =
            "create table " + SQLITE_TABLE_Cobros_Manuales + " ("
                    + CM_id_Cobros_Manuales + " integer primary key autoincrement,"
                    + CM_Categoria_Movimiento + " integer,"
                    + CM_Importe + " real,"
                    + CM_Fecha_Hora + " text,"
                    + CM_Referencia + " text,"
                    + CM_Usuario + " integer,"
                    + CM_Serie + " text,"
                    + CM_Numero + " integer,"
                    + Constants._SINCRONIZAR + " integer);";

    public static final String DELETE_TABLE_COBROS_MANUALES = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Cobros_Manuales;

    public DbAdapter_Cobros_Manuales(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Cobros_Manuales open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    public long createCobrosManuales(int categoriaMovimiento, Double importe, String fechaHora, String referencia, int usuario,String serie, int numero){

        ContentValues initialValues = new ContentValues();
        initialValues.put(CM_Categoria_Movimiento,categoriaMovimiento);// 3
        initialValues.put(CM_Importe,importe);
        initialValues.put(CM_Fecha_Hora,fechaHora); //formato  igual que sqlserver.
        initialValues.put(CM_Referencia,referencia); // t o p
        initialValues.put(CM_Usuario,usuario); // id Agente
        initialValues.put(CM_Serie,serie); //
        initialValues.put(CM_Numero,numero);
        initialValues.put(Constants._SINCRONIZAR,Constants._CREADO);
        return mDb.insert(SQLITE_TABLE_Cobros_Manuales, null, initialValues);
    }
    public Cursor filterExport(){
        Cursor cursor = mDb.rawQuery("select * from "+SQLITE_TABLE_Cobros_Manuales+" where "+Constants._SINCRONIZAR+"='"+Constants._CREADO+"'",null);
        return cursor;
    }


    public long updateCobrosManuales(int estado, int id){

        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR,estado);
        return mDb.update(SQLITE_TABLE_Cobros_Manuales,
                initialValues,
                CM_id_Cobros_Manuales + "=?",
                new String[]{
                        ""+id
                });
    }





}