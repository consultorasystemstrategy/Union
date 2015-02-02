package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.MyApplication;
import union.union_vr1.Vistas.VMovil_Online_Pumovil;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ExportMain extends AsyncTask<String, String, String> {

    private Activity mainActivity;
    private ProgressDialog progressDialog;

    //DEFINO LAS VARIABLES A MIS MANEJADORES DE LAS TABLAS
    private DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
    private DbAdapter_Comprob_Venta dbAdapter_comprob_venta;
    private DbAdapter_Comprob_Venta_Detalle dbAdapter_comprob_venta_detalle;
    private DbAdapter_Histo_Venta dbAdapter_histo_venta;
    private DbAdapter_Comprob_Cobro dbAdapter_comprob_cobro;
    private DbAdapter_Histo_Venta_Detalle dbAdapter_histo_venta_detalle;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;

    public ExportMain(Activity mainActivity) {
        this.mainActivity = mainActivity;
        //INSTANCIO LAS CLASES DE MIS MANEJADORES DE DB
        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(mainActivity);
        dbAdapter_comprob_venta = new DbAdapter_Comprob_Venta(mainActivity);
        dbAdapter_comprob_venta_detalle = new DbAdapter_Comprob_Venta_Detalle(mainActivity);
        dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(mainActivity);
        dbAdapter_histo_venta_detalle = new DbAdapter_Histo_Venta_Detalle(mainActivity);
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdapter_histo_venta = new DbAdapter_Histo_Venta(mainActivity);

        //ABRO LA CONEXIÓN A LA DB
        dbAdapter_informe_gastos.open();
        dbAdapter_comprob_venta.open();
        dbAdapter_comprob_venta_detalle.open();
        dbAdapter_histo_venta_detalle.open();
        dbAdapter_comprob_cobro.open();
        dbAdapter_histo_venta_detalle.open();
        dbAdaptert_evento_establec.open();
        dbAdapter_histo_venta.open();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        createProgressDialog();
    }

    @Override
    protected String doInBackground(String... strings) {
        //LA CLASE QUE TIENE LOS MÉTODOS DE EXPORTACIÓN A CONSUMIR
        StockAgenteRestApi api = new StockAgenteRestApi();

        //FILTRO LOS REGISTROS DE LAS TABLAS A EXPORTAR
        Cursor cursorInformeGastos = dbAdapter_informe_gastos.filterExport();
        Cursor cursorComprobanteVenta = dbAdapter_comprob_venta.filterExport();
        Cursor cursorComprobanteVentaDetalle = dbAdapter_comprob_venta_detalle.filterExport();
        Cursor cursorComprobanteCobro = dbAdapter_comprob_cobro.filterExport();
        Cursor cursorInsertarCaja  = dbAdapter_comprob_cobro.filterExportUpdatedAndEstadoCobro();
        Cursor cursorEventoEstablecimiento = dbAdaptert_evento_establec.filterExportUpdated();

        Cursor cursorHistoVentaCreated = dbAdapter_histo_venta.filterExport();
        Cursor cursorHistoVentaDetalleCreated = dbAdapter_histo_venta_detalle.filterExport();


/*        Cursor cursorCC = dbAdapter_comprob_cobro.fetchAllComprobCobros();


        for (cursorCC.moveToFirst(); !cursorCC.isAfterLast(); cursorCC.moveToNext()){
            Log.d("DATOS CC", ""+
                            cursorCC.getInt(cursorCC.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_cob_historial))+"-"+
                            cursorCC.getDouble(cursorCC.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_monto_a_pagar))+"-"+
                            cursorCC.getString(cursorCC.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_fecha_programada))+"-"+
                            cursorCC.getString(cursorCC.getColumnIndexOrThrow(Constants._SINCRONIZAR))
            );
        }

*/

        Log.d("EXPORT", "INICIANDO EXPORTACIÓN ...");

        publishProgress(""+50);
        List<String> listIdInfomeGastos = new ArrayList<String>();
        List<String> listIdComprobantes = new ArrayList<String>();
        List<String> listIdComprobanteVentaDetalle = new ArrayList<String>();
        List<String> listIdComprobanteCobro = new ArrayList<String>();
        List<String> listIdEstablecimientoUpdated = new ArrayList<String>();
        List<String> listidInsertarCaja = new ArrayList<String>();


        List<String> listIdHVCreated = new ArrayList<String>();
        List<String> listIdHVUpdated = new ArrayList<String>();
        List<String> listIdHVDCreated = new ArrayList<String>();
        List<String> listIdHVDUpdated = new ArrayList<String>();





        //EXPORTAR TODOS LOS REGISTROS CREADOS EN ANDROID [GUARDADOS EN SQLITE]
        if (cursorComprobanteVenta.getCount()>0) {
            for (cursorComprobanteVenta.moveToFirst(); !cursorComprobanteVenta.isAfterLast(); cursorComprobanteVenta.moveToNext()){
                JSONObject jsonObjectSuccesfull = null;
                Log.d("Datos Export CV",cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_serie))+ " - " +
                        cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_num_doc))+ " - " +
                        cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_forma_pago))+ " - " +
                        cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_fecha_doc))+ " - " +
                        cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_base_imp))+ " - " +
                        cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_igv))+ " - " +
                        cursorComprobanteVenta.getDouble(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_total))+ " - " +
                        cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_tipo_doc))+ " - " +
                        cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_agente))+ " - " +
                        cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_estado_comp))+ " - " +
                        0+ " - " +
                        ((MyApplication) mainActivity.getApplication()).getIdLiquidacion()+ " - " +
                        cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_tipo_venta))+ " - " +
                        cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_codigo_erp))+ " - " +
                        cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_establec)));
                try {
                    if (cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_forma_pago))==1){

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
                                ((MyApplication) mainActivity.getApplication()).getIdLiquidacion(),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_tipo_venta)),
                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_codigo_erp)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_establec)),
                                ((MyApplication) mainActivity.getApplication()).getIdUsuario()
                        );


                    }else if (cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_forma_pago))==2){

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
                                ((MyApplication) mainActivity.getApplication()).getIdLiquidacion(),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_tipo_venta)),
                                cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_codigo_erp)),
                                cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_establec)),
                                ((MyApplication) mainActivity.getApplication()).getIdUsuario()
                        );

                        if (isSuccesfulExport(jsonObjectSuccesfull)){
                            int idPlan = idPlanPagoDetalle(jsonObjectSuccesfull);

                            Log.d("Export id CV IGUALES",""+cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)));
                            Cursor cursorPlanPago = dbAdapter_comprob_cobro.filterExportAndFetchById(cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)));

                            for (cursorPlanPago.moveToFirst(); !cursorPlanPago.isAfterLast(); cursorPlanPago.moveToNext()){

                                Log.d("EXPORT PPD", ""+idPlan+"-"+
                                        cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob))+"-"+
                                        cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_fecha_doc))+"-"+
                                        cursorPlanPago.getDouble(cursorPlanPago.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_monto_a_pagar))+"-"+
                                        ((MyApplication) mainActivity.getApplication()).getIdUsuario()+"-"+
                                                cursorPlanPago.getString(cursorPlanPago.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_fecha_programada))+"-"+
                                                cursorPlanPago.getString(cursorPlanPago.getColumnIndexOrThrow(Constants._SINCRONIZAR))
                                );
                                 JSONObject jsonObjectSuccess= api.CreatePlanPagoDetalleExp(
                                        idPlan,
                                        cursorComprobanteVenta.getString(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_fecha_doc)),
                                        cursorPlanPago.getDouble(cursorPlanPago.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_monto_a_pagar)),
                                        ((MyApplication) mainActivity.getApplication()).getIdUsuario(),
                                        cursorPlanPago.getString(cursorPlanPago.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_fecha_programada))
                                        );

                                if (isSuccesfulExport(jsonObjectSuccesfull)){
                                    listIdComprobanteCobro.add(""+cursorPlanPago.getInt(cursorPlanPago.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_cob_historial)));
                                }
                                Log.d("EXPORT JSON SUCCESFULL PPD", jsonObjectSuccess.toString());
                            }

                            String[] idComprobanteCobro = new String[listIdComprobanteCobro.size()];
                            listIdComprobanteCobro.toArray(idComprobanteCobro);

                            for (int i = 0; i < idComprobanteCobro.length; i++) {
                                Log.d("ID EXPORTADOS ", "" + idComprobanteCobro[i]);
                            }

                            if (listIdComprobantes.size()>0) {
                                dbAdapter_comprob_cobro.changeEstadoToExport(idComprobanteCobro, Constants._EXPORTADO);
                            }
                        }

                    }

                    Log.d("JSON INT RETURN ", jsonObjectSuccesfull.toString());

                    Log.d("SUCCESFULL EXPORT", ""+isSuccesfulExport(jsonObjectSuccesfull));

                    if (isSuccesfulExport(jsonObjectSuccesfull)){
                        listIdComprobantes.add(""+cursorComprobanteVenta.getInt(cursorComprobanteVenta.getColumnIndexOrThrow(dbAdapter_comprob_venta.CV_id_comprob)));
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

            if (listIdComprobantes.size()>0) {
                dbAdapter_comprob_venta.changeEstadoToExport(idComprobantes, Constants._EXPORTADO);
            }

        }else
        {
            Log.d("EXPORT ", "TODOS LOS COMPROBANTES DE VENTAS ESTÁN EXPORTADOS");
        }

        if (cursorComprobanteVentaDetalle.getCount()>0){
            for (cursorComprobanteVentaDetalle.moveToFirst(); !cursorComprobanteVentaDetalle.isAfterLast(); cursorComprobanteVentaDetalle.moveToNext()){
                JSONObject jsonObjectSuccesfull = null;

                try {

                    Log.d("VALOR UNIDAD ", ""+ cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_valor_unidad)) );

                    Log.d("DATOS EXPORT CVD ", "" + cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_id_comprob)) + "-" +
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_id_producto)) + "-" +
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_cantidad)) + "-" +
                            cursorComprobanteVentaDetalle.getDouble(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_importe)) + "-" +
                            ((MyApplication) mainActivity.getApplication()).getIdUsuario() + "-" +
                            cursorComprobanteVentaDetalle.getDouble(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_costo_venta)) + "-" +
                            cursorComprobanteVentaDetalle.getDouble(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_precio_unit)) + "-" +
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_valor_unidad)));

                    jsonObjectSuccesfull = api.CreateComprobanteVentaDetalle(
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_id_comprob)),
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_id_producto)),
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_cantidad)),
                            cursorComprobanteVentaDetalle.getDouble(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_importe)),
                            ((MyApplication) mainActivity.getApplication()).getIdUsuario(),
                            cursorComprobanteVentaDetalle.getDouble(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_costo_venta)),
                            cursorComprobanteVentaDetalle.getDouble(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_precio_unit)),
                            cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndexOrThrow(dbAdapter_comprob_venta_detalle.CD_valor_unidad))
                            );

                    Log.d("SUCCESFULL EXPORT", ""+isSuccesfulExport(jsonObjectSuccesfull));
                    if (isSuccesfulExport(jsonObjectSuccesfull)){
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

            if (listIdComprobanteVentaDetalle.size()>0){
                dbAdapter_comprob_venta_detalle.changeEstadoToExport(idComprobantesDetalle, Constants._EXPORTADO);
            }
        }else{

            Log.d("EXPORT ", "TODOS LOS COMPROBANTES DE VENTA DETALLE ESTÁN EXPORTADOS");
        }

        if (cursorInformeGastos.getCount()>0){
            for (cursorInformeGastos.moveToFirst(); !cursorInformeGastos.isAfterLast(); cursorInformeGastos.moveToNext()){
                JSONObject jsonObjectSuccesfull = null;

                try {

                    Log.d("DATOS EXPORT GASTOS", ""+((MyApplication)mainActivity.getApplication()).getIdLiquidacion()+"-"+
                            cursorInformeGastos.getDouble(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_total))+"-"+
                            cursorInformeGastos.getString(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_fecha))+"-"+
                            ((MyApplication)mainActivity.getApplication()).getIdUsuario()+"-"+
                            cursorInformeGastos.getInt(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_id_tipo_gasto))+"-"+
                            cursorInformeGastos.getDouble(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_subtotal))+"-"+
                            cursorInformeGastos.getInt(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_igv))+"-"+
                            cursorInformeGastos.getString(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_referencia))+"-"+
                            cursorInformeGastos.getInt(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_id_proced_gasto)));

                    jsonObjectSuccesfull = api.CreateInformeGastos(

                            ((MyApplication)mainActivity.getApplication()).getIdLiquidacion(),
                            cursorInformeGastos.getDouble(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_total)),
                            cursorInformeGastos.getString(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_fecha)),
                            ((MyApplication)mainActivity.getApplication()).getIdUsuario(),
                            cursorInformeGastos.getInt(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_id_tipo_gasto)),
                            cursorInformeGastos.getDouble(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_subtotal)),
                            cursorInformeGastos.getDouble(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_igv)),
                            cursorInformeGastos.getString(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_referencia)),
                            cursorInformeGastos.getInt(cursorInformeGastos.getColumnIndexOrThrow(dbAdapter_informe_gastos.GA_id_proced_gasto))
                            );

                    Log.d("SUCCESFULL EXPORT", ""+isSuccesfulExport(jsonObjectSuccesfull));
                    if (isSuccesfulExport(jsonObjectSuccesfull)){
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

            if (listIdInfomeGastos.size()>0){
                dbAdapter_informe_gastos.changeEstadoToExport(idInformeGastos, Constants._EXPORTADO);
            }
        }else{

            Log.d("EXPORT ", "TODOS LOS GASTOS ESTÁN EXPORTADOS");
        }

        if (cursorHistoVentaCreated.getCount()>0){

            for (cursorHistoVentaCreated.moveToFirst(); !cursorHistoVentaCreated.isAfterLast(); cursorHistoVentaCreated.moveToNext()){
                JSONObject jsonObject = null;
                Log.d("DATOS EXPORT HV CREATED", ""+0+"-"+
                        1+"-"+
                        cursorHistoVentaCreated.getInt(cursorHistoVentaCreated.getColumnIndexOrThrow(dbAdapter_histo_venta.HV_id_agente))+"-"+
                        4+"-"+
                        cursorHistoVentaCreated.getInt(cursorHistoVentaCreated.getColumnIndexOrThrow(dbAdapter_histo_venta.HV_subtotal))+"-"+

                        cursorHistoVentaCreated.getString(cursorHistoVentaCreated.getColumnIndexOrThrow(dbAdapter_histo_venta.HV_id_histo))
                );
                try {
                    jsonObject = api.CreateHeaderDevoluciones(
                            0,
                            1,
                            cursorHistoVentaCreated.getInt(cursorHistoVentaCreated.getColumnIndexOrThrow(dbAdapter_histo_venta.HV_id_agente)),
                            4,
                            cursorHistoVentaCreated.getInt(cursorHistoVentaCreated.getColumnIndexOrThrow(dbAdapter_histo_venta.HV_subtotal)),
                            cursorHistoVentaCreated.getString(cursorHistoVentaCreated.getColumnIndexOrThrow(dbAdapter_histo_venta.HV_id_histo))

                            );

                    Log.d("EXPORT HV CREATED", jsonObject.toString());

                    if (isSuccesfulExport(jsonObject)){
                        listIdHVCreated.add(""+cursorHistoVentaCreated.getInt(cursorHistoVentaCreated.getColumnIndexOrThrow(dbAdapter_histo_venta.HV_id)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String[] idHVCreated = new String[listIdHVCreated.size()];
            listIdHVCreated.toArray(idHVCreated);

            for (int i = 0; i < idHVCreated.length; i++) {
                Log.d("ID EXPORTADOS ", "" + idHVCreated[i]);
            }

            if (listIdHVCreated.size()>0){
                dbAdapter_histo_venta.changeEstadoToExport(idHVCreated, Constants._EXPORTADO);
            }

        }else {
            Log.d("EXPORT HVD", "TODOS EL HISTORIAL DE VENTA CREADO HA SIDO EXPORTADO");
        }


        if (cursorHistoVentaDetalleCreated.getCount()>0){

            for (cursorHistoVentaDetalleCreated.moveToFirst(); !cursorHistoVentaDetalleCreated.isAfterLast(); cursorHistoVentaDetalleCreated.moveToNext()){
                JSONObject jsonObject = null;
                Log.d("DATOS EXPORT HVD CREATED", ""+cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_id_producto))+"-"+
                        cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_categoria_ope))+"-"+
                        cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_id_tipoper))+"-"+
                        cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_cantidad_ope))+"-"+
                        1+"-"+
                        cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_id_agente))+"-"+
                        1+"-"+
                        cursorHistoVentaDetalleCreated.getString(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_comprobante))+"-"+
                        cursorHistoVentaDetalleCreated.getString(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_lote))+"-"+
                        ((MyApplication)mainActivity.getApplication()).getIdLiquidacion()+"-"+
                        cursorHistoVentaDetalleCreated.getDouble(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_importe_ope))+"-"+
                        cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_id_comprob))+"-"+
                        cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_forma_ope))+"-"+
                        283+"-"+
                        cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_id_establec))+"-"+
                        cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_cantidad_ope_dev))+"-"+
                        cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_categoria_ope_dev))+"-"+
                        cursorHistoVentaDetalleCreated.getDouble(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_importe_ope_dev))+"-"+
                        cursorHistoVentaDetalleCreated.getString(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_fecha_ope_dev))+"-"+
                        cursorHistoVentaDetalleCreated.getString(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_hora_ope_dev)));
                try {
                    jsonObject = api.CreateDevoluciones(
                            cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_id_producto)),
                            cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_categoria_ope)),
                            cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_id_tipoper)),
                            cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_cantidad_ope)),
                            1,
                            cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_id_agente)),
                            1,
                            cursorHistoVentaDetalleCreated.getString(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_comprobante)),
                            cursorHistoVentaDetalleCreated.getString(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_lote)),
                            ((MyApplication)mainActivity.getApplication()).getIdLiquidacion(),
                            cursorHistoVentaDetalleCreated.getDouble(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_importe_ope)),
                            cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_id_comprob)),
                            cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_forma_ope)),
                            283,
                            cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_id_establec)),
                            cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_cantidad_ope_dev)),
                            cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_categoria_ope_dev)),
                            cursorHistoVentaDetalleCreated.getDouble(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_importe_ope_dev)),
                            cursorHistoVentaDetalleCreated.getString(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_fecha_ope_dev)),
                            cursorHistoVentaDetalleCreated.getString(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_hora_ope_dev))
                    );

                    Log.d("EXPORT HVD CREATED", jsonObject.toString());

                    if (isSuccesfulExport(jsonObject)){
                        listIdHVDCreated.add(""+cursorHistoVentaDetalleCreated.getInt(cursorHistoVentaDetalleCreated.getColumnIndexOrThrow(dbAdapter_histo_venta_detalle.HD_id_hventadet)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String[] idHVDCreated = new String[listIdHVDCreated.size()];
            listIdHVDCreated.toArray(idHVDCreated);

            for (int i = 0; i < idHVDCreated.length; i++) {
                Log.d("ID EXPORTADOS ", "" + idHVDCreated[i]);
            }

            if (listIdHVDCreated.size()>0){
                dbAdapter_histo_venta_detalle.changeEstadoToExport(idHVDCreated, Constants._EXPORTADO);
            }

        }else {
            Log.d("EXPORT HVD", "TODOS EL HISTORIAL DE VENTA DETALLE CREADO HA SIDO EXPORTADO");
        }




        if (cursorInsertarCaja.getCount()>0){

            for (cursorInsertarCaja.moveToFirst(); !cursorInsertarCaja.isAfterLast(); cursorInsertarCaja.moveToNext()){
                JSONObject jsonObject = null;


                Log.d(" EXPORT INSERT CAJA DATOS ", ""+((MyApplication)mainActivity.getApplication()).getIdLiquidacion()+"-"+
                        2+"-"+
                        cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_monto_cobrado))+"-"+
                        cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_estado_cobro))+"-"+
                        cursorInsertarCaja.getString(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_fecha_cobro))+"-"+
                        String.valueOf(cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_comprobante_cobro)))+"-"+
                        ((MyApplication)mainActivity.getApplication()).getIdUsuario()+"-"+
                        cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_comprob))+"-"+
                        cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_plan_pago))+"-"+
                        cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_plan_pago_detalle)));
                try {
                    jsonObject = api.CreateInsertarCaja(
                            ((MyApplication)mainActivity.getApplication()).getIdLiquidacion(),
                            2,
                            cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_monto_cobrado)),
                            cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_estado_cobro)),
                            cursorInsertarCaja.getString(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_fecha_cobro)),
                            String.valueOf(cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_comprobante_cobro))),
                            ((MyApplication)mainActivity.getApplication()).getIdUsuario(),
                            cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_comprob)),
                            cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_plan_pago)),
                            cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_plan_pago_detalle))
                            );
                    Log.d("EXPORT INSERTAR CAAJA MESSAGE", jsonObject.toString());
                    Log.d("EXPORT INSERTAR CAAJA MESSAGE SUCCESFUL", ""+isSuccesfulExport(jsonObject));

                    if (isSuccesfulExport(jsonObject)){
                        listidInsertarCaja.add(""+ cursorInsertarCaja.getInt(cursorInsertarCaja.getColumnIndexOrThrow(dbAdapter_comprob_cobro.CC_id_cob_historial)));
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            String[] idCCInsertarCaja = new String[listidInsertarCaja.size()];
            listidInsertarCaja.toArray(idCCInsertarCaja);

            for (int i = 0; i < idCCInsertarCaja.length; i++) {
                Log.d("ID EXPORTADOS ", "" + idCCInsertarCaja[i]);
            }

            if (listidInsertarCaja.size()>0){
                dbAdapter_comprob_cobro.changeEstadoToExport(idCCInsertarCaja, Constants._EXPORTADO);
            }


        }else{
            Log.d("EXPORT INSERTAR CAJA", "TODOS LOS REGISTROS INSERTAR CAJA DE COMPROBANTE COBRO ESTÁN EXPORTADOS");
        }



        //EXPORTAR TODOS LOS REGISTROS ACTUALIZADOS EN ANDROID

        if (cursorEventoEstablecimiento.getCount()>0){

            for (cursorEventoEstablecimiento.moveToFirst(); !cursorEventoEstablecimiento.isAfterLast(); cursorEventoEstablecimiento.moveToNext()){
                JSONObject jsonObject = null;

                Log.d("EVENTO ESTABLECIMIENTO EXPORT DATOS","" +
                        cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_establec))+"-"+
                        ((MyApplication)mainActivity.getApplication()).getIdLiquidacion()+"-"+
                        cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_estado_atencion))+"-"+
                        cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_estado_no_atencion)));
                try {
                    jsonObject = api.UpdateEstadoEstablecimiento(
                            cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_establec)),
                            ((MyApplication)mainActivity.getApplication()).getIdLiquidacion(),
                            cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_estado_atencion)),
                            cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_estado_no_atencion))
                    );

                    Log.d("EXPORT MESSAGE ESTABLECIMIENTO ", jsonObject.toString());

                    if (isSuccesfulExport(jsonObject)){
                        listIdEstablecimientoUpdated.add(""+cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_evt_establec)));
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

            if (listIdInfomeGastos.size()>0){
                dbAdaptert_evento_establec.changeEstadoToExport(idsEstablecimientoUpdated, Constants._EXPORTADO);
            }



        }else{
            Log.d("EXPORT", "TODOS LOS ESTABLECIMIENTOS ESTÁN EXPORTADOS [ACTUALIZADOS]");
        }

        publishProgress(""+100);


        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(Integer.parseInt(values[0]));
    }

    @Override
    protected void onPostExecute(String s) {

        if (mainActivity.isFinishing()){
            //dismissProgressDialog();
            progressDialog.dismiss();
            return;
        }else{
            progressDialog.setProgress(100);
            dismissProgressDialog();
            Toast.makeText(mainActivity.getApplicationContext(), "EXPORTACIÓN EXITOSA", Toast.LENGTH_LONG).show();
        }
        super.onPostExecute(s);
    }


    public void createProgressDialog(){
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setMessage("Exportando...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public boolean isSuccesfulExport(JSONObject jsonObj)
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
    public int  idPlanPagoDetalle(JSONObject jsonObj)
    {
        int idPlanPagoDetalle = -1;
        try {
            Log.d("CADENA A PARSEAR ", jsonObj.toString());
            idPlanPagoDetalle= jsonObj.getInt("Value");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parser Error Message", e.getMessage());
        }
        return idPlanPagoDetalle;
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
