package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.StockAgente;

public class DbAdapter_Stock_Agente {


    // EL PROCEDIMIENTO NOS DA ESTOS DATOS
    public static final String ST_id_stock_agente = "_id";
    //FALTA UN ID STOCK AGENTE PROPIO
    //CUANDO REALIZAMOS UNA VENTA ACTUALIZAMOS EL STOCK DISPONIBLE, FINALL, FISICO, ETC
    public static final String ST_id_producto = "st_in_id_producto";
    public static final String ST_nombre = "st_te_nombre";
    public static final String ST_codigo = "st_te_codigo";
    public static final String ST_codigo_barras = "st_te_codigo_barras";
    public static final String ST_inicial = "st_in_inicial";
    public static final String ST_final = "st_in_final";
    public static final String ST_disponible = "st_in_disponible";
    public static final String ST_id_agente = "st_in_id_agente";

    //EL PROCEDIMIENTO NO TRAE ESTOS DATOS[MODIFICARLO]
    // 2 OPCIONES
    //1 TRAE ESTOS DATOS TAMBIÉN
    //2 ELIMINAMOS ESTOS CAMPOS DE ESTE TABLA
    public static final String ST_ventas = "st_in_ventas";
    public static final String ST_canjes = "st_in_canjes";
    public static final String ST_devoluciones = "st_in_devoluciones";
    public static final String ST_buenos = "st_in_buenos";
    public static final String ST_malos = "st_in_malos";
    public static final String ST_fisico = "st_in_fisico";
    public static final String estado_sincronizacion = "estado_sincronizacion";



    public static final String TAG = "Stock_Agente";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "ProdUnion.sqlite";
    private static final String SQLITE_TABLE_Stock_Agente = "m_stock_agente";
    private final Context mCtx;

    public static final String CREATE_TABLE_STOCK_AGENTE =
            "create table "+SQLITE_TABLE_Stock_Agente+" ("
                    +ST_id_stock_agente+" integer primary key autoincrement,"
                    +ST_id_producto+" integer,"
                    +ST_nombre+" text,"
                    +ST_codigo+" text,"
                    +ST_codigo_barras+" text,"
                    +ST_inicial+" integer,"
                    +ST_final+" integer,"
                    +ST_disponible+" integer,"
                    +ST_ventas+" integer,"
                    +ST_canjes+" integer,"
                    +ST_devoluciones+" integer,"
                    +ST_buenos+" integer,"
                    +ST_malos+" integer,"
                    +ST_fisico+" integer,"
                    +ST_id_agente+" integer, " +
                    estado_sincronizacion+" integer);";


    public static final String DELETE_TABLE_STOCK_AGENTE = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Stock_Agente;

