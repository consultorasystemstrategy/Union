package union.union_vr1.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Temp_DatosSpinner;

/**
 * Created by Kelvin on 04/11/2015.
 */
public class FClienteRegistrar extends Fragment {
    private Spinner spinnerTipoDocumento;
    private Spinner spinnerTipoPesona;
    //
    private EditText editTextNombre;
    private EditText editTextApPaterno;
    private EditText editTextApMaterno;
    private EditText editTextNroDocumento;
    private EditText editTextNroCelular;
    private EditText editTextCorreo;


    private View v;
    private DbAdapter_Temp_DatosSpinner dbAdapter_temp_datosSpinner;
    private ArrayList<String> stringArrayListPersona = new ArrayList<>();
    private ArrayList<String> stringArrayListDocIdentidad = new ArrayList<>();
    private JSONObject jsonObjectChiildIdentidad = null;
    private JSONObject jsonObjectPersona = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cliente, container, false);
        dbAdapter_temp_datosSpinner = new DbAdapter_Temp_DatosSpinner(getActivity().getApplicationContext());
        dbAdapter_temp_datosSpinner.open();
        spinnerTipoPesona = (Spinner) v.findViewById(R.id.spinnerTipoPersona);
        spinnerTipoDocumento = (Spinner) v.findViewById(R.id.spinnerTipoDocumento);
        editTextNombre = (EditText) v.findViewById(R.id.editNombre);
        editTextApPaterno = (EditText) v.findViewById(R.id.editApPaterno);
        editTextApMaterno = (EditText) v.findViewById(R.id.editApMaterno);
        editTextNroDocumento = (EditText) v.findViewById(R.id.editNroDocumento);
        editTextNroCelular = (EditText) v.findViewById(R.id.editNroCelular);
        editTextCorreo = (EditText) v.findViewById(R.id.editCorreo);
        callmethod();
        return v;
    }

    private void callmethod() {

        try {
            setSpinnerDocIdentidad();
            setSpinnerPersona();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("ERROR:c", "" + e.getMessage());
        }
    }


    private void setSpinnerDocIdentidad() throws JSONException {
        Cursor cursor = dbAdapter_temp_datosSpinner.fetchTemSpinnerTipo(2);
        cursor.moveToFirst();
        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
        JSONArray jsonArray = jsonObject.getJSONArray("Value");

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObjectChiildIdentidad = jsonArray.getJSONObject(i);
            stringArrayListDocIdentidad.add(i, jsonObjectChiildIdentidad.getString("TdiVDescripcion"));

        }
        ArrayAdapter<String> adapterTipoIdentidad = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.layout_item_spinner, stringArrayListDocIdentidad);
        adapterTipoIdentidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDocumento.setAdapter(adapterTipoIdentidad);
    }

    private void setSpinnerPersona() throws JSONException {
        Cursor cursor = dbAdapter_temp_datosSpinner.fetchTemSpinnerTipo(3);
        cursor.moveToFirst();
        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
        JSONArray jsonArray = jsonObject.getJSONArray("Value");

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObjectPersona = jsonArray.getJSONObject(i);
            stringArrayListPersona.add(i, jsonObjectPersona.getString("TperVDescripcion"));

        }
        ArrayAdapter<String> adapterTipoPersona = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.layout_item_spinner, stringArrayListPersona);
        adapterTipoPersona.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoPesona.setAdapter(adapterTipoPersona);
    }


}
