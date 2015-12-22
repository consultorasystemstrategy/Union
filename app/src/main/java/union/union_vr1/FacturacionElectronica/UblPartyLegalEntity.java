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
class UblPartyLegalEntity {
    
    @Element(name="RegistrationName")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String registrationName;

    public String getRegistrationName() {
        return registrationName;
    }

    public void setRegistrationName(String registrationName) {
        this.registrationName = registrationName;
    }

    
    
    
    
}