    public DbAdapter_Stock_Agente(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Stock_Agente open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createStockAgente(
            int id_producto, String nombre, String codigo, String codigo_barras, int inicial,
            int finals, int disponible, int ventas, int canjes, int devoluciones,
            int buenos, int malos, int fisico, int id_agente){

        ContentValues initialValues = new ContentValues();
        initialValues.put(ST_id_producto,id_producto);
        initialValues.put(ST_nombre,nombre);
        initialValues.put(ST_codigo,codigo);
        initialValues.put(ST_codigo_barras,codigo_barras);
        initialValues.put(ST_inicial,inicial);
        initialValues.put(ST_final,finals);
        initialValues.put(ST_disponible,disponible);
        initialValues.put(ST_ventas,ventas);
        initialValues.put(ST_canjes,canjes);
        initialValues.put(ST_devoluciones,devoluciones);
        initialValues.put(ST_buenos,buenos);
        initialValues.put(ST_malos,malos);
        initialValues.put(ST_fisico,fisico);
        initialValues.put(ST_id_agente,id_agente);

        return mDb.insert(SQLITE_TABLE_Stock_Agente, null, initialValues);
    }

    public long createStockAgentes(StockAgente stockAgente, int idAgente, int sincronizado){

        ContentValues initialValues = new ContentValues();
        initialValues.put(ST_id_producto,stockAgente.getIdProducto());
        initialValues.put(ST_nombre,stockAgente.getNombre());
        initialValues.put(ST_codigo,stockAgente.getCodigo());
        initialValues.put(ST_codigo_barras,stockAgente.getCodigoBarras());
        initialValues.put(ST_inicial,stockAgente.getStockInicial());
        initialValues.put(ST_final,stockAgente.getStockFinal());
        initialValues.put(ST_disponible,stockAgente.getDisponible());
        initialValues.put(ST_ventas,stockAgente.getVentas());
        initialValues.put(ST_canjes,stockAgente.getCanjes());
        initialValues.put(ST_devoluciones,stockAgente.getDevoluciones());
        initialValues.put(ST_buenos,stockAgente.getBuenos());
        initialValues.put(ST_malos,stockAgente.getMalos());
        initialValues.put(ST_fisico,stockAgente.getFisico());
        initialValues.put(ST_id_agente,idAgente);
        initialValues.put(Constants._SINCRONIZAR, sincronizado);

        return mDb.insert(SQLITE_TABLE_Stock_Agente, null, initialValues);
    }
    public void updateStockAgentes(StockAgente stockAgente, int idAgente){


        ContentValues initialValues = new ContentValues();
        initialValues.put(ST_id_producto,stockAgente.getIdProducto());
        initialValues.put(ST_nombre,stockAgente.getNombre());
        initialValues.put(ST_codigo,stockAgente.getCodigo());
        initialValues.put(ST_codigo_barras,stockAgente.getCodigoBarras());
        initialValues.put(ST_inicial,stockAgente.getStockInicial());
        initialValues.put(ST_final,stockAgente.getStockFinal());
        initialValues.put(ST_disponible,stockAgente.getDisponible());
        initialValues.put(ST_ventas,stockAgente.getVentas());
        initialValues.put(ST_canjes,stockAgente.getCanjes());
        initialValues.put(ST_devoluciones,stockAgente.getDevoluciones());
        initialValues.put(ST_buenos,stockAgente.getBuenos());
        initialValues.put(ST_malos,stockAgente.getMalos());
        initialValues.put(ST_fisico,stockAgente.getFisico());
        initialValues.put(ST_id_agente,idAgente);
        initialValues.put(Constants._SINCRONIZAR, Constants._ACTUALIZADO);

        mDb.update(SQLITE_TABLE_Stock_Agente, initialValues,
                ST_id_producto+"=?",new String[]{""+stockAgente.getIdProducto()});
    }


    public boolean existsStockAgenteByIdProd(String inputText) throws SQLException {
        boolean exists = false;
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Stock_Agente, new String[] {ST_id_producto,
                        ST_final, ST_disponible, ST_ventas, ST_fisico},
                ST_id_producto + " = " + inputText, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
            exists = true;
        }
        if (mCursor.getCount()==0){
            exists = false;
        }
        return exists;

    }


