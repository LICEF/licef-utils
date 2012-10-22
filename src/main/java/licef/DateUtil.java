package licef;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtil {

    /** 
     * Return if two instances of Date are equals (at the minute precision).
     * @param date1 First date to compare.
     * @param date2 Second date to compare.
     * @return <code>true</code> if the date1 == date2.
     */
    public static boolean isSameTime( Date date1, Date date2 )
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime( date1 );
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime( date2 );
        return( ( cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR ) ) &&
                ( cal1.get( Calendar.MONTH ) == cal2.get( Calendar.MONTH ) ) &&
                ( cal1.get( Calendar.DATE ) == cal2.get( Calendar.DATE ) ) &&
                ( cal1.get( Calendar.HOUR_OF_DAY ) == cal2.get( Calendar.HOUR_OF_DAY ) ) &&
                ( cal1.get( Calendar.MINUTE ) == cal2.get( Calendar.MINUTE ) ) );
    }

    /** 
     * Return if two instances of Date are of the same day.
     * @param date1 First date to compare.
     * @param date2 Second date to compare.
     * @return <code>true</code> if date1 and date2 are the same day.
     */
    public static boolean isSameDay( Date date1, Date date2 )
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime( date1 );
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime( date2 );
        return( ( cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR ) ) &&
                ( cal1.get( Calendar.MONTH ) == cal2.get( Calendar.MONTH ) ) &&
                ( cal1.get( Calendar.DATE ) == cal2.get( Calendar.DATE ) ) );
    }

    /** 
     * Add the input hour count, the input minute count and input second count and cumulate that into a second total.
     * @param h Hour count.
     * @param m Minute count.
     * @param s Second count.
     * @return The total of seconds.
     */
    public static int toSeconds(int h, int m, int s)
    {
        int sec = 0;
        sec += h * 3600;
        sec += m * 60;
        sec += s;
        return sec;
    }

    /**
     * Convert a Date to ISO format string
     * @param date
     * @param format
     * @param tz
     * @return result as string 
     */
    public static String toISOString(Date date, String format, TimeZone tz)
    {
        if( format == null ) format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        if( tz == null ) tz = TimeZone.getDefault();
        DateFormat f = new SimpleDateFormat(format);
        f.setTimeZone(tz);
        return f.format(date);
    }

}
