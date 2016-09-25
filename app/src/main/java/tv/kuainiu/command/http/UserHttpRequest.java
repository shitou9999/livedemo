package tv.kuainiu.command.http;

import android.content.Context;
import android.os.Build;

import com.google.gson.Gson;
import tv.kuainiu.command.http.core.OKHttpCallBackListener;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.UserEvent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.AppUtils;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.SecurityUtils;
import tv.kuainiu.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by guxuan on 2016/3/10.
 */
public class UserHttpRequest {
    public static void initApp(Context context, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("ver", String.valueOf(AppUtils.getAppVersionCode(context)));//APP版本
        map.put("platform", "android");//平台类型
        map.put("platform_ver", Build.VERSION.RELEASE);//系统版本号
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.CLIENT_INIT, ParamUtil.getParam(map), action);
    }

    /**
     * 网页同步登陆接口，在本地用户状态是登陆的情况下使用
     *
     * @param context
     * @param user_id
     * @param phoneNumber
     * @param action
     */
    public static void authLogin(Context context, String user_id, String phoneNumber, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("phone", phoneNumber);
//        map.put("push_regid",)
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V21, Api.AUTH_LOGIN, ParamUtil.getParam(map), action);
    }

    public static void login(Context context, String area, String account, String password) {
        String param = prepareLoginParam(area, account, password);
        OKHttpUtils.getInstance().syncPost(context, Api.TEST_DNS_API_HOST_V21, Api.LOGIN, param, new OKHttpCallBackListener() {
            @Override
            public void onSucceed(int code, String msg, JSONObject data) {
                if (Constant.SUCCEED == code) {
                    DebugUtils.dd("user json data : " + data.toString());
                    User user = new Gson().fromJson(data.optString("data"), User.class);
                    DebugUtils.dd("user string : " + user.toString());
                    EventBus.getDefault().post(new UserEvent(code, msg, user));
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                EventBus.getDefault().post(new UserEvent(Constant.ERROR, msg, new User()));
            }

            @Override
            public void onNull(int code) {
                EventBus.getDefault().post(new UserEvent(Constant.ERROR, "没有找到用户信息,请检查您的账号再重试", new User()));
            }
        });
    }
/*    {"status"="0","data"="json"}
    -101 调用自动登录接口*/

    public static void qrCodeLogin(Context context, String session_id, String client_id, String connect_time, Action action){
        Map<String, String> map = new HashMap<>();
        map.put("session_id", session_id);
        map.put("client_id", client_id);
        map.put("connect_time", connect_time);
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V21, Api.QRCODE_LOGIN, ParamUtil.getParam(map), action);
    }

    private static String prepareLoginParam(String area, String account, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("area", StringUtils.replaceBlank(area));
        map.put("phone", StringUtils.replaceBlank(account));
        map.put("password", SecurityUtils.salt(StringUtils.replaceBlank(password))); // 密码加盐
        return ParamUtil.getParam(map);
    }


}
