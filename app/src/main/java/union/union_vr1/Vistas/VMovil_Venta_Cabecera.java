package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import union.union_vr1.AsyncTask.ExportMain;
import union.union_vr1.AsyncTask.SolicitarCredito;
import union.union_vr1.BarcodeScanner.IntentIntegrator;
import union.union_vr1.BarcodeScanner.IntentResult;
import union.union_vr1.FacturacionElectronica.Signature;
import union.union_vr1.FacturacionElectronica.SimpleXMLAndroid;
import union.union_vr1.InputFilterMinMax;
import union.union_vr1.JSONParser.ParserEventoEstablecimiento;
import union.union_vr1.Objects.Credito;
import union.union_vr1.Objects.EventoEstablecimiento;
import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Servicios.ServiceExport;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Temp_Venta;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Forma_Pago;
import union.union_vr1.Sqlite.DbAdapter_Histo_Comprob_Anterior;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_ModalidadCredito;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Doc_Identidad;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.DisplayToast;
import union.union_vr1.Utils.RoundedLetterView;
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
    private DbAdapter_ModalidadCredito dbAdapter_modalidadCredito;
    private DbAdapter_Forma_Pago dbAdapter_forma_pago;
    private EditText savedText;
    private String SERIE_DOCUMENTO;


    //FE
    String textoSHA1 = null;
    private Context contexto;
    //
    private String numeroDocumentoImpresion =null;
    private String nombreCliente = null;
    private String documentoCliente = null;
    int i_tipoDocumento = 0;

    private String nombreEstablecimiento = null ;


    //VARIABLES AGREGAR PRODUCTOS
    private AutoCompleteTextView autoCompleteTextView;
    private Cursor mCursorStockAgente;
    private int id_categoria_establecimiento;
    private int diasCreditoPorDefecto;
    private SimpleCursorAdapter adapterProductos;

    private DbAdapter_Precio dbHelper_Precio;

    private int cantidad;
    private int procedencia = 1;
    private int disponible = 1;
    private String nombre = null;
    private  int id_producto = 0;
    private String codigo_producto = "";
    private int valor_unidad = 1;


    private Cursor mCursorScannerProducto;
    private String barcodeScan;
    private String formatScan;
    //fin variables agregar productos


    private int id_comprobante;
    // IMPRESORA
    private int anchoImpresora = 3;
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
    private String textoImpresion  = ".\n";

    private int idEstablecimiento;
    int id_agente_venta;
    private int idLiquidacion;
    private SimpleCursorAdapter simpleCursorAdapter;
    private Spinner spinnerTipoDocumento;
    private Spinner spinnerFormaPago;
    private Spinner spinnerTipoPago;
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
    int _id_tipo_pago = 9;
    private int direccionCV = Constants._DIRECCION_ESTABLECIMIENTO;
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
    private TextView textViewSlideCargar;
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
    TextView textCargarInventario;
    TextView textConsultarInventario;



    private String NUMERO_DOCUMENTO;
    int slideIdEstablecimiento;

    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DbAdapter_Comprob_Venta dbAdapter_comprob_venta;

    private long id;

    //FRIMADO DIGITAL
    public InputStream keystore;
    private final static String BKS  ="union.bks";

    //XML DATOS
    private Map<String, String> map = new HashMap<>();

    private int ventaRRPP = -1;


    ArrayAdapter<CharSequence> adapterFormaPago ;
    ArrayAdapter<CharSequence> adapterFormaPagoCredito ;

    Utils df = new Utils();


    //FIREBASE
    private Firebase rootRef = null;
    private Firebase creditoRef = null;

    private static String TAG = VMovil_Venta_Cabecera.class.getSimpleName();


    private MenuItem menuSemaforo ;
    private int statusSemaforo = -1;
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
               .setTitle("No terminó la venta")
               .setMessage("¿Está seguro que desea salir?")
               .setNegativeButton(R.string.no, null)
               .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!

                       dbHelper_temp_venta.deleteAllTempVentaDetalle();
                       //((MyApplication)mContext.getApplicationContext()).setDisplayedHistorialComprobanteAnterior(false);
                       session.deleteVariable(6);
                       session.createTempSession(6, 0);
                       session.deleteVariable(5);
                       session.createTempSession(5, 0);

                       finish();
                       Intent intent = new Intent(main, clase);
                       startActivity(intent);
                   }
               }).create();
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.princ_venta_cabecera);

        Firebase.setAndroidContext(this);
        contexto = getApplicationContext();


        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        anchoImpresora= Integer.parseInt(SP.getString("impresoraAncho", "3"));

        rootRef = new Firebase(Constants._APP_ROOT_FIREBASE);
        creditoRef = rootRef.child(Constants._CHILD_CREDITO);

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
        dbAdapter_modalidadCredito = new DbAdapter_ModalidadCredito(this);
        dbAdapter_modalidadCredito.open();

        dbAdapter_forma_pago = new DbAdapter_Forma_Pago(this);
        dbAdapter_forma_pago.open();



        idEstablecimiento = session.fetchVarible(2);

        id_agente_venta = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);

        adapterFormaPago = ArrayAdapter.createFromResource(this, R.array.forma_pago, android.R.layout.simple_spinner_item);
        adapterFormaPagoCredito = ArrayAdapter.createFromResource(this,R.array.forma_pago_credito,android.R.layout.simple_spinner_item);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final Cursor cursorEstablecimiento = dbHelper_Evento_Establecimiento.fetchEstablecsById(""+idEstablecimiento);
        cursorEstablecimiento.moveToFirst();
        id_categoria_establecimiento = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_id_cat_est));
        diasCreditoPorDefecto = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_dias_credito));

        int idTipoDocCliente = 0;
        if (cursorEstablecimiento.getCount()>0) {
            idTipoDocCliente = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_id_tipo_doc_cliente));
        }

        spinnerTipoDocumento = (Spinner) findViewById(R.id.VC_spinnerTipoDocumento);

        int textArrayID = R.array.tipoDocumentoFacturaBoleta;
        switch (idTipoDocCliente){
            case 1:
                textArrayID = R.array.tipoDocumentoBoleta;
                break;
            case 2:
                textArrayID = R.array.tipoDocumentoFacturaBoleta;
                break;
            default:
                textArrayID = R.array.tipoDocumentoBoleta;
                break;
        }

        ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this, textArrayID ,android.R.layout.simple_spinner_item);
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDocumento.setPrompt("Seleccionar Documento");
        spinnerTipoDocumento.setAdapter(adapterTipoDocumento);



        spinnerTipoPago = ((Spinner) findViewById(R.id.VC_spinnerTipoPago));


        Cursor cr = dbAdapter_forma_pago.fetchAllFormaPagos();
        SimpleCursorAdapter simpleCursorAdapter;
        int[] to = new int[]{
                R.id.textSpinner,
        };

        String[] columns = new String[]{
                DbAdapter_Forma_Pago.FP_detalle,

        };
        simpleCursorAdapter = new SimpleCursorAdapter(VMovil_Venta_Cabecera.this, R.layout.toolbar_spinner_item_fp, cr, columns, to, 0);
        simpleCursorAdapter.setDropDownViewResource(R.layout.toolbar_spinner_item_dropdown_fp);
        spinnerTipoPago.setAdapter(simpleCursorAdapter);


        for (int i = 0; i < spinnerTipoPago.getCount(); i++) {
            Cursor value = (Cursor) spinnerTipoPago.getItemAtPosition(i);
            long id = value.getLong(value.getColumnIndex("_id"));
            int FP_selected = value.getInt(value.getColumnIndex(DbAdapter_Forma_Pago.FP_selected));
            if (FP_selected == 1) {
                spinnerTipoPago.setSelection(i);
            }
        }





        spinnerFormaPago = (Spinner) findViewById(R.id.VC_spinnerFormaPago);
        adapterFormaPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormaPago.setPrompt("Seleccionar Forma de Pago");
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

            //DFESACTIVO EL AUTOCOMPLETE TEXTVIEW PARA QUE NO PUEDAN AGREGAR MÁS PRODUCTOS.
            autoCompleteTextView.setEnabled(false);
            autoCompleteTextView.setHint("Crédito Establecido");

            ArrayAdapter<CharSequence> adapterFormaPagoCredito = ArrayAdapter.createFromResource(this,R.array.forma_pago_credito,android.R.layout.simple_spinner_item);
            spinnerFormaPago.setAdapter(adapterFormaPagoCredito);

            int tipoDocumentoSelectedAnterior = -1;
            tipoDocumentoSelectedAnterior = session.fetchVarible(Constants.SESSION_DOCUMENTO);
            switch (tipoDocumentoSelectedAnterior){
                //seleccionado Factura
                case 1:
                    spinnerTipoDocumento.setSelection(adapterTipoDocumento.getPosition(Constants._FACTURA));
                    break;
                //seleccionado Boleta
                case 2:
                    spinnerTipoDocumento.setSelection(adapterTipoDocumento.getPosition(Constants._BOLETA));
                    break;
                //Ninguno SEleccionado
                default:
                    spinnerTipoDocumento.setSelection(adapterTipoDocumento.getPosition(Constants._SPINNER_DEFAULT_COMPROBANTE));
                    break;
            }

            /*ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this,textArrayID,android.R.layout.simple_spinner_item);
            adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTipoDocumento.setPrompt("Seleccionar Documento");
            spinnerTipoDocumento.setAdapter(adapterTipoDocumento);*/

            spinnerTipoPago.setEnabled(false);
            spinnerTipoPago.setVisibility(View.GONE);


        }



        spinnerTipoPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = dbAdapter_forma_pago.fetchFormaPagoByID(id);
                Log.d(TAG, "cursor forma pago selected count : " + cursor.getCount());
                if (cursor.getCount() > 0) {
                    _id_tipo_pago = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Forma_Pago.FP_id_forma_pago));
                    Log.d(TAG, "_id tipo pago selected : " + _id_tipo_pago);
                }else{

                    Log.d(TAG, "SPINNER TIPO PAGO NOT WORKING WELL. NOT RECORDS");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

                String formaPago = adapterView.getItemAtPosition(i).toString();
                formaPagoSelected(formaPago);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tipoDocumento = spinnerTipoDocumento.getSelectedItem().toString();

                if (tipoDocumento.equals(Constants._FACTURA)){
                    textViewFooterText.setText("Total :\n" +
                            "Base imponible :\n" +
                            "IGV :");

                    textViewFooterTotal.setText(" S/. "+df.format(totalFooter)+"\n" +
                            "S/. "+df.format(base_imponibleFooter)+ "\n" +
                            "S/. "+df.format(igvFooter));
                }else if (tipoDocumento.equals(Constants._BOLETA)){
                    textViewFooterText.setText("Total :");
                    textViewFooterTotal.setText("S/. "+df.format(totalFooter));
                }else
                {
                    //DO NOTHING
                }

/*                TipoDocumento tipoDocumento1 = TipoDocumento.valueOf(tipoDocumento);
//                DecimalFormat df = new DecimalFormat("0.00");

                switch (tipoDocumento1){
                    case Factura:

                        break;
                    case Boleta:

                        break;
                    case Ficha:
                        textViewFooterText.setText("Total :");
                        textViewFooterTotal.setText("S/. "+df.format(totalFooter));
                        break;
                }
*/
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
                Log.d("BEFORE TEXTCHANGED", "" + charSequence.toString() + "," + i + "," + i2 + "," + i3);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                Log.d("ON TEXTCHANGED", "" + charSequence.toString() + "," + i + "," + i2 + "," + i3);
                if (i == 0 && i2 == 0 && i3 >= 10) {
                    Log.d("ON TEXTCHANGED", "HA ESCANEADO UN PRODUCTO");
                    Cursor cursor = dbHelper_Stock_Agente.fetchStockAgenteByIdEstablecimientoandName(id_categoria_establecimiento, charSequence.toString(), idLiquidacion);


                    autoCompleteTextView.setText("");

                    //Log.d("TEXTCHANGED COUNT CURSOR",""+cursor.getCount());
                    if (cursor.getCount() >= 1) {
                        cursor.moveToFirst();
                        //El item seleccionado tenía sólo 2 columnas visibles, pero en el cursos se encuentran todas las columnas
                        //Aquí podemos obtener las otras columnas para los que querramos hacer con ellas
                        nombre = cursor.getString(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_nombre));
                        id_producto = cursor.getInt(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_id_producto));
                        codigo_producto = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Stock_Agente.ST_codigo));
                        disponible = cursor.getInt(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_disponible));
                        valor_unidad = cursor.getInt(cursor.getColumnIndex(DbAdapter_Precio.PR_valor_unidad));

                        Cursor cursorExistProductoTemp = dbHelper_temp_venta.fetchTempVentaByIDProducto(id_producto);
                        if (cursorExistProductoTemp.getCount() > 0) {
                            Toast toast = Toast.makeText(mainActivity, "YA EXISTE", Toast.LENGTH_SHORT);
                            toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                            v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                            toast.show();
                        } else {
                            if (disponible > 0) {


                                Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto, id_categoria_establecimiento);

                                if (mCursorPrecioUnitarioGeneral.getCount() > 1) {
                                    myTextDialogValorUnidad().show();
                                    //Toast.makeText(getApplicationContext(), "Hay "+mCursorPrecioUnitarioGeneral.getCount() + " valores unidades para este producto", Toast.LENGTH_SHORT).show();
                                } else {
                                    myTextDialog().show();
                                }
                            } else {
                                Toast toast = Toast.makeText(mainActivity, "No hay Stock", Toast.LENGTH_SHORT);
                                toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                                toast.show();
                            }
                        }
                    } else {
                        Toast toast = Toast.makeText(mainActivity, "No encontrado en almacén", Toast.LENGTH_SHORT);
                        toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                        toast.show();
                    }


                } else {
                    adapterProductos.getFilter().filter(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("AFTER TEXTCHANGED", "" + editable.toString());
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
                codigo_producto = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Stock_Agente.ST_codigo));
                disponible = cursor.getInt(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_disponible));
                valor_unidad = cursor.getInt(cursor.getColumnIndex(DbAdapter_Precio.PR_valor_unidad));

                Cursor cursorExistProductoTemp = dbHelper_temp_venta.fetchTempVentaByIDProducto(id_producto);
                if (cursorExistProductoTemp.getCount() > 0) {
                    Toast toast = Toast.makeText(mainActivity, "YA EXISTE", Toast.LENGTH_SHORT);
                    toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                    toast.show();

                } else {
                    if (disponible > 0) {

                        Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto, id_categoria_establecimiento);

                        if (mCursorPrecioUnitarioGeneral.getCount() > 1) {
                            myTextDialogValorUnidad().show();
                            //Toast.makeText(getApplicationContext(), "Hay "+mCursorPrecioUnitarioGeneral.getCount() + " valores unidades para este producto", Toast.LENGTH_SHORT).show();
                        } else {
                            myTextDialog().show();
                        }
                    } else {
                        Toast toast = Toast.makeText(mainActivity, "No hay Stock", Toast.LENGTH_SHORT);
                        toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                        toast.show();
                    }
                }
            }
        });

        /*listView.setOnTouchListener(new ListView.OnTouchListener() {
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
        });*/


        spinnerTipoPago.setEnabled(false);
        spinnerTipoPago.setVisibility(View.GONE);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int numeroViews = adapterView.getCount();
                if (adapterView.getPositionForView(view) != numeroViews) {

                    Log.d("POSITION SELECTED", adapterView.getPositionForView(view) + " - " + adapterView.getCount());
                    Log.d("POSITION ", i + " - " + adapterView.getCount());

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
                                                    .setNegativeButton(R.string.no, null)
                                                    .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // FIRE ZE MISSILES!
                                                            boolean succesful = dbHelper_temp_venta.deleteTempVentaDetalleById(id_tem_detalle);
                                                            if (succesful) {
                                                                Toast toast = Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_SHORT);
                                                                toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                                                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                                                v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                                                                toast.show();

                                                                mostrarProductosParaVender();
                                                            } else {
                                                                Toast toast = Toast.makeText(getApplicationContext(), "No se pudo eliminar, intente nuevamente", Toast.LENGTH_SHORT);
                                                                toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                                                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                                                v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                                                                toast.show();
                                                            }
                                                        }
                                                    }).create().show();
                                            break;

                                    }
                                }
                            }).create().show();

                }
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("POSITION SELECTED", parent.getPositionForView(view) + " - " + parent.getCount());
                Log.d("POSITION ", position + " - " + parent.getCount());
                //Aquí obtengo el cursor posicionado en la fila que ha seleccionado/clickeado
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                final long id_tem_detalle = cursor.getLong(cursor.getColumnIndex(DBAdapter_Temp_Venta.temp_venta_detalle));
                myEditDialog(id_tem_detalle).show();

            }
        });
        //SLIDING MENU
        showSlideMenu(this);
        Query queryRef = creditoRef.orderByChild("fecha").equalTo(Utils.getDatePhone());
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "ADDED dataSnapshot : " + dataSnapshot.getValue());
                Credito credito = dataSnapshot.getValue(Credito.class);
                Log.d(TAG, "ADDED Monto : " + credito.getMontoCredito());
                Log.d(TAG, "ADDED Estado: " + credito.getEstado());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "CHANGED dataSnapshot : " + dataSnapshot.getValue());
                Credito credito = dataSnapshot.getValue(Credito.class);

                if ((credito.getIdAgente() == id_agente_venta) && credito.getIdEstablecimiento() == idEstablecimiento){

                    switch (credito.getEstado()){
                        case Constants._CREDITO_APROBADO:
                            Log.d(TAG, "CRÉDITO APROBADO : " +credito.getIdEstablecimiento() + ", "+credito.getIdAgente());
                            Log.d(TAG, "CHANGED Monto : " + credito.getMontoCredito());
                            Log.d(TAG, "CHANGED DÍAS DE CRÉDITO : " + credito.getDiasCredito());
                            Log.d(TAG, "CHANGED Estado: " + credito.getEstado());
                            //HABILITO EL BOTÓN
                            //EL SPINNER FORMA DE PAGO AL CRÉDITO
                            //spinnerFormaPago.setAdapter(adapterFormaPagoCredito);
                            //actualizar el registro de la db here!

                            int nroRegistrosUpdate= dbHelper_Evento_Establecimiento.updateEstablecsCredito(credito.getIdEstablecimiento(), credito.getMontoCredito(), credito.getDiasCredito());
                            Log.d(TAG, "NRO ESTABLECIMIETNO CRÉDITOS ACTUALIZADOS : " + nroRegistrosUpdate);

                            if (nroRegistrosUpdate>=1){
                                //SEMÁFORO STATUS = VERDE
                                updateSemaforoStatus(Constants.SEMAFORO_VERDE);
                                //FORMA DE PAGO AL CRÉDITO
                                formaPagoSelected(Constants._CREDITO);
                                //HABILITO EL BOTÓN DE VENDER
                                buttonVender.setEnabled(true);
                                buttonVender.setBackgroundResource(R.color.Dark5);

                                Toast.makeText(VMovil_Venta_Cabecera.this,  "CRÉDITO ACEPTADO PARA : " + nombreEstablecimiento + ".", Toast.LENGTH_SHORT).show();
                            }else{
                                //HUBO UN ERROR
                                Toast.makeText(VMovil_Venta_Cabecera.this,  "HUBO UN ERROR.", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        case Constants._CREDITO_RECHAZADO:
                            Log.d(TAG, "CRÉDITO RECHAZADO : " +credito.getIdEstablecimiento() + ", "+credito.getIdAgente());
                            Log.d(TAG, "CHANGED Monto : " + credito.getMontoCredito());
                            Log.d(TAG, "CHANGED Estado: " + credito.getEstado());


                            spinnerFormaPago.setAdapter(adapterFormaPago);
                            updateSemaforoStatus(Constants.NO_ASIGNADO);
                            buttonVender.setBackgroundResource(R.color.Dark5);
                            buttonVender.setEnabled(true);

                            if(!((Activity) VMovil_Venta_Cabecera.this).isFinishing() ){
                                new AlertDialog.Builder(mContext)
                                        .setTitle("CREDITO RECHAZADO")
                                        .setMessage("Observación : " + credito.getObservacion())
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //DO NOTHING
                                            }
                                        })
                                        .setCancelable(false)
                                        .create().show();
                            }

                            break;
                        case Constants._CREDITO_PENDIENTE:
                            break;
                        default:
                            break;
                    }


                }else{
                    updateSemaforoStatus(Constants.SEMAFORO_ROJO);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

/*        creditoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "There are " + dataSnapshot.getChildrenCount() + " CREDITOS");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Credito credito = postSnapshot.getValue(Credito.class);
                    Log.d(TAG, "Monto : " + credito.getMontoCredito());
                    Log.d(TAG, "Estado: " + credito.isEstado());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/

    }

    class ActualizarEstablecimiento extends AsyncTask<String, String, String>{

        private String TAG  = ActualizarEstablecimiento.class.getSimpleName();
        StockAgenteRestApi api = null;
        Handler mHandler;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mHandler = new Handler();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<EventoEstablecimiento> eventoEstablecimientos = null;
            api = new StockAgenteRestApi(mainActivity);
            try {
                JSONObject jsonObjectEventoEstablecimiento = api.GetEstablecimeintoXRuta(idLiquidacion, Utils.getDatePhone(), id_agente_venta);
                Log.d(TAG,"JSON OBJECT EVENTO ESTABLECIMIENTO : "+ jsonObjectEventoEstablecimiento.toString());
                ParserEventoEstablecimiento parserEventoEstablecimiento = new ParserEventoEstablecimiento();
                eventoEstablecimientos = parserEventoEstablecimiento.parserEventoEstablecimiento(jsonObjectEventoEstablecimiento);


                for (int i = 0; i < eventoEstablecimientos.size(); i++) {
                    Log.d(TAG, "ESTABLECIMIENTOS X RUTAS: " + i+ " Nombre Establecimiento : " + eventoEstablecimientos.get(i).getNombreEstablecimiento() + ", orden : " + eventoEstablecimientos.get(i).getOrden() + ", MONTO CRÉDITO: " + eventoEstablecimientos.get(i).getMontoCredito());
                    boolean existe = dbAdaptert_evento_establec.existeEstablecsById(eventoEstablecimientos.get(i).getIdEstablecimiento());

                    Log.d("EXISTE ESTABLECIMIENTO", "" + existe);
                    if (existe) {
                        //dbAdapter_comprob_cobro.updateComprobCobros(comprobanteCobros.get(i));
                        dbAdaptert_evento_establec.updateEstablecimientos(eventoEstablecimientos.get(i), id_agente_venta, idLiquidacion);
                    } else {
                        //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                        long id = dbAdaptert_evento_establec.createEstablecimientos(eventoEstablecimientos.get(i), id_agente_venta, idLiquidacion,0);

                        Log.d(TAG, "IMPORT INSERT ESTABLECIMIENTO id : " + id);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mHandler.post(new DisplayToast(mainActivity, "CRÉDITO ACEPTADO PARA : " + nombreEstablecimiento + "."));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    formaPagoSelected(Constants._CREDITO);
                    buttonVender.setEnabled(true);
                    buttonVender.setBackgroundResource(R.color.Dark5);
                }
            });

        }
    }

    public void formaPagoSelected(String formaPago){

        final String tipoDocumento = spinnerTipoDocumento.getSelectedItem().toString();
        if(formaPago.equals(Constants._CONTADO)){
            //DO NOTHING
            updateSemaforoStatus(Constants.NO_ASIGNADO);
            buttonVender.setEnabled(true);
            buttonVender.setBackgroundResource(R.color.Dark5);

            spinnerTipoPago.setEnabled(true);
            spinnerTipoPago.setVisibility(View.VISIBLE);


        }else if(formaPago.equals(Constants._CREDITO)){

            spinnerTipoPago.setEnabled(false);
            spinnerTipoPago.setVisibility(View.GONE);
            if (isEstablecidasCuotas){

            }
            else if(!((Activity) VMovil_Venta_Cabecera.this).isFinishing() ){

                Cursor cursorEstablecimientoCredito = dbHelper_Evento_Establecimiento.fetchEstablecsById(""+idEstablecimiento);
                cursorEstablecimientoCredito.moveToFirst();
                int montoCredito = -1;
                Double montoCreditoDouble = 0.0;
                montoCredito = cursorEstablecimientoCredito.getInt(cursorEstablecimientoCredito.getColumnIndexOrThrow(dbHelper_Evento_Establecimiento.EE_monto_credito));
                montoCreditoDouble = cursorEstablecimientoCredito.getDouble(cursorEstablecimientoCredito.getColumnIndexOrThrow(dbHelper_Evento_Establecimiento.EE_monto_credito));

                //VALIDAR QUE EL MONTO DE CRÉDITO NO SEA MENRO A 0


                switch (montoCredito) {

                    case 0:

                        if (conectadoWifi() || conectadoRedMovil()) {
                            new AlertDialog.Builder(VMovil_Venta_Cabecera.this)
                                    .setTitle("Ops, No cuenta con crédito")
                                    .setMessage("" +
                                            "¿Desea solicitar crédito?")
                                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            spinnerFormaPago.setAdapter(adapterFormaPago);
                                        }
                                    })
                                    .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialogSolicitarCredito(totalFooter).show();
                                        }
                                    })
                                    .setCancelable(false)
                                    .create().show();

                        } else {
                            Toast toast = Toast.makeText(mContext, "Sin crédito y sin conexión", Toast.LENGTH_SHORT);
                            toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                            v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                            toast.show();
                            spinnerFormaPago.setAdapter(adapterFormaPago);
                        }

                        break;
                    default:
                        if (simpleCursorAdapter.getCursor().getCount() <= 0) {

                            Toast toast = Toast.makeText(mContext, "AGREGAR PRODUCTOS A LA VENTA", Toast.LENGTH_SHORT);
                            toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                            v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                            toast.show();

                            spinnerFormaPago.setAdapter(adapterFormaPago);

                            break;
                        }

                        //TENER EL MONTO DE CRÉDITO DEL ESTABLECIMIENTO

                        //TENER EL MONTO DE LA VENTA ACTUAL.


                        //VALIDAR QUE EL MONTO DE CRÉDITO SEA MAYOR A 0
                        if (montoCreditoDouble >=0){

                            if(totalFooter <= montoCreditoDouble){
                                new AlertDialog.Builder(VMovil_Venta_Cabecera.this)
                                        .setTitle("Crédito Aceptado")
                                        .setMessage("¿Desea continuar?")
                                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                spinnerFormaPago.setAdapter(adapterFormaPago);

                                                updateSemaforoStatus(Constants.NO_ASIGNADO);
                                                buttonVender.setEnabled(true);
                                                buttonVender.setBackgroundResource(R.color.Dark5);
                                            }
                                        })
                                        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //GUARDAR EL TIPO DE DOCUMENTO
                                                int tipoDocumentoSelected = -1;

                                                switch(tipoDocumento){
                                                    case Constants._FACTURA:
                                                        tipoDocumentoSelected = 1;
                                                        break;
                                                    case Constants._BOLETA:
                                                        tipoDocumentoSelected = 2;
                                                        break;
                                                    default:
                                                        tipoDocumentoSelected = -1;
                                                        break;
                                                }
                                                session.deleteVariable(Constants.SESSION_DOCUMENTO);
                                                session.createTempSession(Constants.SESSION_DOCUMENTO, tipoDocumentoSelected);


                                                Intent intent = new Intent(getApplicationContext(), VMovil_Venta_Cabecera_PlanPagos.class);
                                                intent.putExtra("total", totalFooter);
                                                finish();
                                                startActivity(intent);
                                            }
                                        })
                                        .setCancelable(false)
                                        .create().show();
                            }else{
                                final int finalMontoCredito = montoCredito;
                                final Double finalMontoCreditoDouble = montoCreditoDouble;
                                new AlertDialog.Builder(VMovil_Venta_Cabecera.this)
                                        .setTitle("Solicitar Incremento de Crédito")
                                        .setMessage("Venta total : " + df.format(totalFooter)+"\n" +
                                                "Crédito actual: "+df.format(montoCreditoDouble)+"\n" +
                                                "Crédito adicional : " + df.format(totalFooter - finalMontoCreditoDouble))
                                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                spinnerFormaPago.setAdapter(adapterFormaPago);

                                                updateSemaforoStatus(Constants.NO_ASIGNADO);
                                                buttonVender.setEnabled(true);
                                                buttonVender.setBackgroundResource(R.color.Dark5);
                                            }
                                        })
                                        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialogSolicitarCredito(totalFooter).show();
                                                //MANDAR EL NUEVO CRÉDITO
                                            }
                                        })
                                        .setCancelable(false)
                                        .create().show();
                            }

                        }else{
                            if (conectadoWifi() || conectadoRedMovil()) {
                                new AlertDialog.Builder(VMovil_Venta_Cabecera.this)
                                        .setTitle("Ops, No cuenta con crédito")
                                        .setMessage("" +
                                                "¿Desea solicitar crédito?")
                                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                spinnerFormaPago.setAdapter(adapterFormaPago);
                                            }
                                        })
                                        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialogSolicitarCredito(totalFooter).show();
                                            }
                                        })
                                        .setCancelable(false)
                                        .create().show();

                            } else {
                                Toast toast = Toast.makeText(mContext, "Sin crédito y sin conexión", Toast.LENGTH_SHORT);
                                toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                                toast.show();
                                spinnerFormaPago.setAdapter(adapterFormaPago);
                            }
                        }




                        break;
                }
            }else {
                Utils.setToast(contexto, "EL CRÉDITO YA HA SIDO ACEPTADO.", R.color.verde);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.princ_venta_principal, menu);
        menuSemaforo = menu.findItem(R.id.bandera);
        updateSemaforoStatus(statusSemaforo);
        return true;
    }

    private void updateSemaforoStatus(int status){
        statusSemaforo = status;
        switch (statusSemaforo){
            //AMBAR
            case 1:
                menuSemaforo.setIcon(R.mipmap.ic_ambar).setVisible(true);
                break;
            //ROJO
            case 2:
                menuSemaforo.setIcon(R.mipmap.ic_redc).setVisible(true);
                break;
            //VERDE
            case 3:
                menuSemaforo.setIcon(R.mipmap.ic_green).setVisible(true);
                break;

            default:
                menuSemaforo.setIcon(R.mipmap.ic_green).setVisible(false);
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.fiscal:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                Utils.setToast(mainActivity, "DIRECCIÓN FISCAL", R.color.verde);
                direccionCV = Constants._DIRECCION_FISCAL;
                return true;
            case R.id.establecimiento:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                direccionCV = Constants._DIRECCION_ESTABLECIMIENTO;
                Utils.setToast(mainActivity, "DIRECCIÓN ESTABLECIMIENTO", R.color.verde);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
/*
        switch (item.getItemId()) {
            *//*case R.id.ventaRRPP:

                ventaRRPP =1;
                setTitle("Venta RR.PP.");
                *//**//*IntentIntegrator intentIntegrator = new IntentIntegrator(mainActivity);
                intentIntegrator.initiateScan();*//**//*
                Toast.makeText(contexto, "VENTA RRPP", Toast.LENGTH_LONG).show();

                //DOCUMENTO SÓLO BOLETA
                ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this,R.array.tipoDocumentoBoleta,android.R.layout.simple_spinner_item);
                adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTipoDocumento.setAdapter(adapterTipoDocumento);

                //TIPO DE PAGO SÓLO AL CONTADO
                final ArrayAdapter<CharSequence> adapterFormaPago = ArrayAdapter.createFromResource(this,R.array.forma_pago_contado,android.R.layout.simple_spinner_item);
                adapterFormaPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFormaPago.setAdapter(adapterFormaPago);


                setVentaRRPP();

                //TODOS LOS ELEMENTOS DEBERÁN TENER PRECIO DE 0

                //SE DEBERÁ MANDAR UN PARÁMETRO A IMPRESIÓN, Y A GENERACIÓN DEL FIRMADO QUE ES RR PP

                break;*//*
            *//*case R.id.ventaNormal:
                ventaRRPP = -1;
                setTitle("Venta de Productos");

                dbHelper_temp_venta.deleteAllTempVentaDetalle();
                mostrarProductosParaVender();
                break;*//*
        }*/
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
                mCursorScannerProducto.moveToFirst();

                if (mCursorScannerProducto.getCount()>0){
                    id_producto  = mCursorScannerProducto.getInt(mCursorScannerProducto.getColumnIndexOrThrow(DbAdapter_Precio.PR_id_producto));
                    Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto,id_categoria_establecimiento);
                    if (mCursorPrecioUnitarioGeneral.getCount()>1){
                        scannerDialogValorUnidad().show();
                    }else{
                        scannerDialog().show();
                    }

                }else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Producto con código de barras : "+ barcodeScan + "no disponible en el Stock Actual y/o Categoría establecimiento", Toast.LENGTH_SHORT);
                    toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                    toast.show();
                }
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), "No ha Scaneado ningún producto", Toast.LENGTH_SHORT);
                toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                toast.show();
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

