package union.union_vr1.Utils;

import android.util.Log;

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


}
