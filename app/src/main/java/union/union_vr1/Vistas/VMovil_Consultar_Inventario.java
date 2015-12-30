package union.union_vr1.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableRow.LayoutParams;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import union.union_vr1.AsyncTask.ConsultarInventarioAnterior;
import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapter_Consultar_Inventario;
import union.union_vr1.Sqlite.DBAdapter_Consultar_Inventario_Anterior;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.Utils;

public class VMovil_Consultar_Inventario extends Activity implements View.OnClickListener {
    private DbAdapter_Temp_Session session;
    private String strCurrDate;
    private TextView textGetDate;
    private Activity mainActivity;
    private TextView inventarioTextNombre;
    private DbAdapter_Agente dbAdapter_agente;
    private Button btnAgregarInventario;
    private DateFormat formate = DateFormat.getDateInstance();
    private Calendar calendar = Calendar.getInstance();
    private ConsultarInventarioAnterior consultarInventarioAnterior;
    private Activity context;
    private DBAdapter_Consultar_Inventario_Anterior dbAdapter_consultar_inventario_anterior;
    private CursorAdapter_Consultar_Inventario cursorAdapter_consultar_inventario;
    //private ListView listViewInventario;
    private TableLayout table_layout;

    //------------------------------------Menu--------------//
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
    //------------------------------------Fin Menu----------//
    Utils df = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__consultar__inventario);
        context = this;
        session = new DbAdapter_Temp_Session(this);
        session.open();
        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();
        mainActivity = this;
        String nombreAgente = dbAdapter_agente.getNameAgente();
        inventarioTextNombre = (TextView) findViewById(R.id.InventarioTextNombre);
        inventarioTextNombre.setText(nombreAgente);
        dbAdapter_consultar_inventario_anterior = new DBAdapter_Consultar_Inventario_Anterior(this);
        dbAdapter_consultar_inventario_anterior.open();
        textGetDate = (TextView) findViewById(R.id.textGetDate);
        consultarInventarioAnterior = new ConsultarInventarioAnterior(context);
        btnAgregarInventario = (Button) findViewById(R.id.btnConsultarIventario);
        //listViewInventario = (ListView) findViewById(R.id.listviewInventario);

        displayWidgets();
        textGetDate.setText("Click Here");
    }


    private void displayWidgets() {
        textGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
                // showCalendar();
                //startActivity(new Intent(getApplicationContext(),CalendarViewInventario.class));
            }
        });
        btnAgregarInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textGetDate.getText().toString().equals("Click Here")) {
                    Toast.makeText(getApplicationContext(), "Ingrese una fecha", Toast.LENGTH_SHORT).show();

                } else {

                    table_layout = (TableLayout) findViewById(R.id.tableLayout1);
                    table_layout.removeAllViews();
                    Cursor c = dbAdapter_consultar_inventario_anterior.getConsultarInventario(strCurrDate);
                    if (c.moveToFirst()) {
                        // cursorAdapter_consultar_inventario = new CursorAdapter_Consultar_Inventario(getApplicationContext(),c);
                        // listViewInventario.setAdapter(cursorAdapter_consultar_inventario);

                        int rows = c.getCount();
                        int cols = c.getColumnCount();


                        // outer for loop
                        for (int i = 0; i < rows; i++) {

                            TableRow row = new TableRow(getApplicationContext());
                            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT));
                            row.setBackgroundColor(Color.parseColor("#1565C0"));

                            // inner for loop
                            for (int j = 0; j < cols; j++) {

                                TextView tv = new TextView(getApplicationContext());
                                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                                        LayoutParams.WRAP_CONTENT));
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(12);
                                tv.setTextColor(Color.parseColor("#190707"));
                                tv.setPadding(0, 0, 0, 0);
                                tv.setText(c.getString(j));
                                tv.setBackground(getResources().getDrawable(R.drawable.border_bottom_personalizado_tableinventario));
                                row.addView(tv);
                                if (i == 0) {
                                    tv.setBackground(getResources().getDrawable(R.drawable.border_bottom_personalizado_tableheader));
                                    tv.setTextColor(Color.parseColor("#FFFFFF"));

                                    String tex = "";
                                    switch (j) {
                                        case 0:
                                            tex = "Codigo";
                                            break;
                                        case 1:
                                            tex = "Nombre";
                                            break;
                                        case 2:
                                            tex = "Cantidad";
                                            break;
                                    }
                                    tv.setText(tex);

                                }
                                if (j == 1 && i != 0) {
                                    tv.setGravity(Gravity.LEFT);
                                }

                            }

                            c.moveToNext();

                            table_layout.addView(row);

                        }
                    } else {
                        consultarInventarioAnterior = new ConsultarInventarioAnterior(context);
                        consultarInventarioAnterior.execute(strCurrDate);
                    }

                }
            }
        });

        showSlideMenu(mainActivity);

    }

    public void updatedate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        strCurrDate = dateFormat.format(calendar.getTime());
        Log.d("CALENDARIO", strCurrDate + "");
        textGetDate.setText(formate.format(calendar.getTime()));
    }

    public void setDate() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -1);
        DatePickerDialog datePickerDialog = new DatePickerDialog(VMovil_Consultar_Inventario.this, d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(now.getTime().getTime());
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updatedate();
        }
    };
