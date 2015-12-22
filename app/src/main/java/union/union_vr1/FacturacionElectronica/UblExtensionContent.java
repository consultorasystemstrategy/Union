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
class UblExtensionContent {
    
    @Element(name="AdditionalInformation", required = false)
    //PREFIJO:  sac
    @Namespace(reference="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1")
    private UblAdditionalInformation additionalInformation;

    public UblAdditionalInformation getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(UblAdditionalInformation additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
    
    
    
}
