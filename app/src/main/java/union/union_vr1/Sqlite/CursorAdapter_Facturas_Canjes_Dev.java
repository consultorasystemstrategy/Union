package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import union.union_vr1.R;

/**
 * Created by Usuario on 18/12/2014.
 */
public class CursorAdapter_Facturas_Canjes_Dev extends CursorAdapter {
    private DbAdapter_Canjes_Devoluciones dbHelpeCanjes_Dev;
    private LayoutInflater cursorInflater;

    public CursorAdapter_Facturas_Canjes_Dev(Context context, Cursor c) {
        super(context, c);
        dbHelpeCanjes_Dev = new DbAdapter_Canjes_Devoluciones(context);
        dbHelpeCanjes_Dev.open();
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.lista_personalizada, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewTitulo = (TextView) view.findViewById(R.id.textViewListaTitulo);
        TextView textViewSubtitulo = (TextView) view.findViewById(R.id.textViewListaSubtitulo);
        TextView textViewComment = (TextView) view.findViewById(R.id.textViewListaComment);
        TextView textViewMonto = (TextView) view.findViewById(R.id.textViewListaMonto);
        LinearLayout linearLayoutColor = (LinearLayout) view.findViewById(R.id.linearLayoutLista);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewLista);

        if (cursor.getCount()>0){


            String factura = "";
            String fecha = "";
            String monto = "";
            String comprobante = cursor.getString(7);
            String producto = cursor.getString(9);
            String cantidad = cursor.getString(10);
            int devuelto = cursor.getInt(17)+cursor.getInt(25);

            //Cortar el String Comprobante
            System.out.println("con datos");
            String[] datos = comprobante.split("/");

            if (datos.length >= 2) {
                factura = datos[1];
                fecha = datos[2];
                monto = datos[3];


                String textoAdpater = "Factura: " + factura + ","
                        + "\nProducto: " + producto + ","
                        + "\nCantidad: " + cantidad + "    Dev/Canj: " + devuelto + " "
                        + "\nMonto: " + monto + ""
                        + "\nFecha: " + fecha + "";


                textViewTitulo.setText("Factura : " + factura);
                textViewSubtitulo.setText("Cantidad : "+ cantidad + ", Fecha : "+ fecha);
                textViewComment.setText("Producto "+ producto + "\nDev/Canj : "+ devuelto);
                textViewMonto.setText("S/. "+monto);
            } else {
                String factura2 = cursor.getString(7);
                String fecha2 = cursor.getString(12);
                String monto2 = cursor.getString(11);

                String textoAdpater = "Factura: " + factura2 + ","
                        + "\nProducto: " + producto + ","
                        + "\nCantidad: " + cantidad + ""
                        + "\nMonto: " + monto2 + ""
                        + "\nFecha: " + fecha2 + "";
                textViewTitulo.setText("Factura : " + factura2);
                textViewSubtitulo.setText("Cantidad : "+ cantidad + ", Fecha : "+ fecha2);
                textViewComment.setText("Producto "+ producto);
                textViewMonto.setText("S/. "+monto2);
            }
            linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark1));

        }
    }
}
