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

public class  DbAdapter_Precio {


    //PROCEDIMIENTO TRAE ESTOS CAMPOS
    public static final String PR_id_precio = "_id";
    public static final String PR_id_precio_categoria = "pr_in_id_precio_categoria";
    public static final String PR_id_producto = "pr_in_id_producto";
    public static final String PR_id_cat_est = "pr_in_id_cat_estt";
    public static final String PR_costo_venta = "pr_re_costo_venta";
    public static final String PR_precio_unit = "pr_re_precio_unit";
    public static final String PR_desde = "pr_in_desde";
    public static final String PR_hasta = "pr_in_hasta";
    public static final String estado_sincronizacion = "estado_sincronizacion";


    //FALTA TRAER ESTOS CAMPOS
    public static final String PR_nombreProducto = "pr_in_nombre";
    //este campo me parece redundante
    public static final String PR_valor_unidad = "pr_in_valor_unidad";
    //Este campo me parece innecesario [El precio no depende del id agente]]
    public static final String PR_id_agente = "pr_in_id_agente";

    public static final String TAG = "Precio";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "ProdUnion.sqlite";
    public static final String SQLITE_TABLE_Precio = "m_precio";
    private final Context mCtx;

    public static final String CREATE_TABLE_PRECIO =
            "create table "+SQLITE_TABLE_Precio+" ("
                    +PR_id_precio+" integer primary key autoincrement,"
                    +PR_id_precio_categoria+" integer,"
                    +PR_id_producto+" integer,"
                    +PR_id_cat_est+" integer,"
                    +PR_costo_venta+" real,"
                    +PR_precio_unit+" real,"
                    +PR_valor_unidad+" integer,"
                    +PR_id_agente+" integer,"
                    +PR_desde+" integer,"
                    +PR_hasta+" integer,"
                    +PR_nombreProducto+" text, " +
                    estado_sincronizacion+" integer);"
            ;

    public static final String DELETE_TABLE_PRECIO = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Precio;

    public DbAdapter_Precio(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Precio open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
    public int getValorUnidad(String idProducto) {
        int valorUnidad = 0;

        Cursor cr = mDb.rawQuery("select * from " + SQLITE_TABLE_Precio + " where "+PR_id_producto+"='"+idProducto+"';", null);
        if (cr.moveToFirst()) {
            valorUnidad = cr.getInt(cr.getColumnIndexOrThrow(PR_valor_unidad));

        }
        return valorUnidad;
    }

    public long createPrecio(
            int id_producto, int id_cat_est, double costo_venta, double precio_unit,
            int valor_unidad, int id_agente, int desde, int hasta,String nombre){

        ContentValues initialValues = new ContentValues();
        initialValues.put(PR_id_producto,id_producto);
        initialValues.put(PR_id_cat_est,id_cat_est);
        initialValues.put(PR_costo_venta,costo_venta);
        initialValues.put(PR_precio_unit,precio_unit);
        initialValues.put(PR_valor_unidad,valor_unidad);
        initialValues.put(PR_id_agente,id_agente);
        initialValues.put(PR_desde,desde);
        initialValues.put(PR_hasta,hasta);
        initialValues.put(PR_nombreProducto,nombre);


        return mDb.insert(SQLITE_TABLE_Precio, null, initialValues);
    }

    public long createPrecios(Precio precioCategoria, int idAgente){

        ContentValues initialValues = new ContentValues();
        initialValues.put(PR_id_producto,precioCategoria.getIdPrecioCategoria());
        initialValues.put(PR_id_producto,precioCategoria.getIdProducto());
        initialValues.put(PR_id_cat_est,precioCategoria.getIdCategoriaEstablecimiento());
        initialValues.put(PR_costo_venta,precioCategoria.getCostoVenta());
        initialValues.put(PR_precio_unit,precioCategoria.getPrecioUnitario());
        initialValues.put(PR_valor_unidad,precioCategoria.getValorUnidad());
        initialValues.put(PR_id_agente,idAgente);
        initialValues.put(PR_desde,precioCategoria.getDesde());
        initialValues.put(PR_hasta,precioCategoria.getHasta());
        initialValues.put(PR_nombreProducto,precioCategoria.getNombreProducto());


        return mDb.insert(SQLITE_TABLE_Precio, null, initialValues);
    }
    public long updatePrecios(Precio precioCategoria, int idAgente){

        ContentValues initialValues = new ContentValues();
        initialValues.put(PR_id_producto,precioCategoria.getIdProducto());
        initialValues.put(PR_id_producto,precioCategoria.getIdPrecioCategoria());
        initialValues.put(PR_id_cat_est,precioCategoria.getIdCategoriaEstablecimiento());
        initialValues.put(PR_costo_venta,precioCategoria.getCostoVenta());
        initialValues.put(PR_precio_unit,precioCategoria.getPrecioUnitario());
        initialValues.put(PR_valor_unidad,precioCategoria.getValorUnidad());
        initialValues.put(PR_id_agente,idAgente);
        initialValues.put(PR_desde,precioCategoria.getDesde());
        initialValues.put(PR_hasta,precioCategoria.getHasta());
        initialValues.put(PR_nombreProducto,precioCategoria.getNombreProducto());


        return mDb.update(SQLITE_TABLE_Precio,
                initialValues,
                PR_id_precio_categoria + "=?",
                new String[]{
                        ""+precioCategoria.getIdPrecioCategoria()
                });
    }

    public boolean existePrecio(int idProducto, int catEstablec, int valorUnidad) {

        boolean exists = false;
        Cursor mCursor = mDb.query(SQLITE_TABLE_Precio, new String[] {PR_id_precio,
                        PR_id_producto, PR_id_cat_est, PR_costo_venta, PR_precio_unit},
                PR_id_producto + " = " + idProducto + " AND " + PR_id_cat_est + " = " + catEstablec + " AND " + PR_valor_unidad+ " = " + valorUnidad ,
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
            exists = true;
        }
        if(mCursor.getCount()==0){
            exists=false;
        }
        return exists;
    }

    public boolean deleteAllPrecio() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Precio, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchPrecioByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Precio, new String[] {PR_id_precio,
                            PR_id_producto, PR_id_cat_est, PR_costo_venta, PR_precio_unit},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Precio, new String[] {PR_id_precio,
                            PR_id_producto, PR_id_cat_est, PR_costo_venta, PR_precio_unit},
                    PR_id_producto + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllPrecioByIdProductoAndCategoeria(int idProducto, int idCategoriaEstablecimiento) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Precio, new String[] {PR_id_precio,
                        PR_id_producto, PR_id_cat_est, PR_costo_venta, PR_precio_unit, PR_valor_unidad, PR_nombreProducto},
                PR_id_producto + " = " + idProducto + " AND " + PR_id_cat_est + " = " + idCategoriaEstablecimiento,
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }




