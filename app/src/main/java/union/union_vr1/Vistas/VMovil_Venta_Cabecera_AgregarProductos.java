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
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import union.union_vr1.R;
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
    private SimpleCursorAdapter adapterProductos;
    private DbAdapter_Stock_Agente dbHelper_Stock_Agente;
    private Cursor mCursor;
    private Context mContext;
    private int cantidad;
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


        buttonAgregarProductos = (Button) findViewById(R.id.VCAP_buttonAgregar);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.VCAP_AutoCompleteProductos);


        buttonAgregarProductos.setOnClickListener(this);

        idEstablecimiento = ((MyApplication)this.getApplication()).getIdEstablecimiento();

        final Cursor cursorEstablecimiento = dbHelper_Evento_Establecimiento.fetchEstablecsById(String.valueOf(idEstablecimiento));
        cursorEstablecimiento.moveToFirst();
        id_categoria_establecimiento = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndex(DbAdaptert_Evento_Establec.EE_id_cat_est));


        mCursor = dbHelper_Stock_Agente.fetchStockAgenteByIdEstablecimiento(id_categoria_establecimiento);


        String[] columns = new String[]{
                DbAdapter_Stock_Agente.ST_nombre,
                DbAdapter_Stock_Agente.ST_disponible,

        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.VCAP_producto,
                R.id.VCPA_stock,

        };
        adapterProductos = new SimpleCursorAdapter(this,
                R.layout.infor_venta_cabecera_productos,
                mCursor,
                columns,
                to,
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

/*
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_cantidad_productos, null))
                        // Add action buttons
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                                Toast.makeText(getApplicationContext(), "id : "+id+"\n" +
                                        " dialog" + dialog,Toast.LENGTH_LONG).show();;

                            }
                        });
                builder.create();
                */


                myTextDialog().show();

                //Aquí obtengo el cursor posicionado en la fila que ha seleccionado/clickeado
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);


                autoCompleteTextView.setText("");
                //El item seleccionado tenía sólo 2 columnas visibles, pero en el cursos se encuentran todas las columnas
                //Aquí podemos obtener las otras columnas para los que querramos hacer con ellas
                String nombre = cursor.getString(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_nombre));
                int id_producto = cursor.getInt(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_id_producto));
                int disponible = cursor.getInt(cursor.getColumnIndex(DbAdapter_Stock_Agente.ST_disponible));

                Cursor mCursorPrecioUnitario = dbHelper_Precio.fetchAllPrecioByIdProductoAndCategoeria(""+id_producto,""+id_categoria_establecimiento);
                mCursorPrecioUnitario.moveToFirst();

                int precio_unitario = mCursorPrecioUnitario.getInt(mCursorPrecioUnitario.getColumnIndex(DbAdapter_Precio.PR_precio_unit));

                /*

                Toast.makeText(getApplicationContext(), nombre +
                        " id_producto : " + id_producto + "\n" +
                                "disponible : " + disponible, Toast.LENGTH_SHORT).show();
*/
            }
        });


    }

    private Dialog myTextDialog() {
        final View layout = View.inflate(this, R.layout.dialog_cantidad_productos, null);

        final EditText savedText = ((EditText) layout.findViewById(R.id.VCAP_editTextCantidad));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantidad");
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String texto = savedText.getText().toString().trim();
                cantidad = Integer.parseInt(texto);
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
                startActivity(intent);
                break;

        }
    }
}
