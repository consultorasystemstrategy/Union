package union.union_vr1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.IOException;
import java.util.Vector;

import jpos.JposException;
import jpos.POSPrinterConst;
import union.union_vr1.BlueTooth.AlertView;
import union.union_vr1.BlueTooth.Print;
import union.union_vr1.FacturacionElectronica.NumberToLetterConverter;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Agente_Login;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;
import union.union_vr1.Vistas.VMovil_Evento_Establec;
import union.union_vr1.Vistas.VMovil_Evento_Indice;

public class VMovil_BluetoothImprimir extends Activity implements View.OnClickListener{


    private String nameImpresora = "";
    private String defaultNameImpresora = "mobile printer";
    //private String nameImpresora = "Star Micronics";
    private Button buttonImprimir;
    private Button buttonSincronizar;
    private TextView textViewImprimirCabecera;
    private TextView textViewVentaCabecera;
    private TextView textViewImprimirContenidoLeft;
    private TextView textViewImprimirContenidoRight;
    private TextView textViewContenidoBottom;


    private int idLiquidacion;

    private String textoImpresion = "";
    private String textoImpresionCabecera = "";
    private String textoVentaImpresion = "";
/*    private String textoImpresionContenidoLeft = "";
    private String textoImpresionContenidoRight = "";*/


/*    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;*/

    //private Context contexto;


    private boolean isSincronized= false;
    private boolean estado = false;
    private boolean isConnected = false;
    private int countImpresion = 0;
    private int idComprobante = 0;

    private boolean enabledBluetooth = false;

    private DbAdapter_Comprob_Venta dbHelperComprobanteVenta;
    private DbAdapter_Agente_Login dbAdapter_agente_login;
    private DbAdapter_Comprob_Venta_Detalle dbHelperComprobanteVentaDetalle;
    private DbAdapter_Temp_Session session;
    private DbAdapter_Comprob_Cobro dbAdapter_comprob_cobro;
    private int pulgadasImpresora=0;


    //PANTALLA

    private String textoEmpresa = "\n"
            +"    UNIVERSIDAD PERUANA UNION   \n\n"
            +"     Cent.aplc. Prod. Union     \n"
            +"   C. Central Km 19 Villa Union \n"
            +" Lurigancho-Chosica Fax: 6186311\n"
            +"      Telf: 6186309-6186310     \n"
            +" Casilla 3564, Lima 1, LIMA PERU\n"
            +"         RUC: 20138122256       ";
    private String ventaCabecera="";
    private String textoImpresionContenidoLeft = "";
    private String textoImpresionContenidoRight = "";
    private String textoImpresionContenidoBottom = "";



    //BLUETOOTH VARIABLES
    private String defaultAdressImpresora = "00:12:6F:XX:XX:XX";
    // Intent request codes
    // private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    //ArrayAdapter<String> adapter;
    private BluetoothAdapter mBluetoothAdapter;
    private Vector<BluetoothDevice> remoteDevices;
    /*private BroadcastReceiver searchFinish;
    private BroadcastReceiver searchStart;
    private BroadcastReceiver discoveryResult;
    */
    private Thread hThread;
    private boolean connected;
    private Context contexto;
    // UI
    /*private EditText editText;
    private Button connectButton;
    private Button searchButton;
    private ListView list;*/
    // BT
    private BluetoothPort bp;

    Utils df = new Utils();


