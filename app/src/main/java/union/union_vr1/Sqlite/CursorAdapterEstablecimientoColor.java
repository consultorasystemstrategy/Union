package union.union_vr1.Sqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import union.union_vr1.AsyncTask.ImportEstadoAuEstablec;
import union.union_vr1.Charts.Line;
import union.union_vr1.R;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.VMovil_Evento_Establec;
import union.union_vr1.Vistas.VMovil_Modificar_Estab;

/**
 * Created by Usuario on 08/12/2014.
 */
public class CursorAdapterEstablecimientoColor extends CursorAdapter {
    int estado_autorizado = -1;
    Context context;
    Cursor cursor = null;
    int estado_au = 0;
    int position = -1;
    private DbAdapter_Temp_Session session;
    private LayoutInflater cursorInflater;
    Utils df = new Utils();
    private DbAdaptert_Evento_Establec dbHelper;
    private static final String TAG = CursorAdapterEstablecimientoColor.class.getSimpleName();

    public CursorAdapterEstablecimientoColor(Context context, Cursor c) {
        super(context, c, true);
        this.context = context;
        cursor = c;


        dbHelper = new DbAdaptert_Evento_Establec(context);
        dbHelper.open();
        session = new DbAdapter_Temp_Session(context);
        session.open();
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

 /*   @Override
    public View getView(int position, View view, ViewGroup parent) {

        *//*cursor.moveToPosition(position);*//*



        return super.getView(position, view, parent);
    }*/

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        /*final LayoutInflater inflater  = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.establecimiento_lista, viewGroup,false);
        return view;*/
        return cursorInflater.inflate(R.layout.establecimiento_lista, viewGroup, false);

    }


    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        /*view = cursorInflater.inflate(R.layout.establecimiento_lista, parent, false);*/
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layoutenter);
        ImageButton imageButtonOp = (ImageButton) view.findViewById(R.id.imgOperaciones);
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


        if (cursor.getCount() > 0) {
            estado_au = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_estado_autorizado));
            final String id_establecimiento = cursor.getString(cursor.getColumnIndex(dbHelper.EE_id_establec));
            final String nombre_establecimiento = cursor.getString(cursor.getColumnIndex(dbHelper.EE_nom_establec));
            String nombre_cliente = cursor.getString(cursor.getColumnIndex(dbHelper.EE_nom_cliente));
            int id_estado_atencion = Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.EE_id_estado_atencion)));
            int numeroOrden = cursor.getInt(cursor.getColumnIndexOrThrow(dbHelper.EE_orden));
            double deudaTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_a_pagar"));
            String dir = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.EE_direccion));
            //float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(dbHelper.EE_rating));


            nombreEstablecimiento.setText(numeroOrden + ". " + nombre_establecimiento);
            nombreEstablecimiento.setSingleLine(false);
            nombreCliente.setText(nombre_cliente);
            deuda.setText("S/. " + df.format(deudaTotal));
            direccion.setText(dir);
            ratingBar.setRating(4);

            switch (id_estado_atencion) {
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

            estado_autorizado = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_estado_autorizado));

            switch (estado_autorizado){
                case 1: //editar
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark1));
                    break;
                case 2://nada
                    break;
                case 3:
                    break;
                case 4://editar
                    linearLayout.setBackgroundColor(context.getResources().getColor(R.color.Dark1));
                    break;
                case 5://editar
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark1));
                    break;
                case 6://nada
                    break;
                case 7:
                    break;
                case 8://editar
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark1));
                    break;
                default:
                    break;
            }
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Log.d(TAG, "ESTADO : " + "" + estado_autorizado + id_establecimiento);

                    switch (estado_autorizado) {
                        case 1: //editar
                            Utils.setToast(context, "No esta autorizado para la venta", R.color.rojo);
                            break;
                        case 2://nada
                            Utils.setToast(context, "No esta autorizado para la venta", R.color.rojo);
                            break;
                        case 3:
                            eleccion(id_establecimiento);
                            break;
                        case 4://editar
                            Utils.setToast(context, "No esta autorizado para la venta", R.color.rojo);
                            break;
                        case 5://editar
                            Utils.setToast(context, "No esta autorizado para la venta", R.color.rojo);
                            break;
                        case 6://nada
                            Utils.setToast(context, "No esta autorizado para la venta", R.color.rojo);
                            break;
                        case 7:
                            eleccion(id_establecimiento);
                            break;
                        case 8://editar
                            Utils.setToast(context, "No esta autorizado para la venta", R.color.rojo);
                            break;
                        default:
                            Utils.setToast(context, "No esta autorizado para la venta", R.color.rojo);
                            break;
                    }
                }
            });


            //CUANDO YA ESTÁ AUTORIZADO, OCULTO LA OPCIÓN DE MODIFICAR
            switch (estado_autorizado){
                case 3:
                    imageButtonOp.setActivated(false);
                    imageButtonOp.setClickable(false);
                    imageButtonOp.setAlpha((float) 0.0);
                    break;
                case 7:
                    imageButtonOp.setActivated(false);
                    imageButtonOp.setClickable(false);
                    imageButtonOp.setAlpha((float) 0.0);
                    break;
                default:
                    break;
            }

            imageButtonOp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setActionOperacion(id_establecimiento);
                }
            });
        }
    }

    private void setActionOperacion(final String idEstablec) {

        String[] items = new String[]{};
        Integer[] icons = new Integer[]{};
        ListAdapter adapter = null;

        //SWITCH

        switch (estado_au) {
            case 1: //editar
                Utils.setToast(context, "No esta autorizado para la venta", R.color.rojo);
                items = new String[]{"Editar", "Refrescar"};
                icons = new Integer[]{android.R.drawable.ic_menu_edit,
                        android.R.drawable.ic_menu_upload};

                adapter = new ArrayAdapterWithIcon(context, items, icons);
                messageDialog(adapter, idEstablec);
                break;
            case 2://nada
                Utils.setToast(context, "No esta autorizado para la venta", R.color.rojo);
                break;
            case 3:
                Utils.setToast(context, "Ya está autorizado.", R.color.verde);
                break;
            case 4://editar
                items = new String[]{"Editar", "Refrescar"};
                icons = new Integer[]{android.R.drawable.ic_menu_edit,
                        android.R.drawable.ic_menu_upload};

                adapter = new ArrayAdapterWithIcon(context, items, icons);
                messageDialog(adapter, idEstablec);
                break;
            case 5://editar
                items = new String[]{"Editar", "Refrescar"};
                icons = new Integer[]{android.R.drawable.ic_menu_edit,
                        android.R.drawable.ic_menu_upload};
                adapter = new ArrayAdapterWithIcon(context, items, icons);
                messageDialog(adapter, idEstablec);
                break;
            case 6://nada
                Utils.setToast((Activity) (context), "No esta autorizado para la venta", R.color.rojo);
                break;
            case 7:
                Utils.setToast(context, "Ya está autorizado.", R.color.verde);
                break;
            case 8://editar
                items = new String[]{"Editar", "Refrescar"};
                icons = new Integer[]{android.R.drawable.ic_menu_edit,
                        android.R.drawable.ic_menu_upload};
                adapter = new ArrayAdapterWithIcon(context, items, icons);
                messageDialog(adapter, idEstablec);
                break;
            default:
                Utils.setToast(context, "Ya está autorizado.", R.color.verde);
                break;
        }


    }

    private void messageDialog(final ListAdapter adapter, final String idEstablec) {
        new AlertDialog.Builder(context).setTitle("Escoge una opcion")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        String itemString = (String) adapter.getItem(item);
                        Toast.makeText(context, "Item Selected: " + itemString + "---" + idEstablec, Toast.LENGTH_SHORT).show();

                        switch (itemString) {
                            case "Editar":
                               context.startActivity(new Intent(context, VMovil_Modificar_Estab.class).putExtra("idEstab",""+idEstablec));
                                break;
                            case "Refrescar":
                                new ImportEstadoAuEstablec((Activity) context).execute("0", idEstablec);
                                break;
                        }
                    }
                }).show();
    }

    private void eleccion(String idEstabl) {
        Intent i = new Intent((context), VMovil_Evento_Establec.class);

        //dbAdapter_temp_barcode_scanner.deleteAll();
        //((MyApplication) this.getApplication()).setIdEstablecimiento(Integer.parseInt(idEstabl));
        session.deleteVariable(2);
        session.createTempSession(2, Integer.parseInt(idEstabl));
        //dbAdapter_temp_barcode_scanner.createTempScanner(Integer.parseInt(idEstabl));
        //session.deleteVariable(2);
        //session.createTempSession(2, Integer.parseInt(idEstabl));

        (context).startActivity(i);
    }
}
