package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.TextView;


/**
 * Created by Usuario on 15/12/2014.
 */
public class CursorAdapter_Man_Cbrz extends CursorAdapter {
    private DbAdapter_Comprob_Cobro dbHelper_Comprob_Cobro;
    public CursorAdapter_Man_Cbrz (Context context, Cursor c){
        super(context, c);
        dbHelper_Comprob_Cobro = new DbAdapter_Comprob_Cobro(context);
        dbHelper_Comprob_Cobro.open();

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

       String man_doc = cursor.getString(1);
       String man_cliente = cursor.getString(2);
       String man_fecha= cursor.getString(4);
       String man_hora= cursor.getString(5);
       String man_monto= cursor.getString(6);
       String man_estado= cursor.getString(7);
        String cobros_Man = "Doc: "+man_doc+"," +
                "\nCliente: "+man_cliente+"," +
                "\nFecha: "+man_fecha+"," +
                "\nHora: "+man_hora+"," +
                "\nMonto: "+man_monto+"," +
                "\nEstado: "+man_estado+"";

        textView.setText(cobros_Man);
        String estado = man_estado;
        if (estado.equals("Cobrado")){


            textView.setBackgroundColor(0xff00ff00);

        }else{

            textView.setBackgroundColor(0xffff0000);

        }

    }
}
