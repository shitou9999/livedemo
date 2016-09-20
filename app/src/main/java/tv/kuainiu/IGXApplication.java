package tv.kuainiu;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;

import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import tv.kuainiu.app.MVideoClient;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.preferences.UserPreferencesManager;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.util.AppUtils;
import tv.kuainiu.util.CrashHandler;
import tv.kuainiu.util.DebugUtils;
import tv.kuainiu.util.FileUtils;
import tv.kuainiu.util.LogUtils;
import tv.kuainiu.util.PreferencesUtils;
import tv.kuainiu.util.SDCardUtils;
import tv.kuainiu.util.SecurityUtils;
import tv.kuainiu.util.StringUtils;


public class IGXApplication extends Application {
    private static final String TAG = "IGXApplication";
    private static User user;
    public static final boolean IsDegbug = true;
    public static final String KEY_DEVICEID = "deviceid_key";
    /**
     * <p>
     * 用户被引导关注的老师列表
     * </p>
     *

     */
    public static String followTeaCherIds = "";

    private static IGXApplication mApplication;

    private String device;
    private static String key;


    // MyApplication初始化
    public static synchronized IGXApplication getInstance() {
        return mApplication;
    }


    @Override protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
         init();
        // strictMode(); // 启用严格模式
    }

    public void init() {
        try {
            device = getDeviceId();

            MobclickAgent.setDebugMode(false);
            MobclickAgent.setCatchUncaughtExceptions(true); // 开启错误统计

            // 初始化 JPush
            JPushInterface.init(this);
            // 设置开启日志,发布时请关闭日志
            JPushInterface.setDebugMode(IsDegbug);

            MVideoClient.getmInstance(this).initPolyvCilent();  // 初始化保利威视SDK

            if (IsDegbug) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    LeakCanary.install(this);                       // 监控内存溢出
                }
            }

            if (IsDegbug) {
                String errorFilesSavePath = SDCardUtils.getSDCardPath() +
                        AppUtils.getAppName(this) + File.separator;

                LogUtils.setFileLogPath(errorFilesSavePath);    // 设置log日志保持位置
                CrashHandler crashHandler = CrashHandler.getInstance(); // 异常信息捕获
                crashHandler.init(this, errorFilesSavePath);    // 注册crashHandler
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            DebugUtils.w(TAG, "----执行应用初始化缺少必要的权限----");
        }
        //CC直播网络请求日志

    }

    private String generateDeviceId() {
        String deviceId = SecurityUtils.MD5.getMD5(OKHttpUtils.Utils.getDeviceNumber());
        PreferencesUtils.putString(getInstance(), KEY_DEVICEID, deviceId);
        DebugUtils.d(TAG, "生成设备号成功：" + deviceId);
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        device = deviceId;
        PreferencesUtils.putString(getInstance(), KEY_DEVICEID, deviceId);
    }

    public String getDeviceId() {
        if (TextUtils.isEmpty(device)) {
            String str;
            str = PreferencesUtils.getString(getInstance(), KEY_DEVICEID, "");
            DebugUtils.d(TAG, "读取设备号：" + str);
            if (TextUtils.isEmpty(str)) {
                device = generateDeviceId();
                // 清除用户信息
                clearLocalData();
            } else {
                device = str;
            }

        }
        return device;
    }

    public static void setUser(User user) {
        IGXApplication.user = user;
        FileUtils.saveUser(mApplication, user);
//        PreferencesUtils.putString(mApplication, CustomFragment.CATCH_TIME, "");
        UserPreferencesManager.putUserExtraInfo(user);
        if (user == null) {
            PreferencesUtils.putString(mApplication, Constant.SESSION_ID, "");
        }
    }


    public static User getUser() {
        if (null == user) {
            user = FileUtils.readUser(mApplication);
        }
        return user;
    }


    public static boolean isLogin() {
        return !(null == getUser());
    }


    public static void setKey(String key) {
        IGXApplication.key = key;
        PreferencesUtils.putString(getInstance(), Constant.PRIVATE_KEY, StringUtils.replaceNullToEmpty(key));
    }


    public static String getKey() {
        if (TextUtils.isEmpty(key)) {
            key = PreferencesUtils.getString(getInstance(), Constant.PRIVATE_KEY, "");
        }
        return key;
    }


    public void clearLocalData() {
        setKey("");
        setUser(null);
    }


    private void strictMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }

}