    public boolean deleteAllStockAgente() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Stock_Agente, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchStockAgenteByIdProd(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Stock_Agente, new String[] {ST_id_producto,
                        ST_final, ST_disponible, ST_ventas, ST_fisico},
                ST_id_producto + " = " + inputText, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    public Cursor fetchStockAgenteByIdEstablecimiento(int idCategorìaEstablecimiento) throws SQLException {
        Cursor mCursor = null;
        mCursor = mDb.rawQuery("SELECT *\n" +
                "FROM m_stock_agente SA\n" +
                "INNER JOIN m_precio P\n" +
                "ON SA.st_in_id_producto = P.pr_in_id_producto\n" +
                "WHERE P.pr_in_id_cat_estt = ?", new String[] {""+idCategorìaEstablecimiento});

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchStockAgenteByIdEstablecimientoandName(int idCategorìaEstablecimiento, String nameProducto) throws SQLException {
        Cursor mCursor = null;
        if (nameProducto == null  ||  nameProducto.length () == 0)  {

            mCursor = mDb.rawQuery("SELECT *\n" +
                    "FROM m_stock_agente SA\n" +
                    "INNER JOIN m_precio P\n" +
                    "ON SA.st_in_id_producto = P.pr_in_id_producto\n" +
                    "WHERE P.pr_in_id_cat_estt ="+idCategorìaEstablecimiento,null);

        } else {


            mCursor = mDb.rawQuery("SELECT *\n" +
                    "FROM m_stock_agente SA\n" +
                    "INNER JOIN m_precio P\n" +
                    "ON SA.st_in_id_producto = P.pr_in_id_producto\n" +
                    "WHERE P.pr_in_id_cat_estt = "+idCategorìaEstablecimiento+" " +
                    "AND SA.st_te_nombre LIKE '%"+nameProducto+"%'", null);
        }

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    public void updateStockAgente(int id_producto, int cantidadFinal,int disponible, int ventas, int fisico){

        ContentValues initialValues = new ContentValues();
        initialValues.put(ST_final, cantidadFinal);
        initialValues.put(ST_disponible,disponible);
        initialValues.put(ST_ventas,ventas);
        initialValues.put(ST_fisico,fisico);
        mDb.update(SQLITE_TABLE_Stock_Agente, initialValues,
                ST_id_producto+"=?",new String[]{""+id_producto});
    }

    public void updateStockAgenteCantidad(int id_producto, int cantidadAnulada){
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Stock_Agente, new String[] {ST_id_producto,
                        ST_final, ST_disponible, ST_ventas, ST_fisico},
                ST_id_producto + " = " + id_producto, null, null, null, null, null);

        int cantidadFinal = 0;

        if (mCursor != null) {
            mCursor.moveToFirst();
            cantidadFinal  = mCursor.getInt(mCursor.getColumnIndex(ST_final));
        }
        int sumado = cantidadFinal+cantidadAnulada;
        int restado = cantidadFinal-cantidadAnulada;

        ContentValues initialValues = new ContentValues();
        initialValues.put(ST_final,sumado);
        initialValues.put(ST_disponible,sumado);
        initialValues.put(ST_ventas,restado);
        initialValues.put(ST_fisico,sumado);
        mDb.update(SQLITE_TABLE_Stock_Agente, initialValues,
                ST_id_producto+"=?",new String[]{""+id_producto});

    }

    public Cursor fetchStockAgentePrecioByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.rawQuery("SELECT * FROM " + SQLITE_TABLE_Stock_Agente + " INNER JOIN " +
                    DbAdapter_Precio.SQLITE_TABLE_Precio + " ON " + SQLITE_TABLE_Stock_Agente + "." +
                    ST_id_producto + " = " + DbAdapter_Precio.SQLITE_TABLE_Precio + "." +
                    DbAdapter_Precio.PR_id_producto + " WHERE " + DbAdapter_Precio.PR_id_cat_est + " = 1", null, null);

        }
        else {
            mCursor = mDb.rawQuery("SELECT * FROM " + SQLITE_TABLE_Stock_Agente + " INNER JOIN " +
                    DbAdapter_Precio.SQLITE_TABLE_Precio + " ON " + SQLITE_TABLE_Stock_Agente + "." +
                    ST_id_producto + " = " + DbAdapter_Precio.SQLITE_TABLE_Precio + "." +
                    DbAdapter_Precio.PR_id_producto + " WHERE " + DbAdapter_Precio.PR_id_cat_est + " = 1 AND " +
                    ST_nombre + " like '%" + inputText + "%'", null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchStockAgentePrecioByNameEst(String inputText, String inputText2) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.rawQuery("SELECT * FROM " + SQLITE_TABLE_Stock_Agente + " INNER JOIN " +
                    DbAdapter_Precio.SQLITE_TABLE_Precio + " ON " + SQLITE_TABLE_Stock_Agente + "." +
                    ST_id_producto + " = " + DbAdapter_Precio.SQLITE_TABLE_Precio + "." +
                    DbAdapter_Precio.PR_id_producto + " WHERE " + DbAdapter_Precio.PR_id_cat_est + " = " +
                    inputText, null, null);

        }
        else {
            mCursor = mDb.rawQuery("SELECT * FROM " + SQLITE_TABLE_Stock_Agente + " INNER JOIN " +
                    DbAdapter_Precio.SQLITE_TABLE_Precio + " ON " + SQLITE_TABLE_Stock_Agente + "." +
                    ST_id_producto + " = " + DbAdapter_Precio.SQLITE_TABLE_Precio + "." +
                    DbAdapter_Precio.PR_id_producto + " WHERE " + DbAdapter_Precio.PR_id_cat_est + " = " +
                    inputText2 + " AND " + ST_nombre + " like '%" + inputText + "%'", null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllStockAgente() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Stock_Agente, new String[] {ST_id_stock_agente,
                        ST_id_producto, ST_nombre, ST_codigo, ST_disponible},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllStockAgentePrecio() {

        Cursor mCursor = mDb.rawQuery("SELECT * FROM " + SQLITE_TABLE_Stock_Agente + " INNER JOIN " +
                DbAdapter_Precio.SQLITE_TABLE_Precio + " ON " + SQLITE_TABLE_Stock_Agente + "." +
                ST_id_producto + " = " + DbAdapter_Precio.SQLITE_TABLE_Precio + "." +
                DbAdapter_Precio.PR_id_producto + " WHERE " + DbAdapter_Precio.PR_id_cat_est + " = 1", null, null);

        //Cursor mCursor = mDb.rawQuery("SELECT * FROM m_stock_agente INNER JOIN m_precio" +
        //        " ON m_stock_agente.st_in_id_producto = m_precio.pr_in_id_producto", null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllStockAgentePrecioEst(String inputText) {

        Cursor mCursor = mDb.rawQuery("SELECT * FROM " + SQLITE_TABLE_Stock_Agente + " INNER JOIN " +
                DbAdapter_Precio.SQLITE_TABLE_Precio + " ON " + SQLITE_TABLE_Stock_Agente + "." +
                ST_id_producto + " = " + DbAdapter_Precio.SQLITE_TABLE_Precio + "." +
                DbAdapter_Precio.PR_id_producto + " WHERE " + DbAdapter_Precio.PR_id_cat_est + " = " +
                inputText, null, null);

        //Cursor mCursor = mDb.rawQuery("SELECT * FROM m_stock_agente INNER JOIN m_precio" +
        //        " ON m_stock_agente.st_in_id_producto = m_precio.pr_in_id_producto", null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllStockAgentePrecioIdProd(String val01, String val02) {

        Cursor mCursor = mDb.rawQuery("SELECT pr_re_precio_unit FROM " + SQLITE_TABLE_Stock_Agente + " INNER JOIN " +
                DbAdapter_Precio.SQLITE_TABLE_Precio + " ON " + SQLITE_TABLE_Stock_Agente + "." +
                ST_id_producto + " = " + DbAdapter_Precio.SQLITE_TABLE_Precio + "." +
                DbAdapter_Precio.PR_id_producto + " WHERE " + DbAdapter_Precio.PR_id_cat_est + " = " +
                val01 + " AND " + ST_id_producto + " like '%" + val02 + "%'", null, null);

        //Cursor mCursor = mDb.rawQuery("SELECT * FROM m_stock_agente INNER JOIN m_precio" +
        //        " ON m_stock_agente.st_in_id_producto = m_precio.pr_in_id_producto", null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor listarProductos(){
        Cursor cr = mDb.rawQuery("select DISTINCT(pr_in_id_producto) _id,(pr_in_nombre) from m_precio ",null);
        return  cr ;
    }
    public Cursor listarbyIdProducto  (String nombre)  throws SQLException{
     Cursor cr = mDb.rawQuery("select DISTINCT(pr_in_id_producto) _id,(pr_in_nombre) from m_precio where pr_in_nombre LIKE '%"+nombre+"%'",null);
      return cr;
    }

    public Cursor fetchAllStockAgenteVentas() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Stock_Agente, new String[] {ST_id_stock_agente,
                        ST_codigo,ST_nombre,ST_inicial,ST_final,ST_ventas,ST_devoluciones,ST_canjes,ST_buenos,ST_malos,ST_disponible},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeStockAgente() {

        createStockAgente( 1, "Pan Integral Mediano", "1A", "11", 1, 10, 10, 0, 0, 0, 0, 0, 10, 1);
        createStockAgente( 2, "Pan Blanco Americano", "2A", "22", 20, 20, 20, 0, 0, 0, 0, 0, 20, 1);
        createStockAgente( 3, "Granola con pasas y almendras", "3A", "33", 30, 30, 30, 0, 0, 0, 0, 0, 30, 1);
        createStockAgente( 4, "Panetón Super Boom", "4A", "44", 40, 40, 40, 0, 0, 0, 0, 0, 40, 1);
        createStockAgente( 5, "Panetón Integral", "5A", "55", 50, 50, 50, 0, 0, 0, 0, 0, 50, 1);

    }

}