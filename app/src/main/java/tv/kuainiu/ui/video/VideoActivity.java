package tv.kuainiu.ui.video;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bokecc.sdk.mobile.download.Downloader;
import com.bokecc.sdk.mobile.download.OnProcessDefinitionListener;
import com.bokecc.sdk.mobile.exception.DreamwinException;
import com.bokecc.sdk.mobile.exception.ErrorCode;
import com.bokecc.sdk.mobile.play.DWMediaPlayer;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.ConfigUtil;
import tv.kuainiu.app.DataSet;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.CollectionMessageHttpUtil;
import tv.kuainiu.command.http.CommentHttpUtil;
import tv.kuainiu.command.http.SupportHttpUtil;
import tv.kuainiu.command.http.TeacherHttpUtil;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.CommentItem;
import tv.kuainiu.modle.DownloadInfo;
import tv.kuainiu.modle.TeacherInfo;
import tv.kuainiu.modle.VideoDetail;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.comments.CommentListActivity;
import tv.kuainiu.ui.comments.fragmet.PostCommentListFragment;
import tv.kuainiu.ui.down.DownloadService;
import tv.kuainiu.ui.me.activity.LoginActivity;
import tv.kuainiu.ui.video.adapter.VideoCommentAdapter;
import tv.kuainiu.umeng.UMEventManager;
import tv.kuainiu.utils.CustomLinearLayoutManager;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.MediaUtil;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.ParamsUtil;
import tv.kuainiu.utils.ShareUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.cc.PopMenu;
import tv.kuainiu.widget.cc.VerticalSeekBar;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;

/**
 * Created by sirius on 2016/9/24.
 * 点播
 */

public class VideoActivity extends BaseActivity implements
        DWMediaPlayer.OnBufferingUpdateListener,
        DWMediaPlayer.OnInfoListener,
        DWMediaPlayer.OnPreparedListener, DWMediaPlayer.OnErrorListener,
        MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback, SensorEventListener {
    public static final String NEWS_ID = "news_id";
    public static final String VIDEO_ID = "video_id";
    public static final String CAT_ID = "cat_id";
    public static final String VIDEO_NAME = "video_name";
    @BindView(R.id.tvTiltle)
    TextView tvTiltle;
    @BindView(R.id.tvDescripion)
    TextView tvDescripion;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvViewNumber)
    TextView tvViewNumber;
    @BindView(R.id.tvSupport)
    TextView tvSupport;
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.tvShare)
    TextView tvShare;
    @BindView(R.id.tvDown)
    TextView tvDown;
    @BindView(R.id.ci_avatar)
    CircleImageView ciAvatar;
    @BindView(R.id.rl_avatar)
    RelativeLayout rlAvatar;
    @BindView(R.id.tvTheme)
    TextView tvTheme;
    @BindView(R.id.tvTeacherName)
    TextView tvTeacherName;
    @BindView(R.id.tv_follow_button)
    TextView tvFollowButton;
    @BindView(R.id.tv_follow_number)
    TextView tvFollowNumber;
    @BindView(R.id.tvIntroduce)
    TextView tvIntroduce;
    @BindView(R.id.elvComments)
    RecyclerView elvComments;
    @BindView(R.id.ciCommentAavatar)
    CircleImageView ciCommentAavatar;
    @BindView(R.id.rlCommentAvatar)
    RelativeLayout rlCommentAvatar;
    @BindView(R.id.ivCommentBtn)
    ImageView ivCommentBtn;
    @BindView(R.id.tv_comment_number)
    TextView tvCommentNumber;
    @BindView(R.id.rl_comment_btn)
    RelativeLayout rlCommentBtn;

    private String news_id = "";
    private String cat_id = "";
    private String video_name = "";
    private String teacher_name = "";
    private VideoDetail mVideoDetail = null;
    private List<CommentItem> listCommentItem = new ArrayList<>();
    private BaseActivity activity;
    private Context context;
    /*点播*/
    private boolean networkConnected = true;
    private DWMediaPlayer player;
    //    private Subtitle subtitle;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ProgressBar bufferProgressBar;
    private SeekBar skbProgress;
    private ImageView playOp, backPlayList;
    private TextView videoIdText, playDuration, videoDuration;
    private Button screenSizeBtn, definitionBtn/*, subtitleBtn*/;
    private PopMenu screenSizeMenu, definitionMenu/*, subtitleMenu*/;
    private LinearLayout playerTopLayout, volumeLayout;
    private LinearLayout playerBottomLayout;
    private AudioManager audioManager;
    private VerticalSeekBar volumeSeekBar;
    private int currentVolume;
    private int maxVolume;
    private TextView subtitleText;

    private boolean isLocalPlay;
    private boolean isPrepared;
    private Map<String, Integer> definitionMap;

    private Handler playerHandler;
    private Timer timer = new Timer();
    private TimerTask timerTask, networkInfoTimerTask;

    private int currentScreenSizeFlag = 1;
    private int currrentSubtitleSwitchFlag = 0;
    private int currentDefinition = 0;

    private boolean firstInitDefinition = true;

    private String path;

    private Boolean isPlaying;
    // 当player未准备好，并且当前activity经过onPause()生命周期时，此值为true
    private boolean isFreeze = false;
    private boolean isSurfaceDestroy = false;

    int currentPosition;
    private Dialog dialog;

    private String[] definitionArray;
    private final String[] screenSizeArray = new String[]{"满屏", "100%", "75%", "50%"};
    private final String[] subtitleSwitchArray = new String[]{"开启", "关闭"};
