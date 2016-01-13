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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.w3c.dom.Text;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Utils.Utils;

/**
 * Created by Kelvin on 04/11/2015.
 */
public class FMapaRegistrar extends Fragment implements Validator.ValidationListener {

    // private RadioButton buttonHibrido;
    // private RadioButton buttonNormal;
    private View v;
    boolean estadoPosicion = true;
    // private MapView mapView;
    // private GoogleMap map;
    private boolean estadoDF;
    @Required(order = 1, messageResId = R.string.requerido_input)
    private EditText editTextDescripcion;
    @Required(order = 2, messageResId = R.string.requerido_input)
    private EditText editTextDireccionFiscal;
    private Button btnActualizarP;
    private CheckBox checkBoxDF;
    private WebView webViewMap;
    LocationManager mLocationManager;
    Location myLocation;
    private Validator validator;
    private ViewPager viewPager;
    String idEstablecimiento;
    @Required(order = 3, messageResId = R.string.requerido_input)
    @NumberRule(order = 4, type = NumberRule.NumberType.DOUBLE, messageResId = R.string.requerido_input)
    private EditText textLat;
    @Required(order = 5, messageResId = R.string.requerido_input)
    @NumberRule(order = 6, type = NumberRule.NumberType.DOUBLE, messageResId = R.string.requerido_input)
    private EditText textLon;


    private DbAdapter_Temp_Establecimiento dbAdapter_temp_establecimiento;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map, container, false);

        estadoDF = true;
        myLocation = null;
        //MapsInitializer.initialize(getActivity());
       /* mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();*/
        idEstablecimiento = getArguments().getString("idEstablecimiento");

        dbAdapter_temp_establecimiento = new DbAdapter_Temp_Establecimiento(getActivity());
        dbAdapter_temp_establecimiento.open();


        validator = new Validator(this);
        validator.setValidationListener(this);

        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        btnActualizarP = (Button) v.findViewById(R.id.btnActualizarP);
        editTextDescripcion = (EditText) v.findViewById(R.id.map_descripcion);
        editTextDireccionFiscal = (EditText) v.findViewById(R.id.map_direccion_fiscal);
        checkBoxDF = (CheckBox) v.findViewById(R.id.checkBoxAviableDF);
        webViewMap = (WebView) v.findViewById(R.id.webViewMap);
        textLat = (EditText) v.findViewById(R.id.textLat);
        textLon = (EditText) v.findViewById(R.id.textLon);


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
        setDirFiscal();
        display();
        displatLocation();
        actualizar();
        return v;
    }

    private void displatLocation() {
        mLocationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                0, mLocaction);

    }

    private void actualizar() {
       /* if (conectadoWifi() || conectadoRedMovil()) {*/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("SELECTED", ": " + position);
                switch (position) {
                    case 0:

                        break;
                    case 1:
                        displatLocation();
                        break;
                    case 2:
                        validator.validate();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private final LocationListener mLocaction = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            myLocation = location;
            Log.e("MYLOCATION", "LAT: " + location.getLatitude() + " - LON: " + location.getLongitude());
            if (estadoPosicion) {
                estadoPosicion = false;
                String urlMap = "http://maps.google.com/maps/api/staticmap?center=" + location.getLatitude() + "," + location.getLongitude() + ",&zoom=17&markers=icon:http://www.myiconfinder.com/uploads/iconsets/256-256-a5485b563efc4511e0cd8bd04ad0fe9e.png|" + location.getLatitude() + "," + location.getLongitude() + "&path=color:0x0000FF80|weight:5|" + location.getLatitude() + "," + location.getLongitude() + "&size=400x300";//
                webViewMap.loadUrl(urlMap);

            }
            textLat.setText("" + location.getLatitude() + "");
            textLon.setText("" + location.getLongitude() + "");
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
private void setDirFiscal(){
    if (conectadoRedMovil() || conectadoWifi()) {
        SharedPreferences prefs = getActivity().getSharedPreferences("DIRECCION_FISCAL", Context.MODE_PRIVATE);
        String fiscal = prefs.getString("fiscal", null);

        if(fiscal !=null){
            editTextDireccionFiscal.setText(fiscal);
            editTextDireccionFiscal.setEnabled(false);
        }


    }
}
    private void display() {




        editTextDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!estadoDF) {

                  //  editTextDireccionFiscal.setText(editTextDescripcion.getText().toString());


                } else {
                   // editTextDireccionFiscal.setText("");



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
                    editTextDireccionFiscal.setEnabled(true);
                    //editTextDireccionFiscal.setText("");
                    setDirFiscal();
                    estadoDF = true;
                } else {
                    editTextDireccionFiscal.setEnabled(true);
                   // editTextDireccionFiscal.setText(editTextDescripcion.getText().toString());
                    estadoDF = false;
                }
            }
        });

        btnActualizarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estadoPosicion = true;
            }
        });

    }


    @Override
    public void onResume() {
        displatLocation();
        super.onResume();


    }


    @Override
    public void onValidationSucceeded() {
        String lat = "", lon = "", direccion = "", direccion_fiscal = "";

        lat = textLat.getText().toString();
        lon = textLon.getText().toString();
        direccion = editTextDescripcion.getText().toString();
        direccion_fiscal = editTextDireccionFiscal.getText().toString();


        long estado = dbAdapter_temp_establecimiento.updateTempEstablecDireccion(idEstablecimiento + "", lat, lon, direccion, direccion_fiscal);
        if (estado > 0) {
            viewPager.setCurrentItem(2);
            mLocationManager.removeUpdates(mLocaction);

        } else {
            Utils.setToast(getActivity(), "Ocurrio un error, por favor sal y vuelve a Intentarlo", R.color.rojo);
        }

    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {

        viewPager.setCurrentItem(1);

        String message = failedRule.getFailureMessage();
        Log.d("VALIDACION", "" + message + "****" + failedView.getId());
        if (failedView instanceof EditText) {
            ((EditText) failedView).setError(message);
        }
    }


    //----------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected Boolean conectadoWifi() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    protected Boolean conectadoRedMovil() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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


}
