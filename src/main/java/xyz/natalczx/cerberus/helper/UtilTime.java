// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper;

import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UtilTime
{
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
    
    public static String now() {
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }
    
    public static long nowlong() {
        return System.currentTimeMillis();
    }
    
    public static String when(final long time) {
        final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return sdf.format(time);
    }
    
    public static long a(final String a) {
        if (a.endsWith("s")) {
            return Long.valueOf(a.substring(0, a.length() - 1)) * 1000L;
        }
        if (a.endsWith("m")) {
            return Long.valueOf(a.substring(0, a.length() - 1)) * 60000L;
        }
        if (a.endsWith("h")) {
            return Long.valueOf(a.substring(0, a.length() - 1)) * 3600000L;
        }
        if (a.endsWith("d")) {
            return Long.valueOf(a.substring(0, a.length() - 1)) * 86400000L;
        }
        if (a.endsWith("M")) {
            return Long.valueOf(a.substring(0, a.length() - 1)) * 2592000000L;
        }
        if (a.endsWith("y")) {
            return Long.valueOf(a.substring(0, a.length() - 1)) * 31104000000L;
        }
        return -1L;
    }
    
    public static String date() {
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
    
    public static String getTime(final int time) {
        final Date timeDiff = new Date();
        timeDiff.setTime(time * 1000);
        final SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
        return timeFormat.format(timeDiff);
    }
    
    public static String since(final long epoch) {
        return "Took " + convertString(System.currentTimeMillis() - epoch, 1, TimeUnit.FIT) + ".";
    }
    
    public static double convert(final long time, final int trim, TimeUnit type) {
        if (type == TimeUnit.FIT) {
            if (time < 60000L) {
                type = TimeUnit.SECONDS;
            }
            else if (time < 3600000L) {
                type = TimeUnit.MINUTES;
            }
            else if (time < 86400000L) {
                type = TimeUnit.HOURS;
            }
            else {
                type = TimeUnit.DAYS;
            }
        }
        if (type == TimeUnit.DAYS) {
            return UtilsB.trim(trim, time / 8.64E7);
        }
        if (type == TimeUnit.HOURS) {
            return UtilsB.trim(trim, time / 3600000.0);
        }
        if (type == TimeUnit.MINUTES) {
            return UtilsB.trim(trim, time / 60000.0);
        }
        if (type == TimeUnit.SECONDS) {
            return UtilsB.trim(trim, time / 1000.0);
        }
        return UtilsB.trim(trim, (double)time);
    }
    
    public static String MakeStr(final long time) {
        return convertString(time, 1, TimeUnit.FIT);
    }
    
    public static String MakeStr(final long time, final int trim) {
        return convertString(time, trim, TimeUnit.FIT);
    }
    
    public static String convertString(final long time, final int trim, TimeUnit type) {
        if (time == -1L) {
            return "Permanent";
        }
        if (type == TimeUnit.FIT) {
            if (time < 60000L) {
                type = TimeUnit.SECONDS;
            }
            else if (time < 3600000L) {
                type = TimeUnit.MINUTES;
            }
            else if (time < 86400000L) {
                type = TimeUnit.HOURS;
            }
            else {
                type = TimeUnit.DAYS;
            }
        }
        if (type == TimeUnit.DAYS) {
            return UtilsB.trim(trim, time / 8.64E7) + " Days";
        }
        if (type == TimeUnit.HOURS) {
            return UtilsB.trim(trim, time / 3600000.0) + " Hours";
        }
        if (type == TimeUnit.MINUTES) {
            return UtilsB.trim(trim, time / 60000.0) + " Minutes";
        }
        if (type == TimeUnit.SECONDS) {
            return UtilsB.trim(trim, time / 1000.0) + " Seconds";
        }
        return UtilsB.trim(trim, (double)time) + " Milliseconds";
    }
    
    public static boolean elapsed(final long from, final long required) {
        return System.currentTimeMillis() - from > required;
    }
    
    public static long elapsed(final long starttime) {
        return System.currentTimeMillis() - starttime;
    }
    
    public static long left(final long start, final long required) {
        return required + start - System.currentTimeMillis();
    }
    
    public enum TimeUnit
    {
        FIT, 
        DAYS, 
        HOURS, 
        MINUTES, 
        SECONDS, 
        MILLISECONDS;
    }
}
