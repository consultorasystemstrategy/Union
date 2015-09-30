package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;

public class VMovil_Venta_Credito extends Activity implements OnClickListener {
    private Cursor cursor, cursorx;
    private SimpleCursorAdapter dataAdapter;

    private DbAdapter_Comprob_Cobro dbHelper;
    private Button mRecalcuz, mActualiz, mCancelar;
    private EditText mSPNcredit;
    private double valbaimp, valimpue, valtotal;
    private String estabX, idcomX, tipdoX, detcoX, totalX;
    private double valCredito;

    private int pase = 0;
    private String TipoDocS;
    private double valtotalp;
    private String fechaCobro;

    private int valIdCredito = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        estabX = bundle.getString("estab");
        tipdoX = bundle.getString("tipdo");
        detcoX = bundle.getString("detco");
        totalX = bundle.getString("total");

        setContentView(R.layout.princ_venta_credito);
        dbHelper = new DbAdapter_Comprob_Cobro(this);
        dbHelper.open();
        mSPNcredit = (EditText) findViewById(R.id.VVCR_SPNcredit);

        mRecalcuz = (Button) findViewById(R.id.VVCR_BTNrecalcuz);
        mRecalcuz.setOnClickListener(this);

        mActualiz = (Button) findViewById(R.id.VVCR_BTNactualiz);
        mActualiz.setOnClickListener(this);

        mCancelar = (Button) findViewById(R.id.VVCR_BTNcancelar);
        mCancelar.setOnClickListener(this);

