package union.union_vr1.FacturacionElectronica;

/**
 * Created by Usuario on 17/08/2015.
 */
public class UBLElements {


    //Constants PU
    public static String ID_SIGNATURE_PU = "SignatureUNION";

    //ENCONDING
    public static String ISO88591 = "ISO-8859-1";

    //NAME SPACES
    public static String NAMESPACE_URN = "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2";
    public static String NAMESPACE_CAC = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2";
    public static String NAMESPACE_CBC = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";
    public static String NAMESPACE_CCTS = "urn:un:unece:uncefact:documentation:2";
    public static String NAMESPACE_DS = "http://www.w3.org/2000/09/xmldsig#";
    public static String NAMESPACE_EXT = "urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2";
    public static String NAMESPACE_QDT = "urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2";
    public static String NAMESPACE_SAC = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1";
    public static String NAMESPACE_UDT = "urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2";
    public static String NAMESPACE_XSI = "http://www.w3.org/2001/XMLSchema-instance";

    //NAME PREFIX
    public static String URN = "urn";
    public static String CAC = "cac";
    public static String CBC = "cbc";
    public static String CCTS = "ccts";
    public static String DS = "ds";
    public static String EXT = "ext";
    public static String QDT = "qdt";
    public static String SAC = "sac";
    public static String UDT = "udt";
    public static String XSI = "xsi";





    //ELEMENTOS
    public static String INVOICE = "Invoice";
    public static String UBLEXTENSIONS = "UBLExtensions";
    public static String UBLEXTENSION = "UBLExtension";
    public static String EXTENSIONCONTENT = "ExtensionContent";
    public static String ADDITIONALINFORMATION = "AdditionalInformation";
    public static String ADDITIONALMONETARYTOTAL = "AdditionalMonetaryTotal";
    public static String ADDITIONALPROPERTY = "AdditionalProperty";
    public static String ID = "ID";
    public static String PAYABLEAMOUNT = "PayableAmount";
    public static String VALUE = "Value";
    public static String SIGNATURE = "Signature";
    public static String SIGNEDINFO = "SignedInfo";
    public static String CANNONICALIZATIONMETHOD = "CanonicalizationMethod";
    public static String SIGNATUREMETHOD = "SignatureMethod";
    public static String REFERENCE = "Reference";
    public static String TRANSFORMS = "Transforms";
    public static String DIGESTMETHOD = "DigestMethod";
    public static String SIGNATUREVALUE  = "SignatureValue";
    public static String DIGESTVALUE = "DigestValue";
    public static String KEYINFO = "KeyInfo";
    public static String X509DATA = "X509Data";
    public static String X509SUBJECTNAME = "X509SubjectName";
    public static String X509CERTIFICATE = "X509Certificate";

    /**/
    public static String  UBLVERSIONID = "UBLVersionID";
    public static String  CUSTOMIZATIONID = "CustomizationID";
    public static String  ISSUEDATE = "IssueDate";
    public static String  INVOYCETYPECODE = "InvoiceTypeCode";
    public static String  DOCUMENTCURRENCYCODE = "DocumentCurrencyCode";
    public static String  SIGNATORYPARTY = "SignatoryParty";
    public static String  PARTYIDENTIFIACTION = "PartyIdentification";
    public static String  PARTYNAME = "PartyName";
    public static String  NAME = "Name";
    public static String  DIGITALSIGNATUREATTACHMENT = "DigitalSignatureAttachment";
    public static String  EXTERNALREFERENCE= "ExternalReference";
    public static String  URI = "URI";

    public static String ACCOUNTINGSUPPLIERPARTY = "AccountingSupplierParty";
    public static String CUSTOMERASSIGNEDACCOUNTID = "CustomerAssignedAccountID";
    public static String ADDITIONALACCOUNTID = "AdditionalAccountID";
    public static String PARTY = "Party";
    public static String POSTALADDRESS = "PostalAddress";
    public static String STREETNAME = "StreetName";
    public static String CITYSUBDIVISIONNAME = "CitySubdivisionName";
    public static String CITYNAME = "CityName";
    public static String COUNTRYSUBENTITY = "CountrySubentity";
    public static String DISTRICT = "District";

    public static String COUNTRY = "Country";
    public static String IDENTIFICATIONCODE = "IdentificationCode";
    public static String PARTYLEGALENTITY = "PartyLegalEntity";
    public static String REGISTRATIONNAME= "RegistrationName";

    public static String ACCOUNTINGCUSTOMERPARTY = "AccountingCustomerParty";
    public static String LEGALMONETARYTOTAL = "LegalMonetaryTotal";
    public static String INVOICELINE = "InvoiceLine";
    public static String INVOICEDQUANTITY = "InvoicedQuantity";
    public static String LINEEXTENSIONAMOUNT = "LineExtensionAmount";
    public static String PRICINGREFERENCE = "PricingReference";
    public static String ALTERNATIVECONDITIONPRICE = "AlternativeConditionPrice";
    public static String PRICEAMOUNT = "PriceAmount";
    public static String PRICETYPECODE = "PriceTypeCode";
    public static String TAXTOTAL = "TaxTotal";
    public static String TAXAMOUNT = "TaxAmount";
    public static String TAXSUBTOTAL = "TaxSubtotal";
    public static String TAXCATEGORY = "TaxCategory";
    public static String TAXEXEMPTIONREASONCODE = "TaxExemptionReasonCode";
    public static String TAXSCHEME = "TaxScheme";
    public static String TAXTYPECODE = "TaxTypeCode";
    public static String ITEM = "Item";
    public static String DESCRIPTION = "Description";
    public static String SELLERSITEMIDENTIFICATION = "SellersItemIdentification";
    public static String PRICE = "Price";

    //ATRIBUTOS
    public static String CURRENCYID = "currencyID";
    public static String Id = "Id";


    //VALOR DE LOS ATRIBUTOS
    public static String PEN = "PEN";
}
