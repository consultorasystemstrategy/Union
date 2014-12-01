package union.union_vr1.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;

public class VMovil_Menu_Establec extends Activity {

    private DbAdaptert_Evento_Establec dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_menu_establec);

        dbHelper = new DbAdaptert_Evento_Establec(this);
        dbHelper.open();

        //Clean all data
        //dbHelper.deleteAllEstablecs();
        //Add some data
        //dbHelper.insertSomeEstablecs();

        //Generate ListView from SQLite Database
        displayListView();

    }

    private void eleccion(String idEstabl){
        Intent i = new Intent(this, VMovil_Evento_Establec.class);
        i.putExtra("idEstab", idEstabl);
        startActivity(i);
    }

    private void displayListView() {

        Cursor cursor = dbHelper.fetchAllEstablecs();

        // The desired columns to be bound
        String[] columns = new String[] {
                DbAdaptert_Evento_Establec.EE_id_establec,
                DbAdaptert_Evento_Establec.EE_nom_establec,
                DbAdaptert_Evento_Establec.EE_nom_cliente,
                DbAdaptert_Evento_Establec.EE_doc_cliente
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.VME_codigo,
                R.id.VME_establec,
                R.id.VME_nombre,
                R.id.VME_docum,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_menu_establec,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.VME_listar);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String idEstablec =
                        cursor.getString(cursor.getColumnIndexOrThrow("ee_in_id_establec"));
                //Toast.makeText(getApplicationContext(),
                //        idEstablec, Toast.LENGTH_SHORT).show();
                eleccion(idEstablec);
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.VME_buscar);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchEstablecsByName(constraint.toString());
            }
        });

    }
}