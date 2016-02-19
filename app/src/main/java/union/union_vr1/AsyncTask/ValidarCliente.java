package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.Fragments.FClienteRegistrar;
import union.union_vr1.Fragments.FMapaRegistrar;
import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;

/**
 * Created by Kelvin on 28/08/2015.
 */
public class ValidarCliente extends AsyncTask<String, String, String> {

    private Activity mainActivity;
    private Fragment fragment;
    private ProgressDialog progressDialog;
    private StockAgenteRestApi stockAgenteRestApi = null;
    private JSONObject jsonObject = null;
    private JSONArray jsonArrayCliente = null;
    private String TAG = "VALIDARCLIENTE";
    private int lengthDocIdentidad;


    public ValidarCliente(Activity mainActivity, Fragment fragment) {
        this.mainActivity = mainActivity;
        this.fragment = fragment;

    }

    @Override
    protected String doInBackground(String... strings) {
        publishProgress("" + 10);
        stockAgenteRestApi = new StockAgenteRestApi(mainActivity);

        try {
            lengthDocIdentidad = (strings[0]).length();
            jsonObject = stockAgenteRestApi.validarClienteExistente(strings[0]);
            Log.d(TAG, "" + stockAgenteRestApi.toString());
            if (Utils.isSuccesful(jsonObject)) {
                jsonArrayCliente = jsonObject.getJSONArray("Value");
                Log.d("DATOS", "" + jsonArrayCliente.toString());
            }


        } catch (Exception e) {

            Log.d(TAG + "ERROR", "" + e.getMessage() + "" + jsonObject.toString());
        }

        publishProgress("" + 80);


        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        createProgressDialog();
    }

    @Override
    protected void onPostExecute(String s) {

        if (Utils.isSuccesful(jsonObject)) {


            if (jsonArrayCliente.length() > 0) {
                setHideWidgets();

                dismissProgressDialog();
            } else {
                setShowWidgets();
                dismissProgressDialog();
            }


        }


        super.onPostExecute(s);

    }

    private void setHideWidgets() {
//        EditText dirFiscal = (EditText) new FMapaRegistrar().getView().findViewById(R.id.map_direccion_fiscal);
        EditText nom = (EditText) fragment.getView().findViewById(R.id.editNombre);
        EditText apPater = (EditText) fragment.getView().findViewById(R.id.editApPaterno);
        EditText apMater = (EditText) fragment.getView().findViewById(R.id.editApMaterno);
        EditText cel = (EditText) fragment.getView().findViewById(R.id.editNroCelular);
        EditText correro = (EditText) fragment.getView().findViewById(R.id.editCorreo);

        SharedPreferences.Editor editor = mainActivity.getSharedPreferences("DIRECCION_FISCAL", Context.MODE_PRIVATE).edit();

        FClienteRegistrar.estado = false;

        JSONObject jsonValida = null;
        for (int i = 0; i < jsonArrayCliente.length(); i++) {

            try {
                jsonValida = jsonArrayCliente.getJSONObject(0);
                nom.setText(jsonValida.getString("PerVNombres") + "");
                if (lengthDocIdentidad == 8) {
                    apPater.setText(jsonValida.getString("PerVApellPaterno"));
                    apMater.setText(jsonValida.getString("PerVApellMaterno"));
                }


                cel.setText(jsonValida.getString("PerVCelular"));
                correro.setText(jsonValida.getString("PerVEmail"));
                // dirFiscal.setText(jsonValida.getString("DireccionFiscal"));
                editor.putString("fiscal", jsonValida.getString("DireccionFiscal"));


            } catch (JSONException e) {
                Log.d("ERROR", "" + e.getMessage());
                e.printStackTrace();
            }

        }

        editor.commit();

        nom.setEnabled(false);
        apPater.setEnabled(false);
        apMater.setEnabled(false);
        cel.setEnabled(false);
        correro.setEnabled(false);

        Utils.setToast(mainActivity, "Cliente ya Registrado", R.color.rojo);
    }

    private void setShowWidgets() {
        LinearLayout nom = (LinearLayout) fragment.getView().findViewById(R.id.lynom);
        LinearLayout apPater = (LinearLayout) fragment.getView().findViewById(R.id.layoutPaterno);
        LinearLayout apMater = (LinearLayout) fragment.getView().findViewById(R.id.layoutMaterno);
        LinearLayout cel = (LinearLayout) fragment.getView().findViewById(R.id.layoutTel);
        LinearLayout correro = (LinearLayout) fragment.getView().findViewById(R.id.layoutCorreo);
        nom.setVisibility(View.VISIBLE);
        apPater.setVisibility(View.VISIBLE);
        apMater.setVisibility(View.VISIBLE);
        cel.setVisibility(View.VISIBLE);
        correro.setVisibility(View.VISIBLE);
        FClienteRegistrar.estado = true;
        Utils.setToast(mainActivity, "Puede Registrar Cliente", R.color.verde);
    }


    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(Integer.parseInt(values[0]));
    }

    public void createProgressDialog() {
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setMessage("Verficando ...");
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


    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
