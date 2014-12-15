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

/**
 * Created by Usuario on 09/12/2014.
 */
public class CursorAdapterComprobanteVenta extends CursorAdapter {
    private DbAdapter_Comprob_Venta dbHelper;
    public CursorAdapterComprobanteVenta(Context context, Cursor c) {
        super(context, c);
        dbHelper = new DbAdapter_Comprob_Venta(context);
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


        TextView textView = (TextView) view;
        textView.setSingleLine(false);

        WindowManager.LayoutParams layout_791 = new WindowManager.LayoutParams();
        layout_791.width = WindowManager.LayoutParams.MATCH_PARENT;
        layout_791.height  = WindowManager.LayoutParams.WRAP_CONTENT;
        textView.setLayoutParams(layout_791);


        int _id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_id_comprob)));
        String codigo_erp =cursor.getString(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_codigo_erp));
        String numero_documento = cursor.getString(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_num_doc));
        String total = cursor.getString(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_total));
        int id_forma_pago = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_id_forma_pago)));
        int estado = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_estado_comp)));


        String formaPago = null;
        switch (id_forma_pago){
            case 1:
                formaPago = "AL CONTADO";
                break;
            case 2:
                formaPago = "CRÉDITO";
                break;
            case 3:
                formaPago = "NOTA DE CRÉDITO";
                break;
            case 4:
                break;
            default:
                break;
        }

        String nombreEstado =null;
        switch (estado){
            case 0:
                nombreEstado = "ANULADO";
                textView.setTextColor(Color.RED);
                break;
            case 1:
                nombreEstado  = "CANCELADO";
                textView.setTextColor(Color.BLACK);
                break;
            default:
                break;
        }


        String comprobanteVenta = "id : " + _id+
                ",Doc : "+numero_documento+
                "\nTotal : "+ total+
                "\nForma de pago : "+ formaPago+
                "\nEstado : "+ nombreEstado;




        //TextView textView =(TextView) view;

        textView.setText(comprobanteVenta);

    }
}
