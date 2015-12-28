package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Vistas.VMovil_Cobro_Credito;

/**
 * Created by Kelvin on 28/08/2015.
 */
public class ImportCredito extends AsyncTask<String, String, String> {

    private DbAdapter_Temp_Session session;
    private Activity mainActivity;
    private ProgressDialog progressDialog;
    private DbAdapter_Agente dbAdapter_agente;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DBAdapter_Temp_Autorizacion_Cobro dbAdapter_temp_autorizacion_cobro;
    private int idAgente;
    private int idLiquidacion;
    private int idUsuario;


    public ImportCredito(Activity mainActivity) {
        this.mainActivity = mainActivity;

        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();
        dbAdapter_agente = new DbAdapter_Agente(mainActivity);
        dbAdapter_agente.open();
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdaptert_evento_establec.open();
        dbAdapter_temp_autorizacion_cobro = new DBAdapter_Temp_Autorizacion_Cobro(mainActivity);
        dbAdapter_temp_autorizacion_cobro.open();

        idAgente = session.fetchVarible(1);
        idUsuario = dbAdapter_agente.getIdUsuario();
        idLiquidacion = session.fetchVarible(3);
    }

    @Override
    protected String doInBackground(String... strings) {
        StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);


        publishProgress("" + 10);

        //ACTUALIZAR LOS CRÉDITOS DE LOS ESTABLECIMIENTOS

        try {
            Cursor cursorEstablecimiento = dbAdaptert_evento_establec.fetchAllEstablecs();
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
                        Log.d("DATOS", "" + montocredito+"--"+idEstablecimiento1+"--"+estadoSolicitud+"--"+idAutorizacionCobro+"--"+fechaLimite+"--"+SolReferenciaIdAndroid);

                        boolean exists = dbAdapter_temp_autorizacion_cobro.existeAutorizacionCobro(SolReferenciaIdAndroid);

                        Log.d("EXISTEAC", "" + exists);
                        if (exists) {
                            int isActualizado = dbAdapter_temp_autorizacion_cobro.updateAutorizacionCobro(estadoSolicitud, idEstablecimiento1, fechaLimite, SolReferenciaIdAndroid);

                            Log.d("IMPORT REGISTRO AUTORIZACION COBRO ACTUALIZADO ", "" + isActualizado+"ESTADO SOLICITUD"+estadoSolicitud);
                            if (estadoSolicitud == 2) {
                                Log.d("GET CURSOR INDEX","123456");
                                Cursor cr = dbAdapter_comprob_cobro.getComprobanteCobroById(SolReferenciaIdAndroid);

                                if (cr.moveToFirst()) {

                                    double montopagado = dbAdapter_temp_autorizacion_cobro.getMontoPagado(SolReferenciaIdAndroid);
                                    Log.d("MONTO PAGADO",""+montopagado);
                                    int estado = dbAdapter_comprob_cobro.updateComprobCobrosAutorizacion(0, montopagado, SolReferenciaIdAndroid);
                                    Log.e("IDESTADO",""+estado);
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
                                        long l =dbAdapter_comprob_cobro.createComprobCobros(id_establec, id_comprob, id_plan_pago, id_plan_pago_detalle, desc_tipo_doc, doc, fecha_programada, Double.parseDouble(monto_a_pagar), fecha_cobro, hora_cobro, monto_cobrado, estado_cobro, id_agente, id_forma_cobro, lugar_registro, liquidacion, idComprobante, 4);
                                        Log.d("JSON CREDITOINSERTO",""+l);
                                        if(l >0){
                                            Log.d("JSON CREDITOINSERTO","-"+id_plan_pago+"-"+id_plan_pago_detalle);
                                            JSONObject jsonObject1 = api.CreatePlanPagoDetalleExp(id_plan_pago, getDatePhone() , Double.parseDouble(montocredito),idUsuario, fecha_programada);
                                            Log.d("JSON CREDITO",""+jsonObject1.toString());
                                        }



                                    }
                                }


                            }

                        }
                    }

                }


                if (isSuccesfulImport(jsonObject)) {

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
                }


            }
            publishProgress("" + 80);

        } catch (Exception e) {
            Log.e("Error",""+e.getMessage());

        }
        return null;
    }
    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
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

            progressDialog.setProgress(100);
            dismissProgressDialog();
            Toast.makeText(mainActivity.getApplicationContext(), "CRÉDITOS IMPORTADOS", Toast.LENGTH_LONG).show();

        }
        super.onPostExecute(s);
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
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
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

}
