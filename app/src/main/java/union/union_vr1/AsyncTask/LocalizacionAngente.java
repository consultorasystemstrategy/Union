package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.DBAdapter_Temp_Inventario;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Vistas.VMovil_Cargar_Inventario;

/**
 * Created by Kelvin on 27/08/2015.
 */
public class LocalizacionAngente extends AsyncTask<String, String, String> {
    Context context;

    JSONObject jsonObject = null;

    public LocalizacionAngente(Context ctx) {

        context = ctx;
    }

    @Override
    protected String doInBackground(String... strings) {

        StockAgenteRestApi api = new StockAgenteRestApi(context);
        try {
            publishProgress("" + 25);

            String idAgente = strings[0];
            String latitud = strings[1];
            String longitud = strings[2];

            Log.d("LATLONG", idAgente + " - ");


            jsonObject = api.InsLocalizacionAgente(Integer.parseInt(idAgente), latitud, longitud);
            Log.d("LATLONG", jsonObject.toString());

            publishProgress("" + 50);

        } catch (Exception e) {
            Log.d("LATLONGERROR : ", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("TERMINO", "" + s);

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

    }


}
