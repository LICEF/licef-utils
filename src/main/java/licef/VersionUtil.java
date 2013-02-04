package licef;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class VersionUtil {

    public static int compare( String v1, String v2 ) {
        return( compare( getVersionParts( v1 ), getVersionParts( v2 ) ) );
    }

    public static ArrayList<Integer> getVersionParts( String version ) {
        ArrayList<Integer> parts = new ArrayList<Integer>();

        if( version == null )
            return( parts );

        for( int i = 0; i < versionPatterns.length; i++ ) {
            Matcher m = versionPatterns[ i ].matcher( version );
            if( m.matches() ) {
                for( int g = 1; g <= m.groupCount(); g++ ) {
                    parts.add( Integer.parseInt( m.group( g ) ) );
                }
                break;
            }
        }

        return( parts );
    }

    private static int compare( ArrayList<Integer> v1Parts, ArrayList<Integer> v2Parts ) {
        int i = 0;
        for( ;; ) {
            if( v2Parts.size() < i+1 )
                if( v1Parts.size() < i+1 )
                    return( 0 );
                else
                    return( 1 );
            else {
                if( v1Parts.size() < i+1 )
                    return( -1 );
                else { 
                    int test = v1Parts.get( i ).compareTo( v2Parts.get( i ) );
                    if( test == 0 )
                        i++;
                    else
                        return( test );
                }
            }
                
        }
    }

    private static Pattern vp4 = Pattern.compile( ".*(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+).*" );
    private static Pattern vp3 = Pattern.compile( ".*(\\d+)\\.(\\d+)\\.(\\d+).*" );
    private static Pattern vp2 = Pattern.compile( ".*(\\d+)\\.(\\d+).*" );
    private static Pattern vp1 = Pattern.compile( ".*(\\d+).*" );

    private static Pattern[] versionPatterns = new Pattern[] { vp4, vp3, vp2, vp1 };

}
