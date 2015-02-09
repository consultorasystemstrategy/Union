package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.LightingColorFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;


import union.union_vr1.AsyncTask.ExportMain;
import union.union_vr1.AsyncTask.ImportMain;
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
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.MyApplication;


public class VMovil_Evento_Indice extends Activity implements View.OnClickListener {

    private DbAdapter_Temp_Session session;
    private Cursor cursor;
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
    private TextView titulo;
    private String titulox;
    private Button estado;
    private String estadox;
    private String valIdEstab;
    private Button mClient, mInfgas, mResume, mCarinv, mTrainv, mCobroTotal;
    private VMovil_Evento_Indice mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_evento_indice);
        mainActivity  = this;

        session = new DbAdapter_Temp_Session(this);
        session.open();



        /*
        if (!((MyApplication)getApplication()).isExport()||!((MyApplication)getApplication()).isImportado()){

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

                ((MyApplication)getApplication()).setExport(true);
                ((MyApplication)getApplication()).setImportado(true);
            }
        }


        ((MyApplication) this.getApplication()).setDisplayedHistorialComprobanteAnterior(false);

        */

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

                /*
                ((MyApplication)getApplication()).setExport(true);
                ((MyApplication)getApplication()).setImportado(true);
                */
                session.deleteVariable(7);
                session.deleteVariable(8);
                session.createTempSession(7,1);
                session.createTempSession(8,1);

            }
        }


        //((MyApplication) this.getApplication()).setDisplayedHistorialComprobanteAnterior(false);
        session.deleteVariable(6);
        session.createTempSession(6,0);

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

        //((MyApplication) this.getApplication()).setCuotasEstablecidas(false);

        session.deleteVariable(5);
        session.createTempSession(5,0);

        //Agregando datos de prueba  cada vez que se inicia esta vista

        /*
        dbHelper.deleteAllEstablecs();
        dbHelper.insertSomeEstablecs();


        dbHelper1.deleteAllComprobVenta();
        dbHelper1.insertSomeComprobVenta();
        dbHelper2.deleteAllComprobVentaDetalle();
        dbHelper3.deleteAllAgentes();
        dbHelper3.insertSomeAgentes();

        dbHelper4.insertSomeComprobCobros();
        dbHelper4.deleteAllComprobCobros();
        dbHelper4.insertSomeComprobCobros();

        //dbHelper5.deleteAllHistoVentaDetalle();
        //dbHelper5.insertSomeHistoVentaDetalle();
        dbHelper6.deleteAllStockAgente();
        dbHelper6.insertSomeStockAgente();
        dbHelper7.deleteAllPrecio();
        dbHelper7.insertSomePrecio();
        dbHelper8.deleteAllHistoComprobAnterior();
        dbHelper8.insertSomeHistoComprobAnterior();

        */
        mClient = (Button) findViewById(R.id.VEI_BTNclient);
        mInfgas = (Button) findViewById(R.id.VEI_BTNinfgas);
        mResume = (Button) findViewById(R.id.VEI_BTNresume);
        mCarinv = (Button) findViewById(R.id.VEI_BTNcarinv);
        mTrainv = (Button) findViewById(R.id.VEI_BTNtrainv);
        mCobroTotal = (Button) findViewById(R.id.VEI_BTNcobrarTodo);
        mClient.setOnClickListener(this);
        mInfgas.setOnClickListener(this);
        mResume.setOnClickListener(this);
        //mCarinv.setOnClickListener(this);
        //mTrainv.setOnClickListener(this);
        mCobroTotal.setOnClickListener(this);
        cCobro = new DbAdapter_Comprob_Cobro(this);
        cCobro.open();
        AsignarColor(mCobroTotal);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sincronizar_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){

            case R.id.buttonImport:
                new ImportMain(mainActivity).execute();
                break;
            case R.id.buttonExportar:
                new ExportMain(mainActivity).execute();
                break;
            case R.id.buttonRedireccionarPrincipal:
                Intent intent = new Intent(mainActivity, VMovil_Evento_Indice.class);
                finish();
                startActivity(intent);
                break;
            default:
                //ON ITEM SELECTED DEFAULT
                break;

        }
        return super.onOptionsItemSelected(item);
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
                    btn.setBackgroundColor(0xffff0000);

                }
                if (dSqlite.after(dSistema)) {
                    btn.setBackgroundColor(0xffffff00);
                }
                if (dSqlite.equals(dSistema)) {
                    btn.setBackgroundColor(0xffff0000);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if (cursor.getCount() <= 0) {
            Toast.makeText(getApplicationContext(), "No hay Deudas Por Cobrar", Toast.LENGTH_SHORT).show();
            btn.setBackgroundColor(0xff00ff00);
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
            case R.id.VEI_BTNcarinv:
                Intent is = new Intent(this, DbManager_Evento_Establec_GET.class);
                startActivity(is);
                break;
            case R.id.VEI_BTNtrainv:
                Intent ip = new Intent(this, DbManager_Evento_Establec_POST.class);
                startActivity(ip);
                break;
            case R.id.VEI_BTNcobrarTodo:
                Intent cT = new Intent(this, VMovil_Cobros_Totales.class);
                startActivity(cT);

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
}
