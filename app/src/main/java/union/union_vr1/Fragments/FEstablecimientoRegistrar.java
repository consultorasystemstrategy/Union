package union.union_vr1.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Temp_DatosSpinner;

/**
 * Created by Kelvin on 05/11/2015.
 */
public class FEstablecimientoRegistrar extends Fragment {
    private Spinner spinnerTipoEstablecimeinto;
    private Spinner spinnerCategoriaEstablecimeinto;
    private EditText editTextNombre;
    private EditText editTextTelFijo;
    private EditText editTextTelMovil2;
    private View v;
    private ArrayList<String> stringArrayList = new ArrayList<>();
    private ArrayList<String> stringArrayListCategoria = new ArrayList<>();
    private DbAdapter_Temp_DatosSpinner dbAdapter_temp_datosSpinner;
    private Button buttonFinalizar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_establecimiento, container, false);
        dbAdapter_temp_datosSpinner = new DbAdapter_Temp_DatosSpinner(getActivity().getApplicationContext());
        dbAdapter_temp_datosSpinner.open();
        spinnerTipoEstablecimeinto = (Spinner) v.findViewById(R.id.spinnerTipoEstablecimiento);
        spinnerCategoriaEstablecimeinto = (Spinner) v.findViewById(R.id.spinnerCategoriaEstablecimiento);
        editTextNombre = (EditText)v.findViewById(R.id.editDescripcion);
        editTextTelFijo = (EditText)v.findViewById(R.id.editTelFijo);
        editTextTelMovil2 = (EditText)v.findViewById(R.id.editNumero2);

        callmehod();

        return v;
    }

    private void callmehod() {

        try {
            setSpinnerEstablec();
            setSpinnerCategoriaEstablec();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSpinnerEstablec() throws JSONException {
        Cursor cursor = dbAdapter_temp_datosSpinner.fetchTemSpinnerTipo(1);
        cursor.moveToFirst();
        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
        JSONArray jsonArray = jsonObject.getJSONArray("Value");
        JSONObject jsonObjectChiild = null;
        stringArrayList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObjectChiild = jsonArray.getJSONObject(i);
            stringArrayList.add(i, jsonObjectChiild.getString("TieVDescripcion"));

        }
        ArrayAdapter<String> adapterTipoIdentidad = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.layout_item_spinner, stringArrayList);
        adapterTipoIdentidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoEstablecimeinto.setAdapter(adapterTipoIdentidad);
    }

    private void setSpinnerCategoriaEstablec() throws JSONException {
        Cursor cursor = dbAdapter_temp_datosSpinner.fetchTemSpinnerTipo(4);
        cursor.moveToFirst();
        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
        JSONArray jsonArray = jsonObject.getJSONArray("Value");
        JSONObject jsonObjectChiild = null;
        stringArrayListCategoria.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObjectChiild = jsonArray.getJSONObject(i);
            stringArrayListCategoria.add(i, jsonObjectChiild.getString("CateVDescripcion"));

        }
        ArrayAdapter<String> adapterTipoIdentidad = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.layout_item_spinner, stringArrayListCategoria);
        adapterTipoIdentidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaEstablecimeinto.setAdapter(adapterTipoIdentidad);
    }





}

