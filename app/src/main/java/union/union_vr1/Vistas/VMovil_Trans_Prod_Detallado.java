package union.union_vr1.Vistas;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapter_Trans_Detallado;
import union.union_vr1.Sqlite.DBAdapter_Trans_Detallado;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;

/**
 * Created by kike on 15/01/2016.
 */
public class VMovil_Trans_Prod_Detallado extends Activity {
    private String codigo;
    private TextView textodnguia;
    CursorAdapter adapter;


    private DBAdapter_Trans_Detallado dbAdapter_trans_detallado;

    //private CursorAdapter_Trans_Detallado cursorAdapter_trans_detallado;

    @Override
    protected void onCreate(Bundle savedInstanceStat){
        super.onCreate(savedInstanceStat);
        setContentView(R.layout.activity_vmovil_trans_detalladolist);
        //String numeropro = cursorAdapter_trans_detallado.toString();
        textodnguia=(TextView)findViewById(R.id.textoguiaid);

            codigo=getIntent().getExtras().getString("codigotrans");

            dbAdapter_trans_detallado = new DBAdapter_Trans_Detallado(this);
            dbAdapter_trans_detallado.open();
           // consultardia();
       // textodnguia.setText(DBAdapter_Trans_Detallado.guitran_v_numguia_flex);

    }
/*
    public void consultardia(){

        Cursor cr =dbAdapter_trans_detallado.listarporid(codigo);
        cursorAdapter_trans_detallado = new CursorAdapter_Trans_Detallado(this, cr);
        ListView listView = (ListView) findViewById(R.id.listViewguiatranferencias);
        listView.setAdapter(cursorAdapter_trans_detallado);
    }
*/
    public void displayListStockVenta() {


        //vista
        ListView listView = (ListView) findViewById(R.id.VRC_listarResumenVentas);
        listView.addHeaderView(getLayoutInflater().inflate(R.layout.cabecera_inventario_ventas,null));

        //datos
        Cursor cursor =dbAdapter_trans_detallado.listarporid(codigo);

        //nombre de las columnas
        String[] columns = new String[]{
                DBAdapter_Trans_Detallado.pro_v_id,
                DBAdapter_Trans_Detallado.pro_v_nombre,

        };


        //VISTA
        int[] to = new int[]{
                R.id.VRC_TXproducto,
                R.id.VRC_TXinicialFin,
                R.id.VRC_TXventa,
                R.id.VRC_TXdevol,
                R.id.VRC_TXcanjes
        };
/*
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_resumen_ventas,
                cursor,
                columns,
                to,
                0);

        listView.setAdapter(dataAdapter);*/
    }


}
