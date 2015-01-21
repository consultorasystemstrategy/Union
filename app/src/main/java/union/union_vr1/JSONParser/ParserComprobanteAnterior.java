package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.ComprobanteAnterior;
import union.union_vr1.Sqlite.Constants;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ParserComprobanteAnterior {
    public ParserComprobanteAnterior() {
        super();
    }

    public ArrayList<ComprobanteAnterior> parserComprobanteAnterior(JSONObject object)
    {
        ArrayList<ComprobanteAnterior> arrayList=new ArrayList<ComprobanteAnterior>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new ComprobanteAnterior(
                        jsonObj.getInt("EstIEstablecimientoId"),
                        jsonObj.getInt("ProIProductoId"),
                        jsonObj.getString("ProVNombre"),
                        jsonObj.getInt("ComdICantidad"),
                        jsonObj.getString("PromedioVenta"),
                        jsonObj.getString("CantDevolucion"),
                        jsonObj.getInt("UniIValor"),
                        jsonObj.getInt("ComIAgenteVentaId"),
                        Constants._IMPORTADO
                        ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseComprobanteAnterior", e.getMessage());
        }
        return arrayList;
    }
}
