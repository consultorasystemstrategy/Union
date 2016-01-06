package union.union_vr1.BlueTooth;

/**
 * Created by Steve on 10/11/2015.
 */

import android.content.Context;
import android.database.Cursor;

import com.sewoo.jpos.command.CPCL;
import com.sewoo.jpos.command.CPCLConst;
import com.sewoo.jpos.command.ESCPOS;
import com.sewoo.jpos.request.RequestQueue;
import com.sewoo.jpos.POSPrinterService;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import jpos.JposException;
import jpos.POSPrinterConst;
import union.union_vr1.FacturacionElectronica.NumberToLetterConverter;
import union.union_vr1.Sqlite.Constants;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Informe_Gastos;
import union.union_vr1.Sqlite.DbAdapter_Transferencias;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Sqlite.DbGastos_Ingresos;
import union.union_vr1.Utils.Utils;

public class Print {

    private POSPrinterService posPtr;
    private static char ESC = ESCPOS.ESC;
    private static char LF = ESCPOS.LF;
    private CPCL cpclPtr;
    private RequestQueue rq;
    private static String lineas= "------------------------------------------------------".substring(0,48);
    private static String igualLinea= "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=".substring(0,48);
    private static String asteriscos= "********************************************************".substring(0,48);


    private static int FACTURA = 1;
    private static int BOLETA = 2;

    private Context context;


    private DbAdapter_Comprob_Venta dbHelperComprobanteVenta;
    private DbGastos_Ingresos dbHelperGastosIngr;
    private DbAdapter_Comprob_Venta_Detalle dbHelperComprobanteVentaDetalle;
    private DbAdapter_Informe_Gastos dbAdapter_informe_gastos;
    private DbAdapter_Comprob_Cobro dbAdapter_comprob_cobro;

    private DbAdapter_Transferencias dbAdapter_transferencias;


    int nTotal = 0;
    Double emitidoTotal = 0.0;
    Double pagadoTotal = 0.0;
    Double cobradoTotal = 0.0;


    Double totalRuta = 0.0;
    Double totalPlanta = 0.0;

    public Print(Context context){

        this.context = context;
        posPtr = new POSPrinterService();
        cpclPtr = new CPCL();
        rq = RequestQueue.getInstance();

        //TABLAS VENTA Y VENTA DETALLE
        dbHelperComprobanteVenta = new DbAdapter_Comprob_Venta(context);
        dbHelperComprobanteVentaDetalle = new DbAdapter_Comprob_Venta_Detalle(context);
        dbHelperComprobanteVenta.open();
        dbHelperComprobanteVentaDetalle.open();
        dbHelperGastosIngr =  new DbGastos_Ingresos(context);
        dbHelperGastosIngr.open();
        dbAdapter_informe_gastos = new DbAdapter_Informe_Gastos(context);
        dbAdapter_informe_gastos.open();
        dbAdapter_comprob_cobro = new DbAdapter_Comprob_Cobro(context);
        dbAdapter_comprob_cobro.open();

    }

    public Print(POSPrinterService posPtr) {
        this.posPtr = posPtr;
    }

    public void printDocumento(int idDocumento) throws JposException
    {
        imprimircomprobante(idDocumento, 3);
    }

