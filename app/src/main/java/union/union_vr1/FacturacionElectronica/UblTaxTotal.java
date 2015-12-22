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
class UblTaxTotal {
    
    @Element(name="TaxAmount", required = false)
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private UblTaxAmount taxAmount;
    
    @Element(name="TaxSubtotal", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblTaxSubtotal taxSubtotal;

    public UblTaxAmount getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(UblTaxAmount taxAmount) {
        this.taxAmount = taxAmount;
    }

    public UblTaxSubtotal getTaxSubtotal() {
        return taxSubtotal;
    }

    public void setTaxSubtotal(UblTaxSubtotal taxSubtotal) {
        this.taxSubtotal = taxSubtotal;
    }
    
    
    
}
