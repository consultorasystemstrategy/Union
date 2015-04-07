package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.DecimalFormat;

import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapterFacturas;
import union.union_vr1.Sqlite.CursorAdapterFacturas_dev;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.Utils;

public class VMovil_Evento_Canjes_Dev extends TabActivity implements View.OnClickListener{
    private String establec;
    private AutoCompleteTextView autoComple;
    private SimpleCursorAdapter adapter;
    private DbAdapter_Stock_Agente dbHelper_Stock;
    private int idAgente;
    private int idProducto;
    private DbAdapter_Canjes_Devoluciones dbHelper_CanDev;
    private Button btn_mostrar_guias;
    private Context ctx;
    private TabHost tabHost;
    private int liquidacion;
    private DbAdapter_Temp_Session session;

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

    int slideIdEstablecimiento;

    private DbAdaptert_Evento_Establec dbAdaptert_evento_establec;
    private DbAdapter_Comprob_Venta dbAdapter_comprob_venta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__evento__canjes__dev);
        ctx = this;
        //-------------------------------------------------------
        dbHelper_Stock = new DbAdapter_Stock_Agente(getApplication());
        dbHelper_Stock.open();
        dbHelper_CanDev = new DbAdapter_Canjes_Devoluciones(getApplication());
        dbHelper_CanDev.open();
        session = new DbAdapter_Temp_Session(this);
        session.open();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        liquidacion = session.fetchVarible(3);

        Bundle idE = getIntent().getExtras();
        establec = idE.getString("idEstabX");
        idAgente = idE.getInt("idAgente");
        //Toast.makeText(getApplicationContext(), "" + establec +"-"+ idAgente, Toast.LENGTH_SHORT).show();
        autoComple = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autoComplete();
        //

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab1");
        spec.setContent(R.id.tab1_can);
        spec.setIndicator("Canjes");
        listar_Facturas_can();
        tabHost.addTab(spec);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab2");
        spec2.setContent(R.id.tab2_dev);
        spec2.setIndicator("Devoluciones");
        listar_Facturas_dev();
        tabHost.addTab(spec2);


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


        //SLIDING MENU
        showSlideMenu(this);

        //


    }

    private void autoComplete() {

        Cursor cr = dbHelper_Stock.listarProductosAndStock(liquidacion);
        cr.moveToFirst();



        String[] columnasStock = new String[]{
                cr.getColumnName(1),
                cr.getColumnName(2),


        };

        // the XML defined views which the data will be bound to
        int[] toStock = new int[]{
                R.id.VCAP_producto,
                R.id.VCPA_stock,

        };

        adapter = new SimpleCursorAdapter(this,
                R.layout.infor_venta_cabecera_productos,
                cr,
                columnasStock,
                toStock,
                0);





        autoComple.setAdapter(adapter);
        autoComple.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                adapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                Log.e("Error stock lista"," "+liquidacion);
                Cursor cr = dbHelper_Stock.listarbyIdProductoAndStock(charSequence.toString(), liquidacion);

                return cr;
            }
        });


        autoComple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //adapterView.setBackgroundColor(0x00000000);


                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                String nom = cursor.getString(1);
                idProducto = cursor.getInt(0);
                String valorUnidad = cursor.getString(3);
                autoComple.setText(nom);
                Intent faCanjeDev = new Intent(getApplicationContext(), VMovil_Facturas_Canjes_Dev.class);
                faCanjeDev.putExtra("idAgente", idAgente);
                faCanjeDev.putExtra("idEstablec", establec);
                faCanjeDev.putExtra("idProducto", idProducto);
                faCanjeDev.putExtra("nomProducto", nom);
                faCanjeDev.putExtra("valorUnidad", valorUnidad);
                Log.e("idProducto",""+idProducto+"-"+nom);
                startActivity(faCanjeDev);
                finish();
            }
        });


    }

    private void listar_Facturas_can() {
        Cursor cr = dbHelper_CanDev.obtener_facturas_can(establec);
       //Toast.makeText(getApplicationContext(),"-"+cr.getCount(),Toast.LENGTH_SHORT).show();
        final ListView lista_facturas = (ListView) findViewById(R.id.guias_can_dev);

            CursorAdapterFacturas adapter = new CursorAdapterFacturas(getApplicationContext(), cr);
            lista_facturas.setAdapter(adapter);
            btn_mostrar_guias = (Button) findViewById(R.id.button_mostrar_guias);
            btn_mostrar_guias.setVisibility(View.VISIBLE);
            btn_mostrar_guias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent mostrar = new Intent(getApplicationContext(),mostrar_can_dev_facturas.class);
                    mostrar.putExtra("idEstablec",establec);
                    mostrar.putExtra("idAgente",idAgente);
                    startActivity(mostrar);


                }
            });
        lista_facturas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cr = (Cursor)adapterView.getItemAtPosition(i);
