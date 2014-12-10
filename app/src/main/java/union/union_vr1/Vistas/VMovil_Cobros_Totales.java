package union.union_vr1.Vistas;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;


public class VMovil_Cobros_Totales extends Activity {
DbAdapter_Comprob_Cobro cCobro ;
    private SimpleCursorAdapter dataAdapter;
    private DbAdaptert_Evento_Establec dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__cobros__totales);
        listarCobrosTotales();
    }

    public void listarCobrosTotales(){
        cCobro = new DbAdapter_Comprob_Cobro(this);
        cCobro.open();
        Cursor cr = cCobro.listarComprobantesToCobros();
        ListView listCobros = (ListView) findViewById(R.id.listCobrosTotales);
        dbHelper = new DbAdaptert_Evento_Establec(this);
        dbHelper.open();
        Cursor cursor = dbHelper.fetchAllEstablecsXX();



        // The desired columns to be bound
        String[] columns = new String[] {
                DbAdaptert_Evento_Establec.EE_id_establec,
                DbAdaptert_Evento_Establec.EE_nom_establec,
                DbAdaptert_Evento_Establec.EE_nom_cliente,
                DbAdaptert_Evento_Establec.EE_doc_cliente
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.VME_codigo,
                R.id.VME_establec,
                R.id.VME_nombre,
                R.id.VME_docum,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_menu_establec,
                cursor,
                columns,
                to,
                0);
        listCobros.setAdapter(dataAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vmovil__cobros__totales, menu);
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
