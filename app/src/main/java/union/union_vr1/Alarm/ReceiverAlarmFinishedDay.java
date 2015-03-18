package union.union_vr1.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Usuario on 18/03/2015.
 */
public class ReceiverAlarmFinishedDay extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ALARMA BROADCAST RECEIVER INICIALIZATED","OK");
        Intent service = new Intent(context, ServiceNotifyResumen.class);
        context.startService(service);
    }
}
