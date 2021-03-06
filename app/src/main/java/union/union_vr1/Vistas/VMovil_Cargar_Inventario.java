package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.DecimalFormat;

import union.union_vr1.AsyncTask.CargarInventario;
import union.union_vr1.AsyncTask.GetStockAgente;
import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapter_Cargar_Inventario;
import union.union_vr1.Sqlite.CursorAdapter_Trans_Detallado;
import union.union_vr1.Sqlite.DBAdapter_Temp_Inventario;
import union.union_vr1.Sqlite.DBAdapter_Trans_Detallado;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Ruta_Distribucion;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.Utils;

public class VMovil_Cargar_Inventario extends Activity implements View.OnClickListener {
    private CargarInventario cargarInventario;
    public static String TAG = VMovil_Trans_Prod_Detallado.class.getSimpleName();
    //SLIDING MENU
    private DBAdapter_Temp_Inventario dbAdapter_temp_inventario;
    private DbGastos_Ingresos dbGastosIngresos;
    private DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
    private DbAdapter_Agente dbHelperAgente;
    private DbAdapter_Temp_Session session;
    private Activity mainActivity;
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
    TextView textviewSlideCInventario;
    TextView textviewSlideConsultarInventario;
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
    String nombreAgente = "";
    int nroLiquidacion = 0;

