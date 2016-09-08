package com.iguxuan.iguxuan_friends.util;

import android.text.Html;
import android.text.util.Linkify;
import android.widget.TextView;

import com.iguxuan.iguxuan_friends.widget.MyLinkMovementMethod;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by jack on 2016/5/23.
 */
public class Utils {
    /**
     * Convert time to a string
     *
     * @param millis e.g.time/length from file
     * @return formated string (hh:)mm:ss
     */
    public static String millisToString(long millis) {
        return millisToString(millis, false);
    }

    /**
     * Convert time to a string
     *
     * @param millis e.g.time/length from file
     * @return formated string "[hh]h[mm]min" / "[mm]min[s]s"
     */
    public static String millisToText(long millis) {
        return millisToString(millis, true);
    }

    private static String millisToString(long millis, boolean text) {
        boolean negative = millis < 0;
        millis = java.lang.Math.abs(millis);

        millis /= 1000;
        int sec = (int) (millis % 60);
        millis /= 60;
        int min = (int) (millis % 60);
        millis /= 60;
        int hours = (int) millis;

        String time;
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        format.applyPattern("00");
        if (text) {
            if (millis > 0)
                time = (negative ? "-" : "") + hours + "h" + format.format(min) + "min";
            else if (min > 0)
                time = (negative ? "-" : "") + min + "min";
            else
                time = (negative ? "-" : "") + sec + "s";
        } else {
            if (millis > 0)
                time = (negative ? "-" : "") + hours + ":" + format.format(min) + ":" + format.format(sec);
            else
                time = (negative ? "-" : "") + min + ":" + format.format(sec);
        }
        return time;
    }

    /**
     * 显示评论
     */
    public static void showComment(TextView textview, String content) {
        textview.setAutoLinkMask(Linkify.WEB_URLS);
        textview.setText(Html.fromHtml(content));
        textview.setMovementMethod(MyLinkMovementMethod.getInstance());//设置链接跳转
    }
}
