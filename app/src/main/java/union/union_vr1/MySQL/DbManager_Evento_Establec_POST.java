package union.union_vr1.MySQL;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import union.union_vr1.Conexion.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;

public class DbManager_Evento_Establec_POST extends Activity implements View.OnClickListener {

    private DbAdaptert_Evento_Establec manager;
    private Cursor cursor;
    private Cursor cursorx;
    private ListView lista;
    private SimpleCursorAdapter adapter;
    private TextView tv;
    private ImageButton bt;
    private TextView txtResultado;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static String url_reg_establecs = "http://192.168.0.158:8081/produnion/ing_establec.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_EventoEstablec = "establecs";
    private static final String TAG_id_evt_establec = "id_evt_establec";
    private static final String TAG_id_establec = "id_establec";
    private static final String TAG_id_cat_est = "id_cat_est";
    private static final String TAG_id_tipo_doc_cliente = "id_tipo_doc_cliente";
    private static final String TAG_id_estado_atencion = "id_estado_atencion";
    private static final String TAG_nom_establec = "nom_establec";
    private static final String TAG_nom_cliente = "nom_cliente";
    private static final String TAG_doc_cliente = "doc_cliente";
    private static final String TAG_orden = "orden";
    private static final String TAG_surtido_stock_ant = "surtido_stock_ant";
    private static final String TAG_surtido_venta_ant = "surtido_venta_ant";
    private static final String TAG_monto_credito = "monto_credito";
    private static final String TAG_dias_credito = "dias_credito";
    private static final String TAG_id_agente = "id_agente";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_agentes_sqlite);

        manager = new DbAdaptert_Evento_Establec(this);
        manager.open();

        //lista = (ListView)findViewById(R.id.listView);
        tv = (TextView)findViewById(R.id.editText);
        bt = (ImageButton)findViewById(R.id.imageButton);
        txtResultado = (TextView)findViewById(R.id.txtResultado);
        bt.setOnClickListener(this);
        //manager.EliminarTodo_Agente();
        //manager.Insertar_Agente("1",1,1,
        //        "1","1",1,1,
        //        1,"1",1,"1",
        //        "1","1",1,
        //        1,1);
        //cursor = manager.BuscarAgentes(tv.getText().toString());
        cursor = manager.fetchAllEstablecs();
        //Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String cod = cursor.getString(2);
                String nom = cursor.getString(3);

                txtResultado.append(" " + cod + " - " + nom + "\n");
            } while(cursor.moveToNext());
        }

        new BuscarTask().execute();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.imageButton){
            txtResultado.setText("");
            //new BuscarTask().execute();
            //cursor = manager.BuscarAgentes(tv.getText().toString());
            cursor = manager.fetchEstablecsByName(tv.getText().toString());
            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String cod = cursor.getString(0);
                    String nom = cursor.getString(1);

                    txtResultado.append(" " + cod + " - " + nom + "\n");
                } while(cursor.moveToNext());
            }
        }
        //switch(view.getId()){
        //    case R.id.imageButton:
        //        new BuscarTask().execute();
        //        break;
        //    case R.id.imageButtonS:
        //        new BuscarTask().execute();
        //        break;
        //}
    }

    private class BuscarTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Buscando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //cursor = manager.BuscarAgentes(tv.getText().toString());
            cursorx = manager.fetchAllEstablecsX();
            //Nos aseguramos de que existe al menos un registro
            if (cursorx.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {

                    String S_id_evt_establec = cursorx.getString(0);
                    String S_id_establec =  cursorx.getString(1);
                    String S_id_cat_est = cursorx.getString(2);
                    String S_id_tipo_doc_cliente = cursorx.getString(3);
                    String S_id_estado_atencion = cursorx.getString(4);
                    String S_nom_establec = cursorx.getString(5);
                    String S_nom_cliente = cursorx.getString(6);
                    String S_doc_cliente = cursorx.getString(7);
                    String S_orden = cursorx.getString(8);
                    String S_surtido_stock_ant = cursorx.getString(9);
                    String S_surtido_venta_ant = cursorx.getString(10);
                    String S_monto_credito = cursorx.getString(11);
                    String S_dias_credito = cursorx.getString(12);
                    String S_id_agente = cursorx.getString(13);

                    // Building Parameters

                    List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
                    paramsx.add(new BasicNameValuePair(TAG_id_evt_establec, S_id_evt_establec));
                    paramsx.add(new BasicNameValuePair(TAG_id_establec, S_id_establec));
                    paramsx.add(new BasicNameValuePair(TAG_id_cat_est, S_id_cat_est));
                    paramsx.add(new BasicNameValuePair(TAG_id_tipo_doc_cliente, S_id_tipo_doc_cliente));
                    paramsx.add(new BasicNameValuePair(TAG_id_estado_atencion, S_id_estado_atencion));
                    paramsx.add(new BasicNameValuePair(TAG_nom_establec, S_nom_establec));
                    paramsx.add(new BasicNameValuePair(TAG_nom_cliente, S_nom_cliente));
                    paramsx.add(new BasicNameValuePair(TAG_doc_cliente, S_doc_cliente));
                    paramsx.add(new BasicNameValuePair(TAG_orden, S_orden));
                    paramsx.add(new BasicNameValuePair(TAG_surtido_stock_ant, S_surtido_stock_ant));
                    paramsx.add(new BasicNameValuePair(TAG_surtido_venta_ant, S_surtido_venta_ant));
                    paramsx.add(new BasicNameValuePair(TAG_monto_credito, S_monto_credito));
                    paramsx.add(new BasicNameValuePair(TAG_dias_credito, S_dias_credito));
                    paramsx.add(new BasicNameValuePair(TAG_id_agente, S_id_agente));

                    // sending modified data through http request
                    // Notice that update Empleado url accepts POST method
                    JSONObject json = jsonParser.makeHttpRequest(url_reg_establecs,"POST", paramsx);

                    // check json success tag
                    try {
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            // successfully updated
                            Intent i = getIntent();
                            // send result code 100 to notify about Empleado update
                            setResult(101, i);
                            //finish();
                        } else {
                            // failed to update Empleado
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } while(cursorx.moveToNext());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //adapter.changeCursor(cursor);
            Toast.makeText(getApplicationContext(),"Finalizada...",Toast.LENGTH_SHORT).show();
        }
    }

}
