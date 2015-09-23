package union.union_vr1.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import union.union_vr1.RestApi.StockAgenteRestApi;

/**
 * Created by Kelvin on 21/09/2015.
 */
public class SolicitarAutorizacionCobros extends AsyncTask<String, String, String> {
    Context context;

    public SolicitarAutorizacionCobros(Context ctx) {
        context = ctx;
    }

    JSONObject jsonObject = null;

    @Override
    protected String doInBackground(String... strings) {
        StockAgenteRestApi api = new StockAgenteRestApi(context);
        try {
            int idAgente = Integer.parseInt(strings[0]);
            String motivoSolicitud = strings[1];
            int estadoSolicitud = Integer.parseInt(strings[2]);
            String referencia = strings[3];
            double aPagar = Double.parseDouble(strings[4]);
            double pagado = Double.parseDouble(strings[5]);
            int idEstablec = Integer.parseInt(strings[6]);
            int sincronizacion = Integer.parseInt(strings[7]);
            int idComprobante = Integer.parseInt(strings[8]);
            String nombreEstablec = strings[9];

            Log.d("DATOS DE ARRAY", "" + strings.toString());


          jsonObject = api.CreateSolicitudAutorizacionCreditoExp(idAgente,motivoSolicitud,idEstablec,estadoSolicitud,referencia,referencia,idAgente,aPagar,0);
            Log.d("LATLONG", jsonObject.toString());

            publishProgress("" + 50);

        } catch (Exception e) {
            Log.d("LATLONGERROR : ", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