//    private final String subtitleExampleURL = "http://dev.bokecc.com/static/font/example.utf8.srt";

    private GestureDetector detector;
    private float scrollTotalDistance, scrollCurrentPosition;
    private int lastPlayPosition, currentPlayPosition;
    private String videoId;
    private RelativeLayout rlPlay, rlComment;
    private ScrollView rlBelow;
    private WindowManager wm;
    private ImageView ivFullscreen;
    // 隐藏界面的线程
    private Runnable hidePlayRunnable = new Runnable() {

        @Override
        public void run() {
            setLayoutVisibility(View.GONE, false);
        }
    };
    private boolean isShowLoginTip;
    private boolean isCollect;

    private String title;
    private Downloader downloader;
    private DownloadService.DownloadBinder binder;
    private Intent service;
    private DownloadedReceiver receiver;

    int[] definitionMapKeys;
    /**
     * 视频清晰度
     */
    HashMap<Integer, String> hm;

    /**
     * @param context
     * @param video_id 视频文章id
     * @param cat_id   栏目id
     */
    public static void intoNewIntent(Context context, String news_id, String video_id, String cat_id, String video_name) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(NEWS_ID, news_id);
        intent.putExtra(VIDEO_ID, video_id);
        intent.putExtra(CAT_ID, cat_id);
        intent.putExtra(VIDEO_NAME, video_name);
        context.startActivity(intent);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = (String) msg.obj;
            if (message.equals(POPUP_DIALOG_MESSAGE)) {
                String[] definitionMapValues = new String[hm.size()];
                definitionMapKeys = new int[hm.size()];
                Set<Map.Entry<Integer, String>> set = hm.entrySet();
                Iterator<Map.Entry<Integer, String>> iterator = set.iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    Map.Entry<Integer, String> entry = iterator.next();
                    definitionMapKeys[i] = entry.getKey();
                    definitionMapValues[i] = entry.getValue();
                    i++;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("选择下载清晰度");
                builder.setSingleChoiceItems(definitionMapValues, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int definition = definitionMapKeys[which];

                        title = videoId + "-" + definition;
                        if (DataSet.hasDownloadInfo(title)) {
                            Toast.makeText(context, "该视频已下载过，请勿重复下载", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        File file = MediaUtil.createFile(title);
                        if (file == null) {
                            Toast.makeText(context, "创建文件失败", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (binder == null || binder.isStop()) {
                            Intent service = new Intent(context, DownloadService.class);
                            service.putExtra("title", title);
                            service.putExtra("name", video_name);
                            activity.startService(service);
                        } else {
                            Intent intent = new Intent(ConfigUtil.ACTION_DOWNLOADING);
                            activity.sendBroadcast(intent);
                        }

                        downloader.setFile(file); //确定文件名后，把文件设置到downloader里
                        downloader.setDownloadDefinition(definition);
                        downloaderHashMap.put(title, downloader);
                        //缩略图
                        String thumb = mVideoDetail == null ? "" : mVideoDetail.getThumb();
                        DataSet.addDownloadInfo(new DownloadInfo("", teacher_name, thumb, video_name, news_id, videoId, cat_id, title, 0, null, Downloader.WAIT, new Date(), definition));

                        definitionDialog.dismiss();
                        Toast.makeText(context, "文件已加入下载队列", Toast.LENGTH_SHORT).show();
                    }
                });
                definitionDialog = builder.create();
                definitionDialog.show();
            }

            if (message.equals(GET_DEFINITION_ERROR)) {
                Toast.makeText(context, "网络异常，请重试", Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }

    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("service disconnected", name + "");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownloadService.DownloadBinder) service;
        }
    };
    private OnProcessDefinitionListener onProcessDefinitionListener = new OnProcessDefinitionListener() {
        @Override
        public void onProcessDefinition(HashMap<Integer, String> definitionMap) {
            hm = definitionMap;
            if (hm != null) {
                Message msg = new Message();
                msg.obj = POPUP_DIALOG_MESSAGE;
                handler.sendMessage(msg);
            } else {
                Log.e(TAG, "视频清晰度获取失败");
            }
        }

        @Override
        public void onProcessException(DreamwinException exception) {
            Log.i(TAG, exception.getErrorCode().Value() + " : " + videoId);
            Message msg = new Message();
            msg.obj = GET_DEFINITION_ERROR;
            handler.sendMessage(msg);
        }
    };

    private class DownloadedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 若下载出现异常，提示用户处理
            int errorCode = intent.getIntExtra("errorCode", ParamsUtil.INVALID);
            if (errorCode == ErrorCode.NETWORK_ERROR.Value()) {
                Toast.makeText(context, "网络异常，请检查", Toast.LENGTH_SHORT).show();
            } else if (errorCode == ErrorCode.PROCESS_FAIL.Value()) {
                Toast.makeText(context, "下载失败，请重试", Toast.LENGTH_SHORT).show();
            } else if (errorCode == ErrorCode.INVALID_REQUEST.Value()) {
                Toast.makeText(context, "下载失败，请检查帐户信息", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        activity = this;
        context = getApplicationContext();
        receiver = new DownloadedReceiver();
        activity.registerReceiver(receiver, new IntentFilter(ConfigUtil.ACTION_DOWNLOADING));
        service = new Intent(context, DownloadService.class);
        activity.bindService(service, serviceConnection, Context.BIND_AUTO_CREATE);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // videoId="144640A8D6A51BCB9C33DC5901307461";
        videoId = getIntent().getStringExtra(VIDEO_ID);
        news_id = getIntent().getStringExtra(NEWS_ID);
        cat_id = getIntent().getStringExtra(CAT_ID);
        video_name = getIntent().getStringExtra(VIDEO_NAME);
        if (TextUtils.isEmpty(news_id)) {
            ToastUtils.showToast(this, "未获取到视频信息");
            finish();
            return;
        }
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        detector = new GestureDetector(this, new MyGesture());
        initView();
        initData();
        initPlayHander();
        initPlayInfo();
        initScreenSizeMenu();
        if (!isLocalPlay) {
            initNetworkTimerTask();
        }
    }

    private void initNetworkTimerTask() {
        networkInfoTimerTask = new TimerTask() {
            @Override
            public void run() {
                ConnectivityManager cm = (ConnectivityManager) VideoActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    if (!networkConnected) {
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {

                                if (!isPrepared) {
                                    return;
                                }

                                playerHandler.sendEmptyMessage(0);
                            }
                        };
                        timer.schedule(timerTask, 0, 1000);
                    }
                    networkConnected = true;
                } else {
                    networkConnected = false;
                    timerTask.cancel();
                }

            }
        };

        timer.schedule(networkInfoTimerTask, 0, 600);
    }

    private void initView() {
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(this);
        elvComments.setLayoutManager(mLayoutManager);

        /*点播*/
        rlPlay = (RelativeLayout) findViewById(R.id.rl_play);
        rlComment = (RelativeLayout) findViewById(R.id.rlComment);
        rlBelow = (ScrollView) findViewById(R.id.svBelow);
        rlPlay.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isPrepared) {
                    return true;
                }

                resetHideDelayed();

                // 事件监听交给手势类来处理
                detector.onTouchEvent(event);
                return true;
            }
        });

        rlPlay.setClickable(true);
        rlPlay.setLongClickable(true);
        rlPlay.setFocusable(true);

        surfaceView = (SurfaceView) findViewById(R.id.playerSurfaceView);
        playOp = (ImageView) findViewById(R.id.btnPlay);
        backPlayList = (ImageView) findViewById(R.id.backPlayList);
        bufferProgressBar = (ProgressBar) findViewById(R.id.bufferProgressBar);

        videoIdText = (TextView) findViewById(R.id.videoIdText);
        playDuration = (TextView) findViewById(R.id.playDuration);
        videoDuration = (TextView) findViewById(R.id.videoDuration);
        playDuration.setText(ParamsUtil.millsecondsToStr(0));
        videoDuration.setText(ParamsUtil.millsecondsToStr(0));

        screenSizeBtn = (Button) findViewById(R.id.playScreenSizeBtn);
        definitionBtn = (Button) findViewById(R.id.definitionBtn);
