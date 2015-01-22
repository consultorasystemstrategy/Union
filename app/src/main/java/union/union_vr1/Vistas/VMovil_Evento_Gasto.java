package union.union_vr1.Vistas;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import union.union_vr1.CustomOnItemSelectedListener;
import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Gasto;

public class VMovil_Evento_Gasto extends Activity /*implements OnClickListener */ {

    private final static int IGV = 18;
    private DbAdapter_Informe_Gastos dbHelperInformeGasto;
    private DbAdapter_Tipo_Gasto dbHelperTipoGasto;

    private Spinner spinnerTipoGasto;
    private Spinner spinnerProcedenciaGasto;
    private Spinner spinnerTipoDocumento;
    private EditText editTextTotal;
    private EditText editTextReferencia;
    private ListView listViewInformeGasto;

    private SimpleCursorAdapter dataAdapter;
    private String[] TipoGasto = new String[20];

    /*
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
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Creando la UI
        setContentView(R.layout.princ_evento_gasto);

        dbHelperInformeGasto = new DbAdapter_Informe_Gastos(this);
        dbHelperInformeGasto.open();
        dbHelperTipoGasto = new DbAdapter_Tipo_Gasto(this);
        dbHelperTipoGasto.open();

        /*
        dbHelperTipoGasto.deleteAllTipoGastos();
        dbHelperInformeGasto.deleteAllInformeGastos();
        dbHelperTipoGasto.insertSomeTipoGastos();
        dbHelperInformeGasto.insertSomeInformeGastos();

        */


        spinnerTipoGasto = (Spinner) findViewById(R.id.spinner_VEG_tipoGasto);
        spinnerProcedenciaGasto = (Spinner) findViewById(R.id.spinner_VEG_procedenciaGasto);

        spinnerTipoDocumento = (Spinner) findViewById(R.id.spinner_VEG_tipoDocumento);
        listViewInformeGasto = (ListView) findViewById(R.id.VEG_listViewInformeGasto);

        editTextTotal = (EditText) findViewById(R.id.editText_VEG_total);
        editTextReferencia = (EditText) findViewById(R.id.editText_VEG_referencia);

        addItemsOnSpinners();