    public void printCabecera(int tipoDocumento) throws JposException
    {
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "UNIVERSIDAD PERUANA UNION" + LF+ LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "CENTRO DE APLICACION PRODUCTOS UNION" + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "CAR. CENTRAL KM. 19.5 VILLA UNION-NANA" + LF );
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "LIMA - LIMA - LURIGANCHO" + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "RUC: 20138122256" + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "Telf: 6186309-6186310" + LF  + LF);

        switch (tipoDocumento){
            case Constants.DOCUMENTO_FACTURA:
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "FACTURA ELECTRONICA" + LF + LF);
                break;
            case Constants.DOCUMENTO_BOLETA:
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "BOLETA ELECTRONICA" + LF + LF);
                break;
            case Constants.DOCUMENTO_TRANSFERENCIA:
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT,ESC + "|cA" + ESC + "|bC" + ESC + "|2C" + "TRANSFERENCIAS" + LF + LF + LF);
                break;
            case Constants.DOCUMENTO_ARQUEO:
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT,ESC + "|cA" + ESC + "|bC" + ESC + "|2C" + "RESUMEN DEL DIA" + LF + LF + LF);
                break;
            case Constants.DOCUMENTO_RRPP:
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT,ESC + "|cA" + ESC + "|bC" + ESC + "|2C" + "VENTA RRPP" + LF + LF + LF);
                break;
            default:
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT,ESC + "|cA" + ESC + "|bC" + ESC + "|2C" + "PRODUCTOS UNION" + LF + LF + LF);
                break;

        }
    }

    public void printRepresentacion(int tipoDocumento) throws JposException {

/*        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + Constants.PRINT_AUTORIZADO_ + LF );
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + Constants.PRINT_N_RESOLUCION + LF + LF);*/

        switch (tipoDocumento){
            case Constants.DOCUMENTO_FACTURA:
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "REPRESENTACION IMPRESA DE LA FACTURA DE" + LF);
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "VENTA ELECTRONICA" + LF + LF );
                break;
            case Constants.DOCUMENTO_BOLETA:
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "REPRESENTACION IMPRESA DE LA BOLETA DE " + LF);
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "VENTA ELECTRONICA" + LF + LF );
                break;
            default:
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT,ESC + "|cA" + ESC + "|bC" + ESC + "|2C" + "PRODUCTOS UNION" + LF + LF + LF);
                break;
        }
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + Constants.PRINT_VISUALICE + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + Constants.PRINT_URL + LF + LF + LF );

    }
    private void printLineas() throws JposException
    {
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + lineas + LF);
    }
    private void printIgualLinea() throws JposException
    {
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + igualLinea + LF);
    }


    private void printAsteriscos() throws JposException
    {
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + asteriscos + LF);
    }



    private void imprimircomprobante(int idComprobante, int pulgadas) throws JposException {
        DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00", simbolos);

        String texto= "";
        String comprobante = "";
        String fecha = "";
        String cliente = "";
        String dni_ruc = "";
        String serie  ="";
        String numDoc = "";
        String nombreAgente = "";
        String direccion="";
        String sha1="";

        double base_imponible = 0.0;
        double igv = 0.0;
        double precio_venta = 0.0;
        String ventaDetalle = "";
        int tipoVenta = -1;
        double _CERO= 0.0;
        int idFormaPago = 1;

        Cursor cursorVentaCabecera = dbHelperComprobanteVenta.getVentaCabecerabyID(idComprobante);

        if (cursorVentaCabecera.getCount()>0){
            cursorVentaCabecera.moveToFirst();

            serie = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_serie));
            numDoc = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_num_doc));
            comprobante = serie + "-" +agregarCeros((String.valueOf(numDoc)),8);
            fecha = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_fecha_doc));
            cliente = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_nom_cliente));
            dni_ruc = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_doc_cliente));
            nombreAgente = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Agente.AG_nombre_agente));
            direccion = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdaptert_Evento_Establec.EE_direccion));
            sha1 = cursorVentaCabecera.getString(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_SHA1));
            //tipo de venta NORMAL O RRPP
            tipoVenta = cursorVentaCabecera.getInt(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_id_tipo_venta));
            idFormaPago = cursorVentaCabecera.getInt(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_id_forma_pago));

            /*ventaCabecera+= "NUMERO  : "+comprobante+"\n";
            ventaCabecera+= "FECHA   : "+ fecha+"\n";
            ventaCabecera+= "VENDEDOR: "+ nombreAgente+"\n";
            ventaCabecera+= "CLIENTE : "+ cliente+"\n";
            ventaCabecera+= "DNI/RUC : "+ dni_ruc+"\n";
            ventaCabecera+= "DIRECCION : "+ direccion+"\n";
            ventaCabecera+= "SHA1 : "+ sha1+"\n";
*/


