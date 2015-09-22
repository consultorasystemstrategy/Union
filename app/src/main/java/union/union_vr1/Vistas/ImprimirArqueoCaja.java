package union.union_vr1.Vistas;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbGastos_Ingresos;

public class ImprimirArqueoCaja extends Activity implements View.OnClickListener{



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

    private String nameImpresora = "";
    private int pulgadasImpresora=0;
    private String defaultNameImpresora = "mobile printer";
    //private String nameImpresora = "Star Micronics";
    private Button buttonImprimir;
    private Button buttonSincronizar;


    private String textoImpresion = "";

    private DbAdapter_Temp_Session session;
    private int idLiquidacion;
    private int idAgente;


    int nTotal = 0;
    Double emitidoTotal = 0.0;
    Double pagadoTotal = 0.0;
    Double cobradoTotal = 0.0;


    Double totalRuta = 0.0;
    Double totalPlanta = 0.0;

    //
    private DbGastos_Ingresos dbHelperGastosIngr;
    private DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
    private DbAdapter_Agente dbHelperAgente;
    String nombreAgente="";
    String textoLeft="";
    String textoRight="";

    private TextView textViewLeft;
    private TextView textViewRight;
    private SimpleCursorAdapter simpleCursorAdapterCR;
    private ListView listViewComprobante;

    private View viewResumenTotal;
    private TextView textViewTotalDescripcion;
    private TextView textViewTotalN;
    private TextView textViewTotalEmitido;
    private TextView textViewTotalPagado;
    private TextView textViewTotalCobrado;

    private ListView listViewResumenGastos;