    public Cursor fetchAllPrecioByIdProductoAndCantidadAndValorUnidad(int id_producto, int cantidad, int idCategoriaEstablecimiento, int valor_unidad) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Precio, new String[] {PR_id_precio,
                        PR_id_producto, PR_id_cat_est, PR_costo_venta, PR_precio_unit},
                PR_id_producto + " = " + id_producto+ " AND " + PR_desde + " <= " + cantidad+ " AND " + PR_hasta + " >= " + cantidad + " AND " + PR_id_cat_est + " = " + idCategoriaEstablecimiento + " AND "+PR_valor_unidad + " = " +valor_unidad,
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllPrecioByIdProductoAndCantidad(int id_producto, int cantidad, int idCategoriaEstablecimiento) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Precio, new String[] {PR_id_precio,
                        PR_id_producto, PR_id_cat_est, PR_costo_venta, PR_precio_unit},
                PR_id_producto + " = " + id_producto+ " AND " + PR_desde + " <= " + cantidad+ " AND " + PR_hasta + " >= " + cantidad + " AND " + PR_id_cat_est + " = " + idCategoriaEstablecimiento,
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getProductoByCodigo(int idCategoria,String codigoBarras, int liquidacion){
        return mDb.rawQuery("select *  from m_precio mp,m_stock_agente sa where mp.pr_in_id_producto=sa.st_in_id_producto and st_te_codigo_barras='"+codigoBarras+"' and mp.pr_in_id_cat_estt='"+idCategoria+"' AND sa.liquidacion = '"+liquidacion+"' ",null);

    }

    public Cursor fetchAllPrecio() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Precio, new String[] {PR_id_precio,
                        PR_id_producto, PR_id_cat_est, PR_costo_venta, PR_precio_unit, PR_nombreProducto},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomePrecio() {

        createPrecio( 1, 1, 11.00, 11.50, 1,1, 1, 30,"Pan Integral Mediano");
        createPrecio( 1, 2, 12.00, 12.50, 1,1, 31, 60,"Pan Integral Mediano");
        createPrecio( 1, 3, 12.00, 12.50, 1,1, 61, 100,"Pan Integral Mediano");
        createPrecio( 2, 1, 21.00, 21.50, 1,1, 1, 30,"Pan Blanco Americano");
        createPrecio( 2, 2, 22.00, 22.50, 1,1, 31, 60,"Pan Blanco Americano");
        createPrecio( 2, 3, 23.00, 23.50, 1,1, 61, 1000,"Pan Blanco Americano");
        createPrecio( 3, 1, 31.00, 31.50, 1,1, 1, 30,"Granola con pasas y almendras");
        createPrecio( 3, 2, 32.00, 32.50, 1,1, 31, 60,"Granola con pasas y almendras");
        createPrecio( 3, 3, 33.00, 33.50, 1,1, 61, 1000,"Granola con pasas y almendras");
        createPrecio( 4, 1, 41.00, 41.50, 1,1, 1, 30,"Panetón Super Boom");
        createPrecio( 4, 2, 42.00, 42.50, 1,1, 31, 60,"Panetón Super Boom");
        createPrecio( 4, 3, 43.00, 43.50, 1,1,61, 1000,"Panetón SuperBoom");
        createPrecio( 5, 1, 51.00, 51.50, 1,1, 1, 30,"Panetón Integral");
        createPrecio( 5, 2, 52.00, 52.50, 1,1, 31, 60,"Panetón Integral");
        createPrecio( 5, 3, 53.00, 53.50, 1,1, 61, 1000,"Panetón Integral");
    }

    double formatDecimal(double d)
    {
        DecimalFormat df = new DecimalFormat("#,00");
        return Double.valueOf(df.format(d));
    }

}