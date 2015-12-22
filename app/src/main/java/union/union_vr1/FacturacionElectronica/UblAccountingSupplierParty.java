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
@Order(elements={"CustomerAssignedAccountID","AdditionalAccountID","Party"})
class UblAccountingSupplierParty {
    
    
    @Element(name="CustomerAssignedAccountID")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String customerAssignedAccountID;
    
    @Element(name="AdditionalAccountID")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String additionalAccountID;
    
    @Element(name="Party")
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblParty party;

    public String getCustomerAssignedAccountID() {
        return customerAssignedAccountID;
    }

    public void setCustomerAssignedAccountID(String customerAssignedAccountID) {
        this.customerAssignedAccountID = customerAssignedAccountID;
    }

    public String getAdditionalAccountID() {
        return additionalAccountID;
    }

    public void setAdditionalAccountID(String additionalAccountID) {
        this.additionalAccountID = additionalAccountID;
    }

    public UblParty getParty() {
        return party;
    }

    public void setParty(UblParty party) {
        this.party = party;
    }
    
    
    
    
    
    
    
}
