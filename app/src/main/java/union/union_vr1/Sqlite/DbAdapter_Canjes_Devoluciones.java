package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import union.union_vr1.Conexion.DbHelper;

/**
 * Created by Usuario on 18/12/2014.
 */
public class DbAdapter_Canjes_Devoluciones {
    private DbHelper mDbHelper;
    private final Context mCtx;
    private SQLiteDatabase mDb;
    public DbAdapter_Canjes_Devoluciones(Context ctx){
        this.mCtx=ctx;
    }
    public DbAdapter_Canjes_Devoluciones open(){
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    public Cursor listaFacturasByProducto(int idProducto,int idAgente,int idEstablec){
        Cursor cr = mDb.rawQuery("select * from m_histo_venta_detalle where hd_in_id_producto="+idProducto+" and hd_in_id_agente="+idAgente+" and hd_in_id_establec="+idEstablec+" and hd_in_estado=1",null);
        if(cr !=null){
   cr.moveToFirst();
       }else{
           cr=null;
       }
        return cr;
    }
    public boolean insertarCanjes_Dev(int isEstablec,int idProducto,int idtipoOpe,String comprobante,String nomEstablec,String nomProducto,int idCategoria,int cantidad_dev,double importe,String lote){
        boolean estado= false;
        try {
            mDb.execSQL("insert into m_histo_venta_detalle values ( null,1, 1, 1, 1, 0, 1, '80087/FV030-1858/2014-12-05/2.5', 'TIENDA NA', 'Pan Integral Mediano Union', 1, 2.0, '2014-11-12', '08:10:00', 1, 1, 1, 1, 0.0, '', '', '', '', 1, 1);");
            estado=true;

        } catch (android.database.SQLException e) {
            e.printStackTrace();
            estado= false;
        }

        return estado;
    }
    public Cursor nom_establecimiento(String idEstablecimiento){

        Cursor cr = mDb.rawQuery("select me._id,me.ee_te_nom_establec , max(md.hd_in_id_detalle),me.ee_in_id_cat_est  from m_evento_establec me,m_histo_venta_detalle md where ee_in_id_establec="+idEstablecimiento+"",null);
        return cr;
    }
    public Double obtenerPrecio(int idProducto, int idCategoriEstablec){
        Double precio;
        Cursor cr = mDb.rawQuery("select _id,pr_re_precio_unit  from m_precio where pr_in_id_producto="+idProducto+" and pr_in_id_cat_estt="+idCategoriEstablec+" ",null);
        cr.moveToFirst();
        precio=cr.getDouble(1);
        return precio;

    }
}
