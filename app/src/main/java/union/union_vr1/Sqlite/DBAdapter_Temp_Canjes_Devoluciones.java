package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Utils.Utils;

/**
 * Created by Usuario on 15/12/2014.
 */

public class DBAdapter_Temp_Canjes_Devoluciones {

    public static final String temp_id_canjes_devoluciones = "_id";
    public static final String temp_id_cabecera = "temp_id_cabecera";
    public static final String temp_id_comprob = "temp_in_id_comprob";
    public static final String temp_id_producto = "temp_in_id_producto";
    public static final String temp_nom_producto = "temp_te_nom_producto";
    public static final String temp_codigo_producto = "temp_te_codigo_producto";
    public static final String temp_cantidad = "temp_in_cantidad";
    public static final String temp_precio_unit = "temp_re_precio_unit";
    public static final String temp_importe = "temp_importe";
    public static final String temp_lote = "temp_lote";
    public static final String temp_fecha_emision = "temp_fecha_emision";
    public static final String temp_cliente = "temp_cliente";
    public static final String temp_id_establecimiento = "temp_id_establecimiento";
    public static final String temp_id_documento = "temp_id_documento";
    public static final String temp_id_motivo = "temp_id_motivo";
    public static final String temp_estado_producto = "temp_id_estado_producto";
    public static final String temp_liquidacion = "temp_liquidacion";
    public static final String temp_id_detalle = "temp_id_detalle";
    public static final String temp_id_guia= "temp_id_guia";
    public static final String temp_id_comprob_venta= "temp_id_comprob_venta";
    public static final String temp_id_forma= "temp_id_forma";
    public static final String temp_unidad = "temp_unidad";

    public static final String temp_estado = "temp_estado";

    public DecimalFormat f = new DecimalFormat("0.00");
    public static final String TAG = "Temp_Venta_Detalle";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String SQLITE_TABLE_Temp_Canjes_Devoluciones = "m_temp_canjes_devoluciones";
    private final Context mCtx;

    public static final String CREATE_TABLE_TEMP_CANJES_DEVOLUCIONES =
            "create table " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " ("
                    + temp_id_canjes_devoluciones + " integer primary key autoincrement,"
                    + temp_id_cabecera + " text,"
                    + temp_id_comprob + " text,"
                    + temp_id_producto + " text,"
                    + temp_nom_producto + " text,"
                    + temp_codigo_producto + " text,"
                    + temp_cantidad + " text,"
                    + temp_precio_unit + " text,"
                    + temp_importe + " text,"
                    + temp_lote + " text,"
                    + temp_fecha_emision + " text,"
                    + temp_cliente + " text,"
                    + temp_id_establecimiento + " text,"
                    + temp_id_documento + " text,"
                    + temp_id_motivo + " text,"
                    + temp_estado_producto + " text,"
                    + temp_id_detalle + " text,"
                    + temp_id_guia + " text,"
                    + temp_liquidacion + " text,"
                    + temp_id_comprob_venta + " text,"
                    + temp_id_forma + " text,"
                    + temp_unidad + " text,"
                    + Constants._SINCRONIZAR+ " text,"
                    + temp_estado + " integer);";


    public static final String DELETE_TABLE_TEMP_CANJES_DEVOLUCIONES = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Temp_Canjes_Devoluciones;

