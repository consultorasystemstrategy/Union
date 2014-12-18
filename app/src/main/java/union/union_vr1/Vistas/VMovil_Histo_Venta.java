package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class VMovil_Histo_Venta extends Activity {

    private DbAdapter_Histo_Venta_Detalle dbHelper;
    private SimpleCursorAdapter dataAdapter;
    //private int cant_ope;
    public final String[] items01 = {"1"};
    public final String[] items02 = {"1","2"};
    public final String[] items03 = {"1","2","3"};
    public final String[] items04 = {"1","2","3","4"};
    public final String[] items05 = {"1","2","3","4","5"};
    public final String[] items06 = {"1","2","3","4","5","6"};
    public final String[] items07 = {"1","2","3","4","5","6","7"};
    public final String[] items08 = {"1","2","3","4","5","6","7","8"};
    public final String[] items09 = {"1","2","3","4","5","6","7","8","9"};
    public final String[] items10 = {"1","2","3","4","5","6","7","8","9","10"};
    public String[] items  = new String[20];
    public int tope;
    public int cate;
    public int form;
    public int cant;
    public double impo;
    private String valIdEstabX;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_histo_venta);

        dbHelper = new DbAdapter_Histo_Venta_Detalle(this);
        dbHelper.open();
        Bundle bundle = getIntent().getExtras();
        valIdEstabX=bundle.getString("idEstabX");
        //Clean all data
        //dbHelper.deleteAllHistoVentaDetalle();
        //Add some data
        //dbHelper.insertSomeHistoVentaDetalle();

        //Generate ListView from SQLite Database
        displayListView();

    }

    private void eleccion(final String idEstabl, final double impor){
        //Intent i = new Intent(this, VMovil_Evento_Establec.class);
        //i.putExtra("idEstab", idEstabl);
        //startActivity(i);
        final String[] items = {"Can - Bueno", "Can - Vencido", "Can - Malogrado", "Can - Reclamo",
                "Dev - Bueno", "Dev - Vencido", "Dev - Malogrado", "Dev - Reclamo"};
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("ACCION");
        dialogo.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(item == 0){
                    tope = 2;
                    cate = 1;
                    form = 1;
                    impo = 0;
                    dbHelper.updateHistoVentaDetalle1(idEstabl, tope, cate, form, cant, impo,
                            getDatePhone(), getTimePhone(), "1", "1", 2);
                    displayListView();
                }
                if(item == 1){
                    tope = 2;
                    cate = 2;
                    form = 1;
                    impo = 0;
                    dbHelper.updateHistoVentaDetalle1(idEstabl, tope, cate, form, cant, impo,
                            getDatePhone(), getTimePhone(), "1", "1", 2);
                    displayListView();
                }
                if(item == 2){
                    tope = 2;
                    cate = 3;
                    form = 1;
                    impo = 0;
                    dbHelper.updateHistoVentaDetalle1(idEstabl, tope, cate, form, cant, impo,
                            getDatePhone(), getTimePhone(), "1", "1", 2);
                    displayListView();
                }
                if(item == 3){
                    tope = 2;
                    cate = 4;
                    form = 1;
                    impo = 0;
                    dbHelper.updateHistoVentaDetalle1(idEstabl, tope, cate, form, cant, impo,
                            getDatePhone(), getTimePhone(), "1", "1", 2);
                    displayListView();
                }
                if(item == 4){
                    tope = 1;
                    cate = 1;
                    form = 1;
                    impo = impor;
                    dbHelper.updateHistoVentaDetalle1(idEstabl, tope, cate, form, cant, impo,
                            getDatePhone(), getTimePhone(), "1", "1", 2);
                    displayListView();
                }
                if(item == 5){
                    tope = 1;
                    cate = 2;
                    form = 1;
                    impo = impor;
                    dbHelper.updateHistoVentaDetalle1(idEstabl, tope, cate, form, cant, impo,
                            getDatePhone(), getTimePhone(), "1", "1", 2);
                    displayListView();
                }
                if(item == 6){
                    tope = 1;
                    cate = 3;
                    form = 1;
                    impo = impor;
                    dbHelper.updateHistoVentaDetalle1(idEstabl, tope, cate, form, cant, impo,
                            getDatePhone(), getTimePhone(), "1", "1", 2);
                    displayListView();
                }
                if(item == 7){
                    tope = 1;
                    cate = 4;
                    form = 1;
                    impo = impor;
                    dbHelper.updateHistoVentaDetalle1(idEstabl, tope, cate, form, cant, impo,
                            getDatePhone(), getTimePhone(), "1", "1", 2);
                    displayListView();
                }
                Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_LONG).show();
            }
        });
        dialogo.create();
        dialogo.show();

    }

    private void cantidad(int idCant){
        //Intent i = new Intent(this, VMovil_Evento_Establec.class);
        //i.putExtra("idEstab", idEstabl);
        //startActivity(i);

        if(idCant ==  1) {items = items01;}
        if(idCant ==  2) {items = items02;}
        if(idCant ==  3) {items = items03;}
        if(idCant ==  4) {items = items04;}
        if(idCant ==  5) {items = items05;}
        if(idCant ==  6) {items = items06;}
        if(idCant ==  7) {items = items07;}
        if(idCant ==  8) {items = items08;}
        if(idCant ==  9) {items = items09;}
        if(idCant == 10) {items = items10;}

        AlertDialog.Builder dialogox = new AlertDialog.Builder(this);
        dialogox.setTitle("CANTIDAD");
        dialogox.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogx, int itemx) {
                if(itemx == 0){
                    cant = 1;
                }
                if(itemx == 1){
                    cant = 2;
                }
                if(itemx == 2){
                    cant = 3;
                }
                if(itemx == 3){
                    cant = 4;
                }
                if(itemx == 4){
                    cant = 5;
                }
                if(itemx == 5){
                    cant = 6;
                }
                if(itemx == 6){
                    cant = 7;
                }
                if(itemx == 7){
                    cant = 8;
                }
                if(itemx == 8){
                    cant = 9;
                }
                if(itemx == 9){
                    cant = 10;
                }
                Toast.makeText(getApplicationContext(), items[itemx], Toast.LENGTH_LONG).show();
            }
        });
        dialogox.create();
        dialogox.show();

    }

    private void displayListView() {

        Cursor cursor = dbHelper.fetchAllHistoVentaDetalleByIdEst1(valIdEstabX,"1");
        //Cursor cursor = dbHelper.fetchAllHistoVentaDetalle();
        // The desired columns to be bound
        String[] columns = new String[] {
                DbAdapter_Histo_Venta_Detalle.HD_comprobante,
                DbAdapter_Histo_Venta_Detalle.HD_nom_producto,
                DbAdapter_Histo_Venta_Detalle.HD_cantidad,
                DbAdapter_Histo_Venta_Detalle.HD_fecha,
                DbAdapter_Histo_Venta_Detalle.HD_importe
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.VHV_compr,
                R.id.VHV_produ,
                R.id.VHV_canti,
                R.id.VHV_fecha,
                R.id.VHV_monto,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_histo_venta,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.VHV_listar);
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
                Double importe = cursor.getDouble(cursor.getColumnIndexOrThrow("hd_re_importe"));
                eleccion(idHVD, importe);
                cantidad(cant_ope);
                Toast.makeText(getApplicationContext(),
                        idHVD, Toast.LENGTH_SHORT).show();
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.VHV_buscar);
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
                return dbHelper.fetchHistoVentaDetalleByNameEst1(valIdEstabX, constraint.toString(),"1");
            }
        });

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
}