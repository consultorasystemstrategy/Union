package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.HistorialVentaDetalles;
import union.union_vr1.Sqlite.Constants;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ParserHistorialVentaDetalles {

    public ParserHistorialVentaDetalles() {
        super();
    }

    public ArrayList<HistorialVentaDetalles> parserHistorialVentaDetalles(JSONObject object)
    {
        ArrayList<HistorialVentaDetalles> arrayList=new ArrayList<HistorialVentaDetalles>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new HistorialVentaDetalles(
                        jsonObj.getString("idComprobanteVentaDetalle"),
                        jsonObj.getInt("idComprobanteVenta"),
                        jsonObj.getInt("idEstablec"),
                        jsonObj.getInt("idProducto"),
                        0,
                        0,
                        jsonObj.getString("comprobanteVenta"),
                        jsonObj.getString("nombreEstablecimiento"),
                        jsonObj.getString("nombreProducto"),
                        jsonObj.getInt("cantidad"),
                        jsonObj.getDouble("importe"),
                        jsonObj.getString("fecha"),
                        null,
                        //TRAYENDO EL CAMPO PRECIO UNITARIO* CORREGIR Y PREGUNTARLE A JOSMAR
                        jsonObj.getInt("precioUnitario"),
                        0,
                        0,
                        0,
                        0.0,
                        null,
                        null,
                        null,
                        null,
                        jsonObj.getInt("estado"),
                        jsonObj.getInt("idAgente"),
                        0,
                        0,
                        0.0,
                        null,
                        null,
                        Constants._IMPORTADO
                        ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseHistorialVentaDetalles", e.getMessage());
        }
        return arrayList;
    }
}