//        DecimalFormat df = new DecimalFormat("0.00");
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
        builder.setPositiveButton(R.string.si, new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String texto = savedText.getText().toString().trim();
                if(texto.equals("")){
                    texto = "1";
                }

                cantidad = Integer.parseInt(texto);

                //Toast.makeText(getApplicationContext(),"Cantidad : "+cantidad + " id_producto : "+ id_producto,Toast.LENGTH_LONG).show();

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
                long id = dbHelper_temp_venta.createTempVentaDetalle(1,finalIdProducto, finalNombreP,cantidad,round(total_importe,2), round(precio_unitario,2), promedio_anterior, devuelto, procedencia, 1, ""+finalIdProducto);

                mostrarProductosParaVender();
            }
        });
        builder.setView(layout);
        return builder.create();
    }
    private Dialog scannerDialogValorUnidad(){
            final View layout = View.inflate(this, R.layout.dialog_cantidad_productos_valor_unidad, null);

//            DecimalFormat df = new DecimalFormat("0.00");
            final EditText savedText = ((EditText) layout.findViewById(R.id.VCAP_editTextCantidad));
            final TextView nombreProducto = ((TextView) layout.findViewById(R.id.VCPA_textView2NombreProducto));
            final TextView precio = ((TextView) layout.findViewById(R.id.VCPA_textViewPrecio));
            final TextView valorUnidad = (TextView) layout.findViewById(R.id.VCPA_textViewValorUnidad);

            final double[] precio_unitario = {0.0};

            final Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto, id_categoria_establecimiento);
            mCursorPrecioUnitarioGeneral.moveToFirst();

            if (mCursorPrecioUnitarioGeneral.getCount()>0) {
                precio_unitario[0] = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
                valor_unidad = mCursorPrecioUnitarioGeneral.getInt(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_valor_unidad));
                nombre = mCursorPrecioUnitarioGeneral.getString(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_nombreProducto));
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

                    if (mCursorPrecioUnitarioGeneral.isLast()) {
                        mCursorPrecioUnitarioGeneral.moveToFirst();
                    } else {
                        mCursorPrecioUnitarioGeneral.moveToNext();
                    }
                    precio_unitario[0] = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
                    valor_unidad = mCursorPrecioUnitarioGeneral.getInt(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_valor_unidad));
