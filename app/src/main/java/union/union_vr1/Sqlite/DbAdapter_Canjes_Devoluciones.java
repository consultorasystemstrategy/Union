package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.Conexion.DbHelper;

/**
 * Created by Usuario on 18/12/2014.
 */
public class DbAdapter_Canjes_Devoluciones {
    //EMPRESA =1
    //IDAGENTE
    //TIPO =4
    //TOTAL
    private DbHelper mDbHelper;
    private final Context mCtx;
    private SQLiteDatabase mDb;
    private DbAdapter_Histo_Venta dbHistoVenta ;

    public DbAdapter_Canjes_Devoluciones(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Canjes_Devoluciones open() {
        dbHistoVenta = new DbAdapter_Histo_Venta(mCtx);
        dbHistoVenta.open();
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }


    public Cursor listaFacturasByProducto(int idProducto, int idAgente, String idEstablec) {
        Cursor cr = mDb.rawQuery("select * from m_histo_venta_detalle where hd_in_id_producto=" + idProducto + " and hd_in_id_agente=" + idAgente + " and hd_in_id_establec='" + idEstablec + "' and hd_in_estado=1 and hd_in_id_detalle !=''", null);
        if (cr != null) {
            cr.moveToFirst();
        } else {
            cr = null;
        }
        return cr;
    }

    public boolean insertarCanjes(String idEstablec, int idProducto, int idtipoOpe, String comprobante, String nomEstablec, String nomProducto, int idCategoria, int cantidad, double importe, String lote, int idAgente) {
        boolean estado = false;
        try {
            mDb.execSQL("insert into m_histo_venta_detalle values (null,'', '', '" + idEstablec + "','" + idProducto + "','" + idtipoOpe + "','','" + comprobante + "', '" + nomEstablec + "', '" + nomProducto + "', '','', '', '', '', '" + idCategoria + "','2','" + cantidad + "','" + importe + "','" + getDatePhone() + "','pendiente','" + lote + "','','1','" + idAgente + "',0,0,0.0,'','','"+Constants._CREADO+"');");
            Cursor cr = mDb.rawQuery("select * from m_stock_agente where st_in_id_producto='" + idProducto + "'", null);
            cr.moveToFirst();
            int devol_canjes = cr.getInt(cr.getColumnIndex("st_in_canjes"));
            int total = cantidad + devol_canjes;
            mDb.execSQL("update m_stock_agente set st_in_canjes='" + total + "' where st_in_id_producto='" + idProducto + "'; ");
            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }

    public boolean insertar_Dev(String idEstablec, int idProducto, int idtipoOpe, String comprobante, String nomEstablec, String nomProducto, int idCategoria, int cantidad_dev, double importe, String lote, int idAgente) {
        boolean estado = false;
        try {
            mDb.execSQL("insert into m_histo_venta_detalle (_id,hd_in_id_detalle,hd_in_id_establec,hd_in_id_producto,hd_te_comprobante,hd_te_nom_estab,hd_te_nom_producto,hd_te_lote,hd_in_estado,hd_in_id_agente,hd_in_cantidad_ope_dev,hd_in_categoria_ope_dev,hd_re_importe_ope_dev,hg_te_fecha_ope_dev,hd_te_hora_ope_dev,estado_sincronizacion,hd_in_forma_ope) " +
                    "values(null,'','" + idEstablec + "','" + idProducto + "','" + comprobante + "','" + nomEstablec + "','" + nomProducto + "','" + lote + "','1','" + idAgente + "','" + cantidad_dev + "','" + idCategoria + "','" + importe + "','" + getDatePhone() + "','pendiente','"+Constants._CREADO+"','2') ");
            Cursor cr = mDb.rawQuery("select * from m_stock_agente where st_in_id_producto='" + idProducto + "'", null);
            cr.moveToFirst();
            int devol_canjes = cr.getInt(cr.getColumnIndex("st_in_devoluciones"));
            int total = cantidad_dev + devol_canjes;
            mDb.execSQL("update m_stock_agente set st_in_devoluciones='" + total + "' where st_in_id_producto='" + idProducto + "'; ");
            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }

    public Cursor obtenerStock(int id, int idProducto) {
        Cursor cr = mDb.rawQuery("select *,st_in_disponible- (st_in_canjes) as 'disponible' from m_stock_agente where st_in_id_producto='" + idProducto + "' and st_in_id_agente='" + id + "'", null);
        return cr;
    }

    public Cursor nom_establecimiento(String idEstablecimiento) {

        Cursor cr = mDb.rawQuery("select me._id,me.ee_te_nom_establec , max(md.hd_in_id_detalle),me.ee_in_id_cat_est  from m_evento_establec me,m_histo_venta_detalle md where ee_in_id_establec=" + idEstablecimiento + "", null);
        return cr;
    }

    public String obtenerPrecio(int idProducto, int idCategoriEstablec, Context ctx, String idesta) {
        if (idCategoriEstablec == 0) {
            Cursor ce = mDb.rawQuery("select _id, ee_in_id_cat_est from m_evento_establec where ee_in_id_establec='" + idesta + "' ", null);
            ce.moveToFirst();
            idCategoriEstablec = ce.getInt(1);
        }
        String precio = "No se Encontro precio de producto por favor intente Nuevamente";
        Cursor cr = mDb.rawQuery("select _id,pr_re_precio_unit  from m_precio where pr_in_id_producto=" + idProducto + " and pr_in_id_cat_estt=" + idCategoriEstablec + " ", null);
        if (cr.moveToFirst()) {
            precio = cr.getString(1);
        } else {
            System.out.println(precio + idProducto + idCategoriEstablec);
        }
        return precio;

    }

    public boolean update_Canj(String tipo_op, String categoria_op, String cantidad, String importe, String idDetalle, int devuelto, int idProducto, Context tx) {
        int can = devuelto + Integer.parseInt(cantidad);
        boolean estado = false;
        try {

            mDb.execSQL("update m_histo_venta_detalle set hd_in_id_tipoper='" + tipo_op + "',hd_in_categoria_ope='" + categoria_op + "',hd_in_cantidad_ope='" + can + "',hd_re_importe_ope='" + importe + "',hg_te_fecha_ope='" + getDatePhone() + "',hd_te_hora_ope='pendiente',hd_in_forma_ope='1' where hd_in_id_detalle='" + idDetalle + "';");
            Cursor cr = mDb.rawQuery("select * from m_stock_agente where st_in_id_producto='" + idProducto + "'", null);
            cr.moveToFirst();
            int devol_canjes = cr.getInt(cr.getColumnIndex("st_in_canjes"));
            int total = Integer.parseInt(cantidad) + devol_canjes;
            mDb.execSQL("update m_stock_agente set st_in_canjes='" + total + "' where st_in_id_producto='" + idProducto + "'; ");
            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }

    public boolean update_dev(String tipo_op, String categoria_op, String cantidad, String importe, String idDetalle, int devuelto, int idProducto, Context tx) {
        int can = devuelto + Integer.parseInt(cantidad);
        boolean estado = false;
        try {

            mDb.execSQL("update m_histo_venta_detalle set hd_in_cantidad_ope_dev='" + can + "',hd_in_categoria_ope_dev='" + categoria_op + "',hd_re_importe_ope_dev='" + importe + "',hg_te_fecha_ope_dev='" + getDatePhone() + "',hd_te_hora_ope_dev='pendiente',hd_in_forma_ope='1' where hd_in_id_detalle='" + idDetalle + "';");
            Cursor cr = mDb.rawQuery("select * from m_stock_agente where st_in_id_producto='" + idProducto + "'", null);
            cr.moveToFirst();
            int devol_canjes = cr.getInt(cr.getColumnIndex("st_in_devoluciones"));
            int total = Integer.parseInt(cantidad) + devol_canjes;
            mDb.execSQL("update m_stock_agente set st_in_devoluciones='" + total + "' where st_in_id_producto='" + idProducto + "';");
            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }

    public Cursor obtener_facturas_can(String idEstablec) {
        Cursor cr = mDb.rawQuery("select * from m_histo_venta_detalle where hd_in_id_establec='" + idEstablec + "'  and hd_te_hora_ope='pendiente' and hd_in_id_tipoper='1' and  hg_te_fecha_ope='" + getDatePhone() + "';", null);
        if (cr != null) {
            cr.moveToFirst();
        } else {
            cr = null;
        }
        return cr;
    }

    public Cursor obtener_facturas_dev(String idEstablec) {
        Cursor cr = mDb.rawQuery("select * from m_histo_venta_detalle where hd_in_id_establec='" + idEstablec + "'  and hd_te_hora_ope_dev='pendiente' and  hg_te_fecha_ope_dev='" + getDatePhone() + "';", null);
        if (cr != null) {
            cr.moveToFirst();
        } else {
            cr = null;
        }
        return cr;
    }

    public Cursor obtener_cabecera(String idEstablecimiento) {
        Cursor cr = mDb.rawQuery("select * from m_evento_establec where ee_in_id_establec ='" + idEstablecimiento + "'", null);
        return cr;

    }

    public Cursor obtener_igv(int id, String idEstablec) {
        Cursor cr = mDb.rawQuery("select ROUND(sum(hd_in_cantidad_ope*hd_re_importe_ope ),1)  as total,ROUND(sum(hd_re_importe_ope*hd_in_cantidad_ope)/1.18,1) as subtotal,ROUND((sum(hd_re_importe_ope*hd_in_cantidad_ope)-sum(hd_re_importe_ope*hd_in_cantidad_ope)/1.18),1)  as igv from  m_histo_venta_detalle where  hd_in_id_tipoper='" + id + "'  and  hg_te_fecha_ope='" + getDatePhone() + "' and hd_te_hora_ope ='pendiente' and hd_in_id_establec='" + idEstablec + "'", null);
        return cr;
    }

    public Cursor obtener_igv_dev(int id, String idEstablec) {
        Cursor cr = mDb.rawQuery("select sum(hd_in_cantidad_ope_dev*hd_re_importe_ope_dev )  as total,sum(hd_re_importe_ope_dev*hd_in_cantidad_ope_dev)/1.18 as subtotal,sum(hd_re_importe_ope_dev*hd_in_cantidad_ope_dev)-sum(hd_re_importe_ope_dev*hd_in_cantidad_ope_dev)/1.18  as igv from  m_histo_venta_detalle where   hg_te_fecha_ope_dev='" + getDatePhone() + "' and hd_te_hora_ope_dev ='pendiente' and hd_in_id_establec='" + idEstablec + "'", null);
        return cr;
    }

    public Cursor obtener_facturas_canjes(int tipo, String idEstablec) {
        Cursor cr = mDb.rawQuery("select * from  m_histo_venta_detalle where  hd_in_id_tipoper='" + tipo + "' and hd_in_id_establec='" + idEstablec + "' and  hg_te_fecha_ope='" + getDatePhone() + "' and hd_te_hora_ope ='pendiente'", null);
        if (cr != null) {
            cr.moveToFirst();
        } else {
            cr = null;
        }
        return cr;
    }

    public Cursor obtener_facturas_dev(int tipo, String idEstablec) {
        Cursor cr = mDb.rawQuery("select * from  m_histo_venta_detalle where hd_in_id_establec='" + idEstablec + "' and  hg_te_fecha_ope_dev='" + getDatePhone() + "' and hd_te_hora_ope_dev ='pendiente'", null);
        if (cr != null) {
            cr.moveToFirst();
        } else {
            cr = null;
        }
        return cr;
    }

    public Cursor listarCanjesDev(int idEstablec) {
        Cursor cr = mDb.rawQuery("select * from m_histo_venta_detalle where  hd_in_id_establec=" + idEstablec + " and hg_te_fecha_ope='" + getDatePhone() + "';", null);
        return cr;
    }
    public String guardarCabecera(int idAgente){

       long id  = dbHistoVenta.createHistoVenta(""+idAgente+"."+getDatePhone()+" "+getTimePhone(),idAgente,0.0,getDatePhone(),Constants._CREADO);
      Cursor cr  = mDb.rawQuery("select * from m_histo_venta where _id="+id+"",null);
        cr.moveToFirst();


        return  cr.getString(1);
    }
    private String consultarId(String idGuia){

        Cursor cr  = mDb.rawQuery("select * from m_histo_venta where hv_in_id_histo='"+idGuia+"'",null);
        cr.moveToFirst();
        return  cr.getString(0);
    }

    public boolean guardarCambios(int tipo, String idEstablec,String idGuia) {
        boolean estado = false;

        try {

            Cursor c = mDb.rawQuery("select * from  m_histo_venta_detalle where  hd_in_id_tipoper='" + tipo + "'  and  hg_te_fecha_ope='" + getDatePhone() + "' and hd_in_id_establec='" + idEstablec + "' and hd_te_hora_ope='pendiente';", null);

            while (c.moveToNext()) {
                mDb.execSQL("update m_histo_venta_detalle set hd_te_hora_ope='" + getTimePhone() + "',estado_sincronizacion='"+ Constants._CREADO +"',"+DbAdapter_Histo_Venta_Detalle.HD_Guia+"='"+idGuia+"' where  hd_in_id_detalle='" + c.getString(1) + "';");
            }
            Cursor stock = mDb.rawQuery("select distinct(ag._id),ag.st_in_id_producto,ag.st_te_nombre,ag. st_te_codigo,ag.st_te_codigo_barras,ag.st_in_inicial,ag.st_in_final,ag.st_in_disponible,ag.st_in_ventas,ag.st_in_canjes,ag.st_in_devoluciones,\n" +
                    "ag.st_in_buenos,ag.st_in_malos,ag.st_in_fisico,ag.st_in_id_agente from m_stock_agente ag,m_histo_venta_detalle mv where ag.st_in_id_producto=mv.hd_in_id_producto and mv.hd_te_hora_ope_dev='pendiente'", null);
            int disponible = 0;
            int idProducto = 0;
            while (stock.moveToNext()) {
                disponible = stock.getInt(7) - stock.getInt(9);
                idProducto=stock.getInt(1);
                mDb.execSQL("update m_stock_agente  set st_in_disponible ='" + disponible + "' where st_in_id_producto = '" + idProducto + "' ;");
            }


            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }

    public boolean guardarCambios_dev(String idGuia, String idEstablec) {

        boolean estado = false;

        try {

            Cursor c = mDb.rawQuery("select * from  m_histo_venta_detalle where hg_te_fecha_ope_dev='" + getDatePhone() + "' and hd_in_id_establec='" + idEstablec + "' and hd_te_hora_ope_dev='pendiente';", null);

            double Total = 0.0;
            double subTotal=0.0;
            while (c.moveToNext()) {
                Total = Total+(c.getDouble(27)*c.getInt(25));
                mDb.execSQL("update m_histo_venta_detalle set hd_te_hora_ope_dev='" + getTimePhone() + "',estado_sincronizacion='"+ Constants._CREADO +"',"+DbAdapter_Histo_Venta_Detalle.HD_Guia+"='"+idGuia+"' where  hd_in_id_detalle='" + c.getString(1) + "'");
            }
            subTotal=subTotal/1.18;
           Log.d("IDGUIA",consultarId(idGuia));
            mDb.execSQL("update m_histo_venta set hv_in_subtotal ='"+subTotal+"', estado_sincronizacion="+Constants._CREADO+" where _id='"+consultarId(idGuia)+"' ;");


            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }

    public boolean cancelarCambios(int tipo, String idEstablec, String columna) {
        boolean estado = false;

        try {
            Cursor c = mDb.rawQuery("select * from  m_histo_venta_detalle where  hd_in_id_tipoper='" + tipo + "'  and  hg_te_fecha_ope='" + getDatePhone() + "' and hd_in_id_establec='" + idEstablec + "' and hd_te_hora_ope='pendiente'", null);

            while (c.moveToNext()) {
                if (c.getString(1).equals("")) {
                    mDb.execSQL("DELETE FROM m_histo_venta_detalle WHERE _id='" + c.getString(0) + "'");

                }
                mDb.execSQL("update m_stock_agente set " + columna + "='0' where st_in_id_producto='" + c.getString(4) + "'; ");
                mDb.execSQL("update m_histo_venta_detalle set hd_in_id_tipoper='',hd_in_categoria_ope='',hd_in_cantidad_ope='',hd_re_importe_ope='',hg_te_fecha_ope='',hd_te_hora_ope='' where hd_in_id_detalle='" + c.getString(1) + "';");

            }

            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }

    public boolean cancelarCambios_dev(int tipo, String idEstablec, String columna) {
        boolean estado = false;

        try {
            Cursor c = mDb.rawQuery("select * from  m_histo_venta_detalle where hg_te_fecha_ope_dev='" + getDatePhone() + "' and hd_in_id_establec='" + idEstablec + "' and hd_te_hora_ope_dev='pendiente'", null);

            while (c.moveToNext()) {
                if (c.getString(1).equals("") ) {
                    mDb.execSQL("DELETE FROM m_histo_venta_detalle WHERE _id='" + c.getString(0) + "'");

                }
                mDb.execSQL("update m_stock_agente set " + columna + "='0' where st_in_id_producto='" + c.getString(4) + "'; ");
                mDb.execSQL("update m_histo_venta_detalle set hd_in_id_tipoper='',hd_in_categoria_ope='',hd_in_cantidad_ope='',hd_re_importe_ope='',hg_te_fecha_ope='',hd_te_hora_ope='',hd_in_cantidad_ope_dev='',hd_in_categoria_ope_dev='',hd_re_importe_ope_dev='',hg_te_fecha_ope_dev='',hd_te_hora_ope_dev='' where hd_in_id_detalle='" + c.getString(1) + "';");

            }

            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    private String getTimePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }
}
