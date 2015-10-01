package union.union_vr1.Vistas;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;

public class VMovil_Operacion_Canjes_Devoluciones extends TabActivity {
    private DbAdapter_Temp_Session session;
    private DbAdapter_Stock_Agente dbHelper_Stock;
    private AutoCompleteTextView autoComple;
    private TabHost tabHost;
    private SimpleCursorAdapter adapter;
    private String establec;
    private int idAgente;
    private int idProducto;
    private int liquidacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__operacion__canjes__devoluciones);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        session = new DbAdapter_Temp_Session(this);
        session.open();
        liquidacion = session.fetchVarible(3);
        dbHelper_Stock = new DbAdapter_Stock_Agente(this);
        dbHelper_Stock.open();
        Bundle idE = getIntent().getExtras();
        establec = idE.getString("idEstabX");
        idAgente = idE.getInt("idAgente");
        autoComple = (AutoCompleteTextView) findViewById(R.id.autocompleteBuscar);
        autoComplete();
        tabHost = (TabHost) findViewById(android.R.id.tabhost);

        TabHost.TabSpec specanjes = tabHost.newTabSpec("Tab1");
        specanjes.setContent(R.id.tab1_canjes);
        specanjes.setIndicator("Canjes");
        listar_canjes();
        tabHost.addTab(specanjes);

        TabHost.TabSpec specdevoluciones= tabHost.newTabSpec("Tab2");
        specdevoluciones.setContent(R.id.tab2_devoluciones);
        specdevoluciones.setIndicator("Devoluciones");
        listar_devoluciones();
        tabHost.addTab(specdevoluciones);
    }

    private void listar_canjes() {

    }
    private void listar_devoluciones() {

    }

    private void autoComplete() {

        Cursor cr = dbHelper_Stock.listarProductosAndStock(liquidacion);
        cr.moveToFirst();



        String[] columnasStock = new String[]{
                cr.getColumnName(1),
                cr.getColumnName(2),


        };

        // the XML defined views which the data will be bound to
        int[] toStock = new int[]{
                R.id.VCAP_producto,
                R.id.VCPA_stock,

        };

        adapter = new SimpleCursorAdapter(this,
                R.layout.infor_venta_cabecera_productos,
                cr,
                columnasStock,
                toStock,
                0);





        autoComple.setAdapter(adapter);
        autoComple.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                Log.d("ON TEXTCHANGED", "" + charSequence.toString() + "," + i + "," + i2 + "," + i3);
                if (i==0 && i2==0 && i3>=10){
                    Log.d("ON TEXTCHANGED", "HA ESCANEADO UN PRODUCTO");


                    Cursor cursor = dbHelper_Stock.listarbyIdProductoAndStock(charSequence.toString(), liquidacion);

                    String nom = "";
                    String valorUnidad = "";
                    if (cursor.getCount()>=1) {
                        cursor.moveToFirst();

                        nom = cursor.getString(1);
                        idProducto = cursor.getInt(0);
                        valorUnidad = cursor.getString(3);
                    }


                    autoComple.setText(nom);
                    Intent faCanjeDev = new Intent(getApplicationContext(), VMovil_Facturas_Canjes_Dev.class);
                    faCanjeDev.putExtra("idAgente", idAgente);
                    faCanjeDev.putExtra("idEstablec", establec);
                    faCanjeDev.putExtra("idProducto", idProducto);
                    faCanjeDev.putExtra("nomProducto", nom);
                    faCanjeDev.putExtra("valorUnidad", valorUnidad);
                    Log.e("idProducto",""+idProducto+"-"+nom);
                    startActivity(faCanjeDev);
                    finish();


                }else{
                    adapter.getFilter().filter(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                Log.e("Error stock lista"," "+liquidacion);
                Cursor cr = dbHelper_Stock.listarbyIdProductoAndStock(charSequence.toString(), liquidacion);
                return cr;
            }
        });


        autoComple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //adapterView.setBackgroundColor(0x00000000);


                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                String nom = cursor.getString(1);
                idProducto = cursor.getInt(0);
                String valorUnidad = cursor.getString(3);
                autoComple.setText(nom);
                Intent faCanjeDev = new Intent(getApplicationContext(), VMovil_Facturas_Canjes_Dev.class);
                faCanjeDev.putExtra("idAgente", idAgente);
                faCanjeDev.putExtra("idEstablec", establec);
                faCanjeDev.putExtra("idProducto", idProducto);
                faCanjeDev.putExtra("nomProducto", nom);
                faCanjeDev.putExtra("valorUnidad", valorUnidad);
                Log.e("idProducto",""+idProducto+"-"+nom);
                startActivity(faCanjeDev);
                finish();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vmovil__operacion__canjes__devoluciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
