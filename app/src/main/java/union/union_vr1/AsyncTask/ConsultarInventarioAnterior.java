package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.CursorAdapter_Consultar_Inventario;
import union.union_vr1.Sqlite.DBAdapter_Consultar_Inventario_Anterior;
import union.union_vr1.Sqlite.DBAdapter_Temp_Inventario;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Vistas.VMovil_Cargar_Inventario;

/**
 * Created by Kelvin on 27/08/2015.
 */
public class ConsultarInventarioAnterior extends AsyncTask<String, String, String> {

    private DbAdapter_Temp_Session session;
    private DBAdapter_Consultar_Inventario_Anterior dbAdapter_consultar_inventario_anterior;
    private Activity mainActivity;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject = null;
    private String FECHA;
    private CursorAdapter_Consultar_Inventario cursorAdapter_consultar_inventario;


    public ConsultarInventarioAnterior(Activity mainActivity) {
        this.mainActivity = mainActivity;
        dbAdapter_consultar_inventario_anterior = new DBAdapter_Consultar_Inventario_Anterior(mainActivity);
        dbAdapter_consultar_inventario_anterior.open();
        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();

    }

    @Override
    protected String doInBackground(String... strings) {

        StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);
        try {

            int idAgente = session.fetchVarible(1);
            String fecha = strings[0];
            FECHA = fecha;
            Log.d("IDAGENTE", idAgente + "" + fecha);
            jsonObject = api.GetInventarioAnterior(idAgente, fecha);
            Log.d("JSON CARGAR INVENTARIO", jsonObject.toString());


        } catch (Exception e) {
            Log.d("AysncImport : ", e.getMessage());
            e.printStackTrace();
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


                JSONArray jsonArray = jsonObject.getJSONArray("Value");
                Log.e("JSONERROR", "" + jsonArray.toString());


                JSONObject jsonObj = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObj = jsonArray.getJSONObject(i);
                    String id = jsonObj.getString("codigoProducto");
                    String nombre = jsonObj.getString("nombreProducto");
                    String cantidad = jsonObj.getString("cantidadProducto");
                    Log.d("DATOS", "" + jsonObj.getString("codigoProducto"));
                    dbAdapter_consultar_inventario_anterior.createTempConsultarInventario(id, nombre, cantidad, FECHA);
                }
                Cursor c = dbAdapter_consultar_inventario_anterior.getConsultarInventario(FECHA);
               // cursorAdapter_consultar_inventario = new CursorAdapter_Consultar_Inventario(mainActivity.getApplicationContext(), cursor);
                 TableLayout table_layout;
                table_layout = (TableLayout) mainActivity.findViewById(R.id.tableLayout1);
                table_layout.removeAllViews();

                if (c.moveToFirst()) {
                    // cursorAdapter_consultar_inventario = new CursorAdapter_Consultar_Inventario(getApplicationContext(),c);
                    // listViewInventario.setAdapter(cursorAdapter_consultar_inventario);

                    int rows = c.getCount();
                    int cols = c.getColumnCount();


                    // outer for loop
                    for (int i = 0; i < rows; i++) {

                        TableRow row = new TableRow(mainActivity.getApplicationContext());
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        row.setBackgroundColor(Color.parseColor("#1565C0"));

                        // inner for loop
                        for (int j = 0; j < cols; j++) {

                            TextView tv = new TextView(mainActivity.getApplicationContext());
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(12);
                            tv.setTextColor(Color.parseColor("#190707"));
                            tv.setPadding(0, 0, 0, 0);
                            tv.setText(c.getString(j));
                            tv.setBackground(mainActivity.getResources().getDrawable(R.drawable.border_bottom_personalizado_tableinventario));
                            row.addView(tv);
                            if (i == 0) {
                                tv.setBackground(mainActivity.getResources().getDrawable(R.drawable.border_bottom_personalizado_tableheader));
                                tv.setTextColor(Color.parseColor("#FFFFFF"));

                                String tex = "";
                                switch (j) {
                                    case 0:
                                        tex = "Codigo";
                                        break;
                                    case 1:
                                        tex = "Nombre";
                                        break;
                                    case 2:
                                        tex = "Cantidad";
                                        break;
                                }
                                tv.setText(tex);

                            }
                            if(j==1 && i!=0){
                                tv.setGravity(Gravity.LEFT);
                            }

                        }

                        c.moveToNext();

                        table_layout.addView(row);

                    }
                }
                dismissProgressDialog();


            }

            super.onPostExecute(s);

        } catch (NullPointerException e) {
            Log.e("ERROR", e.getMessage() + "");
        } catch (JSONException e) {
            dismissProgressDialog();
            e.printStackTrace();
            Log.e("ERROR", e.getMessage() + "");
            Toast toast = Toast.makeText(mainActivity.getApplicationContext(), "Inventario No Encontrado", Toast.LENGTH_SHORT);
            toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.rojo));
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
            toast.show();

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
