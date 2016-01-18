package union.union_vr1.Vistas;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapter_Dias_Semanas;
import union.union_vr1.Sqlite.DBAdapter_Cliente_Ruta;

/**
 * Created by kike on 11/01/2016.
 */
public class VMovil_Agentes_Dias_Semanas extends Activity{
    private String dia;
    private Button contador;

    private DBAdapter_Cliente_Ruta dbAdapter_cliente_ruta;
    private CursorAdapter_Dias_Semanas cursorAdapterDiasSemanas;

    @Override
    protected void onCreate(Bundle savedInstanceStat){
        super.onCreate(savedInstanceStat);
        setContentView(R.layout.activity_vmovil_agentes_dia_semana);
        contador=(Button)findViewById(R.id.botoncontador);

        dia=getIntent().getExtras().getString("dia");
        dbAdapter_cliente_ruta = new DBAdapter_Cliente_Ruta(this);
        dbAdapter_cliente_ruta.open();

        consultardia();
    }

    public void consultardia(){

        Cursor cr =dbAdapter_cliente_ruta.listarPorDia(dia);
        contador.setText(cr.getCount()+"");
        cursorAdapterDiasSemanas = new CursorAdapter_Dias_Semanas(this, cr);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(cursorAdapterDiasSemanas);
    }



}
