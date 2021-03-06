package tv.kuainiu.command.http;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import tv.kuainiu.MyApplication;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.utils.StringUtils;

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
        String user_id = "";
        if (MyApplication.getUser() != null) {
            user_id = StringUtils.replaceNullToEmpty(MyApplication.getUser().getUser_id());
        }
        map.put("user_id", user_id);
//        OKHttpUtils.getInstance().syncGet(context, Api.LIVE_INDEX + ParamUtil.getParamForGet(map), action, CacheConfig.getCacheConfig());
        OKHttpUtils.getInstance().syncGet(context, Api.LIVE_INDEX + ParamUtil.getParamForGet(map), action);
    }

    /**
     * @param context
     * @param type    类型 1为直播看盘 2直播预告 3直播回放
     * @param action
     */
    public static void liveIndex(Context context, String type, int page, int size, Action action) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("page", String.valueOf(page));
        map.put("size", String.valueOf(size));
//        OKHttpUtils.getInstance().syncGet(context, Api.LIVE_INDEX + ParamUtil.getParamForGet(map), action, CacheConfig.getCacheConfig());
        OKHttpUtils.getInstance().syncGet(context, Api.LIVE_INDEX + ParamUtil.getParamForGet(map), action);
    }
    public static void liveHomeIndex(Context context, String type, int page, Action action) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("page", String.valueOf(page));
        map.put("user_id", MyApplication.getUser()==null?"":MyApplication.getUser().getUser_id());
//        OKHttpUtils.getInstance().syncGet(context, Api.LIVE_INDEX + ParamUtil.getParamForGet(map), action, CacheConfig.getCacheConfig());
        OKHttpUtils.getInstance().syncGet(context, Api.index_recom_live + ParamUtil.getParamForGet(map), action);
    }

    public static void fetchLiveInfo(Context context,String teacher_id,String user_id,String live_id, Action action) {
        Map<String, Object> map = new HashMap<>();
        map.put("teacher_id",teacher_id);
        map.put("user_id",user_id);
        map.put("live_id", live_id);
        OKHttpUtils.getInstance().syncGet(context, Api.live_info + ParamUtil.getParamForGet(map), action);
    }
    public static void check_permission(Context context,String roomid,String viewername,String viewertoken, Action action) {
        Map<String, Object> map = new HashMap<>();
        map.put("roomid",roomid);
        map.put("viewername",viewername);
        map.put("viewertoken", viewertoken);
        OKHttpUtils.getInstance().syncGet(context, Api.self_check_permission + ParamUtil.getParamForGet(map), action);
    }
}
