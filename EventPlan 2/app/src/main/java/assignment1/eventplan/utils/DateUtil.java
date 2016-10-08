package assignment1.eventplan.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import assignment1.eventplan.entity.EventPlan;

/**
 * Created by yumizhang on 2016/9/26.
 */
public final class DateUtil {


    private static class SingleInstanceHolder {
        static final DateUtil util = new DateUtil();
    }

    final SimpleDateFormat format;
    final Calendar calendar;

    private DateUtil() {
        format = new SimpleDateFormat(EventPlan.formatPattern, Locale.getDefault());
        calendar = Calendar.getInstance();
    }

    public static DateUtil get() {
        return SingleInstanceHolder.util;
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public static String format(long dateTime) {
        return dateTime <= 0 ? "" : get().getFormat().format(dateTime);
    }


    /**
     * // yyyy-MM-dd 00:00:01 dateBegin
     * // yyyy-MM-dd 23:59:59 dateEnd
     *
     * @param dateStr {@link EventPlan#formatPattern}
     * @return null is not fit the set of time ,unless the long[ ]{ dateBegin, dateEnd};
     */
    @Nullable
    public static long[] parseDateTime(@Nullable String dateStr) {
        if (TextUtils.isEmpty(dateStr))
            return null;
        SimpleDateFormat dateFormat = get().getFormat();
        Calendar calendar;
        long dateBegin;
        long dateEnd;
        try {
            // yyyy-MM-dd 00:00:01 dateBegin
            // yyyy-MM-dd 23:59:59 dateEnd
            Date date = dateFormat.parse(dateStr);
            calendar = get().getCalendar();
            calendar.setTimeInMillis(date.getTime());
            resetDateTime(calendar);
            dateBegin = calendar.getTimeInMillis();
            calendar.add(Calendar.DATE, 1);
            calendar.add(Calendar.SECOND, -2);
            dateEnd = calendar.getTimeInMillis();
        } catch (ParseException e) {
            //parse error, so skip it
            e.printStackTrace();
            return null;
        }
        return new long[]{dateBegin, dateEnd};
    }


    /**
     * @param dateStr {@link EventPlan#formatPattern}
     * @return -1 ParseException
     */
    public static long parseDateTimeMillis(String dateStr) {
        SimpleDateFormat dateFormat = get().getFormat();
        Calendar calendar;
        try {
            Date date = dateFormat.parse(dateStr);
            calendar = get().getCalendar();
            calendar.setTimeInMillis(date.getTime());
            return resetDateTime(calendar).getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static long resetDateTime(long startDateTime) {
        Calendar dirty = get().getCalendar();
        dirty.setTimeInMillis(startDateTime);
        return resetDateTime(dirty).getTimeInMillis();
    }

    private static Calendar resetDateTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }


    public static boolean isBefore(long start, long end) {
        return new Date(start).before(new Date(end));
    }

    public static boolean isAfter(long start, long end) {
        return new Date(start).after(new Date(end));
    }
}
