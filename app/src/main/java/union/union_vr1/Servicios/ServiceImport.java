package union.union_vr1.Servicios;

import android.app.Activity;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.JSONParser.ParserComprobanteCobro;
import union.union_vr1.JSONParser.ParserComprobanteVentaDetalle;
import union.union_vr1.JSONParser.ParserEventoEstablecimiento;
import union.union_vr1.JSONParser.ParserHistorialVentaDetalles;
import union.union_vr1.JSONParser.ParserPrecio;
import union.union_vr1.JSONParser.ParserStockAgente;
import union.union_vr1.JSONParser.ParserTipoGasto;
import union.union_vr1.Objects.ComprobanteCobro;
import union.union_vr1.Objects.ComprobanteVentaDetalle;
import union.union_vr1.Objects.EventoEstablecimiento;
import union.union_vr1.Objects.HistorialVentaDetalles;
import union.union_vr1.Objects.Precio;
import union.union_vr1.Objects.StockAgente;
import union.union_vr1.Objects.TipoGasto;
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
import union.union_vr1.Utils.DisplayToast;
import union.union_vr1.Utils.Utils;

/**
 * SERVICIO PARA IMPORTARS
 * */

public class ServiceImport extends IntentService {


    Handler mHandler;
    private DbAdapter_Temp_Session session;
    private Context context;

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

    private int idAgente;
    private int idLiquidacion;
    StockAgenteRestApi api = null;

    private static int _MAX = 10;


    private static String TAG  = ServiceImport.class.getSimpleName();

    public ServiceImport() {
        super("ServiceImport");
        mHandler = new Handler();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = ServiceImport.this;
        inicializarVariables(context);
    }

