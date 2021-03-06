/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package union.union_vr1.FacturacionElectronica;

import android.database.Cursor;
import android.util.Log;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import union.union_vr1.R;
import union.union_vr1.Sqlite.DBAdapter_Temp_Venta;

/**
 *
 * @author Usuario
 */
public class SimpleXMLAndroid {

    private static String TAG = SimpleXMLAndroid.class.getSimpleName();
    public static File generarXMLown(File xmlFile, Map<String, String> map, Cursor cursor) throws Exception{
        
        //INFORMACIÓN GENERAL DE LA VENTA
        String tipo_documento = map.get("tipo_documento");
        String id_documento = map.get("id_documento");
        String user_ruc_dni = map.get("user_ruc_dni");
        String user_name = map.get("user_name");
        String total_operaciones_gravadas = map.get("total_operaciones_gravadas");
        String total_importe_venta = map.get("total_importe_venta");
        String total__igv = map.get("total__igv");




        // INFORMACIÓN DE PRUEBA

        
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        
        DecimalFormat df = new DecimalFormat("0.00", otherSymbols);
        
        
        UblInvoiceXML invoiceXML = new UblInvoiceXML();


        //  II
                        //DATOS POR DEFECTO DE LA FACTURA
			invoiceXML.setUblVersionID("2.0");
			invoiceXML.setCustomizationID("1.0");
			invoiceXML.setID(id_documento);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			invoiceXML.setIssueDate(dateFormat.format(new Date()));
			invoiceXML.setInvoiceTypeCode(tipo_documento);//TIPO DOCUMENTO FACTURA = 01, BOLETA = 03
			invoiceXML.setDocumentCurrencyCode("PEN");//MONEDA NACIONAL
                        
                        //DATOS SIGNATURE DE LA EMPRESA
                        
                        UblSignature signature = new UblSignature();
                        signature.setID("IDSignUNION");
                        
                        UblSignatoryParty signatoryParty = new UblSignatoryParty();
                        UblPartyIdentification identification = new UblPartyIdentification();
                        identification.setID("20138122256");
                        signatoryParty.setPartyIdentification(identification);
                        UblPartyName partyName = new UblPartyName();
                        partyName.setName("Universidad Peruana UNION");
                        signatoryParty.setPartyName(partyName);
                        signature.setSignatoryParty(signatoryParty);
                        
                        UblDigitalSignatureAttachment digitalSignatureAttachment = new UblDigitalSignatureAttachment();
                        UblExternalReference externalReference = new UblExternalReference();
                        externalReference.setURI("#SignatureUNION");
                        digitalSignatureAttachment.setExternalReference(externalReference);
                        signature.setDigitalSignatureAttachment(digitalSignatureAttachment);
                        
                        invoiceXML.setSignature(signature);

                        //III
                        //DATOS DE UBICACIÓN DE LA EMPRESA
                        UblAccountingSupplierParty accountingSupplierParty = new UblAccountingSupplierParty();
                        accountingSupplierParty.setCustomerAssignedAccountID("20138122256");
                        accountingSupplierParty.setAdditionalAccountID("6");
                        
                        UblParty party = new UblParty();
                        
                        UblPartyName name = new UblPartyName("UNIVERSIDAD PERUANA UNION");
                        party.setPartyName(name);
                        
                        UblPostalAddress postalAddress = new UblPostalAddress();
                        postalAddress.setID("150118");
                        postalAddress.setStreetName("CAR. CENTRAL KM. 19");
                        postalAddress.setCitySubdivisionName("VILLA UNION-NANA");
                        postalAddress.setCityName("LIMA");
                        postalAddress.setCountrySubentity("LIMA");
                        postalAddress.setDistrict("LURIGANCHO");
                        UblCountry country = new UblCountry();
                        country.setIdentificationCode("PE");
                        postalAddress.setCountry(country);
                        party.setPostalAddress(postalAddress);
                        
                        
                        UblPartyLegalEntity legalEntity = new UblPartyLegalEntity();
                        //legalEntity.setRegistrationName("PRODUCTOS UNION");
                        legalEntity.setRegistrationName("UNIVERSIDAD PERUANA UNION");
                        party.setPartyLegalEntity(legalEntity);
                        
                        accountingSupplierParty.setParty(party);
                        
                        invoiceXML.setAccountingSupplierParty(accountingSupplierParty);
                        
                        //INFORMACIÓN SOBRE EL CLIENTE
                        UblAccountingCustomerParty accountingCustomerParty = new UblAccountingCustomerParty();

                        if (tipo_documento.equals("03")){
                            if (user_ruc_dni.length()==11){
                                user_ruc_dni =  user_ruc_dni.substring(2,10);
                            }
                        }


                            accountingCustomerParty.setCustomerAssignedAccountID(user_ruc_dni);
                            if (user_ruc_dni.length()==11)
                                accountingCustomerParty.setAdditionalAccountID("6");
                            else if(user_ruc_dni.length()==8)
                                accountingCustomerParty.setAdditionalAccountID("1");
                            else
                                accountingCustomerParty.setAdditionalAccountID("6");

                        
                        UblParty partyCliente = new UblParty();
                        UblPartyLegalEntity legalEntityCliente = new UblPartyLegalEntity();
                        legalEntityCliente.setRegistrationName(user_name);
                        partyCliente.setPartyLegalEntity(legalEntityCliente);
                        accountingCustomerParty.setParty(partyCliente);
                        
                        invoiceXML.setAccountingCustomerParty(accountingCustomerParty);
                        
                        //IV
                        //INFORMACIÓN SOBRE EL IMPUESTO GENERAL
                        
                        UblTaxTotal taxTotal = new UblTaxTotal();
                        
                        UblTaxAmount amount = new UblTaxAmount();
                        amount.setCurrencyID("PEN");
                        amount.setTaxAmount(total__igv);
                        
                        taxTotal.setTaxAmount(amount);
                        
                        UblTaxSubtotal taxSubtotal = new UblTaxSubtotal();
                        taxSubtotal.setTaxAmount(amount);
                        
                        UblTaxCategory taxCategory = new UblTaxCategory();
                        taxCategory.setTaxExemptionReasonCode("10");
                        UblTaxScheme taxScheme = new UblTaxScheme();
                        taxScheme.setID("1000");
                        taxScheme.setName("IGV");
                        taxScheme.setTaxTypeCode("VAT");
                        taxCategory.setTaxScheme(taxScheme);
                        
                        taxSubtotal.setTaxCategory(taxCategory);
                        
                        taxTotal.setTaxSubtotal(taxSubtotal);
                        
                        invoiceXML.setTaxTotal(taxTotal);
                        
                        //INFORMACIÓN GENERAL SOBRE EL LEGAL MONETARY TOTAL
                        
                        UblLegalMonetaryTotal legalMonetaryTotal = new UblLegalMonetaryTotal();
                        UblPayableAmount payableAmount = new UblPayableAmount();
                        payableAmount.setCurrencyID("PEN");
                        payableAmount.setPayableAmount(total_importe_venta);
                        legalMonetaryTotal.setPayableAmount(payableAmount);
                        
                        invoiceXML.setLegalMonetaryTotal(legalMonetaryTotal);
                        

                        //V
                         //INICIO DE LOS INVOICE LINE
                        cursor.moveToFirst();

                        if (cursor.getCount()>0){
                            int i= 0;
                            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                                //OBTENEMOS LOS DATOS DEL ITEM ACTUAL
                                String nombre_producto = cursor.getString(cursor.getColumnIndex(DBAdapter_Temp_Venta.temp_nom_producto));
                                int cantidad = cursor.getInt(cursor.getColumnIndex(DBAdapter_Temp_Venta.temp_cantidad));
                                Double precio_unitario = cursor.getDouble(cursor.getColumnIndex(DBAdapter_Temp_Venta.temp_precio_unit));
                                String codigoProducto = cursor.getString(cursor.getColumnIndex(DBAdapter_Temp_Venta.temp_codigo_producto));
                                Double valor_unitario = precio_unitario/1.18;
                                Double valor_venta_bruto = (cantidad*precio_unitario)/1.18;
                                Double descuentos = 0.00;
                                Double valor_venta_por_item = valor_venta_bruto - descuentos;
                                Double igv = valor_venta_por_item * 0.18;
                                Double total = cantidad*precio_unitario;



                                UblInvoiceLine invoiceLine = new UblInvoiceLine();
                                invoiceLine.setID(++i);

                                UblInvoicedQuantity invoicedQuantity = new UblInvoicedQuantity();
                                invoicedQuantity.setUnitCode("NIU");
                                invoicedQuantity.setInvoicedQuantity(cantidad);
                                invoiceLine.setInvoicedQuantity(invoicedQuantity);

                                UblLineExtensionAmount lineExtensionAmount = new UblLineExtensionAmount();
                                lineExtensionAmount.setCurrencyID("PEN");
                                lineExtensionAmount.setLineExtensionAmount(df.format(valor_venta_por_item));
                                invoiceLine.setLineExtensionAmount(lineExtensionAmount);

                                UblPricingReference pricingReference = new UblPricingReference();
                                UblAlternativeConditionPrice alternativeConditionPrice = new UblAlternativeConditionPrice();
                                UblPriceAmount priceAmount = new UblPriceAmount();
                                priceAmount.setCurrencyID("PEN");
                                priceAmount.setPriceAmount(df.format(precio_unitario));
                                alternativeConditionPrice.setPriceAmount(priceAmount);
                                alternativeConditionPrice.setPriceTypeCode("01");
                                pricingReference.setAlternativeConditionPrice(alternativeConditionPrice);
                                invoiceLine.setPricingReference(pricingReference);

                                UblTaxTotal taxTotal1 = new UblTaxTotal();
                                UblTaxAmount amount1 = new UblTaxAmount();
                                amount1.setCurrencyID("PEN");
                                amount1.setTaxAmount(df.format(igv));
                                Log.d("FORMAT IGV", df.format(igv));
                                taxTotal1.setTaxAmount(amount1);

                                UblTaxSubtotal taxSubtotal1 = new UblTaxSubtotal();
                                taxSubtotal1.setTaxAmount(amount1);
                                UblTaxCategory taxCategory1 = new UblTaxCategory();
                                taxCategory1.setTaxExemptionReasonCode("10");
                                taxSubtotal1.setTaxCategory(taxCategory1);
                                UblTaxScheme scheme = new UblTaxScheme();
                                scheme.setID("1000");
                                scheme.setName("IGV");
                                scheme.setTaxTypeCode("VAT");
                                taxCategory1.setTaxScheme(scheme);
                                taxTotal1.setTaxSubtotal(taxSubtotal1);


                                invoiceLine.setTaxTotal(taxTotal1);

                                UblItem item = new UblItem();
                                item.setDescription(nombre_producto);
                                UblSellersItemIdentification sellersItemIdentification = new UblSellersItemIdentification();
                                sellersItemIdentification.setID(""+codigoProducto);
                                item.setSellersItemIdentification(sellersItemIdentification);
                                invoiceLine.setItem(item);

                                UblPrice price = new UblPrice();
                                UblPriceAmount priceAmount1 = new UblPriceAmount();
                                priceAmount1.setCurrencyID("PEN");
                                priceAmount1.setPriceAmount(df.format(valor_unitario));
                                price.setPriceAmount(priceAmount1);

                                invoiceLine.setPrice(price);

                                invoiceXML.listInvoiceLine.add(invoiceLine);

                            }

                        }

