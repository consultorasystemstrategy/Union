package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.Conexion.JSONParser;
import union.union_vr1.JSONParser.ParserAgente;
import union.union_vr1.JSONParser.ParserComprobanteCobro;
import union.union_vr1.JSONParser.ParserComprobanteVentaDetalle;
import union.union_vr1.JSONParser.ParserEventoEstablecimiento;
import union.union_vr1.JSONParser.ParserHistorialVentaDetalles;
import union.union_vr1.JSONParser.ParserPrecio;
import union.union_vr1.JSONParser.ParserStockAgente;
import union.union_vr1.JSONParser.ParserTipoGasto;
import union.union_vr1.Objects.Agente;
import union.union_vr1.Objects.ComprobanteCobro;
import union.union_vr1.Objects.ComprobanteVentaDetalle;
import union.union_vr1.Objects.EventoEstablecimiento;
import union.union_vr1.Objects.HistorialVentaDetalles;
import union.union_vr1.Objects.Precio;
import union.union_vr1.Objects.StockAgente;
import union.union_vr1.Objects.TipoGasto;
import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Histo_Comprob_Anterior;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Ruta_Distribucion;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Gasto;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.MyApplication;
import union.union_vr1.Vistas.VMovil_Evento_Indice;
import union.union_vr1.Vistas.VMovil_Online_Pumovil;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ImportMain extends AsyncTask<String, String, String> {




    private DbAdapter_Temp_Session session;
    private Activity mainActivity;
    private ProgressDialog progressDialog;

    private DbAdapter_Agente dbAdapter_agente;
    private DbAdapter_Stock_Agente dbAdapter_stock_agente;
    private DbAdapter_Precio dbAdapter_precio;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DbAdapter_Tipo_Gasto dbAdapter_tipo_gasto;
    private DbAdapter_Comprob_Cobro dbAdapter_comprob_cobro;
    private DbAdapter_Histo_Venta_Detalle dbAdapter_histo_venta_detalle;
    private DbAdapter_Histo_Comprob_Anterior dbAdapter_histo_comprob_anterior;
    private DBAdapter_Temp_Autorizacion_Cobro dbAdapter_temp_autorizacion_cobro;
    private DbAdapter_Ruta_Distribucion dbAdapter_ruta_distribucion;



    //private ListView listView;
    private SimpleCursorAdapter simpleCursorAdapter;

    private int idAgente;
    private int idLiquidacion;

    public ImportMain(Activity mainActivity) {
        this.mainActivity = mainActivity;

        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();

        dbAdapter_agente = new DbAdapter_Agente(mainActivity);
        dbAdapter_agente.open();
        dbAdapter_stock_agente = new DbAdapter_Stock_Agente(mainActivity);
        dbAdapter_stock_agente.open();
        dbAdapter_precio = new DbAdapter_Precio(mainActivity);
        dbAdapter_precio.open();
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdaptert_evento_establec.open();
        dbAdapter_tipo_gasto = new DbAdapter_Tipo_Gasto(mainActivity);
        dbAdapter_tipo_gasto.open();
        dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(mainActivity);
        dbAdapter_comprob_cobro.open();
        dbAdapter_histo_venta_detalle = new DbAdapter_Histo_Venta_Detalle(mainActivity);
        dbAdapter_histo_venta_detalle.open();
        dbAdapter_histo_comprob_anterior = new DbAdapter_Histo_Comprob_Anterior(mainActivity);
        dbAdapter_histo_comprob_anterior.open();
        dbAdapter_temp_autorizacion_cobro = new DBAdapter_Temp_Autorizacion_Cobro(mainActivity);
        dbAdapter_temp_autorizacion_cobro.open();
        dbAdapter_ruta_distribucion = new DbAdapter_Ruta_Distribucion(mainActivity);
        dbAdapter_ruta_distribucion.open();



//        idAgente = ((MyApplication)mainActivity.getApplication()).getIdAgente();
            idAgente = session.fetchVarible(1);
            idLiquidacion = session.fetchVarible(3);

        //listView = (ListView) mainActivity.findViewById(R.id.listView);
    }

    @Override
    protected String doInBackground(String... strings) {


        StockAgenteRestApi api = new StockAgenteRestApi();

        Cursor cursor = dbAdapter_agente.fetchAgentesByIds(idAgente, idLiquidacion);
        cursor.moveToFirst();


        /*
        if (cursor.getCount()>0){
            idLiquidacion = cursor.getInt(cursor.getColumnIndexOrThrow(dbAdapter_agente.AG_liquidacion));
        }*/


        ArrayList<StockAgente> stockAgentes = null;
        ArrayList<TipoGasto> tipoGastos = null;
        ArrayList<Precio> precios = null;
        ArrayList<EventoEstablecimiento> eventoEstablecimientos = null;
        ArrayList<ComprobanteCobro> comprobanteCobros = null;
        ArrayList<HistorialVentaDetalles> historialVentaDetalleses = null;
        ArrayList<ComprobanteVentaDetalle> comprobanteVentaDetalles = null;

        String fecha = getDatePhone();

        Log.d("iIMPORT DATOS REALES dAgente, idLiquidacion, Fecha", idAgente+", "+idLiquidacion+","+fecha);

/*
        idAgente = 3;
        idLiquidacion = 10;
        fecha = "02/02/2015";
*/
        Log.d("IMPORT DATOS DE PRUEBA ESTABLECIMIENTO X RUTA idAgente, idLiquidacion, Fecha", idAgente+", "+idLiquidacion+","+fecha);

        try{
            publishProgress(""+5);
            JSONObject jsonObjectStockAgente = api.GetStockAgente(idAgente);
            Log.d("JSON ERROR MESSAGE", getErrorMessage(jsonObjectStockAgente));
            publishProgress(""+10);
            JSONObject jsonObjectTipoGasto = api.GetTipoGasto();
            publishProgress(""+15);
            JSONObject jsonObjectPrecio = api.GetPrecioCategoria(idLiquidacion,idAgente);
            publishProgress(""+20);
            JSONObject jsonObjectEventoEstablecimiento = api.GetEstablecimeintoXRuta(idLiquidacion,fecha, idAgente);
            publishProgress(""+25);
            JSONObject jsonObjectComprobanteCobro = api.GetHistorialCobrosPendientes();
            publishProgress(""+30);
            JSONObject jsonObjectHistorialVentaDetalle = api.GetHistorialVentaDetalle(idAgente);
            publishProgress(""+35);
            JSONObject jsonObjectHistorialComprobanteAnterior = api.GetComprobanteVentaDetalle_Env();
            publishProgress(""+40);
            JSONObject jsonObjectRutaDistribucion = api.GetConsultarPlan_Distribucion(idAgente);


            Log.d("JSON OBJECT STOCK AGENTE : ", jsonObjectStockAgente.toString());
            Log.d("JSON OBJECT PRECIO : ", jsonObjectPrecio.toString());
            Log.d("JSON OBJECT TIPO GASTO : ", jsonObjectTipoGasto.toString());
            Log.d("JSON OBJECT EVENTO ESTABLECIMIENTO : ", jsonObjectEventoEstablecimiento.toString());
            Log.d("JSON OBJECT COMPROBANTE COBRO : ", jsonObjectComprobanteCobro.toString());
            Log.d("JSON OBJECT HISTORIAL VENTA DETALLE : ", jsonObjectHistorialVentaDetalle.toString());
            Log.d("JSON OBJECT HISTORIAL VENTA ANTERIOR ", jsonObjectHistorialComprobanteAnterior.toString());
            Log.d("JSON OBJECT RUTA DISTRIBUCION ", jsonObjectRutaDistribucion.toString());



            ParserStockAgente parserStockAgente = new ParserStockAgente();
            ParserTipoGasto parserTipoGasto = new ParserTipoGasto();
            ParserPrecio parserPrecio = new ParserPrecio(mainActivity);
            ParserEventoEstablecimiento parserEventoEstablecimiento = new ParserEventoEstablecimiento();
            ParserComprobanteCobro  parserComprobanteCobro = new ParserComprobanteCobro();
            ParserHistorialVentaDetalles parserHistorialVentaDetalles = new ParserHistorialVentaDetalles();
            ParserComprobanteVentaDetalle parserComprobanteVentaDetalle = new ParserComprobanteVentaDetalle();

            stockAgentes = parserStockAgente.parserStockAgente(jsonObjectStockAgente);
            tipoGastos = parserTipoGasto.parserTipoGasto(jsonObjectTipoGasto);
            precios = parserPrecio.parserPrecio(jsonObjectPrecio);
            eventoEstablecimientos = parserEventoEstablecimiento.parserEventoEstablecimiento(jsonObjectEventoEstablecimiento);
            comprobanteCobros = parserComprobanteCobro.parserComprobanteCobro(jsonObjectComprobanteCobro);
            historialVentaDetalleses = parserHistorialVentaDetalles.parserHistorialVentaDetalles(jsonObjectHistorialVentaDetalle);
            comprobanteVentaDetalles = parserComprobanteVentaDetalle.parserComprobanteVentaDetalle(jsonObjectHistorialComprobanteAnterior);

            publishProgress(""+45);
            Log.d("IMPORTANDO ", "INICIANDO ...");
            for (int i = 0; i < stockAgentes.size() ; i++) {
                Log.d("Stock Agente"+i, "Nombre : "+stockAgentes.get(i).getNombre());
                Log.d("Stock Agente"+i, "CÓDIGO DE BARRAS : "+stockAgentes.get(i).getCodigoBarras());

                boolean existe = dbAdapter_stock_agente.existsStockAgenteByIdProd(stockAgentes.get(i).getIdProducto(),idLiquidacion);
                Log.d("EXISTE ", ""+existe);
                if (existe){
                    dbAdapter_stock_agente.updateStockAgentes(stockAgentes.get(i),idAgente,idLiquidacion);
                }else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_stock_agente.createStockAgentes(stockAgentes.get(i), idAgente, Constants._IMPORTADO,idLiquidacion);
                }
            }

            publishProgress(""+50);
            for (int i=0; i<tipoGastos.size(); i++){
                Log.d("TipoGastos"+i, "Nombre : "+tipoGastos.get(i).getNombreTipoGasto());

                boolean existe = dbAdapter_tipo_gasto.existeTipoGastos(tipoGastos.get(i).getIdTipoGasto());
                Log.d("EXISTE ", ""+existe);
                if (existe){
                    dbAdapter_tipo_gasto.updateTipoGasto(tipoGastos.get(i));
                }else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_tipo_gasto.createTipoGasto(tipoGastos.get(i));
                }
            }

            for (int i = 0; i < precios.size() ; i++) {
                Log.d("PRECIO CATEGORÌA : " + i, "Nombre producto : " + precios.get(i).getNombreProducto());

                boolean existe = dbAdapter_precio.existePrecio(precios.get(i).getIdProducto(), precios.get(i).getIdCategoriaEstablecimiento(), precios.get(i).getValorUnidad());
                Log.d("EXISTE ", ""+existe);
                if (existe){
                    dbAdapter_precio.updatePrecios(precios.get(i), idAgente);
                }else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_precio.createPrecios(precios.get(i), idAgente);
                }
            }
            publishProgress(""+55);
            for (int i = 0; i < eventoEstablecimientos.size() ; i++) {
                Log.d("ESTABLECIMIENTOS X RUTAS: " + i, " Nombre Establecimiento : " + eventoEstablecimientos.get(i).getNombreEstablecimiento() + ", orden : " + eventoEstablecimientos.get(i).getOrden() +", BARCODE: "+eventoEstablecimientos.get(i).getCodigoBarras());
                boolean existe = dbAdaptert_evento_establec.existeEstablecsById(eventoEstablecimientos.get(i).getIdEstablecimiento());

                Log.d("EXISTE ESTABLECIMIENTO", ""+existe);
                if (existe){
                    //dbAdapter_comprob_cobro.updateComprobCobros(comprobanteCobros.get(i));
                    dbAdaptert_evento_establec.updateEstablecimientos(eventoEstablecimientos.get(i), idAgente, idLiquidacion);
                }else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    long id = dbAdaptert_evento_establec.createEstablecimientos(eventoEstablecimientos.get(i), idAgente, idLiquidacion);

                    Log.d("IMPORT INSERT ESTABLECIMIENTO id ", ""+id);
                }
            }

            publishProgress(""+60);
            for (int i = 0; i < comprobanteCobros.size() ; i++) {
                Log.d("HISTORIAL COBROS PENDIENTES : " + i, " Monto a pagar : " + comprobanteCobros.get(i).getMontoPagar()+"-fecha-"+comprobanteCobros.get(i).getFechaProgramada());
                boolean existe = dbAdapter_comprob_cobro.existeComprobCobro(comprobanteCobros.get(i).getIdComprobanteCobro());
                Log.d("EXISTE ", ""+existe);
                if (existe){
                    dbAdapter_comprob_cobro.updateComprobCobros(comprobanteCobros.get(i));
                }else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_comprob_cobro.createComprobCobro(comprobanteCobros.get(i));
                }
            }
            publishProgress(""+65);
            for (int i = 0; i < historialVentaDetalleses.size() ; i++) {
                Log.d("HISTORIAL VENTA DETALLES : " + i, " ID DETALLE : " + historialVentaDetalleses.get(i).getIdDetalle());
                Log.d("JODER ", "SI HAY DATOS");
                boolean existe = dbAdapter_histo_venta_detalle.existeHistoVentaDetalle(historialVentaDetalleses.get(i).getIdDetalle());

                Log.d("EXISTE ", ""+existe);
                if (existe){
                    dbAdapter_histo_venta_detalle.updateHistoVentaDetalle(historialVentaDetalleses.get(i));
                }else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_histo_venta_detalle.createHistoVentaDetalle(historialVentaDetalleses.get(i));
                }
            }

            publishProgress(""+90);
            for (int i = 0; i < comprobanteVentaDetalles.size() ; i++) {
                Log.d("HISTORIAL VENTA ANTERIOR : " + i + " - "+ comprobanteVentaDetalles.get(i).getIdComprobante()+ " - " + comprobanteVentaDetalles.get(i).getIdEstablecimiento(), " NOMBRE PRODUCTO : " + comprobanteVentaDetalles.get(i).getNombreProducto());

                boolean existe = dbAdapter_histo_comprob_anterior.existeComprobanteVentaAnterior(comprobanteVentaDetalles.get(i).getIdComprobante());

                Log.d("EXISTE ", ""+existe);
                if (existe){
                    dbAdapter_histo_comprob_anterior.updateHistoComprobAnterior(comprobanteVentaDetalles.get(i));
                }else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_histo_comprob_anterior.createHistoComprobAnterior(comprobanteVentaDetalles.get(i));
                }
            }

            publishProgress(""+95);
            //ACTUALIZAR LOS CRÉDITOS DE LOS ESTABLECIMIENTOS

            Cursor cursorEstablecimiento = dbAdaptert_evento_establec.fetchAllEstablecs();

            for (cursorEstablecimiento.moveToFirst(); !cursorEstablecimiento.isAfterLast(); cursorEstablecimiento.moveToNext()){

                int idEstablecimiento = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_establec));
                JSONObject jsonObject = api.GetSolicitudAutorizacionEstablecimiento(idEstablecimiento);
                JSONObject jsonObjectAutorizacion = api.GetConsultarAutorizacion(idEstablecimiento);

                Log.d("IMPORT SOLICITUDES DE AUTORIZACIÓN ", jsonObject.toString());
                Log.d("IMPORT AUTORIZACION COBROS  ", jsonObjectAutorizacion.toString());


                if (isSuccesfulImport(jsonObjectAutorizacion)) {

                    JSONArray jsonArray = jsonObjectAutorizacion.getJSONArray("Value");
                    JSONObject jsonObj = null;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObj = jsonArray.getJSONObject(i);

                        int idEstablecimiento1 = jsonObj.getInt("EstIEstablecimientoId");
                        int estadoSolicitud = jsonObj.getInt("SolIEstadoSolicitudId");
                        int idAutorizacionCobro = jsonObj.getInt("SolObservacion");
                        String fechaLimite = jsonObj.getString("CliDTFechaLimiteCredito");
                        Log.d("HolaMundo",fechaLimite+"-"+idEstablecimiento1+"-"+idAutorizacionCobro);

                        boolean exists = dbAdapter_temp_autorizacion_cobro.existeAutorizacionCobro(idAutorizacionCobro);

                        Log.d("EXISTE AC", "" + exists);
                        if (exists) {
                            int isActualizado = dbAdapter_temp_autorizacion_cobro.updateAutorizacionCobro(idAutorizacionCobro, estadoSolicitud, idEstablecimiento1,fechaLimite);
                            Log.d("IMPORT REGISTRO AUTORIZACION COBRO ACTUALIZADO ", "" + isActualizado);

                        }
                    }

                }


                if (isSuccesfulImport(jsonObject)) {

                    JSONArray jsonArray = jsonObject.getJSONArray("Value");
                    JSONObject jsonObj=null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObj=jsonArray.getJSONObject(i);
                        int idEstablecimiento1 = jsonObj.getInt("EstIEstablecimientoId");
                        int montoCredito = jsonObj.getInt("SolDOMontoCredito");
                        int diasCredito = jsonObj.getInt("SolIVigenciaCredito");
                        Log.d("IMPORT SOLICITUDES DATOS", idEstablecimiento + " - " + montoCredito + " - " + diasCredito);
                        dbAdaptert_evento_establec.updateEstablecsCredito(idEstablecimiento, montoCredito, diasCredito);
                    }
                }


            }

            if (isSuccesfulImport(jsonObjectRutaDistribucion)){
                JSONArray jsonArray = jsonObjectRutaDistribucion.getJSONArray("Value");
                JSONObject jsonObj=null;

                dbAdapter_ruta_distribucion.delleteAllRutaByIdAgente(idAgente);

                for (int i=0; i< jsonArray.length(); i++){
                    jsonObj = jsonArray.getJSONObject(i);

                    int id = jsonObj.getInt("Id");
                    int idRuta = jsonObj.getInt("RutaID");
                    String nombre = jsonObj.getString("Ruta");
                    String diaSemana = jsonObj.getString("DiaSemana");
                    int numeroEstablecimiento = jsonObj.getInt("Establecimientos");




                    Log.d("IMPORT RUTA SEMANAL DATOS", diaSemana + " - " + nombre + " - " + numeroEstablecimiento + " - "+ idAgente);
                    long idRutaInsertada = dbAdapter_ruta_distribucion.createRutaDistribucion(id, idRuta, nombre, diaSemana, numeroEstablecimiento, idAgente);
                    Log.d("IMPORT ID RUTA INSERTADA", ""+ idRutaInsertada);

                }


            }


            publishProgress(""+99);


        }catch (Exception e){
            Log.d("AysncImport : ", e.getMessage());

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        createProgressDialog();
    }

    @Override
    protected void onPostExecute(String s) {
        if(mainActivity.isFinishing()){
            //dismissProgressDialog();
            progressDialog.dismiss();
            return;
        }else {

            progressDialog.setProgress(100);
            dismissProgressDialog();
            Toast.makeText(mainActivity.getApplicationContext(), "IMPORTACIÓN EXITOSA", Toast.LENGTH_LONG).show();

        }
        super.onPostExecute(s);
/*
//stock agente
        Cursor cursor = dbAdapter_stock_agente.fetchAllStockAgente();

        String from[] = {
                DbAdapter_Stock_Agente.ST_id_producto
        };

/*

        //TIPO GASTO
        Cursor cursor = dbAdapter_tipo_gasto.fetchAllTipoGastos();

        String from[] = {
                DbAdapter_Tipo_Gasto.TG_nom_tipo_gasto
        };

        */

/*
        Cursor cursor = dbAdapter_precio.fetchAllPrecio();

        String from[] = {
                DbAdapter_Precio.PR_nombreProducto
        };

*/

        /*
        //EVENTO ESTABLECIMIENTO

        Cursor cursor = dbAdaptert_evento_establec.fetchAllEstablecs();

        String from[] = {
                DbAdaptert_Evento_Establec.EE_nom_establec
        };
*/
        Cursor cursor = dbAdapter_comprob_cobro.fetchAllComprobCobros();

        String from[] = {
                DbAdapter_Comprob_Cobro.CC_fecha_programada
        };


/*

        Cursor cursor = dbAdapter_histo_venta_detalle.fetchAllHistoVentaDetalle();

        String from[] = {
                DbAdapter_Histo_Venta_Detalle.HD_id_detalle
        };

*/

        int to[] = {
                R.id.textView_detalles
        };
        simpleCursorAdapter = new SimpleCursorAdapter(
                mainActivity,
                R.layout.layout_detalles,
                cursor,
                from,
                to,
                0
        );

        //listView.setAdapter(simpleCursorAdapter);

        Intent intent = new Intent(mainActivity, VMovil_Evento_Indice.class);
        mainActivity.finish();
        mainActivity.startActivity(intent);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(Integer.parseInt(values[0]));
    }

    public void createProgressDialog(){
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setMessage("Importando ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public String getErrorMessage(JSONObject jsonObj)
    {
        String errorMessage = "Error Message null";
        try {
               errorMessage +=jsonObj.getString("ErrorMessage");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parser Error Message", e.getMessage());
        }
        return errorMessage;
    }

    public boolean isSuccesfulImport(JSONObject jsonObj)
    {
        boolean succesful= false;
        try {
            Log.d("CADEMA A ÁRSEAR BOOLEAN ", jsonObj.toString());
            succesful= jsonObj.getBoolean("Successful");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parser Error Message", e.getMessage());
            Log.d("JSON PARSER => parser Error Message", e.getMessage());
        }
        return succesful;
    }


    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }

}
