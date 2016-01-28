package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import union.union_vr1.R;
import union.union_vr1.Utils.Utils;

/**
 * Created by Usuario on 28/01/2016.
 */
public class CursorAdapter_ReporteComprobante extends CursorAdapter{
    //infla dentro de la actividad
    private LayoutInflater cursorInflater;


    public CursorAdapter_ReporteComprobante(Context context, Cursor c) {
        super(context, c, true);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.infor_resumen_ingresos, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {




        TextView textViewDescripcion = (TextView) view.findViewById(R.id.VRC_descripcion);
        TextView textViewCantidad = (TextView) view.findViewById(R.id.VRC_cantidad);
        TextView textViewEmitido = (TextView) view.findViewById(R.id.VRC_vendido);
        TextView textViewPagado = (TextView) view.findViewById(R.id.VRC_pagado);
        TextView textViewCobrado = (TextView) view.findViewById(R.id.VRC_cobrado);


        String comprobante = cursor.getString(cursor.getColumnIndexOrThrow("comprobante"));
        int n = cursor.getInt(cursor.getColumnIndexOrThrow("n"));
        Double emitidas = cursor.getDouble(cursor.getColumnIndexOrThrow("emitidas"));
        Double  pagado = cursor.getDouble(cursor.getColumnIndexOrThrow("pagado"));
        Double  cobrado = cursor.getDouble(cursor.getColumnIndexOrThrow("cobrado"));


        textViewDescripcion.setText(comprobante);
        textViewCantidad.setText(""+n);
        textViewEmitido.setText(""+Utils.formatDouble(emitidas));
        textViewPagado.setText(""+Utils.formatDouble(pagado));
        textViewCobrado.setText(""+Utils.formatDouble(cobrado));
    }
}
