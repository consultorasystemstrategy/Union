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

public class DbAdapter_Impresion_Cobros {



    public static final String Imprimir_id= "_id";
    public static final String Imprimir_id_detalle = "Imprimir_id_detalle";
    public static final String Imprimir_id_establecimiento = "Imprimir_id_establecimiento";
    public static final String Imprimir_monto = "Imprimir_monto";
    public static final String Imprimir_tipo = "Imprimir_tipo";
    public static final String Imprimir_fecha = "Imprimir_fecha";
    public static final String Imprimir_cliente = "Imprimir_cliente";
    public static final String Imprimir_documento = "Imprimir_documento";
    public static final String Imprimir_fechaHora = "Imprimir_fechaHora";
    //----------
    public static final String Imprimir_liquidacion= "Imprimir_liquidacion";
    public static final String Imprimir_id_categoria_movimiento = "Imprimir_id_categoria_movimiento";
    public static final String Imprimir_estado_cobrado = "Imprimir_estado_cobrado";
    public static final String Imprimir_referencia = "Imprimir_referencia";
    public static final String Imprimir_id_usuario = "Imprimir_id_usuario";
    public static final String Imprimir_id_comprobante = "Imprimir_id_comprobante";
    public static final String Imprimir_id_plan_pago = "Imprimir_id_plan_pago";
    public static final String Imprimir_id_plan_pago_detalle = "Imprimir_id_plan_pago_detalle";
    public static final String Imprimir_estado_parcial  = "Imprimir_estado_parcial";
    public static final String Imprimir_estado_flex = "Imprimir_estado_flex";
    public static final String Imprimir_id_flex = "Imprimir_id_flex";
    public static final String Imprimir_comprobante_nombre = "Imprimir_comprobante_nombre";





    public static final String TAG = "Precio";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    public static final String SQLITE_TABLE_IMPRIMIR_COBRO = "imprimir_cobro";
    private final Context mCtx;

    public static final String CREATE_TABLE_IMPRIMIR_COBRO =
            "create table "+SQLITE_TABLE_IMPRIMIR_COBRO+" ("
                    +Imprimir_id+" integer primary key autoincrement,"
                    +Imprimir_id_detalle+" integer,"
                    +Imprimir_id_establecimiento+" integer,"
                    +Imprimir_monto+" text,"
                    +Imprimir_fecha+" text,"
                    +Imprimir_cliente+" text,"
                    +Imprimir_documento+" text,"
                    +Imprimir_liquidacion+" text,"
                    +Imprimir_id_categoria_movimiento+" text,"
                    +Imprimir_estado_cobrado+" text,"
                    +Imprimir_referencia+" text,"
                    +Imprimir_id_usuario+" text,"
                    +Imprimir_id_comprobante+" text,"
                    +Imprimir_id_plan_pago+" text,"
                    +Imprimir_id_plan_pago_detalle+" text,"
                    +Imprimir_fechaHora+" text,"
                    +Imprimir_estado_parcial+" text,"
                    +Imprimir_comprobante_nombre+" text,"
                    +Constants._SINCRONIZAR+" integer,"
                    +Imprimir_estado_flex +" integer,"
                    +Imprimir_id_flex +" text,"
                    +Imprimir_tipo+" integer);"
            ;

    public static final String DELETE_TABLE_IMPRIMIR_COBRO = "DROP TABLE IF EXISTS " + SQLITE_TABLE_IMPRIMIR_COBRO;

