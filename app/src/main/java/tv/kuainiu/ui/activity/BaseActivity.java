package tv.kuainiu.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.event.UserEvent;
import tv.kuainiu.modle.InitInfo;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.DialogUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.PreferencesUtils;

/**
 * Created by jack on 2016/9/7.
 */
public class BaseActivity extends AppCompatActivity {
    public String TAG = "";
    public static final String CLIENT_INIT_TIME = "client_init_time";
    public static final int CLIENT_INIT_NUMBER = 5;
    public static final int CLIENT_INIT_MINUTE = 5;
    public static int initClientNumbers = 0;
    public boolean isUpLoadRegistrationID = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Override protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRequestError(HttpEvent event) {
//        initClientPost(event.getCode(), this);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRequestError(UserEvent event) {
        initClientPost(event.getCode(), this);
    }

    @Override protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 初始化客户端数据
     *
     * @param code
     * @param context
     */
    public static void initClientPost(int code, Context context) {
//        LogUtils.d("重新初始化", "BaseActivity 捕获code： " + code);
        String requestTime = PreferencesUtils.getString(context, CLIENT_INIT_TIME, DateUtil.getCurrentDate());
        //初始化大于5分钟后可再次初始化
        if (DateUtil.minutesBetweenTwoDate(requestTime, DateUtil.getCurrentDate()) > CLIENT_INIT_MINUTE) {
            initClientNumbers = 0;
        }
//        initClientNumbers = 0;
        //连续初始化5次之后，再不成功就停止初始化
        if (initClientNumbers < CLIENT_INIT_NUMBER) {

            if (-1001 == code) {
                LogUtils.d("重新初始化", "BaseActivity 重新初始化：" + code);
                IGXApplication.getInstance().clearLocalData();
                if (initClientNumbers == 0) {
                    PreferencesUtils.putString(context, CLIENT_INIT_TIME, DateUtil.getCurrentDate());
                }
                initClientNumbers++;
            } else if (-1003 == code) {
                LogUtils.d("重新初始化", "BaseActivity 重新初始化（重新生成设备号）：" + code);
                if (initClientNumbers == 0) {
                    PreferencesUtils.putString(context, CLIENT_INIT_TIME, DateUtil.getCurrentDate());
                }
                IGXApplication.getInstance().setDeviceId("");
                IGXApplication.getInstance().clearLocalData();
                initClientNumbers++;
            } else if (-1004 == code) {
                LogUtils.d("系统错误", "-1004");
                if (IGXApplication.IsDegbug) {
//                    ToastUtils.showToast(context, "-1004");

                }
                initApp(context);
                return;
            } else if (-1002 == code) {
                offLine(context);
                return;
            } else {
                return;
            }
            if (!NetUtils.isOnline(context)) {
                return;
            }
            OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.CLIENT_INIT, ParamUtil.getParam(null), Action.client_init);
        }

    }

    public static void initApp(final Context context) {
        IGXApplication.setUser(null);
        EventBus.getDefault().post(new HttpEvent(Action.off_line, Constant.SUCCEED));
        DialogUtils.TipInitApp(context, "请点击按钮，初始化APP", new DialogUtils.IDialogButtonOnClickListener() {
            @Override
            public void cancel() {

            }

            @Override
            public void sure() {
                IGXApplication.getInstance().setDeviceId("");
                IGXApplication.getInstance().clearLocalData();
                OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.CLIENT_INIT, ParamUtil.getParam(null), Action.client_init);
            }
        });
    }

    public static void offLine(final Context context) {
        IGXApplication.setUser(null);
        EventBus.getDefault().post(new HttpEvent(Action.off_line, Constant.SUCCEED));
        DialogUtils.TipAccountConflict(context, "您的账号在其他设备上登陆.当前设备已下线", new DialogUtils.IDialogButtonOnClickListener() {
            @Override
            public void cancel() {

            }

            @Override
            public void sure() {
                JPushInterface.clearAllNotifications(context);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void OnEventUploadPushId(HttpEvent event) {
        if (Action.upload_push_id == event.getAction()) {
            DebugUtils.dd("push id : " + event.getMsg());
            if (Constant.SUCCEED == event.getCode()) {
                DebugUtils.dd("push id data : " + event.getData());
                isUpLoadRegistrationID = true;
            } else {
                isUpLoadRegistrationID = false;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientInitEvent(HttpEvent event) {
        if (Action.client_init == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                initClientNumbers = 0;//初始化成功后清空初始化计数
                LogUtils.d("重新初始化", "BaseActivity 重新初始化成功，结果：" + event.getData().toString());
                try {
                    String tempString = event.getData().optString("data");
                    JSONObject object = new JSONObject(tempString);
                    InitInfo initInfo = new Gson().fromJson(tempString, InitInfo.class);
                    PreferencesUtils.putString(this, Constant.COURSE_URL, initInfo.getKe_url());
                    DebugUtils.dd("init info obj : " + initInfo.toString());
                    String privateKey = initInfo.getPrivate_key();
                    if (!TextUtils.isEmpty(privateKey)) {
                        IGXApplication.setKey(privateKey);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.e("重新初始化", "BaseActivity 重新初始化成功，但结果解析异常" + event.getMsg(), e);
                }
            }
        }
    }

}
