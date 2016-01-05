package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Temp_Inventario;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;

/**
 * Created by Kelvin on 08/12/2015.
 */
public class ModificarEstablecimiento extends AsyncTask<String, String, String> {
    private Activity mainActivity;
    private JSONObject jsonObjectCreated = null;
    private StockAgenteRestApi stockAgenteRestApi = null;
    private DbAdapter_Temp_Establecimiento dbAdapter_temp_establecimiento;
    private DbAdapter_Temp_Session dbAdapter_temp_session;


    public ModificarEstablecimiento(Activity activity) {
        mainActivity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        stockAgenteRestApi = new StockAgenteRestApi(mainActivity);
        dbAdapter_temp_establecimiento = new DbAdapter_Temp_Establecimiento(mainActivity);
        dbAdapter_temp_establecimiento.open();
        dbAdapter_temp_session = new DbAdapter_Temp_Session(mainActivity);
        dbAdapter_temp_session.open();

        Cursor cursor = dbAdapter_temp_establecimiento.fetchTemEstablecEdit();
        try {
            int idAgente = dbAdapter_temp_session.fetchVarible(1);
            int idLiquidacion = dbAdapter_temp_session.fetchVarible(3);
            while (cursor.moveToNext()) {


                jsonObjectCreated = stockAgenteRestApi.fupd_ClienteEstablecimiento(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_id_remoto)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nombres)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_apPaterno)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_apMaterno)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nro_documento)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_one)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_correo)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_documento)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_persona)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion)),
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
