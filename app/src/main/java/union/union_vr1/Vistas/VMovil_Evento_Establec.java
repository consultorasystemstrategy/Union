package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.AsyncTask.ExportMain;
import union.union_vr1.AsyncTask.ImportCredito;
import union.union_vr1.AsyncTask.ImportMain;
import union.union_vr1.R;
import union.union_vr1.Servicios.ServiceImport;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.Utils;

//Esti es yb cinebtario

public class VMovil_Evento_Establec extends Activity implements View.OnClickListener {

    private DbAdapter_Temp_Session session;

    private Cursor cursor;
    private DbAdaptert_Evento_Establec dbHelper;
    private DbAdapter_Comprob_Cobro dbHelper44;
    private String valIdEstab;
    private int idAgente;
    private Button mCobros, mCanDev, mVentas, mManten;
    private ImageButton estadoAtencion;
    private VMovil_Evento_Establec mainActivity;
    private int idLiquidacion;

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


    Utils df = new Utils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_evento_establec);

        mainActivity = this;


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


        session.deleteVariable(6);
        session.createTempSession(6,0);



        dbHelper = new DbAdaptert_Evento_Establec(this);
        dbHelper.open();

        mCobros = (Button) findViewById(R.id.VEE_BTNcobros);
        mCanDev = (Button) findViewById(R.id.VEE_BTNcandev);
        mVentas = (Button) findViewById(R.id.VEE_BTNventas);
        mManten = (Button) findViewById(R.id.VEE_BTNmanten);
        estadoAtencion = (ImageButton) findViewById(R.id.imageButtonEstadoAtencion);


        mCobros.setOnClickListener(this);
        mCanDev.setOnClickListener(this);
        mVentas.setOnClickListener(this);
        mManten.setOnClickListener(this);
        estadoAtencion.setOnClickListener(this);
        session.deleteVariable(6);
        session.createTempSession(6,0);

        idLiquidacion = session.fetchVarible(3);



        valIdEstab = ""+session.fetchVarible(2);
        idAgente  = session.fetchVarible(1);


        dbHelper44 = new DbAdapter_Comprob_Cobro(this);
        dbHelper44.open();
        int idEstab = Integer.parseInt(valIdEstab);


        Cursor cursorEstablecimiento = dbHelper.listarEstablecimientosByID(idEstab, idLiquidacion);


        TextView nombreEstablecimiento = (TextView) findViewById(R.id.textViewEstablecimientoNombre);
        TextView nombreCliente = (TextView)findViewById(R.id.textViewEstablecimientoCliente);
        TextView deuda = (TextView) findViewById(R.id.textViewEstablecimientoDeuda);
        LinearLayout linearLayoutColor = (LinearLayout) findViewById(R.id.linearLayoutEstablecimientoColor);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);

/*        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.YELLOW);*/

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(mainActivity, R.color.accent), PorterDuff.Mode.SRC_ATOP);


        cursorEstablecimiento.moveToFirst();

        if (cursorEstablecimiento.getCount()>0){

            String id_establecimiento = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbHelper.EE_id_establec));
            String nombre_establecimiento = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbHelper.EE_nom_establec));
            String nombre_cliente = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbHelper.EE_nom_cliente));
            int id_estado_atencion = Integer.parseInt(cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbHelper.EE_id_estado_atencion)));
            int numeroOrden = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndexOrThrow(dbHelper.EE_orden));
            double deudaTotal = cursorEstablecimiento.getDouble(cursorEstablecimiento.getColumnIndexOrThrow("cc_re_monto_a_pagar")) ;



