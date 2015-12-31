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
import union.union_vr1.Utils.Utils;

/**
 * Created by Usuario on 22/12/2014.
 */
public class CursorAdapter_Man_Can_Dev extends CursorAdapter {


    private LayoutInflater cursorInflater;

    public CursorAdapter_Man_Can_Dev(Context context, Cursor c) {
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

        //" + temp_id_motivo + "='1'


        TextView textViewTitulo = (TextView) view.findViewById(R.id.textViewListaTitulo);
        TextView textViewSubtitulo = (TextView) view.findViewById(R.id.textViewListaSubtitulo);
        TextView textViewComment = (TextView) view.findViewById(R.id.textViewListaComment);
        TextView textViewMonto = (TextView) view.findViewById(R.id.textViewListaMonto);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewLista);

        if (cursor.getCount()>0) {
            String operacion = "";

            int tipoOperacion = cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_motivo));
            if(tipoOperacion==1){
                operacion="Devolucion";
            }else{
                operacion = "Canje";
            }

            imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_accept));
            String nomForma = "";
            String _id_guia = cursor.getString(cursor.getColumnIndex(DBAdapter_Temp_Canjes_Devoluciones.temp_id_canjes_devoluciones));
            String comprobante =cursor.getString(cursor.getColumnIndex(DBAdapter_Temp_Canjes_Devoluciones.temp_id_comprob_venta));
            int forma = cursor.getInt(cursor.getColumnIndex(DBAdapter_Temp_Canjes_Devoluciones.temp_id_forma));
            String importeString = cursor.getString(cursor.getColumnIndex(DBAdapter_Temp_Canjes_Devoluciones.temp_importe));
            Double importe = 0.0;
            if(importeString!=null){
                importe = Double.parseDouble(Utils.replaceComa(importeString));
            }

            if(forma==1){
                nomForma="Normal";
            }else{
                nomForma="Manual";
            }
            DecimalFormat df= new DecimalFormat("#0.00");

            textViewTitulo.setText("COMPROBANTE: " + comprobante +"");
            textViewSubtitulo.setText("ID GUIA: "+_id_guia);
            textViewComment.setText(""+operacion+":  "+nomForma);
            textViewMonto.setText("S/. "+ df.format(importe));

        }

    }
}
