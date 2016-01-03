package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.IOException;
import java.util.Vector;

import jpos.JposException;
import union.union_vr1.BlueTooth.AlertView;
import union.union_vr1.BlueTooth.Print;
import union.union_vr1.BlueTooth.PrintCobros;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Agente_Login;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.Utils;

/**
 * Created by Kelvin on 02/01/2016.
 */
public class VMovil_BluetoothImpCobros extends Activity implements View.OnClickListener {

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
    private String importePagado;
    private String textoImpresion = "";
    private String textoImpresionCabecera = "";
    private String textoVentaImpresion = "";


    private boolean isSincronized = false;
    private boolean estado = false;
    private boolean isConnected = false;
    private int countImpresion = 0;
    private String idComprobante = "";
    private String COMPROBANTE;
    private String IMPORTE;
    private String CLIENTE;
    private String AGENTE;

    private boolean enabledBluetooth = false;

    private DbAdapter_Comprob_Cobro dbAdapter_comprob_cobro;
    private DbAdapter_Agente_Login dbAdapter_agente_login;
    private DbAdapter_Comprob_Venta_Detalle dbHelperComprobanteVentaDetalle;
    private DbAdapter_Temp_Session session;
    private int pulgadasImpresora = 0;


    //PANTALLA

    private String textoEmpresa = "\n"
            + "    UNIVERSIDAD PERUANA UNION   \n"
            + "     Cent.aplc. Prod. Union     \n"
            + "   C. Central Km 19 Villa Union \n"
            + " Lurigancho-Chosica Fax: 6186311\n"
            + "      Telf: 6186309-6186310     \n"
            + " Casilla 3564, Lima 1, LIMA PERU\n"
            + "         RUC: 20138122256       ";
    private String ventaCabecera = "";
    private String textoImpresionContenidoLeft = "";
    private String textoImpresionContenidoRight = "";
    private String textoImpresionContenidoBottom = "";


    //BLUETOOTH VARIABLES
    private String defaultAdressImpresora = "00:12:6F:XX:XX:XX";

    private static final int REQUEST_ENABLE_BT = 2;


    private BluetoothAdapter mBluetoothAdapter;
    private Vector<BluetoothDevice> remoteDevices;


    private Thread hThread;
    private boolean connected;
    private Context contexto;

    private BluetoothPort bp;

