package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.JSONParser.ParserComprobanteCobro;
import union.union_vr1.JSONParser.ParserComprobanteVentaDetalle;
import union.union_vr1.JSONParser.ParserEventoEstablecimiento;
import union.union_vr1.JSONParser.ParserHistorialVentaDetalles;
import union.union_vr1.JSONParser.ParserPrecio;
import union.union_vr1.JSONParser.ParserStockAgente;
import union.union_vr1.JSONParser.ParserTipoGasto;
import union.union_vr1.Objects.ComprobanteCobro;
import union.union_vr1.Objects.ComprobanteVentaDetalle;
import union.union_vr1.Objects.EventoEstablecimiento;
import union.union_vr1.Objects.HistorialVentaDetalles;
import union.union_vr1.Objects.Precio;
import union.union_vr1.Objects.StockAgente;
import union.union_vr1.Objects.TipoGasto;
import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Histo_Comprob_Anterior;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Gasto;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.MyApplication;
import union.union_vr1.Vistas.VMovil_Evento_Indice;
import union.union_vr1.Vistas.VMovil_Online_Pumovil;
import union.union_vr1.Vistas.VMovil_Venta_Cabecera;

/**
 * Created by Usuario on 01/02/2015.
 */
public class SolicitarCredito extends AsyncTask<String, String, String> {



    private DbAdapter_Temp_Session session;

    private Activity mainActivity;
    private ProgressDialog progressDialog;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    JSONObject jsonObject = null;


    public SolicitarCredito(Activity mainActivity) {
        this.mainActivity = mainActivity;
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdaptert_evento_establec.open();

        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();
    }

    @Override
    protected String doInBackground(String... strings) {

        StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);
            try {
                publishProgress(""+25);

                int idAgente = Integer.parseInt(strings[0]);
                int idEstablecimiento = Integer.parseInt(strings[1]);
                int montoCredito = Integer.parseInt(strings[2]);
                int diasCredito =Integer.parseInt(strings[3]) ;
                Log.d("DATOS SOLICITUD CREDITO ", idAgente + " - "+idEstablecimiento + " - "+montoCredito + " - "+diasCredito);

                jsonObject = api.CreateSolicitudAutorizacionCreditoExp(
                        idAgente,
                        "1",
                        idEstablecimiento,
                        1,
                        "",
                        "",
//                        ((MyApplication)mainActivity.getApplication()).getIdUsuario(),
                        session.fetchVarible(4),
                        montoCredito,
                        diasCredito
                );
                Log.d("JSON SOLICTTUD CRÉDITO", jsonObject.toString());

            publishProgress(""+50);

        }catch (Exception e){
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

        if(mainActivity.isFinishing()){
            //dismissProgressDialog();
            progressDialog.dismiss();
            return;
        }else {
            Log.d("JSON SOLICTTUD CRÉDITO", jsonObject.toString());

            progressDialog.setProgress(100);
            dismissProgressDialog();
        }

        super.onPostExecute(s);
        Intent intent = new Intent(mainActivity, VMovil_Online_Pumovil.class);
        mainActivity.finish();
        mainActivity.startActivity(intent);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(Integer.parseInt(values[0]));
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

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


}