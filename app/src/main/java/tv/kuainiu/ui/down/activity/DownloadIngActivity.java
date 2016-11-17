package tv.kuainiu.ui.down.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bokecc.sdk.mobile.download.Downloader;
import com.bokecc.sdk.mobile.exception.ErrorCode;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import tv.kuainiu.R;
import tv.kuainiu.app.ConfigUtil;
import tv.kuainiu.app.DataSet;
import tv.kuainiu.command.dao.VideoDownloadDao;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.DownloadInfo;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.down.DownloadService;
import tv.kuainiu.ui.down.adapter.DownloadIngAdapter;
import tv.kuainiu.utils.ParamsUtil;
import tv.kuainiu.utils.SDCardUtils;
import tv.kuainiu.widget.DividerItemDecoration;

/**
 * 下载列表
 */
public class DownloadIngActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.recyView)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressBar_sdcard_size)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_sdcard_size)
    TextView mTvSDCardCapacity;
    @BindView(R.id.ll_bottom_tab)
    LinearLayout mTabBottom;
    @BindView(R.id.tv_selected_add)
    TextView mTvSelectedAdd;
    @BindView(R.id.tv_delete_selected)
    TextView mTvSelectedDel;

    private LinearLayoutManager mLayoutManager;
    private LinkedList<DownloadInfo> mSelectedList = new LinkedList<>();
    private VideoDownloadDao mDownloadDao;
    private DownloadIngAdapter mAdapter;

    private Context context;
    private tv.kuainiu.ui.activity.BaseActivity activity;
    private LinkedList<DownloadInfo> downloadingInfos = new LinkedList<>();;
    private DownloadService.DownloadBinder binder;
    private ServiceConnection serviceConnection;

    private Timer timter = new Timer();
    private boolean isBind;
    private Intent service;
    private DownloadedReceiver receiver;
    private String currentDownloadTitle;

    private Handler handler = new Handler() {

        private int currentPosition = ParamsUtil.INVALID;
        private int currentProgress = 0;

        @Override
        public void handleMessage(Message msg) {

            String title = (String) msg.obj;

            if (title == null || downloadingInfos.isEmpty()) {
                return;
            }

            resetHandlingTitle(title);

            int progress = binder.getProgress();
            if (progress > 0 && currentPosition != ParamsUtil.INVALID) {

                if (currentProgress == progress) {
                    return;
                }
                currentProgress = progress;
                DownloadInfo downloadInfo = downloadingInfos.remove(currentPosition);

                downloadInfo.setProgress(binder.getProgress());
                downloadInfo.setProgressText(binder.getProgressText());
                DataSet.updateDownloadInfo(downloadInfo);

                downloadingInfos.add(currentPosition, downloadInfo);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.invalidate();

            }

            super.handleMessage(msg);
        }

        private void resetHandlingTitle(String title) {
            for (DownloadInfo d : downloadingInfos) {
                if (d.getTitle().equals(title)) {
                    currentPosition = downloadingInfos.indexOf(d);
                    break;
                }
            }
        }

    };

    // 通过定时器和Handler来更新进度条
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

            if (binder == null || binder.isStop()) {
                return;
            }

            // 判断是否存在正在下载的视频
            if (currentDownloadTitle == null) {
                currentDownloadTitle = binder.getTitle();
            }

            if (currentDownloadTitle == null || downloadingInfos.isEmpty()) {
                return;
            }

            Message msg = new Message();
            msg.obj = currentDownloadTitle;

            handler.sendMessage(msg);
        }
    };

    private class DownloadedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (isBind) {
                bindServer();
            }

            if (intent.getStringExtra("title") != null) {
                currentDownloadTitle = intent.getStringExtra("title");
            }

            int downloadStatus = intent.getIntExtra("status", ParamsUtil.INVALID);
            // 若当前状态为下载中，则重置view的标记位置
            if (downloadStatus == Downloader.DOWNLOAD) {
                currentDownloadTitle = null;
            }

            initData();
