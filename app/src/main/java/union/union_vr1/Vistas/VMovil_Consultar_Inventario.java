package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import union.union_vr1.R;

public class VMovil_Consultar_Inventario extends Activity {

    private EditText textGetDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__consultar__inventario);
        textGetDate = (EditText)findViewById(R.id.textGetDate);
        displayWidgets();
    }
    private void displayWidgets(){
        textGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendar();
            }
        });
    }

    private void showCalendar(){
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.consultar_inventario_calendario, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialoglayout);
        builder.show();
    }




}
