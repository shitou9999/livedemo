package tv.kuainiu.command.http;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.modle.cons.Action;

/**
 * 关注网络请求工具
 */
public class SupportHttpUtil {

    /**
     * 视频文章点赞  news/support_news
     */
    public static void supportVideoDynamics(Context context,String cat_id, String news_id) {
//        Map<String, String> map = new HashMap<>();
//        map.put("is_all", is_all);
//        map.put("is_official", is_official);
//        String param = ParamUtil.getParam(map);
//        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST, Api.FOLLOW_LSIT, param, Action.follow_list);
        Map<String, Object> map = new HashMap<>();
        map.put("news_id", news_id);
        map.put("cat_id", cat_id);
        OKHttpUtils.getInstance().syncGet(context, Api.VIDEO_OR_POST_FAVOUR + ParamUtil.getParamForGet(map), Action.video_favour);
    }
    /**
     * 文字动态点赞
     */
    public static void supportDynamics(Context context, String dynamics_id,Action action) {
//        Map<String, String> map = new HashMap<>();
//        map.put("is_all", is_all);
//        map.put("is_official", is_official);
//        String param = ParamUtil.getParam(map);
//        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST, Api.FOLLOW_LSIT, param, Action.follow_list);
        Map<String, Object> map = new HashMap<>();
        map.put("dynamics_id", dynamics_id);
        OKHttpUtils.getInstance().syncGet(context, Api.SUPPORT_DYNAMICS + ParamUtil.getParamForGet(map),action);
    }

}
