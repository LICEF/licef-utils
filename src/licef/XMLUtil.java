package licef;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class XMLUtil {

    /** 
     * Returns the XML string representation of a Node.
     * @param node Node to convert into XML string. 
     * @return The XML string representation of the input node. 
     * @throws TransformerConfigurationException 
     * @throws TransformerException 
     */
    public static String getXMLString( Node node ) throws TransformerConfigurationException, TransformerException {
        StringWriter writer = new StringWriter();
        DOMSource domSource = new DOMSource( node );
        StreamResult result = new StreamResult( writer );
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty( OutputKeys.ENCODING, "ISO-8859-1" );
        serializer.setOutputProperty( OutputKeys.INDENT, "yes" );
        serializer.transform( domSource, result ); 
        return( writer.toString() );
    }

    /** 
     * Return a Node corresponding to the input XML string representation.
     * @param xmlString XML string representation.
     * @return An instance of Node corresponding to the input XML string representation.
     * @throws ParserConfigurationException 
     * @throws SAXException 
     * @throws IOException 
     */
    public static Node getXMLNode( String xmlString ) throws ParserConfigurationException, SAXException, IOException {
        InputSource inputSource = new InputSource( new StringReader( xmlString ) );
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse( inputSource ); 
        return( doc );
    }

}
