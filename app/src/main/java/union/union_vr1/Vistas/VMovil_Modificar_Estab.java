package union.union_vr1.Vistas;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import union.union_vr1.AsyncTask.CrearEstablecimiento;
import union.union_vr1.Fragments.FClienteEditar;
import union.union_vr1.Fragments.FEstablecimientoEditar;
import union.union_vr1.Fragments.FMapaEditar;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Categoria_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Temp_DatosSpinner;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Doc_Identidad;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Persona;
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
    private DbAdapter_Temp_Establecimiento dbAdapter_temp_establecimiento;
    //DATA CLIENTE:

    String tipoDocumento = "";
    String documento = "";
    String tipoPersona = "";
    String nombres = "";
    String apPaterno = "";
    String apMaterno = "";
    String celular = "";
    String correo = "";

    //WIDGETS CLIENTE
    private Spinner spinnerTipoDocumento;
    private Spinner spinnerTipoPesona;
    private EditText editTextNombre;
    private EditText editTextApPaterno;
    private EditText editTextApMaterno;
    private AutoCompleteTextView autoNroDocumento;
    private EditText editTextNroCelular;
    private EditText editTextCorreo;

    //DATA ESTABLEC
    String EstipoEStablec;
    String EscatEStablec;
    String EsnomEstablec;
    String EstelFijo;
    String Esmovil;

    //WIDGETS ESTABLEC

    private Spinner spinnerTipoEstablecimeinto;
    private Spinner spinnerCategoriaEstablecimeinto;
    private EditText editTextNombreEstablec;
    private EditText editTextTelFijo;
    private EditText editTextTelMovil2;

    //DATA FOR DIRECCION

    String direccionEs="";
    String direccionFiscalEs="";
    String idEstablecimiento;

    //WIDGETS

    private EditText editTextDescripcion;
    private EditText editTextDireccionFiscal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_establecimiento);
        //..........

        idEstablecimiento = getIntent().getExtras().getString("idEstab");
        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();
        idusuario = dbAdapter_agente.getIdUsuario();

        dbAdapter_temp_establecimiento = new DbAdapter_Temp_Establecimiento(this);
        dbAdapter_temp_establecimiento.open();
        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbAdapter_temp_datosSpinner = new DbAdapter_Temp_DatosSpinner(this);
        dbAdapter_temp_datosSpinner.open();
        //..........
        viewPager = (ViewPager) findViewById(R.id.viewpagerarticulos);
        fragmentMapa = new FMapaEditar();
        fragmentCliente = new FClienteEditar();
        fragmentEstablecimiento = new FEstablecimientoEditar();

        Bundle bundle = new Bundle();
        bundle.putString("idEstablecimiento", idEstablecimiento);

        fragmentMapa.setArguments(bundle);
        fragmentCliente.setArguments(bundle);
        fragmentEstablecimiento.setArguments(bundle);



        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(fragmentCliente, "Cliente");
        tabsAdapter.addFragment(fragmentMapa, "Actualizar Direccion");
        tabsAdapter.addFragment(fragmentEstablecimiento, "Establecimiento");
        viewPager.setAdapter(tabsAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(viewPager);

        actualizar();
        //getData();

    }

    @Override
    protected void onPause() {
        super.onPause();

        item.setVisible(false);
    }

    private void actualizar() {
       /* if (conectadoWifi() || conectadoRedMovil()) {*/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("SELECTED", ": " + position);
                switch (position) {
                    case 0:
                        // Log.d("DEVUELVE", validaMapa() + "");
                        item.setVisible(false);

                        break;
                    case 1:
                        //Log.d("DEVUELVE", validaMapa() + "");
                        guardarCliente();
                        item.setVisible(false);
                        break;
                    case 2:
                        guardarDireccion();
                        item.setVisible(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void guardarDireccion(){

        try {
            editTextDescripcion = (EditText) fragmentMapa.getView().findViewById(R.id.map_descripcion);
            editTextDireccionFiscal = (EditText) fragmentMapa.getView().findViewById(R.id.map_direccion_fiscal);

            direccionEs = editTextDescripcion.getText().toString();
            direccionFiscalEs = editTextDireccionFiscal.getText().toString();

        }catch (NullPointerException e){

        }



        Log.d("Informacion-Direccion", direccionEs + "-" + direccionFiscalEs);


    }
    private void guardarEstablec(){

        try {

            spinnerTipoEstablecimeinto = (Spinner) fragmentEstablecimiento.getView().findViewById(R.id.spinnerTipoEstablecimiento);
            spinnerCategoriaEstablecimeinto = (Spinner) fragmentEstablecimiento.getView().findViewById(R.id.spinnerCategoriaEstablecimiento);
            editTextNombreEstablec = (EditText)fragmentEstablecimiento.getView().findViewById(R.id.editDescripcion);
            editTextTelFijo = (EditText)fragmentEstablecimiento.getView().findViewById(R.id.editTelFijo);
            editTextTelMovil2 = (EditText)fragmentEstablecimiento.getView().findViewById(R.id.editNumero2);

            Cursor crEsTE = (Cursor) spinnerTipoEstablecimeinto.getSelectedItem();
            Cursor crEsCE = (Cursor) spinnerCategoriaEstablecimeinto.getSelectedItem();
            EscatEStablec = crEsCE.getString(crEsCE.getColumnIndexOrThrow(DbAdapter_Categoria_Establecimiento.cat_Establec_EstablecId));
            EstipoEStablec = crEsTE.getString(crEsTE.getColumnIndexOrThrow(DbAdapter_Tipo_Establecimiento.tipo_Establecimiento_EstablecimientoId));
            EsnomEstablec = editTextNombreEstablec.getText().toString();
            EstelFijo = editTextTelFijo.getText().toString();
            Esmovil = editTextTelMovil2.getText().toString();

        }catch (NullPointerException e){

        }


        Log.d("Informacion-Establec",EscatEStablec+"-"+Esmovil);
    }

    private void guardarCliente()  {

        try {

            spinnerTipoPesona = (Spinner) fragmentCliente.getView().findViewById(R.id.spinnerTipoPersona);
            spinnerTipoDocumento = (Spinner) fragmentCliente.getView().findViewById(R.id.spinnerTipoDocumento);
            editTextNombre = (EditText) fragmentCliente.getView().findViewById(R.id.editNombre);
            editTextApPaterno = (EditText) fragmentCliente.getView().findViewById(R.id.editApPaterno);
            editTextApMaterno = (EditText) fragmentCliente.getView().findViewById(R.id.editApMaterno);
            autoNroDocumento = (AutoCompleteTextView) fragmentCliente.getView().findViewById(R.id.editNroDocumento);
            editTextNroCelular = (EditText) fragmentCliente.getView().findViewById(R.id.editNroCelular);
            editTextCorreo = (EditText) fragmentCliente.getView().findViewById(R.id.editCorreo);
            Cursor cPer = (Cursor) spinnerTipoPesona.getSelectedItem();
            Cursor cDoc = (Cursor) spinnerTipoDocumento.getSelectedItem();
            //SET  DATA IN STRINGS
            tipoDocumento = cDoc.getString(cDoc.getColumnIndexOrThrow(DbAdapter_Tipo_Doc_Identidad.tipo_Doc_IdentidadId));
            tipoPersona = cPer.getString(cPer.getColumnIndexOrThrow(DbAdapter_Tipo_Persona.tipo_Persona_PersonaId));
            nombres = editTextNombre.getText().toString();
            apPaterno= editTextApPaterno.getText().toString();
            apMaterno =editTextApMaterno.getText().toString();
            documento = autoNroDocumento.getText().toString();
            celular= editTextNroCelular.getText().toString();
            correo =editTextCorreo.getText().toString();

        }catch (NullPointerException e){

        }




        Log.d("Informacion-Cliente",tipoDocumento+"-"+celular);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_agregar_establecimiento, menu);
        item = menu.findItem(R.id.menu_establec);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_establec:
               guardarEstablec();
                alertConfirmar();
                break;
            default:
                //ON ITEM SELECTED DEFAULT
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void alertConfirmar() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Seguro de Guardar");
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Tiene ")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        getDta();
                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void getDta() {
        SharedPreferences prefs = getSharedPreferences("GPS", MODE_PRIVATE);
        Double lat = Double.parseDouble(prefs.getString("LATITUD", null));
        Double lon = Double.parseDouble(prefs.getString("LONGITUD", null));


       long estado = dbAdapter_temp_establecimiento.createTempEstablec(1, idusuario + "",EstelFijo, celular, Esmovil, lat, lon, direccionEs, direccionFiscalEs, Integer.parseInt(tipoPersona),nombres, apPaterno, apMaterno, Integer.parseInt(tipoDocumento),
                Integer.parseInt(documento), 1, correo, Integer.parseInt(EstipoEStablec), EsnomEstablec, Integer.parseInt(EscatEStablec), Constants._CREADO);
        Log.d("ESTADO INSERTO", "" + estado);
        Log.d("", "");
        if (estado > 0) {

            if (conectadoWifi() || conectadoRedMovil()) {
                new CrearEstablecimiento(this).execute();

                startActivity(new Intent(getApplicationContext(), VMovil_Menu_Establec.class));
            }
        } else {

            startActivity(new Intent(getApplicationContext(), VMovil_Menu_Establec.class));
        }

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
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {



                super.onBackPressed();

    }
}