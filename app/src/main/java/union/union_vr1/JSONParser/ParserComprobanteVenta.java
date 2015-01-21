package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.ComprobanteVenta;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ParserComprobanteVenta {


    public ParserComprobanteVenta() {
        super();
    }

    public ArrayList<ComprobanteVenta> parserComprobanteVenta(JSONObject object)
    {
        ArrayList<ComprobanteVenta> arrayList=new ArrayList<ComprobanteVenta>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                /*arrayList.add(new ComprobanteVenta(
                        jsonObj.getInt("idProducto"),
                        ));*/
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseComprobanteVenta", e.getMessage());
        }
        return arrayList;
    }
}
