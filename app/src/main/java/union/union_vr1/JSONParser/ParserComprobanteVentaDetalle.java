package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.ComprobanteVentaDetalle;
import union.union_vr1.Sqlite.Constants;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ParserComprobanteVentaDetalle {

    public ParserComprobanteVentaDetalle() {
        super();
    }

    public ArrayList<ComprobanteVentaDetalle> parserComprobanteVentaDetalle(JSONObject object)
    {
        ArrayList<ComprobanteVentaDetalle> arrayList=new ArrayList<ComprobanteVentaDetalle>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new ComprobanteVentaDetalle(
                        jsonObj.getInt("idComprobVentDetalle"),
                        jsonObj.getInt("ProIProductoId"),
                        jsonObj.getString("ProVNombre"),
                        jsonObj.getInt("ComdICantidad"),
                        jsonObj.getDouble("importe"),
                        jsonObj.getDouble("costoVenta"),
                        jsonObj.getDouble("precioUnitario"),
                        jsonObj.getString("PromedioVenta"),
                        jsonObj.getString("CantDevolucion"),
                        jsonObj.getInt("UniIValor"),
                        Constants._IMPORTADO
                        ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseComprobanteVentaDetalle", e.getMessage());
        }
        return arrayList;
    }
}
