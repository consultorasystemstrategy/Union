package union.union_vr1.MySQL;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import union.union_vr1.Conexion.JSONParser;
import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DbManager_Evento_Establec_GET extends ListActivity {
    private DbAdaptert_Evento_Establec manager;
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> establecsList;

    // url to get all empleados list Reemplaza la IP de tu equipo o la direccion de tu servidor
    // Si tu servidor es tu PC colocar IP Ej: "http://127.97.99.200/taller06oct/..", no colocar "http://localhost/taller06oct/.."
    private static String url_all_establec = "http://192.168.0.158:8083/produnion/lis_establec.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_EventoEstablec = "establecs";
    private static final String TAG_id_evt_establec = "id_evt_establec";
    private static final String TAG_id_establec = "id_establec";
    private static final String TAG_id_cat_est = "id_cat_est";
    private static final String TAG_id_tipo_doc_cliente = "id_tipo_doc_cliente";
    private static final String TAG_id_estado_atencion = "id_estado_atencion";
    private static final String TAG_nom_establec = "nom_establec";
    private static final String TAG_nom_cliente = "nom_cliente";
    private static final String TAG_doc_cliente = "doc_cliente";
    private static final String TAG_orden = "orden";
    private static final String TAG_surtido_stock_ant = "surtido_stock_ant";
    private static final String TAG_surtido_venta_ant = "surtido_venta_ant";
    private static final String TAG_monto_credito = "monto_credito";
    private static final String TAG_dias_credito = "dias_credito";
    private static final String TAG_estado_no_atencion = "estado_no_atencion";
    private static final String TAG_id_agente = "id_agente";

    // empleados JSONArray
    JSONArray empleados = null;

    public int i;

    public int M_id_evt_establec;
    public int M_id_establec;
    public int M_id_cat_est;
    public int M_id_tipo_doc_cliente;
    public int M_id_estado_atencion;
    public String M_nom_establec;
    public String M_nom_cliente;
    public String M_doc_cliente;
    public int M_orden;
    public int M_surtido_stock_ant;
    public int M_surtido_venta_ant;
    public double M_monto_credito;
    public int M_dias_credito;
    public int M_estado_no_atencion;
    public int M_id_agente;

    public String[][] tabla = new String[100][15];

    @Override
    public void onCreate(Bundle savedInstanceState) {

        manager = new DbAdaptert_Evento_Establec(this);
        manager.open();
        manager.deleteAllEstablecs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carga_evento_establec);

        // Hashmap for ListView
        establecsList = new ArrayList<HashMap<String, String>>();
        //manager.deleteAllEstablecs();
        // Loading empleados in Background Thread
        new LoadAllEstablec().execute();
        //sqlite();
    }

    public void sqlite(){
        for (int j = 0; j < i; j++) {
            manager.createEstablecs(
                    Integer.parseInt(tabla[j][2]),
                    Integer.parseInt(tabla[j][3]),
                    Integer.parseInt(tabla[j][4]),
                    Integer.parseInt(tabla[j][5]),
                    tabla[j][6],
                    tabla[j][7],
                    tabla[j][8],
                    Integer.parseInt(tabla[j][9]),
                    Integer.parseInt(tabla[j][10]),
                    Integer.parseInt(tabla[j][11]),
                    Double.parseDouble(tabla[j][12]),
                    Integer.parseInt(tabla[j][13]),
                    Integer.parseInt(tabla[j][14]),
                    Integer.parseInt(tabla[j][15]),
                    1
            );
        }
    }

    /**
     * Background Async Task to Load all Empleado by making HTTP Request
     * */
    class LoadAllEstablec extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DbManager_Evento_Establec_GET.this);
            pDialog.setMessage("Cargando Establecimientos. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All empleados from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_establec, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All empleados: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // empleados found
                    // Getting Array of empleados
                    empleados = json.getJSONArray(TAG_EventoEstablec);

                    // looping through All empleados
                    for (i = 0; i < empleados.length(); i++) {
                        JSONObject c = empleados.getJSONObject(i);

                        // Storing each json item in variable

                        M_id_evt_establec = c.getInt(TAG_id_evt_establec);
                        M_id_establec = c.getInt(TAG_id_establec);
                        M_id_cat_est = c.getInt(TAG_id_cat_est);
                        M_id_tipo_doc_cliente = c.getInt(TAG_id_tipo_doc_cliente);
                        M_id_estado_atencion = c.getInt(TAG_id_estado_atencion);
                        M_nom_establec = c.getString(TAG_nom_establec);
                        M_nom_cliente = c.getString(TAG_nom_cliente);
                        M_doc_cliente = c.getString(TAG_doc_cliente);
                        M_orden = c.getInt(TAG_orden);
                        M_surtido_stock_ant = c.getInt(TAG_surtido_stock_ant);
                        M_surtido_venta_ant = c.getInt(TAG_surtido_venta_ant);
                        M_monto_credito = c.getDouble(TAG_monto_credito);
                        M_dias_credito = c.getInt(TAG_dias_credito);
                        M_estado_no_atencion = c.getInt(TAG_estado_no_atencion);
                        M_id_agente = c.getInt(TAG_id_agente);

                        manager.createEstablecs(
                                M_id_establec,
                                M_id_cat_est,
                                M_id_tipo_doc_cliente,
                                M_id_estado_atencion,
                                M_nom_establec,
                                M_nom_cliente,
                                M_doc_cliente,
                                M_orden,
                                M_surtido_stock_ant,
                                M_surtido_venta_ant,
                                M_monto_credito,
                                M_dias_credito,
                                M_estado_no_atencion,
                                M_id_agente,1);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value

                        map.put(TAG_nom_establec, M_nom_establec);
                        map.put(TAG_nom_cliente, M_nom_cliente);

                        // adding HashList to ArrayList
                        establecsList.add(map);
                    }
                } else {
                    Log.d("Login Failure!", json.getString(TAG_SUCCESS));
                    return json.getString(TAG_SUCCESS);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all empleados
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            DbManager_Evento_Establec_GET.this, establecsList,
                            R.layout.lista_evento_establec, new String[]{TAG_nom_establec,
                            TAG_nom_cliente},
                            new int[]{R.id.cedula, R.id.nombre});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }


}
