package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kelvin on 05/11/2015.
 */
public class GetAddress extends AsyncTask<String,String,String> {
    private String  lat ="";
    private String lng="";
    private URL URLCONNECTION ;
    private JSONObject jsonObject;
    private String urlString="http://maps.google.com/maps/api/geocode/json?";
    private Activity activity;
    public GetAddress(Activity ctx){

        activity=ctx;

    }
    @Override
    protected String doInBackground(String... strings) {
        String idEstablecimiento = strings[0];
        lat = strings[1];
        lng = strings[2];
        try {
            URLCONNECTION= new URL(urlString+"latlng="+lat+","+lng+"&sensor=false");
            HttpURLConnection urlConnection = (HttpURLConnection) URLCONNECTION.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            jsonObject= new JSONObject(responseStrBuilder.toString());
            Log.d("CONNECTEDSERVICE", jsonObject.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