    Utils df = new Utils();
    private static String TAG = VMovil_BluetoothImpCobros.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_imprimir_cobros);
        contexto = this;
        bluetoothSetup();
        dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(this);
        dbHelperComprobanteVentaDetalle = new DbAdapter_Comprob_Venta_Detalle(this);
        dbAdapter_comprob_cobro.open();
        dbHelperComprobanteVentaDetalle.open();
        dbAdapter_agente_login = new DbAdapter_Agente_Login(this);
        dbAdapter_agente_login.open();
        session = new DbAdapter_Temp_Session(this);
        session.open();
        dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(this);
        dbAdapter_comprob_cobro.open();
        idLiquidacion = session.fetchVarible(3);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        nameImpresora = SP.getString("impresoraNombre", defaultNameImpresora);
        pulgadasImpresora = Integer.parseInt(SP.getString("impresoraAncho", "3"));
        defaultAdressImpresora = session.fetchMAC();

        Log.d(TAG, "ADRESS IMPRESORA : " + defaultAdressImpresora);


        buttonImprimir = (Button) findViewById(R.id.buttonImprimir);
        buttonSincronizar = (Button) findViewById(R.id.buttonSincronizar);
        textViewImprimirCabecera = (TextView) findViewById(R.id.textViewImprimirCabecera);
        textViewImprimirContenidoLeft = (TextView) findViewById(R.id.textViewImprimirContenidoLeft);
        textViewImprimirContenidoRight = (TextView) findViewById(R.id.textViewImprimirContenidoRight);
        textViewContenidoBottom = (TextView) findViewById(R.id.textViewBottom);
        textViewVentaCabecera = (TextView) findViewById(R.id.textViewVentaCabecera);

        idComprobante = getIntent().getExtras().getString("idComprobante");
        importePagado = getIntent().getExtras().getString("importe");
        Log.d(TAG, "ID_COMPROBANTE : " + idComprobante);
        textoImpresion = generarTextoImpresion(idComprobante, pulgadasImpresora);

        Log.d(TAG, "TEXTO IMPRESION : " + textoImpresion);


        textViewImprimirCabecera.setText(textoEmpresa);
        textViewVentaCabecera.setText(ventaCabecera);
        textViewImprimirContenidoLeft.setText(textoImpresionContenidoLeft);
        textViewImprimirContenidoRight.setText(textoImpresionContenidoRight);
        textViewContenidoBottom.setText(textoImpresionContenidoBottom);
        Log.d(TAG, "TEXTO textoEmpresa : " + textoEmpresa);
        Log.d(TAG, "TEXTO ventaCabecera : " + ventaCabecera);
        Log.d(TAG, "TEXTO textoImpresionContenidoLeft : " + textoImpresionContenidoLeft);
        Log.d(TAG, "TEXTO textoImpresionContenidoRight : " + textoImpresionContenidoRight);

        if (!connected) {
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
    }

    private void bluetoothSetup() {
        // Initialize
        clearBtDevData();
        bp = BluetoothPort.getInstance();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private String generarTextoImpresion(String idComprobante, int pulgadas) {

        String texto = "";

        String comprobante = "";
        String cliente = "";
        String agente = "";


        Cursor cursorCabecera = dbAdapter_comprob_cobro.printCabecera(idComprobante);

        Log.d(TAG, " COUNT CURSOR VENTA CABECERA : " + cursorCabecera.getCount());

        if (cursorCabecera.getCount() > 0) {
            cursorCabecera.moveToFirst();
            Log.d(TAG, "IdComprobante" + cursorCabecera.getString(cursorCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_id_comprobante_cobro)));
            comprobante = cursorCabecera.getString(cursorCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_doc));
            cliente = cursorCabecera.getString(cursorCabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_nom_cliente));
            agente = cursorCabecera.getString(cursorCabecera.getColumnIndexOrThrow(DbAdapter_Agente.AG_nombre_agente));

            ventaCabecera += "F. RECIBO  : " + Utils.getDatePhone() + "\n";
            ventaCabecera += "F. EMISION   : " + Utils.getDatePhone() + " " + Utils.getTimePhone() + "  \n";
            ventaCabecera += "COMPROBANTE  : " + comprobante + "\n";
            ventaCabecera += "CLIENTE : " + cliente + "\n";
            ventaCabecera += "AGENTE : " + agente + "\n";
            ventaCabecera += "GLOSA : Contado \n";

            COMPROBANTE = comprobante;
            IMPORTE = importePagado;
            CLIENTE = cliente;
            AGENTE = agente;
            //ventaDetalle+=String.format("%-4s",cantidad) + String.format("%-31s",nombreProducto)+String.format("%1$5s"  ,df.format(precioUnitario)) +String.format("%1$8s"  ,df.format(importe)) + "\n";
            textoImpresionContenidoLeft += String.format("%-5s", comprobante + "\n");
            textoImpresionContenidoRight += String.format("%-5s", df.format(Double.parseDouble(Utils.replaceComa(importePagado)))) + "\n";


            //texto += "\n";


        }


        return texto;
    }

    private void clearBtDevData() {
        remoteDevices = new Vector<BluetoothDevice>();
    }

    @Override
    public void onBackPressed() {
        //super.onStop();
        super.onBackPressed();
        Intent intent = new Intent(this, VMovil_Evento_Establec.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if ((hThread != null) && (hThread.isAlive())) {
            hThread.interrupt();
            hThread = null;
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSincronizar:

                if (!connected) {
                    connectInit();
                    try {
                        btConn(mBluetoothAdapter.getRemoteDevice(defaultAdressImpresora));
                    } catch (IllegalArgumentException e) {
                        // Bluetooth Address Format [OO:OO:OO:OO:OO:OO]
                        AlertView.showAlert(e.getMessage(), contexto);
                        return;
                    } catch (IOException e) {
                        AlertView.showAlert(e.getMessage(), contexto);
                        return;
                    }
                }
                // Disconnect routine.
                else {
                    // Always run.
                    btDisconn();
                }

                break;
            case R.id.buttonImprimir:

                PrintCobros print = new PrintCobros(contexto);

                try {
                    print.printDocumento(idComprobante,IMPORTE,COMPROBANTE,CLIENTE,AGENTE);
                } catch (JposException e) {
                    e.printStackTrace();
                    AlertView.showAlert(e.getMessage(), contexto);
                    return;
                }


                break;
            default:
                break;
        }

    }

    // Bluetooth Connection method.
    private void btConn(final BluetoothDevice btDev) throws IOException {
        new connTask().execute(btDev);
    }

    private void connectInit() {
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            try {
                Thread.sleep(3600);
            } catch (Exception e) {
            }
        }
    }

    public class connTask extends AsyncTask<BluetoothDevice, Void, Integer> {
        private final ProgressDialog dialog = new ProgressDialog(VMovil_BluetoothImpCobros.this);
//		private BluetoothPort bp;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
//			bp = BluetoothPort.getInstance();
            dialog.setTitle("Bluetooth");
            dialog.setMessage("Connecting");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(BluetoothDevice... params) {
            // TODO Auto-generated method stub
            Integer retVal = null;
            try {
                bp.connect(params[0]);
                retVal = new Integer(0);
            } catch (IOException e) {

                retVal = new Integer(-1);
            }
            return retVal;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            if (result.intValue() == 0) {
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

            } else if (result.intValue() == -1) {
                Toast toast = Toast.makeText(contexto, "ERROR, REVISE LA IMPRESORA.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 8);
                toast.show();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();


            }
            super.onPostExecute(result);
        }
    }

    private void btDisconn() {
//		mBluetoothAdapter.disable();
        try {
            bp.disconnect();
            Thread.sleep(1200);
        } catch (Exception e) {
            Toast toast = Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 8);
            toast.show();
        }

        if ((hThread != null) && (hThread.isAlive()))
            hThread.interrupt();
        connected = false;
        // UI
        buttonSincronizar.setText("SINCRONIZAR");
        buttonImprimir.setEnabled(connected);
        buttonImprimir.setAlpha((float) 0.0);
        buttonImprimir.setBackgroundColor(getApplication().getResources().getColor(R.color.PersonalizadoSteve4));

        Toast toast = Toast.makeText(contexto, "DESCONECTADO", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 8);
        toast.show();
    }

}
