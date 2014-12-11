package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;


public class VMovil_Cobros_Totales extends Activity {
DbAdapter_Comprob_Cobro cCobro ;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__cobros__totales);
        cCobro = new DbAdapter_Comprob_Cobro(this);
        cCobro.open();
        listarCobrosTotales();
    }

    public void listarCobrosTotales(){

        Cursor cursor = cCobro.listarComprobantesToCobros();
        cursor.moveToFirst();


       // System.out.println("here..!"+ cursor.getColumnName(0)+"-"+cursor.getColumnName(1)+"-"+cursor.getColumnName(2)+"-"+cursor.getColumnName(3));
        // The desired columns to be bound
        String[] columns = new String[] {
                cursor.getColumnName(0) , //DbAdapter_Comprob_Cobro.CC_doc,
                cursor.getColumnName(1),
                cursor.getColumnName(2) ,
                cursor.getColumnName(5),
                cursor.getColumnName(3),
                cursor.getColumnName(4),
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
               R.id.cod,
                R.id.factotales,
                R.id.nomcliente,
                R.id.localCobro,
                R.id.fechpro,
                R.id.repagar,

        };


        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.infor_menu_cobrototal,
                cursor,
                columns,
                to,
                0);
         final ListView listCobros = (ListView) findViewById(R.id.listaCobrosTotales);
        listCobros.setAdapter(dataAdapter);

        listCobros.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                for(int e = 0; e<i3;e++){

                   Cursor cr = (Cursor) listCobros.getItemAtPosition(e);
                    String fecha_Programada = cr.getString(cr.getColumnIndexOrThrow("cc_te_fecha_programada"));
                    cr.getInt(0);
                    try {
                        Date dSqlite = df.parse(fecha_Programada);
                        Date dSistema = df.parse(getDatePhone());
                        View v = listCobros.getChildAt(e);
                        if(dSqlite.before(dSistema)){

                            if(v != null) {
                                v.setBackgroundColor(0xffff0000);
                            }
                        }
                        if(dSqlite.after(dSistema)){

                            if(v != null) {
                                v.setBackgroundColor(0xffffff00);
                            }
                        }
                        if(dSqlite.equals(dSistema)){

                            if(v != null) {
                                v.setBackgroundColor(0xffff0000);
                            }
                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        });

        listCobros.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cr2 = (Cursor) listCobros.getItemAtPosition(i);
                String establec = cr2.getString(cr2.getColumnIndexOrThrow("ee_te_nom_establec"));
                String cliente = cr2.getString(cr2.getColumnIndexOrThrow("ee_te_nom_cliente"));
                int idCCobro = cr2.getInt(0);
                int monto = cr2.getInt(cr2.getColumnIndexOrThrow("cc_re_monto_a_pagar"));
                System.out.println("here"+establec+"-"+idCCobro+"-"+monto+"-"+cliente);
                view.setBackgroundColor(0xffcccccc);
                select(establec,monto,idCCobro,cliente);

            }
        });


    }
    public void select(String establec,int deuda,int idEstable,String cliente){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Cobro de Credito");

        // set dialog message
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Pago de Deuda para el Establecimiento: "+establec+" con Dueño: "+cliente+" : Deuda: "+deuda+" ")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),
                                "Actualizado", Toast.LENGTH_SHORT).show();

                        // displayListViewVCC();


                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),
                                "Cancelo ", Toast.LENGTH_SHORT).show();
                    }
                });
        //displayListViewVCC();

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

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
    private String getDatePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }
}
