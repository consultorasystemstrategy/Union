package union.union_vr1.AsyncTask;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;

/**
 * Created by Kelvin on 27/08/2015.
 */
public class TimerGps extends Service {

    LocationManager lm;
    LocationListener li;
    LocalizacionAngente localizacionAngente;
    Context context;
    DbAdapter_Temp_Session dbAdapter_temp_session;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        li = new MyLocationListener();
        context = this;
        localizacionAngente = new LocalizacionAngente(context);
        dbAdapter_temp_session = new DbAdapter_Temp_Session(context);
        dbAdapter_temp_session.open();


    }

    @Override
    public int onStartCommand(Intent intent, int floga, int startId) {

        Log.i("Empezo el Servicio", "START");
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,300000,0,li);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("Se destruyo", "xD");
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            localizacionAngente = new LocalizacionAngente(context);
            if (location != null){

                Log.i("POSITION",""+isNetworkAvailable());
                if(isNetworkAvailable()){
                    sendPosition(location);
                }


            }
        }
        private void sendPosition(Location location){
            int slideIdAgente = dbAdapter_temp_session.fetchVarible(1);
            localizacionAngente.execute(""+slideIdAgente,""+location.getLatitude(),""+location.getLongitude());
        }
        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        @Override
        public void onStatusChanged(String s, int in, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

}
