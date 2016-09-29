package tv.kuainiu.command.http;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.modle.cons.Action;

/**
 * 直播网络请求工具
 */
public class LiveHttpUtil {
    /**
     * @param context
     * @param type    类型 1为直播看盘 2直播预告 3直播回放
     * @param action
     */
    public static void liveIndex(Context context, String type, int page, Action action) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("page", String.valueOf(page));
//        OKHttpUtils.getInstance().syncGet(context, Api.LIVE_INDEX + ParamUtil.getParamForGet(map), action, CacheConfig.getCacheConfig());
        OKHttpUtils.getInstance().syncGet(context, Api.LIVE_INDEX + ParamUtil.getParamForGet(map), action);
    }
    /**
     * @param context
     * @param type    类型 1为直播看盘 2直播预告 3直播回放
     * @param action
     */
    public static void liveIndex(Context context, String type, int page,int size, Action action) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("page", String.valueOf(page));
        map.put("size", String.valueOf(size));
//        OKHttpUtils.getInstance().syncGet(context, Api.LIVE_INDEX + ParamUtil.getParamForGet(map), action, CacheConfig.getCacheConfig());
        OKHttpUtils.getInstance().syncGet(context, Api.LIVE_INDEX + ParamUtil.getParamForGet(map), action);
    }

}