    //widgets
    private TextView agente;
    private TextView liquidacion;
    private EditText inputNroGuia;
    private Button btnAgregarGuia;
    private ListView listGuias;
    private int nroAgregados=0;
/*
    //cargando transferencia detallado
    private DBAdapter_Trans_Detallado dbAdapter_trans_detallado;
    private CursorAdapter_Trans_Detallado cursorAdapter_trans_detallado;
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__cargar__inventario);

        mainActivity = this;
        //

        dbAdapter_temp_inventario = new DBAdapter_Temp_Inventario(this);
        dbAdapter_temp_inventario.open();
        cargarInventario = new CargarInventario(mainActivity);
        agente = (TextView) findViewById(R.id.textNombreAgente);
        liquidacion = (TextView) findViewById(R.id.textNumeroLiquidacion);
        inputNroGuia = (EditText) findViewById(R.id.editNroGuia);
        btnAgregarGuia = (Button) findViewById(R.id.btnAgregarGuia);
        listGuias = (ListView) findViewById(R.id.listviewGuias);
        session = new DbAdapter_Temp_Session(this);
        session.open();
        dbHelperAgente = new DbAdapter_Agente(this);
        dbHelperAgente.open();
        dbGastosIngresos = new DbGastos_Ingresos(this);
        dbGastosIngresos.open();
        //transferencias
      /*  dbAdapter_trans_detallado = new DBAdapter_Trans_Detallado(this);
        dbAdapter_trans_detallado.open();*/


        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(this);
        dbAdapter_informe_gastos.open();
        displayWidgets();
        //SLIDING MENU
        showSlideMenu(mainActivity);
    }

    private void displayWidgets() {
        //
        Cursor cursor = dbAdapter_temp_inventario.getAllIvnetario();
        CursorAdapter_Cargar_Inventario cursorAdapter_cargar_inventario = new CursorAdapter_Cargar_Inventario(getApplicationContext(), cursor);
        listGuias.setAdapter(cursorAdapter_cargar_inventario);

        listGuias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cr = (Cursor) parent.getItemAtPosition(position);
                String codigo = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Temp_Inventario.temp_in_id_guia));
                Intent asd = new Intent(getApplicationContext(), VMovil_Trans_Prod_Detallado.class);
                asd.putExtra("codigotrans", codigo);
                startActivity(asd);


            }
        });

        ///
        nombreAgente = dbHelperAgente.getNameAgente();
        nroLiquidacion = session.fetchVarible(3);
        slideIdAgente = session.fetchVarible(1);
        agente.setText(nombreAgente);
        liquidacion.setText(nroLiquidacion + "");
        btnAgregarGuia.setOnClickListener(this);
    }

    private void iniciaCargar() {

        final String nroGuia = inputNroGuia.getText().toString();

        if (inputNroGuia.getText() == null || inputNroGuia.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Por favor ingresa la guia", Toast.LENGTH_SHORT).show();
        } else {

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Importante");
            dialogo1.setMessage("¿ Esta seguro de agregar la guia ?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    cargarInventario = new CargarInventario(mainActivity);
                    Log.e("DATOS CARGAR INVENTARIO",""+slideIdAgente+"+++"+nroGuia);
                    cargarInventario.execute("" + slideIdAgente, "" + nroGuia);
                    nroAgregados = nroAgregados+1;
                }
            });

            dialogo1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();

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
        textviewSlideCInventario = (TextView) findViewById(R.id.slide_textViewCargarInventario);
        textviewSlideARendir = (TextView) findViewById(R.id.slide_textViewARendir);

        textViewIngresosTotales = (TextView) findViewById(R.id.textView_IngresosTotales);
        textViewGastos = (TextView) findViewById(R.id.textView_Gastos);

        textviewSlideConsultarInventario.setOnClickListener(this);
        textViewSlidePrincipal.setOnClickListener(this);
        textViewSlideCliente.setOnClickListener(this);
        textviewSlideCobranzas.setOnClickListener(this);
        textviewSlideGastos.setOnClickListener(this);
        textviewSlideResumen.setOnClickListener(this);
        textviewSlideCInventario.setOnClickListener(this);
        textviewSlideARendir.setOnClickListener(this);


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
        DecimalFormat df = new DecimalFormat("0.00");
        textViewSlideNombreAgente.setText("" + slideNombreAgente);
        textViewSlideNombreRuta.setText("" + slideNombreRuta);
        buttonSlideNroEstablecimiento.setText("" + slideNumeroEstablecimientoxRuta);
        textviewSlideARendir.setText("Efectivo a Rendir S/. " + df.format(slide_aRendir));

        textViewIngresosTotales.setText("" + df.format(slide_ingresosTotales));
        textViewGastos.setText("" + df.format(slide_gastosTotales));


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.VEI_BTNclient:
                ifThisUpdate();
                Intent i = new Intent(this, VMovil_Menu_Establec.class);
                startActivity(i);
                break;
            case R.id.VEI_BTNinfgas:
                ifThisUpdate();
                Intent ig = new Intent(this, VMovil_Evento_Gasto.class);
                startActivity(ig);
                break;
            case R.id.VEI_BTNresume:
                ifThisUpdate();
                Intent ir = new Intent(this, VMovil_Resumen_Caja.class);
                startActivity(ir);
                break;

            case R.id.VEI_BTNcobrarTodo:
                ifThisUpdate();
                Intent cT = new Intent(this, VMovil_Cobros_Totales.class);
                startActivity(cT);
                break;
            case R.id.buttonNumeroEstablecimientos:
                ifThisUpdate();
                Intent ine = new Intent(this, VMovil_Menu_Establec.class);
                startActivity(ine);
                break;
            //SLIDING MENU
            case R.id.slide_textviewPrincipal:
                ifThisUpdate();
                Intent iP = new Intent(this, VMovil_Evento_Indice.class);
                startActivity(iP);
                break;
            case R.id.slide_textViewClientes:
                ifThisUpdate();
                Intent ic1 = new Intent(this, VMovil_Menu_Establec.class);
                startActivity(ic1);
                break;
            case R.id.slide_textViewCobranza:
                ifThisUpdate();
                Intent cT1 = new Intent(this, VMovil_Cobros_Totales.class);
                startActivity(cT1);
                break;
            case R.id.slide_TextViewGastos:
                ifThisUpdate();
                Intent ig1 = new Intent(this, VMovil_Evento_Gasto.class);
                startActivity(ig1);
                break;
            case R.id.slide_textViewResumen:
                ifThisUpdate();
                Intent ir1 = new Intent(this, VMovil_Resumen_Caja.class);
                startActivity(ir1);
                break;
            case R.id.slide_textViewCargarInventario:
                menu.toggle();
                break;
            case R.id.slide_textViewARendir:

                break;
            case R.id.btnAgregarGuia:
                if(conectadoRedMovil() || conectadoWifi()){
                    iniciaCargar();
                }
                else{
                    Utils.setToast(getApplicationContext(),"No cuenta con conexion a intenert",R.color.rojo);
                }
                break;
            case R.id.slide_textViewConsultarInventario:
                startActivity(new Intent(getApplicationContext(), VMovil_Consultar_Inventario.class));
                break;
        }
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

    @Override
    protected void onDestroy() {
        cargarInventario.dismissProgressDialog();
        Log.d("ON DESTROY", "DISMISS PROGRESS DIALOG");
        super.onDestroy();

    }
    private void ifThisUpdate(){
        if(nroAgregados>0){
            new GetStockAgente(this).execute();
        }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ifThisUpdate();
            onBackPressed();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), VMovil_Evento_Indice.class));
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ifThisUpdate();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ifThisUpdate();
    }
/*
    public void consultardia(){

        Cursor cr =dbAdapter_trans_detallado.getAllTrans();
        cursorAdapter_trans_detallado = new CursorAdapter_Trans_Detallado(this, cr);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(cursorAdapter_trans_detallado);
    }*/
}
