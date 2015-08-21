package union.union_vr1.FacturacionElectronica;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DBAdapter_Temp_Venta;
import union.union_vr1.Utils.NumberToLetterConverter;
import union.union_vr1.Utils.Utils;

/**
 * Created by Usuario on 17/08/2015.
 */
public class DigitalSignature {

    FileInputStream fis = null;


    public void escribirXML(int tipoDocumento, Context context, String idDocument, String userRUC_DNI, String userName, String totalOperacionesGravadas, String importeTotalVenta, String igvTotal, Cursor cursorTemp) {
        FileOutputStream fout = null;

        try {
            Log.d("GENERATE DS","000");
            File myFile = new File(context.getFilesDir(), "test.xml");
            fout = context.openFileOutput("test.xml", context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        XmlSerializer serializer = Xml.newSerializer();
        try {
            /*
            serializer.startTag(null, "autor");
            serializer.attribute(null, "nombre", "meta");
            serializer.text("Programación en diferentes lenguajes");
            serializer.endTag(null, "autor");

            serializer.startTag(null, "autor");
            serializer.attribute(null, "nombre", "sshMan");
            serializer.text("Seguridad Informática y Pentesting");
            serializer.endTag(null, "autor");
            */

            //DEFINIOMS EL PRÓLOGO DEL XML
            serializer.setOutput(fout, UBLElements.ISO88591);
            serializer.startDocument(null, false);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);


            //ESTRUCTURA UBL EXTENSION
            //http://docs.oasis-open.org/ubl/os-UBL-2.0/UBL-2.0.html

            serializer.setPrefix(UBLElements.URN, UBLElements.NAMESPACE_URN);
            serializer.setPrefix(UBLElements.CAC, UBLElements.NAMESPACE_CAC);
            serializer.setPrefix(UBLElements.CBC, UBLElements.NAMESPACE_CBC);
            serializer.setPrefix(UBLElements.CCTS, UBLElements.NAMESPACE_CCTS);
            serializer.setPrefix(UBLElements.DS, UBLElements.NAMESPACE_DS);
            serializer.setPrefix(UBLElements.EXT, UBLElements.NAMESPACE_EXT);
            serializer.setPrefix(UBLElements.QDT, UBLElements.NAMESPACE_QDT);
            serializer.setPrefix(UBLElements.SAC, UBLElements.NAMESPACE_SAC);
            serializer.setPrefix(UBLElements.UDT, UBLElements.NAMESPACE_UDT);
            serializer.setPrefix(UBLElements.XSI, UBLElements.NAMESPACE_XSI);

            serializer.startTag(null, UBLElements.INVOICE);
            serializer.startTag(UBLElements.NAMESPACE_EXT, UBLElements.UBLEXTENSIONS);
            // INICIO - UBL EXTENSION I
            //SECCIÓN I, PANORAMA GENERAL DE LA VENTA.
            serializer.startTag(UBLElements.NAMESPACE_EXT, UBLElements.UBLEXTENSION);
            serializer.startTag(UBLElements.NAMESPACE_EXT, UBLElements.EXTENSIONCONTENT);
            serializer.startTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALINFORMATION);

            //OPERACIONES GRAVADAS - TOTAL VALOR DE VENTA
            serializer.startTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALMONETARYTOTAL);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.text("1001");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.PAYABLEAMOUNT);
            serializer.attribute(null, UBLElements.CURRENCYID, UBLElements.PEN);
            serializer.text(totalOperacionesGravadas);
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.PAYABLEAMOUNT);
            serializer.endTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALMONETARYTOTAL);
            /*//OPERACIONES INAFECTAS - TOTAL VALOR DE VENTA
                serializer.startTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALMONETARYTOTAL);

                serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
                serializer.text("1004");
                serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
                serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.PAYABLEAMOUNT);
                serializer.attribute(null, UBLElements.CURRENCYID, UBLElements.PEN);
                serializer.text("0.00");
                serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.PAYABLEAMOUNT);

                serializer.endTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALMONETARYTOTAL);*/
            //OPERACIONES EXONERADAS - TOTAL VALOR DE VENTA
            serializer.startTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALMONETARYTOTAL);

            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.text("1003");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.PAYABLEAMOUNT);
            serializer.attribute(null, UBLElements.CURRENCYID, UBLElements.PEN);
            serializer.text("0.00");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.PAYABLEAMOUNT);

            serializer.endTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALMONETARYTOTAL);
            //OPERACIONES GRATUITAS - TOTAL VALOR DE VENTA
            serializer.startTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALMONETARYTOTAL);

            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.text("1004");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.PAYABLEAMOUNT);
            serializer.attribute(null, UBLElements.CURRENCYID, UBLElements.PEN);
            serializer.text("0.00");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.PAYABLEAMOUNT);

            serializer.endTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALMONETARYTOTAL);
            //TOTAL DESCUENTOS
            serializer.startTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALMONETARYTOTAL);

            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.text("2005");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.PAYABLEAMOUNT);
            serializer.attribute(null, UBLElements.CURRENCYID, UBLElements.PEN);
            serializer.text("0.00");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.PAYABLEAMOUNT);

            serializer.endTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALMONETARYTOTAL);
            //IMPORTE TOTAL DE LAS VENTAS[LETRAS]
            serializer.startTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALPROPERTY);

            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.text("1000");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.VALUE);

            serializer.text(NumberToLetterConverter.convertNumberToLetter(importeTotalVenta));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.VALUE);

            serializer.endTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALPROPERTY);


            serializer.endTag(UBLElements.NAMESPACE_SAC, UBLElements.ADDITIONALINFORMATION);
            serializer.endTag(UBLElements.NAMESPACE_EXT, UBLElements.EXTENSIONCONTENT);
            serializer.endTag(UBLElements.NAMESPACE_EXT, UBLElements.UBLEXTENSION);
            // FIN - UBL EXTENSION I
            // INICIO - UBL EXTENSION II
            serializer.startTag(UBLElements.NAMESPACE_EXT, UBLElements.UBLEXTENSION);
            serializer.startTag(UBLElements.NAMESPACE_EXT, UBLElements.EXTENSIONCONTENT);
            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.SIGNATURE);
            serializer.attribute(null, UBLElements.Id, context.getString(R.string.ExternalReferenceURI));

            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.SIGNEDINFO);


            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.CANNONICALIZATIONMETHOD);
            serializer.attribute(null, "Algorithm", "http://www.w3.org/2001/10/xml-exc-c14n#WithComments");
            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.CANNONICALIZATIONMETHOD);

            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.SIGNATUREMETHOD);
            serializer.attribute(null, "Algorithm", "http://www.w3.org/2000/09/xmldsig#rsa-sha1");
            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.SIGNATUREMETHOD);

            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.REFERENCE);
            serializer.attribute(null, "URI", "");

            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.TRANSFORMS);
            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.TRANSFORMS);
            serializer.attribute(null, "Algorithm", "http://www.w3.org/2000/09/xmldsig#enveloped-signature");
            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.TRANSFORMS);
            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.TRANSFORMS);
            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.DIGESTMETHOD);
            serializer.attribute(null, "Algorithm", "http://www.w3.org/2000/09/xmldsig#sha1");
            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.DIGESTMETHOD);
            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.DIGESTVALUE);
            //serializer.comment("APLICAR ALGOTIMO SHA-1");
            //serializer.text("CODIGO SHA1 GENERADO, YA TENGO EL ALGORITMO.");
            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.DIGESTVALUE);

            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.REFERENCE);
            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.SIGNEDINFO);

            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.SIGNATUREVALUE);
            serializer.text("RhaI3o9N7TQhnbnT/VT9edYLmQnv7yvBSnbHBtjh4SL1//Z6UJYj/VUlMsmQFlnPnIbBhz6eBPa1\n" +
                    "j3B3wxYGkoS+p6HSDheAVPMwiRiEA11ON6nl9ugUPW7q/4LFQ1hUbbnof/dwaLeUnsQo68hk7Gzc\n" +
                    "3wQmxRLY0acwzl2Ab7W+7/v9uNoMGG5djK79Kq0UEL/bUMYyQWlE7czNtcxRb8xNZ1b2xWqsXMPi\n" +
                    "3VshUjB+ufBScuQggYHJ4cmNuftAkjz5uP71OSSywz3UAzZiOb7krluCF4OkqdyA6qqh+9Hr8jYN\n" +
                    "oVPKBzOFpEwRC/waeTsGtgnnSITWbFy/oHufsQ==");
            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.SIGNATUREVALUE);

            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.KEYINFO);

            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.X509DATA);
            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.X509SUBJECTNAME);
            serializer.text("CN=CimaIT PE,OU=IT,O=CimaIT PE,L=Lima,ST=Lima,C=PE");
            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.X509SUBJECTNAME);
            serializer.startTag(UBLElements.NAMESPACE_DS, UBLElements.X509CERTIFICATE);
            //serializer.comment("AVERIGUAR CÓMO GENERARÉ ESTE CERTIFICADO X509, EL MISMO QUE UTILIZA LA UPEU");
            serializer.text("MIIDXzCCAkegAwIBAgIEc7E3IDANBgkqhkiG9w0BAQsFADBgMQswCQYDVQQGEwJQRTENMAsGA1UE\n" +
                    "CBMETGltYTENMAsGA1UEBxMETGltYTESMBAGA1UEChMJQ2ltYUlUIFBFMQswCQYDVQQLEwJJVDES\n" +
                    "MBAGA1UEAxMJQ2ltYUlUIFBFMB4XDTE1MDEwNjE5MjkyMFoXDTE2MDEwMTE5MjkyMFowYDELMAkG\n" +
                    "A1UEBhMCUEUxDTALBgNVBAgTBExpbWExDTALBgNVBAcTBExpbWExEjAQBgNVBAoTCUNpbWFJVCBQ\n" +
                    "RTELMAkGA1UECxMCSVQxEjAQBgNVBAMTCUNpbWFJVCBQRTCCASIwDQYJKoZIhvcNAQEBBQADggEP\n" +
                    "ADCCAQoCggEBAKfWDM6NbD5P5SNF0darkkPSCFHCPzaqKxdyGK5lZMWg89viXBzSTrRLqa4L91dt\n" +
                    "8yFf9CmsiX+ChhGL5wGnyxJpfC+v7RoWz/rFfv/bNgSnaOC9HYr9/AsB6i6pAkXBGfbF8cGH+42r\n" +
                    "NnwLfhzKp5oDkcnM5A8YJ4bdOSNqglGQ3diwYOUVBlt5bfTYNfu/GQ8Rvg8BryMJiVPt8Mw2D5JB\n" +
                    "PGDQS5oOZ2udjf2x5ZDDSdO5XCLewtIoYZPJaj+YiXdxMDSHWkB0a0hLax5/eI84bE20Dc9f+EaO\n" +
                    "seRO3WgMs+nkBkOnDrYyPUCrFY5XZslqV4zk/f9gzDlccOetPEMCAwEAAaMhMB8wHQYDVR0OBBYE\n" +
                    "FHmM09Z1eGO+rCINjvpllxPlUF5CMA0GCSqGSIb3DQEBCwUAA4IBAQCYZ21cFL2z7H+mSU+kXBON\n" +
                    "Ob2X09lsYgk/WkrA2KOtHQA5PO5iXIvvshfk4q4Z/MMcQZ2qXUwsC1Q6Wa/svevDocK3e/zbDBAA\n" +
                    "hu0Yjh9HscA9vV3X/c/mUp4oTUHPaN/ieirOzokdRcKjmHAc8t5HI83E1r91pmoDGIAP5RuPP9hE\n" +
                    "AqFtgrKMM7FjPCpvI3JWnsV4PuXEBDwA8HPUztOe8O2LUmjqviWa1fsYzMJJI2SU2Br06hk//WWs\n" +
                    "p2T3iR+m0XqnYZS6IWWSX1uRlN4Vt5MpgT8Yarhjx8NttEvUOenuvojxH4JKN2dNFa2oPI7kfE6p\n" +
                    "iB5WG7uhvUxoxLIp");
            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.X509CERTIFICATE);

            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.X509DATA);
            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.KEYINFO);
            serializer.endTag(UBLElements.NAMESPACE_DS, UBLElements.SIGNATURE);
            serializer.endTag(UBLElements.NAMESPACE_EXT, UBLElements.EXTENSIONCONTENT);
            serializer.endTag(UBLElements.NAMESPACE_EXT, UBLElements.UBLEXTENSION);
            //FIN - UBL EXTENSION II
            serializer.endTag(UBLElements.NAMESPACE_EXT, UBLElements.UBLEXTENSIONS);
            //FIN - UBL EXTENSIONS

            //SECCIÓN II - INFORMACIÓN GENERAL DE LA VENTA
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.UBLVERSIONID);
            serializer.text(context.getString(R.string.UBLVersionID));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.UBLVERSIONID);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.CUSTOMIZATIONID);
            serializer.text(context.getString(R.string.Customization));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.CUSTOMIZATIONID);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.text(idDocument);
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ISSUEDATE);
            Utils utils = new Utils();
            serializer.text(utils.getDayPhone());
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ISSUEDATE);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.INVOYCETYPECODE);
            if (tipoDocumento==1)
                serializer.text(context.getString(R.string.InvoiceTypeCodeFactura));
            else if(tipoDocumento==2)
                serializer.text(context.getString(R.string.InvoiceTypeCodeBoleta));
            else
                serializer.text(context.getString(R.string.InvoiceTypeCodeBoleta));

            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.INVOYCETYPECODE);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.DOCUMENTCURRENCYCODE);
            serializer.text(context.getString(R.string.DocumentCurrencyCode));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.DOCUMENTCURRENCYCODE);

            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.SIGNATURE);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.text(context.getString(R.string.SignatureID));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);

            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.SIGNATORYPARTY);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.PARTYIDENTIFIACTION);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.text(context.getString(R.string.RUC));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.PARTYIDENTIFIACTION);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.PARTYNAME);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.NAME);
            serializer.text(context.getString(R.string.Empresa));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.NAME);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.PARTYNAME);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.SIGNATORYPARTY);

            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.DIGITALSIGNATUREATTACHMENT);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.EXTERNALREFERENCE);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.URI);
            serializer.text("#" + context.getString(R.string.ExternalReferenceURI));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.URI);

            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.EXTERNALREFERENCE);

            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.DIGITALSIGNATUREATTACHMENT);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.SIGNATURE);

            //SECCIÓN III: INFORMACIÓN GENERAL DE LA EMPRESA
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.ACCOUNTINGSUPPLIERPARTY);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.CUSTOMERASSIGNEDACCOUNTID);
            serializer.text(context.getString(R.string.RUC));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.CUSTOMERASSIGNEDACCOUNTID);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ADDITIONALACCOUNTID);
            serializer.text(context.getString(R.string.AdditionalAccountID));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ADDITIONALACCOUNTID);

            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.PARTY);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.POSTALADDRESS);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.STREETNAME);
            serializer.text(context.getString(R.string.StreetName));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.STREETNAME);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.CITYSUBDIVISIONNAME);
            serializer.text(context.getString(R.string.CitySubdivisionName));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.CITYSUBDIVISIONNAME);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.CITYNAME);
            serializer.text(context.getString(R.string.CityName));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.CITYNAME);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.COUNTRYSUBENTITY);
            serializer.text(context.getString(R.string.CountrySubentity));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.COUNTRYSUBENTITY);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.DISTRICT);
            serializer.text(context.getString(R.string.District));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.DISTRICT);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.COUNTRY);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.IDENTIFICATIONCODE);
            serializer.text(context.getString(R.string.IdentificationCode));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.IDENTIFICATIONCODE);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.COUNTRY);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.POSTALADDRESS);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.PARTYLEGALENTITY);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.REGISTRATIONNAME);
            serializer.text(context.getString(R.string.RegistrationName));
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.REGISTRATIONNAME);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.PARTYLEGALENTITY);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.PARTY);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.ACCOUNTINGSUPPLIERPARTY);

            //SECCIÓN IV: INFORMACIÓN DEL USUARIO
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.ACCOUNTINGCUSTOMERPARTY);

            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.CUSTOMERASSIGNEDACCOUNTID);
            serializer.text(userRUC_DNI);
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.CUSTOMERASSIGNEDACCOUNTID);

            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ADDITIONALACCOUNTID);
            if (userRUC_DNI.length()==11)
                serializer.text(context.getString(R.string.UserAdditionalAccountIdRUC));
            else if(userRUC_DNI.length()==8)
                serializer.text(context.getString(R.string.UserAdditionalAccountIdDNI));
            else if (userRUC_DNI.length()>10)
                serializer.text(context.getString(R.string.UserAdditionalAccountIdRUC));
            else if (userRUC_DNI.length()<=10)
                serializer.text(context.getString(R.string.UserAdditionalAccountIdDNI));

            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ADDITIONALACCOUNTID);

            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.PARTY);


            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.PARTYLEGALENTITY);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.REGISTRATIONNAME);
            serializer.text(userName);
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.REGISTRATIONNAME);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.PARTYLEGALENTITY);

            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.PARTY);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.ACCOUNTINGCUSTOMERPARTY);

            //SECCIÓN V: IGV
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXTOTAL);

            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXAMOUNT);
            serializer.attribute(null, "currencyID", "PEN");
            serializer.text(igvTotal);
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXAMOUNT);

            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXSUBTOTAL);


            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXAMOUNT);
            serializer.attribute(null, "currencyID", "PEN");
            serializer.text(igvTotal);
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXAMOUNT);

            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXCATEGORY);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXSCHEME);

            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.text("1000");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.NAME);
            serializer.text("IGV");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.NAME);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXTYPECODE);
            serializer.text("VAT");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXTYPECODE);

            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXSCHEME);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXCATEGORY);

            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXSUBTOTAL);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXTOTAL);
            //SECCIÓN VI: MONTO TOTAL DE LA VENTA
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.LEGALMONETARYTOTAL);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.PAYABLEAMOUNT);
            serializer.attribute(null, "currencyID", "PEN");
            serializer.text(importeTotalVenta);
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.PAYABLEAMOUNT);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.LEGALMONETARYTOTAL);

            // INVOCIE LINE
            //SECCIÓN VII: SECCIÓN DINÁMICA, HABRÁ TANTAS SECCIONES COMO ITEMS DE VENTA
            cursorTemp.moveToFirst();
            if (cursorTemp.getCount()>0) {
                int i=0;
                for (cursorTemp.moveToFirst(); !cursorTemp.isAfterLast(); cursorTemp.moveToNext()) {


                    DecimalFormat df = new DecimalFormat("#.00");/*
                    int _id = cursorTemp.getInt(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_venta_detalle));
                    int id_producto = cursorTemp.getInt(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_id_producto));*/
                    String nombre_producto = cursorTemp.getString(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_nom_producto));
                    int cantidad = cursorTemp.getInt(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_cantidad));
                    Double precio_unitario = cursorTemp.getDouble(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_precio_unit));
                    /*Double importe = cursorTemp.getDouble(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_importe));

                    String promedio_anterior = cursorTemp.getString(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_prom_anterior));
                    String devuelto = cursorTemp.getString(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_devuelto));
                    int valorUnidad = cursorTemp.getInt(cursorTemp.getColumnIndexOrThrow(DBAdapter_Temp_Venta.temp_valor_unidad));*/
                    String codigoProducto = cursorTemp.getString(cursorTemp.getColumnIndex(DBAdapter_Temp_Venta.temp_codigo_producto));

                    Double valorUnitarioItem = precio_unitario/1.18;
                    Double valorVentaItem = (cantidad*precio_unitario)/1.18;
                    Double igvItem = valorVentaItem*0.18;
                    /*serializer.comment("***********************************");
                    serializer.comment("NOMBRE : "+nombre_producto);
                    serializer.comment("CODIGO : "+codigoProducto);
                    serializer.comment("CANTIDAD : "+cantidad);
                    serializer.comment("PRECIO UNITARIO : "+precio_unitario);
                    serializer.comment("VALOR UNITARIO : "+precio_unitario/1.18);
                    serializer.comment("VALOR VENTA ITEM: "+valorVentaItem);
                    serializer.comment("IGV : "+valorVentaItem*0.18);
                    serializer.comment("************************************");*/

                    serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.INVOICELINE);

                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
                    i++;
                    serializer.text(""+i);
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.INVOICEDQUANTITY);
                    serializer.attribute(null, "unitCode", "NIU");
                    serializer.text(""+cantidad);
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.INVOICEDQUANTITY);
                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.LINEEXTENSIONAMOUNT);
                    serializer.attribute(null, "currencyID", "PEN");
                    serializer.text(df.format(valorVentaItem));
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.LINEEXTENSIONAMOUNT);

                    serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.PRICINGREFERENCE);
                    serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.ALTERNATIVECONDITIONPRICE);
                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.PRICEAMOUNT);
                    serializer.attribute(null, "currencyID", "PEN");
                    serializer.text(df.format(precio_unitario));
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.PRICEAMOUNT);
                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.PRICETYPECODE);
                    serializer.text("01");
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.PRICETYPECODE);

                    serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.ALTERNATIVECONDITIONPRICE);
                    serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.PRICINGREFERENCE);

                    serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXTOTAL);
                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXAMOUNT);
                    serializer.attribute(null, "currencyID", "PEN");
                    serializer.text(df.format(igvItem));
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXAMOUNT);
                    serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXSUBTOTAL);
                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXAMOUNT);
                    serializer.attribute(null, "currencyID", "PEN");
                    serializer.text(df.format(igvItem));
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXAMOUNT);
                    serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXCATEGORY);

                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXEXEMPTIONREASONCODE);
                    serializer.text("10");
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXEXEMPTIONREASONCODE);
                    serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXSCHEME);
                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
                    serializer.text("1000");
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.NAME);
                    serializer.text("IGV");
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.NAME);
                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXTYPECODE);
                    serializer.text("VAT");
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXTYPECODE);
                    serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXSCHEME);
                    serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXCATEGORY);
                    serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXSUBTOTAL);
                    serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXTOTAL);

                    serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.ITEM);
                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.DESCRIPTION);
                    serializer.text(""+nombre_producto);
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.DESCRIPTION);
                    serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.SELLERSITEMIDENTIFICATION);
                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
                    serializer.text(""+codigoProducto);
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
                    serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.SELLERSITEMIDENTIFICATION);


                    serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.ITEM);

                    serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.PRICE);
                    serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.PRICEAMOUNT);
                    serializer.attribute(null, "currencyID", "PEN");
                    serializer.text(df.format(valorUnitarioItem));
                    serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.PRICEAMOUNT);
                    serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.PRICE);
                    serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.INVOICELINE);
                }
            }

            //INICIO ÚLTIMO INVOICE LINE
