package union.union_vr1.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DBAdapter_Temp_Venta;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Histo_Comprob_Anterior;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Utils.MyApplication;

public class VMovil_Venta_Cabecera extends Activity implements OnClickListener{


    private DbAdapter_Histo_Comprob_Anterior dbHelper_Histo_comprob_anterior;
    private DBAdapter_Temp_Venta dbHelper_temp_venta;
    private DbAdapter_Comprob_Venta dbHelper_Comprob_Venta;
    private DbAdapter_Agente dbHelperAgente;
    private DbAdapter_Comprob_Venta_Detalle dbHelper_Comprob_Venta_Detalle;

    private int idEstablecimiento;
    int id_agente_venta;
    private SimpleCursorAdapter simpleCursorAdapter;

    private Spinner spinnerTipoDocumento;
    private Spinner spinnerFormaPago;
    private Button buttonAgregar;
    private Button buttonVender;
    private ListView listView;
    private View header;

    /*
    private Cursor cursor, cursorx, cursorv, cursorz, cursorw, cursory, cursort, cursorn, cursorp, cursorf;
    private SimpleCursorAdapter dataAdapter;
    private DbAdapter_Comprob_Venta_Detalle dbHelper;
    private DbAdaptert_Evento_Establec dbHelperx;
    private DbAdapter_Comprob_Cobro dbHelpery;
    private DbAdapter_Comprob_Venta dbHelperz;
    private DbAdapter_Agente dbHelperv;
    private DbAdapter_Histo_Venta_Detalle dbHelpera;
    private DbAdapter_Histo_Comprob_Anterior dbHelper8;
    private DbAdapter_Stock_Agente dbHelperp;
    private DbAdapter_Precio dbHelper7;
    private Button mProduc, mProces, mCancel;
    private Spinner spinner1, spinner2, spinner3;
    private double valbaimp, valimpue, valtotal;
    private String valIdEstabX;
    private double valCredito;
    private int valDiaCred;
    private String valTipVen;
    private String impresion;
    private String idProduc;
    private int pase;
    private int pase01;
    private int pase02;
    private String TipoDocS;
    private double valtotalp;
    private String fechaCobro;
    private String SerieBoleta, SerieFactura, SerieRrpp, SerieUsa;
    private int CorreBoleta, CorreFactura, CorreRrpp, idAgente, CorreUsa;
    private int idComprobV1, idComprobV2;
    private String DetCompr;
    private String valCatEst;
    final Context context = this;
    private String idprod;
    private double vender;
    TextView colors, stoant, stohoy;
    */
    //private int Cantidad, cant1, cant2, cant3, cant4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_venta_cabecera);


        dbHelper_Histo_comprob_anterior = new DbAdapter_Histo_Comprob_Anterior(this);
        dbHelper_Histo_comprob_anterior.open();

        dbHelper_temp_venta = new DBAdapter_Temp_Venta(this);
        dbHelper_temp_venta.open();

        dbHelper_Comprob_Venta = new DbAdapter_Comprob_Venta(this);
        dbHelper_Comprob_Venta.open();

        dbHelperAgente = new DbAdapter_Agente(this);
        dbHelperAgente.open();

        dbHelper_Comprob_Venta_Detalle  = new DbAdapter_Comprob_Venta_Detalle(this);
        dbHelper_Comprob_Venta_Detalle.open();

        Bundle bundle = getIntent().getExtras();
        idEstablecimiento=Integer.parseInt(bundle.getString("idEstabX"));
        id_agente_venta= ((MyApplication) this.getApplication()).getIdAgente();

        setContentView(R.layout.princ_venta_cabecera);




        spinnerTipoDocumento = (Spinner) findViewById(R.id.VC_spinnerTipoDocumento);
        ArrayAdapter<CharSequence> adapterTipoDocumento = ArrayAdapter.createFromResource(this,R.array.tipoDocumento,android.R.layout.simple_spinner_item);
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDocumento.setAdapter(adapterTipoDocumento);

        spinnerFormaPago = (Spinner) findViewById(R.id.VC_spinnerFormaPago);
        ArrayAdapter<CharSequence> adapterFormaPago = ArrayAdapter.createFromResource(this,R.array.forma_pago,android.R.layout.simple_spinner_item);
        adapterFormaPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormaPago.setAdapter(adapterFormaPago);

