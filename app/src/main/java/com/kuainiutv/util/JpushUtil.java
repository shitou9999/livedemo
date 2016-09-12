package com.kuainiutv.util;

import android.content.Context;
import android.text.TextUtils;

import com.kuainiutv.command.http.Api;
import com.kuainiutv.command.http.core.OKHttpUtils;
import com.kuainiutv.command.http.core.ParamUtil;
import com.kuainiutv.modle.cons.Action;
import com.kuainiutv.modle.cons.Constant;

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
