package union.union_vr1.FacturacionElectronica;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Created by Usuario on 04/03/2016.
 */
public class UblCreditedQuantity {

    @Attribute(name = "unitCode")
    private String unitCode;

    @Text
    private int creditedQuantity;

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public int getCreditedQuantity() {
        return creditedQuantity;
    }

    public void setCreditedQuantity(int creditedQuantity) {
        this.creditedQuantity = creditedQuantity;
    }
}
