package union.union_vr1.RestApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Usuario on 19/01/2015.
 */
public class StockAgenteRestApi {
    private final String urlString = "http://192.168.0.121:84/StockAgente.ashx";

    private static String convertStreamToUTF8String(InputStream stream) throws IOException {
        String result = "";
        StringBuilder sb = new StringBuilder();
        try {
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[4096];
            int readedChars = 0;
            while (readedChars != -1) {
                readedChars = reader.read(buffer);
                if (readedChars > 0)
                    sb.append(buffer, 0, readedChars);
            }
            result = sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    private String load(String contents) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(60000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        OutputStreamWriter w = new OutputStreamWriter(conn.getOutputStream());
        w.write(contents);
        w.flush();
        InputStream istream = conn.getInputStream();
        String result = convertStreamToUTF8String(istream);
        return result;
    }


    private Object mapObject(Object o) {
        Object finalValue = null;
        if (o.getClass() == String.class) {
            finalValue = o;
        }
        else if (Number.class.isInstance(o)) {
            finalValue = String.valueOf(o);
        } else if (Date.class.isInstance(o)) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss", new Locale("en", "USA"));
            finalValue = sdf.format((Date)o);
        }
        else if (Collection.class.isInstance(o)) {
            Collection<?> col = (Collection<?>) o;
            JSONArray jarray = new JSONArray();
            for (Object item : col) {
                jarray.put(mapObject(item));
            }
            finalValue = jarray;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            Method[] methods = o.getClass().getMethods();
            for (Method method : methods) {
                if (method.getDeclaringClass() == o.getClass()
                        && method.getModifiers() == Modifier.PUBLIC
                        && method.getName().startsWith("get")) {
                    String key = method.getName().substring(3);
                    try {
                        Object obj = method.invoke(o, null);
                        Object value = mapObject(obj);
                        map.put(key, value);
                        finalValue = new JSONObject(map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return finalValue;
    }

    public JSONObject Get(int idAgente) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "Get");
        p.put("idAgente",mapObject(idAgente));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject GetHistorialVentaDetalle(int idAgente) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetHistorialVentaDetalle");
        p.put("idAgente",mapObject(idAgente));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject CreateComprobanteVenta(String serie,int numDoc,int idFormaPago,String fechaDoc,double baseImponible,double igv,double total,int idTipoCompro,int idAgenteVenta,int anulado,double saldo,int idLiquidacion,int idTipoVenta,String codigoERP,int idEstablec) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CreateComprobanteVenta");
        p.put("serie",mapObject(serie));
        p.put("numDoc",mapObject(numDoc));
        p.put("idFormaPago",mapObject(idFormaPago));
        p.put("fechaDoc",mapObject(fechaDoc));
        p.put("baseImponible",mapObject(baseImponible));
        p.put("igv",mapObject(igv));
        p.put("total",mapObject(total));
        p.put("idTipoCompro",mapObject(idTipoCompro));
        p.put("idAgenteVenta",mapObject(idAgenteVenta));
        p.put("anulado",mapObject(anulado));
        p.put("saldo",mapObject(saldo));
        p.put("idLiquidacion",mapObject(idLiquidacion));
        p.put("idTipoVenta",mapObject(idTipoVenta));
        p.put("codigoERP",mapObject(codigoERP));
        p.put("idEstablec",mapObject(idEstablec));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject CreateInsertarCaja(int idLiquidacionCaja,int idCategoriaMovimiento,double importeDecimal,int estado,String fecha,String referencia,int idusuario,int idcomprbante) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CreateInsertarCaja");
        p.put("idLiquidacionCaja",mapObject(idLiquidacionCaja));
        p.put("idCategoriaMovimiento",mapObject(idCategoriaMovimiento));
        p.put("importeDecimal",mapObject(importeDecimal));
        p.put("estado",mapObject(estado));
        p.put("fecha",mapObject(fecha));
        p.put("referencia",mapObject(referencia));
        p.put("idusuario",mapObject(idusuario));
        p.put("idcomprbante",mapObject(idcomprbante));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject CraeteComprobanteVentaDetalle(int p_ComdBIComprobanteVentaId,int p_ComdIProductoId,int p_ComdICantidad,int p_ComdDOImporte,int p_ComIUsuarioId,double p_ComdDOCostoVenta,double p_ComdDOPrecioUnitario,int p_valor) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CraeteComprobanteVentaDetalle");
        p.put("p_ComdBIComprobanteVentaId",mapObject(p_ComdBIComprobanteVentaId));
        p.put("p_ComdIProductoId",mapObject(p_ComdIProductoId));
        p.put("p_ComdICantidad",mapObject(p_ComdICantidad));
        p.put("p_ComdDOImporte",mapObject(p_ComdDOImporte));
        p.put("p_ComIUsuarioId",mapObject(p_ComIUsuarioId));
        p.put("p_ComdDOCostoVenta",mapObject(p_ComdDOCostoVenta));
        p.put("p_ComdDOPrecioUnitario",mapObject(p_ComdDOPrecioUnitario));
        p.put("p_valor",mapObject(p_valor));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject CraetePlanPagoDetalleExp(int idPlanPago,String fecha,double importeBase,double montoPagar,int idUsuario,String fechaCobro) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CraetePlanPagoDetalleExp");
        p.put("idPlanPago",mapObject(idPlanPago));
        p.put("fecha",mapObject(fecha));
        p.put("importeBase",mapObject(importeBase));
        p.put("montoPagar",mapObject(montoPagar));
        p.put("idUsuario",mapObject(idUsuario));
        p.put("fechaCobro",mapObject(fechaCobro));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject CreatePlanPagoExp(int p_PlpBIComprobanteVentaId,String p_PlpDTFechaPago,double p_PlpDOPorcentajeInteresMes,int p_PlpIUsuarioId,int p_FormaPago,int p_tipoVenta) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CreatePlanPagoExp");
        p.put("p_PlpBIComprobanteVentaId",mapObject(p_PlpBIComprobanteVentaId));
        p.put("p_PlpDTFechaPago",mapObject(p_PlpDTFechaPago));
        p.put("p_PlpDOPorcentajeInteresMes",mapObject(p_PlpDOPorcentajeInteresMes));
        p.put("p_PlpIUsuarioId",mapObject(p_PlpIUsuarioId));
        p.put("p_FormaPago",mapObject(p_FormaPago));
        p.put("p_tipoVenta",mapObject(p_tipoVenta));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject GetAgenteVenta(String usuario,String clave) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetAgenteVenta");
        p.put("usuario",mapObject(usuario));
        p.put("clave",mapObject(clave));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject GetTipoGasto() throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetTipoGasto");
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject GetComprobanteVentaDetalle_Env() throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetComprobanteVentaDetalle_Env");
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject GetComprobanteVentaEnv(int idEstablec) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetComprobanteVentaEnv");
        p.put("idEstablec",mapObject(idEstablec));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject GetEstablecimeintoXRuta(int idCajaLiqui,String fecha) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetEstablecimeintoXRuta");
        p.put("idCajaLiqui",mapObject(idCajaLiqui));
        p.put("fecha",mapObject(fecha));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject GetHistorialCobrosPendientes() throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetHistorialCobrosPendientes");
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject GetPrecioCategoria(int liquidacionCajaId,int StkagIAgenteId) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetPrecioCategoria");
        p.put("liquidacionCajaId",mapObject(liquidacionCajaId));
        p.put("StkagIAgenteId",mapObject(StkagIAgenteId));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject GetStockAgente(int idAgenteVenta) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetStockAgente");
        p.put("idAgenteVenta",mapObject(idAgenteVenta));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
}
