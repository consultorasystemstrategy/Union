package union.union_vr1.Sqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import union.union_vr1.AsyncTask.CrearEstablecimientoDuplicados;
import union.union_vr1.AsyncTask.ExportEstadoAuEstablec;
import union.union_vr1.R;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.VMovil_Evento_Establec;
import union.union_vr1.Vistas.VMovil_Modificar_Estab;

/**
 * Created by Usuario on 08/12/2014.
 */
public class CursorAdapterEstablecimientoColor extends CursorAdapter {

    private String _id_establecimiento_selected = "";
    private int estado_autorizado = -1;
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
        Button btnEstado = (Button)view.findViewById(R.id.btnTextEstado);

/*        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.YELLOW);*/

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.Dark3), PorterDuff.Mode.SRC_ATOP);


        if (cursor.getCount() > 0) {
            estado_au = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_estado_autorizado));
            String id_establecimiento = cursor.getString(cursor.getColumnIndex(dbHelper.EE_id_actualizar));
            final String idEstablec = cursor.getString(cursor.getColumnIndex(dbHelper.EE_id_establec));
            String nombre_establecimiento = cursor.getString(cursor.getColumnIndex(dbHelper.EE_nom_establec));
            String nombre_cliente = cursor.getString(cursor.getColumnIndex(dbHelper.EE_nom_cliente));
            int id_estado_atencion = Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.EE_id_estado_atencion)));
            int numeroOrden = cursor.getInt(cursor.getColumnIndexOrThrow(dbHelper.EE_orden));
            double deudaTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("cc_re_monto_a_pagar"));
            String dir = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.EE_direccion));
            //float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(dbHelper.EE_rating));


            if(nombre_establecimiento.length()>=36){
                nombre_establecimiento = nombre_establecimiento.substring(0, 32);
                nombre_establecimiento += "...";
            }
            nombreEstablecimiento.setText(numeroOrden + ". " + nombre_establecimiento);
            nombreEstablecimiento.setSingleLine(false);
            nombreCliente.setText(nombre_cliente);
            deuda.setText("S/. " + df.format(deudaTotal));
            direccion.setText(dir);
            ratingBar.setRating(1);


            int estado_autorizado_esta = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_estado_autorizado));
           //COLOR DEL BOTTON DE ESTADO REGISTRO DE ESTABLECIMIENTO:

            switch (estado_autorizado_esta){
                case 5: //Registrado solamente puede Editar.
                    btnEstado.setBackground(context.getResources().getDrawable(R.mipmap.ic_created));
                    btnEstado.setText("C");
                    break;
                case 6://No puede hacer nada su estado esta en pendiente
                    btnEstado.setBackground(context.getResources().getDrawable(R.mipmap.ic_ambar));
                    btnEstado.setText("P");
                    break;
                case 7:// Puede hacer todas las operaciones
                    btnEstado.setBackground(context.getResources().getDrawable(R.mipmap.ic_green));
                    btnEstado.setText("A");
                    break;
                case 8: // No puede hacer nada , esta en rechazado
                    btnEstado.setBackground(context.getResources().getDrawable(R.mipmap.ic_redc));
                    btnEstado.setText("R");
                    break;
                case 10: // No puede hacer nada , esta en rechazado
                    btnEstado.setBackground(context.getResources().getDrawable(R.mipmap.ic_created));
                    btnEstado.setText("C");
                    break;
            }

            switch (id_estado_atencion) {
                case 0:
                    linearLayoutColor.setBackgroundColor(context.getResources().getColor(R.color.Dark5));
                    break;
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


            final String final_id = id_establecimiento;
            final int f_estado_autorizado = estado_autorizado_esta;
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    _id_establecimiento_selected = final_id;
                    estado_autorizado = f_estado_autorizado;
                    switch (estado_autorizado) {
                        case 5: //Puede editar o enviar
                            Utils.setToast(context, "No autorizado", R.color.rojo);
                            break;
                        case 6: //No puede hacer nada
                            Utils.setToast(context, "No autorizado", R.color.rojo);
                            break;
                        case 7: //Normal
                            eleccion(idEstablec);
                            break;
                        case 8: //Puede editar y enviar
                            Utils.setToast(context, "No autorizado", R.color.rojo);
                            break;
                        case 10: //Puede editar y enviar
                            Utils.setToast(context, "No autorizado", R.color.rojo);
                            break;
                    }


                    Log.d(TAG, "ESTADO : " + "" + estado_autorizado + _id_establecimiento_selected);

                }
            });








            imageButtonOp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _id_establecimiento_selected = final_id;
                    estado_autorizado = f_estado_autorizado;
                    setActionOperacion(_id_establecimiento_selected, estado_autorizado,idEstablec);
                }
            });
            imgeButtonView(estado_autorizado, imageButtonOp);

        }
    }


    private void imgeButtonView(int estadoAu, ImageButton imageButton){

        switch (estadoAu) {
            case 5: //Puede editar o enviar
                /*imageButton.setVisibility(View.VISIBLE);
                imageButton.setActivated(true);
                imageButton.setClickable(true);
                imageButton.setAlpha((float) 1.0);*/

                break;
            case 6: //No puede hacer nada
                //imageButton.setVisibility(View.INVISIBLE);
                break;
            case 7: //Normal
                //imageButton.setVisibility(View.INVISIBLE);
                break;
            case 8: //Puede editar y enviar
                /*imageButton.setVisibility(View.VISIBLE);
                imageButton.setActivated(true);
                imageButton.setClickable(true);
                imageButton.setAlpha((float) 1.0);*/
                break;

        }

    }

    private void setActionOperacion(final String idEstablecEditar, final int estado_autorizado, final  String IdEstablec) {

        String[] items = new String[]{};
        Integer[] icons = new Integer[]{};
        ListAdapter adapter = null;

        switch (estado_autorizado) {
            case 5: //Puede editar o enviar
                items = new String[]{"Editar", "Enviar"};
                icons = new Integer[]{android.R.drawable.ic_menu_edit,
                        android.R.drawable.ic_menu_upload};

                adapter = new ArrayAdapterWithIcon(context, items, icons);
                messageDialog(adapter, idEstablecEditar,IdEstablec);

                break;
            case 6: //No puede hacer nada

                break;
            case 7: //Normal

                break;
            case 8: //Puede editar y enviar
                items = new String[]{"Editar", "Reenviar"};
                icons = new Integer[]{android.R.drawable.ic_menu_edit,
                        android.R.drawable.ic_menu_upload};

                adapter = new ArrayAdapterWithIcon(context, items, icons);
                messageDialog(adapter, idEstablecEditar,IdEstablec);


                break;
            case 10: //Puede editar y enviar
                items = new String[]{"Editar", "Enviar"};
                icons = new Integer[]{android.R.drawable.ic_menu_edit,
                        android.R.drawable.ic_menu_upload};

                adapter = new ArrayAdapterWithIcon(context, items, icons);
                messageDialogDuplicado(adapter, idEstablecEditar, IdEstablec);


                break;

        }




    }

    private void messageDialogDuplicado(final ListAdapter adapter, final String idEstablecEditar, final String idEstablec) {
        new AlertDialog.Builder(context).setTitle("Escoge una opcion")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        String itemString = (String) adapter.getItem(item);
                        //  Toast.makeText(context, "Item Selected: " + itemString + "---" + idEstablec, Toast.LENGTH_SHORT).show();

                        switch (itemString) {
                            case "Editar":
                                Log.d("IDSEGUIDO",""+idEstablecEditar);
                                context.startActivity(new Intent(context, VMovil_Modificar_Estab.class).putExtra("idEstab", "" + idEstablecEditar));
                                break;
                            case "Enviar":

                                new CrearEstablecimientoDuplicados((Activity) context).execute("0", idEstablecEditar);
                                break;
                            case "Refrescar":
                               // new ImportEstadoAuEstablec((Activity) context).execute("1", idEstablec);
                                break;
                        }
                    }
                }).show();
    }

    private void messageDialog(final ListAdapter adapter, final String idEstablecEditar, final String idEstablec) {
        new AlertDialog.Builder(context).setTitle("Escoge una opcion")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        String itemString = (String) adapter.getItem(item);
                      //  Toast.makeText(context, "Item Selected: " + itemString + "---" + idEstablec, Toast.LENGTH_SHORT).show();
                        Log.d("IDSEGUIDO",""+idEstablecEditar);
                        switch (itemString) {
                            case "Editar":
                                context.startActivity(new Intent(context, VMovil_Modificar_Estab.class).putExtra("idEstab", "" + idEstablecEditar));
                                break;
                            case "Enviar":
                                new ExportEstadoAuEstablec((Activity) context).execute("0", idEstablecEditar);
                                break;
                            case "Reenviar":
                                new ExportEstadoAuEstablec((Activity) context).execute("1", idEstablec);
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
