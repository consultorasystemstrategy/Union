package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.AsyncTask.ExportMain;
import union.union_vr1.AsyncTask.ImportMain;
import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;

//Esti es yb cinebtario

public class VMovil_Evento_Establec extends Activity implements View.OnClickListener {

    private DbAdapter_Temp_Session session;

    private Cursor cursor;
    private DbAdaptert_Evento_Establec dbHelper;
    private DbAdapter_Comprob_Cobro dbHelper44;
    private TextView titulo;
    private String titulox;
    private Button estado;
    private String estadox;
    private int valEstado;
    private String valIdEstab;
    private int idAgente;
    private Button mCobros, mCanDev, mVentas, mManten, mReport, mEstadoAtendido, mEstadoNoAtendido, mEstadoPendiente;
    private ImageButton estadoAtencion;
    private VMovil_Evento_Establec mainActivity;

    //Esto es un comentario
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_evento_establec);

        mainActivity = this;


        session = new DbAdapter_Temp_Session(this);
        session.open();


        session.deleteVariable(6);
        session.createTempSession(6,0);



        dbHelper = new DbAdaptert_Evento_Establec(this);
        dbHelper.open();

        mCobros = (Button) findViewById(R.id.VEE_BTNcobros);
        mCanDev = (Button) findViewById(R.id.VEE_BTNcandev);
        mVentas = (Button) findViewById(R.id.VEE_BTNventas);
        mManten = (Button) findViewById(R.id.VEE_BTNmanten);
        estadoAtencion = (ImageButton) findViewById(R.id.imageButtonEstadoAtencion);

        /*mReport = (Button) findViewById(R.id.VEE_BTNreport);
        mEstadoAtendido = (Button) findViewById(R.id.VEE_BTNEstadoAtendido);
        mEstadoNoAtendido = (Button) findViewById(R.id.VEE_BTNEstadoNoAtendido);
        mEstadoPendiente = (Button) findViewById(R.id.VEE_BTNEstadoPendiente);*/

        mCobros.setOnClickListener(this);
        mCanDev.setOnClickListener(this);
        mVentas.setOnClickListener(this);
        mManten.setOnClickListener(this);
        estadoAtencion.setOnClickListener(this);
        //mReport.setOnClickListener(this);
        /*
        mEstadoAtendido.setOnClickListener(this);
        mEstadoNoAtendido.setOnClickListener(this);
        mEstadoPendiente.setOnClickListener(this);
*/
        session.deleteVariable(6);
        session.createTempSession(6,0);



        Bundle bundle = getIntent().getExtras();


        /*
        valIdEstab = bundle.getString("idEstab");
        idAgente = bundle.getInt("idAgente");
        */


        valIdEstab = ""+session.fetchVarible(2);
        idAgente  = session.fetchVarible(1);





       // valIdEstab="1";
       // idAgente=1;
        //titulos(valIdEstab);



        //titulo = (TextView) findViewById(R.id.VEE_TVWtitulo);







        //titulo.setText(titulox);

        //.-------------------------------------
        dbHelper44 = new DbAdapter_Comprob_Cobro(this);
        dbHelper44.open();
        int idEstab = Integer.parseInt(valIdEstab);


        Cursor cursorEstablecimiento = dbHelper.listarEstablecimientosByID(idEstab);


        TextView nombreEstablecimiento = (TextView) findViewById(R.id.textViewEstablecimientoNombre);
        TextView nombreCliente = (TextView)findViewById(R.id.textViewEstablecimientoCliente);
        TextView deuda = (TextView) findViewById(R.id.textViewEstablecimientoDeuda);
        LinearLayout linearLayoutColor = (LinearLayout) findViewById(R.id.linearLayoutEstablecimientoColor);
        TextView orden = (TextView) findViewById(R.id.textViewEstablecimientoOrden);

        cursorEstablecimiento.moveToFirst();

        if (cursorEstablecimiento.getCount()>0){

            String id_establecimiento = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex("idEstablecimiento"));
            String nombre_establecimiento = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex("nombreEstablecimiento"));
            String nombre_cliente = cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex("nombrecliente"));
            int id_estado_atencion = Integer.parseInt(cursorEstablecimiento.getString(cursorEstablecimiento.getColumnIndex("estadoAtencion")));
            int numeroOrden = cursorEstablecimiento.getInt(cursorEstablecimiento.getColumnIndexOrThrow("orden"));
            double deudaTotal = cursorEstablecimiento.getDouble(cursorEstablecimiento.getColumnIndexOrThrow("deudaTotal")) ;


            DecimalFormat df= new DecimalFormat("#0.00");



            nombreEstablecimiento.setText(numeroOrden + ". " +nombre_establecimiento);
            nombreEstablecimiento.setSingleLine(false);
            nombreCliente.setText(nombre_cliente);
            orden.setText("");
            deuda.setText("S/. "+df.format(deudaTotal));

            switch (id_estado_atencion){
                case 1:
                    linearLayoutColor.setBackgroundColor(getApplication().getResources().getColor(R.color.azul));
                    break;
                case 2:
                    linearLayoutColor.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.verde));
                    break;
                case 3:
                    linearLayoutColor.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.rojo));
                    break;
                case 4:
                    linearLayoutColor.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.amarillo));
                    break;

            }
        }




        Cursor cursor = dbHelper44.listaComprobantes(idEstab);

        //Recorremos el cursor hasta que no haya más registros

        int i;

        if (cursor.moveToFirst()) {
            for (i = 0; i < 1; i++) {
                String fecha = cursor.getString(0);

                try {
                    cursor.moveToNext();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    Date dSqlite = df.parse(fecha);
                    Date dSistema = df.parse(fech());

                    if (dSqlite.equals(dSistema)) {
                        Toast.makeText(getApplicationContext(), "Hay Deudas por Cobrar ", Toast.LENGTH_SHORT).show();
                        //mCobros.getBackground().setColorFilter(new LightingColorFilter(0xffff0000, 0xffff0000));
                        mCobros.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.border_red));

                    }
                    if (dSqlite.before(dSistema)) {
                        Toast.makeText(getApplicationContext(), "Hay Deudas por Cobrar", Toast.LENGTH_SHORT).show();
                        //mCobros.getBackground().setColorFilter(new LightingColorFilter(0xffff0000, 0xffff0000));
                        mCobros.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.border_red));

                    }
                    if (dSqlite.after(dSistema)) {
                        Toast.makeText(getApplicationContext(), "Fechas Proximas", Toast.LENGTH_SHORT).show();
                        //mCobros.getBackground().setColorFilter(new LightingColorFilter(0xffffff00, 0xffffff00));
                        mCobros.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.border_yellow));

                    }


                } catch (ParseException e) {
                    e.printStackTrace();

                }
            }

        }
        if (cursor.getCount() <= 0) {
            Toast.makeText(getApplicationContext(), "No hay Deudas Por Cobrar", Toast.LENGTH_SHORT).show();
            mCobros.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.border_right_green));

        }


        cursor.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sincronizar_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){

            case R.id.buttonImport:
                new ImportMain(mainActivity).execute();
                break;
            case R.id.buttonExportar:
                new ExportMain(mainActivity).execute();
                break;
            case R.id.buttonRedireccionarPrincipal:
                Intent intent = new Intent(mainActivity, VMovil_Evento_Indice.class);
                finish();
                startActivity(intent);
                break;
            default:
                //ON ITEM SELECTED DEFAULT
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private String fech() throws ParseException {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        Date d = df.parse(formatteDate);

        return formatteDate;
    }
//-------------------------------------------------------------------------------------------------------------------------



    /*
    public void titulos(String ids) {
        cursor = dbHelper.fetchEstablecsById(ids);
        if (cursor.moveToFirst()) {
            do {
                titulox =
                        "Cliente : " + cursor.getString(3) +
                                "\nNombre  : " + cursor.getString(4) +
                                "\nOrden   : " + cursor.getString(5);
                if (cursor.getInt(6) == 1) {
                    estadox = "POR ATENDER";
                    valEstado = 1;
                }
                if (cursor.getInt(6) == 2) {
                    estadox = "ATENDIDO";
                    valEstado = 2;
                }
                if (cursor.getInt(6) == 3) {
                    estadox = "NO ATENDIDO";
                    valEstado = 3;
                }
                if (cursor.getInt(6) == 4) {
                    estadox = "PENDIENTE";
                    valEstado = 4;
                }

            } while (cursor.moveToNext());
        }
    }
    */

    private void eleccion(final String idEstabl) {
        //Intent i = new Intent(this, VMovil_Evento_Establec.class);
        //i.putExtra("idEstab", idEstabl);
        //startActivity(i);

        final String[] items = {"No Hallado", "Cerrado", "SUNAT Clausurado", "Tiene Stock Suficiente"};
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("ESTADO");
        dialogo.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dbHelper.updateEstadoNoAtendido(idEstabl, 3, item, items[item]);
                Intent intent2 = new Intent(getApplicationContext(), VMovil_Menu_Establec.class);
                finish();
                startActivity(intent2);

                Toast.makeText(getApplicationContext(), "No atendio por : " + items[item], Toast.LENGTH_LONG).show();
            }
        });
        dialogo.create();
        dialogo.show();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.VEE_BTNcobros:
                Intent i = new Intent(this, VMovil_Cobro_Credito.class);
                i.putExtra("idEstabX", valIdEstab);
                startActivity(i);
                //Toast.makeText(getApplicationContext(),
                //        "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.VEE_BTNcandev:

                Intent idh = new Intent(this, VMovil_Evento_Canjes_Dev.class);
                idh.putExtra("idEstabX", valIdEstab);
                idh.putExtra("idAgente", idAgente);

                startActivity(idh);
                break;
            case R.id.VEE_BTNventas:
                Intent id = new Intent(this, VMovil_Venta_Cabecera.class);
                finish();
                id.putExtra("idEstabX", valIdEstab);
                startActivity(id);
                //Toast.makeText(getApplicationContext(),
                //        "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.VEE_BTNmanten:
                Intent idd = new Intent(this, VMovil_Venta_Comprob.class);
                idd.putExtra("idEstabX", valIdEstab);
                startActivity(idd);
                break;
            /*
            case R.id.VEE_BTNreport:
                Intent is = new Intent(this, VMovil_Canc_Histo.class);
                is.putExtra("idEstabX", valIdEstab);
                startActivity(is);
                //Toast.makeText(getApplicationContext(),
                //        "1", Toast.LENGTH_SHORT).show();
                break;
                */
            case R.id.imageButtonEstadoAtencion:
                new AlertDialog.Builder(this)
                        .setTitle("Estado Atención")
                        .setItems(R.array.estadoAtencion, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        dbHelper.updateEstadoEstablecs(valIdEstab, 2);
                                        Intent intent = new Intent(mainActivity, VMovil_Menu_Establec.class);
                                        finish();
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        eleccion(valIdEstab);
                                        break;
                                    case 2:
                                        dbHelper.updateEstadoEstablecs(valIdEstab, 4);
                                        Intent intent2 = new Intent(mainActivity, VMovil_Menu_Establec.class);
                                        finish();
                                        startActivity(intent2);
                                        break;
                                    default:
                                        break;

                                }
                            }
                        }).create().show();
                break;
            /*
            case R.id.VEE_BTNEstadoAtendido:

                dbHelper.updateEstadoEstablecs(valIdEstab, 2);
                Intent intent = new Intent(this, VMovil_Menu_Establec.class);
                startActivity(intent);
                break;
            case R.id.VEE_BTNEstadoNoAtendido:
                eleccion(valIdEstab);
                break;
            case R.id.VEE_BTNEstadoPendiente:

                dbHelper.updateEstadoEstablecs(valIdEstab, 4);
                //titulos(valIdEstab);
                Intent intent2 = new Intent(this, VMovil_Menu_Establec.class);
                startActivity(intent2);
                break;
                */
            default:
                break;
        }
    }
}
