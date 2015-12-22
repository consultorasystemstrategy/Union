package union.union_vr1.Sqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import union.union_vr1.InputFilterMinMax;
import union.union_vr1.R;

/**
 * Created by Usuario on 14/12/2015.
 */
public class CursorAdapterPlanPagos extends CursorAdapter implements DatePickerDialog.OnDateSetListener {

    private LayoutInflater cursorInflater;
    private Context context;
    private FragmentManager fragmentManager;
    private DbAdapter_Temp_Comprob_Cobro dbHelper_TempComprobCobro;
    private long _id_plan_pago_selected = -1;
    private Calendar minDate;
    private Calendar maxDate;
    private Activity activity;
    private Double total;
    private int cuotas;

    private static String TAG = "CURSOR ADAPTER PLAN PAGOS";

    public CursorAdapterPlanPagos(Context context, Cursor c, Calendar minDate, Calendar maxDate, Double total, int cuotas) {

        super(context, c, 0);
        this.context = context;
        dbHelper_TempComprobCobro = new DbAdapter_Temp_Comprob_Cobro(context);
        dbHelper_TempComprobCobro.open();
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.activity= (Activity) context;
        this.total = total;
        this.cuotas = cuotas;
        // Return the fragment manager
        this.fragmentManager= activity.getFragmentManager();
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.item_cuotas, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView fechaCuota = (TextView) view.findViewById(R.id.fecha_cuota);
        TextView precioCuota = (TextView) view.findViewById(R.id.precio_cuota);
        TextView cambiarFecha = (TextView) view.findViewById(R.id.cambiar_fecha);
        TextView textViewDefinido = (TextView) view.findViewById(R.id.fijo);

        long _id = -1;
        String fecha_programada = "2020/01/01";

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", otherSymbols);

        int definido = -1;
        if (cursor.getCount()>0){

            fecha_programada = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Comprob_Cobro.temp_fecha_programada));
            Double monto_pagar = cursor.getDouble(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Comprob_Cobro.temp_monto_a_pagar));
            _id = cursor.getLong(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Comprob_Cobro.temp_id_cob_historial));
            definido = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Temp_Comprob_Cobro.temp_monto_defined));


            switch (definido){
                case 1:
                    textViewDefinido.setText("DEFINIDO");
                    break;
                case 0:
                    textViewDefinido.setText("AUTOMÁTICO");
                    break;
                default:
                    textViewDefinido.setText("AUTOMÁTICO");
                    break;
            }


            String fechaEntendible = fecha_programada;

            try {
                fechaEntendible = getFechaFull(fecha_programada);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d("PARSE EXCEPTION", e.getMessage());
            }


            fechaCuota.setText(fechaEntendible);
            /*try {
                fechaCuota.setText(getFechaFull(fecha_programada));
            } catch (ParseException e) {
                e.printStackTrace();
            }
*/
            precioCuota.setText(df.format(monto_pagar));

        }
        int position = cursor.getPosition();
        cambiarFecha.setTag(position);
        precioCuota.setTag(position);
        final long final_id = _id;
        final String finalFecha_programada = fecha_programada;
        textViewDefinido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id_plan_pago_selected = final_id;

                dbHelper_TempComprobCobro.updateBlock(""+_id_plan_pago_selected, 0);

                swapCursor(dbHelper_TempComprobCobro.fetchAllComprobCobros());
            }
        });

        cambiarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _id_plan_pago_selected = final_id;
                Calendar now = null;
                try {
                    now = getCalendar(finalFecha_programada);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CursorAdapterPlanPagos.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(minDate);
                dpd.setMaxDate(maxDate);
                dpd.show(fragmentManager, "Datepickerdialog");
            }

        });
        final int finalDefinido = definido;
        precioCuota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id_plan_pago_selected = final_id;
                if (finalDefinido ==1){
                    Toast.makeText(context, "YA ESTÁ DEFINIDO", Toast.LENGTH_SHORT).show();

                }else if( dbHelper_TempComprobCobro.fetchCuotasAutomatically().getCount()<=1){
                    Toast.makeText(context, "ÚLTIMA CUOTA AUTOMÁTICA", Toast.LENGTH_SHORT).show();
                }else {
                    dialogEditarCuota(_id_plan_pago_selected, finalFecha_programada).show();
                }
            }
        });


    }

    private Dialog dialogEditarCuota(final long _id, final String fecha) {

        final View layout = View.inflate(context, R.layout.dialog_editar_cuota_plan, null);

        final EditText savedText = ((EditText) layout.findViewById(R.id.editTextPrecioCuota));
        final TextView textVievFecha = ((TextView) layout.findViewById(R.id.textViewFecha));


        try {
            textVievFecha.setText(getFechaFull(fecha));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //maximo valor y minimo valor
        final Double sum = dbHelper_TempComprobCobro.sumAllDefined();

        int min = 0;
        double max = 0;
        max = total - sum;
        double fractionalPart = max % 1;
        int integralPart = (int)(max - fractionalPart);

        savedText.setFilters(new InputFilter[]{new InputFilterMinMax(min,integralPart)});


        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Editar Cuota");
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String texto = null;
                Double value = 0.0;

                texto = savedText.getText().toString().trim();

                if (texto.length()>0){
                    value= Double.parseDouble(texto);
                    dbHelper_TempComprobCobro.updateMontoPanPagos(""+_id, value, 1);
                }
                //AQUI TENGO QUE RECALCULAR LAS CUOTAS CARAJO.
                //DATOS A OBTENER
                //TOTAL                 ok
                //NRO DE CUOTAS         ok
                //OBTENER UN SUM DEL TOTAL D ECUOTAS DEFINITAS POR EL USUARIO
                Double sumDefined = dbHelper_TempComprobCobro.sumAllDefined();
                Log.d(TAG, "SUM DEFINED" + sumDefined);


                //OBTENER UN CURSOR CON LAS CUOTAS NO DEFINIDAS POR EL USUARIO Y ACTUALIZARLAS
                Cursor cursor = dbHelper_TempComprobCobro.fetchCuotasAutomatically();

                if (cursor.getCount()>0) {
                    cursor.moveToFirst();

                    Double monto = total - sumDefined;
                    Double resto = 0.0;
                    Double montoEntero = 0.0;
                    int cuotasAutomaticas = cursor.getCount();

                    resto = monto % cuotasAutomaticas;
                    montoEntero = monto - resto;

                    Log.d(TAG, "MONTO : "+monto);
                    Log.d(TAG, "RESTO : "+resto);
                    Log.d(TAG, "MONTO ENTERO : "+montoEntero);
                    Log.d(TAG, "CUOTAS AUTOMATICAS : "+cuotasAutomaticas);



                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                        Double cuotaAutomatica = 0.0;
                        if (cursor.isLast()) {
                            cuotaAutomatica = montoEntero / cuotasAutomaticas + resto;

                        } else {
                            cuotaAutomatica = montoEntero / cuotasAutomaticas;
                        }
                        Log.d(TAG, "PRECIO AUTOMATICO : "+cuotasAutomaticas);
                        dbHelper_TempComprobCobro.updateMontoPanPagos("" + cursor.getLong(cursor.getColumnIndex("_id")), round(cuotaAutomatica,2), 0);

                    }


                    swapCursor(dbHelper_TempComprobCobro.fetchAllComprobCobros());
                }else {
                    Toast.makeText(context, "TODAS LAS CUOTAS DEFINIDAS", Toast.LENGTH_SHORT).show();
                }





            }
        });

        builder.setView(layout);

        final AlertDialog alertDialog = builder.create();
        savedText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.d("FOCUS", "ALERT YES");
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                } else {
                    Log.d("FOCUS", "ALERT FALSE");
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            }
        });

        return alertDialog;
    }

    private Calendar getCalendar(String fecha) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        cal.setTime(sdf.parse(fecha));
        return  cal;
    }

    private String getFechaFull(String fecha) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = formatter.parse(fecha);
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
        String formatteDate = format.format(date);
        return formatteDate.substring(0, 1).toUpperCase() + formatteDate.substring(1);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(context,"FECHA CAMBIADA", Toast.LENGTH_SHORT).show();
        if (_id_plan_pago_selected!=-1){

            //VALIDAR QUE NO SEA MAYOR A LA SIGUIENTE FECHA.

            //REALIZAR EL UPDATE
            dbHelper_TempComprobCobro.updateFechaPanPagos("" + _id_plan_pago_selected, year + "/" + (++monthOfYear) + "/" + dayOfMonth);
            swapCursor(dbHelper_TempComprobCobro.fetchAllComprobCobros());
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
