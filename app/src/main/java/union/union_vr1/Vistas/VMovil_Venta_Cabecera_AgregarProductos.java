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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.MyApplication;


public class VMovil_Venta_Cabecera_AgregarProductos extends Activity implements View.OnClickListener{

    private Button buttonAgregarProductos;
    private AutoCompleteTextView autoCompleteTextView;
    private int idEstablecimiento;
    private int id_categoria_establecimiento;
    private DbAdapter_Stock_Agente dbHelper_Stock_Agente;
    private DbAdaptert_Evento_Establec dbHelper_Evento_Establecimiento;
    private SimpleCursorAdapter adapterProductos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__venta__cabecera__agregar_productos);

        dbHelper_Stock_Agente = new DbAdapter_Stock_Agente(this);
        dbHelper_Stock_Agente.open();

        dbHelper_Evento_Establecimiento = new DbAdaptert_Evento_Establec(this);
        dbHelper_Evento_Establecimiento.open();

        buttonAgregarProductos = (Button) findViewById(R.id.VCAP_buttonAgregar);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.VCAP_AutoCompleteProductos);


        buttonAgregarProductos.setOnClickListener(this);

        idEstablecimiento = ((MyApplication)this.getApplication()).getIdEstablecimiento();

        final Cursor cursorEstablecimiento = dbHelper_Evento_Establecimiento.fetchEstablecsById(String.valueOf(idEstablecimiento));
        cursorEstablecimiento.moveToFirst();
        id_categoria_establecimiento = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_id_cat_est));


        Cursor mCursor = dbHelper_Stock_Agente.fetchStockAgenteByIdEstablecimiento(id_categoria_establecimiento);

        String[] columns = new String[]{
                DbAdapter_Stock_Agente.ST_nombre,
                DbAdapter_Stock_Agente.ST_disponible

        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.VCAP_producto,
                R.id.VCPA_stock

        };
        adapterProductos = new SimpleCursorAdapter(this,
                R.layout.infor_venta_cabecera_productos,
                mCursor,
                columns,
                to,
                0);



       autoCompleteTextView.setAdapter(adapterProductos);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                adapterProductos.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


           adapterProductos.setFilterQueryProvider(new FilterQueryProvider() {
               @Override
               public Cursor runQuery(CharSequence charSequence) {
                   return dbHelper_Stock_Agente.fetchStockAgenteByIdEstablecimientoandName(id_categoria_establecimiento, charSequence.toString());

               }
           });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vmovil__venta__cabecera__agregar_productos, menu);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.VCAP_buttonAgregar:
                Intent intent = new Intent(this, VMovil_Venta_Cabecera.class);
                startActivity(intent);
                break;

        }
    }
}
