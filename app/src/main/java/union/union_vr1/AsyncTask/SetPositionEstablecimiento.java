package union.union_vr1.AsyncTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Cliente_Ruta;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;

/**
 * Created by Kelvin on 27/08/2015.
 */
public class SetPositionEstablecimiento extends AsyncTask<String, String, String> {

    private Context context;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private StockAgenteRestApi stockAgenteRestApi;

    private int idEstablecimiento;


    public SetPositionEstablecimiento(Context ctx) {
        context = ctx;
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(context);
        dbAdaptert_evento_establec.open();

    }

    @Override
    protected String doInBackground(String... strings) {
        stockAgenteRestApi = new StockAgenteRestApi(context);

        try {
            JSONObject jsonObject = stockAgenteRestApi.fupd_LocalizacionEstablecimiento(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),strings[2],strings[3]);
            Log.d("EXPORTLATLONG",""+jsonObject.toString());
            if(Utils.isSuccesful(jsonObject) && Utils.validateRespuesta(jsonObject)){
                long s = dbAdaptert_evento_establec.updateEstabLatLong(Integer.parseInt(strings[1]), strings[2], strings[3]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }




}