    public DBAdapter_Temp_Canjes_Devoluciones(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter_Temp_Canjes_Devoluciones open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTempCanjesDevoluciones(String id_comprob, String id_producto, String nom_producto, String codigo_producto, String cantidad, String precio_unit, String importe, String lote, String fecha_emision, String cliente,
                                             String id_establecimiento, String id_documento, String id_motivo, String estado_producto, String estado, String liquidacion, String idDetalle, String id_comprobante_venta,String id_forma, String unidad) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_id_comprob, id_comprob);
        initialValues.put(temp_id_producto, id_producto);
        initialValues.put(temp_nom_producto, nom_producto);
        initialValues.put(temp_codigo_producto, codigo_producto);
        initialValues.put(temp_cantidad, cantidad);
        initialValues.put(temp_precio_unit, precio_unit);
        initialValues.put(temp_importe, importe);
        initialValues.put(temp_lote, lote);
        initialValues.put(temp_fecha_emision, fecha_emision);
        initialValues.put(temp_cliente, cliente);
        initialValues.put(temp_id_establecimiento, id_establecimiento);
        initialValues.put(temp_id_documento, id_documento);
        initialValues.put(temp_id_motivo, id_motivo);
        initialValues.put(temp_estado_producto, estado_producto);
        initialValues.put(temp_estado, estado);
        initialValues.put(temp_liquidacion, liquidacion);
        initialValues.put(temp_id_detalle, idDetalle);
        initialValues.put(temp_fecha_emision, getDatePhone());
        initialValues.put(temp_id_comprob_venta, id_comprobante_venta);
        initialValues.put(temp_id_forma, id_forma);
        initialValues.put(Constants._SINCRONIZAR,Constants._CREADO);
        initialValues.put(temp_unidad, unidad);

