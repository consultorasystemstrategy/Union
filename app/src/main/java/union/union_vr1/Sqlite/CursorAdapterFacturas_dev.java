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

import union.union_vr1.R;

/**
 * Created by Usuario on 29/12/2014.
 */
public class CursorAdapterFacturas_dev extends CursorAdapter {


    private LayoutInflater cursorInflater;

    public CursorAdapterFacturas_dev(Context context, Cursor c) {
        super(context, c);

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


            String producto = cursor.getString(cursor.getColumnIndexOrThrow("hd_te_nom_producto"));
            String cantidad = cursor.getString(cursor.getColumnIndexOrThrow("hd_in_cantidad_ope_dev"));
            String precio = cursor.getString(cursor.getColumnIndexOrThrow("hd_re_importe_ope_dev"));
            String acc = "2";
            String mot = cursor.getString(cursor.getColumnIndexOrThrow("hd_in_categoria_ope_dev"));

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


            textViewTitulo.setText(producto);
            textViewSubtitulo.setText("Cantidad : "+ cantidad);
            textViewComment.setText(acc + ", Motivo : " +mot);
            textViewMonto.setText("S/. "+precio);

            linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark1));
            imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_undo));

        }


    }
}
