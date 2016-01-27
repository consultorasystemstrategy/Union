package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import union.union_vr1.R;

/**
 * Created by kike on 12/01/2016.
 */
public class CursorAdapter_Dias_Semanas extends CursorAdapter {
    //infla dentro de la actividad
    private LayoutInflater cursorInflater;

    public CursorAdapter_Dias_Semanas(Context context, Cursor c) {
        super(context, c);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.lista_ruta_por_dia, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView textViewTitulo = (TextView) view.findViewById(R.id.txtestablecimiento);
        TextView textViewNombre = (TextView) view.findViewById(R.id.textViewListaNombre);
        //TextView textViewApPaterno = (TextView) view.findViewById(R.id.textViewApPaterno);
        TextView direccion = (TextView) view.findViewById(R.id.textViewApMaterno);


//        TextView textViewSubtitulo = (TextView) view.findViewById(R.id.textViewListaSubtitulo2);




        String establecimiento = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_establecimiento));
        String nombres = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_nombres));
        String apellidospa = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_apPaterno));
        String apellidosma = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_apMaterno));
        String dirdireccion = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Cliente_Ruta.cliente_ruta_direccion));

        textViewTitulo.setText(establecimiento);
        //textViewTitulo.setText("Nombres:"+nombres);
        textViewNombre.setText(apellidospa + " " + apellidosma+ " " +nombres);

        //apellidospa + ". " + apellidosma+ ". " +nombres
                // textViewApPaterno.setText("Ap_Paterno:"+apellidospa);
         direccion.setText(dirdireccion);

    }
}
