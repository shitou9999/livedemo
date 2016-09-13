package tv.kuainiu.command.http;

import android.content.Context;

import tv.kuainiu.command.http.core.CacheConfig;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jack on 2016/4/22.
 */
public class CollectionMessageHttpUtil {


/**
     * 2.1 添加收藏
     * <p/>
     * news_id     文章ID   必传
     * news_type   文章类型  1视频 2新闻 3活动     默认1
     * 备注：需要签名跟加密
     */
    public static void addCollect(Context context, String mId, String type, Action action) {
        Map<String, String> map = new HashMap();
        map.put("news_id", mId);
        map.put("news_type", type);
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.ADD_COLLECT, ParamUtil.getParam(map), action);
    }

    /**
     * 取消收藏
     */
    public static void delCollect(Context context, String mId, String type, Action action) {
        Map<String, String> map = new HashMap();
        map.put("news_id", mId);
        map.put("news_type", type);
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.DEL_COLLECT, ParamUtil.getParam(map), action);
    }

    /**
     * 1.14 用户活动收藏列表 （V2)
     *
     * @param context
     * @param builder
     * @param action
     */
    public static void fetcherActivityCollectList(Context context, ParamBuilder builder, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(builder.page));
        map.put("size", String.valueOf(builder.size));
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.ACTIVITY_COLLECT_LIST, ParamUtil.getParam(map), action, CacheConfig.getCacheConfig(builder.page));
    }

    public static class ParamBuilder {
        public int page = 1;
        public int size = Constant.CUSTOM_CONTENT_SIZE;
        public String message_id;

        /**
         * 列表参数
         *
         * @param page
         */
        public ParamBuilder(int page) {
            this.page = page;
            this.size = size;
        }

        /**
         * 详情参数
         *
         * @param message_id
         */
        public ParamBuilder(String message_id) {
            this.message_id = message_id;
        }
    }
}
