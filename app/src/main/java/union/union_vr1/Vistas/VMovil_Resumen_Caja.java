package union.union_vr1.Vistas;



import android.app.TabActivity;
import android.database.Cursor;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Resumen_Caja;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbGastos_Ingresos;


public class VMovil_Resumen_Caja extends TabActivity {

    TabHost tabHost;
    private DbAdapter_Stock_Agente dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private DbGastos_Ingresos dbHelperRC;
    private SimpleCursorAdapter dataAdapterRC;
    private DbAdapter_Comprob_Venta cv;
    private DbAdapter_Resumen_Caja rc;
    private DbAdapter_Comprob_Cobro compV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_resumen_caja);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        dbHelper = new DbAdapter_Stock_Agente(this);
        dbHelper.open();
        cv= new DbAdapter_Comprob_Venta(this);
        cv.open();

        cv.deleteAllComprobVenta();
        dbHelperRC = new DbGastos_Ingresos(this);
        dbHelperRC.open();
        rc= new DbAdapter_Resumen_Caja(this);
        rc.open();
        rc.deleteAllResumenCaja();

        compV = new DbAdapter_Comprob_Cobro(this);
        compV.open();
        compV.deleteAllComprobCobros();


        //dbHelper.deleteAllStockAgente();
        //dbHelper.insertSomeStockAgente();



        TabSpec spec = tabHost.newTabSpec("Tab1");

        spec.setContent(R.id.VRC_THingresos);
        spec.setIndicator("Ingresos y Gastos");
        displayListIngresosGastos();
        tabHost.addTab(spec);

        TabSpec spec2 = tabHost.newTabSpec("Tab2");
        spec2.setContent(R.id.VRC_THiventas);
        spec2.setIndicator("Inventario Ventas");
        displayListStockVenta();
        tabHost.addTab(spec2);

        TabSpec spec3 = tabHost.newTabSpec("Tab3");
        spec3.setContent(R.id.VRC_THiapt);
        spec3.setIndicator("Inventario APT");
        displayListStockApt();
        tabHost.addTab(spec3);

    }


    public void displayListStockVenta() {
        Cursor cursor = dbHelper.fetchAllStockAgenteVentas();
        String[] columns = new String[]{
                DbAdapter_Stock_Agente.ST_nombre,
                DbAdapter_Stock_Agente.ST_inicial,
                DbAdapter_Stock_Agente.ST_ventas,
                DbAdapter_Stock_Agente.ST_devoluciones,
                DbAdapter_Stock_Agente.ST_canjes
        };

        int[] to = new int[]{
                R.id.VRC_TXproducto,
                R.id.VRC_TXinicialFin,
                R.id.VRC_TXventa,
                R.id.VRC_TXdevol,
                R.id.VRC_TXcanjes
        };

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_resumen_ventas,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.VRC_listarResumenVentas);
        listView.setAdapter(dataAdapter);

    }

    public void displayListStockApt() {
        Cursor cursor = dbHelper.fetchAllStockAgenteVentas();
        String[] columns = new String[]{
                DbAdapter_Stock_Agente.ST_nombre,
                DbAdapter_Stock_Agente.ST_final,
                DbAdapter_Stock_Agente.ST_disponible,
                DbAdapter_Stock_Agente.ST_buenos,
                DbAdapter_Stock_Agente.ST_malos
        };

        int[] to = new int[]{
                R.id.VRC_TXproducto,
                R.id.VRC_TXfinal,
                R.id.VRC_TXdisponible,
                R.id.VRC_TXbuenos,
                R.id.VRC_TXmalos
        };

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_resumen_apt,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.VRC_listarResumenAPT);
        listView.setAdapter(dataAdapter);

    }

    public void displayListIngresosGastos() {

        Cursor cursor = dbHelperRC.listarTodo(1);

        String[] columns = new String[]{
                DbAdapter_Resumen_Caja.RC_descripcionComprobante,
                DbAdapter_Resumen_Caja.RC_cantidad,
                DbAdapter_Resumen_Caja.RC_vendido,
                DbAdapter_Resumen_Caja.RC_pagado,
                DbAdapter_Resumen_Caja.RC_cobrado};


        int[] to = new int[]{
                R.id.VRC_descripcion,
                R.id.VRC_cantidad,
                R.id.VRC_vendido,
                R.id.VRC_pagado,
                R.id.VRC_cobrado
        };

        dataAdapterRC = new SimpleCursorAdapter(
          this,R.layout.infor_resumen_ingresos,
                cursor,
                columns,
                to,
                0);
        ListView listView = (ListView)findViewById(R.id.VRC_listarResumenCaja);
        listView.setAdapter(dataAdapterRC);

    }

}

