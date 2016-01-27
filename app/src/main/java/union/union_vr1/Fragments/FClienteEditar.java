package union.union_vr1.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;

import union.union_vr1.Objects.EventoEstablecimiento;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Cliente_Ruta;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Establecimeinto_Historial;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Doc_Identidad;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Persona;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.VMovil_Menu_Establec;

/**
 * Created by Kelvin on 04/11/2015.
 */
public class FClienteEditar extends Fragment implements Validator.ValidationListener {
    //
    private DBAdapter_Cliente_Ruta dbAdapter_cliente_ruta;
    private DbAdapter_Establecimeinto_Historial dbAdapter_establecimeinto_historial;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private int tipoDoc;
    private boolean estadoFinalizar = false;
    //VALIDACION DE LOS WIDGETS
    private Spinner spinnerTipoDocumento;
    private Spinner spinnerTipoPesona;
    private Button btn;

    @Required(order = 1, messageResId = R.string.requerido_input)
    private EditText editTextNombre;


    private EditText editTextApPaterno;
    private EditText editTextApMaterno;

    @Required(order = 2, messageResId = R.string.requerido_input)
    private AutoCompleteTextView autoNroDocumento;

    private EditText editTextNroCelular;

    @Email(order = 3, messageResId = R.string.requerido_correo)
    private EditText editTextCorreo;


    Toast toastBucsando;
    Toast toast;
    private ViewPager viewPager;
    private TextView textViewDoc;
    private TextView textCliente;

    private LinearLayout linearLayoutPadre;
    private LinearLayout linearLayoutMadre;

    private View v;
    private DbAdapter_Tipo_Doc_Identidad dbAdapter_tipo_doc_identidad;
    private DbAdapter_Tipo_Persona dbAdapter_tipo_persona;
    private DbAdapter_Establecimeinto_Historial db_adapter_historial_establecimient;
    private DbAdapter_Agente dbAdapter_agente;
    private Validator validator;

