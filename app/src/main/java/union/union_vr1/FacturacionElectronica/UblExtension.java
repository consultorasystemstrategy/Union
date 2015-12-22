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
class UblExtension {
    
    @Element(name="ExtensionContent", required = false)
    //PREFIJO:  ext
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2")
    private UblExtensionContent extensionContent;

    public UblExtensionContent getExtensionContent() {
        return extensionContent;
    }

    public void setExtensionContent(UblExtensionContent extensionContent) {
        this.extensionContent = extensionContent;
    }
    
    
    
}
