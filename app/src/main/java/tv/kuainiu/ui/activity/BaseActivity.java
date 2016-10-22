package tv.kuainiu.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.event.MessageEvent;
import tv.kuainiu.event.UserEvent;
import tv.kuainiu.modle.InitInfo;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.modle.cons.MessageType;
import tv.kuainiu.modle.push.ActivityMessage;
import tv.kuainiu.modle.push.AppointmentLive;
import tv.kuainiu.modle.push.NewsMessage;
import tv.kuainiu.modle.push.SystemMessage;
import tv.kuainiu.modle.push.VideoMessage;
import tv.kuainiu.ui.MainActivity;
import tv.kuainiu.ui.articles.activity.PostZoneActivity;
import tv.kuainiu.ui.liveold.PlayLiveActivity;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.ui.message.activity.MessageSystemActivity;
import tv.kuainiu.ui.teachers.activity.TeacherZoneActivity;
import tv.kuainiu.ui.video.VideoActivity;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.DialogUtils;
import tv.kuainiu.utils.ExampleUtil;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.StringUtils;

/**
 * Created by jack on 2016/9/7.
 */
public class BaseActivity extends AppCompatActivity {
    public String TAG = "";
    public static final String CLIENT_INIT_TIME = "client_init_time";
    public static final int CLIENT_INIT_NUMBER = 2;
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

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    //    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRequestError(HttpEvent event) {
//        initClientPost(event.getCode(), this);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRequestError(UserEvent event) {
        initClientPost(event.getCode(), this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!(this instanceof MainActivity) && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        MobclickAgent.onPause(this);
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
//        if (initClientNumbers < CLIENT_INIT_NUMBER) {

        if (-1001 == code) {
            LogUtils.d("重新初始化", "BaseActivity 重新初始化：" + code);
            MyApplication.getInstance().clearLocalData();
            if (initClientNumbers == 0) {
                PreferencesUtils.putString(context, CLIENT_INIT_TIME, DateUtil.getCurrentDate());
            }
            initClientNumbers++;
        } else if (-1003 == code) {
            LogUtils.d("重新初始化", "BaseActivity 重新初始化（重新生成设备号）：" + code);
            if (initClientNumbers == 0) {
                PreferencesUtils.putString(context, CLIENT_INIT_TIME, DateUtil.getCurrentDate());
            }
            MyApplication.getInstance().setDeviceId("");
            MyApplication.getInstance().clearLocalData();
            initClientNumbers++;
        } else if (-1004 == code) {
            LogUtils.d("系统错误", "-1004");
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
//            OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST_V2, Api.CLIENT_INIT, ParamUtil.getParam(null), Action.client_init);
//        }

    }

    public static void initApp(final Context context) {
        MyApplication.setUser(null);
        EventBus.getDefault().post(new HttpEvent(Action.off_line, Constant.SUCCEED));
        DialogUtils.TipInitApp(context, "请点击按钮，初始化APP", new DialogUtils.IDialogButtonOnClickListener() {
            @Override
            public void cancel() {

            }

            @Override
            public void sure() {
                MyApplication.getInstance().setDeviceId("");
                MyApplication.getInstance().clearLocalData();
                OKHttpUtils.getInstance().post(context, Api.TEST_DNS_API_HOST, Api.CLIENT_INIT, ParamUtil.getParam(null), Action.client_init);
            }
        });
    }

    public static void offLine(final Context context) {
        MyApplication.setUser(null);
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
                        MyApplication.setKey(privateKey);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.e("重新初始化", "BaseActivity 重新初始化成功，但结果解析异常" + event.getMsg(), e);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event) {
        String alert = event.getAlert();
        String extras = event.getExtras();
        Log.i("JPush", "onEventMainThread——base");
        if (ExampleUtil.isEmpty(extras)) {
            return;
        }
        boolean isNeedAlert = false;
        try {
            final Intent i = new Intent();
            JSONObject jsonObject = new JSONObject(extras);
            if (MessageType.DynamicMessageType.type().equals(jsonObject.getString("type"))) {//动态消息消息
                LogUtils.i("JPush", "动态baseActivity=");
                EventBus.getDefault().post(new HttpEvent(Action.DynamicMessage, Constant.SUCCEED));
            } else if (MessageType.OffLineType.type().equals(jsonObject.getString("type"))) {//离线消息
                offLine(this);
                LogUtils.i("JPush", "离线baseActivity=");
            } else if (MessageType.ActivityType.type().equals(jsonObject.getString("type"))) {//活动消息
                ActivityMessage systemMessage = new Gson().fromJson(extras, ActivityMessage.class);
                LogUtils.i("JPush", "活动消息baseActivity=" + systemMessage.getTitle());
            } else if (jsonObject.has("type")) {
                if (MessageType.SystemType.type().equals(jsonObject.getString("type"))) {//系统消息
                    SystemMessage systemMessage = new Gson().fromJson(extras, SystemMessage.class);
                    isNeedAlert = systemMessage.isNeedAlert();
                    i.setClass(this, MessageSystemActivity.class);
                } else if (MessageType.VideoType.type().equals(jsonObject.getString("type"))) {//视频消息
                    VideoMessage videoMessage = new Gson().fromJson(extras, VideoMessage.class);
                    isNeedAlert = videoMessage.isNeedAlert();
                    i.setClass(this, VideoActivity.class);
                    i.putExtra(VideoActivity.NEWS_ID, String.valueOf(videoMessage.getId()));
                    i.putExtra(VideoActivity.VIDEO_NAME, videoMessage.getNews_title());
                    i.putExtra(VideoActivity.CAT_ID, videoMessage.getCat_id());
                    i.putExtra(VideoActivity.VIDEO_ID, videoMessage.getVideo_id());
                } else if (MessageType.NewsType.type().equals(jsonObject.getString("type"))) {//文章消息
                    NewsMessage newsMessage = new Gson().fromJson(extras, NewsMessage.class);
                    isNeedAlert = newsMessage.isNeedAlert();
                    i.setClass(this, PostZoneActivity.class);
                    i.putExtra(Constant.KEY_ID, newsMessage.getId());
                    i.putExtra(Constant.KEY_CATID, newsMessage.getCat_id());
                } else if (MessageType.LiveType.type().equals(jsonObject.getString("type"))) {
                    i.setClass(this, PlayLiveActivity.class);
                    AppointmentLive mAppointmentLive = new Gson().fromJson(extras, AppointmentLive.class);
                    isNeedAlert = mAppointmentLive.isNeedAlert();
                    LiveParameter liveParameter = new LiveParameter();
                    liveParameter.setLiveId(mAppointmentLive.getLive_id());
                    liveParameter.setLiveTitle(mAppointmentLive.getLive_title());
                    liveParameter.setRoomId(mAppointmentLive.getLive_room_id());
                    liveParameter.setTeacherId(mAppointmentLive.getTeacher_id());
                    i.putExtra(Constant.ARG_LIVING, liveParameter);
                }else if (MessageType.AppointmentLiveType.type().equals(jsonObject.getString("type"))) {
                    i.setClass(this, TeacherZoneActivity.class);
                    AppointmentLive mAppointmentLive = new Gson().fromJson(extras, AppointmentLive.class);
                    isNeedAlert = mAppointmentLive.isNeedAlert();
                    i.putExtra(TeacherZoneActivity.ID, mAppointmentLive.getTeacher_id());
                }
            }
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (isNeedAlert && !TextUtils.isEmpty(alert)) {
                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
                dialogBuilder.withTitle(null).withMessage(null).withDialogColor(getResources().getColor(R.color.white_color)).isCancelableOnTouchOutside(false);
                dialogBuilder.setCustomView(R.layout.dialog_push_message, this);
                TextView pushMessageContent = (TextView) dialogBuilder.findViewById(R.id.tv_push_message_content);
                TextView pushMessageCancel = (TextView) dialogBuilder.findViewById(R.id.tv_pushMessageCancel);
                TextView pushMessageRead = (TextView) dialogBuilder.findViewById(R.id.tv_pushMessageRead);
                pushMessageContent.setText(StringUtils.replaceNullToEmpty(alert));
                pushMessageCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                });
                pushMessageRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JPushInterface.clearAllNotifications(BaseActivity.this);
                        dialogBuilder.dismiss();
                        startActivity(i);
                    }
                });
                dialogBuilder.show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void tip(String content) {
        tip(content, true);
    }

    public void tip(String content, final boolean isClose) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.prompt))
                .setMessage(content)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (isClose) {
                    finish();
                }
            }
        });
        mBuilder.create().show();
    }
}
