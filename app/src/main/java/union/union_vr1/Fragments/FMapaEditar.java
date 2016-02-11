package union.union_vr1.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.w3c.dom.Text;

import union.union_vr1.Objects.EventoEstablecimiento;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Distritos;
import union.union_vr1.Sqlite.DbAdapter_Establecimeinto_Historial;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.VMovil_Menu_Establec;

/**
 * Created by Kelvin on 04/11/2015.
 */
public class FMapaEditar extends Fragment implements Validator.ValidationListener {

    // private RadioButton buttonHibrido;
    // private RadioButton buttonNormal;
    private DbAdapter_Establecimeinto_Historial dbAdapter_establecimeinto_historial;
    private View v;
    boolean estadoPosicion = true;
    private boolean estadoFinalizar = false;
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
    private AutoCompleteTextView completeTextView;
    @NumberRule(order = 3, type = NumberRule.NumberType.DOUBLE, messageResId = R.string.requerido_input)
    private EditText textLat;
    @NumberRule(order = 4, type = NumberRule.NumberType.DOUBLE, messageResId = R.string.requerido_input)
    private EditText textLon;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private String idDistrito = "";
    private DBAdapter_Distritos dbAdapter_distritos;

    private DbAdapter_Establecimeinto_Historial dbAdapter_temp_establecimiento;
    private static final String TAG = FMapaEditar.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);
        estadoDF = true;
        myLocation = null;
        //MapsInitializer.initialize(getActivity());
       /* mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();*/
        idEstablecimiento = getArguments().getString("idEstablecimiento");

        dbAdapter_temp_establecimiento = new DbAdapter_Establecimeinto_Historial(getActivity());
        dbAdapter_temp_establecimiento.open();
        dbAdapter_establecimeinto_historial = new DbAdapter_Establecimeinto_Historial(getActivity());
        dbAdapter_establecimeinto_historial.open();
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(getActivity());
        dbAdaptert_evento_establec.open();
        dbAdapter_distritos = new DBAdapter_Distritos(getActivity());
        dbAdapter_distritos.open();

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
        completeTextView = (AutoCompleteTextView) v.findViewById(R.id.distrito);

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
        actualizar();
        return v;
    }

    private void autoComplete(String id) {
        idDistrito = id;

        String dis = dbAdapter_distritos.listarDistritosName(id);
        completeTextView.setText(dis);
        Cursor cr = dbAdapter_distritos.listarDistritosLikesss(id);

        String[] columnas = new String[]{
                DBAdapter_Distritos.Dis_descripcion
        };
        int[] to = new int[]{
                R.id.textNombre

        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.layout_distrito,
                cr,
                columnas,
                to,
                0);

        completeTextView.setAdapter(adapter);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                Cursor cr = dbAdapter_distritos.listarDistritosLike(charSequence.toString());
                return cr;
            }
        });
        completeTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                idDistrito = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Distritos.Dis_IdDistrito));
                Log.d(TAG, idDistrito + "-" + cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Distritos.Dis_descripcion)));
                completeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Distritos.Dis_descripcion)));
            }
        });

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
            textLat.setText(location.getLatitude() + "");
            textLon.setText(location.getLongitude() + "");
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
        Cursor cr = dbAdapter_temp_establecimiento.fetchTemEstablecById(idEstablecimiento);

        if (cr.moveToFirst()) {
            String urlMap = "http://maps.google.com/maps/api/staticmap?center=" + cr.getDouble(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_latitud)) + "," + cr.getDouble(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_longitud)) + ",&zoom=17&markers=icon:http://www.myiconfinder.com/uploads/iconsets/256-256-a5485b563efc4511e0cd8bd04ad0fe9e.png|" + cr.getDouble(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_latitud)) + "," + cr.getDouble(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_longitud)) + "&path=color:0x0000FF80|weight:5|" + cr.getDouble(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_latitud)) + "," + cr.getDouble(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_longitud)) + "&size=400x300";//
            webViewMap.loadUrl(urlMap);
            editTextDescripcion.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion)));
            editTextDireccionFiscal.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_direccion_fiscal)));
            autoComplete(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_distrito)));

        }


        checkBoxDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!estadoDF) {

                    editTextDireccionFiscal.setText("");
                    estadoDF = true;
                } else {

                    editTextDireccionFiscal.setText(editTextDescripcion.getText().toString());
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
        super.onResume();


    }


    @Override
    public void onValidationSucceeded() {
        String lat = "", lon = "", direccion = "", direccion_fiscal = "";

        lat = textLat.getText().toString();
        lon = textLon.getText().toString();
        direccion = editTextDescripcion.getText().toString();
        direccion_fiscal = editTextDireccionFiscal.getText().toString();
        if (!lat.equals("Obteniendo...") || !lon.equals("Obteniendo...") || idDistrito != "") {

            long estado = dbAdapter_temp_establecimiento.updateTempEstablecDireccion(idEstablecimiento + "", lat, lon, direccion, direccion_fiscal,idDistrito);
            if (estado > 0) {
                if (estadoFinalizar) {
                    guardar();
                } else {
                    viewPager.setCurrentItem(2);
                }


            } else {
                Utils.setToast(getActivity(), "Ocurrio un error, por favor sal y vuelve a Intentarlo", R.color.rojo);
            }

        } else {
            Utils.setToast(getActivity(), "Por favor espere obteniedno posicion", R.color.rojo);
        }

    }

    private void guardar() {
        Cursor cursor = dbAdapter_establecimeinto_historial.fetchTemEstablecEdit(idEstablecimiento);

        while (cursor.moveToNext()) {

            EventoEstablecimiento eventoEstablecimiento = new EventoEstablecimiento(
                    Integer.parseInt(idEstablecimiento),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_categoria_estable)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_documento)),
                    1,
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion_establecimiento
                    )),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nombres)) + " " +
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apPaterno)) + " " +
                            cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apMaterno)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nro_documento)),
                    0,
                    0,
                    0,
                    0.0,
                    0,
                    0,
                    "",
                    0,
                    Constants._CREADO,
                    "",
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion)),
                    Constants.REGISTRO_SIN_INTERNET,
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_direccion_fiscal)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_latitud)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_longitud))
            );
            int upd = dbAdaptert_evento_establec.updateEstablecimientosEditar(eventoEstablecimiento);
            Log.d("EDITO", "" + upd);
        }


        startActivity(new Intent(getActivity().getApplicationContext(), VMovil_Menu_Establec.class));
        getActivity().finish();
    }

    //
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_establec:
                estadoFinalizar = true;
                validator.validate();
                break;
        }

        return false;
    }


}
