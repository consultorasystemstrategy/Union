package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;

public class VMovil_Cobro_Credito extends Activity implements OnClickListener {
    private Cursor cursor, cursorx;
    private SimpleCursorAdapter dataAdapter;
    final Context context = this;
    private DbAdapter_Comprob_Cobro dbHelper;
    private Button mActualiz, mCancelar;
    private EditText mSPNcredit;
    private double valbaimp, valimpue, valtotal;
    private String estabX;
    private double valCredito;

    private int pase = 0;
    private String TipoDocS;
    private double valtotalp;
    private String fechaCobro;
    private String idCobro;
    private String Estado;
    private String idEstado;
    private String idMontoCancelado;
    private double idVal1, idVal2, idDeuda, idValNew;
    private int valIdCredito = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        estabX=bundle.getString("idEstabX");

        setContentView(R.layout.princ_cobro_credito);
        dbHelper = new DbAdapter_Comprob_Cobro(this);
        dbHelper.open();
        //dbHelper.deleteAllComprobCobros();
        //dbHelper.insertSomeComprobCobros();
        mSPNcredit = (EditText)findViewById(R.id.VCCR_SPNcredit);

        mActualiz = (Button)findViewById(R.id.VCCR_BTNactualiz);
        mActualiz.setOnClickListener(this);

        mCancelar = (Button)findViewById(R.id.VCCR_BTNcancelar);
        mCancelar.setOnClickListener(this);

        displayListViewVCC();
    }

    private void displayListViewVCC() {

        Cursor cursor = dbHelper.fetchAllComprobCobrosByEst(estabX);

        // The desired columns to be bound
        String[] columns = new String[]{
                DbAdapter_Comprob_Cobro.CC_doc,
                DbAdapter_Comprob_Cobro.CC_fecha_programada,
                DbAdapter_Comprob_Cobro.CC_monto_a_pagar,
                DbAdapter_Comprob_Cobro.CC_estado_cobro,
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.VCCR_numdocinf,
                R.id.VCCR_fecproinf,
                R.id.VCCR_montosinf,
                R.id.VCCR_estadoinf,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_cobro_credito,
                cursor,
                columns,
                to,
                0);

        final ListView listView = (ListView) findViewById(R.id.VCCR_LSTcresez);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        //----------------------------------------------------------------


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                for(int i = 0; i<totalItemCount;i++){
                    Cursor cr = (Cursor) listView.getItemAtPosition(i);
                    String fecha_Programada = cr.getString(cr.getColumnIndexOrThrow("cc_te_fecha_programada"));

                    try {

                        Date dSqlite = df.parse(fecha_Programada);
                        Date dSistema = df.parse(getDatePhone());

                        if(dSqlite.before(dSistema)){
                            View v = listView.getChildAt(i);
                            if(v != null) {
                                v.setBackgroundColor(0xffff0000);
                            }
                        }
                        if(dSqlite.after(dSistema)){
                            View v = listView.getChildAt(i);
                            if(v != null) {
                                v.setBackgroundColor(0xff00ff00);
                            }
                        }
                        if(dSqlite.equals(dSistema)){
                            View v = listView.getChildAt(i);
                            if(v != null) {
                                v.setBackgroundColor(0xffff0000);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

            }
        } );

        //-----------------------------------------------------------------------------------------

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                idEstado = cursor.getString(cursor.getColumnIndexOrThrow("cc_in_estado_cobro"));
                idCobro = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                idMontoCancelado = cursor.getString(cursor.getColumnIndexOrThrow("cc_re_monto_cobrado"));
                idVal1 = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_a_pagar"));
                idVal2 = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_cobrado"));
                idDeuda = idVal1 - idVal2;
                mSPNcredit.setText(String.valueOf(idDeuda));
                if(Integer.parseInt(idEstado) == 0){
                    Estado = "Pendiente " + idDeuda;
                }
                if(Integer.parseInt(idEstado) == 1){
                    Estado = "Cancelado";
                }
                Toast.makeText(getApplicationContext(),
                        Estado, Toast.LENGTH_SHORT).show();
                }

        });
    }

    public void select(final String estadox){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Cancelar");

        // set dialog message
        alertDialogBuilder
                .setMessage("¿Elegir?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        idValNew = Double.parseDouble(mSPNcredit.getText().toString()) + idVal2;
                        dbHelper.updateComprobCobrosCan(idCobro,getDatePhone(),getTimePhone(),idValNew,estadox);
                        Toast.makeText(getApplicationContext(),
                                "Actualizado", Toast.LENGTH_SHORT).show();
                        displayListViewVCC();
                        mSPNcredit.setText("0.0");
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
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

    public void select2(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Cancelar");

        // set dialog message
        alertDialogBuilder
                .setMessage("¿Elegir?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        idValNew = Double.parseDouble(mSPNcredit.getText().toString()) + idVal2;
                        dbHelper.updateComprobCobrosCan(idCobro,"","",0,"0");
                        Toast.makeText(getApplicationContext(),
                                "Anulado", Toast.LENGTH_SHORT).show();
                        displayListViewVCC();
                        mSPNcredit.setText("0.0");
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
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
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.VCCR_BTNactualiz:
                if(Integer.parseInt(idEstado) == 0){
                    if(idDeuda == Double.parseDouble(mSPNcredit.getText().toString())){
                        select("1");
                    }else{
                    if(idDeuda > Double.parseDouble(mSPNcredit.getText().toString())){
                        select("0");
                    }
                    if(idDeuda < Double.parseDouble(mSPNcredit.getText().toString())){
                        Toast.makeText(getApplicationContext(),"El ingreso sobrepasa la deuda", Toast.LENGTH_SHORT).show();
                    }}
                }
                if(Integer.parseInt(idEstado) == 1){
                    Toast.makeText(getApplicationContext(),"No posee deuda", Toast.LENGTH_SHORT).show();
                    select2();
                }
                //dbHelper.updateComprobCobrosCan(String.valueOf(valIdCredito),getDatePhone(),getTimePhone(),Double.parseDouble(mSPNcredit.getText().toString()));
                displayListViewVCC();
                break;
            case R.id.VCCR_BTNcancelar:
                finish();
                break;
            default:
                break;
        }
    }

    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
    private String getDatePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }
    private String getTimePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }
}