//                    DecimalFormat df = new DecimalFormat("0.00");
                    precio.setText("Precio : S/. " + df.format(precio_unitario[0]));
                    valorUnidad.setText("Unidad : " + valor_unidad);


                }
            });


            Cursor mCursorStock = dbHelper_Stock_Agente.fetchByIdProducto(id_producto,idLiquidacion);
            int maximoValor = 1;
            if (mCursorStock.getCount()>0){
                maximoValor = mCursorStock.getInt(mCursorStock.getColumnIndexOrThrow(dbHelper_Stock_Agente.ST_disponible));
            }

            savedText.setFilters(new InputFilter[]{new InputFilterMinMax(1, maximoValor)});





            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cantidad");
            final int finalMaximoValor = maximoValor;
            builder.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
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
                    long id = dbHelper_temp_venta.createTempVentaDetalle(1, id_producto, nombre, cantidad, round(total_importe, 2), round(precio_unitario[0], 2), promedio_anterior, devuelto, procedencia, valor_unidad, "" + id_producto);
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
                        Log.d("FOCUS", "ALERT YES");
                        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    } else {
                        Log.d("FOCUS", "ALERT FALSE");
                        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    }
                }
            });
            return alertDialog;
    }

    private void setVentaRRPP(){

        int registrosUpdated = dbHelper_temp_venta.updateTempVentaRRPP();
        mostrarProductosParaVender();

    }

    private Dialog dialogSolicitarCredito(Double total) {

        final View layout = View.inflate(this, R.layout.dialog_solicitar_credito, null);

        final String tipoDocumento = spinnerTipoDocumento.getSelectedItem().toString();

        final EditText editTextCantidadCredito = ((EditText) layout.findViewById(R.id.VCSC_editText_CantidadCredito));

        String cantidadSugeridad = Utils.replaceComa(df.format(total));
        if (cantidadSugeridad.equals("0.00")){
            editTextCantidadCredito.setText("100.00");
        }else{
            editTextCantidadCredito.setText(cantidadSugeridad);
        }



        spinnerDiasCredito = ((Spinner) layout.findViewById(R.id.VCSC_spinner_DiasCredito));


        Cursor cr = dbAdapter_modalidadCredito.fetchAllModalidadCreditos();
        SimpleCursorAdapter simpleCursorAdapter;
        int[] to = new int[]{
                R.id.textSpinner,
        };

        String[] columns = new String[]{
                DbAdapter_ModalidadCredito.TM_descripcion,

        };
        simpleCursorAdapter = new SimpleCursorAdapter(VMovil_Venta_Cabecera.this, R.layout.toolbar_spinner_item_actionbar, cr, columns, to, 0);
        simpleCursorAdapter.setDropDownViewResource(R.layout.toolbar_spinner_item_dropdown);
        spinnerDiasCredito.setAdapter(simpleCursorAdapter);


        for (int i = 0; i < spinnerDiasCredito.getCount(); i++) {
            Cursor value = (Cursor) spinnerDiasCredito.getItemAtPosition(i);
            long id = value.getLong(value.getColumnIndex("_id"));
            int TM_dias_credito = value.getInt(value.getColumnIndex(DbAdapter_ModalidadCredito.TM_dias_credito));
            if (TM_dias_credito == diasCreditoPorDefecto) {
                spinnerDiasCredito.setSelection(i);
            }
        }


/*
        spinnerDiasCredito.setSelection(simpleCursorAdapter.getItem(simpleCursorAdapter.getCursor().getPosition()));*/


        spinnerDiasCredito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = dbAdapter_modalidadCredito.fetchModalidadCreditoByID(id);
                if (cursor.getCount() > 0) {
                    diasCredito = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_ModalidadCredito.TM_dias_credito));
                }else{
                    diasCredito = "7";
                    Log.d(TAG, "SPINNER DIAS DE CRÉDITO NOT WORKING WELL.");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Crédito a Solicitar");
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                spinnerFormaPago.setAdapter(adapterFormaPago);
            }
        });
        builder.setPositiveButton(R.string.si, new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //PINTAR
                updateSemaforoStatus(Constants.SEMAFORO_ROJO);
                //DESACTIVAR EL BOTON
                buttonVender.setEnabled(false);
                buttonVender.setBackgroundColor(getResources().getColor(R.color.Dark3));

                String cantidad = Utils.replaceComa(editTextCantidadCredito.getText().toString().trim());
                if (cantidad.length() > 0 && cantidad.length() < 10) {
                    final Double cantidadCredito = Double.parseDouble(cantidad);
                    if (diasCredito==null){
                        diasCredito = "7";
                    }
                    Credito credito = new Credito(id_agente_venta, idEstablecimiento, cantidadCredito, Integer.parseInt(diasCredito), Constants._CREDITO_PENDIENTE, Constants._CREDITO, Utils.getDatePhone());
                    final Firebase newCreditoRef = creditoRef.push();
                    newCreditoRef.setValue(credito, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError != null) {
                                Log.d(TAG, "Data could not be saved. " + firebaseError.getMessage());
                                spinnerFormaPago.setAdapter(adapterFormaPago);
                                Utils.setToast(VMovil_Venta_Cabecera.this, "OCURRIÓ UN ERROR INTENTELO DE NUEVO", R.color.verde);
                            } else {
                                Log.d(TAG, "Data saved successfully.");
                                //GET UNIQUE ID, TIMESTAMP BASED
                                String postId = newCreditoRef.getKey();
                                Log.d(TAG, "GET KEY : " + postId);


                                //SI ES INCREMENTO Y NO MODIFICÓ, LE MANDO EL INCREMENTO A SULLY

                                //SI ES INCREMENTO Y SÍ MODIFICÓ, LE MANDO LA CANTIDAD MODIFICADA.
                                new SolicitarCredito(mainActivity).execute("" + id_agente_venta, "" + idEstablecimiento, "" + cantidadCredito, "" + diasCredito, postId);


                                //GUARDAR EL TIPO DE DOCUMENTO
                                int tipoDocumentoSelected = -1;
                                switch(tipoDocumento){
                                    case Constants._FACTURA:
                                        tipoDocumentoSelected = 1;
                                        break;
                                    case Constants._BOLETA:
                                        tipoDocumentoSelected = 2;
                                        break;
                                    default:
                                        tipoDocumentoSelected = -1;
                                        break;
                                }
                                session.deleteVariable(Constants.SESSION_DOCUMENTO);
                                session.createTempSession(Constants.SESSION_DOCUMENTO, tipoDocumentoSelected);

                                //PINTAR
                                updateSemaforoStatus(Constants.SEMAFORO_ROJO);
                                //DESACTIVAR EL BOTON
                                buttonVender.setEnabled(false);
                                buttonVender.setBackgroundColor(getResources().getColor(R.color.Dark3));
                            }
                        }
                    });



                } else {
                    Toast.makeText(VMovil_Venta_Cabecera.this, "Número invalido, intente de nuevo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setCancelable(false);
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





        Cursor mCursorTempVenta = dbHelper_temp_venta.fetchAllTempVentaDetalleByID(id_temp_venta_detalle);
        int id_producto_temp = -1;
        if (mCursorTempVenta.getCount()>0){
            id_producto_temp = mCursorTempVenta.getInt(mCursorTempVenta.getColumnIndexOrThrow(dbHelper_temp_venta.temp_id_producto));
        }
        Cursor mCursorStock = dbHelper_Stock_Agente.fetchByIdProducto(id_producto_temp,idLiquidacion);
        int maximoValor = 1;
        if (mCursorStock.getCount()>0){
            maximoValor = mCursorStock.getInt(mCursorStock.getColumnIndexOrThrow(dbHelper_Stock_Agente.ST_disponible));
        }

        ///disponible.setText(""+maximoValor);
        savedText.setFilters(new InputFilter[]{new InputFilterMinMax(1,maximoValor)});

        final int finalMaximoValor = maximoValor;
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
                    /*int cantidad = Integer.parseInt(savedText.getText().toString());
                    if (cantidad > finalMaximoValor) {
                        savedText.setError("No Hay Stock Suficiente.");
                    }else{
                    }*/
                    savedText.setError(null);
                }
            }
        });


        String nombre = mCursorTempVenta.getString(mCursorTempVenta.getColumnIndex(DBAdapter_Temp_Venta.temp_nom_producto));
        String devueltoText = mCursorTempVenta.getString(mCursorTempVenta.getColumnIndex(DBAdapter_Temp_Venta.temp_devuelto));
        String promedioAnteriorText = mCursorTempVenta.getString(mCursorTempVenta.getColumnIndex(DBAdapter_Temp_Venta.temp_prom_anterior));

        Double precio_unitario = mCursorTempVenta.getDouble(mCursorTempVenta.getColumnIndex(DBAdapter_Temp_Venta.temp_precio_unit));
        if (ventaRRPP==1){
            precio_unitario = 0.0;
        }

        nombreProducto.setText(nombre);
