package licef;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LangUtil {

    /**
     * Obtains a ISO-639-2 compliant equivalent language code. 
     * @param langCode A 2 or 3 characters long language code like fr or fre.
     * @return A corresponding language code from the ISO-639-2 table or a lowercased value of langCode if langCode is unknown to the system.
     */
    public static String getISO2Language( String langCode ) {
        return( getISO2Language( langCode, false ) );
    }

    /**
     * Obtains a ISO-639-2 compliant equivalent language code. 
     * @param langCode A 2 or 3 characters long language code like fr or fre.
     * @param returnsNullIfNotFound <code>null</code> if the language code is not known by the system or lowercased langCode otherwise.
     * @return A corresponding language code from the ISO-639-2 table.
     */
    public static String getISO2Language( String langCode, boolean returnsNullIfNotFound ) {
        if( langCode == null )
            return( null );

        langCode = langCode.toLowerCase();
        String lang = langISO2.get( langCode );
        if( returnsNullIfNotFound )
            return( lang == null ? null : lang );
        else
            return( lang == null ? langCode : lang );
    }

    /**
     * Converts a language code like fr_CA to fr-CA or fre-FR to fr-FR.
     * @param langCode A language code that may contain either a 2 or 3 characters long language and an optional 2-characters long country code.
     * @return The same language code except that its language part will be converted to ISO-639-2 if possible.
     */
    public static String convertLangToISO2( String langCode ) {
        return( convertLangToISO2( langCode, "-" ) );
    }

    /**
     * Converts a language code like fr_CA to fr-CA or fre-FR to fr-FR.
     * @param langCode A language code that may contain either a 2 or 3 characters long language and an optional 2-characters long country code.
     * @param delimiter Character used to separate the language and country parts.
     * @return The same language code except that its language part will be converted to ISO-639-2 if possible.
     */
    public static String convertLangToISO2( String langCode, String delimiter ) {
        if( langCode == null )
            return( null );

        String[] parts = langCode.split( "[-_]" );
        String lang = parts[ 0 ];
        String country = ( parts.length > 1 ? parts[ 1 ] : null );
        String convertedLang = LangUtil.getISO2Language( lang ) + ( country == null ? "" : delimiter + country );
        return( convertedLang );
    }

    private static Map<String, String> langISO2; // Contains entries like fre -> fr, spa -> es, etc.

    static {
        String[] languages = Locale.getISOLanguages();

        langISO2 = new HashMap<String, String>( languages.length );
        for( String language : languages ) {
            Locale locale = new Locale( language );
            langISO2.put( locale.getISO3Language(), locale.getLanguage() );
        }

        // Add also ISO-639-2B codes.
        langISO2.put( "alb", "sq" );
        langISO2.put( "arm", "hy" );
        langISO2.put( "baq", "eu" );
        langISO2.put( "bur", "my" );
        langISO2.put( "chi", "zh" );
        langISO2.put( "cze", "cs" );
        langISO2.put( "dut", "nl" );
        langISO2.put( "fre", "fr" );
        langISO2.put( "ger", "de" );
        langISO2.put( "geo", "ka" );
        langISO2.put( "gre", "el" );
        langISO2.put( "ice", "is" );
        langISO2.put( "mac", "mk" );
        langISO2.put( "mao", "mi" );
        langISO2.put( "may", "ms" );
        langISO2.put( "per", "fa" );
        langISO2.put( "rum", "ro" );
        langISO2.put( "slo", "sk" );
        langISO2.put( "tib", "bo" );
        langISO2.put( "wel", "cy" );
    }

}
