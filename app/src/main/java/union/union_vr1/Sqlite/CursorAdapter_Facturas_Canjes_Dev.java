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
 * Created by Usuario on 18/12/2014.
 */
public class CursorAdapter_Facturas_Canjes_Dev extends CursorAdapter {
    private DbAdapter_Canjes_Devoluciones dbHelpeCanjes_Dev;

    public CursorAdapter_Facturas_Canjes_Dev(Context context, Cursor c) {
        super(context, c);
        dbHelpeCanjes_Dev = new DbAdapter_Canjes_Devoluciones(context);
        dbHelpeCanjes_Dev.open();

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
                String factura = "";
                String fecha = "";
                String monto = "";
                String comprobante = cursor.getString(7);
                String producto = cursor.getString(9);
                String cantidad = cursor.getString(10);
                String devuelto = cursor.getString(17);
                //Cortar el String Comprobante
                System.out.println("con datos");
                String[] datos = comprobante.split("/");

                if (datos.length >= 2) {
                    factura = datos[1];
                    fecha = datos[2];
                    monto = datos[3];


                    String textoAdpater = "Factura: " + factura + ","
                            + "\nProducto: " + producto + ","
                            + "\nCantidad: " + cantidad + "    Devuelto: " + devuelto + " "
                            + "\nMonto: " + monto + ""
                            + "\nFecha: " + fecha + "";


                    textView.setText(textoAdpater);
                } else {
                    String factura2 = cursor.getString(7);
                    String fecha2 = cursor.getString(12);
                    String monto2 = cursor.getString(11);

                    String textoAdpater = "Factura: " + factura2 + ","
                            + "\nProducto: " + producto + ","
                            + "\nCantidad: " + cantidad + ""
                            + "\nMonto: " + monto2 + ""
                            + "\nFecha: " + fecha2 + "";
                    textView.setText(textoAdpater);
                }
                //-------
            }




    }
}
