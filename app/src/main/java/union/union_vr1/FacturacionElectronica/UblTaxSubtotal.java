/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package union.union_vr1.FacturacionElectronica;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

/**
 *
 * @author Usuario
 */
class UblTaxSubtotal {
    
    @Element(name="TaxAmount", required = false)
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private UblTaxAmount taxAmount;
    
    @Element(name="TaxCategory", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblTaxCategory taxCategory;

    public UblTaxAmount getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(UblTaxAmount taxAmount) {
        this.taxAmount = taxAmount;
    }

    public UblTaxCategory getTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(UblTaxCategory taxCategory) {
        this.taxCategory = taxCategory;
    }
    
    
    
    
    
}
