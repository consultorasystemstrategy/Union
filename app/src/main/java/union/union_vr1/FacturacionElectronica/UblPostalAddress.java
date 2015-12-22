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
@Order(elements={"ID","StreetName","CitySubdivisionName","CityName","CountrySubentity","District","Country"})
class UblPostalAddress {
    
    @Element(name="ID", required = false)
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String ID;
    
    @Element(name="StreetName", required = false)
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String streetName;
    
    @Element(name="CitySubdivisionName", required = false)
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String citySubdivisionName;
    
    @Element(name="CityName", required = false)
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String cityName;
    
    @Element(name="CountrySubentity", required = false)
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String countrySubentity;
    
    @Element(name="District", required = false)
    //PREFIJO:  cbc
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    private String district;
    
    @Element(name="Country", required = false)
    //PREFIJO:  cac
    @Namespace(reference="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    private UblCountry country;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCitySubdivisionName() {
        return citySubdivisionName;
    }

    public void setCitySubdivisionName(String citySubdivisionName) {
        this.citySubdivisionName = citySubdivisionName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountrySubentity() {
        return countrySubentity;
    }

    public void setCountrySubentity(String countrySubentity) {
        this.countrySubentity = countrySubentity;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public UblCountry getCountry() {
        return country;
    }

    public void setCountry(UblCountry country) {
        this.country = country;
    }
    
    
    
    
}
