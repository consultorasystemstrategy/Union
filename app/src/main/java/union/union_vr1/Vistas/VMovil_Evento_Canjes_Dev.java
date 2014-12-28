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
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;

public class VMovil_Evento_Canjes_Dev extends Activity {
    private String establec;
    private AutoCompleteTextView autoComple;
    private SimpleCursorAdapter adapter;
    private DbAdapter_Stock_Agente dbHelper_Stock;
    private int idAgente;
    private int idProducto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__evento__canjes__dev);
        //-------------------------------------------------------
        dbHelper_Stock = new DbAdapter_Stock_Agente(getApplication());
        dbHelper_Stock.open();
        DbAdaptert_Evento_Establec evenEsta = new DbAdaptert_Evento_Establec(this);
        evenEsta.open();
        evenEsta.deleteAllEstablecs();
        evenEsta.insertSomeEstablecs();
        //--------------------------------------------------------
        Bundle idE = getIntent().getExtras();
        establec = idE.getString("idEstabX");
        idAgente = idE.getInt("idAgente");
        autoComple = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autoComplete();
    }

    private void autoComplete() {
        Cursor cr = dbHelper_Stock.listarProductos();
        cr.moveToFirst();
        String[] colum = new String[]{
                cr.getColumnName(1)};
        int[] to = new int[]{
                R.id.autcomcan_dev
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
                Cursor cr = dbHelper_Stock.listarbyIdProducto(charSequence.toString());
                return cr;
            }
        });
        autoComple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setBackgroundColor(0x00000000);
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                String nom = cursor.getString(1);
                idProducto = cursor.getInt(0);
                autoComple.setText(nom);
                Intent faCanjeDev = new Intent(getApplicationContext(), VMovil_Facturas_Canjes_Dev.class);
                faCanjeDev.putExtra("idAgente", idAgente);
                faCanjeDev.putExtra("idEstablec", establec);
                faCanjeDev.putExtra("idProducto", idProducto);
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
