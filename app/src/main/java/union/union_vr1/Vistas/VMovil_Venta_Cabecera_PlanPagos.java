package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DBAdapter_Temp_Venta;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Temp_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.MyApplication;


public class VMovil_Venta_Cabecera_PlanPagos extends Activity{

    private Spinner spinnerCuotas;
    private TextView textViewMontoTotal;
    private Button butttonCalcularCuotas;


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

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "No ha establecido las cuotas", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__venta__cabecera__plan_pagos);
        mContext = this;

        dbHelperEventoEstablecimiento = new DbAdaptert_Evento_Establec(this);
        dbHelperEventoEstablecimiento.open();

        dbHelper_TempComprobCobro = new DbAdapter_Temp_Comprob_Cobro(this);
        dbHelper_TempComprobCobro.open();

        spinnerCuotas = (Spinner) findViewById(R.id.VCPP_spinnerCuotas);
        textViewMontoTotal = (TextView) findViewById(R.id.VCPP_textViewMontoTotal);
        butttonCalcularCuotas = (Button) findViewById(R.id.VCPP_buttonCalcularCuotas);
        listView = (ListView) findViewById(R.id.VCPP_listViewCuotas);


        final Intent intent = getIntent();
        total = intent.getDoubleExtra("total", 0.0);

        textViewMontoTotal.setText("S/. "+total);

        idEstablecimiento = ((MyApplication)this.getApplication()).getIdEstablecimiento();
        idAgente = ((MyApplication)this.getApplication()).getIdAgente();


        cursorEstablecimiento = dbHelperEventoEstablecimiento.fetchEstablecsById(""+idEstablecimiento);

        cursorEstablecimiento.moveToFirst();
        monto_credito = cursorEstablecimiento.getDouble(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_monto_credito));
        dias_credito = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_dias_credito));

        Log.d("Establecimiento count cursor : ", ""+cursorEstablecimiento.getCount());

        if (monto_credito<total) {
            DialogCreditoInsuficiente(this).show();
        }

        switch (dias_credito){
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


        header = getLayoutInflater().inflate(R.layout.venta_cabecera_plan_pagos,null);
        listView.addHeaderView(header);

    }

    private Dialog DialogCreditoInsuficiente(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Crédito Insuficiente");
        builder.setMessage("Monto de Crédito : " + monto_credito + "\n" + " < " +
                "Saldo : " + total);
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
    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
    private Date getDatePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        return date;
    }
    private String getTimePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }
    public static Date sumaDias(Date fecha, int dias){
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DAY_OF_YEAR, dias);
        return cal.getTime();
    }

    public static String dateToString(Date date){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }
    public void CalcularCuotas(View view){

        switch (view.getId()) {
            case R.id.VCPP_buttonCalcularCuotas:
                calcular();
                break;
            case  R.id.VCPP_buttonEstablecer:
              //  ((MyApplication)this.getApplication()).setCuotasEstablecidas(true);
                Toast.makeText(getApplicationContext(),
                        "Cuotas Establecidos \nYa no podrá agregar productos, ni eliminar productos", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this,VMovil_Venta_Cabecera.class);
                startActivity(intent);

                break;
        }
    }

    public void calcular(){

        dbHelper_TempComprobCobro.deleteAllComprobCobros();

        int cuotas = Integer.parseInt(spinnerCuotas.getSelectedItem().toString());


        switch (cuotas){
            case 1:
                if (dias_credito==3){
                    Date date = sumaDias(getDatePhone(),3);
                    dbHelper_TempComprobCobro.createComprobCobros(idEstablecimiento, id_comprobante,id_plan_pago,id_plan_pago_detalle,tipo_documento,doc,dateToString(date),total,fecha_cobro,hora_cobro,monto_cobrado,estado_cobro,idAgente,id_forma_cobro,lugar_registro);

                }else{
                    Date date = sumaDias(getDatePhone(),7);
                    dbHelper_TempComprobCobro.createComprobCobros(idEstablecimiento, id_comprobante,id_plan_pago,id_plan_pago_detalle,tipo_documento,doc,dateToString(date),total,fecha_cobro,hora_cobro,monto_cobrado,estado_cobro,idAgente,id_forma_cobro,lugar_registro);

                }

                break;
            case 2:
                Date date = null;
                Double sub_monto = total/2;
                for (int i = 1; i<=cuotas;i++){
                    if (i==1){
                        date = sumaDias(getDatePhone(),7);
                    }else if(i==2){
                        date = sumaDias(getDatePhone(),14);
                    }
                    dbHelper_TempComprobCobro.createComprobCobros(idEstablecimiento, id_comprobante,id_plan_pago,id_plan_pago_detalle,tipo_documento,doc,dateToString(date),sub_monto,fecha_cobro,hora_cobro,monto_cobrado,estado_cobro,idAgente,id_forma_cobro,lugar_registro);
                }
                break;
            case 3:
                Date date3 = null;
                Double sub_monto3 = total/3;
                for (int i = 1; i<=cuotas;i++){
                    switch (i){
                        case 1:
                            date3 = sumaDias(getDatePhone(),7);
                            break;
                        case 2:
                            date3 = sumaDias(getDatePhone(),14);
                            break;
                        case 3:
                            date3 = sumaDias(getDatePhone(),21);
                            break;
                        default:

                            break;
                    }
                    dbHelper_TempComprobCobro.createComprobCobros(idEstablecimiento, id_comprobante,id_plan_pago,id_plan_pago_detalle,tipo_documento,doc,dateToString(date3),sub_monto3,fecha_cobro,hora_cobro,monto_cobrado,estado_cobro,idAgente,id_forma_cobro,lugar_registro);
                }
                break;
            case 4:
                Date date4 = getDatePhone();
                Double sub_monto4 = total/4;
                for (int i = 1; i<=cuotas;i++){
                    switch (i){
                        case 1:
                            date4 = sumaDias(getDatePhone(),7);
                            break;
                        case 2:
                            date4 = sumaDias(getDatePhone(),14);
                            break;
                        case 3:
                            date4 = sumaDias(getDatePhone(),21);
                            break;
                        case 4:
                            date4 = sumaDias(getDatePhone(),28);
                            break;
                        default:
                            Log.d("Default","Cuotas 4");
                            date = sumaDias(getDatePhone(),7);
                            break;
                    }
                    dbHelper_TempComprobCobro.createComprobCobros(idEstablecimiento, id_comprobante,id_plan_pago,id_plan_pago_detalle,tipo_documento,doc,dateToString(date4),sub_monto4,fecha_cobro,hora_cobro,monto_cobrado,estado_cobro,idAgente,id_forma_cobro,lugar_registro);
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
    }
}
