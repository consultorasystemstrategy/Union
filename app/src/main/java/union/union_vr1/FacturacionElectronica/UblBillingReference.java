package union.union_vr1.FacturacionElectronica;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

/**
 * Created by Usuario on 03/03/2016.
 */
public class UblBillingReference {

    @Element(name="InvoiceDocumentReference")
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblInvoiceDocumentReference invoiceDocumentReference;

    public UblInvoiceDocumentReference getInvoiceDocumentReference() {
        return invoiceDocumentReference;
    }

    public void setInvoiceDocumentReference(UblInvoiceDocumentReference invoiceDocumentReference) {
        this.invoiceDocumentReference = invoiceDocumentReference;
    }
}
