package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.Transferencias;

/**
 * Created by Usuario on 23/12/2015.
 */
public class ParserTransferencias {

    private static final String TAG = ParserTransferencias.class.getSimpleName();
    public ParserTransferencias() {
        super();
    }

    public ArrayList<Transferencias> ParserTransferencias(JSONObject object)
    {
        ArrayList<Transferencias> arrayList=new ArrayList<Transferencias>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new Transferencias(
                        jsonObj.getString("IdTransferencia"),
                        jsonObj.getString("Codigo"),
                        jsonObj.getString("Producto"),
                        jsonObj.getInt("Cantidad"),
                        jsonObj.getString("DescripcionTransferencia"),
                        jsonObj.getInt("AlmacenId")
                ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "JSONParser => parseTransferencias : "+ e.getMessage());
        }
        return arrayList;
    }
}