/*

            Cursor cursorVentaDetalle = dbHelperComprobanteVentaDetalle.fetchAllComprobVentaDetalleByIdComp(idComprobante);

            if (cursorVentaCabecera.getCount()>0){

                for (cursorVentaDetalle.moveToFirst(); !cursorVentaDetalle.isAfterLast();cursorVentaDetalle.moveToNext()) {

                    int cantidad = cursorVentaDetalle.getInt(cursorVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_cantidad));
                    String nombreProducto = cursorVentaDetalle.getString(cursorVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_nom_producto));
                    Double precioUnitario = cursorVentaDetalle.getDouble(cursorVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_precio_unit));
                    Double importe = cursorVentaDetalle.getDouble(cursorVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_importe));

                  *//* texto += "CANT:"+cantidad+"\n";
                   texto += "NOM:"+nombreProducto+"\n";
                   texto += "PU:"+ df.format(precioUnitario)+"\n";
                   texto += "IMP:"+df.format(importe)+"\n";
                   texto += "-------------------\n";*//*

                    if (pulgadas==2){
                        if(nombreProducto.length()>=20){
                            nombreProducto=nombreProducto.substring(0,18);
                            nombreProducto+="..";
                        }
                    }else if (pulgadas==3){
                        if(nombreProducto.length()>=30){
                            nombreProducto=nombreProducto.substring(0,28);
                            nombreProducto+="..";
                        }
                    }
                    ventaDetalle+=String.format("%-4s",cantidad) + String.format("%-31s",nombreProducto)+String.format("%1$5s"  ,df.format(precioUnitario)) +String.format("%1$8s"  ,df.format(importe)) + "\n";
                  }
            }*/

            base_imponible = cursorVentaCabecera.getDouble(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_base_imp));
            igv = cursorVentaCabecera.getDouble(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_igv));
            precio_venta = cursorVentaCabecera.getDouble(cursorVentaCabecera.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_total));
        }

        switch (pulgadas){
            case 2:
                //texto+=" 2 PULGADAS, NO SOPORTADO.\n";
                break;
            case 3:

                //IMPRIMIR INFORMACION SOBRE EMPRESA
                String tipoC="";
                tipoC=comprobante.substring(0,1);
                String tipoComprobante="";
                if(tipoC.equals("F"))
                {
                    printCabecera(FACTURA);
                }
                else if(tipoC.equals("B"))
                {
                    if (tipoVenta==1){
                        printCabecera(BOLETA);
                    }else{
                        printCabecera(Constants.DOCUMENTO_RRPP);
                    }
                } else {
                    printCabecera(BOLETA);
                }

                // IMPRIMIR DATOS GENERALES DEL CLIENTE Y EL DOCUMETNO
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "NUMERO  : " + comprobante + LF);
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "FECHA   : " + fecha+ LF+ LF);
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "CLIENTE : " + cleanAcentos(cliente) + LF);
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "DNI/RUC : " + dni_ruc + LF);
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "DIRECCION : " + cleanAcentos(direccion) + LF + LF);
                //ESTO SOLO PARA PRUEBAS ELIMINARLO
                if (sha1==null){
                    sha1 = "ECGeF3Moo0qfijT3izDanpL8j6I=";
                }

                //IMPRIMIR CABECERA DE LA VENTA
                printLineas();
                String cabeceraVenta = ESC + "|lA" + String.format("%-6s","CANT") + String.format("%-30s","DESCRIPCION")+String.format("%-5s","P.U.")+  String.format("%-7s","IMPORTE")+ LF;
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, cabeceraVenta);
                printLineas();
                //IMPRIMIR DETALLE DE LA VENTA RECORREREMOS EL CURSOR
                Cursor cursorVentaDetalle = dbHelperComprobanteVentaDetalle.fetchAllComprobVentaDetalleByIdComp(idComprobante);

                if (cursorVentaCabecera.getCount()>0) {

                    for (cursorVentaDetalle.moveToFirst(); !cursorVentaDetalle.isAfterLast(); cursorVentaDetalle.moveToNext()) {

                        int cantidad = cursorVentaDetalle.getInt(cursorVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_cantidad));
                        String nombreProducto = cursorVentaDetalle.getString(cursorVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_nom_producto));
                        Double precioUnitario = cursorVentaDetalle.getDouble(cursorVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_precio_unit));
                        Double importe = cursorVentaDetalle.getDouble(cursorVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_importe));

                        if (pulgadas == 2) {
                            if (nombreProducto.length() >= 20) {
                                nombreProducto = nombreProducto.substring(0, 18);
                                nombreProducto += "..";
                            }
                        } else if (pulgadas == 3) {
                            if (nombreProducto.length() >= 30) {
                                nombreProducto = nombreProducto.substring(0, 28);
                                nombreProducto += "..";
                            }
                        }
                        String detalleVenta = ESC + "|lA" + String.format("%-4s", cantidad) + String.format("%-31s", cleanAcentos(nombreProducto)) + String.format("%1$5s", df.format(precioUnitario)) + String.format("%1$8s", df.format(importe)) + LF;
                        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, detalleVenta);
                        //textoImpresionContenidoLeft +=String.format("%-6s",cantidad) + String.format("%-28s",nombreProducto)+ "\n";
                        //textoImpresionContenidoRight+= String.format("%-5s",df.format(importe)) + "\n";


                    }

                }
                printLineas();
                //posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + lineas + LF + LF);

                if (tipoVenta!=2) {
                    //INFORMACION SOBRE LAS OPERACIONES
                    String gravada = ESC + "|lA" + String.format("%-18s", "OP. GRAVADA") + String.format("%-21s", "S/.") + String.format("%1$9s", df.format(base_imponible)) + LF;
                    String inafecta = ESC + "|lA" + String.format("%-18s", "OP. INAFECTA") + String.format("%-21s", "S/.") + String.format("%1$9s", df.format(_CERO)) + LF;
                    String exonerada = ESC + "|lA" + String.format("%-18s", "OP. EXONERADA") + String.format("%-21s", "S/.") + String.format("%1$9s", df.format(_CERO)) + LF;
                    String gratuita = ESC + "|lA" + String.format("%-18s", "OP. GRATUITA") + String.format("%-21s", "S/.") + String.format("%1$9s", df.format(_CERO)) + LF;
                    String descuentos = ESC + "|lA" + String.format("%-18s", "DESCUENTOS") + String.format("%-21s", "S/.") + String.format("%1$9s", df.format(_CERO)) + LF;
                    String IGV = ESC + "|lA" + String.format("%-18s", "I.G.V.") + String.format("%-21s", "S/.") + String.format("%1$9s", df.format(igv)) + LF;
                    String rayaTotal = ESC + "|rA" + "---------" + LF;
                    String precioVenta = String.format("%-18s", "PRECIO VENTA") + String.format("%-21s", "S/.") + String.format("%1$9s", df.format(precio_venta)) + LF;
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, gravada);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, inafecta);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, exonerada);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, gratuita);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, descuentos);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, IGV);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, rayaTotal);
                    //posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|rA" + ESC + "|bC" + "---------" + LF);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, precioVenta);
                }else {
                    String gravada = ESC + "|lA" + String.format("%-18s", "OP. GRAVADA") + String.format("%-21s", "S/.") + String.format("%1$9s", df.format(base_imponible)) + LF;
                    String IGV = ESC + "|lA" + String.format("%-18s", "I.G.V.") + String.format("%-21s", "S/.") + String.format("%1$9s", df.format(igv)) + LF;
                    String rayaTotal = ESC + "|rA" + "---------" + LF;
                    String precioVenta = String.format("%-18s", "PRECIO VENTA") + String.format("%-21s", "S/.") + String.format("%1$9s", df.format(precio_venta)) + LF;
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, gravada);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, IGV);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, rayaTotal);
                    //posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|rA" + ESC + "|bC" + "---------" + LF);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, precioVenta);

                }
                printLineas();
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + (NumberToLetterConverter.convertNumberToLetter(df.format(precio_venta))).toUpperCase() + LF );
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + lineas + LF );
                switch (idFormaPago){
                    case Constants.FORMA_DE_PAGO_CONTADO:
                        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "VENTA AL CONTADO" + LF);
                        break;
                    case Constants.FORMA_DE_PAGO_CREDITO:
                        String credito = ESC + "|lA" + String.format("%-18s", "VENTA AL CREDITO: ") + String.format("%1$9s", df.format(precio_venta))+ String.format("%1$21s", "") + LF;
                        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, credito);
                        /*posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "VENTA AL CREDITO"+ LF + LF);*/
                        Cursor cursorCredito = dbAdapter_comprob_cobro.fetchComprobCobrosByIdComprobante(idComprobante);
                        if (cursorCredito.getCount()>0){
                            cursorCredito.moveToFirst();
                            //SÓLO IMPRIMIR LA PRIMERA CUOTA
                            /*for (cursorCredito.moveToFirst(); !cursorCredito.isAfterLast() ; cursorCredito.moveToNext()){*/
                                String primeraFechaCobro = cursorCredito.getString(cursorCredito.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_fecha_programada));
                                Double monto_Pagar = cursorCredito.getDouble(cursorCredito.getColumnIndexOrThrow(DbAdapter_Comprob_Cobro.CC_monto_a_pagar));
                            String cuotaDetalle = ESC + "|lA" + String.format("%-16s", "Fecha de Pago: ") + String.format("%1$11s", Utils.format(primeraFechaCobro)) + String.format("%1$21s", "__________________")+ LF;
                            String firma = ESC + "|lA" +  String.format("%1$41s", "Firma")+  String.format("%1$7s", "")+ LF;
                            posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, cuotaDetalle);
                            posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, firma);

                            /*}*/
