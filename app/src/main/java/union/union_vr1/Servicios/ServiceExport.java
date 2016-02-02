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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import union.union_vr1.AsyncTask.ExportCanjesDevolucionesLater;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DBAdapter_Temp_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Cobros_Manuales;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Establecimeinto_Historial;
import union.union_vr1.Sqlite.DbAdapter_Exportacion_Comprobantes;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Impresion_Cobros;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.DisplayToast;
import union.union_vr1.Utils.Utils;

/**
 * SERVICE DE EXOPORTACIÓN
 */
public class ServiceExport extends IntentService {


    //VARIABLES
    StockAgenteRestApi api = null;
    private Boolean existeCobrosManuales = false;
    private Boolean existeCobrosNormales = false;
    private DbAdapter_Temp_Session session;
    private int idLiquidacion;
    private int idUsuario;
    private int idAgente;
    private Context context;
    private DbAdapter_Impresion_Cobros dbAdapter_impresion_cobros;

    //DEFINO LAS VARIABLES DE EXPORTACIÓN [TABLAS]
    private DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
    private DbAdapter_Comprob_Venta dbAdapter_comprob_venta;
    private DbAdapter_Comprob_Venta_Detalle dbAdapter_comprob_venta_detalle;
    private DbAdapter_Histo_Venta dbAdapter_histo_venta;
    private DbAdapter_Comprob_Cobro dbAdapter_comprob_cobro;
    private DbAdapter_Histo_Venta_Detalle dbAdapter_histo_venta_detalle;
    //private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DBAdapter_Temp_Autorizacion_Cobro dbAdapter_temp_autorizacion_cobro;
    private DBAdapter_Temp_Canjes_Devoluciones dbAdapter_temp_canjes_devoluciones;
    private DbAdapter_Cobros_Manuales dbAdapter_cobros_manuales;
    private DbAdapter_Exportacion_Comprobantes dbAdapter_exportacion_comprobantes;
    //----------------------------


    private JSONObject jsonObjectCreated = null;
    private StockAgenteRestApi stockAgenteRestApi = null;
    private DbAdapter_Temp_Establecimiento dbAdapter_temp_establecimiento;
    private DbAdapter_Temp_Session dbAdapter_temp_session;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DbAdapter_Establecimeinto_Historial dbAdapter_establecimeinto_historial;

    private static String TAG = ServiceExport.class.getSimpleName();

    Handler mHandler;
    private static int _MAX = 100;

    //EXPORTACIÓN FLEX
    List<String> listIdExportacionFlex = new ArrayList<>();


    public ServiceExport() {
        super("ServiceExport");
        mHandler = new Handler();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = ServiceExport.this;
        inicializarVariables(context);
    }

