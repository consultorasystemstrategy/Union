package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapterComprobanteVenta;
import union.union_vr1.Sqlite.CursorAdapterFacturas;
import union.union_vr1.Sqlite.CursorAdapter_Autorizacion_Cobros;
import union.union_vr1.Sqlite.CursorAdapter_Man_Can_Dev;
import union.union_vr1.Sqlite.CursorAdapter_Man_Cbrz;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DBAdapter_Temp_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.MyApplication;
import union.union_vr1.Utils.Utils;
import union.union_vr1.VMovil_BluetoothImprimir;

import static union.union_vr1.R.layout.prompts_cobros;
import static union.union_vr1.R.layout.prompts_cobros_fecha;

public class VMovil_Venta_Comprob extends Activity implements View.OnClickListener{

    private DBAdapter_Temp_Canjes_Devoluciones dbAdapter_temp_canjes_devoluciones;
    private DbAdapter_Temp_Session session;
    private int idEstablec;
    private DbAdapter_Comprob_Venta dbHelper;
    private CursorAdapterComprobanteVenta cursorAdapterComprobanteVenta;
    private DbAdapter_Comprob_Venta_Detalle dbHelper_Comp_Venta_Detalle;
    private DbAdapter_Stock_Agente dbHelper_Stock_Agente;
    private DbAdapter_Comprob_Cobro dbHelper_Comprob_Cobro;
    private DbAdapter_Canjes_Devoluciones dbHelper_Canjes_Dev;
    private DBAdapter_Temp_Autorizacion_Cobro dbAutorizaciones;
    private int idComprobante;
    private int idAgente;
    private int liquidacion;

    TabHost tH;

    //SLIDING MENU VENTAS
    private DbGastos_Ingresos dbGastosIngresos;
    private DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
    private DbAdapter_Agente dbHelperAgente;



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

    int slideIdAgente = 0;
    int slideIdLiquidacion = 0;



    String slideNombreRuta = "";
    int slideNumeroEstablecimientoxRuta = 0;
    String slideNombreAgente = "";

    Double slide_emitidoTotal = 0.0;
    Double slide_pagadoTotal = 0.0;
    Double slide_cobradoTotal = 0.0;

    Double slide_totalRuta =0.0;
    Double slide_totalPlanta = 0.0;
    Double slide_ingresosTotales = 0.0;
    Double slide_gastosTotales = 0.0;
    Double slide_aRendir = 0.0;

    //SLIDE VENTAS
    TextView textViewSlideNombreEstablecimiento;
    Button buttonSlideVentaDeHoy;
    Button buttonSlideDeudaHoy;
    TextView textViewSlideVenta;
    TextView textViewSlideCobro;
    TextView textViewSlideMantenimiento;
    TextView textViewSlideCanjesDevoluciones;
    private TextView textViewSlideCargar;
    int slideIdEstablecimiento;

    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DbAdapter_Comprob_Venta dbAdapter_comprob_venta;

    private Context contexto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter_temp_canjes_devoluciones = new DBAdapter_Temp_Canjes_Devoluciones(this);
        dbAdapter_temp_canjes_devoluciones.open();
        contexto = this;
        setContentView(R.layout.princ_venta_comprob);

        session = new DbAdapter_Temp_Session(this);
        session.open();

        //SLIDING MENU
        dbGastosIngresos = new DbGastos_Ingresos(this);
        dbGastosIngresos.open();

        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(this);
        dbAdapter_informe_gastos.open();

        dbHelperAgente = new DbAdapter_Agente(this);
        dbHelperAgente.open();

        //VENTAS
        dbAdaptert_evento_establec = new DbAdaptert_Evento_Establec(this);
        dbAdaptert_evento_establec.open();

        dbAdapter_comprob_venta = new DbAdapter_Comprob_Venta(this);
        dbAdapter_comprob_venta.open();

