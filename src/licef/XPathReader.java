package ca.licef.util;

import java.io.InputStream;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XPathReader {
    
    private Document xmlDocument;
    private XPath xPath;
    
    public XPathReader(InputStream xmlStream) {
        try {
            xmlDocument = DocumentBuilderFactory.
            newInstance().newDocumentBuilder().
            parse(xmlStream);            
            xPath =  XPathFactory.newInstance().
            newXPath();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
    }
    
    public XPathReader(String xmlFile) {
        try {
            xmlDocument = DocumentBuilderFactory.
            newInstance().newDocumentBuilder().
            parse(xmlFile);            
            xPath =  XPathFactory.newInstance().
            newXPath();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
    }
    
    public Object read(String expression, QName returnType){
        try {
            XPathExpression xPathExpression = 
            xPath.compile(expression);
            return xPathExpression.evaluate
            (xmlDocument, returnType);
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

