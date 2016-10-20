package tv.kuainiu.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import tv.kuainiu.event.MessageEvent;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.modle.cons.MessageType;
import tv.kuainiu.modle.push.ActivityMessage;
import tv.kuainiu.modle.push.NewsMessage;
import tv.kuainiu.modle.push.SystemMessage;
import tv.kuainiu.modle.push.VideoMessage;
import tv.kuainiu.ui.MainActivity;
import tv.kuainiu.ui.articles.activity.PostZoneActivity;
import tv.kuainiu.ui.message.activity.MessageHomeActivity;
import tv.kuainiu.ui.video.VideoActivity;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private static final String ACTION_MESSAGE = "ACTION_MESSAGE";
    private static final String ACTION_NOTIFICATION = "ACTION_NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle, ACTION_NOTIFICATION);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (isRunningForeground(context)) {//如果APP正在前台运行，显示推送消息窗口
                EventBus.getDefault().post(new MessageEvent(alert, extras));
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            //打开自定义的Activity
            processCustomMessage(context, bundle, ACTION_MESSAGE);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();


    }

    //send msg to MainTabActivity
    private void processCustomMessage(Context context, Bundle bundle, String type) {
        String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(TAG, "extras=" + extras);
        Log.d(TAG, "alert=" + alert);
        if (type.equals(ACTION_NOTIFICATION)) {
            if (isRunningForeground(context)) {
                EventBus.getDefault().post(new MessageEvent(alert, extras));
            }
        } else {
            try {
                Intent i = new Intent();
                JSONObject jsonObject = new JSONObject(extras);
                if (jsonObject.has("type")) {
                    if (MessageType.OffLineType.type().equals(jsonObject.getString("type"))) {//离线消息
                        if (!isRunningForeground(context)) {
                            i.setClass(context, MainActivity.class);
                        } else {
                            return;
                        }
                    } else if (MessageType.ActivityType.type().equals(jsonObject.getString("type"))) {//活动消息
                        ActivityMessage systemMessage = new Gson().fromJson(extras, ActivityMessage.class);
                        Log.d(TAG, "活动消息=" + extras);
                        return;
                    } else if (MessageType.SystemType.type().equals(jsonObject.getString("type"))) {//系统消息
                        SystemMessage systemMessage = new Gson().fromJson(extras, SystemMessage.class);
                        i.setClass(context, MessageHomeActivity.class);
                    } else if (MessageType.VideoType.type().equals(jsonObject.getString("type"))) {//视频消息
                        VideoMessage videoMessage = new Gson().fromJson(extras, VideoMessage.class);
                        i.setClass(context, VideoActivity.class);
                        i.putExtra(VideoActivity.NEWS_ID, String.valueOf(videoMessage.getId()));
                        i.putExtra(VideoActivity.VIDEO_NAME, "");
                        //                        TODO 缺少视频名称
                        i.putExtra(VideoActivity.CAT_ID, videoMessage.getCatid());
                        i.putExtra(VideoActivity.VIDEO_ID, videoMessage.getUpvideoid());
                    } else if (MessageType.NewsType.type().equals(jsonObject.getString("type"))) {//文章消息
                        NewsMessage newsMessage = new Gson().fromJson(extras, NewsMessage.class);
                        i.setClass(context, PostZoneActivity.class);
                        i.putExtra(Constant.KEY_ID, String.valueOf(newsMessage.getDaoshi()));
//                        i.putExtra(Constant.KEY_ID, id);
//                        i.putExtra(Constant.KEY_CATID, catId);
//                        i.putExtra(Constant.KEY_CATNAME, catName);
//                        TODO 文章传参不对
                    }
                }
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            String currentPackageName = cn.getPackageName();
            if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
                Log.d(TAG, "[MyReceiver] APP 在前台运行");
                return true;
            }
            Log.d(TAG, "[MyReceiver] APP 在后台运行");
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }

}
