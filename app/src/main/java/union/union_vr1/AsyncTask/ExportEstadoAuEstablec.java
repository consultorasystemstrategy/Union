package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.firebase.client.Firebase;

import org.json.JSONObject;

import union.union_vr1.Objects.NuevoEstablecimiento;
import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.CursorAdapterEstablecimientoColor;
import union.union_vr1.Sqlite.DbAdapter_Establecimeinto_Historial;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;

/**
 * Created by Kelvin on 28/08/2015.
 */
public class ExportEstadoAuEstablec extends AsyncTask<String, String, String> {
    private DbAdapter_Establecimeinto_Historial dbAdapter_establecimeinto_historial;
    private DbAdapter_Temp_Session session;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private Activity mainActivity;
    private ProgressDialog progressDialog;
    private int idAgente;
    private int idLiquidacion;
    private JSONObject jsonObjectCreated;
    private int idRemoto;

    //FIREBASE
    private Firebase rootRef = null;
    private Firebase nuevoEstablecimientoRef = null;

    private final static String TAG = ExportEstadoAuEstablec.class.getSimpleName();



    public ExportEstadoAuEstablec(Activity mainActivity) {
        this.mainActivity = mainActivity;
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdaptert_evento_establec.open();
        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();
        idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);
        dbAdapter_establecimeinto_historial = new DbAdapter_Establecimeinto_Historial(mainActivity);
        dbAdapter_establecimeinto_historial.open();

        Firebase.setAndroidContext(mainActivity
        );
        rootRef = new Firebase(Constants._APP_ROOT_FIREBASE);
        nuevoEstablecimientoRef = rootRef.child(Constants._CHILD_ESTABLECIMIENTO_NUEVO);
    }

    @Override
    protected String doInBackground(String... strings) {
        StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);

        int existe = Integer.parseInt(strings[0]);
        int idEstablecimiento = Integer.parseInt(strings[1]);
        JSONObject jsonObject = null;

        try {
            //Obteniendo los datos para exportar del establecimiento


            if (existe == 0) {

                Cursor cursor = dbAdapter_establecimeinto_historial.fetchTemEstablecEdit(strings[1]);
                Log.d("IMPORTESTADOESTABLEC", "" + cursor.getCount());

                while (cursor.moveToNext()) {

                    NuevoEstablecimiento nuevoEstablecimiento = new NuevoEstablecimiento(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nro_documento)), Utils.getDatePhone(), Constants.REGISTRO_INTERNET,idAgente);
                    Firebase newEstaclmientoRef = nuevoEstablecimientoRef.push();
                    newEstaclmientoRef.setValue(nuevoEstablecimiento);

                    //GET UNIQUE ID, TIMESTAMP BASED
                    String postId = newEstaclmientoRef.getKey();
                    Log.d(TAG, "GET KEY : " + postId);

                    jsonObjectCreated = api.InsClienteEstablecimiento(
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nombres)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apPaterno)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apMaterno)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nro_documento)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_celular_one)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_correo)),
                            1,
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_documento)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_persona)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_usuario_accion)),
                            1,//Id Empresa con el ususario
                            "554",//codigo
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nro_documento)),
                            1,
                            3,
                            idAgente,//Agente de venta id
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_usuario_accion)),
                            1,//estado de atencion
                            0.0, //Minto de credito
                            5,//modalidad de credito
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_direccion_fiscal)),
                            1,
                            0,//iddistrito
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_telefono_fijo)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_celular_one)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_celular_two)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_usuario_accion)),
                            0,//establecimiento asociado
                            0,//Direccion fiscal Id
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion_establecimiento)),
                            1,
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_usuario_accion)),
                            0,//porcentaje devolucion
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_categoria_estable)),//TIPO establecimiento
                            "Ninguno",
                            0.0,//Monto de compra
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_establecimiento)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_longitud)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_latitud)),
                            1,
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_usuario_accion)),
                            1,
                            idLiquidacion,//Liquidacion
                            4,//Motivo no atewncido
                            0,//RUTAid
                            postId

                    );
                    Log.d("IMPORTESTADOESTABLEC", "" + jsonObjectCreated);

                    if (Utils.isSuccesful(jsonObjectCreated) && Utils.validateRespuesta(jsonObjectCreated)) {
                        idRemoto = jsonObjectCreated.getInt("Value");
                        long s = dbAdaptert_evento_establec.updateEstaIdRemoto(cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_histo_id)), idRemoto + "");
                        long es = dbAdaptert_evento_establec.updateEstabEstado(cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_histo_id)), 6 + "");
                        Log.d("IMPORTESTADOESTABLEC", "" + idRemoto + "--" + s + "--" + es);
                    }
                }
                //-------------------Para Pedir la aprobacion del establecimiento.--------------------
                jsonObject = api.fupd_EstadoClienteEstablecimiento(existe, idRemoto, idAgente);
                Log.d("JSON AUTO ESTAB", "" + jsonObject.toString());
                if (Utils.isSuccesful(jsonObject) && Utils.validateRespuesta(jsonObject)) {
                    int inserto = jsonObject.getInt("Value");
                    if (inserto > 0) {


                        long es = dbAdaptert_evento_establec.updateEstablecsEstadoId(idEstablecimiento);
                        if (es > 0) {
                            Log.d("ESTADO INSERTO", "" + es);
                        }


                    } else {


                    }
                }

            }else{
                idRemoto = Integer.parseInt(strings[1]);
                Log.d("IMPORTESTADOESTABLEC",""+idRemoto);
                //-------------------Para Pedir la aprobacion del establecimiento.--------------------
                jsonObject = api.fupd_EstadoClienteEstablecimiento(existe, idRemoto, idAgente);
                Log.d("IMPORTESTADOESTABLEC", "" + jsonObject.toString());
                if (Utils.isSuccesful(jsonObject)) {
                    int inserto = jsonObject.getInt("Value");
                    if (inserto > 0) {


                        long es = dbAdaptert_evento_establec.updateEstablecsEstadoId(idEstablecimiento);
                        if (es > 0) {
                            Log.d("IMPORTESTADOESTABLEC", "" + es);
                        }


                    } else {


                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.d("JSON AUTO ESTAB", "" + jsonObject.toString());
        }


        publishProgress("" + 10);

        //ACTUALIZAR EN LA TABLA EVENTO POR ESTABLECIMIENTO

        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        createProgressDialog();
    }

    @Override
    protected void onPostExecute(String s) {
        Cursor cursor = dbAdaptert_evento_establec.listarEstablecimientos(idLiquidacion);
        ListView listView = (ListView) mainActivity.findViewById(R.id.VME_listar);
        CursorAdapterEstablecimientoColor cursorAdapterEstablecimientoColor;
        cursorAdapterEstablecimientoColor = new CursorAdapterEstablecimientoColor(mainActivity, cursor);
        listView.setAdapter(cursorAdapterEstablecimientoColor);


        if (mainActivity.isFinishing()) {

            progressDialog.dismiss();
            return;
        } else {

            progressDialog.setProgress(100);


        }
        super.onPostExecute(s);

        dismissProgressDialog();
    }


    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(Integer.parseInt(values[0]));
    }

    public void createProgressDialog() {
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setMessage("Enviando ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();

        }


    }


}
