package licef;

import junit.framework.TestCase;

public class VersionUtilTest extends TestCase {

    public VersionUtilTest( String name ) {
        super( name );
    }

    public void testCompare() throws Exception {
        assertTrue( VersionUtil.compare( null, "" ) == 0 );
        assertTrue( VersionUtil.compare( "", null ) == 0 );
        assertTrue( VersionUtil.compare( "", "" ) == 0 );
        assertTrue( VersionUtil.compare( "1", "" ) == 1 );
        assertTrue( VersionUtil.compare( "", "1" ) == -1 );
        assertTrue( VersionUtil.compare( "1", "1" ) == 0 );
        assertTrue( VersionUtil.compare( "2", "1" ) == 1 );
        assertTrue( VersionUtil.compare( "1", "2" ) == -1 );
        assertTrue( VersionUtil.compare( "1.0", "1" ) == 1 );
        assertTrue( VersionUtil.compare( "1.1", "1" ) == 1 );
        assertTrue( VersionUtil.compare( "1", "1.0" ) == -1 );
        assertTrue( VersionUtil.compare( "1", "1.1" ) == -1 );
        assertTrue( VersionUtil.compare( "1.1", "1.2" ) == -1 );
        assertTrue( VersionUtil.compare( "1.2", "1.1" ) == 1 );
        assertTrue( VersionUtil.compare( "4.0", "4.0.1" ) == -1 );
        assertTrue( VersionUtil.compare( "4.9", "4.10" ) == -1 );
        assertTrue( VersionUtil.compare( "4.90", "4.10" ) == 1 );
    }

}

