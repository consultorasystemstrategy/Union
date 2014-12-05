package union.union_vr1;

import java.util.ArrayList;
import java.util.List;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;

public class MainActivity extends Activity {
    private DbAdaptert_Evento_Establec dbHelper;
    private String Hola[] = new String[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DbAdaptert_Evento_Establec(this);
        dbHelper.open();
        ListView lv = (ListView) findViewById(R.id.listView);
        PlanetAdapter pa = new PlanetAdapter(initList(), this);

        lv.setAdapter(pa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                int Vs = position;
                String po = Hola[Vs];
                Toast.makeText(getApplicationContext(),
                        String.valueOf(po), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private List<Planet> initList() {
        // We populate the planets

        List<Planet> planetsList = new ArrayList<Planet>();
        int sd = 0;
        Cursor cursor = dbHelper.fetchAllEstablecsXX();
        if (cursor.moveToFirst()) {
            do {
                planetsList.add(new Planet(cursor.getString(5), cursor.getString(13)));
                Hola[sd] = cursor.getString(0);
                sd += 1;
            } while(cursor.moveToNext());
        }

        //planetsList.add(new Planet("Mercury", 10));
        //planetsList.add(new Planet("Venus", 20));
        //planetsList.add(new Planet("Mars", 30));
        //planetsList.add(new Planet("Jupiter", 40));
        //planetsList.add(new Planet("Saturn", 50));
        //planetsList.add(new Planet("Uranus", 60));
        //planetsList.add(new Planet("Neptune", 70));

        return planetsList;
    }


}