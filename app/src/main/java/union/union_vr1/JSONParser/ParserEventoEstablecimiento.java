package union.union_vr1.JSONParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import union.union_vr1.Objects.EventoEstablecimiento;
import union.union_vr1.Sqlite.Constants;

/**
 * Created by Usuario on 19/01/2015.
 */
public class ParserEventoEstablecimiento {

    public ParserEventoEstablecimiento() {
        super();
    }

    public ArrayList<EventoEstablecimiento> parserEventoEstablecimiento(JSONObject object)
    {
        ArrayList<EventoEstablecimiento> arrayList=new ArrayList<EventoEstablecimiento>();
        try {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObj=jsonArray.getJSONObject(i);
                int modalidadCredito = jsonObj.getInt("CliIModalidadCreditoId");
                int diasCredito = 0;
                switch (modalidadCredito){
                    case 1:
                        diasCredito = 3;
                        break;
                    case 2:
                        diasCredito = 7;
                        break;
                    case 3:
                        diasCredito = 15;
                        break;
                    case 4:
                        diasCredito = 31;
                        break;
                    case 5:
                        diasCredito = 0;
                        break;
                    default:
                        diasCredito = 0;
                        break;
                }

                arrayList.add(new EventoEstablecimiento(
                        jsonObj.getInt("EveeIEstablecimientoId"),
                        jsonObj.getInt("EstICatEstablecimientoId"),
                        jsonObj.getInt("TdiTipoDocIdentidadId"),
                        jsonObj.getInt("EveeIAtencionEstablecimientoId"),
                        jsonObj.getString("EstVDescripcion"),
                        jsonObj.getString("PerVNombres"),
                        jsonObj.getString("CliVDocidentidad"),
                        jsonObj.getInt("EstIOrden"),
                        jsonObj.getInt("SurtidoStockAnterior"),
                        jsonObj.getInt("SurtidoVentaAnterior"),
                        jsonObj.getDouble("MocrIDescripcion"),
                        diasCredito,
                        0,
                        null,
                        jsonObj.getInt("LiqIAgenteVentaId"),
                        Constants._IMPORTADO,
                        jsonObj.getString("EstVCodigoBarra"),
                        jsonObj.getString("Direccion"),
                        jsonObj.getInt("EstadoId"),
                        jsonObj.getString("DireccionPrincipal"),
                        jsonObj.getString("Latitud"),
                        jsonObj.getString("Longitud")
                        ));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseEventoEstablecimiento", e.getMessage());
        }
        return arrayList;
    }
}
