package tv.kuainiu.command.http;

import android.content.Context;
import android.os.Build;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    public static void login(Context context, String area, String account, String password, String third_uid, String third_type) {
        String param = prepareLoginParam(area, account, password, third_uid, third_type);
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
                EventBus.getDefault().post(new UserEvent(Constant.ERROR, "没有找到该用户信息,请检查您的账后重试", new User()));
            }
        });
    }

    public static void thirdLoginCheck(Context context, String type, String platform_token, String platform_id, String platform_nickname, String platform_avatar, long platform_expires_in, Action action) {
       /* 固定参数  user_id, time ,sign
        业务参数
        type     必传     类型 微博：wb  微信：wx  QQ：qq
        access_token     必传      微博token
        uid  必传    微博用户ID
        name     必传     微博昵称
        avatar     必传     微博头像
        expires_in     必传     微博授权有效期*/

        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("access_token", platform_token);
        map.put("uid", platform_id);
        map.put("name", platform_nickname);
        map.put("avatar", platform_avatar);
        map.put("expires_in", String.valueOf(platform_expires_in));
        OKHttpUtils.getInstance().post(context, Api.third_login, ParamUtil.getParam(map), action);
    }
    public static void thirdUnBindAccount(Context context, String type,Action action) {
       /* 固定参数  user_id, time ,sign
        业务参数
        type     必传     类型 微博：wb  微信：wx  QQ：qq
        access_token     必传      微博token
        uid  必传    微博用户ID
        name     必传     微博昵称
        avatar     必传     微博头像
        expires_in     必传     微博授权有效期*/

        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        OKHttpUtils.getInstance().post(context, Api.third_unbind, ParamUtil.getParam(map), action);
    }
/*    {"status"="0","data"="json"}
    -101 调用自动登录接口*/

    public static void qrCodeLogin(Context context, String session_id, String client_id, String connect_time, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("session_id", session_id);
        map.put("client_id", client_id);
        map.put("connect_time", connect_time);
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V21, Api.QRCODE_LOGIN, ParamUtil.getParam(map), action);
    }

    private static String prepareLoginParam(String area, String account, String password, String third_uid, String third_type) {
        Map<String, String> map = new HashMap<>();
        map.put("third_uid", StringUtils.replaceBlank(third_uid));
        map.put("third_type", StringUtils.replaceBlank(third_type));
        map.put("area", StringUtils.replaceBlank(area));
        map.put("phone", StringUtils.replaceBlank(account));
        map.put("password", SecurityUtils.salt(StringUtils.replaceBlank(password))); // 密码加盐
        return ParamUtil.getParam(map);
    }


}
