
package union.union_vr1.AsyncTask;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Exportacion_Comprobantes;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ExportService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    private static final String TAG = ExportService.class.getSimpleName();

    private DbAdapter_Exportacion_Comprobantes dbAdapter_exportacion_comprobantes;
    StockAgenteRestApi api = null;
    List<String> listIdExportacionFlex = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "SERVICIO EXPORT CREADO...");
        api = new StockAgenteRestApi(getApplicationContext());
        dbAdapter_exportacion_comprobantes = new DbAdapter_Exportacion_Comprobantes(getApplicationContext());
        dbAdapter_exportacion_comprobantes.open();
    }

    public ExportService() {
        super("ExportService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "MANEJANDO EL INTENT...");
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.ACTION_EXPORT_SERVICE.equals(action)) {
                exportarComprobantesFlex();
            }

        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void exportarComprobantesFlex() {


        // Se construye la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_upload)
                .setContentTitle("Exportando a FLEX")
                .setContentText("Procesando...");

        Log.d(TAG, "EXPORTANDO COMPROBANTES AL FLEX...");
        Cursor cursorExportacionFlex = dbAdapter_exportacion_comprobantes.filterExport();


        Log.d(TAG, "COUNT CURSOR_EXPORTACION_FLEX :" + cursorExportacionFlex.getCount());


        if (cursorExportacionFlex.getCount()>0){
            builder.setProgress(cursorExportacionFlex.getCount(), 0, false);
            startForeground(1, builder.build());

            for (cursorExportacionFlex.moveToFirst();!cursorExportacionFlex.isAfterLast(); cursorExportacionFlex.moveToNext()){
                builder.setProgress(cursorExportacionFlex.getCount(), cursorExportacionFlex.getPosition(), false);
                startForeground(1, builder.build());
                JSONObject jsonObjectSuccesfull = null;
                Log.d(TAG, "EXPORTACIÓN AL FLEX -->  _ID : " + cursorExportacionFlex.getLong(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id)) + ", _ID_SID : " +
                                cursorExportacionFlex.getInt(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id_sid)) + ", _ID_SQLITE : " +
                                cursorExportacionFlex.getInt(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id_sqlite)) + ", ESTADO_SINCRONIZACIÓN : " +
                                cursorExportacionFlex.getInt(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_estado_sincronizacion))
                );

                try {
                    jsonObjectSuccesfull = api.InsComprobante(cursorExportacionFlex.getInt(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id_sid)));

                    Log.d(TAG, "SUCCES EXPORTACIÓN FLEX " + isSuccesfulExport(jsonObjectSuccesfull));
                    Log.d(TAG, "JSON EXPORTACIÓN FLEX " + jsonObjectSuccesfull.toString());

                    int idRespuestaFlex = parserIDRespuestaFlex(jsonObjectSuccesfull);

                    Log.d(TAG, "ID RESPUESTA FLEX : " + idRespuestaFlex);

                    if (idRespuestaFlex>=1) {
                        listIdExportacionFlex.add(""+ cursorExportacionFlex.getLong(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id)));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String[] idExportacionComprobantes = new String[listIdExportacionFlex.size()];
            listIdExportacionFlex.toArray(idExportacionComprobantes);

            Log.d(TAG,"NRO REGISTROS EXPORTADOS SATISFACTORIAMENTE AL FLEX : "+ (idExportacionComprobantes.length));
            for (int i = 0; i < idExportacionComprobantes.length; i++) {
                Log.d(TAG, "_ID EXPORT SATISFACTORIO " + idExportacionComprobantes[i]);
            }

            if (listIdExportacionFlex.size() > 0) {
                int nRegistrosExportados= dbAdapter_exportacion_comprobantes.changeEstadoToExport(idExportacionComprobantes, Constants._EXPORTADO);
                Log.d(TAG, "REGISTROS EXPORTADOS AL FLEX : "+ nRegistrosExportados);
            }





        }else{
            Log.d(TAG, "TODOS LOS COMPROBANTES ESTÁN EXPORTADOS AL FLEX");
        }

        // Quitar de primer plano
        stopForeground(true);

    }


    public int parserIDRespuestaFlex(JSONObject jsonObj) {
        int idRespuesta = -1;
        try {
            Log.d("CADENA A PARSEAR ", jsonObj.toString());
            idRespuesta = jsonObj.getInt("Value");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONPARSER ERROR", e.getMessage());
        }
        return idRespuesta;
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