        spinnerTipoGasto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tipoGasto = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), tipoGasto, Toast.LENGTH_SHORT).show();

                if (tipoGasto.equals("Combustible")) {
                    String itemProcedenciaGasto = (String) spinnerProcedenciaGasto.getSelectedItem();
                    ProcedenciaGasto procedenciaGasto = ProcedenciaGasto.valueOf(itemProcedenciaGasto);
                    switch (procedenciaGasto) {
                        case planta:
                            addItemsTipoDocumento_CombustiblePlanta();
                            break;
                        case ruta:
                            addItemsTipoDocumento_CombustibleRuta();
                            break;
                    }
                }
                /*TipoDeGasto tipoDeGasto = null;
                tipoDeGasto = TipoDeGasto.valueOf(tipoGasto);


                switch (tipoDeGasto) {
                    case Combustible:

                        String itemProcedenciaGasto = (String) spinnerProcedenciaGasto.getSelectedItem();
                        ProcedenciaGasto procedenciaGasto = ProcedenciaGasto.valueOf(itemProcedenciaGasto);
                        switch (procedenciaGasto) {
                            case planta:
                                addItemsTipoDocumento_CombustiblePlanta();
                                break;
                            case ruta:
                                addItemsTipoDocumento_CombustibleRuta();
                                break;

                        }
                        break;
                    case Comida:
                        addItemsTipoDocumento();
                        break;
                    case Departamento:
                        addItemsTipoDocumento();
                        break;
                    case Viaje:
                        addItemsTipoDocumento();
                        break;
                    case Nuevo:
                        addItemsTipoDocumento();
                        break;
                    default:
                        addItemsTipoDocumento();
                        break;
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerProcedenciaGasto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int positionTipoGasto = spinnerTipoGasto.getSelectedItemPosition();
                positionTipoGasto++;


                String procedenciaSeleccionada = (String) adapterView.getItemAtPosition(i);
                ProcedenciaGasto procedenciaGasto = ProcedenciaGasto.valueOf(procedenciaSeleccionada);


                if (positionTipoGasto == 1) {
                    switch (procedenciaGasto) {
                        case planta:
                            addItemsTipoDocumento_CombustiblePlanta();
                            break;
                        case ruta:
                            addItemsTipoDocumento_CombustibleRuta();
                            break;

                    }

                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        displayListViewVEG();
        /*
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
        */
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

        public String getDatabaseValue() {
            return databaseValue;
        }

        @Override
        public String toString() {
            return databaseValue;
        }

    }

    public List<SpinnerObject> getAllLabels() {

        Cursor cursor = dbHelperTipoGasto.fetchAllTipoGastos();
        List<SpinnerObject> labels = new ArrayList<SpinnerObject>();
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                Log.d("TIPO GASTO SUB " + i, cursor.getString(2));
                TipoGasto[i] = cursor.getString(1);

                labels.add(new SpinnerObject(cursor.getInt(0), cursor.getString(2)));
                i += 1;
            } while (cursor.moveToNext());
        }
        // returning labels
        return labels;
    }

    public void addItemsOnSpinners() {

        // Spinner Drop down elements
        List<SpinnerObject> labelsTipoGasto = getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<SpinnerObject> dataAdapter = new ArrayAdapter<SpinnerObject>(this,
                android.R.layout.simple_spinner_item, labelsTipoGasto);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerTipoGasto.setAdapter(dataAdapter);

        //Creamos el adaptador
        ArrayAdapter<CharSequence> adapterProcedenciaGasto = ArrayAdapter.createFromResource(this, R.array.procedenciaGasto, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el menú
        adapterProcedenciaGasto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerProcedenciaGasto.setAdapter(adapterProcedenciaGasto);

        addItemsTipoDocumento();


    }

    public void addItemsTipoDocumento_CombustibleRuta() {
        //Creamos el adaptador
        ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this, R.array.tipoDocumentoCombustibleRuta, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el menú
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerTipoDocumento.setAdapter(adapterTipoDocumento);
    }

    public void addItemsTipoDocumento_CombustiblePlanta() {
        //Creamos el adaptador
        ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this, R.array.tipoDocumentoCombustiblePlanta, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el menú
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerTipoDocumento.setAdapter(adapterTipoDocumento);
    }

    public void addItemsTipoDocumento() {
        //Creamos el adaptador
        ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this, R.array.tipoDocumento, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el menú
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerTipoDocumento.setAdapter(adapterTipoDocumento);
    }


    public void agegarGastos(View v) {
        int positionTipoGasto = spinnerTipoGasto.getSelectedItemPosition();
        int positionProcedenciaGasto = spinnerProcedenciaGasto.getSelectedItemPosition();
        int positionTipoDocumento = spinnerTipoDocumento.getSelectedItemPosition();

        positionTipoGasto++;
        positionProcedenciaGasto++;
        positionTipoDocumento++;


        Double total = Double.valueOf(editTextTotal.getText().toString());
        String referencia = editTextReferencia.getText().toString();

        String nombreDocumento = (String) spinnerTipoDocumento.getSelectedItem();

        TipoDocumento tipoDocumento = TipoDocumento.valueOf(nombreDocumento);

        Double igv = 0.0;
        Double subtotal = 0.0;
        int estado = 0;
        int agente = 1;

        long idRegistroGastoInsertado = 0;
        switch (tipoDocumento) {
            case factura:
                igv = IGV * total / 100;
                subtotal = total - igv;

                idRegistroGastoInsertado = dbHelperInformeGasto.createInformeGastos(positionTipoGasto, positionProcedenciaGasto, positionTipoDocumento, null, subtotal, igv, total, null, null, estado, referencia, agente);
                Log.d("TIPO DOCUMENTO", "FACTURA");
                break;
            case boleta:
                idRegistroGastoInsertado = dbHelperInformeGasto.createInformeGastos(positionTipoGasto, positionProcedenciaGasto, positionTipoDocumento, null, subtotal, igv, total, null, null, estado, referencia, agente);
                Log.d("TIPO DOCUMENTO", "BOLETA");
                break;
            case ficha:
                idRegistroGastoInsertado = dbHelperInformeGasto.createInformeGastos(positionTipoGasto, positionProcedenciaGasto, positionTipoDocumento, null, subtotal, igv, total, null, null, estado, referencia, agente);
                Log.d("TIPO DOCUMENTO", "FICHA");
                break;
        }

        displayListViewVEG();
        Toast.makeText(getApplicationContext(), "Registro insertado satisfactoriamente con id : " + idRegistroGastoInsertado, Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplicationContext(),
        //        "idTipoGAsto : " + positionTipoGasto + ", idProcedenciaGasto : " + positionProcedenciaGasto + ", idTipoDocumento"+positionTipoDocumento + ", total : "+total+ ", referencia : " +referencia+", nomobreDocumento : "+nombreDocumento, Toast.LENGTH_SHORT).show();
    }

    private enum TipoDocumento {
        factura, ficha, boleta;
    }

    private enum ProcedenciaGasto {
        planta, ruta
    }

    private enum TipoDeGasto {
        Combustible, Comida, Departamento, Viaje, Nuevo
    }

    private void displayListViewVEG() {


        Cursor cursor = dbHelperInformeGasto.fetchAllInformeGastos();
        cursor.moveToFirst();

        // The desired columns to be bound
        String[] columns = new String[]{
                DbAdapter_Informe_Gastos.GA_id_gasto,
                DbAdapter_Tipo_Gasto.TG_nom_tipo_gasto,
                DbAdapter_Informe_Gastos.GA_total,
                DbAdapter_Informe_Gastos.GA_subtotal,
                DbAdapter_Informe_Gastos.GA_igv,
                DbAdapter_Informe_Gastos.GA_referencia
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.VEG_textViewIdTipoGasto,
                R.id.VEG_textViewNombreGasto,
                R.id.VEG_textViewTotal,
                R.id.VEG_textViewSubtotal,
                R.id.VEG_textViewIgv,
                R.id.VEG_textViewReferencia,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_evento_gasto,
                cursor,
                columns,
                to,
                0);

        // Assign adapter to ListView
        listViewInformeGasto.setAdapter(dataAdapter);
    }
    /*
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
  */
}