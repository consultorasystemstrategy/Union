package union.union_vr1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import union.union_vr1.AsyncTask.GetAddress;
import union.union_vr1.R;

/**
 * Created by Kelvin on 04/11/2015.
 */
public class FMapaRegistrar extends Fragment {
    private RadioButton buttonHibrido;
    private RadioButton buttonNormal;
    private View v;
    private MapView mapView;
    private GoogleMap map;
    private int canEstablec;
    private Location location;
    private LinearLayout linearLayoutDetalle;
    boolean estado = false;
    public EditText editTextDescripcion;
    public EditText editTextDireccionFiscal;
    public Marker markerEstablecimeinto;
    int anInt = 0;
public FMapaRegistrar (){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map, container, false);
        MapsInitializer.initialize(getActivity());
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        mapView.onResume();
        editTextDescripcion = (EditText)v.findViewById(R.id.map_descripcion);
        editTextDireccionFiscal = (EditText)v.findViewById(R.id.map_descripcion);
        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(false);
        map.setMyLocationEnabled(true);
        buttonHibrido = (RadioButton) v.findViewById(R.id.mapHibrido);
        buttonNormal = (RadioButton) v.findViewById(R.id.mapNormal);
        eventButtons();
        linearLayoutDetalle = (LinearLayout) v.findViewById(R.id.layoutflag);
        setMyLocation();
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {


                anInt = anInt + 1;
                if (anInt == 1) {
                    map.clear();
                    location(location);
                }


            }
        });


        displayTouchLayout();
        return v;
    }

    public boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private void displayTouchLayout() {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // id_inserto =


                    if (estado) {
                        estado = false;
                        linearLayoutDetalle.setVisibility(View.VISIBLE);
                    } else {
                        estado = true;
                        linearLayoutDetalle.setVisibility(View.INVISIBLE);
                    }


                return false;
            }
        });
    }

    private void eventButtons() {
        buttonHibrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        //------------
        buttonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
    }

    private void setMyLocation() {

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                location = map.getMyLocation();
                  location(location);
                return false;
            }
        });
    }
    public void location(Location location){
        try {

            new GetAddress(getActivity()).execute("", location.getLatitude() + "", location.getLongitude() + "");
            map.clear();
            canEstablec = canEstablec + 1;
            Log.d("LATLONG", "Lat: " + location.getLatitude() + " , Long: " + location.getLongitude() + " Direccion: ");
            markerEstablecimeinto = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Lat: " + location.getLatitude() + " , Long: " + location.getLongitude()));


        }catch (NullPointerException e){
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }


    public String getAddress(Context ctx, Location location) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address>
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String locality = address.getLocality();
                String city = address.getCountryName();
                String region_code = address.getCountryCode();
                String zipcode = address.getSubLocality();

                result.append(address.getAddressLine(0));
                result.append(address.getPremises());
                result.append(address.getAdminArea());
                result.append(address.getSubAdminArea());
                result.append(address.getFeatureName());
                result.append(address.getLocale().getDisplayName());
                result.append(address.getSubThoroughfare());
                Log.d("DIRECCION", result.toString());

            }
        } catch (IOException e) {
            Log.e("DIRECCION", e.getMessage());
        }

        return result.toString();
    }
}
