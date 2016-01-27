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

public class PrintCobros {

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



    public PrintCobros(Context context){
        this.context = context;
        posPtr = new POSPrinterService();
        cpclPtr = new CPCL();
        rq = RequestQueue.getInstance();



    }

    public PrintCobros(POSPrinterService posPtr) {
        this.posPtr = posPtr;
    }

    public void printDocumento(String idDocumento,String IMPORTE,String COMPROBANTE,String CLIENTE, String AGENTE,int TIPO) throws JposException {

        imprimircomprobante(idDocumento, 3, IMPORTE, COMPROBANTE, CLIENTE, AGENTE,TIPO);

    }

    public void printCabecera() throws JposException
    {
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "UNIVERSIDAD PERUANA UNION" + LF+ LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "CENTRO DE APLICACION PRODUCTOS UNION" + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "CAR. CENTRAL KM. 19.5 VILLA UNION-NANA" + LF );
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "LIMA - LIMA - LURIGANCHO" + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "RUC: 20138122256" + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "Telf: 6186309-6186310" + LF + LF);




    }


    private void printLineas() throws JposException
    {
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + lineas + LF);
    }




    private void imprimircomprobante(String idComprobante, int pulgadas, String IMPORTE,String COMPROBANTE,String CLIENTE, String AGENTE,int tipo) throws JposException {

        String importe = Utils.formatDouble(Double.parseDouble(IMPORTE));
        DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00", simbolos);


        printCabecera();

        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "F. RECIBO  : " + Utils.getDatePhone() + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "F. EMISION   : " + Utils.getDatePhone() + " " + Utils.getTimePhone() + LF + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "COMPROBANTE : " + COMPROBANTE + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "CLIENTE : " + CLIENTE + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "AGENTE : " + AGENTE + LF );
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "GLOSARIO : Contado" + LF + LF);
        if(tipo==1){
            posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "TIPO COBRO : Normal" + LF + LF);
        }else{
            posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|lA" + "TIPO COBRO : Manual" + LF + LF);
        }

        printLineas();
        String cabecera= ESC + "|lA" + String.format("%-31s","COMPROBANTE ") +  String.format("%1$8s","IMPORTE DE COBRO")+ LF;
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, cabecera);
        printLineas();
        String detalle = ESC + "|lA" + String.format("%-30s", COMPROBANTE)  + String.format("%1$18s", importe) + LF;
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, detalle);
        printLineas();
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + (NumberToLetterConverter.convertNumberToLetter(df.format(Double.parseDouble(Utils.replaceComa(importe))))).toUpperCase() + LF+ LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + Constants.PRINT_VISUALICE + LF);
        posPtr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + Constants.PRINT_URL + LF + LF + LF );

    }



    public String getDayPhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }
}
