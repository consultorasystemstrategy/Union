package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.AsyncTask.ExportMain;
import union.union_vr1.AsyncTask.SolicitarCredito;
import union.union_vr1.BarcodeScanner.IntentIntegrator;
import union.union_vr1.BarcodeScanner.IntentResult;
import union.union_vr1.InputFilterMinMax;
import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Temp_Venta;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Histo_Comprob_Anterior;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.Keyboard;
import union.union_vr1.Utils.MyApplication;
import union.union_vr1.Utils.SoftKeyboard;
import union.union_vr1.Utils.Utils;
import union.union_vr1.VMovil_BluetoothImprimir;

public class VMovil_Venta_Cabecera extends Activity implements OnClickListener{


    private DbAdapter_Temp_Session session;

    private DbAdapter_Histo_Comprob_Anterior dbHelper_Histo_comprob_anterior;
    private DBAdapter_Temp_Venta dbHelper_temp_venta;
    private DbAdapter_Comprob_Venta dbHelper_Comprob_Venta;
    //private DbAdapter_Agente dbHelperAgente;
    private DbAdapter_Comprob_Venta_Detalle dbHelper_Comprob_Venta_Detalle;
    private DbAdapter_Temp_Comprob_Cobro dbHelper_Temp_Comprob_Cobros;
    private DbAdapter_Comprob_Cobro dbHelper_Comprob_Cobros;
    private DbAdapter_Stock_Agente dbHelper_Stock_Agente;
    private DbAdaptert_Evento_Establec dbHelper_Evento_Establecimiento;
    private EditText savedText;

    //VARIABLES AGREGAR PRODUCTOS
    private AutoCompleteTextView autoCompleteTextView;
    private Cursor mCursorStockAgente ;
    private int id_categoria_establecimiento;
    private SimpleCursorAdapter adapterProductos;

    private DbAdapter_Precio dbHelper_Precio;


    private int cantidad;
    private int procedencia = 1;
    private int disponible = 1;
    private String nombre = null;
    private  int id_producto = 0;
    private int valor_unidad = 1;


    private Cursor mCursorScannerProducto;
    private String barcodeScan;
    private String formatScan;

    //fin variables agregar productos



    private String textoVentaImpresion = "";

    private String textoImpresionCabecera = "\n"
            +"    UNIVERSIDAD PERUANA UNION   \n"
            +"     Cent.aplc. Prod. Union     \n"
            +"   C. Central Km 19 Villa Union \n"
            +" Lurigancho-Chosica Fax: 6186311\n"
            +"      Telf: 6186309-6186310     \n"
            +" Casilla 3564, Lima 1, LIMA PERU\n"
            +"         RUC: 20138122256       ";



    private String textoImpresionContenidoLeft = "";
    private String textoImpresionContenidoRight = "";
    private String textoImpresion  = ".\n"
            +"    UNIVERSIDAD PERUANA UNION   \n"
            +"     Cent.aplc. Prod. Union     \n"
            +"   C. Central Km 19 Villa Union \n"
            +" Lurigancho-Chosica Fax: 6186311\n"
            +"      Telf: 6186309-6186310     \n"
            +" Casilla 3564, Lima 1, LIMA PERU\n"
            +"         RUC: 20138122256       \n"
            +"--------------------------------\n";

    private int idEstablecimiento;
    int id_agente_venta;
    private int idLiquidacion;
    private SimpleCursorAdapter simpleCursorAdapter;
    private Spinner spinnerTipoDocumento;
    private Spinner spinnerFormaPago;
    //private Button buttonAgregar;
    private Spinner spinnerDiasCredito;
    private TextView textViewFooterText;
    private TextView textViewFooterTotal;
    private TextView textViewFooterSurtidoStock;
    private TextView textViewFooterSurtidoVenta;
    private Button buttonVender;
    private ListView listView;
    private View header;
    private View headerNombreEstablecimiento;
    private View footer;
    private Activity mainActivity;




    private Context mContext;

    private Double totalFooter;
    private Double base_imponibleFooter;
    private double igvFooter;

    final int id_plan_pago = 0;
    final int id_plan_pago_detalle = 0;
    final String tipo_documento = null;
    final String doc = null;
    final String fecha_cobro = null;
    final String hora_cobro = null;
    final Double monto_cobrado = 0.0;
    final int estado_cobro = 1;// 1 NO ESTÁ COBRADO //JAJA ESTÁ AL REVÉS
    final int id_forma_cobro = 1;
    final String lugar_registro = "movil";
    String diasCredito= null;

    private boolean isEstablecidasCuotas;

    private ExportMain exportMain;

    //KEYBOARD SOFT
    RelativeLayout mainLayout;
    InputMethodManager im;
    SoftKeyboard softKeyboard;

    LinearLayout layoutSpinner;
    LinearLayout layoutButton;

    //SLIDING MENU VENTAS
    private DbGastos_Ingresos dbGastosIngresos;
    private DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
    private DbAdapter_Agente dbHelperAgente;



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


    Button buttonSlideNroEstablecimiento;

    int slideIdAgente = 0;
    int slideIdLiquidacion = 0;



    String slideNombreRuta = "";
    int slideNumeroEstablecimientoxRuta = 0;
    String slideNombreAgente = "";

    Double slide_emitidoTotal = 0.0;
    Double slide_pagadoTotal = 0.0;
    Double slide_cobradoTotal = 0.0;

    Double slide_totalRuta =0.0;
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

    @Override
    protected void onDestroy() {
        exportMain.dismissProgressDialog();
        Log.d("ON DESTROY", "DISMISS PROGRESS DIALOG");
        super.onDestroy();

        //softKeyboard.unRegisterSoftKeyboardCallback();
    }

    @Override
    public void onBackPressed() {
        backPressedDialog(mainActivity, VMovil_Evento_Establec.class).show();
    }


   public Dialog backPressedDialog(final Activity main, final Class clase){
       return new AlertDialog.Builder(main)
               .setTitle("No ha vendido nada")
               .setMessage("¿Está seguro que desea salir?")
               .setNegativeButton(android.R.string.no, null)
               .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!

                       dbHelper_temp_venta.deleteAllTempVentaDetalle();
                       //((MyApplication)mContext.getApplicationContext()).setDisplayedHistorialComprobanteAnterior(false);
                       session.deleteVariable(6);
                       session.createTempSession(6,0);
                       session.deleteVariable(5);
                       session.createTempSession(5,0);

                       finish();
                       Intent intent = new Intent(main,clase);
                       startActivity(intent);
                   }
               }).create();
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Keyboard autoCompleteKeyboardLayout = new Keyboard(this, null);

        //ViewGroup viewGroup = (ViewGroup) autoCompleteKeyboardLayout.getRootView();
        //layoutSpinner = (LinearLayout) findViewById(R.id.layoutSpinner);
        //layoutButton = (LinearLayout) findViewById(R.id.layoutButton);





        //mainLayout = (RelativeLayout) findViewById(R.id.softKeyboardMain); // You must use the layout root
        //im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

/*
Instantiate and pass a callback
*/


        setContentView(R.layout.princ_venta_cabecera);
        //setContentView(viewGroup);

