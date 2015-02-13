package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import union.union_vr1.R;

/**
 * Created by Usuario on 08/12/2014.
 */
public class CursorAdapterEstablecimientoColor extends CursorAdapter{

    private LayoutInflater cursorInflater;


    private DbAdaptert_Evento_Establec dbHelper;
    public CursorAdapterEstablecimientoColor(Context context, Cursor c) {
        super(context, c);
        dbHelper = new DbAdaptert_Evento_Establec(context);
        dbHelper.open();
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        /*final LayoutInflater inflater  = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.establecimiento_lista, viewGroup,false);
        return view;*/
        return cursorInflater.inflate(R.layout.establecimiento_lista, viewGroup, false);


    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nombreEstablecimiento = (TextView) view.findViewById(R.id.textViewEstablecimientoNombre);
        TextView nombreCliente = (TextView) view.findViewById(R.id.textViewEstablecimientoCliente);
        TextView deuda = (TextView) view.findViewById(R.id.textViewEstablecimientoDeuda);
        LinearLayout linearLayoutColor = (LinearLayout) view.findViewById(R.id.linearLayoutEstablecimientoColor);
        TextView orden = (TextView) view.findViewById(R.id.textViewEstablecimientoOrden);


        if (cursor.getCount()>0){

            String id_establecimiento = cursor.getString(cursor.getColumnIndex("idEstablecimiento"));
            String nombre_establecimiento = cursor.getString(cursor.getColumnIndex("nombreEstablecimiento"));
            String nombre_cliente = cursor.getString(cursor.getColumnIndex("nombrecliente"));
            int id_estado_atencion = Integer.parseInt(cursor.getString(cursor.getColumnIndex("estadoAtencion")));
            int numeroOrden = cursor.getInt(cursor.getColumnIndexOrThrow("orden"));
            double deudaTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("deudaTotal")) ;


            DecimalFormat df= new DecimalFormat("#0.00");



            nombreEstablecimiento.setText(numeroOrden + ". " +nombre_establecimiento);
            nombreEstablecimiento.setSingleLine(false);
            nombreCliente.setText(nombre_cliente);
            orden.setText("");
            deuda.setText("S/. "+df.format(deudaTotal));

            switch (id_estado_atencion){
                case 1:
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.azul));
                    break;
                case 2:
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.verde));
                    break;
                case 3:
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.rojo));
                    break;
                case 4:
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.amarillo));
                    break;

            }
        }
    }
}
