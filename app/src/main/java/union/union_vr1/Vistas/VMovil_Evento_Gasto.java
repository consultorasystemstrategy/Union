package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Gasto;

public class VMovil_Evento_Gasto extends Activity implements OnClickListener {
    private Cursor cursor, cursorx, cursory;
    private SimpleCursorAdapter dataAdapter;
    private Spinner spinner;
    private DbAdapter_Informe_Gastos dbHelper;
    private DbAdapter_Tipo_Gasto dbHelperx;
    private DbAdapter_Agente dbHelpery;
    private Button mRecalcuz, mActualiz, mCancelar;
    private EditText mSPNgasto;
    private double valbaimp, valimpue, valtotal;
    private String estabX, idcomX, tipdoX, detcoX, totalX;
    private double valCredito;

    private int pase = 0;
    private String TipoDocS;
    private double valtotalp;
    private String fechaCobro;
    private String[] TipGasto = new String[20];
    private int valIdCredito = 0;
    private int idAgente;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_evento_gasto);

        dbHelper = new DbAdapter_Informe_Gastos(this);
        dbHelper.open();
        dbHelperx = new DbAdapter_Tipo_Gasto(this);
        dbHelperx.open();

        dbHelperx.deleteAllTipoGastos();
        dbHelper.deleteAllInformeGastos();
        dbHelperx.insertSomeTipoGastos();
        dbHelper.insertSomeInformeGastos();

        mSPNgasto = (EditText) findViewById(R.id.VEG_SPNgasto);

        mRecalcuz = (Button) findViewById(R.id.VEG_BTNrecalcuz);
        mRecalcuz.setOnClickListener(this);

        mActualiz = (Button) findViewById(R.id.VEG_BTNactualiz);
        mActualiz.setOnClickListener(this);

        mCancelar = (Button) findViewById(R.id.VEG_BTNcancelar);
        mCancelar.setOnClickListener(this);

        spinner = (Spinner) findViewById(R.id.VEG_SPNtipgas);

        addItemsOnSpinner();
        displayListViewVEG();

    }

    public class SpinnerObject {

        private int databaseId;
        private String databaseValue;

        public SpinnerObject(int databaseId, String databaseValue) {
            this.databaseId = databaseId;
            this.databaseValue = databaseValue;
        }

        public int getId() {
            return databaseId;
        }

        public String getValue() {
            return databaseValue;
        }

        @Override
        public String toString() {
            return databaseValue;
        }

    }

    // add items into spinner dynamically
    public List<SpinnerObject> getAllLabels() {

        Cursor cursor = dbHelperx.fetchAllTipoGastos();

        List<SpinnerObject> labels = new ArrayList<SpinnerObject>();
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                TipGasto[i] = cursor.getString(1);
                labels.add(new SpinnerObject(cursor.getInt(0), cursor.getString(2)));
                i += 1;
            } while (cursor.moveToNext());
        }
        // returning labels
        return labels;
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {

        // Spinner Drop down elements
        List<SpinnerObject> lables = getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<SpinnerObject> dataAdapter = new ArrayAdapter<SpinnerObject>(this,
                android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    private void displayListViewVEG() {

        Cursor cursor = dbHelper.fetchAllInformeGastos();

        // The desired columns to be bound
        String[] columns = new String[]{
                DbAdapter_Informe_Gastos.GA_nom_tipo_gasto,
                DbAdapter_Informe_Gastos.GA_fecha,
                DbAdapter_Informe_Gastos.GA_total
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.VEG_nombreinf,
                R.id.VEG_fechasinf,
                R.id.VEG_totalsinf,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_evento_gasto,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.VEG_LSTgassel);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String idNombreGasto =
                        cursor.getString(cursor.getColumnIndexOrThrow("ga_te_nom_tipo_gasto"));
                Toast.makeText(getApplicationContext(),
                        idNombreGasto, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.VEG_BTNrecalcuz:
                //Toast.makeText(getApplicationContext(),
                //        TipGasto[(int)spinner.getSelectedItemId()], Toast.LENGTH_SHORT).show();
                displayListViewVEG();
                break;
            case R.id.VEG_BTNactualiz:
                Double val01 = Double.parseDouble(String.valueOf(mSPNgasto.getText()));
                Double val02 = val01 * 0.18;
                Double val03 = val01 + val02;
                dbHelper.createInformeGastos((int)spinner.getSelectedItemId(), 1, 1, String.valueOf(spinner.getSelectedItem()),
                roundTwoDecimals(val01), roundTwoDecimals(val02), roundTwoDecimals(val03), getDatePhone(), getTimePhone(), 1, idAgente);
                displayListViewVEG();
                break;
            case R.id.VEG_BTNcancelar:
                finish();
                break;
            default:
                break;
        }
    }

    public void procesarCAG(){
        cursory = dbHelpery.fetchAllAgentesVenta();
        //Nos aseguramos de que existe al menos un registro
        if (cursory.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            //do {
            idAgente = cursory.getInt(1);
            //} while(cursorx.moveToNext());
        }
    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    private String getTimePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }
}