/*
        softKeyboard = new SoftKeyboard(viewGroup, im, layoutButton, layoutSpinner);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged()
        {

            @Override
            public void onSoftKeyboardHide()
            {
                // Code here
                Log.d("KEYBOARD","HIDE");
                mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    mainActivity.findViewById(R.id.layoutButton).setVisibility(View.VISIBLE);
                    mainActivity.findViewById(R.id.layoutSpinner).setVisibility(View.VISIBLE);
                }
            });
            }

            @Override
            public void onSoftKeyboardShow()
            {
                // Code here
                Log.d("KEYBOARD","SHOW");
                mainActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mainActivity.findViewById(R.id.layoutButton).setVisibility(View.GONE);
                        mainActivity.findViewById(R.id.layoutSpinner).setVisibility(View.GONE);
                    }
                });
            }
        });

*/

        //SLIDING MENU
        dbGastosIngresos = new DbGastos_Ingresos(this);
        dbGastosIngresos.open();

        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(this);
        dbAdapter_informe_gastos.open();

        dbHelperAgente = new DbAdapter_Agente(this);
        dbHelperAgente.open();

        //VENTAS
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(this);
        dbAdaptert_evento_establec.open();

        dbAdapter_comprob_venta = new DbAdapter_Comprob_Venta(this);
        dbAdapter_comprob_venta.open();


        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.VCAP_AutoCompleteProductos);

        session = new DbAdapter_Temp_Session(this);
        session.open();

        mainActivity = this;
        exportMain = new ExportMain(mainActivity);

        mContext = this;
        dbHelper_Histo_comprob_anterior = new DbAdapter_Histo_Comprob_Anterior(this);
        dbHelper_Histo_comprob_anterior.open();

        dbHelper_temp_venta = new DBAdapter_Temp_Venta(this);
        dbHelper_temp_venta.open();

        dbHelper_Comprob_Venta = new DbAdapter_Comprob_Venta(this);
        dbHelper_Comprob_Venta.open();

        dbHelper_Precio = new DbAdapter_Precio(this);
        dbHelper_Precio.open();

        dbHelperAgente = new DbAdapter_Agente(this);
        dbHelperAgente.open();

        dbHelper_Comprob_Venta_Detalle  = new DbAdapter_Comprob_Venta_Detalle(this);
        dbHelper_Comprob_Venta_Detalle.open();

        dbHelper_Comprob_Cobros = new DbAdapter_Comprob_Cobro(this);
        dbHelper_Comprob_Cobros.open();

        dbHelper_Temp_Comprob_Cobros = new DbAdapter_Temp_Comprob_Cobro(this);
        dbHelper_Temp_Comprob_Cobros.open();

        dbHelper_Stock_Agente = new DbAdapter_Stock_Agente(this);
        dbHelper_Stock_Agente.open();

        dbHelper_Evento_Establecimiento = new DbAdaptert_Evento_Establec(this);
        dbHelper_Evento_Establecimiento.open();


        idEstablecimiento = session.fetchVarible(2);

        id_agente_venta = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);




        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final Cursor cursorEstablecimiento = dbHelper_Evento_Establecimiento.fetchEstablecsById(""+idEstablecimiento);
        cursorEstablecimiento.moveToFirst();
        id_categoria_establecimiento = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_id_cat_est));

        int idTipoDocCliente = 0;
        if (cursorEstablecimiento.getCount()>0) {
            idTipoDocCliente = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_id_tipo_doc_cliente));
        }
        if (idTipoDocCliente==1){
            spinnerTipoDocumento = (Spinner) findViewById(R.id.VC_spinnerTipoDocumento);
            ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this,R.array.tipoDocumentoBoleta,android.R.layout.simple_spinner_item);
            adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTipoDocumento.setAdapter(adapterTipoDocumento);
        }else if (idTipoDocCliente==2){
            spinnerTipoDocumento = (Spinner) findViewById(R.id.VC_spinnerTipoDocumento);
            ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this,R.array.tipoDocumentoFactura,android.R.layout.simple_spinner_item);
            adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTipoDocumento.setAdapter(adapterTipoDocumento);
        }else{
            spinnerTipoDocumento = (Spinner) findViewById(R.id.VC_spinnerTipoDocumento);
            ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this,R.array.tipoDocumento,android.R.layout.simple_spinner_item);
            adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTipoDocumento.setAdapter(adapterTipoDocumento);
        }







        spinnerFormaPago = (Spinner) findViewById(R.id.VC_spinnerFormaPago);
        final ArrayAdapter<CharSequence> adapterFormaPago = ArrayAdapter.createFromResource(this,R.array.forma_pago,android.R.layout.simple_spinner_item);
        adapterFormaPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormaPago.setAdapter(adapterFormaPago);

        //buttonAgregar = (Button) findViewById(R.id.VC_buttonAgregarProductos);
        buttonVender = (Button) findViewById(R.id.VC_buttonVender);
        buttonVender.setOnClickListener(this);
        //buttonAgregar.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.VC_listView);

        switch (session.fetchVarible(5)){
            case 0:
                isEstablecidasCuotas = false;
                break;
            case 1:
                isEstablecidasCuotas = true;
                break;
            default:
                isEstablecidasCuotas = false;
                break;
        }


        if(isEstablecidasCuotas){

            //buttonAgregar.setClickable(false);
            spinnerFormaPago.setClickable(false);
            listView.setFocusable(false);
            listView.setItemsCanFocus(false);
            listView.setLongClickable(false);
            listView.setEnabled(false);
            listView.setClickable(false);


            ArrayAdapter<CharSequence> adapterFormaPagoCredito = ArrayAdapter.createFromResource(this,R.array.forma_pago_credito,android.R.layout.simple_spinner_item);
            spinnerFormaPago.setAdapter(adapterFormaPagoCredito);

        }

        boolean isDisplayed= false;
        switch (session.fetchVarible(6)){
            case 0:
                isDisplayed = false;
                break;
            case 1:
                isDisplayed = true;
                break;
            default:
                isDisplayed = false;
                break;
        }

        if (isDisplayed) {
            //NO SE MUESTRA EL HISTORIAL DEL COMPROBANTE ANTERIOR COMO SUGERENCIA DE VENTA AL USUARIO
        }else{
            displayHistorialComprobanteAnterior();
        }
        mostrarProductosParaVender();
        spinnerFormaPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String formaDePago = adapterView.getItemAtPosition(i).toString();
                FormaPago formaPago = FormaPago.valueOf(formaDePago);

                switch (formaPago){
                    case Contado:

                        break;
                    case Credito:

                        if (isEstablecidasCuotas){

                        }
                        else {

                            Cursor cursorEstablecimientoCredito = dbHelper_Evento_Establecimiento.fetchEstablecsById(""+idEstablecimiento);
                            cursorEstablecimiento.moveToFirst();
                            int montoCredito = -1;
                            montoCredito = cursorEstablecimientoCredito.getInt(cursorEstablecimientoCredito.getColumnIndexOrThrow(dbHelper_Evento_Establecimiento.EE_monto_credito));
                            switch (montoCredito){
                                case -1:

                                    break;
                                case 0:

                                    if (conectadoWifi()||conectadoRedMovil()) {
                                        new AlertDialog.Builder(mContext)
                                                .setTitle("Ops, No cuenta con crédito")
                                                .setMessage("" +
                                                        "¿Desea solicitar crédito?")
                                                .setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        spinnerFormaPago.setAdapter(adapterFormaPago);
                                                    }
                                                })
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialogSolicitarCredito().show();
                                                    }
                                                }).create().show();

                                    }else{
                                        Toast.makeText(mContext, "Sin crédito y sin conexión", Toast.LENGTH_SHORT).show();
                                    }

                                    break;
                                default:

                                    new AlertDialog.Builder(mContext)
                                            .setTitle("Está seguro que es toda su venta")
                                            .setMessage("Si define las cuotas ya no podrá agregar productos a la venta, ni eliminarlos\n" +
                                                    "¿Está seguro que está todo listo?")
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    spinnerFormaPago.setAdapter(adapterFormaPago);
                                                }
                                            })
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent = new Intent(getApplicationContext(), VMovil_Venta_Cabecera_PlanPagos.class);
                                                    intent.putExtra("total", totalFooter);
                                                    finish();
                                                    startActivity(intent);
                                                }
                                            }).create().show();

                                    break;
                            }

                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tipoDocumento = spinnerTipoDocumento.getSelectedItem().toString();
                TipoDocumento tipoDocumento1 = TipoDocumento.valueOf(tipoDocumento);
                DecimalFormat df = new DecimalFormat("#.00");

                switch (tipoDocumento1){
                    case factura:
                        textViewFooterText.setText("Total :\n" +
                                "Base imponible :\n" +
                                "IGV :");

                        textViewFooterTotal.setText(" S/. "+df.format(totalFooter)+"\n" +
                                "S/. "+df.format(base_imponibleFooter)+ "\n" +
                                "S/. "+df.format(igvFooter));

                        break;
                    case boleta:

                        textViewFooterText.setText("Total :");
                        textViewFooterTotal.setText("S/. "+df.format(totalFooter));

                        break;
                    case ficha:
                        textViewFooterText.setText("Total :");
                        textViewFooterTotal.setText("S/. "+df.format(totalFooter));
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //AUTOCOMPLETE BUSCAR PRODUCTOS
        mCursorStockAgente = dbHelper_Stock_Agente.fetchStockAgenteByIdEstablecimiento(id_categoria_establecimiento, idLiquidacion);

        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    Log.d("TIENE FOCO","TIENE EL PUTO FOCO");
                }else{
                    hide_keyboard(mainActivity);
                }
            }
        });
        String[] columnasStock = new String[]{
                DbAdapter_Stock_Agente.ST_nombre,
                DbAdapter_Stock_Agente.ST_disponible,

        };

        // the XML defined views which the data will be bound to
        int[] toStock = new int[]{
                R.id.VCAP_producto,
                R.id.VCPA_stock,

        };
        adapterProductos = new SimpleCursorAdapter(this,
                R.layout.infor_venta_cabecera_productos,
                mCursorStockAgente,
                columnasStock,
                toStock,
                0);


        autoCompleteTextView.setAdapter(adapterProductos);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                adapterProductos.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        adapterProductos.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                return dbHelper_Stock_Agente.fetchStockAgenteByIdEstablecimientoandName(id_categoria_establecimiento, charSequence.toString(), idLiquidacion);

            }
        });



        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                hide_keyboard(mainActivity);
                //mainActivity.getCurrentFocus().clearFocus();
                //Aquí obtengo el cursor posicionado en la fila que ha seleccionado/clickeado
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                autoCompleteTextView.setText("");
                //El item seleccionado tenía sólo 2 columnas visibles, pero en el cursos se encuentran todas las columnas
                //Aquí podemos obtener las otras columnas para los que querramos hacer con ellas
                nombre = cursor.getString(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_nombre));
                id_producto = cursor.getInt(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_id_producto));
                disponible = cursor.getInt(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_disponible));
                valor_unidad = cursor.getInt(cursor.getColumnIndex(DbAdapter_Precio.PR_valor_unidad));



                if (disponible>0){

                    Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto,id_categoria_establecimiento);

                    if (mCursorPrecioUnitarioGeneral.getCount()>1){
                        myTextDialogValorUnidad().show();
                        //Toast.makeText(getApplicationContext(), "Hay "+mCursorPrecioUnitarioGeneral.getCount() + " valores unidades para este producto", Toast.LENGTH_SHORT).show();
                    }else{
                        myTextDialog().show();
                    }
                }else{
                    Toast.makeText(mainActivity, "No hay Stock", Toast.LENGTH_SHORT).show();
                }


            }
        });

        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                Log.d("POSITION SELECTED", parent.getPositionForView(view)+" - " + parent.getCount());
                Log.d("POSITION ", position +" - " + parent.getCount());


                //Aquí obtengo el cursor posicionado en la fila que ha seleccionado/clickeado

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                final long id_tem_detalle = cursor.getLong(cursor.getColumnIndex(DBAdapter_Temp_Venta.temp_venta_detalle));
                myEditDialog(id_tem_detalle).show();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {



                int numeroViews =  adapterView.getCount();
                numeroViews--;
                if (adapterView.getPositionForView(view) != 1 && adapterView.getPositionForView(view) != numeroViews) {

                    Log.d("POSITION SELECTED", adapterView.getPositionForView(view)+" - " + adapterView.getCount());
                    Log.d("POSITION ", i +" - " + adapterView.getCount());


                    //Aquí obtengo el cursor posicionado en la fila que ha seleccionado/clickeado

                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                    final long id_tem_detalle = cursor.getLong(cursor.getColumnIndex(DBAdapter_Temp_Venta.temp_venta_detalle));

                    new AlertDialog.Builder(mContext)
                            .setTitle("Seleccionar una acción")
                            .setItems(R.array.acciones, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switch (i) {
                                        case 0:
                                            myEditDialog(id_tem_detalle).show();
                                            break;
                                        case 1:
                                            new AlertDialog.Builder(mContext)
                                                    .setTitle("Eliminar")
                                                    .setMessage("¿Está seguro que desea eliminar este producto de la venta?")
                                                    .setNegativeButton(android.R.string.no, null)
                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // FIRE ZE MISSILES!
                                                            boolean succesful = dbHelper_temp_venta.deleteTempVentaDetalleById(id_tem_detalle);
                                                            if (succesful) {
                                                                Toast.makeText(getApplicationContext(), "Producto eliminado de la venta actual correctamente", Toast.LENGTH_LONG).show();
                                                                /*finish();
                                                                Intent intent = new Intent(mContext, VMovil_Venta_Cabecera.class);
                                                                startActivity(intent);*/
                                                                mostrarProductosParaVender();
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "No se pudo eliminar, intente nuevamente", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    }).create().show();
                                            break;

                                    }
                                }
                            }).create().show();

                }
                return false;
            }
        });

        //SLIDING MENU
        showSlideMenu(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.princ_venta_principal, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addMenuProduct:
                IntentIntegrator intentIntegrator = new IntentIntegrator(mainActivity);
                intentIntegrator.initiateScan();
                break;
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result}
            barcodeScan = scanResult.getContents();
            formatScan = scanResult.getFormatName();

            /*textViewContent.setText("CODEBAR CONTEN : "+contents);
            textViewFormat.setText("FORMAT : "+format);*/

            if (barcodeScan!=null){

                mCursorScannerProducto = dbHelper_Precio.getProductoByCodigo(id_categoria_establecimiento, barcodeScan, idLiquidacion);

                if (mCursorScannerProducto.getCount()>0){
                    scannerDialog().show();
                }else {
                    Toast.makeText(getApplicationContext(), "Producto con código de barras : "+ barcodeScan + "no disponible en el Stock Actual y/o Categoría establecimiento", Toast.LENGTH_SHORT).show();;
                }
            }else{
                Toast.makeText(getApplicationContext(), "No ha Scaneado ningún producto", Toast.LENGTH_SHORT).show();;
            }



        }
        // else continue with any other code you need in the method
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private Dialog scannerDialog() {

        int idProducto = -1;
        String nombreP = null;
        final View layout = View.inflate(this, R.layout.dialog_cantidad_productos, null);

        DecimalFormat df = new DecimalFormat("#.00");
        final EditText savedText = ((EditText) layout.findViewById(R.id.VCAP_editTextCantidad));
        final TextView nombreProducto = ((TextView) layout.findViewById(R.id.VCPA_textView2NombreProducto));
        final TextView precio = ((TextView) layout.findViewById(R.id.VCPA_textViewPrecio));

        mCursorScannerProducto.moveToFirst();
        if (mCursorScannerProducto.getCount()>0) {
            double precio_unitario = mCursorScannerProducto.getDouble(mCursorScannerProducto.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
            nombreP =  mCursorScannerProducto.getString(mCursorScannerProducto.getColumnIndexOrThrow(DbAdapter_Precio.PR_nombreProducto));
            idProducto = mCursorScannerProducto.getInt(mCursorScannerProducto.getColumnIndexOrThrow(DbAdapter_Precio.PR_id_producto));
            precio.setText("Precio : S/. "+ df.format(precio_unitario));
            nombreProducto.setText("Nombre : S/. " + nombreP);
        }else{
            precio.setText("Producto Scaneado no disponible en el Stock Actual");

        }



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantidad");
        final int finalIdProducto = idProducto;
        final String finalNombreP = nombreP;
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String texto = savedText.getText().toString().trim();
                if(texto.equals("")){
                    texto = "1";
                }

                cantidad = Integer.parseInt(texto);

                Toast.makeText(getApplicationContext(),"Cantidad : "+cantidad + " id_producto : "+ id_producto,Toast.LENGTH_LONG).show();

                Cursor mCursorPrecioUnitario = dbHelper_Precio.fetchAllPrecioByIdProductoAndCantidad(finalIdProducto,cantidad, id_categoria_establecimiento);

                double precio_unitario = 10.0;
                if (mCursorPrecioUnitario.getCount()!=0){
                    precio_unitario = mCursorPrecioUnitario.getDouble(mCursorPrecioUnitario.getColumnIndex(DbAdapter_Precio.PR_precio_unit));
                }else{
                    Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(finalIdProducto,id_categoria_establecimiento);
                    mCursorPrecioUnitarioGeneral.moveToFirst();
                    if (mCursorPrecioUnitarioGeneral.getCount()>0) {
                        precio_unitario = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
                    }else{
                        Toast.makeText(getApplicationContext(), "No se encontró un precio para esta cantidad de productos, agregar el precio a la base de datos", Toast.LENGTH_LONG).show();;
                    }
                }

                double total_importe = precio_unitario*cantidad;
                String promedio_anterior = null;
                String devuelto = null;

                //En una tabla "Temp_Venta" Nos sirve para agregar datos del historial de ventas anteriores y sugerir al usuario, estos son datos temporales
                long id = dbHelper_temp_venta.createTempVentaDetalle(1,finalIdProducto, finalNombreP,cantidad,total_importe, precio_unitario, promedio_anterior, devuelto, procedencia, 1);

                Intent intent = new Intent(mContext, VMovil_Venta_Cabecera_AgregarProductos.class);
                finish();
                startActivity(intent);
            }
        });
        builder.setView(layout);
        return builder.create();
    }
    private Dialog dialogSolicitarCredito() {

        final View layout = View.inflate(this, R.layout.dialog_solicitar_credito, null);



        final EditText editTextCantidadCredito = ((EditText) layout.findViewById(R.id.VCSC_editText_CantidadCredito));
        spinnerDiasCredito = ((Spinner) layout.findViewById(R.id.VCSC_spinner_DiasCredito));


        final ArrayAdapter<CharSequence> adapterDiasCredito = ArrayAdapter.createFromResource(this,R.array.dias_credito,android.R.layout.simple_spinner_item);
        adapterDiasCredito.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiasCredito.setAdapter(adapterDiasCredito);


        spinnerDiasCredito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                diasCredito = spinnerDiasCredito.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantidad a Solicitar");
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String cantidad = editTextCantidadCredito.getText().toString().trim();
                int cantidadCredito = Integer.parseInt(cantidad);

                new SolicitarCredito(mainActivity).execute(""+id_agente_venta,""+idEstablecimiento,""+cantidadCredito,""+diasCredito);

                Toast.makeText(mContext.getApplicationContext(), "Crédito solicitado esperar...",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, VMovil_Evento_Indice.class);
                finish();
                startActivity(intent);
            }
        });
        builder.setView(layout);
        return builder.create();
    }
    private Dialog myEditDialog(final long id_temp_venta_detalle) {

        final View layout = View.inflate(this, R.layout.dialog_editar_productos, null);

        savedText = ((EditText) layout.findViewById(R.id.VCEP_editTextCantidad));
        final TextView nombreProducto = ((TextView) layout.findViewById(R.id.VCEP_textViewNombreProducto));
        final TextView precio = ((TextView) layout.findViewById(R.id.VCEP_textViewPrecio));
        final TextView devuelto = ((TextView) layout.findViewById(R.id.VCEP_textViewDevuelto));
        final TextView promedioAnterior = ((TextView) layout.findViewById(R.id.VCEP_textViewPromedioAnterior));



        savedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                Log.d("BEFORE TEXT CHANGE", "ON");
                if (savedText.getText().toString().trim() != "") {
                    savedText.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("ON TEXT CHANGE", "ON");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("AFTER TEXT CHANGE", "ON");
                if (savedText.getText().toString().trim().equals("")) {
                    savedText.setError("Es Requerido");
                } else {
                    savedText.setError(null);
                }
            }
        });

        Cursor mCursorTempVenta = dbHelper_temp_venta.fetchAllTempVentaDetalleByID(id_temp_venta_detalle);

        String nombre = mCursorTempVenta.getString(mCursorTempVenta.getColumnIndex(DBAdapter_Temp_Venta.temp_nom_producto));
        String devueltoText = mCursorTempVenta.getString(mCursorTempVenta.getColumnIndex(DBAdapter_Temp_Venta.temp_devuelto));
        String promedioAnteriorText = mCursorTempVenta.getString(mCursorTempVenta.getColumnIndex(DBAdapter_Temp_Venta.temp_prom_anterior));

        final Double precio_unitario = mCursorTempVenta.getDouble(mCursorTempVenta.getColumnIndex(DBAdapter_Temp_Venta.temp_precio_unit));

        nombreProducto.setText(nombre);
        DecimalFormat df = new DecimalFormat("#.0");
        precio.setText("Precio : S/. "+df.format(precio_unitario));
        devuelto.setText("Devueltos : "+devueltoText);
        promedioAnterior.setText("Promedio Anterior : " +promedioAnteriorText);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantidad");
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String texto = null;
                if (savedText.getText().toString().trim().equals("")) {
                    texto = "1";
                } else {
                    texto = savedText.getText().toString().trim();
                }

                int cantidad = Integer.parseInt(texto);

                dbHelper_temp_venta.updateTempVentaDetalleCantidad(id_temp_venta_detalle, cantidad, cantidad * precio_unitario);
                /*Intent intent = new Intent(mContext, VMovil_Venta_Cabecera.class);
                finish();
                startActivity(intent);*/


                mostrarProductosParaVender();
            }
        });

        builder.setView(layout);

        final AlertDialog alertDialog = builder.create();
        savedText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.d("FOCUS","ALERT YES");
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }else{
                    Log.d("FOCUS","ALERT FALSE");
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            }
        });

        return alertDialog;
    }
    private enum FormaPago{
        Contado, Credito
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /*case R.id.VC_buttonAgregarProductos:
                Intent intent  = new Intent(this, VMovil_Venta_Cabecera_AgregarProductos.class);
                finish();
                startActivity(intent);
                break;*/
            case R.id.VC_buttonVender:
                //((MyApplication)this.getApplication()).setDisplayedHistorialComprobanteAnterior(false);

                session.deleteVariable(6);
                session.createTempSession(6,0);

                vender();
                break;

                //SLIDING MENU
            case R.id.slide_textviewPrincipal:

                backPressedDialog(mainActivity, VMovil_Evento_Indice.class).show();
                break;
            case R.id.slide_textViewClientes:
                backPressedDialog(mainActivity, VMovil_Menu_Establec.class).show();
                break;
            case R.id.slide_textViewCobranza:
                backPressedDialog(mainActivity, VMovil_Cobros_Totales.class).show();
                break;
            case R.id.slide_TextViewGastos:
                backPressedDialog(mainActivity, VMovil_Evento_Gasto.class).show();
                break;
            case R.id.slide_textViewResumen:
                backPressedDialog(mainActivity, VMovil_Resumen_Caja.class).show();
                break;
            case R.id.slide_textViewARendir:

                break;
            //SLIDING MENU VENTAS
            case R.id.slideVentas_buttonVentaCosto:
                dbHelper_temp_venta.deleteAllTempVentaDetalle();
                //((MyApplication)mContext.getApplicationContext()).setDisplayedHistorialComprobanteAnterior(false);
                session.deleteVariable(6);
                session.createTempSession(6,0);
                session.deleteVariable(5);
                session.createTempSession(5,0);
                Intent ivc1 = new Intent(this, VMovil_Venta_Comprob.class);
                ivc1.putExtra("idEstabX", ""+slideIdEstablecimiento);
                startActivity(ivc1);
                break;
            case R.id.slideVentas_buttonDeudas:
                dbHelper_temp_venta.deleteAllTempVentaDetalle();
                //((MyApplication)mContext.getApplicationContext()).setDisplayedHistorialComprobanteAnterior(false);
                session.deleteVariable(6);
                session.createTempSession(6,0);
                session.deleteVariable(5);
                session.createTempSession(5,0);
                Intent id1 = new Intent(this, VMovil_Cobro_Credito.class);
                id1.putExtra("idEstabX", ""+slideIdEstablecimiento);
                finish();
                startActivity(id1);
                break;
            case R.id.slideVentas_textViewVenta:
                menu.toggle();
                break;
            case R.id.slideVentas_textViewMantenimiento:
                dbHelper_temp_venta.deleteAllTempVentaDetalle();
                //((MyApplication)mContext.getApplicationContext()).setDisplayedHistorialComprobanteAnterior(false);
                session.deleteVariable(6);
                session.createTempSession(6,0);
                session.deleteVariable(5);
                session.createTempSession(5,0);
                Intent im1 = new Intent(this, VMovil_Venta_Comprob.class);
                im1.putExtra("idEstabX", ""+slideIdEstablecimiento);
                finish();
                startActivity(im1);
                break;
            case R.id.slideVentas_textviewCanjesDevoluciones:
                dbHelper_temp_venta.deleteAllTempVentaDetalle();
                //((MyApplication)mContext.getApplicationContext()).setDisplayedHistorialComprobanteAnterior(false);
                session.deleteVariable(6);
                session.createTempSession(6,0);
                session.deleteVariable(5);
                session.createTempSession(5,0);
                Intent idh1 = new Intent(this, VMovil_Evento_Canjes_Dev.class);
                idh1.putExtra("idEstabX", ""+slideIdEstablecimiento);
                idh1.putExtra("idAgente", ""+slideIdAgente);
                finish();
                startActivity(idh1);
                break;
            case R.id.slideVentas_textViewCliente:
                menu.toggle();
            default:
                break;
        }

    }

    public void vender(){

        //Obtener los datos de las ventas

        String formaPago = spinnerFormaPago.getSelectedItem().toString();
        String tipoDocumento = spinnerTipoDocumento.getSelectedItem().toString();

        TipoDocumento tipoDocumento1 = TipoDocumento.valueOf(tipoDocumento);
        FormaPago formaPago1 = FormaPago.valueOf(formaPago);

        int tipoVenta = 1;
        int numero_documento = 1;
        int estado_conexion = 0;
        int i_tipoDocumento = 0;
        int i_formaPago = 0;
        int estado_comprobante = 1;
        Double monto_total  = 0.0;
        Double igv  = 0.0;
        Double base_imponible = 0.0;
        String erp_stringTipoDocumento  = null;
        String serie = null;
        String codigo_erp = null;

        switch (tipoDocumento1){
            case factura:
                i_tipoDocumento = 1;
                erp_stringTipoDocumento="FV";
                serie = dbHelperAgente.getSerieFacturaByIdAgente(id_agente_venta,idLiquidacion );
                codigo_erp = erp_stringTipoDocumento+serie;
                Log.d("CODIGO ERP ", codigo_erp);
                break;
            case boleta:
                i_tipoDocumento = 2;
                erp_stringTipoDocumento="BV";
                serie = dbHelperAgente.getSerieBoletaByIdAgente(id_agente_venta, idLiquidacion);
                codigo_erp = erp_stringTipoDocumento+serie;
                Log.d("CODIGO ERP ", codigo_erp);
                break;
            case ficha:
                Log.d("CODIGO ERP ", codigo_erp);
                break;
        }

        Cursor cursorTempComprobCobros = dbHelper_Temp_Comprob_Cobros.fetchAllComprobCobros();

        Log.d("FORMA DE PAGO ", ""+i_formaPago);
        switch (formaPago1){
            case Contado:
                i_formaPago = 1;
                break;
            case Credito:
                i_formaPago = 2;

                //((MyApplication)this.getApplication()).setCuotasEstablecidas(false)
                session.deleteVariable(5);
                session.createTempSession(5,0);
                break;

        }


        String datosConcatenados = "idEestableciminto : " + idEstablecimiento + "\n" +
                "idAgente : " + id_agente_venta + "\n" +
                "Forma de pago : " + formaPago + "\n" +
                "Tipo Documento : " + tipoDocumento+ "\n" + "---------------------------";

        Cursor cursorAgente = dbHelperAgente.fetchAgentesByIds(id_agente_venta, idLiquidacion);
        cursorAgente.moveToFirst();
        String nombreAgenteVenta = cursorAgente.getString(cursorAgente.getColumnIndexOrThrow(dbHelperAgente.AG_nombre_agente));

        Cursor cursorEstablecimiento = dbHelper_Evento_Establecimiento.fetchEstablecsById(""+idEstablecimiento);
        cursorEstablecimiento.moveToFirst();
        String nombreCliente = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndexOrThrow(dbHelper_Evento_Establecimiento.EE_nom_cliente));
        String documentoCliente = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndexOrThrow(dbHelper_Evento_Establecimiento.EE_doc_cliente));
        textoImpresion+= "Código ERP:  "+codigo_erp+"\n";
        textoImpresion+= "Fecha: "+ getDatePhone()+"\n";
        textoImpresion+= "Vendedor: "+ nombreAgenteVenta+"\n";
        textoImpresion+= "Cliente: "+ nombreCliente+"\n";
        textoImpresion+= "DNI: "+ documentoCliente+"\n";
        //textoImpresion+= "Direccion: Alameda Nro 2039 - Chosica\n";
        textoImpresion+= "-----------------------------------------------\n";
        textoImpresion+= "Cant.             Producto              Importe\n";
        textoImpresion+= "-----------------------------------------------\n";


        textoVentaImpresion+= "Código ERP:  "+codigo_erp+"\n";
        textoVentaImpresion+= "Fecha: "+ getDatePhone()+"\n";
        textoVentaImpresion+= "Vendedor: "+ nombreAgenteVenta+"\n";
        textoVentaImpresion+= "Cliente: "+ nombreCliente+"\n";
        textoVentaImpresion+= "DNI: "+ documentoCliente;
        //textoVentaImpresion+= "Direccion: Alameda Nro 2039 - Chosica\n";

        long id = dbHelper_Comprob_Venta.createComprobVenta(idEstablecimiento,i_tipoDocumento,i_formaPago,tipoVenta,codigo_erp,serie,numero_documento,base_imponible,igv,monto_total,getDatePhone(),null,estado_comprobante, estado_conexion,id_agente_venta, Constants._CREADO, idLiquidacion);

        Log.d("Export id CV IGUALES",""+id);


        int id_comprobante = (int) id;


        Cursor cursorTemp = simpleCursorAdapter.getCursor();



        long comprobVentaDetalle = 0;
        for (cursorTemp.moveToFirst(); !cursorTemp.isAfterLast();cursorTemp.moveToNext()){

            int _id = cursorTemp.getInt(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_venta_detalle));
            int id_producto = cursorTemp.getInt(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_id_producto));
            String nombre_producto = cursorTemp.getString(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_nom_producto));
            int cantidad = cursorTemp.getInt(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_cantidad));
            Double precio_unitario = cursorTemp.getDouble(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_precio_unit));
            Double importe = cursorTemp.getDouble(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_importe));

            String promedio_anterior = cursorTemp.getString(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_prom_anterior));
            String devuelto = cursorTemp.getString(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_devuelto));
            int valorUnidad = cursorTemp.getInt(cursorTemp.getColumnIndexOrThrow(DBAdapter_Temp_Venta.temp_valor_unidad));

            monto_total += importe;

            comprobVentaDetalle = dbHelper_Comprob_Venta_Detalle.createComprobVentaDetalle(id_comprobante, id_producto, nombre_producto, cantidad, importe,0, precio_unitario, promedio_anterior, devuelto, valorUnidad);
            dbHelper_Stock_Agente.updateStockAgenteCantidad(id_producto,-(cantidad*valorUnidad), idLiquidacion);

            if(nombre_producto.length()>=25){
                nombre_producto=nombre_producto.substring(0,25);
                nombre_producto+="...";
            }

            DecimalFormat df = new DecimalFormat("#.00");
            textoImpresion+=String.format("%-6s",cantidad) + String.format("%-34s",nombre_producto) +String.format("%-5s",df.format(importe)) + "\n";
            textoImpresionContenidoLeft +=String.format("%-6s",cantidad) + String.format("%-31s",nombre_producto)+ "\n";
            textoImpresionContenidoRight+= String.format("%-5s",df.format(importe)) + "\n";

            datosConcatenados+="Producto  "+ nombre_producto + "Vendido satisfactoriamente con id : "+ comprobVentaDetalle;
        }

        base_imponible = monto_total /1.18;
        igv = base_imponible*0.18;
        dbHelper_Comprob_Venta.updateComprobanteMontos(id,monto_total,igv, base_imponible);
        datosConcatenados+="total de gasto : " + monto_total;
        datosConcatenados+="base impornible: " + base_imponible;
        datosConcatenados+="igv : " + igv;

        DecimalFormat df = new DecimalFormat("#.00");

        textoImpresion += String.format("%-37s","SUB TOTAL:")+ "S/ "+ df.format(base_imponible)+"\n";
        textoImpresion += String.format("%-37s","IGV:")+  "S/ "+ df.format(igv)+"\n";
        textoImpresion += String.format("%-37s","TOTAL:")+  "S/ "+ df.format(monto_total)+"\n";

        textoImpresionContenidoLeft+=String.format("%-34s","SUB TOTAL:")+"\n";
        textoImpresionContenidoLeft+=String.format("%-34s","IGV:")+"\n";
        textoImpresionContenidoLeft+=String.format("%-34s","TOTAL:")+"\n";

        textoImpresionContenidoRight+= "S/ " +
                ""+ df.format(base_imponible)+"\n";
        textoImpresionContenidoRight+= "S/ "+ df.format(igv)+"\n";
        textoImpresionContenidoRight+= "S/ "+ df.format(monto_total)+"\n";

        dbHelper_Evento_Establecimiento.updateEstadoEstablecs(""+idEstablecimiento,2);


        if (i_formaPago==2){
            for (cursorTempComprobCobros.moveToFirst(); !cursorTempComprobCobros.isAfterLast();cursorTempComprobCobros.moveToNext()){
                String fecha_programada = cursorTempComprobCobros.getString(cursorTempComprobCobros.getColumnIndex(DbAdapter_Temp_Comprob_Cobro.temp_fecha_programada));
                Double monto_a_pagar = cursorTempComprobCobros.getDouble(cursorTempComprobCobros.getColumnIndex(DbAdapter_Temp_Comprob_Cobro.temp_monto_a_pagar));

                Log.d("RECORRE EL CURSOR TEMP COMPROB COBROS", "YES");

                long registroInsertado = dbHelper_Comprob_Cobros.createComprobCobros(idEstablecimiento,Integer.parseInt(id+""),id_plan_pago,id_plan_pago_detalle,tipoDocumento.toUpperCase(),codigo_erp,fecha_programada,monto_a_pagar, fecha_cobro, hora_cobro,monto_cobrado,estado_cobro,id_agente_venta,id_forma_cobro, lugar_registro, idLiquidacion);
                Log.d("CC INSERTADO SATISFACTORIAMENTE ", "ID : "+ registroInsertado);
            }
        }


        dbHelper_temp_venta.deleteAllTempVentaDetalle();
        dbHelper_Temp_Comprob_Cobros.deleteAllComprobCobros();

        if (conectadoWifi()||conectadoRedMovil()){
            exportMain.execute();
        }


        Intent intent= new Intent(this, VMovil_BluetoothImprimir.class);
        intent.putExtra("textoImpresion",textoImpresion);
        intent.putExtra("textoImpresionCabecera", textoImpresionCabecera);
        intent.putExtra("textoVentaImpresion", textoVentaImpresion);
        intent.putExtra("textoImpresionContenidoLeft", textoImpresionContenidoLeft);
        intent.putExtra("textoImpresionContenidoRight", textoImpresionContenidoRight);
        finish();
        Toast.makeText(getApplicationContext(),"Venta Satisfactoria",Toast.LENGTH_LONG).show();
        startActivity(intent);




    }

    public enum TipoDocumento{
        factura, boleta, ficha
    }
    private void displayHistorialComprobanteAnterior() {


        Log.d("DISPLAY ID ESTABLECIMIENTO ", ""+idEstablecimiento);;

        //((MyApplication)this.getApplication()).setDisplayedHistorialComprobanteAnterior(true);
        session.deleteVariable(6);
        session.createTempSession(6,1);
        Cursor cursor = dbHelper_Histo_comprob_anterior.fetchAllHistoComprobAnteriorByIdEstRawQuery(idEstablecimiento);
        //OBTENGO LAS PJOSICIONES DE LAS COLUMNAS DEL CURSOR
        int indice_id = cursor.getColumnIndex("_id");
        int indice_id_producto = cursor.getColumnIndex("id_producto");
        int indice_nombre_producto = cursor.getColumnIndex("nombre_producto");
        int indice_cantidad = cursor.getColumnIndex("cantidad");
        int indice_precio_unitario = cursor.getColumnIndex("pu");
        int indice_importe = cursor.getColumnIndex("total");

        int indice_promedio_anterior = cursor.getColumnIndex("pa");
        int indice_devuelto  = cursor.getColumnIndex("devuelto");
        int indice_valor_unidad = cursor.getColumnIndex("valorUnidad");

        //t indice_valor_unidad = cursor.getColumnIndex("");
        //int indice_costo_venta = cursor.getColumnIndex("");

        cursor.moveToFirst();

        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){

            int _id = cursor.getInt(indice_id);
            int id_producto = cursor.getInt(indice_id_producto);
            String nombre_producto = cursor.getString(indice_nombre_producto);
            int cantidad = cursor.getInt(indice_cantidad);
            Log.d("CANTIDAD HCA - VC", ""+cantidad);
            Double precio_unitario = cursor.getDouble(indice_precio_unitario);
            Double importe = cursor.getDouble(indice_importe);

            String promedio_anterior = cursor.getString(indice_promedio_anterior);
            String devuelto = cursor.getString(indice_devuelto);
            int valor_unidad = cursor.getInt(indice_valor_unidad);

            int procedencia = 0;

            //En una tabla "Temp_Venta" Nos sirve para agregar datos del historial de ventas anteriores y sugerir al usuario, estos son datos temporales
            long id = dbHelper_temp_venta.createTempVentaDetalle(1,id_producto,nombre_producto,cantidad,importe, precio_unitario, promedio_anterior, devuelto, procedencia, valor_unidad);


        }

    }
    public static void hide_keyboard(Activity activity) {
        Log.d("HIDE KEYBOARD","INICIO");
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        Log.d("KEYBOARD",""+view.getTag()+"-"+view.getId());
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if(view == null) {
            view = new View(activity);
        }
         boolean isHide= inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Log.d("HIDE SOFT INPUT",""+isHide);
    }
    public void mostrarProductosParaVender(){

        Log.d("AUTOCOMPLETE KEYBOARD",""+autoCompleteTextView.getTag()+"-"+autoCompleteTextView.getId());

        //autoCompleteTextView.clearFocus();
        //autoCompleteTextView.clearFocus();
        //buttonVender.requestFocus();
        //hide_keyboard(mainActivity);


        Log.d("FOOTER","VIEW COUNT "+listView.getFooterViewsCount());
        if (listView.getFooterViewsCount()<1){

        }else{
            listView.removeFooterView(footer);

        }

        Cursor cursorTempVentaDetalle = dbHelper_temp_venta.fetchAllTempVentaDetalle();
        // The desired columns to be bound
        String[] columns = new String[]{
                DBAdapter_Temp_Venta.temp_nom_producto,
                DBAdapter_Temp_Venta.temp_cantidad,
                DBAdapter_Temp_Venta.temp_precio_unit,
                DBAdapter_Temp_Venta.temp_importe,

        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.VC_producto,
                R.id.VC_promedio,
                R.id.VC_pu,
                R.id.VC_total

        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        simpleCursorAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_venta_cabecera,
                cursorTempVentaDetalle,
                columns,
                to,
                0);

        if (listView.getHeaderViewsCount()<2){
            header = getLayoutInflater().inflate(R.layout.infor_venta_cabecera,null);
            headerNombreEstablecimiento = getLayoutInflater().inflate(R.layout.header_venta,null);
            TextView textViewNombreEstablecimiento = (TextView) headerNombreEstablecimiento.findViewById(R.id.headerEstablecimientoNombre);


            Cursor cursorEstablecimiento = dbHelper_Evento_Establecimiento.fetchEstablecsById(""+idEstablecimiento);
            cursorEstablecimiento.moveToFirst();
            String nombreEstablecimiento = "";
            if (cursorEstablecimiento.getCount()>0) {
                nombreEstablecimiento = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndexOrThrow(dbHelper_Evento_Establecimiento.EE_nom_establec));
            }
            textViewNombreEstablecimiento.setText(nombreEstablecimiento);

            listView.addHeaderView(headerNombreEstablecimiento,null,false);
            listView.addHeaderView(header,null, false);
        }

        //AQUÍ TIENE QUE ESTAR EL CÓDIGO QUE MUESTRE EL MONTO TOTAL DE LA VENTA

        Cursor cursorTemp = simpleCursorAdapter.getCursor();
        int surtidoVenta = cursorTemp.getCount();
        totalFooter = 0.0;

        Cursor cursorStock = dbHelper_Stock_Agente.fetchAllStockAgenteByDay(idLiquidacion);
        int surtidoStock = cursorStock.getCount();


        for (cursorTemp.moveToFirst(); !cursorTemp.isAfterLast();cursorTemp.moveToNext()){

            Double importe = cursorTemp.getDouble(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_importe));
            totalFooter += importe;
        }

        base_imponibleFooter = totalFooter /1.18;
        igvFooter = base_imponibleFooter*0.18;

        footer = getLayoutInflater().inflate(R.layout.footer_venta_cabecera,null);

        textViewFooterText = (TextView)footer.findViewById(R.id.VCAP_textViewFooterText);
        textViewFooterTotal = (TextView)footer.findViewById(R.id.VCAP_textViewFooterTotal);

        textViewFooterSurtidoStock = (TextView)footer.findViewById(R.id.VCAP_textViewSurtidoStock);
        textViewFooterSurtidoVenta = (TextView)footer.findViewById(R.id.VCAP_textViewSurtiDoVenta);

        textViewFooterText.setText("Total :\n" +
                "Base imponible :\n" +
                "IGV :");

        DecimalFormat df = new DecimalFormat("#.00");
        textViewFooterTotal.setText(" S/. "+df.format(totalFooter)+"\n" +
                "S/. "+df.format(base_imponibleFooter)+ "\n" +
                "S/. "+df.format(igvFooter));


        //DATOS DE PRUEBA LOS OBTENDRÈ CUANDO JOSMAR ME PASE ESTOS DATOS
        int surtidoStockAnterior = 10;
        int surtidoVentaAnterior = 1;

        int porcentajeSurtidoAnterior = surtidoVentaAnterior*100/surtidoStockAnterior;

        int porcentajeSurtido = surtidoVenta*100/surtidoStock;

        textViewFooterSurtidoStock.setText(""+
                "Surtido Stock : " + surtidoStock + "\n" + "Porcentaje de Surtido de Venta: " +  + porcentajeSurtido +"%");
        textViewFooterSurtidoVenta.setText(""+
                "Surtido Venta : " + surtidoVenta );

        if (porcentajeSurtido==porcentajeSurtidoAnterior){
            //IGUAL LO PINTAMOS DE AMARILLO
            textViewFooterSurtidoVenta.setTextColor(this.getResources().getColor(R.color.amarillo));
            textViewFooterSurtidoStock.setTextColor(this.getResources().getColor(R.color.amarillo));
        }else if(porcentajeSurtido>porcentajeSurtidoAnterior){
            //PINTAMOS DE VERDE
            textViewFooterSurtidoVenta.setTextColor(this.getResources().getColor(R.color.verde));
            textViewFooterSurtidoStock.setTextColor(this.getResources().getColor(R.color.verde));
        }else if (porcentajeSurtido<porcentajeSurtidoAnterior){
            //PINTAMOS DE ROJO
            textViewFooterSurtidoVenta.setTextColor(this.getResources().getColor(R.color.rojo));
            textViewFooterSurtidoStock.setTextColor(this.getResources().getColor(R.color.rojo));
        }



        if (listView.getFooterViewsCount()<1){
            listView.addFooterView(footer, null, false);
        }else{
            listView.addFooterView(footer);
        }

        //ASIGNO EL ADAPTER AL LISTVIEW
        listView.setAdapter(simpleCursorAdapter);
    }