/*
    private void showCalendar() {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.consultar_inventario_calendario, null);
        final DatePicker datePicker = (DatePicker) dialoglayout.findViewById(R.id.dateConsultar);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialoglayout);
        builder.setCancelable(false);
        builder.setPositiveButton("Listo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int liquidacion) {
                String date = datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear();
                textGetDate.setText(date);
            }
        });
        builder.show();
    }
*/

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

        textViewSlidePrincipal = (TextView) findViewById(R.id.slide_textviewPrincipal);
        textViewSlideCliente = (TextView) findViewById(R.id.slide_textViewClientes);
        textviewSlideCobranzas = (TextView) findViewById(R.id.slide_textViewCobranza);
        textviewSlideGastos = (TextView) findViewById(R.id.slide_TextViewGastos);
        textviewSlideResumen = (TextView) findViewById(R.id.slide_textViewResumen);
        textviewSlideCInventario = (TextView) findViewById(R.id.slide_textViewCargarInventario);
        textviewSlideARendir = (TextView) findViewById(R.id.slide_textViewARendir);

        textviewSlideConsultarInventario = (TextView) findViewById(R.id.slide_textViewConsultarInventario);
        textviewSlideConsultarInventario.setOnClickListener(this);

        textViewIngresosTotales = (TextView) findViewById(R.id.textView_IngresosTotales);
        textViewGastos = (TextView) findViewById(R.id.textView_Gastos);


        textViewSlidePrincipal.setOnClickListener(this);
        textViewSlideCliente.setOnClickListener(this);
        textviewSlideCobranzas.setOnClickListener(this);
        textviewSlideGastos.setOnClickListener(this);
        textviewSlideResumen.setOnClickListener(this);
        textviewSlideCInventario.setOnClickListener(this);
        textviewSlideARendir.setOnClickListener(this);


        slideIdAgente = session.fetchVarible(1);
        slideIdLiquidacion = session.fetchVarible(3);
        slideOpen();
        changeDataSlideMenu();


    }

    private void slideOpen() {
        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(this);
        dbAdapter_informe_gastos.open();
        dbGastosIngresos = new DbGastos_Ingresos(this);
        dbGastosIngresos.open();
        dbHelperAgente = new DbAdapter_Agente(this);
        dbHelperAgente.open();
    }



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

    @Override
    protected void onResume() {
        super.onResume();
        //SLIDING MENU
        changeDataSlideMenu();
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.slide_textViewConsultarInventario:
                menu.toggle();
                break;
            case R.id.VEI_BTNclient:
                Intent i = new Intent(this, VMovil_Menu_Establec.class);
                startActivity(i);
                break;
            case R.id.VEI_BTNinfgas:
                Intent ig = new Intent(this, VMovil_Evento_Gasto.class);
                startActivity(ig);
                break;
            case R.id.VEI_BTNresume:
                Intent ir = new Intent(this, VMovil_Resumen_Caja.class);
                startActivity(ir);
                break;

            case R.id.VEI_BTNcobrarTodo:
                Intent cT = new Intent(this, VMovil_Cobros_Totales.class);
                startActivity(cT);
                break;
            case R.id.buttonNumeroEstablecimientos:
                Intent ine = new Intent(this, VMovil_Menu_Establec.class);
                startActivity(ine);
                break;
            //SLIDING MENU
            case R.id.slide_textviewPrincipal:
                Intent pi = new Intent(this, VMovil_Menu_Establec.class);
                startActivity(pi);

                break;
            case R.id.slide_textViewClientes:
                Intent ic1 = new Intent(this, VMovil_Menu_Establec.class);
                startActivity(ic1);
                break;
            case R.id.slide_textViewCobranza:
                Intent cT1 = new Intent(this, VMovil_Cobros_Totales.class);
                startActivity(cT1);
                break;
            case R.id.slide_TextViewGastos:
                Intent ig1 = new Intent(this, VMovil_Evento_Gasto.class);
                startActivity(ig1);
                break;
            case R.id.slide_textViewResumen:
                Intent ir1 = new Intent(this, VMovil_Resumen_Caja.class);
                startActivity(ir1);
                break;
            case R.id.slide_textViewCargarInventario:
                Intent cInventario = new Intent(this, VMovil_Cargar_Inventario.class);
                startActivity(cInventario);
                break;
            case R.id.slide_textViewARendir:

                break;
            default:
                break;
        }
    }
}
