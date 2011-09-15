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
    public static final String oaidcNSURI = "http://www.openarchives.org/OAI/2.0/oai_dc/";
    public static final String xsdNSURI = "http://www.w3.org/2001/XMLSchema";
    public static final String xsiNSURI = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String fedora_managementNSURI = "http://www.fedora.info/definitions/1/0/management/";

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

    private CommonNamespaceContext() {
        namespaces.put( "lom", lomNSURI );
        namespaces.put( "dc", dcNSURI );
        namespaces.put( "oaidc", oaidcNSURI );
        namespaces.put( "xsd", xsdNSURI );
        namespaces.put( "xsi", xsiNSURI );
        namespaces.put( "fedora_management", fedora_managementNSURI );

        prefixes.put( lomNSURI, "lom" );
        prefixes.put( dcNSURI, "dc" );
        prefixes.put( oaidcNSURI, "oaidc" );
        prefixes.put( xsdNSURI, "xsd" );
        prefixes.put( xsiNSURI, "xsi" );
        prefixes.put( fedora_managementNSURI, "fedora_management" );
    }

    private static CommonNamespaceContext instance;

    private HashMap<String,String> namespaces = new HashMap<String,String>();
    private HashMap<String,String> prefixes = new HashMap<String,String>();

}
