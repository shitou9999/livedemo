package tv.kuainiu.ui.liveold;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import tv.kuainiu.R;
import tv.kuainiu.command.dao.LiveSubscribeDao;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.utils.LogUtils;

/**
 * 直播提醒到时间广播.通过该广播启动一个通知
 *
 * @author nanck on 2016/7/21.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final int REQUEST_MAIN = 10;
    public static final String LIVE_ID = "LiveId";
    public static final String TEACHER_ID = "TeacherId";
    public static final String LIVE_TITLE = "LiveTitle";
    public static final String ROOM_ID = "RoomId";
    String LiveId = "";
    String TeacherId = "";
    String LiveTitle = "";
    String RoomId = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        String content;
        try {
            LiveId = intent.getStringExtra(LIVE_ID);
            TeacherId = intent.getStringExtra(TEACHER_ID);
            LiveTitle = intent.getStringExtra(LIVE_TITLE);
            RoomId = intent.getStringExtra(ROOM_ID);
        } catch (NullPointerException e) {
            //获取内容文本时发生异常，需要制定其他友好方案
            e.printStackTrace();
            LogUtils.e("AlarmReceiver", "获取提醒内容异常", e);
            return;
        }
        LogUtils.e("AlarmReceiver", "RoomId=" + RoomId);
        LogUtils.e("AlarmReceiver", "TeacherId=" + TeacherId);
        LogUtils.e("AlarmReceiver", "LiveTitle=" + LiveTitle);
        LogUtils.e("AlarmReceiver", "LiveId=" + LiveId);
        if (TextUtils.isEmpty(RoomId)) {
            return;
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent liveIntent = new Intent(context, PlayLiveActivity.class);
        LiveParameter mLiveParameter = new LiveParameter();
        mLiveParameter.setRoomId(RoomId);
        mLiveParameter.setTeacherId(TEACHER_ID);
        mLiveParameter.setLiveTitle(LIVE_TITLE);
        mLiveParameter.setLiveId(LIVE_ID);
        liveIntent.putExtra(Constant.ARG_LIVING, mLiveParameter);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), REQUEST_MAIN,
                liveIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("您订阅的直播马上就要开始了~")
                .setContentText(LiveTitle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 1000, 1000, 1000})
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);

        manager.notify(110, builder.build());

        if (!TextUtils.isEmpty(LiveId)) {
            new LiveSubscribeDao(context).delete(LiveId);
        }
//        EventBus.getDefault().post(new HttpEvent(Action.switch_live_fragment, 0));
    }
}