        dbAutorizaciones = new DBAdapter_Temp_Autorizacion_Cobro(this);
        dbAutorizaciones.open();



        dbHelper = new DbAdapter_Comprob_Venta(this);
        dbHelper.open();
        dbHelper_Comp_Venta_Detalle = new DbAdapter_Comprob_Venta_Detalle(this);
        dbHelper_Comp_Venta_Detalle.open();
        dbHelper_Stock_Agente = new DbAdapter_Stock_Agente(this);
        dbHelper_Stock_Agente.open();
        dbHelper_Comprob_Cobro = new DbAdapter_Comprob_Cobro(this);
        dbHelper_Comprob_Cobro.open();


        //---
        dbHelper_Canjes_Dev = new DbAdapter_Canjes_Devoluciones(this);
        dbHelper_Canjes_Dev.open();

        //idAgente=((MyApplication) this.getApplication()).getIdAgente();
        idAgente = session.fetchVarible(1);
        liquidacion = session.fetchVarible(3);

        tH = (TabHost) findViewById(R.id.tabMante);
        tH.setup();
        //idEstablec = ((MyApplication) this.getApplication()).getIdEstablecimiento();

        idEstablec =session.fetchVarible(2);

        //Item1
        TabHost.TabSpec spec = tH.newTabSpec("1");
        spec.setContent(R.id.comprob);
        spec.setIndicator("Comprobantes");
        displayListView();
        tH.addTab(spec);
        // Item2
        TabHost.TabSpec spec2 = tH.newTabSpec("2");
        spec2.setContent(R.id.cobranza);
        spec2.setIndicator("Cobranza");
        listarCobranzas();
        tH.addTab(spec2);
//Item 3
        TabHost.TabSpec spec3 = tH.newTabSpec("3");
        spec3.setContent(R.id.canje);
        spec3.setIndicator("Devoluciones");
        mostrarItemsDevoluciones(idEstablec);
        tH.addTab(spec3);
//Item 4
        TabHost.TabSpec spec4 = tH.newTabSpec("4");
        spec4.setContent(R.id.autoriz);
        spec4.setIndicator("Autorización");
        displayAutorizaciones();
        tH.addTab(spec4);


