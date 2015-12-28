package union.union_vr1.Vistas;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.AsyncTask.ExportCanjesDevoluciones;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.CursorAdapterFacturas;
import union.union_vr1.Sqlite.DBAdapter_Temp_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;

public class VMovil_Operacion_Canjes_Devoluciones extends TabActivity {
    private DBAdapter_Temp_Canjes_Devoluciones dbAdapter_temp_canjes_devoluciones;
    private DbAdapter_Temp_Session session;
    private DbAdapter_Histo_Venta_Detalle dbAdapter_histo_venta_detalle;
    private DbAdapter_Stock_Agente dbHelper_Stock;
    private AutoCompleteTextView autoComple;
    private TabHost tabHost;
    private ListView listViewCanjes;
    private ListView listViewDevoluviones;
    private TextView textViewTotalCanje;
    private TextView textViewBaseImponibleCanje;
    private TextView textViewigvCanje;
    private TextView textViewTotalDevolucion;
    private TextView textViewBaseDevolucion;
    private TextView textViewigvDevolucion;
    private TextView textViewNombreClienteCanje;
    private TextView textViewFechaEmisionCanje;
    private TextView textViewDocumentoCanje;
    private TextView textViewNombreClienteDevolucion;
    private TextView textViewFechaEmisionDevolucion;
    private TextView textViewDocumentoDevolucion;
    private Button buttonSave;
    private SimpleCursorAdapter adapter;
    private String establec;
    private int idAgente;
    private int idProducto;
    private int liquidacion;
    private String [] datosHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__operacion__canjes__devoluciones);
        session = new DbAdapter_Temp_Session(this);
        session.open();
        dbAdapter_temp_canjes_devoluciones = new DBAdapter_Temp_Canjes_Devoluciones(this);
        dbAdapter_temp_canjes_devoluciones.open();
        liquidacion = session.fetchVarible(3);
        dbHelper_Stock = new DbAdapter_Stock_Agente(this);
        dbHelper_Stock.open();
        dbAdapter_histo_venta_detalle = new DbAdapter_Histo_Venta_Detalle(this);
        dbAdapter_histo_venta_detalle.open();
        Bundle idE = getIntent().getExtras();
        establec = idE.getString("idEstabX");
        idAgente = idE.getInt("idAgente");
        autoComple = (AutoCompleteTextView) findViewById(R.id.autocompleteBuscar);
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        listViewCanjes = (ListView) findViewById(R.id.guias_canjes);
        listViewDevoluviones = (ListView) findViewById(R.id.guias_devoluciones);
        textViewTotalCanje = (TextView) findViewById(R.id.TextViewTotalCanje);
        textViewBaseImponibleCanje = (TextView) findViewById(R.id.TextViewBaseCanje);
        textViewigvCanje = (TextView) findViewById(R.id.TextViewIgvCanje);
        textViewTotalDevolucion = (TextView) findViewById(R.id.TextViewTotalDevolucion);
        textViewBaseDevolucion = (TextView) findViewById(R.id.TextViewBaseDevolucion);
        textViewigvDevolucion = (TextView) findViewById(R.id.TextViewIgvDevolucion);
        textViewTotalCanje = (TextView) findViewById(R.id.TextViewTotalCanje);
        textViewNombreClienteCanje = (TextView) findViewById(R.id.textClienteCanje);
        textViewFechaEmisionCanje = (TextView) findViewById(R.id.textfechaEmisionCanje);
        textViewDocumentoCanje = (TextView) findViewById(R.id.textDocumentoCanje);
        textViewNombreClienteDevolucion = (TextView) findViewById(R.id.textClienteDevolucion);
        textViewFechaEmisionDevolucion = (TextView) findViewById(R.id.textfechaEmisionDevolucion);
        textViewDocumentoDevolucion = (TextView) findViewById(R.id.textDocumentoDevolucion);
        buttonSave = (Button)findViewById(R.id.button_save);
        autoComplete();

        datosHeader = dbAdapter_temp_canjes_devoluciones.getInforHeader(establec);

        TabHost.TabSpec specanjes = tabHost.newTabSpec("Tab1");
        specanjes.setContent(R.id.tab1_canjes);
        specanjes.setIndicator("Canjes");
        listar_canjes();
        tabHost.addTab(specanjes);

        TabHost.TabSpec specdevoluciones = tabHost.newTabSpec("Tab2");
        specdevoluciones.setContent(R.id.tab2_devoluciones);
        specdevoluciones.setIndicator("Devoluciones");
        listar_devoluciones();
        tabHost.addTab(specdevoluciones);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbAdapter_temp_canjes_devoluciones.listarCanjes(establec).moveToFirst() || dbAdapter_temp_canjes_devoluciones.listarDevoluciones(establec).moveToFirst()){
                    setMessageforSave();
                }else {
                    Toast.makeText(getApplicationContext(),"No tiene Canjes ni Devoluciones",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void setMessageforSave(){

        final AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("¿Esta seguro?");
        dialogo.setCancelable(false);
        dialogo.setMessage("Guardara Canjes y Devoluciones");
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int liquidacion) {
                dialogInterface.cancel();
            }
        });
        dialogo.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int liquidacion) {
                if(conectadoWifi() || conectadoRedMovil()){
                    transOperaciones();
                }else{
                    saveAndExportLast();
                }

            }
        });
        dialogo.create();
        dialogo.show();

    }
    private void saveAndExportLast(){

        Cursor cursor = dbAdapter_temp_canjes_devoluciones.getAllOperacionByEstablec(establec,Constants._CREADO+"");
        while(cursor.moveToNext()){
            dbAdapter_temp_canjes_devoluciones.updateEstadoExportAfter(cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_canjes_devoluciones)));
        }


        final AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("No estas Conectado");
        dialogo.setCancelable(false);
        dialogo.setMessage("Sin embargo se exportaran mas adelante.");
        dialogo.setPositiveButton("Esta Bien", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int liquidacion) {
                startActivity(new Intent(getApplicationContext(), VMovil_Evento_Establec.class));
                finish();
            }
        });
        dialogo.create();
        dialogo.show();

    }
    private void transOperaciones(){
       /* Cursor getAllOperacion = dbAdapter_temp_canjes_devoluciones.getAllOperacionHeader(getDatePhone(),establec);
        while(getAllOperacion.moveToNext()){
            Log.d("ITEM 1 DEVOLUCIONES", getAllOperacion.getString(getAllOperacion.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_nom_producto)));
        }*/
        ExportCanjesDevoluciones exportCanjesDevoluciones = new ExportCanjesDevoluciones(getApplicationContext(),this);
        exportCanjesDevoluciones.execute(getDatePhone(),establec, Constants._CREADO+"");

        startActivity(new Intent(getApplicationContext(), VMovil_Evento_Establec.class));
        finish();

    }

    private void listar_canjes() {
        //Listar Informacion Cliente
        textViewNombreClienteCanje.setText(datosHeader[0]);
        textViewFechaEmisionCanje.setText(getDatePhone());
        textViewDocumentoCanje.setText(datosHeader[1]);
        mostrarItemsCanjes();
        listarTotalCanjes();


    }
    protected Boolean conectadoWifi(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void mostrarItemsCanjes() {


        //listar Canjes
        final Cursor cursor = dbAdapter_temp_canjes_devoluciones.listarCanjes(establec);
        CursorAdapterFacturas adapter = new CursorAdapterFacturas(getApplicationContext(), cursor);
        listViewCanjes.setAdapter(adapter);
        listViewCanjes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cr = (Cursor) listViewCanjes.getItemAtPosition(position);
                int cantidad = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_cantidad));
                int idProducto = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_producto));
                int idforDelete = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_canjes_devoluciones));
                int liquidacion =  session.fetchVarible(3);
                int estado = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_estado));
                String iddetale = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_detalle));
                Log.d("DATOS RESTART",cantidad+"-"+idProducto+"-"+idforDelete);
                setMessageForDeleteById(idforDelete,2,cantidad,idProducto,liquidacion,estado,iddetale);
                return false;
            }
        });

    }

    private void listarTotalCanjes() {
        Double[] infoFooter = dbAdapter_temp_canjes_devoluciones.getInfoCanje(establec);
        textViewTotalCanje.setText("" + infoFooter[0]);
        textViewBaseImponibleCanje.setText("" + infoFooter[1]);
        textViewigvCanje.setText("" + infoFooter[2]);
    }

    private void listar_devoluciones() {

        textViewNombreClienteDevolucion.setText(datosHeader[0]);
        textViewFechaEmisionDevolucion.setText(getDatePhone());
        textViewDocumentoDevolucion.setText(datosHeader[1]);
        mostrarItemsDevoluciones();
        listarTotalDevoluciones();


    }
    private  void mostrarItemsDevoluciones(){
        //listar devoluciones
        Cursor cursor = dbAdapter_temp_canjes_devoluciones.listarDevoluciones(establec);
        CursorAdapterFacturas adapter = new CursorAdapterFacturas(getApplicationContext(), cursor);
        listViewDevoluviones.setAdapter(adapter);
        listViewDevoluviones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cr = (Cursor) listViewDevoluviones.getItemAtPosition(position);
                int cantidad = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_cantidad));
                int idProducto = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_producto));
                int idforDelete = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_canjes_devoluciones));
                int liquidacion = session.fetchVarible(3);
                int estado = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_estado));
                String iddetale = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Temp_Canjes_Devoluciones.temp_id_detalle));
                Log.d("DATOS RESTART",cantidad+"-"+idProducto+"-"+idforDelete);
                setMessageForDeleteById(idforDelete,1,cantidad,idProducto,liquidacion,estado,iddetale);
                return false;
            }
        });
    }
    private void listarTotalDevoluciones() {
        Double[] infoFooter = dbAdapter_temp_canjes_devoluciones.getInfoDevoluciones(establec);
        textViewTotalDevolucion.setText("" + infoFooter[0]);
        textViewBaseDevolucion.setText("" + infoFooter[1]);
        textViewigvDevolucion.setText("" + infoFooter[2]);
    }

    private void autoComplete() {

        Cursor cr = dbHelper_Stock.listarProductosAndStock(liquidacion);
        cr.moveToFirst();


        String[] columnasStock = new String[]{
                cr.getColumnName(1),
                cr.getColumnName(2),


        };

        // the XML defined views which the data will be bound to
        int[] toStock = new int[]{
                R.id.VCAP_producto,
                R.id.VCPA_stock,

        };

        adapter = new SimpleCursorAdapter(this,
                R.layout.infor_venta_cabecera_productos,
                cr,
                columnasStock,
                toStock,
                0);


        autoComple.setAdapter(adapter);
        autoComple.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                Log.d("ON TEXTCHANGED", "" + charSequence.toString() + "," + i + "," + i2 + "," + i3);
                if (i == 0 && i2 == 0 && i3 >= 10) {
                    Log.d("ON TEXTCHANGED", "HA ESCANEADO UN PRODUCTO");


                    Cursor cursor = dbHelper_Stock.listarbyIdProductoAndStock(charSequence.toString(), liquidacion);

                    String nom = "";
                    String valorUnidad = "";
                    if (cursor.getCount() >= 1) {
                        cursor.moveToFirst();

                        nom = cursor.getString(1);
                        idProducto = cursor.getInt(0);
                        valorUnidad = cursor.getString(3);
                    }


                    autoComple.setText(nom);
                    Intent faCanjeDev = new Intent(getApplicationContext(), VMovil_Facturas_Canjes_Dev.class);
                    faCanjeDev.putExtra("idAgente", idAgente);
                    faCanjeDev.putExtra("idEstablec", establec);
                    faCanjeDev.putExtra("idProducto", idProducto);
                    faCanjeDev.putExtra("nomProducto", nom);
                    faCanjeDev.putExtra("valorUnidad", valorUnidad);
                    Log.e("idProducto", "" + idProducto + "-" + nom);
                    startActivity(faCanjeDev);
                    finish();


                } else {
                    adapter.getFilter().filter(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                Log.e("Error stock lista", " " + liquidacion);
                Cursor cr = dbHelper_Stock.listarbyIdProductoAndStock(charSequence.toString(), liquidacion);
                return cr;
            }
        });


        autoComple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //adapterView.setBackgroundColor(0x00000000);


                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                String nom = cursor.getString(1);
                idProducto = cursor.getInt(0);
                String valorUnidad = cursor.getString(3);
                autoComple.setText(nom);
                Intent faCanjeDev = new Intent(getApplicationContext(), VMovil_Facturas_Canjes_Dev.class);
                faCanjeDev.putExtra("idAgente", idAgente);
                faCanjeDev.putExtra("idEstablec", establec);
                faCanjeDev.putExtra("idProducto", idProducto);
                faCanjeDev.putExtra("nomProducto", nom);
                faCanjeDev.putExtra("valorUnidad", valorUnidad);
                Log.e("idProducto", "" + idProducto + "-" + nom);
                startActivity(faCanjeDev);
                finish();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vmovil__operacion__canjes__devoluciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    @Override
    public void onBackPressed() {

        if(dbAdapter_temp_canjes_devoluciones.listarDevoluciones(establec).moveToFirst() || dbAdapter_temp_canjes_devoluciones.listarCanjes(establec).moveToFirst()){
            setMessageForDelete();
        }else{
            startActivity(new Intent(getApplicationContext(),VMovil_Evento_Establec.class));
            finish();
        }


        //super.onBackPressed();
    }

    private void setMessageForDeleteById(final int id,final int opera,final int cantidad, final  int idProducto, final int liquidacion12, final int estadoOperacion, final String idDetalle) {

        final AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("¿Esta seguro de eliminar?");
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int liquidacion) {
                dialogInterface.cancel();
            }
        });
        dialogo.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int liquidacion) {

                int estado = dbAdapter_temp_canjes_devoluciones.deleteCanjesDevoluciones(id);
                Log.d("LIQUIDACION",""+liquidacion12);
                if (estado > 0) {
                    if(opera ==2){
                        int e =dbHelper_Stock.restartstockCanjes(cantidad,idProducto+"",liquidacion12+"");
                       if(estadoOperacion==4){
                           int c = dbAdapter_histo_venta_detalle.restarupdateHistoVentaDetalleCanje(idDetalle,cantidad);
                       }
                        Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                        Log.d("CANJES",cantidad+"-"+idProducto+"-"+e);
                    }else{

                        int e= dbHelper_Stock.restartstockDevoluciones(cantidad,idProducto+"",liquidacion12+"");
                        if(estadoOperacion==4){
                            int c = dbAdapter_histo_venta_detalle.restarupdateHistoVentaDetalleCanje(idDetalle,cantidad);
                        }
                        Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                        Log.d("DEVOLUCIONES",cantidad+"-"+idProducto+"-"+e);
                    }

                    listar_canjes();
                    listar_devoluciones();
                    autoComplete();
                } else {
                    Toast.makeText(getApplicationContext(), "Ocurrio un error", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialogo.create();
        dialogo.show();
    }

    private void setMessageForDelete() {
        final AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("¿Esta Seguro?");
        dialogo.setMessage("Al retroceder eliminara los canjes y devoluciones.");
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int liquidacion) {
                dialogInterface.cancel();
            }
        });
        dialogo.setPositiveButton("Continuar e Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int liquidacion) {
                int estado = dbAdapter_temp_canjes_devoluciones.truncateCanjesDevoluciones();
                if (estado > 0) {
                    Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), VMovil_Evento_Establec.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Ocurrio un error", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), VMovil_Evento_Establec.class));
                    finish();
                }

            }
        });
        dialogo.create();
        dialogo.show();
    }


}
