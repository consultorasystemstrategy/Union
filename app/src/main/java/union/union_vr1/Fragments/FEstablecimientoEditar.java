package union.union_vr1.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Categoria_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Establecimiento;

/**
 * Created by Kelvin on 05/11/2015.
 */
public class FEstablecimientoEditar extends Fragment {
    private Spinner spinnerTipoEstablecimeinto;
    private Spinner spinnerCategoriaEstablecimeinto;
    private EditText editTextNombre;
    private EditText editTextTelFijo;
    private EditText editTextTelMovil2;
    private View v;
    private DbAdapter_Temp_Establecimiento dbAdapter_temp_establecimiento;
    String idEstablecimiento = "";
    private DbAdapter_Categoria_Establecimiento dbAdapter_categoria_establecimiento;
    private DbAdapter_Tipo_Establecimiento dbAdapter_tipo_establecimiento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_establecimiento, container, false);
        idEstablecimiento = getArguments().getString("idEstablecimiento");
        dbAdapter_temp_establecimiento = new DbAdapter_Temp_Establecimiento(getActivity());
        dbAdapter_temp_establecimiento.open();
        dbAdapter_categoria_establecimiento = new DbAdapter_Categoria_Establecimiento(getActivity());
        dbAdapter_categoria_establecimiento.open();
        dbAdapter_tipo_establecimiento = new DbAdapter_Tipo_Establecimiento(getActivity());
        dbAdapter_tipo_establecimiento.open();
        spinnerTipoEstablecimeinto = (Spinner) v.findViewById(R.id.spinnerTipoEstablecimiento);
        spinnerCategoriaEstablecimeinto = (Spinner) v.findViewById(R.id.spinnerCategoriaEstablecimiento);
        editTextNombre = (EditText) v.findViewById(R.id.editDescripcion);
        editTextTelFijo = (EditText) v.findViewById(R.id.editTelFijo);
        editTextTelMovil2 = (EditText) v.findViewById(R.id.editNumero2);

        callmehod();
        display();

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
                android.R.id.text1,

        };

        String[] columns = new String[]{
                DbAdapter_Tipo_Establecimiento.tipo_Establecimiento_Descripcion

        };
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, cr, columns, to, 0);
        spinnerTipoEstablecimeinto.setAdapter(simpleCursorAdapter);
    }

    private void setCatEstablec() {
        Cursor cr = dbAdapter_categoria_establecimiento.fetchCatEstablecimiento();
        SimpleCursorAdapter simpleCursorAdapter;
        int[] to = new int[]{
                android.R.id.text1,

        };

        String[] columns = new String[]{
                DbAdapter_Categoria_Establecimiento.cat_Establec_Descripcion,

        };
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, cr, columns, to, 0);
        spinnerCategoriaEstablecimeinto.setAdapter(simpleCursorAdapter);
    }

    private void display() {
        Cursor cr = dbAdapter_temp_establecimiento.fetchTemEstablecById(idEstablecimiento);
        if (cr.moveToFirst()) {
            editTextNombre.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_descripcion_establecimiento)));
            editTextTelFijo.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_telefono_fijo)));
            editTextTelMovil2.setText(cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Temp_Establecimiento.establec_celular_two)));
        }
    }


}

