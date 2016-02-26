package union.union_vr1;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sewoo.port.android.BluetoothPort;

import union.union_vr1.Conexion.JSONParser;
import union.union_vr1.JSONParser.ParserAgente;
import union.union_vr1.Objects.Agente;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DBAdapter_Temp_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Agente_Login;
import union.union_vr1.Sqlite.DbAdapter_Cobros_Manuales;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Exportacion_Comprobantes;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Impresion_Cobros;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Motivo_Dev;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.AppPreferences;
import union.union_vr1.Vistas.Bluetooth_Printer;
import union.union_vr1.Vistas.ImprimirStockDisponible;
import union.union_vr1.Vistas.VMovil_Abrir_Caja;
import union.union_vr1.Vistas.VMovil_Evento_Indice;
import union.union_vr1.Vistas.VMovil_Venta_Cabecera;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Login extends Activity implements OnClickListener {


    /**
     * AQUÍ DEFINO LAS VARIABLES PARA EL SYNCADAPTER
     */

/*    public static final String ARG_ACCOUNT_TYPE = "1";
    public static final String ARG_AUTH_TYPE = "2";
    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "3";
    public static final String PARAM_USER_PASS = "4";
    private AccountManager mAccountManager;z

    public static final String CODE_NAME = "name";


    private AccountManagerCallback<Bundle> mGetAuthTokenCallback =
            new AccountManagerCallback<Bundle>() {
                @Override
                public void run(final AccountManagerFuture<Bundle> arg0) {
                    try {
                        token = (String) arg0.getResult().get(AccountManager.KEY_AUTHTOKEN);
                    } catch (Exception e) {
                        // handle error
                    }
                }
            };
    private Account mAccount;
    public static String token = "";

    private boolean addNewAccount = false;*/


    // private DbAdapter_Temp_Session session;
    private Login loginClass;
    private boolean succesMACDevice;
    private boolean succesLogin;
    ProgressDialog prgDialog;
    private EditText user, pass;
    private Button mSubmit, mSalirs;
    private EditText Txt;
    // Progress Dialog
    private ProgressDialog pDialog;
    private String pru;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private String var1 = "";
    private DbAdapter_Agente_Login dbAdapter_agente_login;

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public String getVar1() {
        return this.var1;
    }

    //public void modificarValorVar1(){
    //    this.var1 = "pruebas XD";
    //}

    //php login script location:

    //localhost :  
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/login.php";

    //testing on Emulator:
    //  private static final String LOGIN_URL = "http://192.168.0.158:8083/produnion/login.php";
    //private static final String LOGIN_URL = "http://192.168.0.158:8081/webservice/login.php";
    //testing from a real server:78
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_NOMBRE = "name";



    private int idAgente;
    private int idLiquidacion;
    private String nombreUsuario = "";

    //REDIRECCIONAR PRINCIPAL

    private DbAdapter_Agente dbAdapter_agente;
    private DbAdapter_Temp_Session session;
    private int isCajaOpened;
    private boolean isCajaActual;
    private Activity mainActivity;
    private ImageView logo;

    private static String TAG = Login.class.getSimpleName();

    private int countClicked = 0;


    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 2;
    private boolean fechaCorrecta = false;
    String fechaServer = "";
    String fechaDevice = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mainActivity = this;



/*
        ContentResolver resolver = getContentResolver();

        mAccountManager = (AccountManager) getSystemService(
                ACCOUNT_SERVICE);

        // Se chequea si existe una cuenta asociada a ACCOUNT_TYPE.
        Account[] accounts = mAccountManager.getAccountsByType(AccountAuthenticator.ACCOUNT_TYPE);
        if (accounts.length == 0){
            // También se puede llamar a metodo mAccountManager.addAcount(...)
            //TENGO QUE AGREGAR UNA NUEVA CUENTA
            //PONDRÉ UN VALOR BOOLEANO  ADDNEWACOUNT
            */
/*
            Intent intent = new Intent(this, AuthenticatorActivity.class);
            intent.putExtra(AuthenticatorActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
            startActivity(intent);*//*

            addNewAccount = true;

        }
        else {
            mAccount = accounts[0];
            mAccountManager.getAuthToken(mAccount, AccountAuthenticator.ACCOUNT_TYPE, null, this,
                    mGetAuthTokenCallback, null);
            */
/*resolver.setIsSyncable(mAccount, StudentsContract.AUTHORITY, 1);
            resolver.setSyncAutomatically(mAccount, StudentsContract.AUTHORITY, true);*//*

        }
        Log.d(TAG, "ACCOUNTS "+ addNewAccount);
*/
       // new FechaRest().execute();


        session = new DbAdapter_Temp_Session(this);
        session.open();

        isCajaOpened = session.fetchVarible(9);
        Log.d("IS CAJA OPENED", "" + isCajaOpened);


        if (isCajaOpened == 0) {
            // QUEDARSE AQUÍ, LA CAJA ESTÁ CERRADA.
        } else if (isCajaOpened == 1) {
            //LA CAJA ESTÁ ABIERTA
            redireccionarPrincipal();
        } else {
            redireccionarPrincipal();
        }

        loginClass = this;

        dbAdapter_agente_login = new DbAdapter_Agente_Login(this);
        dbAdapter_agente_login.open();


        //setup input fields
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);

        logo = (ImageView) findViewById(R.id.imageViewLogo);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        nombreUsuario = SP.getString("username", "");

        user.setText("" + nombreUsuario);

        //setup buttons
        mSubmit = (Button) findViewById(R.id.login);
        //mSalirs = (Button)findViewById(R.id.salir);

        //register listeners
        mSubmit.setOnClickListener(this);
        logo.setOnClickListener(this);


        bluetoothSetup();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
    }

    private boolean validarFecha(){
        Log.d(TAG, "FECHA DEVICE CIPHERLAB : "+fechaDevice);
        Log.d(TAG, "FECHA SERVER :" + fechaServer);
        if (fechaServer.length()>0&&fechaDevice.length()>0) {
            if (fechaDevice.equals(fechaServer)){
                fechaCorrecta = true;

            }else{
                //OPCIÓN 1, MANDAR UN MENSAJE FECHA INVÁLIDA, Y ENVIAR A LAS CONFIFURACIONES DE FECHA..
                Utils.dialogCambiarFecha(mainActivity).show();
                //OPCIÓN 2, ESTABLECER LA FECHA PROGRAMANDO, SIN NINGÚN MENSAJE.
            }
        }
        return fechaCorrecta;
    }

     class FechaRest extends AsyncTask<String, String, String>{

        private  String TAG = FechaRest.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {
            // CONSUMIR EL PROCEDIMIENTO QUE ME CONSUMA LA FECHA

            fechaDevice = Utils.getDatePhone();
            StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);
            try {
                JSONObject jsonFecha = api.fsel_FechaActual();
                Log.d(TAG,"jsonFecha : "+ jsonFecha.toString());

                fechaServer = Utils.jsonGetString(jsonFecha);

            } catch (Exception e) {
                e.printStackTrace();
            }
            // SI LA FECHA ES INVALIDA
            Log.d(TAG, "FECHA DEVICE CIPHERLAB : "+fechaDevice);
            Log.d(TAG, "FECHA SERVER :" + fechaServer);

            return null;
        }

         @Override
         protected void onPostExecute(String s) {
             Log.d(TAG, "FECHA DEVICE CIPHERLAB : "+fechaDevice);
             Log.d(TAG, "FECHA SERVER :" + fechaServer);
             if (fechaServer.length()>0&&fechaDevice.length()>0) {
                 if (fechaDevice.equals(fechaServer)){
                     fechaCorrecta = true;

                 }else{
                     //OPCIÓN 1, MANDAR UN MENSAJE FECHA INVÁLIDA, Y ENVIAR A LAS CONFIFURACIONES DE FECHA..
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {

                             Utils.dialogCambiarFecha(mainActivity).show();
                         }
                     });
                     //OPCIÓN 2, ESTABLECER LA FECHA PROGRAMANDO, SIN NINGÚN MENSAJE.
                 }
             }
             super.onPostExecute(s);
         }
     }


    /**
     * Set up Bluetooth.
     */
    private void bluetoothSetup() {
        // Initialize
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        assert mBluetoothAdapter != null;
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainActivity == null) {
            mainActivity = this;
        }

        fechaDevice = Utils.getDatePhone();
        Log.d(TAG, "FECHA DEVICE : " + fechaDevice);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        nombreUsuario = SP.getString("username", "");


        user.setText("" + nombreUsuario);

    }

    public void redireccionarPrincipal() {
        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();


        idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);


        isCajaActual = false;

        Cursor cursorAgenteCajaActual = dbAdapter_agente.fetchAgentesByIds(idAgente, idLiquidacion);
        cursorAgenteCajaActual.moveToFirst();
        String fechaCaja = null;
        if (cursorAgenteCajaActual.getCount() > 0) {
            isCajaActual = true;
            fechaCaja = cursorAgenteCajaActual.getString(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente.AG_fecha));

            if (getDatePhone().equals(fechaCaja)) {
                isCajaActual = true;
            } else {
                //LA CAAJA ESTÁ ABIERTA PERO NO CON LA FECHA ACTUAL
                Toast.makeText(getApplicationContext(), "Debe Abrir Caja", Toast.LENGTH_LONG).show();
                isCajaActual = false;
            }
        }


        if (cursorAgenteCajaActual.getCount() == 0) {
            isCajaActual = false;
        }

        if (isCajaActual) {
                /*
                ((MyApplication) loginClass.getApplication()).setIdAgente(mCursorAgente.getInt(mCursorAgente.getColumnIndexOrThrow(dbAdapter_agente.AG_id_agente_venta)));
                ((MyApplication) loginClass.getApplication()).setIdLiquidacion(mCursorAgente.getInt(mCursorAgente.getColumnIndexOrThrow(dbAdapter_agente.AG_liquidacion)));
                ((MyApplication) loginClass.getApplication()).setIdUsuario(mCursorAgente.getInt(mCursorAgente.getColumnIndexOrThrow(dbAdapter_agente.AG_id_usuario)));
                ((MyApplication) loginClass.getApplication()).setDisplayedHistorialComprobanteAnterior(false);

*/

            session.deleteVariable(1);
            session.deleteVariable(3);
            session.deleteVariable(4);
            session.deleteVariable(6);

            session.createTempSession(1, cursorAgenteCajaActual.getInt(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente.AG_id_agente_venta)));
            session.createTempSession(3, cursorAgenteCajaActual.getInt(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente.AG_liquidacion)));
            session.createTempSession(4, cursorAgenteCajaActual.getInt(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente.AG_id_usuario)));
            session.createTempSession(6, 0);

            Intent i = new Intent(mainActivity, VMovil_Evento_Indice.class);
            finish();
            startActivity(i);
        } else {
            //LA CAJA ESTÁ ABIERTA PERO NO CON LA FECHA ACTUAL, ENTONCES TIENE QUE ABRIR CAJA
            //PERO PRIMER TIENE QUE INICIAR SESIÓN, ASÍ QUE LO DEJAMOS AQÚI.

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nuevo_dia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.buttonAjustes:
                Intent intentA = new Intent(mainActivity, AppPreferences.class);
                startActivity(intentA);
                break;
            case R.id.buttonResumenTransferencias:
                Intent intentB = new Intent(mainActivity, Bluetooth_Printer.class);
                startActivity(intentB);
                break;
/*            case R.id.imprimirDisponible:
                Intent intentImprimirStock = new Intent(mainActivity, ImprimirStockDisponible.class);
                startActivity(intentImprimirStock);
                break;*/
/*
            case R.id.buttonCola:
                idLiquidacion = session.fetchVarible(3);

                int registros = validarExport(idLiquidacion);
                Log.d(TAG, " REGISTROS EN COLA NRO : "+ registros);

                break;
            case R.id.buttonStockLiquidacion:
                DbAdapter_Stock_Agente dbAdapter_stock_agente = new DbAdapter_Stock_Agente(mainActivity);
                dbAdapter_stock_agente.open();
                Cursor cursorStock = dbAdapter_stock_agente.fetchAllStockAgente();


                for (cursorStock.moveToFirst(); !cursorStock.isAfterLast();cursorStock.moveToNext()){

                    int liquidacion = cursorStock.getInt(cursorStock.getColumnIndex(dbAdapter_stock_agente.ST_liquidacion));
                    int codigo_producto = cursorStock.getInt(cursorStock.getColumnIndex(dbAdapter_stock_agente.ST_id_producto));

                    Log.d(TAG, "LIQUIACIÓN : "+ liquidacion + ", ID PRODUCTO : "+codigo_producto);
                }

                break;*/
            default:
                //ON ITEM SELECTED DEFAULT
                break;
        }
        return super.onOptionsItemSelected(item);
    }

