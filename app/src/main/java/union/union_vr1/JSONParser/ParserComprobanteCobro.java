package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.ComprobanteCobro;
import union.union_vr1.Sqlite.Constants;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ParserComprobanteCobro {
    public ParserComprobanteCobro() {
        super();
    }

    public ArrayList<ComprobanteCobro> parserComprobanteCobro(JSONObject object)
    {
        ArrayList<ComprobanteCobro> arrayList=new ArrayList<ComprobanteCobro>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new ComprobanteCobro(
                        jsonObj.getString("idComprobanteCobro"),
                        jsonObj.getInt("idEstablec"),
                        jsonObj.getInt("idComprobanteVenta"),
                        jsonObj.getInt("idPlanPago"),
                        jsonObj.getInt("idPlanpagoDetalle"),
                        jsonObj.getString("descripcion"),
                        jsonObj.getString("Doc"),
                        jsonObj.getString("FechaCobro"),
                        jsonObj.getDouble("montoaPagar"),
                        null, null, 0.0,
                        jsonObj.getInt("estado")
                        , 0, 0, null,
                        Constants._IMPORTADO
                        ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseComprobanteCobro", e.getMessage());
        }
        return arrayList;
    }
}
