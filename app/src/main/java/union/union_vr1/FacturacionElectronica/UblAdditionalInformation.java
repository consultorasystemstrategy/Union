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
class UblAdditionalInformation {
    
    
    @Element(name="AdditionalMonetaryTotal", required = false)
    //PREFIJO:  sac
    @Namespace(reference="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1")
    private UblAdditionalMonetaryTotal additionalMonetaryTotal;
    
    
    @Element(name="AdditionalProperty", required = false)
    //PREFIJO:  sac
    @Namespace(reference="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1")
    private UblAdditionalProperty additionalProperty;

    public UblAdditionalMonetaryTotal getAdditionalMonetaryTotal() {
        return additionalMonetaryTotal;
    }

    public void setAdditionalMonetaryTotal(UblAdditionalMonetaryTotal additionalMonetaryTotal) {
        this.additionalMonetaryTotal = additionalMonetaryTotal;
    }

    public UblAdditionalProperty getAdditionalProperty() {
        return additionalProperty;
    }

    public void setAdditionalProperty(UblAdditionalProperty additionalProperty) {
        this.additionalProperty = additionalProperty;
    }
    
    
    
    
    
    
}