        displayListViewVVC();
    }

    private void displayListViewVVC() {

        Cursor cursor = dbHelper.fetchAllComprobCobros();

        // The desired columns to be bound
        String[] columns = new String[]{
                DbAdapter_Comprob_Cobro.CC_id_plan_pago_detalle,
                DbAdapter_Comprob_Cobro.CC_fecha_programada,
                DbAdapter_Comprob_Cobro.CC_monto_a_pagar
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.VVCR_numeroinf,
                R.id.VVCR_fechasinf,
                R.id.VVCR_montosinf,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_venta_credito,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.VVCR_LSTcresel);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String idNumeroCredito =
                        cursor.getString(cursor.getColumnIndexOrThrow("cc_in_id_plan_pago_detalle"));
                valIdCredito = cursor.getInt(cursor.getColumnIndexOrThrow("cc_in_id_plan_pago_detalle"));
                Double idMontoCredito =
                        cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_a_pagar"));
                mSPNcredit.setText(String.valueOf(idMontoCredito));
                Toast.makeText(getApplicationContext(),
                        idNumeroCredito, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.VVCR_BTNrecalcuz:

                dbHelper.deleteAllComprobCobros();
                final String[] items = {" 7 dias - 1 cuota", "15 dias - 1 cuota", "15 dias - 2 cuota",
                        "30 dias - 1 cuota", "30 dias - 2 cuota", "30 dias - 4 cuota"};

                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                dialogo.setTitle("Dias de Pago");
                dialogo.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        String idComprobanteCobro = dbHelper.getIdComrobanteCobro(estabX+"");
                        if (item == 0) {
                            valtotalp = Double.parseDouble(totalX);
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone1();
                            dbHelper.createComprobCobros(Integer.parseInt(estabX), Integer.parseInt(idcomX), 1, 1,
                                    tipdoX, detcoX, fechaCobro, valtotalp, "", "", 0, 0, 1, 2, "mobile",1,idComprobanteCobro,0);
                        }
                        if (item == 1) {
                            valtotalp = Double.parseDouble(totalX);
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone2();
                            dbHelper.createComprobCobros(Integer.parseInt(estabX), Integer.parseInt(idcomX), 1, 1,
                                    tipdoX, detcoX, fechaCobro, valtotalp, "", "", 0, 0, 1, 2, "mobile",1,idComprobanteCobro,0);
                        }
                        if (item == 2) {
                            valtotalp = Double.parseDouble(totalX) / 2;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone1();
                            dbHelper.createComprobCobros(Integer.parseInt(estabX), Integer.parseInt(idcomX), 1, 1,
                                    tipdoX, detcoX, fechaCobro, valtotalp, "", "", 0, 0, 1, 2, "mobile",1,idComprobanteCobro,0);
                            valtotalp = Double.parseDouble(totalX) / 2;
                            fechaCobro = getDatePhone2();
                            dbHelper.createComprobCobros(Integer.parseInt(estabX), Integer.parseInt(idcomX), 1, 2,
                                    tipdoX, detcoX, fechaCobro, valtotalp, "", "", 0, 0, 1, 2, "mobile",1,idComprobanteCobro,0);
                        }
                        if (item == 3) {
                            valtotalp = Double.parseDouble(totalX);
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone4();
                            dbHelper.createComprobCobros(Integer.parseInt(estabX), Integer.parseInt(idcomX), 1, 1,
                                    tipdoX, detcoX, fechaCobro, valtotalp, "", "", 0, 0, 1, 2, "mobile",1,idComprobanteCobro,0);
                        }
                        if (item == 4) {
                            valtotalp = Double.parseDouble(totalX) / 2;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone2();
                            dbHelper.createComprobCobros(Integer.parseInt(estabX), Integer.parseInt(idcomX), 1, 1,
                                    tipdoX, detcoX, fechaCobro, valtotalp, "", "", 0, 0, 1, 2, "mobile",1,idComprobanteCobro,0);
                            valtotalp = Double.parseDouble(totalX) / 2;
                            fechaCobro = getDatePhone4();
                            dbHelper.createComprobCobros(Integer.parseInt(estabX), Integer.parseInt(idcomX), 1, 2,
                                    tipdoX, detcoX, fechaCobro, valtotalp, "", "", 0, 0, 1, 2, "mobile",1,idComprobanteCobro,0);
                        }
                        if (item == 5) {
                            valtotalp = Double.parseDouble(totalX) / 4;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone1();
                            dbHelper.createComprobCobros(Integer.parseInt(estabX), Integer.parseInt(idcomX), 1, 1,
                                    tipdoX, detcoX, fechaCobro, valtotalp, "", "", 0, 0, 1, 2, "mobile",1,idComprobanteCobro,0);
                            valtotalp = Double.parseDouble(totalX) / 4;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone2();
                            dbHelper.createComprobCobros(Integer.parseInt(estabX), Integer.parseInt(idcomX), 1, 2,
                                    tipdoX, detcoX, fechaCobro, valtotalp, "", "", 0, 0, 1, 2, "mobile",1,idComprobanteCobro,0);
                            valtotalp = Double.parseDouble(totalX) / 4;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone3();
                            dbHelper.createComprobCobros(Integer.parseInt(estabX), Integer.parseInt(idcomX), 1, 3,
                                    tipdoX, detcoX, fechaCobro, valtotalp, "", "", 0, 0, 1, 2, "mobile",1,idComprobanteCobro,0);
                            valtotalp = Double.parseDouble(totalX) / 4;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone4();
                            dbHelper.createComprobCobros(Integer.parseInt(estabX), Integer.parseInt(idcomX), 1, 4,
                                    tipdoX, detcoX, fechaCobro, valtotalp, "", "", 0, 0, 1, 2, "mobile",1,idComprobanteCobro,0);
                        }
                        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_LONG).show();
                        displayListViewVVC();
                    }
                });
                dialogo.create();
                dialogo.show();

                break;
            case R.id.VVCR_BTNactualiz:
                dbHelper.updateComprobCobros(String.valueOf(valIdCredito), Double.parseDouble(mSPNcredit.getText().toString()));
                displayListViewVVC();
                break;
            case R.id.VVCR_BTNcancelar:
                finish();
                break;
            default:
                break;
        }
    }


    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    private String getDatePhone1() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 7);
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    private String getDatePhone2() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 14);
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    private String getDatePhone3() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 21);
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    private String getDatePhone4() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 28);
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }
}