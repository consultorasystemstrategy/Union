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
class UblInvoicedQuantity {
    
    @Attribute(name = "unitCode")
    private String unitCode;
    
    @Text
    private int invoicedQuantity;

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public int getInvoicedQuantity() {
        return invoicedQuantity;
    }

    public void setInvoicedQuantity(int invoicedQuantity) {
        this.invoicedQuantity = invoicedQuantity;
    }

    

 
    
    
    
}
