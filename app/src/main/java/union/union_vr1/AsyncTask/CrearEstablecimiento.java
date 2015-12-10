package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;

/**
 * Created by Kelvin on 08/12/2015.
 */
public class CrearEstablecimiento extends AsyncTask<String,String,String> {
    private Activity mainActivity;
    private JSONObject jsonObjectCreated= null;
    private StockAgenteRestApi stockAgenteRestApi ;
    private DbAdapter_Temp_Establecimiento dbAdapter_temp_establecimiento;


    public CrearEstablecimiento(Activity activity){
        mainActivity=activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        dbAdapter_temp_establecimiento = new DbAdapter_Temp_Establecimiento(mainActivity);
        dbAdapter_temp_establecimiento.open();
        Cursor cursor = dbAdapter_temp_establecimiento.fetchTemEstablec();
        try {
            while(cursor.moveToNext()){

                jsonObjectCreated = stockAgenteRestApi.InsUpdLocalizacionPrueba(
                  cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nombres)),
                  cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_apPaterno)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_apMaterno)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nro_documento)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_correo)),
                        1,//EstadoPersona
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_documento)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_persona)),
                        1,//Persona Usuario Id
                        "021548254",//ERP Codigo
                        1,//Empresa Id
                        "cliente codigo",//Codigo Cliente
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nro_documento)),
                        1,//EstadoCliente
                        1,//Categoria persona
                        3,//Limite de dias de credito
                        1,// Id Agente de  venta
                        1,//Cliente Usuario id
                        0,//ClienteEstadoAtencion
                        10.0,//Monto de Credito
                        1,//Modalidad de Credito
                        "25/12/2015",//Fecha Limite de Credito
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_descripcion)),
                        1,//direccion estado
                        1,//Distrito Id
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular)),//Cambiasr por Telefono fijo Establecimeinto
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular)),//ca,biar por celular 1 establecimeinto
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular)),//ca,biar por celular 2 establecimeinto
                        1,//Usuario id direccion
                        12,//Establecimeinto aasociado
                        13,//direccion fiscal
                        14, //Ruta id
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_descripcion_establecimiento)),
                        0,//orden
                        1,// estado Establecimiento
                        1,//usuario establecimeinto
                        2,//porcentaje devolucion
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_establecimiento)),//Categoria Establecimiento
                        "45454",//Exhibidor
                        0.0,//Monto de Compra
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_tipo_establecimiento)),//TipoEstablecimiento
                        "",//codigo barra
                        "-7198985",//latitud
                        "4545",//longitud
                        1,//localizacion estado
                        112,//usuario estado,
                        12,//AtencionEstabblecimiento id,
                        54, //Liquidacion caja Id
                        1,//motivo no atendido
                        1 //Ruta id
                        );
                if(isSuccesfulExport(jsonObjectCreated)){
                    dbAdapter_temp_establecimiento.updateTempEstablecById(cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_id)), Constants._ACTUALIZADO);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
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
