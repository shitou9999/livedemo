package tv.kuainiu.utils;


public class TimeFormatUtil {
    public static String getTime(long time) {
        time = time / 1000;
        return getTime((int) time);
    }

    public static String getTime(int time) {
        if (time > 0) {
            StringBuilder sb = new StringBuilder();
            int secondTime = time % 60;
            int minuteTime = time / 60;
            sb.append(formatTime(minuteTime));
            sb.append(":");
            sb.append(formatTime(secondTime));
            return sb.toString();
        } else {
            return "00:00";
        }
    }

    private static String formatTime(int time) {
        if (time > 9) {
            return time + "";
        } else {
            return "0" + time;
        }
    }
}