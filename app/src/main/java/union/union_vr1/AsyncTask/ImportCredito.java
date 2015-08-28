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

import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;

/**
 * Created by Kelvin on 28/08/2015.
 */
public class ImportCredito extends AsyncTask<String, String, String> {

    private DbAdapter_Temp_Session session;
    private Activity mainActivity;
    private ProgressDialog progressDialog;

    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DBAdapter_Temp_Autorizacion_Cobro dbAdapter_temp_autorizacion_cobro;
    private int idAgente;
    private int idLiquidacion;


    public ImportCredito(Activity mainActivity) {
        this.mainActivity = mainActivity;

        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();

        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdaptert_evento_establec.open();
        dbAdapter_temp_autorizacion_cobro = new DBAdapter_Temp_Autorizacion_Cobro(mainActivity);
        dbAdapter_temp_autorizacion_cobro.open();

        idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);
    }

    @Override
    protected String doInBackground(String... strings) {
        StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);


        publishProgress(""+10);

        //ACTUALIZAR LOS CRÉDITOS DE LOS ESTABLECIMIENTOS

        try {
            Cursor cursorEstablecimiento = dbAdaptert_evento_establec.fetchAllEstablecs();

            publishProgress(""+20);
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
                        Log.d("CLARO PUES CAMPEON", jsonObj.getInt("SolObservacion") + "");
                        int idEstablecimiento1 = jsonObj.getInt("EstIEstablecimientoId");
                        int estadoSolicitud = jsonObj.getInt("SolIEstadoSolicitudId");
                        int idAutorizacionCobro = jsonObj.getInt("SolObservacion");
                        String fechaLimite = jsonObj.getString("CliDTFechaLimiteCredito");


                        boolean exists = dbAdapter_temp_autorizacion_cobro.existeAutorizacionCobro(idAutorizacionCobro);

                        Log.d("EXISTE AC", "" + exists);
                        if (exists) {
                            int isActualizado = dbAdapter_temp_autorizacion_cobro.updateAutorizacionCobro(idAutorizacionCobro, estadoSolicitud, idEstablecimiento1, fechaLimite);
                            Log.d("IMPORT REGISTRO AUTORIZACION COBRO ACTUALIZADO ", "" + isActualizado);

                        }
                    }

                }


                if (isSuccesfulImport(jsonObject)) {

                    JSONArray jsonArray = jsonObject.getJSONArray("Value");
                    JSONObject jsonObj = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObj = jsonArray.getJSONObject(i);
                        int idEstablecimiento1 = jsonObj.getInt("EstIEstablecimientoId");
                        int montoCredito = jsonObj.getInt("SolDOMontoCredito");
                        int diasCredito = jsonObj.getInt("SolIVigenciaCredito");
                        Log.d("IMPORT SOLICITUDES DATOS", idEstablecimiento + " - " + montoCredito + " - " + diasCredito);
                        dbAdaptert_evento_establec.updateEstablecsCredito(idEstablecimiento, montoCredito, diasCredito);
                    }
                }


            }
            publishProgress(""+80);

        }catch (Exception e){

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

}
