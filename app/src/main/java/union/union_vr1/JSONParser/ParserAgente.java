package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.Agente;
import union.union_vr1.Sqlite.Constants;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ParserAgente {
    public ParserAgente() {
        super();
    }

    public ArrayList<Agente> parserAgente(JSONObject object)
    {
        ArrayList<Agente> arrayList=new ArrayList<Agente>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new Agente(
                        jsonObj.getInt("idAgente"),
                        jsonObj.getInt("idEmpresa"),
                        jsonObj.getInt("idUsuario"),
                        jsonObj.getString("nombre"),
                        jsonObj.getString("usuario"),
                        jsonObj.getString("clave"),
                        jsonObj.getInt("idLiquidacion"),
                        jsonObj.getDouble("kmInicial"),
                        jsonObj.getDouble("kmFinal"),
                        jsonObj.getString("nombreRuta"),
                        jsonObj.getInt("nroBodegas"),
                        jsonObj.getString("serieBoleta"),
                        jsonObj.getString("serieFactura"),
                        jsonObj.getString("serieRPP"),
                        jsonObj.getInt("correlativoBoleta"),
                        jsonObj.getInt("correlativoFactura"),
                        jsonObj.getInt("correlativoRPP"),
                        Constants._IMPORTADO
                ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseAgente", e.getMessage());
        }
        return arrayList;
    }
    public ArrayList<Agente> parserAgenteDatos(JSONObject object)
    {
        ArrayList<Agente> arrayList=new ArrayList<Agente>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new Agente(
                        jsonObj.getInt("AgenteVentaId"),
                        jsonObj.getString("nombre"),
                        jsonObj.getString("serieBoleta"),
                        jsonObj.getString("serieFactura"),
                        jsonObj.getInt("correlativoBoleta"),
                        jsonObj.getInt("correlativoFactura"),
                        "0",
                        1,
                        Constants._IMPORTADO,
                        jsonObj.getInt("Liquidacion")
                ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseAgente", e.getMessage());
        }
        return arrayList;
    }



}