        buttonAgregar = (Button) findViewById(R.id.VC_buttonAgregarProductos);
        buttonVender = (Button) findViewById(R.id.VC_buttonVender);
        buttonVender.setOnClickListener(this);
        buttonAgregar.setOnClickListener(this);

        displayHistorialComprobanteAnterior();

        spinnerFormaPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String formaDePago = adapterView.getItemAtPosition(i).toString();
                FormaPago formaPago = FormaPago.valueOf(formaDePago);

                switch (formaPago){
                    case Contado:

                        break;
                    case Credito:
                        Intent intent = new Intent(getApplicationContext(), VMovil_Venta_Cabecera_PlanPagos.class);
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /*
        pase = 0;
        pase01 = 0;
        pase02 = 0;

        //colors.setBackgroundColor(Color.WHITE);
        //stoant.setText("Surtido del Stock Anterior:1 - Venta Anterior:1(1%)");
        //stohoy.setText("Surtido del Stock de Hoy:2 - Venta Hoy:2(2%)");

        Bundle bundle = getIntent().getExtras();
        valIdEstabX=bundle.getString("idEstabX");
        setContentView(R.layout.princ_venta_cabecera);

        colors = (TextView)findViewById(R.id.VVC_TXVcolors);
        stoant = (TextView)findViewById(R.id.VVC_TXVstoant);
        stohoy = (TextView)findViewById(R.id.VVC_TXVstohoy);

        colors.setBackgroundColor(Color.WHITE);
        stoant.setText(String.valueOf("Surtido del Stock Anterior:0 - Venta Anterior:0(0%)"));
        stohoy.setText(String.valueOf("Surtido del Stock de Hoy:0 - Venta Hoy:0(0%)"));

        dbHelper = new DbAdapter_Comprob_Venta_Detalle(this);
        dbHelper.open();
        dbHelperx = new DbAdaptert_Evento_Establec(this);
        dbHelperx.open();
        dbHelpery = new DbAdapter_Comprob_Cobro(this);
        dbHelpery.open();
        dbHelperz = new DbAdapter_Comprob_Venta(this);
        dbHelperz.open();
        dbHelperv = new DbAdapter_Agente(this);
        dbHelperv.open();
        dbHelpera = new DbAdapter_Histo_Venta_Detalle(this);
        dbHelpera.open();
        dbHelperp = new DbAdapter_Stock_Agente(this);
        dbHelperp.open();
        dbHelper8 = new DbAdapter_Histo_Comprob_Anterior(this);
        dbHelper8.open();
        dbHelper7 = new DbAdapter_Precio(this);
        dbHelper7.open();
        //dbHelperv.insertSomeAgentes();

        procesarCAG();

        //dbHelper.deleteAllComprobVentaDetalle();
        //dbHelperz.deleteAllComprobVenta();
        //dbHelpery.deleteAllComprobCobros();
        //dbHelperx.insertSomeEstablecs();
        spinner1 = (Spinner) findViewById(R.id.VVC_SPNtipven);
        spinner2 = (Spinner) findViewById(R.id.VVC_SPNtipcom);
        spinner3 = (Spinner) findViewById(R.id.VVC_SPNforpag);

        mProduc = (Button)findViewById(R.id.VVC_BTNproducto);
        mProces = (Button)findViewById(R.id.VVC_BTNprocesar);
        mCancel = (Button)findViewById(R.id.VVC_BTNcancelar);

        mProduc.setOnClickListener(this);
        mProces.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        addItemsOnSpinner1();
        addItemsOnSpinner2();
        addItemsOnSpinner3();
        displayListViewVVC();
        */
    }

    private enum FormaPago{
        Contado, Credito
    }

@Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.VC_buttonAgregarProductos:
                Intent intent  = new Intent(this, VMovil_Venta_Cabecera_AgregarProductos.class);

                startActivity(intent);
                break;
            case R.id.VC_buttonVender:
                    vender();
                break;
            default:

