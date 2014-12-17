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

        //Insertar Datos al comprobante de venta

        mDb.execSQL("insert into m_comprob_venta values ( null,1, 1, 1, 1, '0005', '5A', 5, 10, 0, 50, '17/12/2014', '08:10:00', 1, 0,1);");
        mDb.execSQL("insert into m_comprob_venta values ( null,1, 1, 1, 1, '0005', '5A', 5, 10, 0, 50, '17/12/2014', '08:10:00', 1, 0,1);");
        mDb.execSQL("insert into m_comprob_venta values ( null,1, 1, 1, 1, '0005', '5A', 5, 10, 0, 150, '17/12/2014', '08:10:00', 1, 0,1);");
        mDb.execSQL("insert into m_comprob_venta values ( null,1, 2, 1, 1, '0005', '5A', 5, 10, 0, 150, '17/12/2014', '08:10:00', 1, 0,1);");
        mDb.execSQL("insert into m_comprob_venta values ( null,1, 2, 1, 1, '0005', '5A', 5, 10, 0, 50, '17/12/2014', '08:10:00', 1, 0,1);");
        mDb.execSQL("insert into m_comprob_venta values ( null,1, 2, 1, 1, '0005', '5A', 5, 10, 0, 150, '17/12/2014', '08:10:00', 1, 0,1);");

        //Insertar Datos al Comprbante_Cobro

        mDb.execSQL("insert into m_comprob_cobro values (null,2, 2, 2, 2, 'FACTURA', 'FAC-0002', '2014-12-11',200,'17/12/2014','', 200, 1, 1,1,'historial');");
        mDb.execSQL("insert into m_comprob_cobro values (null,2, 2, 2, 2, 'FACTURA', 'FAC-0002', '2014-12-11',200,'17/12/2014','', 200, 1, 1,1,'historial');");
        mDb.execSQL("insert into m_comprob_cobro values (null,2, 2, 2, 2, 'BOLETA', 'FAC-0002', '2014-12-11',200,'17/12/2014','', 200, 1, 1,1,'historial');");
        mDb.execSQL("insert into m_comprob_cobro values (null,2, 2, 2, 2, 'BOLETA', 'FAC-0002', '2014-12-11',200,'17/12/2014','', 200, 1, 1,1,'historial');");

        //insertar Datos para Comprobantes Anulados

        mDb.execSQL("insert into m_comprob_venta values ( null,1, 2, 1, 1, '0005', '5A', 5, 10, 0, 150, '17/12/2014', '08:10:00', 0, 0,1);");
        mDb.execSQL("insert into m_comprob_venta values ( null,1, 1, 1, 1, '0005', '5A', 5, 10, 0, 50, '17/12/2014', '08:10:00', 0, 0,1);");
        mDb.execSQL("insert into m_comprob_venta values ( null,1, 1, 1, 1, '0005', '5A', 5, 10, 0, 150, '17/12/2014', '08:10:00', 0, 0,1);");

        listarGatos_Boletas_Emitidas(idAgente);
        listarGatos_Facturas_Emitidas(idAgente);
         //listarGatos_Notas_Credito_Emitidas(idAgente);
        listarGatos_Facturas_Cobradas(idAgente);
        listarGatos_Boletas_Cobradas(idAgente);
        listarGatos_Boletas_Anuladas(idAgente);
        listarGatos_Facturas_Anuladas(idAgente);
        Cursor cr = mDb.rawQuery("select * from m_resumen_caja  where rc_re_fecha_reporte='"+getDatePhone()+"' and id_Agente="+idAgente+" order by rc_te_tipo_gi",null);

        return cr;
    }
    //Termiando
    public void listarGatos_Facturas_Emitidas(int idAgente){

        Cursor cr1=mDb.rawQuery("select count(*) from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=1 and  id_Agente="+idAgente+"",null);
        Cursor cr2 = mDb.rawQuery("select count(distinct(_id)),sum(cv_re_total) from m_comprob_venta where cv_in_id_agente='"+idAgente+"' and cv_in_id_tipo_doc=1 and cv_te_fecha_doc='"+getDatePhone()+"'",null);
        cr1.moveToFirst();
        cr2.moveToFirst();

        if(cr1.getInt(0)==0){
            System.out.println("dentro de Cr1");

            insertar(idAgente,"Facturas Emitidas",1);
            if(cr2.getInt(0)!=0){
                System.out.println("dentro de Cr2");

                cr1=mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=1 ",null);
                String cantidad = cr2.getString(0);
                String vendido = cr2.getString(1);
                String pagado = "";
                String cobrado ="";
                actualizar(cr1,cantidad,vendido,pagado,cobrado);

            }else{

            }

        }
        else{

            if(cr2.getInt(0)!=0){
                System.out.println("dentro de Cr2 pero con datis existentes");
                String cantidad = cr2.getString(0);
                String vendido = cr2.getString(1);
                String pagado = "";
                String cobrado="";
                actualizar(cr1,cantidad,vendido,pagado,cobrado);
            }
            else{
                System.out.println("No hay Datos");
            }
        }

    }
    //---Terminado
    //--Terminado
    public void listarGatos_Boletas_Emitidas(int idAgente){


        Cursor cr1=mDb.rawQuery("select count(*) from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=2 and  id_Agente="+idAgente+"",null);
        Cursor cr2 = mDb.rawQuery("select count(distinct(_id)),sum(cv_re_total) from m_comprob_venta where cv_in_id_agente='"+idAgente+"' and cv_in_id_tipo_doc=2 and cv_te_fecha_doc='"+getDatePhone()+"'",null);
        cr1.moveToFirst();
        cr2.moveToFirst();

        if(cr1.getInt(0)==0){

            insertar(idAgente,"Boletas Emitidas",2);
            if(cr2.getInt(0)!=0){

                cr1=mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=2 ",null);
                String cantidad = cr2.getString(0);
                String vendido = cr2.getString(1);
                String pagado = "";
                String cobrado="";
                actualizar(cr1,cantidad,vendido,pagado,cobrado);

            }else{

            }

        }
        else{

            if(cr2.getInt(0)!=0){
                String cantidad = cr2.getString(0);
                String vendido = cr2.getString(1);
                String pagado = "";
                String cobrado="";
                actualizar(cr1,cantidad,vendido,pagado,cobrado);
            }
            else{
                System.out.println("No hay Datos");
            }
        }
    }
    //terminado
    //´pendiente
    public void listarGatos_Notas_Credito_Emitidas(int idAgente){

        Cursor cr1=mDb.rawQuery("select count(*) from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=3 and  id_Agente="+idAgente+"",null);
        Cursor cr2 = mDb.rawQuery("",null);
        cr1.moveToFirst();
        cr2.moveToFirst();

        if(cr1.getInt(0)==0){

            insertar(idAgente,"Notas de Credito Emitidas",3);
            if(cr2.getInt(0)!=0){

                cr1=mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=3 ",null);
                String cantidad = cr2.getString(0);
                String vendido = cr2.getString(1);
                String pagado = "";
                String cobrado ="";
                actualizar(cr1,cantidad,vendido,pagado,cobrado);

            }else{

            }

        }
        else{

            if(cr2.getInt(0)!=0){
                String cantidad = cr2.getString(0);
                String vendido = cr2.getString(1);
                String pagado = "";
                String cobrado="";
                actualizar(cr1,cantidad,vendido,pagado,cobrado);
            }
            else{
                System.out.println("No hay Datos");
            }
        }

    }
    //pendiente
    //Terminado
    public void listarGatos_Facturas_Cobradas(int idAgente){
        Cursor cr1=mDb.rawQuery("select count(*) from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=4 and  id_Agente="+idAgente+"",null);
        Cursor cr2 = mDb.rawQuery("select count(_id),sum( cc_re_monto_cobrado) from m_comprob_cobro where cc_in_estado_cobro=1 and cc_te_desc_tipo_doc='FACTURA' and cc_in_id_agente="+idAgente+" and cc_te_fecha_cobro='"+getDatePhone()+"'",null);
        cr1.moveToFirst();
        cr2.moveToFirst();

        if(cr1.getInt(0)==0){

            insertar(idAgente,"Facturas Cobradas",4);
            if(cr2.getInt(0)!=0){

                cr1=mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=4 ",null);
                String cantidad = cr2.getString(0);
                String vendido = "";
                String pagado = "";
                String cobrado =cr2.getString(1);
                actualizar(cr1,cantidad,vendido,pagado,cobrado);

            }else{

            }

        }
        else{

            if(cr2.getInt(0)!=0){
                String cantidad = cr2.getString(0);
                String vendido = "";
                String pagado = "";
                String cobrado=cr2.getString(1);
                actualizar(cr1,cantidad,vendido,pagado,cobrado);
            }
            else{
                System.out.println("No hay Datos");
            }
        }
    }
    //terminado
    //terminado
    public void listarGatos_Boletas_Cobradas(int idAgente){
        Cursor cr1=mDb.rawQuery("select count(*) from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=5 and  id_Agente="+idAgente+"",null);
        Cursor cr2 = mDb.rawQuery("select count(_id),sum( cc_re_monto_cobrado) from m_comprob_cobro where cc_in_estado_cobro=1 and cc_te_desc_tipo_doc='FACTURA' and cc_in_id_agente="+idAgente+" and cc_te_fecha_cobro='"+getDatePhone()+"'",null);
        cr1.moveToFirst();
        cr2.moveToFirst();

        if(cr1.getInt(0)==0){

            insertar(idAgente,"Boletas Cobradas",5);
            if(cr2.getInt(0)!=0){

                cr1=mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=5 ",null);
                String cantidad = cr2.getString(0);
                String vendido = "";
                String pagado = "";
                String cobrado =cr2.getString(1);
                actualizar(cr1,cantidad,vendido,pagado,cobrado);

            }else{

            }

        }
        else{

            if(cr2.getInt(0)!=0){
                String cantidad = cr2.getString(0);
                String vendido = "";
                String pagado = "";
                String cobrado=cr2.getString(1);
                actualizar(cr1,cantidad,vendido,pagado,cobrado);
            }
            else{
                System.out.println("No hay Datos");
            }
        }
    }
    //terminado
    //en proceso
    public void listarGatos_Facturas_Anuladas(int idAgente){
        Cursor cr1=mDb.rawQuery("select count(*) from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=6 and  id_Agente="+idAgente+"",null);
        Cursor cr2 = mDb.rawQuery("select count(_id),sum(cv_re_total) from m_comprob_venta where cv_in_estado_comp='0' and cv_in_id_agente='"+idAgente+"' and cv_te_fecha_doc='"+getDatePhone()+"' and cv_in_id_tipo_doc=1",null);
        cr1.moveToFirst();
        cr2.moveToFirst();

        if(cr1.getInt(0)==0){

            insertar(idAgente,"Facturas Anuladas",6);
            if(cr2.getInt(0)!=0){

                cr1=mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=6 ",null);
                String cantidad = cr2.getString(0);
                String vendido = "";
                String pagado = cr2.getString(1);
                String cobrado ="";
                actualizar(cr1,cantidad,vendido,pagado,cobrado);

            }else{

            }

        }
        else{

            if(cr2.getInt(0)!=0){
                String cantidad = cr2.getString(0);
                String vendido = "";
                String pagado = "";
                String cobrado=cr2.getString(1);
                actualizar(cr1,cantidad,vendido,pagado,cobrado);
            }
            else{
                System.out.println("No hay Datos");
            }
        }
    }
    public void listarGatos_Boletas_Anuladas(int idAgente){
        Cursor cr1=mDb.rawQuery("select count(*) from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=7 and  id_Agente="+idAgente+"",null);
        Cursor cr2 = mDb.rawQuery("select count(_id),sum(cv_re_total) from m_comprob_venta where cv_in_estado_comp='0' and cv_in_id_agente='"+idAgente+"' and cv_te_fecha_doc='"+getDatePhone()+"' and cv_in_id_tipo_doc=2",null);
        cr1.moveToFirst();
        cr2.moveToFirst();

        if(cr1.getInt(0)==0){

            insertar(idAgente,"Boletas Anuladas",7);
            if(cr2.getInt(0)!=0){

                cr1=mDb.rawQuery("select * from m_resumen_caja where rc_re_fecha_reporte='"+getDatePhone()+"' and rc_te_tipo_gi=7 ",null);
                String cantidad = cr2.getString(0);
                String vendido = "";
                String pagado = cr2.getString(1);
                String cobrado ="";
                actualizar(cr1,cantidad,vendido,pagado,cobrado);

            }else{

            }

        }
        else{

            if(cr2.getInt(0)!=0){
                String cantidad = cr2.getString(0);
                String vendido = "";
                String pagado = "";
                String cobrado=cr2.getString(1);
                actualizar(cr1,cantidad,vendido,pagado,cobrado);
            }
            else{
                System.out.println("No hay Datos");
            }
        }
    }
    public void insertar(int idAgente,String descripcion,int tipo_gi){


        mDb.execSQL("insert into m_resumen_caja values (null,"+idAgente+",'"+descripcion+"',"+tipo_gi+",'','','','','"+getDatePhone()+"')");

    }
    public boolean actualizar(Cursor cr,String cantidad,String vendido,String pagado,String cobrado){
        cr.moveToFirst();
        int idResumen = cr.getInt(0);
        mDb.execSQL("update m_resumen_caja set rc_in_cantidad='"+cantidad+"', rc_re_vendido='"+vendido+"', rc_re_pagado='"+pagado+"',rc_re_cobrado='"+cobrado+"' where _id='"+idResumen+"'  ");

        boolean estado = false;
        return estado;
    }
    private String getDatePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }


}
