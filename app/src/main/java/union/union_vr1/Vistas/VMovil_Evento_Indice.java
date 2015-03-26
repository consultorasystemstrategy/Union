package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;


import union.union_vr1.Alarm.ReceiverAlarmFinishedDay;
import union.union_vr1.AsyncTask.ExportMain;
import union.union_vr1.AsyncTask.ImportMain;
import union.union_vr1.BarcodeScanner.IntentIntegrator;
import union.union_vr1.BarcodeScanner.IntentResult;
import union.union_vr1.Charts.Bar;
import union.union_vr1.Charts.BarGraph;
import union.union_vr1.MySQL.DbManager_Evento_Establec_GET;
import union.union_vr1.MySQL.DbManager_Evento_Establec_POST;
import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Histo_Comprob_Anterior;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Ruta_Distribucion;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.MyApplication;


public class VMovil_Evento_Indice extends Activity implements View.OnClickListener {

    private DbAdapter_Temp_Session session;
    private DbAdapter_Comprob_Cobro cCobro;
    private DbAdaptert_Evento_Establec dbHelper;
    private DbAdapter_Comprob_Venta_Detalle dbHelper2;
    private DbAdapter_Comprob_Venta dbHelper1;
    private DbAdapter_Agente dbHelper3;
    private DbAdapter_Comprob_Cobro dbHelper4;
    private DbAdapter_Histo_Venta_Detalle dbHelper5;
    private DbAdapter_Stock_Agente dbHelper6;
    private DbAdapter_Precio dbHelper7;
    private DbAdapter_Histo_Comprob_Anterior dbHelper8;

