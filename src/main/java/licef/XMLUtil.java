package licef;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
        return( getXMLNode( inputSource ) );
    }

    /** 
     * Return a Node corresponding to the input XML string representation.
     * @param xmlFile XML file.
     * @return An instance of Node corresponding to the input XML string representation.
     * @throws ParserConfigurationException 
     * @throws SAXException 
     * @throws IOException 
     */
    public static Node getXMLNode( File xmlFile ) throws ParserConfigurationException, SAXException, IOException {
        InputSource inputSource = new InputSource( new BufferedInputStream( new FileInputStream( xmlFile ) ) );
        return( getXMLNode( inputSource ) );
    }

    /** 
     * Return a Node corresponding to the input XML string representation.
     * @param inputSource source.
     * @return An instance of Node corresponding to the input XML string representation.
     * @throws ParserConfigurationException 
     * @throws SAXException 
     * @throws IOException 
     */
    public static Node getXMLNode( InputSource inputSource ) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware( true );
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse( inputSource ); 
        return( doc );
    }

    /*
     * The following methods come from a library written by Tom Fennelly.
     * Here was the header of the licence.
     */ 

    /*
     Milyn - Copyright (C) 2006

     This library is free software; you can redistribute it and/or
     modify it under the terms of the GNU Lesser General Public
     License (version 2.1) as published by the Free Software 
     Foundation.

     This library is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
     
     See the GNU Lesser General Public License for more details:    
     http://www.gnu.org/licenses/lgpl.txt
     */

    /**
     * Document validation types.
     */
    public static enum VALIDATION_TYPE {
        /**
         * No validation.
         */
        NONE,
        /**
         * DTD based validation.
         */
        DTD,
        /**
         * XSD based validation.
         */
        XSD,
    }
    private static String ELEMENT_NAME_FUNC = "/name()";

    private static XPathFactory xPathFactory = XPathFactory.newInstance();
    /**
     * Get the String data associated with the XPath selection supplied.
     *
     * @param node  The node to be searched.
     * @param xpath The XPath String to be used in the selection.
     * @return The string data located at the specified location in the
     *         document, or an empty string for an empty resultset query.
     */
    public static String getString(Node node, String xpath) {
        NodeList nodeList = getNodeList(node, xpath);

        if (nodeList == null || nodeList.getLength() == 0) {
            return "";
        }

        if (xpath.endsWith(ELEMENT_NAME_FUNC)) {
            if (nodeList.getLength() > 0) {
                return nodeList.item(0).getNodeName();
            } else {
                return "";
            }
        } else {
            return serialize(nodeList);
        }
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     * <p/>
     * The output is unformatted.
     *
     * @param nodeList The DOM subtree as a NodeList.
     * @return The subtree in serailised form.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static String serialize(NodeList nodeList) throws DOMException {
        return serialize(nodeList, false);
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param node The DOM node to be serialized.
     * @param format Format the output.
     * @return The subtree in serailised form.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static String serialize(final Node node, boolean format) throws DOMException {
        StringWriter writer = new StringWriter();
        serialize(node, format, writer);
        return writer.toString();
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param node The DOM node to be serialized.
     * @param format Format the output.
     * @param writer The target writer for serialization.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static void serialize(final Node node, boolean format, Writer writer) throws DOMException {
        if(node.getNodeType() == Node.DOCUMENT_NODE) {
            serialize(node.getChildNodes(), format, writer);
        } else {
            serialize(new NodeList() {
                public Node item(int index) {
                    return node;
                }

                public int getLength() {
                    return 1;
                }
            }, format, writer);
        }
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param nodeList The DOM subtree as a NodeList.
     * @param format Format the output.
     * @return The subtree in serailised form.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static String serialize(NodeList nodeList, boolean format) throws DOMException {
        StringWriter writer = new StringWriter();
        serialize(nodeList, format, writer);
        return writer.toString();
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param nodeList The DOM subtree as a NodeList.
     * @param format Format the output.
     * @param writer The target writer for serialization.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static void serialize(NodeList nodeList, boolean format, Writer writer) throws DOMException {

        if (nodeList == null) {
            throw new IllegalArgumentException(
                    "null 'subtree' NodeIterator arg in method call.");
        }

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer;

            if(format) {
                try {
                    factory.setAttribute("indent-number", new Integer(4));
                } catch(Exception e) {
                    // Ignore... Xalan may throw on this!!
                    // We handle Xalan indentation below (yeuckkk) ...
                }
            }
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            if(format) {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
            }

            int listLength = nodeList.getLength();

            // Iterate through the Node List.
            for (int i = 0; i < listLength; i++) {
                Node node = nodeList.item(i);

                if (isTextNode(node)) {
                    writer.write(node.getNodeValue());
                } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                    writer.write(((Attr) node).getValue());
                } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                    transformer.transform(new DOMSource(node), new StreamResult(writer));
                }
            }
        } catch (Exception e) {
            DOMException domExcep = new DOMException(
                    DOMException.INVALID_ACCESS_ERR,
                    "Unable to serailise DOM subtree.");
            domExcep.initCause(e);
            throw domExcep;
        }
    }

    /**
     * Is the supplied W3C DOM Node a text node.
     *
     * @param node The node to be tested.
     * @return True if the node is a text node, otherwise false.
     */
    public static boolean isTextNode(Node node) {
        short nodeType;

        if (node == null) {
            return false;
        }
        nodeType = node.getNodeType();

        return nodeType == Node.CDATA_SECTION_NODE
                || nodeType == Node.TEXT_NODE;
    }

    /**
     * Get the W3C NodeList instance associated with the XPath selection
     * supplied.
     *
     * @param node  The document node to be searched.
     * @param xpath The XPath String to be used in the selection.
     * @return The W3C NodeList instance at the specified location in the
     *         document, or null.
     */
    public static NodeList getNodeList(Node node, String xpath) {
        if (node == null) {
            throw new IllegalArgumentException(
                    "null 'document' arg in method call.");
        } else if (xpath == null) {
            throw new IllegalArgumentException(
                    "null 'xpath' arg in method call.");
        }
        try {
            XPath xpathEvaluater = xPathFactory.newXPath();
            xpathEvaluater.setNamespaceContext( CommonNamespaceContext.getInstance() );

            if (xpath.endsWith(ELEMENT_NAME_FUNC)) {
                return (NodeList) xpathEvaluater.evaluate(xpath.substring(0,
                        xpath.length() - ELEMENT_NAME_FUNC.length()), node,
                        XPathConstants.NODESET);
            } else {
                return (NodeList) xpathEvaluater.evaluate(xpath, node,
                        XPathConstants.NODESET);
            }
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("bad 'xpath' expression ["
                    + xpath + "].");
        }
    }

    /**
     * Returns the string result of the supplied XPath
     *
     * @param xml  The xml document to be searched.
     * @param xpath The XPath String to be used in the selection.
     * @return String result, XML or literal.
     */
    public static String[] getSubXML( String xml, String xpath ) throws Exception {
        if (xpath == null || "".equals(xpath))
            return new String[]{};
        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        xPath.setNamespaceContext(CommonNamespaceContext.getInstance());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        NodeList list = document.getDocumentElement().getChildNodes();
        NodeList s =  (NodeList)xPath.evaluate(xpath, list, XPathConstants.NODESET);
        String[] res = new String[s.getLength()];
        for (int i = 0; i < s.getLength(); i++) {
            Node n = s.item(i);
            Node sibling = n.getNextSibling();
            if (sibling != null && sibling.getNodeType() == Node.CDATA_SECTION_NODE)
                n = sibling;
            res[i] = XMLUtil.serialize(n, false);
        }
        return res;
    }

    /**
     * Returns the xml result of some elements substitution
     *
     * @param xml  The xml document to be searched.
     * @param xpath The XPath String to be used in the selection.
     * @param values The substitution table.
     * @return String result, substitued XML.
     */
    public static String substituteXMLContent( String xml, String xpath, Hashtable values, boolean keepCDATA) throws Exception {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        xPath.setNamespaceContext(CommonNamespaceContext.getInstance());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        NodeList list = document.getDocumentElement().getChildNodes();
        NodeList s =  (NodeList)xPath.evaluate(xpath, list, XPathConstants.NODESET);
        for (int i = 0; i < s.getLength(); i++) {
            Node n = s.item(i);
            Node sibling = n.getNextSibling();
            boolean isCDATANode = false;
            if (n.getNodeType() == Node.CDATA_SECTION_NODE)
                isCDATANode = true;
            else if (sibling != null && sibling.getNodeType() == Node.CDATA_SECTION_NODE) {
                n = sibling;
                isCDATANode = true;
            }
            String key = n.getTextContent();
            String newValue = (String)values.get(key);
            if (newValue != null) {
                if (isCDATANode && !keepCDATA) {
                    Node newNode = document.createTextNode(newValue);
                    n.getParentNode().replaceChild(newNode, n);
                }
                else
                    n.setNodeValue(newValue);
            }
        }

        return getXMLString(document);
    }

    public static String removeHeaderDirective( String xml ) {
        int indexOfDirStart = xml.indexOf( "<?" );
        if( indexOfDirStart == -1 )
            return( xml );

        int indexOfDirEnd = xml.indexOf( "?>", indexOfDirStart + 2 );
        if( indexOfDirEnd == -1 )
            return( xml );

        return( xml.substring( xml.indexOf( "<", indexOfDirEnd + 2 ) ) );
    }

    /**
     * Returns root tagname
     *
     * @param xml  The xml document to be searched.
     * @return String result.
     */
    public static String getRootTagName(String xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        return document.getDocumentElement().getTagName();
    }

    /**
     * Returns element attributes of the 1st result of the supplied xpath
     *
     * @param xml  The xml document to be searched.
     * @param xpath  The element to find.
     * @return Hashtable results.
     */
    public static Hashtable getAttributes(String xml, String xpath) throws Exception {
        String[] array = getSubXML(xml, xpath);
        if (array.length == 0)
            return null;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(array[0])));
        NamedNodeMap nnm = document.getDocumentElement().getAttributes();
        Hashtable<String, String> res = new Hashtable<String, String>();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node node = nnm.item(i);
            res.put(node.getLocalName(), node.getTextContent());
        }
        return res;
    }

    public static String applyXslToDocument( Source xslt, Source doc, URIResolver resolver, Properties transformerProperties, HashMap<String,String> params ) throws IOException, TransformerConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        if( resolver != null )
            transformerFactory.setURIResolver( resolver );
        
        Transformer transformer = transformerFactory.newTransformer( xslt );
        if( transformerFactory != null )
            transformer.setOutputProperties( transformerProperties );

        if( params != null ) {
            for( Map.Entry<String,String> cursor : params.entrySet() ) {
                transformer.setParameter( cursor.getKey(), cursor.getValue() );
            }
        }

        StringWriter strWriter = new StringWriter();
        StreamResult result = new StreamResult( strWriter );
        transformer.transform( doc, result );

        return( strWriter.toString() );
    }

    public static Node applyXslToDocument2( Source xslt, Source doc, URIResolver resolver, Properties transformerProperties, HashMap<String,String> params ) throws IOException, TransformerConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        if( resolver != null )
            transformerFactory.setURIResolver( resolver );
        
        Transformer transformer = transformerFactory.newTransformer( xslt );
        if( transformerFactory != null )
            transformer.setOutputProperties( transformerProperties );

        if( params != null ) {
            for( Map.Entry<String,String> cursor : params.entrySet() ) {
                transformer.setParameter( cursor.getKey(), cursor.getValue() );
            }
        }

        DOMResult result = new DOMResult();
        transformer.transform( doc, result );

        return( result.getNode() );
    }

}
