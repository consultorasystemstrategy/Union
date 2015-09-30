package union.union_vr1.MySQL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import union.union_vr1.Conexion.JSONParser;
import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Histo_Comprob_Anterior;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Vistas.VMovil_Evento_Indice;
import union.union_vr1.Vistas.VMovil_Online_Pumovil;

public class DbManager_Agente_GET extends Activity {

    //Aqui se se inicializan las variables para manejar los metodos de base de datos sqlite
    private DbAdapter_Agente manager;
    private DbAdaptert_Evento_Establec managerEstablecimiento;
    private DbAdapter_Comprob_Cobro managerCobro;
    private DbAdapter_Histo_Comprob_Anterior managerComprobAnterior;


    //Aqui se inicializa las variables globales para mandar por GET
    private ProgressDialog progressDialog;
    JSONParser jsonParser = new JSONParser();
    JSONParser jsonParserEstablec = new JSONParser();
    JSONParser jsonParserCobro = new JSONParser();
    JSONParser jsonParserComprobAnt = new JSONParser();
    private static String url_agente = "http://192.168.0.158:8083/produnion/lis_agente.php";
    private static String url_cobro = "http://192.168.0.158:8083/produnion/lis_hist_cobro.php";
    private static String url_establec = "http://192.168.0.158:8083/produnion/lis_establec.php";
    private static String url_hist_comprob_ant = "http://192.168.0.158:8083/produnion/lis_hist_comprob_ant.php";
    private static final String TAG_SUCCESS = "success";
    JSONArray agente = null;
    JSONArray establecimientos = null;
    JSONArray cobros=null;
    JSONArray comprob_anterior = null;
    public int i;
    public int k;
    public int c;
    public int a;

    //Aqui se inicializa las variables para Agente de Venta
    private static final String TAG_agente = "agente_venta";
    private static final String TAG_id_agente_venta = "id_agente_venta";
    private static final String TAG_id_liquidacion = "id_liquidacion";
    private static final String TAG_km_inicial = "km_inicial";
    private static final String TAG_km_final = "km_final";
    private static final String TAG_nombre_ruta = "nombre_ruta";
    private static final String TAG_nro_bodegas = "nro_bodegas";
    private static final String TAG_serie_boleta = "serie_boleta";
    private static final String TAG_serie_factura = "serie_factura";
    private static final String TAG_serie_rrpp = "serie_rrpp";
    private static final String TAG_correlativo_boleta = "correlativo_boleta";
    private static final String TAG_correlativo_factura = "correlativo_factura";
    private static final String TAG_correlativo_rrpp = "correlativo_rrpp";


    //Aqui se inicializa las variables para evento establecimiento
    private static final String TAG_EventoEstablec = "establecimientos";
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

    //Aqui se inicializan las variables para el historial de cobros
    public static final String TAG_historial_cobros = "hist_cobros";
    public static final String TAG_id_establec_cobro = "id_establec";
    public static final String TAG_id_comprobante_cobro = "cc_in_id_comprobante_cobro";
    public static final String TAG_id_comprob = "id_comprob";
    public static final String TAG_id_plan_pago = "id_plan_pago";
    public static final String TAG_id_plan_pago_detalle = "id_plan_pago_detalle";
    public static final String TAG_desc_tipo_doc = "desc_tipo_doc";
    public static final String TAG_doc = "doc";
    public static final String TAG_fecha_programada = "fecha_programada";
    public static final String TAG_monto_a_pagar = "monto_a_pagar";
    public static final String TAG_fecha_cobro = "fecha_cobro";
    public static final String TAG_hora_cobro = "hora_cobro";
    public static final String TAG_monto_cobrado = "monto_cobrado";
    public static final String TAG_estado_cobro = "estado_cobro";
    public static final String TAG_id_agente_cobro = "id_agente";
    public static final String TAG_id_forma_cobro = "id_forma_cobro";
    public static final String TAG_lugar_registro = "lugar_registro";

