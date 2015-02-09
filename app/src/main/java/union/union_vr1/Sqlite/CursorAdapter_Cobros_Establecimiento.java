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
public class CursorAdapter_Cobros_Establecimiento extends CursorAdapter {


    public CursorAdapter_Cobros_Establecimiento(Context context, Cursor c) {
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
        layout_791.height = WindowManager.LayoutParams.WRAP_CONTENT;
        textView.setLayoutParams(layout_791);


        String doc = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_doc));
        String fecha = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_fecha_programada));
        String monto = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_monto_a_pagar));
        String estado = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_estado_prologa));
        if (estado != null) {
            if (estado.equals("1")) {
                estado = "En Proceso";
            }
            if (estado.equals("2")) {
                estado = "Aprobado";
            }
        }else{
            estado="";
        }
        String cobros_Man =
                "Nro Doc: " + doc + ",             " + estado + " " +
                        "\nFecha: " + fecha + "," +
                        "\nMonto: " + monto + ",";


        textView.setText(cobros_Man);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {

            Date dSqlite = df.parse(fecha);
            Date dSistema = df.parse(getDatePhone());

            if (dSqlite.before(dSistema)) {


                textView.setBackgroundColor(Color.parseColor("#E64A4A"));

            }
            if (dSqlite.after(dSistema)) {


                textView.setBackgroundColor(Color.parseColor("#F7D358"));

            }
            if (dSqlite.equals(dSistema)) {


                textView.setBackgroundColor(Color.parseColor("#E64A4A"));

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }
}
