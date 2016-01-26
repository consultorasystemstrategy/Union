package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.MainActivity;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Trans_Detallado;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;


/**
 * Created by kike on 15/01/2016.
 */
public class CargarTransDetallado extends AsyncTask<String,String,String> {

    private StockAgenteRestApi stockAgenteRestApi=null;
    private JSONObject jsonobject = null;
    private DBAdapter_Trans_Detallado dbAdapter_trans_detallado;
    private Context context;
    private String GUIA;

    private static String TAG = CargarTransDetallado.class.getSimpleName();

   public CargarTransDetallado(Context ctx) {
       context = ctx;
       Log.d(TAG, "CARGAR");
   }
    @Override
    protected String doInBackground(String... params) {
        stockAgenteRestApi = new StockAgenteRestApi(context);
        dbAdapter_trans_detallado = new DBAdapter_Trans_Detallado(context);
        dbAdapter_trans_detallado.open();
        //dbAdapter_trans_detallado.truncateClienteRuta();
        try {
            String guia = params[0];
            GUIA = guia;
            jsonobject = stockAgenteRestApi.getTransDetalle(guia);
            Log.d(TAG, "OBJECT" + jsonobject.toString());
            parsetransDetallado(jsonobject);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,  "ERROR CLIENTE"+e.getMessage());
        }
        return null;
    }

    private void parsetransDetallado(JSONObject jsonOCliente) {
        try {
            JSONArray jsonArray = jsonOCliente.getJSONArray("Value");
            JSONObject jsonObjectCliente = null;
            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObjectCliente = jsonArray.getJSONObject(i);

                //para identificar el error al enviar por json con log
                Log.d(TAG, "INSERT : "+jsonObjectCliente.toString());

                long inserto = dbAdapter_trans_detallado.createTransDetallado(
                        jsonObjectCliente.getString("ProVCodigo"),
                        jsonObjectCliente.getString("ProVNombre"),
                        jsonObjectCliente.getString("GuitranVNumGuiaFlex"),
                        jsonObjectCliente.getString("GuitranIUsuarioId"),
                        jsonObjectCliente.getString("Cantidad"),
                        jsonObjectCliente.getString("NFecha"),
                        Constants._CREADO);


                    Log.d("INSERTO", "" + inserto);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("INSERTO-data", "" + e.getMessage());
        }
    }
}
