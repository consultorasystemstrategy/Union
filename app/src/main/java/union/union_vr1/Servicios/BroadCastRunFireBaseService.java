package union.union_vr1.Servicios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Kelvin on 14/01/2016.
 */
public class BroadCastRunFireBaseService extends BroadcastReceiver {
    private static String TAG = BroadCastRunFireBaseService.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, ""+context.getClass().getSimpleName());


    }
}
