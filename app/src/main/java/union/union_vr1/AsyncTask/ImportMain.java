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
import union.union_vr1.JSONParser.ParserFormaPago;
import union.union_vr1.JSONParser.ParserHistorialVentaDetalles;
import union.union_vr1.JSONParser.ParserModalidadCredito;
import union.union_vr1.JSONParser.ParserPrecio;
import union.union_vr1.JSONParser.ParserStockAgente;
import union.union_vr1.JSONParser.ParserTipoGasto;
import union.union_vr1.Objects.Agente;
import union.union_vr1.Objects.ComprobanteCobro;
import union.union_vr1.Objects.ComprobanteVentaDetalle;
import union.union_vr1.Objects.EventoEstablecimiento;
import union.union_vr1.Objects.FormaPago;
import union.union_vr1.Objects.HistorialVentaDetalles;
import union.union_vr1.Objects.ModalidadCredito;
import union.union_vr1.Objects.Precio;
import union.union_vr1.Objects.StockAgente;
import union.union_vr1.Objects.TipoGasto;
import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Distritos;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Forma_Pago;
import union.union_vr1.Sqlite.DbAdapter_Histo_Comprob_Anterior;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_ModalidadCredito;
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
    private DBAdapter_Distritos dbAdapter_distritos;
    private DbAdapter_ModalidadCredito dbAdapter_modalidadCredito;
    private DbAdapter_Forma_Pago dbAdapter_forma_pago;


    //private ListView listView;
    //private SimpleCursorAdapter simpleCursorAdapter;

    private int idAgente;
    private int idLiquidacion;

    private static String TAG = "IMPORT MAIN";

    public ImportMain(Activity mainActivity) {
        this.mainActivity = mainActivity;

        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();
        dbAdapter_distritos = new DBAdapter_Distritos(mainActivity);
        dbAdapter_distritos.open();
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
        dbAdapter_modalidadCredito = new DbAdapter_ModalidadCredito(mainActivity);
        dbAdapter_modalidadCredito.open();
        dbAdapter_forma_pago = new DbAdapter_Forma_Pago(mainActivity);
        dbAdapter_forma_pago.open();


//        idAgente = ((MyApplication)mainActivity.getApplication()).getIdAgente();
        idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);
        Log.d("DATOS SESSION", " AGENTE: " + idAgente + " LIQUIDACION: " + idLiquidacion);

        //listView = (ListView) mainActivity.findViewById(R.id.listView);
    }

    @Override
    protected String doInBackground(String... strings) {


        StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);

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
        ArrayList<ModalidadCredito> modalidadCreditos = null;
        ArrayList<FormaPago> formaPagos = null;

        String fecha = getDatePhone();

        Log.d("iIMPORT DATOS REALES dAgente, idLiquidacion, Fecha", idAgente + ", " + idLiquidacion + "," + fecha);

