package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.CursorAdapter_Consultar_Inventario;
import union.union_vr1.Sqlite.DBAdapter_Consultar_Inventario_Anterior;
import union.union_vr1.Sqlite.DBAdapter_Temp_Inventario;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Vistas.VMovil_Cargar_Inventario;

/**
 * Created by Kelvin on 27/08/2015.
 */
public class ConsultarInventarioAnterior extends AsyncTask<String, String, String> {

    private DbAdapter_Temp_Session session;
    private DBAdapter_Consultar_Inventario_Anterior dbAdapter_consultar_inventario_anterior;
    private Activity mainActivity;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject = null;
    private String FECHA;
    private CursorAdapter_Consultar_Inventario cursorAdapter_consultar_inventario;


    public ConsultarInventarioAnterior(Activity mainActivity) {
        this.mainActivity = mainActivity;
        dbAdapter_consultar_inventario_anterior = new DBAdapter_Consultar_Inventario_Anterior(mainActivity);
        dbAdapter_consultar_inventario_anterior.open();
        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();

    }

    @Override
    protected String doInBackground(String... strings) {

        StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);
        try {
            publishProgress("" + 25);

            int idAgente = session.fetchVarible(1);
            String fecha = strings[0];
            FECHA = fecha;
            Log.d("IDAGENTE", idAgente + "" + fecha);
            jsonObject = api.GetInventarioAnterior(idAgente, fecha);
            Log.d("JSON CARGAR INVENTARIO", jsonObject.toString());

            publishProgress("" + 50);

        } catch (Exception e) {
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


        try {
            if (mainActivity.isFinishing()) {
                //dismissProgressDialog();
                progressDialog.dismiss();
                return;
            } else {
                Log.d("JSON CARGAR INVENTARIO", jsonObject.toString());
                //1 si se inserto
                //2 fecha pasada o no existe
                //3 la guia ya se inserto
                //-1 error.
                progressDialog.setProgress(100);


                JSONArray jsonArray = jsonObject.getJSONArray("Value");
                Log.e("JSONERROR", "" + jsonArray.toString());


                JSONObject jsonObj = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObj = jsonArray.getJSONObject(i);
                    String id = jsonObj.getString("codigoProducto");
                    String nombre = jsonObj.getString("nombreProducto");
                    String cantidad = jsonObj.getString("cantidadProducto");
                    Log.d("DATOS", "" + jsonObj.getString("codigoProducto"));
                    dbAdapter_consultar_inventario_anterior.createTempConsultarInventario(id, nombre, cantidad, FECHA);
                }
                Cursor cursor = dbAdapter_consultar_inventario_anterior.getConsultarInventario(FECHA);
               // cursorAdapter_consultar_inventario = new CursorAdapter_Consultar_Inventario(mainActivity.getApplicationContext(), cursor);
               // ListView listView = (ListView) mainActivity.findViewById(R.id.listviewInventario);
               // listView.setAdapter(cursorAdapter_consultar_inventario);
                dismissProgressDialog();


            }

            super.onPostExecute(s);

        } catch (NullPointerException e) {
            Log.e("ERROR", e.getMessage() + "");
        } catch (JSONException e) {
            dismissProgressDialog();
            e.printStackTrace();
            Log.e("ERROR", e.getMessage() + "");
            Toast toast = Toast.makeText(mainActivity.getApplicationContext(), "Inventario No Encontrado", Toast.LENGTH_SHORT);
            toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.rojo));
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
            toast.show();

        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(Integer.parseInt(values[0]));
    }

    public void createProgressDialog() {
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
