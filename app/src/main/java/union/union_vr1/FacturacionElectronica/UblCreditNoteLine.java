package union.union_vr1.FacturacionElectronica;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;

/**
 * Created by Usuario on 04/03/2016.
 */
@Order(elements={"ID","CreditedQuantity","LineExtensionAmount","PricingReference","TaxTotal","Item","Price"})
public class UblCreditNoteLine {


    @Element(name="ID")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private int ID;

    @Element(name="CreditedQuantity")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private UblCreditedQuantity ublCreditedQuantity;


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

    public UblCreditedQuantity getUblCreditedQuantity() {
        return ublCreditedQuantity;
    }

    public void setUblCreditedQuantity(UblCreditedQuantity ublCreditedQuantity) {
        this.ublCreditedQuantity = ublCreditedQuantity;
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
