package union.union_vr1.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import union.union_vr1.R;

public class VMovil_Online_Pumovil extends Activity {

    private  WebView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_web_view);

        view = (WebView)this.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl("http://192.168.0.107:8084/SysMovilProductosUnion");
        view.setWebViewClient(new HelloWebViewCliente());
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


    public void lanzarOnLine(View v){
        Intent i = new Intent(this,VMovil_Evento_Indice.class);
        startActivity(i);
    }

    public void lanzarOffLine(View v){
        Intent i = new Intent(this,VMovil_Online_Pumovil.class);
        startActivity(i);
    }

}
