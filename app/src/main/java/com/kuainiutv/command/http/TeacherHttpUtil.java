package com.kuainiutv.command.http;

import android.content.Context;

import com.kuainiutv.command.http.core.CacheConfig;
import com.kuainiutv.command.http.core.OKHttpUtils;
import com.kuainiutv.command.http.core.ParamUtil;
import com.kuainiutv.modle.cons.Action;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nanck on 2016/4/13.
 */
public class TeacherHttpUtil {


    /**
     * 获取老师列表
     *
     * @param context
     * @param builder
     * @param action
     */
    public static void fetchTeacherList(Context context, ParamBuilder builder, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("is_all", builder.isAll);
        map.put("catid", builder.catId);
        map.put("is_official", builder.isOfficial);
        map.put("last_time", builder.lastTime);
        CacheConfig cacheConfig = new CacheConfig(true, true, 1, -1);
        OKHttpUtils.getInstance().post(context, Api.FETCH_FOLLOW_LIST, ParamUtil.getParam(map), action, CacheConfig.getCacheConfig());
    }


    /**
     * 获取老师详情
     *
     * @param context
     * @param builder
     * @param action
     */
    public static void fetchTeacherDetails(Context context, ParamBuilder builder, Action action) {
        Map<String, Object> map = new HashMap<>();
        map.put("teacherid", builder.teacherId);
        map.put("user_id", builder.userId);
        OKHttpUtils.getInstance().syncGet(context, Api.FETCH_TEAHCER_INFO + ParamUtil.getParamForGet(map), action, CacheConfig.getCacheConfig());
    }

    /**
     * 关注老师
     *
     * @param context
     * @param teacherId
     * @param action
     */
    public static void addFollowForTeacherID(Context context, String teacherId, Action action) {
        OKHttpUtils.getInstance().post(context, Api.ADD_FOLLOW, prepareFollowParam(teacherId), action);
    }


    /**
     * 一次关注多个老师 （v2）
     * teacher_id   老师ID 必传 多个用英文逗号隔开）
     *
     * @param context
     * @param teacherIds
     * @param action
     */
    public static void addFollowForTeacherList(Context context, String teacherIds, Action action) {
        OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.ADD_FOLLOW, prepareFollowParam(teacherIds), action);
    }

    /**
     * 取消关注
     *
     * @param context
     * @param teacherId
     * @param action
     */
    public static void delFollowForTeacherId(Context context, String teacherId, Action action) {
        OKHttpUtils.getInstance().post(context, Api.DEL_FOLLOW, prepareFollowParam(teacherId), action);
    }

    private static String prepareFollowParam(String teacherId) {
        Map<String, String> map = new HashMap<>();
        map.put("teacher_id", teacherId);
        String param = ParamUtil.getParam(map);
        return param;
    }


    /**
     * 参数构建器
     */
    public static class ParamBuilder {
        /**
         * 非必传   是否调取所有老师   || 0否 只调取关注的老师    1调取所有
         */
        private String isAll;
        /**
         * 非必传   如果传了catid 会返回 该用户是否设置允许推送该栏目
         */
        private String catId;
        /**
         * 非必传   是否需要调取出 "股轩官方"   用于"定制"界面     0否  1是  默认0
         */
        private String isOfficial;
        /**
         * 非必传   用户上一次请求该接口时间     默认0     有传值的话  会按照该值算 是否亮"定制"里面的关注老师 的红点
         */
        private String lastTime;

        /**
         * 必传   老师ID
         */
        private String teacherId;
        /**
         * 非必传  用于判断用户是否关注该老师
         */
        private String userId;


        /**
         * 用于获取老师列表
         *
         * @param isAll
         * @param catId
         * @param isOfficial
         * @param lastTime
         */
        public ParamBuilder(String isAll, String catId, String isOfficial, String lastTime) {
            this.isAll = isAll;
            this.catId = catId;
            this.isOfficial = isOfficial;
            this.lastTime = lastTime;
        }

        /**
         * 用于获取老师详情
         *
         * @param teacherId
         * @param userId
         */
        public ParamBuilder(String teacherId, String userId) {
            this.teacherId = teacherId;
            this.userId = userId;
        }

        public void addCatId(String catId) {
            this.catId = catId;
        }

        public void addIsAll(String isAll) {
            this.isAll = isAll;
        }

        public void addIsOfficial(String isOfficial) {
            this.isOfficial = isOfficial;
        }

        public void addLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        public void addTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }

        public void addUserId(String userId) {
            this.userId = userId;
        }
    }
}
