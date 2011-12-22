package licef;

public class ArrayUtil {

    /** 
     * Search an array for a particular value.  The search is linear.
     * @param array Array of objects. 
     * @param value Searched value.
     * @return <code>true</code> if the array contains the searched value.
     */
    public static boolean contains( Object[] array, Object value ) {
        if( array == null || value == null )
            return( false );
        for( int i = 0; i < array.length; i++ ) {
            if( value.equals( array[ i ] ) )
                return( true );
        }
        return( false );
    }
    
}
