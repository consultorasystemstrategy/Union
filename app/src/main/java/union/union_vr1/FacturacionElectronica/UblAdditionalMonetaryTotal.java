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
@Order(elements={"ID","PayableAmount"})
class UblAdditionalMonetaryTotal {
    
    @Element(name="ID")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private int ID;
    
    
    @Element(name="PayableAmount")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private UblPayableAmount payableAmount;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public UblPayableAmount getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(UblPayableAmount payableAmount) {
        this.payableAmount = payableAmount;
    }
    
    
    
    
    
    
}
