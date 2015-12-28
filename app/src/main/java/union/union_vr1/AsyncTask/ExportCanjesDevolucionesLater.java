package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
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
public class ExportCanjesDevolucionesLater extends AsyncTask<String, String, String> {
    private DBAdapter_Temp_Canjes_Devoluciones dbAdapter_temp_canjes_devoluciones;
    private DbAdapter_Temp_Session dbAdapter_temp_session;
    private Context CONTEXT;
    //private Activity activity;
    private String ESTABLEC;
    private int AGENTE;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }



    public ExportCanjesDevolucionesLater(Context context) {
        CONTEXT = context;
        //activity = acti;
        dbAdapter_temp_canjes_devoluciones = new DBAdapter_Temp_Canjes_Devoluciones(context);
        dbAdapter_temp_canjes_devoluciones.open();
        dbAdapter_temp_session = new DbAdapter_Temp_Session(context);
        dbAdapter_temp_session.open();
    }

    @Override
    protected String doInBackground(String... strings) {

        StockAgenteRestApi api = new StockAgenteRestApi(CONTEXT);
        int idAgente = dbAdapter_temp_session.fetchVarible(1);
        String fecha = strings[0];
        String establec = strings[1];
        String ESTADO_SYNS = strings[2];
        Log.d("OPERACIONESCABECERA", "" + ESTADO_SYNS);
        ESTABLEC = establec;
        AGENTE = idAgente;
        //Conseguir Canjes y Devoluciones para poder cambiar el id de canjes.
        dbAdapter_temp_canjes_devoluciones.updateIdCabecera(establec,ESTADO_SYNS);
        //CONSEGUIR DATOS PARA LA CABECERA DE OPERACIONES
        //creado cuando hay internet
        //Actualizado cuando exporta despues
        String[] getCabeceraOperacion = dbAdapter_temp_canjes_devoluciones.getInfoCabeceraOperacion(establec,ESTADO_SYNS);
        Log.d("OPERACIONESCABECERA", "" + getCabeceraOperacion[0] + "-" + getCabeceraOperacion[1] + "-" + getCabeceraOperacion[2] + "-" + getCabeceraOperacion[3]);
        Cursor operacionesDetalle = dbAdapter_temp_canjes_devoluciones.getAllOperacion(getCabeceraOperacion[3],ESTADO_SYNS);
        Cursor operacionesDetalleForUpdate = dbAdapter_temp_canjes_devoluciones.getAllOperacion(getCabeceraOperacion[3],ESTADO_SYNS);
        Cursor operacionesDetalleForUpdateFinish = dbAdapter_temp_canjes_devoluciones.getAllOperacion(getCabeceraOperacion[3],ESTADO_SYNS);


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
            Log.d("ERROR NOSE", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        /*if ((activity.isFinishing())) {
            //dismissProgressDialog();

            return;
        } else {


            Toast.makeText(CONTEXT, "EXPORTANDO... CD", Toast.LENGTH_LONG).show();


        }*/


        super.onPostExecute(s);
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