package union.union_vr1.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.rengwuxian.materialedittext.MaterialEditText;

import union.union_vr1.R;

/**
 * Created by Kelvin on 04/11/2015.
 */
public class FMapaRegistrar extends Fragment {

    // private RadioButton buttonHibrido;
    // private RadioButton buttonNormal;
    private View v;
    boolean estadoPosicion = true;
    // private MapView mapView;
    // private GoogleMap map;
    private boolean estadoDF;
    private MaterialEditText editTextDescripcion;
    private MaterialEditText editTextDireccionFiscal;
    private Button btnActualizarP;
    private CheckBox checkBoxDF;
    private WebView webViewMap;
    LocationManager mLocationManager;
    Location myLocation;
    String idEstablecimiento;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map, container, false);
        estadoDF = true;
        myLocation = null;
        //MapsInitializer.initialize(getActivity());
       /* mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();*/
        btnActualizarP = (Button) v.findViewById(R.id.btnActualizarP);
        editTextDescripcion = (MaterialEditText) v.findViewById(R.id.map_descripcion);
        editTextDireccionFiscal = (MaterialEditText) v.findViewById(R.id.map_direccion_fiscal);
        checkBoxDF = (CheckBox) v.findViewById(R.id.checkBoxAviableDF);
        webViewMap = (WebView) v.findViewById(R.id.webViewMap);


        webViewMap.getSettings().setAppCachePath(getActivity().getApplicationContext().getCacheDir().getAbsolutePath() + "/cache");
        webViewMap.getSettings().setAllowFileAccess(true);
        webViewMap.getSettings().setAppCacheEnabled(true);
        webViewMap.getSettings().setJavaScriptEnabled(true);
        webViewMap.getSettings().setDomStorageEnabled(true);
        webViewMap.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webViewMap.setWebChromeClient(new WebChromeClient());
        webViewMap.setWebViewClient(new WebViewClient());
        webViewMap.setBackgroundColor(0x00000000);
        // Gets to GoogleMap from the MapView and does initialization stuff
        // map = mapView.getMap();
        // map.getUiSettings().setMyLocationButtonEnabled(true);
        // map.getUiSettings().setCompassEnabled(false);
        // map.setMyLocationEnabled(true);
        //  buttonHibrido = (RadioButton) v.findViewById(R.id.mapHibrido);
        //  buttonNormal = (RadioButton) v.findViewById(R.id.mapNormal);

      /*  map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                anInt = anInt + 1;
                if (anInt == 1) {
                    map.clear();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                    map.animateCamera(cameraUpdate);
                    location(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        });
*/


        // displayTouchLayout();
        display();
        displatLocation();
        return v;
    }

    private void displatLocation() {
        mLocationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                0, mLocaction);

    }

    private final LocationListener mLocaction = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            myLocation = location;
            Log.e("MYLOCATION", "LAT: " + location.getLatitude() + " - LON: " + location.getLongitude());
            if (estadoPosicion) {
                SharedPreferences.Editor editor =getActivity().getSharedPreferences("GPS", Context.MODE_PRIVATE).edit();
                editor.putString("LATITUD", ""+location.getLatitude());
                editor.putString("LONGITUD", "" + location.getLongitude());
                editor.commit();
                estadoPosicion=false;
                String urlMap="http://maps.google.com/maps/api/staticmap?center="+location.getLatitude()+","+location.getLongitude()+",&zoom=17&markers=icon:http://www.myiconfinder.com/uploads/iconsets/256-256-a5485b563efc4511e0cd8bd04ad0fe9e.png|"+location.getLatitude()+","+location.getLongitude()+"&path=color:0x0000FF80|weight:5|"+location.getLatitude()+","+location.getLongitude()+"&size=400x300";//
                webViewMap.loadUrl(urlMap);

            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void display() {
       // editTextDireccionFiscal.setEnabled(false);
        editTextDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!estadoDF) {


                    editTextDireccionFiscal.setText(editTextDescripcion.getText().toString());

                } else {

                    editTextDireccionFiscal.setText("");

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checkBoxDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!estadoDF) {
                   // editTextDireccionFiscal.setEnabled(true);
                    editTextDireccionFiscal.setText("");
                    estadoDF = true;
                } else {
                   // editTextDireccionFiscal.setEnabled(false);
                    editTextDireccionFiscal.setText(editTextDescripcion.getText().toString());
                    estadoDF = false;
                }
            }
        });

        btnActualizarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estadoPosicion=true;
            }
        });

    }

    /*
        @Override
        public void onPause() {
            super.onPause();
            Log.d("EVENTOS  ", "PAUSA");
            try {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("INFORMACION_MAPA", Context.MODE_PRIVATE).edit();
                editor.putString("descripcion", editTextDescripcion.getText().toString());
                editor.putString("direccion_fiscal", editTextDireccionFiscal.getText().toString());
                editor.putString("latitud", markerEstablecimeinto.getPosition().latitude + "");
                editor.putString("longitud", markerEstablecimeinto.getPosition().longitude+"");
                editor.commit();
            }catch (NullPointerException e){
           //   setMyLocation();
            }
     }
    */
    @Override
    public void onResume() {
        super.onResume();


    }


}
