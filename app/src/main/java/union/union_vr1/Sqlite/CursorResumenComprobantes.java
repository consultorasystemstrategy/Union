package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

import union.union_vr1.R;

/**
 * Created by Usuario on 17/04/2015.
 */
public class CursorResumenComprobantes extends CursorAdapter {

    private LayoutInflater cursorInflater;
    
    public CursorResumenComprobantes(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = null;
        int n = cursor.getInt(cursor.getColumnIndexOrThrow("n"));
        if (n!=0) {
            view = cursorInflater.inflate(R.layout.infor_resumen_ingresos, viewGroup, false);
        }else{
            view = cursorInflater.inflate(R.layout.layout_empty, viewGroup, false);
        }

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

            if (cursor.getCount() > 0) {
                int n = cursor.getInt(cursor.getColumnIndexOrThrow("n"));
                if (n!=0) {
                    TextView textViewComprobante = (TextView) view.findViewById(R.id.VRC_descripcion);
                    TextView textViewTotalN = (TextView) view.findViewById(R.id.VRC_cantidad);
                    TextView textViewTotalEmitido = (TextView) view.findViewById(R.id.VRC_vendido);
                    TextView textViewTotalPagado = (TextView) view.findViewById(R.id.VRC_pagado);
                    TextView textViewTotalCobrado = (TextView) view.findViewById(R.id.VRC_cobrado);


                    String comprobante = cursor.getString(cursor.getColumnIndexOrThrow("comprobante"));
                    Double emitido = cursor.getDouble(cursor.getColumnIndexOrThrow("emitidas"));
                    Double pagado = cursor.getDouble(cursor.getColumnIndexOrThrow("pagado"));
                    Double cobrado = cursor.getDouble(cursor.getColumnIndexOrThrow("cobrado"));


                    DecimalFormat df = new DecimalFormat("#0.00");

                    textViewComprobante.setText("" + comprobante);
                    textViewTotalN.setText("" + n);
                    textViewTotalEmitido.setText("" + emitido);
                    textViewTotalPagado.setText("" + pagado);
                    textViewTotalCobrado.setText("" + cobrado);
                }else{
                    view.setVisibility(View.GONE);
                }
            }

        }
}
