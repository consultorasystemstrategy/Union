package union.union_vr1.Conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import union.union_vr1.Sqlite.DBAdapter_Consultar_Inventario_Anterior;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DBAdapter_Temp_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DBAdapter_Temp_Inventario;
import union.union_vr1.Sqlite.DBAdapter_Temp_Venta;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Agente_Login;
import union.union_vr1.Sqlite.DbAdapter_Cobros_Manuales;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Histo_Comprob_Anterior;
import union.union_vr1.Sqlite.DbAdapter_Resumen_Caja;
import union.union_vr1.Sqlite.DbAdapter_Ruta_Distribucion;
import union.union_vr1.Sqlite.DbAdapter_Temp_Barcode_Scanner;
import union.union_vr1.Sqlite.DbAdapter_Temp_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Temp_DatosSpinner;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Gasto;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
/**
 * Created by USUARIO on 30/06/2014.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "ProdUniondb.sqlite";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.w(DbAdaptert_Evento_Establec.TAG, DbAdaptert_Evento_Establec.CREATE_TABLE_EVENTO_ESTABLEC);
        db.execSQL(DbAdaptert_Evento_Establec.CREATE_TABLE_EVENTO_ESTABLEC);
        //Log.w(DbAdapter_Tipo_Gasto.TAG, DbAdapter_Tipo_Gasto.CREATE_TABLE_TIPO_GASTO);
        db.execSQL(DbAdapter_Tipo_Gasto.CREATE_TABLE_TIPO_GASTO);
        //Log.w(DbAdapter_Informe_Gastos.TAG, DbAdapter_Informe_Gastos.CREATE_TABLE_INFORME_GASTOS);
        db.execSQL(DbAdapter_Informe_Gastos.CREATE_TABLE_INFORME_GASTOS);
        //Log.w(DbAdapter_Precio.TAG, DbAdapter_Precio.CREATE_TABLE_PRECIO);
        db.execSQL(DbAdapter_Precio.CREATE_TABLE_PRECIO);
        //Log.w(DbAdapter_Stock_Agente.TAG, DbAdapter_Stock_Agente.CREATE_TABLE_STOCK_AGENTE);
        db.execSQL(DbAdapter_Stock_Agente.CREATE_TABLE_STOCK_AGENTE);
        //Log.w(DbAdapter_Comprob_Venta.TAG, DbAdapter_Comprob_Venta.CREATE_TABLE_COMPROB_VENTA);
        db.execSQL(DbAdapter_Comprob_Venta.CREATE_TABLE_COMPROB_VENTA);
        //Log.w(DbAdapter_Comprob_Venta_Detalle.TAG, DbAdapter_Comprob_Venta_Detalle.CREATE_TABLE_COMPROB_VENTA_DETALLE);
        db.execSQL(DbAdapter_Comprob_Venta_Detalle.CREATE_TABLE_COMPROB_VENTA_DETALLE);
        //Log.w(DbAdaptert_Agente.TAG, DbAdaptert_Agente.CREATE_TABLE_AGENTE);
        db.execSQL(DbAdapter_Agente.CREATE_TABLE_AGENTE);
        //Log.w(DbAdapter_Comprob_Cobro.TAG, DbAdapter_Comprob_Cobro.CREATE_TABLE_COMPROB_COBRO);
        db.execSQL(DbAdapter_Comprob_Cobro.CREATE_TABLE_COMPROB_COBRO);
        //Log.w(DbAdapter_Histo_Venta.TAG, DbAdapter_Histo_Venta.CREATE_TABLE_HISTO_VENTA);
        db.execSQL(DbAdapter_Histo_Venta.CREATE_TABLE_HISTO_VENTA);
        //Log.w(DbAdapter_Histo_Venta_Detalle.TAG, DbAdapter_Histo_Venta_Detalle.CREATE_TABLE_HISTO_VENTA_DETALLE);
        db.execSQL(DbAdapter_Histo_Venta_Detalle.CREATE_TABLE_HISTO_VENTA_DETALLE);
        //Log.w(DbAdapter_Histo_Comprob_Anterior.TAG, DbAdapter_Histo_Comprob_Anterior.CREATE_TABLE_HISTO_COMPROB_ANTERIOR);
        db.execSQL(DbAdapter_Histo_Comprob_Anterior.CREATE_TABLE_HISTO_COMPROB_ANTERIOR);
        //Log.w(DbAdapter_Resumen_Caja.TAG, DbAdapter_Resumen_Caja.CREATE_TABLE_RESUMEN_CAJA);
        db.execSQL(DbAdapter_Resumen_Caja.CREATE_TABLE_RESUMEN_CAJA);
        db.execSQL(DBAdapter_Temp_Venta.CREATE_TABLE_TEMP_VENTA_DETALLE);
        db.execSQL(DbAdapter_Temp_Comprob_Cobro.CREATE_TABLE_TEMP_COMPROB_COBRO);
        db.execSQL(DBAdapter_Temp_Autorizacion_Cobro.CREATE_TABLE_TEMP_Autorizacion_Cobro);
        db.execSQL(DbAdapter_Temp_Barcode_Scanner.CREATE_TABLE_Temp_scanner);
        db.execSQL(DbAdapter_Temp_Session.CREATE_TABLE_Temp_Session);
        db.execSQL(DbAdapter_Ruta_Distribucion.CREATE_TABLE_RUTA_DISTRIBUCION);
        db.execSQL(DBAdapter_Temp_Inventario.CREATE_TABLE_TEMP_VENTA_Inventario);
        db.execSQL(DBAdapter_Consultar_Inventario_Anterior.CREATE_TABLE_TEMP_CONSULTAR_Inventario);
        db.execSQL(DbAdapter_Agente_Login.CREATE_TABLE_AGENTE);
        db.execSQL(DBAdapter_Temp_Canjes_Devoluciones.CREATE_TABLE_TEMP_CANJES_DEVOLUCIONES);
        db.execSQL(DbAdapter_Cobros_Manuales.CREATE_TABLE_COBROS_MANUALES);
        db.execSQL(DbAdapter_Temp_DatosSpinner.CREATE_TABLE_Temp_Session);
        db.execSQL(DbAdapter_Temp_Establecimiento.CREATE_TABLE_TEMP_ESTABLEC);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.w(DbAdaptert_Evento_Establec.TAG, "Upgrading database from version " + oldVersion + " to "
        //        + newVersion + ", which will destroy all old data");
        db.execSQL(DbAdaptert_Evento_Establec.DELETE_TABLE_EVENTO_ESTABLEC);
        db.execSQL(DbAdapter_Tipo_Gasto.DELETE_TABLE_TIPO_GASTO);
        db.execSQL(DbAdapter_Informe_Gastos.DELETE_TABLE_INFORME_GASTOS);
        db.execSQL(DbAdapter_Precio.DELETE_TABLE_PRECIO);
        db.execSQL(DbAdapter_Stock_Agente.DELETE_TABLE_STOCK_AGENTE);
        db.execSQL(DbAdapter_Comprob_Venta.DELETE_TABLE_COMPROB_VENTA);
        db.execSQL(DbAdapter_Comprob_Venta_Detalle.DELETE_TABLE_COMPROB_VENTA_DETALLE);
        db.execSQL(DbAdapter_Agente.DELETE_TABLE_AGENTE);
        db.execSQL(DbAdapter_Comprob_Cobro.DELETE_TABLE_COMPROB_COBRO);
        db.execSQL(DbAdapter_Histo_Venta.DELETE_TABLE_HISTO_VENTA);
        db.execSQL(DbAdapter_Histo_Venta_Detalle.DELETE_TABLE_HISTO_VENTA_DETALLE);
        db.execSQL(DbAdapter_Histo_Comprob_Anterior.DELETE_TABLE_HISTO_COMPROB_ANTERIOR);
        db.execSQL(DbAdapter_Resumen_Caja.DELETE_TABLE_RESUMEN_CAJA);
        db.execSQL(DBAdapter_Temp_Venta.DELETE_TABLE_TEMP_VENTA_DETALLE);
        db.execSQL(DbAdapter_Temp_Comprob_Cobro.DELETE_TABLE_TEMP_COMPROB_COBRO);
        db.execSQL(DBAdapter_Temp_Autorizacion_Cobro.DELETE_TABLE_AUTORIZACION_COBRO);
        db.execSQL(DbAdapter_Temp_Barcode_Scanner.DELETE_TABLE_Temp_scanner);
        db.execSQL(DbAdapter_Temp_Session.DELETE_TABLE_Temp_session);
        db.execSQL(DbAdapter_Ruta_Distribucion.DELETE_TABLE_RUTA_DISTRIBUCION);
        db.execSQL(DBAdapter_Temp_Inventario.DELETE_TABLE_TEMP_VENTA_DETALLE);
        db.execSQL(DBAdapter_Consultar_Inventario_Anterior.DELETE_TABLE_TEMP_CONSULTAR_Inventario);
        db.execSQL(DbAdapter_Agente_Login.DELETE_TABLE_AGENTE_LOGIN);
        db.execSQL(DBAdapter_Temp_Canjes_Devoluciones.DELETE_TABLE_TEMP_CANJES_DEVOLUCIONES);
        db.execSQL(DbAdapter_Cobros_Manuales.DELETE_TABLE_COBROS_MANUALES);
        db.execSQL(DbAdapter_Temp_DatosSpinner.DELETE_TABLE_Temp_data_spinner);
        db.execSQL(DbAdapter_Temp_Establecimiento.DELETE_TABLE_Temp_Establec);
        onCreate(db);
    }
}
