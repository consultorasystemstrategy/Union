package union.union_vr1;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;


public class activity_agregar_establecimiento extends Activity {

    private DbAdapter_Temp_Session session;
    private DbAdapter_Agente dbAdapter_agente;
    private TextView textViewNombreRuta;
    private int idLiquidacion;
    private Activity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_agregar_establecimiento);

        mainActivity = this;

        session = new DbAdapter_Temp_Session(this);
        session.open();

        idLiquidacion = session.fetchVarible(3);
        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();

        textViewNombreRuta = (TextView) findViewById(R.id.textViewNombreRuta);

        int idAgente = session.fetchVarible(1);
        Cursor cursorAgente = dbAdapter_agente.fetchAgentesByIds(idAgente, idLiquidacion);
        cursorAgente.moveToFirst();

        String nombreAgente = "";
        int numeroEstablecimientoxRuta = 0;
        if (cursorAgente.getCount()>0){
            nombreAgente = cursorAgente.getString(cursorAgente.getColumnIndexOrThrow(dbAdapter_agente.AG_nombre_agente));
            numeroEstablecimientoxRuta = cursorAgente.getInt(cursorAgente.getColumnIndexOrThrow(dbAdapter_agente.AG_nro_bodegas));

        }
        textViewNombreRuta.setText("Agente : "+nombreAgente);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_agregar_establecimiento, menu);
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
