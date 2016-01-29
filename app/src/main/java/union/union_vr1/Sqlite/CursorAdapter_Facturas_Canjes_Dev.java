package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
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
import union.union_vr1.Utils.Utils;

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
            String comprobante = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_comprobante));
            String producto = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_nom_producto));
            String cantidad = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_cantidad));

            int canje = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_cantidad_ope));
            int dev = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_cantidad_ope_dev));

            //Cortar el String Comprobante
            System.out.println("con datos");
            String[] datos = comprobante.split("/");

            if (datos.length >= 2) {
                factura = datos[1];
                String [] com = factura.split("-");
                int num = Integer.parseInt(com[1]);
                String compr =com[0] +"-"+ String.format("%08d", num);
                fecha = datos[2];
                monto = datos[3];
                Log.d("FACTURAS DEVOLUCIONES",""+comprobante);

                double total = Double.parseDouble(monto) * Integer.parseInt(cantidad);


                textViewTitulo.setText("Comprobante: " + compr);
                textViewSubtitulo.setText("Cantidad: " + cantidad + "\nFecha : " + fecha);
                textViewComment.setText("Producto: " + producto + "\nCanjes: " + canje + "\nDevoluciones: " + dev);
                textViewMonto.setText("S/. " + Utils.formatDouble(total));
            } else {

            }
            linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark1));

        }
    }
}
