package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.InformeGastos;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ParserInformeGastos {

    public ParserInformeGastos() {
        super();
    }

    public ArrayList<InformeGastos> parserInformeGastos(JSONObject object)
    {
        ArrayList<InformeGastos> arrayList=new ArrayList<InformeGastos>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                /*arrayList.add(new InformeGastos(
                        jsonObj.getInt("idProducto"),
                        ));*/
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseInformeGastos", e.getMessage());
        }
        return arrayList;
    }
}
