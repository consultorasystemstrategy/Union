package union.union_vr1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DBAdapter_Cliente_Ruta;
import union.union_vr1.Sqlite.DbAdapter_Temp_DatosSpinner;
import union.union_vr1.Vistas.VMovil_Facturas_Canjes_Dev;

/**
 * Created by Kelvin on 04/11/2015.
 */
public class FClienteRegistrar extends Fragment {
    private Spinner spinnerTipoDocumento;
    private Spinner spinnerTipoPesona;
    private DBAdapter_Cliente_Ruta dbAdapter_cliente_ruta;
    private int tipoDoc;
    private EditText editTextNombre;
    private EditText editTextApPaterno;
    private EditText editTextApMaterno;
    private AutoCompleteTextView autoNroDocumento;
    private EditText editTextNroCelular;
    private EditText editTextCorreo;
    Toast toastBucsando;
    Toast toast;

    private View v;
    private DbAdapter_Temp_DatosSpinner dbAdapter_temp_datosSpinner;
    private ArrayList<String> stringArrayListPersona = new ArrayList<>();
    private ArrayList<String> stringArrayListDocIdentidad = new ArrayList<>();
    private JSONObject jsonObjectChiildIdentidad = null;
    private JSONObject jsonObjectPersona = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cliente, container, false);
        toastBucsando= Toast.makeText(getActivity().getApplicationContext(), "Buscando...", 1000);
        toast = Toast.makeText(getActivity().getApplicationContext(), "Encontrado", Toast.LENGTH_SHORT);
        toast.getView().setBackgroundColor(getActivity().getResources().getColor(R.color.verde));
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        TextView vz = (TextView) toast.getView().findViewById(android.R.id.message);
        vz.setTextColor(getActivity().getResources().getColor(R.color.Blanco));
        //-.....
        toastBucsando.getView().setBackgroundColor(getActivity().getResources().getColor(R.color.amarillo));
        toastBucsando.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        TextView va = (TextView) toastBucsando.getView().findViewById(android.R.id.message);
        va.setTextColor(getActivity().getResources().getColor(R.color.Blanco));
        dbAdapter_temp_datosSpinner = new DbAdapter_Temp_DatosSpinner(getActivity().getApplicationContext());
        dbAdapter_temp_datosSpinner.open();
        dbAdapter_cliente_ruta = new DBAdapter_Cliente_Ruta(getActivity().getApplicationContext());
        dbAdapter_cliente_ruta.open();
        spinnerTipoPesona = (Spinner) v.findViewById(R.id.spinnerTipoPersona);
        spinnerTipoDocumento = (Spinner) v.findViewById(R.id.spinnerTipoDocumento);
        editTextNombre = (EditText) v.findViewById(R.id.editNombre);
        editTextApPaterno = (EditText) v.findViewById(R.id.editApPaterno);
        editTextApMaterno = (EditText) v.findViewById(R.id.editApMaterno);
        autoNroDocumento = (AutoCompleteTextView) v.findViewById(R.id.editNroDocumento);
        editTextNroCelular = (EditText) v.findViewById(R.id.editNroCelular);
        editTextCorreo = (EditText) v.findViewById(R.id.editCorreo);
        callmethod();
        spinnerTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoDoc = tipo_Documento(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        autoComplete();
        return v;
    }

    private void autoComplete() {


        final SimpleCursorAdapter adapter;
        Cursor cr = dbAdapter_cliente_ruta.listarDocumentoClientexRuta("", tipoDoc);
        cr.moveToFirst();


        String[] columnas = new String[]{
                cr.getColumnName(5),


        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.textIdNumeroC,

        };

        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.layout_item_cliente_documento,
                cr,
                columnas,
                to,
                0);


        autoNroDocumento.setAdapter(adapter);
        autoNroDocumento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Boolean estado = dbAdapter_cliente_ruta.existeClienteRuta(editable.toString(), tipoDoc);
                Log.d("ESTADOEXISTERUTA", "" + estado);
                if (estado) {
                    toastBucsando.cancel();

                    toast.show();


                    disableWidgets();

                } else {

                    try {
                        toast.cancel();

                        toastBucsando.show();
                        setSpinnerDocIdentidad();
                        enableWidgets();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                Cursor cr = dbAdapter_cliente_ruta.listarDocumentoClientexRuta(charSequence.toString(), tipoDoc);
                return cr;
            }
        });


        autoNroDocumento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toastBucsando.cancel();
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Encontrado", Toast.LENGTH_SHORT);
                toast.getView().setBackgroundColor(getActivity().getResources().getColor(R.color.verde));
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                TextView vz = (TextView) toast.getView().findViewById(android.R.id.message);
                vz.setTextColor(getActivity().getResources().getColor(R.color.Blanco));
                toast.show();
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                String nom = "";
                cursor.moveToFirst();
                nom = cursor.getString(5);
                autoNroDocumento.setText(nom);
                setValueForm(cursor);

            }
        });


    }

    private void disableWidgets() {
        spinnerTipoPesona.setEnabled(false);
        editTextNombre.setEnabled(false);
        editTextApPaterno.setEnabled(false);
        editTextApMaterno.setEnabled(false);
        editTextNroCelular.setEnabled(false);
        editTextCorreo.setEnabled(false);
    }

    private void enableWidgets() {
        spinnerTipoPesona.setEnabled(true);
        editTextNombre.setEnabled(true);
        editTextApPaterno.setEnabled(true);
        editTextApMaterno.setEnabled(true);
        editTextNroCelular.setEnabled(true);
        editTextCorreo.setEnabled(true);
    }

    private void setValueForm(Cursor cr) {
        cr.moveToFirst();
        int tipoPersona = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_tipo_PerIdentidad));
        String TipoPersona = tipo_DocumentoById(tipoPersona);
        String nombres = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_nombres));
        String apPaterno = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_apPaterno));
        String apMaterno = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_apMaterno));
        String celular = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_celular));
        Log.d("CELULAR", "" + celular);
        String correo = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_email));
        stringArrayListPersona = new ArrayList<String>();

        stringArrayListPersona.add(TipoPersona);

        ArrayAdapter<String> adapterTipoPersona = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.layout_item_spinner, stringArrayListPersona);
        adapterTipoPersona.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoPesona.setAdapter(adapterTipoPersona);

        editTextNombre.setText(nombres);
        editTextApPaterno.setText(apPaterno);
        editTextApMaterno.setText(apMaterno);
        editTextNroCelular.setText(celular);
        editTextCorreo.setText(correo);
        disableWidgets();


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
        spinnerTipoDocumento = (Spinner)v.findViewById(R.id.spinnerTipoDocumento);
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
        spinnerTipoPesona = (Spinner)v.findViewById(R.id.spinnerTipoPersona);
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


    private int tipo_Documento(int position) {
        Cursor cursor = dbAdapter_temp_datosSpinner.fetchTemSpinnerTipo(2);
        cursor.moveToFirst();
        int id = 0;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
            JSONArray jsonArray = jsonObject.getJSONArray("Value");
            id = jsonArray.getJSONObject(position).getInt("TdiTipoDocIdentidadId");
            Log.d("TIPO DOCUMENTO", "" + jsonArray.get(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return id;
    }

    private String tipo_DocumentoById(int idTipo) {
        Cursor cursor = dbAdapter_temp_datosSpinner.fetchTemSpinnerTipo(3);
        cursor.moveToFirst();
        String id = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
            JSONArray jsonArray = jsonObject.getJSONArray("Value");

            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getInt("TperITipoPersonaId") == idTipo) {
                    id = jsonArray.getJSONObject(i).getString("TperVDescripcion");
                }
                ;
                Log.d("TIPO DOCUMENTO", "" + jsonArray.get(i).toString());
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }
        return id;
    }


}
