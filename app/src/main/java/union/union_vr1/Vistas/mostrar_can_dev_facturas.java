package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapterFacturas;
import union.union_vr1.Sqlite.CursorAdapterFacturas_Canjes;
import union.union_vr1.Sqlite.CursorAdapterFacturas_Canjes_Dev;
import union.union_vr1.Sqlite.CursorAdapter_Facturas_Canjes_Dev;
import union.union_vr1.Sqlite.DbAdapter_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.VMovil_BluetoothImprimir;

public class mostrar_can_dev_facturas extends TabActivity {
    private String idEstablec;
    private int idAgente;
    private TabHost tabHost;
    private DbAdapter_Canjes_Devoluciones dbHelper_CanDev;
    DbAdapter_Temp_Session session ;
    private TextView textViewFooterText;
    private TextView textViewFooterTotal;
    private TextView textViewHeader;
    private View header;
    private View footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infor_mostrar_facturas_can_dev);

        dbHelper_CanDev = new DbAdapter_Canjes_Devoluciones(getApplication());
        dbHelper_CanDev.open();

        session = new DbAdapter_Temp_Session(this);
        session.open();

        Bundle bl = getIntent().getExtras();
        idEstablec = bl.getString("idEstablec");
        idAgente = bl.getInt("idAgente");


        Button btn_guardar = (Button) findViewById(R.id.btn_fac_guardar);
        Button btn_cancelar = (Button) findViewById(R.id.btn_fac_cancel);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab1");
        spec.setContent(R.id.tab_Canjes);
        spec.setIndicator("Canjes");
        displayListCanjes();
        tabHost.addTab(spec);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab2");
        spec2.setContent(R.id.tab_Devoluciones);
        spec2.setIndicator("Devoluciones");
        displayListDevoluciones();
        tabHost.addTab(spec2);

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idTab = tabHost.getCurrentTab();
                if (idTab == 0) {
                    dialog_save("Canjes", 1,idEstablec);


                }
                if (idTab == 1) {
                    dialog_save("Devoluciones", 2,idEstablec);
                }

            }
        });
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idTab = tabHost.getCurrentTab();
                if (idTab == 0) {
                    dialog_cancel("Canjes", 1, "st_in_canjes");

                }
                if (idTab == 1) {
                    dialog_cancel("Devoluciones", 2, "st_in_devoluciones");
                }
            }
        });

    }


    private void displayListCanjes() {
        header = getLayoutInflater().inflate(R.layout.header_canjes_devoluciones, null);

         textViewHeader = (TextView) header.findViewById(R.id.textViewHeaderText);

        footer = getLayoutInflater().inflate(R.layout.footer_canjes_devoluciones,null);

        textViewFooterText = (TextView)footer.findViewById(R.id.textViewFooterText);
        textViewFooterTotal = (TextView)footer.findViewById(R.id.textViewFooterTotal);

        String textoView = "";
        Cursor cabecera = dbHelper_CanDev.obtener_cabecera(idEstablec);

        if (cabecera.moveToFirst()) {
            String cliente = cabecera.getString(cabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_nom_cliente));
            String documento = cabecera.getString(cabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_doc_cliente));
            String texto = "Fecha de Emision: " + getDatePhone() + "," +
                    "\nCliente: " + cliente + "," +
                    "\nDocumento: " + documento + "";
            textoView = texto;

        } else {
        }

        Cursor cr = dbHelper_CanDev.obtener_facturas_canjes(1, idEstablec);
        ListView lista_facturas = (ListView) findViewById(R.id.listViewCanjes);
        Cursor precio = dbHelper_CanDev.obtener_igv(1, idEstablec);
        String textFooter = "";


        /*
        if (precio.moveToFirst()) {

            DecimalFormat df = new DecimalFormat("#.00");

            textViewFooterText.setText("Total :\n" +
                    "Base imponible :\n" +
                    "IGV :");.

            textViewFooterTotal.setText(" S/. "+precio.getString(0)+"\n" +
                    "S/. "+precio.getString(1)+ "\n" +
                    "S/. "+precio.getString(2));

        }
        */
        CursorAdapterFacturas_Canjes adapter_facturas = null;
        if (cr.moveToFirst()) {


            textViewHeader.setText(textoView);
            lista_facturas.addHeaderView(header);
            adapter_facturas = new CursorAdapterFacturas_Canjes(getApplicationContext(), cr);
            lista_facturas.setAdapter(adapter_facturas);

        } else {

        }

        Double totalFooter = 0.0;
        Double base_imponibleFooter = 0.0;
        Double igvFooter = 0.0;
        if (adapter_facturas!=null) {
            Cursor cursorTemp = adapter_facturas.getCursor();

            for (cursorTemp.moveToFirst(); !cursorTemp.isAfterLast(); cursorTemp.moveToNext()) {

                Double importe = cursorTemp.getDouble(18);
                totalFooter += importe;
            }
            base_imponibleFooter = totalFooter / 1.18;
            igvFooter = base_imponibleFooter * 0.18;
        }
            if (precio.moveToFirst()) {

                DecimalFormat df = new DecimalFormat("#.00");

                textViewFooterText.setText("Total :\n" +
                        "Base imponible :\n" +
                        "IGV :");

                textViewFooterTotal.setText(" S/. " + df.format(totalFooter) + "\n" +
                        "S/. " + df.format(base_imponibleFooter) + "\n" +
                        "S/. " + df.format(igvFooter));

            }


        lista_facturas.addFooterView(footer);

    }

    private void displayListDevoluciones() {

        header = getLayoutInflater().inflate(R.layout.header_canjes_devoluciones, null);

        textViewHeader = (TextView) header.findViewById(R.id.textViewHeaderText);

        footer = getLayoutInflater().inflate(R.layout.footer_canjes_devoluciones,null);

        textViewFooterText = (TextView)footer.findViewById(R.id.textViewFooterText);
        textViewFooterTotal = (TextView)footer.findViewById(R.id.textViewFooterTotal);


        Cursor cabecera = dbHelper_CanDev.obtener_cabecera(idEstablec);
        String textoAdpater = "";
        if (cabecera.moveToFirst()) {
            //obtener datos del cliente
            String cliente = cabecera.getString(cabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_nom_cliente));
            String documento = cabecera.getString(cabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_doc_cliente));
            textoAdpater = "Fecha de Emision: " + getDatePhone() + "," +
                    "\nCliente: " + cliente + "," +
                    "\nDocumento: " + documento + "";


        } else {
        }

        Cursor cr = dbHelper_CanDev.obtener_facturas_dev(2, idEstablec);
        ListView lista_facturas = (ListView) findViewById(R.id.listViewDevoluciones);
        Cursor precio = dbHelper_CanDev.obtener_igv_dev(2, idEstablec);
        String textFooter = "";
        if (precio.moveToFirst()) {


            DecimalFormat df = new DecimalFormat("#.00");

            textViewFooterText.setText("Total :\n" +
                    "Base imponible :\n" +
                    "IGV :");

            Double base = precio.getDouble(1);
            Double igv = precio.getDouble(2);

            textViewFooterTotal.setText(" S/. "+precio.getString(0)+"\n" +
                    "S/. "+df.format(base)+ "\n" +
                    "S/. "+df.format(igv));

        }

        lista_facturas.addFooterView(footer);
        if (cr.moveToFirst()) {
            textViewHeader.setText(textoAdpater);
            lista_facturas.addHeaderView(header);
            CursorAdapterFacturas_Canjes_Dev adapter_facturas = new CursorAdapterFacturas_Canjes_Dev(getApplicationContext(), cr);
            lista_facturas.setAdapter(adapter_facturas);


        } else {
        }

    }

    public void dialog_save(final String op, final int operacion, final String establecimiento) {
        final int liquidacion = session.fetchVarible(3);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Seguro de Guardar");
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Operacion: " + op + "")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String idGuia = dbHelper_CanDev.guardarCabecera(idAgente,establecimiento);
                        if (operacion == 2) {
                            boolean estado = dbHelper_CanDev.guardarCambios_dev(idGuia, idEstablec,liquidacion);
                            if (estado) {
                               // Toast.makeText(getApplicationContext(),""+idGuia,Toast.LENGTH_SHORT).show();
                                exito();
                            } else {
                                Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (operacion == 1) {
                            boolean estado = dbHelper_CanDev.guardarCambios(operacion, idEstablec,idGuia,liquidacion);
                            if (estado) {
                               // Toast.makeText(getApplicationContext(),""+idGuia,Toast.LENGTH_SHORT).show();
                                exito();
                            } else {
                                Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_SHORT).show();
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

    public void dialog_cancel(final String op, final int tipo, final String columna) {
        final int liquidacion = session.fetchVarible(3);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Seguro de Borrar Los Cambios");
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Operacion: " + op + "")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (tipo == 1) {
                            boolean estado = dbHelper_CanDev.cancelarCambios(tipo, idEstablec, columna,liquidacion);
                            if (estado) {

                                exito();
                            } else {
                                Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (tipo == 2) {
                            boolean estado = dbHelper_CanDev.cancelarCambios_dev(tipo, idEstablec, columna,liquidacion);
                            if (estado) {
                                exito();
                            } else {
                                Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_SHORT).show();
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


    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:

                dialog_regresar();

                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                dialog_regresar();

                return true;

        }
        return super.onKeyDown(keyCode, event);
    }


    public void exito() {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Exito.!");

        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Guardado Correctamente")
                .setCancelable(false)
                .setPositiveButton("Regresar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent in = new Intent(getApplicationContext(), VMovil_Evento_Canjes_Dev.class);
                        in.putExtra("idEstabX", idEstablec);
                        in.putExtra("idAgente", idAgente);
                        startActivity(in);
                        finish();
                    }

                });


        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    public void dialog_regresar() {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Debe guardar");

        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Debe guardar los Canjes/Devoluciones.")
                .setCancelable(false)
                .setPositiveButton("Regresar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent in = new Intent(getApplicationContext(), VMovil_Menu_Establec.class);
                      //  in.putExtra("idEstabX", idEstablec);
                       // in.putExtra("idAgente", idAgente);
                        startActivity(in);
                        finish();
                    }

                })
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mostrar_can_dev_facturas, menu);
        return true;
    }
    private String cabecera(){
       String textoImpresionCabecera  = ".\n"
                +"    UNIVERSIDAD PERUANA UNION   \n"
                +"     Cent.aplc. Prod. Union     \n"
                +"   C. Central Km 19 Villa Union \n"
                +" Lurigancho-Chosica Fax: 6186311\n"
                +"      Telf: 6186309-6186310     \n"
                +" Casilla 3564, Lima 1, LIMA PERU\n"
                +"         RUC: 20138122256       \n"
                +"--------------------------------\n"
                +"        NOTA DE CREDITO         \n"
                +"--------------------------------\n";
        return textoImpresionCabecera;
    }
    private String detalle(){
        Cursor cabecera = dbHelper_CanDev.obtener_cabecera(idEstablec);
        cabecera.moveToFirst();
        String textoImpresion = "\n"
                +"Nota de Credito Nro: "+cabecera.getString(7)+"\n"
                +"Fecha: "+getDatePhone()+"       \n"
                +"Cliente: "+cabecera.getString(6)+"\n";
        return textoImpresion;
    }
    private String text(){

        Cursor cr = dbHelper_CanDev.obtener_facturas_dev2(2,idEstablec);
        String texto = "";
        Log.e("errores","0");

        while(cr.moveToNext()){
            Log.e("Errores", "" + "" + cr.getString(cr.getColumnIndexOrThrow("hd_in_cantidad_ope_dev")) + " " + cr.getString(cr.getColumnIndexOrThrow("hd_te_nom_producto")) + "\n");
            texto=texto+("" + cr.getString(cr.getColumnIndexOrThrow("hd_in_cantidad_ope_dev")) + " " + cr.getString(cr.getColumnIndexOrThrow("hd_te_nom_producto")) + "\n");

        }

        return texto;
    }
    private String rigth(){

        DecimalFormat df= new DecimalFormat("#0.00");

        Cursor cr = dbHelper_CanDev.obtener_facturas_dev2(2,idEstablec);

        String texto = "";

        while(cr.moveToNext()){
            Double total=cr.getDouble(cr.getColumnIndexOrThrow("hd_re_importe_ope_dev"));
           // Log.e("Errores", "" + "" + cr.getString(25) + " " + cr.getString(9) + "\n");
            texto=texto+("" +df.format(total)+ "\n");

        }

        return texto;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idTab=tabHost.getCurrentTab();
        //item.setVisible(false);
        switch (item.getItemId()) {
            case R.id.printCanjesDev:
                if(idTab==1){
                Intent  i = new Intent(getApplicationContext(),VMovil_BluetoothImprimir.class);
                    i.putExtra("textoImpresion",cabecera());
                    i.putExtra("textoImpresionCabecera", cabecera());
                    i.putExtra("textoVentaImpresion", detalle());
                    i.putExtra("textoImpresionContenidoLeft", text());
                    i.putExtra("textoImpresionContenidoRight", rigth());

                    startActivity(i);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(),"Usted no Puede Imprimir Canjes",Toast.LENGTH_SHORT).show();

                }
                break;
        }
        return true;
    }

}

