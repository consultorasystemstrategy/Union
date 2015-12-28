package union.union_vr1.Vistas;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.IOException;
import java.util.Vector;

import jpos.JposException;
import union.union_vr1.BlueTooth.AlertView;
import union.union_vr1.BlueTooth.Print;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Agente_Login;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;

public class Bluetooth_Printer extends Activity {

    private ToggleButton buttonTransferencias;
    private ToggleButton buttonArqueo;



    private DbAdapter_Temp_Session session;
    private DbAdapter_Agente dbAdapter_agente;
    private DbAdapter_Agente_Login dbAdapter_agente_login;

    private int idAgente = -1;
    private int idLiquidacion = -1;
    private String nombreAgente = "AGENTE 001";

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

    private final static String TAG = Bluetooth_Printer.class.getSimpleName();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if((hThread != null) && (hThread.isAlive()))
        {
            hThread.interrupt();
            hThread = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth__printer);

        bluetoothSetup();
        contexto = this;


        session = new DbAdapter_Temp_Session(this);
        session.open();
        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();
        dbAdapter_agente_login = new DbAdapter_Agente_Login(this);
        dbAdapter_agente_login.open();




        idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);

        Cursor cursor = dbAdapter_agente.fetchAgentesByIds(idAgente, idLiquidacion);
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            nombreAgente = cursor.getString(cursor.getColumnIndexOrThrow(dbAdapter_agente.AG_nombre_agente));
            //AQUÍ OBTENTRÉ LA MAC ASOCIADA AL AGENTE
            //defaultAdressImpresora = cursor.getString(cursor.getColumnIndexOrThrow(dbAdapter_agente.MAC));
        }

        /*Cursor cursorMAC = dbAdapter_agente_login.fetchAgenteMAC(idLiquidacion);
        if (cursor.getCount()>0){
            defaultAdressImpresora = cursorMAC.getString(cursorMAC.getColumnIndexOrThrow(dbAdapter_agente_login.AG_MAC));
        }*/

        defaultAdressImpresora = session.fetchMAC();
        Log.d(TAG, "ADRESS IMPRESORA : " + defaultAdressImpresora);

        buttonArqueo = (ToggleButton) findViewById(R.id.buttonArqueoCaja);
        buttonTransferencias = (ToggleButton) findViewById(R.id.buttonTransferencias);

        buttonTransferencias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    //SINCRONIZAR
                    /*if(!connected)
                    {*/
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
                    /*
                    // Disconnect routine.
                    /*else
                    {
                        // Always run.
                        btDisconn();
                    }*/
                }else{
                    //IMPRIMIR

                    Print print = new Print(contexto);

                    try {
                        print.printTransferencia(idLiquidacion, nombreAgente);
                    } catch (JposException e) {
                        e.printStackTrace();
                        AlertView.showAlert(e.getMessage(), contexto);
                        return;
                    }


                }
            }
        });

        buttonArqueo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //SINCRONIZAR
                    /*if(!connected)
                    {*/
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
                    /*
                    // Disconnect routine.
                    /*else
                    {
                        // Always run.
                        btDisconn();
                    }*/
                }else{
                    //IMPRIMIR

                    Print print = new Print(contexto);

                    try {
                        //AQUÍ IMPRIMIRÉ EL ARQUEO
                        print.printArqueo(idLiquidacion, nombreAgente);
                    } catch (JposException e) {
                        e.printStackTrace();
                        AlertView.showAlert(e.getMessage(), contexto);
                        return;
                    }


                }
            }
        });


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

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
        private final ProgressDialog dialog = new ProgressDialog(Bluetooth_Printer.this);
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
                        buttonTransferencias.setChecked(true);
                        buttonArqueo.setChecked(true);

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
                if (!connected){
                    Toast toast = Toast.makeText(contexto, "ERROR, REVISE LA IMPRESORA.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 8);
                    toast.show();
                }
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
            buttonArqueo.setChecked(false);
        buttonTransferencias.setChecked(false);

    /*list.setEnabled(true);
    editText.setEnabled(true);
    searchButton.setEnabled(true);*/
        Toast toast = Toast.makeText(contexto, "DESCONECTADO", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 8);
        toast.show();
    }



}
