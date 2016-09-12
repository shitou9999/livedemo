package com.kuainiutv.umeng;

import android.content.Context;
import android.support.annotation.NonNull;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * UMENG 自定义事件管理类
 * <p>
 * 建议参考友盟SDK文档 <a href='http://dev.umeng.com/analytics/android-doc/integration#3'>自定义事件的统计</a>
 * </p>
 *
 * @author nanck on 2016/5/24.
 */
public final class UMEventManager {
    public static final String ID_COLLECTION = "collection";
    public static final String ID_DOWNLOAD = "download";
    public static final String ID_FEEDBACK = "feedback";
    public static final String ID_VIEW_ARTICLE = "viewArticle";
    public static final String ID_SHARE = "share";

    /**
     * 添加计算事件
     *
     * @param context nil
     * @param id      事件ID
     */
    public static void onEvent(@NonNull Context context, @NonNull String id) {
        MobclickAgent.onEvent(context, id);
    }


    /**
     * 添加计算事件
     *
     * @param context nil
     * @param id      事件ID
     * @param map     当前事件的属性和取值
     */
    public static void onEvent(@NonNull Context context, @NonNull String id, HashMap<String, String> map) {
        MobclickAgent.onEvent(context, id, map);
    }

    /**
     * 添加计数事件
     *
     * @param context nil
     * @param id      事件ID
     * @param map     当前事件的属性和取值
     * @param value   当前事件的数值
     */
    public static void onEventValue(@NonNull Context context, @NonNull String id, HashMap<String, String> map, int value) {

    }
}
