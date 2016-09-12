package com.kuainiutv.command.http;

import android.content.Context;
import android.os.Build;

import com.kuainiutv.command.http.core.OKHttpUtils;
import com.kuainiutv.command.http.core.ParamUtil;
import com.kuainiutv.modle.cons.Action;
import com.kuainiutv.modle.cons.Constant;
import com.kuainiutv.util.AppUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jack on 2016/5/30.
 */
public class CommentHttpUtil {
    /**
     * 发表评论
     */
    public static void addComment(Context context, String mId, String mCatId, String content, String parentId, String nickname, String isReply) {
        Map<String, String> map = new HashMap<>();
        map.put(Constant.KEY_ID, mId);
        map.put(Constant.KEY_CATID, mCatId);
        map.put("nick_name", nickname);
        map.put("comment_id", parentId);
        map.put("content", content);
        map.put("is_reply", isReply);
        map.put("source", Constant.PUSH_TYPE_ANDROID);//来源
        map.put("system_version", Build.VERSION.RELEASE);//操作系统版本号
        map.put("model", Build.MODEL);//手机型号
        map.put("app_version", String.valueOf(AppUtils.getAppVersionCode(context)));//APP版本
        String param = ParamUtil.getParam(map);
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.ADD_COMMENT, param, Action.add_comment);
    }
}
