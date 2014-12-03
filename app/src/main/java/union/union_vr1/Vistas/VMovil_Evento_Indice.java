package union.union_vr1.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import union.union_vr1.MySQL.DbManager_Evento_Establec_GET;
import union.union_vr1.MySQL.DbManager_Evento_Establec_POST;
import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Histo_Comprob_Anterior;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;


public class VMovil_Evento_Indice extends Activity implements View.OnClickListener {
    private Cursor cursor;
    private DbAdaptert_Evento_Establec dbHelper;
    private DbAdapter_Comprob_Venta_Detalle dbHelper2;
    private DbAdapter_Comprob_Venta dbHelper1;
    private DbAdapter_Agente dbHelper3;
    private DbAdapter_Comprob_Cobro dbHelper4;
    private DbAdapter_Histo_Venta_Detalle dbHelper5;
    private DbAdapter_Stock_Agente dbHelper6;
    private DbAdapter_Precio dbHelper7;
    private DbAdapter_Histo_Comprob_Anterior dbHelper8;
    private TextView titulo;
    private String titulox;
    private Button estado;
    private String estadox;
    private String valIdEstab;
    private Button mClient, mInfgas, mResume, mCarinv, mTrainv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_evento_indice);
        dbHelper1 = new DbAdapter_Comprob_Venta(this);
        dbHelper1.open();
        dbHelper2 = new DbAdapter_Comprob_Venta_Detalle(this);
        dbHelper2.open();
        dbHelper = new DbAdaptert_Evento_Establec(this);
        dbHelper.open();
        dbHelper3 = new DbAdapter_Agente(this);
        dbHelper3.open();
        dbHelper4 = new DbAdapter_Comprob_Cobro(this);
        dbHelper4.open();
        dbHelper5 = new DbAdapter_Histo_Venta_Detalle(this);
        dbHelper5.open();
        dbHelper6 = new DbAdapter_Stock_Agente(this);
        dbHelper6.open();
        dbHelper7 = new DbAdapter_Precio(this);
        dbHelper7.open();
        dbHelper8 = new DbAdapter_Histo_Comprob_Anterior(this);
        dbHelper8.open();
        dbHelper.deleteAllEstablecs();
        dbHelper.insertSomeEstablecs();
        dbHelper1.deleteAllComprobVenta();
        dbHelper2.deleteAllComprobVentaDetalle();
        dbHelper3.deleteAllAgentes();
        dbHelper3.insertSomeAgentes();
        //dbHelper4.insertSomeComprobCobros();
        dbHelper4.deleteAllComprobCobros();
        dbHelper4.insertSomeComprobCobros();
        dbHelper5.deleteAllHistoVentaDetalle();
        dbHelper5.insertSomeHistoVentaDetalle();
        dbHelper6.deleteAllStockAgente();
        dbHelper6.insertSomeStockAgente();
        dbHelper7.deleteAllPrecio();
        dbHelper7.insertSomePrecio();
        dbHelper8.deleteAllHistoComprobAnterior();
        dbHelper8.insertSomeHistoComprobAnterior();
        mClient = (Button)findViewById(R.id.VEI_BTNclient);
        mInfgas = (Button)findViewById(R.id.VEI_BTNinfgas);
        mResume = (Button)findViewById(R.id.VEI_BTNresume);
        mCarinv = (Button)findViewById(R.id.VEI_BTNcarinv);
        mTrainv = (Button)findViewById(R.id.VEI_BTNtrainv);

        mClient.setOnClickListener(this);
        mInfgas.setOnClickListener(this);
        mResume.setOnClickListener(this);
        mCarinv.setOnClickListener(this);
        mTrainv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.VEI_BTNclient:
                Intent i = new Intent(this, VMovil_Menu_Establec.class);
                startActivity(i);
                break;
            case R.id.VEI_BTNinfgas:
                Intent ig = new Intent(this,VMovil_Evento_Gasto.class);
                startActivity(ig);
                break;
            case R.id.VEI_BTNresume:
                Intent ir = new Intent(this,VMovil_Resumen_Caja.class);
                startActivity(ir);
                break;
            case R.id.VEI_BTNcarinv:
                Intent is = new Intent(this, DbManager_Evento_Establec_GET.class);
                startActivity(is);
                break;
            case R.id.VEI_BTNtrainv:
                Intent ip = new Intent(this, DbManager_Evento_Establec_POST.class);
                startActivity(ip);
                break;
            default:
                break;
        }
    }
}
