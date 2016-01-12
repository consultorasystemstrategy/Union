package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.ModalidadCredito;
import union.union_vr1.Objects.TipoGasto;
import union.union_vr1.Sqlite.Constants;

/**
 * Created by Usuario on 12/01/2016.
 */
public class ParserModalidadCredito {
    public ParserModalidadCredito() {
        super();
    }

    public ArrayList<ModalidadCredito> parserModalidadCredito(JSONObject object)
    {
        ArrayList<ModalidadCredito> arrayList=new ArrayList<ModalidadCredito>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new ModalidadCredito(
                        jsonObj.getInt("Id"),
                        jsonObj.getString("Descripcion"),
                        jsonObj.getString("DiasCredito"),
                        Constants._IMPORTADO
                ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseModalidadCredito", e.getMessage());
        }
        return arrayList;
    }
}