    private TextView textViewResumenGastosTotalRuta;
    private TextView textViewResumenGastosTotalPlanta;
    private View viewlayoutFooterGastos;
    private TextView textViewResumenGastoNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprimir_arqueo_caja);
        contexto = this;

        session = new DbAdapter_Temp_Session(this);
        session.open();
        dbHelperGastosIngr =  new DbGastos_Ingresos(this);
        dbHelperGastosIngr.open();

        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(this);
        dbAdapter_informe_gastos.open();

        dbHelperAgente = new DbAdapter_Agente(this);
        dbHelperAgente.open();


        textViewLeft = (TextView) findViewById(R.id.textViewLeft);
        textViewRight = (TextView) findViewById(R.id.textViewRight);
        listViewComprobante = (ListView) findViewById(R.id.listViewComprobantes);
        listViewResumenGastos = (ListView) findViewById(R.id.listViewResumenGastos);



        idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);

        Cursor cursor = dbHelperAgente.fetchAgentesByIds(idAgente, idLiquidacion);
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            nombreAgente = cursor.getString(cursor.getColumnIndexOrThrow(dbHelperAgente.AG_nombre_agente));
        }


        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        nameImpresora= SP.getString("impresoraNombre", defaultNameImpresora);
        pulgadasImpresora= Integer.parseInt(SP.getString("impresoraAncho", "3"));


        buttonImprimir = (Button) findViewById(R.id.buttonImprimir);
        buttonSincronizar = (Button) findViewById(R.id.buttonSincronizar);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        textoImpresion = generarTextoImpresionResumen(pulgadasImpresora);


        textViewRight.setText(textoRight);
        textViewLeft.setText(textoLeft);

        showComprobantesList();



        if (!isSincronized){
            buttonImprimir.setEnabled(isSincronized);
            buttonImprimir.setAlpha((float) 0.0);
            buttonImprimir.setBackgroundColor(getApplication().getResources().getColor(R.color.PersonalizadoSteve4));
        }

        buttonImprimir.setOnClickListener(this);
        buttonSincronizar.setOnClickListener(this);


    }

    public void showComprobantesList(){

        DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("#.00", simbolos);

        Cursor cursorResumen = dbHelperGastosIngr.listarIngresosGastos(idLiquidacion);


        viewResumenTotal = getLayoutInflater().inflate(R.layout.resumen_total, null);
        textViewTotalDescripcion = (TextView) viewResumenTotal.findViewById(R.id.resumen_total_descripcion);
        textViewTotalN = (TextView) viewResumenTotal.findViewById(R.id.resumen_total_n);
        textViewTotalEmitido = (TextView) viewResumenTotal.findViewById(R.id.resumen_total_emitido);
        textViewTotalPagado = (TextView) viewResumenTotal.findViewById(R.id.resumen_total_pagado);
        textViewTotalCobrado = (TextView) viewResumenTotal.findViewById(R.id.resumen_total_cobrado);


        String[] columns = new String[]{
                "comprobante",
                "n",
                "emitidas",
                "pagado",
                "cobrado"};


        int[] to = new int[]{
                R.id.VRC_descripcion,
                R.id.VRC_cantidad,
                R.id.VRC_vendido,
                R.id.VRC_pagado,
                R.id.VRC_cobrado
        };

        simpleCursorAdapterCR = new SimpleCursorAdapter(
                this, R.layout.infor_resumen_ingresos,
                cursorResumen,
                columns,
                to,
                0);


        textViewTotalCobrado.setText("Total");
        textViewTotalN.setText("" + nTotal);
        textViewTotalEmitido.setText("S/" + df.format(emitidoTotal));
        textViewTotalPagado.setText("S/" + df.format(pagadoTotal));
        textViewTotalCobrado.setText("S/" + df.format(cobradoTotal));
        listViewComprobante.setAdapter(simpleCursorAdapterCR);
        listViewComprobante.addFooterView(viewResumenTotal);


        //GASTOS

        viewlayoutFooterGastos = getLayoutInflater().inflate(R.layout.resumen_informe_gastos, null);
        textViewResumenGastoNombre      = (TextView) viewlayoutFooterGastos.findViewById(R.id.textviewNombre);
        textViewResumenGastosTotalPlanta = (TextView) viewlayoutFooterGastos.findViewById(R.id.textViewGastoPlanta);
        textViewResumenGastosTotalRuta = (TextView) viewlayoutFooterGastos.findViewById(R.id.textViewGastoRuta);

        Cursor cursorGastos =dbAdapter_informe_gastos.resumenInformeGastos(getDayPhone());

        String[] de = {
                "tg_te_nom_tipo_gasto",
                "RUTA",
                "PLANTA"
        };
        int[] para = {
                R.id.textviewNombre,
                R.id.textViewGastoRuta,
                R.id.textViewGastoPlanta

        };

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.resumen_informe_gastos,
                cursorGastos,
                de,
                para
        );
        listViewResumenGastos.setAdapter(simpleCursorAdapter);
        listViewResumenGastos.addFooterView(viewlayoutFooterGastos);

        textViewResumenGastoNombre.setText("Total");
        textViewResumenGastoNombre.setTypeface(null, Typeface.BOLD);

        textViewResumenGastosTotalPlanta.setText("S/. " + df.format(totalPlanta));
        textViewResumenGastosTotalPlanta.setTypeface(null, Typeface.BOLD);

        textViewResumenGastosTotalRuta.setText("S/. " + df.format(totalRuta));
        textViewResumenGastosTotalRuta.setTypeface(null, Typeface.BOLD);

    }

    public String generarTextoImpresionResumen(int pulgadasImpresora) {
        DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("#.00", simbolos);

        String texto = ".\n";


        Cursor cursorResumen = dbHelperGastosIngr.listarIngresosGastos(idLiquidacion);
        cursorResumen.moveToFirst();

        for (cursorResumen.moveToFirst(); !cursorResumen.isAfterLast(); cursorResumen.moveToNext()) {
            int n = cursorResumen.getInt(cursorResumen.getColumnIndexOrThrow("n"));
            Double emitido = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("emitidas"));
            Double pagado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("pagado"));
            Double cobrado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("cobrado"));
            String documento = cursorResumen.getString(cursorResumen.getColumnIndexOrThrow("comprobante"));
            nTotal += n;
            emitidoTotal += emitido;
            pagadoTotal += pagado;
            cobradoTotal += cobrado;

        }


        Cursor cursorTotalGastos = dbAdapter_informe_gastos.resumenInformeGastos(getDayPhone());
        for (cursorTotalGastos.moveToFirst(); !cursorTotalGastos.isAfterLast(); cursorTotalGastos.moveToNext()) {
            Double rutaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("RUTA"));
            Double plantaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("PLANTA"));
            String nombreGasto = cursorTotalGastos.getString(cursorTotalGastos.getColumnIndexOrThrow("tg_te_nom_tipo_gasto"));

            totalRuta += rutaGasto;
            totalPlanta += plantaGasto;
        }



        Double ingresosTotales = cobradoTotal + pagadoTotal;
        Double gastosTotales = totalRuta;
        Double aRendir = ingresosTotales - gastosTotales;

        texto += "********************************************************".substring(0, 48) + "\n";
        texto += "\n"+"                RESUMEN DEL DIA                 ".substring(0, 48) + "\n";
        texto += "********************************************************".substring(0, 48) + "\n";
        texto += "\n" + String.format("%-11s", "Fecha") + String.format("%1$37s", getDatePhone());
        texto += "\n" + String.format("%-11s", "Liquidacion") + String.format("%1$37s", idLiquidacion);
        texto += "\n" + String.format("%-11s", "Cajero(a)") + String.format("%1$37s", nombreAgente);
        textoLeft+="Fecha"+"\n";
        textoLeft+="Liquidación"+"\n";
        textoLeft+="Cajero(a)"+"\n\n";
        textoLeft+="INGRESOS TOTALES"+"\n";
        textoLeft+="GASTOS TOTALES"+"\n";
        textoLeft+="TOTAL A RENDIR"+"\n";
        textoRight+=getDatePhone()+"\n";
        textoRight+=idLiquidacion+"\n";
        textoRight+=nombreAgente+"\n\n";
        textoRight+= df.format(ingresosTotales)+"\n";
        textoRight+= df.format(gastosTotales)+"\n";
        textoRight+= df.format(aRendir)+"\n";


        texto += "\n\n" + String.format("%-20s", "INGRESOS TOTALES") + String.format("%-16s", "S/.") + String.format("%1$12s", df.format(ingresosTotales));
        texto += "\n" + String.format("%-20s", "GASTOS TOTALES") + String.format("%-16s", "S/.") + String.format("%1$12s", df.format(gastosTotales));
        texto += "\n" + String.format("%-20s", "TOTAL A RENDIR") + String.format("%-16s", "S/.") + String.format("%1$12s", df.format(aRendir));
        texto += "\n" + "**********************************************************".substring(0, 48) + "\n";

        texto += "\n"+"                    INGRESOS                    ".substring(0, 48) + "\n";
        texto += "\n" + "**********************************************************".substring(0, 48) + "\n";
        texto += "\n" + String.format("%-20s", "Comprobante") + String.format("%1$4s", "Nro") + String.format("%1$8s", "Emit.") + String.format("%1$8s", "Pag.") + String.format("%1$8s", "Cob.") + "\n";


        cursorResumen.moveToFirst();
        for (cursorResumen.moveToFirst(); !cursorResumen.isAfterLast(); cursorResumen.moveToNext()) {
            int n = cursorResumen.getInt(cursorResumen.getColumnIndexOrThrow("n"));
            Double emitido = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("emitidas"));
            Double pagado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("pagado"));
            Double cobrado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("cobrado"));
            String documento = cursorResumen.getString(cursorResumen.getColumnIndexOrThrow("comprobante"));
            texto += String.format("%-20s", documento) + String.format("%1$4s", n) + String.format("%1$8s", df.format(emitido)) + String.format("%1$8s", df.format(pagado)) + String.format("%1$8s", df.format(cobrado)) + "\n";
        }
        texto += String.format("%1$48s","----------------------------");
        texto += "\n" + String.format("%-20s", "Total") + String.format("%1$4s", nTotal) + String.format("%1$8s", df.format(emitidoTotal)) + String.format("%1$8s", df.format(pagadoTotal)) + String.format("%1$8s", df.format(cobradoTotal)) + "\n";

        texto += "\n" + "**********************************************************".substring(0, 48) + "\n";
        texto += "\n"+"                     GASTOS                     ".substring(0, 48) + "\n";
        texto += "\n" + "**********************************************************".substring(0, 48) + "\n";
        texto += "\n" + String.format("%-27s", "Tipo de Gasto") + String.format("%1$7s", "Ruta") + String.format("%1$14s", "Planta")+"\n";

        cursorTotalGastos.moveToFirst();
        for (cursorTotalGastos.moveToFirst(); !cursorTotalGastos.isAfterLast(); cursorTotalGastos.moveToNext()) {
            Double rutaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("RUTA"));
            Double plantaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("PLANTA"));
            String nombreGasto = cursorTotalGastos.getString(cursorTotalGastos.getColumnIndexOrThrow("tg_te_nom_tipo_gasto"));

            if (nombreGasto.length()>=27){
                nombreGasto= nombreGasto.substring(0,24);
                nombreGasto+="..";

            }
            texto += String.format("%-27s", nombreGasto) + String.format("%1$7s", df.format(rutaGasto)) + String.format("%1$14s", df.format(plantaGasto))+"\n";
        }

        texto += "\n" + String.format("%-27s", "Total") + String.format("%1$7s", df.format(totalRuta)) + String.format("%1$14s", df.format(totalPlanta))+"\n";
        texto += "\n" + "**********************************************************".substring(0, 48) + "\n";
        texto+=".";
        return texto;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_imprimir_arqueo_caja, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                if (countImpresion<=4) {
                    new AsyncTaskImprimir().execute(textoImpresion);
                }else{
                    Toast.makeText(getApplicationContext(), "Sólo puede imprimir 4 veces.", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }


    protected class AsyncTaskImprimir extends AsyncTask<String, String, String> {

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
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public String getDayPhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }
    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }
}
