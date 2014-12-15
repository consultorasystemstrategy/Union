package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;


public class VMovil_Online_Pumovil extends Activity {
    private DbAdapter_Comprob_Cobro dbHelper4;

    private  WebView view;
    private TextView textView;

    private DbAdapter_Agente dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_web_view);


        textView = (TextView)findViewById(R.id.tag_message);
        Bundle bundle = getIntent().getExtras();
        textView.setText(bundle.getString("putPassUsuario"));


        view = (WebView)this.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        view.loadUrl("http://192.168.0.105:8084/SysMovilProductosUnion");

        //Asignamos a la vista web el cliente (navegador)
        //que hemos creado como clase privada (ver más abajo
        //y que extiende del que trae Android por defecto.
        //Esta clase maneja el navegador:
        view.setWebViewClient(new MiWebViewClient());

        //Asignamos a la vista web la clase MiWebViewClient
        //que hemos creado como clase privada (ver más abajo)
        //y que extiende del que trae Android por defecto.
        //Esta clase permite controlar los eventos que se producen
        //en el navegador:
        view.setWebChromeClient(new MiWebCromeClient());

        //displayUpdateAgente();

        //Insertar Algunos Datos de Prueba

      //  dbHelper4 = new DbAdapter_Comprob_Cobro(this);
       // dbHelper4.open();
        //dbHelper4.deleteAllComprobCobros();
        //dbHelper4.insertSomeComprobCobros();
    }

    private class HelloWebViewCliente extends WebViewClient{
        public boolean loading(WebView webView,String url){
             webView.loadUrl(url);
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vmovil__online__pumovil, menu);
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

    public void displayUpdateAgente(){
        dbHelper = new DbAdapter_Agente(this);
        dbHelper.open();
        dbHelper.deleteAllAgentes();
        dbHelper.insertSomeAgentes();
        Bundle bundle = getIntent().getExtras();
        String nombre_usuario = bundle.getString("putNombreUsuario");
        String nombre_agente = bundle.getString("putNombreAgente");
        String pass_usuario = bundle.getString("putPassUsuario");
        String id_agente_venta = bundle.getString("putIdAgenteVenta");
        String id_usuario = bundle.getString("putIdUsuario");
        String id_empresa = bundle.getString("putIdEmpresa");
        dbHelper.updateAgente(id_agente_venta,id_usuario,id_empresa,nombre_usuario,nombre_agente,pass_usuario);
    }


    public void lanzarOnLine(View v){
        Intent i = new Intent(this,VMovil_Evento_Indice.class);
        startActivity(i);
    }

    public void lanzarOffLine(View v){
        Intent i = new Intent(this,VMovil_Online_Pumovil.class);
        startActivity(i);
    }

    private class MiWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }

    //Con esta clase controlamos algunos eventos javascript del navegador
    final class MiWebCromeClient extends WebChromeClient
    {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result)
        {
            new AlertDialog.Builder(view.getContext()).setMessage(message).setCancelable(true).show();
            result.confirm();
            return true;
        }
    }

}