    private void inicializarVariables(Context context) {


        api = new StockAgenteRestApi(context);
        dbAdapter_temp_canjes_devoluciones = new DBAdapter_Temp_Canjes_Devoluciones(context);
        dbAdapter_temp_canjes_devoluciones.open();
        session = new DbAdapter_Temp_Session(context);
        session.open();
        dbAdapter_impresion_cobros = new DbAdapter_Impresion_Cobros(context);
        dbAdapter_impresion_cobros.open();

        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(context);
        dbAdapter_comprob_venta = new DbAdapter_Comprob_Venta(context);
        dbAdapter_comprob_venta_detalle = new DbAdapter_Comprob_Venta_Detalle(context);
        dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(context);
        dbAdapter_histo_venta_detalle = new DbAdapter_Histo_Venta_Detalle(context);
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(context);
        dbAdapter_histo_venta = new DbAdapter_Histo_Venta(context);
        dbAdapter_temp_autorizacion_cobro = new DBAdapter_Temp_Autorizacion_Cobro(context);
        dbAdapter_cobros_manuales = new DbAdapter_Cobros_Manuales(context);
        dbAdapter_exportacion_comprobantes = new DbAdapter_Exportacion_Comprobantes(context);

        //ABRO LA CONEXIÓN A LA DB
        dbAdapter_informe_gastos.open();
        dbAdapter_comprob_venta.open();
        dbAdapter_comprob_venta_detalle.open();
        dbAdapter_histo_venta_detalle.open();
        dbAdapter_comprob_cobro.open();
        dbAdapter_histo_venta_detalle.open();
        dbAdaptert_evento_establec.open();
        dbAdapter_histo_venta.open();
        dbAdapter_temp_autorizacion_cobro.open();
        dbAdapter_cobros_manuales.open();
        dbAdapter_exportacion_comprobantes.open();


        //
        dbAdapter_temp_establecimiento = new DbAdapter_Temp_Establecimiento(context);
        dbAdapter_temp_establecimiento.open();
        dbAdapter_establecimeinto_historial = new DbAdapter_Establecimeinto_Historial(context);
        dbAdapter_establecimeinto_historial.open();
        dbAdapter_temp_session = new DbAdapter_Temp_Session(context);
        dbAdapter_temp_session.open();
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(context);
        dbAdaptert_evento_establec.open();
        int ruta = dbAdapter_temp_session.fetchVarible(777);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.ACTION_EXPORT_SERVICE.equals(action)) {
                exportar();
                //SHOW TOAST
                mHandler.post(new DisplayToast(this, "EXPORTACIÓN FINALIZADA."));
            }
        }
    }

    private void exportar() {
        //FILTRO LOS REGISTROS DE LAS TABLAS A EXPORTAR

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_upload)
                .setContentTitle("Exportando")
                .setContentText("Procesando...");


        //INICIALIZAR EL PROGRESS DE LA NOTIFICACIÓN EN 0
        builder.setProgress(_MAX, 0, false);
        startForeground(1, builder.build());


        Cursor cursorInformeGastos = dbAdapter_informe_gastos.filterExport();
        Cursor cursorComprobanteVenta = dbAdapter_comprob_venta.filterExport();

        builder.setProgress(_MAX, 10, false);
        startForeground(1, builder.build());

        //Cursor cursorComprobanteVentaDetalle = dbAdapter_comprob_venta_detalle.filterExport();
       // Cursor cursorComprobanteCobro = dbAdapter_comprob_cobro.filterExport();
        Cursor cursorInsertarCaja = dbAdapter_comprob_cobro.filterExportUpdatedAndEstadoCobro();
        Cursor cursorInsertarCajaParcial = dbAdapter_comprob_cobro.filterExportUpdatedAndEstadoCobroParcial();
        Cursor cursorEventoEstablecimiento = dbAdaptert_evento_establec.filterExportUpdated();




        //

        //Cobros




        //Cobros.......



        //Log.d("COBROSNORMALES",""+crCobrosInsrCaja.getCount());
        Log.d("COBROSNORMALES",""+cursorComprobanteVenta.getCount());

        builder.setProgress(_MAX, 15, false);
        startForeground(1, builder.build());

        //HISTORIAL DE VENTA
        Cursor cursorHistoVentaCreated = dbAdapter_histo_venta.filterExport();

        Cursor cursorHistoVentaDetalleCreated = dbAdapter_histo_venta_detalle.filterExport(idLiquidacion);


        builder.setProgress(_MAX, 20, false);
        startForeground(1, builder.build());


        Cursor cursorAutorizacionCobro = dbAdapter_temp_autorizacion_cobro.filterExport();

        Cursor cursorCobrosManuales = dbAdapter_cobros_manuales.filterExport();

        idAgente = session.fetchVarible(1);
        idUsuario = session.fetchVarible(4);
        idLiquidacion = session.fetchVarible(3);

        Log.d("EXPORT", "INICIANDO EXPORTACIÓN ..." + TAG);

        builder.setProgress(_MAX, 30, false);
        startForeground(1, builder.build());

        List<String> listIdInfomeGastos = new ArrayList<String>();
        List<String> listIdComprobantes = new ArrayList<String>();
        List<String> listIdComprobanteVentaDetalle = new ArrayList<String>();
        List<String> listIdComprobanteCobro = new ArrayList<String>();
        List<String> listIdEstablecimientoUpdated = new ArrayList<String>();
        List<String> listidInsertarCaja = new ArrayList<String>();
        List<String> listIdAutorizacionCobro = new ArrayList<String>();


        List<String> listIdHVCreated = new ArrayList<String>();
        List<String> listIdHVUpdated = new ArrayList<String>();
        List<String> listIdHVDCreated = new ArrayList<String>();
        List<String> listIdHVDUpdated = new ArrayList<String>();

        builder.setProgress(_MAX, 40, false);
        startForeground(1, builder.build());

        //EXPORTAR TODOS LOS REGISTROS CREADOS EN ANDROID [GUARDADOS EN SQLITE]

        //COMPROBANTE DE VENTA
        if (cursorComprobanteVenta.getCount() > 0) {
            for (cursorComprobanteVenta.moveToFirst(); !cursorComprobanteVenta.isAfterLast(); cursorComprobanteVenta.moveToNext()) {
                JSONObject jsonObjectSuccesfull = null;
                int idComprobanteVentaRetornado = -1;
                long _id_comp_venta = -1;
                _id_comp_venta = cursorComprobanteVenta.getLong(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob));
                Log.d("Datos Export CV",
                        cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)) + " - " +
                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_serie)) + " - " +
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_num_doc)) + " - " +
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_forma_pago)) + " - " +
                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_fecha_doc)) + " - " +
                                cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_base_imp)) + " - " +
                                cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_igv)) + " - " +
                                cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_total)) + " - " +
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_tipo_doc)) + " - " +
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_agente)) + " - " +
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_estado_comp)) + " - " +
                                0 + " - " +
                                idLiquidacion + " - " +
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_tipo_venta)) + " - " +
                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_codigo_erp)) + " - " +
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_establec)));
                try {
                    if (cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_forma_pago)) == 1) {

                        jsonObjectSuccesfull = api.CreateComprobanteVenta(

                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_serie)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_num_doc)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_forma_pago)),
                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_fecha_doc)),
                                cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_base_imp)),
                                cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_igv)),
                                cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_total)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_tipo_doc)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_agente)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_estado_comp)),
                                0,
                                idLiquidacion,
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_tipo_venta)),
                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_codigo_erp)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_establec)),
                                idUsuario
                        );

                        if (Utils.isSuccesful(jsonObjectSuccesfull)) {
                            idComprobanteVentaRetornado = getIdComprobanteVentaRetornado(jsonObjectSuccesfull);
                            Log.d("ID COMP V RETURN", "" + idComprobanteVentaRetornado);

                            int registrosActualizados = dbAdapter_comprob_venta_detalle.updateComprobVentaDetalleReal(_id_comp_venta, idComprobanteVentaRetornado);

                            Log.d(TAG, "DETALLES ACTUALIZADOS : " + registrosActualizados);
                            long _id_mapeo = -1;
                            _id_mapeo = dbAdapter_exportacion_comprobantes.createRegistroExportacion(_id_comp_venta, idComprobanteVentaRetornado, Constants._CREADO);

                            Log.d(TAG, "_ID_MAPEO EXPORTACION COMPROBANTS : " + _id_mapeo);
                            Log.d("CV UPDATE", "" + registrosActualizados);
                        }
                    } else if (cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_forma_pago)) == 2) {

                        jsonObjectSuccesfull = api.CreateComprobanteVenta(

                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_serie)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_num_doc)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_forma_pago)),
                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_fecha_doc)),
                                cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_base_imp)),
                                cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_igv)),
                                cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_total)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_tipo_doc)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_agente)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_estado_comp)),
                                cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_total)),
                                idLiquidacion,
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_tipo_venta)),
                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_codigo_erp)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_establec)),
                                idUsuario
                        );
                        Log.d("COBROSNORMALESVENTA", "" + jsonObjectSuccesfull.toString());
                        if (Utils.isSuccesful(jsonObjectSuccesfull)) {

                            idComprobanteVentaRetornado = getIdComprobanteVentaRetornado(jsonObjectSuccesfull);
                            Log.d("COBROSNORMALESVENTAID", "" + idComprobanteVentaRetornado);

                            int registrosActualizados = dbAdapter_comprob_venta_detalle.updateComprobVentaDetalleReal(cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)), idComprobanteVentaRetornado);
                            Log.d("CV REGISTROS UP", "" + registrosActualizados);
                            //EXPORTO Y SI ES CV AL CRÉDITO ACTUALIZO EL ID CREADO POR ANDROID AL ID CREADO EN SQL SERVER
                            int registrosUpdtCV = dbAdapter_comprob_venta.updateComprobanteIDReal(cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)), idComprobanteVentaRetornado);

                            /**
                             * creo un registro para mapear los _id entre los sistemas
                             * */
                            long _id_mapeo = dbAdapter_exportacion_comprobantes.createRegistroExportacion(cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)), idComprobanteVentaRetornado, Constants._CREADO);

                            Log.d(TAG, "_ID_MAPEO EXPORTACION COMPROBANTS : " + _id_mapeo);


                            Log.d("n# IDS cv -><-", "" + cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)) + "to" + idComprobanteVentaRetornado + "#s UPD-->" + registrosUpdtCV);


                            int idPlan = getidPlanPagoRetornado(jsonObjectSuccesfull);
                            Log.d("COBROSNORMALESVENTAIPLAN", "" + idPlan);

                            Log.d("Export id CV IGUALES", "" + cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)));
                            Cursor cursorPlanPago = dbAdapter_comprob_cobro.filterExportAndFetchById(cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)), idPlan);
                            Log.d("IDIDCOMPROBANTE", "" + cursorPlanPago.getCount());
                            Log.d("IDIDCOMPROBANTE", "" + cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)));
                            for (cursorPlanPago.moveToFirst(); !cursorPlanPago.isAfterLast(); cursorPlanPago.moveToNext()) {

                                Log.d("EXPORT PPD", "" + idPlan + "-" +
                                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)) + "-" +
                                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_fecha_doc)) + "-" +
                                                cursorPlanPago.getDouble(cursorPlanPago.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_monto_a_pagar)) + "-" +
                                                idUsuario + "-" +
                                                cursorPlanPago.getString(cursorPlanPago.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_fecha_programada)) + "-" +
                                                cursorPlanPago.getString(cursorPlanPago.getColumnIndexOrThrow(Constants._SINCRONIZAR))
                                );
                                JSONObject jsonObjectSuccess = api.CreatePlanPagoDetalleExp(
                                        idPlan,
                                        cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_fecha_doc)),
                                        cursorPlanPago.getDouble(cursorPlanPago.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_monto_a_pagar)),
                                        idUsuario,
                                        cursorPlanPago.getString(cursorPlanPago.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_fecha_programada))
                                );
                                Log.d("COBROSNORMALESIDDETALLE",jsonObjectSuccess.toString() );
                                if (Utils.isSuccesful(jsonObjectSuccesfull)) {
                                    listIdComprobanteCobro.add("" + cursorPlanPago.getInt(cursorPlanPago.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_cob_historial)));
                                    //Update IdPlanPagoDetalle

                                }
                                if (Utils.isSuccesful(jsonObjectSuccess)) {
                                    int estado = dbAdapter_comprob_cobro.updatePlanPagoDetalleCobros(cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)), jsonObjectSuccess.getInt("Value"));
                                    if (estado > 0) {
                                        Log.d("COBROSNORMALESPLANES", ""+idPlan+"-"+ jsonObjectSuccess.getInt("Value"));
                                        int insComprovanteVenta = dbAdapter_comprob_cobro.updateIdComprobanteVenta(cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)),idComprobanteVentaRetornado);
                                        int actualizopIp = dbAdapter_impresion_cobros.updateCobrosIdPlanDetalle(cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)), idPlan, jsonObjectSuccess.getInt("Value"));
                                        int estadoActualizo = dbAdapter_impresion_cobros.updateCobrosIdComprobanteVenta(cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)),idComprobanteVentaRetornado);

                                        Log.d("COBROSNORMALES", ""+actualizopIp+"-"+estadoActualizo+"-"+insComprovanteVenta);
                                    }

                                }
                                Log.d("EXPORT JSON PPD S", jsonObjectSuccess.toString());
                            }

                            String[] idComprobanteCobro = new String[listIdComprobanteCobro.size()];
                            listIdComprobanteCobro.toArray(idComprobanteCobro);

                            for (int i = 0; i < idComprobanteCobro.length; i++) {
                                Log.d("ID EXPORTADOS ", "" + idComprobanteCobro[i]);
                            }

                            if (listIdComprobantes.size() > 0) {
                                dbAdapter_comprob_cobro.changeEstadoToExport(idComprobanteCobro, Constants._EXPORTADO);
                            }
                        }

                    }


                    Log.d("JSON INT RETURN ", jsonObjectSuccesfull.toString());

                    Log.d("SUCCESFULL EXPORT CV", "" + Utils.isSuccesful(jsonObjectSuccesfull));
                    Log.d("ID CV RETORNADO", "" + idComprobanteVentaRetornado);
                    if (Utils.isSuccesful(jsonObjectSuccesfull)) {
                        listIdComprobantes.add("" + cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)));
                    }
                    Log.d(" EXPORT JSON MESSAGE CV", jsonObjectSuccesfull.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String[] idComprobantes = new String[listIdComprobantes.size()];
            listIdComprobantes.toArray(idComprobantes);

            for (int i = 0; i < idComprobantes.length; i++) {
                Log.d("ID EXPORTADOS ", "" + idComprobantes[i]);
            }

            if (listIdComprobantes.size() > 0) {
                dbAdapter_comprob_venta.changeEstadoToExport(idComprobantes, Constants._EXPORTADO);
            }

        } else {
            Log.d("EXPORT ", "TODOS LOS COMPROBANTES DE VENTAS ESTÁN EXPORTADOS");
        }

        builder.setProgress(_MAX, 55, false);
        startForeground(1, builder.build());

        Cursor cursorComprobanteVentaDetalle = dbAdapter_comprob_venta_detalle.filterExport();

        if (cursorComprobanteVentaDetalle.getCount() > 0) {
            for (cursorComprobanteVentaDetalle.moveToFirst(); !cursorComprobanteVentaDetalle.isAfterLast(); cursorComprobanteVentaDetalle.moveToNext()) {
                JSONObject jsonObjectSuccesfull = null;

                try {

                    Log.d("VALOR UNIDAD ", "" + cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_valor_unidad)));

                    Log.d("DATOS EXPORT CVD ", "" + cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_id_comprob_real)) + "-" +
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_id_producto)) + "-" +
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_cantidad)) + "-" +
                            cursorComprobanteVentaDetalle.getDouble(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_importe)) + "-" +
                            idUsuario + "-" +
                            cursorComprobanteVentaDetalle.getDouble(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_costo_venta)) + "-" +
                            cursorComprobanteVentaDetalle.getDouble(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_precio_unit)) + "-" +
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_valor_unidad)));

                    jsonObjectSuccesfull = api.CreateComprobanteVentaDetalle(
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_id_comprob_real)),
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_id_producto)),
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_cantidad)),
                            cursorComprobanteVentaDetalle.getDouble(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_importe)),
                            idUsuario,
                            cursorComprobanteVentaDetalle.getDouble(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_costo_venta)),
                            cursorComprobanteVentaDetalle.getDouble(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_precio_unit)),
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_valor_unidad))
                    );

                    Log.d("SUCCESFULL EXPORT CVD", "" + Utils.isSuccesful(jsonObjectSuccesfull));
                    Log.d("EXPORT CVD MESSAGE ", "" + jsonObjectSuccesfull.toString());

                    if (Utils.isSuccesful(jsonObjectSuccesfull)) {
                        listIdComprobanteVentaDetalle.add("" + cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_comp_detalle)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String[] idComprobantesDetalle = new String[listIdComprobanteVentaDetalle.size()];
            listIdComprobanteVentaDetalle.toArray(idComprobantesDetalle);

            for (int i = 0; i < idComprobantesDetalle.length; i++) {
                Log.d("ID EXPORTADOS ", "" + idComprobantesDetalle[i]);
            }

            if (listIdComprobanteVentaDetalle.size() > 0) {
                dbAdapter_comprob_venta_detalle.changeEstadoToExport(idComprobantesDetalle, Constants._EXPORTADO);
            }
        } else {

            Log.d("EXPORT ", "TODOS LOS COMPROBANTES DE VENTA DETALLE ESTÁN EXPORTADOS");
        }


        builder.setProgress(_MAX, 60, false);
        startForeground(1, builder.build());

        if (cursorInformeGastos.getCount() > 0) {
            for (cursorInformeGastos.moveToFirst(); !cursorInformeGastos.isAfterLast(); cursorInformeGastos.moveToNext()) {
                JSONObject jsonObjectSuccesfull = null;

                try {

                    Log.d("DATOS EXPORT GASTOS", "" + idLiquidacion + "-" +
                            cursorInformeGastos.getDouble(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_total)) + "-" +
                            cursorInformeGastos.getString(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_fecha)) + "-" +
                            idUsuario + "-" +
                            cursorInformeGastos.getInt(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_id_tipo_gasto)) + "-" +
                            cursorInformeGastos.getDouble(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_subtotal)) + "-" +
                            cursorInformeGastos.getInt(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_igv)) + "-" +
                            cursorInformeGastos.getString(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_referencia)) + "-" +
                            cursorInformeGastos.getInt(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_id_proced_gasto)));

                    jsonObjectSuccesfull = api.CreateInformeGastos(

                            idLiquidacion,
                            cursorInformeGastos.getDouble(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_total)),
                            cursorInformeGastos.getString(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_fecha)),
                            idUsuario,
                            cursorInformeGastos.getInt(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_id_tipo_gasto)),
                            cursorInformeGastos.getDouble(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_subtotal)),
                            cursorInformeGastos.getDouble(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_igv)),
                            cursorInformeGastos.getString(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_referencia)),
                            cursorInformeGastos.getInt(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_id_proced_gasto))
                    );

                    Log.d("SUCCES EXPORT GASTOS ", "" + Utils.isSuccesful(jsonObjectSuccesfull));
                    Log.d("SUCCES EXPORT GASTOS", "" + jsonObjectSuccesfull.toString());

                    if (Utils.isSuccesful(jsonObjectSuccesfull)) {
                        listIdInfomeGastos.add("" + cursorInformeGastos.getInt(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_id_gasto)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String[] idInformeGastos = new String[listIdInfomeGastos.size()];
            listIdInfomeGastos.toArray(idInformeGastos);

            for (int i = 0; i < idInformeGastos.length; i++) {
                Log.d("ID EXPORTADOS ", "" + idInformeGastos[i]);
            }

            if (listIdInfomeGastos.size() > 0) {
                dbAdapter_informe_gastos.changeEstadoToExport(idInformeGastos, Constants._EXPORTADO);
            }
        } else {

            Log.d("EXPORT ", "TODOS LOS GASTOS ESTÁN EXPORTADOS");
        }

        builder.setProgress(_MAX, 65, false);
        startForeground(1, builder.build());

        if (cursorCobrosManuales.getCount() > 0) {
            existeCobrosManuales = true;
            while (cursorCobrosManuales.moveToNext()) {
                JSONObject jsonObject = null;

                try {
                    int _id = cursorCobrosManuales.getInt(cursorCobrosManuales.getColumnIndexOrThrow(DbAdapter_Cobros_Manuales.CM_id_Cobros_Manuales));
                    int categoriaMovimiento = cursorCobrosManuales.getInt(cursorCobrosManuales.getColumnIndexOrThrow(DbAdapter_Cobros_Manuales.CM_Categoria_Movimiento));
                    Double Importe = cursorCobrosManuales.getDouble(cursorCobrosManuales.getColumnIndexOrThrow(DbAdapter_Cobros_Manuales.CM_Importe));
                    String fechaHora = cursorCobrosManuales.getString(cursorCobrosManuales.getColumnIndexOrThrow(DbAdapter_Cobros_Manuales.CM_Fecha_Hora));
                    String referencia = cursorCobrosManuales.getString(cursorCobrosManuales.getColumnIndexOrThrow(DbAdapter_Cobros_Manuales.CM_Referencia));
                    int usuario = cursorCobrosManuales.getInt(cursorCobrosManuales.getColumnIndexOrThrow(DbAdapter_Cobros_Manuales.CM_Usuario));
                    String serie = cursorCobrosManuales.getString(cursorCobrosManuales.getColumnIndexOrThrow(DbAdapter_Cobros_Manuales.CM_Serie));
                    int numero = cursorCobrosManuales.getInt(cursorCobrosManuales.getColumnIndexOrThrow(DbAdapter_Cobros_Manuales.CM_Numero_comprobante));


                    jsonObject = api.InsCobroManual(idLiquidacion, categoriaMovimiento, Importe, fechaHora, referencia, usuario, serie, numero);
                    Log.d("JSONCOBROSMANUALES", jsonObject.toString());

                    if (Utils.isSuccesful(jsonObject)) {
                        int idReturn = jsonObject.getInt("Value");
                        long estadoUpdate = dbAdapter_cobros_manuales.updateCobrosManuales(Constants._EXPORTADO, _id);
                        if (estadoUpdate > 0) {
                            Log.d("ACTUALIZOCOBROSMANUALES", "" + estadoUpdate);//
                            JSONObject jsonObjectFlex = null;
                            jsonObjectFlex = api.InsCobro(idReturn);

                            Log.d("JSONOBJECTFLEX", "" + jsonObjectFlex.toString());

                            if (Utils.isSuccesful(jsonObjectFlex)) {
                                int idFlex = jsonObjectFlex.getInt("Value");
                                if(idFlex==1 || idFlex ==2){
                                    long gu = dbAdapter_cobros_manuales.updateCobrosManualesPorId(Constants._EXPORTADO_FLEX, _id, idReturn);
                                }else{
                                    long gu = dbAdapter_cobros_manuales.updateCobrosManualesPorId(Constants.POR_EXPORTAR_FLEX, _id, idReturn);
                                }

                                //Actualizar Campo xD sino traer otra vez para exportarlo :c
                            }else{
                                long gu = dbAdapter_cobros_manuales.updateCobrosManualesPorId(Constants.POR_EXPORTAR_FLEX, _id, idReturn);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //Cobros
        //============================================================================================================
        Cursor crCobrosInsrCaja = dbAdapter_impresion_cobros.listarParaExportar();

        if (crCobrosInsrCaja.getCount() > 0) {
            existeCobrosManuales = true;

            for (crCobrosInsrCaja.moveToFirst(); !crCobrosInsrCaja.isAfterLast(); crCobrosInsrCaja.moveToNext()) {
                JSONObject jsonObject = null;



                try {

                    Log.d("COBROSNORMALESnORMALES",": "+   idLiquidacion+"-"+
                            2+"-"+
                            crCobrosInsrCaja.getDouble(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_monto))+"-"+
                            crCobrosInsrCaja.getInt(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_estado_cobrado))+"-"+
                            crCobrosInsrCaja.getString(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_fecha))+"-"+
                            String.valueOf(crCobrosInsrCaja.getString(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_referencia)))+"-"+
                            idUsuario+"-"+
                            crCobrosInsrCaja.getInt(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_id_comprobante))+"-"+
                            crCobrosInsrCaja.getInt(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_id_plan_pago))+"-"+
                            crCobrosInsrCaja.getInt(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_id_plan_pago_detalle))
                    );


                    jsonObject = api.CreateInsertarCaja(
                            idLiquidacion,
                            2,
                            crCobrosInsrCaja.getDouble(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_monto)),
                            crCobrosInsrCaja.getInt(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_estado_cobrado)),
                            crCobrosInsrCaja.getString(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_fecha)),
                            String.valueOf(crCobrosInsrCaja.getString(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_referencia))),
                            idUsuario,
                            crCobrosInsrCaja.getInt(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_id_comprobante)),
                            crCobrosInsrCaja.getInt(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_id_plan_pago)),
                            crCobrosInsrCaja.getInt(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_id_plan_pago_detalle))
                    );
                    Log.d("COBROSNORMALESnORMALES", jsonObject.toString());
                    Log.d("EXPORT INSERT C", "" + Utils.isSuccesful(jsonObject));

                    if (Utils.isSuccesful(jsonObject)) {
                        int idImpresionCobro= crCobrosInsrCaja.getInt(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_id));
                        int id = jsonObject.getInt("Value");//Para Insertar en Flex registrar cobros en el flex
                        //listidInsertarCaja.add("" + crCobrosInsrCaja.getInt(crCobrosInsrCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_cob_historial)));
                        int actualizoExport =0;
                        if(id>0){
                            actualizoExport = dbAdapter_impresion_cobros.updateCobrosExportado(idImpresionCobro,Constants._EXPORTADO);
                        }
                        if(id>0){
                            JSONObject jsonObjectFlex = null;
                            jsonObjectFlex = api.InsCobro(id);
                            int idFlex=0;
                            if (Utils.isSuccesful(jsonObjectFlex)) {
                                Log.d(TAG+"COBROS",""+jsonObjectFlex.toString());
                                idFlex = jsonObjectFlex.getInt("Value");
                                if(idFlex>0){
                                    int gu = dbAdapter_impresion_cobros.updateCobrosExportadoFlex(idImpresionCobro,Constants._EXPORTADO,id);
                                }else{
                                    int gS = dbAdapter_impresion_cobros.updateCobrosExportadoFlex(idImpresionCobro,Constants._CREADO,id);
                                }

                                //Actualizar Campo xD sino traer otra vez para exportarlo :c

                            }else{
                                int gS = dbAdapter_impresion_cobros.updateCobrosExportadoFlex(idImpresionCobro,Constants._CREADO,id);
                            }
                            Log.d("COBROSNORMALES", jsonObjectFlex.toString()+"-"+crCobrosInsrCaja.getInt(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_id_comprobante))+"-"+crCobrosInsrCaja.getString(crCobrosInsrCaja.getColumnIndexOrThrow(DbAdapter_Impresion_Cobros.Imprimir_monto)));
                        }
                        //int ac = dbAdapter_comprob_cobro.changeEstadoToExportParcial(crCobrosInsrCaja.getString(crCobrosInsrCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_cob_historial)), Constants._EXPORTADO);
                        /*if (ac > 0) {
                            JSONObject jsonObjectFlex = null;
                            jsonObjectFlex = api.InsCobro(id);

                            Log.d("JSONOBJECTFLEX", "" + jsonObjectFlex.toString());

                            if (!Utils.isSuccesful(jsonObjectFlex)) {
                                int idFlex = jsonObjectFlex.getInt("Value");
                                int gu = dbAdapter_comprob_cobro.changeEstadoToExportToFlexForId(crCobrosInsrCaja.getString(crCobrosInsrCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_cob_historial)), Constants.POR_EXPORTAR_FLEX, id);
                                //Actualizar Campo xD sino traer otra vez para exportarlo :c

                            }
                        }*/

                    }


                } catch (Exception e) {
                    Log.e("ERROR AL EXPORTAR", "" + e.getMessage());
                    e.printStackTrace();
                }


            }