        return mDb.insert(SQLITE_TABLE_Temp_Canjes_Devoluciones, null, initialValues);
    }

    public void updateIdCabecera( String establec,String ESTADO) {
        String id = "";
        id = getIdForHeader(getDatePhone(), establec);
        Cursor op = getAllOperacionByEstablec(establec,ESTADO);
        if (op !=null) {

            while (op.moveToNext()) {
                Log.d("COUNTDETALLE",""+op.getCount());
                ContentValues initialValues = new ContentValues();
                initialValues.put(temp_id_cabecera, id);
                mDb.update(SQLITE_TABLE_Temp_Canjes_Devoluciones, initialValues,
                        temp_id_canjes_devoluciones + "=?", new String[]{op.getString(op.getColumnIndexOrThrow(temp_id_canjes_devoluciones))});

            }
        }

    }
    public int updateIdDetalle( String id,int idGuia) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(temp_id_guia, idGuia);
                return mDb.update(SQLITE_TABLE_Temp_Canjes_Devoluciones, initialValues,
                        temp_id_canjes_devoluciones + "=?", new String[]{id});
    }
    public int updateEstadoExport( String id) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR, Constants._EXPORTADO+"");
        return mDb.update(SQLITE_TABLE_Temp_Canjes_Devoluciones, initialValues,
                temp_id_canjes_devoluciones + "=?", new String[]{id});
    }
    public int updateEstadoExportAfter( String id) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR, Constants._ACTUALIZADO+"");
        return mDb.update(SQLITE_TABLE_Temp_Canjes_Devoluciones, initialValues,
                temp_id_canjes_devoluciones + "=?", new String[]{id});
    }

    public String getIdForHeader(String fecha, String establec) {
        String id = "";
        Cursor cursor = mDb.rawQuery("select MAX(" + temp_id_cabecera + ") as " + temp_id_cabecera + " from " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " where " + temp_fecha_emision + "='" + fecha + "' and " + temp_id_establecimiento + "='" + establec + "'", null);
        cursor.moveToFirst();
        try {
            String[] spl = cursor.getString(cursor.getColumnIndexOrThrow(temp_id_cabecera)).split("\\*");
            int count = Integer.parseInt(spl[2]);
            count = count + 1;
            id = "" + fecha + "*" + establec + "*" + count;

        } catch (NullPointerException e) {
            id = "" + fecha + "*" + establec + "*" + 1;
        }


        return id;
    }
    public Cursor getAllOperacionEstablecimiento() {
        Cursor cursor = mDb.rawQuery("select DISTINCT("+temp_id_establecimiento+") from " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " where "+Constants._SINCRONIZAR+"='"+Constants._ACTUALIZADO+"'", null);
        return cursor;
    }
    public Cursor getAllOperacionByEstablec(String establec,String ESTADO) {
        Cursor cursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " where " + temp_id_establecimiento + "='" + establec + "' and "+temp_fecha_emision+"='"+getDatePhone()+"' and "+Constants._SINCRONIZAR+"='"+ESTADO+"'", null);
        return cursor;
    }


    public Cursor getAllOperacion(String idOperacion,String ESTADO) {
        Cursor cursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " where " + temp_id_cabecera + "='" + idOperacion + "' and "+Constants._SINCRONIZAR+"='"+ESTADO+"' ", null);
        return cursor;
    }

    public Cursor listarCanjes(String establec) {
        Cursor cursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " where " + temp_id_motivo + "='2' and " + temp_id_establecimiento + "='" + establec + "' and " + temp_fecha_emision + "='" + getDatePhone() + "' and "+Constants._SINCRONIZAR+"='"+Constants._CREADO+"';", null);
        return cursor;
    }

    public String[] getInforHeader(String idEstablecimiento) {
        Cursor cabecera;
        cabecera = mDb.rawQuery("select * from m_evento_establec where ee_in_id_establec ='" + idEstablecimiento + "'", null);
        String cliente = "", documento = "";
        if (cabecera.moveToFirst()) {
            cliente = cabecera.getString(cabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_nom_cliente));
            documento = cabecera.getString(cabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_doc_cliente));
        }
        return new String[]{cliente, documento};
    }

    public Double[] getInfoCanje(String establec) {
        Cursor cursor = mDb.rawQuery("select sum(" + temp_importe + ") as " + temp_importe + "," + temp_id_cabecera + " from " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " where " + temp_id_motivo + "='2' and " + temp_id_establecimiento + "='" + establec + "' and " + temp_fecha_emision + "='" + getDatePhone() + "' and "+Constants._SINCRONIZAR+"='"+Constants._CREADO+"';", null);
        double total = 0.0000;
        double igv = 0.0000;
        double base = 0.0000;
        cursor.moveToFirst();
        if (cursor.getDouble(cursor.getColumnIndexOrThrow(temp_importe)) > 0) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow(temp_importe));
            base = total / 1.18;
            igv = total - base;
            Log.d("INFO", ":Total: " + f.format(total) + " Igv: " + f.format(igv) + " BASE: " + f.format(base) + "IDCOMPROBANTE " + cursor.getString(cursor.getColumnIndexOrThrow(temp_id_cabecera)));
        }
        return new Double[]{Double.parseDouble(Utils.replaceComa(f.format(total))), Double.parseDouble(Utils.replaceComa(f.format(base))), Double.parseDouble(Utils.replaceComa(f.format(igv)))};
    }

    public String[] getInfoCabeceraOperacion(String establec,String ESTADO) {
        Cursor cursor = mDb.rawQuery("select sum(" + temp_importe + ") as " + temp_importe + "," + temp_id_cabecera + " from " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " where " + temp_id_establecimiento + "='" + establec + "' and " + temp_fecha_emision + "='" + getDatePhone() + "' and "+Constants._SINCRONIZAR+"='"+ESTADO+"';", null);
        double total = 0.0000;
        double igv = 0.0000;
        double base = 0.0000;
        String idCabecera = "";
        cursor.moveToFirst();
        if (cursor.getDouble(cursor.getColumnIndexOrThrow(temp_importe)) > 0) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow(temp_importe));
            base = total / 1.18;
            igv = total - base;
            idCabecera = cursor.getString(cursor.getColumnIndexOrThrow(temp_id_cabecera));
            Log.d("INFO", ":Total: " + f.format(total) + " Igv: " + f.format(igv) + " BASE: " + f.format(base) + "IDCOMPROBANTE " + cursor.getString(cursor.getColumnIndexOrThrow(temp_id_cabecera)));
        }
        return new String[]{Double.parseDouble(Utils.replaceComa(f.format(total))) + "", Double.parseDouble(Utils.replaceComa(f.format(base))) + "", Double.parseDouble(Utils.replaceComa(f.format(igv))) + "", idCabecera};
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }
    public Cursor listarDevolucionesMantenimiento(String establec) {
        Cursor cursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " where   " + temp_id_establecimiento + "='" + establec + "' and " + temp_fecha_emision + "='" + getDatePhone() + "' ;", null);
        return cursor;
    }
    public Cursor listarDevoluciones(String establec) {
        Cursor cursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " where " + temp_id_motivo + "='1' and " + temp_id_establecimiento + "='" + establec + "' and " + temp_fecha_emision + "='" + getDatePhone() + "' and "+Constants._SINCRONIZAR+"='"+Constants._CREADO+"';", null);
        return cursor;
    }
    public Cursor listarDevolucionesImpresion(String establec) {
        Cursor cursor = mDb.rawQuery("select distinct("+temp_id_guia+") as "+temp_id_canjes_devoluciones+","+temp_cliente+","+temp_id_forma+",sum("+temp_importe+") as "+temp_importe+"   from " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " where "  + temp_id_establecimiento + "='" + establec + "' and " + temp_fecha_emision + "='" + getDatePhone() + "';", null);
        return cursor;
    }

    public Double[] getInfoDevoluciones(String establec) {
        double total = 0.0000;
        double igv = 0.00000;
        double base = 0.00000;
        Cursor cursor = mDb.rawQuery("select sum(" + temp_importe + ") as " + temp_importe + " from " + SQLITE_TABLE_Temp_Canjes_Devoluciones + " where " + temp_id_motivo + "='1' and " + temp_id_establecimiento + "='" + establec + "' and " + temp_fecha_emision + "='" + getDatePhone() + "' and "+Constants._SINCRONIZAR+"='"+Constants._CREADO+"';", null);
        cursor.moveToFirst();

        if (cursor.getDouble(cursor.getColumnIndexOrThrow(temp_importe)) > 0) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow(temp_importe));
            base = total / 1.18;
            igv = total - base;
            Log.d("INFO", ":Total: " + f.format(total) + " Igv: " + f.format(igv) + " BASE: " + f.format(base));
        }
        Log.d("INFO", ":Total: " + f.format(total) + " Igv: " + f.format(igv) + " BASE: " + f.format(base));
        return new Double[]{Double.parseDouble(Utils.replaceComa(f.format(total))), Double.parseDouble(Utils.replaceComa(f.format(base))), Double.parseDouble(Utils.replaceComa(f.format(igv)))};
    }

    public void updateTempCanjesDevoluciones2(String idorig, String iddest, String vals) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_id_comprob, iddest);

        mDb.update(SQLITE_TABLE_Temp_Canjes_Devoluciones, initialValues,
                temp_id_canjes_devoluciones + "=?", new String[]{idorig});
    }

    public void updateTempCanjesDevoluciones(long id_temp_venta_detalle, int cantidad, double total) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(temp_cantidad, cantidad);
        initialValues.put(temp_importe, total);

        mDb.update(SQLITE_TABLE_Temp_Canjes_Devoluciones, initialValues,
                temp_id_canjes_devoluciones + "=?", new String[]{"" + id_temp_venta_detalle});


    }

    public int truncateCanjesDevoluciones() {
        Cursor cursor = mDb.rawQuery("select * from " + SQLITE_TABLE_Temp_Canjes_Devoluciones + "", null);
        if (cursor.moveToFirst()) {
            return mDb.delete(SQLITE_TABLE_Temp_Canjes_Devoluciones, null, null);
        } else {
            return 1;
        }

    }

    public int deleteCanjesDevoluciones(int id) {
        return mDb.delete(SQLITE_TABLE_Temp_Canjes_Devoluciones, temp_id_canjes_devoluciones + " = ?", new String[]{id + ""});
    }


}
