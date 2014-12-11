package union.union_vr1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import union.union_vr1.Conexion.JSONParser;
import union.union_vr1.Vistas.VMovil_Evento_Indice;
import union.union_vr1.Vistas.VMovil_Online_Pumovil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends Activity implements OnClickListener{
	
	private EditText user, pass;
	private Button mSubmit, mSalirs;
    private EditText Txt;
	 // Progress Dialog
    private ProgressDialog pDialog;
    private String pru;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private String var1 = "";

    public void setVar1(String var1){
        this.var1=var1;
    }

    public String getVar1(){
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
    private static final String LOGIN_URL = "http://192.168.0.158:8083/produnion/login.php";
    //private static final String LOGIN_URL = "http://192.168.0.158:8081/webservice/login.php";
  //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";
    
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_NOMBRE  = "name";

    //JSON elementos para la tabla m_agente
    private static final String TAG_id_agente = "id_agente";
    private static final String TAG_id_agente_venta = "id_agente_venta";
    private static final String TAG_id_empresa = "id_empresa";
    private static final String TAG_id_usuario = "id_usuario";
    private static final String TAG_nombre_agente = "nombre_agente";
    private static final String TAG_nombre_usuario = "nombre_usuario";
    private static final String TAG_pass_usuario = "pass_usuario";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		//setup input fields
		user = (EditText)findViewById(R.id.username);
		pass = (EditText)findViewById(R.id.password);

		//setup buttons
		mSubmit = (Button)findViewById(R.id.login);
        mSalirs = (Button)findViewById(R.id.salir);
		
		//register listeners
		mSubmit.setOnClickListener(this);
        mSalirs.setOnClickListener(this);
        //estaConectado();
        //if(isOnline()){
        //    user.setText("exito");
        //}else{
        //    user.setText("error");
        //}

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


    protected Boolean estaConectado(){
        if(conectadoWifi()){
            user.setText("Conexion a Wifi");
            return true;
        }else{
            if(conectadoRedMovil()){
                user.setText("Conexion a Movil");
                return true;
            }else{
                user.setText("No Tiene Conexion a Internet");
                return false;
            }
        }
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login:
			new AttemptLogin().execute();
			break;
		case R.id.salir:
            finish();
			break;
		default:
			break;
		}
	}

	class AttemptLogin extends AsyncTask<String, String, String> {

		 /**
         * Before starting background thread Show Progress Dialog
         * */
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
		/**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
		 

}
