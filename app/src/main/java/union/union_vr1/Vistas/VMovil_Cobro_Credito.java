package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.AsyncTask.ExportMain;
import union.union_vr1.AsyncTask.SolicitarAutorizacionCobros;
import union.union_vr1.R;
import union.union_vr1.Servicios.ServiceExport;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.CursorAdapter_Cobros_Establecimiento;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Cobros_Manuales;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Impresion_Cobros;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.RoundedLetterView;
import union.union_vr1.Utils.Utils;


import static union.union_vr1.R.layout.prompts_cobros;

public class VMovil_Cobro_Credito extends Activity implements OnClickListener, Validator.ValidationListener, DatePickerDialog.OnDateSetListener {

    private DbAdapter_Temp_Session session;
    String COMPROID;
    int IDCOMPROBANTEVENTA;
    int PLANPAGO;
    int PLANPAGODETALLE;
    private Cursor cursor, cursorx;
    private SimpleCursorAdapter dataAdapter;
    final Context context = this;
    private DbAdapter_Comprob_Cobro dbHelper;
    private Button mActualiz;//, mCancelar;
    private TextView mSPNcredit;
    private double valbaimp, valimpue, valtotal;
    private String estabX;
    private double valCredito;
    private int pase = 0;
    private TextView textViewFecha;

    int idPlanPago;
    int idPlanPagoDetalle;
    String tipoDoc;
    String doc;
    int comprobanteVenta;
    private String idCobro = "-1";
    private String Estado;
    private String idEstado;
    private String idMontoCancelado;
    private double idVal1, idVal2, idDeuda, idValNew;
    private int valIdCredito = 0;
    private DBAdapter_Temp_Autorizacion_Cobro dbAutorizacionCobro;
    private DbAdapter_Comprob_Cobro dbComprobanteCobro;

    private Context mContext;
    private Activity mainActivity;
    private String TIPODOCUMENTO;

    //SLIDING MENU VENTAS
    private DbGastos_Ingresos dbGastosIngresos;
    private DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
    private DbAdapter_Agente dbHelperAgente;
    private DbAdapter_Cobros_Manuales dbAdapter_cobros_manuales;


    SlidingMenu menu;
    View layoutSlideMenu;
    TextView textViewSlidePrincipal;
    TextView textViewSlideCliente;
    TextView textviewSlideCobranzas;
    TextView textviewSlideGastos;
    TextView textviewSlideResumen;
    TextView textviewSlideARendir;
    TextView textViewSlideNombreAgente;
    TextView textViewSlideNombreRuta;
    TextView textViewSlideCargar;


    Button buttonSlideNroEstablecimiento;

    int slideIdAgente = 0;
    int slideIdLiquidacion = 0;


    String slideNombreRuta = "";
    int slideNumeroEstablecimientoxRuta = 0;
    String slideNombreAgente = "";

    Double slide_emitidoTotal = 0.0;
    Double slide_pagadoTotal = 0.0;
    Double slide_cobradoTotal = 0.0;

    Double slide_totalRuta = 0.0;
    Double slide_totalPlanta = 0.0;
    Double slide_ingresosTotales = 0.0;
    Double slide_gastosTotales = 0.0;
    Double slide_aRendir = 0.0;

    //SLIDE VENTAS
    TextView textViewSlideNombreEstablecimiento;
    Button buttonSlideVentaDeHoy;
    Button buttonSlideDeudaHoy;
    TextView textViewSlideVenta;
    TextView textViewSlideCobro;
    TextView textViewSlideMantenimiento;
    TextView textViewSlideCanjesDevoluciones;

    int slideIdEstablecimiento;

    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DbAdapter_Comprob_Venta dbAdapter_comprob_venta;

    @Required(order = 5, messageResId = R.string.requerido_input)
    @NumberRule(order = 6, type = NumberRule.NumberType.DOUBLE, messageResId = R.string.requerido_input)
    private EditText cantidadHoy;
    private Validator validator;

    private boolean estado;


    private DbAdapter_Impresion_Cobros dbAdapter_impresion_cobros;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        estado = false;
        session = new DbAdapter_Temp_Session(this);
        session.open();
        mainActivity = this;