    private Button mClient, mInfgas, mResume, mCarinv, mTrainv, mCobroTotal,  mNumeroEstablecimientos;
    private TextView mNombreRuta, mFecha;
    private VMovil_Evento_Indice mainActivity;
    private DbAdapter_Ruta_Distribucion dbAdapter_ruta_distribucion;
    private int idLiquidacion;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_evento_indice);
        mainActivity  = this;

        session = new DbAdapter_Temp_Session(this);
        session.open();


        boolean export = false;
        boolean importado = false;

        switch (session.fetchVarible(7)){
            case 0:
                export = false;
                break;
            case 1:
                export = true;
                break;
            default:
                export = false;
                break;
        }

        switch (session.fetchVarible(8)){
            case 0:
                importado = false;
                break;
            case 1:
                importado = true;
                break;
            default:
                importado = false;
                break;
        }


        setAlarm();

        if (!export||!importado){

            if (conectadoWifi()||conectadoRedMovil()) {
                new AlertDialog.Builder(mainActivity)
                        .setTitle("Hemos detectado una Conexión a Internet")
                        .setMessage("" +
                                "¿Desea exportar los datos?")
                        .setNegativeButton(android.R.string.no,null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new ExportMain(mainActivity).execute();
                                new ImportMain(mainActivity).execute();
                            }
                        }).create().show();


                session.deleteVariable(7);
                session.deleteVariable(8);
                session.createTempSession(7,1);
                session.createTempSession(8,1);

            }
        }




        dbHelper1 = new DbAdapter_Comprob_Venta(this);
        dbHelper1.open();
        dbHelper2 = new DbAdapter_Comprob_Venta_Detalle(this);
        dbHelper2.open();
        dbHelper = new DbAdaptert_Evento_Establec(this);
        dbHelper.open();


        dbHelper3 = new DbAdapter_Agente(this);
        dbHelper3.open();
        dbHelper4 = new DbAdapter_Comprob_Cobro(this);
        dbHelper4.open();
        dbHelper5 = new DbAdapter_Histo_Venta_Detalle(this);
        dbHelper5.open();
        dbHelper6 = new DbAdapter_Stock_Agente(this);
        dbHelper6.open();
        dbHelper7 = new DbAdapter_Precio(this);
        dbHelper7.open();
        dbHelper8 = new DbAdapter_Histo_Comprob_Anterior(this);
        dbHelper8.open();

        dbAdapter_ruta_distribucion = new DbAdapter_Ruta_Distribucion(this);
        dbAdapter_ruta_distribucion.open();



        session.deleteVariable(5);
        session.createTempSession(5,0);




        mClient = (Button) findViewById(R.id.VEI_BTNclient);
        mInfgas = (Button) findViewById(R.id.VEI_BTNinfgas);
        mResume = (Button) findViewById(R.id.VEI_BTNresume);
        mNombreRuta = (TextView) findViewById(R.id.textViewNombreRuta);
        mNumeroEstablecimientos  = (Button) findViewById(R.id.buttonNumeroEstablecimientos);
        mFecha = (TextView) findViewById(R.id.textViewFecha);


        int idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);
        Cursor cursorAgente = dbHelper3.fetchAgentesByIds(idAgente,idLiquidacion);
        cursorAgente.moveToFirst();

        String nombreRuta = "";
        int numeroEstablecimientoxRuta = 0;
        if (cursorAgente.getCount()>0){
            nombreRuta = cursorAgente.getString(cursorAgente.getColumnIndexOrThrow(dbHelper3.AG_nombre_ruta));
            numeroEstablecimientoxRuta = cursorAgente.getInt(cursorAgente.getColumnIndexOrThrow(dbHelper3.AG_nro_bodegas));

        }
        mNombreRuta.setText("" + nombreRuta);
        mNumeroEstablecimientos.setText(""+numeroEstablecimientoxRuta);
        mFecha.setText(""+getDateFull().substring(0, 1).toUpperCase() + getDateFull().substring(1));

        mCobroTotal = (Button) findViewById(R.id.VEI_BTNcobrarTodo);
        mClient.setOnClickListener(this);
        mInfgas.setOnClickListener(this);
        mResume.setOnClickListener(this);

        mCobroTotal.setOnClickListener(this);
        cCobro = new DbAdapter_Comprob_Cobro(this);
        mNumeroEstablecimientos.setOnClickListener(this);
        cCobro.open();
        AsignarColor(mCobroTotal);


        Log.d("ID AGENTE ", ""+idAgente);

        Cursor cursorRutaSemanal = dbAdapter_ruta_distribucion.fetchRutaDistribucionByIdAgente(idAgente);
        cursorAgente.moveToFirst();
