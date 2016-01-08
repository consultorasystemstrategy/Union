package union.union_vr1;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.Toast;

import com.sewoo.port.android.BluetoothPort;

import union.union_vr1.Conexion.JSONParser;
import union.union_vr1.JSONParser.ParserAgente;
import union.union_vr1.Objects.Agente;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Agente_Login;
import union.union_vr1.Sqlite.DbAdapter_Motivo_Dev;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.AppPreferences;
import union.union_vr1.Vistas.VMovil_Abrir_Caja;
import union.union_vr1.Vistas.VMovil_Evento_Indice;

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

    //JSON elementos para la tabla m_agente
    // private static final String TAG_id_agente = "id_agente";
    // private static final String TAG_id_agente_venta = "id_agente_venta";
    // private static final String TAG_id_empresa = "id_empresa";
    // private static final String TAG_id_usuario = "id_usuario";
    // private static final String TAG_nombre_agente = "nombre_agente";
    // private static final String TAG_nombre_usuario = "nombre_usuario";
    //  private static final String TAG_pass_usuario = "pass_usuario";
    private int idAgente;
    private int idLiquidacion;
    private String nombreUsuario = "";

    //REDIRECCIONAR PRINCIPAL

    private DbAdapter_Agente dbAdapter_agente;
    private DbAdapter_Temp_Session session;
    private int isCajaOpened;
    private boolean isCajaActual;
    private Activity mainActivity;

    private static String TAG = Login.class.getSimpleName();


    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 2;

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

       /* session = new DbAdapter_Temp_Session(this);
        session.open();
*/


        //  idAgente = session.fetchVarible(1);
        //  idLiquidacion = session.fetchVarible(3);

        //((MyApplication)getApplication()).setImportado(false);
        //((MyApplication)getApplication()).setExport(false);


        // session.deleteVariable(7);
        // session.createTempSession(7,0);
        // session.deleteVariable(8);
        // session.createTempSession(8,0);

        loginClass = this;

        dbAdapter_agente_login = new DbAdapter_Agente_Login(this);
        dbAdapter_agente_login.open();


        //setup input fields
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        nombreUsuario = SP.getString("username", "");

        user.setText("" + nombreUsuario);

        //setup buttons
        mSubmit = (Button) findViewById(R.id.login);
        //mSalirs = (Button)findViewById(R.id.salir);

        //register listeners
        mSubmit.setOnClickListener(this);
        //mSalirs.setOnClickListener(this);
        //estaConectado();
        //if(isOnline()){
        //    user.setText("exito");
        //}else{
        //    user.setText("error");
        //

        bluetoothSetup();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
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

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        nombreUsuario = SP.getString("username", "emerson.f");


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
            /*case R.id.buttonNewDay:
                dbAdapter_agente.deleteAllAgentes();
                break;*/
            default:
                //ON ITEM SELECTED DEFAULT
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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

                if (estaConectado()) {
                    new LoginRest().execute();
                } else {
                    cajaAbierta();
                }
                //new AttemptLogin().execute();
                break;
        /*case R.id.salir:
            finish();
			break;*/
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

                Log.d("LOGIN DATOS , ", "" + user.getText().toString() + " - " + pass.getText().toString() + " - " + getDatePhone());
                jsonObjAgente = api.GetDatosAgente(user.getText().toString(), pass.getText().toString());
                Log.d("JSON OBJECT AGENTE", "" + jsonObjAgente.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("JSON OBJECT AGENTE", "" + e.getMessage());
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
