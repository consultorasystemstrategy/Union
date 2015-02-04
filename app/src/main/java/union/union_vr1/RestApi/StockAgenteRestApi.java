package union.union_vr1.RestApi;

import android.util.Log;
import android.widget.Toast;

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
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

    public JSONObject CreateComprobanteVenta(String serie,int numDoc,int idFormaPago,String fechaDoc,double baseImponible,double igv,double total,int idTipoCompro,int idAgenteVenta,int anulado,double saldo,int idLiquidacion,int idTipoVenta,String codigoERP,int idEstablec,int idUsuario) throws Exception {
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
        p.put("idUsuario",mapObject(idUsuario));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }


    public JSONObject CreateInsertarCaja(int idLiquidacionCaja,int idCategoriaMovimiento,double importeDecimal,int estado,String fecha,String referencia,int idusuario,int idcomprbante,int idPlanPago,int idPlanPagoDetalle) throws Exception {
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
        p.put("idPlanPago",mapObject(idPlanPago));
        p.put("idPlanPagoDetalle",mapObject(idPlanPagoDetalle));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject CreateComprobanteVentaDetalle(int p_ComdBIComprobanteVentaId,int p_ComdIProductoId,int p_ComdICantidad,double p_ComdDOImporte,int p_ComIUsuarioId,double p_ComdDOCostoVenta,double p_ComdDOPrecioUnitario,int p_valor) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CreateComprobanteVentaDetalle");
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
    public JSONObject CreatePlanPagoDetalleExp(int idPlanPago,String fecha,double montoPagar,int idUsuario,String fechaCobro) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CreatePlanPagoDetalleExp");
        p.put("idPlanPago",mapObject(idPlanPago));
        p.put("fecha",mapObject(fecha));
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

    public JSONObject GetEstablecimeintoXRuta(int idCajaLiqui,String fecha,int idAgente) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetEstablecimeintoXRuta");
        p.put("idCajaLiqui",mapObject(idCajaLiqui));
        p.put("fecha",mapObject(fecha));
        p.put("idAgente",mapObject(idAgente));
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

    public JSONObject CreateInformeGastos(int idLiquidacionCaja,double total,String fecha,int idUsuario,int idTipoGasto,double subtotal,double igv,String referencia,int idprocedenciaGasto) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CreateInformeGastos");
        p.put("idLiquidacionCaja",mapObject(idLiquidacionCaja));
        p.put("total",mapObject(total));
        p.put("fecha",mapObject(fecha));
        p.put("idUsuario",mapObject(idUsuario));
        p.put("idTipoGasto",mapObject(idTipoGasto));
        p.put("subtotal",mapObject(subtotal));
        p.put("igv",mapObject(igv));
        p.put("referencia",mapObject(referencia));
        p.put("idprocedenciaGasto",mapObject(idprocedenciaGasto));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject CreateDevoluciones(int idProducto,int idCategoriaOpe,int idTipoOp,int cantidad,int idEmpresa,int idAgenteVenta,int idGuiaRemision,String referencia,String lote,int idLiquidacion,double total,int idComprobanteVenta,int idTipoDev,int unidad,int idEstablec,int cantidadDev,int categoriaDev,double importeDev,String fechaDev,String horaDev) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CreateDevoluciones");
        p.put("idProducto",mapObject(idProducto));
        p.put("idCategoriaOpe",mapObject(idCategoriaOpe));
        p.put("idTipoOp",mapObject(idTipoOp));
        p.put("cantidad",mapObject(cantidad));
        p.put("idEmpresa",mapObject(idEmpresa));
        p.put("idAgenteVenta",mapObject(idAgenteVenta));
        p.put("idGuiaRemision",mapObject(idGuiaRemision));
        p.put("referencia",mapObject(referencia));
        p.put("lote",mapObject(lote));
        p.put("idLiquidacion",mapObject(idLiquidacion));
        p.put("total",mapObject(total));
        p.put("idComprobanteVenta",mapObject(idComprobanteVenta));
        p.put("idTipoDev",mapObject(idTipoDev));
        p.put("unidad",mapObject(unidad));
        p.put("idEstablec",mapObject(idEstablec));
        p.put("cantidadDev",mapObject(cantidadDev));
        p.put("categoriaDev",mapObject(categoriaDev));
        p.put("importeDev",mapObject(importeDev));
        p.put("fechaDev",mapObject(fechaDev));
        p.put("horaDev",mapObject(horaDev));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject UpdateEstadoEstablecimiento(int idEstablecimiento,int idLiquidacion,int estado,int estadoNoAtencion) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "UpdateEstadoEstablecimiento");
        p.put("idEstablecimiento",mapObject(idEstablecimiento));
        p.put("idLiquidacion",mapObject(idLiquidacion));
        p.put("estado",mapObject(estado));
        p.put("estadoNoAtencion",mapObject(estadoNoAtencion));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject CreateHeaderDevoluciones(int id,int idEmpresa,int idAgenteVenta,int TipoMovimiento,int subTotal,String idAndroid) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CreateHeaderDevoluciones");
        p.put("id",mapObject(id));
        p.put("idEmpresa",mapObject(idEmpresa));
        p.put("idAgenteVenta",mapObject(idAgenteVenta));
        p.put("TipoMovimiento",mapObject(TipoMovimiento));
        p.put("subTotal",mapObject(subTotal));
        p.put("idAndroid",mapObject(idAndroid));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject GetSolicitudAutorizacionEstablecimiento(int idEstablecimiento) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetSolicitudAutorizacionEstablecimiento");
        p.put("idEstablecimiento",mapObject(idEstablecimiento));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject CreateSolicitudAutorizacionCreditoExp(int idAgente,String idMotivoSolicitud,int idEstablecimiento,int estadoSolicitud,String observacion,String referencia,int idUsuario,double montoCredito,int vigenciaCredito) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CreateSolicitudAutorizacionCreditoExp");
        p.put("idAgente",mapObject(idAgente));
        p.put("idMotivoSolicitud",mapObject(idMotivoSolicitud));
        p.put("idEstablecimiento",mapObject(idEstablecimiento));
        p.put("estadoSolicitud",mapObject(estadoSolicitud));
        p.put("observacion",mapObject(observacion));
        p.put("referencia",mapObject(referencia));
        p.put("idUsuario",mapObject(idUsuario));
        p.put("montoCredito",mapObject(montoCredito));
        p.put("vigenciaCredito",mapObject(vigenciaCredito));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject CreateSolicitudAutorizacionCobro(int idAgente,int motivoSolicitud,int idEStablecimiento,int estadoSolicitud,String referencia,double montoCreditoPagado,double montoCreditoPagar,int usuario,int idAutorizacion) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CreateSolicitudAutorizacionCobro");
        p.put("idAgente",mapObject(idAgente));
        p.put("motivoSolicitud",mapObject(motivoSolicitud));
        p.put("idEStablecimiento",mapObject(idEStablecimiento));
        p.put("estadoSolicitud",mapObject(estadoSolicitud));
        p.put("referencia",mapObject(referencia));
        p.put("montoCreditoPagado",mapObject(montoCreditoPagado));
        p.put("montoCreditoPagar",mapObject(montoCreditoPagar));
        p.put("usuario",mapObject(usuario));
        p.put("idAutorizacion",mapObject(idAutorizacion));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
}