/*
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.INVOICELINE);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.text("2");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);

            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.INVOICEDQUANTITY);
            serializer.attribute(null, "unitCode", "EA");
            serializer.text("12");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.INVOICEDQUANTITY);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.LINEEXTENSIONAMOUNT);
            serializer.attribute(null, "CurrencyID", "PEN");
            serializer.text("2.2881");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.LINEEXTENSIONAMOUNT);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.PRICINGREFERENCE);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.ALTERNATIVECONDITIONPRICE);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.PRICEAMOUNT);
            serializer.attribute(null, "CurrencyID", "PEN");
            serializer.text("2.2881");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.PRICEAMOUNT);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.PRICETYPECODE);
            serializer.text("01");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.PRICETYPECODE);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.ALTERNATIVECONDITIONPRICE);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.PRICINGREFERENCE);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXTOTAL);

            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXAMOUNT);
            serializer.attribute(null, "CurrencyID", "PEN");
            serializer.text("0.00");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXAMOUNT);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXSUBTOTAL);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXAMOUNT);
            serializer.attribute(null, "CurrencyID", "PEN");
            serializer.text("0.00");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXAMOUNT);

            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXCATEGORY);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXEXEMPTIONREASONCODE);
            serializer.text("30");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXEXEMPTIONREASONCODE);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXSCHEME);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.text("1000");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.NAME);
            serializer.text("IGV");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.NAME);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXTYPECODE);
            serializer.text("VAT");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.TAXTYPECODE);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXSCHEME);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXCATEGORY);

            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXSUBTOTAL);


            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.TAXTOTAL);

            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.ITEM);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.DESCRIPTION);
            serializer.text("Tostada Blanca x10");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.DESCRIPTION);
            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.SELLERSITEMIDENTIFICATION);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.ID);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.SELLERSITEMIDENTIFICATION);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.ITEM);

            serializer.startTag(UBLElements.NAMESPACE_CAC, UBLElements.PRICE);
            serializer.startTag(UBLElements.NAMESPACE_CBC, UBLElements.PRICEAMOUNT);
            serializer.attribute(null, "currencyID", "PEN");
            serializer.text("27.4572");
            serializer.endTag(UBLElements.NAMESPACE_CBC, UBLElements.PRICEAMOUNT);
            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.PRICE);

            serializer.endTag(UBLElements.NAMESPACE_CAC, UBLElements.INVOICELINE);*/




            serializer.endTag(null, UBLElements.INVOICE);
            serializer.endDocument();
            serializer.flush();

            if (fout != null) {
                fout.close();
            }


            //Toast.makeText(context, "Escrito correctamente", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public byte[] leerXML(Context context) throws IOException, ParserConfigurationException, SAXException {
        fis = context.openFileInput("test.xml");
        /*InputStream is = context.openFileInput("test.xml");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        */
        //System.out.println(file.exists() + "!!");
        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[(int) fis.getChannel().size()];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                //System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            //Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
