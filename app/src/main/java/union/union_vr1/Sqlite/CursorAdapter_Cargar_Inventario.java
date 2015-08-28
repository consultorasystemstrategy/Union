package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import union.union_vr1.R;

/**
 * Created by Usuario on 18/12/2014.
 */
public class CursorAdapter_Cargar_Inventario extends CursorAdapter {
    private DBAdapter_Temp_Inventario dbAdapter_temp_inventario;
    private LayoutInflater cursorInflater;

    public CursorAdapter_Cargar_Inventario(Context context, Cursor c) {
        super(context, c);
        dbAdapter_temp_inventario = new DBAdapter_Temp_Inventario(context);
        dbAdapter_temp_inventario.open();
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.lista_inventario, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titulo = (TextView) view.findViewById(R.id.textViewListaTituloInventario);
        titulo.setText("Guia: "+cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Inventario.temp_in_id_guia)));


    }
}
