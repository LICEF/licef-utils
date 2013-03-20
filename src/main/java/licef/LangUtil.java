package licef;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LangUtil {

    public static String getISO2Language( String langCode, boolean returnNullIfNotFound ) {
        langCode = langCode.toLowerCase();
        String lang = langISO2.get( langCode );
        if( returnNullIfNotFound )
            return( lang == null ? null : lang );
        else
            return( lang == null ? langCode : lang );
    }

    public static String getISO2Language( String langCode ) {
        return( getISO2Language( langCode, false ) );
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
