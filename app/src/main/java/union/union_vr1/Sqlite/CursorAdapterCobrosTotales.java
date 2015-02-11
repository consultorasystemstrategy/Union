package union.union_vr1.Sqlite;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;

/**
 * Created by Usuario on 15/12/2014.
 */
public class CursorAdapterCobrosTotales extends CursorAdapter {

    private LayoutInflater cursorInflater;
    DbAdapter_Comprob_Cobro cCobro ;
    public CursorAdapterCobrosTotales (Context context, Cursor c){
        super(context, c);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        cCobro = new DbAdapter_Comprob_Cobro(context);
        cCobro.open();
    }

    private String getDatePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.cobros_totales_lista, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView textViewNombreEstablecimiento= (TextView) view.findViewById(R.id.textViewCobroTotalEstablecimiento);
        TextView textViewFecha = (TextView) view.findViewById(R.id.textViewCobroTotalFecha);
        TextView textViewDocumento = (TextView) view.findViewById(R.id.textViewCobroTotalDocumento);
        LinearLayout linearLayoutColor = (LinearLayout) view.findViewById(R.id.linearLayoutCobroTotalColor);

        TextView textViewDeuda= (TextView) view.findViewById(R.id.textViewCobroTotalMontoDeuda);

        if (cursor.getCount()>0){

            String factotales = cursor.getString(1);
            String nomcliente = cursor.getString(2);
            String localCobro = cursor.getString(5);
            String fechpro = cursor.getString(3);
            String repagar = cursor.getString(4);

            textViewNombreEstablecimiento.setText(localCobro);
            textViewFecha.setText(fechpro);
            textViewDocumento.setText("Documento "+factotales);

            textViewDeuda.setText("S/. " + repagar);

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String fecha_Programada = fechpro;
            try {
                Date dSqlite = df.parse(fecha_Programada);
                Date dSistema = df.parse(getDatePhone());
                if(dSqlite.before(dSistema)){
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.rojo));
                }
                if(dSqlite.after(dSistema)){
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.amarillo));
                }
                if(dSqlite.equals(dSistema)){
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.rojo));
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            }


        }

    }
}