/*
    private int validarExport(int idLiquidacion) {


        //DEFINO LAS VARIABLES A MIS MANEJADORES DE LAS TABLAS
        DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
        DbAdapter_Comprob_Venta dbAdapter_comprob_venta;
        DbAdapter_Comprob_Venta_Detalle dbAdapter_comprob_venta_detalle;
        DbAdapter_Histo_Venta dbAdapter_histo_venta;
        DbAdapter_Comprob_Cobro dbAdapter_comprob_cobro;
        DbAdapter_Histo_Venta_Detalle dbAdapter_histo_venta_detalle;
        DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
        DBAdapter_Temp_Autorizacion_Cobro dbAdapter_temp_autorizacion_cobro;
        DBAdapter_Temp_Canjes_Devoluciones dbAdapter_temp_canjes_devoluciones;
        DbAdapter_Cobros_Manuales dbAdapter_cobros_manuales;
        DbAdapter_Exportacion_Comprobantes dbAdapter_exportacion_comprobantes;
        DbAdapter_Impresion_Cobros dbAdapter_impresion_cobros;

        //INSTANCIO LAS CLASES DE MIS MANEJADORES DE DB

        dbAdapter_temp_canjes_devoluciones = new DBAdapter_Temp_Canjes_Devoluciones(mainActivity);
        dbAdapter_temp_canjes_devoluciones.open();
        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();

        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(mainActivity);
        dbAdapter_comprob_venta = new DbAdapter_Comprob_Venta(mainActivity);
        //dbAdapter_comprob_venta_detalle = new DbAdapter_Comprob_Venta_Detalle(mainActivity);
        dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(mainActivity);
        dbAdapter_histo_venta_detalle = new DbAdapter_Histo_Venta_Detalle(mainActivity);
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdapter_histo_venta = new DbAdapter_Histo_Venta(mainActivity);
        dbAdapter_temp_autorizacion_cobro = new DBAdapter_Temp_Autorizacion_Cobro(mainActivity);
        dbAdapter_cobros_manuales = new DbAdapter_Cobros_Manuales(mainActivity);
        dbAdapter_exportacion_comprobantes = new DbAdapter_Exportacion_Comprobantes(mainActivity);
        dbAdapter_impresion_cobros = new DbAdapter_Impresion_Cobros(mainActivity);

        //ABRO LA CONEXIÓN A LA DB
        dbAdapter_informe_gastos.open();
        dbAdapter_comprob_venta.open();
        //dbAdapter_comprob_venta_detalle.open();
        dbAdapter_histo_venta_detalle.open();
        dbAdapter_comprob_cobro.open();
        dbAdapter_histo_venta_detalle.open();
        dbAdaptert_evento_establec.open();
        dbAdapter_histo_venta.open();
        dbAdapter_temp_autorizacion_cobro.open();
        dbAdapter_cobros_manuales.open();
        dbAdapter_exportacion_comprobantes.open();
        dbAdapter_impresion_cobros.open();


        //FILTRO LOS REGISTROS DE LAS TABLAS A EXPORTAR

        int colaInformeGastos = dbAdapter_informe_gastos.filterExport().getCount();
        int colaComprobanteVenta = dbAdapter_comprob_venta.filterExport().getCount();
        //int colaComprobanteVentaDetalle = dbAdapter_comprob_venta_detalle.filterExport().getCount();
        int colaComprobanteCobro = dbAdapter_comprob_cobro.filterExport().getCount();
        //
        // int colaInsertarCaja = dbAdapter_comprob_cobro.filterExportUpdatedAndEstadoCobro().getCount();
        int colaInsertarCaja = dbAdapter_impresion_cobros.listarParaExportar().getCount();
        int colaEventoEstablecimiento = dbAdaptert_evento_establec.filterExportUpdated(idLiquidacion).getCount();
        Cursor cursorEventoEstablecimiento = dbAdaptert_evento_establec.filterExportUpdated(idLiquidacion);
        cursorEventoEstablecimiento.moveToFirst();
        String time = "TIME_HERE";
        time = cursorEventoEstablecimiento.getString(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_time_atencion));

        Log.d(TAG, "TIME ATENCIÓN : "+ time);

        //EXPORTAR TODOS LOS REGISTROS ACTUALIZADOS EN ANDROID

        if (colaEventoEstablecimiento > 0) {

            for (cursorEventoEstablecimiento.moveToFirst(); !cursorEventoEstablecimiento.isAfterLast(); cursorEventoEstablecimiento.moveToNext()) {
                JSONObject jsonObject = null;

                Log.d(TAG, "EE EXPORT DATOS" +
                                cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_establec)) + "-" +
                                idLiquidacion + "-" +
                                cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_estado_atencion)) + "-" +
                                cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_estado_no_atencion)) + "1-" +
                                cursorEventoEstablecimiento.getString(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_estado_no_atencion_comentario)) + "-" +
                                cursorEventoEstablecimiento.getString(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_time_atencion))
                );
                try {
                    StockAgenteRestApi api = null;
                    api = new StockAgenteRestApi(mainActivity);
                    jsonObject = api.UpdateEstadoEstablecimiento2(
                            cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_establec)),
                            idLiquidacion,
                            cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_estado_atencion)),
                            cursorEventoEstablecimiento.getInt(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_id_estado_no_atencion)),
                            cursorEventoEstablecimiento.getString(cursorEventoEstablecimiento.getColumnIndexOrThrow(dbAdaptert_evento_establec.EE_time_atencion))
                    );

                    Log.d(TAG, "E M ESTABLECIMIENTO "+ jsonObject.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        } else {
            Log.d(TAG, "EXPORT, TODOS LOS ESTABLECIMIENTOS ESTÁN EXPORTADOS [ACTUALIZADOS]");
        }

        //histo venta
        int colaHistoVentaCreated = dbAdapter_histo_venta.filterExport().getCount();
        int colaHistoVentaDetalleCreated = dbAdapter_histo_venta_detalle.filterExport(idLiquidacion).getCount();
        int colaAutorizacionCobro = dbAdapter_temp_autorizacion_cobro.filterExport().getCount();
        int colaCobrosManuales = dbAdapter_cobros_manuales.filterExport().getCount();
        int colaExportacionFlex = dbAdapter_exportacion_comprobantes.filterExport(idLiquidacion).getCount();
        int colaExportCManuFlex = dbAdapter_cobros_manuales.filterExportForFlex().getCount();
        int colaExportCNormFlex = dbAdapter_impresion_cobros.listarCobrosExpFlex().getCount();
        int colaCliente = dbAdaptert_evento_establec.listarToExport(idLiquidacion).getCount();
        //KELVIN LO REVISARÁ Y ME DIRÁ
        int colaCAnjesDevoluciones = dbAdapter_temp_canjes_devoluciones.getAllOperacionEstablecimiento().getCount();


        int totalRegistrosEnCola = colaInformeGastos + colaComprobanteVenta +
                colaComprobanteCobro + colaInsertarCaja + colaEventoEstablecimiento + colaHistoVentaCreated + colaHistoVentaDetalleCreated + colaAutorizacionCobro
                + colaCobrosManuales + colaExportacionFlex + colaExportCManuFlex + colaExportCNormFlex + colaCliente + colaCAnjesDevoluciones;

        Log.d(TAG, "COLA  TOTAL: " + totalRegistrosEnCola);
        Log.d(TAG, "COLA colaInformeGastos : " + colaInformeGastos);
        Log.d(TAG, "COLA colaComprobanteVenta : " + colaComprobanteVenta);
        //Log.d(TAG, "COLA colaComprobanteVentaDetalle : " + colaComprobanteVentaDetalle);
        Log.d(TAG, "COLA colaComprobanteCobro : " + colaComprobanteCobro);
        Log.d(TAG, "COLA colaInsertarCaja : " + colaInsertarCaja);
        Log.d(TAG, "COLA colaEventoEstablecimiento : " + colaEventoEstablecimiento);
        Log.d(TAG, "COLA colaHistoVentaCreated : " + colaHistoVentaCreated);
        Log.d(TAG, "COLA colaHistoVentaDetalleCreated : " + colaHistoVentaDetalleCreated);
        Log.d(TAG, "COLA colaAutorizacionCobro : " + colaAutorizacionCobro);
        Log.d(TAG, "COLA colaCobrosManuales : " + colaCobrosManuales);
        Log.d(TAG, "COLA colaExportacionFlex : " + colaExportacionFlex);
        Log.d(TAG, "COLA colaCAnjesDevoluciones : " + colaCAnjesDevoluciones);
        Log.d(TAG, "COLA colaExportCNormFlex : " + colaExportCNormFlex);
        Log.d(TAG, "COLA colaExportCManuFlex : " + colaExportCManuFlex);
        Log.d(TAG, "COLA colaCliente : " + colaCliente);


        return totalRegistrosEnCola;
    }*/
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }


    protected Boolean estaConectado() {
        if (conectadoWifi()) {
            //user.setText("Conexion a Wifi");
            return true;
        } else {
            if (conectadoRedMovil()) {
                //  user.setText("Conexion a Movil");
                return true;
            } else {
                //user.setText("No Tiene Conexion a Internet");
                return false;
            }
        }
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

    public void cajaAbierta() {
        succesLogin = false;

        Cursor cursorAgenteCajaActual = dbAdapter_agente_login.fetchAllAgentesVentaLogin(getDatePhone());
        cursorAgenteCajaActual.moveToFirst();
        String fechaCaja = null;
        if (cursorAgenteCajaActual.getCount() > 0) {
            succesLogin = true;
            fechaCaja = cursorAgenteCajaActual.getString(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente_login.AG_fecha));
            Log.d("FECHA", "" + fechaCaja + "-" + cursorAgenteCajaActual.getString(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente_login.AG_LIQUIDACION)));
            if (getDatePhone().equals(fechaCaja)) {
                succesLogin = true;
            } else {
                succesLogin = false;
                Toast.makeText(getApplicationContext(), "Abriendo nueva caja...", Toast.LENGTH_LONG).show();
            }
        }


        if (cursorAgenteCajaActual.getCount() == 0) {
            succesLogin = false;
        }

        if (succesLogin) {
                /*
                ((MyApplication) loginClass.getApplication()).setIdAgente(mCursorAgente.getInt(mCursorAgente.getColumnIndexOrThrow(dbAdapter_agente.AG_id_agente_venta)));
                ((MyApplication) loginClass.getApplication()).setIdLiquidacion(mCursorAgente.getInt(mCursorAgente.getColumnIndexOrThrow(dbAdapter_agente.AG_liquidacion)));
                ((MyApplication) loginClass.getApplication()).setIdUsuario(mCursorAgente.getInt(mCursorAgente.getColumnIndexOrThrow(dbAdapter_agente.AG_id_usuario)));
                ((MyApplication) loginClass.getApplication()).setDisplayedHistorialComprobanteAnterior(false);

*/

              /*  session.deleteVariable(1);
                session.deleteVariable(3);
                session.deleteVariable(4);
                session.deleteVariable(6);
                session.deleteVariable(9);*/

               /* session.createTempSession(1,cursorAgenteCajaActual.getInt(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente.AG_id_agente_venta)));
                session.createTempSession(3,cursorAgenteCajaActual.getInt(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente.AG_liquidacion)));
                session.createTempSession(4,cursorAgenteCajaActual.getInt(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente.AG_id_usuario)));
                session.createTempSession(6,0);
                session.createTempSession(9,1);*/


            Toast.makeText(getApplicationContext(), "Login correcto", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Login.this, VMovil_Abrir_Caja.class);
            finish();
            startActivity(i);
        } else {
            if (conectadoRedMovil() || conectadoWifi()) {
                new LoginRest().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Necesita estar conectado a internet la primera vez", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:

                if (validarFecha()){

                        if (estaConectado()) {
                            new LoginRest().execute();
                        } else {
                            cajaAbierta();
                        }

                }else{
                    if (estaConectado()){
                        Utils.setToast(mainActivity, "VALIDANDO FECHA...", R.color.verde);
                        new FechaRest().execute();
                    }else{
                        Utils.setToast(mainActivity, "No está conectado a internet.", R.color.verde);
                    }

                }

                //new AttemptLogin().execute();
                break;

            case R.id.imageViewLogo:

                countClicked++;
                if (countClicked>=7){
                    succesMACDevice = true;
                    Toast.makeText(Login.this, "EQUIPO AUTORIZADO", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    class LoginRest extends AsyncTask<String, String, String> {
        private String usuario;
        private String clave;
        private DbAdapter_Motivo_Dev dbAdapter_motivo_dev;

        @Override
        protected String doInBackground(String... strings) {
            StockAgenteRestApi api = new StockAgenteRestApi(mainActivity);
            dbAdapter_motivo_dev = new DbAdapter_Motivo_Dev(mainActivity);
            dbAdapter_motivo_dev.open();
            dbAdapter_motivo_dev.deleteMotDev();
            JSONObject jsonObjectMotivo = null;
            ArrayList<Agente> agenteLista = null;
            JSONObject jsonObjAgente = null;
            publishProgress("" + 10);

            try {

                Log.d(TAG, "LOGIN DATOS : " +user.getText().toString() + " - " + pass.getText().toString() + " - " + getDatePhone());
                jsonObjAgente = api.GetDatosAgente(user.getText().toString(), pass.getText().toString());
                Log.d(TAG, "JSON OBJECT AGENTE : " + jsonObjAgente.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "JSON OBJECT AGENTE" + e.getMessage());
            }
            ParserAgente parserAgente = new ParserAgente();

            publishProgress("" + 25);
            //agenteLista = parserAgente.parserAgenteDatos(jsonObjAgente);
            agenteLista = parserAgente.parserAgente(jsonObjAgente, user.getText().toString(), pass.getText().toString());
            publishProgress("" + 50);
            if (agenteLista.size() > 0) {
                succesLogin = true;
                usuario = user.getText().toString();
                clave = pass.getText().toString();
                for (int i = 0; i < agenteLista.size(); i++) {
                    Log.d(TAG, "Agente : " + i + " , Nombre : " + agenteLista.get(i).getNombreAgente() + ", MAC CIPHERLAB: " + agenteLista.get(i).getMAC2());
                    session.deleteVariable(777);
                    session.createTempSession(777, agenteLista.get(i).getRutaId());
                    /*
                    //VARIABLE GLOBAL, PARA OBTENERLA DESDE CUALQUIER SITIO DE LA APLICACIÓN
                    ((MyApplication) loginClass.getApplication()).setIdAgente(agenteLista.get(i).getIdAgenteVenta());
                    ((MyApplication) loginClass.getApplication()).setIdLiquidacion(agenteLista.get(i).getLiquidacion());
                    ((MyApplication) loginClass.getApplication()).setDisplayedHistorialComprobanteAnterior(false);
                    ((MyApplication) loginClass.getApplication()).setIdUsuario(agenteLista.get(i).getIdUsuario());
*/
/*
                    session.deleteVariable(1);
                    session.deleteVariable(3);
                    session.deleteVariable(4);
                    session.deleteVariable(6);
                    session.deleteVariable(9);
                    session.deleteVariable(10);
                    session.deleteVariable(11);


                    session.createTempSession(1,agenteLista.get(i).getIdAgenteVenta());
                    session.createTempSession(3,agenteLista.get(i).getLiquidacion());
                    session.createTempSession(4,agenteLista.get(i).getIdUsuario());
                    session.createTempSession(6,0);
                    session.createTempSession(9,1);
                    int correlativoFactura = agenteLista.get(i).getCorrelativoFactura()+1;
                    int correlativoBoleta = agenteLista.get(i).getCorrelativoBoleta()+1;
                    session.createTempSession(10,correlativoFactura);
                    session.createTempSession(11,correlativoBoleta);


*/


                    /*WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    WifiInfo info = manager.getConnectionInfo();
                    String address = info.getMacAddress().toUpperCase();
*/
                    String address = Utils.getBluetoothMacAddress();
                    Log.d(TAG, "MAC ADRESS : " + address);

                    if (address.equals(agenteLista.get(i).getMAC2())) {
                        succesMACDevice = true;
                    }


                    agenteLista.get(i).getIdAgenteVenta();
                    boolean existe = dbAdapter_agente_login.existeAgentesById(agenteLista.get(i).getIdAgenteVenta());
                    Log.d("EXISTE ", "" + existe + "LIQUIDACION : " + agenteLista.get(i).getIdAgenteVenta());
                    try {
                        jsonObjectMotivo = api.fsel_MotivoDevolucion(agenteLista.get(i).getIdAgenteVenta());
                        Log.d("JSONOBJECTMOTIVO",""+jsonObjectMotivo.toString());
                        if (Utils.isSuccesful(jsonObjectMotivo)) {
                            JSONArray jsonArray = jsonObjectMotivo.getJSONArray("Value");
                            for (int u = 0; u < jsonArray.length(); u++) {
                                JSONObject json = jsonArray.getJSONObject(u);
                                long a = dbAdapter_motivo_dev.createMotDev(json.getInt("Id"),json.getString("Descripcion"));
                                Log.d("ESTAD GUARDO: ",a+"");
                            }
                            Log.d("", "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (existe) {
                        dbAdapter_agente_login.updateAgente(agenteLista.get(i), getDatePhone(), usuario, clave);
                    } else {
                        //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                        dbAdapter_agente_login.createAgente(agenteLista.get(i), getDatePhone(), usuario, clave);
                    }
                }
            }
            publishProgress("" + 75);
            return null;
        }

        @Override
        protected void onPreExecute() {
            createProgressDialog();
        }

        private void dismissProgressDialog() {
            if (prgDialog != null && prgDialog.isShowing()) {
                prgDialog.dismiss();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (mainActivity.isFinishing()) {
                //dismissprgDialog();
                prgDialog.dismiss();
                return;
            } else {

                prgDialog.setProgress(100);
                dismissProgressDialog();
            }

            if (succesLogin) {

/*                if (addNewAccount){
                    agregarCuenta();
                }*/
                if (succesMACDevice) {
                    Toast.makeText(getApplicationContext(), "Login correcto", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, VMovil_Abrir_Caja.class);
                    finish();
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "USUARIO NO AUTORIZADO EN ESTE DISPOSITIVO.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Las credenciales son incorrectas", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            prgDialog.setProgress(Integer.parseInt(values[0]));
        }
    }

/*    public void agregarCuenta(){

                final Account account = new Account(user.getText().toString(),
                        AccountManager.KEY_ACCOUNT_TYPE);
                // Creando cuenta en el dispositivo y seteando el token que obtuvimos.
                mAccountManager.addAccountExplicitly(account, pass.getText().toString(), null);

                // Ojo: hay que setear el token explicitamente si la cuenta no existe,
                // no basta con mandarlo al authenticator
                mAccountManager.setAuthToken(account, AccountAuthenticator.AUTHTOKEN_TYPE, user.getText().toString()+"."+pass.getText().toString());
    }*/
/*
    class AttemptLogin extends AsyncTask<String, String, String> {

		 /**
         * Before starting background thread Show Progress Dialog

		boolean failure = false;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
            int success;
            String nombre = "";
            String username = user.getText().toString();
            String password = pass.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
 
                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);
 
                // check your log for json response
                Log.d("Login attempt", json.toString());
 
                // json success tag
                success = json.getInt(TAG_SUCCESS);
                nombre = json.getString(TAG_SUCCESS);
                //user.setText(String.valueOf(nombre));
                //modificarValorVar1();
                //setVar1(String.valueOf(success));
                //pass.setText(String.valueOf(success));
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                	Intent i = new Intent(Login.this, VMovil_Online_Pumovil.class);

                    i.putExtra("putPassUsuario", json.getString(TAG_pass_usuario));
                    i.putExtra("putNombreAgente", json.getString(TAG_nombre_agente));
                    i.putExtra("putNombreUsuario", json.getString(TAG_nombre_usuario));
                    i.putExtra("putIdAgenteVenta", json.getString(TAG_id_agente_venta));
                    i.putExtra("putIdEmpresa", json.getString(TAG_id_empresa));
                    i.putExtra("putIdUsuario", json.getString(TAG_id_usuario));


                	finish();
    				startActivity(i);
                	return json.getString(TAG_MESSAGE);
                }
                if (success == 0) {
                	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
			
		}
		**
         * After completing background task Dismiss the progress dialog
         * *
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
    */

    public void createProgressDialog() {
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Logeando...");
        prgDialog.setIndeterminate(false);
        prgDialog.setMax(100);
        prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        prgDialog.setCancelable(false);
        prgDialog.show();

    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }

}
