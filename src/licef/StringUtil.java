package licef;

import java.util.*;

public class StringUtil {

    public static final char NEW_LINE = '\n';

    /** 
     * Return the capitalized version of a string (i.e.: the first letter is capitalized). 
     * @param text String to capitalize. 
     * @return The capitalized string. 
     */
    public static String capitalize( String text )
    {
        if( text == null || text.length() == 0 )
            return( text );
        else if( text.length() == 1 )
            return( text.substring( 0, 1 ).toUpperCase() );
        else
            return( text.substring( 0, 1 ).toUpperCase() + text.substring( 1 ) );
    }

    /** 
     * Returns a string with leading characters in front of it.   
     * @param c Padding character.
     * @param numberOfChars Number of padding character to insert.
     * @param str String to modify.
     * @return A string preceeded by padding character.
     */
    public static String insertLeading( char c, int numberOfChars, String str )
    {
        StringBuffer result = new StringBuffer( numberOfChars );
        int n = numberOfChars - str.length();
        for( int i = 0; i < n; i++ )
            result.append( c );
        result.append( str );
        return( result.toString() );
    }

    /**
     * Return a string composed of several strings separated by a specified limiter and delimiter.
     * We usually pass an array of strings.  If we pass an array of objects, the <code>toString()</code> value
     * will be used.
     * <p>
     * For example,
     * <p>
     * joinString( new String[] { "titi", "toto", "tata" }, '\'',',' ) returns "'titi','toto','tata'".<br>
     * joinString( new String[] { "test" }, ':' ) returns "test".<br>
     * joinString( new Integer[] { new Integer( 2 ), new Integer( 4 ) }, ',' ) returns "2,4".<br>
     * <p>
     * @param strToJoin Array of objects from which the <code>toString()</code> values will be concatenated.
     * @param limiter Limiter character surrounding string values.
     * @param delimiter Delimiter character used to separate string values between each others.
     * @return A string resulting from the concatenation of the string values of the input array of objects.
     */
    public static String joinString(final Object[] strToJoin, final char limiter, final char delimiter  ) {
        StringBuffer str = new StringBuffer();
        int i = 0;
        for(; i < strToJoin.length-1; i++ )
            str.append(limiter + strToJoin[ i ].toString() + limiter  + delimiter);
        str.append(limiter +  strToJoin[ i ].toString()+limiter);
        return ( str.toString());
    }


    /**
     * Return a string composed of several strings separated by a specified delimiter.
     * We usually pass an array of strings.  If we pass an array of objects, the <code>toString()</code> value
     * will be used.
     * If a null object is passed, an empty string will be used.
     * If a null array is passed, an empty string will be returned.
     * <p>
     * For example,
     * <p>
     * join( new String[] { "titi", "toto", "tata" }, '/' ) returns "titi/toto/tata".<br>
     * join( new String[] { "test" }, ':' ) returns "test".<br>
     * join( new Integer[] { new Integer( 2 ), new Integer( 4 ) }, ',' ) returns "2,4".<br>
     * join( new Integer[] { new Integer( 2 ), null, new Integer( 4 ) }, ',' ) returns "2,,4".<br>
     * join( null, ',' ); returns "".<br>
     * <p>
     * @param strToJoin Array of objects from which the <code>toString()</code> values will be concatenated.
     * @param delimiter Delimiter character used to separate string values between each others.
     * @return A string resulting from the concatenation of the string values of the input array of objects separated
     * by the specified delimiter.
     */
    public static String join( final Object[] strToJoin, final char delimiter ) {
        if( strToJoin == null || strToJoin.length == 0 )
            return( "" );
        StringBuffer str = new StringBuffer();
        int i = 0;
        for(; i < strToJoin.length - 1; i++ ) {
            if( strToJoin[ i ] != null )
                str.append( strToJoin[ i ].toString() );
            str.append( delimiter );
        }
        if( strToJoin[ i ] != null )
            str.append( strToJoin[ i ].toString() );
        return( str.toString() );
    }

    /**
     * Split a string containing a specified delimiter character into several substrings. 
     * This method never returns null.  If no delimiter character is found in the input string,
     * the returned array will contain only the input string.
     * <p>
     * For example,
     * <p>
     * split( "titi/toto/tata" , '/' ) returns String[] { "titi", "toto", "tata" }.<br>
     * split( "allo bonjour", ':' ) returns String[] { "allo bonjour" }.<br>
     * <p>
     * @param strToSplit String to split.
     * @param delimiter Delimiter character used to split the string.
     * @return Array of strings.
     */
    public static String[] split( final String strToSplit, final char delimiter ) {
        Vector words = new Vector();
        int fromNdx = 0;
        int toNdx = -1;
        String word = null;
        int strLength = strToSplit.length();

        while( fromNdx < strLength ) {
            toNdx = strToSplit.indexOf( delimiter, fromNdx );
            if( toNdx == -1 )
                toNdx = strLength;
            word = strToSplit.substring( fromNdx, toNdx );
            words.addElement( word );
            fromNdx = toNdx + 1;
        }

        if( words.size() == 0 )
            return( new String[] { strToSplit } );

        String[] wordArray = new String[ words.size() ];
        words.copyInto( wordArray );
        return( wordArray );
    }

    /** 
     * Escape common HTML entities from a string. 
     * @param strToConvert String to convert.
     * @return A string with HTML entities. 
     */
    public static String escapeHTMLEntities( String strToConvert ) {
        StringBuffer str = new StringBuffer( strToConvert.length() );
        for( int i = 0; i < strToConvert.length(); i++ ) {
            char c = strToConvert.charAt( i );
            if( c == '"' )
                str.append( "&quot;" );
            else if( c == '&' )
                str.append( "&amp;" );
            else if( c == '<' )
                str.append( "&lt;" );
            else if( c == '>' )
                str.append( "&gt;" );
            else
                str.append( c );
        }
        return( str.toString() );
    }

    /** 
     * Returns the file extension (all the charcters following the last dot). 
     * @param filename String containing a filename.
     * @return The file extension of a string containing a filename.
     */
    public static String getFileExtension( String filename ) {
        int lastIndexOfDot = filename.lastIndexOf( "." );
        if( lastIndexOfDot != -1 && ( lastIndexOfDot + 1 < filename.length() ) ) 
            return( filename.substring( lastIndexOfDot + 1 ) );
        return( null );
    }

    /** 
     * Returns <code>true</code> if the input string is empty (i.e., null, of length 0 or containing only white spaces.
     * @param str String to verify.
     * @return <code>true</code> if the input string is empty (i.e., null, of length 0 or containing only white spaces.
     */
    public static boolean isEmptyString( String str ) {
        if( str == null )
            return( true );
        if( str.length() == 0 )
            return( true );
        char[] chars = str.toCharArray();
        for( int i = 0; i < chars.length; i++ ) {
            if( !Character.isWhitespace( chars[ i ] ) )
                return( false );
        }
        return( true );
    }

    /** 
     * Escape single quotes to backslash quotes for JavaScript.
     * @param str "I love O'Reilley's books" 
     * @return "I love O\'Reilley\'s books"
     */
    public static String escapeSingleQuotes( String str ) {
        return( str.replaceAll( "'", "\\\\'" ) );
    }

}
