package union.union_vr1.Servicios;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import union.union_vr1.Objects.EstablecTemp;
import union.union_vr1.Objects.NuevoEstablecimiento;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Establecimeinto_Historial;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.VMovil_Menu_Establec;
//FIREBASE


/**
 * Created by Kelvin on 13/01/2016.
 */
public class ServiceFireListenerTemp extends Service {
    //private Firebase rootRef = null;
    //private Firebase nuevoEstablecimientoRefTemp = null;
    private DbAdapter_Establecimeinto_Historial dbAdapter_establecimeinto_historial;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DbAdapter_Temp_Session dbAdapter_temp_session;

    private Context context;


    private static String TAG = ServiceFireListenerTemp.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.d(TAG, "" + "Created");
        context=this;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "" + "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "" + "onStartCommand");
        Firebase rootRef = null;
        Firebase nuevoEstablecimientoRefTemp = null;
        Firebase.setAndroidContext(context);
        rootRef = new Firebase(Constants._APP_ROOT_FIREBASE);
        nuevoEstablecimientoRefTemp = rootRef.child(Constants._CHILD_ESTABLECIMIENTO_TEMPORAL);
        Log.d(TAG, "" + "onStartCommand"+nuevoEstablecimientoRefTemp.getKey());
        dbAdapter_temp_session = new DbAdapter_Temp_Session(this);
        dbAdapter_temp_session.open();
        final int idAgente = dbAdapter_temp_session.fetchVarible(1);
        dbAdapter_establecimeinto_historial = new DbAdapter_Establecimeinto_Historial(this);
        dbAdapter_establecimeinto_historial.open();
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(this);
        dbAdaptert_evento_establec.open();
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(this);
        dbAdaptert_evento_establec.open();

        Query queryRef = nuevoEstablecimientoRefTemp.orderByChild("fecha").equalTo(Utils.getDatePhone());

        ChildEventListener handler;
        handler = new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {



                Log.d(TAG, "ADDED dataSnapshot : " + dataSnapshot.getValue());
                EstablecTemp nuevoEstablecimiento = null;
                nuevoEstablecimiento = dataSnapshot.getValue(EstablecTemp.class);
                Log.d(TAG, "ADDED NRO DOC : " + nuevoEstablecimiento.getIdEstablecTemp());
                Log.d(TAG, "ADDED FECHA : " + nuevoEstablecimiento.getFecha());
                Log.d(TAG, "ADDED ESTADO: " + nuevoEstablecimiento.getEstado());

                String idParent = nuevoEstablecimiento.getIdEstablecTemp();

                if(idAgente==nuevoEstablecimiento.getIdAgente()){
                Log.d(TAG, "ADDED ID ESTABLEC: " + idParent);
                int estadoInserto = dbAdaptert_evento_establec.updateEstabEstado(Integer.parseInt(idParent), "" + nuevoEstablecimiento.getEstado());
                    //ASOCIAR EL TEMPORAL CON EL SID
                    dbAdaptert_evento_establec.updateIDSIDEstablecimiento(Integer.parseInt(idParent), nuevoEstablecimiento.getIdEstablecimientoSID());
                Log.d(TAG, "ADDED ID ESTABLEC: " + estadoInserto);
                String titulo = "", mensaje = "";
                int color = 0;
                mensaje = "C. Doc: " + nuevoEstablecimiento.getIdEstablecTemp();

                    switch (nuevoEstablecimiento.getEstado()){
                        case Constants.REGISTRO_APROBADO:
                            titulo = "Establecimiento Aprobado";
                            color = Color.GREEN;
                            dbAdaptert_evento_establec.updateEstabEstadoAtencion(Integer.parseInt(idParent), 1);

                            break;
                        case Constants.REGISTRO_RECHAZADO:
                            titulo = "Establecimiento Rechazado";
                            color = Color.RED;
                            break;
                    }

                    postNotif(titulo, mensaje, color);

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



        //==========================================================================================


        return Service.START_STICKY;
    }


    private void postNotif(String titulo, String mensaje, int estado) {

        Log.d(TAG, "POST NOTIFICACIÓN ...");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.drawable.ic_launcher;

        Intent intent = new Intent(this, VMovil_Menu_Establec.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n = new Notification.Builder(this)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setSmallIcon(icon)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setLights(estado, 500, 500)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000})
                .setStyle(new Notification.BigTextStyle().bigText("")).build();
        //  .addAction(R.drawable.line, "", pIntent).build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(Constants.ID_NOTIFICATION_FIREBASE, n);
    }



    @Override
    public void onDestroy() {
        Log.d(TAG,"Destroy");
        super.onDestroy();
    }
}
