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
 * Created by Usuario on 22/12/2014.
 */
public class CursorAdapter_Man_Can_Dev extends CursorAdapter {
    DbAdapter_Canjes_Devoluciones dbHelper_Canjes_Dev;

    private LayoutInflater cursorInflater;

    public CursorAdapter_Man_Can_Dev(Context context, Cursor c) {
        super(context, c);
        dbHelper_Canjes_Dev = new DbAdapter_Canjes_Devoluciones(context);
        dbHelper_Canjes_Dev.open();

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

        if (cursor.getCount()>0) {


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
            Double importe = cursor.getDouble(18);

            String textoAdpater = "Producto: "+nomProducto+"," +
                    "\nOperacion: "+tipo_Op+"," +
                    "\nCategoria: "+categoria_op+"," +
                    "\nImporte: "+importe+"," +
                    "\nFecha: "+fecha+"";


            DecimalFormat df= new DecimalFormat("#0.00");

            textViewTitulo.setText(nomProducto);
            textViewSubtitulo.setText(tipo_Op + ",  "+ categoria_op);
            textViewComment.setText(fecha);
            textViewMonto.setText("S/. "+df.format(importe));

            linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark1));
            imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_refresh));

        }
    }
}
