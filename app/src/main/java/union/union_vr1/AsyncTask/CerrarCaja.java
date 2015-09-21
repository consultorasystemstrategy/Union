package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Vistas.VMovil_Online_Pumovil;

/**
 * Created by Usuario on 18/09/2015.
 */
public class CerrarCaja extends AsyncTask<String, String, String> {


    private DbAdapter_Temp_Session session;

    private Activity mainActivity;
    private ProgressDialog progressDialog;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    JSONObject jsonObject = null;
    private int successCerrarCaja = -1;


    public CerrarCaja(Activity mainActivity) {
        this.mainActivity = mainActivity;
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdaptert_evento_establec.open();

        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();
    }

    @Override
    protected String doInBackground(String... strings) {

        StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);
        try {
            publishProgress(""+25);

            int liquidacion = Integer.parseInt(strings[0]);
            double ingresos = Double.parseDouble(strings[1]);
            double gastos = Double.parseDouble(strings[2]);
            double aRendir = Double.parseDouble(strings[3]);
            int kmFinal =Integer.parseInt(strings[4]) ;
            Log.d("DATOS CERRAR CAJA ", ingresos + " - " + gastos + " - " + aRendir + " - " + kmFinal);

            jsonObject = api.UpdCerrarCaja(
                    liquidacion,
                    ingresos,
                    gastos,
                    aRendir,
                    kmFinal
            );

            successCerrarCaja = jsonObject.getInt("Value");
            Log.d("JSON CERRAR CAJA", jsonObject.toString());

            publishProgress(""+50);

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
            //Log.d("JSON CERRAR CAJA", jsonObject.toString());

            progressDialog.setProgress(100);
            dismissProgressDialog();
        }

        super.onPostExecute(s);
        if (successCerrarCaja>=0) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity.getApplicationContext(), "CAJA CERRADA.", Toast.LENGTH_SHORT).show();
                }
            });

            Intent intent = new Intent(mainActivity, VMovil_Online_Pumovil.class);
            mainActivity.finish();
            mainActivity.startActivity(intent);
        }else {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity.getApplicationContext(), "ERROR, INTENTE DE NUEVO.", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(Integer.parseInt(values[0]));
    }

    public void createProgressDialog(){
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setMessage("Solicitando ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
