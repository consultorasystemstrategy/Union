package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapterFacturas;
import union.union_vr1.Sqlite.CursorAdapterFacturas_dev;
import union.union_vr1.Sqlite.DbAdapter_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;

public class VMovil_Evento_Canjes_Dev extends TabActivity {
    private String establec;
    private AutoCompleteTextView autoComple;
    private SimpleCursorAdapter adapter;
    private DbAdapter_Stock_Agente dbHelper_Stock;
    private int idAgente;
    private int idProducto;
    private DbAdapter_Canjes_Devoluciones dbHelper_CanDev;
    private Button btn_mostrar_guias;
    private Context ctx;
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__evento__canjes__dev);
        ctx = this;
        //-------------------------------------------------------
        dbHelper_Stock = new DbAdapter_Stock_Agente(getApplication());
        dbHelper_Stock.open();
        dbHelper_CanDev = new DbAdapter_Canjes_Devoluciones(getApplication());
        dbHelper_CanDev.open();
        //------------------------------------------------------
        //DbAdaptert_Evento_Establec evenEsta = new DbAdaptert_Evento_Establec(this);
        //evenEsta.open();
        //evenEsta.deleteAllEstablecs();
        //evenEsta.insertSomeEstablecs();
        //--------------------------------------------------------

        Bundle idE = getIntent().getExtras();
        establec = idE.getString("idEstabX");
        idAgente = idE.getInt("idAgente");
        Toast.makeText(getApplicationContext(), "" + establec + idAgente, Toast.LENGTH_SHORT).show();
        autoComple = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autoComplete();
        //

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab1");
        spec.setContent(R.id.tab1_can);
        spec.setIndicator("Canjes");
        listar_Facturas_can();
        tabHost.addTab(spec);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab2");
        spec2.setContent(R.id.tab2_dev);
        spec2.setIndicator("Devoluciones");
        listar_Facturas_dev();
        tabHost.addTab(spec2);


        //


    }

    private void autoComplete() {
        Cursor cr = dbHelper_Stock.listarProductos();
        cr.moveToFirst();
        String[] colum = new String[]{
                cr.getColumnName(1)};
        int[] to = new int[]{
                R.id.autcomcan_dev
        };
        adapter = new SimpleCursorAdapter(this, R.layout.infor_canjes_dev, cr, colum, to, 0);
        autoComple.setAdapter(adapter);
        autoComple.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                adapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                Cursor cr = dbHelper_Stock.listarbyIdProducto(charSequence.toString());
                return cr;
            }
        });
        autoComple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setBackgroundColor(0x00000000);
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                String nom = cursor.getString(1);
                idProducto = cursor.getInt(0);
                autoComple.setText(nom);
                Intent faCanjeDev = new Intent(getApplicationContext(), VMovil_Facturas_Canjes_Dev.class);
                faCanjeDev.putExtra("idAgente", idAgente);
                faCanjeDev.putExtra("idEstablec", establec);
                faCanjeDev.putExtra("idProducto", idProducto);
                faCanjeDev.putExtra("nomProducto", nom);
                startActivity(faCanjeDev);
                finish();
            }
        });


    }

    private void listar_Facturas_can() {
        Cursor cr = dbHelper_CanDev.obtener_facturas_can(establec);
        final ListView lista_facturas = (ListView) findViewById(R.id.guias_can_dev);

            CursorAdapterFacturas adapter = new CursorAdapterFacturas(getApplicationContext(), cr);
            lista_facturas.setAdapter(adapter);
            btn_mostrar_guias = (Button) findViewById(R.id.button_mostrar_guias);
            btn_mostrar_guias.setVisibility(View.VISIBLE);
            btn_mostrar_guias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(getApplicationContext(), mostrar_can_dev_facturas.class);
                    i.putExtra("idEstablec", establec);
                    i.putExtra("idAgente", idAgente);
                    startActivity(i);
                   finish();


                }
            });
        lista_facturas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cr = (Cursor)adapterView.getItemAtPosition(i);
                cr.moveToPosition(i);
                String idHistoVenta = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_hventadet));
                String idProducto = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_producto));
                String idHistoDetalle = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_detalle));
                int cantProductos = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_cantidad_ope));

                select("1","Canje",idHistoVenta,idProducto,idHistoDetalle,cantProductos);
                return false;
            }
        });



    }
    private void select (final String idOperacion,String Operacion, final String idHistoVenta, final String idProducto, final String idHistoDetalle, final int cantProductos ){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Anular "+Operacion+" ");
            AlertDialog.Builder builder = alertDialogBuilder
                    .setMessage("¿Desea Anular?")
                    .setCancelable(false)
                    .setPositiveButton("Quitar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            boolean estado =false;
                            if (idOperacion.equals("1")) {//cannje
                                estado =  dbHelper_CanDev.cancelarCabiosByIdCanjes(idProducto,cantProductos,idHistoVenta,idHistoDetalle);
                                if(estado){
                                    Toast.makeText(getApplicationContext(),"Quitado de la Lista",Toast.LENGTH_SHORT).show();
                                    regresar();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Ocurrio un Error",Toast.LENGTH_SHORT).show();
                                }
                            }
                            if(idOperacion.equals("2")){//devoluciones
                                estado =  dbHelper_CanDev.cancelarCabiosByIdDevoluciones(idProducto,cantProductos,idHistoVenta,idHistoDetalle);
                                if(estado){
                                    Toast.makeText(getApplicationContext(),"Quitado de la Lista",Toast.LENGTH_SHORT).show();
                                    regresar();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Ocurrio un Error",Toast.LENGTH_SHORT).show();
                                }

                            }

                        }

                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

    private void listar_Facturas_dev() {
        Cursor cr = dbHelper_CanDev.obtener_facturas_dev(establec);
        final ListView lista_facturas = (ListView) findViewById(R.id.guias_can_2);

            CursorAdapterFacturas_dev adapter = new CursorAdapterFacturas_dev(getApplicationContext(), cr);
            lista_facturas.setAdapter(adapter);
            btn_mostrar_guias = (Button) findViewById(R.id.button_mostrar_guias);
            btn_mostrar_guias.setVisibility(View.VISIBLE);
            btn_mostrar_guias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    back();
                }
            });
        lista_facturas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cr = (Cursor)adapterView.getItemAtPosition(i);
                cr.moveToPosition(i);
                String idHistoVenta = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_hventadet));
                String idProducto = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_producto));
                String idHistoDetalle = cr.getString(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_detalle));
                int cantProductos = cr.getInt(cr.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_cantidad_ope));
                select("2", "Devolucion",idHistoVenta,idProducto,idHistoDetalle,cantProductos);
                return false;
            }
        });

    }
    public void back(){
        Intent i = new Intent(getApplicationContext(), mostrar_can_dev_facturas.class);
        i.putExtra("idEstablec", establec);
        i.putExtra("idAgente", idAgente);
        this.startActivity(i);
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vmovil__facturas__canjes__dev, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.retrocederActivity:
             regresar();
                break;
        }
        return true;
    }

    private void regresar(){
        Intent i = new Intent(ctx, VMovil_Evento_Establec.class);
        i.putExtra("idEstab", establec);
        i.putExtra("idAgente", idAgente);
        this.startActivity(i);
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                regresar();

                return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}


