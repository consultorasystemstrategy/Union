package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import android.widget.Spinner;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.BlueTooth.BtConexion;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
public class VMovil_Venta_Producto extends Activity {
    private Spinner VVP_seleciS;
    TextView titulo;
    private Cursor cursor, cursorx;
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
    public String txtResultado = ".\n";
    //BtConexion BlueTooth = new BtConexion();
    private DbAdapter_Stock_Agente dbHelper;
    private DbAdapter_Precio dbHelperx;
    private DbAdapter_Comprob_Venta_Detalle dbHelpery;
    private DbAdaptert_Evento_Establec dbHelperz;
    private SimpleCursorAdapter dataAdapter;
    final Context context = this;
    public int idprod;
    public int cantid;
    public String nombre;
    public Double precio;
    public Double preuni;
    private String valIdEstabXY;
    private String valIdTipoVen;
    private String valCatEst;
    private int valIdTipoVenInt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_venta_producto);
        Bundle bundle = getIntent().getExtras();
        valIdEstabXY=bundle.getString("idEstabXY");
        valIdTipoVen=bundle.getString("idTipoVen");
        Toast.makeText(getApplicationContext(),
                valIdTipoVen, Toast.LENGTH_SHORT).show();
        titulo = (TextView) findViewById(R.id.VVP_titulo);

        dbHelper = new DbAdapter_Stock_Agente(this);
        dbHelper.open();
        dbHelperx = new DbAdapter_Precio(this);
        dbHelperx.open();
        dbHelpery = new DbAdapter_Comprob_Venta_Detalle(this);
        dbHelpery.open();
        dbHelperz = new DbAdaptert_Evento_Establec(this);
        dbHelperz.open();
        //Clean all data
        //dbHelper.deleteAllStockAgente();
        //dbHelperx.deleteAllPrecio();

        procesarCES();
        //Add some data
        //dbHelper.insertSomeStockAgente();
        //dbHelperx.insertSomePrecio();

        //Generate ListView from SQLite Database
        displayListView();
    }

    public void procesarCES(){

        cursorx = dbHelperz.fetchEstablecsById(valIdEstabXY);
        //Nos aseguramos de que existe al menos un registro
        if (cursorx.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                valCatEst = cursorx.getString(2);
            } while(cursorx.moveToNext());
        }
    }

    private void displayListViewVVC() {

        Cursor cursor = dbHelpery.fetchAllComprobVentaDetalle();

        // The desired columns to be bound
        String[] columns = new String[]{
                DbAdapter_Comprob_Venta_Detalle.CD_nom_producto,
                DbAdapter_Comprob_Venta_Detalle.CD_importe
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.VVC_producinf,
                R.id.VVC_precioinf,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_venta_cabecera,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.VVC_LSTprosel);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    private void displayListView() {

        Cursor cursor = dbHelper.fetchAllStockAgentePrecioEst(valCatEst);

        // The desired columns to be bound
        String[] columns = new String[] {
                DbAdapter_Stock_Agente.ST_nombre,
                DbAdapter_Precio.PR_precio_unit
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.VVP_produc,
                R.id.VVP_precio,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_venta_producto,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.VVP_listar);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                EditText texcan = (EditText) findViewById(R.id.VVP_cantid);
                String sval = texcan.getText().toString();

                // Get the state's capital from this row in the database.
                idprod = cursor.getInt(cursor.getColumnIndexOrThrow("st_in_id_producto"));
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("st_te_nombre"));
                preuni = cursor.getDouble(cursor.getColumnIndexOrThrow("pr_re_precio_unit"));
                if (sval.matches("")) {
                    cantid = 0;
                }else{
                    cantid = Integer.parseInt(texcan.getText().toString());
                }

                precio = preuni * cantid;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Productos");

                // set dialog message
                alertDialogBuilder
                        .setMessage("¿Elegir?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                //MainActivity.this.finish();
                                valIdTipoVenInt = Integer.parseInt(valIdTipoVen);
                                if(valIdTipoVenInt == 0){
                                    dbHelpery.createComprobVentaDetalle( 0, idprod, nombre, cantid, precio, precio, preuni, "0/0", "0", 10);
                                }
                                if(valIdTipoVenInt == 1){
                                    dbHelpery.createComprobVentaDetalle( 0, idprod, nombre, cantid, 0, 0, 0, "0/0", "0", 10);
                                }
                                Toast.makeText(getApplicationContext(),
                                        "Registrado", Toast.LENGTH_SHORT).show();
                                //displayListViewVVC();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                //dialog.cancel();
                                if(idprod == 1){
                                    findBT();
                                }
                                if(idprod == 2){
                                    try{
                                        openBT();
                                    } catch (IOException ex) {

                                    }
                                }
                                if(idprod == 3){
                                    try{
                                        sendData();
                                    } catch (IOException ex) {

                                    }
                                }
                                if(idprod == 4){
                                    try{
                                        closeBT();
                                    } catch (IOException ex) {

                                    }
                                }
                                Toast.makeText(getApplicationContext(),
                                        "Cancelo " + idprod, Toast.LENGTH_SHORT).show();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }

        });

        EditText myFilter = (EditText) findViewById(R.id.VVP_buscar);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchStockAgentePrecioByNameEst(constraint.toString(),valCatEst);
            }
        });

    }

    // This will find a bluetooth printer device
    public void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                titulo.setText("No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // MP300 is the name of the bluetooth printer device
                    if (device.getName().equals("Star Micronics")) {
                        mmDevice = device;
                        break;
                    }
                }
            }
            titulo.setText("Bluetooth Device Found");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            titulo.setText("Bluetooth Opened");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                titulo.setText(data);
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

    /*
     * This will send data to be printed by the bluetooth printer
     */
    public void sendData() throws IOException {
        try {

            // the text typed by the user
            //String msg = myTextbox.getText().toString();
            //msg += "\n";

            //cursor = manager.BuscarAgentes(tv.getText().toString());
            cursor = dbHelpery.fetchAllComprobVentaDetalle();
            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                txtResultado = "";
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String val01 = cursor.getString(3);
                    String val02 = cursor.getString(2);
                    String val03 = cursor.getString(5);
                    //txtResultado += val01 + " - " + val02 + " - " + val03 + " - " + "\n";
                    if(val02.length()>17) {
                        txtResultado += String.format("%-6s", val01) + String.format("%-19s", val02.substring(0, 17) + ".") + String.format("%7s", val03) + "\n";
                    }else{
                        txtResultado += String.format("%-6s", val01) + String.format("%-19s", val02) + String.format("%7s", val03) + "\n";
                    }
                } while(cursor.moveToNext());
            }

            String texto = ".\n";
            texto += "    UNIVERSIDAD PERUANA UNION   \n";
            texto += "     Cent.aplc. Prod. Union     \n";
            texto += "   C. Central Km 19 Villa Union \n";
            texto += " Lurigancho-Chosica Fax: 6186311\n";
            texto += "      Telf: 6186309-6186310     \n";
            texto += " Casilla 3564, Lima 1, LIMA PERU\n";
            texto += "         RUC: 20138122256       \n";
            texto += "--------------------------------\n";
            //texto += "Factura Nro. 030-000212\n";
            //texto += "Fecha: 12/11/2014\n";
            //texto += "Cajero: Juan Perez Perez\n";
            //texto += "Cliente: Perico Palotes Palotes\n";
            //texto += "DNI: 47678934\n";
            //texto += "Direccion: Alameda Nro 2039 - Chosica\n";
            //texto += "--------------------------------\n";
            //texto += "Cant. Producto           Importe\n";
            //texto += "--------------------------------\n";
            //texto += String.format("%-6s", 5) + String.format("%-19s", "Pan Americano Mediano Union".substring(0, 17) + ".") + String.format("%7s", 40.50) + "\n";
            //texto += String.format("%-6s", 5) + String.format("%-19s", "Rollo de caneladddddddddd".substring(0, 17) + ".") + String.format("%7s", 30.50) + "\n";
            //texto += String.format("%-6s", 5) + String.format("%-19s", "Paneton Unioneeeeeeeeee".substring(0, 17) + ".") + String.format("%7s", 10.50) + "\n";
            //texto += String.format("%-6s", 5) + String.format("%-19s", "Paneton Super Bom".substring(0, 17) + ".") + String.format("%7s", 3.40) + "\n";
            //texto += String.format("%-6s", 5) + String.format("%-19s", "Pan Americano Sandwich".substring(0, 17) + ".") + String.format("%7s", 100.3) + "\n";
            //texto += String.format("%-6s", 5) + String.format("%-19s", "Pan Americano Mediano Union".substring(0, 17) + ".") + String.format("%7s", 2.34) + "\n";
            //texto += String.format("%-25s", "SUB TOTAL:") + String.format("%7s", 1000.00) + "\n";
            //texto += String.format("%-25s", "IGV:") + String.format("%7s", 180.00) + "\n";
            //texto += String.format("%-25s", "TOTAL:") + String.format("%7s", 1800.00) + "\n";

            mmOutputStream.write(txtResultado.getBytes());

            // tell the user data were sent
            titulo.setText("Data Sent");

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
            titulo.setText("Bluetooth Closed");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}