/*
        //DOM (Por ejemplo)
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document dom = builder.parse(fil);

        StreamResult streamResult = new StreamResult(fil);


        String documentoTexto = dom.toString();

        //A partir de aquí se trataría el árbol DOM como siempre.
        //Por ejemplo:
        Element root = dom.getDocumentElement();

        return documentoTexto;*/
    }

    public String putSHA1toXML(Context context, String nombreDocumento, String SHA1) throws IOException, ParserConfigurationException, SAXException, TransformerException {


        FileInputStream file = context.openFileInput("test.xml");
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(file);

        // Change the content of node
        Node nodes = doc.getElementsByTagName("ds:DigestValue").item(0);
        Log.d("NODES DIGESTVALUE", "" + nodes.getNodeName() + "," + nodes.getTextContent() + "," + nodes.getNodeValue() + "," + nodes.getLocalName());

        nodes.setNodeValue(SHA1);
        nodes.setTextContent(SHA1);
        Log.d("NODES SHA1 PUT",""+nodes.getTextContent());

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

/*
        File roott = Environment.getExternalStorageDirectory();
        File filee = new File(roott, "factura/test.xml");
*/

        FileOutputStream fileOutput= context.openFileOutput("test.xml", context.MODE_PRIVATE);

        // initialize StreamResult with File object to save to file
        //StreamResult resultSinFirmar = new StreamResult(new FileOutputStream(getFileStreamPath("test.xml")));

        /*File myFile = new File("/sdcard/test.xml");
        boolean created = myFile.createNewFile();
        Log.d("FILE CREATED", ""+created);
        FileOutputStream fOut = new FileOutputStream(myFile);*/

        Log.d("FILE DIR",""+context.getFilesDir());
        Log.d("GENERATE DOCUMENT",""+nombreDocumento+".xml");
        File myFile = new File(context.getFilesDir(), nombreDocumento+".xml");
        StreamResult result = new StreamResult(myFile);
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);



        return SHA1;

        /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[(int) fis.getChannel().size()];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                //System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            //Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] bytes = bos.toByteArray();*/


        /*Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        // initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(filee);
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);*/



        /*NodeList nodeList= doc.getChildNodes();

        for (int i= 0; i<nodeList.getLength(); i++){

            Node node = nodeList.item(i);
            NodeList nodeListSon = node.getChildNodes();

            for (int j= 0; j<nodeListSon.getLength(); j++){
                Log.d("NODES", "" + nodeListSon.item(j).getNodeName());
            }

        }
*/
        //return bytes;
       /* try {
            File root = Environment.getExternalStorageDirectory();
            File file1 = new File(root, "factura/test2.xml");

            FileInputStream file= context.openFileInput("test.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);
            // Change the content of node
            Node nodes = doc.getElementsByTagName("DigestValue").item(0);

            NodeList keyList = doc.getElementsByTagName("DigestValue");
            Node Keynode = keyList.item(0);
            Element fstElmnt = (Element) Keynode;
            fstElmnt.setAttribute("value", SHA1);//set the value of new edited ip here
            String newNodeValue =   fstElmnt.getAttribute("value");
            Log.d("SHA1", newNodeValue);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(file1);
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);

            Log.d("SHA1", newNodeValue);
        } catch (Exception e) {
            System.out.println("XML Parsing Excpetion = " + e);
        }*/

        /*try {
            //Obtenemos la referencia al fichero XML de entrada
            FileInputStream fil = context.openFileInput("test.xml");

//DOM (Por ejemplo)
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(fil);

//A partir de aquí se trataría el árbol DOM como siempre.
//Por ejemplo:
            Element root = dom.getDocumentElement();

            // Change the content of node
            Node nodes = dom.getElementsByTagName("DigestValue").item(0);
            // I changed the below line form nodes.setNodeValue to nodes.setTextContent
            nodes.setTextContent(SHA1);
            Log.d("SHA1 - TEXTO ", SHA1);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            *//*File roott = Environment.getExternalStorageDirectory();
            File filee = new File(roott, "factura/test.xml");*//*

            FileOutputStream file= context.openFileOutput("test2.xml", MODE_PRIVATE);
            // initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(file);
            DOMSource source = new DOMSource(dom);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }*/


        /*//Obtenemos la referencia al fichero XML de entrada
        FileInputStream fil = context.openFileInput("prueba.xml");

//DOM (Por ejemplo)
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document dom = builder.parse(fil);

//A partir de aquí se trataría el árbol DOM como siempre.
//Por ejemplo:
        Element root = dom.getDocumentElement();*/


    }


}
