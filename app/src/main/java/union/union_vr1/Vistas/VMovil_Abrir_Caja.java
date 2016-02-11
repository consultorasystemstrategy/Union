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
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.JSONParser.ParserAgente;
import union.union_vr1.Objects.Agente;
import union.union_vr1.R;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Agente_Login;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Utils.Utils;

public class VMovil_Abrir_Caja extends Activity implements View.OnClickListener {
    private boolean succesLogin;
    private boolean succesMACDevice;
    private DbAdapter_Temp_Session session;
    private Button btnAbrirCaja;
    private Button btnVerInventario;
    private DbAdapter_Agente_Login dbAdapter_agente_login;
    private int idAgente;
    private int kilometraje;
    private Activity activity;
    private String login_usuario;
    private String login_clave;
    private DbAdapter_Agente dbAdapter_agente;

    private static final String TAG = VMovil_Abrir_Caja.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__abrir__caja);
        activity = this;
        dbAdapter_agente_login = new DbAdapter_Agente_Login(this);
        dbAdapter_agente_login.open();
        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();
        session = new DbAdapter_Temp_Session(this);
        session.open();
        int cursorLiquidacion = dbAdapter_agente_login.fetchLiquidacion(getDatePhone());

        Cursor cursor = dbAdapter_agente_login.fetchAllAgentesVentaLogin(getDatePhone());
        idAgente = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Agente_Login.AG_id_agente_venta));
        login_usuario = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Agente_Login.LOGIN_usuario));
        login_clave = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Agente_Login.LOGIN_clave));
        btnAbrirCaja = (Button) findViewById(R.id.btnAbrirCja);
        btnVerInventario = (Button) findViewById(R.id.btnVerInventario);
        btnAbrirCaja.setOnClickListener(this);
        btnVerInventario.setOnClickListener(this);
        if (cursorLiquidacion > 0) {
            //  int liquidacion = cursorLiquidacion.
            cajaestaAbierto();
        }
    }

    private void cajaestaAbierto() {
        new TraerDatos().execute();
        this.finish();
        startActivity(new Intent(getApplicationContext(), VMovil_Evento_Indice.class));
    }

    public void displayModal() {
        final EditText editKm = new EditText(getApplicationContext());
        editKm.setInputType(InputType.TYPE_CLASS_NUMBER);
        editKm.setTextColor(0xff000000);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("¿Esta Seguro de Abrir Cuenta?");
        alertDialogBuilder.setView(editKm);
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Ingrese Kilometraje")
                .setCancelable(false)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (editKm.getText().toString().equals("")) {
                            editKm.setError("Ingrese Kilometraje");
                        } else {
                            Toast.makeText(getApplicationContext(), "Abriendo Cuenta...", Toast.LENGTH_SHORT).show();
                            kilometraje = Integer.parseInt(editKm.getText().toString());
                            new AbriCaja().execute();
                        }

                    }

                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAbrirCja:
                displayModal();
                break;
            case R.id.btnVerInventario:
                startActivity(new Intent(getApplicationContext(), VMovil_Consultar_Inventario.class));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    class TraerDatos extends AsyncTask<String, String, String> {
        ArrayList<Agente> agenteLista = null;

        @Override
        protected String doInBackground(String... strings) {
            StockAgenteRestApi api = new StockAgenteRestApi(activity);
            JSONObject jsonObjAgenteDatos = null;
            try {
                ParserAgente parserAgente = new ParserAgente();
                jsonObjAgenteDatos = api.GetAgenteVenta(login_usuario, login_clave, getDatePhone());
                //agenteLista = parserAgente.parserAgenteDatos(jsonObjAgenteDatos);
                agenteLista = parserAgente.parserAgente(jsonObjAgenteDatos, login_usuario, login_clave);
                Log.d("JSON OBJECT AGENTE", "" + jsonObjAgenteDatos.toString());
                if (agenteLista.size() > 0) {
                    succesLogin = true;

                    for (int i = 0; i < agenteLista.size(); i++) {
                        Log.d(TAG, "Agente : " + i + " , Nombre : " + agenteLista.get(i).getNombreAgente() + ", MAC CIPHERLAB: " + agenteLista.get(i).getMAC2());
                        session.deleteVariable(1);
                        session.deleteVariable(3);
                        session.deleteVariable(4);
                        session.deleteVariable(6);
                        session.deleteVariable(9);
                        session.deleteVariable(10);
                        session.deleteVariable(11);
                        session.deleteVariable(Constants._ID_SESSION_MAC);
                        session.deleteVariable(Constants._ID_SESSION_MAC_DEVICE_CIPHER_LAB);
                        session.deleteVariable(Constants.SESSION_ESTADO_DEVOLUCIONES);
                        session.createTempSession(1, agenteLista.get(i).getIdAgenteVenta());
                        session.createTempSession(3, agenteLista.get(i).getLiquidacion());
                        session.createTempSession(4, agenteLista.get(i).getIdUsuario());
                        session.createTempSession(6, 0);
                        session.createTempSession(9, 1);
                        int correlativoFactura = agenteLista.get(i).getCorrelativoFactura() + 1;
                        int correlativoBoleta = agenteLista.get(i).getCorrelativoBoleta() + 1;
                        session.createTempSession(10, correlativoFactura);
                        session.createTempSession(11, correlativoBoleta);
                        session.createTempSession(Constants.SESSION_ESTADO_DEVOLUCIONES, 0);
                        session.createTempSession(Constants.SESSION_VALIDACION_REGISTROS_EXPORTADOS, 0);
                        session.createTempSessionString(Constants._ID_SESSION_MAC, agenteLista.get(i).getMAC());
                        session.createTempSessionString(Constants._ID_SESSION_MAC_DEVICE_CIPHER_LAB, agenteLista.get(i).getMAC2());

                       /* WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        WifiInfo info = manager.getConnectionInfo();
                        String address = info.getMacAddress().toUpperCase();

                        Log.d(TAG, "MAC ADRESS : "+ address);

                        if (address.equals(agenteLista.get(i).getMAC2())){
                            succesMACDevice = true;
                        }*/

                        String address =  Utils.getBluetoothMacAddress();
                        Log.d(TAG, "MAC ADRESS : "+ address);

                        if (address.equals(agenteLista.get(i).getMAC2())){
                            succesMACDevice = true;
                        }

                        agenteLista.get(i).getIdAgenteVenta();
                        boolean existe = dbAdapter_agente.existeAgentesById(agenteLista.get(i).getIdAgenteVenta());
                        Log.d("EXISTE ", "" + existe);
                        if (existe) {
                            dbAdapter_agente.updateAgente(agenteLista.get(i), getDatePhone());
                        } else {
                            //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                            dbAdapter_agente.createAgente(agenteLista.get(i), getDatePhone());
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), "Datos Actualizados", Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }
    }

    class AbriCaja extends AsyncTask<String, String, String> {
        ArrayList<Agente> agenteLista = null;

        @Override
        protected String doInBackground(String... strings) {

            StockAgenteRestApi api = new StockAgenteRestApi(activity);
            JSONObject jsonObjAgente = null;
            JSONObject jsonObjAgenteDatos = null;


            publishProgress("" + 10);
            try {
                Log.d("LOGIN DATOS", "" + idAgente + "----" + kilometraje);
                jsonObjAgente = api.InsAbrirCaja(idAgente, kilometraje);
                Log.d("JSON OBJECT AbrirCaja", "" + jsonObjAgente.toString());
                int objrturn = -1;



                Log.d("JSON RETURN INT CAJA", "" + objrturn);

                if (Utils.isSuccesful(jsonObjAgente) && Utils.validateRespuesta(jsonObjAgente)){
                    objrturn = jsonObjAgente.getInt("Value");
                    if (objrturn > 0) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "ABRIENDO CAJA...", Toast.LENGTH_SHORT).show();
                            }
                        });
                        jsonObjAgenteDatos = api.GetAgenteVenta(login_usuario, login_clave, getDatePhone());
                        ParserAgente parserAgente = new ParserAgente();
                        //agenteLista = parserAgente.parserAgenteDatos(jsonObjAgenteDatos);
                        agenteLista = parserAgente.parserAgente(jsonObjAgenteDatos, login_usuario, login_clave);
                        Log.d("JSON OBJECT AGENTE", "" + jsonObjAgenteDatos.toString());

                        if (agenteLista.size() > 0) {
                            succesLogin = true;

                            for (int i = 0; i < agenteLista.size(); i++) {
                                Log.d(TAG,"Agente : " + i +  " ,Nombre : " + agenteLista.get(i).getNombreAgente() + ", MAC CIPHERLAB: "+agenteLista.get(i).getMAC2());
                                session.deleteVariable(1);
                                session.deleteVariable(3);
                                session.deleteVariable(4);
                                session.deleteVariable(6);
                                session.deleteVariable(9);
                                session.deleteVariable(10);
                                session.deleteVariable(11);
                                session.deleteVariable(12);
                                session.deleteVariable(13);
                                session.deleteVariable(Constants.SESSION_ESTADO_DEVOLUCIONES);

                                session.createTempSession(1, agenteLista.get(i).getIdAgenteVenta());
                                session.createTempSession(3, agenteLista.get(i).getLiquidacion());
                                session.createTempSession(4, agenteLista.get(i).getIdUsuario());
                                session.createTempSession(6, 0);
                                session.createTempSession(9, 1);
                                int correlativoFactura = agenteLista.get(i).getCorrelativoFactura() + 1;
                                int correlativoBoleta = agenteLista.get(i).getCorrelativoBoleta() + 1;
                                session.createTempSession(10, correlativoFactura);
                                session.createTempSession(11, correlativoBoleta);
                                session.createTempSessionString(12, agenteLista.get(i).getMAC());
                                session.createTempSessionString(13, agenteLista.get(i).getMAC2());
                                session.createTempSession(Constants.SESSION_ESTADO_DEVOLUCIONES, 0);
/*
                            WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                            WifiInfo info = manager.getConnectionInfo();
                            String address = info.getMacAddress().toUpperCase();
                            Log.d(TAG, "MAC ADRESS : "+ address);

                            if (address.equals(agenteLista.get(i).getMAC2())){
                                succesMACDevice = true;
                            }*/

                                String address =  Utils.getBluetoothMacAddress();
                                Log.d(TAG, "MAC ADRESS : "+ address);

                                if (address.equals(agenteLista.get(i).getMAC2())){
                                    succesMACDevice = true;
                                }

                                agenteLista.get(i).getIdAgenteVenta();
                                boolean existe = dbAdapter_agente.existeAgentesById(agenteLista.get(i).getIdAgenteVenta());
                                Log.d("EXISTE ", "" + existe);
                                if (existe) {
                                    dbAdapter_agente.updateAgente(agenteLista.get(i), getDatePhone());
                                } else {
                                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                                    dbAdapter_agente.createAgente(agenteLista.get(i), getDatePhone());
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "DATOS ACTUALIZADOS", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                }else {
                    if (conectadoRedMovil() || conectadoWifi()) {

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "NO HAY CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (succesLogin) {
                /*if (succesMACDevice){

                }
                */
                    Toast.makeText(getApplicationContext(), "ÉXITO", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), VMovil_Evento_Indice.class);
                    finish();
                    startActivity(i);
                /*}else {
                    Toast.makeText(getApplicationContext(), "USUARIO NO AUTORIZADO EN ESTE DISPOSITIVO.", Toast.LENGTH_SHORT).show();
                }*/
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "ERROR, INTENTE DE NUEVO", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            super.onPostExecute(s);
        }
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

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }
}
