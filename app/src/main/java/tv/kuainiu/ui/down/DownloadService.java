package tv.kuainiu.ui.down;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.bokecc.sdk.mobile.download.DownloadListener;
import com.bokecc.sdk.mobile.download.Downloader;
import com.bokecc.sdk.mobile.exception.DreamwinException;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import tv.kuainiu.R;
import tv.kuainiu.app.ConfigUtil;
import tv.kuainiu.app.DataSet;
import tv.kuainiu.modle.DownloadInfo;
import tv.kuainiu.ui.down.activity.DownloadActivity;
import tv.kuainiu.ui.down.activity.DownloadIngActivity;
import tv.kuainiu.ui.video.VideoActivity;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.MediaUtil;
import tv.kuainiu.utils.ParamsUtil;

/**
 * DownloadService，用于支持后台下载
 *
 * @author CC视频
 */
public class DownloadService extends Service {
    private final int NOTIFY_ID = 10;
    private final String TAG = "tv.kuainiu.download.DownloadService";

    private Downloader downloader;
    private File file;
    private String title, name;
    private String videoId;
    private String catId;
    private int progress;
    private String progressText;

    private boolean stop = true;
    private DownloadBinder binder = new DownloadBinder();

    private NotificationManager notificationManager;
    private Notification notification;

    private Timer timer = new Timer();
    private TimerTask timerTask;

    public class DownloadBinder extends Binder {

        public String getTitle() {
            return title;
        }

        public int getProgress() {
            return progress;
        }

        public String getProgressText() {
            return progressText;
        }

        public boolean isStop() {
            return stop;
        }

        public void pause() {
            if (downloader == null) {
                return;
            }
            downloader.pause();
        }

        public void download() {
            if (downloader == null) {
                return;
            }

            if (downloader.getStatus() == Downloader.WAIT) {
                downloader.start();
            }

            if (downloader.getStatus() == Downloader.PAUSE) {
                downloader.resume();
            }
        }

        public void cancel() {

            if (downloader == null) {
                return;
            }

            downloader.cancel();
        }

        public int getDownloadStatus() {
            if (downloader == null) {
                return Downloader.WAIT;
            }

            return downloader.getStatus();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        super.onCreate();
    }

    private String getVideoId(String title) {
        if (title == null) {
            return null;
        }

        int charIndex = title.indexOf('-');

        if (-1 == charIndex) {
            return title;
        } else {
            return title.substring(0, charIndex);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            LogUtils.i(TAG, "intent is null.");
            return Service.START_STICKY;
        }

        if (downloader != null) {
            LogUtils.i(TAG, "downloader exists.");
            return Service.START_STICKY;
        }

        title = intent.getStringExtra("title");
        name = intent.getStringExtra("name");
        if (title == null) {
            LogUtils.i(TAG, "title is null");
            return Service.START_STICKY;
        }

        videoId = getVideoId(title);
        if (videoId == null) {
            LogUtils.i(TAG, "videoId is null");
            return Service.START_STICKY;
        }

        downloader = VideoActivity.downloaderHashMap.get(title);
        if (downloader == null) {
            file = MediaUtil.createFile(title);
            if (file == null) {
                LogUtils.i(TAG, "File is null");
                return Service.START_STICKY;
            }
            downloader = new Downloader(file, videoId, ConfigUtil.USERID, ConfigUtil.API_KEY);
            VideoActivity.downloaderHashMap.put(title, downloader);
        }

        downloader.setDownloadListener(downloadListener);
        downloader.start();

        Intent notifyIntent = new Intent(ConfigUtil.ACTION_DOWNLOADING);
        notifyIntent.putExtra("status", Downloader.WAIT);
        notifyIntent.putExtra("title", title);
        sendBroadcast(notifyIntent);

        setUpNotification();
        stop = false;

        LogUtils.i(TAG, "Start download service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        if (downloader != null) {
            downloader.cancel();
            resetDownloadService();
        }

        notificationManager.cancel(NOTIFY_ID);
        super.onTaskRemoved(rootIntent);
    }

