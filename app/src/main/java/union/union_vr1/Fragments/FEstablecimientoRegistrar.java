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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Categoria_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Temp_DatosSpinner;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Establecimiento;

/**
 * Created by Kelvin on 05/11/2015.
 */
public class FEstablecimientoRegistrar extends Fragment {
    private Spinner spinnerTipoEstablecimeinto;
    private Spinner spinnerCategoriaEstablecimeinto;
    private MaterialEditText editTextNombre;
    private MaterialEditText editTextTelFijo;
    private MaterialEditText editTextTelMovil2;
    private View v;

    private DbAdapter_Categoria_Establecimiento dbAdapter_categoria_establecimiento;
    private DbAdapter_Tipo_Establecimiento dbAdapter_tipo_establecimiento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_establecimiento, container, false);


        dbAdapter_categoria_establecimiento = new DbAdapter_Categoria_Establecimiento(getActivity());
        dbAdapter_categoria_establecimiento.open();
        dbAdapter_tipo_establecimiento = new DbAdapter_Tipo_Establecimiento(getActivity());
        dbAdapter_tipo_establecimiento.open();
        spinnerTipoEstablecimeinto = (Spinner) v.findViewById(R.id.spinnerTipoEstablecimiento);
        spinnerCategoriaEstablecimeinto = (Spinner) v.findViewById(R.id.spinnerCategoriaEstablecimiento);
        editTextNombre = (MaterialEditText)v.findViewById(R.id.editDescripcion);
        editTextTelFijo = (MaterialEditText)v.findViewById(R.id.editTelFijo);
        editTextTelMovil2 = (MaterialEditText)v.findViewById(R.id.editNumero2);

      callmehod();

        return v;
    }

    private void callmehod() {
        setTipoEstablec();
        setCatEstablec();
    }
    private void setTipoEstablec(){
        Cursor cr = dbAdapter_tipo_establecimiento.fetchTipoEstablec();
        SimpleCursorAdapter simpleCursorAdapter ;
        int[] to = new int[]{
                android.R.id.text1,

        };

        String[] columns = new String[]{
                DbAdapter_Tipo_Establecimiento.tipo_Establecimiento_Descripcion

        };
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),R.layout.toolbar_spinner_item_actionbar,cr,columns,to,0);
        simpleCursorAdapter.setDropDownViewResource(R.layout.toolbar_spinner_item_dropdown);
        spinnerTipoEstablecimeinto.setAdapter(simpleCursorAdapter);
    }

    private void setCatEstablec(){
        Cursor cr = dbAdapter_categoria_establecimiento.fetchCatEstablecimiento();
        SimpleCursorAdapter simpleCursorAdapter ;
        int[] to = new int[]{
                android.R.id.text1,

        };

        String[] columns = new String[]{
                DbAdapter_Categoria_Establecimiento.cat_Establec_Descripcion,

        };
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),R.layout.toolbar_spinner_item_actionbar,cr,columns,to,0);
        simpleCursorAdapter.setDropDownViewResource(R.layout.toolbar_spinner_item_dropdown);
        spinnerCategoriaEstablecimeinto.setAdapter(simpleCursorAdapter);
    }







}

