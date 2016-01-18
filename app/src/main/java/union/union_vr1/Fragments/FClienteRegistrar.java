package union.union_vr1.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Rules;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import java.util.ArrayList;
import java.util.List;

import union.union_vr1.AsyncTask.ValidarCliente;
import union.union_vr1.R;
import union.union_vr1.Sqlite.DBAdapter_Cliente_Ruta;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Doc_Identidad;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Persona;
import union.union_vr1.Utils.Utils;

/**
 * Created by Kelvin on 04/11/2015.
 */
public class FClienteRegistrar extends Fragment implements Validator.ValidationListener {

    private DBAdapter_Cliente_Ruta dbAdapter_cliente_ruta;
    private int tipoDoc;
    private boolean estado = false;

    //VALIDACION DE LOS WIDGETS
    private Spinner spinnerTipoDocumento;
    private Spinner spinnerTipoPesona;

    @Required(order = 1, messageResId = R.string.requerido_input)
    private EditText editTextNombre;
    private Fragment fragment;

    private EditText editTextApPaterno;
    private EditText editTextApMaterno;

    //@Required(order = 2, messageResId = R.string.requerido_input)
    private AutoCompleteTextView autoNroDocumento;

    private EditText editTextNroCelular;

    @Email(order = 4, messageResId = R.string.requerido_correo)
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
    private DbAdapter_Temp_Establecimiento dbAdapter_temp_establecimiento;
    private DbAdapter_Agente dbAdapter_agente;
    private Validator validator;
    private Button btnValidarCliente;
    String idEstablecimiento;
    int idusuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cliente, container, false);
        setHasOptionsMenu(false);
        fragment = this;
        idEstablecimiento = getArguments().getString("idEstablecimiento");
        toastBucsando = Toast.makeText(getActivity().getApplicationContext(), "Buscando...", 1000);
        toast = Toast.makeText(getActivity().getApplicationContext(), "Encontrado", Toast.LENGTH_SHORT);
        toast.getView().setBackgroundColor(getActivity().getResources().getColor(R.color.verde));
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        TextView vz = (TextView) toast.getView().findViewById(android.R.id.message);
        vz.setTextColor(getActivity().getResources().getColor(R.color.Blanco));
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        btnValidarCliente = (Button) v.findViewById(R.id.btnValidarCliente);
        //...........
        //VALIDATOR INTANCE
        validator = new Validator(this);
        validator.setValidationListener(this);

        //--------------


        dbAdapter_tipo_doc_identidad = new DbAdapter_Tipo_Doc_Identidad(getActivity());
        dbAdapter_tipo_persona = new DbAdapter_Tipo_Persona(getActivity());
        dbAdapter_tipo_doc_identidad.open();
        dbAdapter_tipo_persona.open();
        dbAdapter_temp_establecimiento = new DbAdapter_Temp_Establecimiento(getActivity());
        dbAdapter_temp_establecimiento.open();
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
                List<Rule<?>> nro = new ArrayList<Rule<?>>(3);
                Log.d("TIPO DOC", "" + tipoDoc);
                String tipo = "";
                switch (tipoDoc) {
                    case 1:
                        tipo = "DNI";
                        validator.removeRulesFor(autoNroDocumento);
                        nro.add(Rules.required("Requerido", true));
                        nro.add(Rules.minLength("Formato incorrecto", 8, true));
                        nro.add(Rules.maxLength("Formato incorrecto", 8, true));
                        validator.put(autoNroDocumento, nro);
                        break;
                    case 2:
                        tipo = "RUC";
                        validator.removeRulesFor(autoNroDocumento);
                        nro.add(Rules.required("Requerido", true));
                        nro.add(Rules.minLength("Formato incorrecto", 11, true));
                        nro.add(Rules.maxLength("Formato incorrecto", 11, true));
                        validator.put(autoNroDocumento, nro);
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
        autoComplete();


        btnValidarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (autoNroDocumento.getText().toString() != null || autoNroDocumento.getText().toString() != "") {

                    if (autoNroDocumento.getText().toString().length() == 8 && tipoDoc == 1) {
                        estado = true;

                        if (conectadoWifi() || conectadoRedMovil()) {
                            new ValidarCliente(getActivity(), fragment).execute(autoNroDocumento.getText().toString());
                        }
                    }
                    if (autoNroDocumento.getText().toString().length() == 11 && tipoDoc == 2) {
                        estado = true;
                        if (conectadoWifi() || conectadoRedMovil()) {
                            new ValidarCliente(getActivity(), fragment).execute(autoNroDocumento.getText().toString());
                        }
                    }
                }

            }
        });

        return v;
    }


    private void hideWifgets(int tipo) {

        switch (tipo) {
            case 1:
                setAdapterTipoDocIdentidad(-1);
                textCliente.setText("Nombres");
                // linearLayoutMadre.setVisibility(View.VISIBLE);
                // linearLayoutPadre.setVisibility(View.VISIBLE);
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

    private void autoComplete() {


      /*  final SimpleCursorAdapter adapter;
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


        autoNroDocumento.setAdapter(adapter);*/

        autoNroDocumento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("CONTAR", "" + count);
                enableWidgets();
                if (count == 0) {
                    setAdapterTipoDocIdentidad(-1);
                    setAdapterTipoPersona(-1);

                }
                if (count == 9 && tipoDoc == 1) {
                    autoNroDocumento.setError("No permitido");
                }
                if (count == 12 && tipoDoc == 2) {
                    autoNroDocumento.setError("No permitido");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

     /*   adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                Cursor cr = dbAdapter_cliente_ruta.listarDocumentoClientexRuta(charSequence.toString(), tipoDoc);
                return cr;
            }
        });*/


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
      /*  editTextNombre.setText("");
        editTextApPaterno.setText("");
        editTextApMaterno.setText("");
        editTextNroCelular.setText("");
        editTextCorreo.setText("");*/
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


        long estadoIn = dbAdapter_temp_establecimiento.updateTempEstablecCliente(idEstablecimiento + "", idusuario + "", celular_one, tipo_cliente + "", nombre, ap_paterno, ap_materno, tipo_doc + "", nroDocumento, 1 + "", correo);
        if (estadoIn > 0 && estado == true) {
            viewPager.setCurrentItem(1);
        } else {

            if (conectadoRedMovil() || conectadoWifi()) {
                viewPager.setCurrentItem(0);
                Utils.setToast(getActivity(), "Ocurrio un error.", R.color.rojo);
            }

        }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected Boolean conectadoWifi() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }


}