//INICIO MÉTODO AGREGAR PRODUCTOS

    private Dialog myTextDialog() {
        final View layout = View.inflate(this, R.layout.dialog_cantidad_productos, null);

        DecimalFormat df = new DecimalFormat("#.00");
        final EditText savedText = ((EditText) layout.findViewById(R.id.VCAP_editTextCantidad));
        final TextView nombreProducto = ((TextView) layout.findViewById(R.id.VCPA_textView2NombreProducto));
        final TextView precio = ((TextView) layout.findViewById(R.id.VCPA_textViewPrecio));

        Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto,id_categoria_establecimiento);

        if (mCursorPrecioUnitarioGeneral.getCount()>1){
            Toast.makeText(getApplicationContext(), "Hay "+mCursorPrecioUnitarioGeneral.getCount() + " valores unidades para este producto", Toast.LENGTH_SHORT).show();
        }


        Cursor mCursorStock = dbHelper_Stock_Agente.fetchByIdProducto(id_producto,idLiquidacion);
        int maximoValor = 1;
        if (mCursorStock.getCount()>0){
            maximoValor = mCursorStock.getInt(mCursorStock.getColumnIndexOrThrow(dbHelper_Stock_Agente.ST_disponible));
        }

        savedText.setFilters(new InputFilter[]{new InputFilterMinMax(0,maximoValor)});


        if (mCursorPrecioUnitarioGeneral.getCount()>0) {
            double precio_unitario = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
            precio.setText("Precio : S/. "+ df.format(precio_unitario));
        }else{
            precio.setText("Precio : S/. No encontrado");
        }
        nombreProducto.setText(nombre);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantidad");
        final int finalMaximoValor = maximoValor;
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                String texto = savedText.getText().toString().trim();
                if(texto.equals("")){
                    texto = "1";
                }

                cantidad = Integer.parseInt(texto);

                Toast.makeText(getApplicationContext(),"Cantidad : "+cantidad + " id_producto : "+ id_producto,Toast.LENGTH_LONG).show();

                Cursor mCursorPrecioUnitario = dbHelper_Precio.fetchAllPrecioByIdProductoAndCantidad(id_producto,cantidad, id_categoria_establecimiento);

                double precio_unitario = 10.0;
                if (mCursorPrecioUnitario.getCount()!=0){
                    precio_unitario = mCursorPrecioUnitario.getDouble(mCursorPrecioUnitario.getColumnIndex(DbAdapter_Precio.PR_precio_unit));
                }else{
                    Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto,id_categoria_establecimiento);
                    mCursorPrecioUnitarioGeneral.moveToFirst();
                    if (mCursorPrecioUnitarioGeneral.getCount()>0) {
                        precio_unitario = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
                    }else{
                        Toast.makeText(getApplicationContext(), "No se encontró un precio para esta cantidad de productos, agregar el precio a la base de datos", Toast.LENGTH_LONG).show();;
                    }
                }

                double total_importe = precio_unitario*cantidad;
                String promedio_anterior = null;
                String devuelto = null;

                //En una tabla "Temp_Venta" Nos sirve para agregar datos del historial de ventas anteriores y sugerir al usuario, estos son datos temporales
                long id = dbHelper_temp_venta.createTempVentaDetalle(1,id_producto,nombre,cantidad,total_importe, precio_unitario, promedio_anterior, devuelto, procedencia, valor_unidad);
                //softKeyboard.closeSoftKeyboard();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                mostrarProductosParaVender();
            }
        });
        builder.setView(layout);

        final AlertDialog alertDialog = builder.create();
        savedText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.d("FOCUS","ALERT YES");
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }else{
                    Log.d("FOCUS","ALERT FALSE");
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }

            }
        });
        return alertDialog;
    }
    private Dialog myTextDialogValorUnidad() {
        final View layout = View.inflate(this, R.layout.dialog_cantidad_productos_valor_unidad, null);

        DecimalFormat df = new DecimalFormat("#.00");
        final EditText savedText = ((EditText) layout.findViewById(R.id.VCAP_editTextCantidad));
        final TextView nombreProducto = ((TextView) layout.findViewById(R.id.VCPA_textView2NombreProducto));
        final TextView precio = ((TextView) layout.findViewById(R.id.VCPA_textViewPrecio));
        final TextView valorUnidad = (TextView) layout.findViewById(R.id.VCPA_textViewValorUnidad);

        final double[] precio_unitario = {0.0};

        final Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto,id_categoria_establecimiento);
        mCursorPrecioUnitarioGeneral.moveToFirst();

        if (mCursorPrecioUnitarioGeneral.getCount()>0) {
            precio_unitario[0] = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
            valor_unidad = mCursorPrecioUnitarioGeneral.getInt(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_valor_unidad));
            precio.setText("Precio : S/. "+ df.format(precio_unitario[0]));
            valorUnidad.setText("Unidad : "+valor_unidad);
        }else{
            precio.setText("Precio : S/. No encontrado");
        }
        nombreProducto.setText(nombre);


        valorUnidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mCursorPrecioUnitarioGeneral.getPosition();
                //Toast.makeText(getApplicationContext(), "Position del cursor Valor Unidad "+position, Toast.LENGTH_SHORT).show();

                if (mCursorPrecioUnitarioGeneral.isLast()){
                    mCursorPrecioUnitarioGeneral.moveToFirst();
                }else{
                    mCursorPrecioUnitarioGeneral.moveToNext();
                }
                precio_unitario[0] = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
                valor_unidad = mCursorPrecioUnitarioGeneral.getInt(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_valor_unidad));
                DecimalFormat df = new DecimalFormat("#.00");
                precio.setText("Precio : S/. "+ df.format(precio_unitario[0]));
                valorUnidad.setText("Unidad : "+valor_unidad);


            }
        });


        Cursor mCursorStock = dbHelper_Stock_Agente.fetchByIdProducto(id_producto,idLiquidacion);
        int maximoValor = 1;
        if (mCursorStock.getCount()>0){
            maximoValor = mCursorStock.getInt(mCursorStock.getColumnIndexOrThrow(dbHelper_Stock_Agente.ST_disponible));
        }

        savedText.setFilters(new InputFilter[]{new InputFilterMinMax(0,maximoValor)});





        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantidad");
        final int finalMaximoValor = maximoValor;
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String texto = savedText.getText().toString().trim();
                if(texto.equals("")){
                    texto = "1";
                }

                cantidad = Integer.parseInt(texto);

                //Toast.makeText(getApplicationContext(),"Cantidad : "+cantidad + " id_producto : "+ id_producto + "Precio Unitario : " +precio_unitario[0] + "Valor Unidad : "+ valor_unidad,Toast.LENGTH_LONG).show();

                Cursor mCursorPrecioUnitario = dbHelper_Precio.fetchAllPrecioByIdProductoAndCantidadAndValorUnidad(id_producto,cantidad, id_categoria_establecimiento,valor_unidad);

                if (mCursorPrecioUnitario.getCount()!=0){
                    precio_unitario[0] = mCursorPrecioUnitario.getDouble(mCursorPrecioUnitario.getColumnIndex(DbAdapter_Precio.PR_precio_unit));
                }else{
                    /*
                    Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto,id_categoria_establecimiento);
                    mCursorPrecioUnitarioGeneral.moveToFirst();
                    if (mCursorPrecioUnitarioGeneral.getCount()>0) {
                        precio_unitario = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
                    }else{
                        Toast.makeText(getApplicationContext(), "No se encontró un precio para esta cantidad de productos, agregar el precio a la base de datos", Toast.LENGTH_LONG).show();;
                    }*/
                }

                double total_importe = precio_unitario[0]*cantidad;
                String promedio_anterior = null;
                String devuelto = null;

                //En una tabla "Temp_Venta" Nos sirve para agregar datos del historial de ventas anteriores y sugerir al usuario, estos son datos temporales
                long id = dbHelper_temp_venta.createTempVentaDetalle(1,id_producto,nombre,cantidad,total_importe, precio_unitario[0], promedio_anterior, devuelto, procedencia, valor_unidad);
                //softKeyboard.closeSoftKeyboard();


                mostrarProductosParaVender();
            }
        });
        builder.setView(layout);

        final AlertDialog alertDialog = builder.create();
        savedText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.d("FOCUS","ALERT YES");
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }else{
                    Log.d("FOCUS","ALERT FALSE");
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            }
        });
        return alertDialog;
    }



    public String getDatePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }

    double formatDecimal(double d)
    {
        DecimalFormat df = new DecimalFormat("#,00");
        return Double.valueOf(df.format(d));
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

    @Override
    public View getCurrentFocus() {
        return super.getCurrentFocus();
    }


    //SLIDING MENU
    public void showSlideMenu(Activity activity){
        layoutSlideMenu = View.inflate(activity, R.layout.slide_menu_ventas, null);
        // configure the SlidingMenu
        menu =  new SlidingMenu(activity);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.space_slide);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.space_slide);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(layoutSlideMenu);

        textViewSlideNombreAgente = (TextView)findViewById(R.id.slide_textViewNombreAgente);
        textViewSlideNombreRuta = (TextView)findViewById(R.id.slide_textViewNombreRuta);
        buttonSlideNroEstablecimiento = (Button) findViewById(R.id.slide_buttonNroEstablecimiento);

        textViewSlidePrincipal = (TextView)findViewById(R.id.slide_textviewPrincipal);
        textViewSlideCliente = (TextView)findViewById(R.id.slide_textViewClientes);
        textviewSlideCobranzas = (TextView)findViewById(R.id.slide_textViewCobranza);
        textviewSlideGastos = (TextView)findViewById(R.id.slide_TextViewGastos);
        textviewSlideResumen = (TextView)findViewById(R.id.slide_textViewResumen);
        textviewSlideARendir = (TextView)findViewById(R.id.slide_textViewARendir);


        textViewSlideNombreEstablecimiento = (TextView)findViewById(R.id.slideVentas_textViewCliente);
        buttonSlideVentaDeHoy  = (Button)findViewById(R.id.slideVentas_buttonVentaCosto);
        buttonSlideDeudaHoy = (Button)findViewById(R.id.slideVentas_buttonDeudas);
        textViewSlideVenta = (TextView)findViewById(R.id.slideVentas_textViewVenta);
        //COBRAR
        textViewSlideMantenimiento = (TextView)findViewById(R.id.slideVentas_textViewMantenimiento);
        textViewSlideCanjesDevoluciones  = (TextView)findViewById(R.id.slideVentas_textviewCanjesDevoluciones);





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
        slideIdLiquidacion  = session.fetchVarible(3);
        slideIdEstablecimiento = session.fetchVarible(2);


        changeDataSlideMenu();


    }

    //SLIDING MENU
    public void changeDataSlideMenu(){

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

        if (cursorAgente.getCount()>0){
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
        Cursor cursorTotalGastos =dbAdapter_informe_gastos.resumenInformeGastos(utils.getDayPhone());

        for (cursorTotalGastos.moveToFirst(); !cursorTotalGastos.isAfterLast(); cursorTotalGastos.moveToNext()){
            Double rutaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("RUTA"));
            Double plantaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("PLANTA"));

            slide_totalRuta += rutaGasto;
            slide_totalPlanta += plantaGasto;
        }

        slide_ingresosTotales = slide_cobradoTotal + slide_pagadoTotal;
        slide_gastosTotales = slide_totalRuta;
        slide_aRendir = slide_ingresosTotales-slide_gastosTotales;



        //MOSTRAMOS EN EL SLIDE LOS DATOS OBTENIDOS
        DecimalFormat df = new DecimalFormat("#.00");
        textViewSlideNombreAgente.setText(""+slideNombreAgente);
        textViewSlideNombreRuta.setText(""+slideNombreRuta);
        buttonSlideNroEstablecimiento.setText(""+slideNumeroEstablecimientoxRuta);
        textviewSlideARendir.setText("Efectivo a Rendir S/. " + df.format(slide_aRendir));


        //DATA VENTAS
        Cursor cursorEstablecimiento = dbAdaptert_evento_establec.listarEstablecimientosByID(slideIdEstablecimiento, slideIdLiquidacion);

        cursorEstablecimiento.moveToFirst();

        if (cursorEstablecimiento.getCount()>0){

            String nombre_establecimiento = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbAdaptert_evento_establec.EE_nom_establec));
            String nombre_cliente = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbAdaptert_evento_establec.EE_nom_cliente));
            int id_estado_atencion = Integer.parseInt(cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbAdaptert_evento_establec.EE_id_estado_atencion)));
            double deudaTotal = cursorEstablecimiento.getDouble(cursorEstablecimiento.getColumnIndexOrThrow("cc_re_monto_a_pagar")) ;


            textViewSlideNombreEstablecimiento.setText(""+nombre_establecimiento);
            buttonSlideDeudaHoy.setText(""+df.format(deudaTotal));
        }

        Cursor cursorVentasTotales = dbAdapter_comprob_venta.getTotalVentaByIdEstablecimientoAndLiquidacion(slideIdEstablecimiento, slideIdLiquidacion);
        cursorVentasTotales.moveToFirst();
        if (cursorVentasTotales.getCount()>0){
            Double total = cursorVentasTotales.getDouble(cursorVentasTotales.getColumnIndexOrThrow("total"));
            buttonSlideVentaDeHoy.setText(""+df.format(total));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeDataSlideMenu();
    }


}

