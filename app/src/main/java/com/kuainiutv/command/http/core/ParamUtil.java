package com.kuainiutv.command.http.core;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.kuainiutv.IGXApplication;
import com.kuainiutv.command.http.Api;
import com.kuainiutv.modle.User;
import com.kuainiutv.modle.cons.Constant;
import com.kuainiutv.util.DebugUtils;
import com.kuainiutv.util.SecurityUtils;
import com.kuainiutv.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网络请求参数工具
 *
 * @author nanck  2016-03-08
 */
public final class ParamUtil {
    // 只允许静态访问
    private ParamUtil() {
    }


    private static String getSign(Map<String, String> map, String key) {
        StringBuilder sb = new StringBuilder();
        List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        for (Map.Entry<String, String> o : list) {
            if (o != null && !TextUtils.isEmpty(StringUtils.replaceNullToEmpty(o.getValue()))) {
                DebugUtils.dd(o.getKey() + "===" + o.getValue());
                sb.append(o.getKey()).append("=").append(o.getValue());
            }
        }
        sb.append(key);
        String data = sb.toString();
        String sign = SecurityUtils.MD5.getMD5(SecurityUtils.MD5.getMD5(data));
        return sign.toLowerCase();
    }

    private static String getSign(Map<String, String> map) {
        return getSign(map, getKey());
    }


    public static String getParam(Map<String, String> map, String key, String uid) {
        if (null == map) {
            map = new HashMap<>();
        }
        map.put(Constant.PARAM_KEY_TIME, String.valueOf(System.currentTimeMillis()));
        map.put(Constant.PARAM_KEY_UID, uid);
        String sign = getSign(map);
        map.put(Constant.PARAM_KEY_SIGN, sign);
        String json = new Gson().toJson(map);
        return SecurityUtils.DESUtil.en(key, json).trim();
    }

    public static String getParam(@Nullable Map<String, String> map) {
        return getParam(map, getKey(), getUid());
    }


    /**
     * 构造 GET 请求参数
     * <p/>
     * 构造只需要传入一个的 GET 请求参数，如果需要传入多个参数@see{@link #getParam(Map, String, String)}函数。
     *
     * @param key   参数名
     * @param value 参数值
     * @return Such as: format --> ?x=y
     */
    public static String getParamForGet(String key, String value) {
        return "?".concat(key).concat("=").concat(value);
    }

    /**
     * 构造 GET 请求参数
     * <p/>
     *
     * @param map 参数键值对
     * @return Such as: format --> ?[x=y]
     */
    public static String getParamForGet(Map<String, Object> map) {
        if (null == map || map.size() < 1) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        StringBuilder sb = new StringBuilder("?");
        for (Map.Entry<String, Object> o : map.entrySet()) {
            sb.append(o.getKey())
                    .append("=")
                    .append(o.getValue())
                    .append("&");
            DebugUtils.dd(o.getKey() + " -- " + o.getValue());
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }


    /**
     * 获取私钥
     *
     * @return key
     */
    public static String getKey() {
        String key = IGXApplication.getKey();
        DebugUtils.dd("private key 1 : " + key);
        if (TextUtils.isEmpty(key)) {
            key = Api.PUBLIC_KEY;
            DebugUtils.dd("private key 2 : " + key);
            return key;
        }

        DebugUtils.dd("private key 3 : " + key);
        key = SecurityUtils.DESUtil.de(Api.PUBLIC_KEY, key);
        DebugUtils.dd("private key 4 : " + key);
        return key;
    }

    /**
     * 获取 uid
     *
     * @return
     */
    private static String getUid() {
        User user = IGXApplication.getUser();
        String uid = (null == user || TextUtils.isEmpty(user.getUser_id())) ? "0" : user.getUser_id();
        DebugUtils.dd(uid);
        return uid;
    }
}