                        //PRUEBA
//                        invoiceXML.listInvoiceLine.add(invoiceLine);
//                        invoiceXML.listInvoiceLine.add(invoiceLine);
//                        invoiceXML.listInvoiceLine.add(invoiceLine);
                        

                        //I
                         // INFORMACIÓN ADICIONAL Y SIGNATURE
                        UblExtensions ublExtensions = new UblExtensions();
                        
                        //INFORMACIÓN UBL EXTENSION DE LA VENTA DE LA VENTA
                        UblExtension extension = new UblExtension();
                        UblExtensionContent content = new UblExtensionContent();
                        UblAdditionalInformation  additionalInformation = new UblAdditionalInformation();
                        UblAdditionalMonetaryTotal additionalMonetaryTotal = new UblAdditionalMonetaryTotal();
                        additionalMonetaryTotal.setID(1001);//1001 OPERACIONES GRAVADAS
                        UblPayableAmount amountGeneral = new UblPayableAmount();
                        amountGeneral.setCurrencyID("PEN");
                        amountGeneral.setPayableAmount(total_operaciones_gravadas);
                        additionalMonetaryTotal.setPayableAmount(amountGeneral);
                        
                        //COLOCAR UNA LISTA, PUEDE HABER MÁS DE UN ADDITIONAL MONETARY TOTAL, EPRO PARA EFECTOS PRÁCTICOS O ES NECESARIO. INCLUSO GUARDA IGUALDAD CON LA GENERACIÓN DE LA UNIVERSIDAD ES OPCIONAL.
                        additionalInformation.setAdditionalMonetaryTotal(additionalMonetaryTotal);
                        