    private void inicializarVariables(Context context){

        api = new StockAgenteRestApi(context);
        session = new DbAdapter_Temp_Session(context);
        session.open();

        dbAdapter_agente = new DbAdapter_Agente(context);
        dbAdapter_agente.open();
        dbAdapter_stock_agente = new DbAdapter_Stock_Agente(context);
        dbAdapter_stock_agente.open();
        dbAdapter_precio = new DbAdapter_Precio(context);
        dbAdapter_precio.open();
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(context);
        dbAdaptert_evento_establec.open();
        dbAdapter_tipo_gasto = new DbAdapter_Tipo_Gasto(context);
        dbAdapter_tipo_gasto.open();
        dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(context);
        dbAdapter_comprob_cobro.open();
        dbAdapter_histo_venta_detalle = new DbAdapter_Histo_Venta_Detalle(context);
        dbAdapter_histo_venta_detalle.open();
        dbAdapter_histo_comprob_anterior = new DbAdapter_Histo_Comprob_Anterior(context);
        dbAdapter_histo_comprob_anterior.open();
        dbAdapter_temp_autorizacion_cobro = new DBAdapter_Temp_Autorizacion_Cobro(context);
        dbAdapter_temp_autorizacion_cobro.open();
        dbAdapter_ruta_distribucion = new DbAdapter_Ruta_Distribucion(context);
        dbAdapter_ruta_distribucion.open();

        idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);
        Log.d(TAG, "DATOS SESSION, AGENTE: " + idAgente + " LIQUIDACION: " + idLiquidacion);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.ACTION_IMPORT_SERVICE.equals(action)) {
                importar();
                //SHOW TOAST
                mHandler.post(new DisplayToast(this, "IMPORTACIÓN FINALIZADA."));
            }

        }
    }

    /**
     * Handle action importacion
     */
    private void importar() {
        // TODO : IMPORTACIÓN HERE!

        // Se construye la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_save)
                .setContentTitle("Importando")
                .setContentText("Procesando...");

        Cursor cursor = dbAdapter_agente.fetchAgentesByIds(idAgente, idLiquidacion);
        cursor.moveToFirst();

        ArrayList<StockAgente> stockAgentes = null;
        ArrayList<TipoGasto> tipoGastos = null;
        ArrayList<Precio> precios = null;
        ArrayList<EventoEstablecimiento> eventoEstablecimientos = null;
        ArrayList<ComprobanteCobro> comprobanteCobros = null;
        ArrayList<HistorialVentaDetalles> historialVentaDetalleses = null;
        ArrayList<ComprobanteVentaDetalle> comprobanteVentaDetalles = null;

        String fecha = Utils.getDatePhone();

        Log.d(TAG, "IMPORT DATOS REALES idAgente, idLiquidacion, Fecha"+ idAgente + ", " + idLiquidacion + "," + fecha);

        Log.d(TAG, "IMPORT DATOS DE PRUEBA ESTABLECIMIENTO X RUTA idAgente, idLiquidacion, Fecha"+ idAgente + ", " + idLiquidacion + "," + fecha);

        //INICIALIZAR EL PROGRESS DE LA NOTIFICACIÓN EN 0
        builder.setProgress(_MAX, 0, false);
        startForeground(1, builder.build());

        try{




            JSONObject jsonObjectStockAgente = api.GetStockAgente(idAgente);
            //Log.d("JSON ERROR MESSAGE", getErrorMessage(jsonObjectStockAgente));
            builder.setProgress(_MAX, 2, false);
            startForeground(1, builder.build());
            JSONObject jsonObjectTipoGasto = api.GetTipoGasto();

            JSONObject jsonObjectPrecio = api.GetPrecioCategoria(idLiquidacion, idAgente);

            JSONObject jsonObjectEventoEstablecimiento = api.GetEstablecimeintoXRuta(idLiquidacion, fecha, idAgente);

            JSONObject jsonObjectComprobanteCobro = api.GetHistorialCobrosPendientes();

            builder.setProgress(_MAX, 3, false);
            startForeground(1, builder.build());
            JSONObject jsonObjectHistorialVentaDetalle = api.GetHistorialVentaDetalle(idAgente);

            JSONObject jsonObjectHistorialComprobanteAnterior = api.GetComprobanteVentaDetalle_Env();
            builder.setProgress(_MAX, 4, false);
            startForeground(1, builder.build());
            JSONObject jsonObjectRutaDistribucion = api.GetConsultarPlan_Distribucion(idAgente);


            Log.d(TAG,"JSON OBJECT STOCK AGENTE : "+ jsonObjectStockAgente.toString());
            Log.d(TAG,"JSON OBJECT PRECIO : "+ jsonObjectPrecio.toString());
            Log.d(TAG,"JSON OBJECT TIPO GASTO : "+ jsonObjectTipoGasto.toString());
            Log.d(TAG,"JSON OBJECT EVENTO ESTABLECIMIENTO : "+ jsonObjectEventoEstablecimiento.toString());
            Log.d(TAG,"JSON OBJECT COMPROBANTE COBRO : "+ jsonObjectComprobanteCobro.toString());
            Log.d(TAG,"JSON OBJECT HISTORIAL VENTA DETALLE : "+ jsonObjectHistorialVentaDetalle.toString());
            Log.d(TAG,"JSON OBJECT HISTORIAL VENTA ANTERIOR "+ jsonObjectHistorialComprobanteAnterior.toString());
            Log.d(TAG,"JSON OBJECT RUTA DISTRIBUCION "+ jsonObjectRutaDistribucion.toString());


            ParserStockAgente parserStockAgente = new ParserStockAgente();
            ParserTipoGasto parserTipoGasto = new ParserTipoGasto();
            ParserPrecio parserPrecio = new ParserPrecio(idAgente);
            ParserEventoEstablecimiento parserEventoEstablecimiento = new ParserEventoEstablecimiento();
            ParserComprobanteCobro parserComprobanteCobro = new ParserComprobanteCobro();
            ParserHistorialVentaDetalles parserHistorialVentaDetalles = new ParserHistorialVentaDetalles();
            ParserComprobanteVentaDetalle parserComprobanteVentaDetalle = new ParserComprobanteVentaDetalle();

            stockAgentes = parserStockAgente.parserStockAgente(jsonObjectStockAgente);
            tipoGastos = parserTipoGasto.parserTipoGasto(jsonObjectTipoGasto);
            precios = parserPrecio.parserPrecio(jsonObjectPrecio);
            eventoEstablecimientos = parserEventoEstablecimiento.parserEventoEstablecimiento(jsonObjectEventoEstablecimiento);
            comprobanteCobros = parserComprobanteCobro.parserComprobanteCobro(jsonObjectComprobanteCobro);
            historialVentaDetalleses = parserHistorialVentaDetalles.parserHistorialVentaDetalles(jsonObjectHistorialVentaDetalle);
            comprobanteVentaDetalles = parserComprobanteVentaDetalle.parserComprobanteVentaDetalle(jsonObjectHistorialComprobanteAnterior);


            for(int i =0;i<historialVentaDetalleses.size();i++){
                boolean existe = dbAdapter_histo_venta_detalle.existeHistoVentaDetalle(historialVentaDetalleses.get(i).getIdDetalle());

                Log.d(TAG, "EXISTE DETALLE : " + existe);
                if (existe) {
                    dbAdapter_histo_venta_detalle.updateHistoVentaDetalle(historialVentaDetalleses.get(i));
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_histo_venta_detalle.createHistoVentaDetalle(historialVentaDetalleses.get(i));
                }
            }

            boolean rutaSuccess = Utils.isSuccesfulImport(jsonObjectRutaDistribucion);
            Log.d(TAG, "RUTA SUCCESS : " + rutaSuccess);
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
                    Log.d(TAG, "IMPORT RUTA : "+ diaSemana + " - " + nombre + " - " + numeroEstablecimiento + " - " + idAgente);
                    long idRutaInsertada = dbAdapter_ruta_distribucion.createRutaDistribucion(id, idRuta, nombre, diaSemana, numeroEstablecimiento, idAgente);
                    Log.d(TAG, "IMPORT ID RUTA INS : " + idRutaInsertada);
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

            builder.setProgress(_MAX, 5, false);
            startForeground(1, builder.build());
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

            for (int i = 0; i < precios.size(); i++) {
                Log.d("PRECIO CATEGORÌA : " + i, "Nombre producto : " + precios.get(i).getNombreProducto());

                boolean existe = dbAdapter_precio.existePrecio(precios.get(i).getIdProducto(), precios.get(i).getIdCategoriaEstablecimiento(), precios.get(i).getValorUnidad());
                Log.d("EXISTE ", "" + existe);
                if (existe) {
                    dbAdapter_precio.updatePrecios(precios.get(i), idAgente);
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_precio.createPrecios(precios.get(i), idAgente);
                }
            }


            for (int i = 0; i < eventoEstablecimientos.size(); i++) {
                Log.d(TAG, "ESTABLECIMIENTOS X RUTAS: " + i+ " Nombre Establecimiento : " + eventoEstablecimientos.get(i).getNombreEstablecimiento() + ", orden : " + eventoEstablecimientos.get(i).getOrden() + ", BARCODE: " + eventoEstablecimientos.get(i).getCodigoBarras());
                boolean existe = dbAdaptert_evento_establec.existeEstablecsById(eventoEstablecimientos.get(i).getIdEstablecimiento());

                Log.d("EXISTE ESTABLECIMIENTO", "" + existe);
                if (existe) {
                    //dbAdapter_comprob_cobro.updateComprobCobros(comprobanteCobros.get(i));
                    dbAdaptert_evento_establec.updateEstablecimientos(eventoEstablecimientos.get(i), idAgente, idLiquidacion);
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    long id = dbAdaptert_evento_establec.createEstablecimientos(eventoEstablecimientos.get(i), idAgente, idLiquidacion);

                    Log.d(TAG, "IMPORT INSERT ESTABLECIMIENTO id : " + id);
                }
            }

            builder.setProgress(_MAX, 6, false);
            startForeground(1, builder.build());
            for (int i = 0; i < comprobanteCobros.size(); i++) {
                Log.d("COBROS PENDIENTES", "" + comprobanteCobros.toString());
                Log.d(TAG, "HISTORIAL COBROS PENDIENTES : " + i+ " Monto a pagar : " + comprobanteCobros.get(i).getIdComprobante() + "-fecha-" + comprobanteCobros.get(i).getFechaProgramada());
                boolean existe = dbAdapter_comprob_cobro.existeComprobCobro(comprobanteCobros.get(i).getIdComprobanteCobro());
                Log.d("EXISTE ", "" + existe);
                if (existe) {
                    dbAdapter_comprob_cobro.updateComprobCobros(comprobanteCobros.get(i));
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_comprob_cobro.createComprobCobro(comprobanteCobros.get(i));
                }
            }

            builder.setProgress(_MAX, 9, false);
            startForeground(1, builder.build());
            for (int i = 0; i < comprobanteVentaDetalles.size(); i++) {
                Log.d(TAG, "HVA HISTORIAL VENTA ANTERIOR : " + i + " - " + comprobanteVentaDetalles.get(i).getIdComprobante() + " - " + comprobanteVentaDetalles.get(i).getIdEstablecimiento()+ " NOMBRE PRODUCTO : " + comprobanteVentaDetalles.get(i).getNombreProducto());

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


            //ACTUALIZAR LOS CRÉDITOS DE LOS ESTABLECIMIENTOS

            Cursor cursorEstablecimiento = dbAdaptert_evento_establec.fetchAllEstablecs();

            for (cursorEstablecimiento.moveToFirst(); !cursorEstablecimiento.isAfterLast(); cursorEstablecimiento.moveToNext()) {

                int idEstablecimiento = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_establec));
                JSONObject jsonObject = api.GetSolicitudAutorizacionEstablecimiento(idEstablecimiento);
                JSONObject jsonObjectAutorizacion = api.GetConsultarAutorizacion(idEstablecimiento);

                Log.d(TAG, "IMPORT SOLICITUDES DE AUTORIZACIÓN :"+ jsonObject.toString());
                Log.d(TAG, "IMPORT AUTORIZACION COBROS  : "+ jsonObjectAutorizacion.toString());

                if (Utils.isSuccesfulImport(jsonObject)) {

                    JSONArray jsonArray = jsonObject.getJSONArray("Value");
                    JSONObject jsonObj = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObj = jsonArray.getJSONObject(i);
                        int idEstablecimiento1 = jsonObj.getInt("EstIEstablecimientoId");
                        int montoCredito = jsonObj.getInt("SolDOMontoCredito");
                        int diasCredito = jsonObj.getInt("SolIVigenciaCredito");
                        Log.d(TAG, "IMPORT SOLICITUDES DATOS"+ idEstablecimiento + " - " + montoCredito + " - " + diasCredito);
                        dbAdaptert_evento_establec.updateEstablecsCredito(idEstablecimiento, (double) montoCredito, diasCredito);
                    }
                }


            }


            builder.setProgress(_MAX, _MAX, false);
            startForeground(1, builder.build());




        }catch (Exception e) {
            Log.d(TAG, " SERVICE IMPORT : " +  e.getMessage());
            Log.d(TAG, "ERROR : " +e.getStackTrace()[2].getMethodName()+"");
        }



        // Quitar de primer plano
        stopForeground(true);
    }
}
