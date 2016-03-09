package union.union_vr1.Vistas;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.AsyncTask.CerrarCaja;
import union.union_vr1.AsyncTask.ExportService;
import union.union_vr1.AsyncTask.SolicitarCredito;
import union.union_vr1.Objects.DevolucionEstado;
import union.union_vr1.R;
import union.union_vr1.Servicios.ServiceExport;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.CursorAdapter_ReporteComprobante;
import union.union_vr1.Sqlite.CursorAdapter_ResumenGastos;
import union.union_vr1.Sqlite.CursorResumenComprobantes;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DBAdapter_Temp_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Cobros_Manuales;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Exportacion_Comprobantes;
import union.union_vr1.Sqlite.DbAdapter_Forma_Pago;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Impresion_Cobros;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Resumen_Caja;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.MyApplication;
import union.union_vr1.Utils.Utils;
import union.union_vr1.VMovil_BluetoothImprimir;


public class VMovil_Resumen_Caja extends TabActivity implements View.OnClickListener {

    TabHost tabHost;
    private DbAdapter_Stock_Agente dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private DbAdapter_Resumen_Caja dbHelperRC;
    private DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
    private DbAdapter_Forma_Pago dbAdapter_forma_pago;
    //private SimpleCursorAdapter dataAdapterRC;
    private CursorAdapter_ReporteComprobante cursorAdapterReporteComprobante;
    private DbGastos_Ingresos dbHelperGastosIngr;
    private DbAdapter_Agente dbAdapter_agente;
    private DbAdapter_Temp_Session session;
    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private int idLiquidacion;
    private int idAgente;
    private String nombreAgente = "Agente 001";
    private TextView textViewFecha, textViewLiquidacion, textViewAgente;
    private View viewResumenTotal;
    private TextView textViewTotalDescripcion;
    private TextView textViewTotalN;
    private TextView textViewTotalEmitido;
    private TextView textViewTotalPagado;
    private TextView textViewTotalCobrado;

    private View viewResumenGastos;
    private ListView listViewResumenGastos;
    private TextView textViewResumenGastosTotalRuta;
    /*private TextView textViewResumenGastosTotalPlanta;*/
    private View viewlayoutFooterGastos;
    private TextView textViewResumenGastoNombre;
    private TextView textViewSlideCargar;
    private TextView textViewResumenIngresos;
    private TextView textViewIngresosTipoNombre;
    private TextView textViewnIngresosTipoCantidad;

    private TextView textViewResumenGastos;
    private TextView textViewResumenARendir;
    private TextView textviewSlideConsultarInventario;
    private View viewResumen;
    private Activity mainActivity;
    private Button button;


    Double totalRuta = 0.0;
    Double totalPlanta = 0.0;

    int nTotal = 0;
    Double emitidoTotal = 0.0;
    Double pagadoTotal = 0.0;
    Double cobradoTotal = 0.0;

    private Activity activity;


    //SLIDING MENU
    private DbGastos_Ingresos dbGastosIngresos;
    //private DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
    private DbAdapter_Agente dbHelperAgente;
    //
    private DbAdapter_Impresion_Cobros dbAdapter_impresion_cobros;


    SlidingMenu menu;
    View layoutSlideMenu;
    TextView textViewSlidePrincipal;
    TextView textViewSlideCliente;
    TextView textviewSlideCobranzas;
    TextView textviewSlideGastos;
    TextView textviewSlideResumen;
    TextView textviewSlideARendir;
    TextView textViewSlideNombreAgente;
    TextView textViewSlideNombreRuta;
    Button buttonSlideNroEstablecimiento;

    TextView textViewIngresosTotales;
    TextView textViewGastos;


    int slideIdAgente = 0;
    int slideIdLiquidacion = 0;


    String slideNombreRuta = "";
    int slideNumeroEstablecimientoxRuta = 0;
    String slideNombreAgente = "";

    Double slide_emitidoTotal = 0.0;
    Double slide_pagadoTotal = 0.0;
    Double slide_cobradoTotal = 0.0;

    Double slide_totalRuta = 0.0;
    Double slide_totalPlanta = 0.0;
    Double slide_ingresosTotales = 0.0;
    Double slide_gastosTotales = 0.0;
    Double slide_aRendir = 0.0;

    //private CerrarCaja cerrarCaja;
    Utils df = new Utils();
    //FIREBASE
    private Firebase rootRef = null;
    private Firebase devolucionRef = null;

