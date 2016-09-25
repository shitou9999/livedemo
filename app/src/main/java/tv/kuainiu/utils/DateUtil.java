package tv.kuainiu.utils;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by guxuan on 2016/3/16.
 */
public class DateUtil {
    public static final String FORMAT_DATE = "yyyy-MM-dd";

    /**
     * 将时间戳转换成某种格式的字符串形式
     *
     * @param value
     * @return
     */
    public static String toStringForTimestamp(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE, Locale.CHINA);
        try {
            Date date = sdf.parse(value);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            // java格式时间戳传给php后台，需要截取后三位
            long result = toPhp(calendar.getTimeInMillis());
            return String.valueOf(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return value;
        }
    }


    /**
     * 将日期格式的字符串转换成时间戳
     *
     * @param format
     * @param time
     * @return
     */

    public static String toTimestampForString(String format, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        String result = "000000";
        try {
            result = sdf.format(new Date(time));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static long toPhp(long millis) {
        String temp = String.valueOf(millis);
        return Long.parseLong(temp.substring(0, temp.length() - 3));
    }

    public static long toJava(long millis) {
        String temp = String.valueOf(millis);
        return Long.parseLong(temp.concat("000"));
    }


    /**
     * 计算当前日期与某日期相差的天数
     *
     * @param returnDate
     * @return day
     * @see {@link #daysBetween(long, long)}
     */
    public static int daysBetween(long returnDate) {
        Calendar cal = Calendar.getInstance();
        DebugUtils.dd("dtae time : " + returnDate);
        DebugUtils.dd("dtae cal : " + cal.getTimeInMillis());
        DebugUtils.dd("dtae time day : " + daysBetween(cal.getTimeInMillis(), returnDate));
        return daysBetween(cal.getTimeInMillis(), returnDate);
    }

    /**
     * 计算某个日期毫秒数与另一个日期毫秒数相差的天数
     *
     * @param now
     * @param returnDate
     * @return day
     * @see {@link #daysBetween(Date, Date)}
     */
    public static int daysBetween(long now, long returnDate) {
        return daysBetween(new Date(now), new Date(returnDate));
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param now
     * @param returnDate
     * @return day
     */
    public static int daysBetween(Date now, Date returnDate) {
        Calendar cNow = Calendar.getInstance();
        Calendar cReturnDate = Calendar.getInstance();
        cNow.setTime(now);
        cReturnDate.setTime(returnDate);
        setTimeToMidnight(cNow);
        setTimeToMidnight(cReturnDate);
        long todayMs = cNow.getTimeInMillis();
        long returnMs = cReturnDate.getTimeInMillis();
        long intervalMs = todayMs - returnMs;
        return millisecondsToDays(intervalMs);
    }

    public static int millisecondsToDays(long intervalMs) {
        return (int) (intervalMs / (1000 * 86400));
    }

    public static void setTimeToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }


    public static Duration millise(long endInstant) {
        return new Duration(DateTime.now().getMillis(), endInstant);
    }

    public static String getDurationString(String dateFormat, long value) {
        Duration duration = DateUtil.millise(value);
        long day = Math.abs(duration.getStandardDays());
        long hour = Math.abs(duration.getStandardHours());
        long minutes = Math.abs(duration.getStandardMinutes());
        long seconds = Math.abs(duration.getStandardSeconds());
        String date;
        if (day < 1) {
            if (hour < 1) {
                if (seconds < 30) {
                    date = "刚刚";
                } else {
                    date = minutes + "分钟前";
                }
            } else {
                date = hour + "小时前";
            }
        } else {
            date = DateUtil.toTimestampForString(dateFormat, value);
        }
        return date;
    }

    public static String getDurationString(long value) {
        return getDurationString("yyyy-MM-dd", value);
    }
    public static String getDurationString(String value) {
        return getDurationString("yyyy-MM-dd", Long.parseLong(value));
    }
    /**
     * 计算两个日期相隔的分钟
     *
     * @param firstString  第一个时间
     * @param secondString 第二个时间
     * @return
     */
    public static int minutesBetweenTwoDate(String firstString, String secondString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date firstDate = null;
        Date secondDate = null;
        int minute = 0;
        try {
            firstDate = df.parse(firstString);
            secondDate = df.parse(secondString);
            minute = (int) ((secondDate.getTime() - firstDate.getTime()) / (60 * 1000));
        } catch (Exception e) {
            minute = 0;
        }
        return minute;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentDate() {
        return getCurrentDate("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentDate(String format) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(StringUtils.replaceNullToEmpty(format, "yyyy-MM-dd HH:mm:ss"),
                Locale.CHINA);// 可以方便地修改日期格式
        return dateFormat.format(now);
    }
}
