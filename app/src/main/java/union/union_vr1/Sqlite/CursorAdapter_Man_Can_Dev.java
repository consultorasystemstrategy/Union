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
 * Created by Usuario on 22/12/2014.
 */
public class CursorAdapter_Man_Can_Dev extends CursorAdapter {
    DbAdapter_Canjes_Devoluciones dbHelper_Canjes_Dev;

    public CursorAdapter_Man_Can_Dev(Context context, Cursor c) {
        super(context, c);
        dbHelper_Canjes_Dev = new DbAdapter_Canjes_Devoluciones(context);
        dbHelper_Canjes_Dev.open();
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
        if (cursor.moveToFirst()) {
            String idDetalle= cursor.getString(1);
            String tipo_Op = cursor.getString(5);
            if(tipo_Op.equals("1")){
                tipo_Op="Canje";
            }
            else{
                tipo_Op="Devolucion";
            }
            String categoria_op=cursor.getString(15);
            if(categoria_op.equals("1")){
                categoria_op="Bueno";
            }
            if(categoria_op.equals("2")){
                categoria_op="Malogrado";
            }
            if(categoria_op.equals("3")){
                categoria_op="Reclamo";
            }
            if(categoria_op.equals("4")){
                categoria_op="Vencido-Malo";
            }
            String nomProducto = cursor.getString(9);
            String fecha = cursor.getString(19);
            String importe = cursor.getString(18);

            String textoAdpater = "Producto: "+nomProducto+"," +
                    "\nOperacion: "+tipo_Op+"," +
                    "\nCategoria: "+categoria_op+"," +
                    "\nImporte: "+importe+"," +
                    "\nFecha: "+fecha+"";




            textView.setText(textoAdpater);

            textView.setBackgroundColor(0xff00ff00);
        }
    }
}
