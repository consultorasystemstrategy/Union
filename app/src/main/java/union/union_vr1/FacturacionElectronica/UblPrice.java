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
class UblPrice {
    
    @Element(name="PriceAmount")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private UblPriceAmount priceAmount;

    public UblPriceAmount getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(UblPriceAmount priceAmount) {
        this.priceAmount = priceAmount;
    }
    
    
    
    
}