//            mAdapter.notifyDataSetChanged();
//            downloadingListView.invalidate();

            // 若当前状态为下载完成，且下载队列不为空，则启动service下载其他视频
            if (downloadStatus == Downloader.FINISH) {

                if (!downloadingInfos.isEmpty()) {
                    startWaitStatusDownload();
                }
            }

            // 若下载出现异常，提示用户处理
            int errorCode = intent.getIntExtra("errorCode", ParamsUtil.INVALID);
            if (errorCode == ErrorCode.NETWORK_ERROR.Value()) {
                Toast.makeText(context, "网络异常，请检查", Toast.LENGTH_SHORT).show();
            } else if (errorCode == ErrorCode.PROCESS_FAIL.Value()) {
                Toast.makeText(context, "下载失败，请重试", Toast.LENGTH_SHORT).show();
            } else if (errorCode == ErrorCode.INVALID_REQUEST.Value()) {
                Toast.makeText(context, "下载失败，请检查帐户信息", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void startWaitStatusDownload() {
        for (DownloadInfo downloadInfo : downloadingInfos) {
            if (downloadInfo.getStatus() == Downloader.WAIT) {
                currentDownloadTitle = downloadInfo.getTitle();
                Intent service = new Intent(context, DownloadService.class);
                service.putExtra("title", currentDownloadTitle);
                activity.startService(service);
                break;
            }
        }
    }

    private void bindServer() {
        service = new Intent(context, DownloadService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("service disconnected", name + "");
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (DownloadService.DownloadBinder) service;
            }
        };
        activity.bindService(service, serviceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMyTitle(R.string.offline_caching);
        setRightButtonVisibility(View.VISIBLE);
        setRightButton(R.mipmap.del);

    }


    private void initData() {
        downloadingInfos.clear();
        List<DownloadInfo> downloadInfos = DataSet.getDownloadInfos();
        for (DownloadInfo downloadInfo : downloadInfos) {

            if (downloadInfo.getStatus() == Downloader.FINISH) {
                continue;
            }

            if ((downloadInfo.getStatus() == Downloader.DOWNLOAD) && (binder == null || binder.isStop())) {
                Intent service = new Intent(context, DownloadService.class);
                service.putExtra("title", downloadInfo.getTitle());
                activity.startService(service);
            }
            downloadingInfos.add(downloadInfo);
        }
        if (mAdapter == null) {
            mAdapter = new DownloadIngAdapter(this, downloadingInfos);
            mRecyclerView.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_download_ing);
        long totalSize = SDCardUtils.getSDTotalSize(this);
        long freeSize = SDCardUtils.getSDAvailableSize();
        String textTotalSize = Formatter.formatFileSize(this, totalSize - freeSize);
        String textFreeSize = Formatter.formatFileSize(this, freeSize);

        mProgressBar.setMax((int) (totalSize / 1024 / 1024));
        mProgressBar.setProgress((mProgressBar.getMax() - (int) (freeSize / 1024 / 1024)));
        mTvSDCardCapacity.setText(getString(R.string.value_sdcard_capacity, textTotalSize, textFreeSize));
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setSmoothScrollbarEnabled(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        activity = this;
        context = getApplicationContext();
        receiver = new DownloadedReceiver();
        currentDownloadTitle = activity.getIntent().getStringExtra("title");
        activity.registerReceiver(receiver, new IntentFilter(ConfigUtil.ACTION_DOWNLOADING));
        timter.schedule(timerTask, 0, 1000);
        bindServer();
        initData();
    }


    @Override
    protected void initListener() {
        super.initListener();
        mTvSelectedDel.setOnClickListener(this);
        mTvSelectedAdd.setOnClickListener(this);

        mAdapter.setOnSelectedListener(new DownloadIngAdapter.OnSelectListener() {
            @Override
            public void selected(LinkedList<DownloadInfo> selectedInfos) {
                mSelectedList = selectedInfos;
                mTvSelectedDel.setText(getString(R.string.value_delete_count, selectedInfos.size()));
                if (mSelectedList.size() == downloadingInfos.size()) {
                    mTvSelectedAdd.setText("全不选");
                } else {
                    mTvSelectedAdd.setText("全选");
                }
            }

            @Override
            public void click(View v) {
                switch (v.getId()) {
                    case R.id.fl_dl_status:
                        DownloadInfo info2 = (DownloadInfo) v.getTag();
                        //若下载任务已停止，则下载新数据
                        if (binder.isStop()) {
                            Intent service = new Intent(activity, DownloadService.class);
                            service.putExtra("title", info2.getTitle());
                            activity.startService(service);
                            currentDownloadTitle = info2.getTitle();

                        } else if (info2.getTitle().equals(currentDownloadTitle)) {

                            switch (binder.getDownloadStatus()) {
                                case Downloader.PAUSE:
                                    binder.download();
                                    break;
                                case Downloader.DOWNLOAD:
                                    binder.pause();
                                    break;
                            }
                        }
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete_selected:
                if (downloadingInfos == null || downloadingInfos.size() < 1) return;
                if (mSelectedList == null || mSelectedList.size() < 1) return;

                for (DownloadInfo entry : mSelectedList) {
                    String path=Environment.getExternalStorageDirectory()
                            + "/".concat(ConfigUtil.DOWNLOAD_DIR).concat("/")
                            .concat(entry.getTitle()).concat(".mp4");
                    DataSet.removeDownloadInfo(entry.getTitle());
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                    downloadingInfos.remove(entry);
                }
                mAdapter.notifyDataSetChanged();
                mSelectedList.clear();
                mTvSelectedDel.setText(getString(R.string.value_delete_count, mSelectedList.size()));
                break;

            case R.id.tv_selected_add:
                if (downloadingInfos == null || downloadingInfos.size() < 1) return;
                if (mSelectedList == null) return;

                if (mSelectedList.size() == downloadingInfos.size()) {
                    mTvSelectedAdd.setText("全选");
                    mSelectedList.clear();
                } else {
                    mTvSelectedAdd.setText("全不选");
                    mSelectedList.clear();
                    mSelectedList.addAll(downloadingInfos);
                    mAdapter.setSelectedInfos(mSelectedList);
                }

                mTvSelectedDel.setText(getString(R.string.value_delete_count, mSelectedList.size()));
                mAdapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onRightTitleClick() {
        super.onRightTitleClick();
        mAdapter.setEditing();

        if (mTabBottom.isShown()) {
            setRightButtonVisibility(View.VISIBLE);
            setRightTitleVisibility(View.GONE);
            mTabBottom.setVisibility(View.GONE);
        } else {
            mTabBottom.setVisibility(View.VISIBLE);
            setRightTitleVisibility(View.VISIBLE);
            setRightTitle(getString(R.string.cancel));
            setRightButtonVisibility(View.GONE);
        }
        if (mSelectedList != null) {
            mSelectedList.clear();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTodo(HttpEvent event) {

    }

    @Override
    public void onDestroy() {
        activity.unbindService(serviceConnection);
        activity.unregisterReceiver(receiver);
        timerTask.cancel();
        isBind = false;
        super.onDestroy();
    }
}
