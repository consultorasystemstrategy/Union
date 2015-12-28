package union.union_vr1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
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
    private ImageView imageViewIcono;
    public EditText editTextDescripcion;
    public EditText editTextDireccionFiscal;
    public Marker markerEstablecimeinto;
    int anInt = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map, container, false);

        MapsInitializer.initialize(getActivity());
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        editTextDescripcion = (EditText)v.findViewById(R.id.map_descripcion);
        editTextDireccionFiscal = (EditText)v.findViewById(R.id.map_direccion_fiscal);
        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(false);
        map.setMyLocationEnabled(true);
        buttonHibrido = (RadioButton) v.findViewById(R.id.mapHibrido);
        buttonNormal = (RadioButton) v.findViewById(R.id.mapNormal);
        imageViewIcono = (ImageView)v.findViewById(R.id.imgFiscal);
        eventButtons();
        linearLayoutDetalle = (LinearLayout) v.findViewById(R.id.layoutflag);
        setMyLocation();
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
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

        imageViewIcono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descr = editTextDescripcion.getText().toString();
                editTextDireccionFiscal.setText(descr);
            }
        });


        // displayTouchLayout();
        return v;
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
        try {
            Log.d("EVENTOS  ","RESUMEN");
            SharedPreferences getDataMapa = getActivity().getSharedPreferences("INFORMACION_MAPA", Context.MODE_PRIVATE);
            double latitud=Double.parseDouble(getDataMapa.getString("latitud", null));
            double longitud=Double.parseDouble(getDataMapa.getString("longitud", null));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud), 17);
            map.animateCamera(cameraUpdate);
            location(new LatLng(latitud,longitud));
        }catch (NullPointerException e){

        }
        catch (NumberFormatException e){

        }


    }
    public Marker getMarkerEstablecimeinto (){
        return markerEstablecimeinto;
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



        try {

            final double finalLon = map.getMyLocation().getLatitude();
            final double finalLat = map.getMyLocation().getLongitude();
            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    location = map.getMyLocation();
                    location(new LatLng(finalLat, finalLon));
                    return false;
                }
            });


        }catch (NullPointerException e){

            try {
                Log.d("EVENTOS  ","RESUMEN");
                SharedPreferences getDataMapa = getActivity().getSharedPreferences("INFORMACION_MAPA", Context.MODE_PRIVATE);
                double latitud=Double.parseDouble(getDataMapa.getString("latitud", null));
                double longitud=Double.parseDouble(getDataMapa.getString("longitud", null));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud), 17);
                map.animateCamera(cameraUpdate);
                location(new LatLng(latitud,longitud));
            }catch (NullPointerException v){

            }
            catch (NumberFormatException v){

            }

        }
    }
    public void location(LatLng location){
        try {

            new GetAddress(getActivity()).execute("", location.latitude + "", location.longitude + "");
            map.clear();
            canEstablec = canEstablec + 1;
            Log.d("LATLONG", "Lat: " + location.latitude + " , Long: " + location.longitude + " Direccion: ");
            markerEstablecimeinto = map.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("Lat: " + location.latitude + " , Long: " + location.longitude));
            SharedPreferences.Editor editor =getActivity().getSharedPreferences("INFORMACION_MAPA", Context.MODE_PRIVATE).edit();
            editor.putString("latitud",  location.latitude+"");
            editor.putString("longitud", location.longitude+"");
            editor.commit();

        }catch (NullPointerException e){
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }


}
