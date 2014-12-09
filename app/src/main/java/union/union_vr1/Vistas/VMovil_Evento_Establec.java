package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;


public class VMovil_Evento_Establec extends Activity implements View.OnClickListener {
    private Cursor cursor;
    private DbAdaptert_Evento_Establec dbHelper;
    private TextView titulo;
    private String titulox;
    private Button estado;
    private String estadox;
    private int valEstado;
    private String valIdEstab;
    private Button mCobros, mCanDev, mVentas, mManten, mReport, mEstadoAtendido, mEstadoNoAtendido, mEstadoPendiente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_evento_establec);
        dbHelper = new DbAdaptert_Evento_Establec(this);
        dbHelper.open();

        mCobros = (Button)findViewById(R.id.VEE_BTNcobros);
        mCanDev = (Button)findViewById(R.id.VEE_BTNcandev);
        mVentas = (Button)findViewById(R.id.VEE_BTNventas);
        mManten = (Button)findViewById(R.id.VEE_BTNmanten);
        mReport = (Button)findViewById(R.id.VEE_BTNreport);
        mEstadoAtendido = (Button)findViewById(R.id.VEE_BTNEstadoAtendido);
        mEstadoNoAtendido = (Button)findViewById(R.id.VEE_BTNEstadoNoAtendido);
        mEstadoPendiente = (Button)findViewById(R.id.VEE_BTNEstadoPendiente);


        mCobros.setOnClickListener(this);
        mCanDev.setOnClickListener(this);
        mVentas.setOnClickListener(this);
        mManten.setOnClickListener(this);
        mReport.setOnClickListener(this);
        mEstadoAtendido.setOnClickListener(this);
        mEstadoNoAtendido.setOnClickListener(this);
        mEstadoPendiente.setOnClickListener(this);


        Bundle bundle = getIntent().getExtras();
        valIdEstab=bundle.getString("idEstab");
        titulos(valIdEstab);

        titulo = (TextView) findViewById(R.id.VEE_TVWtitulo);
        titulo.setText(titulox);

    }

    public void titulos(String ids){
        cursor = dbHelper.fetchEstablecsById(ids);
        if (cursor.moveToFirst()) {
            do {
                titulox =
                    "Cliente : "+ cursor.getString(3) +
                    "\nNombre  : "+ cursor.getString(4) +
                    "\nOrden   : "+ cursor.getString(5);
                if(cursor.getInt(6)==1){
                    estadox = "POR ATENDER";
                    valEstado = 1;
                }
                if(cursor.getInt(6)==2){
                    estadox = "ATENDIDO";
                    valEstado = 2;
                }
                if(cursor.getInt(6)==3){
                    estadox = "NO ATENDIDO";
                    valEstado = 3;
                }
                if(cursor.getInt(6)==4){
                    estadox = "PENDIENTE";
                    valEstado = 4;
                }

            } while(cursor.moveToNext());
        }
    }

    private void eleccion(final String idEstabl){
        //Intent i = new Intent(this, VMovil_Evento_Establec.class);
        //i.putExtra("idEstab", idEstabl);
        //startActivity(i);

        final String[] items = {"No Hallado", "Cerrado", "SUNAT Clausurado", "Tiene Stock Suficiente"};
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("ESTADO");
        dialogo.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                    dbHelper.updateEstadoNoAtendido(idEstabl, 3, item,items[item]);
                    Intent intent2= new Intent(getApplicationContext(), VMovil_Menu_Establec.class);
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
                Intent idh = new Intent(this, VMovil_Histo_Venta.class);
                idh.putExtra("idEstabX", valIdEstab);
                startActivity(idh);
                break;
            case R.id.VEE_BTNventas:
                Intent id = new Intent(this, VMovil_Venta_Cabecera.class);
                id.putExtra("idEstabX", valIdEstab);
                startActivity(id);
                //Toast.makeText(getApplicationContext(),
                //        "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.VEE_BTNmanten:
                Intent idd = new Intent(this, VMovil_Canc_Histo.class);
                idd.putExtra("idEstabX", valIdEstab);
                startActivity(idd);
                break;
            case R.id.VEE_BTNreport:
                Intent is = new Intent(this, VMovil_Venta_Comprob.class);
                is.putExtra("idEstabX", valIdEstab);
                startActivity(is);
                //Toast.makeText(getApplicationContext(),
                //        "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.VEE_BTNEstadoAtendido:

                dbHelper.updateEstadoEstablecs(valIdEstab, 2);
                Intent intent= new Intent(this, VMovil_Menu_Establec.class);
                startActivity(intent);
                break;
            case R.id.VEE_BTNEstadoNoAtendido:
                eleccion(valIdEstab);
                break;
            case R.id.VEE_BTNEstadoPendiente:

                dbHelper.updateEstadoEstablecs(valIdEstab, 4);
                titulos(valIdEstab);
                Intent intent2= new Intent(this, VMovil_Menu_Establec.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
