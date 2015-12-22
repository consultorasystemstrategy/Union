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
class UblSignatoryParty {
    
    
    @Element(name="PartyIdentification")
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblPartyIdentification partyIdentification;
    
    @Element(name="PartyName")
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblPartyName partyName;

    public UblPartyIdentification getPartyIdentification() {
        return partyIdentification;
    }

    public void setPartyIdentification(UblPartyIdentification partyIdentification) {
        this.partyIdentification = partyIdentification;
    }

    public UblPartyName getPartyName() {
        return partyName;
    }

    public void setPartyName(UblPartyName partyName) {
        this.partyName = partyName;
    }
    
            
    
}
