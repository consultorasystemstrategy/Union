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
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DBAdapter_Temp_Venta;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.MyApplication;


public class VMovil_Venta_Cabecera_AgregarProductos extends Activity implements View.OnClickListener{

    private Button buttonAgregarProductos;
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

    private ListView listView;
    private View header;

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Usted Tiene que agregar los productos", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__venta__cabecera__agregar_productos);


        mContext = this;
        dbHelper_Stock_Agente = new DbAdapter_Stock_Agente(this);
        dbHelper_Stock_Agente.open();

        dbHelper_Precio = new DbAdapter_Precio(this);
        dbHelper_Precio.open();

        dbHelper_Evento_Establecimiento = new DbAdaptert_Evento_Establec(this);
        dbHelper_Evento_Establecimiento.open();

        dbHelper_Temp_Venta = new DBAdapter_Temp_Venta(this);
        dbHelper_Temp_Venta.open();




        buttonAgregarProductos = (Button) findViewById(R.id.VCAP_buttonAgregar);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.VCAP_AutoCompleteProductos);


        buttonAgregarProductos.setOnClickListener(this);

        idEstablecimiento = ((MyApplication)this.getApplication()).getIdEstablecimiento();

        final Cursor cursorEstablecimiento = dbHelper_Evento_Establecimiento.fetchEstablecsById(String.valueOf(idEstablecimiento));
        cursorEstablecimiento.moveToFirst();
        id_categoria_establecimiento = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_id_cat_est));



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
        header = getLayoutInflater().inflate(R.layout.infor_venta_cabecera,null);
        listView.addHeaderView(header);
        // Assign adapter to ListView
        listView.setAdapter(simpleCursorAdapter);



        mCursor = dbHelper_Stock_Agente.fetchStockAgenteByIdEstablecimiento(id_categoria_establecimiento);


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
                   return dbHelper_Stock_Agente.fetchStockAgenteByIdEstablecimientoandName(id_categoria_establecimiento, charSequence.toString());

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

                myTextDialog().show();

            }
        });


    }

    private Dialog myTextDialog() {
        final View layout = View.inflate(this, R.layout.dialog_cantidad_productos, null);

        final EditText savedText = ((EditText) layout.findViewById(R.id.VCAP_editTextCantidad));
        final TextView nombreProducto = ((TextView) layout.findViewById(R.id.VCPA_textView2NombreProducto));
        final TextView precio = ((TextView) layout.findViewById(R.id.VCPA_textViewPrecio));

        Cursor mCursorPrecioUnitarioGeneral = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(""+id_producto,""+1);
        double precio_unitario = mCursorPrecioUnitarioGeneral.getDouble(mCursorPrecioUnitarioGeneral.getColumnIndex(DbAdapter_Precio.PR_precio_unit));

        nombreProducto.setText(nombre);
        precio.setText("Precio : S/. "+precio_unitario);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantidad");
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String texto = savedText.getText().toString().trim();
                cantidad = Integer.parseInt(texto);

                Toast.makeText(getApplicationContext(),"Cantidad : "+cantidad + " id_producto : "+ id_producto,Toast.LENGTH_LONG).show();

                Cursor mCursorPrecioUnitario = dbHelper_Precio.fetchAllPrecioByIdProductoAndCantidad(id_producto,cantidad);
                double precio_unitario = 10.0;
                if (mCursorPrecioUnitario==null){
                    Toast.makeText(getApplicationContext(), "No se encontró un precio para esta cantidad de productos, agregar el precio a la base de datos", Toast.LENGTH_LONG).show();;
                }else{
                    precio_unitario = mCursorPrecioUnitario.getDouble(mCursorPrecioUnitario.getColumnIndex(DbAdapter_Precio.PR_precio_unit));
                }

                double total_importe = precio_unitario*cantidad;
                String promedio_anterior = null;
                String devuelto = null;

                //En una tabla "Temp_Venta" Nos sirve para agregar datos del historial de ventas anteriores y sugerir al usuario, estos son datos temporales
                long id = dbHelper_Temp_Venta.createTempVentaDetalle(1,id_producto,nombre,cantidad,total_importe, precio_unitario, promedio_anterior, devuelto, procedencia);

                Intent intent = new Intent(mContext, VMovil_Venta_Cabecera_AgregarProductos.class);
                finish();
                startActivity(intent);
            }
        });
        builder.setView(layout);
        return builder.create();
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

        }
    }
}
