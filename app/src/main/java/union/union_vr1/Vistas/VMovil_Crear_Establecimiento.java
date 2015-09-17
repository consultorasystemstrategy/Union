package union.union_vr1.Vistas;

import android.app.TabActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import union.union_vr1.R;

public class VMovil_Crear_Establecimiento extends TabActivity  implements View.OnClickListener{
    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmovil__crear__establecimiento);
        tabHost = (TabHost) findViewById(android.R.id.tabhost);

        //This is for Personal Information
        TabHost.TabSpec specPersonal = tabHost.newTabSpec("Tab1");
        specPersonal.setContent(R.id.tab_Personal);
        specPersonal.setIndicator("Personal");
        displayPersonal();
        tabHost.addTab(specPersonal);
        // end

        //This is for Client Information
        TabHost.TabSpec specClient = tabHost.newTabSpec("Tab2");
        specClient.setContent(R.id.tab_Cliente);
        specClient.setIndicator("Cliente");
        displayPersonal();
        tabHost.addTab(specClient);
        // end

        //This is for Addres Information
        TabHost.TabSpec specAddres = tabHost.newTabSpec("Tab3");
        specAddres.setContent(R.id.tab_Direccion);
        specAddres.setIndicator("Direccion");
        displayPersonal();
        tabHost.addTab(specAddres);
        // end

        //This is for store Information
        TabHost.TabSpec specStore = tabHost.newTabSpec("Tab4");
        specStore.setContent(R.id.tab_Establecimiento);
        specStore.setIndicator("Establecimiento");
        displayPersonal();
        tabHost.addTab(specStore);
        // end


        //This is for Location Information
        TabHost.TabSpec specLocation = tabHost.newTabSpec("Tab5");
        specLocation.setContent(R.id.tab_Localizacion);
        specLocation.setIndicator("Localizacion");
        displayPersonal();
        tabHost.addTab(specLocation);
        // end


        //This is for EventoOfStore Information
        TabHost.TabSpec specEventoOfStore = tabHost.newTabSpec("Tab6");
        specEventoOfStore.setContent(R.id.tab_EvenEstablecimiento);
        specEventoOfStore.setIndicator("Evento Establecimiento");
        displayPersonal();
        tabHost.addTab(specEventoOfStore);
        // end
    }

    private void displayPersonal() {

    }


    @Override
    public void onClick(View view) {

    }

}
