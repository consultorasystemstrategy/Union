package union.union_vr1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.NumberToLetterConverter;
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


    private String textoImpresion = "";
    private String textoImpresionCabecera = "";
    private String textoVentaImpresion = "";
/*    private String textoImpresionContenidoLeft = "";
    private String textoImpresionContenidoRight = "";*/


    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    private Context contexto;


    private boolean isSincronized= false;
    private boolean estado = false;
    private boolean isConnected = false;
    private int countImpresion = 0;
    private int idComprobante = 0;

    private boolean enabledBluetooth = false;

    private DbAdapter_Comprob_Venta dbHelperComprobanteVenta;
    private DbAdapter_Comprob_Venta_Detalle dbHelperComprobanteVentaDetalle;
    private int pulgadasImpresora=0;


    //PANTALLA

    private String textoEmpresa = "\n"
            +"    UNIVERSIDAD PERUANA UNION   \n"
            +"     Cent.aplc. Prod. Union     \n"
            +"   C. Central Km 19 Villa Union \n"
            +" Lurigancho-Chosica Fax: 6186311\n"
            +"      Telf: 6186309-6186310     \n"
            +" Casilla 3564, Lima 1, LIMA PERU\n"
            +"         RUC: 20138122256       ";
    private String ventaCabecera="";
    private String textoImpresionContenidoLeft = "";
    private String textoImpresionContenidoRight = "";

    @Override
    public void onBackPressed() {
        //super.onStop();
        super.onBackPressed();
        Intent intent = new Intent(this, VMovil_Evento_Establec.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__bluetooth_imprimir);

        contexto = this;


        dbHelperComprobanteVenta = new DbAdapter_Comprob_Venta(this);
        dbHelperComprobanteVentaDetalle = new DbAdapter_Comprob_Venta_Detalle(this);
        dbHelperComprobanteVenta.open();
        dbHelperComprobanteVentaDetalle.open();


        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        nameImpresora= SP.getString("impresoraNombre", defaultNameImpresora);
        pulgadasImpresora= Integer.parseInt(SP.getString("impresoraAncho", "3"));

        buttonImprimir = (Button) findViewById(R.id.buttonImprimir);
        buttonSincronizar = (Button) findViewById(R.id.buttonSincronizar);
        textViewImprimirCabecera = (TextView) findViewById(R.id.textViewImprimirCabecera);
        textViewImprimirContenidoLeft = (TextView) findViewById(R.id.textViewImprimirContenidoLeft);
        textViewImprimirContenidoRight = (TextView) findViewById(R.id.textViewImprimirContenidoRight);
        textViewVentaCabecera = (TextView) findViewById(R.id.textViewVentaCabecera);


        idComprobante = getIntent().getExtras().getInt("idComprobante");

        textoImpresion = generarTextoImpresion(idComprobante, pulgadasImpresora);

        /*textoImpresion = getIntent().getExtras().getString("textoImpresion");
        textoImpresionCabecera = getIntent().getExtras().getString("textoImpresionCabecera");
        textoImpresionContenidoLeft = getIntent().getExtras().getString("textoImpresionContenidoLeft");
        textoImpresionContenidoRight = getIntent().getExtras().getString("textoImpresionContenidoRight");
        textoVentaImpresion = getIntent().getExtras().getString("textoVentaImpresion");*/

        textViewImprimirCabecera.setText(textoEmpresa);
        textViewVentaCabecera.setText(ventaCabecera);
        textViewImprimirContenidoLeft.setText(textoImpresionContenidoLeft);
        textViewImprimirContenidoRight.setText(textoImpresionContenidoRight);


        if (!isSincronized){
            buttonImprimir.setEnabled(isSincronized);
            buttonImprimir.setAlpha((float) 0.0);
            buttonImprimir.setBackgroundColor(getApplication().getResources().getColor(R.color.PersonalizadoSteve4));
        }

        buttonImprimir.setOnClickListener(this);
        buttonSincronizar.setOnClickListener(this);


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Log.d("Bluetooth message","No bluetooth adapter available");
        }

        if (!mBluetoothAdapter.isEnabled()) {
            //enabledBluetooth = true;
         /*   Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
*/
        }
        /*mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Log.d("Bluetooth message","No bluetooth adapter available");
        }

        if (!mBluetoothAdapter.isEnabled()) {
            estado = true;
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);

        }*/
        /*buttonSincronizar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(contexto, "Procesando ...", Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/


    }

   private String generarTextoImpresion(int idComprobante, int pulgadas){
       DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.ENGLISH);

       DecimalFormat df = new DecimalFormat("#.00", simbolos);

       String texto= "";
       String comprobante = "";
       String fecha = "";
       String cliente = "";
       String dni_ruc = "";
       String serie  ="";
       String numDoc = "";
       String nombreAgente = "";
       String direccion="";
       //String sha1="";

       double base_imponible = 0.0;
       double igv = 0.0;
       double precio_venta = 0.0;

       String ventaDetalle = "";

       Cursor cursorVentaCabecera = dbHelperComprobanteVenta.getVentaCabecerabyID(idComprobante);

       if (cursorVentaCabecera.getCount()>0){
           cursorVentaCabecera.moveToFirst();

           serie = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_serie));
           numDoc = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_num_doc));
           comprobante = serie + "-" +agregarCeros((String.valueOf(numDoc)),7);
           fecha = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_fecha_doc));
           cliente = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_nom_cliente));
           dni_ruc = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_doc_cliente));
           nombreAgente = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Agente.AG_nombre_agente));
           direccion = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_direccion));
           //sha1 = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_SHA1));


           ventaCabecera+= "NUMERO  : "+comprobante+"\n";
           ventaCabecera+= "FECHA   : "+ fecha+"\n";
           ventaCabecera+= "VENDEDOR: "+ nombreAgente+"\n";
           ventaCabecera+= "CLIENTE : "+ cliente+"\n";
           ventaCabecera+= "DNI/RUC : "+ dni_ruc+"\n";
           ventaCabecera+= "DIRECCION : "+ direccion+"\n";
//           ventaCabecera+= "SHA1 : "+ sha1+"\n";





           Cursor cursorVentaDetalle = dbHelperComprobanteVentaDetalle.fetchAllComprobVentaDetalleByIdComp(idComprobante);

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

               textoImpresionContenidoLeft+="SUB TOTAL:\n";
               textoImpresionContenidoLeft+="IGV:\n";
               textoImpresionContenidoLeft+="TOTAL:\n";

           }

           //texto += "\n";
           base_imponible = cursorVentaCabecera.getDouble(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_base_imp));
           igv = cursorVentaCabecera.getDouble(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_igv));
           precio_venta = cursorVentaCabecera.getDouble(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_total));


           textoImpresionContenidoRight+= "S/" +
                   ""+ df.format(base_imponible)+"\n";
           textoImpresionContenidoRight+= "S/"+ df.format(igv)+"\n";
           textoImpresionContenidoRight+= "S/"+ df.format(precio_venta)+"\n";
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
               texto+=
                       "            UNIVERSIDAD PERUANA UNION           \n"
                               +"      CENTRO DE APLICACION PRODUCTOS UNION      \n"
                               +"     CAR. CENTRAL KM. 19.5 VILLA UNION-NANA     \n"
                               +"         Lurigancho-Chosica Fax: 6186311        \n"
                               +"              Telf: 6186309-6186310             \n"
                               +"         Casilla 3564, Lima 1, LIMA PERU        \n"
                               +"                 RUC: 20138122256               \n\n";
               texto +="NUMERO  : "+comprobante+"\n";
               texto+= "FECHA   : "+ fecha+"\n";
               texto+= "CLIENTE : "+ cliente+"\n";
               texto+= "DNI/RUC : "+ dni_ruc+"\n";
               texto+= "DIRECCIÓN : "+ direccion+"\n";
               //texto+= "SHA1  : "+ sha1+"\n";
               texto+= "------------------------------------------------------".substring(0,48)+"\n";
               texto+=String.format("%-6s","CANT") + String.format("%-30s","PRODUCTO")+String.format("%-5s","P.U.")+  String.format("%-7s","IMPORTE")+"\n";
               texto+= "------------------------------------------------------".substring(0,48)+"\n";
               texto+=ventaDetalle;
               texto += "\n"+String.format("%-18s","OP. GRAVADA")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format(base_imponible));
               texto += "\n"+String.format("%-18s","OP. INAFECTA")+String.format("%-21s","S/.")+ String.format("%1$9s","0.00");
               texto += "\n"+String.format("%-18s","OP. EXONERADA")+String.format("%-21s","S/.")+ String.format("%1$9s","0.00");
               texto += "\n"+String.format("%-18s","OP. GRATUITA")+String.format("%-21s","S/.")+ String.format("%1$9s","0.00");
               texto += String.format("%-18s","I.G.V.")+String.format("%-21s","S/.")+  String.format("%1$9s",df.format(igv));
               texto += String.format("%-18s","PRECIO VENTA")+String.format("%-21s","S/.")+  String.format("%1$9s",df.format(precio_venta))+"\n\n";
               texto+= "------------------------------------------------------".substring(0,48)+"\n";
               texto+= "Son "+ NumberToLetterConverter.convertNumberToLetter(df.format(precio_venta).replace(',','.')).toLowerCase()+"\n";
               texto+= "------------------------------------------------------".substring(0,48)+"\n";
               texto+= "VENDEDOR : "+ nombreAgente+"\n";
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

                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBluetooth = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetooth, 0);
                }else{
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
                }




                break;
            case R.id.buttonImprimir:
                countImpresion++;
                if (countImpresion<=2) {
                    new AsyncTaskImprimir().execute(textoImpresion);
                }else{
                    Toast.makeText(getApplicationContext(), "Sólo puede imprimir 2 veces.", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }

    }

    protected class AsyncTaskImprimir extends AsyncTask<String, String, String>{

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
            /*
            Collection<Charset> charsets= Charset.availableCharsets().values();
            for (Charset c: charsets){
                mmOutputStream.write((c.name()+"  áéíóú\n").getBytes(Charset.forName(c.name())));
                Log.d("Avalaible charset", ""+ c.name());
            }
            */


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
    }

    @Override
    protected void onStop() {

        super.onStop();
        try {
            closeBT();
        } catch (IOException e) {
            Log.d("STACKSTRACE PU", Log.getStackTraceString(e));
            //e.printStackTrace();
        }
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

}
