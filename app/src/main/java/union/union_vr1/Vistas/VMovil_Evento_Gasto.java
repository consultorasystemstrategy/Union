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
import android.view.Gravity;
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
import union.union_vr1.Servicios.ServiceExport;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.CursorAdapter_Gastos;
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

    private CursorAdapter_Gastos cursorAdapterGastos;
    private String[] TipoGasto = new String[20];
    private View header;
    private Activity activity;

    private EditText editText;
    int agente = 1;
    private int idLiquidacion = 1;
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

    private final static String TAG = VMovil_Evento_Gasto.class.getSimpleName();


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ServiceExport.class);
        intent.setAction(Constants.ACTION_EXPORT_SERVICE);
        startService(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Creando la UI
        setContentView(R.layout.princ_evento_gasto);

        activity = this;
        session = new DbAdapter_Temp_Session(this);
        session.open();


        idLiquidacion = session.fetchVarible(3);
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

        editTextReferencia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("BEFORE TEXT CHANGE", "ON");
                if (editTextReferencia.getText().toString().trim() != "") {
                    editTextReferencia.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("AFTER TEXT CHANGE", "ON");
                if (editTextReferencia.getText().toString().trim().equals("")) {
                    editTextReferencia.setError("Es Requerido");
                } else {
                    editTextReferencia.setError(null);

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
                    if (itemProcedenciaGasto.toLowerCase().equals("planta")){
                        addItemsTipoDocumento_CombustiblePlanta();
                    }else if (itemProcedenciaGasto.toLowerCase().equals("ruta")){
                        addItemsTipoDocumento_CombustibleRuta();
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

                if (tipoGasto.equals("Combustible")) {
                    if (procedenciaSeleccionada.toLowerCase().equals("planta")){
                        addItemsTipoDocumento_CombustiblePlanta();
                    }else if (procedenciaSeleccionada.toLowerCase().equals("ruta")){
                        addItemsTipoDocumento_CombustibleRuta();
                    }
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

    public void validateTipoDocumento(String tipoDoc){
        if(tipoDoc.equals(Constants._SPINNER_DEFAULT_COMPROBANTE)) {
            Toast.makeText(VMovil_Evento_Gasto.this, "Seleccionar Tipo de Comprobante", Toast.LENGTH_SHORT).show();
            return;
        }
    }



    public void agegarGastos(View v) {


        String tipoGasto = spinnerTipoGasto.getItemAtPosition(spinnerTipoGasto.getSelectedItemPosition()).toString();
        String procedenciaGasto = spinnerProcedenciaGasto.getItemAtPosition(spinnerProcedenciaGasto.getSelectedItemPosition()).toString();
        String tipoDoc = spinnerTipoDocumento.getItemAtPosition(spinnerTipoDocumento.getSelectedItemPosition()).toString();

        Log.d(TAG, "TIPO DE GASTOS SELECTED : "+tipoGasto);
        Log.d(TAG, "procedenciaGasto SELECTED : "+procedenciaGasto);
        Log.d(TAG, "tipoDoc SELECTED : "+tipoDoc);


        validateTipoDocumento(tipoDoc);
        if(editTextReferencia.getText().toString().equals("")){
            Toast.makeText(VMovil_Evento_Gasto.this, "Debe agregar un documento de referencia.", Toast.LENGTH_SHORT).show();
            return;
        }
         Cursor cursorTipoGasto = dbHelperTipoGasto.fetchTipoGastosByName(tipoGasto);
        int positionTipoGasto = 0;
        int positionProcedenciaGasto = 0;
        int positionTipoDocumento = 0;


        Log.d(TAG, "COUNT CUROSR TIPO GASTO: "  + cursorTipoGasto.getCount());

        if (cursorTipoGasto.getCount()>0) {
            cursorTipoGasto.moveToFirst();
            positionTipoGasto = cursorTipoGasto.getInt(cursorTipoGasto.getColumnIndexOrThrow(dbHelperTipoGasto.TG_id_tgasto));
        }

        if (procedenciaGasto.toLowerCase().equals("planta")){
            positionProcedenciaGasto = 1;
        }else if (procedenciaGasto.toLowerCase().equals("ruta")){
            positionProcedenciaGasto = 2;
        }

        Double total = 0.0;



        if (editTextTotal.getText().toString().equals("")){
            Toast.makeText(VMovil_Evento_Gasto.this, "Debe agregar un monto.", Toast.LENGTH_SHORT).show();
            total = 0.0;
            return;
        }else{
            total = Double.valueOf(editTextTotal.getText().toString());
        }


        String referencia = editTextReferencia.getText().toString();

        String nombreDocumento = (String) spinnerTipoDocumento.getSelectedItem();

        /*TipoDocumento tipoDocumento = TipoDocumento.valueOf(nombreDocumento);*/

        Double igv = 0.0;
        Double subtotal = 0.0;
        int estado = 0;

        long idRegistroGastoInsertado = 0;

        if (tipoDoc.equals(Constants._FACTURA)){
            igv = IGV * total / 100;
            subtotal = total - igv;
            positionTipoDocumento = 1;
            idRegistroGastoInsertado = dbHelperInformeGasto.createInformeGastos(positionTipoGasto, positionProcedenciaGasto, positionTipoDocumento, tipoGasto, subtotal, igv, total, getDatePhone(), null, estado, referencia, agente, Constants._CREADO, idLiquidacion);
            Log.d("TIPO DOCUMENTO", "FACTURA");
        }else if (tipoDoc.equals(Constants._BOLETA)){
            positionTipoDocumento = 2;
            idRegistroGastoInsertado = dbHelperInformeGasto.createInformeGastos(positionTipoGasto, positionProcedenciaGasto, positionTipoDocumento, tipoGasto, subtotal, igv, total, getDatePhone(), null, estado, referencia, agente, Constants._CREADO, idLiquidacion);
            Log.d("TIPO DOCUMENTO", "BOLETA");
        }else if (tipoDoc.equals(Constants._FICHA))
        {
            positionTipoDocumento = 4;
            idRegistroGastoInsertado = dbHelperInformeGasto.createInformeGastos(positionTipoGasto, positionProcedenciaGasto, positionTipoDocumento, tipoGasto, subtotal, igv, total, getDatePhone(), null, estado, referencia, agente, Constants._CREADO, idLiquidacion);
            Log.d("TIPO DOCUMENTO", "FICHA");
            //DO NOTHING
        }

        editTextTotal.setText("");
        editTextTotal.setError(null);
        editTextReferencia.setText("");
        editTextReferencia.setError(null);

        displayListViewVEG();
        Log.d(TAG, "id registro insertado : "+ idRegistroGastoInsertado);
        if(idRegistroGastoInsertado > 0){

            Toast toast = Toast.makeText(VMovil_Evento_Gasto.this,"Gasto Agregado", Toast.LENGTH_LONG);
            toast.getView().setBackgroundColor(VMovil_Evento_Gasto.this.getResources().getColor(R.color.verde));
            TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
            textView.setTextColor(VMovil_Evento_Gasto.this.getResources().getColor(R.color.Blanco));
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 8);
            toast.show();
        }else{
            Toast toast = Toast.makeText(VMovil_Evento_Gasto.this, "Ocurrió un error, Intente de nuevo.", Toast.LENGTH_LONG);
            toast.getView().setBackgroundColor(VMovil_Evento_Gasto.this.getResources().getColor(R.color.verde));
            TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
            textView.setTextColor(VMovil_Evento_Gasto.this.getResources().getColor(R.color.Blanco));
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 8);
            toast.show();
        }
}

    /*private enum TipoDocumento {
        factura, ficha,TipodeComprobante, boleta;
    }

    private enum ProcedenciaGasto {
        planta, ruta
    }
    */

    private enum TipoDeGasto {
        Combustible, Comida, Departamento, Viaje, Nuevo
    }

    private void displayListViewVEG() {


        //CHANGE DATA SLIDE MENU
        changeDataSlideMenu();

        Cursor cursor = dbHelperInformeGasto.fetchAllInformeGastos(getDayPhone());
        cursor.moveToFirst();

        cursorAdapterGastos = new CursorAdapter_Gastos(VMovil_Evento_Gasto.this, cursor);


        // Assign adapter to ListView
        listViewInformeGasto.setAdapter(cursorAdapterGastos);
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
                                                .setNegativeButton(R.string.no, null)
                                                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
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
        builder.setTitle("Editar Gasto");
        builder.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
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
        DecimalFormat df = new DecimalFormat("0.00");
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