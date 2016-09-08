package com.iguxuan.iguxuan_friends.util;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * 测量辅助类
 *
 * @author nanck on 2016/4/18.
 */
public class MeasureUtil {

    /**
     * px to dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int pxToDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dip to px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dipToPx(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px to sp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int pxToSp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * sp to px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int spToPx(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕尺寸大小 </p>
     *
     * @param context
     * @return int array arr[0] = width , arr[1] = height
     */
    public static int[] getScreenSize(Context context) {
        int[] sizeArray = new int[2];
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        sizeArray[0] = point.x;
        sizeArray[1] = point.y;
        return sizeArray;
    }

}
