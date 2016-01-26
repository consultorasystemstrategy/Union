package union.union_vr1.Vistas;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
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
    private TextView textodfecha;
    CursorAdapter dataAdapter;
    private static String TAG = VMovil_Trans_Prod_Detallado.class.getSimpleName();

    private DBAdapter_Trans_Detallado dbAdapter_trans_detallado;

    // private CursorAdapter_Trans_Detallado cursorAdapter_trans_detallado;

    @Override
    protected void onCreate(Bundle savedInstanceStat) {
        super.onCreate(savedInstanceStat);
        setContentView(R.layout.activity_vmovil_trans_detalladolist);
        textodnguia = (TextView) findViewById(R.id.textoguiaid);
        textodfecha = (TextView) findViewById(R.id.textofecha);


        codigo = getIntent().getExtras().getString("codigotrans");
        textodnguia.setText(codigo);
        textodfecha.setText(DBAdapter_Trans_Detallado.guitran_fecha_producto);

                dbAdapter_trans_detallado = new DBAdapter_Trans_Detallado(this);
        dbAdapter_trans_detallado.open();

        //consultardia();
        displayList();

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
    public void displayList() {

        //vista
        ListView listView = (ListView) findViewById(R.id.listViewguiatranferencias);
        // listView.addHeaderView(getLayoutInflater().inflate(R.layout.activity_head_guia,null));
        //datos
        Cursor cursor = dbAdapter_trans_detallado.listarporid(codigo);
        //nombre de las columnas
        String[] columns = new String[]{
                DBAdapter_Trans_Detallado.pro_v_id,
                DBAdapter_Trans_Detallado.pro_v_nombre,
                DBAdapter_Trans_Detallado.guitran_v_cantidad_nm
        };
        //VISTA
        int[] to = new int[]{
                R.id.textocodigoid,
                R.id.textodescripcion,
                R.id.textocantidad
        };
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.activity_lista_gui_detallado,
                cursor,
                columns,
                to,
                0);
        listView.setAdapter(dataAdapter);



        String producto = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter_Trans_Detallado.guitran_fecha_producto));
        textodfecha.setText(" "+producto);

    }





}
