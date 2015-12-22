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
//@Order(elements={"TaxScheme","TaxExemptionReasonCode"})
@Order(elements={"TaxExemptionReasonCode","TaxScheme"})
class UblTaxCategory {
    
    
    @Element(name="TaxExemptionReasonCode", required = false)
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String taxExemptionReasonCode;
    
    
    @Element(name="TaxScheme", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblTaxScheme taxScheme;

    public String getTaxExemptionReasonCode() {
        return taxExemptionReasonCode;
    }

    public void setTaxExemptionReasonCode(String taxExemptionReasonCode) {
        this.taxExemptionReasonCode = taxExemptionReasonCode;
    }

    public UblTaxScheme getTaxScheme() {
        return taxScheme;
    }

    public void setTaxScheme(UblTaxScheme taxScheme) {
        this.taxScheme = taxScheme;
    }
    
    
}
