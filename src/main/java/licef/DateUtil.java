package licef;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import org.w3c.util.DateParser;

public class DateUtil {

    static int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

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

    /**
     * Convert an ISO Date string to a java Date
     * @param isoDate
     * @return result as string
     */
    public static Date toDate(String isoDate) throws Exception {
        return( DateParser.parse( isoDate ).getTime() );
    }

    /**
     * Return next day as ISO date without time.
     * @param strDate in ISO format. Should be passed without time.
     * @return
     */
    public static String nextDay(String strDate) {
        String[] spl = strDate.split("-");
        Date date =  new Date(Integer.parseInt(spl[0]) - 1900, Integer.parseInt(spl[1]) - 1, Integer.parseInt(spl[2]));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date.getTime() + MILLIS_IN_DAY);
    }

    /**
     * Compute duration between 2 dates
     * @param date1: the start date
     * @param date2: the end date
     * @return Integer array with number of [days, hours, mins, secs]
     */
    public static long[] getDuration(Date date1, Date date2) {
        return getDuration(date2.getTime() - date1.getTime());
    }

    public static long[] getDuration(long millis) {
        if(millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return new long[]{days, hours, minutes, seconds};
    }
}
