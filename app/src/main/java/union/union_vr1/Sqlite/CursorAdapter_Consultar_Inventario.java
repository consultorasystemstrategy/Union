package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import union.union_vr1.R;

/**
 * Created by Usuario on 18/12/2014.
 */
public class CursorAdapter_Consultar_Inventario extends CursorAdapter {

    private LayoutInflater cursorInflater;

    public CursorAdapter_Consultar_Inventario(Context context, Cursor c) {
        super(context, c);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.layout_consultar_inventario, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titulo = (TextView) view.findViewById(R.id.textConsultarNombre);
        TextView cantidad = (TextView) view.findViewById(R.id.textConsultarCantidad);
        titulo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Consultar_Inventario_Anterior.temp_in_nombre)));
        cantidad.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Consultar_Inventario_Anterior.temp_te_cantidad)));


    }
}
