package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.JSONParser.ParserStockAgente;
import union.union_vr1.Objects.StockAgente;
import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Temp_Inventario;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Vistas.VMovil_Cargar_Inventario;

/**
 * Created by Kelvin on 27/08/2015.
 */
public class GetStockAgente extends AsyncTask<String, String, String> {
    private DbAdapter_Stock_Agente dbAdapter_stock_agente;
    private DbAdapter_Temp_Session session;
    private DBAdapter_Temp_Inventario dbAdapter_temp_inventario;
    private Activity mainActivity;
    private ProgressDialog progressDialog;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private int IDAGENTE;
    private int LIQUIDACION;
    JSONObject jsonObjectStockAgente = null;
    ArrayList<StockAgente> stockAgentes = null;


    public GetStockAgente(Activity mainActivity) {
        this.mainActivity = mainActivity;
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdaptert_evento_establec.open();
        dbAdapter_temp_inventario = new DBAdapter_Temp_Inventario(mainActivity);
        dbAdapter_temp_inventario.open();
        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();
        IDAGENTE = session.fetchVarible(1);
        LIQUIDACION = session.fetchVarible(3);
        dbAdapter_stock_agente = new DbAdapter_Stock_Agente(mainActivity);
        dbAdapter_stock_agente.open();

    }

    @Override
    protected String doInBackground(String... strings) {
        StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);
        try {
            jsonObjectStockAgente = api.GetStockAgente(IDAGENTE);
            ParserStockAgente parserStockAgente = new ParserStockAgente();
            stockAgentes = parserStockAgente.parserStockAgente(jsonObjectStockAgente);
            for (int i = 0; i < stockAgentes.size() ; i++) {

                boolean existe = dbAdapter_stock_agente.existsStockAgenteByIdProd(stockAgentes.get(i).getIdProducto(),LIQUIDACION);
                Log.d("DATO ", ""+i);
                if (existe){
                    dbAdapter_stock_agente.updateStockAgentes(stockAgentes.get(i),IDAGENTE,LIQUIDACION);
                }else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_stock_agente.createStockAgentes(stockAgentes.get(i), IDAGENTE, Constants._IMPORTADO,LIQUIDACION);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String s) {


        try {
                Log.d("MENSAJE",""+jsonObjectStockAgente.toString());
               /* Toast toast = Toast.makeText(mainActivity.getApplicationContext(), "Stock Agente Importado Correctamente", Toast.LENGTH_SHORT);
                toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                toast.show();*/

            super.onPostExecute(s);

        } catch (NullPointerException e) {
            Log.e("ERROR", e.getMessage() + "");
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(Integer.parseInt(values[0]));
    }



}
