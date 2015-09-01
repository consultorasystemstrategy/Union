package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;

public class DbAdapter_Comprob_Venta_Detalle {

    //ESTOS CAMPOS NOS OBTIENE EL PROCEDIMIENTO
    public static final String CD_comp_detalle = "_id";
    public static final String CD_id_comprob = "cd_in_id_comprob";

    //FALTA EL COMPROBANTE DE VENTA DETALLE ID
    public static final String CD_id_producto = "cd_in_id_producto";
    public static final String CD_nom_producto = "cd_te_nom_producto";
    public static final String CD_cantidad = "cd_in_cantidad";
    public static final String CD_importe = "cd_re_importe";
    public static final String CD_costo_venta = "cd_re_costo_venta";
    public static final String CD_precio_unit = "cd_re_precio_unit";
    public static final String CD_prom_anterior = "cd_te_prom_anterior";
    public static final String CD_devuelto = "cd_te_devuelto";
    //este campo es redundante[lo mismo que precio unitario (preguntar)]
    public static final String CD_valor_unidad = "cd_in_valor_unidad";
    public static final String estado_sincronizacion = "estado_sincronizacion";

    //PARA FINES DE EXPORTACIÓN
    public static final String CD_id_comprob_real = "cd_in_id_comprob_real";

    public static final String TAG = "Comprob_Venta_Detalle";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "ProdUnion.sqlite";
    private static final String SQLITE_TABLE_Comprob_Venta_Detalle = "m_comprob_venta_detalle";
    private final Context mCtx;

    public static final String CREATE_TABLE_COMPROB_VENTA_DETALLE =
            "create table "+SQLITE_TABLE_Comprob_Venta_Detalle+" ("
                    +CD_comp_detalle+" integer primary key autoincrement,"
                    +CD_id_comprob+" integer,"
                    +CD_id_producto+" integer,"
                    +CD_nom_producto+" text,"
                    +CD_cantidad+" integer,"
                    +CD_importe+" real,"
                    +CD_costo_venta+" real,"
                    +CD_precio_unit+" real,"
                    +CD_prom_anterior+" text,"
                    +CD_devuelto+" text,"
                    +CD_valor_unidad+" integer, "
                    +CD_id_comprob_real+" integer, "
                    +estado_sincronizacion+" integer);";

    public static final String DELETE_TABLE_COMPROB_VENTA_DETALLE = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Comprob_Venta_Detalle;

    public DbAdapter_Comprob_Venta_Detalle(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Comprob_Venta_Detalle open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createComprobVentaDetalle(
            int id_comprob, int id_producto, String nom_producto, int cantidad, double importe,
            double costo_venta, double precio_unit, String prom_anterior, String devuelto,
            int valor_unidad){

        ContentValues initialValues = new ContentValues();
        initialValues.put(CD_id_comprob,id_comprob);
        initialValues.put(CD_id_producto,id_producto);
        initialValues.put(CD_nom_producto,nom_producto);
        initialValues.put(CD_cantidad,cantidad);
        initialValues.put(CD_importe,importe);
        initialValues.put(CD_costo_venta,costo_venta);
        initialValues.put(CD_precio_unit,precio_unit);
        initialValues.put(CD_prom_anterior,prom_anterior);
        initialValues.put(CD_devuelto,devuelto);
        initialValues.put(CD_valor_unidad,valor_unidad);
        initialValues.put(Constants._SINCRONIZAR, Constants._CREADO);

        return mDb.insert(SQLITE_TABLE_Comprob_Venta_Detalle, null, initialValues);
    }

    public int updateComprobVentaDetalleReal(int idComprobante, int idReal){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CD_id_comprob_real, idReal);

        return mDb.update(SQLITE_TABLE_Comprob_Venta_Detalle, initialValues,
                CD_id_comprob+"=?",new String[]{""+idComprobante});
    }

    public void changeEstadoToExport(String[] idComprobanteDetalle, int estadoSincronizacion){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR,estadoSincronizacion);

        String signosInterrogacion = "";
        for (int i=0; i<idComprobanteDetalle.length; i++){
            if (i==idComprobanteDetalle.length-1)
            {
                signosInterrogacion+= "?";
            }else {
                signosInterrogacion+= "? OR ";
            }

        }

        Log.d("SIGNOS INTERROGACIÓN", signosInterrogacion);
        int cantidadRegistros = mDb.update(SQLITE_TABLE_Comprob_Venta_Detalle, initialValues,
                CD_id_comprob+"= "+ signosInterrogacion,idComprobanteDetalle);


        Log.d("REGISTROS EXPORTADOS ", ""+cantidadRegistros);
    }

    public void updateComprobVentaDetalle2(String idorig, String iddest, String vals){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CD_id_comprob,iddest);

        mDb.update(SQLITE_TABLE_Comprob_Venta_Detalle, initialValues,
                CD_id_comprob+"=? AND "+CD_importe+"=?",new String[]{idorig, vals});
    }

    public boolean deleteAllComprobVentaDetalle() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Comprob_Venta_Detalle, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public boolean deleteAllComprobVentaDetalleById(String id) {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Comprob_Venta_Detalle, CD_comp_detalle+"=?", new String[]{id});
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchComprobVentaDetalleByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta_Detalle, new String[] {CD_comp_detalle,
                            CD_id_comprob, CD_nom_producto, CD_cantidad, CD_precio_unit, CD_importe},
                    null, null, null, null, null);
        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Comprob_Venta_Detalle, new String[] {CD_comp_detalle,
                            CD_id_comprob, CD_nom_producto, CD_cantidad, CD_precio_unit, CD_importe},
                    CD_id_comprob + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllComprobVentaDetalle() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta_Detalle, new String[] {CD_comp_detalle,
                        CD_id_comprob, CD_nom_producto, CD_cantidad, CD_precio_unit, CD_importe},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllComprobVentaDetalle0() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta_Detalle, new String[] {CD_comp_detalle,
                        CD_id_comprob, CD_nom_producto, CD_cantidad, CD_precio_unit, CD_importe,
                        CD_prom_anterior, CD_devuelto},
                CD_id_comprob + " = 0", null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllComprobVentaDetalle0A() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta_Detalle, new String[] {CD_comp_detalle,
                        CD_id_comprob, CD_id_producto, CD_nom_producto, CD_cantidad, CD_precio_unit, CD_importe, CD_id_comprob_real},
                CD_id_comprob + " = 0", null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllComprobVentaDetalleByIdComp(int id) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta_Detalle, new String[] {CD_comp_detalle,
                        CD_id_comprob, CD_id_producto, CD_costo_venta, CD_nom_producto, CD_cantidad,
                        CD_precio_unit, CD_importe, CD_prom_anterior, CD_devuelto, CD_id_comprob_real},
                CD_id_comprob + " = " + id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor filterExport() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Comprob_Venta_Detalle, new String[] {CD_comp_detalle,
                        CD_id_comprob, CD_id_producto, CD_costo_venta, CD_nom_producto, CD_cantidad,
                        CD_precio_unit, CD_importe, CD_prom_anterior, CD_devuelto, CD_valor_unidad, CD_id_comprob_real},
                Constants._SINCRONIZAR + " = " + Constants._CREADO + " OR " + Constants._SINCRONIZAR + " = " + Constants._ACTUALIZADO, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



}