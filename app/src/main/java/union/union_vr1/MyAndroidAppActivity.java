package union.union_vr1;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Gasto;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
public class MyAndroidAppActivity extends Activity {

    private Spinner spinner1, spinner2, spinner3;
    private Button btnSubmit;
    //private DbAdapter_Tipo_Gasto dbAdapterTipoGasto ;
    private Cursor cursorTipoGasto ;
    private DbAdapter_Stock_Agente dbHelper;
    private DbAdapter_Precio dbHelperx;
    private DbAdapter_Comprob_Venta_Detalle dbHelpery;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        dbHelper = new DbAdapter_Stock_Agente(this);
        dbHelper.open();
        dbHelperx = new DbAdapter_Precio(this);
        dbHelperx.open();
        dbHelpery = new DbAdapter_Comprob_Venta_Detalle(this);
        dbHelpery.open();
        //Clean all data
        dbHelper.deleteAllStockAgente();
        dbHelperx.deleteAllPrecio();
        dbHelpery.deleteAllComprobVentaDetalle();
        //Add some data
        dbHelper.insertSomeStockAgente();
        dbHelperx.insertSomePrecio();

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        addItemsOnSpinner1();
        addItemsOnSpinner2();
        addItemsOnSpinner3();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner1() {
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this,R.array.country_arrays,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.add("list 1");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    public class SpinnerObject {

        private  int databaseId;
        private String databaseValue;

        public SpinnerObject ( int databaseId , String databaseValue ) {
            this.databaseId = databaseId;
            this.databaseValue = databaseValue;
        }

        public int getId () {
            return databaseId;
        }

        public String getValue () {
            return databaseValue;
        }

        @Override
        public String toString () {
            return databaseValue;
        }

    }

    // add items into spinner dynamically
    public List < SpinnerObject> getAllLabels(){

        Cursor cursor = dbHelper.fetchAllStockAgentePrecio();

        //SimpleCursorAdapter adapterSituacion = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,cursor,new String[] {DbAdapter_Tipo_Gasto.TG_nom_tipo_gasto}, new int[] {android.R.id.text1});
        //adapterSituacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner3.setAdapter(adapterSituacion);

        List < SpinnerObject > labels = new ArrayList < SpinnerObject > ();

        if ( cursor.moveToFirst () ) {
            do {
                labels.add ( new SpinnerObject ( cursor.getInt(0) , cursor.getString(2) ) );
            } while (cursor.moveToNext());
        }
        // returning labels
        return labels;
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner3() {

        // Spinner Drop down elements
        List <SpinnerObject> lables = getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<SpinnerObject> dataAdapter = new ArrayAdapter<SpinnerObject>(this,
                android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner3.setAdapter(dataAdapter);

    }
    public void addListenerOnSpinnerItemSelection() {

        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(MyAndroidAppActivity.this,
                        "OnClickListener : " +
                                "\nSpinner N : "+ String.valueOf(spinner1.getSelectedItemId()+1) +
                                "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem()) +
                                "\nSpinner 2 : "+ String.valueOf(spinner2.getSelectedItem()) +
                                "\nSpinner 3 : "+ String.valueOf(spinner3.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

        });
    }
}