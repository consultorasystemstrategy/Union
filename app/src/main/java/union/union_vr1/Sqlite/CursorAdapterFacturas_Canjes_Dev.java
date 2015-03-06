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
 * Created by Usuario on 30/12/2014.
 */
public class CursorAdapterFacturas_Canjes_Dev extends CursorAdapter{

    private LayoutInflater cursorInflater;
    public CursorAdapterFacturas_Canjes_Dev(Context context, Cursor c) {
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

            String producto = cursor.getString(9);
            String cantidad = cursor.getString(25);
            Double precio = cursor.getDouble(27);
            String idProducto = cursor.getString(4);

            DecimalFormat df= new DecimalFormat("#0.00");
            String textAdapter = "Producto: "+producto+"" +
                    "\nCantidad: "+cantidad+"" +
                    "\nP.U: "+precio+"" +
                    "\nTot: "+precio*cursor.getInt(25)+"" +
                    "\nRef: "+idProducto+"";

            textViewTitulo.setText(producto);
            textViewSubtitulo.setText("Cantidad : "+ cantidad + ", Precio Unitario : S/. "+ precio);
            textViewComment.setText("Referencia "+ idProducto);
            textViewMonto.setText("S/. "+df.format(precio*cursor.getInt(25)));

            linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark1));
            imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_undo));
        }


    }
}
