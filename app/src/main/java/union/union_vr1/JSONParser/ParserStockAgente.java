package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.StockAgente;
import union.union_vr1.Sqlite.Constants;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ParserStockAgente {

    public ParserStockAgente() {
        super();
    }

    public ArrayList<StockAgente> parserStockAgente(JSONObject object)
    {
        ArrayList<StockAgente> arrayList=new ArrayList<StockAgente>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new StockAgente(
                        jsonObj.getInt("idProducto"),
                        jsonObj.getString("nombreProducto"),
                        jsonObj.getString("codigo"),
                        jsonObj.getString("codigoBarras"),
                        jsonObj.getInt("stockInicial"),
                        jsonObj.getInt("stockFinal"),
                        jsonObj.getInt("stockDisponible"),
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        jsonObj.getInt("idAgente"),
                        Constants._IMPORTADO
                        ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseStockAgente", e.getMessage());
        }
        return arrayList;
    }
}
