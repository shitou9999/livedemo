package com.iguxuan.iguxuan_friends.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.iguxuan.iguxuan_friends.IGXApplication;


public class DebugUtils {

    public static boolean DEBUG_MODE = IGXApplication.IsDegbug;

    public static void v(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.d(tag, msg);
        }
    }

    public static void dd(String tag, String msg) {
        if (DEBUG_MODE) {
            String prdfix = (Thread.currentThread().getStackTrace()[3]).getMethodName();
            Log.d(tag, prdfix + " " + msg);
        }
    }

    public static void dd(String msg) {
        if (DEBUG_MODE) {
            String tag = (Thread.currentThread().getStackTrace()[3]).getClassName();
            String prdfix = (Thread.currentThread().getStackTrace()[3]).getMethodName();
            d(tag, prdfix + "----" + msg);
        }
    }

    public static void outLife() {
        if (DEBUG_MODE) {
            String tag = (Thread.currentThread().getStackTrace()[3]).getClassName();
            String prdfix = (Thread.currentThread().getStackTrace()[3]).getMethodName();
            d(tag, "life----" + prdfix);
        }
    }

    public static void outLife(String tag) {
        if (DEBUG_MODE) {
            String prdfix = (Thread.currentThread().getStackTrace()[3]).getMethodName();
            d(tag, "life----" + prdfix);
        }
    }


    public static void i(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.e(tag, msg);
        }
    }


    public static void showToast(Context context, String message) {
        ToastUtils.showToast(context, message);
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

    public static void showToastResponse(Context context, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (!"重新初始化".equals(msg)) {
            showToast(context, msg);
        }
    }

}