//        DecimalFormat df = new DecimalFormat("0.00");
        precio.setText("Precio : S/. "+df.format(precio_unitario));
        if (devueltoText==null) {
            devuelto.setText("Devueltos : 0");
        }else {
            devuelto.setText("Devueltos : " + devueltoText);
        }
        if (promedioAnteriorText==null){
            promedioAnterior.setText("Promedio Anterior : " +0);
        }else{
            promedioAnterior.setText("Promedio Anterior : " +promedioAnteriorText);
        }


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stock ("+maximoValor+")");
        final Double finalPrecio_unitario = precio_unitario;
        builder.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String texto = null;
                if (savedText.getText().toString().trim().equals("")) {
                    texto = "1";
                } else {
                    texto = savedText.getText().toString().trim();
                }

                int cantidad = Integer.parseInt(texto);

                dbHelper_temp_venta.updateTempVentaDetalleCantidad(id_temp_venta_detalle, cantidad, cantidad * finalPrecio_unitario);



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
/*    private enum FormaPago{
        Contado, Credito, Seleccionar
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /*case R.id.VC_buttonAgregarProductos:
                Intent intent  = new Intent(this, VMovil_Venta_Cabecera_AgregarProductos.class);
                finish();
                startActivity(intent);
                break;*/
            case R.id.slide_textViewConsultarInventario:
                startActivity(new Intent(this,VMovil_Consultar_Inventario.class));
                break;
            case R.id.slide_textViewCargarInventario:
                Intent cInventario = new Intent(this, VMovil_Cargar_Inventario.class);
                startActivity(cInventario);
                break;
            case R.id.VC_buttonVender:
                //((MyApplication)this.getApplication()).setDisplayedHistorialComprobanteAnterior(false);

                if (buttonVender.isEnabled()){

                    Cursor cursorTemp = simpleCursorAdapter.getCursor();

                    if (cursorTemp.getCount() <= 0) {
                        Log.d("CT VC", cursorTemp.getCount() + "");
                        Toast toast = Toast.makeText(mainActivity, "AGREGAR PRODUCTOS A LA VENTA", Toast.LENGTH_LONG);
                        toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                        toast.show();
                        return;
                    }

                    String formaPago = spinnerFormaPago.getSelectedItem().toString();
                    String tipoDocumento = spinnerTipoDocumento.getSelectedItem().toString();

                    if (formaPago.equals(Constants._SPINNER_DEFAULT_COMPROBANTE) && tipoDocumento.equals(Constants._SPINNER_DEFAULT_PAGO)){
                        Toast.makeText(VMovil_Venta_Cabecera.this, "Seleccionar Tipo de Documento, y Forma de pago", Toast.LENGTH_SHORT).show();
                        return;
                    }else if (formaPago.equals(Constants._SPINNER_DEFAULT_PAGO)){
                        Toast.makeText(VMovil_Venta_Cabecera.this, "Seleccionar Forma de pago", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(tipoDocumento.equals(Constants._SPINNER_DEFAULT_COMPROBANTE)) {
                        Toast.makeText(VMovil_Venta_Cabecera.this, "Seleccionar Tipo de Documento", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new AlertDialog.Builder(VMovil_Venta_Cabecera.this)
                            .setTitle("Confirmación")
                            .setMessage("¿Está seguro de generar el comprobante electrónico?")
                            .setNegativeButton(R.string.no, null)
                            .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                    vender();
                                    session.deleteVariable(6);
                                    session.createTempSession(6,0);
                                }
                            })
                            .create().show();
                }else{
                    Toast.makeText(VMovil_Venta_Cabecera.this, "Esperando Crédito.", Toast.LENGTH_SHORT).show();
                }
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
                Intent idh1 = new Intent(this, VMovil_Operacion_Canjes_Devoluciones.class);
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

    public void vender()  {

        String fecha = getDatePhone();
        //Obtener los datos de las ventas

        Toast.makeText(VMovil_Venta_Cabecera.this, "Generando ...", Toast.LENGTH_SHORT).show();


        String formaPago = spinnerFormaPago.getSelectedItem().toString();
        String tipoDocumento = spinnerTipoDocumento.getSelectedItem().toString();

        /*TipoDocumento tipoDocumento1 = TipoDocumento.valueOf(tipoDocumento);
        FormaPago formaPago1 = FormaPago.valueOf(formaPago);*/

        int tipoVenta = 1;
        if (ventaRRPP==1){
            tipoVenta = 2;
        }
        int numero_documento = 1;
        int estado_conexion = 0;

        int i_formaPago = 0;
        int estado_comprobante = Constants._CV_VALIDO;
        Double monto_total  = 0.0;
        Double igv  = 0.0;
        Double base_imponible = 0.0;
        String erp_stringTipoDocumento  = null;
        String serie = "XR";
        String codigo_erp = null;
        numeroDocumentoImpresion =null;

        Cursor cursorEstablecimiento = dbHelper_Evento_Establecimiento.fetchEstablecsById(""+idEstablecimiento);
        cursorEstablecimiento.moveToFirst();
        documentoCliente = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndexOrThrow(dbHelper_Evento_Establecimiento.EE_doc_cliente));
        nombreCliente = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndexOrThrow(dbHelper_Evento_Establecimiento.EE_nom_cliente));


        if (tipoDocumento.equals(Constants._FACTURA)){
            i_tipoDocumento = 1;
            erp_stringTipoDocumento="FV";
            serie = dbHelperAgente.getSerieFacturaByIdAgente(id_agente_venta,idLiquidacion );
            codigo_erp = erp_stringTipoDocumento+serie;
            numero_documento = session.fetchVarible(10);
            Log.d("CODIGO ERP ", codigo_erp);
            session.deleteVariable(10);
            session.createTempSession(10,numero_documento+1);
        }else if (tipoDocumento.equals(Constants._BOLETA)){
            i_tipoDocumento = 2;
            erp_stringTipoDocumento="BV";
            serie = dbHelperAgente.getSerieBoletaByIdAgente(id_agente_venta, idLiquidacion);
            codigo_erp = erp_stringTipoDocumento+serie;
            numero_documento = session.fetchVarible(11);
            session.deleteVariable(11);
            session.createTempSession(11,numero_documento+1);
            Log.d("CODIGO ERP ", codigo_erp);
        }else
        {
            Log.d("CODIGO ERP ", codigo_erp);
            //DO NOTHING
        }


        numeroDocumentoImpresion = serie + "-" +agregarCeros((String.valueOf(numero_documento)),8);
        Cursor cursorTempComprobCobros = dbHelper_Temp_Comprob_Cobros.fetchAllComprobCobros();
        cursorTempComprobCobros.moveToFirst();
        //Log.d("FORMA DE PAGO ", ""+i_formaPago);

        if(formaPago.equals(Constants._CONTADO)){

            i_formaPago = 1;
            if (_id_tipo_pago ==0){
                _id_tipo_pago = 9;
            }
        }else if(formaPago.equals(Constants._CREDITO)){

            i_formaPago = 2;
            session.deleteVariable(5);
            session.createTempSession(5,0);

            _id_tipo_pago = 0;
        }


     /*   String datosConcatenados = "idEestableciminto : " + idEstablecimiento + "\n" +
                "idAgente : " + id_agente_venta + "\n" +
                "Forma de pago : " + formaPago + "\n" +
                "Tipo Documento : " + tipoDocumento+ "\n" + "---------------------------";*/

        Cursor cursorAgente = dbHelperAgente.fetchAgentesByIds(id_agente_venta, idLiquidacion);
        cursorAgente.moveToFirst();
        String nombreAgenteVenta = cursorAgente.getString(cursorAgente.getColumnIndexOrThrow(dbHelperAgente.AG_nombre_agente));


            NUMERO_DOCUMENTO = numero_documento + "";
            SERIE_DOCUMENTO = serie;


            Log.d(TAG, "SERIE : " + serie);
            id = dbHelper_Comprob_Venta.createComprobVenta(idEstablecimiento, i_tipoDocumento, i_formaPago, _id_tipo_pago, tipoVenta, codigo_erp, serie, numero_documento, base_imponible, igv, monto_total, fecha, null, estado_comprobante, estado_conexion, id_agente_venta, Constants._CREADO, idLiquidacion, direccionCV);

        Log.d(TAG, "_ID COMPROBANTE DE VENTA : "+id);
            Log.d("Export id CV IGUALES", "" + id);

        Log.d(TAG, "ID COMPROBANTE VENTA : " + id);



            id_comprobante = (int) id;

        Log.d(TAG, "_ID COMPROBANTE DE VENTA : "+id_comprobante);


            Cursor cursorTemp = simpleCursorAdapter.getCursor();

            if (cursorTemp.getCount() <= 0) {
                Log.d("CT VC", cursorTemp.getCount() + "");
                Toast toast = Toast.makeText(mainActivity, "AGREGAR PRODUCTOS A LA VENTA", Toast.LENGTH_LONG);
                toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                toast.show();
                return;
            }


            long comprobVentaDetalle = 0;


            for (cursorTemp.moveToFirst(); !cursorTemp.isAfterLast(); cursorTemp.moveToNext()) {


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

                comprobVentaDetalle = dbHelper_Comprob_Venta_Detalle.createComprobVentaDetalle(id_comprobante, id_producto, nombre_producto, cantidad, importe, 0, precio_unitario, promedio_anterior, devuelto, valorUnidad);
                Log.d(TAG, "ID COMPROBANTE VENTA DETALLE: " + comprobVentaDetalle);
                int updateStockAgenteCantidad = dbHelper_Stock_Agente.updateStockAgenteCantidad(id_producto, -(cantidad * valorUnidad), idLiquidacion);
                Log.d(TAG, "UPDATE STOCK AGENTE CANTIDAD ROWS AFECTED: " + updateStockAgenteCantidad);

                //3pg, STAR MICRONICS
            /*if(nombre_producto.length()>=25){
                nombre_producto=nombre_producto.substring(0,22);
                nombre_producto+="...";
            }*/
                //sewoo

                if (anchoImpresora == 2) {
                    if (nombre_producto.length() >= 20) {
                        nombre_producto = nombre_producto.substring(0, 18);
                        nombre_producto += "..";
                    }
                } else if (anchoImpresora == 3) {
                    if (nombre_producto.length() >= 30) {
                        nombre_producto = nombre_producto.substring(0, 28);
                        nombre_producto += "..";
                    }
                }

//                DecimalFormat df = new DecimalFormat("0.00");
                //star micronics, 3pg
                //textoImpresion+=String.format("%-6s",cantidad) + String.format("%-34s",nombre_producto) +String.format("%-5s",df.format(importe)) + "\n";

                if (anchoImpresora == 2) {
                    textoImpresion += String.format("%-4s", cantidad) + String.format("%-21s", nombre_producto) + String.format("%1$7s", df.format(importe)) + "\n";
                } else if (anchoImpresora == 3) {
                    textoImpresion += String.format("%-4s", cantidad) + String.format("%-31s", nombre_producto) + String.format("%1$5s", df.format(precio_unitario)) + String.format("%1$8s", df.format(importe)) + "\n";
                }

                textoImpresionContenidoLeft += String.format("%-6s", cantidad) + String.format("%-28s", nombre_producto) + "\n";
                textoImpresionContenidoRight += String.format("%-5s", df.format(importe)) + "\n";

           /* datosConcatenados+="Producto  "+ nombre_producto + "Vendido satisfactoriamente con id : "+ comprobVentaDetalle;*/
            }

            base_imponible = monto_total / 1.18;
            igv = base_imponible * 0.18;
            int rowsUpdatemonto = dbHelper_Comprob_Venta.updateComprobanteMontos(id, monto_total, igv, base_imponible);

        Log.d(TAG, "UPDATE COMPROBANTE VENTA, LOS MONTOS TOTALES NRO DE COLMNAS AFECTADAS : "+rowsUpdatemonto);
