package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.FormaPago;
import union.union_vr1.Objects.ModalidadCredito;
import union.union_vr1.Sqlite.Constants;

/**
 * Created by Usuario on 10/02/2016.
 */
public class ParserFormaPago {
    private static String TAG =  ParserFormaPago.class.getSimpleName();
    public ParserFormaPago() {
        super();
    }

    public ArrayList<FormaPago> parserFormaPago(JSONObject object, int liquidacion, String fecha)
    {
        ArrayList<FormaPago> arrayList=new ArrayList<FormaPago>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new FormaPago(
                        jsonObj.getInt("Id"),
                        jsonObj.getString("Descripcion"),
                        jsonObj.getInt("Selected"),
                        Constants._IMPORTADO,
                        liquidacion,
                        fecha
                ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "JSONParser => parseFormaPago"+e.getMessage());
        }
        return arrayList;
    }
}