        mContext = this;
        validator = new Validator(this);
        validator.setValidationListener(this);
        Bundle bundle = getIntent().getExtras();
        estabX = bundle.getString("idEstabX");
        dbAutorizacionCobro = new DBAdapter_Temp_Autorizacion_Cobro(this);
        dbAutorizacionCobro.open();
        dbComprobanteCobro = new DbAdapter_Comprob_Cobro(this);
        dbComprobanteCobro.open();
        setContentView(R.layout.princ_cobro_credito);
        dbHelper = new DbAdapter_Comprob_Cobro(this);
        dbHelper.open();
        dbAdapter_impresion_cobros = new DbAdapter_Impresion_Cobros(this);
        dbAdapter_impresion_cobros.open();
        //dbHelper.deleteAllComprobCobros();
        //dbHelper.insertSomeComprobCobros();
        mSPNcredit = (TextView) findViewById(R.id.VCCR_SPNcredit);

        mActualiz = (Button) findViewById(R.id.VCCR_BTNactualiz);
        mActualiz.setOnClickListener(this);

        //mCancelar = (Button) findViewById(R.id.VCCR_BTNcancelar);
        //mCancelar.setOnClickListener(this);
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSPNcredit.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        displayListViewVCC();


        //SLIDING MENU
        dbGastosIngresos = new DbGastos_Ingresos(this);
        dbGastosIngresos.open();

        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(this);
        dbAdapter_informe_gastos.open();

        dbHelperAgente = new DbAdapter_Agente(this);
        dbHelperAgente.open();

        dbAdapter_cobros_manuales = new DbAdapter_Cobros_Manuales(this);
        dbAdapter_cobros_manuales.open();


        //VENTAS
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(this);
        dbAdaptert_evento_establec.open();

        dbAdapter_comprob_venta = new DbAdapter_Comprob_Venta(this);
        dbAdapter_comprob_venta.open();

        //SLIDING MENU
        showSlideMenu(this);

