package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.JSONParser.ParserTransferencias;
import union.union_vr1.Login;
import union.union_vr1.Objects.Transferencias;
import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdapter_Transferencias;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.Bluetooth_Printer;
import union.union_vr1.Vistas.VMovil_Online_Pumovil;

/**
 * Created by Usuario on 18/09/2015.
 */
public class CerrarCaja extends AsyncTask<String, String, String> {


    private DbAdapter_Temp_Session session;

    private Activity mainActivity;
    private ProgressDialog progressDialog;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DbAdapter_Transferencias dbAdapter_transferencias;
    private DbAdapter_Stock_Agente dbAdapter_stock_agente;
    JSONObject jsonObject = null;
    private int successCerrarCaja = -1;


    private static final String TAG = CerrarCaja.class.getSimpleName();

    public CerrarCaja(Activity mainActivity) {
        this.mainActivity = mainActivity;
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdaptert_evento_establec.open();

        dbAdapter_transferencias = new DbAdapter_Transferencias(mainActivity);
        dbAdapter_transferencias.open();
        dbAdapter_stock_agente = new DbAdapter_Stock_Agente(mainActivity);
        dbAdapter_stock_agente.open();


        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();
    }

    @Override
    protected String doInBackground(String... strings) {

        StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);
        try {

            int liquidacion = Integer.parseInt(strings[0]);
            double ingresos = Double.parseDouble(strings[1]);
            double gastos = Double.parseDouble(strings[2]);
            double aRendir = Double.parseDouble(strings[3]);
            int kmFinal =Integer.parseInt(strings[4]) ;
            int agente =Integer.parseInt(strings[5]) ;

            Log.d(TAG, "DATOS CERRAR CAJA : " + liquidacion + "-" + ingresos + " - " + gastos + " - " + aRendir + " - " + kmFinal + "-" + agente);

            JSONObject jsonObjecExportTransferencias =  api.InsExportarTransferencias(liquidacion, agente);
            Log.d(TAG, "JSON TRANSFERENCIAS : "+jsonObjecExportTransferencias.toString());
            int successTransferencias = jsonObjecExportTransferencias.getInt("Value");

            Log.d(TAG, "TRANSFERENCIAS success : "+successTransferencias);



            if (successTransferencias==1 || successTransferencias ==2){

                JSONObject jsonObjectTransferencias = api.fsel_ObtenerTransferencias(agente, liquidacion);

                Log.d(TAG, "JSON TRANSFERENCIAS PARA IMPRIMIR : " +jsonObjectTransferencias.toString());

                ParserTransferencias parserTransferencias = new ParserTransferencias();

                ArrayList<Transferencias> transferenciasArrayList = null;
                transferenciasArrayList = parserTransferencias.ParserTransferencias(jsonObjectTransferencias);

                if (transferenciasArrayList.size()>0) {
                    for (int i = 0; i < transferenciasArrayList.size(); i++) {

                        Log.d(TAG, "TRANSFERENCIAS : " + transferenciasArrayList.get(i).getIdTransferencia() + ", PRODUCTO : " + transferenciasArrayList.get(i).getProducto());

                        boolean existeTransferencia = dbAdapter_transferencias.existeTransferencia("" + transferenciasArrayList.get(i).getIdTransferencia());
                        Log.d(TAG, "EXISTE TRANSFERENCIA : " + existeTransferencia);
                        if (existeTransferencia) {
                            //UPDATE TRANSFERENCIA
                            int registrosUpdateTransferencia = dbAdapter_transferencias.updateTransferencia(transferenciasArrayList.get(i), liquidacion);
                            Log.d(TAG, " NRO REGISTROS UPDATE TRANSFERENCIAS : " + registrosUpdateTransferencia);
                        } else {
                            long _id_transferencia = dbAdapter_transferencias.createTransferencias(transferenciasArrayList.get(i), liquidacion);
                            Log.d(TAG, "_ID TRANSFERENCIA IMPORTADO : " + _id_transferencia);
                        }

                    }
                }else {
                    Log.d(TAG, "NO HAY TRANSFERENCIAS PARA IMPRIMIR");
                }

                successCerrarCaja =1;

                jsonObject = api.UpdCerrarCaja(
                        liquidacion,
                        ingresos,
                        gastos,
                        aRendir,
                        kmFinal
                );

                successCerrarCaja = jsonObject.getInt("Value");
                Log.d("JSON CERRAR CAJA", jsonObject.toString());

                //TODAVÍA NO SE HA VALIDADO LAS DEVOLUCIONES
            }




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
            dismissProgressDialog();
        }

        super.onPostExecute(s);
        if (successCerrarCaja>0) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity.getApplicationContext(), "CAJA CERRADA.", Toast.LENGTH_SHORT).show();
                }
            });

            session.deleteVariable(9);
            session.createTempSession(9, 0);
            session.deleteVariable(7);
            session.deleteVariable(8);
            session.createTempSession(7, 0);
            session.createTempSession(8, 0);

            /* int nroRegistrosBorrados = dbAdapter_stock_agente.deleteAllStockAgente();
            Log.d(TAG, "NRO REGISTROS STOCK BORRADOS : "+ nroRegistrosBorrados);*/

            //REDIRIJO AL LOGIN
            /*Intent intent = new Intent(mainActivity, Login.class);
            mainActivity.finish();
            mainActivity.startActivity(intent);*/

            //AHORA REDIGIRÁ A IMPRESIÓN
            Intent intent = new Intent(mainActivity, Bluetooth_Printer.class);
            mainActivity.finish();
            mainActivity.startActivity(intent);

        }/*else if (successCerrarCaja ==Constants._DEVOLUCIONES_NO_VALIDADAS){

            Utils.setToast(mainActivity.getApplicationContext(), "LAS DEVOLUCIONES NO SON VÁLIDAS.", R.color.verde);

        }*/else{
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity.getApplicationContext(), "ERROR, INTENTE DE NUEVO.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    public void createProgressDialog(){
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
