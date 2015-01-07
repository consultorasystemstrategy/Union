package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Usuario on 29/12/2014.
 */
public class CursorAdapterFacturas_dev extends CursorAdapter {
    public CursorAdapterFacturas_dev(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view;
        textView.setSingleLine(false);

        WindowManager.LayoutParams layout_791 = new WindowManager.LayoutParams();
        layout_791.width = WindowManager.LayoutParams.MATCH_PARENT;
        layout_791.height = WindowManager.LayoutParams.WRAP_CONTENT;
        textView.setLayoutParams(layout_791);

        String producto = cursor.getString(9);
        String cantidad = cursor.getString(25);
        String precio = cursor.getString(27);
        String acc = "2";
        String mot = cursor.getString(26);

        if (acc.equals("1")) {
            acc = "Canje";

        } else {
            acc = "Devolucion";
        }
        if (mot.equals("1")) {
            mot = "Bueno";

        }
        if (mot.equals("2")) {
            mot = "Malogrado";

        }
        if (mot.equals("3")) {
            mot = "Reclamo";

        }
        if (mot.equals("4")) {
            mot = "Vencido-Malo";

        }
        String textAdapter = "Producto: " + producto + "" +
                "\nCantidad: " + cantidad + "" +
                "\nPrecio: " + precio + "" +
                "\nAcc: " + acc + "" +
                "\nMot: " + mot + "";
        textView.setText(textAdapter);

    }
}