    private static final String TAG = VMovil_Resumen_Caja.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_resumen_caja);

        mainActivity = this;
        activity = this;


        Firebase.setAndroidContext(this);

        rootRef = new Firebase(Constants._APP_ROOT_FIREBASE);
        devolucionRef = rootRef.child(Constants._CHILD_DEVOLUCION);

        session = new DbAdapter_Temp_Session(this);
        session.open();

        dbAdapter_impresion_cobros = new DbAdapter_Impresion_Cobros(this);
        dbAdapter_impresion_cobros.open();

        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();


        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(this);
        dbAdaptert_evento_establec.open();

        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(this);
        dbAdapter_informe_gastos.open();


        dbAdapter_forma_pago = new DbAdapter_Forma_Pago(this);
        dbAdapter_forma_pago.open();
        //SLIDING MENU
        dbGastosIngresos = new DbGastos_Ingresos(this);
        dbGastosIngresos.open();
        //cerrarCaja = new CerrarCaja(mainActivity);

        //dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(this);
        //dbAdapter_informe_gastos.open();

        dbHelperAgente = new DbAdapter_Agente(this);
        dbHelperAgente.open();


        String fecha = Utils.getDatePhone();

        idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);

        Cursor cursor = dbAdapter_agente.fetchAgentesByIds(idAgente, idLiquidacion);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            nombreAgente = cursor.getString(cursor.getColumnIndexOrThrow(dbAdapter_agente.AG_nombre_agente));
        }


        textViewAgente = (TextView) findViewById(R.id.textViewEjecutivo);
        textViewLiquidacion = (TextView) findViewById(R.id.textViewLiquidacionNumero);
        textViewFecha = (TextView) findViewById(R.id.textViewFecha);

        textViewAgente.setText("Ejecutivo : " + nombreAgente);
        textViewFecha.setText("Fecha : " + Utils.getDateFull());
        textViewLiquidacion.setText("Liquidación Nro : " + idLiquidacion);


        dbHelperGastosIngr = new DbGastos_Ingresos(this);
        dbHelperGastosIngr.open();

        tabHost = (TabHost) findViewById(android.R.id.tabhost);

        dbHelper = new DbAdapter_Stock_Agente(this);
        dbHelper.open();


        dbHelperRC = new DbAdapter_Resumen_Caja(this);
        dbHelperRC.open();

        TabSpec spec = tabHost.newTabSpec("Tab1");
        spec.setContent(R.id.VRC_THingresos);
        spec.setIndicator("Ingresos y Gastos");
        displayListIngresosGastos();
        tabHost.addTab(spec);

        TabSpec spec2 = tabHost.newTabSpec("Tab2");
        spec2.setContent(R.id.VRC_THiventas);
        spec2.setIndicator("Inventario Ventas");
        displayListStockVenta();
        tabHost.addTab(spec2);

        TabSpec spec3 = tabHost.newTabSpec("Tab3");
        spec3.setContent(R.id.VRC_THiapt);
        spec3.setIndicator("Inventario APT");
        displayListStockApt();
        tabHost.addTab(spec3);

        //SLIDING MENU
        showSlideMenu(mainActivity);

        validarDevoluciones();
        //bloquear el botón cerrar caja si hay producdtos malos
        Log.d(TAG, "FECHA :" + fecha);
        Query queryRef = devolucionRef.orderByChild("fecha").equalTo(fecha);

        queryRef.addChildEventListener(new ChildEventListener() {
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
                    validarDevoluciones();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Log.d(TAG, "ADDED dataSnapshot : " + dataSnapshot.getValue());
                DevolucionEstado devolucionEstado = dataSnapshot.getValue(DevolucionEstado.class);
                Log.d(TAG, "ADDED Monto : " + devolucionEstado.getEstado());
                Log.d(TAG, "ADDED Estado: " + devolucionEstado.getFecha());
                Log.d(TAG, "ADDED Estado: " + devolucionEstado.getAgenteId());
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
        });


    }

    private void validarDevoluciones() {
        Cursor cursor = dbHelper.fetchDevolucionesMalas(idLiquidacion);
        if (cursor.getCount() > 0) {
            //SÍ HAY DEVOLUCIONES
            int estadoDevoluciones = session.fetchVarible(Constants.SESSION_ESTADO_DEVOLUCIONES);
            switch (estadoDevoluciones) {
                //ESTÁN BLOQUEADAS
                case 0:
                    button.setEnabled(false);
                    button.setBackgroundColor(mainActivity.getResources().getColor(R.color.Dark4));
                    break;
                //YA SE DESBLOQUEARON
                case 1:
                    button.setEnabled(true);
                    button.setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                    break;
                default:
                    button.setEnabled(true);
                    button.setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                    break;

            }

        } else {
            //No hay devoluciones, no bloquear el botón
            button.setEnabled(true);
            button.setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_imprimir, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intentExportService = new Intent(mainActivity, ServiceExport.class);
        intentExportService.setAction(Constants.ACTION_EXPORT_SERVICE);
        switch (id) {
            /*case R.id.buttonPrint:
                Intent intent = new Intent(mainActivity, ImprimirArqueoCaja.class);
                startActivity(intent);
                break;*/
            case R.id.buttonExportERP:

                mainActivity.startService(intentExportService);
                break;

            case R.id.imprimirDisponible:
                //mainActivity.startService(intentExportService);
                Intent intentImprimirStock = new Intent(mainActivity, ImprimirStockDisponible.class);
                startActivity(intentImprimirStock);
                break;

            case R.id.imprimirDevoluciones:
                //mainActivity.startService(intentExportService);
                Intent intentImprimirDevoluciones = new Intent(mainActivity, ImprimirDevoluciones.class);
                startActivity(intentImprimirDevoluciones);
                break;
            case R.id.buttonResumenTransferencias:
                Intent intentB = new Intent(mainActivity, ArqueoCaja.class);
                startActivity(intentB);
                break;
            default:
                //ON ITEM SELECTED DEFAULT
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void displayListStockVenta() {
        //vista
        ListView listView = (ListView) findViewById(R.id.VRC_listarResumenVentas);
        listView.addHeaderView(getLayoutInflater().inflate(R.layout.cabecera_inventario_ventas, null));
        //datos
        Cursor cursor = dbHelper.fetchAllStockAgenteVentas(idLiquidacion);
        //nombre de las columnas
        String[] columns = new String[]{
                DbAdapter_Stock_Agente.ST_nombre,
                DbAdapter_Stock_Agente.ST_inicial,
                DbAdapter_Stock_Agente.ST_ventas,
                DbAdapter_Stock_Agente.ST_canjes,
                DbAdapter_Stock_Agente.ST_disponible
        };
        //VISTA
        int[] to = new int[]{
                R.id.VRC_TXproducto,
                R.id.VRC_TXinicialFin,
                R.id.VRC_TXventa,
                R.id.VRC_TXcanjes,
                R.id.VRC_TXdisp
        };
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_resumen_ventas,
                cursor,
                columns,
                to,
                0);
        listView.setAdapter(dataAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //SLIDING MENU
        changeDataSlideMenu();
    }

    public void displayListStockApt() {
        ListView listView = (ListView) findViewById(R.id.VRC_listarResumenAPT);

        listView.addHeaderView(getLayoutInflater().inflate(R.layout.cabecera_inventario_apt, null));

        Cursor cursor = dbHelper.fetchAllStockAgenteVentas(idLiquidacion);
        String[] columns = new String[]{
                DbAdapter_Stock_Agente.ST_nombre,
                DbAdapter_Stock_Agente.ST_final,
                DbAdapter_Stock_Agente.ST_disponible,
                DbAdapter_Stock_Agente.ST_buenos,
                DbAdapter_Stock_Agente.ST_malos
        };

        int[] to = new int[]{
                R.id.VRC_TXproducto,
                R.id.VRC_TXfinal,
                R.id.VRC_TXdisponible,
                R.id.VRC_TXbuenos,
                R.id.VRC_TXmalos
        };

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_resumen_apt,
                cursor,
                columns,
                to,
                0);

        listView.setAdapter(dataAdapter);

    }

    public void displayListIngresosGastos() {


        Cursor cursor = dbHelperGastosIngr.listarIngresosGastos(idLiquidacion);


        viewResumenTotal = getLayoutInflater().inflate(R.layout.resumen_total, null);
        textViewTotalDescripcion = (TextView) viewResumenTotal.findViewById(R.id.resumen_total_descripcion);
        textViewTotalN = (TextView) viewResumenTotal.findViewById(R.id.resumen_total_n);
        textViewTotalEmitido = (TextView) viewResumenTotal.findViewById(R.id.resumen_total_emitido);
        textViewTotalPagado = (TextView) viewResumenTotal.findViewById(R.id.resumen_total_pagado);
        textViewTotalCobrado = (TextView) viewResumenTotal.findViewById(R.id.resumen_total_cobrado);


        cursorAdapterReporteComprobante = new CursorAdapter_ReporteComprobante(VMovil_Resumen_Caja.this, cursor);


        Cursor cursorResumen = cursorAdapterReporteComprobante.getCursor();
        cursorResumen.moveToFirst();


        for (cursorResumen.moveToFirst(); !cursorResumen.isAfterLast(); cursorResumen.moveToNext()) {
            int n = cursorResumen.getInt(cursorResumen.getColumnIndexOrThrow("n"));
            Double emitido = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("emitidas"));
            Double pagado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("pagado"));
            Double cobrado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("cobrado"));
            nTotal += n;
            emitidoTotal += emitido;
            pagadoTotal += pagado;
            cobradoTotal += cobrado;

        }

        textViewTotalCobrado.setText("Total");
        textViewTotalN.setText("" + nTotal);
        textViewTotalEmitido.setText("" + df.format(emitidoTotal));
        textViewTotalPagado.setText("" + df.format(pagadoTotal));
        textViewTotalCobrado.setText("" + df.format(cobradoTotal));


        ListView listView = (ListView) findViewById(R.id.VRC_listarResumenCaja);
        //RESUMEN CABECERA
        viewResumen = getLayoutInflater().inflate(R.layout.resumen, null);

        textViewResumenIngresos = (TextView) viewResumen.findViewById(R.id.resumenIngresosTotales);
        textViewIngresosTipoNombre = (TextView)viewResumen.findViewById(R.id.textViewNombreIngresos);
        textViewnIngresosTipoCantidad = (TextView)viewResumen.findViewById(R.id.textViewTipoIngresosCant);

        textViewResumenGastos = (TextView) viewResumen.findViewById(R.id.resumenGastosTotales);
        textViewResumenARendir = (TextView) viewResumen.findViewById(R.id.resumenEfectivoARendir);

        listView.addHeaderView(viewResumen);


        listView.addHeaderView(getLayoutInflater().inflate(R.layout.resumen_informe_ingresos_cabecera, null));

        listView.setAdapter(cursorAdapterReporteComprobante);
        //listView.setAdapter(cursorAdapter);
        listView.addFooterView(viewResumenTotal);


        viewResumenGastos = getLayoutInflater().inflate(R.layout.resumen_gastos, null);

        listViewResumenGastos = (ListView) viewResumenGastos.findViewById(R.id.listViewResumenGastos);


        viewlayoutFooterGastos = getLayoutInflater().inflate(R.layout.resumen_informe_gastos, null);
        listViewResumenGastos.addFooterView(viewlayoutFooterGastos);
        textViewResumenGastoNombre = (TextView) viewlayoutFooterGastos.findViewById(R.id.textviewNombre);
        /*textViewResumenGastosTotalPlanta = (TextView) viewlayoutFooterGastos.findViewById(R.id.textViewGastoPlanta);*/
        textViewResumenGastosTotalRuta = (TextView) viewlayoutFooterGastos.findViewById(R.id.textViewGastoRuta);


        Cursor cursorGastos = dbAdapter_informe_gastos.resumenInformeGastos(getDayPhone());

        Cursor cursorTipoIngresos = dbAdapter_forma_pago.fetchAllFormaPagos();



        String cadenaDescripciontipoIngreso ="";
        String cadenaTotalTipoIngreso = "";
        for (cursorTipoIngresos.moveToFirst(); !cursorTipoIngresos.isAfterLast(); cursorTipoIngresos.moveToNext()) {
            int _id = cursorTipoIngresos.getInt(cursorTipoIngresos.getColumnIndexOrThrow(DbAdapter_Forma_Pago.FP_id));
            int _id_forma_pago = cursorTipoIngresos.getInt(cursorTipoIngresos.getColumnIndexOrThrow(DbAdapter_Forma_Pago.FP_id_forma_pago));
            String descripcionTipoIngreso = cursorTipoIngresos.getString(cursorTipoIngresos.getColumnIndexOrThrow(DbAdapter_Forma_Pago.FP_detalle));
            int _selected = cursorTipoIngresos.getInt(cursorTipoIngresos.getColumnIndexOrThrow(DbAdapter_Forma_Pago.FP_selected));

            Double totalVenta = 0.0;
            Double totalCobro = 0.0;
            Double totalIngreso = 0.0;

            totalVenta = dbAdapter_informe_gastos. getTotalVentaByTipoIngreso(idLiquidacion, _id_forma_pago);
            totalCobro = dbAdapter_informe_gastos.getTotalCobroByTipoIngreso(idLiquidacion, _id_forma_pago);

            totalIngreso = totalVenta + totalCobro;



            if (totalIngreso >0 ){
                cadenaDescripciontipoIngreso += "Ingresos en "+ descripcionTipoIngreso+" : \n";
                cadenaTotalTipoIngreso += df.format(totalIngreso)+"\n";

            }
        }

        textViewIngresosTipoNombre.setText(cadenaDescripciontipoIngreso);
        textViewnIngresosTipoCantidad.setText(cadenaTotalTipoIngreso);

        CursorAdapter_ResumenGastos cursorAdapter_resumenGastos = new CursorAdapter_ResumenGastos(VMovil_Resumen_Caja.this, cursorGastos);

        listViewResumenGastos.setAdapter(cursorAdapter_resumenGastos);
        Cursor cursorTotalGastos = cursorGastos;
        cursorTotalGastos.moveToFirst();

        for (cursorTotalGastos.moveToFirst(); !cursorTotalGastos.isAfterLast(); cursorTotalGastos.moveToNext()) {
            Double rutaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("RUTA"));
            Double plantaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("PLANTA"));

            totalRuta += rutaGasto;
            totalPlanta += plantaGasto;
        }
        textViewResumenGastoNombre.setText("Total");
        textViewResumenGastoNombre.setTypeface(null, Typeface.BOLD);

        /*textViewResumenGastosTotalPlanta.setText("S/. " + df.format(totalPlanta));
        textViewResumenGastosTotalPlanta.setTypeface(null, Typeface.BOLD);*/

        textViewResumenGastosTotalRuta.setText("" + df.format(totalRuta));
        textViewResumenGastosTotalRuta.setTypeface(null, Typeface.BOLD);

        //listViewResumenGastos.addFooterView(viewlayoutFooterGastos);

        Double ingresosTotales = cobradoTotal + pagadoTotal;
        Double gastosTotales = totalRuta;
        Double aRendir = ingresosTotales - gastosTotales;


        textViewResumenIngresos.setText("S/. " + df.format(ingresosTotales));

        textViewResumenGastos.setText("S/. " + df.format(gastosTotales));
        textViewResumenARendir.setText("S/. " + df.format(aRendir));


        listView.addFooterView(viewResumenGastos);


        final View viewCerrarCaja = getLayoutInflater().inflate(R.layout.layout_cerrar_caja, null);
        button = (Button) viewCerrarCaja.findViewById(R.id.buttonCerrarCaja);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*session.deleteVariable(9);
                session.createTempSession(9, 0);*/
                /*Intent intent = new Intent(activity, VMovil_Online_Pumovil.class);
                finish();
                startActivity(intent);*/

                if (conectadoWifi() || conectadoRedMovil()) {
                    new AlertDialog.Builder(mainActivity)
                            .setTitle("Cerrar cuenta")
                            .setMessage("" +
                                    "¿Está seguro que desea cerrar caja?")
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int estadoValidacion = session.fetchVarible(Constants.SESSION_VALIDACION_REGISTROS_EXPORTADOS);
                                    if (estadoValidacion == 0){
                                        int registrosEnCola = 11;
                                        registrosEnCola = validarExport();
                                        Toast.makeText(VMovil_Resumen_Caja.this, registrosEnCola + " registros en cola.", Toast.LENGTH_SHORT).show();
                                        if (registrosEnCola == 0) {
                                            dialogCerrarCaja().show();
                                        }
                                    }else if (estadoValidacion == 1){
                                        Toast.makeText(VMovil_Resumen_Caja.this, "REGISTROS EN COLA VALIDADOS DESDE FIREBASE.", Toast.LENGTH_SHORT).show();
                                        dialogCerrarCaja().show();
                                    }

                                }
                            }).create().show();

                } else {
                    Toast toast = Toast.makeText(mainActivity, "No se puede cerrar caja sin conexión.", Toast.LENGTH_SHORT);
                    toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                    TextView view = (TextView) toast.getView().findViewById(android.R.id.message);
                    view.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                    toast.show();
                }
            }
        });
        listView.addFooterView(viewCerrarCaja, null, true);

    }

    private int validarExport() {


        //DEFINO LAS VARIABLES A MIS MANEJADORES DE LAS TABLAS
        DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
        DbAdapter_Comprob_Venta dbAdapter_comprob_venta;
        DbAdapter_Comprob_Venta_Detalle dbAdapter_comprob_venta_detalle;
        DbAdapter_Histo_Venta dbAdapter_histo_venta;
        DbAdapter_Comprob_Cobro dbAdapter_comprob_cobro;
        DbAdapter_Histo_Venta_Detalle dbAdapter_histo_venta_detalle;
        DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
        DBAdapter_Temp_Autorizacion_Cobro dbAdapter_temp_autorizacion_cobro;
        DBAdapter_Temp_Canjes_Devoluciones dbAdapter_temp_canjes_devoluciones;
        DbAdapter_Cobros_Manuales dbAdapter_cobros_manuales;
        DbAdapter_Exportacion_Comprobantes dbAdapter_exportacion_comprobantes;


        //INSTANCIO LAS CLASES DE MIS MANEJADORES DE DB

        dbAdapter_temp_canjes_devoluciones = new DBAdapter_Temp_Canjes_Devoluciones(mainActivity);
        dbAdapter_temp_canjes_devoluciones.open();
        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();

        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(mainActivity);
        dbAdapter_comprob_venta = new DbAdapter_Comprob_Venta(mainActivity);
        //dbAdapter_comprob_venta_detalle = new DbAdapter_Comprob_Venta_Detalle(mainActivity);
        dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(mainActivity);
        dbAdapter_histo_venta_detalle = new DbAdapter_Histo_Venta_Detalle(mainActivity);
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(mainActivity);
        dbAdapter_histo_venta = new DbAdapter_Histo_Venta(mainActivity);
        dbAdapter_temp_autorizacion_cobro = new DBAdapter_Temp_Autorizacion_Cobro(mainActivity);
        dbAdapter_cobros_manuales = new DbAdapter_Cobros_Manuales(mainActivity);
        dbAdapter_exportacion_comprobantes = new DbAdapter_Exportacion_Comprobantes(mainActivity);

        //ABRO LA CONEXIÓN A LA DB
        dbAdapter_informe_gastos.open();
        dbAdapter_comprob_venta.open();
        //dbAdapter_comprob_venta_detalle.open();
        dbAdapter_histo_venta_detalle.open();
        dbAdapter_comprob_cobro.open();
        dbAdapter_histo_venta_detalle.open();
        dbAdaptert_evento_establec.open();
        dbAdapter_histo_venta.open();
        dbAdapter_temp_autorizacion_cobro.open();
        dbAdapter_cobros_manuales.open();
        dbAdapter_exportacion_comprobantes.open();



        /*Cursor cursorExportacionFlex = dbAdapter_exportacion_comprobantes.fetchAll();
        cursorExportacionFlex.moveToFirst();
        for (cursorExportacionFlex.moveToFirst(); !cursorExportacionFlex.isAfterLast(); cursorExportacionFlex.moveToNext()) {

            Log.d(TAG, "EXPORTACIÓN AL FLEX -->  _ID : " + cursorExportacionFlex.getLong(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id)) + ", _ID_SID : " +
                            cursorExportacionFlex.getInt(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id_sid)) + ", _ID_SQLITE : " +
                            cursorExportacionFlex.getInt(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_id_sqlite)) + ", ESTADO_SINCRONIZACIÓN : " +
                            cursorExportacionFlex.getInt(cursorExportacionFlex.getColumnIndexOrThrow(dbAdapter_exportacion_comprobantes.EC_estado_sincronizacion))

            );
        }*/
        //FILTRO LOS REGISTROS DE LAS TABLAS A EXPORTAR

        int colaComprobanteVenta = dbAdapter_comprob_venta.filterExport(idLiquidacion).getCount();
        int colaInformeGastos = dbAdapter_informe_gastos.filterExport(idLiquidacion).getCount();
        //int colaComprobanteVentaDetalle = dbAdapter_comprob_venta_detalle.filterExport().getCount();
        int colaComprobanteCobro = dbAdapter_comprob_cobro.filterExport(idLiquidacion).getCount();
        //
        // int colaInsertarCaja = dbAdapter_comprob_cobro.filterExportUpdatedAndEstadoCobro().getCount();
        int colaInsertarCaja = dbAdapter_impresion_cobros.listarParaExportar(idLiquidacion).getCount();
        int colaEventoEstablecimiento = dbAdaptert_evento_establec.filterExportUpdated(idLiquidacion).getCount();
        //histo venta
        //int colaHistoVentaDetalleCreated = dbAdapter_histo_venta_detalle.filterExport(idLiquidacion).getCount();
        int colaAutorizacionCobro = dbAdapter_temp_autorizacion_cobro.filterExport(idLiquidacion).getCount();
        int colaCobrosManuales = dbAdapter_cobros_manuales.filterExport(idLiquidacion).getCount();
        int colaExportacionFlex = dbAdapter_exportacion_comprobantes.filterExport(idLiquidacion).getCount();
        int colaExportCManuFlex = dbAdapter_cobros_manuales.filterExportForFlexId(idLiquidacion).getCount();
        int colaExportCNormFlex = dbAdapter_impresion_cobros.listarCobrosExpFlex(idLiquidacion).getCount();
        int colaCliente = dbAdaptert_evento_establec.listarToExport(idLiquidacion).getCount();
        //KELVIN LO REVISARÁ Y ME DIRÁ
        int colaCAnjesDevoluciones = dbAdapter_temp_canjes_devoluciones.getAllOperacionEstablecimiento(idLiquidacion).getCount();


        int totalRegistrosEnCola = colaInformeGastos + colaComprobanteVenta +
                colaComprobanteCobro + colaInsertarCaja + colaEventoEstablecimiento + colaAutorizacionCobro
                + colaCobrosManuales + colaExportacionFlex + colaExportCManuFlex + colaExportCNormFlex + colaCliente + colaCAnjesDevoluciones;

        Log.d(TAG, "COLA  TOTAL: " + totalRegistrosEnCola);
        Log.d(TAG, "COLA colaInformeGastos : " + colaInformeGastos);
        Log.d(TAG, "COLA colaComprobanteVenta : " + colaComprobanteVenta);
        Log.d(TAG, "COLA colaComprobanteCobro : " + colaComprobanteCobro);
        Log.d(TAG, "COLA colaInsertarCaja : " + colaInsertarCaja);
        Log.d(TAG, "COLA colaEventoEstablecimiento : " + colaEventoEstablecimiento);
        Log.d(TAG, "COLA colaAutorizacionCobro : " + colaAutorizacionCobro);
        Log.d(TAG, "COLA colaCobrosManuales : " + colaCobrosManuales);
        Log.d(TAG, "COLA colaExportacionFlex : " + colaExportacionFlex);
        Log.d(TAG, "COLA colaCAnjesDevoluciones : " + colaCAnjesDevoluciones);
        Log.d(TAG, "COLA colaExportCNormFlex : " + colaExportCNormFlex);
        Log.d(TAG, "COLA colaExportCManuFlex : " + colaExportCManuFlex);
        Log.d(TAG, "COLA colaCliente : " + colaCliente);


        return totalRegistrosEnCola;
    }

    private Dialog dialogCerrarCaja() {

        final View layout = View.inflate(this, R.layout.dialog_cerrar_caja, null);
        final EditText editTextKmFinal = ((EditText) layout.findViewById(R.id.editText_kmFinal));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingrese Km Final");
        builder.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                String kmFinal = editTextKmFinal.getText().toString().trim();
                if (kmFinal.length() > 0) {
                    int kmFinall = Integer.parseInt(kmFinal);

                    Toast toast = Toast.makeText(mainActivity.getApplicationContext(), "CERRANDO CAJA...", Toast.LENGTH_SHORT);

                    toast.getView().setBackgroundColor(mainActivity.getResources().getColor(R.color.verde));
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(mainActivity.getResources().getColor(R.color.Blanco));
                    toast.show();
                    Log.d("CERRAR CAJA", "" + idLiquidacion + "-" + slide_ingresosTotales + "-" + slide_gastosTotales + "" + slide_aRendir + "-" + kmFinall);
                    new CerrarCaja(mainActivity).execute("" + idLiquidacion, "" + Utils.formatDouble(slide_ingresosTotales), "" + Utils.formatDouble(slide_gastosTotales), "" + Utils.formatDouble(slide_aRendir), "" + kmFinall, "" + idAgente);
                }
            }
        });
        builder.setView(layout);
        return builder.create();
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    public String getDayPhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            //SLIDING MENU
            case R.id.slide_textViewCargarInventario:
                Intent cInventario = new Intent(this, VMovil_Cargar_Inventario.class);
                startActivity(cInventario);
                break;
            case R.id.slide_textviewPrincipal:
                Intent ip1 = new Intent(this, VMovil_Evento_Indice.class);
                finish();
                startActivity(ip1);
                break;
            case R.id.slide_textViewClientes:
                Intent ic1 = new Intent(this, VMovil_Menu_Establec.class);
                finish();
                startActivity(ic1);
                break;
            case R.id.slide_textViewCobranza:
                Intent cT1 = new Intent(this, VMovil_Cobros_Totales.class);
                finish();
                startActivity(cT1);
                break;
            case R.id.slide_TextViewGastos:
                Intent ig1 = new Intent(this, VMovil_Evento_Gasto.class);
                finish();
                startActivity(ig1);
                break;
            case R.id.slide_textViewResumen:
                menu.toggle();
                break;
            case R.id.slide_textViewARendir:

                break;
            case R.id.slide_textViewConsultarInventario:
                startActivity(new Intent(getApplicationContext(), VMovil_Consultar_Inventario.class));
                break;
            default:
                break;
        }
    }


    //SLIDING MENU
    public void showSlideMenu(Activity activity) {
        layoutSlideMenu = View.inflate(activity, R.layout.slide_menu, null);
        // configure the SlidingMenu
        menu = new SlidingMenu(activity);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.space_slide);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.space_slide);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(layoutSlideMenu);

        textViewSlideNombreAgente = (TextView) findViewById(R.id.slide_textViewNombreAgente);
        textViewSlideNombreRuta = (TextView) findViewById(R.id.slide_textViewNombreRuta);
        buttonSlideNroEstablecimiento = (Button) findViewById(R.id.slide_buttonNroEstablecimiento);
        textviewSlideConsultarInventario = (TextView) findViewById(R.id.slide_textViewConsultarInventario);
        textViewSlidePrincipal = (TextView) findViewById(R.id.slide_textviewPrincipal);
        textViewSlideCliente = (TextView) findViewById(R.id.slide_textViewClientes);
        textviewSlideCobranzas = (TextView) findViewById(R.id.slide_textViewCobranza);
        textviewSlideGastos = (TextView) findViewById(R.id.slide_TextViewGastos);
        textviewSlideResumen = (TextView) findViewById(R.id.slide_textViewResumen);
        textviewSlideARendir = (TextView) findViewById(R.id.slide_textViewARendir);

        textViewIngresosTotales = (TextView) findViewById(R.id.textView_IngresosTotales);
        textViewGastos = (TextView) findViewById(R.id.textView_Gastos);

        textViewSlideCargar = (TextView) findViewById(R.id.slide_textViewCargarInventario);
        textViewSlideCargar.setOnClickListener(this);
        textviewSlideConsultarInventario.setOnClickListener(this);
        textViewSlidePrincipal.setOnClickListener(this);
        textViewSlideCliente.setOnClickListener(this);
        textviewSlideCobranzas.setOnClickListener(this);
        textviewSlideGastos.setOnClickListener(this);
        textviewSlideResumen.setOnClickListener(this);
        textviewSlideARendir.setOnClickListener(this);


        slideIdAgente = session.fetchVarible(1);
        slideIdLiquidacion = session.fetchVarible(3);

        changeDataSlideMenu();


    }

    //SLIDING MENU
    public void changeDataSlideMenu() {

        //INICIALIZAMOS OTRA VEZ LAS VARIABLES
        slide_emitidoTotal = 0.0;
        slide_pagadoTotal = 0.0;
        slide_cobradoTotal = 0.0;
        slide_totalRuta = 0.0;
        slide_totalPlanta = 0.0;
        slide_ingresosTotales = 0.0;
        slide_gastosTotales = 0.0;
        slide_aRendir = 0.0;

        // AGENTE, RUTA Y ESTABLECIMIENTOS
        Cursor cursorAgente = dbHelperAgente.fetchAgentesByIds(slideIdAgente, slideIdLiquidacion);
        cursorAgente.moveToFirst();

        if (cursorAgente.getCount() > 0) {
            slideNombreRuta = cursorAgente.getString(cursorAgente.getColumnIndexOrThrow(dbHelperAgente.AG_nombre_ruta));
            slideNumeroEstablecimientoxRuta = cursorAgente.getInt(cursorAgente.getColumnIndexOrThrow(dbHelperAgente.AG_nro_bodegas));
            slideNombreAgente = cursorAgente.getString(cursorAgente.getColumnIndexOrThrow(dbHelperAgente.AG_nombre_agente));
        }

        //INGRESOS
        Cursor cursorResumen = dbGastosIngresos.listarIngresosGastos(slideIdLiquidacion);
        cursorResumen.moveToFirst();
        for (cursorResumen.moveToFirst(); !cursorResumen.isAfterLast(); cursorResumen.moveToNext()) {
            //int n = cursorResumen.getInt(cursorResumen.getColumnIndexOrThrow("n"));
            Double emitido = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("emitidas"));
            Double pagado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("pagado"));
            Double cobrado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("cobrado"));
            //nTotal += n;
            slide_emitidoTotal += emitido;
            slide_pagadoTotal += pagado;
            slide_cobradoTotal += cobrado;
        }
        //GASTOS
        Utils utils = new Utils();
        Cursor cursorTotalGastos = dbAdapter_informe_gastos.resumenInformeGastos(utils.getDayPhone());

        for (cursorTotalGastos.moveToFirst(); !cursorTotalGastos.isAfterLast(); cursorTotalGastos.moveToNext()) {
            Double rutaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("RUTA"));
            Double plantaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("PLANTA"));

            slide_totalRuta += rutaGasto;
            slide_totalPlanta += plantaGasto;
        }

        slide_ingresosTotales = slide_cobradoTotal + slide_pagadoTotal;
        slide_gastosTotales = slide_totalRuta;
        slide_aRendir = slide_ingresosTotales - slide_gastosTotales;


        //MOSTRAMOS EN EL SLIDE LOS DATOS OBTENIDOS
        textViewSlideNombreAgente.setText("" + slideNombreAgente);
        textViewSlideNombreRuta.setText("" + slideNombreRuta);
        buttonSlideNroEstablecimiento.setText("" + slideNumeroEstablecimientoxRuta);
        textviewSlideARendir.setText("Efectivo a Rendir S/. " + df.format(slide_aRendir));

        textViewIngresosTotales.setText("" + df.format(slide_ingresosTotales));
        textViewGastos.setText("" + df.format(slide_gastosTotales));

    }

    protected Boolean conectadoWifi() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

}

