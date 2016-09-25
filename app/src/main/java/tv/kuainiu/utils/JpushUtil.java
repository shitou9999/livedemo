package tv.kuainiu.utils;

import android.content.Context;
import android.text.TextUtils;

import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nanck on 2016/4/15.
 */
public class JpushUtil {

    /**
     * 上传极光推送id
     *
     * @param pushId id
     */
    public static void updoadPushId(Context context, String pushId) {
        if (TextUtils.isEmpty(pushId)) {
            DebugUtils.dd("push id is null");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("type", Constant.PUSH_TYPE_ANDROID);
        map.put("push_regid", pushId);
        OKHttpUtils.getInstance().post(context,Api.TEST_DNS_API_HOST_V2, Api.UPLOAD_PUSH_ID, ParamUtil.getParam(map), Action.upload_push_id);
    }
}
