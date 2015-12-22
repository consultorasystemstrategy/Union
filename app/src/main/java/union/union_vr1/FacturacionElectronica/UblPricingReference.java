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
class UblPricingReference {

    @Element(name="AlternativeConditionPrice", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblAlternativeConditionPrice alternativeConditionPrice;

    public UblAlternativeConditionPrice getAlternativeConditionPrice() {
        return alternativeConditionPrice;
    }

    public void setAlternativeConditionPrice(UblAlternativeConditionPrice alternativeConditionPrice) {
        this.alternativeConditionPrice = alternativeConditionPrice;
    }
    
    
    
    
}