/*        datosConcatenados+="total de gasto : " + monto_total;
        datosConcatenados+="base impornible: " + base_imponible;
        datosConcatenados+="igv : " + igv;*/


/*            DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.getDefault());
            simbolos.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("0.00", simbolos);*/

            //3PG, STAR MICRONICS
        /*textoImpresion += String.format("%-37s","SUB TOTAL:")+ "S/ "+ df.format(base_imponible)+"\n";
        textoImpresion += String.format("%-37s","IGV:")+  "S/ "+ df.format(igv)+"\n";
        textoImpresion += String.format("%-37s","TOTAL:")+  "S/ "+ df.format(monto_total)+"\n";
*/

/*        if (anchoImpresora==2){
            textoImpresion += "\n"+String.format("%-18s","OP. GRAVADA")+String.format("%-7s","S/.")+ String.format("%1$7s",df.format(base_imponible))+"\n";
            textoImpresion += String.format("%-18s","I.G.V.")+String.format("%-7s","S/.")+  String.format("%1$7s",df.format(igv))+"\n";
            textoImpresion += String.format("%-18s","PRECIO VENTA")+String.format("%-7s","S/.")+  String.format("%1$7s",df.format(monto_total))+"\n";
        }else if (anchoImpresora==3){
            textoImpresion += "\n"+String.format("%-18s","OP. GRAVADA")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format(base_imponible));
            textoImpresion += "\n"+String.format("%-18s","OP. INAFECTA")+String.format("%-21s","S/.")+ String.format("%1$9s","0.00");
            textoImpresion += "\n"+String.format("%-18s","OP. EXONERADA")+String.format("%-21s","S/.")+ String.format("%1$9s","0.00");
            textoImpresion += "\n"+String.format("%-18s","OP. GRATUITA")+String.format("%-21s","S/.")+ String.format("%1$9s","0.00");
            textoImpresion += String.format("%-18s","I.G.V.")+String.format("%-21s","S/.")+  String.format("%1$9s",df.format(igv));
            textoImpresion += String.format("%-18s","PRECIO VENTA")+String.format("%-21s","S/.")+  String.format("%1$9s",df.format(monto_total))+"\n\n";
            textoImpresion+= "------------------------------------------------------".substring(0,48)+"\n";

*//*            textoImpresion+= "Son "+NumberToLetterConverter.convertNumberToLetter(df.format(monto_total)).toLowerCase()+"\n";
            textoImpresion+= "------------------------------------------------------".substring(0,48)+"\n";*//*
            textoImpresion+= "VENDEDOR : "+ nombreAgenteVenta+"\n";

        }


        textoImpresionContenidoLeft+="SUB TOTAL:\n";
        textoImpresionContenidoLeft+="IGV:\n";
        textoImpresionContenidoLeft+="TOTAL:\n";

        textoImpresionContenidoRight+= "S/" +
                ""+ df.format(base_imponible)+"\n";
            textoImpresionContenidoRight+= "S/"+ df.format(igv)+"\n";
        textoImpresionContenidoRight+= "S/"+ df.format(monto_total)+"\n";*/


            dbHelper_Evento_Establecimiento.updateEstadoEstablecs("" + idEstablecimiento, 2, fecha);

            Log.d("FORMA DE PAGO", "" + i_formaPago);
            if (i_formaPago == 2) {
                for (cursorTempComprobCobros.moveToFirst(); !cursorTempComprobCobros.isAfterLast(); cursorTempComprobCobros.moveToNext()) {
                    String fecha_programada = cursorTempComprobCobros.getString(cursorTempComprobCobros.getColumnIndex(DbAdapter_Temp_Comprob_Cobro.temp_fecha_programada));
                    Double monto_a_pagar = cursorTempComprobCobros.getDouble(cursorTempComprobCobros.getColumnIndex(DbAdapter_Temp_Comprob_Cobro.temp_monto_a_pagar));
                    //String idComprobanteCobro = "M:" + dbHelper_Comprob_Cobros.getIdComrobanteCobro(idEstablecimiento + "");
                    String idComprobanteCobro = "M:" + idEstablecimiento+"-"+fecha+cursorTempComprobCobros.getLong(cursorTempComprobCobros.getColumnIndexOrThrow(DbAdapter_Temp_Comprob_Cobro.temp_id_cob_historial));
                    String NUMERO_COMPROBANTE = String.format("%08d", Integer.parseInt(NUMERO_DOCUMENTO));
                    Log.d(TAG, "RECORRE EL CURSOR TEMP COMPROB COBROS"+ "YES" + "--" + NUMERO_COMPROBANTE); //...
                    Log.d(TAG, "FECHA PROGRAMADA : "+ fecha_programada);
                    Log.d(TAG, "MONTO A PAGAR : "+ monto_a_pagar);
                    //Crear una consulta para añadir un id al comprobante cobro.
                    Log.d(TAG, "IDIDCOMPROBANTE,"+Integer.parseInt(id + "")+",");
                    long registroInsertado = dbHelper_Comprob_Cobros.createComprobCobros(idEstablecimiento, Integer.parseInt(id + ""), id_plan_pago, id_plan_pago_detalle, tipoDocumento.toUpperCase(), SERIE_DOCUMENTO + "-" + NUMERO_COMPROBANTE,fecha_programada, monto_a_pagar, fecha_cobro, hora_cobro, monto_cobrado, estado_cobro, id_agente_venta, id_forma_cobro, lugar_registro, idLiquidacion, idComprobanteCobro, 0);
                    Log.d(TAG, "COMPROBANTE COBRO _ID : , "+registroInsertado);
                    Log.d(TAG, "CC INSERTADO SATISFACTORIAMENTE "+ "ID : " + idComprobanteCobro + "....." + registroInsertado + "-" + Integer.parseInt(id + ""));
                }

                //DESCONTAR EL CRÉDITO DEL ESTABLECIMIENTO, CON EL MONTO TOTAL VENDIDO AL CRÉDITO
                Cursor cursorCredito = dbHelper_Evento_Establecimiento.fetchEstablecsById("" + idEstablecimiento);

                cursorEstablecimiento.moveToFirst();
                Double monto_credito = cursorEstablecimiento.getDouble(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_monto_credito));
                int dias_credito = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_dias_credito));

                Double descontado = monto_credito - monto_total;

                int rowsUpdated = dbHelper_Evento_Establecimiento.updateEstablecsCredito(idEstablecimiento, descontado, dias_credito);
                Log.d(TAG, "UPDATE CRÉDITO ESTABLECIMIENTO: ROWS 1 == " + rowsUpdated);
            }


            dbHelper_temp_venta.deleteAllTempVentaDetalle();
            dbHelper_Temp_Comprob_Cobros.deleteAllComprobCobros();


        buttonVender.setEnabled(false);
        buttonVender.setActivated(false);
        buttonVender.setClickable(false);

           if (conectadoWifi() || conectadoRedMovil()) {
               // exportMain.execute();
               Intent intent = new Intent(this, ServiceExport.class);
               intent.setAction(Constants.ACTION_EXPORT_SERVICE);
               startService(intent);

            }

            new GenerateDigitalSignature().execute();

        /*try {
             String stringPath = Signature.writeDocument(getDocumentFromFilePath("20138122256-01-F100-00000040.XML"), createFile("invoice_ubl.xml"));
                Toast.makeText(getApplicationContext(), stringPath, Toast.LENGTH_SHORT).show();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

/*        Intent intent= new Intent(this, VMovil_BluetoothImprimir.class);
        intent.putExtra("idComprobante",id_comprobante);
        finish();
        startActivity(intent);*/
    }

 /*   public enum TipoDocumento{
        Factura, Boleta, Ficha, Seleccionar
    }*/
    private void displayHistorialComprobanteAnterior() {

        //Log.d("DISPLAY ID ESTABLECIMIENTO ", ""+idEstablecimiento);;

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
        //int indice_costo_venta = cursor.getColumnIndex("");

        cursor.moveToFirst();

        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){

            int _id = cursor.getInt(indice_id);
            int id_producto = cursor.getInt(indice_id_producto);

            String codigoProductoHVA = "HVA - 1001";
            int stockActualDisponible = 0;
            Cursor cursorStockAgente = dbHelper_Stock_Agente.fetchByIdProducto(id_producto, idLiquidacion);
            cursorStockAgente.moveToFirst();
            if (cursorStockAgente.getCount()>0){
                codigoProductoHVA = cursorStockAgente.getString(cursorStockAgente.getColumnIndexOrThrow(DbAdapter_Stock_Agente.ST_codigo));
                stockActualDisponible = cursorStockAgente.getInt(cursorStockAgente.getColumnIndexOrThrow(DbAdapter_Stock_Agente.ST_disponible));
            }


            String nombre_producto = cursor.getString(indice_nombre_producto);
            int cantidad = cursor.getInt(indice_cantidad);
            Log.d("CANTIDAD HCA - VC", ""+cantidad);
            Double precio_unitario = cursor.getDouble(indice_precio_unitario);
            Double importe = cursor.getDouble(indice_importe);

            if (ventaRRPP ==1){
                precio_unitario =0.0;
                importe = 0.0;
            }

            String promedio_anterior = cursor.getString(indice_promedio_anterior);
            String devuelto = cursor.getString(indice_devuelto);
            int valor_unidad = cursor.getInt(indice_valor_unidad);

            int procedencia = 0;

            long id = 1;
            Log.d("STOCK DISPONIBLE,HCA",""+stockActualDisponible+"=="+cantidad+"*"+valor_unidad);
            //En una tabla "Temp_Venta" Nos sirve para agregar datos del historial de ventas anteriores y sugerir al usuario, estos son datos temporales
            if (stockActualDisponible>=cantidad*valor_unidad){
                id = dbHelper_temp_venta.createTempVentaDetalle(1,id_producto,nombre_producto,cantidad,round(importe,2), round(precio_unitario,2), promedio_anterior, devuelto, procedencia, valor_unidad, ""+codigoProductoHVA);
            }else if (stockActualDisponible>0)
            {
                id = dbHelper_temp_venta.createTempVentaDetalle(1,id_producto,nombre_producto,stockActualDisponible,round(precio_unitario*valor_unidad*stockActualDisponible,2), round(precio_unitario,2), promedio_anterior, devuelto, procedencia, valor_unidad, ""+codigoProductoHVA);
            }

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


        TextView textViewNombreEstablecimiento = (TextView) findViewById(R.id.completeName);
        RoundedLetterView letter = (RoundedLetterView) findViewById(R.id.letter);



        Cursor cursorEstablecimiento = dbHelper_Evento_Establecimiento.fetchEstablecsById(""+idEstablecimiento);
        cursorEstablecimiento.moveToFirst();
        nombreEstablecimiento = "";
        if (cursorEstablecimiento.getCount()>0) {
            nombreEstablecimiento = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndexOrThrow(dbHelper_Evento_Establecimiento.EE_nom_establec));
        }
        textViewNombreEstablecimiento.setText(nombreEstablecimiento);
        if(nombreEstablecimiento.length() == 0){
            letter.setTitleText("A");
        }else{
            letter.setTitleText(nombreEstablecimiento.substring(0, 1).toUpperCase());
        }

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


        if (listView.getHeaderViewsCount()<1){
            header = getLayoutInflater().inflate(R.layout.infor_venta_cabecera,null);
            /*headerNombreEstablecimiento = getLayoutInflater().inflate(R.layout.header_establecimiento,null);
            TextView textViewNombreEstablecimiento = (TextView) headerNombreEstablecimiento.findViewById(R.id.completeName);
            RoundedLetterView letter = (RoundedLetterView) headerNombreEstablecimiento.findViewById(R.id.letter);



            Cursor cursorEstablecimiento = dbHelper_Evento_Establecimiento.fetchEstablecsById(""+idEstablecimiento);
            cursorEstablecimiento.moveToFirst();
            String nombreEstablecimiento = "";
            if (cursorEstablecimiento.getCount()>0) {
                nombreEstablecimiento = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndexOrThrow(dbHelper_Evento_Establecimiento.EE_nom_establec));
            }
            textViewNombreEstablecimiento.setText(nombreEstablecimiento);
            if(nombreEstablecimiento.length() == 0){
                letter.setTitleText("A");
            }else{
                letter.setTitleText(nombreEstablecimiento.substring(0, 1).toUpperCase());
            }


            listView.addHeaderView(headerNombreEstablecimiento, null, false);*/
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


//        DecimalFormat df = new DecimalFormat("0.00");
        textViewFooterTotal.setText(" S/. "+df.format(totalFooter)+"\n" +
                "S/. "+df.format(base_imponibleFooter)+ "\n" +
                "S/. "+df.format(igvFooter));


        //DATOS DE PRUEBA LOS OBTENDRE CUANDO SULLY ME LOS PASE.
        int surtidoStockAnterior = 10;
        int surtidoVentaAnterior = 1;

        int porcentajeSurtidoAnterior = surtidoVentaAnterior*100/surtidoStockAnterior;

        if (surtidoStock==0){
            surtidoStock++;
        }
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

        //DecimalFormat df = new DecimalFormat("0.00");
        final EditText savedText = ((EditText) layout.findViewById(R.id.VCAP_editTextCantidad));
        final TextView nombreProducto = ((TextView) layout.findViewById(R.id.VCPA_textView2NombreProducto));
        final TextView precio = ((TextView) layout.findViewById(R.id.VCPA_textViewPrecio));
        //final TextView disponible = ((TextView) layout.findViewById(R.id.VCPA_textViewDisponible));


        Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto,id_categoria_establecimiento);

        if (mCursorPrecioUnitarioGeneral.getCount()>1){
            Toast.makeText(getApplicationContext(), "Hay "+mCursorPrecioUnitarioGeneral.getCount() + " valores unidades para este producto", Toast.LENGTH_SHORT).show();
        }


        Cursor mCursorStock = dbHelper_Stock_Agente.fetchByIdProducto(id_producto,idLiquidacion);
        int maximoValor = 1;
        if (mCursorStock.getCount()>0){
            maximoValor = mCursorStock.getInt(mCursorStock.getColumnIndexOrThrow(dbHelper_Stock_Agente.ST_disponible));
        }

        ///disponible.setText(""+maximoValor);
        savedText.setFilters(new InputFilter[]{new InputFilterMinMax(1,maximoValor)});


        if (mCursorPrecioUnitarioGeneral.getCount()>0) {
            double precio_unitario = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));

            if (ventaRRPP==1){
                precio_unitario =0.0;
            }
            precio.setText("Precio : S/. "+ df.format(precio_unitario));

        }else{
            precio.setText("Precio : S/. No encontrado");
        }
        nombreProducto.setText(nombre);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stock "+"("+maximoValor+")");
        final int finalMaximoValor = maximoValor;
        builder.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                String texto = savedText.getText().toString().trim();
                if(texto.equals("")){
                    texto = "1";
                }

                cantidad = Integer.parseInt(texto);

               //(getApplicationContext(),"Cantidad : "+cantidad + " id_producto : "+ id_producto,Toast.LENGTH_LONG).show();

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
                        Toast toast = Toast.makeText(getApplicationContext(), "No se encontró un precio para esta cantidad de productos, agregar el precio a la base de datos", Toast.LENGTH_LONG);
                        toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                        toast.show();
                    }
                }

                if (ventaRRPP==1){
                    precio_unitario =0.0;
                }

                double total_importe = precio_unitario*cantidad;
                String promedio_anterior = null;
                String devuelto = null;

