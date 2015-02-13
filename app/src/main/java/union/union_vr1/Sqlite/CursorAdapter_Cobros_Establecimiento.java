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
 * Created by Usuario on 18/12/2014.
 */
public class CursorAdapter_Cobros_Establecimiento extends CursorAdapter {
    private LayoutInflater cursorInflater;
    private DbAdapter_Comprob_Cobro dbAdapter_comprob_cobro;

    public CursorAdapter_Cobros_Establecimiento(Context context, Cursor c) {
        super(context, c);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(context);
        dbAdapter_comprob_cobro.open();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.cobros_lista, viewGroup, false);
    }



    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView textViewFecha = (TextView) view.findViewById(R.id.textViewCobroFecha);
        TextView textViewDocumento = (TextView) view.findViewById(R.id.textViewCobroDocumento);
        TextView textViewDeuda = (TextView) view.findViewById(R.id.textViewCobroMontoDeuda);
        LinearLayout linearLayoutColor = (LinearLayout) view.findViewById(R.id.linearLayoutCobroColor);

        TextView textViewEstado= (TextView) view.findViewById(R.id.textViewCobroEstado);

        if (cursor.getCount()>0){


            String doc = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_comprob));
            String fecha = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_fecha_programada));
            int monto = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_monto_a_pagar));
            int montoCobrado = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_monto_cobrado));
            String estado = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_estado_prologa));
            monto=monto-montoCobrado;


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

            textViewFecha.setText(fecha);
            textViewFecha.setTextColor(context.getApplicationContext().getResources().getColor(R.color.Dark1));
            textViewDocumento.setText("Documento " + doc);
            textViewDocumento.setTextColor(context.getApplicationContext().getResources().getColor(R.color.Dark1));
            textViewDeuda.setText("S/. " +monto);
            textViewEstado.setText(estado);



            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {

                Date dSqlite = df.parse(fecha);
                Date dSistema = df.parse(getDatePhone());

                if (dSqlite.before(dSistema)) {


                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.rojo));

                }
                if (dSqlite.after(dSistema)) {

                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.amarillo));

                }
                if (dSqlite.equals(dSistema)) {


                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.rojo));

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
