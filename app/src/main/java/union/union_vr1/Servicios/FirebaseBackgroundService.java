package union.union_vr1.Servicios;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import union.union_vr1.Objects.NuevoEstablecimiento;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.VMovil_Menu_Establec;

/**
 * Created by Steve on 13/01/2016.
 */
public class FirebaseBackgroundService extends Service {

    private Firebase f = new Firebase(Constants._APP_ROOT_FIREBASE);
    private Firebase refEstablecimientoNuevo = f.child(Constants._CHILD_ESTABLECIMIENTO_NUEVO);
    private ChildEventListener handler;

    private static String TAG = FirebaseBackgroundService.class.getSimpleName();
    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "ON BIND ...");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "ON CREATE...");

        Query queryRef = refEstablecimientoNuevo.orderByChild("fecha").equalTo(Utils.getDatePhone());
        handler = new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "ADDED dataSnapshot : " + dataSnapshot.getValue());
                NuevoEstablecimiento nuevoEstablecimiento = dataSnapshot.getValue(NuevoEstablecimiento.class);
                Log.d(TAG, "ADDED NRO DOC : " + nuevoEstablecimiento.getNroDoc());
                Log.d(TAG, "ADDED FECHA : " + nuevoEstablecimiento.getFecha());
                Log.d(TAG, "ADDED ESTADO: " + nuevoEstablecimiento.getEstado());

                if(nuevoEstablecimiento.getEstado()==Constants.REGISTRO_APROBADO){


                    //ACTUALIZAR EL ESTABLECIMIENTO.

                    //ACTUALIZO EL CURSOR ADAPTER.


                }



            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        queryRef.addChildEventListener(handler);
    }

    private void postNotif(String notifString) {

        Log.d(TAG, "POST NOTIFICACIÓN ...");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.drawable.ic_launcher;

        Intent intent = new Intent(this, VMovil_Menu_Establec.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Establecimiento Aprobado")
                .setContentText("Mensaje : " + notifString)
                .setSmallIcon(icon)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setStyle(new Notification.BigTextStyle().bigText("")).build();
        //  .addAction(R.drawable.line, "", pIntent).build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(Constants.ID_NOTIFICATION_FIREBASE, n);
    }

}