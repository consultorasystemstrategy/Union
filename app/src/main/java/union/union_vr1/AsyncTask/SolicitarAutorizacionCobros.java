package union.union_vr1.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Utils.Utils;

/**
 * Created by Kelvin on 21/09/2015.
 */
public class SolicitarAutorizacionCobros extends AsyncTask<String, String, String> {
    private int actualizacion;
    private Context context;
    private DbAdapter_Comprob_Cobro dbAdapter_comprob_cobro;

    public SolicitarAutorizacionCobros(Context ctx) {
        dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(context);
        dbAdapter_comprob_cobro.open();
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
            String idComprobante = strings[8];
            String nombreEstablec = strings[9];
            String idActualizar = strings[10];

            Log.d("DATOS DE ARRAY", "" + strings.toString());


            jsonObject = api.CreateSolicitudAutorizacionCreditoExp(idAgente, motivoSolicitud, idEstablec, estadoSolicitud, referencia, idComprobante, idAgente, aPagar, 0);
            Log.d("LATLONG", jsonObject.toString());

            if (Utils.isSuccesful(jsonObject) && Utils.validateRespuesta(jsonObject)) {
                int estado = jsonObject.getInt("");
                if (estado > 0) {
                    actualizacion = dbAdapter_comprob_cobro.updateComprobCobrosExport(idActualizar);
                    Log.d("ACTUALIZO",""+actualizacion);
                }
            }

            publishProgress("" + 50);

        } catch (Exception e) {
            Log.d("LATLONGERROR : ", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(actualizacion<0){
            Utils.setToast(context, "Solicitud enviada correctamente", R.color.rojo);
        }else{
            Utils.setToast(context,"Solicitud enviada correctamente", R.color.verde);
        }

        super.onPostExecute(s);
    }
}
