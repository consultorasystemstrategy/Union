/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package union.union_vr1.FacturacionElectronica;

import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 *
 * @author Usuario
 */

@Root(name = "Invoice")
@NamespaceList({
        @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:Invoice-2"),
        @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", prefix="cac"),
        @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", prefix="cbc"),
        @Namespace(reference="urn:un:unece:uncefact:documentation:2", prefix="ccts"),
        @Namespace(reference="http://www.w3.org/2000/09/xmldsig#", prefix="ds"),
        @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2", prefix="ext"),
        @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2", prefix="qdt"),
        @Namespace(reference="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", prefix="sac"),
        @Namespace(reference="urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2", prefix="udt"),
        @Namespace(reference="http://www.w3.org/2001/XMLSchema-instance", prefix="xsi")
        
}
        )
@Order(elements={"UBLExtensions","UBLVersionID","CustomizationID","ID","IssueDate","InvoiceTypeCode","DocumentCurrencyCode","Signature","AccountingSupplierParty","AccountingCustomerParty","TaxTotal","LegalMonetaryTotal"})
class UblInvoiceXML {

    @Element(name="UBLExtensions", required = false)
    //PREFIJO:  ext
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2")
    private UblExtensions ublExtensions;
    
    @Element(name="UBLVersionID")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String ublVersionID;
    
    @Element(name="CustomizationID")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String customizationID;
    
    
    @Element(name="ID")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String ID;
    
    @Element(name="IssueDate")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String issueDate;
    
    
    @Element(name="InvoiceTypeCode")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String invoiceTypeCode;
    
    
    @Element(name="DocumentCurrencyCode")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String documentCurrencyCode;
    
    @Element(name="Signature")
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblSignature signature;
    
    @Element(name="AccountingSupplierParty")
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblAccountingSupplierParty accountingSupplierParty;
    
    @Element(name="AccountingCustomerParty", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblAccountingCustomerParty accountingCustomerParty;
    
    @Element(name="TaxTotal", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblTaxTotal taxTotal;
    
    @Element(name="LegalMonetaryTotal", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblLegalMonetaryTotal legalMonetaryTotal;
    
    
    @ElementList(name="InvoiceLine", inline=true, required = false, entry = "cac:InvoiceLine")
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    public List<UblInvoiceLine> listInvoiceLine = new ArrayList<>();

    public UblExtensions getUblExtensions() {
        return ublExtensions;
    }

    public void setUblExtensions(UblExtensions ublExtensions) {
        this.ublExtensions = ublExtensions;
    }

    public String getUblVersionID() {
        return ublVersionID;
    }

    public void setUblVersionID(String ublVersionID) {
        this.ublVersionID = ublVersionID;
    }

    public String getCustomizationID() {
        return customizationID;
    }

    public void setCustomizationID(String customizationID) {
        this.customizationID = customizationID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getInvoiceTypeCode() {
        return invoiceTypeCode;
    }

    public void setInvoiceTypeCode(String invoiceTypeCode) {
        this.invoiceTypeCode = invoiceTypeCode;
    }

    public String getDocumentCurrencyCode() {
        return documentCurrencyCode;
    }

    public void setDocumentCurrencyCode(String documentCurrencyCode) {
        this.documentCurrencyCode = documentCurrencyCode;
    }

    public UblSignature getSignature() {
        return signature;
    }

    public void setSignature(UblSignature signature) {
        this.signature = signature;
    }

    public UblAccountingSupplierParty getAccountingSupplierParty() {
        return accountingSupplierParty;
    }

    public void setAccountingSupplierParty(UblAccountingSupplierParty accountingSupplierParty) {
        this.accountingSupplierParty = accountingSupplierParty;
    }

    public UblAccountingCustomerParty getAccountingCustomerParty() {
        return accountingCustomerParty;
    }

    public void setAccountingCustomerParty(UblAccountingCustomerParty accountingCustomerParty) {
        this.accountingCustomerParty = accountingCustomerParty;
    }

    public UblTaxTotal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(UblTaxTotal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public UblLegalMonetaryTotal getLegalMonetaryTotal() {
        return legalMonetaryTotal;
    }

    public void setLegalMonetaryTotal(UblLegalMonetaryTotal legalMonetaryTotal) {
        this.legalMonetaryTotal = legalMonetaryTotal;
    }

    public List<UblInvoiceLine> getListInvoiceLine() {
        return listInvoiceLine;
    }

    public void setListInvoiceLine(List<UblInvoiceLine> listInvoiceLine) {
        this.listInvoiceLine = listInvoiceLine;
    }

    
            
}
