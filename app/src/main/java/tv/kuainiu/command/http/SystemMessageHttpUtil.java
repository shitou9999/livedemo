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
public class SystemMessageHttpUtil {

    /**
     * 消息首页
     *
     * @param context
     * @param action
     */
    public static void fetcherMessageHomeList(Context context, Action action) {
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.MESSAGE_INDEX, ParamUtil.getParam(null), action);
    }

    /**
     * 股轩助手消息接口
     *
     * @param context
     * @param builder
     * @param action
     */
    public static void fetcherGxHelperList(Context context, ParamBuilder builder, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(builder.page));
        map.put("size", String.valueOf(builder.size));
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.OFFICIAL_NEWS_LIST, ParamUtil.getParam(map), action, CacheConfig.getCacheConfig(builder.page));
    }

    /**
     * 股轩评论消息接口
     *
     * @param context
     * @param builder
     * @param action
     */
    public static void fetcherCommentList(Context context, ParamBuilder builder, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(builder.page));
        map.put("size", String.valueOf(builder.size));
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.COMMENT_MESSAGE_LIST, ParamUtil.getParam(map), action, CacheConfig.getCacheConfig(builder.page));
    }
    /**
     * 股轩活动消息详情接口
     *1.6.1 活动消息详情页 （V2)
     * @param context
     * @param builder
     * @param action
     */
    public static void fetcherActivityDetail(Context context, ParamBuilder builder, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(builder.page));
        map.put("size", String.valueOf(builder.size));
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.ACTIVITY_INFO, ParamUtil.getParam(map), action, CacheConfig.getCacheConfig(builder.page));
    }
    /**
     * 1.6股轩活动消息列表接口
     *1.6 活动消息列表 （V2)
     * @param context
     * @param builder
     * @param action
     */
    public static void fetcherActivityList(Context context, ParamBuilder builder, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(builder.page));
        map.put("size", String.valueOf(builder.size));
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.ACTIVITY_LIST, ParamUtil.getParam(map), action, CacheConfig.getCacheConfig(builder.page));
    }

    /**
     * 系统消息接口
     *
     * @param context
     * @param builder
     * @param action
     */
    public static void fetcherSystemMessageList(Context context, ParamBuilder builder, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(builder.page));
        map.put("size", String.valueOf(builder.size));
        OKHttpUtils.getInstance().post(context, Api.SYSTEM_MSG_LIST, ParamUtil.getParam(map), action);
    }

    /**
     * 系统消息详情
     *
     * @param context
     * @param builder
     * @param action
     */
    public static void fetcherSystemMessageDetail(Context context, ParamBuilder builder, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("message_id", builder.message_id);
        OKHttpUtils.getInstance().post(context, Api.SYSTEM_MESSAGE_DETAIL, ParamUtil.getParam(map), action);
    }


    /**
     * 2.13 系统消息列表   sns/system_msg_list
     * <p/>
     * 固定参数  user_id, time ,sign
     * 业务参数
     * page          页面          非必传     默认1
     * size           每页条数     非必传     默认20
     * <p/>
     * 备注：需要签名跟加密
     * {"status"="0","data"="json"}
     */
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
