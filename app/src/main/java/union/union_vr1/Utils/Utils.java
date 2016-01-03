package union.union_vr1.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
        return df.format(value);
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
    public static void setToast(Activity activity,String mensaje,int tipoMensaje){
        Toast toast = Toast.makeText(activity.getApplicationContext(),mensaje, Toast.LENGTH_LONG);
        toast.getView().setBackgroundColor(activity.getResources().getColor(tipoMensaje));
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(activity.getResources().getColor(R.color.Blanco));
        toast.show();
    }

}
