package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Servicios.ServiceImport;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.CursorAdapterEstablecimientoColor;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;

/**
 * Created by Kelvin on 28/08/2015.
 */
public class ImportEstadoAuEstablec extends AsyncTask<String, String, String> {

    private DbAdapter_Temp_Session session;
    private Activity mainActivity;
    private ProgressDialog progressDialog;
    private int idAgente;
    private int idLiquidacion;


    public ImportEstadoAuEstablec(Activity mainActivity) {
        this.mainActivity = mainActivity;

        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();
        idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);
    }

    @Override
    protected String doInBackground(String... strings) {
        StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);

        int idCliente = Integer.parseInt(strings[0]);
        int idEstablecimiento = Integer.parseInt(strings[1]);

        try {
            JSONObject jsonObject = api.fupd_EstadoClienteEstablecimiento(idCliente,idEstablecimiento,idAgente);
            Log.d("JSON AUTO ESTAB",""+jsonObject.toString());
            if(Utils.isSuccesful(jsonObject)){
                int inserto = jsonObject.getInt("Value");
                if(inserto>0){
                    Intent intent = new Intent(mainActivity, ServiceImport.class);
                    intent.setAction(Constants.ACTION_IMPORT_SERVICE);
                    mainActivity.startService(intent);
                }else{

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        publishProgress("" + 10);

        //ACTUALIZAR EN LA TABLA EVENTO POR ESTABLECIMIENTO

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

            progressDialog.dismiss();
            return;
        } else {

            progressDialog.setProgress(100);

            Utils.setToast(mainActivity,"Actualizando datos", R.color.verde);

        }
        super.onPostExecute(s);

        dismissProgressDialog();
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
