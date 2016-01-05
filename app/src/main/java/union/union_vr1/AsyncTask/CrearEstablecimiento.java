package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Establecimeinto_Historial;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Utils.Utils;

/**
 * Created by Kelvin on 08/12/2015.
 */
public class CrearEstablecimiento extends AsyncTask<String, String, String> {
    private Activity mainActivity;
    private JSONObject jsonObjectCreated = null;
    private StockAgenteRestApi stockAgenteRestApi = null;
    private DbAdapter_Temp_Establecimiento dbAdapter_temp_establecimiento;
    private DbAdapter_Establecimeinto_Historial dbAdapter_establecimeinto_historial;
    private DbAdapter_Temp_Session dbAdapter_temp_session;


    public CrearEstablecimiento(Activity activity) {
        mainActivity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        stockAgenteRestApi = new StockAgenteRestApi(mainActivity);
        dbAdapter_temp_establecimiento = new DbAdapter_Temp_Establecimiento(mainActivity);
        dbAdapter_temp_establecimiento.open();
        dbAdapter_establecimeinto_historial = new DbAdapter_Establecimeinto_Historial(mainActivity);
        dbAdapter_establecimeinto_historial.open();
        dbAdapter_temp_session = new DbAdapter_Temp_Session(mainActivity);
        dbAdapter_temp_session.open();
        int ruta = dbAdapter_temp_session.fetchVarible(777);
        Cursor cursor = dbAdapter_temp_establecimiento.fetchTemEstablec();
        try {
            int idAgente = dbAdapter_temp_session.fetchVarible(1);
            int idLiquidacion = dbAdapter_temp_session.fetchVarible(3);
            while (cursor.moveToNext()) {
                Log.d("DATOS ERROR", "" + idLiquidacion + "-" + idAgente + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nombres)) + ""
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_apPaterno))
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_apMaterno))
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nro_documento))
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_one))
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_correo))
                        + "-" + cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_documento))
                        + "-" + cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_persona))
                        + "-" + cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion))
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nro_documento))
                        + "-" + cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_establecimiento))
                        + "-" + cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion))
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_descripcion))
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_telefono_fijo))
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_one))
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_two))
                        + "-" + cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion))
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_descripcion_establecimiento))
                        + "-" + cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion))
                        + "-" + cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_categoria_estable))
                        + "-" + cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_establecimiento))
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_longitud))
                        + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_latitud))
                        + "-" + cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion)));


                jsonObjectCreated = stockAgenteRestApi.InsClienteEstablecimiento(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nombres)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_apPaterno)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_apMaterno)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nro_documento)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_one)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_correo)),
                        1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_documento)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_persona)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion)),
                        1,//Id Empresa con el ususario
                        "554",//codigo
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nro_documento)),
                        1,
                        3,
                        idAgente,//Agente de venta id
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion)),
                        1,//estado de atencion
                        0.0, //Minto de credito
                        5,//modalidad de credito
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_descripcion)),
                        1,
                        0,//iddistrito
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_telefono_fijo)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_one)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_two)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion)),
                        0,//establecimiento asociado
                        0,//Direccion fiscal Id
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_descripcion_establecimiento)),
                        1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion)),
                        0,//porcentaje devolucion
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_categoria_estable)),//TIPO establecimiento
                        "Ninguno",
                        0.0,//Monto de compra
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_establecimiento)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_longitud)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_latitud)),
                        1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion)),
                        1,
                        idLiquidacion,//Liquidacion
                        4,//Motivo no atewncido
                        ruta,//RUTAid
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_direccion_fiscal))

                );

                //----------------------------
                Log.d("JSONJSON CJSON EST", ""+jsonObjectCreated.toString());

                if (isSuccesfulExport(jsonObjectCreated)) {
                    int idRemoto = jsonObjectCreated.getInt("Value");
                   long a = dbAdapter_temp_establecimiento.updateTempEstablecById(cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_id)), idRemoto, Constants._ACTUALIZADO);
                    Log.d("ESTADO",""+a+"--"+idRemoto);
                    if(a>0){
                        long inserto = dbAdapter_establecimeinto_historial.createTempEstablec(
                                cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_id_remoto)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_usuario_accion)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_telefono_fijo)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_one)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_two)),
                                cursor.getDouble(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_latitud)),
                                cursor.getDouble(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_longitud)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_descripcion)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_direccion_fiscal)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_persona)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nombres)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_apPaterno)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_apMaterno)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_documento)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nro_documento)),
                                1,
                                cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_correo)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_establecimiento)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_descripcion_establecimiento)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_categoria_estable)),
                                Constants._ACTUALIZADO,
                                0);

                        Log.d("INSERTO EN HISTORIAL",""+inserto);
                    }
                }

                Log.d("GUARDAR ESTABLECIMIENTO", "" + 1);
            }
            Log.d("GUARDAR ESTABLECIMIENTO", "" + jsonObjectCreated);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("GUARDAR ESTABLECIMIENTO", "" + e.getMessage());
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
        Utils.setToast(mainActivity, "Guardado Correctamente", R.color.verde);
        super.onPostExecute(s);
    }
}
