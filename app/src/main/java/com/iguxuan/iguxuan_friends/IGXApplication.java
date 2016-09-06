package com.iguxuan.iguxuan_friends;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * @author nanck on 2016/7/29.
 */
public class IGXApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static final boolean DEBUG = BuildConfig.DEBUG;

    @Override public void onCreate() {
        super.onCreate();
        if (DEBUG) {
            registerActivityLifecycleCallbacks(this);
        }
    }


    // ===============================================================================
    // Activity life callback ||
    // ===============================================================================
    @Override public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.d(activity.getClass().getSimpleName(), "onActivityCreated");
    }

    @Override public void onActivityDestroyed(Activity activity) {
        Log.d(activity.getClass().getSimpleName(), "onActivityDestroyed");
    }

    @Override public void onActivityPaused(Activity activity) {
        Log.d(activity.getClass().getSimpleName(), "onActivityPaused");

    }

    @Override public void onActivityResumed(Activity activity) {
        Log.d(activity.getClass().getSimpleName(), "onActivityResumed");

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.d(activity.getClass().getSimpleName(), "onActivitySaveInstanceState");

    }

    @Override public void onActivityStarted(Activity activity) {
        Log.d(activity.getClass().getSimpleName(), "onActivityStarted");

    }

    @Override public void onActivityStopped(Activity activity) {
        Log.d(activity.getClass().getSimpleName(), "onActivityStopped");
    }
}
