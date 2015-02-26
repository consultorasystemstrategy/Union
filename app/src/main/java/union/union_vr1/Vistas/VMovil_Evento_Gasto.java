package union.union_vr1.Vistas;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import union.union_vr1.CustomOnItemSelectedListener;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
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
    private View header;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Creando la UI
        setContentView(R.layout.princ_evento_gasto);

        dbHelperInformeGasto = new DbAdapter_Informe_Gastos(this);
        dbHelperInformeGasto.open();
        dbHelperTipoGasto = new DbAdapter_Tipo_Gasto(this);
        dbHelperTipoGasto.open();


        spinnerTipoGasto = (Spinner) findViewById(R.id.spinner_VEG_tipoGasto);
        spinnerProcedenciaGasto = (Spinner) findViewById(R.id.spinner_VEG_procedenciaGasto);

        spinnerTipoDocumento = (Spinner) findViewById(R.id.spinner_VEG_tipoDocumento);
        listViewInformeGasto = (ListView) findViewById(R.id.VEG_listViewInformeGasto);

        header = getLayoutInflater().inflate(R.layout.infor_gasto_cabecera,null);
        listViewInformeGasto.addHeaderView(header);

        editTextTotal = (EditText) findViewById(R.id.editText_VEG_total);
        editTextReferencia = (EditText) findViewById(R.id.editText_VEG_referencia);

        editTextTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                Log.d("BEFORE TEXT CHANGE", "ON");
                if (editTextTotal.getText().toString().trim() != "") {
                    editTextTotal.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("ON TEXT CHANGE", "ON");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("AFTER TEXT CHANGE", "ON");
                if (editTextTotal.getText().toString().trim().equals("")) {
                    editTextTotal.setError("Es Requerido");
                } else {
                    editTextTotal.setError(null);

                }
            }
        });

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
                }else{
                    addItemsTipoDocumento();
                }

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
        String tipoGasto = spinnerTipoGasto.getItemAtPosition(spinnerTipoGasto.getSelectedItemPosition()).toString();
        String procedenciaGasto = spinnerProcedenciaGasto.getItemAtPosition(spinnerProcedenciaGasto.getSelectedItemPosition()).toString();
        String tipoDoc = spinnerTipoDocumento.getItemAtPosition(spinnerTipoDocumento.getSelectedItemPosition()).toString();



         Cursor cursorTipoGasto = dbHelperTipoGasto.fetchTipoGastosByName(tipoGasto);
        int positionTipoGasto = 0;
        int positionProcedenciaGasto = 0;
        int positionTipoDocumento = 0;



        if (cursorTipoGasto!=null) {
            positionTipoGasto = cursorTipoGasto.getInt(cursorTipoGasto.getColumnIndexOrThrow(dbHelperTipoGasto.TG_id_tgasto));
        }

        if (procedenciaGasto.toLowerCase().equals("planta")){
            positionProcedenciaGasto = 1;
        }else if (procedenciaGasto.toLowerCase().equals("ruta")){
            positionProcedenciaGasto = 2;
        }

        Double total = 0.0;

        if (editTextTotal.getText().toString().equals("")){
            total = 0.0;
        }else{
            total = Double.valueOf(editTextTotal.getText().toString());
        }


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
                positionTipoDocumento = 1;
                idRegistroGastoInsertado = dbHelperInformeGasto.createInformeGastos(positionTipoGasto, positionProcedenciaGasto, positionTipoDocumento, null, subtotal, igv, total, getDatePhone(), null, estado, referencia, agente, Constants._CREADO);
                Log.d("TIPO DOCUMENTO", "FACTURA");
                break;
            case boleta:
                positionTipoDocumento = 2;
                idRegistroGastoInsertado = dbHelperInformeGasto.createInformeGastos(positionTipoGasto, positionProcedenciaGasto, positionTipoDocumento, null, subtotal, igv, total, getDatePhone(), null, estado, referencia, agente, Constants._CREADO);
                Log.d("TIPO DOCUMENTO", "BOLETA");
                break;
            case ficha:
                positionTipoDocumento = 4;
                idRegistroGastoInsertado = dbHelperInformeGasto.createInformeGastos(positionTipoGasto, positionProcedenciaGasto, positionTipoDocumento, null, subtotal, igv, total, getDatePhone(), null, estado, referencia, agente, Constants._CREADO);
                Log.d("TIPO DOCUMENTO", "FICHA");
                break;
        }

        displayListViewVEG();
        Toast.makeText(getApplicationContext(), "Registro insertado satisfactoriamente con id : " + idRegistroGastoInsertado, Toast.LENGTH_SHORT).show();
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


        Cursor cursor = dbHelperInformeGasto.fetchAllInformeGastos(getDatePhone());
        cursor.moveToFirst();

        // The desired columns to be bound
        String[] columns = new String[]{
                //DbAdapter_Informe_Gastos.GA_id_gasto,
                DbAdapter_Tipo_Gasto.TG_nom_tipo_gasto,
                DbAdapter_Informe_Gastos.GA_total,
                //DbAdapter_Informe_Gastos.GA_subtotal,
                //DbAdapter_Informe_Gastos.GA_igv,
                //DbAdapter_Informe_Gastos.GA_referencia
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                //R.id.VEG_textViewIdTipoGasto,
                R.id.gastoNombre,
                R.id.gastoImporte,
                //R.id.VEG_textViewSubtotal,
                //R.id.gastoReferencia
                //R.id.VEG_textViewReferencia,
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

    public String getDatePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }
}