                break;
        }

    }

    public void vender(){

            //Obtener los datos de las ventas

            String formaPago = spinnerFormaPago.getSelectedItem().toString();
            String tipoDocumento = spinnerTipoDocumento.getSelectedItem().toString();

            TipoDocumento tipoDocumento1 = TipoDocumento.valueOf(tipoDocumento);
            FormaPago formaPago1 = FormaPago.valueOf(formaPago);

            int tipoVenta = 1;
            int numero_documento = 1;
            int estado_conexion = 0;
            int i_tipoDocumento = 0;
            int i_formaPago = 0;
            int estado_comprobante = 1;
            Double monto_total  = 0.0;
            Double igv  = 0.0;
            Double base_imponible = 0.0;
            String erp_stringTipoDocumento  = null;
            String serie = null;
            String codigo_erp = null;

            switch (tipoDocumento1){
                case factura:
                    i_tipoDocumento = 1;
                    erp_stringTipoDocumento="FV";
                    serie = dbHelperAgente.getSerieFacturaByIdAgente("14");
                    codigo_erp = erp_stringTipoDocumento+serie;
                    break;
                case boleta:
                    i_tipoDocumento = 2;
                    erp_stringTipoDocumento="BV";
                    serie = dbHelperAgente.getSerieBoletaByIdAgente("14");
                    codigo_erp = erp_stringTipoDocumento+serie;
                    break;
                case ficha:
                    break;
            }
            switch (formaPago1){
                case Contado:
                    i_formaPago = 1;
                    break;
                case Credito:
                    i_formaPago = 2;
                    break;

            }


        String datosConcatenados = "idEestableciminto : " + idEstablecimiento + "\n" +
                "idAgente : " + id_agente_venta + "\n" +
                "Forma de pago : " + formaPago + "\n" +
                "Tipo Documento : " + tipoDocumento+ "\n" + "---------------------------";

        long id = dbHelper_Comprob_Venta.createComprobVenta(idEstablecimiento,i_tipoDocumento,i_formaPago,tipoVenta,codigo_erp,serie,numero_documento,base_imponible,igv,monto_total,null,null,estado_comprobante, estado_conexion,id_agente_venta);



        int id_comprobante = (int) id;


        Cursor cursorTemp = simpleCursorAdapter.getCursor();



        for (cursorTemp.moveToFirst(); !cursorTemp.isAfterLast();cursorTemp.moveToNext()){

            int _id = cursorTemp.getInt(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_venta_detalle));
            int id_producto = cursorTemp.getInt(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_id_producto));
            String nombre_producto = cursorTemp.getString(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_nom_producto));
            int cantidad = cursorTemp.getInt(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_cantidad));
            Double precio_unitario = cursorTemp.getDouble(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_precio_unit));
            Double importe = cursorTemp.getDouble(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_importe));

            String promedio_anterior = cursorTemp.getString(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_prom_anterior));
            String devuelto = cursorTemp.getString(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_devuelto));

            monto_total += importe;

            long comprobVentaDetalle = dbHelper_Comprob_Venta_Detalle.createComprobVentaDetalle(id_comprobante, id_producto, nombre_producto, cantidad, importe,0, precio_unitario, promedio_anterior, devuelto,0);


            datosConcatenados+="Producto  "+ nombre_producto + "Vendido satisfactoriamente con id : "+ comprobVentaDetalle;
        }

        base_imponible = monto_total /1.18;
        igv = base_imponible*0.18;
        dbHelper_Comprob_Venta.updateComprobanteMontos(id,monto_total,igv, base_imponible);
        datosConcatenados+="total de gasto : " + monto_total;
        datosConcatenados+="base impornible: " + base_imponible;
        datosConcatenados+="igv : " + igv;

        dbHelper_temp_venta.deleteAllTempVentaDetalle();

        Intent intent= new Intent(this, VMovil_Evento_Indice.class);
        Toast.makeText(getApplicationContext(),datosConcatenados,Toast.LENGTH_LONG).show();
        startActivity(intent);


    }

    public enum TipoDocumento{
        factura, boleta, ficha
    }
    private void displayHistorialComprobanteAnterior() {

        Cursor cursor = dbHelper_Histo_comprob_anterior.fetchAllHistoComprobAnteriorByIdEstRawQuery(idEstablecimiento);
        //OBTENGO LAS PJOSICIONES DE LAS COLUMNAS DEL CURSOR
        int indice_id = cursor.getColumnIndex("_id");
        int indice_id_producto = cursor.getColumnIndex("id_producto");
        int indice_nombre_producto = cursor.getColumnIndex("nombre_producto");
        int indice_cantidad = cursor.getColumnIndex("cantidad");
        int indice_precio_unitario = cursor.getColumnIndex("pu");
        int indice_importe = cursor.getColumnIndex("total");

        int indice_promedio_anterior = cursor.getColumnIndex("pa");
        int indice_devuelto  = cursor.getColumnIndex("devuelto");

        //t indice_valor_unidad = cursor.getColumnIndex("");
        //int indice_costo_venta = cursor.getColumnIndex("");

        cursor.moveToFirst();

        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){

            int _id = cursor.getInt(indice_id);
            int id_producto = cursor.getInt(indice_id_producto);
            String nombre_producto = cursor.getString(indice_nombre_producto);
            int cantidad = cursor.getInt(indice_cantidad);
            Double precio_unitario = cursor.getDouble(indice_precio_unitario);
            Double importe = cursor.getDouble(indice_importe);

            String promedio_anterior = cursor.getString(indice_promedio_anterior);
            String devuelto = cursor.getString(indice_devuelto);

            //En una tabla "Temp_Venta" Nos sirve para agregar datos del historial de ventas anteriores y sugerir al usuario, estos son datos temporales
            long id = dbHelper_temp_venta.createTempVentaDetalle(1,id_producto,nombre_producto,cantidad,importe, precio_unitario, promedio_anterior, devuelto);


        }

        Cursor cursorTempVentaDetalle = dbHelper_temp_venta.fetchAllTempVentaDetalle();
        // The desired columns to be bound
        String[] columns = new String[]{
                DBAdapter_Temp_Venta.temp_nom_producto,
                DBAdapter_Temp_Venta.temp_cantidad,
                DBAdapter_Temp_Venta.temp_precio_unit,
                DBAdapter_Temp_Venta.temp_importe,

        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.VC_producto,
                R.id.VC_promedio,
                R.id.VC_pu,
                R.id.VC_total

        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        simpleCursorAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_venta_cabecera,
                cursorTempVentaDetalle,
                columns,
                to,
                0);

        listView = (ListView) findViewById(R.id.VC_listView);
        header = getLayoutInflater().inflate(R.layout.infor_venta_cabecera,null);
        listView.addHeaderView(header);
        // Assign adapter to ListView
        listView.setAdapter(simpleCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Toast.makeText(getApplicationContext(),""+view,Toast.LENGTH_SHORT).show();
            }

        });
    }

    /*
    private void displayListViewVVC() {

        Cursor cursor = dbHelper.fetchAllComprobVentaDetalle0();

        // The desired columns to be bound
        String[] columns = new String[]{
                DbAdapter_Comprob_Venta_Detalle.CD_cantidad,
                DbAdapter_Comprob_Venta_Detalle.CD_nom_producto,
                DbAdapter_Comprob_Venta_Detalle.CD_prom_anterior,
                DbAdapter_Comprob_Venta_Detalle.CD_devuelto,
                DbAdapter_Comprob_Venta_Detalle.CD_precio_unit,
                DbAdapter_Comprob_Venta_Detalle.CD_importe
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.VVC_cantidinf,
                R.id.VVC_producinf,
                R.id.VVC_proantinf,
                R.id.VVC_devoluinf,
                R.id.VVC_preuniinf,
                R.id.VVC_precioinf,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_venta_cabecera,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.VVC_LSTprosel);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursorw = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                idProduc = cursorw.getString(cursorw.getColumnIndexOrThrow("_id"));

                eliminar(idProduc);
                //displayListViewVVC();
                Toast.makeText(getApplicationContext(),
                        idProduc, Toast.LENGTH_SHORT).show();
            }

        });
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner1() {
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this,R.array.tipo_venta,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
    }

    public void addItemsOnSpinner2() {
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this,R.array.tipo_comprobante,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
    }

    public void addItemsOnSpinner3() {
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this,R.array.forma_pago,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter);
    }



    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.VVC_BTNproducto:
                displayListViewVVC();
                porcstock();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Precargar Producto");

                // set dialog message
                alertDialogBuilder
                        .setMessage("¿Elegir?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                precarga2();
                                Toast.makeText(getApplicationContext(),
                                        idprod, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(),
                                        "Precargado", Toast.LENGTH_SHORT).show();
                                //displayListViewVVC();
                                displayListViewVVC();
                                porcstock();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                producto();
                                Toast.makeText(getApplicationContext(),
                                        "Sin Precargar", Toast.LENGTH_SHORT).show();
                                porcstock();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                break;
            case R.id.VVC_BTNprocesar:
                displayListViewVVC();
                porcstock();
                String prevenp = "Va a vender " + vender + "% que la venta anterior";
                Toast.makeText(getApplicationContext(),
                        prevenp, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder1.setTitle("Precargar Producto");

                // set dialog message
                alertDialogBuilder1
                        .setMessage("¿Elegir?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                aceptar();
                                }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                
                                Toast.makeText(getApplicationContext(),
                                        "Cancelar", Toast.LENGTH_SHORT).show();

                            }
                        });

                // create alert dialog
                AlertDialog alertDialog1 = alertDialogBuilder1.create();

                // show it
                alertDialog1.show();

                break;
            case R.id.VVC_BTNcancelar:
                finish();
                break;
            default:
                break;
        }
    }

    private void aceptar(){
        procesarPRO();
        valbaimp = 0;
        valimpue = 0;
        valtotal = 0;
        displayListViewVVC();
        procesarCVD();
        procesarCRE();
        if(spinner2.getSelectedItemId()==0){
            SerieUsa = SerieFactura;
            CorreUsa = CorreFactura;
            TipoDocS = "FACTURA";
            impresion =
                    "FACTURA : " +
                            "\nValor Inicial : "+ valbaimp +
                            "\nValor Impuest : "+ valimpue +
                            "\nValor Total   : "+ valtotal;

        }
        if(spinner2.getSelectedItemId()==1){
            SerieUsa = SerieBoleta;
            CorreUsa = CorreBoleta;
            TipoDocS = "BOLETA";
            impresion =
                    "BOLETA : " +
                            "\nValor Inicial : "+ valbaimp +
                            "\nValor Impuest : "+ valimpue +
                            "\nValor Total   : "+ valtotal;

        }
        DetCompr = SerieUsa +" " + CorreUsa;

        if(pase01 == 1) {
            dbHelperz.createComprobVenta(Integer.parseInt(valIdEstabX),
                    Integer.parseInt(String.valueOf(spinner2.getSelectedItemId() + 1)),
                    Integer.parseInt(String.valueOf(spinner3.getSelectedItemId() + 1)),
                    1, "1", SerieUsa, CorreUsa, valbaimp, valimpue, valtotal,
                    getDatePhone(), getTimePhone(), 1, 0, idAgente);
            cursorz = dbHelperz.fetchAllComprobVenta();
            cursorz.moveToLast();
            idComprobV1 = cursorz.getInt(0);
            dbHelper.updateComprobVentaDetalle1("0", String.valueOf(idComprobV1), "0");
            if(spinner2.getSelectedItemId()==0){
                dbHelperv.updateAgentesFA(String.valueOf(idAgente), SerieFactura,
                        String.valueOf(CorreFactura));
            }
            if(spinner2.getSelectedItemId()==1){
                dbHelperv.updateAgentesBO(String.valueOf(idAgente), SerieBoleta,
                        String.valueOf(CorreBoleta));
            }
        }
        if(pase02 == 1) {
            dbHelperz.createComprobVenta(Integer.parseInt(valIdEstabX),
                    Integer.parseInt(String.valueOf(spinner2.getSelectedItemId() + 1)),
                    Integer.parseInt(String.valueOf(spinner3.getSelectedItemId() + 1)),
                    2, "1", SerieRrpp, CorreRrpp, 0, 0, 0,
                    getDatePhone(), getTimePhone(), 1, 0, idAgente);
            cursorz = dbHelperz.fetchAllComprobVenta();
            cursorz.moveToLast();
            idComprobV2 = cursorz.getInt(0);
            if(pase01 == 0){
                dbHelper.updateComprobVentaDetalle1("0", String.valueOf(idComprobV2), "0");
            }
            if(pase02 == 1) {
                dbHelper.updateComprobVentaDetalle2(String.valueOf(idComprobV1), String.valueOf(idComprobV2), "0");
            }
            dbHelperv.updateAgentesRP(String.valueOf(idAgente), SerieRrpp,
                    String.valueOf(CorreRrpp));
        }
        if(pase01 == 1 || pase02 == 1){
            dbHelperx.updateEstablecs(valIdEstabX, 2);
        }
        if(spinner3.getSelectedItemId()==0){

            impresion +=
                    "\nCONTADO";
            Toast.makeText(getApplicationContext(), impresion, Toast.LENGTH_SHORT).show();
            finish();
        }
        if(spinner3.getSelectedItemId()==1){
            impresion +=
                    "\nCREDITO : " +
                            "\nMonto : "+ valCredito +
                            "\nDias  : "+ valDiaCred;
            //Toast.makeText(getApplicationContext(), impresion, Toast.LENGTH_SHORT).show();
            if(valtotal <= valCredito){
                pase = 1;
                impresion +=
                        "\nCREDITO APROBADO";
                Toast.makeText(getApplicationContext(), impresion, Toast.LENGTH_SHORT).show();
                //dbHelpery.deleteAllComprobCobros();
                final String[] items = {" 7 dias - 1 cuota", "15 dias - 1 cuota", "15 dias - 2 cuota",
                        "30 dias - 1 cuota", "30 dias - 2 cuota", "30 dias - 4 cuota"};

                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                dialogo.setTitle("Dias de Pago");
                dialogo.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0){
                            valtotalp = valtotal;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone1();
                            dbHelpery.createComprobCobros(Integer.parseInt(valIdEstabX), idComprobV1, 1, 1,
                                    TipoDocS, DetCompr, fechaCobro, valtotalp, "", "", 0, 0, 1);
                        }
                        if(item == 1){
                            valtotalp = valtotal;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone2();
                            dbHelpery.createComprobCobros(Integer.parseInt(valIdEstabX), idComprobV1, 1, 1,
                                    TipoDocS, DetCompr, fechaCobro, valtotalp, "", "", 0, 0, 1);
                        }
                        if(item == 2){
                            valtotalp = valtotal/2;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone1();
                            dbHelpery.createComprobCobros(Integer.parseInt(valIdEstabX), idComprobV1, 1, 1,
                                    TipoDocS, DetCompr, fechaCobro, valtotalp, "", "", 0, 0, 1);
                            valtotalp = valtotal/2;
                            fechaCobro = getDatePhone2();
                            dbHelpery.createComprobCobros(Integer.parseInt(valIdEstabX), idComprobV1, 1, 2,
                                    TipoDocS, DetCompr, fechaCobro, valtotalp, "", "", 0, 0, 1);
                        }
                        if(item == 3){
                            valtotalp = valtotal;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone4();
                            dbHelpery.createComprobCobros(Integer.parseInt(valIdEstabX), idComprobV1, 1, 1,
                                    TipoDocS, DetCompr, fechaCobro, valtotalp, "", "", 0, 0, 1);
                        }
                        if(item == 4){
                            valtotalp = valtotal/2;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone2();
                            dbHelpery.createComprobCobros(Integer.parseInt(valIdEstabX), idComprobV1, 1, 1,
                                    TipoDocS, DetCompr, fechaCobro, valtotalp, "", "", 0, 0, 1);
                            valtotalp = valtotal/2;
                            fechaCobro = getDatePhone4();
                            dbHelpery.createComprobCobros(Integer.parseInt(valIdEstabX), idComprobV1, 1, 2,
                                    TipoDocS, DetCompr, fechaCobro, valtotalp, "", "", 0, 0, 1);
                        }
                        if(item == 5){

                            valtotalp = valtotal/4;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone1();
                            dbHelpery.createComprobCobros(Integer.parseInt(valIdEstabX), idComprobV1, 1, 1,
                                    TipoDocS, DetCompr, fechaCobro, valtotalp, "", "", 0, 0, 1);
                            valtotalp = valtotal/4;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone2();
                            dbHelpery.createComprobCobros(Integer.parseInt(valIdEstabX), idComprobV1, 1, 2,
                                    TipoDocS, DetCompr, fechaCobro, valtotalp, "", "", 0, 0, 1);
                            valtotalp = valtotal/4;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone3();
                            dbHelpery.createComprobCobros(Integer.parseInt(valIdEstabX), idComprobV1, 1, 3,
                                    TipoDocS, DetCompr, fechaCobro, valtotalp, "", "", 0, 0, 1);
                            valtotalp = valtotal/4;
                            valtotalp = roundTwoDecimals(valtotalp);
                            fechaCobro = getDatePhone4();
                            dbHelpery.createComprobCobros(Integer.parseInt(valIdEstabX), idComprobV1, 1, 4,
                                    TipoDocS, DetCompr, fechaCobro, valtotalp, "", "", 0, 0, 1);
                        }
                        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_LONG).show();
                        eleccion(valIdEstabX, String.valueOf(idComprobV1), TipoDocS, DetCompr, String.valueOf(valtotal));
                    }
                });
                dialogo.create();
                dialogo.show();

            }else{
                pase = 0;
                impresion +=
                        "\nCREDITO NO APROBADO";
                Toast.makeText(getApplicationContext(), impresion, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void producto(){
        valTipVen = String.valueOf(spinner1.getSelectedItemId());
        //Toast.makeText(getApplicationContext(),
        //       valTipVen, Toast.LENGTH_SHORT).show();

        if(spinner1.getSelectedItemId()==0){
            if(pase01 == 0) {
                pase01 = 1;
            }
        }
        if(spinner1.getSelectedItemId()==1){
            if(pase02 == 0) {
                pase02 = 1;
            }
        }

        Intent i = new Intent(this, VMovil_Venta_Producto.class);
        i.putExtra("idEstabXY", valIdEstabX);
        i.putExtra("idTipoVen", valTipVen);
        startActivity(i);
    }

    private void precarga(){
        //cursor = manager.BuscarAgentes(tv.getText().toString());
        pase01 = 1;
        cursory = dbHelpera.fetchAllHistoVentaDetalleByIdEst2(valIdEstabX, "1");
        //Nos aseguramos de que existe al menos un registro
        if (cursory.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                int val01 = cursory.getInt(4);
                String val02 = cursory.getString(8);
                int val03 = cursory.getInt(9);
                Double val04 = cursory.getDouble(10);
                Double val05 = val04/val03;

                dbHelper.createComprobVentaDetalle( 0, val01, val02, val03, val04, val04, val05, "0/0", "0", 10);

            } while(cursory.moveToNext());
        }

    }

    public void porcstock(){

        int pridto = 0;
        int stocto = 0;
        int stkant = 0;
        int venant = 0;
        double porc01;
        double porc02;
        String mens01;
        String mens02;

        Cursor cursorj1 = dbHelper.fetchAllComprobVentaDetalle0();
        if (cursorj1.moveToFirst()) {
            do { pridto += 1; } while(cursorj1.moveToNext());
        }

        Cursor cursorj2 = dbHelperp.fetchAllStockAgente();
        if (cursorj2.moveToFirst()) {
            do { stocto += 1; } while(cursorj2.moveToNext());
        }

        Cursor cursorj3 = dbHelperx.fetchEstablecsByIdX(valIdEstabX);
        if (cursorj3.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                stkant = cursorj3.getInt(9);
                venant = cursorj3.getInt(10);
            } while(cursorj3.moveToNext());
        }

        porc01 = (100 * venant)/stkant;
        porc02 = (100 * pridto)/stocto;
        vender = porc01 - porc02;
        if(porc01 == porc02){colors.setBackgroundColor(Color.WHITE);}
        if(porc01 < porc02) {colors.setBackgroundColor(Color.GREEN);}
        if(porc01 > porc02) {colors.setBackgroundColor(Color.RED);}

        mens01 = "Surtido del Stock Anterior:" + stkant + " - Venta Anterior:"+ venant +"("+ porc01 +"%)";
        mens02 = "Surtido del Stock de Hoy:" + stocto + " - Venta Hoy:"+ pridto +"("+ porc02 +"%)";
        //colors.setBackgroundColor(Color.WHITE);
        stoant.setText(String.valueOf(mens01));
        stohoy.setText(String.valueOf(mens02));

    }

    public void procesarCES(){

        cursorx = dbHelperx.fetchEstablecsById(valIdEstabX);
        //Nos aseguramos de que existe al menos un registro
        if (cursorx.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                valCatEst = cursorx.getString(2);
            } while(cursorx.moveToNext());
        }
    }

    private void precarga2(){
        procesarCES();
        //cursor = manager.BuscarAgentes(tv.getText().toString());
        pase01 = 1;
        cursorn = dbHelper8.fetchAllHistoComprobAnteriorByIdEst(valIdEstabX);
        //Nos aseguramos de que existe al menos un registro
        if (cursorn.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                idprod = cursorn.getString(2);

                cursorp = dbHelper7.fetchAllPrecioById(idprod, valCatEst);
                cursorp.moveToFirst();
                Double precio = cursorp.getDouble(4);
                int val01 = cursorn.getInt(2);
                String val02 = cursorn.getString(3);
                int val03 = cursorn.getInt(4);
                Double val04 = precio;
                //Double val04 = 1.0;
                Double val05 = val04/val03;
                String val06 = cursorn.getString(5);
                String val07 = cursorn.getString(6);

                dbHelper.createComprobVentaDetalle( 0, val01, val02, val03, val04, val04, val05, val06, val07, 10);

            } while(cursorn.moveToNext());
        }

    }

    public void eliminar(final String idElim){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Eliminar Producto");

        // set dialog message
        alertDialogBuilder
                .setMessage("¿Elegir?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dbHelper.deleteAllComprobVentaDetalleById(idElim);
                        Toast.makeText(getApplicationContext(),
                                "Eliminado", Toast.LENGTH_SHORT).show();
                        //displayListViewVVC();
                        displayListViewVVC();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Toast.makeText(getApplicationContext(),
                                "No Eliminado", Toast.LENGTH_SHORT).show();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void procesarCAG(){
        cursorv = dbHelperv.fetchAllAgentesVenta();
        //Nos aseguramos de que existe al menos un registro
        if (cursorv.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            //do {
                idAgente = cursorv.getInt(1);
                SerieBoleta = cursorv.getString(2);
                SerieFactura = cursorv.getString(3);
                SerieRrpp = cursorv.getString(4);
                CorreBoleta = cursorv.getInt(5) + 1;
                CorreFactura = cursorv.getInt(6) + 1;
                CorreRrpp = cursorv.getInt(7) + 1;
            //} while(cursorx.moveToNext());
        }
    }

    public void procesarCRE(){
        cursorx = dbHelperx.fetchEstablecsById(valIdEstabX);
        //Nos aseguramos de que existe al menos un registro
        if (cursorx.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                valCredito = cursorx.getDouble(7);
                valDiaCred = cursorx.getInt(8);
            } while(cursorx.moveToNext());
        }
    }

    public void procesarCVD(){
        //cursor = manager.BuscarAgentes(tv.getText().toString());
        cursor = dbHelper.fetchAllComprobVentaDetalle0();
        //Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                valbaimp += cursor.getDouble(5);
            } while(cursor.moveToNext());
            if(spinner2.getSelectedItemId()==0){
                valimpue = valbaimp * 0.18;
                valtotal = valbaimp + valimpue;
            }
            if(spinner2.getSelectedItemId()==1){
                valimpue = valbaimp * 0;
                valtotal = valbaimp + valimpue;
            }
            //valbaimp = roundTwoDecimals(valbaimp);
            //valimpue = roundTwoDecimals(valimpue);
            //valtotal = roundTwoDecimals(valtotal);
        }
    }

    public void procesarPRO(){
        //cursor = manager.BuscarAgentes(tv.getText().toString());
        cursort = dbHelper.fetchAllComprobVentaDetalle0A();
        //Nos aseguramos de que existe al menos un registro
        if (cursort.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                int idproduc = cursort.getInt(2);
                int cantidad = cursort.getInt(4);
                elegirPRO(idproduc, cantidad);
            } while(cursort.moveToNext());
        }
    }

    public void elegirPRO(int idProd, int Cant){
        cursorw = dbHelperp.fetchStockAgenteByIdProd(String.valueOf(idProd));
        //Nos aseguramos de que existe al menos un registro
        cursorw.moveToFirst();

        int cant1 = cursorw.getInt(1);
        int cant2 = cursorw.getInt(2);
        int cant3 = cursorw.getInt(3);
        int cant4 = cursorw.getInt(4);
        dbHelperp.updateStockAgente(idProd, cant1 - Cant,
                cant2 - Cant, cant3 + Cant, cant4 - Cant);
    }

    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
    private String getTimePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }
    private String getDatePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }
    private String getDatePhone1()
    {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 7);
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }
    private String getDatePhone2()
    {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 14);
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }
    private String getDatePhone3()
    {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 21);
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }
    private String getDatePhone4()
    {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 28);
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    private void eleccion(String valIdEstS, String idComproS, String TipoDocSS, String DetComprS, String valtotalS){

        Intent idr = new Intent(this, VMovil_Venta_Credito.class);
        idr.putExtra("estab", valIdEstS);
        idr.putExtra("idcom", idComproS);
        idr.putExtra("tipdo", TipoDocSS);
        idr.putExtra("detco", DetComprS);
        idr.putExtra("total", valtotalS);
        startActivity(idr);
        finish();
    }
    /*

     */


    }
