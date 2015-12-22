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
class UblDigitalSignatureAttachment {
    
    
    @Element(name="ExternalReference")
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblExternalReference externalReference;

    public UblDigitalSignatureAttachment(UblExternalReference externalReference) {
        this.externalReference = externalReference;
    }

    public UblDigitalSignatureAttachment() {
    }
    
    

    public UblExternalReference getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(UblExternalReference externalReference) {
        this.externalReference = externalReference;
    }
    
    
    
    
    
}
