package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.CursorAdapter_Cobros_Establecimiento;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Utils.MyApplication;


import static union.union_vr1.R.layout.prompts_cobros;

public class VMovil_Cobro_Credito extends Activity implements OnClickListener {
    private Cursor cursor, cursorx;
    private SimpleCursorAdapter dataAdapter;
    final Context context = this;
    private DbAdapter_Comprob_Cobro dbHelper;
    private Button mActualiz, mCancelar;
    private TextView mSPNcredit;
    private double valbaimp, valimpue, valtotal;
    private String estabX;
    private double valCredito;
    private int pase = 0;

    int idPlanPago;
    int idPlanPagoDetalle;
    String tipoDoc;
    String doc;
    int comprobanteVenta;
    private String idCobro;
    private String Estado;
    private String idEstado;
    private String idMontoCancelado;
    private double idVal1, idVal2, idDeuda, idValNew;
    private int valIdCredito = 0;
    private DBAdapter_Temp_Autorizacion_Cobro dbAutorizacionCobro;
    private DbAdapter_Comprob_Cobro dbComprobanteCobro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        estabX = bundle.getString("idEstabX");
        dbAutorizacionCobro= new DBAdapter_Temp_Autorizacion_Cobro(this);
        dbAutorizacionCobro.open();
        dbComprobanteCobro = new DbAdapter_Comprob_Cobro(this);
        dbComprobanteCobro.open();
        setContentView(R.layout.princ_cobro_credito);
        dbHelper = new DbAdapter_Comprob_Cobro(this);
        dbHelper.open();
        //dbHelper.deleteAllComprobCobros();
        //dbHelper.insertSomeComprobCobros();
        mSPNcredit = (TextView) findViewById(R.id.VCCR_SPNcredit);

        mActualiz = (Button) findViewById(R.id.VCCR_BTNactualiz);
        mActualiz.setOnClickListener(this);

