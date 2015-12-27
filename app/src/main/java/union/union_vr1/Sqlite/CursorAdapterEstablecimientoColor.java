package union.union_vr1.Sqlite;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import union.union_vr1.R;
import union.union_vr1.Utils.Utils;

/**
 * Created by Usuario on 08/12/2014.
 */
public class CursorAdapterEstablecimientoColor extends CursorAdapter{

    private LayoutInflater cursorInflater;
    Utils df = new Utils();
    private DbAdaptert_Evento_Establec dbHelper;
    public CursorAdapterEstablecimientoColor(Context context, Cursor c) {
        super(context, c, true);
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
        TextView direccion = (TextView) view.findViewById(R.id.textViewDireccion);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating);

/*        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.YELLOW);*/

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.accent), PorterDuff.Mode.SRC_ATOP);


        if (cursor.getCount()>0){

            String id_establecimiento = cursor.getString(cursor.getColumnIndex(dbHelper.EE_id_establec));
            String nombre_establecimiento = cursor.getString(cursor.getColumnIndex(dbHelper.EE_nom_establec));
            String nombre_cliente = cursor.getString(cursor.getColumnIndex(dbHelper.EE_nom_cliente));
            int id_estado_atencion = Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.EE_id_estado_atencion)));
            int numeroOrden = cursor.getInt(cursor.getColumnIndexOrThrow(dbHelper.EE_orden));
            double deudaTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_a_pagar")) ;
            String dir = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.EE_direccion));
            //float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(dbHelper.EE_rating));


            nombreEstablecimiento.setText(numeroOrden + ". " +nombre_establecimiento);
            nombreEstablecimiento.setSingleLine(false);
            nombreCliente.setText(nombre_cliente);
            deuda.setText("S/. "+df.format(deudaTotal));
            direccion.setText(dir);
            ratingBar.setRating(4);

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