/*
        idAgente = 3;
        idLiquidacion = 10;
        fecha = "02/02/2015";
*/
        Log.d(TAG, "IMPORT DATOS DE PRUEBA ESTABLECIMIENTO X RUTA idAgente, idLiquidacion, Fecha : " + idAgente + ", " + idLiquidacion + "," + fecha);

        try {



            //
            JSONObject jsonObjectStockAgente = api.GetStockAgente(idAgente);
            //Log.d("JSON ERROR MESSAGE", getErrorMessage(jsonObjectStockAgente));
            JSONObject jsonObjectTipoGasto = api.GetTipoGasto();
            JSONObject jsonObjectModalidadCredito = api.fsel_ModalidadCredito(idAgente);
            JSONObject jsonObjectFormaPago = api.fsel_TipoPago();

            JSONObject jsonObjectPrecio = api.GetPrecioCategoria(idLiquidacion, idAgente);
            JSONObject jsonObjectEventoEstablecimiento = api.GetEstablecimeintoXRuta(idLiquidacion, fecha, idAgente);
            //JSONObject jsonObjectComprobanteCobro = api.GetHistorialCobrosPendientes();
            JSONObject jsonObjectComprobanteCobro = api.GetHistorialCobrosPendientesXLiquidacion(idLiquidacion, idAgente);
            JSONObject jsonObjectHistorialVentaDetalle = api.GetHistorialVentaDetalle(idAgente);
            JSONObject jsonObjectHistorialComprobanteAnterior = api.fsel_UltimaVenta(idLiquidacion, idAgente);
            JSONObject jsonObjectRutaDistribucion = api.GetConsultarPlan_Distribucion(idAgente);


            Log.d(TAG, "JSON OBJECT STOCK AGENTE : " + jsonObjectStockAgente.toString());
            Log.d(TAG, "JSON OBJECT PRECIO : " + jsonObjectPrecio.toString());
            Log.d(TAG, "JSON OBJECT TIPO GASTO : " + jsonObjectTipoGasto.toString());
            Log.d(TAG, "JSON MODALIDAD CREDITO : " + jsonObjectModalidadCredito.toString());
            Log.d(TAG, "JSON FORMA PAGO : " + jsonObjectFormaPago.toString());
            Log.d(TAG, "JSON OBJECT EVENTO ESTABLECIMIENTO : " + jsonObjectEventoEstablecimiento.toString());
            Log.d(TAG, "JSON OBJECT COMPROBANTE COBRO : " + jsonObjectComprobanteCobro.toString());
            Log.d(TAG, "JSON OBJECT HISTORIAL VENTA DETALLE : " + jsonObjectHistorialVentaDetalle.toString());
            Log.d(TAG, "JSON OBJECT HISTORIAL VENTA ANTERIOR " + jsonObjectHistorialComprobanteAnterior.toString());
            Log.d(TAG, "JSON OBJECT RUTA DISTRIBUCION " + jsonObjectRutaDistribucion.toString());


            ParserStockAgente parserStockAgente = new ParserStockAgente();
            ParserTipoGasto parserTipoGasto = new ParserTipoGasto();
            ParserModalidadCredito parserModalidadCredito = new ParserModalidadCredito();
            ParserFormaPago parserFormaPago = new ParserFormaPago();
            ParserPrecio parserPrecio = new ParserPrecio(idAgente);
            ParserEventoEstablecimiento parserEventoEstablecimiento = new ParserEventoEstablecimiento();
            ParserComprobanteCobro parserComprobanteCobro = new ParserComprobanteCobro();
            ParserHistorialVentaDetalles parserHistorialVentaDetalles = new ParserHistorialVentaDetalles();
            ParserComprobanteVentaDetalle parserComprobanteVentaDetalle = new ParserComprobanteVentaDetalle();

            stockAgentes = parserStockAgente.parserStockAgente(jsonObjectStockAgente);
            tipoGastos = parserTipoGasto.parserTipoGasto(jsonObjectTipoGasto);
            modalidadCreditos = parserModalidadCredito.parserModalidadCredito(jsonObjectModalidadCredito);
            formaPagos = parserFormaPago.parserFormaPago(jsonObjectFormaPago, idLiquidacion, fecha);
            precios = parserPrecio.parserPrecio(jsonObjectPrecio);
            eventoEstablecimientos = parserEventoEstablecimiento.parserEventoEstablecimiento(jsonObjectEventoEstablecimiento);
            comprobanteCobros = parserComprobanteCobro.parserComprobanteCobro(jsonObjectComprobanteCobro);
            historialVentaDetalleses = parserHistorialVentaDetalles.parserHistorialVentaDetalles(jsonObjectHistorialVentaDetalle);
            comprobanteVentaDetalles = parserComprobanteVentaDetalle.parserComprobanteVentaDetalle(jsonObjectHistorialComprobanteAnterior);


            //Import Distrito

            //Ver si existe ya datos en el dispositivo

            int disCursor = dbAdapter_distritos.listarDistritos().getCount();

            if (disCursor == 0) {

                JSONObject jsonObjectDistritos = api.fsel_Distritos();
                Log.d(TAG, "JSON OBJECT DISTRITOS " + jsonObjectDistritos.toString());

                JSONArray jsonArray = jsonObjectDistritos.getJSONArray("Value");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);

                    long in = dbAdapter_distritos.createDistritos(object.getInt("Id"),object.getString("Distrito"));
                    Log.d(TAG, "JSON OBJECT DISTRITOS INSERTO " + in);

                }


            } else {
                Log.d(TAG, "JSON OBJECT DISTRITOS " + "DISTRITOS YA IMPORTADOS");
            }


            if (eventoEstablecimientos.size() <= 0) {
                Log.d(TAG, "EVENTO ESTABLECIMIENTO VACÍO, OBTENIENDO NUEVAMENTE ...");
                jsonObjectEventoEstablecimiento = api.GetEstablecimeintoXRuta(idLiquidacion, fecha, idAgente);
                eventoEstablecimientos = parserEventoEstablecimiento.parserEventoEstablecimiento(jsonObjectEventoEstablecimiento);
            }

            for (int i = 0; i < historialVentaDetalleses.size(); i++) {

                boolean existe = dbAdapter_histo_venta_detalle.existeHistoVentaDetalle(historialVentaDetalleses.get(i).getIdDetalle());

                Log.d("EXISTE DETALLE", "" + existe);
                if (existe) {
                    dbAdapter_histo_venta_detalle.updateHistoVentaDetalle(historialVentaDetalleses.get(i));
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_histo_venta_detalle.createHistoVentaDetalle(historialVentaDetalleses.get(i));
                }
            }


            boolean rutaSuccess = isSuccesfulImport(jsonObjectRutaDistribucion);
            Log.d("RUTA SUCCESS", "" + rutaSuccess);
            if (rutaSuccess) {
                JSONArray jsonArray = jsonObjectRutaDistribucion.getJSONArray("Value");
                JSONObject jsonObj = null;

                dbAdapter_ruta_distribucion.delleteAllRutaByIdAgente(idAgente);

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObj = jsonArray.getJSONObject(i);

                    int id = jsonObj.getInt("Id");
                    int idRuta = jsonObj.getInt("RutaID");
                    String nombre = jsonObj.getString("Ruta");
                    String diaSemana = jsonObj.getString("DiaSemana");
                    int numeroEstablecimiento = jsonObj.getInt("Establecimientos");


                    Log.d("IMPORT RUTA ", diaSemana + " - " + nombre + " - " + numeroEstablecimiento + " - " + idAgente);
                    long idRutaInsertada = dbAdapter_ruta_distribucion.createRutaDistribucion(id, idRuta, nombre, diaSemana, numeroEstablecimiento, idAgente);
                    Log.d("IMPORT ID RUTA ins", "" + idRutaInsertada);

                }


            }

            Log.d("IMPORTANDO ", "INICIANDO ...");
            for (int i = 0; i < stockAgentes.size(); i++) {
                Log.d("Stock Agente" + i, "Nombre : " + stockAgentes.get(i).getNombre());
                Log.d("Stock Agente" + i, "CÓDIGO DE BARRAS : " + stockAgentes.get(i).getCodigoBarras());
                Log.d("Stock Agente" + i, "CÓDIGO DE producto : " + stockAgentes.get(i).getCodigo());


                boolean existe = dbAdapter_stock_agente.existsStockAgenteByIdProd(stockAgentes.get(i).getIdProducto(), idLiquidacion);
                Log.d("EXISTE ", "" + existe);
                if (existe) {
                    dbAdapter_stock_agente.updateStockAgentes(stockAgentes.get(i), idAgente, idLiquidacion);
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_stock_agente.createStockAgentes(stockAgentes.get(i), idAgente, Constants._IMPORTADO, idLiquidacion);
                }
            }


            for (int i = 0; i < tipoGastos.size(); i++) {
                Log.d("TipoGastos" + i, "Nombre : " + tipoGastos.get(i).getNombreTipoGasto());

                boolean existe = dbAdapter_tipo_gasto.existeTipoGastos(tipoGastos.get(i).getIdTipoGasto());
                Log.d("EXISTE ", "" + existe);
                if (existe) {
                    dbAdapter_tipo_gasto.updateTipoGasto(tipoGastos.get(i));
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_tipo_gasto.createTipoGasto(tipoGastos.get(i));
                }
            }

            dbAdapter_modalidadCredito.deleteAllModalidadCreditos();


            for (int i = 0; i < modalidadCreditos.size(); i++) {
                Log.d("MODALIDAD DE CRÉDITOS" + i, "ID : " + modalidadCreditos.get(i).getId() + ", descripcion : " + modalidadCreditos.get(i).getDescripcion() + ", DIAS CRÉDITO :" + modalidadCreditos.get(i).getDiasCredito());

                boolean existe = dbAdapter_modalidadCredito.existeModalidadCreditos(modalidadCreditos.get(i).getId());
                Log.d(TAG, "EXISTE MODALIDAD CREDITO " + existe);

                if (existe) {
                    dbAdapter_modalidadCredito.updateModalidadCredito(modalidadCreditos.get(i));
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO

                    dbAdapter_modalidadCredito.createModalidadCreditos(modalidadCreditos.get(i));
                }
            }

            dbAdapter_forma_pago.deleteAllFormaPagos();

            //Log.d(TAG, "FORMA PAGO ELIMNADO COUNT : "+ocuntFPDeleted);
            for (int i = 0; i < formaPagos.size(); i++) {
                Log.d(TAG, "FORMA DE PAGOS" + i + "ID : " + formaPagos.get(i).get_id_forma_pago() + ", descripcion : " + formaPagos.get(i).getDetalle() + ", SELECTED :" + formaPagos.get(i).getSelected());

                boolean existe = dbAdapter_forma_pago.existeFormaPagos(formaPagos.get(i).get_id_forma_pago());
                Log.d(TAG, "EXISTE FORMA PAGO" + existe);

                if (existe) {
                    dbAdapter_forma_pago.updateFormaPago(formaPagos.get(i));
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO

                    dbAdapter_forma_pago.createFormaPago(formaPagos.get(i));
                }
            }

            for (int i = 0; i < precios.size(); i++) {
                Log.d("PRECIO CATEGORÌA : " + i, "Nombre producto : " + precios.get(i).getNombreProducto());

                boolean existe = dbAdapter_precio.existePrecio(precios.get(i).getIdProducto(), precios.get(i).getIdCategoriaEstablecimiento(), precios.get(i).getValorUnidad());
                Log.d("EXISTE ", "" + existe);
                if (existe) {
                    int updatedPrecios = dbAdapter_precio.updatePrecios(precios.get(i), idAgente);
                    Log.d(TAG, "ACTUALIZADOS NRO: "+ updatedPrecios);
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_precio.createPrecios(precios.get(i), idAgente);
                }
            }


            for (int i = 0; i < eventoEstablecimientos.size(); i++) {
                Log.d(TAG, "ESTABLECIMIENTOS X RUTAS: " + i+", Nombre Establecimiento : " + eventoEstablecimientos.get(i).getNombreEstablecimiento() + ", orden : " + eventoEstablecimientos.get(i).getOrden() + ", BARCODE: " + eventoEstablecimientos.get(i).getCodigoBarras());
                boolean existe = dbAdaptert_evento_establec.existeEstablecsById(eventoEstablecimientos.get(i).getIdEstablecimiento());

                Log.d("EXISTE ESTABLECIMIENTO", "" + existe);
                if (existe) {
                    //dbAdapter_comprob_cobro.updateComprobCobros(comprobanteCobros.get(i));
                    dbAdaptert_evento_establec.updateEstablecimientos(eventoEstablecimientos.get(i), idAgente, idLiquidacion);
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    long id = dbAdaptert_evento_establec.createEstablecimientos(eventoEstablecimientos.get(i), idAgente, idLiquidacion, -1);

                    Log.d("IMPORT INSERT ESTABLECIMIENTO id ", "" + id);
                }
            }


            for (int i = 0; i < comprobanteCobros.size(); i++) {
                Log.d("COBROS PENDIENTES", "" + comprobanteCobros.toString());
                Log.d("HISTORIAL COBROS PENDIENTES : " + i, " Monto a pagar : " + comprobanteCobros.get(i).getIdComprobante() + "-fecha-" + comprobanteCobros.get(i).getFechaProgramada());
                boolean existe = dbAdapter_comprob_cobro.existeComprobCobro(comprobanteCobros.get(i).getIdComprobanteCobro());
                Log.d("EXISTE ", "" + existe);
                if (existe) {
                    dbAdapter_comprob_cobro.updateComprobCobrosLiq(comprobanteCobros.get(i), idLiquidacion);
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_comprob_cobro.createComprobCobro(comprobanteCobros.get(i), idLiquidacion);
                }
            }


            for (int i = 0; i < comprobanteVentaDetalles.size(); i++) {
                Log.d("HVA HISTORIAL VENTA ANTERIOR : " + i + " - " + comprobanteVentaDetalles.get(i).getIdComprobante() + " - " + comprobanteVentaDetalles.get(i).getIdEstablecimiento(), " NOMBRE PRODUCTO : " + comprobanteVentaDetalles.get(i).getNombreProducto());

                boolean existe = dbAdapter_histo_comprob_anterior.existeComprobanteVentaAnterior(comprobanteVentaDetalles.get(i).getIdComprobante());
                Log.d("HVA ID PRODUCTO", "" + comprobanteVentaDetalles.get(i).getIdProducto());
                Log.d("EXISTE ", "" + existe);
                if (existe) {
                    dbAdapter_histo_comprob_anterior.updateHistoComprobAnterior(comprobanteVentaDetalles.get(i));
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_histo_comprob_anterior.createHistoComprobAnterior(comprobanteVentaDetalles.get(i));
                }
            }

