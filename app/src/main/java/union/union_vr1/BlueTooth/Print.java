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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import jpos.JposException;
import jpos.POSPrinterConst;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;

public class Print {

    private POSPrinterService posPtr;
    private static char ESC = ESCPOS.ESC;
    private static char LF = ESCPOS.LF;
    private CPCL cpclPtr;
    private RequestQueue rq;
    private static String lineas= "------------------------------------------------------".substring(0,48);

    private static int FACTURA = 1;
    private static int BOLETA = 2;

    private Context context;


    private DbAdapter_Comprob_Venta dbHelperComprobanteVenta;
    private DbAdapter_Comprob_Venta_Detalle dbHelperComprobanteVentaDetalle;

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
    }

    public Print(POSPrinterService posPtr) {
        this.posPtr = posPtr;
    }

    public void printDocumento(int idDocumento) throws JposException
    {

        imprimircomprobante(idDocumento, 3);
        /*DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("#.00", simbolos);
        Double precioUnitario = 3.10;
        Double importe = 124.00;
        Double base_imponible = 124.00;
        Double zero = 0.0;
        Double igv = 124.00;
        Double precio_venta = 124.00;
        String nombreAgente = "EMERSON QUISPE FANO";

        printCabecera(FACTURA);


        printLineas();
        String cabeceraVenta = ESC + "|lA" + String.format("%-6s","CANT") + String.format("%-30s","PRODUCTO")+String.format("%-5s","P.U.")+  String.format("%-7s","IMPORTE")+ LF;
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, cabeceraVenta);
        printLineas();
        String ventaDetalle=  ESC + "|lA" + String.format("%-4s","40") + String.format("%-31s","Pan Integral Mediano Union")+String.format("%1$5s"  ,df.format(precioUnitario)) +String.format("%1$8s"  ,df.format(importe)) + LF;
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ventaDetalle);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ventaDetalle);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ventaDetalle);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ventaDetalle);
        //printLineas();
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + lineas + LF + LF);
        String gravada = ESC + "|lA" + String.format("%-18s","OP. GRAVADA")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format(base_imponible))+ LF;
        String inafecta = ESC + "|lA" + String.format("%-18s","OP. INAFECTA")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format(zero))+ LF;
        String exonerada = ESC + "|lA" + String.format("%-18s","OP. EXONERADA")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format(zero))+ LF;
        String gratuita = ESC + "|lA" + String.format("%-18s","OP. GRATUIRA")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format(zero))+ LF;
        String IGV = ESC + "|lA" + String.format("%-18s","I.G.V.")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format(igv))+ LF;
        String rayaTotal = ESC + "|rA" +"---------"+ LF;
        String precioVenta= String.format("%-18s","PRECIO VENTA")+String.format("%-21s","S/.")+  String.format("%1$9s",df.format(precio_venta))+ LF + LF;
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, gravada);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, inafecta);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, exonerada);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, gratuita);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, IGV);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, rayaTotal);
        //posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|rA" + ESC + "|bC" + "---------" + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, precioVenta);

        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + lineas + LF + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "VENDEDOR: " + nombreAgente + LF + LF + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT,ESC + "|cA" + ESC + "|bC" + ESC + "|2C" + "GRACIAS POR SU COMPRA" + LF + LF + LF);*/

    }

    private void printCabecera(int tipoDocumento) throws JposException
    {
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "UNIVERSIDAD PERUANA UNION" + LF );
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "CENTRO DE APLICACION PRODUCTOS UNION" + LF );
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "CAR. CENTRAL KM. 19.5 VILLA UNION-NANA" + LF );
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "Lurigancho-Chosica Fax: 6186311" + LF );
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "Telf: 6186309-6186310" + LF );
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "Casilla 3564, Lima 1, LIMA PERU" + LF );
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "RUC: 20138122256" + LF + LF );


        switch (tipoDocumento){
            case 1:
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "FACTURA ELECTRONICA" + LF + LF);
                break;
            case 2:
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "BOLETA ELECTRONICA" + LF + LF);
                break;
            default:
                break;

        }
    }

    private void printLineas() throws JposException
    {
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + lineas + LF);
    }

    private void imprimircomprobante(int idComprobante, int pulgadas) throws JposException {
        DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("#.00", simbolos);

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
                    printCabecera(BOLETA);
                } else {
                    printCabecera(BOLETA);
                }

                // IMPRIMIR DATOS GENERALES DEL CLIENTE Y EL DOCUMETNO
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "NUMERO  : " + comprobante + LF);
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "FECHA   : " + fecha+ LF);
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "CLIENTE : " + cliente + LF);
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "DNI/RUC : " + dni_ruc + LF);
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "DIRECCION : " + direccion + LF);
                //ESTO SOLO PARA PRUEBAS ELIMINARLO
                if (sha1==null){
                    sha1 = "ECGeF3Moo0qfijT3izDanpL8j6I=";
                }
                posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "SHA1    : " + sha1 + LF + LF);

                //IMPRIMIR CABECERA DE LA VENTA
                printLineas();
                String cabeceraVenta = ESC + "|lA" + String.format("%-6s","CANT") + String.format("%-30s","PRODUCTO")+String.format("%-5s","P.U.")+  String.format("%-7s","IMPORTE")+ LF;
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
                        String detalleVenta = ESC + "|lA" + String.format("%-4s", cantidad) + String.format("%-31s", nombreProducto) + String.format("%1$5s", df.format(precioUnitario)) + String.format("%1$8s", df.format(importe)) + LF;
                        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, detalleVenta);
                        //textoImpresionContenidoLeft +=String.format("%-6s",cantidad) + String.format("%-28s",nombreProducto)+ "\n";
                        //textoImpresionContenidoRight+= String.format("%-5s",df.format(importe)) + "\n";


                    }

                }
                printLineas();
                //posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + lineas + LF + LF);

                    //INFORMACION SOBRE LAS OPERACIONES
                    String gravada = ESC + "|lA" + String.format("%-18s","OP. GRAVADA")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format(base_imponible))+ LF;
                    /*String inafecta = ESC + "|lA" + String.format("%-18s","OP. INAFECTA")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format("0.00"))+ LF;
                    String exonerada = ESC + "|lA" + String.format("%-18s","OP. EXONERADA")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format("0.00"))+ LF;
                    String gratuita = ESC + "|lA" + String.format("%-18s","OP. GRATUIRA")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format("0.00"))+ LF;*/
                    String IGV = ESC + "|lA" + String.format("%-18s","I.G.V.")+String.format("%-21s","S/.")+ String.format("%1$9s",df.format(igv))+ LF;
                    String rayaTotal = ESC + "|rA" +"---------"+ LF;
                    String precioVenta= String.format("%-18s","PRECIO VENTA")+String.format("%-21s","S/.")+  String.format("%1$9s",df.format(precio_venta))+ LF;
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, gravada);
                /*    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, inafecta);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, exonerada);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, gratuita);
                */
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, IGV);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, rayaTotal);
                    //posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|rA" + ESC + "|bC" + "---------" + LF);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, precioVenta);


                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + lineas + LF + LF);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + "VENDEDOR: " + nombreAgente + LF + LF + LF);
                    posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT,ESC + "|cA" + ESC + "|bC" + ESC + "|2C" + "GRACIAS POR SU COMPRA" + LF + LF + LF);
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

}
