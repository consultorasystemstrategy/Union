package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;

public class VMovil_Canc_Histo extends Activity {

    private DbAdapter_Histo_Venta_Detalle dbHelper;
    private SimpleCursorAdapter dataAdapter;
    //private int cant_ope;
    public String[] items = new String[20];
    public int tope;
    public int cate;
    public int form;
    public int cant;
    public double impo;
    private String valIdEstabX;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_canc_histo);

        dbHelper = new DbAdapter_Histo_Venta_Detalle(this);
        dbHelper.open();
        Bundle bundle = getIntent().getExtras();
        valIdEstabX = bundle.getString("idEstabX");
        //Clean all data
        //dbHelper.deleteAllHistoVentaDetalle();
        //Add some data
        //dbHelper.insertSomeHistoVentaDetalle();

        //Generate ListView from SQLite Database
        displayListView();

    }

    private void displayListView() {

        Cursor cursor = dbHelper.fetchAllHistoVentaDetalleByIdEst1(valIdEstabX, "2");
        //Cursor cursor = dbHelper.fetchAllHistoVentaDetalle();
        // The desired columns to be bound
        String[] columns = new String[]{
                DbAdapter_Histo_Venta_Detalle.HD_comprobante,
                DbAdapter_Histo_Venta_Detalle.HD_nom_producto,
                DbAdapter_Histo_Venta_Detalle.HD_cantidad_ope,
                DbAdapter_Histo_Venta_Detalle.HD_id_tipoper,
                DbAdapter_Histo_Venta_Detalle.HD_categoria_ope
        };
        // hola que k aseaaaa

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.VCH_compr,
                R.id.VCH_produ,
                R.id.VCH_canti,
                R.id.VCH_tipos,
                R.id.VCH_categ,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_canc_histo,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.VCH_listar);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.

                String idHVD = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                int cant_ope = cursor.getInt(cursor.getColumnIndexOrThrow("hd_in_cantidad"));
                Double importe = cursor.getDouble(cursor.getColumnIndexOrThrow("hd_re_importe_ope"));
                Toast.makeText(getApplicationContext(),
                        String.valueOf(importe), Toast.LENGTH_SHORT).show();
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.VCH_buscar);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchHistoVentaDetalleByNameEst1(valIdEstabX, constraint.toString(), "2");
            }
        });

    }

    private String getTimePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }
}