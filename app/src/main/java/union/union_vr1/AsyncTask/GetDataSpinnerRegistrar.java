package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.DbAdapter_Categoria_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Temp_DatosSpinner;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Doc_Identidad;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Establecimiento;
import union.union_vr1.Sqlite.DbAdapter_Tipo_Persona;

/**
 * Created by Kelvin on 23/11/2015.
 */
public class GetDataSpinnerRegistrar extends AsyncTask<String, String, String> {

    private Activity mainActivity;
    private JSONObject jsonObjectEstablecimiento = null;
    private JSONObject jsonObjectIdentidad = null;
    private JSONObject jsonObjectPersona = null;
    private JSONObject jsonObjectCategoriaEstable = null;
    private StockAgenteRestApi stockAgenteRestApi;

    private DbAdapter_Categoria_Establecimiento dbAdapter_categoria_establecimiento;
    private DbAdapter_Tipo_Doc_Identidad dbAdapter_tipo_doc_identidad;
    private DbAdapter_Tipo_Establecimiento dbAdapter_tipo_establecimiento;
    private DbAdapter_Tipo_Persona dbAdapter_tipo_persona;

    public GetDataSpinnerRegistrar(Activity activity) {
        mainActivity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        dbAdapter_categoria_establecimiento = new DbAdapter_Categoria_Establecimiento(mainActivity);
        dbAdapter_tipo_doc_identidad = new DbAdapter_Tipo_Doc_Identidad(mainActivity);
        dbAdapter_tipo_establecimiento = new DbAdapter_Tipo_Establecimiento(mainActivity);
        dbAdapter_tipo_persona = new DbAdapter_Tipo_Persona(mainActivity);
        dbAdapter_categoria_establecimiento.open();
        dbAdapter_tipo_doc_identidad.open();
        dbAdapter_tipo_establecimiento.open();
        dbAdapter_tipo_persona.open();
        //Elimienando  xD
        dbAdapter_categoria_establecimiento.deleteCatEstablecimiento();
        dbAdapter_tipo_doc_identidad.deleteAllTipoDocIden();
        dbAdapter_tipo_establecimiento.deleteAllTipoEstablec();
        dbAdapter_tipo_persona.deleteAllTipoPersona();

        stockAgenteRestApi = new StockAgenteRestApi(mainActivity.getApplicationContext());
        try {
            jsonObjectEstablecimiento = stockAgenteRestApi.sel_SPINNER_TIPO_ESTABLECIMIENTO();
            jsonObjectIdentidad = stockAgenteRestApi.sel_SPINNER_TIPO_DOC_IDENTIDAD();
            jsonObjectPersona = stockAgenteRestApi.sel_SPINNER_TIPO_PERSONA();
            jsonObjectCategoriaEstable = stockAgenteRestApi.sel_SPINNER_CATEGORIA_ESTABLECIMIENTO();
            //SetEstablecimiento
            setTipoEstablecimiento(jsonObjectEstablecimiento);
            //SetIdentidad
            setTipoDocIdentidad(jsonObjectIdentidad);
            //SetPersona
            setTipoPersona(jsonObjectPersona);
            //---------
            //Set Categoria Establecimiento
            setCatEstablecimeinto(jsonObjectCategoriaEstable);
            Log.d("ESTABLECIMIENTO", "" + jsonObjectEstablecimiento.toString());
            Log.d("IDENTIDAD", "" + jsonObjectIdentidad.toString());
            Log.d("PERSONA", "" + jsonObjectPersona.toString());
            Log.d("CATEGORIAESTABLEC", "" + jsonObjectCategoriaEstable.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    #######
    */

    private boolean setTipoEstablecimiento(JSONObject object) {
        try {
            JSONArray jsonArray = object.getJSONArray("Value");
            JSONObject jsonObject = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                dbAdapter_tipo_establecimiento.creatTipoEstablec(jsonObject.getInt("TieITipEstId"), jsonObject.getString("TieVDescripcion"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }


    /*##################*/

    /*
    #######
    */

    private boolean setCatEstablecimeinto(JSONObject object) {
        try {
            JSONArray jsonArray = object.getJSONArray("Value");
            JSONObject jsonObject = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                dbAdapter_categoria_establecimiento.createCatEstablecimiento(jsonObject.getInt("CateICatEstablecimientoId"), jsonObject.getString("CateVDescripcion"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }


    /*##################*/

    /*
    #######
    */

    private boolean setTipoPersona(JSONObject object) {
        try {
            JSONArray jsonArray = object.getJSONArray("Value");
            JSONObject jsonObject = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                dbAdapter_tipo_persona.createTipoPersona(jsonObject.getInt("TperITipoPersonaId"), jsonObject.getString("TperVDescripcion"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

     /*
    ####### Persona
    */

    private boolean setTipoDocIdentidad(JSONObject object) {
        try {
            JSONArray jsonArray = object.getJSONArray("Value");
            JSONObject jsonObject = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                dbAdapter_tipo_doc_identidad.createTipoDocIden(jsonObject.getInt("TdiTipoDocIdentidadId"), jsonObject.getString("TdiVDescripcion"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }




    /*##################*/
}
