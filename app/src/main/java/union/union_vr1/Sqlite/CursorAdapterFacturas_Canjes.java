package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by Usuario on 30/12/2014.
 */
public class CursorAdapterFacturas_Canjes extends CursorAdapter{
    public CursorAdapterFacturas_Canjes(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        final LayoutInflater inflater  = LayoutInflater.from(context);
        final View view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, viewGroup,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view;
        textView.setSingleLine(false);
        WindowManager.LayoutParams layout_791 = new WindowManager.LayoutParams();
        layout_791.width = WindowManager.LayoutParams.MATCH_PARENT;
        layout_791.height  = WindowManager.LayoutParams.WRAP_CONTENT;
        textView.setLayoutParams(layout_791);

        String producto = cursor.getString(9);
        String cantidad = cursor.getString(17);
        Double precio = cursor.getDouble(18);
        String idProducto = cursor.getString(4);
        DecimalFormat df= new DecimalFormat("#0.00");



        String textAdapter = "Producto: "+producto+"" +
                "\nCantidad: "+cantidad+"" +
                "\nP.U: "+precio+"" +
                "\nTot: "+df.format(precio*cursor.getInt(17))+"" +
                "\nRef: "+idProducto+"";
        textView.setText(textAdapter);
    }
}
