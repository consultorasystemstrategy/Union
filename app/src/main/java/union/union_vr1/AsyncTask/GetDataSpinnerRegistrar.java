package union.union_vr1.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;


import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.DbAdapter_Temp_DatosSpinner;

/**
 * Created by Kelvin on 23/11/2015.
 */
public class GetDataSpinnerRegistrar extends AsyncTask<String,String,String> {

    private Activity mainActivity;
    private JSONObject jsonObjectEstablecimiento = null;
    private JSONObject jsonObjectIdentidad = null;
    private JSONObject jsonObjectPersona= null;
    private JSONObject jsonObjectCategoriaEstable= null;
    private StockAgenteRestApi stockAgenteRestApi ;
    private DbAdapter_Temp_DatosSpinner dbAdapter_temp_datosSpinner;

    public GetDataSpinnerRegistrar(Activity activity){
        mainActivity=activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        dbAdapter_temp_datosSpinner = new DbAdapter_Temp_DatosSpinner(mainActivity.getApplicationContext());
        dbAdapter_temp_datosSpinner.open();
        dbAdapter_temp_datosSpinner.deleteAll();
        stockAgenteRestApi = new StockAgenteRestApi(mainActivity.getApplicationContext());
        try {
            jsonObjectEstablecimiento = stockAgenteRestApi.sel_SPINNER_TIPO_ESTABLECIMIENTO();
            jsonObjectIdentidad = stockAgenteRestApi.sel_SPINNER_TIPO_DOC_IDENTIDAD();
            jsonObjectPersona = stockAgenteRestApi.sel_SPINNER_TIPO_PERSONA();
            jsonObjectCategoriaEstable = stockAgenteRestApi.sel_SPINNER_CATEGORIA_ESTABLECIMIENTO();
            //SetEstablecimiento
            setPreferEstablecimiento(jsonObjectEstablecimiento);
            //SetIdentidad
            setPreferIdentidad(jsonObjectIdentidad);
            //SetPersona
            setPreferPersona(jsonObjectPersona);
            //---------
            //Set Categoria Establecimiento
            setPreferCatEstablec(jsonObjectCategoriaEstable);
            Log.d("ESTABLECIMIENTO", "" + jsonObjectEstablecimiento.toString());
            Log.d("IDENTIDAD",""+jsonObjectIdentidad.toString());
            Log.d("PERSONA",""+jsonObjectPersona.toString());
            Log.d("CATEGORIAESTABLEC",""+jsonObjectCategoriaEstable.toString());




        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    ####### Establecimiento
    */

    private boolean setPreferEstablecimiento(JSONObject stringMap){
        long a = dbAdapter_temp_datosSpinner.createTempSpinner(stringMap.toString(),1);

        if(a>0){
            return true;
        }else{
            return false;
        }

    }


    /*##################*/

    /*
    ####### Identidad
    */

    private boolean setPreferIdentidad(JSONObject stringMap){

        long a = dbAdapter_temp_datosSpinner.createTempSpinner(stringMap.toString(),2);
        if(a>0){
            return true;
        }else{
            return false;
        }
    }


    /*##################*/

    /*
    ####### Persona
    */

    private boolean setPreferPersona(JSONObject  stringMap){
        long a = dbAdapter_temp_datosSpinner.createTempSpinner(stringMap.toString(),3);
        if(a>0){
            return true;
        }else{
            return false;
        }
    }

     /*
    ####### Persona
    */

    private boolean setPreferCatEstablec(JSONObject  stringMap){
        long a = dbAdapter_temp_datosSpinner.createTempSpinner(stringMap.toString(),4);
        if(a>0){
            return true;
        }else{
            return false;
        }
    }




    /*##################*/
}
