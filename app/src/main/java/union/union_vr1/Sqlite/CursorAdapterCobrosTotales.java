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
 * Created by Usuario on 15/12/2014.
 */
public class CursorAdapterCobrosTotales extends CursorAdapter {
    DbAdapter_Comprob_Cobro cCobro ;
    public CursorAdapterCobrosTotales (Context context, Cursor c){
        super(context, c);
        cCobro = new DbAdapter_Comprob_Cobro(context);
        cCobro.open();
    }

    private String getDatePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        final LayoutInflater inflater  = LayoutInflater.from(context);
        final View view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, viewGroup,false);
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

        String factotales = cursor.getString(1);
        String nomcliente = cursor.getString(2);
        String localCobro = cursor.getString(5);
        String fechpro = cursor.getString(3);
        String repagar = cursor.getString(4);
        String cobrosTotales = "Doc: "+factotales+"," +
                "\nCliente: "+nomcliente+"," +
                "\nEstablec: "+localCobro+"," +
                "\nFecha: "+fechpro+"," +
                "\nMonto: "+repagar+"";
        textView.setText(cobrosTotales);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String fecha_Programada = fechpro;
        try {
            Date dSqlite = df.parse(fecha_Programada);
            Date dSistema = df.parse(getDatePhone());
            if(dSqlite.before(dSistema)){


                textView.setBackgroundColor(0xffff0000);

            }
            if(dSqlite.after(dSistema)){

                textView.setBackgroundColor(0xffffff00);

            }
            if(dSqlite.equals(dSistema)){


                textView.setBackgroundColor(0xffff0000);

            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }



    }
}