/*
            String[] idCCInsertarCaja = new String[listidInsertarCaja.size()];
            listidInsertarCaja.toArray(idCCInsertarCaja);
            //ID_ACTUALIZAR = idCCInsertarCaja;

            for (int i = 0; i < idCCInsertarCaja.length; i++) {
                Log.d("ID EXPORTADOS ", "" + idCCInsertarCaja[i]);
            }

            if (listidInsertarCaja.size() > 0) {
                dbAdapter_comprob_cobro.changeEstadoToExport(idCCInsertarCaja, Constants._EXPORTADO);
            }*/


        } else {
            Log.d("EXPORT INSERTAR CAJA", "TODOS LOS REGISTROS INSERTAR CAJA DE COMPROBANTE COBRO ESTÁN EXPORTADOS");
        }


        //============================================================================================================

        Cursor crCobrosExpFlex = dbAdapter_impresion_cobros.listarCobrosExpFlex();
        Log.d(TAG+"COBROSFLEXNO",""+crCobrosExpFlex.getCount());
        for (crCobrosExpFlex.moveToFirst(); !crCobrosExpFlex.isAfterLast(); crCobrosExpFlex.moveToNext()) {
            JSONObject jsonObject = null;
            try {
                jsonObject = api.InsCobro(crCobrosExpFlex.getInt(crCobrosExpFlex.getColumnIndex(DbAdapter_Impresion_Cobros.Imprimir_id_flex)));

                if (Utils.isSuccesful(jsonObject)) {
                    int idFlex = jsonObject.getInt("Value");
                    if(idFlex==1 || idFlex==2){
                        int gu = dbAdapter_impresion_cobros.updateCobrosExportadoFlex(crCobrosExpFlex.getInt(crCobrosExpFlex.getColumnIndex(DbAdapter_Impresion_Cobros.Imprimir_id)),Constants._EXPORTADO,idFlex);
                    }

                }
                Log.d(TAG+"COBROSFLEXNO",""+jsonObject.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        String[] ID_ACTUALIZAR = new String[]{};
/*
        builder.setProgress(_MAX, 75, false);
        startForeground(1, builder.build());

        if (cursorInsertarCaja.getCount() > 0) {
            existeCobrosManuales = true;

            for (cursorInsertarCaja.moveToFirst(); !cursorInsertarCaja.isAfterLast(); cursorInsertarCaja.moveToNext()) {
                JSONObject jsonObject = null;


                Log.d("EXPORT INSERT C", "" + idLiquidacion + "-" +
                        2 + "-" +
                        cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_monto_cobrado)) + "-" +
                        cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_estado_cobro)) + "-" +
                        cursorInsertarCaja.getString(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_fecha_cobro)) + "-" +
                        String.valueOf(cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_comprobante_cobro))) + "-" +
                        idUsuario + "-" +
                        cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_comprob)) + "-" +
                        cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_plan_pago)) + "-" +
                        cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_plan_pago_detalle)));
                try {
                    jsonObject = api.CreateInsertarCaja(
                            idLiquidacion,
                            2,
                            cursorInsertarCaja.getDouble(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_monto_cobrado)),
                            cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_estado_cobro)),
                            cursorInsertarCaja.getString(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_fecha_cobro)),
                            String.valueOf(cursorInsertarCaja.getString(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_comprobante_cobro))),
                            idUsuario,
                            cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_comprob)),
                            cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_plan_pago)),
                            cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_plan_pago_detalle))
                    );
                    Log.d("COBROSNORMALES", jsonObject.toString());
                    Log.d("EXPORT INSERT C", "" + Utils.isSuccesful(jsonObject));

                    if (Utils.isSuccesful(jsonObject)) {
                        int id = jsonObject.getInt("Value");
                        listidInsertarCaja.add("" + cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_cob_historial)));
                        int ac = dbAdapter_comprob_cobro.changeEstadoToExportToFlex(cursorInsertarCaja.getString(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_cob_historial)), Constants._EXPORTADO);
                        if (ac > 0) {
                            JSONObject jsonObjectFlex = null;
                            jsonObjectFlex = api.InsCobro(id);

                            Log.d("JSONOBJECTFLEX", "" + jsonObjectFlex.toString());

                            if (!Utils.isSuccesful(jsonObjectFlex)) {
                                int idFlex = jsonObjectFlex.getInt("Value");
                                int gu = dbAdapter_comprob_cobro.changeEstadoToExportToFlexForId(cursorInsertarCaja.getString(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_cob_historial)), Constants.POR_EXPORTAR_FLEX, id);
                                //Actualizar Campo xD sino traer otra vez para exportarlo :c

                            }
                        }

                    }


                } catch (Exception e) {
                    Log.e("ERROR AL EXPORTAR", "" + e.getMessage());
                    e.printStackTrace();
                }


            }

            String[] idCCInsertarCaja = new String[listidInsertarCaja.size()];
            listidInsertarCaja.toArray(idCCInsertarCaja);
            ID_ACTUALIZAR = idCCInsertarCaja;

            for (int i = 0; i < idCCInsertarCaja.length; i++) {
                Log.d("ID EXPORTADOS ", "" + idCCInsertarCaja[i]);
            }

            if (listidInsertarCaja.size() > 0) {
                dbAdapter_comprob_cobro.changeEstadoToExport(idCCInsertarCaja, Constants._EXPORTADO);
            }


        } else {
            Log.d("EXPORT INSERTAR CAJA", "TODOS LOS REGISTROS INSERTAR CAJA DE COMPROBANTE COBRO ESTÁN EXPORTADOS");
        }


        */
        //======================================================
        builder.setProgress(_MAX, 80, false);
        //Get Important for Flex Manual

        Cursor crManual = dbAdapter_cobros_manuales.filterExportForFlex();
        for (crManual.moveToFirst(); !crManual.isAfterLast(); crManual.moveToNext()) {
            JSONObject jsonObject = null;
            try {
                jsonObject = api.InsCobro(crManual.getInt(crManual.getColumnIndex(DbAdapter_Cobros_Manuales.CM_id_flex)));

                if (Utils.isSuccesful(jsonObject)) {
                    int idFlex = jsonObject.getInt("Value");
                    if(idFlex==1 || idFlex==2){
                        long gu = dbAdapter_cobros_manuales.updateCobrosManualesPorId(Constants.POR_EXPORTAR_FLEX, crManual.getInt(crManual.getColumnIndex(DbAdapter_Cobros_Manuales.CM_id_Cobros_Manuales)), idFlex);

                    }

                }
                Log.d(TAG+"COBROSFLEXMA",""+jsonObject.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*
        //
        //Get Important for Flex  Normal
        Cursor crNormal = dbAdapter_comprob_cobro.fetchNormal();
        for (crNormal.moveToFirst(); !crNormal.isAfterLast(); crNormal.moveToNext()) {
            JSONObject jsonObject = null;
            try {
                jsonObject = api.InsCobro(crNormal.getInt(crNormal.getColumnIndex(DbAdapter_Comprob_Cobro.CC_id_flex)));

                if (Utils.isSuccesful(jsonObject)) {
                    int idFlex = jsonObject.getInt("Value");
                    int gu = dbAdapter_comprob_cobro.changeEstadoToExportToFlexForId(crNormal.getString(crNormal.getColumnIndex(DbAdapter_Comprob_Cobro.CC_id_cob_historial)), Constants._EXPORTADO_FLEX, idFlex);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */

        //
        startForeground(1, builder.build());


        if (cursorAutorizacionCobro.getCount() > 0) {

            for (cursorAutorizacionCobro.moveToFirst(); !cursorAutorizacionCobro.isAfterLast(); cursorAutorizacionCobro.moveToNext()) {
                JSONObject jsonObject = null;


                Log.d("EXPORT AUT COBROS", "");
                try {
                    jsonObject = api.CreateSolicitudAutorizacionCobro(
                            idAgente,
                            cursorAutorizacionCobro.getInt(cursorAutorizacionCobro.getColumnIndexOrThrow(dbAdapter_temp_autorizacion_cobro.temp_id_motivo_solicitud)),
                            cursorAutorizacionCobro.getInt(cursorAutorizacionCobro.getColumnIndexOrThrow(dbAdapter_temp_autorizacion_cobro.temp_establec)),
                            cursorAutorizacionCobro.getInt(cursorAutorizacionCobro.getColumnIndexOrThrow(dbAdapter_temp_autorizacion_cobro.temp_id_estado_solicitud)),
                            cursorAutorizacionCobro.getString(cursorAutorizacionCobro.getColumnIndexOrThrow(dbAdapter_temp_autorizacion_cobro.temp_referencia)),
                            cursorAutorizacionCobro.getDouble(cursorAutorizacionCobro.getColumnIndexOrThrow(dbAdapter_temp_autorizacion_cobro.temp_montoCredito)),
                            0.0,
                            idUsuario,
                            cursorAutorizacionCobro.getInt(cursorAutorizacionCobro.getColumnIndexOrThrow(dbAdapter_temp_autorizacion_cobro.temp_id_autorizacion_cobro))
                    );
                    Log.d("EXPORT AUTO COBROS", jsonObject.toString());
                    Log.d("EXPORT AUTO COBROS", "" + Utils.isSuccesful(jsonObject));

                    if (Utils.isSuccesful(jsonObject)) {
                        listIdAutorizacionCobro.add("" + cursorAutorizacionCobro.getInt(cursorAutorizacionCobro.getColumnIndexOrThrow(dbAdapter_temp_autorizacion_cobro.temp_autorizacion_cobro)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            String[] idAutorizacionCobro = new String[listIdAutorizacionCobro.size()];
            listIdAutorizacionCobro.toArray(idAutorizacionCobro);

            for (int i = 0; i < idAutorizacionCobro.length; i++) {
                Log.d("ID EXPORTADOS ", "" + idAutorizacionCobro[i]);
            }

            if (listIdAutorizacionCobro.size() > 0) {
                dbAdapter_temp_autorizacion_cobro.changeEstadoToExport(idAutorizacionCobro, Constants._EXPORTADO);
            }


        } else {
            Log.d("EXPORT AUT COB", "TODOS LAS AUTORIZACIONES DE COBRO ESÁN EXPORTADAS");
        }


        builder.setProgress(_MAX, 90, false);
        startForeground(1, builder.build());


        //EXPORTAR TODOS LOS REGISTROS ACTUALIZADOS EN ANDROID

        if (cursorEventoEstablecimiento.getCount() > 0) {

            for (cursorEventoEstablecimiento.moveToFirst(); !cursorEventoEstablecimiento.isAfterLast(); cursorEventoEstablecimiento.moveToNext()) {
                JSONObject jsonObject = null;

                Log.d("EE EXPORT DATOS", "" +
                                cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_establec)) + "-" +
                                idLiquidacion + "-" +
                                cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_estado_atencion)) + "-" +
                                cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_estado_no_atencion)) + "-" +
                                cursorEventoEstablecimiento.getString(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_time_atencion))
                );
                try {
                    jsonObject = api.UpdateEstadoEstablecimiento2(
                            cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_establec)),
                            idLiquidacion,
                            cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_estado_atencion)),
                            cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_estado_no_atencion)),
                            cursorEventoEstablecimiento.getString(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_time_atencion))
                    );

                    Log.d("E M ESTABLECIMIENTO ", jsonObject.toString());

                    if (Utils.isSuccesful(jsonObject)) {
                        listIdEstablecimientoUpdated.add("" + cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_evt_establec)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String[] idsEstablecimientoUpdated = new String[listIdEstablecimientoUpdated.size()];
            listIdEstablecimientoUpdated.toArray(idsEstablecimientoUpdated);

            for (int i = 0; i < idsEstablecimientoUpdated.length; i++) {
                Log.d("ID EXPORTADOS ", "" + idsEstablecimientoUpdated[i]);
            }

            if (listIdEstablecimientoUpdated.size() > 0) {
                dbAdaptert_evento_establec.changeEstadoToExport(idsEstablecimientoUpdated, Constants._EXPORTADO);
            }


        } else {
            Log.d("EXPORT", "TODOS LOS ESTABLECIMIENTOS ESTÁN EXPORTADOS [ACTUALIZADOS]");
        }

        //Conseguir Operaciones:
        Cursor cursor = dbAdapter_temp_canjes_devoluciones.getAllOperacionEstablecimiento();
        while (cursor.moveToNext()) {
            String idEstablec = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_establecimiento));
            Log.d("Hola mundo", "" + cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_establecimiento)));
            ExportCanjesDevolucionesLater exportCanjesDevoluciones = new ExportCanjesDevolucionesLater(context);
            exportCanjesDevoluciones.execute(Utils.getDatePhone(), idEstablec, Constants._ACTUALIZADO + "");
        }


        //EXPORTACIÓN AL FLEX

        builder.setContentTitle("Exportando a FLEX");

        Cursor cursorExportacionFlex = dbAdapter_exportacion_comprobantes.filterExport();

        Log.d(TAG, "COUNT CURSOR_EXPORTACION_FLEX :" + cursorExportacionFlex.getCount());


        int count = cursorExportacionFlex.getCount();
        if (cursorExportacionFlex.getCount() > 0) {

            for (cursorExportacionFlex.moveToFirst(); !cursorExportacionFlex.isAfterLast(); cursorExportacionFlex.moveToNext()) {
                builder.setProgress(_MAX + (count * 10), 90 + (cursorExportacionFlex.getPosition() * 10), false);
                startForeground(1, builder.build());
                JSONObject jsonObjectSuccesfull = null;
                Log.d(TAG, "EXPORTACIÓN AL FLEX -->  _ID : " + cursorExportacionFlex.getLong(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id)) + ", _ID_SID : " +
                                cursorExportacionFlex.getInt(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id_sid)) + ", _ID_SQLITE : " +
                                cursorExportacionFlex.getInt(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id_sqlite)) + ", ESTADO_SINCRONIZACIÓN : " +
                                cursorExportacionFlex.getInt(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_estado_sincronizacion))
                );

                try {
                    jsonObjectSuccesfull = api.InsComprobante(cursorExportacionFlex.getInt(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id_sid)));

                    Log.d(TAG, "SUCCES EXPORTACIÓN FLEX " + Utils.isSuccesful(jsonObjectSuccesfull));
                    Log.d(TAG, "JSON EXPORTACIÓN FLEX " + jsonObjectSuccesfull.toString());

                    int idRespuestaFlex = parserIDRespuestaFlex(jsonObjectSuccesfull);

                    Log.d(TAG, "ID RESPUESTA FLEX : " + idRespuestaFlex);

                    if (idRespuestaFlex == 1 || idRespuestaFlex == 2 ) {
                        listIdExportacionFlex.add("" + cursorExportacionFlex.getLong(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id)));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String[] idExportacionComprobantes = new String[listIdExportacionFlex.size()];
            listIdExportacionFlex.toArray(idExportacionComprobantes);

            Log.d(TAG, "NRO REGISTROS EXPORTADOS SATISFACTORIAMENTE AL FLEX : " + (idExportacionComprobantes.length));
            for (int i = 0; i < idExportacionComprobantes.length; i++) {
                Log.d(TAG, "_ID EXPORT SATISFACTORIO " + idExportacionComprobantes[i]);
            }

            if (listIdExportacionFlex.size() > 0) {
                int nRegistrosExportados = dbAdapter_exportacion_comprobantes.changeEstadoToExport(idExportacionComprobantes, Constants._EXPORTADO);
                Log.d(TAG, "REGISTROS EXPORTADOS AL FLEX : " + nRegistrosExportados);
            }


        } else {
            Log.d(TAG, "TODOS LOS COMPROBANTES ESTÁN EXPORTADOS AL FLEX");
        }

        //-------------------------
        /*
        Cursor cr = dbAdapter_establecimeinto_historial.fetchTemEstablecEnviar(10);
        JSONObject jsonObject = null;
        Log.d("ESTABLECI",""+cr.getCount());
        while (cr.moveToNext()) {
            try {
                jsonObject = api.fins_ClienteTemporal(
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nombres)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apMaterno)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apPaterno)),
                        cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_categoria_estable)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_celular_one)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_celular_two)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion_establecimiento)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_direccion_fiscal)),
                        0,
                        0,
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nro_documento)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_correo)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_latitud)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_longitud)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_telefono_fijo)),
                        cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_documento)),
                        cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_establecimiento)),
                        cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_persona)),
                        cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_usuario_accion))

                );
                Log.d("ESTABLECI",""+jsonObject.toString());

                if (Utils.isSuccesful(jsonObject)) {
                    int id = jsonObject.getInt("Value");
                    if (id > 1) {
                        long a = dbAdapter_establecimeinto_historial.updateTempEstablecById(cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_histo_id)), id, Constants._ACTUALIZADO);
                        if (a > 0) {
                            //
                            dbAdaptert_evento_establec.updateEstabEstado(cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_histo_id)), 6 + "");
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("ESTABLECI",""+e.getMessage());
                e.printStackTrace();
            }
        }

        */


        builder.setProgress(_MAX, _MAX, false);
        startForeground(1, builder.build());


        // Quitar de primer plano
        stopForeground(true);

    }

    public int getIdComprobanteVentaRetornado(JSONObject object) {
        Log.d(TAG, "JSON PARSER ID_COMPROB_VENTA_RETORNADO : " + object);
        int idComprobante = -1;
        try {
            JSONArray jsonArray = object.getJSONArray("Value");
            JSONObject jsonObj = null;

            jsonObj = jsonArray.getJSONObject(0);
            idComprobante = jsonObj.getInt("idComprobanteVenta");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSON ID C V R", e.getMessage());
        }
        return idComprobante;
    }

    public int getidPlanPagoRetornado(JSONObject object) {
        int idPlanPago = -1;
        try {
            JSONArray jsonArray = object.getJSONArray("Value");
            JSONObject jsonObj = null;

            jsonObj = jsonArray.getJSONObject(0);
            idPlanPago = jsonObj.getInt("idPlanPago");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONPARSER ERROR", e.getMessage());
        }
        return idPlanPago;
    }

    public int parserIDRespuestaFlex(JSONObject jsonObj) {
        int idRespuesta = -1;
        try {
            Log.d("CADENA A PARSEAR ", jsonObj.toString());
            idRespuesta = jsonObj.getInt("Value");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONPARSER ERROR", e.getMessage());
        }
        return idRespuesta;
    }
}