                        UblAdditionalProperty additionalProperty = new UblAdditionalProperty();
                        additionalProperty.setID(1000);
                        //RECORDAR VERIFICAR LOS VALORES DE CIENTOS DE COLES : SEISCIENTOS =! SEICIENTOS.
                        additionalProperty.setValue(NumberToLetterConverter.convertNumberToLetter(total_importe_venta));
                        additionalInformation.setAdditionalProperty(additionalProperty);
                        content.setAdditionalInformation(additionalInformation);
                        extension.setExtensionContent(content);
                        
                        //INFORMACIÓN SOBRE EL FIRMADO DIGITAL
                        
                        UblExtension extensionFirmado =  new UblExtension();
                        UblExtensionContent contentFirmado = new UblExtensionContent();

                        extensionFirmado.setExtensionContent(contentFirmado);
                                                
//                        List<UblExtension> extensions = new ArrayList<>();
//                        extensions.add(extension);
//                        extensions.add(extensionFirmado);
//                        ublExtensions.setUblExtension(extensions);
                        
                        ublExtensions.listUblExtension.add(extension);
                        ublExtensions.listUblExtension.add(extensionFirmado);
                        
                        
                        
                        invoiceXML.setUblExtensions(ublExtensions);
                        

        
        //File xmlFile = new File(PATH_FIRMADO);


