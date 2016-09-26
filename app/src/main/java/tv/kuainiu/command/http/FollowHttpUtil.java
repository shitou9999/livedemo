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
public class FollowHttpUtil {
    /**
     * 2.11 用户关注列表   sns/follow_list     圈子 - 名师观点 - 名师
     * <p>
     * 固定参数  user_id, time ,sign
     * 业务参数
     * user_id     当前用户ID     必传
     * is_all         是否返回全部      非必传   0否1是   默认0 默认只返回关注的老师列表
     * is_official   是否返回官方栏目     非必传     0否1是     默认0
     * 备注：需要签名跟加密
     * {"status"="0","data"="json"}
     */
    public static void followList(Context context, String is_all, String is_official) {
        Map<String, String> map = new HashMap<>();
        map.put("is_all", is_all);
        map.put("is_official", is_official);
        String param = ParamUtil.getParam(map);
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST, Api.FOLLOW_LSIT, param, Action.follow_list);

    }
    /**
     * 关注老师
     * TeacherHttpUtil.addFollowForTeacherID();
     */
    /**
     * 取消关注
     * TeacherHttpUtil.delFollowForTeacherId();
     */


}
