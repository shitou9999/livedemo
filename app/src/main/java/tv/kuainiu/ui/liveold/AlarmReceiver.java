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
import tv.kuainiu.ui.MainActivity;

/**
 * 直播提醒到时间广播.通过该广播启动一个通知
 *
 * @author nanck on 2016/7/21.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final int REQUEST_MAIN = 10;

    @Override public void onReceive(Context context, Intent intent) {
        String content;
        String id = null;
        try {
            id = intent.getStringExtra("id");
            content = intent.getStringExtra("title");
        } catch (NullPointerException e) {
            // TODO 获取内容文本时发生异常，需要制定其他友好方案
            content = "";
            e.printStackTrace();
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent liveIntent = new Intent(context, MainActivity.class);

//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(MainTabActivity.class);
//        stackBuilder.addNextIntent(liveIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), REQUEST_MAIN,
                liveIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("您订阅的直播马上就要开始了~")
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 1000, 1000, 1000})
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);

        manager.notify(110, builder.build());

        if (!TextUtils.isEmpty(id)) {
            new LiveSubscribeDao(context).delete(id);
        }

//        EventBus.getDefault().post(new HttpEvent(Action.switch_live_fragment, 0));
    }
}
