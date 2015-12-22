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
class UblItem {
    
    
    @Element(name="Description")
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String description;
    
    
    @Element(name="SellersItemIdentification", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblSellersItemIdentification sellersItemIdentification;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UblSellersItemIdentification getSellersItemIdentification() {
        return sellersItemIdentification;
    }

    public void setSellersItemIdentification(UblSellersItemIdentification sellersItemIdentification) {
        this.sellersItemIdentification = sellersItemIdentification;
    }
    
    
    
    
    
}
