package licef;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.w3c.dom.Document;

public class LOMUtil {

    /**
     * Return a IEEE Lom with some metadatas if there are passed in parameter.
     * name of creator will be completed on Paloma server on insertion.
     * @param title
     * @param location
     * @return
     */
    public static String buildMinimalLom(String englishTitle, String frenchTitle, String metadataLang, String location) {
        Calendar cal = Calendar.getInstance();
        String now = cal.get(Calendar.YEAR) + "-";
        now += StringUtil.insertLeading( '0', 2, cal.get(Calendar.MONTH) + 1 + "" );
        now += "-";
        now += StringUtil.insertLeading( '0', 2, cal.get(Calendar.DATE) + "" );

        //general
        String lom = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
                "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchemainstance\" " +
                "xsi:schemaLocation=\"http://ltsc.ieee.org/xsd/LOM " +
                "http://ltsc.ieee.org/xsd/lomv1.0/lom.xsd\">\n" +
                "<general><title>" +
                "<string language=\"en\">" + englishTitle + "</string>" +
                "<string language=\"fr\">" + frenchTitle + "</string>" +
                "</title></general>";

        //metaMetadata
        lom += "<metaMetadata><contribute><role><source>LOMv1.0</source><value>creator</value></role>" +
                "<date><dateTime>" + now + "</dateTime></date></contribute>"+
                "<metadataSchema>LOMv1.0</metadataSchema>"+
                ((metadataLang != null) ?"<language>" + metadataLang + "</language>":"")+
                "</metaMetadata>";

        //technical
        if (location != null) {
            lom += "<technical>";
            if (location != null)
                lom += "<location>" + location + "</location>";
            lom += "</technical>";
        }

        lom += "</lom>";

        return lom;
    }

    /** 
     * Utility class parsing LOM Xml to extract titles.
     */
    public static class LOMTitlesRetriever extends DefaultHandler {

        public LOMTitlesRetriever( String xml ) {
            try {
                SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                SAXParser parser = parserFactory.newSAXParser();
                parser.parse( new InputSource( new StringReader( xml ) ), this );        
            }
            catch( IOException e ) {
                e.printStackTrace();
            }
            catch( ParserConfigurationException e ) {
                e.printStackTrace();
            }
            catch( SAXException e ) {
                e.printStackTrace();
            }
        }

        public Enumeration getTitles( String language ) {
            Vector titles = (Vector)hTitle.get( language );
            if( titles == null )
                return( null );
            return( titles.elements() );
        }

        public String getFirstTitle( String language ) {
            Vector titles = (Vector)hTitle.get( language );
            if( titles == null || titles.size() == 0 )
                return( null );
            return( (String)titles.firstElement() );
        }

        public void startElement( String uri, String localName, String qName, Attributes attribs ) throws SAXException {
            tagStack.push( qName );
            //System.out.println( getPath() );

            if( "string".equals( qName ) && 
                "lom|general|title|string|".equals( getPath() ) ) {
                title = new StringBuffer();
                isTitleParsed = false;
                language = attribs.getValue( "language" );
            }
        }

        public void characters( char[] ch, int start, int length ) throws SAXException {
            String str = new String( ch, start, length );

            if( !isTitleParsed && "string".equals( tagStack.peek() ) && 
                "lom|general|title|string|".equals( getPath() ) ) {
                title.append( str );
            }
        }

        public void endElement( String uri, String localName, String qName ) throws SAXException {
            if( !isTitleParsed && "string".equals( qName ) && 
                "lom|general|title|string|".equals( getPath() ) ) {
                isTitleParsed = true;
                Vector vTitle = (Vector)hTitle.get( language );
                if( vTitle == null ) {
                    vTitle = new Vector();
                    hTitle.put( language, vTitle );
                }
                isTitleParsed = false;
                try {
                    vTitle.addElement( new String( title.toString().getBytes(), "UTF-8" ) );
                }
                catch( UnsupportedEncodingException ex ) {
                    vTitle.addElement( title.toString() );
                }
            }

            tagStack.pop();
        }

        private String getPath() {
            StringBuffer strPath = new StringBuffer();
            for( Enumeration e = tagStack.elements(); e.hasMoreElements(); )
                strPath.append( e.nextElement().toString() ).append( "|" );
            return( strPath.toString() );
        }

        private StringBuffer    title           = null;
        private String          language        = null;
        private boolean         isTitleParsed   = false;
        private Hashtable       hTitle          = new Hashtable();

        private Stack           tagStack        = new Stack();
        
    }

}
