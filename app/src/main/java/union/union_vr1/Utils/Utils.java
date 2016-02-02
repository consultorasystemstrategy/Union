package union.union_vr1.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import union.union_vr1.R;

/**
 * Created by Usuario on 26/03/2015.
 */
public class Utils {

    private static String TAG = Utils.class.getSimpleName();

    public String getDayPhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public String format(Double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return replaceComa(df.format(value));
    }
    public static String formatDouble(Double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return replaceComa(df.format(value));
    }

    public static String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    public static String getTimePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }

    public static boolean isSuccesfulImport(JSONObject jsonObj) {
        boolean succesful = false;
        try {
            Log.d(TAG, "CADEMA A ÁRSEAR BOOLEAN " + jsonObj.toString());
            succesful = jsonObj.getBoolean("Successful");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "JSONParser => parser Error Message" + e.getMessage());
            Log.d(TAG, "JSON PARSER => parser Error Message" + e.getMessage());
        }
        return succesful;
    }

    public static boolean isSuccesful(JSONObject jsonObj) {
        boolean succesful = false;
        try {
            Log.d("CADENA JSON ", jsonObj.toString());
            succesful = jsonObj.getBoolean("Successful");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSON PARSER ERROR", e.getMessage());
        }
        return succesful;
    }

    public static String getDatePhoneConvert(String date) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy/mmm/dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd/mm/yyyy");
        String fechaR = "";
        try {
            Date fecha = format1.parse(date);
            fechaR = format2.format(fecha);
        } catch (ParseException ex) {

        }
        return fechaR;
    }
    public static String getDatePhoneConvertFormat(String date) {
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy/mm/dd");
        SimpleDateFormat format1 = new SimpleDateFormat("dd/mm/yyyy");
        String fechaR = "";
        try {
            Date fecha = format1.parse(date);
            fechaR = format2.format(fecha);
        } catch (ParseException ex) {

        }
        Log.d("FECHA",""+fechaR);
        return fechaR;
    }
    public static String format(String date) {
        SimpleDateFormat format2 = new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat format1 = new SimpleDateFormat("dd/mm/yyyy");
        String fechaR = "";
        try {
            Date fecha = format2.parse(date);
            fechaR = format1.format(fecha);
        } catch (ParseException ex) {

        }
        Log.d("FECHA",""+fechaR);
        return fechaR;
    }


    public static String replaceComa(String string) {
        String original = ",";
        String ascii = ".";
        if (string != null) {
            //Recorro la cadena y remplazo los caracteres originales por aquellos sin acentos
            for (int i = 0; i < original.length(); i++ ) {
                string = string.replace(original.charAt(i), ascii.charAt(i));
            }
        }
        return string;
    }
    public static void setToast(Context context,String mensaje,int tipoMensaje){
        Toast toast = Toast.makeText(context.getApplicationContext(),mensaje, Toast.LENGTH_LONG);
        toast.getView().setBackgroundColor(context.getResources().getColor(tipoMensaje));
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(context.getResources().getColor(R.color.Blanco));
        toast.show();
    }


    /**
     * get bluetooth local device name
     * @return device name String
     */
    public static String getLocalBluetoothName() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // if device does not support Bluetooth
        if(mBluetoothAdapter==null){
            Log.d(TAG,"device does not support bluetooth");
            return null;
        }

        return mBluetoothAdapter.getName();
    }

    /**
     * get bluetooth adapter MAC address
     * @return MAC address String
     */
    public static String getBluetoothMacAddress() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // if device does not support Bluetooth
        if(mBluetoothAdapter==null){
            Log.d(TAG,"device does not support bluetooth");
            return null;
        }

        return mBluetoothAdapter.getAddress();
    }

    public static String sumarRestarDiasFecha(Date fecha, int dias) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fecha); // Configuramos la fecha que se recibe

        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        return df.format(calendar.getTime()); // Devuelve el objeto Date con los nuevos días añadidos

    }

    public static Date getDateConvert(String fecha) {
        Calendar cal = new GregorianCalendar();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        cal.set(2016, 01, 11);
        Date date = cal.getTime();
        try {
            date = df.parse(fecha);
        } catch (ParseException ex) {
           Log.e(TAG,""+ex.getMessage());
        }
        return date;

    }




    public static  int JSONResult(JSONObject jsonObj) {
        int idRespuesta = -1;
        try {
            Log.d("CADENA A PARSEAR ", jsonObj.toString());
            idRespuesta = jsonObj.getInt("Value");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONPARSER ERROR", e.getMessage());
        }
        return idRespuesta;
    }

    public static  String jsonGetString(JSONObject jsonObj) {
        String stringRespuesta = "";
        try {
            Log.d("CADENA A PARSEAR ", jsonObj.toString());
            stringRespuesta = jsonObj.getString("Value");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONPARSER ERROR", e.getMessage());
        }
        return stringRespuesta;
    }



    /**
     * Utils.dialogNoInternet(this).show();
     */
    public static Dialog dialogNoInternet(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No se Detectó Conexión a Internet");
        builder.setMessage("Por favor, conectarse a una red Wifi o habilitar la conexión a datos.");
        builder.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }

    /**
     * Utils.dialogCambiarFecha(this).show();
     */

    public static Dialog dialogCambiarFecha(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Fecha Sin Actualizar");
        builder.setMessage("Por favor, actualice la fecha del dispositivo.");
        builder.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }
    public static String getDateFull() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
        String formatteDate = format.format(date);
        return formatteDate.substring(0, 1).toUpperCase() + formatteDate.substring(1);
    }
}
