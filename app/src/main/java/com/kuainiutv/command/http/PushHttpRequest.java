package com.kuainiutv.command.http;

import android.content.Context;

import com.kuainiutv.command.http.core.ParamUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guxuan on 2016/3/10.
 */
public class PushHttpRequest {
    private static String TAG = "PushHttpRequest";

    private PushHttpRequest() {
    }

    public static void run(Context context, String url, String param) {
    }


    public static void openPushForTeacher(Context context, String catId, String teacherId) {
        run(context, "sns/add_push_setting", prepareParam("1", catId, teacherId));
    }

    public static void openPushForColumn(Context context, String catId) {
        run(context, "sns/add_push_setting", prepareParam("2", catId, ""));
    }


    public static void cancelPushForTeacher(Context context, String catId, String teacherId) {
        run(context, "sns/del_push_setting", prepareParam("1", catId, teacherId));
    }

    public static void cancelPushForColumn(Context context, String catId) {
        run(context, "sns/add_push_setting", prepareParam("2", catId, ""));
    }


    public static String prepareParam(String type, String catId, String teacherId) {
        Map<String, String> map = new HashMap<>();
        map.put("catid", catId);
        map.put("teacher_id", teacherId);
        map.put("type", type);
        return ParamUtil.getParam(map);
    }

}