/*
                            posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "" + LF);
                            posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "____________________" + LF);
                            posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "Firma del cliente" + LF + LF);
*/


                        }
                        break;
                    default:
                        break;
                }
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "AGENTE : " + cleanAcentos(nombreAgente) + LF);

                    if (tipoVenta!=2){
                        printIgualLinea();
                        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "V. Resumen: " + sha1 + LF + LF);

                        if(tipoC.equals("F"))
                        {
                            printRepresentacion(Constants.DOCUMENTO_FACTURA);
                        }
                        else if(tipoC.equals("B")) {
                            printRepresentacion(Constants.DOCUMENTO_BOLETA);
                        }
                    }


                break;
            default:
                //texto+=" NO SE PUEDE RECONOCER EL NUMERO DE PULGADAS...";
                break;
        }
    }

    private static String agregarCeros(String string, int largo)
    {
        String ceros = "";
        int cantidad = largo - string.length();
        if (cantidad >= 1)
        {
            for(int i=0;i<cantidad;i++)
            {
                ceros += "0";
            }
            return (ceros + string);
        }
        else
            return string;
    }

    public void printTransferencia(int idLiquidacion, String nombreAgente) throws JposException {

        dbAdapter_transferencias = new DbAdapter_Transferencias(context);
        dbAdapter_transferencias.open();

        Cursor cursorTransferencias= dbAdapter_transferencias.getTransferencias(idLiquidacion);

        cursorTransferencias.moveToFirst();



        if (cursorTransferencias.getCount()>0){



            printCabecera(Constants.DOCUMENTO_TRANSFERENCIA);
            posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "AGENTE       : " + cleanAcentos(nombreAgente) + LF);
            posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "LIQUIDACION  : " + idLiquidacion + LF);
            posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "FECHA        : " + cleanAcentos(getDateFull()) + LF);

            int id_almacen = -1;
            int i= 0;

            for (cursorTransferencias.moveToFirst(); !cursorTransferencias.isAfterLast(); cursorTransferencias.moveToNext()){

                String codigo = cursorTransferencias.getString(cursorTransferencias.getColumnIndexOrThrow(DbAdapter_Transferencias.T_codigo));
                String producto = cursorTransferencias.getString(cursorTransferencias.getColumnIndexOrThrow(DbAdapter_Transferencias.T_producto));
                String descripcion_transferencia = cursorTransferencias.getString(cursorTransferencias.getColumnIndexOrThrow(DbAdapter_Transferencias.T_descripcion_transferencia));
                int id_almacen_actual = cursorTransferencias.getInt(cursorTransferencias.getColumnIndexOrThrow(DbAdapter_Transferencias.T_almacen_id));
                int cantidad = cursorTransferencias.getInt(cursorTransferencias.getColumnIndexOrThrow(DbAdapter_Transferencias.T_cantidad));

                if (id_almacen != id_almacen_actual){
                    //IMPRIMIR TRANSFERENCIA DESCRIPCION
                    printLineas();
                    //posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + cleanAcentos(descripcion_transferencia) + LF + LF + LF);
                    //posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT,ESC + "|cA" + ESC + "|bC" + ESC + "|2C" + cleanAcentos(descripcion_transferencia) + LF + LF + LF);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + cleanAcentos(descripcion_transferencia) + LF + LF );
                    String cabeceraVenta = ESC + "|lA" +String.format("%-3s","#")+  String.format("%-6s","COD") + String.format("%-30s","PRODUCTO")+ String.format("%1$9s","CANT") + LF;
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, cabeceraVenta);
                    printLineas();
                    //ASIGNAR ID_ALMACEN

                    id_almacen = id_almacen_actual;
                }

                if (producto.length() >= 34) {
                    producto = producto.substring(0, 32);
                    producto += "..";
                }

                String detalleTransferencia = ESC + "|lA" + String.format("%-3s", ++i) + String.format("%-6s", codigo)+ String.format("%-34s", cleanAcentos(producto)) + String.format("%1$5s", cantidad) + LF;
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, detalleTransferencia);
            }
        }

    }

    public void printArqueo(int idLiquidacion, String nombreAgente) throws JposException {
        DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00", simbolos);

        //String texto = ".\n";


        Cursor cursorResumen = dbHelperGastosIngr.listarIngresosGastos(idLiquidacion);
        cursorResumen.moveToFirst();

        if (cursorResumen.getCount()>0) {
            for (cursorResumen.moveToFirst(); !cursorResumen.isAfterLast(); cursorResumen.moveToNext()) {
                int n = cursorResumen.getInt(cursorResumen.getColumnIndexOrThrow("n"));
                Double emitido = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("emitidas"));
                Double pagado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("pagado"));
                Double cobrado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("cobrado"));
                String documento = cursorResumen.getString(cursorResumen.getColumnIndexOrThrow("comprobante"));
                nTotal += n;
                emitidoTotal += emitido;
                pagadoTotal += pagado;
                cobradoTotal += cobrado;

            }
        }


        Cursor cursorTotalGastos = dbAdapter_informe_gastos.resumenInformeGastos(getDayPhone());

        if (cursorTotalGastos.getCount()>0){
            for (cursorTotalGastos.moveToFirst(); !cursorTotalGastos.isAfterLast(); cursorTotalGastos.moveToNext()) {
                Double rutaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("RUTA"));
                Double plantaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("PLANTA"));
                String nombreGasto = cursorTotalGastos.getString(cursorTotalGastos.getColumnIndexOrThrow("tg_te_nom_tipo_gasto"));

                totalRuta += rutaGasto;
                totalPlanta += plantaGasto;
            }
        }



        Double ingresosTotales = cobradoTotal + pagadoTotal;
        Double gastosTotales = totalRuta;
        Double aRendir = ingresosTotales - gastosTotales;


        printCabecera(Constants.DOCUMENTO_ARQUEO);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "AGENTE       : " + cleanAcentos(nombreAgente) + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "LIQUIDACION  : " + idLiquidacion + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "FECHA        : " + cleanAcentos(getDateFull()) + LF+ LF+ LF);


        String ingresos = ESC + "|lA" +String.format("%-20s", "INGRESOS TOTALES")+ String.format("%-16s", "S/.") + String.format("%1$12s", df.format(ingresosTotales)) + LF;
        String gastos = ESC + "|lA" +String.format("%-20s", "GASTOS TOTALES\"")+ String.format("%-16s", "S/.") + String.format("%1$12s", df.format(gastosTotales)) + LF;
        String total = ESC + "|lA" +String.format("%-20s", "TOTAL A RENDIR")+ String.format("%-16s", "S/.") + String.format("%1$12s", df.format(aRendir)) + LF+ LF+ LF;

        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ingresos);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, gastos);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, total);




        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + cleanAcentos("INGRESOS") + LF + LF);



        String cabeceraIngresos = ESC + "|cA" + ESC + "|bC"+String.format("%-18s", "COMPROBANTE")+ String.format("%1$6s", "CANT.")  + String.format("%1$8s", "EMIT.") + String.format("%1$8s", "PAG.") + String.format("%1$8s", "COB.") + LF;
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, cabeceraIngresos);

        cursorResumen.moveToFirst();
        if (cursorResumen.getCount()>0){
            for (cursorResumen.moveToFirst(); !cursorResumen.isAfterLast(); cursorResumen.moveToNext()) {
                int n = cursorResumen.getInt(cursorResumen.getColumnIndexOrThrow("n"));
                Double emitido = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("emitidas"));
                Double pagado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("pagado"));
                Double cobrado = cursorResumen.getDouble(cursorResumen.getColumnIndexOrThrow("cobrado"));
                String documento = cursorResumen.getString(cursorResumen.getColumnIndexOrThrow("comprobante"));
                String detalle = ESC + "|lA" +String.format("%-20s", documento)+ String.format("%1$4s", n)  + String.format("%1$8s",  df.format(emitido)) + String.format("%1$8s", df.format(pagado)) + String.format("%1$8s", df.format(cobrado)) + LF;
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, detalle);
            }
        }
        printLineas();
        String totalIngresos = ESC + "|cA" + ESC + "|bC" +String.format("%-20s", "Total") + String.format("%1$4s", nTotal) + String.format("%1$8s", df.format(emitidoTotal)) + String.format("%1$8s", df.format(pagadoTotal)) + String.format("%1$8s", df.format(cobradoTotal)) +LF+LF+LF;
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, totalIngresos);

        /*String cabeceraGastos = ESC + "|lA" +String.format("%-20s", "Total") + String.format("%1$4s", nTotal) + String.format("%1$8s", df.format(emitidoTotal)) + String.format("%1$8s", df.format(pagadoTotal)) + String.format("%1$8s", df.format(cobradoTotal)) + LF;
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, cabeceraGastos);*/


        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + cleanAcentos("GASTOS") + LF + LF);



        String cabeceraGastos = ESC + "|cA" + ESC + "|bC" + String.format("%-27s", "Tipo de Gasto") + String.format("%1$21s", "Ruta") + LF;
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, cabeceraGastos);


        cursorTotalGastos.moveToFirst();
        for (cursorTotalGastos.moveToFirst(); !cursorTotalGastos.isAfterLast(); cursorTotalGastos.moveToNext()) {
            Double rutaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("RUTA"));
            Double plantaGasto = cursorTotalGastos.getDouble(cursorTotalGastos.getColumnIndexOrThrow("PLANTA"));
            String nombreGasto = cursorTotalGastos.getString(cursorTotalGastos.getColumnIndexOrThrow("tg_te_nom_tipo_gasto"));

            if (nombreGasto.length()>=27){
                nombreGasto= nombreGasto.substring(0,24);
                nombreGasto+="..";

            }
            String detalleGastos = ESC + "|lA" + String.format("%-27s", nombreGasto) + String.format("%1$21s", df.format(rutaGasto)) + LF ;
            posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, detalleGastos);
        }


        printLineas();
        String totalGastos = ESC + "|cA" + ESC + "|bC" +String.format("%-27s", "Total") + String.format("%1$21s", df.format(totalRuta)) +LF + LF + LF  ;
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, totalGastos);
    }

    private String getDateFull() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
        String formatteDate = format.format(date);
        return formatteDate.substring(0, 1).toUpperCase() + formatteDate.substring(1);
    }

    public static String cleanAcentos(String string) {
        /*String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇü·':";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcCu   ";*/
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇü·'°";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcCu  .";
        if (string != null) {
            //Recorro la cadena y remplazo los caracteres originales por aquellos sin acentos
            for (int i = 0; i < original.length(); i++ ) {
                string = string.replace(original.charAt(i), ascii.charAt(i));
            }
        }
        return string;
    }

    public String getDayPhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }
}
