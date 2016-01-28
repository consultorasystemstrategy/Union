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
public class CursorAdapter_ResumenGastos extends CursorAdapter {
    //infla dentro de la actividad
    private LayoutInflater cursorInflater;


    public CursorAdapter_ResumenGastos(Context context, Cursor c) {
        super(context, c, true);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.resumen_informe_gastos, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView textViewGastoNombre = (TextView) view.findViewById(R.id.textviewNombre);
        TextView textViewGastoImporte = (TextView) view.findViewById(R.id.textViewGastoRuta);


        String gastoNombre = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Tipo_Gasto.TG_nom_tipo_gasto ));
        Double gastoImporte = cursor.getDouble(cursor.getColumnIndexOrThrow("RUTA"));

        textViewGastoNombre.setText(gastoNombre);
        textViewGastoImporte.setText(Utils.formatDouble(gastoImporte));

    }
}
