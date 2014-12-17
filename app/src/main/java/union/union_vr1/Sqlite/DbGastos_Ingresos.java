package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    public  DbGastos_Ingresos(Context context){

        this.mCtx=context;
    }
    public DbGastos_Ingresos open(){
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public Cursor listarTodo(int idAgente){

        listarGatos_Boletas_Emitidas(idAgente);
        /*listarGatos_Facturas_Emitidas(idAgente);
        listarGatos_Notas_Credito_Emitidas(idAgente);
        listarGatos_Facturas_Cobradas(idAgente);
        listarGatos_Boletas_Cobradas(idAgente);
        listarGatos_Boletas_Anuladas(idAgente);
        listarGatos_Facturas_Anuladas(idAgente);*/
        Cursor cr = mDb.rawQuery("select * from m_resumen_caja ",null);

        return cr;
    }
    public void listarGatos_Boletas_Emitidas(int idAgente){
        Cursor cr1=mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=1 ",null);
        Cursor cr2 = mDb.rawQuery("select count(distinct(_id)),sum(cv_re_total) from m_comprob_venta where cv_in_id_agente='"+idAgente+"' and cv_te_fecha_doc='"+getDatePhone()+"'",null);
        cr1.moveToFirst();
        cr2.moveToFirst();
        System.out.println("here new "+cr1.getString(0)+"-"+cr2.getString(0));
        if(cr1.getString(0)==null){
            insertar(idAgente,"Boletas Emitidas",1);
            if(cr2.getString(0)!=null){
                cr1=mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=1 ",null);
                actualizar(cr1,cr2);

            }else{

            }

        }
        else{
            actualizar(cr1,cr2);
        }

    }
    public Cursor listarGatos_Facturas_Emitidas(int idAgente){
        Cursor cr1 = mDb.rawQuery("select distinct(mv._id),(cv_re_total) from m_comprob_venta mv, m_comprob_cobro cc where mv.cv_in_id_agente=cc.cc_in_id_agente and cc.cc_in_id_agente=1 and mv.cv_te_fecha_doc='"+getDatePhone()+"' and mv.cv_in_id_tipo_doc=2",null);
        cr1.moveToFirst();
        if(cr1.getString(0)==null){
            insertar(idAgente,"Facturas Emitidas",2);

        }
        else{
            //  actualizar(cr1);
        }
        return null;
    }
    public Cursor listarGatos_Notas_Credito_Emitidas(int idAgente){
        Cursor cr1 = mDb.rawQuery("select distinct(mv._id),(cv_re_total) from m_comprob_venta mv, m_comprob_cobro cc where mv.cv_in_id_agente=cc.cc_in_id_agente and cc.cc_in_id_agente=1 and mv.cv_te_fecha_doc='"+getDatePhone()+"' and mv.cv_in_id_tipo_doc=1",null);
        cr1.moveToFirst();
        if(cr1.getString(0)==null){
            insertar(idAgente,"Notas de Credito Emitidas",3);

        }
        else{
            //actualizar(cr1);
        }
        return null;
    }
    public Cursor listarGatos_Facturas_Cobradas(int idAgente){
        Cursor cr1 = mDb.rawQuery("select distinct(mv._id),(cv_re_total) from m_comprob_venta mv, m_comprob_cobro cc where mv.cv_in_id_agente=cc.cc_in_id_agente and cc.cc_in_id_agente=1 and mv.cv_te_fecha_doc='"+getDatePhone()+"' and mv.cv_in_id_tipo_doc=1",null);
        cr1.moveToFirst();
        if(cr1.getString(0)==null){
            insertar(idAgente,"Facturas Cobradas",4);

        }
        else{
            // actualizar(cr1);
        }
        return null;
    }
    public Cursor listarGatos_Boletas_Cobradas(int idAgente){
        Cursor cr1 = mDb.rawQuery("select distinct(mv._id),(cv_re_total) from m_comprob_venta mv, m_comprob_cobro cc where mv.cv_in_id_agente=cc.cc_in_id_agente and cc.cc_in_id_agente=1 and mv.cv_te_fecha_doc='"+getDatePhone()+"' and mv.cv_in_id_tipo_doc=1",null);
        cr1.moveToFirst();
        if(cr1.getString(0)==null){
            insertar(idAgente,"Boletas Cobradas",5);

        }
        else{
            //  actualizar(cr1);
        }
        return null;
    }
    public Cursor listarGatos_Boletas_Anuladas(int idAgente){
        Cursor cr1 = mDb.rawQuery("select distinct(mv._id),(cv_re_total) from m_comprob_venta mv, m_comprob_cobro cc where mv.cv_in_id_agente=cc.cc_in_id_agente and cc.cc_in_id_agente=1 and mv.cv_te_fecha_doc='"+getDatePhone()+"' and mv.cv_in_id_tipo_doc=1",null);
        cr1.moveToFirst();
        if(cr1.getString(0)==null){
            insertar(idAgente,"Boletas Anuladas",6);

        }
        else{
            //  actualizar(cr1);
        }
        return null;
    }
    public Cursor listarGatos_Facturas_Anuladas(int idAgente){
        Cursor cr1 = mDb.rawQuery("select distinct(mv._id),(cv_re_total) from m_comprob_venta mv, m_comprob_cobro cc where mv.cv_in_id_agente=cc.cc_in_id_agente and cc.cc_in_id_agente=1 and mv.cv_te_fecha_doc='"+getDatePhone()+"' and mv.cv_in_id_tipo_doc=1",null);
        cr1.moveToFirst();
        if(cr1.getString(0)==null){
            insertar(idAgente,"Facturas Anuladas",7);

        }
        else{
            //   actualizar(cr1);
        }
        return null;
    }
    public void insertar(int idAgente,String descripcion,int tipo_gi){

        mDb.execSQL("insert into m_resumen_caja values ("+idAgente+",'"+descripcion+"',"+tipo_gi+",'','','','','"+getDatePhone()+"')");

    }
    public boolean actualizar(Cursor cr,Cursor actualizar){
        cr.moveToFirst();
        actualizar.moveToFirst();
        int idResumen = cr.getInt(0);
        int cantidad = actualizar.getInt(0);
        int vendido = actualizar.getInt(1);
        int pagado;


        mDb.execSQL("update m_resumen_caja set rc_in_cantidad='"+cantidad+"', rc_re_vendido='"+vendido+"', rc_re_pagado='' where _id='"+idResumen+"'  ");

        boolean estado = false;
        return estado;
    }
    private String getDatePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }


}
