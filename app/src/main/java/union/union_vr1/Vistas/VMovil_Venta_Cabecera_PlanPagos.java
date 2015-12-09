package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DBAdapter_Temp_Venta;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Temp_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.MyApplication;


public class VMovil_Venta_Cabecera_PlanPagos extends Activity implements DatePickerDialog.OnDateSetListener {


    private DbAdapter_Temp_Session session;

    private Spinner spinnerCuotas;
    private TextView textViewMontoTotal;
    private Button butttonCalcularCuotas;
    private Button buttonEstablecerCuotas;

    DecimalFormat df = new DecimalFormat("#.00");

    private Cursor cursorEstablecimiento;
    private DbAdaptert_Evento_Establec dbHelperEventoEstablecimiento;
    private DbAdapter_Temp_Comprob_Cobro dbHelper_TempComprobCobro;
    private ArrayAdapter<CharSequence> adapterCuotas;
    private Double monto_credito;
    private int dias_credito;
    private Double total = 0.0;
    private int numeroCuotas;
    private int idEstablecimiento;
    private int idAgente;
    private SimpleCursorAdapter simpleCursorAdapter;
    private ListView listView;
    private View header;
    private Context mContext;


    //DATOS NULOS A AGREGAR EL EL TEMPORAL COMPROBANTE COBROS
    final int id_comprobante = 0;
    final int id_plan_pago = 0;
    final int id_plan_pago_detalle = 0;
    final String tipo_documento = null;
    final String doc = null;
    final String fecha_cobro = null;
    final String hora_cobro = null;
    final Double monto_cobrado = 0.0;
    final int estado_cobro = 0;
    final int id_forma_cobro = 1;
    final String lugar_registro = "movil";

    private boolean isCuotasCalculated = false;


