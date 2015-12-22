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
@Order(elements={"ID","SignatoryParty","DigitalSignatureAttachment"})
class UblSignature {
    
    @Element(name="ID")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String ID;
    
    @Element(name="SignatoryParty")
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblSignatoryParty signatoryParty;
    
    
    @Element(name="DigitalSignatureAttachment")
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblDigitalSignatureAttachment digitalSignatureAttachment;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public UblSignatoryParty getSignatoryParty() {
        return signatoryParty;
    }

    public void setSignatoryParty(UblSignatoryParty signatoryParty) {
        this.signatoryParty = signatoryParty;
    }

    public UblDigitalSignatureAttachment getDigitalSignatureAttachment() {
        return digitalSignatureAttachment;
    }

    public void setDigitalSignatureAttachment(UblDigitalSignatureAttachment digitalSignatureAttachment) {
        this.digitalSignatureAttachment = digitalSignatureAttachment;
    }
    
    
    
    
    
}
