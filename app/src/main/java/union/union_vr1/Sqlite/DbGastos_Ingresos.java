package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.Conexion.DbHelper;

/**
 * Created by Usuario on 15/12/2014.
 */
public class DbGastos_Ingresos {
    private DbHelper mDbHelper;
    private final Context mCtx;
    private SQLiteDatabase mDb;

    public DbGastos_Ingresos(Context context) {

        this.mCtx = context;
    }

    public DbGastos_Ingresos open() {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public Cursor listarTodo(int idAgente) {
 /*       DbAdapter_Comprob_Venta cv = new DbAdapter_Comprob_Venta(mCtx);
        cv.open();
        cv.deleteAllComprobVenta();
        cv.insertSomeComprobVenta();
        DbAdapter_Comprob_Cobro cc = new DbAdapter_Comprob_Cobro(mCtx);
        cc.open();
        cc.deleteAllComprobCobros();
        cc.insertSomeComprobCobros();*/
        listarGatos_Boletas_Emitidas(idAgente);
        listarGatos_Facturas_Emitidas(idAgente);
        listarGatos_Notas_Credito_Emitidas(idAgente);
        listarGatos_Facturas_Cobradas(idAgente);
        listarGatos_Boletas_Cobradas(idAgente);
        listarGatos_Boletas_Anuladas(idAgente);
        listarGatos_Facturas_Anuladas(idAgente);
        Cursor cr = mDb.rawQuery("select * from m_resumen_caja ", null);
        return cr;
    }

    public void listarGatos_Boletas_Emitidas(int idAgente) {
        Cursor cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=1 ", null);
        Cursor cr2 = mDb.rawQuery("select count(distinct(_id)),sum(cv_re_total) from m_comprob_venta where  cv_te_fecha_doc='" + getDatePhone() + "' and cv_in_id_tipo_doc=2 ", null);
        cr2.moveToFirst();
        System.out.println("here new -" + cr2.getString(0));
        if (cr1.getCount() < 1) {
            insertar(idAgente, "Boletas Emitidas", 1);
            if (cr2.getString(0) != null) {
                cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=1 ", null);
                actualizar(cr1, cr2, "rc_re_vendido");

            } else {

            }

        } else {
            actualizar(cr1, cr2, "rc_re_vendido");
        }

    }

    public Cursor listarGatos_Facturas_Emitidas(int idAgente) {
        Cursor cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=2 ", null);
        Cursor cr2 = mDb.rawQuery("select count(distinct(_id)),sum(cv_re_total) from m_comprob_venta where  cv_te_fecha_doc='" + getDatePhone() + "' and cv_in_id_tipo_doc=1 ", null);
        cr1.moveToFirst();
        cr2.moveToFirst();
        if (cr1.getCount() < 1) {
            insertar(idAgente, "Facturas Emitidas", 2);
            if (cr2.getString(0) != null) {
                cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=2 ", null);
                actualizar(cr1, cr2, "rc_re_vendido");

            } else {

            }

        } else {
            actualizar(cr1, cr2, "rc_re_vendido");
        }
        return null;
    }

    public Cursor listarGatos_Notas_Credito_Emitidas(int idAgente) {
        Cursor cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=3 ", null);
        Cursor cr2 = mDb.rawQuery("select count(distinct(_id)),sum(cv_re_total) from m_comprob_venta where  cv_te_fecha_doc='" + getDatePhone() + "' and cv_in_id_tipo_doc=1 ", null);
        cr1.moveToFirst();
        cr2.moveToFirst();
        if (cr1.getCount() < 1) {
            insertar(idAgente, "Notas de Credito Emitidas", 3);
            if (cr2.getString(0) != null) {
                cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=3 ", null);
                actualizar(cr1, cr2, "rc_re_vendido");

            } else {

            }

        } else {
            actualizar(cr1, cr2, "rc_re_vendido");
        }
        return null;
    }

    public Cursor listarGatos_Facturas_Cobradas(int idAgente) {
        Cursor cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=4 ", null);
        Cursor cr2 = mDb.rawQuery("select count(distinct(_id)),sum(cc_re_monto_cobrado) from m_comprob_cobro where  cc_te_fecha_cobro='" + getDatePhone() + "' and cc_in_estado_cobro=1 and cc_te_desc_tipo_doc='FACTURA';", null);
        cr1.moveToFirst();
        cr2.moveToFirst();
        if (cr1.getCount() < 1) {
            insertar(idAgente, "Facturas Cobradas", 4);
            if (cr2.getString(0) != null) {
                cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=4 ", null);
                actualizar(cr1, cr2, "rc_re_cobrado");

            } else {

            }

        } else {
            actualizar(cr1, cr2, "rc_re_cobrado");
        }
        return null;
    }

    public Cursor listarGatos_Boletas_Cobradas(int idAgente) {
        Cursor cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=5 ", null);
        Cursor cr2 = mDb.rawQuery("select count(distinct(_id)),sum(cc_re_monto_cobrado) from m_comprob_cobro where  cc_te_fecha_cobro='" + getDatePhone() + "' and cc_in_estado_cobro=1 and cc_te_desc_tipo_doc='BOLETA'", null);
        cr1.moveToFirst();
        cr2.moveToFirst();

        if (cr1.getCount() < 1) {
            insertar(idAgente, "Boletas Cobradas", 5);
            if (cr2.getString(0) != null) {
                cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=5 ", null);
                actualizar(cr1, cr2, "rc_re_cobrado");

            } else {

            }

        } else {
            actualizar(cr1, cr2, "rc_re_cobrado");
        }
        return null;
    }

    public Cursor listarGatos_Boletas_Anuladas(int idAgente) {
        Cursor cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=6 ", null);
        Cursor cr2 = mDb.rawQuery("select count(distinct(_id)),sum(cv_re_total) from m_comprob_venta where  cv_te_fecha_doc='" + getDatePhone() + "' and cv_in_id_tipo_doc=2  and cv_in_estado_comp='0'", null);
        cr1.moveToFirst();
        cr2.moveToFirst();
        if (cr1.getCount() < 1) {
            insertar(idAgente, "Boletas Anuladas", 6);
            if (cr2.getString(0) != null) {
                cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=6 ", null);
                actualizar(cr1, cr2, "rc_re_pagado");

            } else {

            }

        } else {
            actualizar(cr1, cr2, "rc_re_pagado");
        }
        return null;
    }

    public Cursor listarGatos_Facturas_Anuladas(int idAgente) {
        Cursor cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=7", null);
        Cursor cr2 = mDb.rawQuery("select count(distinct(_id)),sum(cv_re_total) from m_comprob_venta where  cv_te_fecha_doc='" + getDatePhone() + "' and cv_in_id_tipo_doc=1  and cv_in_estado_comp='0'", null);
        cr1.moveToFirst();
        cr2.moveToFirst();
        if (cr1.getCount() < 1) {
            insertar(idAgente, "Facturas  Anuladas", 7);
            if (cr2.getString(0) != null) {
                cr1 = mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='" + getDatePhone() + "' and rc_te_tipo_gi=7 ", null);
                actualizar(cr1, cr2, "rc_re_pagado");

            } else {

            }

        } else {
            actualizar(cr1, cr2, "rc_re_pagado");
        }
        return null;
    }

    public Cursor listarIngresosGastos(int liquidacion){
        Cursor mCursor = null;

        mCursor = mDb.rawQuery("SELECT 1 as _id, 'Facturas Emitidas' AS comprobante,\n" +
                "\tCOUNT(*) AS n,\n" +
                "\tROUND(SUM(cv_re_total)) AS emitidas, \n" +
                "\t(SELECT ROUND(SUM(cv_re_total)) FROM m_comprob_venta WHERE cv_in_id_tipo_doc = '1' AND cv_in_id_forma_pago = '1' AND id_liquidacion = '"+liquidacion+"') AS pagado,\n" +
                "\t0 AS cobrado \n" +
                "FROM m_comprob_venta\n" +
                "WHERE cv_in_id_tipo_doc = '1'\n" +
                "AND id_liquidacion  = '"+liquidacion+"'\n" +
                "UNION\n" +
                "SELECT 2 as _id, 'Boletas Emitidas' AS comprobante,\n" +
                "\tCOUNT(*) AS n,\n" +
                "\tROUND(SUM(cv_re_total)) AS emitidas, \n" +
                "\t(SELECT ROUND(SUM(cv_re_total)) FROM m_comprob_venta WHERE cv_in_id_tipo_doc = '2' AND cv_in_id_forma_pago = '1' AND id_liquidacion = '"+liquidacion+"') AS pagado,\n" +
                "\t0 AS cobrado \n" +
                "FROM m_comprob_venta\n" +
                "WHERE cv_in_id_tipo_doc = '2'\n" +
                "AND id_liquidacion  = '"+liquidacion+"'\n" +
                "\n" +
                "UNION \n" +
                "\n" +
                "SELECT \t 3 as _id, 'Facturas Cobradas' AS comprobante,\n" +
                "\t(SELECT COUNT(DISTINCT(cc_in_id_comprob)) FROM m_comprob_cobro WHERE UPPER(cc_te_desc_tipo_doc) like '%FACTURA%' AND  cc_in_estado_cobro='0' AND '"+getDatePhone()+"') AS n,\n" +
                "\t0 AS emitidas,\n" +
                "\t0 AS pagado,\n" +
                "\t(SELECT ROUND(SUM(cc_re_monto_cobrado)) FROM m_comprob_cobro WHERE UPPER(cc_te_desc_tipo_doc) like '%FACTURA%' AND cc_in_estado_cobro='0' AND '"+getDatePhone()+"') AS cobrado\n" +
                "\n" +
                "UNION \n" +
                "\n" +
                "SELECT \t 4 as _id, 'Boletas Cobradas' AS comprobante,\n" +
                "\t(SELECT COUNT(DISTINCT(cc_in_id_comprob)) FROM m_comprob_cobro WHERE UPPER(cc_te_desc_tipo_doc) like '%BOLETA%' AND  cc_in_estado_cobro='0' AND '"+getDatePhone()+"') AS n,\n" +
                "\t0 AS emitidas,\n" +
                "\t0 AS pagado,\n" +
                "\t(SELECT ROUND(SUM(cc_re_monto_cobrado)) FROM m_comprob_cobro WHERE UPPER(cc_te_desc_tipo_doc) like '%BOLETA%'  AND cc_in_estado_cobro='0' AND '"+getDatePhone()+"') AS cobrado",null);

        if (mCursor!=null){
            mCursor.moveToFirst();
        }
        if (mCursor.getCount()==0){
            //CURSOR VACÍO
        }

        return  mCursor;
    }

    public void insertar(int idAgente, String descripcion, int tipo_gi) {

        mDb.execSQL("insert into m_resumen_caja values (null," + idAgente + ",'" + descripcion + "'," + tipo_gi + ",'','','','','" + getDatePhone() + "')");

    }

    public boolean actualizar(Cursor cr, Cursor actualizar, String columna) {
        cr.moveToFirst();
        actualizar.moveToFirst();
        int idResumen = cr.getInt(0);
        int cantidad = actualizar.getInt(0);
        int vendido = actualizar.getInt(1);
        int pagado;
        mDb.execSQL("update m_resumen_caja set rc_in_cantidad='" + cantidad + "', " + columna + "='" + vendido + "' where _id='" + idResumen + "'");
        boolean estado = false;
        return estado;
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }


}