/*            boolean rutaSuccess = isSuccesfulImport(jsonObjectRutaDistribucion);
            Log.d("RUTA SUCCESS", ""+rutaSuccess);
            if (rutaSuccess){
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




                    Log.d("IMPORT RUTA ", diaSemana + " - " + nombre + " - " + numeroEstablecimiento + " - "+ idAgente);
                    long idRutaInsertada = dbAdapter_ruta_distribucion.createRutaDistribucion(id, idRuta, nombre, diaSemana, numeroEstablecimiento, idAgente);
                    Log.d("IMPORT ID RUTA ins", ""+ idRutaInsertada);

                }


            }*/

            //ACTUALIZAR LOS CRÉDITOS DE LOS ESTABLECIMIENTOS

             /*Actualizando estado  de cobro*/
            Cursor cursorEstablecimiento = dbAdaptert_evento_establec.fetchAllEstablecs();
            int idUsuario = dbAdapter_agente.getIdUsuario();
            DbAdapter_Comprob_Cobro dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(mainActivity);
            dbAdapter_comprob_cobro.open();

            for (cursorEstablecimiento.moveToFirst(); !cursorEstablecimiento.isAfterLast(); cursorEstablecimiento.moveToNext()) {

                int idEstablecimiento = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_establec));
                JSONObject jsonObject = api.GetSolicitudAutorizacionEstablecimiento(idEstablecimiento);
                JSONObject jsonObjectAutorizacion = api.GetConsultarAutorizacion(idEstablecimiento);

                Log.d("IMPORT SOLICITUDES DE AUTORIZACIÓN ", jsonObject.toString());
                Log.d("IMPORT AUTORIZACION COBROS  ", jsonObjectAutorizacion.toString());


                if (isSuccesfulImport(jsonObjectAutorizacion)) {

                    JSONArray jsonArray = jsonObjectAutorizacion.getJSONArray("Value");
                    JSONObject jsonObj = null;
                    Log.d("IMPORT AUTORIZACION COBROS  ", jsonArray.length() + ":CANTIDAD" + isSuccesfulImport(jsonObjectAutorizacion));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObj = jsonArray.getJSONObject(i);
                        Log.d("JSON DATOS", jsonObj.toString());
                        String montocredito = jsonObj.getString("SolDOMontoCredito");
                        int idEstablecimiento1 = jsonObj.getInt("EstIEstablecimientoId");
                        int estadoSolicitud = jsonObj.getInt("SolIEstadoSolicitudId");
                        String idAutorizacionCobro = jsonObj.getString("SolObservacion");
                        String fechaLimite = jsonObj.getString("CliDTFechaLimiteCredito");
                        // String fechaLimite = "02/10/2015";
                        String SolReferenciaIdAndroid = jsonObj.getString("SolReferencia");
                        Log.d("DATOS", "" + montocredito + "--" + idEstablecimiento1 + "--" + estadoSolicitud + "--" + idAutorizacionCobro + "--" + fechaLimite + "--" + SolReferenciaIdAndroid);

                        boolean exists = dbAdapter_temp_autorizacion_cobro.existeAutorizacionCobro(SolReferenciaIdAndroid);

                        Log.d("EXISTEAC", "" + exists);
                        if (exists) {
                            int isActualizado = dbAdapter_temp_autorizacion_cobro.updateAutorizacionCobro(estadoSolicitud, idEstablecimiento1, fechaLimite, SolReferenciaIdAndroid);

                            Log.d("IMPORT REGISTRO AUTORIZACION COBRO ACTUALIZADO ", "" + isActualizado + "ESTADO SOLICITUD" + estadoSolicitud);
                            if (estadoSolicitud == 2) {
                                Log.d("GET CURSOR INDEX", "123456");
                                Cursor cr = dbAdapter_comprob_cobro.getComprobanteCobroById(SolReferenciaIdAndroid);

                                if (cr.moveToFirst()) {

                                    double montopagado = dbAdapter_temp_autorizacion_cobro.getMontoPagado(SolReferenciaIdAndroid);
                                    Log.d("MONTO PAGADO", "" + montopagado);
                                    int estado = dbAdapter_comprob_cobro.updateComprobCobrosAutorizacion(0, montopagado, SolReferenciaIdAndroid);
                                    Log.e("IDESTADO", "" + estado);
                                    if (estado > 0) {
                                        String idComprobante = dbAdapter_comprob_cobro.getIdComrobanteCobro(idEstablecimiento1 + "");
                                        int id_establec = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_establec));
                                        int id_comprob = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_comprob));
                                        int id_plan_pago = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_plan_pago));
                                        int id_plan_pago_detalle = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_plan_pago_detalle));
                                        String desc_tipo_doc = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_desc_tipo_doc));
                                        String doc = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_doc));
                                        String fecha_programada = fechaLimite;
                                        String monto_a_pagar = montocredito;
                                        String fecha_cobro = "";
                                        String hora_cobro = "";
                                        double monto_cobrado = 0.0;
                                        int estado_cobro = 1;
                                        int id_agente = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_agente));
                                        int id_forma_cobro = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_forma_cobro));
                                        String lugar_registro = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_lugar_registro));
                                        int liquidacion = idLiquidacion;
                                        long l = dbAdapter_comprob_cobro.createComprobCobros(id_establec, id_comprob, id_plan_pago, id_plan_pago_detalle, desc_tipo_doc, doc, fecha_programada, Double.parseDouble(monto_a_pagar), fecha_cobro, hora_cobro, monto_cobrado, estado_cobro, id_agente, id_forma_cobro, lugar_registro, liquidacion, idComprobante, 4);
                                        Log.d("JSON CREDITOINSERTO", "" + l);
                                        if (l > 0) {
                                            Log.d("JSON CREDITOINSERTO", "-" + id_plan_pago + "-" + id_plan_pago_detalle);
                                            JSONObject jsonObject1 = api.CreatePlanPagoDetalleExp(id_plan_pago, getDatePhone(), Double.parseDouble(montocredito), idUsuario, fecha_programada);
                                            Log.d("JSON CREDITO", "" + jsonObject1.toString());
                                        }


                                    }
                                }


                            }

                        }
                    }

                }


