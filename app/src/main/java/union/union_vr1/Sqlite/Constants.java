package union.union_vr1.Sqlite;

/**
 * Created by Usuario on 16/01/2015.
 */
public class Constants {

    public static final String _SINCRONIZAR = "estado_sincronizacion";
    public static final int _IMPORTADO = 1;
    public static final int _CREADO = 2;
    public static final int _ACTUALIZADO = 3;
    public static final int _EXPORTADO = 4;

    public static final int _CANJES = 5;
    public static final int _DEVOLUCIONES = 6;
    public static final int POR_EXPORTAR_FLEX = 7;
    public static final int _EXPORTADO_FLEX = 8;

    public static final int _FLEX_ID_DEFECTO = -1;

    public static final int FROM_ACTIVITY = 1;
    public static final int FROM_SERVICE = 2;
    public static final int _ID_ALARM = 6975;

    public static final int DOCUMENTO_TRANSFERENCIA = 1000;
    public static final int DOCUMENTO_FACTURA = 1;
    public static final int DOCUMENTO_BOLETA = 2;


    public static final int DOCUMENTO_ARQUEO = 1001;
    public static final int DOCUMENTO_RRPP = 1002;
    public static final int DOCUMENTO_STOCK_DISPONIBLE = 1003;
    public static final int DOCUMENTO_DEVOLUCIONES_MALAS = 1004;


    public static final int DOCUMENTO_RESUMEN_FINAL = 1005;
    public static final int DOCUMENTO_RESUMEN_PROVISIONAL = 1006;




    public static final int DOCUMENTO_EXTERNO = 700;
    public static final int DOCUMENTO_INTERNO = 701;
    public static final int COBRO_NORMAL = 1;
    public static final int COBRO_MANUAL = 2;
    public static final int COBRO_PARCIAL = 5;
    public static final int COBRO_PARCIAL_EXPORTADO=10;

    public static final int COBRO_ESTADO_PARCIAL=1;
    public static final int COBRO_ESTADO_TOTAL=2;




    public static final int NO_ASIGNADO = -1;


    public static final String ACTION_EXPORT_SERVICE = "EXPORT_SERVICE";
    public static final String ACTION_IMPORT_SERVICE = "IMPORT_SERVICE";
    public static final String ACTION_IMPORT_SERVICE_RUTA_DETALLE = "IMPORT_SERVICE_RUTA_DETALLE";



    public static final String PRINT_AUTORIZADO_= "AUTORIZADO MEDIANTE RESOLUCION";
    public static final String PRINT_N_RESOLUCION= "N. 0180050000804/SUNAT";
    public static final String PRINT_VISUALICE= "Visualice este documento en";
    public static final String PRINT_URL= "http://www.upeu.edu.pe/";

    public static final int FORMA_DE_PAGO_CONTADO = 1;
    public static final int FORMA_DE_PAGO_CREDITO = 2;

    public static final String REPRESENTACION_BOLETA = "REPRESENTACION IMPRESA DE LA BOLETA DE VENTA ELECTRONICA";
    public static final String REPRESENTACION_FACTURA = "REPRESENTACION IMPRESA DE LA FACTURA DE VENTA ELECTRONICA";

    public static final String _SPINNER_DEFAULT_COMPROBANTE = "Tipo de Comprobante";
    public static final String _FACTURA = "Factura";
    public static final String _BOLETA = "Boleta";
    public static final String _FICHA = "Ficha";


    public static final String _SPINNER_DEFAULT_PAGO = "Forma de pago";
    public static final String _CONTADO = "Contado";
    public static final String _CREDITO = "Credito";

    //public static final String _APP_ROOT_FIREBASE = "https://productosunion.firebaseio.com/app";
    public static final String _APP_ROOT_FIREBASE = "https://sidim.firebaseio.com/app";
    public static final String _CHILD_CREDITO = "credito";
    public static final String _CHILD_DEVOLUCION = "devolucion";
    public static final String _CHILD_RUTA_DETALLE= "ruta_detalle";
    public static final String _CHILD_EXPORTACIONES = "exportaciones";


    public static final String _CHILD_ESTABLECIMIENTO_NUEVO = "establecimiento_nuevo";
    public static final String _CHILD_ESTABLECIMIENTO_TEMPORAL = "establecimiento_temporal";



    public static final int SEMAFORO_AMBAR = 1;
    public static final int SEMAFORO_ROJO = 2;
    public static final int SEMAFORO_VERDE = 3;
    public static final int REGISTRO_INTERNET = 5;
    public static final int REGISTRO_CREADO = 5;
    public static final int REGISTRO_APROBADO = 7;
    public static final int REGISTRO_RECHAZADO = 8;

    public static final int REGISTRO_SIN_INTERNET = 10;

    public static final int _CREDITO_PENDIENTE = 1;
    public static final int _CREDITO_APROBADO = 2;
    public static final int _CREDITO_RECHAZADO = 3;

    public static final int ID_NOTIFICATION_FIREBASE = 4650;


    public static final int _DEVOLUCIONES_NO_VALIDADAS = -2;

    public static final int _ID_SID_ESTABLECIMIENTO_TEMPORAL = -1;



    //VARIABLES DE SESSION

    public static final int SESSION_ESTADO_DEVOLUCIONES = 21;
    public static final int SESSION_VALIDACION_REGISTROS_EXPORTADOS = 23;
    public static final int SESSION_DOCUMENTO = 22; //ESTADO -1 = NINGUNO, ESTADO 1 = FACTURA, ESTADO 2 = BOLETA
    public static final int _ID_SESSION_MAC = 12;
    public static final int _ID_SESSION_MAC_DEVICE_CIPHER_LAB = 13;
    public static final int _ID_SESSION_PIN = 24;



    //RESPUESTA SUCCESSS //
    public static final int _ZERO = 0;

    //VENTA DE PRODUCTOS
    public static final int _DIRECCION_ESTABLECIMIENTO = 1;
    public static final int _DIRECCION_FISCAL = 2;
    public static final int _CV_ANULADO = 0;
    public static final int _CV_VALIDO = 1;

    //
    public static final int _SELECTED = 1;

    public static final String _OBSERVACION = "Observacion";















}