//            DecimalFormat df= new DecimalFormat("#0.00");



            nombreEstablecimiento.setText(numeroOrden + ". " +nombre_establecimiento);
            nombreEstablecimiento.setSingleLine(false);
            nombreCliente.setText(nombre_cliente);
            deuda.setText("S/. "+df.format(deudaTotal));
            ratingBar.setRating(4);

            switch (id_estado_atencion){
                case 1:
                    linearLayoutColor.setBackgroundColor(getApplication().getResources().getColor(R.color.azul));
                    break;
                case 2:
                    linearLayoutColor.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.verde));
                    break;
                case 3:
                    linearLayoutColor.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.rojo));
                    break;
                case 4:
                    linearLayoutColor.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.amarillo));
                    break;

            }
        }




        Cursor cursor = dbHelper44.listaComprobantes(idEstab);

        //Recorremos el cursor hasta que no haya más registros

        int i;

        if (cursor.moveToFirst()) {
            for (i = 0; i < 1; i++) {
                String fecha = cursor.getString(0);

                try {
                    cursor.moveToNext();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    Date dSqlite = df.parse(Utils.getDatePhoneConvert(fecha));
                    Date dSistema = df.parse(fech());

                    if (dSqlite.equals(dSistema)) {
                        //Toast.makeText(getApplicationContext(), "Hay Deudas por Cobrar ", Toast.LENGTH_SHORT).show();
                        //mCobros.getBackground().setColorFilter(new LightingColorFilter(0xffff0000, 0xffff0000));
                        mCobros.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.border_red));

                    }
                    if (dSqlite.before(dSistema)) {
                        //Toast.makeText(getApplicationContext(), "Hay Deudas por Cobrar", Toast.LENGTH_SHORT).show();
                        //mCobros.getBackground().setColorFilter(new LightingColorFilter(0xffff0000, 0xffff0000));
                        mCobros.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.border_red));

                    }
                    if (dSqlite.after(dSistema)) {
                        //Toast.makeText(getApplicationContext(), "Fechas Proximas", Toast.LENGTH_SHORT).show();
                        //mCobros.getBackground().setColorFilter(new LightingColorFilter(0xffffff00, 0xffffff00));
                        mCobros.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.border_yellow));

                    }


                } catch (ParseException e) {
                    e.printStackTrace();

                }
            }

        }
        if (cursor.getCount() <= 0) {
            //Toast.makeText(getApplicationContext(), "No hay Deudas Por Cobrar", Toast.LENGTH_SHORT).show();
            mCobros.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.border_right_green));

        }


        cursor.close();

        //SLIDING MENU
        showSlideMenu(mainActivity);

    }

    @Override
    protected void onResume() {
        super.onResume();
        changeDataSlideMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sincronizar_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
/*
            case R.id.buttonImportCredito:
                //new ImportMain(mainActivity).execute();
                new ImportCredito(mainActivity).execute();
                break;*/
            case R.id.buttonImport:
                //new ImportMain(mainActivity).execute();
                new ImportCredito(mainActivity).execute();
                Intent intentImportService = new Intent(mainActivity, ServiceImport.class);
                intentImportService.setAction(Constants.ACTION_IMPORT_SERVICE);
                mainActivity.startService(intentImportService);
                break;
            case R.id.buttonExportar:
                new ExportMain(mainActivity).execute();
                break;
            case R.id.buttonRedireccionarPrincipal:
                Intent intent = new Intent(mainActivity, VMovil_Evento_Indice.class);
                finish();
                startActivity(intent);
                break;
            /*case R.id.buttonImportCredito:
                //new ImportCredito(mainActivity).execute();
                break;*/
            default:
                //ON ITEM SELECTED DEFAULT
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private String fech() throws ParseException {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        Date d = df.parse(formatteDate);

        return formatteDate;
    }

    private void eleccion(final String idEstabl) {
        //Intent i = new Intent(this, VMovil_Evento_Establec.class);
        //i.putExtra("idEstab", idEstabl);
        //startActivity(i);

        final String[] items = {"No Hallado", "Cerrado", "SUNAT Clausurado", "Tiene Stock Suficiente"};
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("ESTADO");
        dialogo.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                int idmotivoNoAtencion = ++item;
                dbHelper.updateEstadoNoAtendido(idEstabl, 3, idmotivoNoAtencion, items[item], getDatePhone());
                Intent intent2 = new Intent(getApplicationContext(), VMovil_Menu_Establec.class);
                finish();
                startActivity(intent2);

                Toast.makeText(getApplicationContext(), "No atendio por : " + items[item], Toast.LENGTH_LONG).show();
            }
        });
        dialogo.create();
        dialogo.show();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.VEE_BTNcobros:
                Intent i = new Intent(this, VMovil_Cobro_Credito.class);
                i.putExtra("idEstabX", valIdEstab);
                finish();
                startActivity(i);
                //Toast.makeText(getApplicationContext(),
                //        "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.slide_textViewCargarInventario:
                Intent cInventario = new Intent(this, VMovil_Cargar_Inventario.class);
                startActivity(cInventario);
                break;
            case R.id.VEE_BTNcandev:

                Intent idh = new Intent(this, VMovil_Operacion_Canjes_Devoluciones.class);
                idh.putExtra("idEstabX", valIdEstab);
                idh.putExtra("idAgente", idAgente);
                finish();
                startActivity(idh);
                break;
            case R.id.VEE_BTNventas:
                Intent id = new Intent(this, VMovil_Venta_Cabecera.class);
                finish();
                id.putExtra("idEstabX", valIdEstab);
                startActivity(id);
                //Toast.makeText(getApplicationContext(),
                //        "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.VEE_BTNmanten:
                Intent idd = new Intent(this, VMovil_Venta_Comprob.class);
                idd.putExtra("idEstabX", valIdEstab);
                startActivity(idd);
                break;

            case R.id.imageButtonEstadoAtencion:
                new AlertDialog.Builder(this)
                        .setTitle("Estado Atención")
                        .setItems(R.array.estadoAtencion, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        dbHelper.updateEstadoEstablecs(valIdEstab, 2, getDatePhone());
                                        Intent intent = new Intent(mainActivity, VMovil_Menu_Establec.class);
                                        finish();
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        eleccion(valIdEstab);
                                        break;
                                    case 2:
                                        dbHelper.updateEstadoEstablecs(valIdEstab, 4, getDatePhone());
                                        Intent intent2 = new Intent(mainActivity, VMovil_Menu_Establec.class);
                                        finish();
                                        startActivity(intent2);
                                        break;
                                    default:
                                        break;

                                }
                            }
                        }).create().show();
                break;
            //SLIDING MENU
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
                Intent ivc1 = new Intent(this, VMovil_Venta_Comprob.class);
                ivc1.putExtra("idEstabX", valIdEstab);
                startActivity(ivc1);
                break;
            case R.id.slideVentas_buttonDeudas:
                Intent id1 = new Intent(this, VMovil_Cobro_Credito.class);
                id1.putExtra("idEstabX", valIdEstab);
                finish();
                startActivity(id1);
                break;
            case R.id.slideVentas_textViewVenta:
                Intent iv1 = new Intent(this, VMovil_Venta_Cabecera.class);
                finish();
                iv1.putExtra("idEstabX", valIdEstab);
                startActivity(iv1);
                break;
            case R.id.slideVentas_textViewMantenimiento:
                Intent im1 = new Intent(this, VMovil_Venta_Comprob.class);
                im1.putExtra("idEstabX", valIdEstab);
                startActivity(im1);
                break;
            case R.id.slideVentas_textviewCanjesDevoluciones:
                Intent idh1 = new Intent(this, VMovil_Evento_Canjes_Dev.class);
                idh1.putExtra("idEstabX", valIdEstab);
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
        textViewSlideCargar = (TextView)findViewById(R.id.slide_textViewCargarInventario);
        textViewSlideCargar.setOnClickListener(this);

        textViewSlideNombreEstablecimiento = (TextView)findViewById(R.id.slideVentas_textViewCliente);
        buttonSlideVentaDeHoy  = (Button)findViewById(R.id.slideVentas_buttonVentaCosto);
        buttonSlideDeudaHoy = (Button)findViewById(R.id.slideVentas_buttonDeudas);
        textViewSlideVenta = (TextView)findViewById(R.id.slideVentas_textViewVenta);
         //COBRAR
        textViewSlideMantenimiento = (TextView)findViewById(R.id.slideVentas_textViewMantenimiento);
        textViewSlideCanjesDevoluciones  = (TextView)findViewById(R.id.slideVentas_textviewCanjesDevoluciones);





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
//        DecimalFormat df = new DecimalFormat("#.00");
        textViewSlideNombreAgente.setText(""+slideNombreAgente);
        textViewSlideNombreRuta.setText(""+slideNombreRuta);
        buttonSlideNroEstablecimiento.setText(""+slideNumeroEstablecimientoxRuta);
        textviewSlideARendir.setText("Efectivo a Rendir S/. " + df.format(slide_aRendir));


        //DATA VENTAS
        Cursor cursorEstablecimiento = dbHelper.listarEstablecimientosByID(slideIdEstablecimiento, slideIdLiquidacion);

        cursorEstablecimiento.moveToFirst();

        if (cursorEstablecimiento.getCount()>0){

            String nombre_establecimiento = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbHelper.EE_nom_establec));
            String nombre_cliente = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbHelper.EE_nom_cliente));
            int id_estado_atencion = Integer.parseInt(cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex(dbHelper.EE_id_estado_atencion)));
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

    public String getDatePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(date);
    }
}