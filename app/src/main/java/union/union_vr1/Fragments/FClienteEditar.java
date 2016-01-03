package union.union_vr1.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DBAdapter_Cliente_Ruta;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Doc_Identidad;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Persona;

/**
 * Created by Kelvin on 04/11/2015.
 */
public class FClienteEditar extends Fragment {
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
    private DbAdapter_Temp_Establecimiento dbAdapter_temp_establecimiento;
    Toast toastBucsando;
    Toast toast;

    String idEstablecimiento ="";


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
        dbAdapter_temp_establecimiento = new DbAdapter_Temp_Establecimiento(getActivity());
        dbAdapter_temp_establecimiento.open();
        idEstablecimiento = getArguments().getString("idEstablecimiento");
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

        display();

    }



    private void display() {
        Cursor cr = dbAdapter_temp_establecimiento.fetchTemEstablecById(idEstablecimiento);
        if(cr.moveToFirst()){
            //spinnerTipoPesona.setEnabled(false);

            editTextNombre.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nombres)));
            autoNroDocumento.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_nro_documento)));
            editTextApPaterno.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_apPaterno)));
            editTextApMaterno.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_apMaterno)));
            editTextNroCelular.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_one)));
            editTextCorreo.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_correo)));

        }

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
        simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoPesona.setAdapter(simpleCursorAdapter);
    }












}
