package union.union_vr1.Sqlite;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import union.union_vr1.R;
import union.union_vr1.Utils.Utils;

/**
 * Created by Steve on 28/02/2016.
 */
public class CursorAdapterAllCV extends CursorAdapter {

    private DbAdapter_Exportacion_Comprobantes dbHelper;
    private LayoutInflater cursorInflater;

    public CursorAdapterAllCV(Context context, Cursor c) {
        super(context, c, true);
        dbHelper = new DbAdapter_Exportacion_Comprobantes(context);
        dbHelper.open();

        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.item_cv_exportacion, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView textViewEstablecimiento = (TextView) view.findViewById(R.id.textViewEstablecimiento);
        TextView textViewComprobante = (TextView) view.findViewById(R.id.textViewComprobante);
        TextView textViewComment = (TextView) view.findViewById(R.id.textViewListaComment);
        TextView textViewMonto = (TextView) view.findViewById(R.id.textViewListaMonto);
        TextView textviewEstadoExport = (TextView) view.findViewById(R.id.textViewEstadoExport);
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
            String nombre_establecimiento = cursor.getString(cursor.getColumnIndex(DbAdaptert_Evento_Establec.EE_nom_establec));
            int estado_exportacion = cursor.getInt(cursor.getColumnIndexOrThrow(Constants._SINCRONIZAR));

            String _id_sid= dbHelper.fetchIDSID_bysqlite(_id);


            switch (estado){
                case 0:
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.rojo));
                    imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_cancel));
                    break;
                case 1:
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark1));
                    imageView.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.ic_action_accept));
                    break;
                default:
                    break;
            }

            switch (estado_exportacion){
                case Constants._CREADO:
                    textviewEstadoExport.setTextColor(context.getResources().getColor(R.color.rojo));
                    textviewEstadoExport.setText(R.string.NoExport);
                    break;
                case Constants._ACTUALIZADO:
                    textviewEstadoExport.setTextColor(context.getResources().getColor(R.color.rojo));
                    textviewEstadoExport.setText(R.string.NoExport);
                    break;
                case Constants._EXPORTADO:
                    textviewEstadoExport.setTextColor(context.getResources().getColor(R.color.azul));
                    textviewEstadoExport.setText(R.string.Exported);
                    break;
                case Constants._IMPORTADO:
                    textviewEstadoExport.setTextColor(context.getResources().getColor(R.color.rojo));
                    textviewEstadoExport.setText(R.string.NoExport);
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





            textViewEstablecimiento.setText(nombre_establecimiento);
            textViewComprobante.setText(tipoDocumento+": "+serie+"-"+Utils.agregarCeros(numero_documento, 8)
                    +"\nID SID: "+_id_sid );
            textViewComment.setText(formaPago);
            textViewMonto.setText("S/. "+ Utils.formatDouble(total));

        }

    }
}
