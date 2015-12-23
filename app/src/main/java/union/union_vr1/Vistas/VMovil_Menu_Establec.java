package union.union_vr1.Vistas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.DecimalFormat;

import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapterEstablecimientoColor;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Barcode_Scanner;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.MyApplication;
import union.union_vr1.Utils.Utils;
import union.union_vr1.activity_agregar_establecimiento;

public class VMovil_Menu_Establec extends Activity implements View.OnClickListener{


    private DbAdapter_Temp_Session session;
    private DbAdaptert_Evento_Establec dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private CursorAdapterEstablecimientoColor cursorAdapterEstablecimientoColor;
    private DbAdapter_Temp_Barcode_Scanner dbAdapter_temp_barcode_scanner;
    private DbAdapter_Agente dbAdapter_agente;
    private TextView textViewNombreRuta;
    private int idLiquidacion;
    private Activity mainActivity;
    private Button buttonAgregarEstablecimiento;
    private TextView textViewSlideCargar;


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

    Double slide_totalRuta =0.0;
    Double slide_totalPlanta = 0.0;
    Double slide_ingresosTotales = 0.0;
    Double slide_gastosTotales = 0.0;
    Double slide_aRendir = 0.0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_menu_establec);


        mainActivity = this;

        session = new DbAdapter_Temp_Session(this);
        session.open();

        idLiquidacion = session.fetchVarible(3);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();
        dbHelper = new DbAdaptert_Evento_Establec(this);
        dbHelper.open();
        dbAdapter_temp_barcode_scanner = new DbAdapter_Temp_Barcode_Scanner(this);
        dbAdapter_temp_barcode_scanner.open();

        //SLIDING MENU
        dbGastosIngresos = new DbGastos_Ingresos(this);
        dbGastosIngresos.open();

        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(this);
        dbAdapter_informe_gastos.open();

        dbHelperAgente = new DbAdapter_Agente(this);
        dbHelperAgente.open();


        textViewNombreRuta = (TextView) findViewById(R.id.textViewNombreRuta);

        int idAgente = session.fetchVarible(1);
        Cursor cursorAgente = dbAdapter_agente.fetchAgentesByIds(idAgente, idLiquidacion);
        cursorAgente.moveToFirst();

        String nombreRuta = "";
        int numeroEstablecimientoxRuta = 0;
        if (cursorAgente.getCount()>0){
            nombreRuta = cursorAgente.getString(cursorAgente.getColumnIndexOrThrow(dbAdapter_agente.AG_nombre_ruta));
            numeroEstablecimientoxRuta = cursorAgente.getInt(cursorAgente.getColumnIndexOrThrow(dbAdapter_agente.AG_nro_bodegas));

        }
        textViewNombreRuta.setText("Ruta : "+nombreRuta);
        buttonAgregarEstablecimiento = (Button) findViewById(R.id.buttonAddEstablecimientos);
        buttonAgregarEstablecimiento.setOnClickListener(this);

        //Generate ListView from SQLite Database
        displayListView();

        //SLIDING MENU
        showSlideMenu(mainActivity);

    }

    private void eleccion(String idEstabl, int idAgente) {
        Intent i = new Intent(this, VMovil_Evento_Establec.class);

        //dbAdapter_temp_barcode_scanner.deleteAll();
        //((MyApplication) this.getApplication()).setIdEstablecimiento(Integer.parseInt(idEstabl));
        session.deleteVariable(2);
        session.createTempSession(2,Integer.parseInt(idEstabl));
        //dbAdapter_temp_barcode_scanner.createTempScanner(Integer.parseInt(idEstabl));
        //session.deleteVariable(2);
        //session.createTempSession(2, Integer.parseInt(idEstabl));

        startActivity(i);
    }

    private void displayListView() {

        Cursor cursor = dbHelper.listarEstablecimientos(idLiquidacion);

        /*
        // The desired columns to be bound
        String[] columns = new String[] {
                DbAdaptert_Evento_Establec.EE_id_establec,
                DbAdaptert_Evento_Establec.EE_nom_establec,
                DbAdaptert_Evento_Establec.EE_nom_cliente,
                DbAdaptert_Evento_Establec.EE_doc_cliente
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.VME_codigo,
                R.id.VME_establec,
                R.id.VME_nombre,
                R.id.VME_docum,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_menu_establec,
                cursor,
                columns,
                to,
                0);

*/
        cursorAdapterEstablecimientoColor = new CursorAdapterEstablecimientoColor(this, cursor);


        ListView listView = (ListView) findViewById(R.id.VME_listar);
        listView.setAdapter(cursorAdapterEstablecimientoColor);


        // Assign adapter to ListView

        //bindView();
        //View v = listView.getSelectedView();
        //v.setBackgroundColor(Color.BLUE);
        //listView.findViewById(0).setBackgroundColor(Color.BLUE);
        //v0.setBackgroundColor(Color.BLUE);
        //View v1 = listView.findViewById(0);
        //v1.setBackgroundColor(Color.GREEN);
        //View v2 = listView.findViewById(0);
        //v2.setBackgroundColor(Color.RED);
        //View v3 = listView.findViewById(0);
        //v3.setBackgroundColor(Color.YELLOW);
        //View v4 = listView.getSelectedView();
        //v4.setBackgroundColor(Color.BLUE);

        for (int i = 0; i < listView.getCount(); i++) {
            final View row = listView.getAdapter().getView(i, null, null);
            //Solo deseo colocar background color a las 3 primeras filas, pero
            //al ejecutar no pinta ninguna fila de azul
            row.setBackgroundColor(Color.BLUE);
        }

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String idEstablec =
                        cursor.getString(cursor.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_id_establec));

                int id_agente = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_id_agente));

                //if(idEstEst == 1){
                //    view.setBackgroundColor(Color.BLUE);
                //}
                //if(idEstEst == 2){
                //    view.setBackgroundColor(Color.GREEN);
                //}
                //if(idEstEst == 3){
                //    view.setBackgroundColor(Color.RED);
                //}
                //if(idEstEst == 4){
                //    view.setBackgroundColor(Color.YELLOW);
                //}


                /*Toast.makeText(getApplicationContext(),
                        idEstablec + "Aqui po" + id_agente, Toast.LENGTH_SHORT).show();*/
                eleccion(idEstablec, id_agente);
                //listView.setBackgroundColor(Color.GREEN);

            }
        });

        EditText myFilter = (EditText) findViewById(R.id.VME_buscar);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                cursorAdapterEstablecimientoColor.getFilter().filter(s.toString());
            }
        });

        cursorAdapterEstablecimientoColor.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.listarEstablecimientosPorNombre(constraint.toString(), idLiquidacion);
            }
        });

    }

    public void bindView(View view, Context context, Cursor cur) {

        String color_picto = (cur.getString(cur.getColumnIndex("ee_in_estado_no_atencion")));

        if (color_picto.equals("0")) {

            view.setBackgroundColor(Color.parseColor("#f48905"));
        } else if (color_picto.equals("1")) {
            view.setBackgroundColor(Color.parseColor("#688f2b"));
        } else if (color_picto.equals("2")) {
            view.setBackgroundColor(Color.parseColor("#F781F3"));
        } else if (color_picto.equals("3")) {
            view.setBackgroundColor(Color.parseColor("#003366"));
        } else if (color_picto.equals("4")) {
            view.setBackgroundColor(Color.parseColor("#F7FE2E"));
        } else if (color_picto.equals("5")) {
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    //SLIDING MENU
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

        textViewIngresosTotales = (TextView) findViewById(R.id.textView_IngresosTotales);
        textViewGastos = (TextView) findViewById(R.id.textView_Gastos);
        textViewSlideCargar = (TextView)findViewById(R.id.slide_textViewCargarInventario);
        textViewSlideCargar.setOnClickListener(this);


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
        //SLIDING MENU
        changeDataSlideMenu();
        displayListView();
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

        textViewIngresosTotales.setText(""+df.format(slide_ingresosTotales));
        textViewGastos.setText(""+df.format(slide_gastosTotales));

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.buttonAddEstablecimientos:
                Intent iae = new Intent(this, VMovil_Crear_Establecimiento.class);
                startActivity(iae);
                break;
            case R.id.slide_textViewCargarInventario:
                Intent cInventario = new Intent(this, VMovil_Cargar_Inventario.class);
                startActivity(cInventario);
                break;

            //SLIDING MENU
            case R.id.slide_textviewPrincipal:
                Intent ip1 = new Intent(this, VMovil_Evento_Indice.class);
                finish();
                startActivity(ip1);
                break;
            case R.id.slide_textViewClientes:
                menu.toggle();
                break;
            case R.id.slide_textViewCobranza:
                Intent cT1 = new Intent(this, VMovil_Cobros_Totales.class);
                //finish();
                startActivity(cT1);
                break;
            case R.id.slide_TextViewGastos:
                Intent ig1 = new Intent(this, VMovil_Evento_Gasto.class);
                //finish();
                startActivity(ig1);
                break;
            case R.id.slide_textViewResumen:
                Intent ir1 = new Intent(this, VMovil_Resumen_Caja.class);
                //finish();
                startActivity(ir1);
                break;
            case R.id.slide_textViewARendir:

                break;
            default:
                break;
        }
    }

}