package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import union.union_vr1.R;
import union.union_vr1.Servicios.ServiceExport;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.CursorAdapterAllCV;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Utils.Utils;
import union.union_vr1.VMovil_BluetoothImprimir;

public class MantenimientoExportacion extends Activity {



    DbAdapter_Temp_Session session;
    private int idLiquidacion;
    private int idComprobante;
    private DbAdapter_Comprob_Venta dbAdapter_comprob_venta;
    private DbAdapter_Comprob_Venta_Detalle dbAdapter_comprob_venta_detalle;
    private DbAdapter_Stock_Agente dbAdapter_stock_agente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento_exportacion);

        session = new DbAdapter_Temp_Session(this);
        session.open();
        dbAdapter_comprob_venta = new DbAdapter_Comprob_Venta(getActivity());
        dbAdapter_comprob_venta.open();
        dbAdapter_comprob_venta_detalle = new DbAdapter_Comprob_Venta_Detalle(getActivity());
        dbAdapter_comprob_venta_detalle.open();
        dbAdapter_stock_agente = new DbAdapter_Stock_Agente(getActivity());
        dbAdapter_stock_agente.open();


        idLiquidacion = session.fetchVarible(3);

        listarComprobantesVentas();
    }

    private void listarComprobantesVentas(){
        CursorAdapterAllCV cursorAdapterComprobanteVenta;
        Cursor cursor = dbAdapter_comprob_venta.fetchAllComprobVenta(idLiquidacion);
        cursorAdapterComprobanteVenta = new CursorAdapterAllCV(this, cursor);

        ListView listView = (ListView) findViewById(R.id.listViewCompVenta);
        listView.setAdapter(cursorAdapterComprobanteVenta);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                idComprobante =
                        Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_id_comprob)));

                int estado = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_estado_comp)));


                switch (estado) {
                    case 0:
                        Toast.makeText(getApplicationContext(),
                                "El comprobante se encuentra anulado", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        dialogAcciones(idComprobante);
                        break;
                    default:

                        break;

                }

            }
        });

    }


    public void dialogAcciones(final int idComprobante) {

        //NO SE PODRÁ ANULAR, POR EL MOMENTO.
        final String[] items = {"Imprimir","Anular Comprobante","Cambiar Estado Exportación"};
        //final String[] items = {"Imprimir"};
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Seleccionar una acción");
        dialogo.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        Intent intent = new Intent(getActivity(), VMovil_BluetoothImprimir.class);
                        intent.putExtra("idComprobante", idComprobante);
                        startActivity(intent);
                        break;

                    case 1:
                        anularComprobante(getActivity()).show();
                        break;
                    case 2:
                        eleccion(idComprobante);

                        break;
                    default:
                        //
                        break;

                }

            }
        });
        dialogo.create();
        dialogo.show();
    }
    private void eleccion(final int idComprobante) {
        //Intent i = new Intent(this, VMovil_Evento_Establec.class);
        //i.putExtra("idEstab", idEstabl);
        //startActivity(i);

        final String[] items = {getActivity().getString(R.string.NoExport),getActivity().getString(R.string.Exported)};
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Estado Exportación");
        dialogo.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                int updated = 0;
                switch (item){
                    //CAMBIAR ESTADO A NO EXPORTADO
                    case 0:
                        updated = dbAdapter_comprob_venta.changeEstadoExport( idComprobante, Constants._CREADO);
                        if (updated>0){
                            Utils.setToast(getActivity(), "ESTADO ACTUALIZADO", R.color.verde);
                        }

                        break;
                    //CAMBIAR ESTADO A EXPORTADO
                    case 1:
                        updated = dbAdapter_comprob_venta.changeEstadoExport( idComprobante, Constants._EXPORTADO);
                        if (updated>0){
                            Utils.setToast(getActivity(), "ESTADO ACTUALIZAQDO", R.color.verde);
                        }

                        break;
                    default:
                        break;

                }
                listarComprobantesVentas();

            }
        });
        dialogo.create();
        dialogo.show();

    }


    public Dialog anularComprobante(final Activity main){
        return new AlertDialog.Builder(main)
                .setTitle("Anular Comprobante")
                .setMessage("¿Está seguro que desea anular el comprobante?")
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!

                        //ESTE ESTÁ DESHABILITADO
                        Cursor cursorComprobanteVentaDetalle = dbAdapter_comprob_venta_detalle.fetchAllComprobVentaDetalleByIdComp(idComprobante);
                        cursorComprobanteVentaDetalle.moveToFirst();

                        int id_producto;
                        int cantidad;
                        int precioUnitario;
                        int costeVenta;

                        int updated = 0;
                        cursorComprobanteVentaDetalle.moveToFirst();
                        if (cursorComprobanteVentaDetalle.getCount() > 0) {
                            do {
                                id_producto = cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_id_producto));
                                cantidad = cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_cantidad));

                                //FALTÓ MULTIPLICARLO POR EL VALOR UNIDAD.
                                updated += dbAdapter_stock_agente.updateStockAgenteCantidad(id_producto, cantidad, idLiquidacion);

                            } while (cursorComprobanteVentaDetalle.moveToNext());
                        } else {
                            Toast.makeText(getApplicationContext(), "No ha registros de este comprobante de venta, intente de nuevo. ", Toast.LENGTH_LONG).show();

                        }

                        if (updated > 0) {
                            dbAdapter_comprob_venta.updateComprobante(idComprobante, Constants._CV_ANULADO, Constants._ACTUALIZADO);
                            Utils.setToast(getActivity(), "Comprobante Anulado ", R.color.verde);
                        }

                        listarComprobantesVentas();

                        if (conectadoWifi() || conectadoRedMovil()) {
                            // exportMain.execute();
                            Intent intent = new Intent(getActivity(), ServiceExport.class);
                            intent.setAction(Constants.ACTION_EXPORT_SERVICE);
                            startService(intent);

                        }
                    }
                }).create();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mant_exportar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intentExportService = new Intent(getActivity(), ServiceExport.class);
        intentExportService.setAction(Constants.ACTION_EXPORT_SERVICE);
        switch (id) {
            case R.id.Exportar:

                getActivity().startService(intentExportService);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Activity getActivity(){
        return this;
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
}
