package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private DbHelper mDbHelper;
    private final Context mCtx;
    private SQLiteDatabase mDb;

    public DbAdapter_Canjes_Devoluciones(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Canjes_Devoluciones open() {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public Cursor listaFacturasByProducto(int idProducto, int idAgente, String idEstablec) {
        Cursor cr = mDb.rawQuery("select * from m_histo_venta_detalle where hd_in_id_producto=" + idProducto + " and hd_in_id_agente=" + idAgente + " and hd_in_id_establec='" + idEstablec + "' and hd_in_estado=1", null);
        if (cr != null) {
            cr.moveToFirst();
        } else {
            cr = null;
        }
        return cr;
    }

    public boolean insertarCanjes_Dev(String idEstablec, int idProducto, int idtipoOpe, String comprobante, String nomEstablec, String nomProducto, int idCategoria, int cantidad_dev, String importe, String lote, int idAgente) {
        boolean estado = false;
        try {
            mDb.execSQL("insert into m_histo_venta_detalle values ( null,'', '', '" + idEstablec + "','" + idProducto + "','" + idtipoOpe + "','','" + comprobante + "', '" + nomEstablec + "', '" + nomProducto + "', '','', '', '', '', '" + idCategoria + "','','" + cantidad_dev + "','" + importe + "','" + getDatePhone() + "','" + getTimePhone() + "','" + lote + "','','1','" + idAgente + "');");
            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }
    public Cursor obtenerStock(int id, int idProducto){
        Cursor cr = mDb.rawQuery("select * from m_stock_agente where st_in_id_producto='"+idProducto+"' and st_in_id_agente='"+id+"'",null);
        return cr;
    }

    public Cursor nom_establecimiento(String idEstablecimiento) {

        Cursor cr = mDb.rawQuery("select me._id,me.ee_te_nom_establec , max(md.hd_in_id_detalle),me.ee_in_id_cat_est  from m_evento_establec me,m_histo_venta_detalle md where ee_in_id_establec=" + idEstablecimiento + "", null);
        return cr;
    }

    public String obtenerPrecio(int idProducto, int idCategoriEstablec, Context ctx) {
        Toast.makeText(ctx, "here" + idProducto + idCategoriEstablec, Toast.LENGTH_SHORT).show();
        String precio = "No se Encontro precio de producto por favor intente Nuevamente";
        Cursor cr = mDb.rawQuery("select _id,pr_re_precio_unit  from m_precio where pr_in_id_producto=" + idProducto + " and pr_in_id_cat_estt=" + idCategoriEstablec + " ", null);
        if (cr.moveToFirst()) {
            precio = cr.getString(1);
        } else {
            Toast.makeText(ctx, precio, Toast.LENGTH_SHORT).show();
        }
        return precio;

    }

    public boolean update_Canj_dev(String tipo_op, String categoria_op, String cantidad, String importe, String idDetalle) {
        boolean estado = false;
        try {
            mDb.execSQL("update m_histo_venta_detalle set hd_in_id_tipoper='" + tipo_op + "',hd_in_categoria_ope='" + categoria_op + "',hd_in_cantidad_ope='" + cantidad + "',hd_re_importe_ope='" + importe + "',hg_te_fecha_ope='"+getDatePhone()+"',hd_te_hora_ope='"+getTimePhone()+"' where hd_in_id_detalle='" + idDetalle + "';");
            estado = true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado = false;
        }

        return estado;
    }
    public Cursor listarCanjesDev(int idEstablec){
       Cursor cr = mDb.rawQuery("select * from m_histo_venta_detalle where  hd_in_id_establec="+idEstablec+" and hg_te_fecha_ope='"+getDatePhone()+"'",null);
        return cr;
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
