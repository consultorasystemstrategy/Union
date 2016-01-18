package union.union_vr1.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;


import com.firebase.client.Firebase;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;



import union.union_vr1.Objects.EventoEstablecimiento;
import union.union_vr1.R;
import union.union_vr1.Servicios.FirebaseBackgroundService;
import union.union_vr1.Servicios.ServiceFireListener;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Categoria_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Establecimeinto_Historial;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Establecimiento;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.VMovil_Menu_Establec;

/**
 * Created by Kelvin on 05/11/2015.
 */
public class FEstablecimientoRegistrar extends Fragment implements Validator.ValidationListener {
    private Spinner spinnerTipoEstablecimeinto;
    private Spinner spinnerCategoriaEstablecimeinto;

    @Required(order = 1, messageResId = R.string.requerido_input)
    private EditText editTextNombre;
    private EditText editTextTelFijo;
    private EditText editTextTelMovil2;
    private Button btnGuardar;
    private View v;
    private Validator validator;
    private DbAdapter_Categoria_Establecimiento dbAdapter_categoria_establecimiento;
    private DbAdapter_Tipo_Establecimiento dbAdapter_tipo_establecimiento;
    private DbAdapter_Temp_Establecimiento dbAdapter_temp_establecimiento;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DbAdapter_Temp_Session dbAdapter_temp_session;
    private DbAdapter_Establecimeinto_Historial dbAdapterEstablecimeintoHistorial;
    private ViewPager viewPager;
    String idEstablecimiento;
    int idcat_establec = 0;
    int idTipo_establec = 0;

    //FIREBASE
   // private Firebase rootRef = null;
   // private Firebase nuevoEstablecimientoRef = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_establecimiento, container, false);
        setHasOptionsMenu(true);
        idEstablecimiento = getArguments().getString("idEstablecimiento");
        validator = new Validator(this);
        validator.setValidationListener(this);


        //Firebase.setAndroidContext(getActivity().getApplicationContext());
        //rootRef = new Firebase(Constants._APP_ROOT_FIREBASE);
        //nuevoEstablecimientoRef = rootRef.child(Constants._CHILD_ESTABLECIMIENTO_NUEVO);

        dbAdapter_temp_establecimiento = new DbAdapter_Temp_Establecimiento(getActivity());
        dbAdapter_temp_establecimiento.open();
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        dbAdapter_categoria_establecimiento = new DbAdapter_Categoria_Establecimiento(getActivity());
        dbAdapter_categoria_establecimiento.open();
        dbAdapter_tipo_establecimiento = new DbAdapter_Tipo_Establecimiento(getActivity());
        dbAdapter_tipo_establecimiento.open();
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(getActivity());
        dbAdaptert_evento_establec.open();
        dbAdapter_temp_session = new DbAdapter_Temp_Session(getActivity());
        dbAdapter_temp_session.open();
        dbAdapterEstablecimeintoHistorial = new DbAdapter_Establecimeinto_Historial(getActivity());
        dbAdapterEstablecimeintoHistorial.open();
        spinnerTipoEstablecimeinto = (Spinner) v.findViewById(R.id.spinnerTipoEstablecimiento);
        spinnerCategoriaEstablecimeinto = (Spinner) v.findViewById(R.id.spinnerCategoriaEstablecimiento);
        editTextNombre = (EditText) v.findViewById(R.id.editDescripcion);
        editTextTelFijo = (EditText) v.findViewById(R.id.editTelFijo);
        editTextTelMovil2 = (EditText) v.findViewById(R.id.editNumero2);
        btnGuardar = (Button) v.findViewById(R.id.buttonGuardar);

        callmehod();
        actualizar();
        //--------

        Cursor crCat = (Cursor) spinnerCategoriaEstablecimeinto.getItemAtPosition(0);
        idcat_establec = crCat.getInt(crCat.getColumnIndexOrThrow(DbAdapter_Categoria_Establecimiento.cat_Establec_EstablecId));


