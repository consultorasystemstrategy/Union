package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapterFacturas;
import union.union_vr1.Sqlite.CursorAdapter_Facturas_Canjes_Dev;
import union.union_vr1.Sqlite.DbAdapter_Canjes_Devoluciones;
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
    private DbAdapter_Canjes_Devoluciones dbHelper_CanDev;
    private Button btn_mostrar_guias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__evento__canjes__dev);
        //-------------------------------------------------------
        dbHelper_Stock = new DbAdapter_Stock_Agente(getApplication());
        dbHelper_Stock.open();
        dbHelper_CanDev =  new DbAdapter_Canjes_Devoluciones(getApplication());
        dbHelper_CanDev.open();
        //------------------------------------------------------
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
        listar_Facturas();
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
    private void listar_Facturas(){
        Cursor cr = dbHelper_CanDev.obtener_facturas(establec);
        final ListView lista_facturas = (ListView) findViewById(R.id.guias_can_dev);
        if(cr.moveToFirst()){
            CursorAdapterFacturas adapter = new CursorAdapterFacturas(getApplicationContext(),cr);
            lista_facturas.setAdapter(adapter);
            btn_mostrar_guias = (Button) findViewById(R.id.button_mostrar_guias);
            btn_mostrar_guias.setVisibility(View.VISIBLE);
            btn_mostrar_guias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"tas poes",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),mostrar_can_dev_facturas.class);
                    i.putExtra("idEstablec",establec);
                    i.putExtra("idAgente",idAgente);
                    startActivity(i);


                }
            });
        }else{
            btn_mostrar_guias = (Button) findViewById(R.id.button_mostrar_guias);
            btn_mostrar_guias.setVisibility(View.INVISIBLE);

        }



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
