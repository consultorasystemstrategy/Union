package union.union_vr1.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Cliente_Ruta;

/**
 * Created by Kelvin on 27/08/2015.
 */
public class GetClienteRuta extends AsyncTask<String, String, String> {
    private StockAgenteRestApi stockAgenteRestApi = null;
    private JSONObject jsonobject = null;
    private DBAdapter_Cliente_Ruta dbAdapter_cliente_ruta;
    private Context context;

    public GetClienteRuta(Context ctx) {
        context = ctx;
        Log.d("OBJECT", "DATO1");
    }

    @Override
    protected String doInBackground(String... strings) {
        stockAgenteRestApi = new StockAgenteRestApi(context);
        dbAdapter_cliente_ruta = new DBAdapter_Cliente_Ruta(context);
        dbAdapter_cliente_ruta.open();
        dbAdapter_cliente_ruta.truncateClienteRuta();
        try {
            jsonobject = stockAgenteRestApi.getClientesRuta(Integer.parseInt(strings[0]));
            Log.d("OBJECT-CLIENTES", "" + jsonobject.toString());
            parseClienteRuta(jsonobject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }

    private void parseClienteRuta(JSONObject jsonOCliente) {
        try {
            JSONArray jsonArray = jsonOCliente.getJSONArray("Value");
            JSONObject jsonObjectCliente = null;
            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObjectCliente = jsonArray.getJSONObject(i);



                    long inserto = dbAdapter_cliente_ruta.createClienteRuta(
                            jsonObjectCliente.getInt("CliIClienteId"),
                            jsonObjectCliente.getString("PerVNombres"),
                            jsonObjectCliente.getString("PerVApellPaterno"),
                            jsonObjectCliente.getString("PerVApellMaterno"),
                            jsonObjectCliente.getString("PerVDocIdentidad"),
                            jsonObjectCliente.getInt("PerITipoDocIdentidadId"),
                            jsonObjectCliente.getInt("PerITipoPersonaId"),
                            jsonObjectCliente.getString("PerVCelular"),
                            jsonObjectCliente.getString("PerVEmail"),
                            jsonObjectCliente.getString("PerVCodigoERP"),
                            jsonObjectCliente.getInt("PerIEmpresaId"),
                            jsonObjectCliente.getInt("RutIRutaId"),
                            Constants._CREADO);

                    if (inserto > 0) {
                        Log.d("INSERTO-data", "" + inserto);

                    }



            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("INSERTO-data", "" + e.getMessage());
        }
    }
}
