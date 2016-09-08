package com.iguxuan.iguxuan_friends.command.http.core;


import com.iguxuan.iguxuan_friends.IGXApplication;
import com.iguxuan.iguxuan_friends.modle.cons.Constant;
import com.iguxuan.iguxuan_friends.util.DebugUtils;
import com.iguxuan.iguxuan_friends.util.LogUtils;
import com.iguxuan.iguxuan_friends.util.StringUtils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 网络请求回调
 *
 * @author nanck 2016/03/14
 * @since OkHttp 3
 */
public abstract class OKHttpCallBackListener implements Callback {
    private static String TAG = "OKHttpCallBackListener";

    /**
     * 请求成功
     */
    public abstract void onSucceed(int code, String msg, JSONObject data);


    /**
     * 请求失败
     */
    public abstract void onFailure(int code, String msg);


    /**
     * 请求成功。无数据。
     *
     * @param code
     */
    public abstract void onNull(int code);

    /**
     * 请求被中断
     */
//    public abstract void onInterrupt();

    /**
     * 请求被取消(请求无效)
     */
//    public abstract void onCancel();
    @Override public void onFailure(Call call, IOException e) {
        LogUtils.e("onFailure", "请求失败", e);
        onFailure(Constant.NETWORK_ERROR, "请求失败");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        byte[] bytes = response.body().source().readByteArray();
        String data = new String(bytes);
        int code;
        try {
            if (IGXApplication.IsDegbug) {
                DebugUtils.dd("data : " + data);
            }
            JSONObject object = (JSONObject) new JSONTokener(data).nextValue();
            String temp = object.optString("status");
            code = Integer.parseInt(StringUtils.replaceBlank(temp));
            if (code == 0) {
                onSucceed(code, object.optString("msg"), object);
            } else {
                onFailure(code, object.optString("msg"));
            }
        } catch (Exception e) {
            LogUtils.e("onResponse", "data=" + data, e);
            onFailure(Constant.ERROR, "数据解析异常");
        } finally {
            // TODO: 2016/5/5 Finally stub.
        }
    }

}
