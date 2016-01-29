package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import union.union_vr1.R;
import union.union_vr1.Utils.Utils;

/**
 * Created by Usuario on 29/12/2014.
 */
public class CursorAdapterFacturas extends CursorAdapter {

    private LayoutInflater cursorInflater;

    public CursorAdapterFacturas(Context context, Cursor c) {
        super(context, c);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.item_canjes_devoluciones, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        DecimalFormat df = new DecimalFormat("0.00");
        TextView textViewListaTituloOperacion = (TextView) view.findViewById(R.id.textViewListaTituloOperacion);
        TextView textViewCantidadOperacion = (TextView) view.findViewById(R.id.textViewCantidadOperacion);
        TextView textViewPrecioUnitario = (TextView) view.findViewById(R.id.textViewPrecioUnitario);
        TextView textViewReferencia = (TextView) view.findViewById(R.id.textViewReferencia);
        TextView textViewImporteTotal = (TextView) view.findViewById(R.id.textViewImporteTotal);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewLista);

        String nombreProducto = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_nom_producto));
        String cantidad = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_cantidad));
        String precioUnitario = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_precio_unit));
        Double importe = cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_importe));
        String referencia = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_lote));

        textViewListaTituloOperacion.setText("Producto: "+nombreProducto);
        textViewCantidadOperacion.setText("Cantidad: "+cantidad);
        textViewPrecioUnitario.setText("Precio Unitario: "+precioUnitario);
        textViewReferencia.setText("Referencia Lote: "+referencia);
        textViewImporteTotal.setText("Importe: "+ Utils.formatDouble(importe)+"");
        if(cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_motivo))==1){
            imageView.setImageDrawable(null);
            imageView.setBackgroundResource(R.drawable.ic_action_undo);
        }



    }
}
