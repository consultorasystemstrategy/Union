package union.union_vr1.RestApi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
    //SERVIDOR LOCAL
    private final String urlStringLocal = "http://192.168.0.158/RestFull/StockAgente.ashx";
    //SERVIDOR REMOTO
        private final String urlStringRemoto = "http://190.81.172.113/RestFull/StockAgente.ashx";
    /*private final String urlStringRedInterna = "http://192.168.13.31/RestFull/StockAgente.ashx";*/
    private final String urlStringPU= "https://sidim.upeu.edu.pe/ServiciosAndroid/StockAgente.ashx";
    private final String urlInternaPU= "http://sidim.upeu.edu.pe/ServiciosAndroid/StockAgente.ashx";

    private String urlString = "http://192.168.0.158/RestFull/StockAgente.ashx";
    private Context contexto;
    private int servidorTipo=1;

    private static final String TAG = StockAgenteRestApi.class.getSimpleName();

    public StockAgenteRestApi(Context contexto) {

        this.contexto = contexto;
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(contexto);
        servidorTipo =  Integer.parseInt(SP.getString("servidorTipo", "1"));
        switch (servidorTipo){
            case 1:
                urlString = urlStringLocal;
                break;
            case 2:
                urlString = urlStringRemoto;
                break;
            case 3:
                urlString = urlStringPU;
                break;
            case 4:
                urlString = urlInternaPU;
                break;
            default:
                break;
        }
        Log.d(TAG, urlString);

        if (servidorTipo==1){
        }else if (servidorTipo==2){
        }
    }

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
    public JSONObject InsCobroManual(int vbint_LiquidacionId,int vint_CategoriaMovimientoId,double vdou_Importe,String vstr_FechaHora,String vstr_Referencia,int vint_UsuarioId,String vstr_Serie,int vint_Numero) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "InsCobroManual");
        p.put("vbint_LiquidacionId",mapObject(vbint_LiquidacionId));
        p.put("vint_CategoriaMovimientoId",mapObject(vint_CategoriaMovimientoId));
        p.put("vdou_Importe",mapObject(vdou_Importe));
        p.put("vstr_FechaHora",mapObject(vstr_FechaHora));
        p.put("vstr_Referencia",mapObject(vstr_Referencia));
        p.put("vint_UsuarioId",mapObject(vint_UsuarioId));
        p.put("vstr_Serie",mapObject(vstr_Serie));
        p.put("vint_Numero",mapObject(vint_Numero));
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


    public JSONObject CreateInsertarCaja(int idLiquidacionCaja,int idCategoriaMovimiento,double importeDecimal,int estado,String fecha,String referencia,int idusuario,int idcomprobante,int idPlanPago,int idPlanPagoDetalle) throws Exception {
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
        p.put("idcomprobante",mapObject(idcomprobante));
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

    public JSONObject GetAgenteVenta(String usuario,String clave,String fecha) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetAgenteVenta");
        p.put("usuario",mapObject(usuario));
        p.put("clave",mapObject(clave));
        p.put("fecha",mapObject(fecha));
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
    public JSONObject CreateDetalleOperacionesCD(int idProducto,int idCategoriaOpe,int idTipoOp,int cantidad,int idEmpresa,int idAgenteVenta,String idGuiaRemision,String referencia,String lote,int idLiquidacion,double total,int idComprobanteVenta,int idTipoDev,int unidad,int idEstablec) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CreateDetalleOperacionesCD");
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
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject InsCobro(int vint_ComprobanteId) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "InsCobro");
        p.put("vint_ComprobanteId",mapObject(vint_ComprobanteId));
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
    public JSONObject UpdateEstadoEstablecimiento(int idEstablecimiento,int idLiquidacion,int estado,int estadoNoAtencion,String hora) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "UpdateEstadoEstablecimiento");
        p.put("idEstablecimiento",mapObject(idEstablecimiento));
        p.put("idLiquidacion",mapObject(idLiquidacion));
        p.put("estado",mapObject(estado));
        p.put("estadoNoAtencion",mapObject(estadoNoAtencion));
        p.put("hora",mapObject(hora));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject CreateHeaderDevoluciones(int id,int idEmpresa,int idAgenteVenta,int TipoMovimiento,Double subTotal,String idAndroid) throws Exception {
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
    public JSONObject GetConsultarAutorizacion(int idEstablecimiento) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetConsultarAutorizacion");
        p.put("idEstablecimiento",mapObject(idEstablecimiento));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject GetConsultarPlan_Distribucion(int idAgente) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetConsultarPlan_Distribucion");
        p.put("idAgente",mapObject(idAgente));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject ObtenerStockAgente(String numGuia,int AgenteId) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "ObtenerStockAgente");
        p.put("numGuia",mapObject(numGuia));
        p.put("AgenteId",mapObject(AgenteId));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject InsLocalizacionAgente(int vint_AgenteId,String vstr_Latitud,String vstr_Longitud) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "InsLocalizacionAgente");
        p.put("vint_AgenteId",mapObject(vint_AgenteId));
        p.put("vstr_Latitud",mapObject(vstr_Latitud));
        p.put("vstr_Longitud",mapObject(vstr_Longitud));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject UpdateEstadoEstablecimiento2(int idEstablecimiento,int idLiquidacion,int estado,int estadoNoAtencion,String fechahora) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "UpdateEstadoEstablecimiento2");
        p.put("idEstablecimiento",mapObject(idEstablecimiento));
        p.put("idLiquidacion",mapObject(idLiquidacion));
        p.put("estado",mapObject(estado));
        p.put("estadoNoAtencion",mapObject(estadoNoAtencion));
        p.put("fechahora",mapObject(fechahora));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject GetInventarioAnterior(int vint_AgentaVentaId,String vstr_fecha) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetInventarioAnterior");
        p.put("vint_AgentaVentaId",mapObject(vint_AgentaVentaId));
        p.put("vstr_fecha",mapObject(vstr_fecha));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject InsClienteEstablecimiento(String PerVNombres,String PerVApellPaterno,String PerVApellMaterno,String PerVDocIdentidad,String PerVCelular,String PerVEmail,int PerBEstado,int PerITipoDocIdentidadId,int PerITipoPersonaId,int PerIUsuarioId,int PerIEmpresaId,String CliVCodigo,String CliVDocidentidad,int CliBEstado,int CliICategoriaClienteId,int CliIAgenteVentaId,int CliIUsuarioId,int CliBEstadoAtencion,double CliDOMontoCredito,int CliIModalidadCreditoId,String DirVDescripcion,String DirVDescripcionFiscal,int DirBEstado,int DirIDistritoId,String DirVTelefonoFijo,String DirVCelular1,String DirVCelular2,int DirIUsuarioId,int DirBEstablecimientoAsociado,int DirBDireccionFiscal,String EstVDescripcion,int EstBEstado,int EstIUsuarioId,int EstIPorcentajeDevolucion,int EstICatEstablecimientoId,String EstVExhibidor,double EstDOMontoCompra,int EstITipEstId,String LocVLongitude,String LocVLatitude,int LocBEstado,int LocIUsuarioId,int EveeIAtencionEstablecimientoId,int EveeBILiquidacionCajaId,int EveeIMotivoNoAtendidoId,int EveeIRutaId,String EstVKeyFireBase) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "InsClienteEstablecimiento");
        p.put("PerVNombres",mapObject(PerVNombres));
        p.put("PerVApellPaterno",mapObject(PerVApellPaterno));
        p.put("PerVApellMaterno",mapObject(PerVApellMaterno));
        p.put("PerVDocIdentidad",mapObject(PerVDocIdentidad));
        p.put("PerVCelular",mapObject(PerVCelular));
        p.put("PerVEmail",mapObject(PerVEmail));
        p.put("PerBEstado",mapObject(PerBEstado));
        p.put("PerITipoDocIdentidadId",mapObject(PerITipoDocIdentidadId));
        p.put("PerITipoPersonaId",mapObject(PerITipoPersonaId));
        p.put("PerIUsuarioId",mapObject(PerIUsuarioId));
        p.put("PerIEmpresaId",mapObject(PerIEmpresaId));
        p.put("CliVCodigo",mapObject(CliVCodigo));
        p.put("CliVDocidentidad",mapObject(CliVDocidentidad));
        p.put("CliBEstado",mapObject(CliBEstado));
        p.put("CliICategoriaClienteId",mapObject(CliICategoriaClienteId));
        p.put("CliIAgenteVentaId",mapObject(CliIAgenteVentaId));
        p.put("CliIUsuarioId",mapObject(CliIUsuarioId));
        p.put("CliBEstadoAtencion",mapObject(CliBEstadoAtencion));
        p.put("CliDOMontoCredito",mapObject(CliDOMontoCredito));
        p.put("CliIModalidadCreditoId",mapObject(CliIModalidadCreditoId));
        p.put("DirVDescripcion",mapObject(DirVDescripcion));
        p.put("DirVDescripcionFiscal",mapObject(DirVDescripcionFiscal));
        p.put("DirBEstado",mapObject(DirBEstado));
        p.put("DirIDistritoId",mapObject(DirIDistritoId));
        p.put("DirVTelefonoFijo",mapObject(DirVTelefonoFijo));
        p.put("DirVCelular1",mapObject(DirVCelular1));
        p.put("DirVCelular2",mapObject(DirVCelular2));
        p.put("DirIUsuarioId",mapObject(DirIUsuarioId));
        p.put("DirBEstablecimientoAsociado",mapObject(DirBEstablecimientoAsociado));
        p.put("DirBDireccionFiscal",mapObject(DirBDireccionFiscal));
        p.put("EstVDescripcion",mapObject(EstVDescripcion));
        p.put("EstBEstado",mapObject(EstBEstado));
        p.put("EstIUsuarioId",mapObject(EstIUsuarioId));
        p.put("EstIPorcentajeDevolucion",mapObject(EstIPorcentajeDevolucion));
        p.put("EstICatEstablecimientoId",mapObject(EstICatEstablecimientoId));
        p.put("EstVExhibidor",mapObject(EstVExhibidor));
        p.put("EstDOMontoCompra",mapObject(EstDOMontoCompra));
        p.put("EstITipEstId",mapObject(EstITipEstId));
        p.put("LocVLongitude",mapObject(LocVLongitude));
        p.put("LocVLatitude",mapObject(LocVLatitude));
        p.put("LocBEstado",mapObject(LocBEstado));
        p.put("LocIUsuarioId",mapObject(LocIUsuarioId));
        p.put("EveeIAtencionEstablecimientoId",mapObject(EveeIAtencionEstablecimientoId));
        p.put("EveeBILiquidacionCajaId",mapObject(EveeBILiquidacionCajaId));
        p.put("EveeIMotivoNoAtendidoId",mapObject(EveeIMotivoNoAtendidoId));
        p.put("EveeIRutaId",mapObject(EveeIRutaId));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject GetDatosAgente(String vstr_Usuario,String vstr_pass) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "GetDatosAgente");
        p.put("vstr_Usuario",mapObject(vstr_Usuario));
        p.put("vstr_pass",mapObject(vstr_pass));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        Log.d(TAG, "STRING R: "+ r);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject InsAbrirCaja(int vint_AgenteId,int vint_KilometrajeInicial) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "InsAbrirCaja");
        p.put("vint_AgenteId",mapObject(vint_AgenteId));
        p.put("vint_KilometrajeInicial",mapObject(vint_KilometrajeInicial));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject UpdCerrarCaja(int vint_LiquidacionCajaId,double vdou_LiqDOIngresos,double vdou_LiqDOGastos,double vdou_LiqDOMontoARendir,int vint_KilometrajeFinal) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "UpdCerrarCaja");
        p.put("vint_LiquidacionCajaId",mapObject(vint_LiquidacionCajaId));
        p.put("vdou_LiqDOIngresos",mapObject(vdou_LiqDOIngresos));
        p.put("vdou_LiqDOGastos",mapObject(vdou_LiqDOGastos));
        p.put("vdou_LiqDOMontoARendir",mapObject(vdou_LiqDOMontoARendir));
        p.put("vint_KilometrajeFinal",mapObject(vint_KilometrajeFinal));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject sel_SPINNER_TIPO_ESTABLECIMIENTO() throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "sel_SPINNER_TIPO_ESTABLECIMIENTO");
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject sel_SPINNER_TIPO_PERSONA() throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "sel_SPINNER_TIPO_PERSONA");
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject sel_SPINNER_TIPO_DOC_IDENTIDAD() throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "sel_SPINNER_TIPO_DOC_IDENTIDAD");
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject sel_SPINNER_CATEGORIA_ESTABLECIMIENTO() throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "sel_SPINNER_CATEGORIA_ESTABLECIMIENTO");
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject InsComprobante(int vint_ComprobanteId) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "InsComprobante");
        p.put("vint_ComprobanteId",mapObject(vint_ComprobanteId));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject InsComprobantes(int vint_LiquidacionId) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "InsComprobantes");
        p.put("vint_LiquidacionId",mapObject(vint_LiquidacionId));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject InsExportarTransferencias(int IdLiquidacion,int IdAgente) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "InsExportarTransferencias");
        p.put("IdLiquidacion",mapObject(IdLiquidacion));
        p.put("IdAgente",mapObject(IdAgente));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject fsel_ObtenerTransferencias(int vint_AgenteId,int vint_LiquidacionId) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "fsel_ObtenerTransferencias");
        p.put("vint_AgenteId",mapObject(vint_AgenteId));
        p.put("vint_LiquidacionId",mapObject(vint_LiquidacionId));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject getClientesRuta(int idAgente) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "getClientesRuta");
        p.put("idAgente",mapObject(idAgente));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject fsel_MotivoDevolucion(int vint_AgenteId) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "fsel_MotivoDevolucion");
        p.put("vint_AgenteId",mapObject(vint_AgenteId));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject fupd_EstadoClienteEstablecimiento(int vint_ClienteId,int vint_EstablecimientoId,int vint_AgenteId) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "fupd_EstadoClienteEstablecimiento");
        p.put("vint_ClienteId",mapObject(vint_ClienteId));
        p.put("vint_EstablecimientoId",mapObject(vint_EstablecimientoId));
        p.put("vint_AgenteId",mapObject(vint_AgenteId));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject fupd_ClienteEstablecimiento(int EstablecimientoId,String PerVNombres,String PerVApellPaterno,String PerVApellMaterno,String PerVDocIdentidad,String PerVCelular,String PerVEmail,int PerITipoDocIdentidadId,int PerITipoPersonaId,int PerIUsuarioId,int PerIEmpresaId,String CliVCodigo,int CliICategoriaClienteId,int CliIAgenteVentaId,int CliIUsuarioId,String DirVDescripcion,String DirVDescripcion_Fiscal,int DirBEstado,int DirIDistritoId,String DirVTelefonoFijo,String DirVCelular1,String DirBDireccionFiscal,String EstVDescripcion,int EstIUsuarioId,int EstICatEstablecimientoId,int EstITipEstId,String LocVLongitude,String LocVLatitude) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "fupd_ClienteEstablecimiento");
        p.put("EstablecimientoId",mapObject(EstablecimientoId));
        p.put("PerVNombres",mapObject(PerVNombres));
        p.put("PerVApellPaterno",mapObject(PerVApellPaterno));
        p.put("PerVApellMaterno",mapObject(PerVApellMaterno));
        p.put("PerVDocIdentidad",mapObject(PerVDocIdentidad));
        p.put("PerVCelular",mapObject(PerVCelular));
        p.put("PerVEmail",mapObject(PerVEmail));
        p.put("PerITipoDocIdentidadId",mapObject(PerITipoDocIdentidadId));
        p.put("PerITipoPersonaId",mapObject(PerITipoPersonaId));
        p.put("PerIUsuarioId",mapObject(PerIUsuarioId));
        p.put("PerIEmpresaId",mapObject(PerIEmpresaId));
        p.put("CliVCodigo",mapObject(CliVCodigo));
        p.put("CliICategoriaClienteId",mapObject(CliICategoriaClienteId));
        p.put("CliIAgenteVentaId",mapObject(CliIAgenteVentaId));
        p.put("CliIUsuarioId",mapObject(CliIUsuarioId));
        p.put("DirVDescripcion",mapObject(DirVDescripcion));
        p.put("DirVDescripcion_Fiscal",mapObject(DirVDescripcion_Fiscal));
        p.put("DirBEstado",mapObject(DirBEstado));
        p.put("DirIDistritoId",mapObject(DirIDistritoId));
        p.put("DirVTelefonoFijo",mapObject(DirVTelefonoFijo));
        p.put("DirVCelular1",mapObject(DirVCelular1));
        p.put("DirBDireccionFiscal",mapObject(DirBDireccionFiscal));
        p.put("EstVDescripcion",mapObject(EstVDescripcion));
        p.put("EstIUsuarioId",mapObject(EstIUsuarioId));
        p.put("EstICatEstablecimientoId",mapObject(EstICatEstablecimientoId));
        p.put("EstITipEstId",mapObject(EstITipEstId));
        p.put("LocVLongitude",mapObject(LocVLongitude));
        p.put("LocVLatitude",mapObject(LocVLatitude));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject fupd_LocalizacionEstablecimiento(int vint_AgenteId,int vint_EstablecimientoId,String vstr_Latitud,String vstr_Longitud) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "fupd_LocalizacionEstablecimiento");
        p.put("vint_AgenteId",mapObject(vint_AgenteId));
        p.put("vint_EstablecimientoId",mapObject(vint_EstablecimientoId));
        p.put("vstr_Latitud",mapObject(vstr_Latitud));
        p.put("vstr_Longitud",mapObject(vstr_Longitud));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject CreateSolicitudAutorizacionCredito(int idAgente,String idMotivoSolicitud,int idEstablecimiento,int estadoSolicitud,String observacion,String referencia,int idUsuario,double montoCredito,int vigenciaCredito,String vstr_KeyFireBase) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "CreateSolicitudAutorizacionCredito");
        p.put("idAgente",mapObject(idAgente));
        p.put("idMotivoSolicitud",mapObject(idMotivoSolicitud));
        p.put("idEstablecimiento",mapObject(idEstablecimiento));
        p.put("estadoSolicitud",mapObject(estadoSolicitud));
        p.put("observacion",mapObject(observacion));
        p.put("referencia",mapObject(referencia));
        p.put("idUsuario",mapObject(idUsuario));
        p.put("montoCredito",mapObject(montoCredito));
        p.put("vigenciaCredito",mapObject(vigenciaCredito));
        p.put("vstr_KeyFireBase",mapObject(vstr_KeyFireBase));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject fsel_ModalidadCredito(int vint_AgenteVentaId) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "fsel_ModalidadCredito");
        p.put("vint_AgenteVentaId",mapObject(vint_AgenteVentaId));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject validarClienteExistente(String documento) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "validarClienteExistente");
        p.put("documento",mapObject(documento));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject fins_ClienteTemporal(String Nombres,String ApellidoMaterno,String ApellidoPaterno,int CategoriaEstablecimientoId,String CelularCliente,String CelularEstablecimiento,String DescripcionEstablecimiento,String DireccionEstablecimiento,String DireccionFiscal,int DistritoDireccionId,int DistritoFiscalId,String DocIdentidad,String Email,String Latitude,String Longitude,String TelefonoEstablecimiento,int TipoDocIdentidadId,int TipoNegocio,int TipoPersonaId,int UsuarioId,String KeyFireBase) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "fins_ClienteTemporal");
        p.put("Nombres",mapObject(Nombres));
        p.put("ApellidoMaterno",mapObject(ApellidoMaterno));
        p.put("ApellidoPaterno",mapObject(ApellidoPaterno));
        p.put("CategoriaEstablecimientoId",mapObject(CategoriaEstablecimientoId));
        p.put("CelularCliente",mapObject(CelularCliente));
        p.put("CelularEstablecimiento",mapObject(CelularEstablecimiento));
        p.put("DescripcionEstablecimiento",mapObject(DescripcionEstablecimiento));
        p.put("DireccionEstablecimiento",mapObject(DireccionEstablecimiento));
        p.put("DireccionFiscal",mapObject(DireccionFiscal));
        p.put("DistritoDireccionId",mapObject(DistritoDireccionId));
        p.put("DistritoFiscalId",mapObject(DistritoFiscalId));
        p.put("DocIdentidad",mapObject(DocIdentidad));
        p.put("Email",mapObject(Email));
        p.put("Latitude",mapObject(Latitude));
        p.put("Longitude",mapObject(Longitude));
        p.put("TelefonoEstablecimiento",mapObject(TelefonoEstablecimiento));
        p.put("TipoDocIdentidadId",mapObject(TipoDocIdentidadId));
        p.put("TipoNegocio",mapObject(TipoNegocio));
        p.put("TipoPersonaId",mapObject(TipoPersonaId));
        p.put("UsuarioId",mapObject(UsuarioId));
        p.put("KeyFireBase",mapObject(KeyFireBase));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject getTransDetalle(String guiaTrans) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "getTransDetalle");
        p.put("guiaTrans",mapObject(guiaTrans));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }
    public JSONObject getGuiaRemision(int idguia) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","RestAPI");
        o.put("method", "getGuiaRemision");
        p.put("idguia",mapObject(idguia));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }


}