        showHeader();
    }

    private void showHeader() {

        TextView textViewNombreEstablecimiento = (TextView) findViewById(R.id.completeName);
        RoundedLetterView letter = (RoundedLetterView) findViewById(R.id.letter);

        Cursor cursorEstablecimiento = dbAdaptert_evento_establec.fetchEstablecsById("" + slideIdEstablecimiento);
        cursorEstablecimiento.moveToFirst();
        String nombreEstablecimiento = "";
        if (cursorEstablecimiento.getCount() > 0) {
            nombreEstablecimiento = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_nom_establec));
        }
        textViewNombreEstablecimiento.setText(nombreEstablecimiento);
        if (nombreEstablecimiento.length() == 0) {
            letter.setTitleText("A");
        } else {
            letter.setTitleText(nombreEstablecimiento.substring(0, 1).toUpperCase());
        }
    }

    private void displayListViewVCC() {

        Cursor cursor = dbHelper.fetchAllComprobCobrosByEst(estabX);


        Log.d("COBRO DE CRÉDITO", "ESTABLECIMIENTO ID:" + estabX);
        CursorAdapter_Cobros_Establecimiento adapterCobros = new CursorAdapter_Cobros_Establecimiento(getApplicationContext(), cursor);

        final ListView listView = (ListView) findViewById(R.id.VCCR_LSTcresez);
        // Assign adapter to ListView


        if (cursor.getCount() == 0) {

            if (listView.getFooterViewsCount() < 1) {
                View headerSinDatos = getLayoutInflater().inflate(R.layout.header_datos_vacios, null);
                listView.addFooterView(headerSinDatos, null, false);
                mActualiz.setEnabled(false);
            }

        } else if (cursor.getCount() < 0) {
            if (listView.getFooterViewsCount() < 1) {
                View headerSinDatos = getLayoutInflater().inflate(R.layout.header_datos_vacios, null);
                listView.addFooterView(headerSinDatos, null, false);

                mActualiz.setEnabled(false);
            }
        }
        listView.setAdapter(adapterCobros);
        //----------------------------------------------------------------


        //-----------------------------------------------------------------------------------------

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set


                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                DecimalFormat df = new DecimalFormat("0.00");
                // Get the state's capital from this row in the database.
                idEstado = cursor.getString(cursor.getColumnIndexOrThrow("cc_in_estado_cobro"));
                idCobro = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                idMontoCancelado = cursor.getString(cursor.getColumnIndexOrThrow("cc_re_monto_cobrado"));
                idVal1 = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_a_pagar"));
                idVal2 = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_cobrado"));
                idDeuda = idVal1;
                mSPNcredit.setText(Utils.formatDouble(idDeuda));
//1

                COMPROID = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_doc));
                IDCOMPROBANTEVENTA = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_comprob));
                PLANPAGO = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_plan_pago));
                PLANPAGODETALLE = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_plan_pago_detalle));
                TIPODOCUMENTO = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_desc_tipo_doc));


                if (Integer.parseInt(idEstado) == 1 || Integer.parseInt(idEstado) == 2) {
                    Estado = "Pendiente " + Utils.formatDouble(idDeuda);
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


    private void inicioAutorizacion(final ListView listView) {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                mSPNcredit.setText("");
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                idVal1 = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_a_pagar"));
                idVal2 = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_cobrado"));
                Log.d("DECIMALES", "" + idVal1 + "-" + idVal2);

                String idComprobante = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_cob_historial));
                Log.d("IDCOMPROBANTE", "" + idComprobante);
                int estado = dbComprobanteCobro.verProceso(idComprobante);

                switch (estado) {
                    case 0:
                        autorizacion(cursor, i);
                        Toast.makeText(getApplicationContext(), "Ya realizo prorroga para este comprobante", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(getApplicationContext(), "Ya realizo prorroga para este comprobante", Toast.LENGTH_SHORT).show();
                        break;
                    case 10:
                        Toast.makeText(getApplicationContext(), "Ya realizo prorroga para este comprobante", Toast.LENGTH_SHORT).show();
                        break;
                }


                return false;
            }
        });
    }

    public void select() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Cobro");

        // set dialog message
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("¿Esta Seguro de Realizar el cobro?")
                .setCancelable(false)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Double suma = (Double.parseDouble(Utils.replaceComa(mSPNcredit.getText().toString())));
                        // idValNew = Double.parseDouble(mSPNcredit.getText().toString()) + idVal2;
                        idValNew = suma;
                        long h = dbHelper.updateComprobCobrosCan(idCobro, getDatePhone(), getTimePhone(), idValNew, "0");
                        if (h > 0) {
                            //Cobros totales
                            Log.d("IDIDCOMPROBANTE", "" + IDCOMPROBANTEVENTA);
                            String esta = dbAdaptert_evento_establec.getNameEstablec(Integer.parseInt(estabX));
                            long a = dbAdapter_impresion_cobros.createImprimir(Integer.parseInt(idCobro), Integer.parseInt(estabX), idValNew, Constants.COBRO_NORMAL, getDatePhone(), esta, COMPROID, getDatePhone() + " " + getTimePhone(), slideIdLiquidacion + "", 0, "", IDCOMPROBANTEVENTA, PLANPAGO, PLANPAGODETALLE, Constants.COBRO_ESTADO_TOTAL, TIPODOCUMENTO);
                            Log.d("VMovil_Cobor", "" + a);
                        }
                        Toast.makeText(getApplicationContext(),
                                "Actualizado", Toast.LENGTH_SHORT).show();


                        if (conectadoWifi() || conectadoRedMovil()) {
                            Intent intentExportService = new Intent(mainActivity, ServiceExport.class);
                            intentExportService.setAction(Constants.ACTION_EXPORT_SERVICE);
                            startService(intentExportService);
                        }

                        startActivity(new Intent(getApplicationContext(), VMovil_BluetoothImpCobros.class).putExtra("idComprobante", "" + idCobro).putExtra("importe", "" + idValNew));
