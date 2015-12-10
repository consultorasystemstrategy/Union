package union.union_vr1.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    private View v;
    private ArrayList<String> stringArrayList  = new ArrayList<>();
    private DbAdapter_Temp_DatosSpinner dbAdapter_temp_datosSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_establecimiento, container, false);
        dbAdapter_temp_datosSpinner= new DbAdapter_Temp_DatosSpinner(getActivity().getApplicationContext());
        dbAdapter_temp_datosSpinner.open();
        spinnerTipoEstablecimeinto = (Spinner) v.findViewById(R.id.spinnerTipoEstablecimiento);
        try {
            setSpinnerEstablec();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }
    private void setSpinnerEstablec() throws JSONException {
        Cursor cursor = dbAdapter_temp_datosSpinner.fetchTemSpinnerTipo(1);
        cursor.moveToFirst();
        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
        JSONArray jsonArray = jsonObject.getJSONArray("Value");
        JSONObject jsonObjectChiild=null;
        stringArrayList.clear();
        for(int i=0;i<jsonArray.length();i++){
            jsonObjectChiild = jsonArray.getJSONObject(i);
            stringArrayList.add(i,jsonObjectChiild.getString("TieVDescripcion"));

        }
        ArrayAdapter<String> adapterTipoIdentidad=  new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.layout_item_spinner,stringArrayList);
        adapterTipoIdentidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoEstablecimeinto.setAdapter(adapterTipoIdentidad);
    }
}
