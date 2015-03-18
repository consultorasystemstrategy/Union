package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import union.union_vr1.BarcodeScanner.IntentIntegrator;
import union.union_vr1.BarcodeScanner.IntentResult;
import union.union_vr1.InputFilterMinMax;
import union.union_vr1.R;
import union.union_vr1.Sqlite.DBAdapter_Temp_Venta;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Barcode_Scanner;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.MyApplication;


public class VMovil_Venta_Cabecera_AgregarProductos extends Activity implements View.OnClickListener{

    private Button buttonAgregarProductos;
    private Button buttonScan;
    private AutoCompleteTextView autoCompleteTextView;
    private int idEstablecimiento;
    private int id_categoria_establecimiento = 1;
    private DbAdaptert_Evento_Establec dbHelper_Evento_Establecimiento;
    private DbAdapter_Precio dbHelper_Precio;
    private DBAdapter_Temp_Venta dbHelper_Temp_Venta;
    private SimpleCursorAdapter adapterProductos;
    private DbAdapter_Stock_Agente dbHelper_Stock_Agente;
    private Cursor mCursor;
    private Context mContext;
    private int cantidad;
    private int procedencia = 1;
    private int disponible = 1;
    private String nombre = null;
    private  int id_producto = 0;
    private int valor_unidad = 1;
    private Activity mainActivity;
    private ListView listView;
    private View header;
    private Cursor mCursorScannerProducto;
    private String barcodeScan;
    private String formatScan;
    private DbAdapter_Temp_Barcode_Scanner dbAdapter_temp_barcode_scanner;
    private DbAdapter_Temp_Session session;

    private int liquidacion;

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Usted Tiene que agregar los productos", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__venta__cabecera__agregar_productos);
        session =  new DbAdapter_Temp_Session(this);
        session.open();

        mContext = this;
        this.mainActivity = this;
        dbHelper_Stock_Agente = new DbAdapter_Stock_Agente(this);
        dbHelper_Stock_Agente.open();

        dbAdapter_temp_barcode_scanner = new DbAdapter_Temp_Barcode_Scanner(this);
        dbAdapter_temp_barcode_scanner.open();

        dbHelper_Precio = new DbAdapter_Precio(this);
        dbHelper_Precio.open();

        dbHelper_Evento_Establecimiento = new DbAdaptert_Evento_Establec(this);
        dbHelper_Evento_Establecimiento.open();

        dbHelper_Temp_Venta = new DBAdapter_Temp_Venta(this);
        dbHelper_Temp_Venta.open();

        liquidacion = session.fetchVarible(3);


