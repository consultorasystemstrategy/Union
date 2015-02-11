package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;

/**
 * Created by Usuario on 18/12/2014.
 */
public class CursorAdapter_Autorizacion_Cobros extends CursorAdapter {


    private LayoutInflater cursorInflater;

    public CursorAdapter_Autorizacion_Cobros(Context context, Cursor c) {
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

        if (cursor.getCount()>0) {





            String motivo = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_id_motivo_solicitud));
            String estado = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_id_estado_solicitud));
            String referencia= cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_referencia));
            Double monto= cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_montoCredito));


            if(motivo.equals("4")){
                motivo = "Prologa de Pago";
            }
            if(estado.equals("1")){
                estado="Pendiente";
                linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.amarillo));
                imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_make_available_offline));
            }
            if(estado.equals("2")){
                estado="Aprobado";
                linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.verde));
                imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_accept));
            }
            if(estado.equals("4")){
                estado="Anulado";
                linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.rojo));
                imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_cancel));
            }
            if(estado.equals("5")){
                estado="Ejecutado";
                linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark1));
                imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_accept));
            }


            DecimalFormat df= new DecimalFormat("#0.00");



            textViewTitulo.setText(estado);
            textViewSubtitulo.setText("Motivo " + motivo);
            textViewComment.setText("Referencia "+ referencia);
            textViewMonto.setText("S/. "+df.format(monto));

        }





}
}