    //cambiar de fecha long l
    private long _id_plan_pago_selected = -1;
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "No ha establecido las cuotas", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__venta__cabecera__plan_pagos);
        mContext = this;


        session = new DbAdapter_Temp_Session(this);
        session.open();

        dbHelperEventoEstablecimiento = new DbAdaptert_Evento_Establec(this);
        dbHelperEventoEstablecimiento.open();

        dbHelper_TempComprobCobro = new DbAdapter_Temp_Comprob_Cobro(this);
        dbHelper_TempComprobCobro.open();

        spinnerCuotas = (Spinner) findViewById(R.id.VCPP_spinnerCuotas);
        textViewMontoTotal = (TextView) findViewById(R.id.VCPP_textViewMontoTotal);
        butttonCalcularCuotas = (Button) findViewById(R.id.VCPP_buttonCalcularCuotas);
        buttonEstablecerCuotas = (Button) findViewById(R.id.VCPP_buttonEstablecer);
        listView = (ListView) findViewById(R.id.VCPP_listViewCuotas);

        if (!isCuotasCalculated){
            buttonEstablecerCuotas.setEnabled(isCuotasCalculated);
            buttonEstablecerCuotas.setBackgroundColor(getApplication().getResources().getColor(R.color.PersonalizadoSteve4));

        }



        final Intent intent = getIntent();
        total = intent.getDoubleExtra("total", 0.0);

        textViewMontoTotal.setText("S/. " + df.format(total));

        //idEstablecimiento = ((MyApplication) this.getApplication()).getIdEstablecimiento();
        //idAgente = ((MyApplication) this.getApplication()).getIdAgente();

        idEstablecimiento = session.fetchVarible(2);
        idAgente = session.fetchVarible(1);



        cursorEstablecimiento = dbHelperEventoEstablecimiento.fetchEstablecsById("" + idEstablecimiento);

        cursorEstablecimiento.moveToFirst();
        monto_credito = cursorEstablecimiento.getDouble(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_monto_credito));
        dias_credito = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_dias_credito));

        Log.d("Establcount cursor : ", "" + cursorEstablecimiento.getCount());

        if (monto_credito < total) {
            DialogCreditoInsuficiente(this).show();
        }

        switch (dias_credito) {
            case 3:
                adapterCuotas = ArrayAdapter.createFromResource(this, R.array.nCuota1, android.R.layout.simple_spinner_item);
                numeroCuotas = 1;
                break;
            case 7:
                adapterCuotas = ArrayAdapter.createFromResource(this, R.array.nCuota1, android.R.layout.simple_spinner_item);
                numeroCuotas = 1;
                break;
            case 15:
                adapterCuotas = ArrayAdapter.createFromResource(this, R.array.nCuota2, android.R.layout.simple_spinner_item);
                numeroCuotas = 2;
                break;
            case 30:
                adapterCuotas = ArrayAdapter.createFromResource(this, R.array.nCuota4, android.R.layout.simple_spinner_item);
                numeroCuotas = 4;
                break;
            case 31:
                adapterCuotas = ArrayAdapter.createFromResource(this, R.array.nCuota4, android.R.layout.simple_spinner_item);
                numeroCuotas = 4;
                break;

            default:
                adapterCuotas = ArrayAdapter.createFromResource(this, R.array.nCuota1, android.R.layout.simple_spinner_item);
                numeroCuotas = 1;
                break;

        }
        adapterCuotas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCuotas.setAdapter(adapterCuotas);


        header = getLayoutInflater().inflate(R.layout.venta_cabecera_plan_pagos, null);
        listView.addHeaderView(header, null, false);

    }

    private Dialog DialogCreditoInsuficiente(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Crédito Insuficiente");
        builder.setMessage("Monto de Crédito : " + monto_credito + "\n" + " < " +
                "Saldo : " + df.format(total));
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, VMovil_Venta_Cabecera.class);
                finish();
                startActivity(intent);
            }
        });
        return builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vmovil__venta__cabecera__plan_pagos, menu);
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

    public Date getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        return date;
    }

    private String getTimePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }

    public static Date sumaDias(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DAY_OF_YEAR, dias);
        return cal.getTime();
    }

    public static String dateToString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }

    public void CalcularCuotas(View view) {

        switch (view.getId()) {
            case R.id.VCPP_buttonCalcularCuotas:
                calcular();
                break;
            case R.id.VCPP_buttonEstablecer:
                  ((MyApplication)this.getApplication()).setCuotasEstablecidas(true);
                if (!isCuotasCalculated)
                    Toast.makeText(getApplicationContext(),"Primero, debe calcular las cuotas.", Toast.LENGTH_SHORT).show();


                session.deleteVariable(5);
                session.createTempSession(5,1);

                Toast.makeText(getApplicationContext(),
                        "Cuotas Establecidos \nYa no podrá agregar productos, ni eliminar productos", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, VMovil_Venta_Cabecera.class);
                finish();
                startActivity(intent);

                break;
        }
    }

    public void calcular() {

        isCuotasCalculated = true;
        //ACTIVO EL BOTÓN ESTABLECER
        buttonEstablecerCuotas.setEnabled(isCuotasCalculated);
        buttonEstablecerCuotas.setActivated(isCuotasCalculated);
        buttonEstablecerCuotas.setBackgroundColor(getApplication().getResources().getColor(R.color.PersonalizadoSteve2));

        //DESACTIVO EL BOTÓN CALCULAR
        //butttonCalcularCuotas.setEnabled(!isCuotasCalculated);
        //butttonCalcularCuotas.setActivated(!isCuotasCalculated);
        butttonCalcularCuotas.setBackgroundColor(getApplication().getResources().getColor(R.color.PersonalizadoSteve4));

        dbHelper_TempComprobCobro.deleteAllComprobCobros();

        int cuotas = Integer.parseInt(spinnerCuotas.getSelectedItem().toString());
        int NRO_DIAS_SEMANA = 7;

        Double resto=0.0;
        Double montoEntero=0.0;

        switch (cuotas) {
            case 1:
                if (dias_credito == 3) {
                    Date date = sumaDias(getDatePhone(), 3);
                    long id = dbHelper_TempComprobCobro.createComprobCobros(idEstablecimiento, id_comprobante, id_plan_pago, id_plan_pago_detalle, tipo_documento, doc, dateToString(date), total, fecha_cobro, hora_cobro, monto_cobrado, estado_cobro, idAgente, id_forma_cobro, lugar_registro);
                    Log.d("PPC  DÍAS", cuotas+"-"+id);
                } else {
                    Date date = sumaDias(getDatePhone(), 7);
                    long id = dbHelper_TempComprobCobro.createComprobCobros(idEstablecimiento, id_comprobante, id_plan_pago, id_plan_pago_detalle, tipo_documento, doc, dateToString(date), total, fecha_cobro, hora_cobro, monto_cobrado, estado_cobro, idAgente, id_forma_cobro, lugar_registro);
                    Log.d("PPC  DÍAS", cuotas+"-"+id);
                }

                break;
            case 2:
                Date date = null;
                Double sub_monto = 0.0;
                resto = total%cuotas;
                montoEntero = total-resto;

                for (int i = 1; i <= cuotas; i++) {

                    if(i==cuotas){
                        //ES LA CUOTA FINAL
                        sub_monto = montoEntero/cuotas + resto;
                    }else{
                        //SON LAS  CUOTAS NORMALES
                        sub_monto = montoEntero/cuotas;
                    }

                    date = sumaDias(getDatePhone(), i*NRO_DIAS_SEMANA);
                    long id = dbHelper_TempComprobCobro.createComprobCobros(idEstablecimiento, id_comprobante, id_plan_pago, id_plan_pago_detalle, tipo_documento, doc, dateToString(date), sub_monto, fecha_cobro, hora_cobro, monto_cobrado, estado_cobro, idAgente, id_forma_cobro, lugar_registro);
                    Log.d("PPC  DÍAS", cuotas+"-"+id);
                }
                break;
            case 3:
                Date date3 = null;
                Double sub_monto3 = 0.0;
                resto = total%cuotas;
                montoEntero = total-resto;
                for (int i = 1; i <= cuotas; i++) {

                    if(i==cuotas){
                        //ES LA CUOTA FINAL
                        sub_monto3 = montoEntero/cuotas + resto;
                    }else{
                        //SON LAS  CUOTAS NORMALES
                        sub_monto3 = montoEntero/cuotas;
                    }

                    date3 = sumaDias(getDatePhone(), i*NRO_DIAS_SEMANA);
                    long id = dbHelper_TempComprobCobro.createComprobCobros(idEstablecimiento, id_comprobante, id_plan_pago, id_plan_pago_detalle, tipo_documento, doc, dateToString(date3), sub_monto3, fecha_cobro, hora_cobro, monto_cobrado, estado_cobro, idAgente, id_forma_cobro, lugar_registro);
                    Log.d("PPC  DÍAS", cuotas+"-"+id);
                }
                break;
            case 4:
                Date date4 = getDatePhone();
                Double sub_monto4 = 0.0;
                resto = total%cuotas;
                montoEntero = total-resto;
                for (int i = 1; i <= cuotas; i++) {

                    if(i==cuotas){
                        //ES LA CUOTA FINAL
                        sub_monto4 = montoEntero/cuotas + resto;
                    }else{
                        //SON LAS  CUOTAS NORMALES
                        sub_monto4 = montoEntero/cuotas;
                    }

                    date4 = sumaDias(getDatePhone(), i*NRO_DIAS_SEMANA);
                    long id = dbHelper_TempComprobCobro.createComprobCobros(idEstablecimiento, id_comprobante, id_plan_pago, id_plan_pago_detalle, tipo_documento, doc, dateToString(date4), sub_monto4, fecha_cobro, hora_cobro, monto_cobrado, estado_cobro, idAgente, id_forma_cobro, lugar_registro);
                    Log.d("PPC  DÍAS", cuotas+"-"+id);
                }

                break;
        }

        Cursor cursorTempVentaDetalle = dbHelper_TempComprobCobro.fetchAllComprobCobros();
        // The desired columns to be bound
        String[] columns = new String[]{
                DbAdapter_Temp_Comprob_Cobro.temp_fecha_programada,
                DbAdapter_Temp_Comprob_Cobro.temp_monto_a_pagar
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.VCPP_Fecha,
                R.id.VCPP_monto

        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        simpleCursorAdapter = new SimpleCursorAdapter(
                this, R.layout.venta_cabecera_plan_pagos,
                cursorTempVentaDetalle,
                columns,
                to,
                0);

        listView.setAdapter(simpleCursorAdapter);
        //CUANDO QUIERE CAMBIAR DE FECHA, AGREGADO A LA DEUDA TÉCNICA
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                _id_plan_pago_selected = cursor.getLong(cursor.getColumnIndexOrThrow(dbHelper_TempComprobCobro.temp_id_cob_historial));

                //LANZAR EL DATE PICKER FRAGMENT

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        VMovil_Venta_Cabecera_PlanPagos.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });

        //CUANDO QUIERA CAMBIAR DE PRECIO, AGREGADO A LA DEUDA TÉCNIA
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                _id_plan_pago_selected = cursor.getLong(cursor.getColumnIndexOrThrow(dbHelper_TempComprobCobro.temp_id_cob_historial));
                dbHelper_TempComprobCobro.updateMontoPanPagos(""+_id_plan_pago_selected, 100.00);
                return false;
            }
        });

    }

    double formatDecimal(double d) {
        DecimalFormat df = new DecimalFormat("#,00");
        return Double.valueOf(df.format(d));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        Toast.makeText(getApplicationContext(),"FECHA CAMBIADA: " + year + "/" +(++monthOfYear) + "/" + dayOfMonth, Toast.LENGTH_SHORT).show();
        /*if (_id_plan_pago_selected!=-1){

            //VALIDAR QUE NO SEA MAYOR A LA SIGUIENTE FECHA.

            //REALIZAR EL UPDATE
            dbHelper_TempComprobCobro.updateFechaPanPagos(""+_id_plan_pago_selected,dayOfMonth+"/"+(++monthOfYear)+"/"+year);
            Snackbar.make(butttonCalcularCuotas, "FECHA CAMBIADA: " + year + "/" +(++monthOfYear) + "/" + dayOfMonth, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        */

    }
}