//<
                        //displayListViewVCC();
                        idCobro = "-1";
                        mSPNcredit.setText("0.0");
                        displayListViewVCC();

                    }

                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

        Intent returnAc = new Intent(mContext, VMovil_Evento_Establec.class);
        returnAc.putExtra("idEstab", estabX);
        finish();
        startActivity(returnAc);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnAc = new Intent(mContext, VMovil_Evento_Establec.class);
        returnAc.putExtra("idEstab", estabX);
        finish();
        startActivity(returnAc);
    }

    private void autorizacion(Cursor cursor, int p) {
        Cursor cursorEstablecimiento = dbAdaptert_evento_establec.fetchEstablecsById(estabX);
        cursorEstablecimiento.moveToFirst();

        //Variables Operacion
        final Double[] valorProrroga = {0.0, 0.0};
        //Obteniendo Datos del Cursor
        cursor.moveToPosition(p);
        final int IDHISTOCOBRO = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_cob_historial));
        final String COMPROBANTE = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_doc));
        final String idComprobante = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_comprobante_cobro));
        final String nombreEstablec = cursor.getString(cursor.getColumnIndexOrThrow("ee_te_nom_establec"));
        comprobanteVenta = cursor.getInt(cursor.getColumnIndexOrThrow("cc_in_id_comprob"));
        idPlanPago = cursor.getInt(cursor.getColumnIndexOrThrow("cc_in_id_plan_pago"));
        idPlanPagoDetalle = cursor.getInt(cursor.getColumnIndexOrThrow("cc_in_id_plan_pago_detalle"));
        tipoDoc = cursor.getString(cursor.getColumnIndexOrThrow("cc_te_desc_tipo_doc"));
        doc = cursor.getString(cursor.getColumnIndexOrThrow("cc_te_doc"));
        idEstado = cursor.getString(cursor.getColumnIndexOrThrow("cc_in_estado_cobro"));
        idCobro = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        idVal1 = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_a_pagar"));
        idVal2 = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_cobrado"));
        String fecha = cursor.getString(cursor.getColumnIndexOrThrow("cc_te_fecha_programada"));
        int diasCreadito = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_dias_credito));


        idDeuda = idVal1;
        Log.d("DECIMALES SUMA COBROS", ": " + idVal1 + "-" + idVal2);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout_cobros = inflater.inflate(R.layout.prompts_cobros, null);
        //Iniciando y Parseando Widgets
        final TextView descripcion = (TextView) layout_cobros.findViewById(R.id.textOperacionSol);
        descripcion.setText("Prorroga para la deuda: " + Utils.formatDouble(idVal1) + "");

        cantidadHoy = (EditText) layout_cobros.findViewById(R.id.cantidadHoy);
        final TextView cantidadProrroga = (TextView) layout_cobros.findViewById(R.id.montoProrroga);
        textViewFecha = (TextView) layout_cobros.findViewById(R.id.textFecha);

        textViewFecha.setText(Utils.sumarRestarDiasFecha(Utils.getDateConvert(fecha), diasCreadito));
        cantidadProrroga.setEnabled(false);
        //Calculando el monto Prorroga.
        cantidadHoy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence valorHoy, int i, int i2, int i3) {

                Log.d("Parametros", "idDeuda" + idDeuda + "Monto a Pagar:" + idVal1 + "Monto Pagado" + idVal2 + "");

                if (valorHoy.toString().length() > 0) {


                    try {

                        valorProrroga[0] = idDeuda - Double.parseDouble(Utils.replaceComa(valorHoy.toString()));

                        if (valorProrroga[0] < 0 || valorProrroga[0] >= idDeuda || Double.parseDouble(Utils.replaceComa(valorHoy.toString())) == idDeuda) {
                            valorProrroga[1] = Double.parseDouble(Utils.replaceComa(valorHoy.toString()));
                            cantidadProrroga.setError("Error en Valor");
                            cantidadProrroga.setText("");
                            cantidadHoy.setText("");
                        } else {
                            cantidadProrroga.setError(null);
                            cantidadProrroga.setText(Utils.formatDouble(valorProrroga[0]));
                        }

                    } catch (NumberFormatException e) {
                        cantidadProrroga.setError("Error en Valor");
                        cantidadHoy.setText("");
                        cantidadProrroga.setText("");
                    }


                } else {
                    cantidadProrroga.setError("Error en Valor");
                }

            }

            @Override
            public void afterTextChanged(Editable valorHoy) {

            }
        });
        builder.setView(layout_cobros);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.si,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        validator.validate();
                        if (estado) {
                            DecimalFormat df = new DecimalFormat("0.00");
                            double valorPago = Double.parseDouble(Utils.replaceComa(cantidadHoy.getText().toString()));
                            double valorCobrar = Double.parseDouble(Utils.replaceComa(cantidadProrroga.getText().toString()));
                            String fechaProgramada = textViewFecha.getText().toString();
                            if (valorPago != 0.0 && valorCobrar != 0.0) {

                                int idAgente = session.fetchVarible(1);
                                Log.e("DATOSENVIADOS", "" + idAgente + "-" + 4 + "-" + 1 + "-" + comprobanteVenta + "-" + valorCobrar + "-" + valorPago + "-" + Integer.parseInt(estabX) + "-" + Constants._CREADO + "-" + idComprobante + "-" + nombreEstablec);
                                //long idAutorizacion = dbAutorizacionCobro.createTempAutorizacionPago(idAgente, 4, 1, comprobanteVenta, valorCobrar, valorPago, Integer.parseInt(estabX), Constants._CREADO, idComprobante, nombreEstablec);

                                int estad = dbComprobanteCobro.updateComprobCobrosSN(idComprobante, fechaProgramada, valorPago, valorCobrar);
                                if (estad > 0) {
//Cobors parciales.
                                    long a = dbAdapter_impresion_cobros.createImprimir(IDHISTOCOBRO, Integer.parseInt(estabX), valorPago, Constants.COBRO_NORMAL, getDatePhone(), nombreEstablec,COMPROBANTE, getDatePhone() + " " + getTimePhone(), slideIdLiquidacion + "", 0, idComprobante, comprobanteVenta, idPlanPago, idPlanPagoDetalle, Constants.COBRO_ESTADO_PARCIAL,tipoDoc);
                                    //new  SolicitarAutorizacionCobros(getApplicationContext()).execute(idAgente + "", 4 + "", 1 + "", comprobanteVenta + "", valorCobrar + "", valorPago + "", estabX, Constants._CREADO + "", idComprobante + "", nombreEstablec + "",idAutorizacion+"");
                                    //   Back();
                                    Log.d("VMovil_Cobor", "" + a + "-" + estabX);
                                    startActivity(new Intent(getApplicationContext(), VMovil_BluetoothImpCobros.class).putExtra("idComprobante", "" + IDHISTOCOBRO).putExtra("importe", "" + valorPago));
                                    finish();
                                } else {
                                    Utils.setToast(getApplicationContext(), "Ocurrio un error", R.color.rojo);
                                }
                                if (conectadoWifi() || conectadoRedMovil()) {
                                    // new ExportMain(VMovil_Cobros_Totales.this).execute();
                                    Intent intent = new Intent(getApplicationContext(), ServiceExport.class);
                                    intent.setAction(Constants.ACTION_EXPORT_SERVICE);
                                    startService(intent);
                                }


                            } else {
                                Utils.setToast(getApplicationContext(), "Por favor Ingrese Todos los Campos", R.color.rojo);
                            }
                        } else {

                            Utils.setToast(getApplicationContext(), "Por favor Ingrese Todos los Campos", R.color.rojo);
                        }

                    }
                })
                .setNegativeButton(R.string.no,
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

                if (idEstado != null) {
                    DecimalFormat df = new DecimalFormat("0.00");

                    if (idEstado.equals("1")) {
                        if (mSPNcredit.getText().equals("") || idCobro.equals("-1")) {
                            Toast.makeText(getApplicationContext(), "Tiene que Seleccionar una Deuda", Toast.LENGTH_SHORT).show();
                        }
                        if (Double.parseDouble(Utils.replaceComa(df.format(idDeuda))) == Double.parseDouble(Utils.replaceComa(mSPNcredit.getText().toString()))) {
                            Log.d("VCCR_BTNactualiz", "VCCR_BTNactualiz");
                            select();
                        } else {
                            if (idDeuda > Double.parseDouble(Utils.replaceComa(mSPNcredit.getText().toString()))) {

                            }
                            if (idDeuda < Double.parseDouble(Utils.replaceComa(mSPNcredit.getText().toString()))) {
                                //  Toast.makeText(getApplicationContext(), "El ingreso sobrepasa la deuda", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Seleccione Documento a Pagar", Toast.LENGTH_SHORT).show();
                }
                //dbHelper.updateComprobCobrosCan(String.valueOf(valIdCredito),getDatePhone(),getTimePhone(),Double.parseDouble(mSPNcredit.getText().toString()));
                displayListViewVCC();
                break;
            /*case R.id.VCCR_BTNcancelar:
                Back();
                break;
                */
            //SLIDING MENU
            case R.id.slide_textViewCargarInventario:
                Intent cInventario = new Intent(this, VMovil_Cargar_Inventario.class);
                startActivity(cInventario);
                break;
            case R.id.slide_textviewPrincipal:
                Intent ip1 = new Intent(this, VMovil_Evento_Indice.class);
                finish();
                startActivity(ip1);
                break;
            case R.id.slide_textViewClientes:
                Intent ic1 = new Intent(this, VMovil_Menu_Establec.class);
                finish();
                startActivity(ic1);
                break;
            case R.id.slide_textViewCobranza:
                Intent cT1 = new Intent(this, VMovil_Cobros_Totales.class);
                finish();
                startActivity(cT1);
                break;
            case R.id.slide_TextViewGastos:
                Intent ig1 = new Intent(this, VMovil_Evento_Gasto.class);
                finish();
                startActivity(ig1);
                break;
            case R.id.slide_textViewResumen:
                Intent ir1 = new Intent(this, VMovil_Resumen_Caja.class);
                finish();
                startActivity(ir1);
                break;
            case R.id.slide_textViewARendir:

                break;
            //SLIDING MENU VENTAS
            case R.id.slideVentas_buttonVentaCosto:
                Intent ivc1 = new Intent(this, VMovil_Venta_Comprob.class);
                ivc1.putExtra("idEstabX", "" + slideIdEstablecimiento);
                startActivity(ivc1);
                break;
            case R.id.slideVentas_buttonDeudas:
                menu.toggle();
                break;
            case R.id.slideVentas_textViewVenta:
                Intent iv1 = new Intent(this, VMovil_Venta_Cabecera.class);
                finish();
                iv1.putExtra("idEstabX", "" + slideIdEstablecimiento);
                startActivity(iv1);
                break;
            case R.id.slideVentas_textViewMantenimiento:
                Intent im1 = new Intent(this, VMovil_Venta_Comprob.class);
                im1.putExtra("idEstabX", "" + slideIdEstablecimiento);
                startActivity(im1);
                break;
            case R.id.slideVentas_textviewCanjesDevoluciones:
                menu.toggle();
                break;
            case R.id.slideVentas_textViewCliente:
                menu.toggle();
            default:
                break;
        }
    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("0.00");
        return Double.valueOf(twoDForm.format(d));
    }

    private String getTimeAndDate() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        String formatteDate = df.format(date);
        return formatteDate;
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

    //SLIDING MENU
    public void showSlideMenu(Activity activity) {
        layoutSlideMenu = View.inflate(activity, R.layout.slide_menu_ventas, null);
        // configure the SlidingMenu
        menu = new SlidingMenu(activity);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.space_slide);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.space_slide);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(layoutSlideMenu);

        textViewSlideNombreAgente = (TextView) findViewById(R.id.slide_textViewNombreAgente);
        textViewSlideNombreRuta = (TextView) findViewById(R.id.slide_textViewNombreRuta);
        buttonSlideNroEstablecimiento = (Button) findViewById(R.id.slide_buttonNroEstablecimiento);

        textViewSlidePrincipal = (TextView) findViewById(R.id.slide_textviewPrincipal);
        textViewSlideCliente = (TextView) findViewById(R.id.slide_textViewClientes);
        textviewSlideCobranzas = (TextView) findViewById(R.id.slide_textViewCobranza);
        textviewSlideGastos = (TextView) findViewById(R.id.slide_TextViewGastos);
        textviewSlideResumen = (TextView) findViewById(R.id.slide_textViewResumen);
        textviewSlideARendir = (TextView) findViewById(R.id.slide_textViewARendir);
        textViewSlideCargar = (TextView) findViewById(R.id.slide_textViewCargarInventario);
        textViewSlideCargar.setOnClickListener(this);


        textViewSlideNombreEstablecimiento = (TextView) findViewById(R.id.slideVentas_textViewCliente);
        buttonSlideVentaDeHoy = (Button) findViewById(R.id.slideVentas_buttonVentaCosto);
        buttonSlideDeudaHoy = (Button) findViewById(R.id.slideVentas_buttonDeudas);
        textViewSlideVenta = (TextView) findViewById(R.id.slideVentas_textViewVenta);
        //COBRAR
        textViewSlideMantenimiento = (TextView) findViewById(R.id.slideVentas_textViewMantenimiento);
        textViewSlideCanjesDevoluciones = (TextView) findViewById(R.id.slideVentas_textviewCanjesDevoluciones);


        textViewSlidePrincipal.setOnClickListener(this);
        textViewSlideCliente.setOnClickListener(this);
        textviewSlideCobranzas.setOnClickListener(this);
        textviewSlideGastos.setOnClickListener(this);
        textviewSlideResumen.setOnClickListener(this);
        textviewSlideARendir.setOnClickListener(this);

        //VENTAS
        buttonSlideDeudaHoy.setOnClickListener(this);
        buttonSlideVentaDeHoy.setOnClickListener(this);
        textViewSlideVenta.setOnClickListener(this);
        textViewSlideMantenimiento.setOnClickListener(this);
        textViewSlideCanjesDevoluciones.setOnClickListener(this);
        textViewSlideNombreEstablecimiento.setOnClickListener(this);
        slideIdAgente = session.fetchVarible(1);
        slideIdLiquidacion = session.fetchVarible(3);
        slideIdEstablecimiento = session.fetchVarible(2);


        changeDataSlideMenu();


    }

    //SLIDING MENU
    public void changeDataSlideMenu() {

        //INICIALIZAMOS OTRA VEZ LAS VARIABLES
        slide_emitidoTotal = 0.0;
        slide_pagadoTotal = 0.0;
        slide_cobradoTotal = 0.0;
        slide_totalRuta = 0.0;
        slide_totalPlanta = 0.0;
        slide_ingresosTotales = 0.0;
        slide_gastosTotales = 0.0;
        slide_aRendir = 0.0;

        // AGENTE, RUTA Y ESTABLECIMIENTOS
        Cursor cursorAgente = dbHelperAgente.fetchAgentesByIds(slideIdAgente, slideIdLiquidacion);
        cursorAgente.moveToFirst();

        if (cursorAgente.getCount() > 0) {
            slideNombreRuta = cursorAgente.getString(cursorAgente.getColumnIndexOrThrow(dbHelperAgente.AG_nombre_ruta));
            slideNumeroEstablecimientoxRuta = cursorAgente.getInt(cursorAgente.getColumnIndexOrThrow(dbHelperAgente.AG_nro_bodegas));
            slideNombreAgente = cursorAgente.getString(cursorAgente.getColumnIndexOrThrow(dbHelperAgente.AG_nombre_agente));
        }

        //INGRESOS
        Cursor cursorResumen = dbGastosIngresos.listarIngresosGastos(slideIdLiquidacion);
        cursorResumen.moveToFirst();
        for (cursorResumen.moveToFirst(); !cursorResumen.isAfterLast(); cursorResumen.moveToNext()) {
            //int n = cursorResumen.getInt(cursorResumen.getColumnIndexOrThrow("n"));
            Double emitido = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("emitidas"));
            Double pagado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("pagado"));
            Double cobrado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("cobrado"));
            //nTotal += n;
            slide_emitidoTotal += emitido;
            slide_pagadoTotal += pagado;
            slide_cobradoTotal += cobrado;
        }
        //GASTOS
        Utils utils = new Utils();
        Cursor cursorTotalGastos = dbAdapter_informe_gastos.resumenInformeGastos(utils.getDayPhone());

        for (cursorTotalGastos.moveToFirst(); !cursorTotalGastos.isAfterLast(); cursorTotalGastos.moveToNext()) {
            Double rutaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("RUTA"));
            Double plantaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("PLANTA"));

            slide_totalRuta += rutaGasto;
            slide_totalPlanta += plantaGasto;
        }

        slide_ingresosTotales = slide_cobradoTotal + slide_pagadoTotal;
        slide_gastosTotales = slide_totalRuta;
        slide_aRendir = slide_ingresosTotales - slide_gastosTotales;


        //MOSTRAMOS EN EL SLIDE LOS DATOS OBTENIDOS
        DecimalFormat df = new DecimalFormat("0.00");
        textViewSlideNombreAgente.setText("" + slideNombreAgente);
        textViewSlideNombreRuta.setText("" + slideNombreRuta);
        buttonSlideNroEstablecimiento.setText("" + slideNumeroEstablecimientoxRuta);
        textviewSlideARendir.setText("Efectivo a Rendir S/. " + df.format(slide_aRendir));


        //DATA VENTAS
        Cursor cursorEstablecimiento = dbAdaptert_evento_establec.listarEstablecimientosByID(slideIdEstablecimiento, slideIdLiquidacion);

        cursorEstablecimiento.moveToFirst();

        if (cursorEstablecimiento.getCount() > 0) {

            String nombre_establecimiento = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbAdaptert_evento_establec.EE_nom_establec));
            String nombre_cliente = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbAdaptert_evento_establec.EE_nom_cliente));
            int id_estado_atencion = Integer.parseInt(cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbAdaptert_evento_establec.EE_id_estado_atencion)));
            double deudaTotal = cursorEstablecimiento.getDouble(cursorEstablecimiento.getColumnIndexOrThrow("cc_re_monto_a_pagar"));


            textViewSlideNombreEstablecimiento.setText("" + nombre_establecimiento);
            buttonSlideDeudaHoy.setText("" + df.format(deudaTotal));
        }

        Cursor cursorVentasTotales = dbAdapter_comprob_venta.getTotalVentaByIdEstablecimientoAndLiquidacion(slideIdEstablecimiento, slideIdLiquidacion);
        cursorVentasTotales.moveToFirst();
        if (cursorVentasTotales.getCount() > 0) {
            Double total = cursorVentasTotales.getDouble(cursorVentasTotales.getColumnIndexOrThrow("total"));
            buttonSlideVentaDeHoy.setText("" + df.format(total));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeDataSlideMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cobros_manuales, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cobros) {
            displayModalCobrosManuales();
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayModalCobrosManuales() {
        final View layout_Cobros_Manuales = View.inflate(this, R.layout.prompts_cobros_manuales, null);
        final Spinner spinnerTipoCobro = (Spinner) layout_Cobros_Manuales.findViewById(R.id.cobrosManualTipo_cobro_manual);
        final EditText editTextSerie = (EditText) layout_Cobros_Manuales.findViewById(R.id.cobrosManualSerie);
        final EditText editTextNumero = (EditText) layout_Cobros_Manuales.findViewById(R.id.cobrosManualNumero);
        final EditText editTextImporte = (EditText) layout_Cobros_Manuales.findViewById(R.id.cobrosManualImporte);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Cobros Manuales");

        // set dialog message
        AlertDialog.Builder builder = alertDialogBuilder
                .setView(layout_Cobros_Manuales)
                .setCancelable(false)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NumberFormat formatter = new DecimalFormat("0.00");
                        //Posicion 0 == cobro total, 1 == Parcial
                        Log.d("ITEMSELECTED", spinnerTipoCobro.getSelectedItemPosition() + "");
                        int itemTipo = spinnerTipoCobro.getSelectedItemPosition();
                        String serie = editTextSerie.getText().toString();
                        String numeroString = editTextNumero.getText().toString();
                        String importeString = editTextImporte.getText().toString();
                        if (serie.equals("") || numeroString.equals("") || importeString.equals("") || serie == null || numeroString == null || importeString == null) {
                            Toast.makeText(getApplicationContext(), "Debe completar todos los campos", Toast.LENGTH_LONG).show();
                        } else {
                            int numero = Integer.parseInt(numeroString);
                            Double importe = Double.parseDouble(Utils.replaceComa(formatter.format(Double.parseDouble(Utils.replaceComa(importeString)))));
                            estaSeguroCobrar(serie, numero, importe, itemTipo);

                        }

                    }

                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

        ArrayAdapter<CharSequence> adapterTipoOperacion = ArrayAdapter.createFromResource(this, R.array.tipo_cobros_manuales, android.R.layout.simple_spinner_item);

        adapterTipoOperacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTipoCobro.setAdapter(adapterTipoOperacion);
    }

    private void estaSeguroCobrar(final String serie, final int numero, final Double importe, final int tipoCobro) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("¿Seguro?");

        // set dialog message
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("¿Esta seguro de cobrar: S/  " + importe + " ?")
                .setCancelable(false)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String cateMovimiento = "";
                        if (tipoCobro > 0) {
                            cateMovimiento = "t";
                        } else {
                            cateMovimiento = "p";
                        }
                        long l = dbAdapter_cobros_manuales.createCobrosManuales(3, importe, getTimeAndDate(), cateMovimiento, slideIdAgente, serie, numero, dbAdaptert_evento_establec.getNameCliente(Integer.parseInt(estabX)), Integer.parseInt(estabX), getDatePhone(), getTimePhone());
                        if (l > 0) {
                            //Cobros Manuales
                            String NUMERO = "";
                            String chater = String.valueOf(numero);
                            if (chater.length() > 8) {
                                NUMERO = serie + "-" + numero;
                            } else {
                                String NUMERO_COMPROBANTE = String.format("%08d", numero);
                                NUMERO = serie + "-" + NUMERO_COMPROBANTE;
                            }
                            long h = dbAdapter_impresion_cobros.createImprimir(Integer.parseInt(l + ""), slideIdEstablecimiento, importe, Constants.COBRO_MANUAL, getDatePhone(), dbAdaptert_evento_establec.getNameEstablec(Integer.parseInt(estabX)), "" + NUMERO, getDatePhone() + " " + getTimePhone(), slideIdLiquidacion + "", 0, "", numero, 0, 0, 0, "Otros");
                            Log.d("COBROS", "" + h);
                            if (conectadoRedMovil() || conectadoWifi()) {

                                Intent intentExportService = new Intent(mainActivity, ServiceExport.class);
                                intentExportService.setAction(Constants.ACTION_EXPORT_SERVICE);
                                startService(intentExportService);

                                startActivity(new Intent(getApplicationContext(), VMovil_BluetoothImpCobrosManuales.class).putExtra("idComprobante", "" + l));
                            } else {
                                Toast.makeText(getApplicationContext(), "Guardado correctamente, se exportara cuando tengas conexion a internet", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

    protected Boolean conectadoWifi() {
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

    protected Boolean conectadoRedMovil() {
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

    @Override
    public void onValidationSucceeded() {
        estado = true;
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            estado = false;
            ((EditText) failedView).setError(message);
            Utils.setToast(getApplicationContext(), "Revise los campos", R.color.rojo);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int mont = monthOfYear + 1;
        textViewFecha.setText(year + "/" + mont + "/" + dayOfMonth);
    }
}