// The desired columns to be bound
        String[] columns = new String[]{
                DbAdapter_Ruta_Distribucion.RD_dia_semana,
                DbAdapter_Ruta_Distribucion.RD_nombre_ruta,
                DbAdapter_Ruta_Distribucion.RD_numero_establecimientos
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.textViewDiaSemana,
                R.id.textViewNombreRuta,
                R.id.textViewNumeroEstablecimiento
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                this, R.layout.lista_ruta_semanal,
                cursorRutaSemanal,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listViewRutaSemanal);
        // Assign adapter to ListView
        listView.setAdapter(simpleCursorAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){

            case R.id.addMenuProduct:
                IntentIntegrator intentIntegrator = new IntentIntegrator(mainActivity);
                intentIntegrator.initiateScan();
                break;
            case R.id.buttonImport:
                new ImportMain(mainActivity).execute();
                break;
            case R.id.buttonExportar:
                new ExportMain(mainActivity).execute();
                break;
            default:
                //ON ITEM SELECTED DEFAULT
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result}
            String barcodeScan = scanResult.getContents();
            String formatScan = scanResult.getFormatName();


            Toast toast = Toast.makeText(getApplicationContext(),
                   "CODIGO DE BARRAS: " +  barcodeScan  , Toast.LENGTH_SHORT);
            toast.show();
            /*textViewContent.setText("CODEBAR CONTEN : "+contents);
            textViewFormat.setText("FORMAT : "+format);*/



            if (barcodeScan.length()>0){

                Cursor cursorEstablecimiento = dbHelper.fetchEstablecsByBarcode(barcodeScan, idLiquidacion);

                if (cursorEstablecimiento.getCount()>0){
                    cursorEstablecimiento.moveToFirst();
                    Toast.makeText(getApplicationContext(),"ESTABLECIMIENTO SCANEADO: "+cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndexOrThrow(dbHelper.EE_nom_establec)),Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "No encontrado", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "¡No ha escaneado!", Toast.LENGTH_SHORT).show();
            }

            /*
            if (barcodeScan.length()>0){

                mCursorScannerProducto = dbHelper_Precio.getProductoByCodigo(id_categoria_establecimiento, barcodeScan, idLiquidacion);

                if (mCursorScannerProducto.getCount()>0){
                    scannerDialog().show();
                }else {
                    Toast.makeText(getApplicationContext(), "Producto con código de barras : "+ barcodeScan + "no disponible en el Stock Actual y/o Categoría establecimiento", Toast.LENGTH_SHORT).show();;
                }
            }else{
                Toast.makeText(getApplicationContext(), "No ha Scaneado ningún producto", Toast.LENGTH_SHORT).show();;
            }
*/


        }
        // else continue with any other code you need in the method
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void AsignarColor(Button btn) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Cursor cursor = cCobro.listarComprobantesToCobros();
        if (cursor.moveToFirst()) {
            String fecha_Programada = cursor.getString(cursor.getColumnIndexOrThrow("cc_te_fecha_programada"));
            try {
                Date dSqlite = df.parse(fecha_Programada);
                Date dSistema = df.parse(getDatePhone());
                if (dSqlite.before(dSistema)) {
                    btn.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.border_right_red));
                }
                if (dSqlite.after(dSistema)) {
                    btn.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.border_right_yellow));
                }
                if (dSqlite.equals(dSistema)) {
                    btn.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.border_right_red));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if (cursor.getCount() <= 0) {
            //verde
            Toast.makeText(getApplicationContext(), "No hay Deudas Por Cobrar", Toast.LENGTH_SHORT).show();
            btn.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.border_right_green));
        }


        cursor.close();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.VEI_BTNclient:
                Intent i = new Intent(this, VMovil_Menu_Establec.class);
                startActivity(i);
                break;
            case R.id.VEI_BTNinfgas:
                Intent ig = new Intent(this, VMovil_Evento_Gasto.class);
                startActivity(ig);
                break;
            case R.id.VEI_BTNresume:
                Intent ir = new Intent(this, VMovil_Resumen_Caja.class);
                startActivity(ir);
                break;

            case R.id.VEI_BTNcobrarTodo:
                Intent cT = new Intent(this, VMovil_Cobros_Totales.class);
                startActivity(cT);
                break;
            case R.id.buttonNumeroEstablecimientos:
                Intent ine = new Intent(this, VMovil_Menu_Establec.class);
                startActivity(ine);
                break;
            default:
                break;
        }
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    private String getDateFull() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
        String formatteDate = format.format(date);
        return formatteDate;
    }



    protected Boolean conectadoWifi(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setAlarm(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            Log.d("CODE EXECUTE ONE TIME","SÍ PRIMERA VEZ");


            Intent myIntent = new Intent(VMovil_Evento_Indice.this, ReceiverAlarmFinishedDay.class);
            pendingIntent = PendingIntent.getBroadcast(VMovil_Evento_Indice.this, 0, myIntent, 0);


            Calendar calendar = Calendar.getInstance();
            //ALARMA ESTABLECIDA A LAS 5 DE LA TARDE
            calendar.set(Calendar.HOUR_OF_DAY, 16);
            calendar.set(Calendar.MINUTE, 45);
            calendar.set(Calendar.SECOND, 00);
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //SE REPETIRÁ CADA 24 HORAS


            Log.d("ALARMA ESTABLECIDA", "OK"+calendar.getTime());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }else{
            Log.d("CODE NO SE EJECUTÓ","NO ES LA PRIMERA VEZ");
        }
    }

}
