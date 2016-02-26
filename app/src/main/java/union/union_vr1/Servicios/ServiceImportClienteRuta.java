package union.union_vr1.Servicios;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.JSONParser.ParserEventoEstablecimiento;
import union.union_vr1.JSONParser.ParserPrecio;
import union.union_vr1.Objects.EventoEstablecimiento;
import union.union_vr1.Objects.Precio;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.DisplayToast;
import union.union_vr1.Utils.Utils;

/**
 * Created by Steve on 25/02/2016.
 */
public class ServiceImportClienteRuta extends IntentService {

    //VARIABLES
    StockAgenteRestApi api = null;
    private DbAdapter_Temp_Session session;
    private DbAdapter_Precio dbAdapter_precio;
    private int idLiquidacion;
    private int idAgente;
    private Context context;

    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    public static String TAG = ServiceImportClienteRuta.class.getSimpleName();

    Handler mHandler;
    private static int _MAX = 100;



    public ServiceImportClienteRuta() {
        super("ServiceImportClienteRuta");
        mHandler = new Handler();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = ServiceImportClienteRuta.this;
        inicializarVariables(context);
    }

    private void inicializarVariables(Context context){

        api = new StockAgenteRestApi(context);
        session = new DbAdapter_Temp_Session(context);
        session.open();


        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(context);
        dbAdaptert_evento_establec.open();

        dbAdapter_precio = new DbAdapter_Precio(context);
        dbAdapter_precio.open();


        idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);
        Log.d(TAG, "DATOS SESSION, AGENTE: " + idAgente + " LIQUIDACION: " + idLiquidacion);

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.ACTION_IMPORT_SERVICE_RUTA_DETALLE.equals(action)) {
                importar();

                //SHOW TOAST
                mHandler.post(new DisplayToast(this, "CLIENTE NUEVO AGREGADO."));

            }

        }
    }

    /**
     * Handle action importacion
     */
    private void importar() {
        // TODO : IMPORTACIÓN HERE!

        // Se construye la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_save)
                .setContentTitle("Cliente Agregado a su Ruta")
                .setContentText("Importando...");

        ArrayList<EventoEstablecimiento> eventoEstablecimientos = null;
        ArrayList<Precio> precios = null;

        String fecha = Utils.getDatePhone();

        Log.d(TAG, "IMPORT DATOS REALES idAgente, idLiquidacion, Fecha"+ idAgente + ", " + idLiquidacion + "," + fecha);

        //INICIALIZAR EL PROGRESS DE LA NOTIFICACIÓN EN 0
        builder.setProgress(_MAX, 0, false);
        startForeground(1, builder.build());

        try{



            JSONObject jsonObjectEventoEstablecimiento = api.GetEstablecimeintoXRuta(idLiquidacion, fecha, idAgente);
            Log.d(TAG, "JSON OBJECT EVENTO ESTABLECIMIENTO : " + jsonObjectEventoEstablecimiento.toString());


            builder.setProgress(_MAX, 3, false);
            startForeground(1, builder.build());

            JSONObject jsonObjectPrecio = api.GetPrecioCategoria(idLiquidacion, idAgente);
            Log.d(TAG, "JSON OBJECT PRECIO : " + jsonObjectPrecio.toString());

            builder.setProgress(_MAX, 4, false);
            startForeground(1, builder.build());
            JSONObject jsonObjectRutaDistribucion = api.GetConsultarPlan_Distribucion(idAgente);





            ParserEventoEstablecimiento parserEventoEstablecimiento = new ParserEventoEstablecimiento();
            ParserPrecio parserPrecio = new ParserPrecio(idAgente);

            eventoEstablecimientos = parserEventoEstablecimiento.parserEventoEstablecimiento(jsonObjectEventoEstablecimiento);
            precios = parserPrecio.parserPrecio(jsonObjectPrecio);





            builder.setProgress(_MAX, 7, false);
            for (int i = 0; i < precios.size(); i++) {
                Log.d("PRECIO CATEGORÌA : " + i, "Nombre producto : " + precios.get(i).getNombreProducto());

                boolean existe = dbAdapter_precio.existePrecio(precios.get(i).getIdProducto(), precios.get(i).getIdCategoriaEstablecimiento(), precios.get(i).getValorUnidad());
                Log.d("EXISTE ", "" + existe);
                if (existe) {
                    dbAdapter_precio.updatePrecios(precios.get(i), idAgente);
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    dbAdapter_precio.createPrecios(precios.get(i), idAgente);
                }
            }

            builder.setProgress(_MAX, 8, false);

            for (int i = 0; i < eventoEstablecimientos.size(); i++) {
                Log.d(TAG, "ESTABLECIMIENTOS X RUTAS: " + i+ " Nombre Establecimiento : " + eventoEstablecimientos.get(i).getNombreEstablecimiento() + ", orden : " + eventoEstablecimientos.get(i).getOrden() + ", BARCODE: " + eventoEstablecimientos.get(i).getCodigoBarras()+", ID TIPO CLIENTE :"+eventoEstablecimientos.get(i).getTipoDocCliente());
                boolean existe = dbAdaptert_evento_establec.existeEstablecsById(eventoEstablecimientos.get(i).getIdEstablecimiento());

                Log.d("EXISTE ESTABLECIMIENTO", "" + existe);
                if (existe) {
                    //dbAdapter_comprob_cobro.updateComprobCobros(comprobanteCobros.get(i));
                    dbAdaptert_evento_establec.updateEstablecimientos(eventoEstablecimientos.get(i), idAgente, idLiquidacion);
                } else {
                    //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                    long id = dbAdaptert_evento_establec.createEstablecimientos(eventoEstablecimientos.get(i), idAgente, idLiquidacion,0);

                    Log.d(TAG, "IMPORT INSERT ESTABLECIMIENTO id : " + id);
                }
            }



            builder.setProgress(_MAX, _MAX, false);
            startForeground(1, builder.build());





        }catch (Exception e) {
            Log.d(TAG, "EXCEPTION MESSAGE : " +  e.getMessage());
            Log.d(TAG, "EXCEPTION LOLIZADES MESSAGE : " +  e.getLocalizedMessage());
            Log.d(TAG, "EXCEPTION CAUSE : " +  e.getCause());
            for (int i = 0; i< e.getStackTrace().length; i++){

                Log.d(TAG, "EXCEPTION STACKTRAECE : " +i+", " + e.getStackTrace()[i]);
            }
            Log.d(TAG, "EXCEPTION : " + e);
        }



        // Quitar de primer plano
        stopForeground(true);
    }
}