    String idEstablecimiento;
    int idusuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cliente, container, false);
        setHasOptionsMenu(true);
        idEstablecimiento = getArguments().getString("idEstablecimiento");

        Log.d("ESTABLECIMIENTO CLIENTE REGISTRAR", "" + idEstablecimiento);

        toastBucsando = Toast.makeText(getActivity().getApplicationContext(), "Buscando...", 1000);
        toast = Toast.makeText(getActivity().getApplicationContext(), "Encontrado", Toast.LENGTH_SHORT);
        toast.getView().setBackgroundColor(getActivity().getResources().getColor(R.color.verde));
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        TextView vz = (TextView) toast.getView().findViewById(android.R.id.message);
        vz.setTextColor(getActivity().getResources().getColor(R.color.Blanco));
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        btn = (Button) v.findViewById(R.id.btnValidarCliente);
        btn.setVisibility(View.GONE);
        //...........
        //VALIDATOR INTANCE
        validator = new Validator(this);
        validator.setValidationListener(this);
        //--------------
        dbAdapter_establecimeinto_historial = new DbAdapter_Establecimeinto_Historial(getActivity());
        dbAdapter_establecimeinto_historial.open();
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(getActivity());
        dbAdaptert_evento_establec.open();


        dbAdapter_tipo_doc_identidad = new DbAdapter_Tipo_Doc_Identidad(getActivity());
        dbAdapter_tipo_persona = new DbAdapter_Tipo_Persona(getActivity());
        dbAdapter_tipo_doc_identidad.open();
        dbAdapter_tipo_persona.open();
        db_adapter_historial_establecimient = new DbAdapter_Establecimeinto_Historial(getActivity());
        db_adapter_historial_establecimient.open();
        dbAdapter_agente = new DbAdapter_Agente(getActivity());
        dbAdapter_agente.open();
        idusuario = dbAdapter_agente.getIdUsuario();
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
        textViewDoc = (TextView) v.findViewById(R.id.textDocumento);
        textCliente = (TextView) v.findViewById(R.id.textViewNombreCliente);

        linearLayoutMadre = (LinearLayout) v.findViewById(R.id.layoutMaterno);
        linearLayoutPadre = (LinearLayout) v.findViewById(R.id.layoutPaterno);

        callmethod();
        actualizar();

        spinnerTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                tipoDoc = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Tipo_Doc_Identidad.tipo_Doc_IdentidadId));

                Log.d("TIPO DOC", "" + tipoDoc);
                String tipo = "";
                switch (tipoDoc) {
                    case 1:
                        tipo = "DNI";
                        break;
                    case 2:
                        tipo = "RUC";
                        break;
                }
                textViewDoc.setText("Nro de " + tipo);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTipoPesona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                int tipoCliente = cursor.getInt(cursor.getColumnIndex(dbAdapter_tipo_persona.tipo_Persona_PersonaId));
                Log.d("TIPO CLIENTE", "" + tipoCliente);
                hideWifgets(tipoCliente);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        display();
        return v;
    }

    private void display() {
        Cursor cr = db_adapter_historial_establecimient.fetchTemEstablecById(idEstablecimiento);
        Log.d("DATOS", "" + cr.getCount());
        if (cr.moveToFirst()) {

            int tipoPersona = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_persona));
            int tipoDoc = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_documento));
            setAdapterTipoPersona(tipoPersona);
            setAdapterTipoDocIdentidad(tipoDoc);
            if (tipoPersona > 2) {

                textCliente.setText("Razon Social");
                linearLayoutMadre.setVisibility(View.GONE);
                linearLayoutPadre.setVisibility(View.GONE);

            }


            editTextNombre.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nombres)));
            autoNroDocumento.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nro_documento)));
            editTextApPaterno.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apPaterno)));
            editTextApMaterno.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apMaterno)));
            editTextNroCelular.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_celular_one)));
            editTextCorreo.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_correo)));

        }

    }

    private void hideWifgets(int tipo) {

        switch (tipo) {
            case 1:
                setAdapterTipoDocIdentidad(-1);
                textCliente.setText("Nombres");
                linearLayoutMadre.setVisibility(View.VISIBLE);
                linearLayoutPadre.setVisibility(View.VISIBLE);
                break;
            case 2:
                setAdapterTipoDocIdentidad(2);
                textCliente.setText("Razon Social");
                linearLayoutMadre.setVisibility(View.GONE);
                linearLayoutPadre.setVisibility(View.GONE);
                break;

        }

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
                        validator.validate();

                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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


    private void callmethod() {
        setAdapterTipoDocIdentidad(-1);
        setAdapterTipoPersona(-1);

    }

    private void setAdapterTipoDocIdentidad(int id) {
        Cursor cr = dbAdapter_tipo_doc_identidad.fetchTipoDocIden(id);
        SimpleCursorAdapter simpleCursorAdapter;
        int[] to = new int[]{
                R.id.textSpinner,
        };

        String[] columns = new String[]{
                DbAdapter_Tipo_Doc_Identidad.tipo_Doc_Descripcion,

        };
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.toolbar_spinner_item_actionbar, cr, columns, to, 0);
        simpleCursorAdapter.setDropDownViewResource(R.layout.toolbar_spinner_item_dropdown);
        spinnerTipoDocumento.setAdapter(simpleCursorAdapter);
    }

    private void setAdapterTipoPersona(int id) {
        Cursor cr = dbAdapter_tipo_persona.fetchTipoPersona(id);
        SimpleCursorAdapter simpleCursorAdapter;
        int[] to = new int[]{
                R.id.textSpinner,

        };

        String[] columns = new String[]{
                DbAdapter_Tipo_Persona.tipo_Persona_Descripcion,

        };
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.toolbar_spinner_item_actionbar, cr, columns, to, 0);
        simpleCursorAdapter.setDropDownViewResource(R.layout.toolbar_spinner_item_dropdown);
        spinnerTipoPesona.setAdapter(simpleCursorAdapter);
    }


    @Override
    public void onValidationSucceeded() {

        String celular_one = "";
        celular_one = editTextNroCelular.getText().toString();
        String nombre = "";
        nombre = editTextNombre.getText().toString();
        String ap_paterno = "";
        ap_paterno = editTextApPaterno.getText().toString();
        String ap_materno = "";
        ap_materno = editTextApMaterno.getText().toString();
        String correo = "";
        correo = editTextCorreo.getText().toString();
        String nroDocumento = "";
        nroDocumento = autoNroDocumento.getText().toString();

        int tipo_doc = 0;
        int tipo_cliente = 0;

        Cursor crDoc = (Cursor) spinnerTipoDocumento.getItemAtPosition(spinnerTipoDocumento.getSelectedItemPosition());
        Cursor crCli = (Cursor) spinnerTipoPesona.getItemAtPosition(spinnerTipoPesona.getSelectedItemPosition());

        tipo_doc = crDoc.getInt(crDoc.getColumnIndex(dbAdapter_tipo_doc_identidad.tipo_Doc_IdentidadId));
        tipo_cliente = crCli.getInt(crCli.getColumnIndex(dbAdapter_tipo_persona.tipo_Persona_PersonaId));


        long estado = db_adapter_historial_establecimient.updateTempEstablecCliente(idEstablecimiento + "", idusuario + "", celular_one, tipo_cliente + "", nombre, ap_paterno, ap_materno, tipo_doc + "", nroDocumento, 1 + "", correo);
        if (estado > 0) {

            if (estadoFinalizar) {
                guardar();
            } else {

                viewPager.setCurrentItem(1);
            }
        } else {
            Utils.setToast(getActivity(), "Ocurrio un error, por favor sal y vuelve a Intentarlo", R.color.rojo);
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

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        viewPager.setCurrentItem(0);

        String message = failedRule.getFailureMessage();
        Log.d("VALIDACION", "" + message + "****" + failedView.getId());
        if (failedView instanceof EditText) {
            ((EditText) failedView).setError(message);
            Utils.setToast(getActivity().getApplicationContext(), "Revise los campos", R.color.rojo);
        }
    }

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
