package tv.kuainiu;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.preferences.UserPreferencesManager;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.AppUtils;
import tv.kuainiu.utils.CrashHandler;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.FileUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.SDCardUtils;
import tv.kuainiu.utils.SecurityUtils;
import tv.kuainiu.utils.StringUtils;


public class MyApplication extends android.support.multidex.MultiDexApplication {
    private static final String TAG = "MyApplication";
    private static User user;
    public static final boolean IsDegbug = true;
    public static final String KEY_DEVICEID = "deviceid_key";
    /**
     * <p>
     * 用户被引导关注的老师列表
     * </p>
     */
    public static String followTeaCherIds = "";

    private static MyApplication mApplication;

    private String device;
    private static String key;


    // MyApplication初始化
    public static synchronized MyApplication getInstance() {
        return mApplication;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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


//            if (IsDegbug) {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                    LeakCanary.install(this);                       // 监控内存溢出
//                }
//            }

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
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        ImageLoader.getInstance().init(configuration);

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
        MyApplication.user = user;
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
        MyApplication.key = key;
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
