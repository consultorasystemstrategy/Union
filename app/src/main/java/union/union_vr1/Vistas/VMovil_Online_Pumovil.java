package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.Login;
import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;

public class VMovil_Online_Pumovil extends Activity {

    //SERVIDOR LOCAL
    private static final String urlLocal = "http://192.168.0.158:8080/SysMovilProductosUnion";
    //SERVIDOR REMOTO - PU
    private static final String urlRemoto = "http://190.81.172.113:8080/SysMovilProductosUnion";
    //
    private String url = "http://192.168.0.158:8080/SysMovilProductosUnion";

    private ProgressBar progressBar;
    private WebView webView;
    private Activity activity;


    private DbAdapter_Agente dbAdapter_agente;
    private boolean succesLogin;
    private DbAdapter_Temp_Session session;
    private int idAgente;
    private int idLiquidacion;
    private int isCajaOpened;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int servidorTipo =  Integer.parseInt(SP.getString("servidorTipo", "1"));

        if (servidorTipo==1){
            url = urlLocal;
        }else if(servidorTipo ==2){
            url = urlRemoto;
        }else{

        }

        displayURL();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_web_view);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int servidorTipo =  Integer.parseInt(SP.getString("servidorTipo", "1"));

        if (servidorTipo==1){
            url = urlLocal;
        }else if(servidorTipo ==2){
            url = urlRemoto;
        }else{

        }

        activity = this;

        session = new DbAdapter_Temp_Session(this);
        session.open();




        isCajaOpened = session.fetchVarible(9);
        Log.d("IS CAJA OPENED", ""+isCajaOpened);

        if (isCajaOpened==0){
            //LA CAJA NO ESTÁ ABIERTA
            displayURL();
        }else if (isCajaOpened==1){
            //LA CAJA ESTÁ ABIERTA
            redireccionarPrincipal();
        }else{
            redireccionarPrincipal();
        }
    }

    public void displayURL(){



        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 100);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //return super.onJsAlert(view, url, message, result);
                new AlertDialog.Builder(view.getContext()).setMessage(message).setCancelable(true).show();
                result.confirm();
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                super.onPageFinished(view, url);
            }

        });

        webView.loadUrl(url);
    }

    public void redireccionarPrincipal(){
        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();


        idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);


        succesLogin = false;

        Cursor cursorAgenteCajaActual = dbAdapter_agente.fetchAgentesByIds(idAgente,idLiquidacion);
        cursorAgenteCajaActual.moveToFirst();
        String fechaCaja = null;
        if (cursorAgenteCajaActual.getCount()>0) {
            succesLogin=true;
            fechaCaja = cursorAgenteCajaActual.getString(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente.AG_fecha));

            if (getDatePhone().equals(fechaCaja)){
                succesLogin=true;
            }else{
                //LA CAAJA ESTÁ ABIERTA PERO NO CON LA FECHA ACTUAL
                Toast.makeText(getApplicationContext(), "Debe Abrir Caja", Toast.LENGTH_LONG).show();
                succesLogin=false;
            }
        }


        if(cursorAgenteCajaActual.getCount()==0){
            succesLogin=false;
        }

        if (succesLogin){
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

            session.createTempSession(1,cursorAgenteCajaActual.getInt(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente.AG_id_agente_venta)));
            session.createTempSession(3,cursorAgenteCajaActual.getInt(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente.AG_liquidacion)));
            session.createTempSession(4,cursorAgenteCajaActual.getInt(cursorAgenteCajaActual.getColumnIndexOrThrow(dbAdapter_agente.AG_id_usuario)));
            session.createTempSession(6,0);

            Intent i = new Intent(activity, VMovil_Evento_Indice.class);
            finish();
            startActivity(i);
        }else{
            //LA CAJA ESTÁ ABIERTA PERO NO CON LA FECHA ACTUAL, ENTONCES TIENE QUE ABRIR CAJA
            displayURL();
        }


    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
        {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ir_offline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.buttonRedireccionarPrincipal:
                Intent intent = new Intent(activity, Login.class);
                finish();
                startActivity(intent);
                break;
            case R.id.buttonAjustes:
                Intent intentA = new Intent(activity, AppPreferences.class);
                startActivity(intentA);
            default:
                //ON ITEM SELECTED DEFAULT
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }
}
