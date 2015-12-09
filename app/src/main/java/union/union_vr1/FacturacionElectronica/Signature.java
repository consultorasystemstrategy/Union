package union.union_vr1.FacturacionElectronica;

import android.util.Log;

/*import com.phloc.commons.error.IResourceErrorGroup;
import com.phloc.ubl.EUBL20DocumentType;
import com.phloc.ubl.UBL20DocumentTypes;
import com.phloc.ubl.UBL20Reader;
import com.phloc.ubl.UBL20Validator;
import com.phloc.ubl.UBL20Writer;*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.crypto.MarshalException;

import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/*import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.InvoicedQuantityType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;
import un.unece.uncefact.codelist.specification._66411._2001.UnitCodeContentType;*/

/**
 * Created by Steve on 12/10/2015.
 */
public class Signature {

    //LE TENGO QUE PASAR PARÁMETROS
    /*
    * FILEINPUTSTREAM JKS
    * FILEINPUTSTREAM SIN FIRMAR
    * PATH DE LA SALIDA
    * */
    public static String add(InputStream BKS, InputStream DocumentoSinFirmar, File fileDocumentoFirmado) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException, CertificateException, FileNotFoundException, IOException, UnrecoverableEntryException, ParserConfigurationException, SAXException, MarshalException, XMLSignatureException, TransformerConfigurationException, TransformerException  {

        Security.addProvider(new org.jcp.xml.dsig.internal.dom.XMLDSigRI());
        Provider providerCrypto = null;
        for (Provider p : Security.getProviders()) {
            Log.d("PROVIDERS",p.getName());
            Log.d("INFO", p.getInfo());
            //Log.d("SERVICES", ""+p.getServices());

        }

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

        Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA1, null),
                Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
                null, null);

        SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE_WITH_COMMENTS,
                        (C14NMethodParameterSpec)null),
                fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                Collections.singletonList(ref));

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(BKS,"upeukeystore".toCharArray());
        KeyStore.PrivateKeyEntry keyEntry
                = (KeyStore.PrivateKeyEntry) keyStore.getEntry("le-fb1066b1-b857-499b-a744-f7b90be3437a", new KeyStore.PasswordProtection("upeu".toCharArray()));

        X509Certificate cert = (X509Certificate) keyEntry.getCertificate();

        KeyInfoFactory kif = fac.getKeyInfoFactory();
        List x509Content = new ArrayList();
        x509Content.add(cert.getSubjectX500Principal().getName());
        x509Content.add(cert);
        X509Data xd = kif.newX509Data(x509Content);
        KeyInfo ki= kif.newKeyInfo(Collections.singletonList(xd));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = (Document) dbf.newDocumentBuilder().parse(DocumentoSinFirmar);

        DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), doc.getElementsByTagNameNS("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2","ExtensionContent").item(1));
        dsc.setDefaultNamespacePrefix("ds");
        XMLSignature signature = fac.newXMLSignature(si, ki, null,"#IDSignUNION", null);

        signature.sign(dsc);

        OutputStream os = new FileOutputStream(fileDocumentoFirmado);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        trans.setOutputProperty(OutputKeys.STANDALONE, "no");
        trans.transform(new DOMSource(doc), new StreamResult(os));

        Node node = doc.getElementsByTagName("ds:DigestValue").item(0);

        Log.d("PATH FIRMA",fileDocumentoFirmado.getAbsolutePath());
        //Log.d("DOC",dsc.getParent().getTextContent());
        return node.getTextContent();
    }
/*
    public static String writeDocument(Document document, File fileOutput) throws FileNotFoundException, TransformerException {
*//*


        // Read
        final Document aDoc = DOMReader.readXMLDOM (new ClassPathResource (sFilename),
                new DOMReaderSettings ().setSchema (EUBL20DocumentType.INVOICE.getSchema ()));*//*

        // Read
        final InvoiceType aUBLObject = UBL20Reader.readInvoice(document);




        // Validate
        IResourceErrorGroup aErrors = UBL20Validator.validateInvoice(aUBLObject);
        
      *//*  InvoiceType invoiceType = new InvoiceType();
        InvoiceLineType invoiceLineType = new InvoiceLineType();

        IDType idType = new IDType("1");
        InvoicedQuantityType invoicedQuantityType = new InvoicedQuantityType();

        BigDecimal bigDecimal = new BigDecimal(150);
        UnitCodeContentType unitCodeContentType = UnitCodeContentType.EACH;

        invoicedQuantityType.setValue(bigDecimal);
        invoicedQuantityType.setUnitCode(unitCodeContentType);


        invoiceType.setInvoiceLine(Collections.singletonList(invoiceLineType));
        invoiceLineType.setID(idType);
        invoiceLineType.setInvoicedQuantity(invoicedQuantityType);*//*

        // write again
        //EUBL20DocumentType ubl20DocumentTypes = UBL20DocumentTypes.getDocumentTypeOfNamespace(UBLElements.NAMESPACE_CAC);




        final Document documentwrite = UBL20Writer.writeInvoice(aUBLObject);


        OutputStream os = new FileOutputStream(fileOutput);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        trans.setOutputProperty(OutputKeys.STANDALONE, "no");
        trans.transform(new DOMSource(documentwrite), new StreamResult(os));

        Log.d("PATH FIRMA", fileOutput.getAbsolutePath());
        return fileOutput.getAbsolutePath().toString();
    }*/
}
