package union.union_vr1.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
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

import union.union_vr1.R;
import union.union_vr1.Sqlite.DBAdapter_Cliente_Ruta;
import union.union_vr1.Sqlite.DbAdapter_Categoria_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Temp_DatosSpinner;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Doc_Identidad;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Persona;

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
    private DbAdapter_Tipo_Doc_Identidad dbAdapter_tipo_doc_identidad;
    private DbAdapter_Tipo_Persona dbAdapter_tipo_persona;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cliente, container, false);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;

            }
        });
        toastBucsando= Toast.makeText(getActivity().getApplicationContext(), "Buscando...", 1000);
        toast = Toast.makeText(getActivity().getApplicationContext(), "Encontrado", Toast.LENGTH_SHORT);
        toast.getView().setBackgroundColor(getActivity().getResources().getColor(R.color.verde));
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        TextView vz = (TextView) toast.getView().findViewById(android.R.id.message);
        vz.setTextColor(getActivity().getResources().getColor(R.color.Blanco));
        //...........


        dbAdapter_tipo_doc_identidad = new DbAdapter_Tipo_Doc_Identidad(getActivity());
        dbAdapter_tipo_persona = new DbAdapter_Tipo_Persona(getActivity());
        dbAdapter_tipo_doc_identidad.open();
        dbAdapter_tipo_persona.open();
        //-.....
        toastBucsando.getView().setBackgroundColor(getActivity().getResources().getColor(R.color.amarillo));
        toastBucsando.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        TextView va = (TextView) toastBucsando.getView().findViewById(android.R.id.message);
        va.setTextColor(getActivity().getResources().getColor(R.color.Blanco));
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
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(i);
                tipoDoc=cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Tipo_Doc_Identidad.tipo_Doc_IdentidadId));
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
                if(charSequence.toString().equals("")){
                   // toastBucsando.cancel();
                  //  toast.show();
                    spinnerTipoDocumento.setEnabled(true);
                    enableWidgets();
                    setAdapterTipoDocIdentidad();
                    setAdapterTipoPersona(-1);

                }
                Boolean estado = dbAdapter_cliente_ruta.existeClienteRuta(charSequence.toString(), tipoDoc);
                Log.d("ESTADOEXISTERUTA", "" + estado);
                if (estado) {
                    // toastBucsando.cancel();
                    // toast.show();
                    spinnerTipoDocumento.setEnabled(false);
                    disableWidgets();

                } else {

                    // toast.cancel();
                    // toastBucsando.show();
                    spinnerTipoDocumento.setEnabled(true);
                    enableWidgets();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {



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



        editTextNombre.setText("");
        editTextApPaterno.setText("");
        editTextApMaterno.setText("");
        editTextNroCelular.setText("");
        editTextCorreo.setText("");
    }

    private void setValueForm(Cursor cr) {
        cr.moveToFirst();
        int tipoPersona = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_tipo_PerIdentidad));
        setAdapterTipoPersona(tipoPersona);
        String nombres = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_nombres));
        String apPaterno = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_apPaterno));
        String apMaterno = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_apMaterno));
        String celular = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_celular));
        Log.d("CELULAR", "" + celular);
        String correo = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_email));


        editTextNombre.setText(nombres);
        editTextApPaterno.setText(apPaterno);
        editTextApMaterno.setText(apMaterno);
        editTextNroCelular.setText(celular);
        editTextCorreo.setText(correo);
        disableWidgets();


    }

    private void callmethod() {
        setAdapterTipoDocIdentidad();
        setAdapterTipoPersona(-1);

    }
    private void setAdapterTipoDocIdentidad(){
        Cursor cr = dbAdapter_tipo_doc_identidad.fetchTipoDocIden();
        SimpleCursorAdapter simpleCursorAdapter ;
        int[] to = new int[]{
                android.R.id.text1,

        };

        String[] columns = new String[]{
                DbAdapter_Tipo_Doc_Identidad.tipo_Doc_Descripcion,

        };
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),android.R.layout.simple_dropdown_item_1line,cr,columns,to,0);
        spinnerTipoDocumento.setAdapter(simpleCursorAdapter);
    }

    private void setAdapterTipoPersona(int id){
        Cursor cr = dbAdapter_tipo_persona.fetchTipoPersona(id);
        SimpleCursorAdapter simpleCursorAdapter ;
        int[] to = new int[]{
                android.R.id.text1,

        };

        String[] columns = new String[]{
                DbAdapter_Tipo_Persona.tipo_Persona_Descripcion,

        };
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),android.R.layout.simple_dropdown_item_1line,cr,columns,to,0);
        spinnerTipoPesona.setAdapter(simpleCursorAdapter);
    }












}
