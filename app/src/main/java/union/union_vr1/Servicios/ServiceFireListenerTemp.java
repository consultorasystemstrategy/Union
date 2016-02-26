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

import union.union_vr1.Objects.ClienteAdded;
import union.union_vr1.Objects.DevolucionEstado;
import union.union_vr1.Objects.EstablecTemp;
import union.union_vr1.Objects.EventoEstablecimiento;
import union.union_vr1.Objects.Exportaciones;
import union.union_vr1.Objects.NuevoEstablecimiento;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Establecimeinto_Historial;
import union.union_vr1.Sqlite.DbAdapter_Exportacion_Comprobantes;
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
    private DbAdapter_Exportacion_Comprobantes dbAdapter_exportacion_comprobantes;
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
        String fecha = Utils.getDatePhone();
        Firebase rootRef = null;
        Firebase nuevoEstablecimientoRefTemp = null;
        Firebase devolucionRef = null;
        Firebase exportacionesRef = null;
        Firebase rutaDetalleRef = null;

        Firebase.setAndroidContext(context);
        rootRef = new Firebase(Constants._APP_ROOT_FIREBASE);
        nuevoEstablecimientoRefTemp = rootRef.child(Constants._CHILD_ESTABLECIMIENTO_TEMPORAL);
        exportacionesRef =  rootRef.child(Constants._CHILD_EXPORTACIONES);
        devolucionRef = rootRef.child(Constants._CHILD_DEVOLUCION);
        rutaDetalleRef = rootRef.child(Constants._CHILD_RUTA_DETALLE);

        Log.d(TAG, "" + "onStartCommand"+nuevoEstablecimientoRefTemp.getKey());
        dbAdapter_temp_session = new DbAdapter_Temp_Session(this);
        dbAdapter_temp_session.open();
        final int idAgente = dbAdapter_temp_session.fetchVarible(1);
        final int idLiquidacion = dbAdapter_temp_session.fetchVarible(3);

        dbAdapter_establecimeinto_historial = new DbAdapter_Establecimeinto_Historial(this);
        dbAdapter_establecimeinto_historial.open();
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(this);
        dbAdaptert_evento_establec.open();
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(this);
        dbAdaptert_evento_establec.open();

        dbAdapter_exportacion_comprobantes = new DbAdapter_Exportacion_Comprobantes(this);
        dbAdapter_exportacion_comprobantes.open();

        Query queryRef = nuevoEstablecimientoRefTemp.orderByChild("fecha").equalTo(fecha);
        Query queryDevolucionRef = devolucionRef.orderByChild("fecha").equalTo(fecha);
        Query queryExportaciones = exportacionesRef.orderByChild("fecha").equalTo(fecha);




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

                    postNotif(titulo, mensaje, color, true);

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

        ChildEventListener handlerDevoluciones;

        final DbAdapter_Temp_Session session;
        session = new DbAdapter_Temp_Session(this);
        session.open();
         handlerDevoluciones = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "ADDED dataSnapshot : " + dataSnapshot.getValue());
                DevolucionEstado devolucionEstado = dataSnapshot.getValue(DevolucionEstado.class);
                Log.d(TAG, "ADDED Monto : " + devolucionEstado.getEstado());
                Log.d(TAG, "ADDED Estado: " + devolucionEstado.getFecha());
                Log.d(TAG, "ADDED Estado: " + devolucionEstado.getAgenteId());

                if (devolucionEstado.getAgenteId() == idAgente && devolucionEstado.getEstado() == 2) {
                    //ES EL AGENTE Y ESTÁ APROBADO

                    session.deleteVariable(Constants.SESSION_ESTADO_DEVOLUCIONES);
                    session.createTempSession(Constants.SESSION_ESTADO_DEVOLUCIONES, 1);
                }
                if (devolucionEstado.getAgenteId() == idAgente && devolucionEstado.getEstado() == 777) {
                    //ES EL AGENTE Y ESTÁ APROBADO

                    session.deleteVariable(Constants.SESSION_ESTADO_DEVOLUCIONES);
                    session.createTempSession(Constants.SESSION_ESTADO_DEVOLUCIONES, 0);
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Log.d(TAG, "ADDED dataSnapshot : " + dataSnapshot.getValue());
                DevolucionEstado devolucionEstado = dataSnapshot.getValue(DevolucionEstado.class);
                Log.d(TAG, "ADDED Monto : " + devolucionEstado.getEstado());
                Log.d(TAG, "ADDED Estado: " + devolucionEstado.getFecha());
                Log.d(TAG, "ADDED Estado: " + devolucionEstado.getAgenteId());

                if (devolucionEstado.getAgenteId() == idAgente && devolucionEstado.getEstado() == 2) {
                    //ES EL AGENTE Y ESTÁ APROBADO

                    session.deleteVariable(Constants.SESSION_ESTADO_DEVOLUCIONES);
                    session.createTempSession(Constants.SESSION_ESTADO_DEVOLUCIONES, 1);
                }
                if (devolucionEstado.getAgenteId() == idAgente && devolucionEstado.getEstado() == 777) {
                    //ES EL AGENTE Y ESTÁ APROBADO

                    session.deleteVariable(Constants.SESSION_ESTADO_DEVOLUCIONES);
                    session.createTempSession(Constants.SESSION_ESTADO_DEVOLUCIONES, 0);
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

        queryDevolucionRef.addChildEventListener(handlerDevoluciones);



        ChildEventListener handlerExportaciones;


        handlerExportaciones = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "ADDED dataSnapshot : " + dataSnapshot.getValue());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "CHANGED dataSnapshot : " + dataSnapshot.getValue());
                Exportaciones exportaciones = dataSnapshot.getValue(Exportaciones.class);
                Log.d(TAG, "CHANGED ESTADO : " + exportaciones.getEstado());
                Log.d(TAG, "CHANGED AGENTE : " + exportaciones.getAgenteId());
                Log.d(TAG, "CHANGED id comprobante: " + exportaciones.getIdComprobante());
                Log.d(TAG, "CHANGED FECHA : " + exportaciones.getFecha());
                Log.d(TAG, "CHANGED LIQUIDACION: " + exportaciones.getLiquidacion());


                if (exportaciones.getAgenteId() == idAgente && exportaciones.getEstado() == 100 && exportaciones.getLiquidacion() == idLiquidacion) {
                    //VA A PONER COMO NO EXPORTADO TODOS LOS REGISTROS


                    //REGISTROS ACTUALIZADOS A NO EXPORT
                    int registrosActualizados = dbAdapter_exportacion_comprobantes.changeEstado(Constants._CREADO, idLiquidacion);
                    Log.d(TAG, "REGISTROS ACDTUALIZDOS A NO EXPORT : "+ registrosActualizados);
                }
                if (exportaciones.getAgenteId() == idAgente && exportaciones.getEstado() == 777) {
                    //VA A PONER COMO EXPORTADO UN REGISTRO ESPECÍFICO

                    //REGISTROS ACTUALIZADOS A NO EXPORT
                    int registrosActualizados1 = dbAdapter_exportacion_comprobantes.changeEstadoToNoExportOne(exportaciones.getIdComprobante());
                    Log.d(TAG, "REGISTRO SID MAPEO :"+ exportaciones.getIdComprobante()+" ACTUALIZDOS A NO EXPORT COUNT : "+ registrosActualizados1);
                }
                if (exportaciones.getAgenteId() == idAgente && exportaciones.getEstado() == 888 && exportaciones.getLiquidacion() == idLiquidacion) {
                    // SI EL REGISTRO ES 888 y EL IDCOMPPROBANTE ES 888
                    if (exportaciones.getIdComprobante() == 0){
                        session.deleteVariable(Constants.SESSION_VALIDACION_REGISTROS_EXPORTADOS);
                        session.createTempSession(Constants.SESSION_VALIDACION_REGISTROS_EXPORTADOS, 0);
                    }else if (exportaciones.getIdComprobante() == 1){
                        session.deleteVariable(Constants.SESSION_VALIDACION_REGISTROS_EXPORTADOS);
                        session.createTempSession(Constants.SESSION_VALIDACION_REGISTROS_EXPORTADOS, 1);
                    }
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

        queryExportaciones.addChildEventListener(handlerExportaciones);

        ChildEventListener handlerRutaDetalle;

        handlerRutaDetalle = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, " ADDED RUTA DETALLE SNAPSHOT : "+dataSnapshot.getValue());

                ClienteAdded clienteAdded = dataSnapshot.getValue(ClienteAdded.class);
                Log.d(TAG, "CLIENTE ADDED IDLIQUIDACION : "+ clienteAdded.getIdLiquidacion());
                Log.d(TAG, "CLIENTE ADDED IDAGENTE : "+ clienteAdded.getIdAgente());
                Log.d(TAG, "CLIENTE ADDED ID ESTABLEC : "+ clienteAdded.getIdEstablecimiento());



                if (clienteAdded.getIdLiquidacion() == idLiquidacion && clienteAdded.getIdAgente() == idAgente){
                    iniciarServicio(ServiceImportClienteRuta.class, Constants.ACTION_IMPORT_SERVICE_RUTA_DETALLE);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, " ADDED RUTA DETALLE SNAPSHOT : "+dataSnapshot.getValue());
                ClienteAdded clienteAdded = dataSnapshot.getValue(ClienteAdded.class);
                Log.d(TAG, "CLIENTE RUTA IDLIQUIDACION : "+ clienteAdded.getIdLiquidacion());
                Log.d(TAG, "CLIENTE RUTA IDAGENTE : "+ clienteAdded.getIdAgente());
                Log.d(TAG, "CLIENTE ADDED ID ESTABLEC : "+ clienteAdded.getIdEstablecimiento());

                if (clienteAdded.getIdLiquidacion() == idLiquidacion && clienteAdded.getIdAgente() == idAgente){
                    iniciarServicio(ServiceImportClienteRuta.class, Constants.ACTION_IMPORT_SERVICE_RUTA_DETALLE);
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

        rutaDetalleRef.addChildEventListener(handlerRutaDetalle);

        //==========================================================================================
        return Service.START_STICKY;
    }

    private void iniciarServicio(Class clase, String action){
        Intent ints = new Intent(this, clase);
        ints.setAction(action);
        startService(ints);
    }

    private void postNotif(String titulo, String mensaje, int estado, boolean startImport) {
//
        if (startImport){

            Intent ints = new Intent(this, ServiceImport.class);
            ints.setAction(Constants.ACTION_IMPORT_SERVICE);
            startService(ints);
        }

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
