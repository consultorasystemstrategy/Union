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
import android.widget.TextView;

import java.util.ArrayList;

import union.union_vr1.R;

/**
 * Created by Usuario on 08/12/2014.
 */
public class CursorAdapterEstablecimientoColor extends CursorAdapter{
    private DbAdaptert_Evento_Establec dbHelper;
    public CursorAdapterEstablecimientoColor(Context context, Cursor c) {
        super(context, c);
        dbHelper = new DbAdaptert_Evento_Establec(context);
        dbHelper.open();

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        final LayoutInflater inflater  = LayoutInflater.from(context);
        final View view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, viewGroup,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

            String id_establecimiento = cursor.getString(cursor.getColumnIndex(DbAdaptert_Evento_Establec.EE_id_establec));
            String nombre_establecimiento = cursor.getString(cursor.getColumnIndex(DbAdaptert_Evento_Establec.EE_nom_establec));
            String nombre_cliente = cursor.getString(cursor.getColumnIndex(DbAdaptert_Evento_Establec.EE_nom_cliente));
            String doc_cliente = cursor.getString(cursor.getColumnIndex(DbAdaptert_Evento_Establec.EE_doc_cliente));
            int id_estado_atencion = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbAdaptert_Evento_Establec.EE_id_estado_atencion)));


            String establecimiento = "Còdigo : "+id_establecimiento+
                    "\nEstablec : "+ nombre_establecimiento+
                    "\nNombre : "+ nombre_cliente +
                    "\nDocum : "+doc_cliente;



            TextView textView = (TextView) view;
            textView.setSingleLine(false);
            textView.setText(establecimiento);

            WindowManager.LayoutParams layout_791 = new WindowManager.LayoutParams();
            layout_791.width = WindowManager.LayoutParams.MATCH_PARENT;
            layout_791.height  = WindowManager.LayoutParams.WRAP_CONTENT;
            textView.setLayoutParams(layout_791);

            //TextView textView =(TextView) view;

            textView.setText(establecimiento);
            switch (id_estado_atencion){
                case 1:
                    textView.setBackgroundColor(Color.BLUE);
                    break;
                case 2:
                    textView.setBackgroundColor(Color.GREEN);
                    break;
                case 3:
                    textView.setBackgroundColor(Color.RED);
                    break;
                case 4:
                    textView.setBackgroundColor(Color.YELLOW);
                    break;

            }
    }
}
