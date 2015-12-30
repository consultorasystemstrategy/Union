package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import union.union_vr1.CustomOnItemSelectedListener;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DBAdapter_Temp_Venta;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Gasto;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.Utils;

public class VMovil_Evento_Gasto extends Activity implements View.OnClickListener {

    private final static int IGV = 18;
    private DbAdapter_Informe_Gastos dbHelperInformeGasto;
    private DbAdapter_Tipo_Gasto dbHelperTipoGasto;

    private Spinner spinnerTipoGasto;
    private Spinner spinnerProcedenciaGasto;
    private Spinner spinnerTipoDocumento;
    private EditText editTextTotal;
    private EditText editTextReferencia;
    private ListView listViewInformeGasto;

    private SimpleCursorAdapter dataAdapter;
    private String[] TipoGasto = new String[20];
    private View header;
    private Activity activity;

    private EditText editText;
    int agente = 1;
    int tipoDocumento = 1;
    private DbAdapter_Temp_Session session;


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
    TextView textviewSlideConsultarInventario;
    Button buttonSlideNroEstablecimiento;
    private TextView textViewSlideCargar;
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
        //Creando la UI
        setContentView(R.layout.princ_evento_gasto);

        activity = this;
        session = new DbAdapter_Temp_Session(this);
        session.open();

        agente = session.fetchVarible(1);

        dbHelperInformeGasto = new DbAdapter_Informe_Gastos(this);
        dbHelperInformeGasto.open();
        dbHelperTipoGasto = new DbAdapter_Tipo_Gasto(this);
        dbHelperTipoGasto.open();


        spinnerTipoGasto = (Spinner) findViewById(R.id.spinner_VEG_tipoGasto);
        spinnerProcedenciaGasto = (Spinner) findViewById(R.id.spinner_VEG_procedenciaGasto);

        spinnerTipoDocumento = (Spinner) findViewById(R.id.spinner_VEG_tipoDocumento);
        listViewInformeGasto = (ListView) findViewById(R.id.VEG_listViewInformeGasto);

        header = getLayoutInflater().inflate(R.layout.infor_gasto_cabecera,null);
        listViewInformeGasto.addHeaderView(header,null,false);

        editTextTotal = (EditText) findViewById(R.id.editText_VEG_total);
        editTextReferencia = (EditText) findViewById(R.id.editText_VEG_referencia);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //SLIDING MENU
        dbGastosIngresos = new DbGastos_Ingresos(this);
        dbGastosIngresos.open();

        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(this);
        dbAdapter_informe_gastos.open();

        dbHelperAgente = new DbAdapter_Agente(this);
        dbHelperAgente.open();

        editTextTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                Log.d("BEFORE TEXT CHANGE", "ON");
                if (editTextTotal.getText().toString().trim() != "") {
                    editTextTotal.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("ON TEXT CHANGE", "ON");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("AFTER TEXT CHANGE", "ON");
                if (editTextTotal.getText().toString().trim().equals("")) {
                    editTextTotal.setError("Es Requerido");
                } else {
                    editTextTotal.setError(null);

                }
            }
        });

        addItemsOnSpinners();


        spinnerTipoGasto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tipoGasto = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(getApplicationContext(), tipoGasto, Toast.LENGTH_SHORT).show();

                if (tipoGasto.equals("Combustible")) {
                    String itemProcedenciaGasto = (String) spinnerProcedenciaGasto.getSelectedItem();
                    ProcedenciaGasto procedenciaGasto = ProcedenciaGasto.valueOf(itemProcedenciaGasto);
                    switch (procedenciaGasto) {
                        case planta:
                            addItemsTipoDocumento_CombustiblePlanta();
                            break;
                        case ruta:
                            addItemsTipoDocumento_CombustibleRuta();
                            break;
                    }
                }else{
                    addItemsTipoDocumento();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerProcedenciaGasto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int positionTipoGasto = spinnerTipoGasto.getSelectedItemPosition();
                String tipoGasto = spinnerTipoGasto.getItemAtPosition(spinnerTipoGasto.getSelectedItemPosition()).toString();


                String procedenciaSeleccionada = (String) adapterView.getItemAtPosition(i);
                ProcedenciaGasto procedenciaGasto = ProcedenciaGasto.valueOf(procedenciaSeleccionada);

                if (tipoGasto.equals("Combustible")) {
                    switch (procedenciaGasto) {
                        case planta:
                            addItemsTipoDocumento_CombustiblePlanta();
                            break;
                        case ruta:
                            addItemsTipoDocumento_CombustibleRuta();
                            break;

                    }

                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //SLIDING MENU
        showSlideMenu(activity);

        displayListViewVEG();
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
            case R.id.slide_textViewCargarInventario:
                Intent cInventario = new Intent(this, VMovil_Cargar_Inventario.class);
                startActivity(cInventario);
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
                menu.toggle();
                break;
            case R.id.slide_textViewResumen:
                Intent ir1 = new Intent(this, VMovil_Resumen_Caja.class);
                finish();
                startActivity(ir1);
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
    public class SpinnerObject {

        private int databaseId;
        private String databaseValue;

        public SpinnerObject(int databaseId, String databaseValue) {
            this.databaseId = databaseId;
            this.databaseValue = databaseValue;
        }

        public int getId() {
            return databaseId;
        }

        public String getDatabaseValue() {
            return databaseValue;
        }

        @Override
        public String toString() {
            return databaseValue;
        }

    }

    public List<SpinnerObject> getAllLabels() {

        Cursor cursor = dbHelperTipoGasto.fetchAllTipoGastos();
        List<SpinnerObject> labels = new ArrayList<SpinnerObject>();
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                Log.d("TIPO GASTO SUB " + i, cursor.getString(2));
                TipoGasto[i] = cursor.getString(1);

                labels.add(new SpinnerObject(cursor.getInt(0), cursor.getString(2)));
                i += 1;
            } while (cursor.moveToNext());
        }
        // returning labels
        return labels;
    }

    public void addItemsOnSpinners() {

        // Spinner Drop down elements
        List<SpinnerObject> labelsTipoGasto = getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<SpinnerObject> dataAdapter = new ArrayAdapter<SpinnerObject>(this,
                android.R.layout.simple_spinner_item, labelsTipoGasto);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerTipoGasto.setAdapter(dataAdapter);

        //Creamos el adaptador
        ArrayAdapter<CharSequence> adapterProcedenciaGasto = ArrayAdapter.createFromResource(this, R.array.procedenciaGasto, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el menú
        adapterProcedenciaGasto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerProcedenciaGasto.setAdapter(adapterProcedenciaGasto);

        addItemsTipoDocumento();


    }

    public void addItemsTipoDocumento_CombustibleRuta() {
        //Creamos el adaptador
        ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this, R.array.tipoDocumentoCombustibleRuta, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el menú
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerTipoDocumento.setAdapter(adapterTipoDocumento);
    }

    public void addItemsTipoDocumento_CombustiblePlanta() {
        //Creamos el adaptador
        ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this, R.array.tipoDocumentoCombustiblePlanta, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el menú
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerTipoDocumento.setAdapter(adapterTipoDocumento);
    }

    public void addItemsTipoDocumento() {
        //Creamos el adaptador
        ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this, R.array.tipoDocumento, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el menú
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerTipoDocumento.setAdapter(adapterTipoDocumento);
    }


    public void agegarGastos(View v) {
        String tipoGasto = spinnerTipoGasto.getItemAtPosition(spinnerTipoGasto.getSelectedItemPosition()).toString();
        String procedenciaGasto = spinnerProcedenciaGasto.getItemAtPosition(spinnerProcedenciaGasto.getSelectedItemPosition()).toString();
        String tipoDoc = spinnerTipoDocumento.getItemAtPosition(spinnerTipoDocumento.getSelectedItemPosition()).toString();



         Cursor cursorTipoGasto = dbHelperTipoGasto.fetchTipoGastosByName(tipoGasto);
        int positionTipoGasto = 0;
        int positionProcedenciaGasto = 0;
        int positionTipoDocumento = 0;



        if (cursorTipoGasto!=null) {
            positionTipoGasto = cursorTipoGasto.getInt(cursorTipoGasto.getColumnIndexOrThrow(dbHelperTipoGasto.TG_id_tgasto));
        }

        if (procedenciaGasto.toLowerCase().equals("planta")){
            positionProcedenciaGasto = 1;
        }else if (procedenciaGasto.toLowerCase().equals("ruta")){
            positionProcedenciaGasto = 2;
        }

        Double total = 0.0;

        if (editTextTotal.getText().toString().equals("")){
            total = 0.0;
        }else{
            total = Double.valueOf(editTextTotal.getText().toString());
        }


        String referencia = editTextReferencia.getText().toString();

        String nombreDocumento = (String) spinnerTipoDocumento.getSelectedItem();

        TipoDocumento tipoDocumento = TipoDocumento.valueOf(nombreDocumento);

        Double igv = 0.0;
        Double subtotal = 0.0;
        int estado = 0;

        long idRegistroGastoInsertado = 0;
        switch (tipoDocumento) {
            case factura:
                igv = IGV * total / 100;
                subtotal = total - igv;
                positionTipoDocumento = 1;
                idRegistroGastoInsertado = dbHelperInformeGasto.createInformeGastos(positionTipoGasto, positionProcedenciaGasto, positionTipoDocumento, tipoGasto, subtotal, igv, total, getDatePhone(), null, estado, referencia, agente, Constants._CREADO);
                Log.d("TIPO DOCUMENTO", "FACTURA");
                break;
            case boleta:
                positionTipoDocumento = 2;
                idRegistroGastoInsertado = dbHelperInformeGasto.createInformeGastos(positionTipoGasto, positionProcedenciaGasto, positionTipoDocumento, tipoGasto, subtotal, igv, total, getDatePhone(), null, estado, referencia, agente, Constants._CREADO);
                Log.d("TIPO DOCUMENTO", "BOLETA");
                break;
            case ficha:
                positionTipoDocumento = 4;
                idRegistroGastoInsertado = dbHelperInformeGasto.createInformeGastos(positionTipoGasto, positionProcedenciaGasto, positionTipoDocumento, tipoGasto, subtotal, igv, total, getDatePhone(), null, estado, referencia, agente, Constants._CREADO);
                Log.d("TIPO DOCUMENTO", "FICHA");
                break;
        }


        editTextTotal.setText("");
        editTextTotal.setError(null);
        editTextReferencia.setText("Referencia");

        displayListViewVEG();
        Toast.makeText(getApplicationContext(), "Gasto Agregado", Toast.LENGTH_SHORT).show();
}

    private enum TipoDocumento {
        factura, ficha, boleta;
    }

    private enum ProcedenciaGasto {
        planta, ruta
    }

    private enum TipoDeGasto {
        Combustible, Comida, Departamento, Viaje, Nuevo
    }

    private void displayListViewVEG() {


        //CHANGE DATA SLIDE MENU
        changeDataSlideMenu();

        Cursor cursor = dbHelperInformeGasto.fetchAllInformeGastos(getDayPhone());
        cursor.moveToFirst();

        // The desired columns to be bound
        String[] columns = new String[]{
                //DbAdapter_Informe_Gastos.GA_id_gasto,
                DbAdapter_Tipo_Gasto.TG_nom_tipo_gasto,
                DbAdapter_Informe_Gastos.GA_total,
                //DbAdapter_Informe_Gastos.GA_subtotal,
                //DbAdapter_Informe_Gastos.GA_igv,
                //DbAdapter_Informe_Gastos.GA_referencia
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                //R.id.VEG_textViewIdTipoGasto,
                R.id.gastoNombre,
                R.id.gastoImporte,
                //R.id.VEG_textViewSubtotal,
                //R.id.gastoReferencia
                //R.id.VEG_textViewReferencia,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_evento_gasto,
                cursor,
                columns,
                to,
                0);

        // Assign adapter to ListView
        listViewInformeGasto.setAdapter(dataAdapter);
            listViewInformeGasto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Aquí obtengo el cursor posicionado en la fila que ha seleccionado/clickeado

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                final long id_gasto = cursor.getLong(cursor.getColumnIndex(DbAdapter_Informe_Gastos.GA_id_gasto));

                new AlertDialog.Builder(activity)
                        .setTitle("Seleccionar una acción")
                        .setItems(R.array.acciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        myEditDialog((int) id_gasto).show();
                                        break;
                                    case 1:
                                        new AlertDialog.Builder(activity)
                                                .setTitle("Eliminar")
                                                .setMessage("¿Está seguro que desea eliminar este gasto?")
                                                .setNegativeButton(android.R.string.no, null)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // FIRE ZE MISSILES!
                                                        boolean succesful = dbHelperInformeGasto.deleteGastoById((int) id_gasto);
                                                        if (succesful) {
                                                            Toast.makeText(getApplicationContext(),"Gasto eliminado.", Toast.LENGTH_LONG).show();
                                                                displayListViewVEG();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "No se pudo eliminar, intente nuevamente.", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                }).create().show();
                                        break;

                                }
                            }
                        }).create().show();
            }
        });
    }
    private Dialog myEditDialog(final int id_gasto) {
        final View layout = View.inflate(this, R.layout.editar_gastos, null);
        editText = ((EditText) layout.findViewById(R.id.editTextCosto));
        final TextView nombreGasto = ((TextView) layout.findViewById(R.id.textViewNombreGasto));



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                Log.d("BEFORE TEXT CHANGE", "ON");
                if (editText.getText().toString().trim() != "") {
                    editText.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("ON TEXT CHANGE", "ON");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("AFTER TEXT CHANGE", "ON");
                if (editText.getText().toString().trim().equals("")) {
                    editText.setError("Es Requerido");
                } else {
                    editText.setError(null);
                }
            }
        });

        Cursor cursorGasto = dbHelperInformeGasto.fetchGastosById(id_gasto);

        cursorGasto.moveToFirst();
        String nombre = "Nombre de Gasto";
        if (cursorGasto.getCount()>0){
            nombre = cursorGasto.getString(cursorGasto.getColumnIndexOrThrow(dbHelperInformeGasto.GA_nom_tipo_gasto));
            tipoDocumento = cursorGasto.getInt(cursorGasto.getColumnIndexOrThrow(dbHelperInformeGasto.GA_id_tipo_doc));
            nombreGasto.setText(""+nombre);
        }





        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar Costo");
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String texto = null;
                if (editText.getText().toString().trim().equals("")) {
                    texto = "10";
                } else {
                    texto = editText.getText().toString().trim();
                }

                Double costo = Double.valueOf(texto);
                Double subtotal = 0.0;
                Double igv = 0.0;
                switch (tipoDocumento){
                    case 1:
                        //CASO FACTURA
                        igv = IGV*costo/100;
                        subtotal = costo-igv;
                        dbHelperInformeGasto.updateGastosById(id_gasto,costo,subtotal,igv);
                        break;
                    case 2:
                        //CASO BOLETA
                        dbHelperInformeGasto.updateGastosById(id_gasto,costo,subtotal,igv);
                        break;
                    case 4:
                        dbHelperInformeGasto.updateGastosById(id_gasto,costo,subtotal,igv);
                        //CASO FICHA
                        break;
                    default:

                        break;
                }
                displayListViewVEG();
            }
        });

        builder.setView(layout);

        final AlertDialog alertDialog = builder.create();
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        return alertDialog;
    }

    public String getDatePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return df.format(date);
    }

    public String getDayPhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
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
        textviewSlideConsultarInventario = (TextView) findViewById(R.id.slide_textViewConsultarInventario);
        textViewIngresosTotales = (TextView) findViewById(R.id.textView_IngresosTotales);
        textViewGastos = (TextView) findViewById(R.id.textView_Gastos);

        textViewSlideCargar = (TextView)findViewById(R.id.slide_textViewCargarInventario);
        textViewSlideCargar.setOnClickListener(this);
        textviewSlideConsultarInventario.setOnClickListener(this);
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
    protected void onResume() {
        super.onResume();
        //SLIDING MENU
        changeDataSlideMenu();
    }
}