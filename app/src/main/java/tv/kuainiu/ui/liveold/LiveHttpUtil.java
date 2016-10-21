package tv.kuainiu.ui.liveold;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;

import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.CacheConfig;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;

/**
 * 直播网络请求工具类
 * @author nanck on 2016/7/7.
 */
public class LiveHttpUtil {
    /**
     * 获取直播列表
     *
     * @param context nil
     */
    public static void fetchLiveList(Context context) {
        CacheConfig cacheConfig = new CacheConfig();
        OKHttpUtils.getInstance()
                .syncGet(context, Api.TEST_DNS_API_HOST_V21, Api.LIVE_LIST, Action.live_fetch_live_list, cacheConfig);
    }

    /**
     * 获取直播房间信息
     *
     * @param context nil
     */
    public static void fetchRoomInfo(Context context) {
        CacheConfig cacheConfig = new CacheConfig();
        OKHttpUtils.getInstance()
                .syncGet(context, Api.TEST_DNS_API_HOST_V21, Api.LIVE_ROOM_INFO, Action.live_fetch_room_info, cacheConfig);
    }


    /**
     * 获取正在直播的视频的相关信息
     *
     * @param context nil
     */
    public static void fetchLiveNowTopInfo(Context context) {
        CacheConfig cacheConfig = new CacheConfig();
        OKHttpUtils.getInstance()
                .syncGet(context, Api.TEST_DNS_API_HOST, Api.LIVE_LIVEING, Action.live_fetch_living_info, cacheConfig);
    }

    /**
     * 直播点赞
     *
     * @param context nil
     * @param id      直播 id
     */
    public static void executeAddLike(Context context, String id) {
        CacheConfig cacheConfig = new CacheConfig();
        String param = ParamUtil.getParamForGet(Constant.KEY_ID, id);
        OKHttpUtils.getInstance()
                .syncGet(context, Api.TEST_DNS_API_HOST_V21, Api.LIVE_LIKE + param, Action.live_add_like, cacheConfig);
    }


    /**
     * 获取往期视频
     *
     * @param context nil
     */
    public static void fetchLiveHistory(Context context, int page) {
        CacheConfig cacheConfig = new CacheConfig();
        Map<String, Object> map = new HashMap<>();
        map.put(Constant.KEY_PAGE, page);
        map.put(Constant.KEY_SIZE, Constant.REQUEST_SIZE);
        String param = ParamUtil.getParamForGet(map);
        OKHttpUtils.getInstance()
                .syncGet(context, Api.TEST_DNS_API_HOST_V21, Api.LIVE_HISTORY + param, Action.live_history, cacheConfig);
    }


    /**
     * 回放次数+1。用于后台统计
     *
     * @param context nil
     * @param id      回放视频 id
     */
    public static void historyCountPlusOne(Context context, String id) {
        CacheConfig cacheConfig = new CacheConfig();
        String param = ParamUtil.getParamForGet(Constant.KEY_ID, id);
        OKHttpUtils.getInstance().syncGet(
                context, Api.TEST_DNS_API_HOST_V21, Api.LIVE_HISTORY_PLUS_ONE + param, Action.live_history_plus_one, cacheConfig);
    }
}