                try
                {

                    // Serialize the Person
                    Serializer serializer = new Persister(new Format(0,"<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>"));
                    //Serializer serializer = new Persister();
                    serializer.write(invoiceXML, xmlFile);
                    /*UblInvoiceXML invoiceRead = serializer.read(UblInvoiceXML.class, xmlFile);
                    System.out.println("INVOICE VALUE TOTAL: "+invoiceRead.getUblExtensions().getListUblExtension().get(0).getExtensionContent().getAdditionalInformation().getAdditionalProperty().getValue());
                    System.out.println("INVOICE READ: "+invoiceRead.getID());
                    System.out.println("INVOICE SIGNATURE : "+invoiceRead.getSignature().getSignatoryParty().getPartyIdentification().getID());
                    System.out.println("INVOICE UBICACION "+invoiceRead.getAccountingSupplierParty().getParty().getPostalAddress().getStreetName());
                    System.out.println("INVOICE CLIENTE: "+invoiceRead.getAccountingCustomerParty().getParty().getPartyLegalEntity().getRegistrationName());
                    System.out.println("INVOICE IMPUESTO: "+invoiceRead.getTaxTotal().getTaxSubtotal().getTaxCategory().getTaxScheme().getTaxTypeCode());
                    System.out.println("INVOICE TOTAL PAYABLE: "+invoiceRead.getLegalMonetaryTotal().getPayableAmount().getPayableAmount());

                    for(int i=0; i<invoiceRead.getListInvoiceLine().size(); i++){
                        int index = i+1;
                        System.out.println("INVOICE ITEM: "+ index);
                        System.out.println("INVOICE ITEM NAME: "+ invoiceRead.getListInvoiceLine().get(i).getItem().getDescription());

                    }
                    */
                    
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }finally{
                    //Signature.add(xmlFile);
                    
                }
                