//        subtitleBtn = (Button) findViewById(R.id.subtitleBtn);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar = (VerticalSeekBar) findViewById(R.id.volumeSeekBar);
        volumeSeekBar.setThumbOffset(2);

        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);
        volumeSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        skbProgress = (SeekBar) findViewById(R.id.skbProgress);
        skbProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);

        playerTopLayout = (LinearLayout) findViewById(R.id.playerTopLayout);
        volumeLayout = (LinearLayout) findViewById(R.id.volumeLayout);
        playerBottomLayout = (LinearLayout) findViewById(R.id.playerBottomLayout);

        ivFullscreen = (ImageView) findViewById(R.id.iv_fullscreen);

        ivFullscreen.setOnClickListener(onClickListener);
        playOp.setOnClickListener(onClickListener);
        backPlayList.setOnClickListener(onClickListener);
        screenSizeBtn.setOnClickListener(onClickListener);
        definitionBtn.setOnClickListener(onClickListener);
//        subtitleBtn.setOnClickListener(onClickListener);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //2.3及以下使用，不然出现只有声音没有图像的问题
        surfaceHolder.addCallback(this);

//        subtitleText = (TextView) findViewById(R.id.subtitleText);

    }

    private void initData() {
        getNews();
        getHotCommentList();
    }

    final String POPUP_DIALOG_MESSAGE = "dialogMessage";

    final String GET_DEFINITION_ERROR = "getDefinitionError";

    //定义hashmap存储downloader信息
    public static HashMap<String, Downloader> downloaderHashMap = new HashMap<String, Downloader>();

    AlertDialog definitionDialog;


    @OnClick({R.id.tvDown, R.id.tvCollection, R.id.tvShare, R.id.tvSupport, R.id.rlComment, R.id.tv_follow_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvDown:
                if (mVideoDetail != null) {
                    //视频下载
                    downloader = new Downloader(videoId, ConfigUtil.USERID, ConfigUtil.API_KEY);
                    downloader.setOnProcessDefinitionListener(onProcessDefinitionListener);
                    downloader.getDefinitionMap();
                }
                break;
            case R.id.tvCollection:
                if (!MyApplication.isLogin()) {
                    showLoginTip();
                    return;
                }
                if (mVideoDetail.getCollected() != Constant.COLLECTED) {
                    addCollect();
                } else {
                    delCollect();
                }
                break;
            case R.id.tvShare:
                ShareUtils.showShare(ShareUtils.VIDEO, this, "", mVideoDetail.getTitle(), mVideoDetail.getThumb(), StringUtils.replaceNullToEmpty(mVideoDetail.getUrl(), "http://www.kuainiu.tv"), null);
                break;
            case R.id.tvSupport:
                if (!MyApplication.isLogin()) {
                    showLoginTip();
                    return;
                }
                if (null == mVideoDetail) {
                    DebugUtils.showToast(this, "未获取到相关信息，请刷新或稍后重试");
                    return;
                }

                if (!NetUtils.isOnline(this)) {
                    DebugUtils.showToast(this, getString(R.string.toast_not_network));
                    return;
                }
                SupportHttpUtil.supportVideoDynamics(this, cat_id, news_id);
                break;
            case R.id.rlComment:
                CommentListActivity.intoNewIntent(this, PostCommentListFragment.MODE_ARTICLE, news_id, cat_id);
                break;
            case R.id.tv_follow_button:
                addFollow(mVideoDetail.getTeacher_info().getIs_follow(), mVideoDetail.getTeacher_info().getId());
                break;
        }
    }

    private void addCollect() {
        CollectionMessageHttpUtil.addCollect(this, news_id, String.valueOf(Constant.NEWS_TYPE_VIDEO));
    }

    private void delCollect() {
        CollectionMessageHttpUtil.delCollect(this, news_id, String.valueOf(Constant.NEWS_TYPE_VIDEO));
    }

    // 添加 or 取消关注
    private void addFollow(int is_follow, String teacherId) {
        if (Constant.FOLLOWED == is_follow) {
            TeacherHttpUtil.delFollowForTeacherId(this, teacherId, Action.del_follow);
        } else {
            TeacherHttpUtil.addFollowForTeacherID(this, teacherId, Action.add_follow);
        }
    }

    private void dataBindComment() {
        VideoCommentAdapter videoCommentAdapter = new VideoCommentAdapter(this);
        videoCommentAdapter.setData(listCommentItem);
        elvComments.setAdapter(videoCommentAdapter);
    }

    private void dataBind() {
        if (mVideoDetail != null) {
            rlBelow.setVisibility(View.VISIBLE);
            rlComment.setVisibility(View.VISIBLE);
        } else {
            rlBelow.setVisibility(View.INVISIBLE);
            rlComment.setVisibility(View.INVISIBLE);
            return;
        }
        video_name = StringUtils.replaceNullToEmpty(mVideoDetail.getTitle());
        tvTiltle.setText(StringUtils.replaceNullToEmpty(mVideoDetail.getTitle()));
        tvDescripion.setText(StringUtils.replaceNullToEmpty(mVideoDetail.getCatname()));//栏目

        tvDate.setText(DateUtil.getDurationString(DateUtil.toJava(mVideoDetail.getInputtime())));//日期
        //播放次数
        tvViewNumber.setText(String.format(Locale.CHINA, "%s次", StringUtils.getDecimal(Integer.parseInt(StringUtils.replaceNullToEmpty(mVideoDetail.getView_num(), "0")), Constant.TEN_THOUSAND, "万", "")));
        //点赞次数
        tvSupport.setText(String.format(Locale.CHINA, "%s", StringUtils.getDecimal(Integer.parseInt(StringUtils.replaceNullToEmpty(mVideoDetail.getSupport_num(), "0")), Constant.TEN_THOUSAND, "万", "")));
        //评论次数
        tvCommentNumber.setText(String.format(Locale.CHINA, "%s", StringUtils.getDecimal(Integer.parseInt(StringUtils.replaceNullToEmpty(mVideoDetail.getComment_num(), "0")), Constant.TEN_THOUSAND, "万", "")));
        teacherDataBind(mVideoDetail.getTeacher_info());
        ImageDisplayUtil.displayImage(this, ciCommentAavatar, StringUtils.replaceNullToEmpty(MyApplication.isLogin() ? MyApplication.getUser().getAvatar() : ""), R.mipmap.default_avatar);
        videoIdText.setText(mVideoDetail.getTitle());
        tvCollection.setSelected(mVideoDetail.getCollected() == Constant.COLLECTED);
        tvSupport.setSelected(mVideoDetail.getIs_support() == Constant.FAVOURED);

    }

    private void teacherDataBind(TeacherInfo teacherInfo) {
        if (teacherInfo == null) {
            return;
        }
        teacher_name = teacherInfo.getNickname();
        ImageDisplayUtil.displayImage(this, ciAvatar, StringUtils.replaceNullToEmpty(teacherInfo.getAvatar()), R.mipmap.default_avatar);
        tvTheme.setText(StringUtils.replaceNullToEmpty(teacherInfo.getSlogan()));
        tvTeacherName.setText(StringUtils.replaceNullToEmpty(teacherInfo.getNickname()));
        tvFollowNumber.setText(String.format(Locale.CHINA, "%s人关注", StringUtils.getDecimal(teacherInfo.getFans_count(), Constant.TEN_THOUSAND, "万", "")));
        tvFollowButton.setSelected(teacherInfo.getIs_follow() == Constant.FOLLOWED);
        if (teacherInfo.getIs_follow() != Constant.FOLLOWED) {
            tvFollowButton.setText("+关注");

        } else {
            tvFollowButton.setText("已关注");
        }
        tvIntroduce.setText(StringUtils.replaceNullToEmpty(mVideoDetail.getDescription()));//介绍
    }

    private void getNews() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", news_id);
        map.put("user_id", MyApplication.isLogin() ? MyApplication.getUser().getUser_id() : "");
        OKHttpUtils.getInstance().syncGet(this, Api.VIDEO_OR_POST_DETAILS + ParamUtil.getParamForGet(map), Action.video_details);
    }

    private void getHotCommentList() {
        CommentHttpUtil.hotComment(this, "1", cat_id, news_id, "", 1, Action.comment_list_hot_video);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case video_details:
                if (SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        String info = object.optString("info");
                        LogUtils.e("VideoActivity2222", info);
                        mVideoDetail = new DataConverter<VideoDetail>().JsonToObject(info, VideoDetail.class);
                        dataBind();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e("VideoActivity", "获取视频详情数据失败:" + event.getMsg());
                        ToastUtils.showToast(this, StringUtils.replaceNullToEmpty("视频详情数据解析失败"));
                        finish();
                    }
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取视频详情数据失败"));
                    LogUtils.e("VideoActivity", "获取视频详情数据失败:" + event.getMsg());
                    finish();
                }
                break;
            case comment_list_hot_video:
                if (SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    LogUtils.e("VideoActivity", "json=" + json);
                    try {
                        JSONObject object = new JSONObject(json);

                        JSONArray jsonArray = object.optJSONArray("list");
                        List<CommentItem> tempNewsList = new DataConverter<CommentItem>().JsonToListObject(jsonArray.toString(), new TypeToken<List<CommentItem>>() {
                        }.getType());
                        if (tempNewsList != null && tempNewsList.size() > 0) {
                            listCommentItem.addAll(tempNewsList);
                            dataBindComment();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e("VideoActivity", "解析视频热门评论数据失败:" + event.getMsg());
                        ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "解析视频热门评论数据失败"));
                    }
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取视频热门评论失败"));
                    LogUtils.e("VideoActivity", "获取视频热门评论失败：" + event.getMsg());
                }
                break;
            // 添加收藏回调
            case add_collect:
                if (SUCCEED == event.getCode()) {
                    isCollect = true;
                    mVideoDetail.setCollected(Constant.COLLECTED);
                    DebugUtils.showToast(this, event.getMsg());
                    HashMap<String, String> map = new HashMap<>();
                    map.put("type", "video");
                    UMEventManager.onEvent(this, UMEventManager.ID_COLLECTION, map);
                    dataBind();
                } else {
                    DebugUtils.showToast(this, event.getMsg());
                }
                break;

            // 取消收藏回调
            case del_collect:
                if (SUCCEED == event.getCode()) {
                    isCollect = false;
                    mVideoDetail.setCollected(Constant.UNCOLLECT);
                    DebugUtils.showToast(this, event.getMsg());
                    dataBind();
                }
                break;
            // 视频点赞回调
            case video_favour:
                if (SUCCEED == event.getCode()) {
                    DebugUtils.showToast(VideoActivity.this, event.getMsg());
                    mVideoDetail.setSupport_num(mVideoDetail.getSupport_num() + 1);
                    mVideoDetail.setIs_support(Constant.FAVOURED);
                    dataBind();
                    DebugUtils.showToast(VideoActivity.this, "点赞成功");
                } else if (-2 == event.getCode()) {
                    DebugUtils.showToastResponse(this, "已支持过");
                } else {
                    DebugUtils.showToastResponse(this, "点赞失败,请稍后重试");
                }
                break;
            // 添加关注回调
            case add_follow:
                if (SUCCEED == event.getCode()) {
                    mVideoDetail.getTeacher_info().setIs_follow(Constant.FOLLOWED);
                    mVideoDetail.getTeacher_info().setFans_count(mVideoDetail.getTeacher_info().getFans_count() + 1);
                    dataBind();
                } else {
                    DebugUtils.showToastResponse(this, event.getMsg());
                }
                break;

            // 取消关注回调
            case del_follow:
                if (SUCCEED == event.getCode()) {
                    mVideoDetail.getTeacher_info().setIs_follow(Constant.UNFOLLOW);
                    mVideoDetail.getTeacher_info().setFans_count(mVideoDetail.getTeacher_info().getFans_count() - 1);
                    dataBind();
                } else {
                    DebugUtils.showToast(this, event.getMsg());
                }
                break;
        }
    }

    private void initPlayHander() {
        playerHandler = new Handler() {
            public void handleMessage(Message msg) {

                if (player == null) {
                    return;
                }

                // 刷新字幕
//                subtitleText.setText(subtitle.getSubtitleByTime(player
//                        .getCurrentPosition()));

                // 更新播放进度
                currentPlayPosition = player.getCurrentPosition();
                int duration = player.getDuration();

                if (duration > 0) {
                    long pos = skbProgress.getMax() * currentPlayPosition / duration;
                    playDuration.setText(ParamsUtil.millsecondsToStr(player
                            .getCurrentPosition()));
                    skbProgress.setProgress((int) pos);
                }
            }

            ;
        };

        // 通过定时器和Handler来更新进度
        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (!isPrepared) {
                    return;
                }

                playerHandler.sendEmptyMessage(0);
            }
        };

    }

    private void initPlayInfo() {
        timer.schedule(timerTask, 0, 1000);
        isPrepared = false;
        player = new DWMediaPlayer();
        player.reset();
        player.setOnErrorListener(this);
        player.setOnVideoSizeChangedListener(this);
        player.setOnInfoListener(this);


        isLocalPlay = getIntent().getBooleanExtra("isLocalPlay", false);
        try {

            if (!isLocalPlay) {// 播放线上视频
                lastPlayPosition = DataSet.getVideoPosition(videoId);
                player.setVideoPlayInfo(videoId, ConfigUtil.USERID, ConfigUtil.API_KEY, this);
                // 设置默认清晰度
                player.setDefaultDefinition(DWMediaPlayer.NORMAL_DEFINITION);

            } else {// 播放本地已下载视频

                if (android.os.Environment.MEDIA_MOUNTED.equals(Environment
                        .getExternalStorageState())) {
                    path = Environment.getExternalStorageDirectory()
                            + "/".concat(ConfigUtil.DOWNLOAD_DIR).concat("/")
                            .concat(videoId).concat(".mp4");
                    if (!new File(path).exists()) {
                        return;
                    }

                    player.setDataSource(path);
                }
            }

            player.prepareAsync();

        } catch (IllegalArgumentException e) {
            LogUtils.e("player error", e.getMessage());
        } catch (SecurityException e) {
            LogUtils.e("player error", e.getMessage());
        } catch (IllegalStateException e) {
            LogUtils.e("player error", e + "");
        } catch (IOException e) {
            LogUtils.e("player error", e.getMessage());
        }

        // 设置视频字幕
//        subtitle = new Subtitle(new Subtitle.OnSubtitleInitedListener() {
//
//            @Override
//            public void onInited(Subtitle subtitle) {
//                // 初始化字幕控制菜单
//                initSubtitleSwitchpMenu(subtitle);
//            }
//        });
//        subtitle.initSubtitleResource(subtitleExampleURL);

    }

    private void initScreenSizeMenu() {
        screenSizeMenu = new PopMenu(this, R.drawable.popdown,
                currentScreenSizeFlag);
        screenSizeMenu.addItems(screenSizeArray);
        screenSizeMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                // 提示已选择的屏幕尺寸
                ToastUtils.showToast(getApplicationContext(), screenSizeArray[position]);
                LayoutParams params = getScreenSizeParams(position);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                surfaceView.setLayoutParams(params);
            }
        });
    }

    private LayoutParams getScreenSizeParams(int position) {
        currentScreenSizeFlag = position;
        int width = 600;
        int height = 400;
        if (isPortrait()) {
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight() * 2 / 5; //TODO 根据当前布局更改
        } else {
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight();
        }


        String screenSizeStr = screenSizeArray[position];
        if (screenSizeStr.indexOf("%") > 0) {// 按比例缩放
            int vWidth = player.getVideoWidth();
            if (vWidth == 0) {
                vWidth = 600;
            }

            int vHeight = player.getVideoHeight();
            if (vHeight == 0) {
                vHeight = 400;
            }

            if (vWidth > width || vHeight > height) {
                float wRatio = (float) vWidth / (float) width;
                float hRatio = (float) vHeight / (float) height;
                float ratio = Math.max(wRatio, hRatio);

                width = (int) Math.ceil((float) vWidth / ratio);
                height = (int) Math.ceil((float) vHeight / ratio);
            } else {
                float wRatio = (float) width / (float) vWidth;
                float hRatio = (float) height / (float) vHeight;
                float ratio = Math.min(wRatio, hRatio);

                width = (int) Math.ceil((float) vWidth * ratio);
                height = (int) Math.ceil((float) vHeight * ratio);
            }


            int screenSize = ParamsUtil.getInt(screenSizeStr.substring(0, screenSizeStr.indexOf("%")));
            width = (width * screenSize) / 100;
            height = (height * screenSize) / 100;
        }

        LayoutParams params = new LayoutParams(width, height);
        return params;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnBufferingUpdateListener(this);
            player.setOnPreparedListener(this);
            player.setDisplay(holder);
            player.setScreenOnWhilePlaying(true);
            if (isSurfaceDestroy) {
                if (isLocalPlay) {
                    player.setDataSource(path);
                }
                player.prepareAsync();
            }
        } catch (Exception e) {
            LogUtils.e("videoPlayer", "error", e);
        }
        LogUtils.i("videoPlayer", "surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        holder.setFixedSize(width, height);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player == null) {
            return;
        }
        if (isPrepared) {
            currentPosition = player.getCurrentPosition();
        }

        isPrepared = false;
        isSurfaceDestroy = true;

        player.stop();
        player.reset();

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        if (!isFreeze) {
            if (isPlaying == null || isPlaying.booleanValue()) {
//                player.pause();
                playOp.setImageResource(R.drawable.btn_play);
                currentPosition = 0;
            }
        }

        if (currentPosition >= 0) {
            player.seekTo(currentPosition);
        } else {
            if (lastPlayPosition > 0) {
                player.seekTo(lastPlayPosition);
            }
        }

        definitionMap = player.getDefinitions();
        if (!isLocalPlay) {
            initDefinitionPopMenu();
        }

        bufferProgressBar.setVisibility(View.GONE);
        setSurfaceViewLayout();
        videoDuration.setText(ParamsUtil.millsecondsToStr(player.getDuration()));
    }

    // 设置surfaceview的布局
    private void setSurfaceViewLayout() {
        LayoutParams params = getScreenSizeParams(currentScreenSizeFlag);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        surfaceView.setLayoutParams(params);
    }

    private void initDefinitionPopMenu() {

        if (definitionMap.size() > 1 && firstInitDefinition) {
            currentDefinition = 1;
            firstInitDefinition = false;
        }

        definitionMenu = new PopMenu(this, R.drawable.popup, currentDefinition);
        // 设置清晰度列表
        definitionArray = new String[]{};
        definitionArray = definitionMap.keySet().toArray(definitionArray);

        definitionMenu.addItems(definitionArray);
        definitionMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                try {

                    currentDefinition = position;
                    int definitionCode = definitionMap
                            .get(definitionArray[position]);

                    if (isPrepared) {
                        currentPosition = player.getCurrentPosition();
                        if (player.isPlaying()) {
                            isPlaying = true;
                        } else {
                            isPlaying = false;
                        }
                    }

                    setLayoutVisibility(View.GONE, false);
                    bufferProgressBar.setVisibility(View.VISIBLE);

                    player.reset();

                    player.setDefinition(getApplicationContext(),
                            definitionCode);

                } catch (IOException e) {
                    LogUtils.e("player error", e.getMessage());
                }

            }
        });
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        skbProgress.setSecondaryProgress(percent);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            resetHideDelayed();

            switch (v.getId()) {
                case R.id.btnPlay:
                    if (!isPrepared) {
                        return;
                    }
                    changePlayStatus();
                    break;

                case R.id.backPlayList:
                    if (isPortrait()) {
                        finish();
                    } else {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    break;
                case R.id.iv_fullscreen:
                    if (isPortrait()) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } else {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    break;
                case R.id.playScreenSizeBtn:
                    screenSizeMenu.showAsDropDown(v);
                    break;
//                case R.id.subtitleBtn:
//                    subtitleMenu.showAsDropDown(v);
//                    break;
                case R.id.definitionBtn:
                    definitionMenu.showAsDropDown(v);
                    break;
            }
        }
    };

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        int progress = 0;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (networkConnected || isLocalPlay) {
                player.seekTo(progress);
                playerHandler.postDelayed(hidePlayRunnable, 5000);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            playerHandler.removeCallbacks(hidePlayRunnable);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (networkConnected || isLocalPlay) {
                this.progress = progress * player.getDuration() / seekBar.getMax();
            }
        }
    };

    VerticalSeekBar.OnSeekBarChangeListener seekBarChangeListener = new VerticalSeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            currentVolume = progress;
            volumeSeekBar.setProgress(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            playerHandler.removeCallbacks(hidePlayRunnable);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            playerHandler.postDelayed(hidePlayRunnable, 5000);
        }

    };

    // 控制播放器面板显示
    private boolean isDisplay = false;