//                cr.moveToPosition(i);
                String idHistoVenta = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_hventadet));
                String idProducto = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_producto));
                String idHistoDetalle = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_detalle));
                int cantProductos = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_cantidad_ope));
                String datos = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_nom_producto));
                Log.e("datos",""+datos);
               // Toast.makeText(getApplicationContext(),"ID"+idHistoVenta,Toast.LENGTH_LONG).show();
                select("1","Canje",idHistoVenta,idProducto,idHistoDetalle,cantProductos);

                return false;
            }
        });



    }
    private void select (final String idOperacion,String Operacion, final String idHistoVenta, final String idProducto, final String idHistoDetalle, final int cantProductos ){
        final int liquidacion = session.fetchVarible(3);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Anular "+Operacion+" ");
            AlertDialog.Builder builder = alertDialogBuilder
                    .setMessage("¿Desea Anular?")
                    .setCancelable(false)
                    .setPositiveButton("Quitar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            boolean estado =false;
                            if (idOperacion.equals("1")) {//cannjeggggg
                                Log.e("Hola K ASE",""+idProducto+"-"+cantProductos+"-"+idHistoDetalle+"-"+idHistoVenta+"-"+liquidacion);
                                estado =  dbHelper_CanDev.cancelarCabiosByIdCanjes(idProducto,cantProductos,idHistoVenta,idHistoDetalle,liquidacion);
                                if(estado){
                                    Toast.makeText(getApplicationContext(),"Quitado de la Lista",Toast.LENGTH_SHORT).show();
                                   regresar();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Ocurrio un Error",Toast.LENGTH_SHORT).show();
                                }
                            }
                            if(idOperacion.equals("2")){//devoluciones
                                Log.e("Hola K ASE",""+idProducto+"-"+cantProductos+"-"+idHistoVenta+"-"+idHistoDetalle+"-"+liquidacion);
                                estado =  dbHelper_CanDev.cancelarCabiosByIdDevoluciones(idProducto,cantProductos,idHistoVenta,idHistoDetalle,liquidacion);
                                if(estado){
                                    Toast.makeText(getApplicationContext(),"Quitado de la Lista",Toast.LENGTH_SHORT).show();
                                    regresar();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Ocurrio un Error",Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

    private void listar_Facturas_dev() {
        Cursor cr = dbHelper_CanDev.obtener_facturas_dev(establec);
        final ListView lista_facturas = (ListView) findViewById(R.id.guias_can_2);

            CursorAdapterFacturas_dev adapter = new CursorAdapterFacturas_dev(getApplicationContext(), cr);
            lista_facturas.setAdapter(adapter);
            btn_mostrar_guias = (Button) findViewById(R.id.button_mostrar_guias);
            btn_mostrar_guias.setVisibility(View.VISIBLE);
            btn_mostrar_guias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mostrar = new Intent(getApplicationContext(),mostrar_can_dev_facturas.class);
                    mostrar.putExtra("idEstablec",establec);
                    mostrar.putExtra("idAgente",idAgente);
                    startActivity(mostrar);
                }
            });
        lista_facturas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cr = (Cursor)adapterView.getItemAtPosition(i);
                cr.moveToPosition(i);
                String idHistoVenta = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_hventadet));
                String idProducto = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_producto));
                String idHistoDetalle = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_detalle));
                int cantProductos = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_cantidad_ope));
                select("2", "Devolucion",idHistoVenta,idProducto,idHistoDetalle,cantProductos);
                return false;
            }
        });

    }
    public void back(){
        //Intent i = new Intent(getApplicationContext(), VMovil_Menu_Establec.class);
       // i.putExtra("idEstablec", establec);
      // i.putExtra("idAgente", idAgente);
      //  this.startActivity(i);
       // finish();
    }





    private void regresar(){
        listar_Facturas_dev();
        listar_Facturas_can();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent mostrar = new Intent(getApplicationContext(),mostrar_can_dev_facturas.class);
                mostrar.putExtra("idEstablec",establec);
                mostrar.putExtra("idAgente",idAgente);
                startActivity(mostrar);
                finish();
                return true;

        }
        return super.onKeyDown(keyCode, event);
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
    public void onClick(View view) {
        switch (view.getId()){

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
                ivc1.putExtra("idEstabX", ""+slideIdEstablecimiento);
                startActivity(ivc1);
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
                Intent im1 = new Intent(this, VMovil_Venta_Comprob.class);
                im1.putExtra("idEstabX", ""+slideIdEstablecimiento);
                startActivity(im1);
                break;
            case R.id.slideVentas_textviewCanjesDevoluciones:
                menu.toggle();
                break;
            case R.id.slideVentas_textViewCliente:
                menu.toggle();
            default:
                break;
        }
    }
}


