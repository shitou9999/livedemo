package tv.kuainiu.utils;

import android.app.Activity;

import tv.kuainiu.ui.MainActivity;
import tv.kuainiu.ui.SplashActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName ExitUtil
 * @Description 退出程序工具
 */
public class ExitUtil {

    private static ExitUtil exitutil = null;
    private static final Object mLock = new Object();

    public static ExitUtil getInstance() {
        synchronized (mLock) {
            if (exitutil == null) {
                exitutil = new ExitUtil();
            }
            return exitutil;
        }
    }

    private List<Activity> activityList = new LinkedList<>();

    /**
     * 添加Activity到容器中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityList != null && activity != null) {
            activityList.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (activityList != null && activity != null && activityList.contains(activity)) {
            activityList.remove(activity);
        }
    }

    /**
     * 遍历所有Activity并finish
     */
    public void closes() {
        if (activityList == null) {
            return;
        }
        for (Activity activity : activityList) {
            if (activity != null) {
                activity.finish();
            }
        }

    }

    /**
     * 关闭所有活动的Activity除了指定的一个之外
     */
    public void gotoHome() {
        if (activityList == null) {
            return;
        }
        for (Activity activity : activityList) {
            if (activity != null) {
                if (activity instanceof MainActivity) {
                    continue;
                }
                activity.finish();
            }
        }
    }

    /**
     * 关闭所有活动的Activity除了指定的一个之外
     */
    public void clear() {
        if (activityList == null) {
            return;
        }
        for (Activity activity : activityList) {
            if (activity != null) {
                if (activity instanceof MainActivity
                        || activity instanceof SplashActivity) {
                    continue;
                }
                activity.finish();
            }
        }
    }

    /**
     * 测底关闭程序，遍历所有Activity并finish
     */
    public void exit() {
        closes();
        System.exit(0);
    }

}