    //Aqui se inicializa las variables para el historial del ultimo comprobante
    public static final String TAG_comprob_anterior = "hist_comp_ant";
    public static final String TAG_id_establec_comp_ant = "id_establec";
    public static final String TAG_id_producto = "id_producto";
    public static final String TAG_nom_producto = "nom_producto";
    public static final String TAG_cantidad = "cantidad";
    public static final String TAG_prom_anterior = "prom_anterior";
    public static final String TAG_devuelto = "devuelto";
    public static final String TAG_valor_unidad = "valor_unidad";
    public static final String TAG_id_agente_comp_ant = "id_agente";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_json_agente);

        manager = new DbAdapter_Agente(this);
        manager.open();

        managerEstablecimiento = new DbAdaptert_Evento_Establec(this);
        managerEstablecimiento.open();
        managerEstablecimiento.deleteAllEstablecs();

        managerCobro = new DbAdapter_Comprob_Cobro(this);
        managerCobro.open();
        managerCobro.deleteAllComprobCobros();

        managerComprobAnterior = new DbAdapter_Histo_Comprob_Anterior(this);
        managerComprobAnterior.open();
        managerComprobAnterior.deleteAllHistoComprobAnterior();





        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_json_agente);
        new LoadUpdateAgente().execute();
        new LoadInsertEstablec().execute();
        new LoadInsertHistCobros().execute();
        new LoadInsertHistCompAnterior().execute();
        //new DbManager_Stock_Agente_GET().doInBackground();
    }

    class LoadUpdateAgente extends AsyncTask<String,String,String>{
        protected String doInBackground(String... args) {
            Bundle bundle = getIntent().getExtras();
            String id_agente_venta = bundle.getString("putIdAgenteVenta");
            //Aqui se lista los parametros a enviar al PHP
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_agente_venta", id_agente_venta));
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_agente, "GET", params);
            //Impresion en consola de prueba para verificar si se recibió algún dato del PHP
            Log.d("agente de venta: ", json.toString());

            try {
                //Aqui se verifica si se envio recibio los datos correctamente
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Aqui se inserta la lista de datos en un JSON array
                    agente = json.getJSONArray(TAG_agente);


                    //Se recorre el array del agente
                    for (i = 0; i < agente.length(); i++) {
                        JSONObject c = agente.getJSONObject(i);

                        //Aqui se hace uso de las variables recibidas para cualquier metodo de base de datos
                        manager.updateAgenteBefore(
                                c.getString(TAG_id_agente_venta),
                                c.getString(TAG_id_liquidacion),
                                c.getString(TAG_km_inicial),
                                c.getString(TAG_km_final),
                                c.getString(TAG_nombre_ruta),
                                c.getString(TAG_nro_bodegas),
                                c.getString(TAG_serie_boleta),
                                c.getString(TAG_serie_factura),
                                c.getString(TAG_serie_rrpp),
                                c.getString(TAG_correlativo_boleta),
                                c.getString(TAG_correlativo_factura),
                                c.getString(TAG_correlativo_rrpp)
                        );
                    }

                } else {
                    Log.d("Fallo Sincronizacion!", json.getString(TAG_SUCCESS));
                    return json.getString(TAG_SUCCESS);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    class LoadInsertEstablec extends AsyncTask<String,String,String>{

        protected String doInBackground(String... args) {
            Bundle bundle = getIntent().getExtras();
            String id_agente_venta = bundle.getString("putIdAgenteVenta");
            //Aqui se lista los parametros a enviar al PHP
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_agente_venta", id_agente_venta));
            // getting JSON string from URL
            JSONObject jsonEstablec = jsonParserEstablec.makeHttpRequest(url_establec, "GET", params);
            //Impresion en consola de prueba para verificar si se recibió algún dato del PHP
            Log.d("establecimientos: ", jsonEstablec.toString());

            try {
                //Aqui se verifica si se envio recibio los datos correctamente
                int success = jsonEstablec.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Aqui se inserta la lista de datos en un JSON array
                    establecimientos = jsonEstablec.getJSONArray(TAG_EventoEstablec);

                    //Se recorre el array de los establecimientos
                    for (k = 0; k < establecimientos.length(); k++) {
                        JSONObject ce = establecimientos.getJSONObject(k);

                        //Aqui se hace uso de las variables recibidas para cualquier metodo de base de datos
                        managerEstablecimiento.createEstablecs(
                                ce.getInt(TAG_id_establec),
                                ce.getInt(TAG_id_cat_est),
                                ce.getInt(TAG_id_tipo_doc_cliente),
                                ce.getInt(TAG_id_estado_atencion),
                                ce.getString(TAG_nom_establec),
                                ce.getString(TAG_nom_cliente),
                                ce.getString(TAG_doc_cliente),
                                ce.getInt(TAG_orden),
                                ce.getInt(TAG_surtido_stock_ant),
                                ce.getInt(TAG_surtido_venta_ant),
                                ce.getDouble(TAG_monto_credito),
                                ce.getInt(TAG_dias_credito),
                                ce.getInt(TAG_estado_no_atencion),
                                ce.getInt(TAG_id_agente),
                                1
                        );
                    }


                } else {
                    Log.d("Fallo Sincronizacion!", jsonEstablec.getString(TAG_SUCCESS));
                    return jsonEstablec.getString(TAG_SUCCESS);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    class LoadInsertHistCobros extends AsyncTask<String,String,String>{

        protected String doInBackground(String... args) {
            Bundle bundle = getIntent().getExtras();
            String id_agente_venta = bundle.getString("putIdAgenteVenta");
            //Aqui se lista los parametros a enviar al PHP
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_agente_venta", id_agente_venta));
            // getting JSON string from URL
            JSONObject jsonCobro = jsonParserCobro.makeHttpRequest(url_cobro, "GET", params);
            //Impresion en consola de prueba para verificar si se recibió algún dato del PHP
            Log.d("cobros: ", jsonCobro.toString());

            try {
                //Aqui se verifica si se envio recibio los datos correctamente
                int success = jsonCobro.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Aqui se inserta la lista de datos en un JSON array
                    cobros = jsonCobro.getJSONArray(TAG_historial_cobros);


                    //Se recorre el array del agente
                    for (c = 0; c < cobros.length(); c++) {
                        JSONObject co = cobros.getJSONObject(c);

                        //Aqui se hace uso de las variables recibidas para cualquier metodo de base de datos
                        managerCobro.createComprobCobros(
                                co.getInt(TAG_id_establec_cobro),
                                co.getInt(TAG_id_comprob),
                                co.getInt(TAG_id_plan_pago),
                                co.getInt(TAG_id_plan_pago_detalle),
                                co.getString(TAG_desc_tipo_doc),
                                co.getString(TAG_doc),
                                co.getString(TAG_fecha_programada),
                                co.getDouble(TAG_monto_a_pagar),
                                co.getString(TAG_fecha_cobro),
                                co.getString(TAG_hora_cobro),
                                co.getDouble(TAG_monto_cobrado),
                                co.getInt(TAG_estado_cobro),
                                co.getInt(TAG_id_agente_cobro),
                                co.getInt(TAG_id_forma_cobro),
                                co.getString(TAG_lugar_registro),
                                0,
                                co.getString(TAG_id_comprobante_cobro),
                                0
                        );
                    }

                } else {
                    Log.d("Fallo Sincronizacion!", jsonCobro.getString(TAG_SUCCESS));
                    return jsonCobro.getString(TAG_SUCCESS);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    class LoadInsertHistCompAnterior extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(DbManager_Agente_GET.this);
            progressDialog.setMessage("Importando Historial de Establecimientos de Sistema On line...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
        protected String doInBackground(String... args) {
            Bundle bundle = getIntent().getExtras();
            String id_agente_venta = bundle.getString("putIdAgenteVenta");
            //Aqui se lista los parametros a enviar al PHP
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_agente_venta", id_agente_venta));
            // getting JSON string from URL
            JSONObject jsonCompAnt = jsonParserComprobAnt.makeHttpRequest(url_hist_comprob_ant, "GET", params);
            //Impresion en consola de prueba para verificar si se recibió algún dato del PHP
            Log.d("Comprobante anterior: ", jsonCompAnt.toString());

            try {
                //Aqui se verifica si se envio recibio los datos correctamente
                int success = jsonCompAnt.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Aqui se inserta la lista de datos en un JSON array
                    comprob_anterior = jsonCompAnt.getJSONArray(TAG_comprob_anterior);


                    //Se recorre el array del agente
                    for (c = 0; c < comprob_anterior.length(); c++) {
                        JSONObject co = comprob_anterior.getJSONObject(c);

                        //Aqui se hace uso de las variables recibidas para cualquier metodo de base de datos
                        managerComprobAnterior.createHistoComprobAnterior(
                                co.getInt(TAG_id_establec_comp_ant),
                                co.getInt(TAG_id_producto),
                                co.getString(TAG_nom_producto),
                                co.getInt(TAG_cantidad),
                                co.getString(TAG_prom_anterior),
                                co.getString(TAG_devuelto),
                                co.getInt(TAG_valor_unidad),
                                co.getInt(TAG_id_agente_comp_ant)
                        );
                    }

                } else {
                    Log.d("Fallo Sincronizacion!", jsonCompAnt.getString(TAG_SUCCESS));
                    return jsonCompAnt.getString(TAG_SUCCESS);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String file_url) {
            progressDialog.dismiss();
            final Button btnSgt = (Button)findViewById(R.id.JS_AG_BTN_sig);
            btnSgt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),VMovil_Evento_Indice.class);
                    finish();
                    startActivity(intent);
                }
            });
        }

    }
}
