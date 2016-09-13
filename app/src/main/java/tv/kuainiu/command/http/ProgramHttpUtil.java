package tv.kuainiu.command.http;

import android.content.Context;
import android.text.TextUtils;

import tv.kuainiu.command.http.core.CacheConfig;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nanck on 2016/4/13.
 */
public class ProgramHttpUtil {

    /**
     * 2.9 用户订阅列表   sns/subscibe_list
     * <p/>
     * 固定参数  user_id, time ,sign
     * 业务参数
     * is_all   非必传     是否调取所有节目    0否 只调取订阅的节目    1调取所有   默认0
     * 备注：需要签名跟加密
     * {"status"="0","data"="json"}
     * <p/>
     */

    public static void fetchProgramList(Context context, boolean isAll, Action action) {
        String value = isAll ? "1" : "0";
        Map<String, String> map = new HashMap<>();
        map.put("is_all", value);
        String param = ParamUtil.getParam(map);
        OKHttpUtils.getInstance().post(context, Api.SUBSCRIBER_LSIT, param, action, CacheConfig.getCacheConfig());
    }


    public static void addSubscriptionForCatId(Context context, String catId, Action action) {
        if (TextUtils.isEmpty(catId)) {
            throw new NullPointerException("Method add_SubscriptionForCatId the catId is null");
        }
        Map<String, String> map = new HashMap<>();
        map.put(Constant.KEY_CATID, catId);
        String param = ParamUtil.getParam(map);

        OKHttpUtils.getInstance().post(context, Api.ADD_SUBSCRIBER, param, action);
    }


    public static void delSubscriptionForCatId(Context context, String catId, Action action) {
        if (TextUtils.isEmpty(catId)) {
            throw new NullPointerException("Method del_SubscriptionForCatId the catId is null");
        }
        Map<String, String> map = new HashMap<>();
        map.put(Constant.KEY_CATID, catId);
        String param = ParamUtil.getParam(map);

        OKHttpUtils.getInstance().post(context, Api.DEL_SUBSCRIBER, param, action);
    }

}