    private static String TAG = VMovil_BluetoothImprimir.class.getSimpleName();
    @Override
    public void onBackPressed() {
        //super.onStop();
        super.onBackPressed();
        Intent intent = new Intent(this, VMovil_Evento_Establec.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        if((hThread != null) && (hThread.isAlive()))
        {
            hThread.interrupt();
            hThread = null;
        }
//		if(mBluetoothAdapter.enable())
//			mBluetoothAdapter.disable();
       /* unregisterReceiver(searchFinish);
        unregisterReceiver(searchStart);
        unregisterReceiver(discoveryResult);*/
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__bluetooth_imprimir);

        bluetoothSetup();
        contexto = this;


        dbHelperComprobanteVenta = new DbAdapter_Comprob_Venta(this);
        dbHelperComprobanteVentaDetalle = new DbAdapter_Comprob_Venta_Detalle(this);
        dbHelperComprobanteVenta.open();
        dbHelperComprobanteVentaDetalle.open();
        dbAdapter_agente_login = new DbAdapter_Agente_Login(this);
        dbAdapter_agente_login.open();
        session = new DbAdapter_Temp_Session(this);
        session.open();
        dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(this);
        dbAdapter_comprob_cobro.open();


        idLiquidacion = session.fetchVarible(3);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        nameImpresora= SP.getString("impresoraNombre", defaultNameImpresora);
        pulgadasImpresora= Integer.parseInt(SP.getString("impresoraAncho", "3"));

       /* Cursor cursor = dbAdapter_agente_login.fetchAgenteMAC(idLiquidacion);
        cursor.moveToFirst();
        Log.d(TAG, "CURSOR COUNT MAC : "+cursor.getCount());
        if (cursor.getCount()>0){
            defaultAdressImpresora = cursor.getString(cursor.getColumnIndexOrThrow(dbAdapter_agente_login.AG_MAC));
        }*/

        defaultAdressImpresora = session.fetchMAC(Constants._ID_SESSION_MAC);

        Log.d(TAG, "ADRESS IMPRESORA : " +defaultAdressImpresora);


        buttonImprimir = (Button) findViewById(R.id.buttonImprimir);
        buttonSincronizar = (Button) findViewById(R.id.buttonSincronizar);
        textViewImprimirCabecera = (TextView) findViewById(R.id.textViewImprimirCabecera);
        textViewImprimirContenidoLeft = (TextView) findViewById(R.id.textViewImprimirContenidoLeft);
        textViewImprimirContenidoRight = (TextView) findViewById(R.id.textViewImprimirContenidoRight);
        textViewContenidoBottom = (TextView) findViewById(R.id.textViewBottom);
        textViewVentaCabecera = (TextView) findViewById(R.id.textViewVentaCabecera);



        idComprobante = getIntent().getExtras().getInt("idComprobante");
        Log.d(TAG, "ID_COMPROBANTE : "+idComprobante);
        textoImpresion = generarTextoImpresion(idComprobante, pulgadasImpresora);

        Log.d(TAG, "TEXTO IMPRESION : " + textoImpresion);
        /*textoImpresion = getIntent().getExtras().getString("textoImpresion");
        textoImpresionCabecera = getIntent().getExtras().getString("textoImpresionCabecera");
        textoImpresionContenidoLeft = getIntent().getExtras().getString("textoImpresionContenidoLeft");
        textoImpresionContenidoRight = getIntent().getExtras().getString("textoImpresionContenidoRight");
        textoVentaImpresion = getIntent().getExtras().getString("textoVentaImpresion");*/

        textViewImprimirCabecera.setText(textoEmpresa);
        textViewVentaCabecera.setText(ventaCabecera);
        textViewImprimirContenidoLeft.setText(textoImpresionContenidoLeft);
        textViewImprimirContenidoRight.setText(textoImpresionContenidoRight);
        textViewContenidoBottom.setText(textoImpresionContenidoBottom);
        Log.d(TAG, "TEXTO textoEmpresa : " + textoEmpresa);
        Log.d(TAG, "TEXTO ventaCabecera : " + ventaCabecera);
        Log.d(TAG, "TEXTO textoImpresionContenidoLeft : " + textoImpresionContenidoLeft);
        Log.d(TAG, "TEXTO textoImpresionContenidoRight : " + textoImpresionContenidoRight);



        if (!connected){
            buttonImprimir.setEnabled(connected);
            buttonImprimir.setAlpha((float) 0.0);
            buttonImprimir.setBackgroundColor(getApplication().getResources().getColor(R.color.PersonalizadoSteve4));
        }

        buttonImprimir.setOnClickListener(this);
        buttonSincronizar.setOnClickListener(this);


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        /*mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Log.d("Bluetooth message","No bluetooth adapter available");
        }*/

        /*if (!mBluetoothAdapter.isEnabled()) {
            //enabledBluetooth = true;
         *//*   Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
*//*
        }
        *//*mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Log.d("Bluetooth message","No bluetooth adapter available");
        }

        if (!mBluetoothAdapter.isEnabled()) {
            estado = true;
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);

        }*//*
        *//*buttonSincronizar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(contexto, "Procesando ...", Toast.LENGTH_SHORT).show();
                return false;
            }
        });*//*
*/

    }

