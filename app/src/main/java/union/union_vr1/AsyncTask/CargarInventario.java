package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Servicios.ServiceExport;
import union.union_vr1.Servicios.ServiceImport;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.CursorAdapter_Cargar_Inventario;
import union.union_vr1.Sqlite.DBAdapter_Temp_Inventario;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Vistas.VMovil_Cargar_Inventario;
import union.union_vr1.Vistas.VMovil_Online_Pumovil;

/**
 * Created by Kelvin on 27/08/2015.
 */
public class CargarInventario extends AsyncTask<String, String, String> {

    private DbAdapter_Temp_Session session;
    private DBAdapter_Temp_Inventario dbAdapter_temp_inventario;
    private Activity mainActivity;
    private ProgressDialog progressDialog;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private String GUIA;
    JSONObject jsonObject = null;

    private static String TAG = CargarInventario.class.getSimpleName();

    public CargarInventario(Activity mainActivity) {
        this.mainActivity = mainActivity;
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdaptert_evento_establec.open();
        dbAdapter_temp_inventario = new DBAdapter_Temp_Inventario(mainActivity);
        dbAdapter_temp_inventario.open();

        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();

    }

    @Override
    protected String doInBackground(String... strings) {

        StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);
        try {

            String idAgente = strings[0];
            String guia = strings[1];
            GUIA = guia;
            Log.d(TAG,"DATOS DE CARGAR INVENTARIO: "+idAgente + " - " + guia);


            jsonObject = api.ObtenerStockAgente(guia, Integer.parseInt(idAgente));

            Log.d(TAG,"JSON CARGAR INVENTARIO: "+ jsonObject.toString());


        } catch (Exception e) {
            Log.d("AysncImport : ", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        createProgressDialog();
    }

    @Override
    protected void onPostExecute(String s) {



        try {
            if (mainActivity.isFinishing()) {
                //dismissProgressDialog();
                progressDialog.dismiss();
                return;
            } else {
                Log.d("JSON CARGAR INVENTARIO", jsonObject.toString());
                //1 si se inserto
                //2 fecha pasada o no existe
                //3 la guia ya se inserto
                //-1 error.
                dismissProgressDialog();
                int objrturn = Integer.parseInt(jsonObject.get("Value").toString());
                String Mensaje = "";
                switch (objrturn){
                    case -1:
                        Mensaje = "Ocurrio un error";
                        break;
                    case 1:
                        long estadoI = dbAdapter_temp_inventario.createTempInventario(GUIA,objrturn);
                        if(estadoI>0){
                            Mensaje = "Se guardo correctamente";
                        }else{
                            Mensaje = "Ocurrio un error al insertar en Android";
                        }
                        break;
                    case 2:
                        Mensaje = "Fecha pasada o no existe";
                        break;
                    case 3:
                        Mensaje = "La guia ya se inserto";
                        break;
                }
                new CargarTransDetallado(mainActivity).execute(GUIA);
                //new CargarTest(mainActivity).execute();



                Toast toast = Toast.makeText(mainActivity.getApplicationContext(), "" +Mensaje, Toast.LENGTH_SHORT);
                toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                toast.show();


                Intent intent = new Intent(mainActivity, ServiceImport.class);
                intent.setAction(Constants.ACTION_IMPORT_SERVICE);
                mainActivity.startService(intent);




            }

            super.onPostExecute(s);



            ListView listView = (ListView)mainActivity.findViewById(R.id.listviewGuias);
            Cursor cursor = dbAdapter_temp_inventario.getAllIvnetario();
            CursorAdapter_Cargar_Inventario cursorAdapter_cargar_inventario = new CursorAdapter_Cargar_Inventario(mainActivity, cursor);
            listView.setAdapter(cursorAdapter_cargar_inventario);

            TextView tx = (TextView)mainActivity.findViewById(R.id.editNroGuia);
            tx.setText("043-");


        } catch (NullPointerException e) {
            Log.e("ERROR", e.getMessage() + "");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR", e.getMessage() + "");
        }
    }

    public void createProgressDialog() {
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setMessage("Solicitando ...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
