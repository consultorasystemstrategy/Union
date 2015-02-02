package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.AsyncTask.ExportMain;
import union.union_vr1.AsyncTask.ImportMain;
import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapterCobrosTotales;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Utils.MyApplication;


public class VMovil_Cobros_Totales extends Activity {
    DbAdapter_Comprob_Cobro cCobro;
    private SimpleCursorAdapter dataAdapter;
    private VMovil_Cobros_Totales mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_vmovil__cobros__totales);
        cCobro = new DbAdapter_Comprob_Cobro(this);
        cCobro.open();
        listarCobrosTotales();
    }

    public void listarCobrosTotales() {

        Cursor cursor = cCobro.listarComprobantesToCobros();
        CursorAdapterCobrosTotales cACobros = new CursorAdapterCobrosTotales(this, cursor);

        final ListView listCobros = (ListView) findViewById(R.id.listaCobrosTotales);
        listCobros.setAdapter(cACobros);


        listCobros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cr2 = (Cursor) listCobros.getItemAtPosition(i);
                String establec = cr2.getString(cr2.getColumnIndexOrThrow("ee_te_nom_establec"));
                String cliente = cr2.getString(cr2.getColumnIndexOrThrow("ee_te_nom_cliente"));
                String idCCobro = cr2.getString(0);
                int monto = cr2.getInt(cr2.getColumnIndexOrThrow("cc_re_monto_a_pagar"));
                //System.out.println("here"+establec+"-"+idCCobro+"-"+monto+"-"+cliente);
                view.setBackgroundColor(0xffcccccc);
                Dialog(establec, monto, idCCobro, cliente);

            }
        });


    }

    public void Dialog(String establec, final int deuda, final String idCCobro, String cliente) {

        cCobro = new DbAdapter_Comprob_Cobro(this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Cobro de Credito");

        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Pago de Deuda para el Establecimiento: " + establec + " con Dueño: " + cliente + " : Deuda: " + deuda + " ")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        cCobro.open();
                        int estado = cCobro.updateComprobCobrosCan2(idCCobro, getDatePhone(), getTimePhone(), deuda, "1");

                        if (estado == 1) {
                            listarCobrosTotales();

                            Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_SHORT).show();

                            Back();

                        } else {
                            Toast.makeText(getApplicationContext(), "Error Interno", Toast.LENGTH_SHORT).show();
                        }


                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),
                                "Cancelo ", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    public void Back() {
        Intent i = new Intent(this, VMovil_Evento_Indice.class);
        startActivity(i);
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

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    private String getTimePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }
}
