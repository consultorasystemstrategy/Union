package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import union.union_vr1.R;

/**
 * Created by kike on 15/01/2016.
 */
public class CursorAdapter_Trans_Detallado extends CursorAdapter {
    //infla dentro de la actividad
    private LayoutInflater cursorInflater;

    public CursorAdapter_Trans_Detallado(Context context, Cursor c) {
        super(context, c);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.activity_vmovil_lista_trans_detallado, viewGroup, false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textoproducto = (TextView) view.findViewById(R.id.textproducto);
        TextView textonumepro = (TextView) view.findViewById(R.id.textnumeropro);

        String producto = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Trans_Detallado.pro_v_nombre));
        String numeropro = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Trans_Detallado.guitran_v_numguia_flex));
/*
        textoproducto.setText("Nombre producto" + producto);
        textoproducto.setText("Numero producto"+numeropro);
  */
        textoproducto.setText("Nombre producto:"+producto);
        textonumepro.setText("Numero producto:"+numeropro);
    }
}
