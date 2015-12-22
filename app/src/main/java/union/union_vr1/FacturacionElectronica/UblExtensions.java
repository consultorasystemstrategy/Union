/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package union.union_vr1.FacturacionElectronica;

import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;

/**
 *
 * @author Usuario
 */
class UblExtensions {
    
    @ElementList(name = "UBLExtension", inline=true, required = false, entry = "ext:UBLExtension")
    //PREFIJO:  ext
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2")
    public List<UblExtension> listUblExtension = new ArrayList<UblExtension>();

    public List<UblExtension> getListUblExtension() {
        return listUblExtension;
    }

    public void setListUblExtension(List<UblExtension> listUblExtension) {
        this.listUblExtension = listUblExtension;
    }
    
    
    
}