//                DecimalFormat df = new DecimalFormat("0.00");
                //En una tabla "Temp_Venta" Nos sirve para agregar datos del historial de ventas anteriores y sugerir al usuario, estos son datos temporales

                long id = dbHelper_temp_venta.createTempVentaDetalle(1,id_producto,nombre,cantidad,round(total_importe,2), round(precio_unitario,2), promedio_anterior, devuelto, procedencia, valor_unidad,""+codigo_producto);
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

//        DecimalFormat df = new DecimalFormat("0.00");
        final EditText savedText = ((EditText) layout.findViewById(R.id.VCAP_editTextCantidad));
        final TextView nombreProducto = ((TextView) layout.findViewById(R.id.VCPA_textView2NombreProducto));
        final TextView precio = ((TextView) layout.findViewById(R.id.VCPA_textViewPrecio));
        final TextView valorUnidad = (TextView) layout.findViewById(R.id.VCPA_textViewValorUnidad);
        //final TextView disponible = ((TextView) layout.findViewById(R.id.VCPA_textViewDisponible));

        final double[] precio_unitario = {0.0};

        final Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto,id_categoria_establecimiento);
        mCursorPrecioUnitarioGeneral.moveToFirst();

        if (mCursorPrecioUnitarioGeneral.getCount()>0) {
            precio_unitario[0] = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
            valor_unidad = mCursorPrecioUnitarioGeneral.getInt(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_valor_unidad));
            if (ventaRRPP==1){
                precio_unitario[0] = 0.0;
            }
            precio.setText("Precio : S/. "+ df.format(precio_unitario[0]));
            valorUnidad.setText("Unidad : "+valor_unidad);

        }else{
            precio.setText("Precio : S/. No encontrado");
        }






        valorUnidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position = mCursorPrecioUnitarioGeneral.getPosition();
                //Toast.makeText(getApplicationContext(), "Position del cursor Valor Unidad "+position, Toast.LENGTH_SHORT).show();
                if (mCursorPrecioUnitarioGeneral.isLast()){
                    mCursorPrecioUnitarioGeneral.moveToFirst();
                }else{
                    mCursorPrecioUnitarioGeneral.moveToNext();
                }
                precio_unitario[0] = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
                valor_unidad = mCursorPrecioUnitarioGeneral.getInt(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_valor_unidad));
                if (ventaRRPP==1){
                    precio_unitario[0] = 0.0;
                }

