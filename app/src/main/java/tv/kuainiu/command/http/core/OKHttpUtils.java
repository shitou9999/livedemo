package tv.kuainiu.command.http.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import tv.kuainiu.MyApplication;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.CacheManage;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.MeasureUtil;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.SecurityUtils;

/**
 * Created by guxuan on 2016/2/29.
 */
public class OKHttpUtils {
    private static final String TAG = "OKHttpUtils";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient mClient = new OkHttpClient();
    private static OKHttpUtils mInstance;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(10000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(50000, TimeUnit.MILLISECONDS);
        mClient = builder.build();
    }

    private OKHttpUtils() {
    }

    public static OKHttpUtils getInstance() {
        if (null == mInstance) {
            synchronized (OKHttpUtils.class) {
                if (null == mInstance) {
                    mInstance = new OKHttpUtils();
                }
            }
        }
        return mInstance;
    }


    public void syncGet(final Context context, String host, final String url, final Action action, final CacheConfig cacheConfig) {
        String urlGet = host + url;
        DebugUtils.dd("url :\n=======================================================\n" +
                urlGet +
                "\n=======================================================");
        if (!NetUtils.isOnline(context)) {
            if (cacheConfig != null && cacheConfig.isCached()) {
                String cacheFileName = SecurityUtils.MD5.getMD5(url);
                if (cacheConfig.isCacheFirstPage()) {
                    cacheFileName = SecurityUtils.MD5.getMD5(url + action.toString() + 1);
                } else {
                    cacheFileName = SecurityUtils.MD5.getMD5(url + action.toString() + cacheConfig.getPageNumber());
                }
                String data = CacheManage.getInstance().readStringObject(context, cacheFileName);
                LogUtils.i("catchString", url + "========,data=" + data);
                if (data != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(data);
                        EventBus.getDefault().post(new HttpEvent(action, Constant.SUCCEED, jsonObject, "请求缓存成功"));
                    } catch (JSONException e) {
                        LogUtils.e("catchString", "data JSON 解析异常" + data, e);
                        EventBus.getDefault().post(new HttpEvent(action, Constant.ERROR));
                        e.printStackTrace();
                    }
                } else {
                    EventBus.getDefault().post(new HttpEvent(action, Constant.NETWORK_ERROR));
                }
            } else {
                EventBus.getDefault().post(new HttpEvent(action, Constant.NETWORK_ERROR));
            }
            return;
        }
        Request request = new Request.Builder()
                .url(urlGet)
                .addHeader("User-Agent", testUserAgent(context))
                .get()
                .build();
        mClient.newCall(request).enqueue(new OKHttpCallBackListener() {
            @Override
            public void onSucceed(int code, String msg, JSONObject data) {
                DebugUtils.dd(data.toString());
                if (cacheConfig != null && cacheConfig.isCached()) {
                    if (cacheConfig.isCacheFirstPage()) {
                        if (cacheConfig.getPageNumber() == 1) {
                            CacheManage.getInstance().saveStringObject(context, data.toString(), SecurityUtils.MD5.getMD5(url + action.toString() + cacheConfig.getPageNumber()));
                        }
                    } else {
                        CacheManage.getInstance().saveStringObject(context, data.toString(), SecurityUtils.MD5.getMD5(url + action.toString() + cacheConfig.getPageNumber()));
                    }
                }
                EventBus.getDefault().post(new HttpEvent(action, code, data, msg));
            }

            @Override
            public void onFailure(int code, String msg) {
                EventBus.getDefault().post(new HttpEvent(action, code, msg));
            }

            @Override
            public void onNull(int code) {
                EventBus.getDefault().post(new HttpEvent(action, code));
            }
        });

    }

    public void syncGet(Context context, String url, final Action action) {
        syncGet(context, Api.TEST_DNS_API_HOST, url, action, null);
    }

    public void syncGet(Context context, String url, final Action action, CacheConfig cacheConfig) {
        syncGet(context, Api.TEST_DNS_API_HOST, url, action, cacheConfig);
    }


    public void syncPost(Context context, String host, String url, String param, Callback callback) {
        url = host + url;
        DebugUtils.dd("url :\n=======================================================\n" +
                url +
                "\n=======================================================");
        RequestBody body = new FormBody.Builder().add("data", param).build();
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", testUserAgent(context))
                .post(body)
                .build();
        mClient.newCall(request).enqueue(callback);
    }

    public void post(final Context context, String host, final String url, final String param, final Action action, final CacheConfig cacheConfig) {
        String urlPost = host.concat(url);
        DebugUtils.dd("url :\n=======================================================\n" +
                urlPost +
                "\n=======================================================");
        DebugUtils.dd("param :\n=======================================================\n" +
                param +
                "\n=======================================================");
        if (!NetUtils.isOnline(context)) {
            if (cacheConfig != null && cacheConfig.isCached()) {
                String cacheFileName = SecurityUtils.MD5.getMD5(url);
                if (cacheConfig.isCacheFirstPage()) {
                    cacheFileName = SecurityUtils.MD5.getMD5(url + action.toString() + 1);
                } else {
                    cacheFileName = SecurityUtils.MD5.getMD5(url + action.toString() + cacheConfig.getPageNumber());
                }
                String data = CacheManage.getInstance().readStringObject(context, cacheFileName);
                LogUtils.i("catchString", url + "========,data=" + data);
                if (cacheConfig != null && data != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(data);
                        EventBus.getDefault().post(new HttpEvent(action, Constant.SUCCEED, jsonObject, "请求缓存成功"));
                    } catch (JSONException e) {
                        LogUtils.e("catchString", "data JSON 解析异常" + data, e);
                        EventBus.getDefault().post(new HttpEvent(action, Constant.ERROR));
                        e.printStackTrace();
                    }
                } else {
                    EventBus.getDefault().post(new HttpEvent(action, Constant.NETWORK_ERROR));
                }
            } else {
                EventBus.getDefault().post(new HttpEvent(action, Constant.NETWORK_ERROR));
            }
            return;
        }
        RequestBody body = new FormBody.Builder().add("data", param).build();
        Request request = new Request.Builder()
                .url(urlPost)
                .header("User-Agent", testUserAgent(context))
                .post(body)
                .build();
        mClient.newCall(request).enqueue(new OKHttpCallBackListener() {
            @Override
            public void onSucceed(int code, String msg, JSONObject data) {
                if (cacheConfig != null && cacheConfig.isCached()) {
                    if (cacheConfig.isCacheFirstPage()) {
                        if (cacheConfig.getPageNumber() == 1) {
                            CacheManage.getInstance().saveStringObject(context, data.toString(), SecurityUtils.MD5.getMD5(url + action.toString() + cacheConfig.getPageNumber()));
                        }
                    } else {
                        CacheManage.getInstance().saveStringObject(context, data.toString(), SecurityUtils.MD5.getMD5(url + action.toString() + cacheConfig.getPageNumber()));
                    }
                }

                EventBus.getDefault().post(new HttpEvent(action, code, data, msg));
            }

            @Override
            public void onFailure(int code, String msg) {
                EventBus.getDefault().post(new HttpEvent(action, code, msg));
            }

            @Override
            public void onNull(int code) {
                EventBus.getDefault().post(new HttpEvent(action, code));
            }
        });
    }

    public void post(Context context, String url, String param, Action action) {

        post(context, Api.TEST_DNS_API_HOST, url, param, action, null);
    }

    public void post(final Context context, String host, final String url, final String param, final Action action) {
        post(context, host, url, param, action, null);
    }

    public void post(Context context, String url, String param, Action action, CacheConfig cacheConfig) {

        post(context, Api.TEST_DNS_API_HOST, url, param, action, cacheConfig);
    }

    public void post(Context context, String url, String param) {
        post(context, Api.TEST_DNS_API_HOST, url, param, Action.normal, null);
    }

    public void post(Context context, String url, String param, CacheConfig cacheConfig) {
        post(context, Api.TEST_DNS_API_HOST, url, param, Action.normal, cacheConfig);
    }

    public static String testUserAgent(Context context) {
        StringBuilder sb = new StringBuilder("iguxuan ");
        sb.append(Utils.getAppVersionName(context));
        sb.append("(");
        sb.append(Constant.PUSH_TYPE_ANDROID);
        sb.append(" -- ");
        sb.append(Utils.getDevice(context));
        sb.append(" ");
        sb.append(MeasureUtil.getScreenSize(context)[1] + " * " + MeasureUtil.getScreenSize(context)[0]);
        sb.append(";");
        sb.append(Build.MANUFACTURER.concat("_").concat(Build.MODEL));
        sb.append(" sdk:");
        sb.append(Build.VERSION.SDK_INT);
        sb.append(";");
        sb.append(Utils.getLanguageAndLocal());
        sb.append(";");
        sb.append("device ");
        sb.append(MyApplication.getInstance().getDeviceId());
        sb.append(")");
        DebugUtils.d(TAG, "testUserAgent device : " + sb.toString());
        return sb.toString();
    }


    public static class Utils {
        /**
         * 返回当前程序版本名
         */
        public static String getAppVersionName(Context context) {
            String versionName = "";
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                versionName = pi.versionName;
                if (versionName == null || versionName.length() <= 0) {
                    return "";
                }
            } catch (Exception e) {
                DebugUtils.e("VersionInfo Exception", e.getMessage());
            }
            return versionName;
        }

        public static String getLanguageAndLocal() {
            Locale locale = Locale.getDefault();
            return locale.getLanguage() + "_" + locale.getCountry();
        }


        public static String getDeviceNumber() {
            SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd:hh-mm-ss", Locale.CHINA);
            Random random = new Random(10000000L);
            String key1 = format.format(System.currentTimeMillis());
            String key2 = Build.MODEL;
            String key3 = String.valueOf(random.nextLong());
            String result = SecurityUtils.MD5.getMD5(key1 + "zyabxwcd" + key2 + key3 + "0123456789abcdefg");
            return result;
        }


        /**
         * 判断设备类型
         *
         * @param context
         * @return true:平板,false:手机
         */
        public static boolean isTabletDevice(Context context) {
            return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                    Configuration.SCREENLAYOUT_SIZE_LARGE;
        }

        public static String getDevice(Context context) {
            return isTabletDevice(context) ? "Pad" : "Phone";
        }


    }
}
