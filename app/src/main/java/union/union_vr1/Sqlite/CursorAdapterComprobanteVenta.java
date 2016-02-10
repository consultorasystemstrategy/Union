package union.union_vr1.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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
 * Created by Usuario on 09/12/2014.
 */
public class CursorAdapterComprobanteVenta extends CursorAdapter {
    private DbAdapter_Comprob_Venta dbHelper;
    private LayoutInflater cursorInflater;

    public CursorAdapterComprobanteVenta(Context context, Cursor c) {
        super(context, c);
        dbHelper = new DbAdapter_Comprob_Venta(context);
        dbHelper.open();

        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.lista_personalizada, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView textViewTitulo = (TextView) view.findViewById(R.id.textViewListaTitulo);
        TextView textViewSubtitulo = (TextView) view.findViewById(R.id.textViewListaSubtitulo);
        TextView textViewComment = (TextView) view.findViewById(R.id.textViewListaComment);
        TextView textViewMonto = (TextView) view.findViewById(R.id.textViewListaMonto);
        LinearLayout linearLayoutColor = (LinearLayout) view.findViewById(R.id.linearLayoutLista);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewLista);

        if (cursor.getCount()>0) {

            int _id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_id_comprob)));
            String codigo_erp =cursor.getString(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_codigo_erp));
            String numero_documento = cursor.getString(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_num_doc));
            Double total = cursor.getDouble(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_total));
            int id_forma_pago = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_id_forma_pago)));
            int estado = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_estado_comp)));
            String serie = cursor.getString(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_serie));
            int id_tipo_documento = cursor.getInt(cursor.getColumnIndex(DbAdapter_Comprob_Venta.CV_id_tipo_doc));



            String nombreEstado =null;
            switch (estado){
                case 0:
                    nombreEstado = "ANULADO";
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.rojo));
                    imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_cancel));
                    break;
                case 1:
                    nombreEstado  = "CANCELADO";
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark1));
                    imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_accept));
                    break;
                default:
                    break;
            }

            String tipoDocumento = null;
            switch (id_tipo_documento){
                case 1:
                    tipoDocumento= "FACTURA";
                    break;
                case 2:
                    tipoDocumento= "BOLETA";
                    break;
                default:
                    break;
            }




            String formaPago = null;
            switch (id_forma_pago){
                case 1:
                    formaPago = "AL CONTADO";
                    break;
                case 2:
                    formaPago = "CRÉDITO";
                    if (estado==0){
                        imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_cancel));
                    }else {
                        imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_make_available_offline));
                    }
                    break;
                case 3:
                    formaPago = "NOTA DE CRÉDITO";
                    break;
                case 4:
                    break;
                default:
                    break;
            }


            String comprobanteVenta = "id : " + _id+
                    ",Doc : "+numero_documento+
                    "\nTotal : "+ total+
                    "\nForma de pago : "+ formaPago+
                    "\nEstado : "+ nombreEstado;



            textViewTitulo.setText(tipoDocumento+": "+serie+"-"+agregarCeros(numero_documento,8) );
            textViewSubtitulo.setText(formaPago);
            textViewComment.setText(nombreEstado);
            textViewMonto.setText("S/. "+ Utils.formatDouble(total));

        }

    }
    private static String agregarCeros(String string, int largo)
    {
        String ceros = "";
        int cantidad = largo - string.length();
        if (cantidad >= 1)
        {
            for(int i=0;i<cantidad;i++)
            {
                ceros += "0";
            }
            return (ceros + string);
        }
        else
            return string;
    }
}
