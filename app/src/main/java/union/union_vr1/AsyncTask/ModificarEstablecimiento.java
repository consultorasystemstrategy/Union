package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Servicios.ServiceImport;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Temp_Inventario;
import union.union_vr1.Sqlite.DbAdapter_Establecimeinto_Historial;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Vistas.VMovil_Evento_Indice;
import union.union_vr1.Vistas.VMovil_Menu_Establec;

/**
 * Created by Kelvin on 08/12/2015.
 */
public class ModificarEstablecimiento extends AsyncTask<String, String, String> {
    private Activity mainActivity;
    private ProgressDialog progressDialog;
    private JSONObject jsonObjectCreated = null;
    private StockAgenteRestApi stockAgenteRestApi = null;
    private DbAdapter_Establecimeinto_Historial dbAdapter_temp_establecimiento;
    private DbAdapter_Temp_Session dbAdapter_temp_session;


    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Intent intent = new Intent(mainActivity, ServiceImport.class);
        intent.setAction(Constants.ACTION_IMPORT_SERVICE);
        mainActivity.startService(intent);

    }


    public void createProgressDialog(){
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setMessage("Solicitando ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        createProgressDialog();
    }

    @Override
    protected void onPostExecute(String s) {

        if(mainActivity.isFinishing()){
            //dismissProgressDialog();
            progressDialog.dismiss();
            return;
        }else {


            progressDialog.setProgress(100);
            dismissProgressDialog();
        }

        mainActivity.startActivity(new Intent(mainActivity, VMovil_Evento_Indice.class));
        mainActivity.finish();

        super.onPostExecute(s);

    }

    public ModificarEstablecimiento(Activity activity) {
        mainActivity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        publishProgress(""+25);
        stockAgenteRestApi = new StockAgenteRestApi(mainActivity);
        dbAdapter_temp_establecimiento = new DbAdapter_Establecimeinto_Historial(mainActivity);
        dbAdapter_temp_establecimiento.open();
        dbAdapter_temp_session = new DbAdapter_Temp_Session(mainActivity);
        dbAdapter_temp_session.open();
        Log.d("ESTABLECIMIENTO ID ",""+strings[0]);
        Cursor cursor = dbAdapter_temp_establecimiento.fetchTemEstablecEdit(strings[0]);
        publishProgress(""+55);
        try {
            int idAgente = dbAdapter_temp_session.fetchVarible(1);
            int idLiquidacion = dbAdapter_temp_session.fetchVarible(3);
            while (cursor.moveToNext()) {


                jsonObjectCreated = stockAgenteRestApi.fupd_ClienteEstablecimiento(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_id_remoto)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nombres)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apPaterno)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apMaterno)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nro_documento)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_celular_one)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_correo)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_documento)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_persona)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_usuario_accion)),
                        1,//Id Empresa con el ususario
                        "554",//codigo
                        3,
                        idAgente,//Agente de venta id
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion)),
                     //Minto de credito
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_descripcion)),//modalidad de credito
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_direccion_fiscal)),
                        1,
                        0,//iddistrito
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_telefono_fijo)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_one)),
                        0+"",
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_descripcion_establecimiento)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion)),//establecimiento asociado
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_categoria_estable)),//Direccion fiscal Id
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_establecimiento)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_longitud)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_latitud))

                );
                Log.e("JSON EDIT ESTAB", "" + jsonObjectCreated.toString());
                //----------------------------
                Log.e("JSON EDIT ESTAB", "" + jsonObjectCreated.toString());

                if (isSuccesfulExport(jsonObjectCreated)) {
                    int idRemoto = jsonObjectCreated.getInt("Value");
                }

                Log.e("EDIT ESTAB ESTABLECIMIENTO", "" + 1);
            }
            publishProgress(""+85);
            Log.e("EDIT ESTAB ESTABLECIMIENTO", "" + jsonObjectCreated);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EDIT ESTAB ESTABLECIMIENTO", "" + e.getMessage());
        }
        return null;
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
