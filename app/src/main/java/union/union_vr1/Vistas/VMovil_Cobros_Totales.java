package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.AsyncTask.ExportMain;
import union.union_vr1.AsyncTask.ImportMain;
import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapterCobrosTotales;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.Utils;


public class VMovil_Cobros_Totales extends Activity implements View.OnClickListener{


    private DbAdapter_Temp_Session session;
    DbAdapter_Comprob_Cobro cCobro;
    private SimpleCursorAdapter dataAdapter;
    private VMovil_Cobros_Totales mainActivity;
    ListView listCobros;
    View headerSinDatos;

    //SLIDING MENU
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_vmovil__cobros__totales);


        session = new DbAdapter_Temp_Session(this);
        session.open();

        cCobro = new DbAdapter_Comprob_Cobro(this);
        cCobro.open();
        listCobros = (ListView) findViewById(R.id.listaCobrosTotales);

        listarCobrosTotales();

        //SLIDING MENU
        dbGastosIngresos = new DbGastos_Ingresos(this);
        dbGastosIngresos.open();

        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(this);
        dbAdapter_informe_gastos.open();

        dbHelperAgente = new DbAdapter_Agente(this);
        dbHelperAgente.open();

        //SLIDING MENU
        showSlideMenu(mainActivity);
    }

    public void listarCobrosTotales() {

        Cursor cursor = cCobro.listarComprobantesToCobros();


        CursorAdapterCobrosTotales cACobros = new CursorAdapterCobrosTotales(this, cursor);


        if (cursor.getCount()==0){
            headerSinDatos= getLayoutInflater().inflate(R.layout.header_datos_vacios,null);
            listCobros.addFooterView(headerSinDatos,null, false);
        }else if(cursor.getCount()<0){
            listCobros.removeAllViews();
            headerSinDatos= getLayoutInflater().inflate(R.layout.header_datos_vacios,null);
            listCobros.addFooterView(headerSinDatos,null, false);
        }
        listCobros.setAdapter(cACobros);
        listCobros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    Cursor cr2 = (Cursor) listCobros.getItemAtPosition(i);
                    String establec = cr2.getString(cr2.getColumnIndexOrThrow("ee_te_nom_establec"));
                    String cliente = cr2.getString(cr2.getColumnIndexOrThrow("ee_te_nom_cliente"));
                    String idCCobro = cr2.getString(0);
                    Double monto = cr2.getDouble(cr2.getColumnIndexOrThrow("cc_re_monto_a_pagar"));
                    //System.out.println("here"+establec+"-"+idCCobro+"-"+monto+"-"+cliente);
                    //view.setBackgroundColor(0xffcccccc);
                    Dialog(establec, monto, idCCobro, cliente);


            }
        });


    }

    public void Dialog(String establec, final Double deuda, final String idCCobro, String cliente) {

        cCobro = new DbAdapter_Comprob_Cobro(this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Cobro de Credito");

        DecimalFormat df = new DecimalFormat("#.00");

        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Pago de Deuda para el Establecimiento: " + establec + ", con Dueño: " + cliente + " :. Deuda: " + df.format(deuda) + " ")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        cCobro.open();
                        int estado = cCobro.updateComprobCobrosCan2(idCCobro, getDatePhone(), getTimePhone(), deuda, "0");

                        if (estado == 1) {
                            listarCobrosTotales();

                            Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_SHORT).show();

                            //Back();

                        } else {
                            Toast.makeText(getApplicationContext(), "Error Interno", Toast.LENGTH_SHORT).show();
                        }


                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),
                                "Cancelo ", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    public void Back() {
        Intent i = new Intent(this, VMovil_Evento_Indice.class);
        startActivity(i);
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

            case R.id.buttonImport:
                new ImportMain(mainActivity).execute();
                break;
            case R.id.buttonExportar:
                new ExportMain(mainActivity).execute();
                break;
            case R.id.buttonRedireccionarPrincipal:
                Intent intent = new Intent(mainActivity, VMovil_Evento_Indice.class);
                finish();
                startActivity(intent);
                break;
            default:
                //ON ITEM SELECTED DEFAULT
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    private String getTimePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }


    public void showSlideMenu(Activity activity){
        layoutSlideMenu = View.inflate(activity, R.layout.slide_menu, null);
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





        textViewSlidePrincipal.setOnClickListener(this);
        textViewSlideCliente.setOnClickListener(this);
        textviewSlideCobranzas.setOnClickListener(this);
        textviewSlideGastos.setOnClickListener(this);
        textviewSlideResumen.setOnClickListener(this);
        textviewSlideARendir.setOnClickListener(this);


        slideIdAgente = session.fetchVarible(1);
        slideIdLiquidacion  = session.fetchVarible(3);

        changeDataSlideMenu();


    }

    @Override
    protected void onResume() {
        super.onResume();
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

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

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
                menu.toggle();
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
            default:
                break;
        }
    }
}
