package union.union_vr1.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;

public class VMovil_Evento_Canjes_Dev extends Activity {
    private String establec;
    private AutoCompleteTextView autoComple;
    private SimpleCursorAdapter adapter;
    private DbAdapter_Histo_Venta_Detalle dbHelper_Hi_De;
    private DbAdapter_Stock_Agente dbHelper_Stock;
    private int idAgente;
    private int idProducto;
    private int stock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //-------------------------------------------------------
        dbHelper_Stock = new DbAdapter_Stock_Agente(this);
        dbHelper_Stock.open();
        dbHelper_Stock.deleteAllStockAgente();
        dbHelper_Stock.insertSomeStockAgente();
        //--------------------------------------------------------
        dbHelper_Hi_De = new DbAdapter_Histo_Venta_Detalle(this);
        dbHelper_Hi_De.open();
        dbHelper_Hi_De.deleteAllHistoVentaDetalle();
        //--------------------------------------------------------
        Bundle idE = getIntent().getExtras();
        establec = idE.getString("idEstabX");
        idAgente = idE.getInt("idAgente");
        setContentView(R.layout.activity_vmovil__evento__canjes__dev);
        autoComple = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        System.out.println("here" + idAgente + "-" + establec);
        autoComplete();


    }

    private void autoComplete() {


        Cursor cr = dbHelper_Stock.listarById(idAgente);
        cr.moveToFirst();
        System.out.println(cr.getString(0));

        Toast.makeText(this, cr.getString(2), Toast.LENGTH_SHORT).show();
        String[] colum = new String[]{
                cr.getColumnName(2),
                cr.getColumnName(3)
        };
        int[] to = new int[]{
                R.id.autcomcan_dev,
                R.id.autocom_can_dev
        };
        adapter = new SimpleCursorAdapter(this, R.layout.infor_canjes_dev, cr, colum, to, 0);
        autoComple.setAdapter(adapter);


        autoComple.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                adapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                Cursor cr = dbHelper_Stock.listarbyIdProducto(charSequence.toString(), idAgente);
                return cr;
            }
        });
        autoComple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setBackgroundColor(0x00000000);
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                String nom = cursor.getString(2);
                idProducto = cursor.getInt(1);
                autoComple.setText(nom);
                stock = cursor.getInt(3);
                Intent faCanjeDev = new Intent(getApplicationContext(), VMovil_Facturas_Canjes_Dev.class);
                faCanjeDev.putExtra("idAgente", idAgente);
                faCanjeDev.putExtra("idEstablec", establec);
                faCanjeDev.putExtra("idProducto", idProducto);
                faCanjeDev.putExtra("stock", stock);
                faCanjeDev.putExtra("nomProducto", nom);
                startActivity(faCanjeDev);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
