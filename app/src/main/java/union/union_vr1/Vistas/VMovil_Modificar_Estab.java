package union.union_vr1.Vistas;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import union.union_vr1.Fragments.FClienteEditar;
import union.union_vr1.Fragments.FClienteRegistrar;
import union.union_vr1.Fragments.FEstablecimientoEditar;
import union.union_vr1.Fragments.FEstablecimientoRegistrar;
import union.union_vr1.Fragments.FMapaEditar;
import union.union_vr1.Fragments.FMapaRegistrar;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_DatosSpinner;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Utils.TabsAdapter;

public class VMovil_Modificar_Estab extends AppCompatActivity {

    private Fragment fragmentMapa;
    private Fragment fragmentCliente;
    private Fragment fragmentEstablecimiento;
    private MenuItem item;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DbAdapter_Agente dbAdapter_agente;
    private int idusuario;
    private DbAdapter_Temp_DatosSpinner dbAdapter_temp_datosSpinner;
    private String idEstablecimiento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_establecimiento);

        idEstablecimiento = getIntent().getExtras().getString("idEstab");
        Log.d("ESTABLECIMIENTO", "" + idEstablecimiento);
        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();
        idusuario = dbAdapter_agente.getIdUsuario();

        //------------------

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#19262F")));
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);

        //------------------


        //
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbAdapter_temp_datosSpinner = new DbAdapter_Temp_DatosSpinner(this);
        dbAdapter_temp_datosSpinner.open();
        //..........
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fragmentMapa = new FMapaEditar();
        fragmentCliente = new FClienteEditar();
        fragmentEstablecimiento = new FEstablecimientoEditar();

        //ENVIANDO EL ID Nuevo

        Bundle bundle = new Bundle();
        bundle.putString("idEstablecimiento", idEstablecimiento + "");

        fragmentMapa.setArguments(bundle);
        fragmentCliente.setArguments(bundle);
        fragmentEstablecimiento.setArguments(bundle);


        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(fragmentCliente, "Cliente");
        tabsAdapter.addFragment(fragmentMapa, "Direccion");
        tabsAdapter.addFragment(fragmentEstablecimiento, "Establecimiento");
        viewPager.setAdapter(tabsAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        //getData();

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_agregar_establecimiento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                onBackPressed();
                break;
            case R.id.menu_establec:

                break;
        }
        return super.onOptionsItemSelected(item); // important line
    }


    protected Boolean conectadoWifi() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }



    @Override
    public void onBackPressed() {
        seguroSalir();

    }

    public void seguroSalir() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("No se ha guardado los datos");
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Realmente ¿Desea salir?")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getApplicationContext(), VMovil_Menu_Establec.class));
                        finish();
                    }

                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}