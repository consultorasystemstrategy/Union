package union.union_vr1.Vistas;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.TableRow.LayoutParams;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import union.union_vr1.AsyncTask.ConsultarInventarioAnterior;
import union.union_vr1.MainActivity;
import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapter_Consultar_Inventario;
import union.union_vr1.Sqlite.DBAdapter_Consultar_Inventario_Anterior;
import union.union_vr1.Sqlite.DbAdapter_Agente;

public class VMovil_Consultar_Inventario extends Activity {
    private String strCurrDate;
    private TextView textGetDate;
    private TextView inventarioTextNombre;
    private DbAdapter_Agente dbAdapter_agente;
    private Button btnAgregarInventario;
    private DateFormat formate = DateFormat.getDateInstance();
    private Calendar calendar = Calendar.getInstance();
    private ConsultarInventarioAnterior consultarInventarioAnterior;
    private Activity context;
    private DBAdapter_Consultar_Inventario_Anterior dbAdapter_consultar_inventario_anterior;
    private CursorAdapter_Consultar_Inventario cursorAdapter_consultar_inventario;
    //private ListView listViewInventario;
    private TableLayout table_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__consultar__inventario);
        context = this;
        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();
        String nombreAgente = dbAdapter_agente.getNameAgente();
        inventarioTextNombre = (TextView) findViewById(R.id.InventarioTextNombre);
        inventarioTextNombre.setText(nombreAgente);
        dbAdapter_consultar_inventario_anterior = new DBAdapter_Consultar_Inventario_Anterior(this);
        dbAdapter_consultar_inventario_anterior.open();
        textGetDate = (TextView) findViewById(R.id.textGetDate);
        consultarInventarioAnterior = new ConsultarInventarioAnterior(context);
        btnAgregarInventario = (Button) findViewById(R.id.btnConsultarIventario);
        //listViewInventario = (ListView) findViewById(R.id.listviewInventario);

        displayWidgets();
        textGetDate.setText("Click Here");
    }

    private void displayWidgets() {
        textGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
                // showCalendar();
                //startActivity(new Intent(getApplicationContext(),CalendarViewInventario.class));
            }
        });
        btnAgregarInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textGetDate.getText().toString().equals("Click Here")) {
                    Toast.makeText(getApplicationContext(), "Ingrese una fecha", Toast.LENGTH_SHORT).show();

                } else {

                    table_layout = (TableLayout) findViewById(R.id.tableLayout1);
                    table_layout.removeAllViews();
                    Cursor c = dbAdapter_consultar_inventario_anterior.getConsultarInventario(strCurrDate);
                    if (c.moveToFirst()) {
                        // cursorAdapter_consultar_inventario = new CursorAdapter_Consultar_Inventario(getApplicationContext(),c);
                        // listViewInventario.setAdapter(cursorAdapter_consultar_inventario);

                        int rows = c.getCount();
                        int cols = c.getColumnCount();


                        // outer for loop
                        for (int i = 0; i < rows; i++) {

                            TableRow row = new TableRow(getApplicationContext());
                            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT));
                            row.setBackgroundColor(Color.parseColor("#1565C0"));

                            // inner for loop
                            for (int j = 0; j < cols; j++) {

                                TextView tv = new TextView(getApplicationContext());
                                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                                        LayoutParams.WRAP_CONTENT));
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(12);
                                tv.setTextColor(Color.parseColor("#190707"));
                                tv.setPadding(0, 0, 0, 0);
                                tv.setText(c.getString(j));
                                tv.setBackground(getResources().getDrawable(R.drawable.border_bottom_personalizado_tableinventario));
                                row.addView(tv);
                                if (i == 0) {
                                    tv.setBackground(getResources().getDrawable(R.drawable.border_bottom_personalizado_tableheader));
                                    tv.setTextColor(Color.parseColor("#FFFFFF"));

                                    String tex = "";
                                    switch (j) {
                                        case 0:
                                            tex = "Codigo";
                                            break;
                                        case 1:
                                            tex = "Nombre";
                                            break;
                                        case 2:
                                            tex = "Cantidad";
                                            break;
                                    }
                                    tv.setText(tex);

                                }
                                if(j==1 && i!=0){
                                    tv.setGravity(Gravity.LEFT);
                                }

                            }

                            c.moveToNext();

                            table_layout.addView(row);

                        }
                    } else {
                        consultarInventarioAnterior = new ConsultarInventarioAnterior(context);
                        consultarInventarioAnterior.execute(strCurrDate);
                    }

                }
            }
        });

    }

    public void updatedate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        strCurrDate = dateFormat.format(calendar.getTime());
        Log.d("CALENDARIO", strCurrDate + "");
        textGetDate.setText(formate.format(calendar.getTime()));
    }

    public void setDate() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -1);
        DatePickerDialog datePickerDialog = new DatePickerDialog(VMovil_Consultar_Inventario.this, d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(now.getTime().getTime());
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updatedate();
        }
    };
/*
    private void showCalendar() {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.consultar_inventario_calendario, null);
        final DatePicker datePicker = (DatePicker) dialoglayout.findViewById(R.id.dateConsultar);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialoglayout);
        builder.setCancelable(false);
        builder.setPositiveButton("Listo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int liquidacion) {
                String date = datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear();
                textGetDate.setText(date);
            }
        });
        builder.show();
    }
*/

}
