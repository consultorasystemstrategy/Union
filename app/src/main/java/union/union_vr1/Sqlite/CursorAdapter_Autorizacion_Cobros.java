package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Usuario on 18/12/2014.
 */
public class CursorAdapter_Autorizacion_Cobros extends CursorAdapter {


    public CursorAdapter_Autorizacion_Cobros(Context context, Cursor c) {
        super(context, c);



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
        layout_791.height  = WindowManager.LayoutParams.WRAP_CONTENT;
        textView.setLayoutParams(layout_791);



        String motivo = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_id_motivo_solicitud));
        String estado = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_id_estado_solicitud));
        String referencia= cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_referencia));
        String monto= cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_montoCredito));
        if(motivo.equals("4")){
            motivo = "Prologa de Pago";
        }
        if(estado.equals("1")){
            estado="Pendiente";
            textView.setBackgroundColor(Color.parseColor("#F6BC48"));
        }
        if(estado.equals("2")){
            estado="Aprobado";
            textView.setBackgroundColor(Color.parseColor("#00FF80"));
        }
        if(estado.equals("4")){
            estado="Anulado";
            textView.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        if(estado.equals("5")){
            estado="Ejecutado";
            textView.setBackgroundColor(Color.parseColor("#4000FF"));
        }

        String autorizacion =
                "Motivo: "+motivo+"," +
                "\nEstado: "+estado+"," +
                "\nReferencia: "+referencia+"," +
                "\nMonto:"+monto;


        textView.setText(autorizacion);





}
}
