package union.union_vr1.JSONParser;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.Precio;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Utils.MyApplication;
import union.union_vr1.Vistas.VMovil_Online_Pumovil;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ParserPrecio {


    private DbAdapter_Temp_Session session;
    private Activity mainActivity;
    public ParserPrecio(Activity mainActivity) {
        super();
        this.mainActivity = mainActivity;

        session = new DbAdapter_Temp_Session(mainActivity);
        session.open();

    }

    public ArrayList<Precio> parserPrecio(JSONObject object)
    {
        ArrayList<Precio> arrayList=new ArrayList<Precio>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new Precio(
                        jsonObj.getInt("idPrecioCategoria"),
                        jsonObj.getInt("idProducto"),
                        jsonObj.getInt("idCateEstablec"),
                        jsonObj.getDouble("costoVenta"),
                        jsonObj.getDouble("costoUnidad"),
                        jsonObj.getInt("valorUnidad"),
//                        ((MyApplication) mainActivity.getApplication()).getIdAgente(),
                        session.fetchVarible(1),
                        jsonObj.getInt("desde"),
                        jsonObj.getInt("hasta"),
                        jsonObj.getString("nombreProducto"),
                        Constants._IMPORTADO
                        ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parsePrecio", e.getMessage());
        }
        return arrayList;
    }
}
