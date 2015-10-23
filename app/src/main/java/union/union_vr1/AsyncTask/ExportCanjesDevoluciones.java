package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.DBAdapter_Temp_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;

/**
 * Created by Kelvin on 22/10/2015.
 */
public class ExportCanjesDevoluciones extends AsyncTask<String, String, String> {
    private DBAdapter_Temp_Canjes_Devoluciones dbAdapter_temp_canjes_devoluciones;
    private DbAdapter_Temp_Session dbAdapter_temp_session;
    private Context CONTEXT;
    private Activity activity;
    private String ESTABLEC;
    private int AGENTE;
    private ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        createProgressDialog();
    }

    public void createProgressDialog() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Exportando...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public ExportCanjesDevoluciones(Context context,Activity  acti) {
        CONTEXT = context;
        activity = acti;
        dbAdapter_temp_canjes_devoluciones = new DBAdapter_Temp_Canjes_Devoluciones(context);
        dbAdapter_temp_canjes_devoluciones.open();
        dbAdapter_temp_session = new DbAdapter_Temp_Session(context);
        dbAdapter_temp_session.open();
    }

    @Override
    protected String doInBackground(String... strings) {
        publishProgress("" + 30);
        StockAgenteRestApi api = new StockAgenteRestApi(CONTEXT);
        int idAgente = dbAdapter_temp_session.fetchVarible(1);
        String fecha = strings[0];
        String establec = strings[1];
        ESTABLEC = establec;
        AGENTE = idAgente;
        //Conseguir Canjes y Devoluciones para poder cambiar el id de canjes.
        dbAdapter_temp_canjes_devoluciones.updateIdCabecera(establec);
        //CONSEGUIR DATOS PARA LA CABECERA DE OPERACIONES
        String[] getCabeceraOperacion = dbAdapter_temp_canjes_devoluciones.getInfoCabeceraOperacion(establec);
        Log.d("OPERACIONESCABECERA", "" + getCabeceraOperacion[0] + "-" + getCabeceraOperacion[1] + "-" + getCabeceraOperacion[2] + "-" + getCabeceraOperacion[3]);
        Cursor operacionesDetalle = dbAdapter_temp_canjes_devoluciones.getAllOperacion(getCabeceraOperacion[3]);
        Cursor operacionesDetalleForUpdate = dbAdapter_temp_canjes_devoluciones.getAllOperacion(getCabeceraOperacion[3]);
        Cursor operacionesDetalleForUpdateFinish = dbAdapter_temp_canjes_devoluciones.getAllOperacion(getCabeceraOperacion[3]);

        publishProgress("" + 50);
        //Enviar datos de la cabecera
        JSONObject jsonObject = null;
        try {
            jsonObject = api.CreateHeaderDevoluciones(
                    0,
                    1,
                    idAgente,
                    2,
                    Double.parseDouble(getCabeceraOperacion[1]),
                    getCabeceraOperacion[3]
            );
            Log.d("EXPORT HV CREATED", jsonObject.toString());
            int idGuia = parserIdGuiaRetornado(jsonObject);

            while (operacionesDetalleForUpdate.moveToNext()) {
                Log.d("Actualizo", idGuia + "");
                int estadoUpdateGuia = dbAdapter_temp_canjes_devoluciones.updateIdDetalle(operacionesDetalleForUpdate.getString(operacionesDetalleForUpdate.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_canjes_devoluciones)), idGuia);
                Log.d("Actualizo", estadoUpdateGuia + "");
            }
            JSONObject jsonObjectDetalle = null;
            publishProgress("" + 70);
            Log.d("ESTADO DEL CAMPO", "" + idGuia);
            while (operacionesDetalle.moveToNext()) {

                jsonObjectDetalle = api.CreateDetalleOperacionesCD(operacionesDetalle.getInt(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_producto)),
                        operacionesDetalle.getInt(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_estado_producto)),
                        operacionesDetalle.getInt(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_motivo)),
                        operacionesDetalle.getInt(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_cantidad)),
                        1,
                        idAgente,
                        operacionesDetalle.getString(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_guia)),
                        operacionesDetalle.getString(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_comprob)),
                        operacionesDetalle.getString(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_lote)),
                        operacionesDetalle.getInt(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_liquidacion)),
                        operacionesDetalle.getDouble(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_importe)),
                        operacionesDetalle.getInt(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_comprob_venta)),// Editar por comprobante de venta
                        operacionesDetalle.getInt(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_forma)),//Agregar
                        operacionesDetalle.getInt(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_unidad)),//Agregar
                        operacionesDetalle.getInt(operacionesDetalle.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_establecimiento))

                );

                Log.d("DETALLE", jsonObjectDetalle.toString());

                publishProgress("" + 100);
            }
            if (isSuccesfulExport(jsonObjectDetalle)) {
                Log.d("TERMINO BIEN", jsonObjectDetalle.toString());
                while (operacionesDetalleForUpdateFinish.moveToNext()) {
                    Log.d("ACTUALIZO",  "BIENN");
                    int estadoEstado = dbAdapter_temp_canjes_devoluciones.updateEstadoExport(operacionesDetalleForUpdateFinish.getString(operacionesDetalleForUpdateFinish.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_canjes_devoluciones)));
                    Log.d("ACTUALIZO", estadoEstado + "");
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(Integer.parseInt(values[0]));
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if ((activity.isFinishing())) {
            //dismissProgressDialog();
            progressDialog.dismiss();
            return;
        } else {
            progressDialog.setProgress(100);
            dismissProgressDialog();
            Toast.makeText(CONTEXT, "EXPORTACIÓN EXITOSA", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(activity, activity.getClass());
            intent.putExtra("idEstabX",ESTABLEC);
            intent.putExtra("idAgente",AGENTE);
            activity.finish();
            activity.startActivity(intent);
        }


        super.onPostExecute(s);
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    public int parserIdGuiaRetornado(JSONObject jsonObj) {
        int idGuia = -1;
        try {
            Log.d("CADENA A PARSEAR ", jsonObj.toString());
            idGuia = jsonObj.getInt("Value");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONPARSER ERROR", e.getMessage());
        }
        return idGuia;
    }

    public boolean isSuccesfulExport(JSONObject jsonObj) {
        boolean succesful = false;
        try {
            Log.d("CADENA JSON ", jsonObj.toString());
            succesful = jsonObj.getBoolean("Successful");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSON PARSER ERROR", e.getMessage());
        }
        return succesful;
    }
}
