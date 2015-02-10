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
 * Created by Usuario on 15/12/2014.
 */
public class CursorAdapter_Man_Cbrz extends CursorAdapter {
    private DbAdapter_Comprob_Cobro dbHelper_Comprob_Cobro;


    private LayoutInflater cursorInflater;

    public CursorAdapter_Man_Cbrz (Context context, Cursor c){
        super(context, c);
        dbHelper_Comprob_Cobro = new DbAdapter_Comprob_Cobro(context);
        dbHelper_Comprob_Cobro.open();

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


        cursor.moveToFirst();

        if (cursor.getCount()>0) {


            String man_doc = cursor.getString(1);
            String man_cliente = cursor.getString(2);
            String man_fecha= cursor.getString(4);
            String man_hora= cursor.getString(5);
            String man_monto= cursor.getString(6);
            String man_estado= cursor.getString(7);


            String cobros_Man = "Doc: "+man_doc+"," +
                    "\nCliente: "+man_cliente+"," +
                    "\nFecha: "+man_fecha+"," +
                    "\nHora: "+man_hora+"," +
                    "\nMonto: "+man_monto+"," +
                    "\nEstado: "+man_estado+"";

            String estado = man_estado;
            if (estado.equals("Cobrado")){


                linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.verde));
                imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_accept));
            }else{

                linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.rojo));
                imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_cancel));

            }


            DecimalFormat df= new DecimalFormat("#0.00");

            textViewTitulo.setText(man_cliente);
            textViewSubtitulo.setText("Documento : "+man_doc);
            textViewComment.setText( man_fecha + " " + man_hora );
            textViewMonto.setText("S/. "+ man_monto);


        }

    }
}