/*                if (isSuccesfulImport(jsonObject)) {

                    JSONArray jsonArray = jsonObject.getJSONArray("Value");
                    JSONObject jsonObj = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObj = jsonArray.getJSONObject(i);
                        int idEstablecimiento1 = jsonObj.getInt("EstIEstablecimientoId");
                        double montoCredito = jsonObj.getInt("SolDOMontoCredito");
                        int diasCredito = jsonObj.getInt("SolIVigenciaCredito");
                        Log.d("IMPORT SOLICITUDES DATOS", idEstablecimiento + " - " + montoCredito + " - " + diasCredito);
                        dbAdaptert_evento_establec.updateEstablecsCredito(idEstablecimiento, montoCredito, diasCredito);
                    }
                }*/


            }



        } catch (Exception e) {
            Log.d("AysncImport : ", e.getMessage());
            Log.d("ERROR", e.getStackTrace()[2].getMethodName() + "");
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


        if (mainActivity.isFinishing()) {
            //dismissProgressDialog();
            progressDialog.dismiss();
            return;
        } else {

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



        //TIPO GASTO
        Cursor cursor = dbAdapter_tipo_gasto.fetchAllTipoGastos();

        String from[] = {
                DbAdapter_Tipo_Gasto.TG_nom_tipo_gasto
        };




        Cursor cursor = dbAdapter_precio.fetchAllPrecio();

        String from[] = {
                DbAdapter_Precio.PR_nombreProducto
        };




        //EVENTO ESTABLECIMIENTO

        Cursor cursor = dbAdaptert_evento_establec.fetchAllEstablecs();

        String from[] = {
                DbAdaptert_Evento_Establec.EE_nom_establec
        };

        /*Cursor cursor = dbAdapter_comprob_cobro.fetchAllComprobCobros();

        String from[] = {
                DbAdapter_Comprob_Cobro.CC_fecha_programada
        };




        Cursor cursor = dbAdapter_histo_venta_detalle.fetchAllHistoVentaDetalle();

        String from[] = {
                DbAdapter_Histo_Venta_Detalle.HD_id_detalle
        };



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
*/
        //listView.setAdapter(simpleCursorAdapter);

        Intent intent = new Intent(mainActivity, mainActivity.getClass());
        mainActivity.finish();
        mainActivity.startActivity(intent);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(Integer.parseInt(values[0]));
    }

    public void createProgressDialog() {
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setMessage("Importando ...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public String getErrorMessage(JSONObject jsonObj) {
        String errorMessage = "Error Message null";
        try {
            errorMessage += jsonObj.getString("ErrorMessage");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parser Error Message", e.getMessage());
        }
        return errorMessage;
    }

    public boolean isSuccesfulImport(JSONObject jsonObj) {
        boolean succesful = false;
        try {
            Log.d("CADEMA A ÁRSEAR BOOLEAN ", jsonObj.toString());
            succesful = jsonObj.getBoolean("Successful");
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