        buttonAgregarProductos = (Button) findViewById(R.id.VCAP_buttonAgregar);
        buttonScan = (Button) findViewById(R.id.VCAP_buttonScanear);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.VCAP_AutoCompleteProductos);


        buttonAgregarProductos.setOnClickListener(this);
        buttonScan.setOnClickListener(this);



        Cursor cursorTempScanner = dbAdapter_temp_barcode_scanner.fetchAll();
        cursorTempScanner.moveToFirst();
        idEstablecimiento = cursorTempScanner.getInt(cursorTempScanner.getColumnIndexOrThrow(dbAdapter_temp_barcode_scanner.temp_id_establecimiento));


        Log.d("VARIABLE GLOBAL ID ESTABLECIMIENTO ",""+idEstablecimiento);


        final Cursor cursorEstablecimiento = dbHelper_Evento_Establecimiento.fetchEstablecsById(String.valueOf(idEstablecimiento));
        cursorEstablecimiento.moveToFirst();
        id_categoria_establecimiento = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_id_cat_est));

        mostrarProductosTemporales();


        mCursor = dbHelper_Stock_Agente.fetchStockAgenteByIdEstablecimiento(id_categoria_establecimiento, liquidacion);


        String[] columnasStock = new String[]{
                DbAdapter_Stock_Agente.ST_nombre,
                DbAdapter_Stock_Agente.ST_disponible,

        };

        // the XML defined views which the data will be bound to
        int[] toStock = new int[]{
                R.id.VCAP_producto,
                R.id.VCPA_stock,

        };
        adapterProductos = new SimpleCursorAdapter(this,
                R.layout.infor_venta_cabecera_productos,
                mCursor,
                columnasStock,
                toStock,
                0);

       autoCompleteTextView.setAdapter(adapterProductos);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                adapterProductos.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


           adapterProductos.setFilterQueryProvider(new FilterQueryProvider() {
               @Override
               public Cursor runQuery(CharSequence charSequence) {
                   return dbHelper_Stock_Agente.fetchStockAgenteByIdEstablecimientoandName(id_categoria_establecimiento, charSequence.toString(), liquidacion);

               }
           });


        autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),
                    "Ser on Item Selected",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Aquí obtengo el cursor posicionado en la fila que ha seleccionado/clickeado
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                autoCompleteTextView.setText("");
                //El item seleccionado tenía sólo 2 columnas visibles, pero en el cursos se encuentran todas las columnas
                //Aquí podemos obtener las otras columnas para los que querramos hacer con ellas
                nombre = cursor.getString(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_nombre));
                id_producto = cursor.getInt(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_id_producto));
                disponible = cursor.getInt(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_disponible));
                valor_unidad = cursor.getInt(cursor.getColumnIndex(DbAdapter_Precio.PR_valor_unidad));


                if (disponible>0){
                    myTextDialog().show();
                }else{
                    Toast.makeText(mainActivity, "No hay Stock", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    public void mostrarProductosTemporales(){
        Cursor cursorTempVentaDetalleByProcedencia = dbHelper_Temp_Venta.fetchAllTempVentaDetalleByProcedencia(procedencia);

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
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_venta_cabecera,
                cursorTempVentaDetalleByProcedencia,
                columns,
                to,
                0);


        listView = (ListView) findViewById(R.id.VCAP_listViewProductosAgregados);
        if (listView.getHeaderViewsCount()<1){
            header = getLayoutInflater().inflate(R.layout.infor_venta_cabecera,null);
            listView.addHeaderView(header);
        }
        // Assign adapter to ListView
        listView.setAdapter(simpleCursorAdapter);
    }

    private Dialog myTextDialog() {
        final View layout = View.inflate(this, R.layout.dialog_cantidad_productos, null);

        DecimalFormat df = new DecimalFormat("#.00");
        final EditText savedText = ((EditText) layout.findViewById(R.id.VCAP_editTextCantidad));
        final TextView nombreProducto = ((TextView) layout.findViewById(R.id.VCPA_textView2NombreProducto));
        final TextView precio = ((TextView) layout.findViewById(R.id.VCPA_textViewPrecio));

        Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto,id_categoria_establecimiento);


        Cursor mCursorStock = dbHelper_Stock_Agente.fetchByIdProducto(id_producto,liquidacion);
        int maximoValor = 1;
        if (mCursorStock.getCount()>0){
            maximoValor = mCursorStock.getInt(mCursorStock.getColumnIndexOrThrow(dbHelper_Stock_Agente.ST_disponible));
        }

        savedText.setFilters(new InputFilter[]{new InputFilterMinMax(0,maximoValor)});


        if (mCursorPrecioUnitarioGeneral.getCount()>0) {
            double precio_unitario = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
            precio.setText("Precio : S/. "+ df.format(precio_unitario));
        }else{
            precio.setText("Precio : S/. No encontrado");
        }
        nombreProducto.setText(nombre);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantidad");
        final int finalMaximoValor = maximoValor;
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                String texto = savedText.getText().toString().trim();
                if(texto.equals("")){
                    texto = "1";
                }

                cantidad = Integer.parseInt(texto);

                Toast.makeText(getApplicationContext(),"Cantidad : "+cantidad + " id_producto : "+ id_producto,Toast.LENGTH_LONG).show();

                Cursor mCursorPrecioUnitario = dbHelper_Precio.fetchAllPrecioByIdProductoAndCantidad(id_producto,cantidad, id_categoria_establecimiento);

                double precio_unitario = 10.0;
                if (mCursorPrecioUnitario.getCount()!=0){
                    precio_unitario = mCursorPrecioUnitario.getDouble(mCursorPrecioUnitario.getColumnIndex(DbAdapter_Precio.PR_precio_unit));
                }else{
                    Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(id_producto,id_categoria_establecimiento);
                    mCursorPrecioUnitarioGeneral.moveToFirst();
                    if (mCursorPrecioUnitarioGeneral.getCount()>0) {
                        precio_unitario = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
                    }else{
                        Toast.makeText(getApplicationContext(), "No se encontró un precio para esta cantidad de productos, agregar el precio a la base de datos", Toast.LENGTH_LONG).show();;
                    }
                }

                double total_importe = precio_unitario*cantidad;
                String promedio_anterior = null;
                String devuelto = null;

                //En una tabla "Temp_Venta" Nos sirve para agregar datos del historial de ventas anteriores y sugerir al usuario, estos son datos temporales
                long id = dbHelper_Temp_Venta.createTempVentaDetalle(1,id_producto,nombre,cantidad,total_importe, precio_unitario, promedio_anterior, devuelto, procedencia, valor_unidad);

               mostrarProductosTemporales();
            }
        });
        builder.setView(layout);

        final AlertDialog alertDialog = builder.create();
        savedText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        return alertDialog;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vmovil__venta__cabecera__agregar_productos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.VCAP_buttonAgregar:
                Intent intent = new Intent(this, VMovil_Venta_Cabecera.class);
                finish();
                startActivity(intent);
                break;
            case R.id.VCAP_buttonScanear:

                IntentIntegrator intentIntegrator = new IntentIntegrator(mainActivity);
                intentIntegrator.initiateScan();

                break;


        }


    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result}
            barcodeScan = scanResult.getContents();
            formatScan = scanResult.getFormatName();

            /*textViewContent.setText("CODEBAR CONTEN : "+contents);
            textViewFormat.setText("FORMAT : "+format);*/

            if (barcodeScan.length()>0){

                mCursorScannerProducto = dbHelper_Precio.getProductoByCodigo(id_categoria_establecimiento, barcodeScan, liquidacion);

                if (mCursorScannerProducto.getCount()>0){
                    scannerDialog().show();
                }else {
                    Toast.makeText(getApplicationContext(), "Producto con código de barras : "+ barcodeScan + "no disponible en el Stock Actual y/o Categoría establecimiento", Toast.LENGTH_SHORT).show();;
                }
            }else{
                Toast.makeText(getApplicationContext(), "No ha Scaneado ningún producto", Toast.LENGTH_SHORT).show();;
            }



        }
        // else continue with any other code you need in the method
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private Dialog scannerDialog() {

        int idProducto = -1;
        String nombreP = null;
        final View layout = View.inflate(this, R.layout.dialog_cantidad_productos, null);

        DecimalFormat df = new DecimalFormat("#.00");
        final EditText savedText = ((EditText) layout.findViewById(R.id.VCAP_editTextCantidad));
        final TextView nombreProducto = ((TextView) layout.findViewById(R.id.VCPA_textView2NombreProducto));
        final TextView precio = ((TextView) layout.findViewById(R.id.VCPA_textViewPrecio));

        mCursorScannerProducto.moveToFirst();
        if (mCursorScannerProducto.getCount()>0) {
            double precio_unitario = mCursorScannerProducto.getDouble(mCursorScannerProducto.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
            nombreP =  mCursorScannerProducto.getString(mCursorScannerProducto.getColumnIndexOrThrow(DbAdapter_Precio.PR_nombreProducto));
            idProducto = mCursorScannerProducto.getInt(mCursorScannerProducto.getColumnIndexOrThrow(DbAdapter_Precio.PR_id_producto));
            precio.setText("Precio : S/. "+ df.format(precio_unitario));
            nombreProducto.setText("Nombre : S/. " + nombreP);
        }else{
            precio.setText("Producto Scaneado no disponible en el Stock Actual");

        }



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantidad");
        final int finalIdProducto = idProducto;
        final String finalNombreP = nombreP;
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String texto = savedText.getText().toString().trim();
                if(texto.equals("")){
                    texto = "1";
                }

                cantidad = Integer.parseInt(texto);

                Toast.makeText(getApplicationContext(),"Cantidad : "+cantidad + " id_producto : "+ id_producto,Toast.LENGTH_LONG).show();

                Cursor mCursorPrecioUnitario = dbHelper_Precio.fetchAllPrecioByIdProductoAndCantidad(finalIdProducto,cantidad, id_categoria_establecimiento);

                double precio_unitario = 10.0;
                if (mCursorPrecioUnitario.getCount()!=0){
                    precio_unitario = mCursorPrecioUnitario.getDouble(mCursorPrecioUnitario.getColumnIndex(DbAdapter_Precio.PR_precio_unit));
                }else{
                    Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(finalIdProducto,id_categoria_establecimiento);
                    mCursorPrecioUnitarioGeneral.moveToFirst();
                    if (mCursorPrecioUnitarioGeneral.getCount()>0) {
                        precio_unitario = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndexOrThrow(DbAdapter_Precio.PR_precio_unit));
                    }else{
                        Toast.makeText(getApplicationContext(), "No se encontró un precio para esta cantidad de productos, agregar el precio a la base de datos", Toast.LENGTH_LONG).show();;
                    }
                }

                double total_importe = precio_unitario*cantidad;
                String promedio_anterior = null;
                String devuelto = null;

                //En una tabla "Temp_Venta" Nos sirve para agregar datos del historial de ventas anteriores y sugerir al usuario, estos son datos temporales
                long id = dbHelper_Temp_Venta.createTempVentaDetalle(1,finalIdProducto, finalNombreP,cantidad,total_importe, precio_unitario, promedio_anterior, devuelto, procedencia, 1);

                Intent intent = new Intent(mContext, VMovil_Venta_Cabecera_AgregarProductos.class);
                finish();
                startActivity(intent);
            }
        });
        builder.setView(layout);
        return builder.create();
    }
}
