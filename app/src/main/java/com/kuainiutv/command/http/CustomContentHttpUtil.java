package com.kuainiutv.command.http;

import android.content.Context;

import com.kuainiutv.command.http.core.OKHttpUtils;
import com.kuainiutv.command.http.core.ParamUtil;
import com.kuainiutv.modle.cons.Action;
import com.kuainiutv.modle.cons.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nanck on 2016/4/13.
 */
public class CustomContentHttpUtil {

    public static void fetcherCustomContentList(Context context, ParamBuilder builder, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("type", builder.type);
        map.put("catid", builder.catid);
        map.put("page", String.valueOf(builder.page));
        map.put("size", String.valueOf(builder.size));
        map.put("is_official", String.valueOf(builder.size));
        map.put("last_time", String.valueOf(builder.size));
        OKHttpUtils.getInstance().post(context,Api.TEST_DNS_API_HOST_V2, Api.CUSTOM_CONTENT, ParamUtil.getParam(map), action);
    }


    /**
     * 2.10 用户关注列表   sns/follow_list
     * <p>
     * 固定参数  user_id, time ,sign
     * 业务参数
     * is_all   非必传     是否调取所有老师    0否 只调取关注的老师    1调取所有
     * catid   非必传     如果传了catid 会返回 该用户是否设置允许推送该栏目
     * is_official     非必传     是否需要调取出 "股轩官方"   用于"定制"界面   0否  1是  默认0
     * last_time      非必传     用户上一次请求该接口时间     默认0     有传值的话  会按照该值算 是否亮"定制"里面的关注老师 的红点
     * 备注：需要签名跟加密
     * {"status"="0","data"="json"}
     */
    public static class ParamBuilder {
        public String type = "0";
        public String catid = "";
        public int page = 1;
        public int size = Constant.CUSTOM_CONTENT_SIZE;
        private String is_official="1";
        private String last_time="0";

        public ParamBuilder(String type, String catid, int page, int size, String last_time) {
            this.type = type;
            this.catid = catid;
            this.page = page;
            this.size = size;
            this.last_time=last_time;
        }
    }
}
