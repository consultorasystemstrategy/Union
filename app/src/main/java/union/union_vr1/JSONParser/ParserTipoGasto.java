package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.TipoGasto;
import union.union_vr1.Sqlite.Constants;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ParserTipoGasto {

    public ParserTipoGasto() {
        super();
    }

    public ArrayList<TipoGasto> parserTipoGasto(JSONObject object)
    {
        ArrayList<TipoGasto> arrayList=new ArrayList<TipoGasto>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new TipoGasto(
                        jsonObj.getInt("TgasITipoGastoId"),
                        jsonObj.getString("TgasVDescripcion"),
                        Constants._IMPORTADO
                        ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseTipoGasto", e.getMessage());
        }
        return arrayList;
    }
}
