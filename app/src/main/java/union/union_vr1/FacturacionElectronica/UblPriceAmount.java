/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package union.union_vr1.FacturacionElectronica;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 *
 * @author Usuario
 */
class UblPriceAmount {
    
    @Attribute(name = "currencyID")
    private String currencyID;
    
    @Text
    private String priceAmount;

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public String getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(String priceAmount) {
        this.priceAmount = priceAmount;
    }
    
    
}
