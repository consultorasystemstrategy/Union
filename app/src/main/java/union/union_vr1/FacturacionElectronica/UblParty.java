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

@Order(elements={"PartyName","PostalAddress","PartyLegalEntity"})
class UblParty {
    
    @Element(name="PartyName", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblPartyName partyName;
    
    @Element(name="PostalAddress", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblPostalAddress postalAddress;
    
    
    @Element(name="PartyLegalEntity")
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblPartyLegalEntity partyLegalEntity;

    public UblPartyName getPartyName() {
        return partyName;
    }

    public void setPartyName(UblPartyName partyName) {
        this.partyName = partyName;
    }

    
    
    public UblPostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(UblPostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    public UblPartyLegalEntity getPartyLegalEntity() {
        return partyLegalEntity;
    }

    public void setPartyLegalEntity(UblPartyLegalEntity partyLegalEntity) {
        this.partyLegalEntity = partyLegalEntity;
    }
    
    
    
    
    
}
