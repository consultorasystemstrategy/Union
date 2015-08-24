package union.union_vr1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.UUID;

import union.union_vr1.Vistas.VMovil_Evento_Establec;
import union.union_vr1.Vistas.VMovil_Evento_Indice;

public class VMovil_BluetoothImprimir extends Activity{


    private String nameImpresora = "";
    private String defaultNameImpresora = "Mobile Printer";
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
    private String textoImpresionContenidoLeft = "";
    private String textoImpresionContenidoRight = "";


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, VMovil_Evento_Establec.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__bluetooth_imprimir);


        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        nameImpresora= SP.getString("impresoraNombre", defaultNameImpresora);

        buttonImprimir = (Button) findViewById(R.id.button);
        buttonSincronizar = (Button) findViewById(R.id.buttonSincronizar);
        textViewImprimirCabecera = (TextView) findViewById(R.id.textViewImprimirCabecera);
        textViewImprimirContenidoLeft = (TextView) findViewById(R.id.textViewImprimirContenidoLeft);
        textViewImprimirContenidoRight = (TextView) findViewById(R.id.textViewImprimirContenidoRight);
        textViewVentaCabecera = (TextView) findViewById(R.id.textViewVentaCabecera);

        textoImpresion = getIntent().getExtras().getString("textoImpresion");
        textoImpresionCabecera = getIntent().getExtras().getString("textoImpresionCabecera");
        textoImpresionContenidoLeft = getIntent().getExtras().getString("textoImpresionContenidoLeft");
        textoImpresionContenidoRight = getIntent().getExtras().getString("textoImpresionContenidoRight");
        textoVentaImpresion = getIntent().getExtras().getString("textoVentaImpresion");

        textViewImprimirCabecera.setText(textoImpresionCabecera);
        textViewVentaCabecera.setText(textoVentaImpresion);
        textViewImprimirContenidoLeft.setText(textoImpresionContenidoLeft);
        textViewImprimirContenidoRight.setText(textoImpresionContenidoRight);

        buttonImprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTaskImprimir().execute(textoImpresion);
            }
        });
        buttonSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean estado = findBT();
                try {
                    openBT();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(!estado){
                    Toast.makeText(getApplicationContext(), "Revise si la impresora está encendida. Si el problema persiste pruebe reiniciar", Toast.LENGTH_LONG).show();
                }

            }
        });

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
                    e.printStackTrace();
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
                e.printStackTrace();
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
                estado = true;
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
                        Toast.makeText(getApplicationContext(), "Sincronizado con : "+device.getName(), Toast.LENGTH_LONG).show();
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
    public void openBT() throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();


            beginListenForData();

            Log.d("Bluetooth message", "Bluetooth Opened");
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d("Bluetooth message O", e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Bluetooth message O", e.getMessage());


        }
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
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
        try {
            closeBT();
        } catch (IOException e) {
            e.printStackTrace();
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

}