    public DbAdapter_Impresion_Cobros(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Impresion_Cobros open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    public long createImprimir(int id_detalle,int idEstablecimiento,double monto,int tipo,String fecha,String cliente,String documento, String fechaHora,String liquidacion,int estadoCobro,String refere
    ,int idComprobante,int idPlan, int idPlanDetalle,int estadoParcial,String nombre_comprobante){

        ContentValues initialValues = new ContentValues();
        initialValues.put(Imprimir_id_detalle,id_detalle);
        initialValues.put(Imprimir_monto,monto);
        initialValues.put(Imprimir_fecha,fecha);
        initialValues.put(Imprimir_cliente,cliente);
        initialValues.put(Imprimir_documento,documento);
        initialValues.put(Imprimir_fechaHora,fechaHora);
        initialValues.put(Imprimir_id_establecimiento,idEstablecimiento);
        initialValues.put(Imprimir_tipo,tipo);

        //---------------
        initialValues.put(Imprimir_liquidacion,liquidacion);
        initialValues.put(Imprimir_estado_cobrado,estadoCobro);
        initialValues.put(Imprimir_referencia,refere);
        initialValues.put(Imprimir_id_comprobante,idComprobante);
        initialValues.put(Imprimir_id_plan_pago,idPlan);
        initialValues.put(Imprimir_id_plan_pago_detalle,idPlanDetalle);
        initialValues.put(Imprimir_estado_parcial,estadoParcial);
        initialValues.put(Imprimir_estado_flex,Constants._CREADO);
        initialValues.put(Imprimir_comprobante_nombre,nombre_comprobante);
        initialValues.put(Constants._SINCRONIZAR,Constants._ACTUALIZADO);


        return mDb.insert(SQLITE_TABLE_IMPRIMIR_COBRO, null, initialValues);
    }

    public Cursor listarCobrosExpFlex(){

        Cursor cr = mDb.rawQuery("select * from "+SQLITE_TABLE_IMPRIMIR_COBRO+" where "+Imprimir_estado_flex+"='"+Constants._CREADO+"' and "+Imprimir_id_flex+" != '0' AND "+Imprimir_tipo+" !='"+Constants.COBRO_MANUAL+"' ",null);

        return cr;
    }

    public Cursor listarImprimirCobros(String fecha,int idEsta){

        Cursor cr = mDb.rawQuery("select * from "+SQLITE_TABLE_IMPRIMIR_COBRO+" where "+Imprimir_fecha+"='"+fecha+"' and "+Imprimir_id_establecimiento+"='"+idEsta+"' ",null);

        return cr;
    }

    public Cursor listarParaExportar(){

        Cursor cr = mDb.rawQuery("select * from "+SQLITE_TABLE_IMPRIMIR_COBRO+" where  "+Constants._SINCRONIZAR+"='"+Constants._ACTUALIZADO+"' and "+Imprimir_tipo+" !='"+Constants.COBRO_MANUAL+"'  ",null);

        return cr;
    }

    public int updateCobrosIdComprobanteVenta(int id, int idComprobanteVenta) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(Imprimir_id_comprobante,idComprobanteVenta);


        return mDb.update(SQLITE_TABLE_IMPRIMIR_COBRO, initialValues,
                Imprimir_id_comprobante + " = ?",
                new String[]{
                        "" + id
                });
    }

    public int updateCobrosIdPlanDetalle(int id, int idPago, int idPagoDetalle) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(Imprimir_id_plan_pago,idPago);
        initialValues.put(Imprimir_id_plan_pago_detalle,idPagoDetalle);


        return mDb.update(SQLITE_TABLE_IMPRIMIR_COBRO, initialValues,
                Imprimir_id_comprobante + " = ?",
                new String[]{
                        "" + id
                });
    }

    public int updateCobrosExportado(int idCobro, int estado) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR,estado);


        return mDb.update(SQLITE_TABLE_IMPRIMIR_COBRO, initialValues,
                Imprimir_id + " = ?",
                new String[]{
                        "" + idCobro
                });
    }
    public int updateCobrosExportadoFlex(int idCobro, int estado,int idFlex) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(Imprimir_estado_flex,estado);
        initialValues.put(Imprimir_id_flex,idFlex);


        return mDb.update(SQLITE_TABLE_IMPRIMIR_COBRO, initialValues,
                Imprimir_id + " = ?",
                new String[]{
                        "" + idCobro
                });
    }

}