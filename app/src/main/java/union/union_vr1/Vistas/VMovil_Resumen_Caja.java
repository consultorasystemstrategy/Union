package union.union_vr1.Vistas;


import android.app.TabActivity;
import android.database.Cursor;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Resumen_Caja;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.MyApplication;


public class VMovil_Resumen_Caja extends TabActivity {

    TabHost tabHost;
    private DbAdapter_Stock_Agente dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private DbAdapter_Resumen_Caja dbHelperRC;
    private SimpleCursorAdapter dataAdapterRC;
    private DbGastos_Ingresos dbHelperGastosIngr;
    private DbAdapter_Agente dbAdapter_agente;
    private DbAdapter_Temp_Session session;
    private int idLiquidacion;
    private int idAgente;
    private String nombreAgente =  "Agente 001";
    private TextView textViewFecha, textViewLiquidacion, textViewAgente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_resumen_caja);

        session = new DbAdapter_Temp_Session(this);
        session.open();

        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();




        idAgente = session.fetchVarible(1);
        idLiquidacion = session.fetchVarible(3);

        Cursor cursor = dbAdapter_agente.fetchAgentesByIds(idAgente, idLiquidacion);
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            nombreAgente = cursor.getString(cursor.getColumnIndexOrThrow(dbAdapter_agente.AG_nombre_agente));
        }


        textViewAgente = (TextView) findViewById(R.id.textViewEjecutivo);
        textViewLiquidacion = (TextView) findViewById(R.id.textViewLiquidacionNumero);
        textViewFecha = (TextView) findViewById(R.id.textViewFecha);

        textViewAgente.setText("Ejecutivo : "+nombreAgente);
        textViewFecha.setText("Fecha : "+ getDatePhone());
        textViewLiquidacion.setText("Liquidación Nro : "+idLiquidacion);


        dbHelperGastosIngr =  new DbGastos_Ingresos(this);
        dbHelperGastosIngr.open();

        tabHost = (TabHost) findViewById(android.R.id.tabhost);

        dbHelper = new DbAdapter_Stock_Agente(this);
        dbHelper.open();


        dbHelperRC = new DbAdapter_Resumen_Caja(this);
        dbHelperRC.open();
        /*
        dbHelperRC.deleteAllResumenCaja();
        dbHelperRC.insertSomeResumenCaja();


        dbHelper.deleteAllStockAgente();
        dbHelper.insertSomeStockAgente();
*/

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


        Cursor cursor = dbHelperGastosIngr.listarIngresosGastos(idLiquidacion);

        String[] columns = new String[]{
                "comprobante",
                "n",
                "emitidas",
                "pagado",
                "cobrado"};


        int[] to = new int[]{
                R.id.VRC_descripcion,
                R.id.VRC_cantidad,
                R.id.VRC_vendido,
                R.id.VRC_pagado,
                R.id.VRC_cobrado
        };

        dataAdapterRC = new SimpleCursorAdapter(
                this, R.layout.infor_resumen_ingresos,
                cursor,
                columns,
                to,
                0);
        ListView listView = (ListView) findViewById(R.id.VRC_listarResumenCaja);
        listView.setAdapter(dataAdapterRC);
    }
    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }
}

