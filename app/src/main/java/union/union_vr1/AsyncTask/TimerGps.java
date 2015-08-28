package union.union_vr1.AsyncTask;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

/**
 * Created by Kelvin on 27/08/2015.
 */
public class TimerGps extends TimerTask {
    @Override
    public void run() {
        Log.e("TIMER START", "HOLA MUNDO"+":Timer :D "+ getTimePhone());
    }
    private String getTimePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }
}