   private String generarTextoImpresion(int idComprobante, int pulgadas){


       String texto= "";
       String comprobante = "WS";
       String fecha = "";
       String cliente = "";
       String dni_ruc = "";
       String serie  ="XS";
       String numDoc = "";
       String nombreAgente = "";
       String direccion="";
       String sha1="";
       String tipoC="";

       double base_imponible = 0.0;
       double igv = 0.0;
       double precio_venta = 0.0;
       String ventaDetalle = "";
       int tipoVenta = -1;
       int idFormaPago = -1;

       Cursor cursorVentaCabecera = dbHelperComprobanteVenta.getVentaCabecerabyID(idComprobante);

       Log.d(TAG, " COUNT CURSOR VENTA CABECERA : "+ cursorVentaCabecera.getCount());

       if (cursorVentaCabecera.getCount()>0){
           cursorVentaCabecera.moveToFirst();

           serie = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_serie));
           numDoc = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_num_doc));
           comprobante = serie + "-" +agregarCeros((String.valueOf(numDoc)),8);
           tipoC=comprobante.substring(0,1);
           Log.d(TAG, "SERIE : "+serie);
           Log.d(TAG, "NRMDOC : "+numDoc
           );
           Log.d(TAG, "COMPROBANTE : "+comprobante);
           fecha = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_fecha_doc));
           cliente = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_nom_cliente));
           dni_ruc = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_doc_cliente));
           nombreAgente = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Agente.AG_nombre_agente));
           direccion = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_direccion));
           sha1 = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_SHA1));
           tipoVenta = cursorVentaCabecera.getInt(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_id_tipo_venta));
           idFormaPago = cursorVentaCabecera.getInt(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_id_forma_pago));


           ventaCabecera+= "NUMERO  : "+comprobante+"\n";
           ventaCabecera+= "FECHA   : "+ fecha+"\n";
           ventaCabecera+= "CLIENTE : "+ cliente+"\n";
           ventaCabecera+= "DNI/RUC : "+ dni_ruc+"\n";
           ventaCabecera+= "DIRECCION : "+ direccion+"\n";





           Cursor cursorVentaDetalle = dbHelperComprobanteVentaDetalle.fetchAllComprobVentaDetalleByIdComp(idComprobante);

           Log.d(TAG, "COUNT DETALLES : "+cursorVentaDetalle.getCount());

           if (cursorVentaCabecera.getCount()>0){

               for (cursorVentaDetalle.moveToFirst(); !cursorVentaDetalle.isAfterLast();cursorVentaDetalle.moveToNext()) {

                   int cantidad = cursorVentaDetalle.getInt(cursorVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_cantidad));
                   String nombreProducto = cursorVentaDetalle.getString(cursorVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_nom_producto));
                   Double precioUnitario = cursorVentaDetalle.getDouble(cursorVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_precio_unit));
                   Double importe = cursorVentaDetalle.getDouble(cursorVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_importe));

                  /* texto += "CANT:"+cantidad+"\n";
                   texto += "NOM:"+nombreProducto+"\n";
                   texto += "PU:"+ df.format(precioUnitario)+"\n";
                   texto += "IMP:"+df.format(importe)+"\n";
                   texto += "-------------------\n";*/

                   if (pulgadas==2){
                       if(nombreProducto.length()>=20){
                           nombreProducto=nombreProducto.substring(0,18);
                           nombreProducto+="..";
                       }
                   }else if (pulgadas==3){
                       if(nombreProducto.length()>=30){
                           nombreProducto=nombreProducto.substring(0,28);
                           nombreProducto+="..";
                       }
                   }
                   ventaDetalle+=String.format("%-4s",cantidad) + String.format("%-31s",nombreProducto)+String.format("%1$5s"  ,df.format(precioUnitario)) +String.format("%1$8s"  ,df.format(importe)) + "\n";
                   textoImpresionContenidoLeft +=String.format("%-6s",cantidad) + String.format("%-28s",nombreProducto)+ "\n";
                   textoImpresionContenidoRight+= String.format("%-5s",df.format(importe)) + "\n";


               }

               textoImpresionContenidoLeft+="\nOP. GRAVADA      S/.\n";
               textoImpresionContenidoLeft+="OP. INAFECTA      S/.\n";
               textoImpresionContenidoLeft+="OP. EXONERADA S/.\n";
               textoImpresionContenidoLeft+="OP. GRATUITA     S/.\n";
               textoImpresionContenidoLeft+="DESCUENTOS      S/.\n";
               textoImpresionContenidoLeft+="IGV                          S/.\n";
               textoImpresionContenidoLeft+="PRECIO VENTA   " +
                       "S/.";

           }

           //texto += "\n";
           base_imponible = cursorVentaCabecera.getDouble(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_base_imp));
           igv = cursorVentaCabecera.getDouble(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_igv));
           precio_venta = cursorVentaCabecera.getDouble(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_total));


           textoImpresionContenidoRight+= "\n" +df.format(base_imponible)+"\n";
           textoImpresionContenidoRight+= "0.00"+"\n";
           textoImpresionContenidoRight+= "0.00"+"\n";
           textoImpresionContenidoRight+= "0.00"+"\n";
           textoImpresionContenidoRight+= "0.00"+"\n";
           textoImpresionContenidoRight+= df.format(igv)+"\n";
           textoImpresionContenidoRight+= df.format(precio_venta);


           textoImpresionContenidoBottom+= NumberToLetterConverter.convertNumberToLetter(precio_venta).toUpperCase() +"\n";

           switch (idFormaPago){
               case Constants.FORMA_DE_PAGO_CONTADO:
                   textoImpresionContenidoBottom+= "\nVENTA AL CONTADO"+"\n";
                   break;
               case Constants.FORMA_DE_PAGO_CREDITO:
                   textoImpresionContenidoBottom+= "\nVENTA AL CRÉDITO"+"\n\n";
                   Cursor cursorCredito = dbAdapter_comprob_cobro.fetchComprobCobrosByIdComprobante(idComprobante);
                   if (cursorCredito.getCount()>0){
                       cursorCredito.moveToFirst();
                       /*for (cursorCredito.moveToFirst(); !cursorCredito.isAfterLast() ; cursorCredito.moveToNext()){*/
                           String primeraFechaCobro = cursorCredito.getString(cursorCredito.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_fecha_programada));
                           Double monto_Pagar = cursorCredito.getDouble(cursorCredito.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_monto_a_pagar));

                           textoImpresionContenidoBottom+= String.format("%-14s", primeraFechaCobro) + String.format("%1$10s", df.format(monto_Pagar)) +"\n";
                       /*}*/

                       textoImpresionContenidoBottom+= "\n";
                       textoImpresionContenidoBottom+= "____________________"+"\n";
                       textoImpresionContenidoBottom+= "Firma del cliente"+"\n\n";



                   }
                   break;
               default:
                   break;
           }

           textoImpresionContenidoBottom+= "AGENTE  : "+nombreAgente+"\n\n";
           if (tipoVenta!=2){
               textoImpresionContenidoBottom+= "V. Resumen: " + sha1 +"\n\n";
               if(tipoC.equals("F"))
               {
                   textoEmpresa+="\n\n" + "BOLETA ELECTRÓNICA"+"\n";
                   textoImpresionContenidoBottom+= Constants.REPRESENTACION_FACTURA+"\n\n";
               }else if(tipoC.equals("B")){
                   textoEmpresa+="\n\n"+"FACTURA ELECTRÓNICA"+"\n";
                   textoImpresionContenidoBottom+= Constants.REPRESENTACION_BOLETA+"\n\n";
               }
               textoImpresionContenidoBottom+= Constants.PRINT_VISUALICE+"\n";
               textoImpresionContenidoBottom+= Constants.PRINT_URL+"\n";
           }

