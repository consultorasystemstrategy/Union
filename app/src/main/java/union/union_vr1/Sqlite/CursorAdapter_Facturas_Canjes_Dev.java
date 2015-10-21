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
        DecimalFormat df = new DecimalFormat("#.00");
        TextView textViewTitulo = (TextView) view.findViewById(R.id.textViewListaTitulo);
        TextView textViewSubtitulo = (TextView) view.findViewById(R.id.textViewListaSubtitulo);
        TextView textViewComment = (TextView) view.findViewById(R.id.textViewListaComment);
        TextView textViewMonto = (TextView) view.findViewById(R.id.textViewListaMonto);
        LinearLayout linearLayoutColor = (LinearLayout) view.findViewById(R.id.linearLayoutLista);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewLista);

        if (cursor.getCount() > 0) {


            String factura = "";
            String fecha = "";
            String monto = "";
            String comprobante = cursor.getString(7);
            String producto = cursor.getString(9);
            String cantidad = cursor.getString(10);

            int canje = cursor.getInt(17);
            int dev = cursor.getInt(25);

            //Cortar el String Comprobante
            System.out.println("con datos");
            String[] datos = comprobante.split("/");

            if (datos.length >= 2) {
                factura = datos[1];
                fecha = datos[2];
                monto = datos[3];

                double total = Double.parseDouble(monto) * Integer.parseInt(cantidad);


                textViewTitulo.setText("Factura: " + factura);
                textViewSubtitulo.setText("Cantidad: " + cantidad + "\nFecha : " + fecha);
                textViewComment.setText("Producto: " + producto + "\nCanjes: " + canje + "\nDevoluciones: " + dev);
                textViewMonto.setText("S/. " + df.format(total));
            } else {

            }
            linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark1));

        }
    }
}