//                DecimalFormat df = new DecimalFormat("0.00");
                precio.setText("Precio : S/. "+ df.format(precio_unitario[0]));
                valorUnidad.setText("Unidad : "+valor_unidad);


            }
        });


        Cursor mCursorStock = dbHelper_Stock_Agente.fetchByIdProducto(id_producto,idLiquidacion);
        int maximoValor = 1;
        if (mCursorStock.getCount()>0){
            maximoValor = mCursorStock.getInt(mCursorStock.getColumnIndexOrThrow(dbHelper_Stock_Agente.ST_disponible));
        }
        //disponible.setText(""+maximoValor);
        savedText.setFilters(new InputFilter[]{new InputFilterMinMax(1,maximoValor)});



        nombreProducto.setText(nombre);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stock "+"("+maximoValor+")");
        final int finalMaximoValor = maximoValor;
        builder.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
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
                if (ventaRRPP==1){
                    precio_unitario[0] = 0.0;
                }

                double total_importe = precio_unitario[0]*cantidad;
                String promedio_anterior = null;
                String devuelto = null;

                //En una tabla "Temp_Venta" Nos sirve para agregar datos del historial de ventas anteriores y sugerir al usuario, estos son datos temporales
                long id = dbHelper_temp_venta.createTempVentaDetalle(1,id_producto,nombre,cantidad,round(total_importe,2), round(precio_unitario[0],2), promedio_anterior, devuelto, procedencia, valor_unidad,""+codigo_producto);
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
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(date);
    }

    double formatDecimal(double d)
    {
//        DecimalFormat df = new DecimalFormat("0,00");
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

        textViewSlideCargar = (TextView)findViewById(R.id.slide_textViewCargarInventario);
        textConsultarInventario = (TextView)findViewById(R.id.slide_textViewConsultarInventario);
        textViewSlideCargar.setOnClickListener(this);
        textConsultarInventario.setOnClickListener(this);



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
        //DecimalFormat df = new DecimalFormat("0.00");
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
    private static String agregarCeros(String string, int largo)
    {
        String ceros = "";
        int cantidad = largo - string.length();
        if (cantidad >= 1)
        {
            for(int i=0;i<cantidad;i++)
            {
                ceros += "0";
            }
            return (ceros + string);
        }
        else
            return string;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    class GenerateDigitalSignature extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            //POR DEFECTO FACTURA
            String tipo_doccumento = "01";

            if (i_tipoDocumento==1)
                tipo_doccumento = "01";
            else if(i_tipoDocumento == 2)
                tipo_doccumento = "03";
            else
                tipo_doccumento = "03";

            map.put("tipo_documento", tipo_doccumento);
            map.put("id_documento", numeroDocumentoImpresion);
            map.put("user_ruc_dni", documentoCliente);
            map.put("user_name", nombreCliente);
            map.put("total_operaciones_gravadas", df.format(base_imponibleFooter));
            map.put("total_importe_venta", df.format(totalFooter));
            map.put("total__igv", df.format(igvFooter));

            Iterator it = map.keySet().iterator();
            while(it.hasNext()){
                String key = (String) it.next();
                Log.d(TAG, "GENERATE DS Clave: " + key + " -> Valor: " + map.get(key));
            }

            File filesinFirmar = null;
            try {
                keystore = getFilefromAssets(BKS);
                filesinFirmar = SimpleXMLAndroid.generarXMLown(File.createTempFile(contexto.getString(R.string.RUC) + "-" + tipo_doccumento + "-" + numeroDocumentoImpresion, "XML"), map, simpleCursorAdapter.getCursor());
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    textoSHA1 = Signature.add(keystore, filesinFirmar, createFile(contexto.getString(R.string.RUC)+"-"+tipo_doccumento+"-"+numeroDocumentoImpresion+".XML"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

          return textoSHA1;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "ON POST EXECUTE");
            int updatedSHA1NRo = dbHelper_Comprob_Venta.updateSHA1(id_comprobante, textoSHA1);
            Log.d(ServiceExport.TAG, "SHA1: "+textoSHA1 +", id_comprobante : "+ id_comprobante + ", registros actualizados : "+updatedSHA1NRo);
            Log.d(TAG, "_ID COMPROBANTE DE VENTA : "+id_comprobante);
            Intent intent= new Intent(contexto, VMovil_BluetoothImprimir.class);
            intent.putExtra("idComprobante",id_comprobante);
            finish();
            startActivity(intent);
        }
    }

    InputStream getFilefromAssets(String nameDocument)
            throws IOException
    {
        AssetManager am = contexto.getAssets();
        return am.open(nameDocument);
    }

    Document getDocumentFromFilePath(String filePath)
            throws IOException, ParserConfigurationException, SAXException {
        AssetManager am = contexto.getAssets();
        InputStream is = am.open(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(is);
    }

    File createFile(String nameDocument)
            throws IOException, ParserConfigurationException, SAXException {
        //String dir = Environment.DIRECTORY_DOWNLOADS+"/FACTURACION/";
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), nameDocument);
        /*
        File file = new File(dir, nameDocument);
        //return File.createTempFile(pathFile,"xml",contexto.getCacheDir());
        file.mkdir();
        file.createNewFile();*/
        return file;
    }




}

