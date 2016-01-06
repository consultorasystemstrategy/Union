package union.union_vr1.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.AsyncTask.CrearEstablecimiento;
import union.union_vr1.AsyncTask.ImportMain;
import union.union_vr1.AsyncTask.ModificarEstablecimiento;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Categoria_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Establecimeinto_Historial;
import union.union_vr1.Sqlite.DbAdapter_Temp_DatosSpinner;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Establecimiento;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.VMovil_Menu_Establec;

/**
 * Created by Kelvin on 05/11/2015.
 */
public class FEstablecimientoEditar extends Fragment implements Validator.ValidationListener {
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
    private ViewPager viewPager;
    private DbAdapter_Establecimeinto_Historial dbAdapter_temp_establecimiento;
    String idEstablecimiento;
    int idcat_establec = 0;
    int idTipo_establec = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_establecimiento, container, false);
        setHasOptionsMenu(true);
        idEstablecimiento = getArguments().getString("idEstablecimiento");
        validator = new Validator(this);
        validator.setValidationListener(this);

        dbAdapter_temp_establecimiento = new DbAdapter_Establecimeinto_Historial(getActivity());
        dbAdapter_temp_establecimiento.open();
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        dbAdapter_categoria_establecimiento = new DbAdapter_Categoria_Establecimiento(getActivity());
        dbAdapter_categoria_establecimiento.open();
        dbAdapter_tipo_establecimiento = new DbAdapter_Tipo_Establecimiento(getActivity());
        dbAdapter_tipo_establecimiento.open();
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
        display();


        return v;
    }

    private void display() {
        Cursor cr = dbAdapter_temp_establecimiento.fetchTemEstablecById(idEstablecimiento);
        if (cr.moveToFirst()) {
            editTextNombre.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_descripcion_establecimiento)));
            editTextTelFijo.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_telefono_fijo)));
            editTextTelMovil2.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_two)));
        }
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
        Cursor crCat = (Cursor) spinnerCategoriaEstablecimeinto.getItemAtPosition(spinnerCategoriaEstablecimeinto.getSelectedItemPosition());
        Cursor crTipoEs = (Cursor) spinnerCategoriaEstablecimeinto.getItemAtPosition(spinnerCategoriaEstablecimeinto.getSelectedItemPosition());

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
        alertDialogBuilder.setTitle("Seguro de Guardar");
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Operacion:")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        new ModificarEstablecimiento(getActivity()).execute(idEstablecimiento);

                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
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


}