/*
           texto += "B.I:"+df.format(base_imponible)+"\n";
           texto += "IGV.:"+df.format(igv)+"\n";
           texto += "P.V."+df.format(precio_venta)+"\n";
           texto += "AGENTE:"+nombreAgente;*/
       }

       switch (pulgadas){
           case 2:
               texto+=" 2 PULGADAS, NO SOPORTADO.\n";
               break;
           case 3:
/*               String tipoC="";
               tipoC=comprobante.substring(0,1);
               String tipoComprobante="";
               if(tipoC.equals("F"))
               {
                   tipoComprobante="FACTURA ELECTRONICA";

               }
               else
               {
                   tipoComprobante="BOLETA ELECTRONICA";
               }
               texto+=
                       "            UNIVERSIDAD PERUANA UNION           \n"
                               +"      CENTRO DE APLICACION PRODUCTOS UNION      \n"
                               +"     CAR. CENTRAL KM. 19.5 VILLA UNION-NANA     \n"
                               +"         Lurigancho-Chosica Fax: 6186311        \n"
                               +"              Telf: 6186309-6186310             \n"
                               +"         Casilla 3564, Lima 1, LIMA PERU        \n"
                               +"                 RUC: 20138122256               \n\n";
               texto+="               "+tipoComprobante+"             \n\n";
               texto +="NUMERO  : "+comprobante+"\n";
               texto+= "FECHA   : "+ fecha+"\n";
               texto+= "CLIENTE : "+ cliente+"\n";
               texto+= "DNI/RUC : "+ dni_ruc+"\n";
               texto+= "DIRECCIÓN : "+ direccion+"\n\n";
               texto+= "SHA1  : "+ sha1+"\n";
               texto+= "------------------------------------------------------".substring(0,48)+"\n";
               texto+=String.format("%-6s","CANT") + String.format("%-30s","PRODUCTO")+String.format("%-5s","P.U.")+  String.format("%-7s","IMPORTE")+"\n";
               texto+= "------------------------------------------------------".substring(0,48)+"\n";
               texto+=ventaDetalle;
               texto+= "------------------------------------------------------".substring(0,48)+"\n";
               texto += "\n"+String.format("%-18s","OP. GRAVADA")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format(base_imponible));
               texto += "\n"+String.format("%-18s","OP. INAFECTA")+String.format("%-21s","S/.")+ String.format("%1$9s","0.00");
               texto += "\n"+String.format("%-18s","OP. EXONERADA")+String.format("%-21s","S/.")+ String.format("%1$9s","0.00");
               texto += "\n"+String.format("%-18s","OP. GRATUITA")+String.format("%-21s","S/.")+ String.format("%1$9s","0.00");
               texto += String.format("%-18s","I.G.V.")+String.format("%-21s","S/.")+  String.format("%1$9s",df.format(igv));
               texto += String.format("%1$48s","---------");
               texto += String.format("%-18s","PRECIO VENTA")+String.format("%-21s","S/.")+  String.format("%1$9s",df.format(precio_venta))+"\n\n";
               texto+= "------------------------------------------------------".substring(0,48)+"\n";
*//*               texto+= "Son "+ NumberToLetterConverter.convertNumberToLetter(df.format(precio_venta).replace(',','.')).toLowerCase()+"\n";
               texto+= "------------------------------------------------------".substring(0,48)+"\n";*//*
               texto+= "VENDEDOR : "+ nombreAgente+"\n";*/
               break;
           default:
               texto+=" NO SE PUEDE RECONOCER EL NÚMERO DE PULGADAS...";
               break;
       }
        return texto;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.buttonSincronizar:

                if(!connected)
                {
                    connectInit();
                    try
                    {
                        btConn(mBluetoothAdapter.getRemoteDevice(defaultAdressImpresora));
                    }
                    catch(IllegalArgumentException e)
                    {
                        // Bluetooth Address Format [OO:OO:OO:OO:OO:OO]
                        AlertView.showAlert(e.getMessage(), contexto);
                        return;
                    }
                    catch (IOException e)
                    {
                        AlertView.showAlert(e.getMessage(), contexto);
                        return;
                    }
                }
                // Disconnect routine.
                else
                {
                    // Always run.
                    btDisconn();
                }

                /*
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBluetooth = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetooth, 0);
                }

                else{
                    estado = findBT();
                    if (estado) {
                        try {
                            isConnected = openBT();
                        } catch (IOException e) {
                            Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
                            //e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Hacer un emparejamiento Bluetooth.", Toast.LENGTH_LONG).show();
                    }
                    if (isConnected) {
                        buttonImprimir.setEnabled(isConnected);
                        buttonImprimir.setBackgroundColor(contexto.getResources().getColor(R.color.PersonalizadoSteve2));
                        buttonImprimir.setAlpha((float) 1.0);
                        //buttonSincronizar.setEnabled(!isConnected);
                        //buttonSincronizar.setAlpha((float)0.0);
                        Toast.makeText(getApplicationContext(), "Sincronizado con : " + nameImpresora, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error : Revise si la impresora está encendida.", Toast.LENGTH_LONG).show();
                    }
                }*/




                break;
            case R.id.buttonImprimir:

                Print print = new Print(contexto);

                try {
                    print.printDocumento(idComprobante);
                } catch (JposException e) {
                    e.printStackTrace();
                    AlertView.showAlert(e.getMessage(), contexto);
                    return;
                }
                /*countImpresion++;
                if (countImpresion<=2) {
                    new AsyncTaskImprimir().execute(textoImpresion);
                }else{
                    Toast.makeText(getApplicationContext(), "Sólo puede imprimir 2 veces.", Toast.LENGTH_LONG).show();
                }*/

                break;
            default:
                break;
        }

    }

 /*   protected class AsyncTaskImprimir extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {

            Log.d("Bluetooth message", "Async Task Imprimir begin");
            if (mmDevice==null){
                Log.d("Bluetooth message", "Device is null");
                findBT();
                try {
                    openBT();
                } catch (IOException e) {
                    Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Revise si la impresora está encendida. Si el problema persiste pruebe reiniciar", Toast.LENGTH_LONG).show();
                }
            }
            return strings[0];
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                sendData(textoImpresion);
            } catch (IOException e) {
                Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
                //e.printStackTrace();
            }
        }
    }


    // This will find a bluetooth printer device
    public boolean findBT() {

        boolean estado = false;

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Log.d("Bluetooth message","No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                //estado = true;
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);

            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    Log.d("Bluetooth name : ", device.getName());
                    // MP300 is the name of the bluetooth printer device

                    if (device.getName().equals(nameImpresora)) {

                        estado = true;
                        Log.d("Bluetooth message","Bluetooth Device Found");
                        mmDevice = device;
                        device.getUuids();
                        ParcelUuid[] parcelUuids= device.getUuids();
                        Log.d("Bluetooth, UUIDS  ",parcelUuids[1].getUuid().toString());
                        break;
                    }
                }
            }


        } catch (NullPointerException e) {
            Log.d("Bluetooth message", e.getMessage());
        } catch (Exception e) {
            Log.d("Bluetooth message", e.getMessage());
        }

        return  estado;
    }

    // Tries to open a connection to the bluetooth printer device
    public boolean openBT() throws IOException {
        boolean estado = false;
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            estado = mmSocket.isConnected();

            beginListenForData();

            Log.d("Bluetooth message", "Bluetooth Opened");
        } catch (NullPointerException e) {
            Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
            //e.printStackTrace();
            Log.d("Bluetooth message O", e.getMessage());

        } catch (Exception e) {
            Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
            //e.printStackTrace();
            Log.d("Bluetooth message O", e.getMessage());


        }
        return  estado;
    }

    // After opening a connection to bluetooth printer device,
    // we have to listen and check if a data were sent to be printed.
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];

                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes,
                                                Charset.forName("UTF-8")
                                        );
                                        readBufferPosition = 0;
                                        handler.post(new Runnable() {
                                            public void run() {
                                                Log.d("Bluetooth message",data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            stopWorker = true;
                        }
                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
            //e.printStackTrace();
        } catch (Exception e) {
            Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
            //e.printStackTrace();
        }
    }
    public void sendData(String textoImprimir) throws IOException {
        try {
            String textoCleaned = cleanAcentos(textoImprimir);
            mmOutputStream.write(textoCleaned.getBytes());
            *//*
            Collection<Charset> charsets= Charset.availableCharsets().values();
            for (Charset c: charsets){
                mmOutputStream.write((c.name()+"  áéíóú\n").getBytes(Charset.forName(c.name())));
                Log.d("Avalaible charset", ""+ c.name());
            }
            *//*


            // tell the user data were sent
            Log.d("Bluetooth message", "Data Sent");

        } catch (NullPointerException e) {
            Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
            //e.printStackTrace();
        } catch (Exception e) {
            Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
            //e.printStackTrace();
        }
    }

    // Close the connection to bluetooth printer.
    public void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            Log.d("Bluetooth message","Bluetooth Closed");
        } catch (NullPointerException e) {
            Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
            //e.printStackTrace();
        } catch (Exception e) {
            Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
            //e.printStackTrace();
        }
    }*/

    @Override
    protected void onStop() {

        super.onStop();
        //btDisconn();
        /*try {
            closeBT();
        } catch (IOException e) {
            Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
            //e.printStackTrace();
        }*/
    }


    public static String cleanAcentos(String string) {
        /*String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇü·':";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcCu   ";*/
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇü·'";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcCu  ";
        if (string != null) {
            //Recorro la cadena y remplazo los caracteres originales por aquellos sin acentos
            for (int i = 0; i < original.length(); i++ ) {
                string = string.replace(original.charAt(i), ascii.charAt(i));
            }
        }
        return string;
    }

    private static String agregarCeros(String string, int largo)
    {
        String ceros = "";
        int cantidad = largo - string.length();
        if (cantidad >= 1)
        {
            for(int i=0;i<cantidad;i++)
            {
                ceros += "0";
            }
            return (ceros + string);
        }
        else
            return string;
    }


    /**
     * Set up Bluetooth.
     */
    private void bluetoothSetup()
    {
        // Initialize
        clearBtDevData();
        bp = BluetoothPort.getInstance();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null)
        {
            // Device does not support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void clearBtDevData()
    {
        remoteDevices = new Vector<BluetoothDevice>();
    }

    // For the Desire Bluetooth close() bug.
    private void connectInit()
    {
        if(!mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.enable();
            try
            {
                Thread.sleep(3600);
            }catch(Exception e){}
        }
    }

    // Bluetooth Connection method.
    private void btConn(final BluetoothDevice btDev) throws IOException
    {
        new connTask().execute(btDev);
    }


    /** ------------------------------------------------------------------ */
    public class connTask extends AsyncTask<BluetoothDevice, Void, Integer>
    {
        private final ProgressDialog dialog = new ProgressDialog(VMovil_BluetoothImprimir.this);
//		private BluetoothPort bp;

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
//			bp = BluetoothPort.getInstance();
            dialog.setTitle("Bluetooth");
            dialog.setMessage("Connecting");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(BluetoothDevice... params)
        {
            // TODO Auto-generated method stub
            Integer retVal = null;
            try
            {
                bp.connect(params[0]);
                retVal = new Integer(0);
            }
            catch (IOException e)
            {

                retVal = new Integer(-1);
            }
            return retVal;
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            // TODO Auto-generated method stub
            if(result.intValue() == 0)
            {
                RequestHandler rh = new RequestHandler();
                hThread = new Thread(rh);
                hThread.start();
                connected = true;


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // UI
                        buttonImprimir.setEnabled(connected);
                        buttonImprimir.setBackgroundColor(contexto.getResources().getColor(R.color.PersonalizadoSteve2));
                        buttonImprimir.setAlpha((float) 1.0);

                        buttonSincronizar.setText("DESCONECTAR");

                        //TOAST
                        Toast toast = Toast.makeText(contexto, "SINCRONIZADO", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 8);
                        toast.show();
                    }
                });
                /*list.setEnabled(false);
                editText.setEnabled(false);
                searchButton.setEnabled(false);*/
            }else if (result.intValue() == -1)
            {
                Toast toast = Toast.makeText(contexto, "ERROR, REVISE LA IMPRESORA.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 8);
                toast.show();
            }
            if(dialog.isShowing())
            {
                dialog.dismiss();


            }
            super.onPostExecute(result);
        }
    }
    /** ------------------------------------------------------------------ */

// Bluetooth Disconnection method. - Abnormal Method (Desire Bluetooth close() Bug).
    private void btDisconn()
    {
//		mBluetoothAdapter.disable();
        try
        {
            bp.disconnect();
            Thread.sleep(1200);
        }catch (Exception e){
            Toast toast = Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 8);
            toast.show();
        }

        if((hThread != null) && (hThread.isAlive()))
            hThread.interrupt();
        connected = false;
        // UI
        buttonSincronizar.setText("SINCRONIZAR");
        buttonImprimir.setEnabled(connected);
        buttonImprimir.setAlpha((float) 0.0);
        buttonImprimir.setBackgroundColor(getApplication().getResources().getColor(R.color.PersonalizadoSteve4));
    /*list.setEnabled(true);
    editText.setEnabled(true);
    searchButton.setEnabled(true);*/
        Toast toast = Toast.makeText(contexto, "DESCONECTADO", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 8);
        toast.show();
    }


}
