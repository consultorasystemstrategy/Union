package union.union_vr1.Alarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.VMovil_Resumen_Caja;

/**
 * Created by Usuario on 18/03/2015.
 */
public class ServiceNotifyResumen extends Service {

    private DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
    private DbGastos_Ingresos dbHelperGastosIngr;
    private DbAdapter_Temp_Session session;
    private int idLiquidacion = 1;
    private Double pagadoTotal = 0.0;
    private Double cobradoTotal = 0.0;
    private Double totalRuta = 0.0;
    private Double totalPlanta = 0.0;
    private Double aRendir = 0.0;

    Utils df = new Utils();
    private NotificationManager mManager;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        session = new DbAdapter_Temp_Session(this);
        session.open();
        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(this);
        dbAdapter_informe_gastos.open();
        dbHelperGastosIngr =  new DbGastos_Ingresos(this);
        dbHelperGastosIngr.open();

        idLiquidacion = session.fetchVarible(3);

        calcularTotalARendir();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ALARMA SERVICE INICIALIZATED", "OK");

        createNotification(df.format(aRendir));
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void createNotification(String aRendir) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, VMovil_Resumen_Caja.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        // Build notification
        // Actions are just fake

        String textoARendir = "Efectivo a Rendir : S/. "+aRendir;
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Resumen del día")
                .setContentText(textoARendir).setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                /*
                .addAction(R.drawable.ic_launcher, "Call", pIntent)
                .addAction(R.drawable.ic_launcher, "More", pIntent)
                .addAction(R.drawable.ic_launcher, "And more", pIntent)*/

                .setVibrate(pattern)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(new Notification.InboxStyle())
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(Constants._ID_ALARM, noti);
    }


    public String getDayPhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public Double calcularTotalARendir(){
        Cursor cursorResumen = dbHelperGastosIngr.listarIngresosGastos(idLiquidacion);

        for (cursorResumen.moveToFirst(); !cursorResumen.isAfterLast(); cursorResumen.moveToNext()) {
            int n = cursorResumen.getInt(cursorResumen.getColumnIndexOrThrow("n"));
            Double emitido = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("emitidas"));
            Double pagado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("pagado"));
            Double cobrado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("cobrado"));
            //nTotal += n;
            //emitidoTotal += emitido;
            pagadoTotal += pagado;
            cobradoTotal += cobrado;

        }

        Cursor cursorTotalGastos =dbAdapter_informe_gastos.resumenInformeGastos(getDayPhone());

        for (cursorTotalGastos.moveToFirst(); !cursorTotalGastos.isAfterLast(); cursorTotalGastos.moveToNext()){
            Double rutaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("RUTA"));
            Double plantaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("PLANTA"));

            totalRuta += rutaGasto;
            totalPlanta += plantaGasto;
        }

        Double ingresosTotales = cobradoTotal + pagadoTotal;
        Double gastosTotales = totalRuta;
        aRendir = ingresosTotales-gastosTotales;
        return aRendir;
    }
}
