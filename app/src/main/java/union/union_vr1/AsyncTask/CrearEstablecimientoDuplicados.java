package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import union.union_vr1.Objects.EventoEstablecimiento;
import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.CursorAdapterEstablecimientoColor;
import union.union_vr1.Sqlite.DbAdapter_Establecimeinto_Historial;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.VMovil_Menu_Establec;

/**
 * Created by Kelvin on 08/12/2015.
 */
public class CrearEstablecimientoDuplicados extends AsyncTask<String, String, String> {
    private int idRemotoDuplicado;
    private Activity mainActivity;
    private JSONObject jsonObjectCreated = null;
    private StockAgenteRestApi stockAgenteRestApi = null;
    private DbAdapter_Temp_Establecimiento dbAdapter_temp_establecimiento;
    private DbAdapter_Temp_Session dbAdapter_temp_session;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DbAdapter_Establecimeinto_Historial dbAdapter_establecimeinto_historial;

    private static final String TAG = CrearEstablecimientoDuplicados.class.getSimpleName();



    public CrearEstablecimientoDuplicados(Activity activity) {
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
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdaptert_evento_establec.open();
        int ruta = dbAdapter_temp_session.fetchVarible(777);
        Log.d(TAG, "RUTA:" + ruta);

        Cursor cr = dbAdapter_establecimeinto_historial.fetchTemEstablecEnviar(Constants.REGISTRO_SIN_INTERNET);
        Log.d(TAG, "RUTA:" + cr.getCount());

        while (cr.moveToNext()){
            try {
                jsonObjectCreated = stockAgenteRestApi.fins_ClienteTemporal(
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nombres)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apMaterno)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apPaterno)),
                        cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_categoria_estable)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_celular_one)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_celular_two)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion_establecimiento)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_direccion_fiscal)),
                        0,
                        0,
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nro_documento)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_correo)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_latitud)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_longitud)),
                        cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_telefono_fijo)),
                        cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_documento)),
                        cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_establecimiento)),
                        cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_persona)),
                        cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_usuario_accion))

                );
                Log.d("HHHH", jsonObjectCreated.toString());
                if (Utils.isSuccesful(jsonObjectCreated)) {
                     idRemotoDuplicado = jsonObjectCreated.getInt("Value");
                    if (idRemotoDuplicado > 0) {
                        long h = dbAdapter_establecimeinto_historial.updateEstado(cr.getInt(
                                cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_histo_id)), Constants._EXPORTADO, idRemotoDuplicado);
                        long c = dbAdaptert_evento_establec.updateEstabEstado(cr.getInt(
                                cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_histo_id)),""+6);
                        Log.d("HHHH", "" + h);
                    }


                }


            } catch (Exception e) {
                Log.d("HHHH", e.getMessage() + "");
            }
        }

        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        SharedPreferences.Editor editor = mainActivity.getSharedPreferences("DIRECCION_FISCAL", Context.MODE_PRIVATE).edit();
        editor.putString("fiscal", null);
        editor.commit();

        if(idRemotoDuplicado>0){
            int idLiquidacion = dbAdapter_temp_session.fetchVarible(3);
            Cursor cursor = dbAdaptert_evento_establec.listarEstablecimientos(idLiquidacion);
            CursorAdapterEstablecimientoColor
            cursorAdapterEstablecimientoColor = new CursorAdapterEstablecimientoColor(mainActivity, cursor);
            ListView listView = (ListView)mainActivity.findViewById(R.id.VME_listar);
            listView.setAdapter(cursorAdapterEstablecimientoColor);
            Utils.setToast(mainActivity,"Actualizado",R.color.verde);
        }else{
            Utils.setToast(mainActivity,"Ocurrio un error",R.color.rojo);
        }

        super.onPostExecute(s);

    }
}
