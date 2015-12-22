/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package union.union_vr1.FacturacionElectronica;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;

/**
 *
 * @author Usuario
 */

@Order(elements={"ID","InvoicedQuantity","LineExtensionAmount","PricingReference","TaxTotal","Item","Price"})
class UblInvoiceLine {
    
    @Element(name="ID")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private int ID;
    
    @Element(name="InvoicedQuantity")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private UblInvoicedQuantity invoicedQuantity;
            
    
    @Element(name="LineExtensionAmount")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private UblLineExtensionAmount lineExtensionAmount;
    
    
    @Element(name="PricingReference", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblPricingReference pricingReference;
    
    
    @Element(name="TaxTotal", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblTaxTotal taxTotal;
    
    
    @Element(name="Item", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblItem item;
    
    
    @Element(name="Price", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblPrice price;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public UblInvoicedQuantity getInvoicedQuantity() {
        return invoicedQuantity;
    }

    public void setInvoicedQuantity(UblInvoicedQuantity invoicedQuantity) {
        this.invoicedQuantity = invoicedQuantity;
    }

    public UblLineExtensionAmount getLineExtensionAmount() {
        return lineExtensionAmount;
    }

    public void setLineExtensionAmount(UblLineExtensionAmount lineExtensionAmount) {
        this.lineExtensionAmount = lineExtensionAmount;
    }

    public UblPricingReference getPricingReference() {
        return pricingReference;
    }

    public void setPricingReference(UblPricingReference pricingReference) {
        this.pricingReference = pricingReference;
    }

    public UblTaxTotal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(UblTaxTotal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public UblItem getItem() {
        return item;
    }

    public void setItem(UblItem item) {
        this.item = item;
    }

    public UblPrice getPrice() {
        return price;
    }

    public void setPrice(UblPrice price) {
        this.price = price;
    }
    
    
    
    
}