        mCancelar = (Button) findViewById(R.id.VCCR_BTNcancelar);
        mCancelar.setOnClickListener(this);
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSPNcredit.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        displayListViewVCC();

    }

    private void displayListViewVCC() {

        Cursor cursor = dbHelper.fetchAllComprobCobrosByEst(estabX);

        CursorAdapter_Cobros_Establecimiento  adapterCobros = new CursorAdapter_Cobros_Establecimiento(getApplicationContext(),cursor);

        final ListView listView = (ListView) findViewById(R.id.VCCR_LSTcresez);
        // Assign adapter to ListView
        listView.setAdapter(adapterCobros);

        //----------------------------------------------------------------




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
                idDeuda = idVal1 ;
                mSPNcredit.setText(String.valueOf(idDeuda));
                if (Integer.parseInt(idEstado) == 1) {
                    Estado = "Pendiente " + idDeuda;
                }
                if (Integer.parseInt(idEstado) == 0) {
                    Estado = "Cancelado";
                }
                Toast.makeText(getApplicationContext(),
                        Estado, Toast.LENGTH_SHORT).show();
            }

        });
        inicioAutorizacion(listView);
    }
    private void inicioAutorizacion(final ListView listView){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                mSPNcredit.setText("");
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                autorizacion(cursor,i);
                return false;
            }
        });
    }

    public void select(final String estadox) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Cancelar");

        // set dialog message
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("¿Elegir?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        idValNew = Double.parseDouble(mSPNcredit.getText().toString()) + idVal2;
                        dbHelper.updateComprobCobrosCan(idCobro, getDatePhone(), getTimePhone(), idValNew, estadox);
                        Toast.makeText(getApplicationContext(),
                                "Actualizado", Toast.LENGTH_SHORT).show();
//<
                        //displayListViewVCC();
                        mSPNcredit.setText("0.0");
                        Back();

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

    public void Back() {

        Intent returnAc = new Intent(this, VMovil_Evento_Establec.class);
        returnAc.putExtra("idEstab", estabX);
        startActivity(returnAc);
        finish();
    }

    private void autorizacion(Cursor cursor,int p ) {
        //Variables Operacion
        final Double[] valorProloga = {0.0,0.0};
        //Obteniendo Datos del Cursor
        cursor.moveToPosition(p);
        comprobanteVenta = cursor.getInt(cursor.getColumnIndexOrThrow("cc_in_id_comprob"));
        idPlanPago = cursor.getInt(cursor.getColumnIndexOrThrow("cc_in_id_plan_pago"));
        idPlanPagoDetalle = cursor.getInt(cursor.getColumnIndexOrThrow("cc_in_id_plan_pago_detalle"));
        tipoDoc = cursor.getString(cursor.getColumnIndexOrThrow("cc_te_desc_tipo_doc"));
        doc = cursor.getString(cursor.getColumnIndexOrThrow("cc_te_doc"));
        idEstado = cursor.getString(cursor.getColumnIndexOrThrow("cc_in_estado_cobro"));
        idCobro = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        idVal1 = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_a_pagar"));
        idVal2 = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_cobrado"));

        idDeuda = idVal1 ;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Esta Realizando la Prologa de la Deuda a Pagar:");
        builder.setCustomTitle(title);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout_cobros = inflater.inflate(prompts_cobros, null);
        //Iniciando y Parseando Widgets
        final EditText cantidadHoy = (EditText) layout_cobros.findViewById(R.id.cantidadHoy);
        final DatePicker fechaProloga = (DatePicker) layout_cobros.findViewById(R.id.dateProloga);
        final TextView cantidadProloga = (TextView) layout_cobros.findViewById(R.id.montoProloga);
        //Calculando el monto Prologa.
        cantidadHoy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged( Editable valorHoy) {
                Log.d("Parametros","idDeuda"+idDeuda+"Monto a Pagar:"+idVal1+"Monto Pagado"+idVal2+"");

                if( valorHoy.length() >0 ){

                    valorProloga[0] = idDeuda-Double.parseDouble(valorHoy.toString());
                    if(valorProloga[0]<=0 || valorProloga[0] >= idDeuda){
                        valorProloga[1]= Double.parseDouble(valorHoy.toString());
                        cantidadProloga.setError("Error en Valor");
                        cantidadHoy.setText("");
                    }else{
                        cantidadProloga.setError(null);
                        DecimalFormat df = new DecimalFormat("#.00");
                        cantidadProloga.setText(df.format(valorProloga[0]));
                    }

              }else{
                    cantidadProloga.setError("Error en Valor");
                }
            }
        });
        builder.setView(layout_cobros);
        builder.setCancelable(false);
        builder.setPositiveButton("Guardar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        DecimalFormat df = new DecimalFormat("#.00");
                        int dia = fechaProloga.getDayOfMonth();
                        int mes = fechaProloga.getMonth()+1;
                        int año = fechaProloga.getYear();
                        double valorP = Double.parseDouble(cantidadHoy.getText().toString());
                        double valorR = Double.parseDouble(cantidadProloga.getText().toString());
                        String fechaP = dia+"/"+mes+"/"+año;
                        if(fechaP!="" || valorP !=0.0 ){

                            int idAgente = ((MyApplication)getApplicationContext()).getIdAgente();
                            long idDetalleCobro = dbComprobanteCobro.createComprobCobros(Integer.parseInt(estabX),comprobanteVenta,idPlanPago,idPlanPagoDetalle,tipoDoc,doc,fechaP,valorP,"","",0.0,2,idAgente,2,"");
                            Log.d("parametroscobros",""+valorP+"-"+valorR);
                            boolean upCobros = dbComprobanteCobro.updateCobro(idCobro,valorP,valorR);
                            long idAutorizacion = dbAutorizacionCobro.createTempAutorizacionPago(idAgente,4,1,comprobanteVenta,valorP,Integer.parseInt(idDetalleCobro+""),Integer.parseInt(estabX),Constants._CREADO,Integer.parseInt(idCobro));

                            if(idAutorizacion!=0 && idDetalleCobro !=0 && upCobros==true){
                                Toast.makeText(getApplicationContext(),"Guardado Correctamente",Toast.LENGTH_SHORT).show();
                                Back();

                            }else{
                                Toast.makeText(getApplicationContext(),"Ocurrio un Error",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                        Toast.makeText(getApplicationContext(), "Por favor Ingrese Todos los Campos", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder.show();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.VCCR_BTNactualiz:

                if(idEstado !=null) {

                    if (idEstado.equals("1")) {
                        if (mSPNcredit.getText().equals("")) {
                            Toast.makeText(getApplicationContext(), "Tiene que Seleccionar una Deuda", Toast.LENGTH_SHORT).show();
                        }
                        if (idDeuda == Double.parseDouble(mSPNcredit.getText().toString())) {
                            select("0");
                        } else {
                            if (idDeuda > Double.parseDouble(mSPNcredit.getText().toString())) {
                                select("1");
                            }
                            if (idDeuda < Double.parseDouble(mSPNcredit.getText().toString())) {
                                //  Toast.makeText(getApplicationContext(), "El ingreso sobrepasa la deuda", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No tiene Deuda por cobrar",Toast.LENGTH_SHORT).show();
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

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
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