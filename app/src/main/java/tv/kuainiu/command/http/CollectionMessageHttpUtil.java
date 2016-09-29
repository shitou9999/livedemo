package tv.kuainiu.command.http;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import tv.kuainiu.command.http.core.CacheConfig;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;

/**
 * 收藏相关请求
 */
public class CollectionMessageHttpUtil {


    /**
     * 2.6 添加收藏
     *
     * 业务参数
     * news_id     文章ID     必传
     * news_type     文章类型 必传  区分收藏类型 1文章 2视频 3声音 11活动消息     默认1
     * 备注：需要签名跟加密
     */
    public static void addCollect(Context context, String news_id, String type) {
        Map<String, String> map = new HashMap();
        map.put("news_id", news_id);
        map.put("news_type", type);
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST, Api.ADD_COLLECT, ParamUtil.getParam(map), Action.add_collect);
    }

    /**
     * 2.7 取消收藏   sns/del_collect
     * 取消收藏
     * 固定参数  user_id, time ,sign
     * 业务参数
     * news_id     文章ID     必传   多个以英文逗号隔开
     * news_type     文章类型 必传  区分收藏类型 1文章 2视频 3声音 11活动消息     默认1
     * 备注：需要签名跟加密
     * {"status"="0","data"="json"}
     */
    public static void delCollect(Context context, String news_id, String type) {
        Map<String, String> map = new HashMap();
        map.put("news_id", news_id);
        map.put("news_type", type);
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST, Api.DEL_COLLECT, ParamUtil.getParam(map), Action.del_collect);
    }

    /**
     * 2.8 用户收藏列表   sns/collect_list
     * <p>
     * 固定参数  user_id, time ,sign
     * 业务参数
     * page          页面          非必传     默认1
     * size           每页条数     非必传     默认20
     * 备注：需要签名跟加密
     * {"status"="0","data"="json"}
     */
    public static void collectList(Context context, String mId, String type, int page) {
        Map<String, String> map = new HashMap();
        map.put("page", String.valueOf(page));
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST, Api.COLLECT_LIST, ParamUtil.getParam(map), Action.collect_list);
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
