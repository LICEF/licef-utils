package licef;

import java.util.HashMap;
import java.util.Iterator;
import javax.xml.*;
import javax.xml.namespace.NamespaceContext;

/**
 * Singleton containing the commonly used namespaces for XML handling.
 */
public class CommonNamespaceContext implements NamespaceContext {

    public static final String lomNSURI = "http://ltsc.ieee.org/xsd/LOM";
    public static final String dcNSURI = "http://purl.org/dc/elements/1.1/";
    public static final String dctNSURI = "http://purl.org/dc/terms/";
    public static final String oaidcNSURI = "http://www.openarchives.org/OAI/2.0/oai_dc/";
    public static final String oaiNSURI = "http://www.openarchives.org/OAI/2.0/";
    public static final String xsdNSURI = "http://www.w3.org/2001/XMLSchema";
    public static final String xsiNSURI = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String rdfNSURI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String rdfsNSURI = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String vcardNSURI = "http://www.w3.org/2006/vcard/ns#";
    public static final String skosNSURI = "http://www.w3.org/2004/02/skos/core#";
    public static final String foafNSURI = "http://xmlns.com/foaf/0.1/";
    public static final String orgNSURI = "http://www.w3.org/ns/org#";
    public static final String vdexNSURI = "http://www.imsglobal.org/xsd/imsvdex_v1p0";
    public static final String fedora_managementNSURI = "http://www.fedora.info/definitions/1/0/management/";
    public static final String cometeNSURI = "http://comete.licef.ca/reference#";
    public static final String comete_InternalFormatNSURI = "http://comete.licef.ca/internal-format";

    public static CommonNamespaceContext getInstance() {
        if( instance == null )
            instance = new CommonNamespaceContext();
        return( instance );
    }

    public String getNamespaceURI( String prefix ) {
        if( namespaces.containsKey( prefix ) )
            return( (String)namespaces.get( prefix ) );
        else
            return( XMLConstants.NULL_NS_URI );
    }

    public String getPrefix(String uri) {
        return( (String)prefixes.get( uri ) );
    }

    // Not used for now.
    public Iterator getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }

    public Iterator getAllPrefixes() {
        return( prefixes.values().iterator() );
    }

    public void setPrefixNamespace(String prefix, String namespace) {
        namespaces.put( prefix, namespace );
        prefixes.put( namespace, prefix );
    }

    private CommonNamespaceContext() {
        setPrefixNamespace( "lom", lomNSURI );
        setPrefixNamespace( "dc", dcNSURI );
        setPrefixNamespace( "dct", dctNSURI );
        setPrefixNamespace( "oaidc", oaidcNSURI );
        setPrefixNamespace( "oai", oaiNSURI );
        setPrefixNamespace( "xsd", xsdNSURI );
        setPrefixNamespace( "xsi", xsiNSURI );
        setPrefixNamespace( "rdf", rdfNSURI );
        setPrefixNamespace( "rdfs", rdfsNSURI );
        setPrefixNamespace( "vcard", vcardNSURI );
        setPrefixNamespace( "skos", skosNSURI );
        setPrefixNamespace( "foaf", foafNSURI );
        setPrefixNamespace( "vdex", vdexNSURI );
        setPrefixNamespace( "fedora_management", fedora_managementNSURI );
        setPrefixNamespace( "comete", cometeNSURI );
        setPrefixNamespace( "comete_if", comete_InternalFormatNSURI );
    }

    private static CommonNamespaceContext instance;

    private HashMap<String,String> namespaces = new HashMap<String,String>();
    private HashMap<String,String> prefixes = new HashMap<String,String>();

}