        //SLIDING MENU
        showSlideMenu(this);

    }

    private  void mostrarItemsDevoluciones(int establec){
        //listar devoluciones
        ListView listaCanjes_Dev = (ListView) findViewById(R.id.listarCanjDev);
        Cursor cursor = dbAdapter_temp_canjes_devoluciones.listarDevolucionesImpresion(establec+"");
        cursor.moveToFirst();
        CursorAdapter_Man_Can_Dev adapter = new CursorAdapter_Man_Can_Dev(getApplicationContext(), cursor);
        listaCanjes_Dev.setAdapter(adapter);
    }

    private void listarCobranzas() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Cursor cursor = dbHelper_Comprob_Cobro.listarComprobantesToCobrosMante("" + idEstablec);
        CursorAdapter_Man_Cbrz cAdapter_Cbrz_Man = new CursorAdapter_Man_Cbrz(this, cursor);
        final ListView listCbrz = (ListView) findViewById(R.id.VVCO_cbrz);
        listCbrz.setAdapter(cAdapter_Cbrz_Man);


        listCbrz.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cr2 = (Cursor) listCbrz.getItemAtPosition(i);
                if (cr2.getString(7).equals("Cobrado")) {
                    String idCompro = cr2.getString(0);
                    String factura = cr2.getString(1);
                    String hora = cr2.getString(5);
                    String fecha = cr2.getString(9);
                    Double monto = Double.parseDouble(cr2.getString(6));

                    dialog(idCompro, factura, hora, fecha, monto);
                } else {
                    Toast.makeText(getApplicationContext(), "Ya se Encuentra Anulado", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void dialog(final String idCompro, String factura, final String hora, final String fecha, final Double monto) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Anular");

        // set dialog message
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("¿Desea Anular?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dbHelper_Comprob_Cobro.updateComprobCobrosMan(idCompro, fecha, hora, monto, "1");
                        Toast.makeText(getApplicationContext(),
                                "Actualizado", Toast.LENGTH_SHORT).show();

                        Intent w = new Intent(getApplicationContext(), VMovil_Evento_Establec.class);
                        w.putExtra("idEstab", ""+idEstablec);
                        w.putExtra("idAgente", idAgente);

                        startActivity(w);
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),
                                "Cancelo", Toast.LENGTH_SHORT).show();
                    }
                });
        listarCobranzas();

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    private void displayAutorizaciones(){

        Cursor cursor = dbAutorizaciones.listarAutorizaciones(idEstablec);
        CursorAdapter_Autorizacion_Cobros adapterAutorizacion = new CursorAdapter_Autorizacion_Cobros(getApplicationContext(),cursor);
        ListView listAuCobros = (ListView) findViewById(R.id.listAutorizacionCobros);
        listAuCobros.setAdapter(adapterAutorizacion);
        listAuCobros.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor crCobros = (Cursor)adapterView.getItemAtPosition(i);
                crCobros.moveToPosition(i);
                String id = crCobros.getString(crCobros.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_autorizacion_cobro));
                String estado = crCobros.getString(crCobros.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_id_estado_solicitud));
                String idDetalleCobro = crCobros.getString(crCobros.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_montoCredito));
                String idComprobante = crCobros.getString(crCobros.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_id_comprobante));
                String fecha = crCobros.getString(crCobros.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_fechaLimite));
                if(estado.equals("1")){

                    Toast.makeText(getApplicationContext(),"Solicitud Aun por Aprobarse",Toast.LENGTH_SHORT).show();
                }if(estado.equals("2")){
                    select(crCobros,fecha);

                }if(estado.equals("4")){
                    selectEliminar(id,idDetalleCobro,idComprobante);
                    //Toast.makeText(getApplicationContext(),"Solicitud Anulada",Toast.LENGTH_SHORT).show();

                }if(estado.equals("5")){

                    Toast.makeText(getApplicationContext(),"Ya Ejecutada"+id+"-"+idDetalleCobro+"",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
    public void selectEliminar(final String idAutorizacion, final String idDetalleCobro, final String idComprobante){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Cancelar el Proceso");

        // set dialog message
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Se ha Negado la Prologa de Credito, tiene que Anular el Proceso.")
                .setCancelable(false)
                .setPositiveButton("Anular", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       // boolean up = dbAutorizaciones.anularAutorizacion(idAutorizacion, idDetalleCobro, idComprobante);
                        if (true) {
                            back();
                            Toast.makeText(getApplicationContext(), "Anulado Correctamente", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }
    public long calcularFecha(String fecha){
        long startDate=0;
        Log.d("FECHA","  fecha:"+fecha);
        try {
            String dateString = fecha;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateString);
            startDate = date.getTime();
            Log.d("FECHA","lon:"+startDate+"  fecha:"+fecha);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return  startDate;
    }
    public long calcularFechaHoy(){
        long startDate=0;

        try {
            String dateString = getDatePhone();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateString);
            startDate = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return  startDate;
    }

    public void cambiarFecha(Cursor cr, final String fecha){
        final int idAutorizacion = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_id_autorizacion_cobro));
        final int idAEstablecimiento = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_establec));
        final String idComprobanteCobro = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_id_comprobante));
        final Double montoCredito = cr.getDouble(cr.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_monto_pagado));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle("Confirmar Prologa de Pagos");
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout_cobros = inflater.inflate(prompts_cobros_fecha, null);
        final DatePicker dp = (DatePicker) layout_cobros.findViewById(R.id.fechakel);
        Log.d("PARAMS",""+idComprobanteCobro+"-"+montoCredito);
        dp.setMaxDate(calcularFecha(fecha));
        dp.setMinDate(calcularFechaHoy());
        alertDialogBuilder.setView(layout_cobros);
        // set dialog message
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("La Feha Limite es: "+fecha)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int day = dp.getDayOfMonth();
                        int month = dp.getMonth()+1;
                        int year = dp.getYear();
                        String fechaRenovada = day + "/" + month + "/" + year;
                        int aut = dbAutorizaciones.updateAutorizacionCobro_Au(idAutorizacion, 5, idAEstablecimiento, fecha);
                        int cobr = dbHelper_Comprob_Cobro.updateComprobCobros_Auto(idComprobanteCobro, montoCredito, fechaRenovada);

                        if (cobr == 1 && aut==1) {
                            Toast.makeText(getApplicationContext(), "Inserto Correctamente", Toast.LENGTH_SHORT).show();
                            back();

                        } else {
                            Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_SHORT).show();
                        }

                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),
                                "Cancelo", Toast.LENGTH_SHORT).show();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    public void select(final Cursor cr,final String fecha){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Confirmar Prologa de Pagos");

            // set dialog message
            AlertDialog.Builder builder = alertDialogBuilder
                    .setMessage("Aprobado, la Fecha Limite para el cobro es: " + fecha + "")
                    .setCancelable(false)
                    .setPositiveButton("Ir", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // boolean up = dbAutorizaciones.updateAutorizacionAprobado(idAutorizacion, idDetalleCobro);
                            if (true) {
                                cambiarFecha(cr,fecha);
                                Toast.makeText(getApplicationContext(), "Guardado Correctamente", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });


            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

    }

private void back(){
    Intent w = new Intent(getApplicationContext(), VMovil_Evento_Establec.class);
    w.putExtra("idEstab", ""+idEstablec);
    w.putExtra("idAgente", idAgente);

    startActivity(w);
}


    private void displayListView() {

        Cursor cursor = dbHelper.fetchAllComprobVenta(idEstablec, liquidacion);
        cursorAdapterComprobanteVenta = new CursorAdapterComprobanteVenta(this, cursor);


        ListView listView = (ListView) findViewById(R.id.VVCO_listar);
        listView.setAdapter(cursorAdapterComprobanteVenta);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                idComprobante =
                        Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_id_comprob)));

                int estado = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_estado_comp)));


                switch (estado) {
                    case 0:
                        Toast.makeText(getApplicationContext(),
                                "El comprobante se encuentra anulado", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        dialogAcciones(idComprobante);
                        break;
                    default:

                        break;

                }

            }
        });


        EditText myFilter = (EditText) findViewById(R.id.VVCO_buscar);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                cursorAdapterComprobanteVenta.getFilter().filter(s.toString());
            }
        });

        cursorAdapterComprobanteVenta.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchAllComprobVentaByName(idEstablec, liquidacion,constraint.toString());
            }
        });

    }


    public void dialogAcciones(final int idComprobante) {

        final String[] items = {"Imprimir","Anular"};
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Seleccionar una acción");
        dialogo.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item){
                    case 0:
                        Intent intent= new Intent(contexto, VMovil_BluetoothImprimir.class);
                        intent.putExtra("idComprobante",idComprobante);
                        finish();
                        startActivity(intent);
                        break;
                    case 1:

                        Cursor cursorComprobanteVentaDetalle = dbHelper_Comp_Venta_Detalle.fetchAllComprobVentaDetalleByIdComp(idComprobante);
                        cursorComprobanteVentaDetalle.moveToFirst();

                        int id_producto;
                        int cantidad;
                        int precioUnitario;
                        int costeVenta;

                        cursorComprobanteVentaDetalle.moveToFirst();
                        if (cursorComprobanteVentaDetalle.getCount() > 0) {
                            do {
                                id_producto = cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_id_producto));
                                cantidad = cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_cantidad));

                                dbHelper_Stock_Agente.updateStockAgenteCantidad(id_producto, cantidad, liquidacion);

                            } while (cursorComprobanteVentaDetalle.moveToNext());
                        } else {
                            Toast.makeText(getApplicationContext(), "No ha registros de este comprobante de venta : ", Toast.LENGTH_LONG).show();
                        }

                        dbHelper.updateComprobante(idComprobante, 0);

                        finish();
                        Intent intent2 = new Intent(getApplicationContext(), VMovil_Venta_Comprob.class);
                        startActivity(intent2);

                        Toast.makeText(getApplicationContext(), "Comprobante Anulado ", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        //
                        break;

                }

            }
        });
        dialogo.create();
        dialogo.show();
    }


    //SLIDING MENU
    public void showSlideMenu(Activity activity){
        layoutSlideMenu = View.inflate(activity, R.layout.slide_menu_ventas, null);
        // configure the SlidingMenu
        menu =  new SlidingMenu(activity);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.space_slide);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.space_slide);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(layoutSlideMenu);

        textViewSlideNombreAgente = (TextView)findViewById(R.id.slide_textViewNombreAgente);
        textViewSlideNombreRuta = (TextView)findViewById(R.id.slide_textViewNombreRuta);
        buttonSlideNroEstablecimiento = (Button) findViewById(R.id.slide_buttonNroEstablecimiento);

        textViewSlidePrincipal = (TextView)findViewById(R.id.slide_textviewPrincipal);
        textViewSlideCliente = (TextView)findViewById(R.id.slide_textViewClientes);
        textviewSlideCobranzas = (TextView)findViewById(R.id.slide_textViewCobranza);
        textviewSlideGastos = (TextView)findViewById(R.id.slide_TextViewGastos);
        textviewSlideResumen = (TextView)findViewById(R.id.slide_textViewResumen);
        textviewSlideARendir = (TextView)findViewById(R.id.slide_textViewARendir);


        textViewSlideNombreEstablecimiento = (TextView)findViewById(R.id.slideVentas_textViewCliente);
        buttonSlideVentaDeHoy  = (Button)findViewById(R.id.slideVentas_buttonVentaCosto);
        buttonSlideDeudaHoy = (Button)findViewById(R.id.slideVentas_buttonDeudas);
        textViewSlideVenta = (TextView)findViewById(R.id.slideVentas_textViewVenta);
        //COBRAR
        textViewSlideMantenimiento = (TextView)findViewById(R.id.slideVentas_textViewMantenimiento);
        textViewSlideCanjesDevoluciones  = (TextView)findViewById(R.id.slideVentas_textviewCanjesDevoluciones);
        textViewSlideCargar = (TextView)findViewById(R.id.slide_textViewCargarInventario);
        textViewSlideCargar.setOnClickListener(this);




        textViewSlidePrincipal.setOnClickListener(this);
        textViewSlideCliente.setOnClickListener(this);
        textviewSlideCobranzas.setOnClickListener(this);
        textviewSlideGastos.setOnClickListener(this);
        textviewSlideResumen.setOnClickListener(this);
        textviewSlideARendir.setOnClickListener(this);

        //VENTAS
        buttonSlideDeudaHoy.setOnClickListener(this);
        buttonSlideVentaDeHoy.setOnClickListener(this);
        textViewSlideVenta.setOnClickListener(this);
        textViewSlideMantenimiento.setOnClickListener(this);
        textViewSlideCanjesDevoluciones.setOnClickListener(this);
        textViewSlideNombreEstablecimiento.setOnClickListener(this);

        slideIdAgente = session.fetchVarible(1);
        slideIdLiquidacion  = session.fetchVarible(3);
        slideIdEstablecimiento = session.fetchVarible(2);


        changeDataSlideMenu();


    }

    //SLIDING MENU
    public void changeDataSlideMenu(){

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

        if (cursorAgente.getCount()>0){
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
        Cursor cursorTotalGastos =dbAdapter_informe_gastos.resumenInformeGastos(utils.getDayPhone());

        for (cursorTotalGastos.moveToFirst(); !cursorTotalGastos.isAfterLast(); cursorTotalGastos.moveToNext()){
            Double rutaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("RUTA"));
            Double plantaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("PLANTA"));

            slide_totalRuta += rutaGasto;
            slide_totalPlanta += plantaGasto;
        }

        slide_ingresosTotales = slide_cobradoTotal + slide_pagadoTotal;
        slide_gastosTotales = slide_totalRuta;
        slide_aRendir = slide_ingresosTotales-slide_gastosTotales;



        //MOSTRAMOS EN EL SLIDE LOS DATOS OBTENIDOS
        DecimalFormat df = new DecimalFormat("#.00");
        textViewSlideNombreAgente.setText(""+slideNombreAgente);
        textViewSlideNombreRuta.setText(""+slideNombreRuta);
        buttonSlideNroEstablecimiento.setText(""+slideNumeroEstablecimientoxRuta);
        textviewSlideARendir.setText("Efectivo a Rendir S/. " + df.format(slide_aRendir));


        //DATA VENTAS
        Cursor cursorEstablecimiento = dbAdaptert_evento_establec.listarEstablecimientosByID(slideIdEstablecimiento, slideIdLiquidacion);

        cursorEstablecimiento.moveToFirst();

        if (cursorEstablecimiento.getCount()>0){

            String nombre_establecimiento = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbAdaptert_evento_establec.EE_nom_establec));
            String nombre_cliente = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbAdaptert_evento_establec.EE_nom_cliente));
            int id_estado_atencion = Integer.parseInt(cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbAdaptert_evento_establec.EE_id_estado_atencion)));
            double deudaTotal = cursorEstablecimiento.getDouble(cursorEstablecimiento.getColumnIndexOrThrow("cc_re_monto_a_pagar")) ;


            textViewSlideNombreEstablecimiento.setText(""+nombre_establecimiento);
            buttonSlideDeudaHoy.setText(""+df.format(deudaTotal));
        }

        Cursor cursorVentasTotales = dbAdapter_comprob_venta.getTotalVentaByIdEstablecimientoAndLiquidacion(slideIdEstablecimiento, slideIdLiquidacion);
        cursorVentasTotales.moveToFirst();
        if (cursorVentasTotales.getCount()>0){
            Double total = cursorVentasTotales.getDouble(cursorVentasTotales.getColumnIndexOrThrow("total"));
            buttonSlideVentaDeHoy.setText(""+df.format(total));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeDataSlideMenu();
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
                Intent ir1 = new Intent(this, VMovil_Resumen_Caja.class);
                finish();
                startActivity(ir1);
                break;
            case R.id.slide_textViewARendir:

                break;
            //SLIDING MENU VENTAS
            case R.id.slideVentas_buttonVentaCosto:
                menu.toggle();
                break;
            case R.id.slideVentas_buttonDeudas:
                Intent id1 = new Intent(this, VMovil_Cobro_Credito.class);
                id1.putExtra("idEstabX", ""+slideIdEstablecimiento);
                finish();
                startActivity(id1);
                break;
            case R.id.slideVentas_textViewVenta:
                Intent iv1 = new Intent(this, VMovil_Venta_Cabecera.class);
                finish();
                iv1.putExtra("idEstabX", ""+slideIdEstablecimiento);
                startActivity(iv1);
                break;
            case R.id.slideVentas_textViewMantenimiento:
                menu.toggle();
                break;
            case R.id.slideVentas_textviewCanjesDevoluciones:
                Intent idh1 = new Intent(this, VMovil_Evento_Canjes_Dev.class);
                idh1.putExtra("idEstabX", ""+slideIdEstablecimiento);
                idh1.putExtra("idAgente", idAgente);
                finish();
                startActivity(idh1);
                break;
            case R.id.slideVentas_textViewCliente:
                menu.toggle();
            default:
                break;
        }
    }

}