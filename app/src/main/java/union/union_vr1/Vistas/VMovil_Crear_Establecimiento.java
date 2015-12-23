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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import union.union_vr1.AsyncTask.CrearEstablecimiento;
import union.union_vr1.AsyncTask.GetDataSpinnerRegistrar;
import union.union_vr1.Fragments.FClienteRegistrar;
import union.union_vr1.Fragments.FEstablecimientoRegistrar;
import union.union_vr1.Fragments.FMapaRegistrar;
import union.union_vr1.R;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_DatosSpinner;
import union.union_vr1.Sqlite.DbAdapter_Temp_Establecimiento;
import union.union_vr1.Utils.TabsAdapter;

public class VMovil_Crear_Establecimiento extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_establecimiento);
        //..........
        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();
        idusuario = dbAdapter_agente.getIdUsuario();

        dbAdapter_temp_establecimiento = new DbAdapter_Temp_Establecimiento(this);
        dbAdapter_temp_establecimiento.open();
        new GetDataSpinnerRegistrar(this).execute();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbAdapter_temp_datosSpinner = new DbAdapter_Temp_DatosSpinner(this);
        dbAdapter_temp_datosSpinner.open();
        //..........
        viewPager = (ViewPager) findViewById(R.id.viewpagerarticulos);
        fragmentMapa = new FMapaRegistrar();
        fragmentCliente = new FClienteRegistrar();
        fragmentEstablecimiento = new FEstablecimientoRegistrar();
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(fragmentMapa, "Registro Mapa");
        tabsAdapter.addFragment(fragmentCliente, "Cliente");
        tabsAdapter.addFragment(fragmentEstablecimiento, "Establecimiento");
        viewPager.setAdapter(tabsAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(viewPager);

        actualizar();
        //getData();

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
                        Log.d("DEVUELVE", validaMapa() + "");
                        item.setVisible(false);
                        break;
                    case 1:
                        Log.d("DEVUELVE", validaMapa() + "");
                        saveMapa();
                        item.setVisible(false);
                        break;
                    case 2:
                        item.setVisible(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private boolean validaMapa() {
        EditText editTextDescripcion;
        EditText editTextDireccionFiscal;
        editTextDescripcion = (EditText) fragmentMapa.getView().findViewById(R.id.map_descripcion);
        editTextDireccionFiscal = (EditText) fragmentMapa.getView().findViewById(R.id.map_direccion_fiscal);
        if ((editTextDescripcion.getText().toString() == null || editTextDescripcion.getText().toString() == "" || editTextDireccionFiscal.getText().toString() == null || editTextDireccionFiscal.getText().toString() == "")) {
            return false;
        } else {
            return true;
        }

    }

    private boolean validaCliente() {
        return false;
    }

    private boolean validaEstablecimiento() {
        return false;
    }


    public void saveMapa() {


        try {
            EditText editTextDescripcion;
            EditText editTextDireccionFiscal;
            editTextDescripcion = (EditText) fragmentMapa.getView().findViewById(R.id.map_descripcion);
            editTextDireccionFiscal = (EditText) fragmentMapa.getView().findViewById(R.id.map_direccion_fiscal);
            SharedPreferences.Editor editor = getSharedPreferences("INFORMACION_MAPA", Context.MODE_PRIVATE).edit();
            editor.putString("descripcion", editTextDescripcion.getText().toString());
            editor.putString("direccion_fiscal", editTextDireccionFiscal.getText().toString());

            editor.commit();
        } catch (NullPointerException e) {
            Log.d("ERROR", "" + e.getMessage());
        }


    }

    private void saveCliente() {


        try {
            Spinner spinnerTipoDocumento;
            Spinner spinnerTipoPesona;
            //
            EditText editTextNombre;
            EditText editTextApPaterno;
            EditText editTextApMaterno;
            EditText editTextNroDocumento;
            EditText editTextNroCelular;
            EditText editTextCorreo;
            spinnerTipoPesona = (Spinner) fragmentCliente.getView().findViewById(R.id.spinnerTipoPersona);
            spinnerTipoDocumento = (Spinner) fragmentCliente.getView().findViewById(R.id.spinnerTipoDocumento);
            editTextNombre = (EditText) fragmentCliente.getView().findViewById(R.id.editNombre);
            editTextApPaterno = (EditText) fragmentCliente.getView().findViewById(R.id.editApPaterno);
            editTextApMaterno = (EditText) fragmentCliente.getView().findViewById(R.id.editApMaterno);
            editTextNroDocumento = (EditText) fragmentCliente.getView().findViewById(R.id.editNroDocumento);
            editTextNroCelular = (EditText) fragmentCliente.getView().findViewById(R.id.editNroCelular);
            editTextCorreo = (EditText) fragmentCliente.getView().findViewById(R.id.editCorreo);

            int tipo_persona = spinnerTipoPesona.getSelectedItemPosition();
            int tipo_documento = spinnerTipoDocumento.getSelectedItemPosition();
            String nombre = editTextNombre.getText().toString();
            String apPaterno = editTextApPaterno.getText().toString();
            String apMaterno = editTextApMaterno.getText().toString();
            String nroDocumento = editTextNroDocumento.getText().toString();
            String nroCelular = editTextNroCelular.getText().toString();
            String correo = editTextCorreo.getText().toString();
            SharedPreferences.Editor editor = getSharedPreferences("INFORMACION_CLIENTE", Context.MODE_PRIVATE).edit();
            editor.putString("tipo_persona", tipo_persona + "");
            editor.putString("tipo_documento", tipo_documento + "");
            editor.putString("nombre", nombre + "");
            editor.putString("apPaterno", apPaterno + "");
            editor.putString("apMaterno", apMaterno + "");
            editor.putString("nroDocumento", nroDocumento + "");
            editor.putString("nroCelular", nroCelular + "");
            editor.putString("correo", correo + "");

            editor.commit();
        } catch (NullPointerException e) {

        }
    }

    private void saveEstalecimiento() {

        try {
            Spinner spinnerTipoEstablecimeinto;
            Spinner spinnerCategoriaEstablecimeinto;
            EditText editTextNombre;
            EditText editTextTelFijo;
            EditText editTextNumero2;
            spinnerTipoEstablecimeinto = (Spinner) fragmentEstablecimiento.getView().findViewById(R.id.spinnerTipoEstablecimiento);
            spinnerCategoriaEstablecimeinto = (Spinner) fragmentEstablecimiento.getView().findViewById(R.id.spinnerCategoriaEstablecimiento);
            editTextNombre = (EditText) fragmentEstablecimiento.getView().findViewById(R.id.editDescripcion);
            editTextTelFijo = (EditText) fragmentEstablecimiento.getView().findViewById(R.id.editTelFijo);
            editTextNumero2 = (EditText) fragmentEstablecimiento.getView().findViewById(R.id.editNumero2);
            int catEstablec = spinnerCategoriaEstablecimeinto.getSelectedItemPosition();
            int tipoEstablec = spinnerTipoEstablecimeinto.getSelectedItemPosition();
            String nombreEstablec = editTextNombre.getText().toString();
            String telFijo = editTextTelFijo.getText().toString();
            String numero2 = editTextNumero2.getText().toString();
            SharedPreferences.Editor editor = getSharedPreferences("INFORMACION_ESTABLECIMIENTO", Context.MODE_PRIVATE).edit();
            editor.putString("nombre", nombreEstablec);
            editor.putString("tipo_establec", tipoEstablec + "");
            editor.putString("categoria_establec", catEstablec + "");
            editor.putString("telFijo", telFijo + "");
            editor.putString("numero2", numero2 + "");
            editor.commit();
        } catch (NullPointerException e) {

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
            case R.id.menu_establec:
                saveCliente();
                saveEstalecimiento();
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
                .setMessage("Operacion:")
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
        //Informacion de Mapa

        SharedPreferences getDataMapa = getSharedPreferences("INFORMACION_MAPA", MODE_PRIVATE);
        String descripcion = getDataMapa.getString("descripcion", null);
        String direccion_fiscal = getDataMapa.getString("direccion_fiscal", null);
        double latitud = Double.parseDouble(getDataMapa.getString("latitud", null));
        double longitud = Double.parseDouble(getDataMapa.getString("longitud", null));

        Log.d("INFORMACION MAPA {", " descripcion: " + descripcion + " : direccion_fiscal: " + direccion_fiscal + " : latitud" + latitud + " : longitud" + longitud + "");

        //Informacion de Cliente

        SharedPreferences getDataCliente = getSharedPreferences("INFORMACION_CLIENTE", MODE_PRIVATE);
        //Log.d("DATA EN JSON",""+tipo_persona);
        int idTipoPersona = tipo_Persona(Integer.parseInt(getDataCliente.getString("tipo_persona", null)));
        int idTipoDocumento = tipo_Documento(Integer.parseInt(getDataCliente.getString("tipo_documento", null)));
        String nombre = getDataCliente.getString("nombre", null);
        String apPaterno = getDataCliente.getString("apPaterno", null);
        String apMaterno = getDataCliente.getString("apMaterno", null);
        String nroDocumento = getDataCliente.getString("nroDocumento", null);
        String nroCelular = getDataCliente.getString("nroCelular", null);
        String correo = getDataCliente.getString("correo", null);

        Log.d("INFORMACION CLIENTE{", " tipo_persona: " + idTipoPersona + " : tipo_documento: " + idTipoDocumento + " : nombre" + nombre + " : apPaterno" + apPaterno + " : apMaterno" + apMaterno + " : nroDocumento" + nroDocumento + " : nroCelular" + nroCelular + " : correo" + correo);

        //Informacion de establecimiento
        SharedPreferences getDataEstablec = getSharedPreferences("INFORMACION_ESTABLECIMIENTO", MODE_PRIVATE);
        String nombreEstablec = getDataEstablec.getString("nombre", null);
        int idcat_Establec = categoria_Establec(Integer.parseInt(getDataEstablec.getString("categoria_establec", null)));
        int idtipo_Establec = tipo_Establec(Integer.parseInt(getDataEstablec.getString("tipo_establec", null)));
        String telFijo = getDataEstablec.getString("telFijo", null);
        String numero2 = getDataEstablec.getString("numero2", null);
        Log.d("INFORMACION ESTABLEC", "TIPO PERSONA: " + idTipoPersona + " TIPO DOCUMENTO: " + idTipoDocumento + " CATE ESTABLEC: " + idcat_Establec + " TIPO_ESTABLEC : " + idtipo_Establec);
        Log.d("INFORMACION ESTABLEC {",1 + "-"+ idusuario +   "-"+ telFijo + "-"+ nroCelular + "-"+ numero2 + "-"+ latitud + "-"+ longitud + "-"+ descripcion + "-"+ direccion_fiscal + "-"+ idTipoPersona + "-"+ nombre + "-"+ apPaterno + "-"+ apMaterno + "-"+ idTipoDocumento + "-"+Integer.parseInt(nroDocumento) + "-"+ 1 + "-"+ correo + "-"+ idtipo_Establec + "-"+ nombreEstablec + "-"+idcat_Establec + "-"+Constants._CREADO);
        long estado = dbAdapter_temp_establecimiento.createTempEstablec(1, idusuario + "", telFijo, nroCelular, numero2, latitud, longitud, descripcion, direccion_fiscal, idTipoPersona, nombre, apPaterno, apMaterno, idTipoDocumento,
                Integer.parseInt(nroDocumento), 1, correo, idtipo_Establec, nombreEstablec,idcat_Establec,Constants._CREADO);
        Log.d("ESTADO INSERTO", "" + estado);
        Log.d("","");
        if (estado > 0) {
            if (conectadoWifi() || conectadoRedMovil()) {
                new CrearEstablecimiento(this).execute();
            }
        } else {

        }

    }

    private int tipo_Persona(int position) {
        Cursor cursor = dbAdapter_temp_datosSpinner.fetchTemSpinnerTipo(3);
        cursor.moveToFirst();
        int id = 0;
        // Log.d("DATA EN JSON", "" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
            JSONArray jsonArray = jsonObject.getJSONArray("Value");
            id = jsonArray.getJSONObject(position).getInt("TperITipoPersonaId");
            Log.d("TIPO PERSONA", "" + jsonArray.get(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return id;
    }

    private int tipo_Documento(int position) {
        Cursor cursor = dbAdapter_temp_datosSpinner.fetchTemSpinnerTipo(2);
        cursor.moveToFirst();
        int id = 0;
        // Log.d("DATA EN JSON", "" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
            JSONArray jsonArray = jsonObject.getJSONArray("Value");
            id = jsonArray.getJSONObject(position).getInt("TdiTipoDocIdentidadId");
            Log.d("TIPO DOCUMENTO", "" + jsonArray.get(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return id;
    }

    private int categoria_Establec(int position) {
        Cursor cursor = dbAdapter_temp_datosSpinner.fetchTemSpinnerTipo(4);
        cursor.moveToFirst();
        int id = 0;
        // Log.d("DATA EN JSON", "" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
            JSONArray jsonArray = jsonObject.getJSONArray("Value");
            id = jsonArray.getJSONObject(position).getInt("CateICatEstablecimientoId");
            Log.d("CATEGORIA ESTABLEC", "" + jsonArray.get(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return id;
    }

    private int tipo_Establec(int position) {
        Cursor cursor = dbAdapter_temp_datosSpinner.fetchTemSpinnerTipo(1);
        cursor.moveToFirst();
        int id = 0;
        // Log.d("DATA EN JSON", "" + cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Temp_DatosSpinner.spinner_variable)));
            JSONArray jsonArray = jsonObject.getJSONArray("Value");
            id = jsonArray.getJSONObject(position).getInt("TieITipEstId");
            Log.d("TIPO ESTABLEC", "" + jsonArray.get(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return id;
    }


}