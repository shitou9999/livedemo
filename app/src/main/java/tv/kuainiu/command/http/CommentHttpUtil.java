package tv.kuainiu.command.http;

import android.content.Context;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;

import tv.kuainiu.MyApplication;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.AppUtils;

/**
 * 评论请求工具
 */
public class CommentHttpUtil {
    /**
     * 发表评论
     type          评论类型     必传      1是对文章的评论 2是对动态的评论
     nick_name     用户名     非必传
     news_id               文章ID     type为1时必传
     dynamics_id     动态ID     type为2时必传
     is_reply          非必传     是否为回复评论   否0是1
     comment_id  父评论ID      选填      如为回复的话，需传
     content      评论内容    必传
     key_id          评论消息列表的key_id
     reply_teacher         是否为回复老师的评论

     source     来源。pc,android,iphone,ipad
     model     手机型号
     system_version     操作系统版本
     app_version          APP版本

     */
    public static void addComment(Context context, String type, String nick_name,String news_id,String dynamics_id, String is_reply,
                                  String comment_id,String content, String key_id, String reply_teacher) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("nick_name", nick_name);
        map.put("news_id", news_id);
        map.put("dynamics_id", dynamics_id);
        map.put("is_reply", is_reply);
        map.put("comment_id", comment_id);
        map.put("content", content);
        map.put("key_id", key_id);
        map.put("reply_teacher", reply_teacher);

        map.put("source", Constant.PUSH_TYPE_ANDROID);//来源
        map.put("system_version", Build.VERSION.RELEASE);//操作系统版本号
        map.put("model", Build.MODEL);//手机型号
        map.put("app_version", String.valueOf(AppUtils.getAppVersionCode(context)));//APP版本
        String param = ParamUtil.getParam(map);
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST, Api.ADD_COMMENT, param, Action.add_comment);
    }
    /**
     * 热门评论列表
     type          评论类型     必传      1是对文章的评论 2是对动态的评论
     cat_id          栏目ID      type为1时必传
     news_id               文章ID     type为1时 必传
     dynamics_id     动态ID     type为2时必传
     page          页面          非必传     默认1
     size           每页条数     非必传     默认5

     */
    public static void hotComment(Context context, String type, String cat_id, String news_id, String dynamics_id, int page,Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("cat_id", cat_id);
        map.put("news_id", news_id);
        map.put("dynamics_id", dynamics_id);
        map.put("page",String.valueOf(page));
        String param = ParamUtil.getParam(map);
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST, Api.COMMENT_LIST_HOT, param,action);
    }
    /**
     * 评论列表
     type          评论类型     必传      1是对文章的评论 2是对动态的评论
     cat_id          栏目ID      type为1时必传
     news_id               文章ID     type为1时 必传
     dynamics_id     动态ID     type为2时必传
     page          页面          非必传     默认1
     size           每页条数     非必传     默认20
     just_teacher   是否仅老师的评论  非必传     是1否0

     */
    public static void commentList(Context context, String type, String cat_id, String news_id, String dynamics_id, int page, String just_teacher) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("cat_id", cat_id);
        map.put("news_id", news_id);
        map.put("dynamics_id", dynamics_id);
        map.put("page",String.valueOf(page));
        map.put("just_teacher", just_teacher);
        String param = ParamUtil.getParam(map);
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST, Api.COMMENT_LIST, param, Action.comment_list);
    }

    /**
     * 评论点赞
     device     设备号     必传
     id           评论id    必传
     */
    public static void commentList(Context context, String comment_id) {
        Map<String, String> map = new HashMap<>();
        map.put("device", MyApplication.getInstance().getDeviceId());
        map.put("comment_id", comment_id);
        String param = ParamUtil.getParam(map);
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST, Api.FAVOUR_COMMENT, param, Action.favour_comment);
    }
}
