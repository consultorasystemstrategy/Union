package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import union.union_vr1.AsyncTask.ExportMain;
import union.union_vr1.AsyncTask.ImportMain;
import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Utils.DialogSincronizarOffLine;
import union.union_vr1.Utils.MyApplication;


public class VMovil_Online_Pumovil extends Activity {

    private WebView view;
    private VMovil_Online_Pumovil mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_web_view);

        mContext = this;

        //((MyApplication)getApplication()).setMainActivity(this);

        showWebPU();
/*
        DbAdapter_Stock_Agente dbHelper_Stock;
        DbAdapter_Histo_Venta_Detalle dbHelper_Hi_De;
        dbHelper_Stock = new DbAdapter_Stock_Agente(this);
        dbHelper_Stock.open();
        dbHelper_Stock.deleteAllStockAgente();
        dbHelper_Stock.insertSomeStockAgente();
        //--------------------------------------------------------
        dbHelper_Hi_De = new DbAdapter_Histo_Venta_Detalle(this);
        dbHelper_Hi_De.open();
        dbHelper_Hi_De.deleteAllHistoVentaDetalle();
        dbHelper_Hi_De.insertSomeHistoVentaDetalle();

        DbAdapter_Precio adapprecio = new DbAdapter_Precio(this);
        adapprecio.open();
        adapprecio.deleteAllPrecio();
        adapprecio.insertSomePrecio();
*/
        //showWebPU();

        /*
        final Button button = (Button) findViewById(R.id.buttonImport);
        final Button buttonExport = (Button) findViewById(R.id.buttonExportar);
        final Button buttonTemp = (Button) findViewById(R.id.buttonTemp);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                DialogSincronizarOffLine dialogConfirm = new DialogSincronizarOffLine();
                dialogConfirm.show(manager, "DialogSincronizarOffLine");


                new ImportMain(mContext).execute();
            }
        });
        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExportMain(mContext).execute();
            }
        });
        buttonTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VMovil_Evento_Indice.class);
                finish();
                startActivity(intent);
            }
        });
*/
    }

    public void showWebPU(){
        view = (WebView) this.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        view.loadUrl("http://192.168.0.100:8084/SysMovilProductosUnion");

        /*Asignamos a la vista web el cliente (navegador)
        que hemos creado como clase privada (ver más abajo
        y que extiende del que trae Android por defecto.
        Esta clase maneja el navegador:*/
        view.setWebViewClient(new MiWebViewClient());

        /*Asignamos a la vista web la clase MiWebViewClient
        que hemos creado como clase privada (ver más abajo)
        y que extiende del que trae Android por defecto.
        Esta clase permite controlar los eventos que se producen
        en el navegador:*/
        view.setWebChromeClient(new MiWebCromeClient());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sincronizar_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){

            case R.id.buttonImport:
                new ImportMain(mContext).execute();
                break;
            case R.id.buttonExportar:
                new ExportMain(mContext).execute();
                break;
            case R.id.buttonRedireccionarPrincipal:
                Intent intent = new Intent(mContext, VMovil_Evento_Indice.class);
                finish();
                startActivity(intent);
                break;
            default:
                //ON ITEM SELECTED DEFAULT
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private class MiWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    //Con esta clase controlamos algunos eventos javascript del navegador
    final class MiWebCromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            new AlertDialog.Builder(view.getContext()).setMessage(message).setCancelable(true).show();
            result.confirm();
            return true;
        }
    }

}