        Cursor cr = (Cursor) spinnerTipoEstablecimeinto.getItemAtPosition(0);
        idTipo_establec = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Tipo_Establecimiento.tipo_Establecimiento_EstablecimientoId));

        btnGuardar.setVisibility(View.GONE);
        //----------
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // validator.validate();
            }
        });

        spinnerCategoriaEstablecimeinto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Cursor cr = (Cursor) parent.getItemAtPosition(position);
                idcat_establec = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Categoria_Establecimiento.cat_Establec_EstablecId));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTipoEstablecimeinto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor cr = (Cursor) parent.getItemAtPosition(position);
                idTipo_establec = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Tipo_Establecimiento.tipo_Establecimiento_EstablecimientoId));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    private void callmehod() {
        setTipoEstablec();
        setCatEstablec();
    }

    private void setTipoEstablec() {
        Cursor cr = dbAdapter_tipo_establecimiento.fetchTipoEstablec();
        SimpleCursorAdapter simpleCursorAdapter;
        int[] to = new int[]{
                R.id.textSpinner,

        };

        String[] columns = new String[]{
                DbAdapter_Tipo_Establecimiento.tipo_Establecimiento_Descripcion

        };
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.toolbar_spinner_item_actionbar, cr, columns, to, 0);
        simpleCursorAdapter.setDropDownViewResource(R.layout.toolbar_spinner_item_dropdown);
        spinnerTipoEstablecimeinto.setAdapter(simpleCursorAdapter);
    }

    private void setCatEstablec() {
        Cursor cr = dbAdapter_categoria_establecimiento.fetchCatEstablecimiento();
        SimpleCursorAdapter simpleCursorAdapter;
        int[] to = new int[]{
                R.id.textSpinner,

        };

        String[] columns = new String[]{
                DbAdapter_Categoria_Establecimiento.cat_Establec_Descripcion,

        };
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.toolbar_spinner_item_actionbar, cr, columns, to, 0);
        simpleCursorAdapter.setDropDownViewResource(R.layout.toolbar_spinner_item_dropdown);
        spinnerCategoriaEstablecimeinto.setAdapter(simpleCursorAdapter);
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


    @Override
    public void onValidationSucceeded() {
        String cat_est = "", tipo_esta = "", nomb_estb = "", fijo = "", two = "";

        cat_est = idcat_establec + "";
        tipo_esta = idTipo_establec + "";
        nomb_estb = editTextNombre.getText().toString();
        fijo = editTextTelFijo.getText().toString();
        two = editTextTelMovil2.getText().toString();

        long estado = dbAdapter_temp_establecimiento.updateTempEstablecEstabl(idEstablecimiento + "", cat_est, tipo_esta, nomb_estb, fijo, two);
        if (estado > 0) {

           alertConfirmar();

        } else {
            Utils.setToast(getActivity(), "Ocurrio un error, por favor sal y vuelve a Intentarlo", R.color.rojo);
        }
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        viewPager.setCurrentItem(2);
        Log.d("VALIDACION", "" + message + "****" + failedView.getId());
        if (failedView instanceof EditText) {
            ((EditText) failedView).setError(message);
        }
    }


    public void alertConfirmar() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("¿Seguro de Guardar?");
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("")
                .setCancelable(false)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(conectadoRedMovil() || conectadoWifi()){
                            guardar(Constants.REGISTRO_INTERNET);
                        }else{
                            guardar(Constants.REGISTRO_SIN_INTERNET);
                        }


                    }

                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity_agregar_establecimiento, menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_establec:
                validator.validate();
                break;
        }

        return false;
    }


    protected Boolean conectadoWifi() {
        ConnectivityManager connectivity = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
        ConnectivityManager connectivity = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private void guardar(int estadoAutorizado){
        int idLiquidacion = dbAdapter_temp_session.fetchVarible(3);
        int idAgente = dbAdapter_temp_session.fetchVarible(1);
        int nroOrder = dbAdaptert_evento_establec.ordern();
        //Si no hay coneccion el estado autorizacion se guarda en 100
        Cursor cursor = dbAdapter_temp_establecimiento.fetchTemEstablec();
        while(cursor.moveToNext()){
            //Evento por establecimiento Historial para poder editar

            int inserto = dbAdapterEstablecimeintoHistorial.createTempEstablec(
                    0,
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_usuario_accion)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_telefono_fijo)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_celular_one)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_celular_two)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_latitud)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_longitud)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_direccion_fiscal)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_persona)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nombres)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apPaterno)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apMaterno)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_documento)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nro_documento)),
                    estadoAutorizado, // Estado para desoues enviar
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_correo)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_establecimiento)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion_establecimiento)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_categoria_estable)),
                    Constants._CREADO,
                    0);



            //Evento por extablecimiemto
            EventoEstablecimiento eventoEstablecimiento = new EventoEstablecimiento(
                    Integer.parseInt(""+inserto),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_categoria_estable)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_tipo_documento)),
                    0,
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion_establecimiento)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nombres)) + " "+
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apPaterno)) + " "+
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_apMaterno)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_nro_documento)),
                    nroOrder,
                    0,
                    0,
                    0.0,
                    0,
                    0,
                    "",
                    idAgente,
                    Constants._CREADO,
                    "",
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_descripcion)),
                    estadoAutorizado,//Estado Creado para desdepues poder enviar
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_direccion_fiscal)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_latitud)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Establecimeinto_Historial.establec_longitud))
            );
            long estadoInserto = dbAdaptert_evento_establec.createEstablecimientos(eventoEstablecimiento,idAgente,idLiquidacion,inserto);
            Log.d("IDSEGUIDO",""+inserto);
        }

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("DIRECCION_FISCAL", Context.MODE_PRIVATE).edit();
        editor.putString("fiscal", null);
        editor.commit();
        dbAdapter_temp_establecimiento.deleteAll();



        startActivity(new Intent(getActivity().getApplicationContext(), VMovil_Menu_Establec.class));
        getActivity().finish();
    }



}