    private DownloadListener downloadListener = new DownloadListener() {

        @SuppressWarnings("deprecation")
        @Override
        public void handleStatus(String videoId, int status) {

            Intent intent = new Intent(ConfigUtil.ACTION_DOWNLOADING);
            intent.putExtra("status", status);
            intent.putExtra("title", title);
            updateDownloadInfoByStatus(status);

            switch (status) {
                case Downloader.PAUSE:
                    sendBroadcast(intent);

                    LogUtils.i(TAG, "pause");
                    break;
                case Downloader.DOWNLOAD:
                    sendBroadcast(intent);

                    LogUtils.i(TAG, "download");
                    break;
                case Downloader.FINISH:
                    // 指定个性化视图
//                    RemoteViews contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_layout);
//                    contentView.setTextViewText(R.id.fileName, name);
                    Intent openIntent = new Intent(getApplicationContext(), DownloadActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, openIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher);
                    Notification.Builder builder = new Notification.Builder(getApplicationContext())
                            .setContentTitle("下载完成")
                            .setContentText(name).setLargeIcon(icon).setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())// 设置时间发生时间
                            .setContentIntent(pi)
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setAutoCancel(true);// 设置可以清除;

//                    Intent openIntent = new Intent(getApplicationContext(), DownloadActivity.class);
//                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, openIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//                    // 下载完毕后变换通知形式
//                    Notification.Builder builder = new Notification.Builder(getApplicationContext())
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setWhen(System.currentTimeMillis())// 设置时间发生时间
//                            .setContentTitle("下载完成")
//                            .setContentText("文件已下载完毕")
//                            .setContentIntent(pi)
//                            .setAutoCancel(true);// 设置可以清除;

                    notification = builder.getNotification();
                    // 通知更新
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(NOTIFY_ID, notification);
                    // 停掉服务自身
                    stopSelf();
                    // 重置下载服务
                    resetDownloadService();
                    // 通知已下载队列
                    sendBroadcast(new Intent(ConfigUtil.ACTION_DOWNLOADED));
                    // 通知下载中队列
                    sendBroadcast(intent);
                    //移除完成的downloader
                    //TODO 移除已下载完成的列表
                    LogUtils.i(TAG, "download finished.");
                    break;
            }
        }

        @Override
        public void handleProcess(long start, long end, String videoId) {
            if (stop) {
                return;
            }

            progress = (int) ((double) start / end * 100);
            if (progress <= 100) {
                progressText = ParamsUtil.byteToM(start).
                        concat(" M / ").
                        concat(ParamsUtil.byteToM(end).
                                concat(" M"));
            }
        }

        @Override
        public void handleException(DreamwinException exception, int status) {
            LogUtils.i("Download exception", exception.getErrorCode().Value() + " : " + title);
            // 停掉服务自身
            stopSelf();

            updateDownloadInfoByStatus(status);

            Intent intent = new Intent(ConfigUtil.ACTION_DOWNLOADING);
            intent.putExtra("errorCode", exception.getErrorCode().Value());
            intent.putExtra("title", title);
            sendBroadcast(intent);
            notificationManager.cancel(NOTIFY_ID);
        }

        @Override
        public void handleCancel(String videoId) {
            LogUtils.i(TAG, "cancel download, title: " + title + ", videoId: " + videoId);

            stopSelf();

            resetDownloadService();

            notificationManager.cancel(NOTIFY_ID);
        }
    };

    private void notifyProgress() {
        RemoteViews contentView = notification.contentView;
        contentView.setTextViewText(R.id.progressRate, progress + "%");
        contentView.setProgressBar(R.id.progress, 100, progress, false);
        // 通知更新  
        notificationManager.notify(NOTIFY_ID, notification);
    }

    @SuppressWarnings("deprecation")
    private void setUpNotification() {
        // 指定个性化视图  
        RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
        contentView.setTextViewText(R.id.fileName, name);
        Intent openIntent = new Intent(this, DownloadIngActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, openIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setContentTitle("开始下载")
                .setContent(contentView).setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())// 设置时间发生时间
                .setContentIntent(pi)
                .setAutoCancel(true);// 设置可以清除;

        notification = builder.getNotification();

        // 放置在"正在运行"栏目中  
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(NOTIFY_ID, notification);

        if (timerTask != null) {
            timerTask.cancel();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                notifyProgress();
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    private void resetDownloadService() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        progress = 0;
        progressText = null;
        downloader = null;
        stop = true;
    }

    private void updateDownloadInfoByStatus(int status) {
        DownloadInfo downloadInfo = DataSet.getDownloadInfo(title);
        if (downloadInfo == null) {
            return;
        }
        downloadInfo.setStatus(status);

        if (progress > 0) {
            downloadInfo.setProgress(progress);
        }

        if (progressText != null) {
            downloadInfo.setProgressText(progressText);
        }

        DataSet.updateDownloadInfo(downloadInfo);
    }

}
