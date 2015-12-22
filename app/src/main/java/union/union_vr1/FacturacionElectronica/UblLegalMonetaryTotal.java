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
class UblLegalMonetaryTotal {
    
    @Element(name="PayableAmount")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private UblPayableAmount payableAmount;

    public UblPayableAmount getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(UblPayableAmount payableAmount) {
        this.payableAmount = payableAmount;
    }
    
    
    
}