//    private void initSubtitleSwitchpMenu(Subtitle subtitle) {
//        this.subtitle = subtitle;
//        subtitleMenu = new PopMenu(this, R.drawable.popup, currrentSubtitleSwitchFlag);
//        subtitleMenu.addItems(subtitleSwitchArray);
//        subtitleMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                switch (position) {
//                    case 0:// 开启字幕
//                        currentScreenSizeFlag = 0;
//                        subtitleText.setVisibility(View.VISIBLE);
//                        break;
//                    case 1:// 关闭字幕
//                        currentScreenSizeFlag = 1;
//                        subtitleText.setVisibility(View.GONE);
//                        break;
//                }
//            }
//        });
//    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 监测音量变化
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN
                || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {

            int volume = audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume != volume) {
                currentVolume = volume;
                volumeSeekBar.setProgress(currentVolume);
            }

            if (isPrepared) {
                setLayoutVisibility(View.VISIBLE, true);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void setLayoutVisibility(int visibility, boolean isDisplay) {
        if (player == null) {
            return;
        }

        if (player.getDuration() <= 0) {
            return;
        }

        playerHandler.removeCallbacks(hidePlayRunnable);

        this.isDisplay = isDisplay;

        if (isDisplay) {
            playerHandler.postDelayed(hidePlayRunnable, 5000);
        }

        playerTopLayout.setVisibility(visibility);
        playerBottomLayout.setVisibility(visibility);

        if (isPortrait()) {
            volumeLayout.setVisibility(View.GONE);
            screenSizeBtn.setVisibility(View.GONE);
            definitionBtn.setVisibility(View.GONE);
//            subtitleBtn.setVisibility(View.GONE);
        } else {
            volumeLayout.setVisibility(visibility);
            screenSizeBtn.setVisibility(visibility);
            definitionBtn.setVisibility(visibility);
//            subtitleBtn.setVisibility(visibility);
        }
    }

    private Handler alertHandler = new Handler() {

        AlertDialog.Builder builder;
        AlertDialog.OnClickListener onClickListener = new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        };

        @Override
        public void handleMessage(Message msg) {

            String message = "";
            boolean isSystemError = false;
            if (ErrorCode.INVALID_REQUEST.Value() == msg.what) {
                message = "无法播放此视频，请检查视频状态";
            } else if (ErrorCode.NETWORK_ERROR.Value() == msg.what) {
                message = "无法播放此视频，请检查网络状态";
            } else if (ErrorCode.PROCESS_FAIL.Value() == msg.what) {
                message = "无法播放此视频，请检查帐户信息";
            } else {
                isSystemError = true;
            }

            if (!isSystemError) {
                builder = new AlertDialog.Builder(VideoActivity.this);
                dialog = builder.setTitle("提示").setMessage(message)
                        .setPositiveButton("OK", onClickListener)
                        .setCancelable(false).show();
            }

            super.handleMessage(msg);
        }

    };

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Message msg = new Message();
        msg.what = what;
        if (alertHandler != null) {
            alertHandler.sendMessage(msg);
        }
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        setSurfaceViewLayout();
    }


    // 重置隐藏界面组件的延迟时间
    private void resetHideDelayed() {
        playerHandler.removeCallbacks(hidePlayRunnable);
        playerHandler.postDelayed(hidePlayRunnable, 5000);
    }

    // 手势监听器类
    private class MyGesture extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (!isDisplay) {
                setLayoutVisibility(View.VISIBLE, true);
            }
            scrollTotalDistance += distanceX;

            float duration = (float) player.getDuration();

            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

            float width = wm.getDefaultDisplay().getWidth() * 0.75f; // 设定总长度是多少，此处根据实际调整

            float currentPosition = scrollCurrentPosition - (float) duration
                    * scrollTotalDistance / width;

            if (currentPosition < 0) {
                currentPosition = 0;
            } else if (currentPosition > duration) {
                currentPosition = duration;
            }

            player.seekTo((int) currentPosition);

            playDuration.setText(ParamsUtil
                    .millsecondsToStr((int) currentPosition));
            int pos = (int) (skbProgress.getMax() * currentPosition / duration);
            skbProgress.setProgress(pos);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {

            scrollTotalDistance = 0f;

            scrollCurrentPosition = (float) player.getCurrentPosition();

            return super.onDown(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!isDisplay) {
                setLayoutVisibility(View.VISIBLE, true);
            }
            changePlayStatus();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isDisplay) {
                setLayoutVisibility(View.GONE, false);
            } else {
                setLayoutVisibility(View.VISIBLE, true);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    private void changePlayStatus() {
        if (player.isPlaying()) {
            player.pause();
            playOp.setImageResource(R.drawable.btn_play);

        } else {
            player.start();
            playOp.setImageResource(R.drawable.btn_pause);
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case DWMediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (player.isPlaying()) {
                    bufferProgressBar.setVisibility(View.VISIBLE);
                }
                break;
            case DWMediaPlayer.MEDIA_INFO_BUFFERING_END:
                bufferProgressBar.setVisibility(View.GONE);
                break;
        }
        return false;
    }

    // 获得当前屏幕的方向
    private boolean isPortrait() {
        int mOrientation = getApplicationContext().getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        } else {
            return true;
        }
    }

    private int mX, mY, mZ;
    private long lastTimeStamp = 0;
    private Calendar mCalendar;
    private SensorManager sensorManager;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];
            mCalendar = Calendar.getInstance();
            long stamp = mCalendar.getTimeInMillis() / 1000l;

            int second = mCalendar.get(Calendar.SECOND);// 53

            int px = Math.abs(mX - x);
            int py = Math.abs(mY - y);
            int pz = Math.abs(mZ - z);

            int maxvalue = getMaxValue(px, py, pz);
            if (maxvalue > 2 && (stamp - lastTimeStamp) > 1) {
                lastTimeStamp = stamp;
                LogUtils.d("demo", " sensor isMoveorchanged....");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
            mX = x;
            mY = y;
            mZ = z;
        }
    }

    /**
     * 获取一个最大值
     *
     * @param px
     * @param py
     * @param pz
     * @return
     */
    private int getMaxValue(int px, int py, int pz) {
        int max = 0;
        if (px > py && px > pz) {
            max = px;
        } else if (py > px && py > pz) {
            max = py;
        } else if (pz > px && pz > py) {
            max = pz;
        }

        return max;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onBackPressed() {

        if (isPortrait()) {
            super.onBackPressed();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onResume() {
        if (isFreeze) {
            isFreeze = false;
            if (isPrepared) {
                player.start();
            }
        } else {
            if (isPlaying != null && isPlaying.booleanValue() && isPrepared) {
                player.start();
            }
        }
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        if (isPrepared) {
            // 如果播放器prepare完成，则对播放器进行暂停操作，并记录状态
            if (player.isPlaying()) {
                isPlaying = true;
            } else {
                isPlaying = false;
            }
            player.pause();
        } else {
            // 如果播放器没有prepare完成，则设置isFreeze为true
            isFreeze = true;
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (playerHandler != null) {
            playerHandler.removeCallbacksAndMessages(null);
            playerHandler = null;
        }
        if (alertHandler != null) {
            alertHandler.removeCallbacksAndMessages(null);
            alertHandler = null;
        }

        if (currentPlayPosition > 0) {
            if (lastPlayPosition > 0) {
                DataSet.updateVideoPosition(videoId, currentPlayPosition);
            } else {
                DataSet.insertVideoPosition(videoId, currentPlayPosition);
            }
        }

        if (player != null) {
            player.reset();
            player.release();
            player = null;
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        if (!isLocalPlay && networkInfoTimerTask != null) {
            networkInfoTimerTask.cancel();
        }
        if (serviceConnection != null) {
            activity.unbindService(serviceConnection);
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        if (isPrepared) {
            // 刷新界面
            setLayoutVisibility(View.GONE, false);
            setLayoutVisibility(View.VISIBLE, true);
        }

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            rlBelow.setVisibility(View.VISIBLE);
            rlComment.setVisibility(View.VISIBLE);
            ivFullscreen.setImageResource(R.drawable.fullscreen_close);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            rlBelow.setVisibility(View.GONE);
            rlComment.setVisibility(View.GONE);
            ivFullscreen.setImageResource(R.drawable.fullscreen_open);
        }

        setSurfaceViewLayout();
    }

    private void showLoginTip() {
        if (isShowLoginTip) {
            return;
        }
        LoginPromptDialog loginPromptDialog = new LoginPromptDialog(VideoActivity.this);
        loginPromptDialog.setCallBack(new LoginPromptDialog.CallBack() {
            @Override
            public void onCancel(DialogInterface dialog, int which) {

            }

            @Override
            public void onLogin(DialogInterface dialog, int which) {
                Intent intent = new Intent(VideoActivity.this, LoginActivity.class);
                VideoActivity.this.startActivity(intent);
                isShowLoginTip = true;
            }

            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowLoginTip = false;
            }
        });
        loginPromptDialog.show();
    }
}