                //System.out.println("PATH: "+xmlFile.getAbsolutePath());
        
        
        return xmlFile;
    }


    public static File generateCreditNote(File xmlFile, Map<String, String> map, Cursor cursor){

        //INFORMACIÓN GENERAL DE LA VENTA
        String tipo_documento = map.get("tipo_documento");
        String id_documento = map.get("id_documento");
        String user_ruc_dni = map.get("user_ruc_dni");
        String user_name = map.get("user_name");
        String total_operaciones_gravadas = map.get("total_operaciones_gravadas");
        String total_importe_venta = map.get("total_importe_venta");
        String total__igv = map.get("total__igv");




        // INFORMACIÓN DE PRUEBA
        Log.d(TAG, "GENERATE CREDIT NOTE INICIANDO...");


        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');

        DecimalFormat df = new DecimalFormat("0.00", otherSymbols);


        UblCreditNote creditNoteXML = new UblCreditNote();


        //  II
        //DATOS POR DEFECTO DE LA FACTURA
        creditNoteXML.setUblVersionID("2.0");
        creditNoteXML.setCustomizationID("1.0");
        creditNoteXML.setID(id_documento);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        creditNoteXML.setIssueDate(dateFormat.format(new Date()));
        //invoiceXML.setInvoiceTypeCode(tipo_documento);//TIPO DOCUMENTO FACTURA = 01, BOLETA = 03
        creditNoteXML.setDocumentCurrencyCode("PEN");//MONEDA NACIONAL

        UblDiscrepancyResponse ublDiscrepancyResponse = new UblDiscrepancyResponse();
        ublDiscrepancyResponse.setReferenceID(id_documento);
        ublDiscrepancyResponse.setResponseCode("07");
        ublDiscrepancyResponse.setDescription("Unidades Defectuosas, no leen CD que contengan archivos MP3.");
        creditNoteXML.setUblDiscrepancyResponse(ublDiscrepancyResponse);

        UblBillingReference ublBillingReference = new UblBillingReference();
        UblInvoiceDocumentReference ublInvoiceDocumentReference = new UblInvoiceDocumentReference();
        ublInvoiceDocumentReference.setID("F001-4355");
        ublInvoiceDocumentReference.setDocumentTypeCode("01");
        ublBillingReference.setInvoiceDocumentReference(ublInvoiceDocumentReference);
        creditNoteXML.setUblBillingReference(ublBillingReference);


        //DATOS SIGNATURE DE LA EMPRESA

        UblSignature signature = new UblSignature();
        signature.setID("IDSignUNION");

        UblSignatoryParty signatoryParty = new UblSignatoryParty();
        UblPartyIdentification identification = new UblPartyIdentification();
        identification.setID("20138122256");
        signatoryParty.setPartyIdentification(identification);
        UblPartyName partyName = new UblPartyName();
        partyName.setName("Universidad Peruana UNION");
        signatoryParty.setPartyName(partyName);
        signature.setSignatoryParty(signatoryParty);

        UblDigitalSignatureAttachment digitalSignatureAttachment = new UblDigitalSignatureAttachment();
        UblExternalReference externalReference = new UblExternalReference();
        externalReference.setURI("#SignatureUNION");
        digitalSignatureAttachment.setExternalReference(externalReference);
        signature.setDigitalSignatureAttachment(digitalSignatureAttachment);

        creditNoteXML.setSignature(signature);
        //I
        // INFORMACIÓN ADICIONAL Y SIGNATURE
        UblExtensions ublExtensions = new UblExtensions();

        //INFORMACIÓN UBL EXTENSION DE LA VENTA DE LA VENTA
        UblExtension extension = new UblExtension();
        UblExtensionContent content = new UblExtensionContent();
        UblAdditionalInformation  additionalInformation = new UblAdditionalInformation();
        UblAdditionalMonetaryTotal additionalMonetaryTotal = new UblAdditionalMonetaryTotal();
        additionalMonetaryTotal.setID(1001);//1001 OPERACIONES GRAVADAS
        UblPayableAmount amountGeneral = new UblPayableAmount();
        amountGeneral.setCurrencyID("PEN");
        amountGeneral.setPayableAmount(total_operaciones_gravadas);
        additionalMonetaryTotal.setPayableAmount(amountGeneral);

        //COLOCAR UNA LISTA, PUEDE HABER MÁS DE UN ADDITIONAL MONETARY TOTAL, EPRO PARA EFECTOS PRÁCTICOS O ES NECESARIO. INCLUSO GUARDA IGUALDAD CON LA GENERACIÓN DE LA UNIVERSIDAD ES OPCIONAL.
        additionalInformation.setAdditionalMonetaryTotal(additionalMonetaryTotal);

        /*UblAdditionalProperty additionalProperty = new UblAdditionalProperty();
        additionalProperty.setID(1000);
        //RECORDAR VERIFICAR LOS VALORES DE CIENTOS DE COLES : SEISCIENTOS =! SEICIENTOS.
        additionalProperty.setValue(NumberToLetterConverter.convertNumberToLetter(total_importe_venta));
        additionalInformation.setAdditionalProperty(additionalProperty);*/
        content.setAdditionalInformation(additionalInformation);
        extension.setExtensionContent(content);

        //INFORMACIÓN SOBRE EL FIRMADO DIGITAL

        UblExtension extensionFirmado =  new UblExtension();
        UblExtensionContent contentFirmado = new UblExtensionContent();

        extensionFirmado.setExtensionContent(contentFirmado);

//                        List<UblExtension> extensions = new ArrayList<>();
//                        extensions.add(extension);
//                        extensions.add(extensionFirmado);
//                        ublExtensions.setUblExtension(extensions);

        ublExtensions.listUblExtension.add(extension);
        ublExtensions.listUblExtension.add(extensionFirmado);



        creditNoteXML.setUblExtensions(ublExtensions);




        //III
        //DATOS DE UBICACIÓN DE LA EMPRESA
        UblAccountingSupplierParty accountingSupplierParty = new UblAccountingSupplierParty();
        accountingSupplierParty.setCustomerAssignedAccountID("20138122256");
        accountingSupplierParty.setAdditionalAccountID("6");

        UblParty party = new UblParty();

        UblPartyName name = new UblPartyName("UNIVERSIDAD PERUANA UNION");
        party.setPartyName(name);

        UblPostalAddress postalAddress = new UblPostalAddress();
        postalAddress.setID("150118");
        postalAddress.setStreetName("CAR. CENTRAL KM. 19");
        postalAddress.setCitySubdivisionName("VILLA UNION-NANA");
        postalAddress.setCityName("LIMA");
        postalAddress.setCountrySubentity("LIMA");
        postalAddress.setDistrict("LURIGANCHO");
        UblCountry country = new UblCountry();
        country.setIdentificationCode("PE");
        postalAddress.setCountry(country);
        party.setPostalAddress(postalAddress);


        UblPartyLegalEntity legalEntity = new UblPartyLegalEntity();
        //legalEntity.setRegistrationName("PRODUCTOS UNION");
        legalEntity.setRegistrationName("UNIVERSIDAD PERUANA UNION");
        party.setPartyLegalEntity(legalEntity);

        accountingSupplierParty.setParty(party);

        creditNoteXML.setAccountingSupplierParty(accountingSupplierParty);

        //INFORMACIÓN SOBRE EL CLIENTE
        UblAccountingCustomerParty accountingCustomerParty = new UblAccountingCustomerParty();

        if (tipo_documento.equals("03")){
            if (user_ruc_dni.length()==11){
                user_ruc_dni =  user_ruc_dni.substring(2,10);
            }
        }


        accountingCustomerParty.setCustomerAssignedAccountID(user_ruc_dni);
        if (user_ruc_dni.length()==11)
            accountingCustomerParty.setAdditionalAccountID("6");
        else if(user_ruc_dni.length()==8)
            accountingCustomerParty.setAdditionalAccountID("1");
        else
            accountingCustomerParty.setAdditionalAccountID("6");


        UblParty partyCliente = new UblParty();
        UblPartyLegalEntity legalEntityCliente = new UblPartyLegalEntity();
        legalEntityCliente.setRegistrationName(user_name);
        partyCliente.setPartyLegalEntity(legalEntityCliente);
        accountingCustomerParty.setParty(partyCliente);

        creditNoteXML.setAccountingCustomerParty(accountingCustomerParty);

        //IV
        //INFORMACIÓN SOBRE EL IMPUESTO GENERAL

        UblTaxTotal taxTotal = new UblTaxTotal();

        UblTaxAmount amount = new UblTaxAmount();
        amount.setCurrencyID("PEN");
        amount.setTaxAmount(total__igv);

        taxTotal.setTaxAmount(amount);

        UblTaxSubtotal taxSubtotal = new UblTaxSubtotal();
        taxSubtotal.setTaxAmount(amount);

        UblTaxCategory taxCategory = new UblTaxCategory();
        taxCategory.setTaxExemptionReasonCode("10");
        UblTaxScheme taxScheme = new UblTaxScheme();
        taxScheme.setID("1000");
        taxScheme.setName("IGV");
        taxScheme.setTaxTypeCode("VAT");
        taxCategory.setTaxScheme(taxScheme);

        taxSubtotal.setTaxCategory(taxCategory);

        taxTotal.setTaxSubtotal(taxSubtotal);

        creditNoteXML.setTaxTotal(taxTotal);

        //INFORMACIÓN GENERAL SOBRE EL LEGAL MONETARY TOTAL

        UblLegalMonetaryTotal legalMonetaryTotal = new UblLegalMonetaryTotal();
        UblPayableAmount payableAmount = new UblPayableAmount();
        payableAmount.setCurrencyID("PEN");
        payableAmount.setPayableAmount(total_importe_venta);
        legalMonetaryTotal.setPayableAmount(payableAmount);

        creditNoteXML.setLegalMonetaryTotal(legalMonetaryTotal);



        //V
        //INICIO DE LOS INVOICE LINE
        //cursor.moveToFirst();

        //if (cursor.getCount()>0){
            int i= 0;
            //for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                //OBTENEMOS LOS DATOS DEL ITEM ACTUAL
                /*String nombre_producto = cursor.getString(cursor.getColumnIndex(DBAdapter_Temp_Venta.temp_nom_producto));
                int cantidad = cursor.getInt(cursor.getColumnIndex(DBAdapter_Temp_Venta.temp_cantidad));
                Double precio_unitario = cursor.getDouble(cursor.getColumnIndex(DBAdapter_Temp_Venta.temp_precio_unit));
                String codigoProducto = cursor.getString(cursor.getColumnIndex(DBAdapter_Temp_Venta.temp_codigo_producto));
*/
                String nombre_producto = "Pan Integral mediano Unión";
                int cantidad = 100;
                Double precio_unitario = 6.66;
                String codigoProducto = "W_666";


                Double valor_unitario = precio_unitario/1.18;
                Double valor_venta_bruto = (cantidad*precio_unitario)/1.18;
                Double descuentos = 0.00;
                Double valor_venta_por_item = valor_venta_bruto - descuentos;
                Double igv = valor_venta_por_item * 0.18;
                Double total = cantidad*precio_unitario;



                UblCreditNoteLine ublCreditNoteLine = new UblCreditNoteLine();
                ublCreditNoteLine.setID(++i);

                UblCreditedQuantity ublCreditedQuantity = new UblCreditedQuantity();
        ublCreditedQuantity.setUnitCode("NIU");
        ublCreditedQuantity.setCreditedQuantity(cantidad);
                ublCreditNoteLine.setUblCreditedQuantity(ublCreditedQuantity);

                UblLineExtensionAmount lineExtensionAmount = new UblLineExtensionAmount();
                lineExtensionAmount.setCurrencyID("PEN");
                lineExtensionAmount.setLineExtensionAmount(df.format(valor_venta_por_item));
        ublCreditNoteLine.setLineExtensionAmount(lineExtensionAmount);

                UblPricingReference pricingReference = new UblPricingReference();
                UblAlternativeConditionPrice alternativeConditionPrice = new UblAlternativeConditionPrice();
                UblPriceAmount priceAmount = new UblPriceAmount();
                priceAmount.setCurrencyID("PEN");
                priceAmount.setPriceAmount(df.format(precio_unitario));
                alternativeConditionPrice.setPriceAmount(priceAmount);
                alternativeConditionPrice.setPriceTypeCode("01");
                pricingReference.setAlternativeConditionPrice(alternativeConditionPrice);
        ublCreditNoteLine.setPricingReference(pricingReference);

                UblTaxTotal taxTotal1 = new UblTaxTotal();
                UblTaxAmount amount1 = new UblTaxAmount();
                amount1.setCurrencyID("PEN");
                amount1.setTaxAmount(df.format(igv));
                Log.d("FORMAT IGV", df.format(igv));
                taxTotal1.setTaxAmount(amount1);

                UblTaxSubtotal taxSubtotal1 = new UblTaxSubtotal();
                taxSubtotal1.setTaxAmount(amount1);
                UblTaxCategory taxCategory1 = new UblTaxCategory();
                taxCategory1.setTaxExemptionReasonCode("10");
                taxSubtotal1.setTaxCategory(taxCategory1);
                UblTaxScheme scheme = new UblTaxScheme();
                scheme.setID("1000");
                scheme.setName("IGV");
                scheme.setTaxTypeCode("VAT");
                taxCategory1.setTaxScheme(scheme);
                taxTotal1.setTaxSubtotal(taxSubtotal1);


        ublCreditNoteLine.setTaxTotal(taxTotal1);

                UblItem item = new UblItem();
                item.setDescription(nombre_producto);
                UblSellersItemIdentification sellersItemIdentification = new UblSellersItemIdentification();
                sellersItemIdentification.setID(""+codigoProducto);
                item.setSellersItemIdentification(sellersItemIdentification);
        ublCreditNoteLine.setItem(item);

                UblPrice price = new UblPrice();
                UblPriceAmount priceAmount1 = new UblPriceAmount();
                priceAmount1.setCurrencyID("PEN");
                priceAmount1.setPriceAmount(df.format(valor_unitario));
                price.setPriceAmount(priceAmount1);

        ublCreditNoteLine.setPrice(price);

                creditNoteXML.listCreditNoteLine.add(ublCreditNoteLine);

            //}

        //}
        //File xmlFile = new File(PATH_FIRMADO);


        try
        {

            // Serialize the Person
            Serializer serializer = new Persister(new Format(1,"<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>"));
            //Serializer serializer = new Persister();
            serializer.write(creditNoteXML, xmlFile);
                    /*UblInvoiceXML invoiceRead = serializer.read(UblInvoiceXML.class, xmlFile);
                    System.out.println("INVOICE VALUE TOTAL: "+invoiceRead.getUblExtensions().getListUblExtension().get(0).getExtensionContent().getAdditionalInformation().getAdditionalProperty().getValue());
                    System.out.println("INVOICE READ: "+invoiceRead.getID());
                    System.out.println("INVOICE SIGNATURE : "+invoiceRead.getSignature().getSignatoryParty().getPartyIdentification().getID());
                    System.out.println("INVOICE UBICACION "+invoiceRead.getAccountingSupplierParty().getParty().getPostalAddress().getStreetName());
                    System.out.println("INVOICE CLIENTE: "+invoiceRead.getAccountingCustomerParty().getParty().getPartyLegalEntity().getRegistrationName());
                    System.out.println("INVOICE IMPUESTO: "+invoiceRead.getTaxTotal().getTaxSubtotal().getTaxCategory().getTaxScheme().getTaxTypeCode());
                    System.out.println("INVOICE TOTAL PAYABLE: "+invoiceRead.getLegalMonetaryTotal().getPayableAmount().getPayableAmount());

                    for(int i=0; i<invoiceRead.getListInvoiceLine().size(); i++){
                        int index = i+1;
                        System.out.println("INVOICE ITEM: "+ index);
                        System.out.println("INVOICE ITEM NAME: "+ invoiceRead.getListInvoiceLine().get(i).getItem().getDescription());

                    }
                    */

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally{
            //Signature.add(xmlFile);

        }

        //System.out.println("PATH: "+xmlFile.getAbsolutePath());
                 Log.d(TAG, " PATH ABSOLUTO GENERADO" + xmlFile.getAbsolutePath());
        return xmlFile;
    }
}
