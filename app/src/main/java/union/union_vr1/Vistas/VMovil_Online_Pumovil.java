package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import union.union_vr1.Login;
import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Utils.DialogSincronizarOffLine;
import union.union_vr1.Utils.MyApplication;


public class VMovil_Online_Pumovil extends Activity {

    private WebView view;
    private VMovil_Online_Pumovil mContext;
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_web_view);
        mContext = this;
        new AsyncTaskCargarPagina().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();
    }

    public void showWebPU() {
        view = (WebView) this.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        view.loadUrl("http://192.168.0.158:8080/SysMovilProductosUnion");
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
                Intent intent = new Intent(mContext, Login.class);
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

    class AsyncTaskCargarPagina extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view = (WebView) mContext.findViewById(R.id.webView);
                    view.getSettings().setJavaScriptEnabled(true);
                    view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    view.loadUrl("http://192.168.0.158:8080/SysMovilProductosUnion");

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
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            createProgressDialog();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
        }
    }

    public void createProgressDialog() {
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Cargando...");
        prgDialog.show();

    }

    private void dismissProgressDialog() {
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
    }

}
