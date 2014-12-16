package union.union_vr1.Vistas;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import union.union_vr1.R;


public class VMovil_Venta_Cabecera_PlanPagos extends Activity {

    private Spinner spinnerCuotas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__venta__cabecera__plan_pagos);

        spinnerCuotas = (Spinner) findViewById(R.id.VCPP_spinnerCuotas);
        ArrayAdapter<CharSequence> adapterCuotas = ArrayAdapter.createFromResource(this,R.array.Cuotas,android.R.layout.simple_spinner_item);
        adapterCuotas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCuotas.setAdapter(adapterCuotas);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vmovil__venta__cabecera__plan_pagos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
