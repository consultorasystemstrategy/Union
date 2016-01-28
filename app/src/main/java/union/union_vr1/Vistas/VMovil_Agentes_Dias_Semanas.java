package union.union_vr1.Vistas;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapter_Dias_Semanas;
import union.union_vr1.Sqlite.DBAdapter_Cliente_Ruta;
import union.union_vr1.Sqlite.DbAdapter_Agente;

/**
 * Created by kike on 11/01/2016.
 */
public class VMovil_Agentes_Dias_Semanas extends Activity {
    private String dia;
    private String rutaag;
    private Button contador;
    private TextView disem;
    private TextView rutaa;
    private EditText busc;

    private DBAdapter_Cliente_Ruta dbAdapter_cliente_ruta;
    private CursorAdapter_Dias_Semanas cursorAdapterDiasSemanas;

    private DbAdapter_Agente dbAdapter_agente;

    @Override
    protected void onCreate(Bundle savedInstanceStat) {
        super.onCreate(savedInstanceStat);
        setContentView(R.layout.activity_vmovil_agentes_dia_semana);
        contador = (Button) findViewById(R.id.botoncontador);
        disem = (TextView) findViewById(R.id.textodiasemana);
        rutaa = (TextView) findViewById(R.id.ruta);
        //lista = (ListView) findViewById(R.id.listView);
        busc = (EditText) findViewById(R.id.buscar_dia);




        dia = getIntent().getExtras().getString("dia");
        rutaag = getIntent().getExtras().getString("ruta");

        //disem.setText(dia + " " + getDateFull().substring(8));
        disem.setText(rutaag);

        dbAdapter_cliente_ruta = new DBAdapter_Cliente_Ruta(this);
        dbAdapter_cliente_ruta.open();
        //consultardia();

        consultardia();
        buscardisplay();


    }

    public void buscardisplay() {
        busc.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                cursorAdapterDiasSemanas.getFilter().filter(s.toString());
            }

        });
        cursorAdapterDiasSemanas.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {

                return dbAdapter_cliente_ruta.listarPorNombre(constraint.toString(), dia);
            }
        });

    }

    public void consultardia() {

        Cursor cr = dbAdapter_cliente_ruta.listarPorDia(dia);
        contador.setText(cr.getCount() + "");
        cursorAdapterDiasSemanas = new CursorAdapter_Dias_Semanas(this, cr);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(cursorAdapterDiasSemanas);

      //String rutex = cr.getString(cr.getColumnIndexOrThrow(dbAdapter_agente.AG_nombre_ruta));
      //rutaa.setText("" + rutex);



    }

    private String getDateFull() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
        String formatteDate = format.format(date);
        return formatteDate;
    }

/*
    private void displayListviewByText(String dep) {

        Cursor cr =dbAdapter_cliente_ruta.listarPorNombre();
        cursorAdapterDiasSemanas = new CursorAdapter_Dias_Semanas(this, cr);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(cursorAdapterDiasSemanas);
    }*/


}
