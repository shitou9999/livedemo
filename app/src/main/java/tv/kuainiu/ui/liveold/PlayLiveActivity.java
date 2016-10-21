package tv.kuainiu.ui.liveold;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveListener;
import com.bokecc.sdk.mobile.live.DWLiveLoginListener;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.Answer;
import com.bokecc.sdk.mobile.live.pojo.ChatMessage;
import com.bokecc.sdk.mobile.live.pojo.Question;
import com.bokecc.sdk.mobile.live.pojo.RoomInfo;
import com.bokecc.sdk.mobile.live.pojo.TemplateInfo;
import com.bokecc.sdk.mobile.live.pojo.Viewer;
import com.bokecc.sdk.mobile.live.widget.DocView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.TeacherHttpUtil;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.TeacherInfo;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.activity.WebActivity;
import tv.kuainiu.ui.liveold.adapter.MyChatListViewAdapter;
import tv.kuainiu.ui.liveold.adapter.MyGridViewAdapter;
import tv.kuainiu.ui.liveold.adapter.MyQAListViewAdapter;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.ui.liveold.model.QAMsg;
import tv.kuainiu.ui.me.activity.LoginActivity;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.TimeFormatUtil;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.utils.WeakHandler;
import tv.kuainiu.widget.BarrageLayout;
import tv.kuainiu.widget.dialog.LoginPromptDialog;


/**
 * 直播
 */
public class PlayLiveActivity extends BaseActivity implements
        OnClickListener,
        SurfaceHolder.Callback,
        IjkMediaPlayer.OnPreparedListener,
        IjkMediaPlayer.OnVideoSizeChangedListener,
        IjkMediaPlayer.OnErrorListener,
        IjkMediaPlayer.OnBufferingUpdateListener,
        IjkMediaPlayer.OnInfoListener,
        IjkMediaPlayer.OnCompletionListener {

    RelativeLayout rlPlay;
    LinearLayout llBottomLayout;
    LinearLayout llFullscreen;
    TextView tvCount;
    TabLayout mTabLayout;
    SurfaceView sv;
    ProgressBar pb_loading;
    RelativeLayout rl_control;
    ImageView iv_back;
    ImageView iv_share;
    ImageView iv_control;
    ImageView full_screen;
    EditText etFullscreen;
    Button btnFullscreenSendMsg;
    BarrageLayout mBarrageLayout;
    ViewPager mPager;
    CircleImageView civ_avatar;
    TextView tv_live_teacher_zan;
    TextView tv_live_title;
    TextView tv_teacher_fans;
    TextView tv_live_teacher;
    Button btn_teacher_follow;
    TextView mInfo;
    RelativeLayout rl_mInfo;
    ImageView iv_mInfo;
    private List<TabLayout.Tab> listTab;

    private final String LIVE = "聊天室";
    private final String PUT_QUESTION = "问答";
    private final String ABOUT = "关于";

    private String[] tabNames = new String[]{LIVE, PUT_QUESTION, ABOUT};
    private int[] tabNamesTags = new int[]{0, 1, 2};
    private LiveParameter mLivingInfo;
    private TeacherInfo mTeacherInfo;
    private String liveId = "";
    private String teacherId = "";

    /**
     * 直播平台用户id
     */
    private String userId = "514F2E686C96FF47";
    /**
     * 房间号
     */
    private String roomId = "7h89Z1uHcCTEOTDsn6rQkCaj8lwaztWM";

    /**
     * 直播平台用户名
     */
    private String viewerName = "匿名";

    private String password = "cc123";

    private IjkMediaPlayer player;

    private DWLive dwLive;

    private SurfaceHolder holder;

    private List<View> pagerViewList = new ArrayList<View>();
    //    private Switch swi;
    private Button sendMsgBtn;
    //    private Button btnFullScreen, changeSource, changeSoundVideo;
    private ImageButton sendQABtn;
    private EditText etMsg, etQA;
    private ListView lvChat, lvQA;
    private MyChatListViewAdapter chatAdapter;
    private MyQAListViewAdapter qaAdapter;
    private List<ChatMessage> chatMsgs = new ArrayList<ChatMessage>();
    private LinkedHashMap<String, QAMsg> qaMap = new LinkedHashMap<String, QAMsg>();
    private boolean isKickOut = false;
    private WindowManager wm;
    private InputMethodManager imm;

    private boolean isSendPublicChatMsg = false;

    private int playSourceCount = 0;

    private int sourceChangeCount = 0;
    private Viewer viewer;
    private boolean isStop = false;
    private boolean isFinish = false;
    private boolean isPrepared = false;
    private static final int SHOW_PALY = 13;
    private static final int HIDE_PALY = 12;
    private static final int HIDE_CONTROL = 14;
    private static final int SHOW_CONTROL = 15;
    private static final int INIT_LVCHAT = 16;
    private static final int PUBLIC_MSG = 0;
    private static final int PRIVATE_QUESTION_MSG = 1;
    private static final int PRIVATE_ANSWER_MSG = 2;
    private static final int QUESTION = 10;
    private static final int ANSWER = 11;
    private static final int USER_COUNT = 20;
    private static final int FINISH = 40;
    private static final int KICK_OUT = -1;
    private static final int NOT_START = 41;
    private static final int FADE_OUT_INFO = 4;
    TemplateInfo templateInfo;
    private MyHandle handler;
    private boolean isLoginSuccess = false;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver mReceiver;
    private IntentFilter intentFilter;

    //Touch Events
    private int mVideoHeight;
    private int mVideoWidth;
    private static final int TOUCH_NONE = 0;
    private static final int TOUCH_VOLUME = 1;
    private static final int TOUCH_BRIGHTNESS = 2;
    private static final int TOUCH_SEEK = 3;
    private int mTouchAction;
    private int mSurfaceYDisplayRange;
    private float mTouchY, mTouchX, mVol;

    private boolean mIsLocked = false;
    private boolean mShowing;
    //Volume
    private AudioManager mAudioManager;
    private int mAudioMax;
    // Brightness
    private boolean mIsFirstBrightnessGesture = true;
    private boolean mEnableBrightnessGesture = true;
    /**
     * 是否可以拾遗
     */
    private int dvr;

    public static void intoNewIntent(Context context, LiveParameter liveParameter) {
        Intent intent = new Intent(context, PlayLiveActivity.class);
        intent.putExtra(Constant.ARG_LIVING, liveParameter);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ButterKnife.bind(this);
        registerBroadcast();
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mAudioMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    private void registerBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == Constant.INTENT_ACTION_GET_CUSTOM) {
//                    if (dwLive != null) {
//                        dwLive.stop();
//                        isLoginSuccess = false;
//                    }
                    loginLive();
                    getTeacherInfo();
                }
            }

        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.INTENT_ACTION_GET_CUSTOM);
        intentFilter.addAction(Constant.INTENT_ACTION_ACTIVITY_MSG_NUM);
        localBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_live2_play);
        rlPlay = (RelativeLayout) findViewById(R.id.rl_play2);
        llBottomLayout = (LinearLayout) findViewById(R.id.ll_bottom_layout2);
        llFullscreen = (LinearLayout) findViewById(R.id.ll_fullscreen_msg_send2);
        tvCount = (TextView) findViewById(R.id.tvCount2);
        mTabLayout = (TabLayout) findViewById(R.id.tab_live_top2);
        sv = (SurfaceView) findViewById(R.id.sv2);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        rl_control = (RelativeLayout) findViewById(R.id.rl_control2);
        iv_back = (ImageView) findViewById(R.id.iv_back2);
        iv_share = (ImageView) findViewById(R.id.iv_share2);
        iv_control = (ImageView) findViewById(R.id.iv_control2);
        full_screen = (ImageView) findViewById(R.id.full_screen2);
        etFullscreen = (EditText) findViewById(R.id.et_fullscreen2);
        btnFullscreenSendMsg = (Button) findViewById(R.id.btn_fullscreen_send2);
        mBarrageLayout = (BarrageLayout) findViewById(R.id.bl_barrage2);
        mPager = (ViewPager) findViewById(R.id.mPager2);
        civ_avatar = (CircleImageView) findViewById(R.id.civ_avatar2);
        tv_live_teacher_zan = (TextView) findViewById(R.id.tv_live_teacher_zan2);
        tv_live_title = (TextView) findViewById(R.id.tv_live_title2);
        tv_teacher_fans = (TextView) findViewById(R.id.tv_teacher_fans2);
        tv_live_teacher = (TextView) findViewById(R.id.tv_live_teacher2);
        btn_teacher_follow = (Button) findViewById(R.id.btn_teacher_follow2);
        mInfo = (TextView) findViewById(R.id.mInfo);
        rl_mInfo = (RelativeLayout) findViewById(R.id.rl_mInfo);
        iv_mInfo = (ImageView) findViewById(R.id.iv_mInfo);


        handler = new MyHandle(this);
        mLivingInfo = getIntent().getExtras().getParcelable(Constant.ARG_LIVING);
        if (mLivingInfo == null) {
            ToastUtils.showToast(this, "未获取到直播信息");
            finish();
            return;
        }

        liveId = mLivingInfo.getLiveId();
        teacherId = mLivingInfo.getTeacherId();
        roomId = mLivingInfo.getRoomId();
        dwLive = DWLive.getInstance();
        loginLive();
        getTeacherInfo();

    }

    private void intView() {
        holder = sv.getHolder();
        holder.addCallback(this);
        handler.sendEmptyMessage(HIDE_CONTROL);
    }

    private void bindDataToView() {
        ImageDisplayUtil.displayImage(this, civ_avatar, mTeacherInfo.getAvatar(), R.mipmap.default_avatar);
        tv_live_title.setText(mLivingInfo.getLiveTitle());
        tv_live_teacher.setText(mTeacherInfo.getNickname());
        tv_live_teacher_zan.setText(String.format(Locale.CHINA, "%d赞", mTeacherInfo.getLive_info().getSupport_num()));
//        tv_teacher_fans.setText(String.format(Locale.CHINA, "%d人在线", mTeacherInfo.getOnLineNumber()));
        if (mTeacherInfo.getIs_follow() == 0) {
            btn_teacher_follow.setSelected(false);
        } else {
            btn_teacher_follow.setSelected(true);
        }
        if (mTeacherInfo.getLive_info().getIs_support() == 0) {
            tv_live_teacher_zan.setSelected(false);
        } else {
            tv_live_teacher_zan.setSelected(true);
        }
    }

    private void initTab(int selectedIndex) {
        if (listTab == null) {
            listTab = new ArrayList<>();
            mTabLayout.removeAllTabs();
            for (int i = 0; i < tabNames.length; i++) {
                TabLayout.Tab tab = mTabLayout.newTab();
                tab.setText(tabNames[i]);
                tab.setTag(tabNamesTags[i]);
                if (templateInfo != null && templateInfo.getChatView().equals("0") && LIVE.equals(tabNames[i])) {
                    continue;
                }
                if (templateInfo != null && templateInfo.getQaView().equals("0") && PUT_QUESTION.equals(tabNames[i])) {
                    continue;
                }
                listTab.add(tab);
                mTabLayout.addTab(tab);

            }
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTabLayout.setOnTabSelectedListener(getOnTabSelectedListener());
        iv_back.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        iv_control.setOnClickListener(this);
        full_screen.setOnClickListener(this);
        etFullscreen.setOnClickListener(this);
        tv_live_teacher_zan.setOnClickListener(this);
        btn_teacher_follow.setOnClickListener(this);

        etFullscreen.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    fullScreenSendText();
                }
                return false;
            }
        });
        btnFullscreenSendMsg.setOnClickListener(this);
//        rlPlay.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (isPrepared) {
//                    if (rl_control.getVisibility() == View.VISIBLE) {
//                        handler.removeCallbacks(playHideRunnable);
//                        handler.sendEmptyMessage(HIDE_CONTROL);
//                    } else {
//                        handler.sendEmptyMessage(SHOW_CONTROL);
//                        hidePlayHander();
//                    }
//                }
//            }
//        });
    }

    @NonNull
    private TabLayout.OnTabSelectedListener getOnTabSelectedListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag() instanceof Integer) {
                    int i = (int) tab.getTag();
                    mPager.setCurrentItem(i);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    private void loginLive() {
        viewerName = PreferencesUtils.getString(this, MyApplication.KEY_DEVICEID, "");
        Map<String, String> map = new HashMap<>();
        map.put("teacher_id", teacherId);
        password = ParamUtil.getParam(map);
        pb_loading.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (password == null || "".equals(password)) {
                    dwLive.setDWLiveLoginParams(new LiveLoginListener(), userId, roomId, viewerName);
                } else {
                    dwLive.setDWLiveLoginParams(new LiveLoginListener(), userId, roomId, viewerName, password);
                }
                LogUtils.e("login", "userId=" + userId);
                LogUtils.e("login", "roomId=" + roomId);
                LogUtils.e("login", "viewerName=" + viewerName);
                LogUtils.e("login", "password=" + password);
                dwLive.startLogin();
            }
        }, 1000);
    }

    int loginTime = 0;

    /**
     * 登陆监听
     */
    class LiveLoginListener implements DWLiveLoginListener {
        @Override
        public void onLogin(TemplateInfo _templateInfo, Viewer _viewer, RoomInfo roomInfo) {
            templateInfo = _templateInfo;
            viewer = _viewer;
            isLoginSuccess = true;
            loginTime = 0;
            LogUtils.e("login", "登陆成功");
            dvr = roomInfo.getDvr();
            if (dvr > 0) {
                durationMax = dvr * 3600;
            }
            showLive();
        }

        @Override
        public void onException(DWLiveException e) {
            LogUtils.e("login", "登陆回调返回异常", e);
            isLoginSuccess = false;
            Message msg = handler.obtainMessage();
            msg.obj = e;
            msg.what = HIDE_PALY;
            handler.sendMessage(msg);
        }
    }

    private void initPlayer() {
        player = new IjkMediaPlayer();
        player.setOnPreparedListener(this);
        player.setOnVideoSizeChangedListener(this);
        player.setOnErrorListener(this);
        player.setOnBufferingUpdateListener(this);
        player.setOnInfoListener(this);
        player.setOnCompletionListener(this);
    }

    private void showLive() {
        intView();
        initSeekBar();
        initPlayer();
        dwLive.setDWLivePlayParams(dwLiveListener, docView, player);
        initAndStartLivePlay();
        handler.sendEmptyMessage(SHOW_PALY);
        handler.sendEmptyMessage(INIT_LVCHAT);

    }

    private GridView gvFace;

    private void initFace(View view) {
        gvFace = (GridView) view.findViewById(R.id.gv_face);
        gvFace.setAdapter(new MyGridViewAdapter(this, etMsg));
    }

    private void initPager() {
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPager.setAdapter(new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

                container.removeView(pagerViewList.get(position));
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = pagerViewList.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return pagerViewList.size();
            }
        });
    }

    private void initPagerItemView() {
        pagerViewList.clear();
        LayoutInflater inflater = LayoutInflater.from(this);
        //聊天
        View chatView = inflater.inflate(R.layout.chat_layout, null);
        initChatLayout(chatView);
        if (templateInfo != null && templateInfo.getChatView().equals("1")) {
            pagerViewList.add(chatView);
        }
        //提问题
        View qaView = inflater.inflate(R.layout.qa_layout, null);
        initQaLayout(qaView);
        if (templateInfo != null && templateInfo.getQaView().equals("1")) {
            pagerViewList.add(qaView);
        }
        //教案白板,关于
//        View picView = inflater.infate(R.layout.pic_layout, null);
        View picView = inflater.inflate(R.layout.live_about_layout, null);
        pagerViewList.add(picView);
        initPicLayout(picView);


    }

    private DocView docView;
    private WebView web_live_about;

    private void initPicLayout(View view) {
//        docView = (DocView) view.findViewById(R.id.live_docView);
        web_live_about = (WebView) view.findViewById(R.id.web_live_about);

    }

    private Switch swi;

    private void initChatLayout(View view) {
        swi = (Switch) view.findViewById(R.id.swi);
        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSendPublicChatMsg = true;
                } else {
                    isSendPublicChatMsg = false;
                }
            }
        });
        swi.performClick();

        sendMsgBtn = (Button) view.findViewById(R.id.btn_msg);
        sendMsgBtn.setOnClickListener(this);

        lvChat = (ListView) view.findViewById(R.id.lv_chat);

        etMsg = (EditText) view.findViewById(R.id.et_msg);
        etMsg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendChatMsg(true);
                }
                return false;
            }
        });

        etMsg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showNormalKeyBoard();
            }
        });

        initFace(view);
        initSmileKeyboard(view);
    }

    private void showNormalKeyBoard() {
        gvFace.setVisibility(View.GONE);
        ivSmile.setVisibility(View.VISIBLE);
        ivKeyBoard.setVisibility(View.GONE);
        showBottomEditTextSoftInput();
    }

    private void showSmileKeyboard() {
        hideEditTextSoftInput(etMsg);
        etMsg.requestFocus();
        ivSmile.setVisibility(View.GONE);
        ivKeyBoard.setVisibility(View.VISIBLE);
        gvFace.setVisibility(View.VISIBLE);
    }

    private Runnable playHideRunnable = new Runnable() {

        @Override
        public void run() {
            handler.sendEmptyMessage(HIDE_CONTROL);
        }
    };

    private void setPlayControllerVisible(boolean isVisible) {
        int visibility = 0;
        if (isVisible) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }
        if (!isPortrait()) {
//            llFullscreen.setVisibility(visibility);
            etFullscreen.requestFocus();
        }
        rl_control.setVisibility(visibility);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRelativeLayoutPlay(true);
            llFullscreen.setVisibility(View.GONE);
            mBarrageLayout.stop();
            mBarrageLayout.setVisibility(View.GONE);
            llProgress.setVisibility(View.VISIBLE);
            full_screen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            full_screen.setImageDrawable(getResources().getDrawable(R.mipmap.full_screen_b));

        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRelativeLayoutPlay(false);
            llFullscreen.setVisibility(View.VISIBLE);
            mBarrageLayout.start();
            mBarrageLayout.setVisibility(View.VISIBLE);
            llProgress.setVisibility(View.GONE);
//           	etFullscreen.requestFocus();
            full_screen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            full_screen.setImageDrawable(getResources().getDrawable(R.mipmap.exit_full_b));
        }
        sv.setLayoutParams(getScreenSizeParams());
        handler.removeCallbacks(playHideRunnable);
        handler.sendEmptyMessage(SHOW_CONTROL);
        hidePlayHander();
    }

    private void setRelativeLayoutPlay(boolean isPortraitOrien) {
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams layoutParams = null;
        if (isPortraitOrien) {
            layoutParams = new LinearLayout.LayoutParams(width, height / 3);
        } else {
            layoutParams = new LinearLayout.LayoutParams(width, height);
        }
        rlPlay.setLayoutParams(layoutParams);
    }

    @Override
    public void onBackPressed() {
        if (isPortrait()) {
            if (gvFace != null && gvFace.getVisibility() == View.VISIBLE) {
                gvFace.setVisibility(View.GONE);
                ivSmile.setVisibility(View.VISIBLE);
                ivKeyBoard.setVisibility(View.GONE);
            } else {
                super.onBackPressed();
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void showBottomEditTextSoftInput() {
        imm.showSoftInput(etMsg, 0);
    }

    private void sendChatMsg(boolean isKeyboard) {
        String msg = etMsg.getText().toString().trim();
        if (!"".equals(msg)) {
            if (isSendPublicChatMsg) {
                dwLive.sendPublicChatMsg(msg);
            } else {
                dwLive.sendPrivateChatMsg(msg);
            }
        }
        etMsg.setText("");
        gvFace.setVisibility(View.GONE);
        ivSmile.setVisibility(View.VISIBLE);
        ivKeyBoard.setVisibility(View.GONE);
        if (isKeyboard) {
            hideKeyBoardEditTextSoftInput();
        } else {
            hideEditTextSoftInput(etMsg);
        }
    }

    private ImageView ivSmile, ivKeyBoard;

    private void initSmileKeyboard(View view) {
        ivSmile = (ImageView) view.findViewById(R.id.iv_smile);
        ivSmile.setOnClickListener(this);
        ivKeyBoard = (ImageView) view.findViewById(R.id.iv_keyboard);
        ivKeyBoard.setOnClickListener(this);
    }

    private void initLvChat() {
        chatAdapter = new MyChatListViewAdapter(this, viewer, chatMsgs);
        lvChat.setAdapter(chatAdapter);
    }

    private void initQaLayout(View view) {
        sendQABtn = (ImageButton) view.findViewById(R.id.btn_qa);
        sendQABtn.setOnClickListener(this);

        lvQA = (ListView) view.findViewById(R.id.lv_qa);
        etQA = (EditText) view.findViewById(R.id.et_qa);
    }

    private void initLvQa() {
        qaAdapter = new MyQAListViewAdapter(this, viewer, qaMap);
        lvQA.setAdapter(qaAdapter);
    }

    private void initAbout() {
//        String hhh = "毛鹏皓老师具有30年以上操作经历。<br/>毛鹏皓老师不仅是&ldquo;孔明线&rdquo;的创始人，也是飙股工作室总监。<br/>&nbsp;<a href=\"http://wwww.baidu.com\">ssssssss</a><br/>毛鹏皓老师曾任多家证券投资顾问公司副总经理与总经理，在各家投资顾问公司的操作绩效极优，三位数绩效股多达十余档以上。<br/><img alt=\"\"src=\"http://www.iguxuan.com/uploadfile/2015/0803/20150803122307630.png\"style=\"height: 186px; width: 369px\"/><br/>毛鹏皓老师曾担任非凡电视台、台湾电视台、TVBS电视台、学者电视台等电视台专属讲师，中国广播公司、正声广播电台、快乐广播电台特聘讲师。<br/>&nbsp;<br/>毛鹏皓老师也曾担任财讯日报、产经日报、鑫报之特聘主笔。<br/><img alt=\"\"src=\"http://www.iguxuan.com/uploadfile/2015/0803/20150803122335409.png\"style=\"height: 394px; width: 554px\"/><br/><div style=\"text-align: center\">TVBS&ldquo;热线一路发&rdquo;投资组合成绩报告</div>&nbsp;<br/>毛鹏皓老师曾三次参加台湾电视台投资组合竞赛，皆获冠军并获得&ldquo;股市不败神话&rdquo;之雅号。<br/><img alt=\"\"src=\"http://www.iguxuan.com/uploadfile/2015/0803/20150803122410329.png\"style=\"height: 369px; width: 441px\"/><br/>毛鹏皓老师他的&ldquo;天机操盘术&rdquo;帮助投资者选择飙股；他的&ldquo;孔明线战法&rdquo;是帮助操盘者掌握高低档的买卖点，绝对能帮助投资者们超越指数、战胜大盘。<br/>&nbsp;<br/>这一次，毛鹏皓老师将把他多年来的操盘心法分享给大家，并将他&ldquo;孔明线战法&rdquo;的精华融入他的课堂内容中。这一期的课程会从简单的价、量、指标与波浪等技术分析一一切入。除了技术分析领域以外，毛鹏皓老师将配合基本面、筹码面、心理面的掌控，让具有多年股龄和熟悉技术分析的学员们都能从课程中吸收到毛鹏皓老师独特的操盘技巧与心法。<br/>&nbsp;<br/>毛鹏皓老师的课程能帮助学员们走向成功之路，他不仅要帮助你改变你的脑袋，还要改造你的态度，因为，脑袋会改变你的口袋，正确的态度会决定你的命运！<br/>&nbsp;<br/>毛鹏皓老师会与学员们分享德国股神、日本股神、美国股神、债券天王、新兴市场教父等投资大师的操盘心得。例如，毛鹏皓老师会提供给你巴菲特的六大选股法则，教你怎样选择长期投资的优良标的。<br/>&nbsp;<br/>&ldquo;天机操盘术&rdquo;将教你如何选股，你会掌握短线、中线和长线如何切入、如何加码、如何观察成交量，并且指导你如何运用K线、把握布局时机以及各种指标处于不同的多空时点该如何运用与操盘。<br/>&nbsp;<br/>&ldquo;天机操盘术&rdquo;的操盘课程会教你&ldquo;天机操盘术&rdquo;的72项绝技，让新手知道如何辨识头部与底部现象，让老手能准确地掌握底部买进的切入点和顶部如何避开风险的法则与退场卖点。<br/>&nbsp;<br/>毛鹏皓老师的股市操盘18招更是广大股民前所未见的投资秘笈。<br/>&nbsp;<br/>毛鹏皓老师作为理周集团证券分析师教育训练总督导已经培养出不计其数的优秀分析师，其中有5位达到千万元绩效，有一位达到上亿绩效！<br/>&nbsp;<br/>欢迎各路高手一起来探索&ldquo;孔明线&rdquo;与&ldquo;天机战法&rdquo;的奥妙之处！<br/>";
//        LogUtils.i("web_live_about", StringUtils.replaceNullToEmpty(mLivingInfo.getLiveing().getAnchor_about(), "45555"));
        if (web_live_about!=null && mTeacherInfo != null) {
            web_live_about.setWebViewClient(new MyWebViewClient());
            web_live_about.loadDataWithBaseURL(null, mTeacherInfo.getIntroduce(), "text/html", "utf-8", null);
        }
//        web_live_about.loadDataWithBaseURL(null, StringUtils.replaceNullToEmpty(mLivingInfo.getLiveing().getAnchor_about()), "text/html", "utf-8", null);

    }

    private TextView tvLiveDuration, total_time;
    //    private TextView tvCurrentPlayTip, tvLiveDuration, tvSeekTime;
    private int durationMax = 3600;
    private SeekBar seekBar;
    private Timer playerTimer = new Timer();
    private TimerTask playbackTimerTask;
    private int currentPosition;
    private int seekBarMax = 1000;
    private boolean isPlayBack = false;
    //    private RelativeLayout rlDvrTime;
    private LinearLayout llProgress;

    //    private TextView backToLive;
    private void initSeekBar() {
        llProgress = (LinearLayout) findViewById(R.id.ll_seek);
        tvLiveDuration = (TextView) findViewById(R.id.current_time);
        total_time = (TextView) findViewById(R.id.total_time);
        seekBar = (SeekBar) findViewById(R.id.play_seekBar);
        if (dvr < 1) {
            return;
        }
//        backToLive = (TextView) findViewById(R.id.back_to_live);
//        tvCurrentPlayTip = (TextView) findViewById(R.id.tv_current_play_tip);

//        tvSeekTime = (TextView) findViewById(R.id.tv_seek_time);
//        rlDvrTime = (RelativeLayout) findViewById(R.id.rl_dvr_time);
//        backToLive.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                player.pause();
//                setHolderBlack(plsWait);
//                startLivePlay();
//            }
//        });


        seekBar.setMax(seekBarMax);

        seekBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            } // 去除界面点击触发
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            Runnable seekTipsR = new Runnable() {

                @Override
                public void run() {
//                    rlDvrTime.setVisibility(View.GONE);
                    fadeOutInfo();
                }
            };

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//				stopPlaybackTimerTask();
                isStartTracking = false;
                player.pause();
//                setHolderBlack(plsWait);
                handler.postDelayed(seekTipsR, 2000); //解决快速滑动，导致显示不准的问题

                if (progress == seekBarMax) {
                    startLivePlay();
                    return;
                }

                String centerDvrStr = String.format("%s/%s", TimeFormatUtil.getTime(getRelTime()), TimeFormatUtil.getTime(liveDuration));
//                mInfo.setText(centerDvrStr);
                if (liveDuration > 0) {
                    showInfo(centerDvrStr, 2000, -1);
                }

                try {
                    currentPosition = getRelTime();
                    dwLive.startPlayedBackPlay(liveDuration - currentPosition);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DWLiveException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(seekTipsR);
                isStartTracking = true;
//                rlDvrTime.setVisibility(View.VISIBLE);
                String centerDvrStr = String.format("%s/%s", TimeFormatUtil.getTime(getRelTime()), TimeFormatUtil.getTime(liveDuration));
//                mInfo.setText(centerDvrStr);
                if (liveDuration > 0) {
                    showInfo(centerDvrStr, 2000, -1);
                }
            }

            @Override
            public void onProgressChanged(final SeekBar seekBar, int _progress, boolean fromUser) {
                Log.e("demo", _progress + "");
                progress = _progress;
                String centerDvrStr = String.format("%s/%s", TimeFormatUtil.getTime(getRelTime()), TimeFormatUtil.getTime(liveDuration));
//                mInfo.setText(centerDvrStr);
                if (liveDuration > 0) {
                    showInfo(centerDvrStr, 2000, -1);
                }
            }
        });

        seekBar.setProgress(seekBar.getMax());
    }

    int progress;

    // 获取当前的偏移时间
    private int getRelTime() {
        int mRelTime = 0;
        if (liveDuration > durationMax) {
            mRelTime = (int) ((float) durationMax * (float) progress / (float) seekBarMax) + liveDuration - durationMax;
        } else {
            mRelTime = (int) ((float) liveDuration * (float) progress / (float) seekBarMax);
        }
        return mRelTime;
    }

    private boolean isStartTracking = false;

    private int liveDuration;
    private TimerTask liveTimerTask;
    private boolean isLiveTimerTaskStart = false;

    // 回放计时器
    private void startPlaybackTimerTask() {
        stopPlaybackTimerTask();
        isPlayBack = true;
//        tvCurrentPlayTip.setText("回放中");
//        backToLive.setVisibility(View.VISIBLE);
        playbackTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentPosition++;
                        if (isPlayBack) {
                            tvLiveDuration.setText(TimeFormatUtil.getTime(currentPosition));
                            total_time.setText(TimeFormatUtil.getTime(liveDuration));
                        }

                        // 如果当前正在拖动seekbar，则停止刷新滑块位置
                        if (!isStartTracking) {
                            if (liveDuration > durationMax) {
                                seekBar.setProgress((int) ((float) seekBarMax * (float) (durationMax - (liveDuration - currentPosition)) / (float) durationMax));
                            } else {
                                seekBar.setProgress((int) ((float) seekBarMax * (float) currentPosition / (float) liveDuration));
                            }
                        }
                    }
                });
            }
        };
        playerTimer.schedule(playbackTimerTask, 0, 1000);
    }

    private void stopPlaybackTimerTask() {
        isPlayBack = false;
        if (playbackTimerTask != null) {
            playbackTimerTask.cancel();
        }
    }

    private void startLivePlay() {
        stopPlaybackTimerTask();
        try {
            dwLive.startLivePlay();
        } catch (Exception e) {
            Log.e("demo", e + "");
        }
        isPlayBack = false;

        setLiveStatusView();
    }

    private void setLiveStatusView() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (seekBar == null) {
                    return;
                }
                seekBar.setProgress(seekBarMax);
//                tvCurrentPlayTip.setText("直播中");
//                backToLive.setVisibility(View.INVISIBLE);
                currentPosition = 0;
            }
        });
    }

    // 开始直播定时器
    private void startLiveTimerTask() {
        isLiveTimerTaskStart = true;

        if (liveTimerTask != null) {
            liveTimerTask.cancel();
        }

        liveTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        liveDuration++;
                        if (!isPlayBack) {
                            tvLiveDuration.setText(TimeFormatUtil.getTime(liveDuration));
                            total_time.setText(TimeFormatUtil.getTime(liveDuration));
                        }
                    }
                });
            }
        };
        playerTimer.schedule(liveTimerTask, 0, 1000);
        setLiveStatusView();
    }

    private void stopLiveTimerTask() {
        isLiveTimerTaskStart = false;
        liveDuration = 0;
        if (liveTimerTask != null) {
            liveTimerTask.cancel();
        }

    }

    /**
     * 视频流更新
     *
     * @param iMediaPlayer
     * @param i
     */
    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        LogUtils.e("demo", "=============================>onError:" + what);
        Toast.makeText(this, "播放异常，回到直播", Toast.LENGTH_LONG).show();
        if (dwLive != null && !isStop) {
            qaMap.clear();
            dwLive.stop();
            initAndStartLivePlay();
        }
        return false;
    }

    Runnable r;

    @Override
    public boolean onInfo(IMediaPlayer arg0, int arg1, int arg2) {
        Log.e("demo", "oninfo");
        if (arg1 == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            r = new Runnable() {
                @Override
                public void run() {
                    dwLive.stop();
                    if (isLoginSuccess) {
                        initAndStartLivePlay();
                    }

                }
            };
            handler.postDelayed(r, 10000);
        } else if (arg1 == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            if (r != null) {
                handler.removeCallbacks(r);
            }
        }
        return false;
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        LogUtils.e("demo", "=============================>onCompletion");
        if (dwLive != null && !isStop) {
            qaMap.clear();
            dwLive.stop();
            if (isLoginSuccess) {
                initAndStartLivePlay();
            }
        }
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        LogUtils.i("demo", "onPrepared");
        isPrepared = true;

        pb_loading.setVisibility(View.GONE);
        llBottomLayout.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(SHOW_CONTROL);
        hidePlayHander();
        player.start();
        if (dvr > 0) {
            if (!isLiveTimerTaskStart) {
                liveDuration = 0;
                startLiveTimerTask();
            }

            if (seekBar.getProgress() < seekBarMax) {
                startPlaybackTimerTask();
            }
        }
    }

    /**
     * 初始化界面和定时器，并调用start方法开始播放
     */
    private void initAndStartLivePlay() {
        resumeLiveView();
        dwLive.getLivePlayedTime();
        dwLive.start(holder.getSurface());
    }

    /**
     * 恢复到直播的界面显示
     */
    private void resumeLiveView() {
        stopPlaybackTimerTask();
        setLiveStatusView();
    }

    /**
     * 隐藏控制界面
     */
    private void hidePlayHander() {
        handler.removeCallbacks(playHideRunnable);
        handler.postDelayed(playHideRunnable, 2000);
    }

    /**
     * 视频大小改变
     */
    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
        LogUtils.i("demo", "onVideoSizeChanged" + "width" + width + "height" + height);
        if (width == 0 || height == 0) {
            setHolderBlack("音频播放中……");
            return;
        }
//        tvPlayMsg.setVisibility(View.GONE);
        if (sv != null) {
            sv.setLayoutParams(getScreenSizeParams());
        }
    }

    private boolean isPortrait() {
        int mOrientation = getApplicationContext().getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        } else {
            return true;
        }
    }

    private RelativeLayout.LayoutParams getScreenSizeParams() {
        int width = 600;
        int height = 400;
        if (isPortrait()) {
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight() / 3; //TODO 根据当前布局更改
        } else {
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight();
        }

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

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        return params;
    }

    private DWLiveListener dwLiveListener = new DWLiveListener() {
        @Override
        public void onQuestion(Question question) {
            LogUtils.i("demo", "onQuestion:" + question.toString());
            Message msg = new Message();
            msg.what = QUESTION;
            msg.obj = question;
            handler.sendMessage(msg);
        }

        @Override
        public void onAnswer(Answer answer) {
            LogUtils.i("demo", "onAnswer:" + answer.toString());
            Message msg = new Message();
            msg.what = ANSWER;
            msg.obj = answer;
            handler.sendMessage(msg);
        }

        @Override
        public void onLiveStatus(DWLive.PlayStatus status) {
            LogUtils.i("demo", "onLiveStatusChange:" + status);
            switch (status) {
                case PLAYING:
                    isStop = false;
                    break;
                case PREPARING:
                    isStop = true;
                    handler.sendEmptyMessage(NOT_START);
                    break;
            }
        }

        @Override
        public void onPublicChatMessage(ChatMessage msg) {
            LogUtils.i("demo", "onPublicChatMessage:" + msg + "" + msg.getAvatar());
            Message handlerMsg = new Message();
            handlerMsg.what = PUBLIC_MSG;
            handlerMsg.obj = msg;
            handler.sendMessage(handlerMsg);
        }

        @Override
        public void onPrivateQuestionChatMessage(ChatMessage msg) {
            LogUtils.i("demo", "onPrivateQuestionChatMessage:" + msg);
            Message handlerMsg = new Message();
            handlerMsg.what = PRIVATE_QUESTION_MSG;
            handlerMsg.obj = msg;
            handler.sendMessage(handlerMsg);
        }

        @Override
        public void onPrivateAnswerChatMessage(ChatMessage msg) {
            LogUtils.i("demo", "onPrivateAnswerChatMessage:" + msg);
            Message handlerMsg = new Message();
            handlerMsg.what = PRIVATE_ANSWER_MSG;
            handlerMsg.obj = msg;
            handler.sendMessage(handlerMsg);
        }

        @Override
        public void onUserCountMessage(int count) {
            Message msg = new Message();
            msg.what = USER_COUNT;
            msg.obj = count;
            handler.sendMessage(msg);
        }

        @Override
        public void onNotification(String msg) {
            LogUtils.i("demo", "onNotification:" + msg);
        }

        @Override
        public void onInformation(String msg) {
            LogUtils.i("demo", "information:" + msg);
        }

        @Override
        public void onException(DWLiveException exception) {
            LogUtils.e("demo", exception.getMessage() + "");
        }

        @Override
        public void onIntervalException(Exception e) {
            //TODO 间隙播放请求异常

        }

        @Override
        public void onInitFinished(int playSourceCount) {
            PlayLiveActivity.this.playSourceCount = playSourceCount;
        }

        @Override
        public void onSilenceUserChatMessage(ChatMessage msg) {
            LogUtils.i("demo", "onSilenceUserChatMessage:" + msg);
            Message handlerMsg = new Message();
            handlerMsg.what = PUBLIC_MSG; //收到禁言消息，作为公有消息展示出去，也可以不展示
            handlerMsg.obj = msg;
            handler.sendMessage(handlerMsg);
        }

        @Override
        public void onKickOut() {
            Message kickOutMsg = new Message();
            kickOutMsg.what = KICK_OUT;
            handler.sendMessage(kickOutMsg);
        }

        @Override
        public void onLivePlayedTime(int playedTime) {
            if (playedTime >= 0 && dvr > 0) {

                stopLiveTimerTask();
                liveDuration = playedTime;
                startLiveTimerTask();
            }
        }

        @Override
        public void onLivePlayedTimeException(Exception e) {
            dwLive.getLivePlayedTime();
        }

        @Override
        public void isPlayedBack(boolean isPlayedBack) {
            if (!isPlayedBack) {
                // 收到这条信息相当于是重新加载，所以把界面初始化到直播状态
                stopPlaybackTimerTask();
                setLiveStatusView();
            }
        }


        @Override
        public void onStreamEnd(boolean isNormal) {
            isStop = true;
            isPrepared = false;
            Message msg = new Message();
            msg.what = FINISH;
            handler.sendMessage(msg);
        }

    };

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (dwLive != null && isLoginSuccess) {
            dwLive.start(holder.getSurface());
        }
        player.setScreenOnWhilePlaying(true);
        this.holder = surfaceHolder;
        setHolderBlack("请稍候……");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    boolean isSurfaceDestroyed = false;

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isSurfaceDestroyed = true;
        Log.i("demo", "surfaceDestroyed");
    }

    private void setHolderBlack(String text) {
        SurfaceHolder mHolder = sv.getHolder();
        Canvas canvas = mHolder.lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawColor(Color.BLACK);
        mHolder.unlockCanvasAndPost(canvas);

//        tvPlayMsg.setVisibility(View.VISIBLE);
//        tvPlayMsg.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.iv_back2:
                if (isPortrait()) {//竖屏就返回
                    finish();
                } else {//全屏就返回正常模式
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            //分享按钮
            case R.id.iv_share2:
                //TODO 分享
//                String imageUrl = "http://www.mob.com/files/apps/icon/1462255299.png";
//                ShareUtils.showShare(ShareUtils.APP, this, "爱股轩 - [爱股轩]", "爱股轩，您身边的股票技术分析师", imageUrl, "http://app.iguxuan.com/wap.html", null);
                break;
            // 暂停播放按钮
            case R.id.iv_control2:

                if (iv_control.isSelected()) {
                    iv_control.setSelected(false);
                    player.pause();
                    stopLiveTimerTask();
                } else {
                    iv_control.setSelected(true);
                    player.start();
                    dwLive.getLivePlayedTime();
                    startLiveTimerTask();
                }
                break;
            //全屏按钮
            case R.id.full_screen2:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏切换成竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    hideEditTextSoftInput(etFullscreen);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏切换成横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    hideEditTextSoftInput(etMsg);
                    gvFace.setVisibility(View.GONE);
                    ivSmile.setVisibility(View.VISIBLE);
                    ivKeyBoard.setVisibility(View.GONE);
                }
                break;
            //发送聊天按钮
            case R.id.btn_msg:
                sendChatMsg(false);
                break;
            //表情按钮
            case R.id.iv_smile:
                showSmileKeyboard();
                break;
            //切换文字输入弹幕按钮
            case R.id.iv_keyboard:
                showNormalKeyBoard();
                break;
            //全屏输入弹幕文本框，点击时取消隐藏控制界面
            case R.id.et_fullscreen2:
                handler.removeCallbacks(playHideRunnable);
                break;
            //全屏发送弹幕按钮
            case R.id.btn_fullscreen_send2:
                fullScreenSendText();
                break;
            case R.id.btn_qa:
                String qaMsg = etQA.getText().toString().trim();
                if (qaMsg != null && qaMsg.length() > 140) {
                    ToastUtils.showToast(this, "最多140个字符");
                    return;
                }
                if (!"".equals(qaMsg)) {
                    try {
                        dwLive.sendQuestionMsg(qaMsg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                etQA.setText("");
                hideEditTextSoftInput(etQA);
                break;
            //点赞
            case R.id.tv_live_teacher_zan2:
                if (mTeacherInfo != null && isLogin(this)) {
                    LiveHttpUtil.executeAddLike(this, liveId);
                }
                break;
            //关注
            case R.id.btn_teacher_follow2:
                if (mTeacherInfo != null && isLogin(this)) {
                    addFollow(teacherId, btn_teacher_follow.isSelected());
                } else {

                }
                break;

        }
    }

    // 添加 or 取消关注
    private void addFollow(String teacherId, boolean is_follow) {
        if (is_follow) {
            TeacherHttpUtil.delFollowForTeacherId(this, teacherId, Action.teacher_fg_del_follow);
        } else {
            TeacherHttpUtil.addFollowForTeacherID(this, teacherId, Action.teacher_fg_add_follow);
        }
    }


    /**
     * 全屏发送文字
     */
    private void fullScreenSendText() {
        String info = etFullscreen.getText().toString().trim();
        if (info != null && info.length() > 140) {
            ToastUtils.showToast(this, "最多140个字符");
        }
        if (!"".equals(info)) {
            dwLive.sendPublicChatMsg(info);
            etFullscreen.setText("");
        }
        hideEditTextSoftInput(etFullscreen);
        hidePlayHander();
    }


    private void hideEditTextSoftInput(EditText editText) {
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void hideKeyBoardEditTextSoftInput() {
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static class MyHandle extends WeakHandler<PlayLiveActivity> {

        public MyHandle(PlayLiveActivity owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            final PlayLiveActivity playLiveActivity = getOwner();
            switch (msg.what) {
                case SHOW_CONTROL:
                    playLiveActivity.setPlayControllerVisible(true);
                    break;
                case HIDE_CONTROL:
                    playLiveActivity.setPlayControllerVisible(false);
                    break;
                case HIDE_PALY:

                    if (playLiveActivity.loginTime < 3) {
                        playLiveActivity.loginLive();
                        playLiveActivity.loginTime++;
                    } else {
                        String errorMessage = "进入直播间失败，请重试！";
                        if (msg.obj != null) {
                            DWLiveException mDWLiveException = (DWLiveException) msg.obj;
                            if (mDWLiveException != null) {
                                errorMessage = StringUtils.replaceNullToEmpty(mDWLiveException.getMessage(), errorMessage);
                            }
                        }
                        playLiveActivity.tip(errorMessage);
                    }
                    break;
                case SHOW_PALY:
                    playLiveActivity.iv_control.setSelected(true);

//                    playLiveActivity.bindDataToView(playLiveActivity.mTeacherInfo);
                    break;
                case INIT_LVCHAT:
                    playLiveActivity.initTab(0);
                    playLiveActivity.initPagerItemView();
                    playLiveActivity.initPager();
                    playLiveActivity.initLvChat();
                    playLiveActivity.initLvQa();
                    playLiveActivity.initAbout();
                    break;
                case PUBLIC_MSG:
                    ChatMessage publicMsg = (ChatMessage) msg.obj;

                    if (playLiveActivity.mBarrageLayout.getVisibility() == View.VISIBLE) {
                        playLiveActivity.mBarrageLayout.addNewInfo(publicMsg.getMessage());
                    }
                    playLiveActivity.chatMsgs.add(publicMsg);
                    playLiveActivity.chatAdapter.notifyDataSetChanged();
                    playLiveActivity.lvChat.setSelection(playLiveActivity.chatMsgs.size() - 1);
                    break;
                case PRIVATE_QUESTION_MSG:
                case PRIVATE_ANSWER_MSG:
                    ChatMessage privateMsg = (ChatMessage) msg.obj;
                    playLiveActivity.chatMsgs.add(privateMsg);
                    playLiveActivity.chatAdapter.notifyDataSetChanged();
                    playLiveActivity.lvChat.setSelection(playLiveActivity.chatMsgs.size() - 1);
                    break;
                case QUESTION:
                    Question question = (Question) msg.obj;
                    String questionId = question.getId();
                    if (!playLiveActivity.qaMap.containsKey(questionId)) {
                        QAMsg qaMsg = new QAMsg();
                        qaMsg.setQuestion(question);
                        playLiveActivity.qaMap.put(questionId, qaMsg);
                        playLiveActivity.qaAdapter.notifyDataSetChanged();
                        playLiveActivity.lvQA.setSelection(playLiveActivity.qaMap.size() - 1);
                    }
                    break;
                case ANSWER:
                    Answer answer = (Answer) msg.obj;
                    String qaId = answer.getQuestionId();
                    int indexQa = new ArrayList<String>(playLiveActivity.qaMap.keySet()).indexOf(qaId);
                    if (indexQa == -1) {
                        return; //没有收到answer对应的问题，直接返回
                    }
                    QAMsg qaMsg = playLiveActivity.qaMap.get(qaId);
                    qaMsg.setAnswer(answer);
                    playLiveActivity.qaAdapter.notifyDataSetChanged();
                    playLiveActivity.lvQA.setSelection(indexQa);
                    break;
                case USER_COUNT:
                    if (playLiveActivity.tvCount != null && msg.obj != null) {
                        playLiveActivity.tvCount.setText((Integer) msg.obj + "人");
                        playLiveActivity.tv_teacher_fans.setText(String.format(Locale.CHINA, "%d人在线", (Integer) msg.obj));
                    }
                    break;
                case FINISH:
                    if (playLiveActivity.isFinish) {
                        return;
                    }
                    playLiveActivity.tip("直播结束");
                    playLiveActivity.setHolderBlack("直播结束");
                    break;
                case KICK_OUT:
                    playLiveActivity.isKickOut = true;
                    playLiveActivity.tip("已被踢出");
                    break;
                case NOT_START:

                    playLiveActivity.tip("直播未开始");
                    break;
                case FADE_OUT_INFO:
                    playLiveActivity.fadeOutInfo();
                    break;
            }
        }
    }

    public void tip(String content) {
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
                finish();
            }
        });
        mBuilder.create().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(HttpEvent event) {
        switch (event.getAction()) {
            case live_add_like:
                if (Constant.SUCCEED == event.getCode()) {
                    tv_live_teacher_zan.setText(String.format(Locale.CHINA, "%d赞", mTeacherInfo.getLive_info().getSupport_num() + 1));
                    tv_live_teacher_zan.setSelected(true);
                    ToastUtils.showToast(this, "点赞成功");
                } else {
                    LogUtils.e("点赞失败", StringUtils.replaceNullToEmpty(event.getMsg()));
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "点赞失败"));
                }
                break;
            case teacher_fg_del_follow:
                if (Constant.SUCCEED == event.getCode()) {
                    mTeacherInfo.setFans_count(mTeacherInfo.getFans_count() - 1);
                    tv_teacher_fans.setText(String.format(Locale.CHINA, "%d粉丝", mTeacherInfo.getFans_count()));
                    btn_teacher_follow.setSelected(false);
                    btn_teacher_follow.setText("＋关注");
                } else {
                    LogUtils.e("关注失败", StringUtils.replaceNullToEmpty(event.getMsg()));
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "关注失败"));
                }
                break;
            case teacher_fg_add_follow:
                if (Constant.SUCCEED == event.getCode()) {
                    mTeacherInfo.setFans_count(mTeacherInfo.getFans_count() + 1);
                    tv_teacher_fans.setText(String.format(Locale.CHINA, "%d粉丝", mTeacherInfo.getFans_count()));
                    btn_teacher_follow.setSelected(true);
                    btn_teacher_follow.setText("已关注");
                } else {
                    LogUtils.e("取消关注失败", StringUtils.replaceNullToEmpty(event.getMsg()));
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "取消关注失败"));
                }
                break;
            case off_line:
                if (dwLive != null) {
                    dwLive.stop();
                    isLoginSuccess = false;
                }
                loginLive();
                getTeacherInfo();
                break;
            case live_teacher_info:
                if (Constant.SUCCEED == event.getCode()) {
                    try {
                        DebugUtils.dd("Live paling info : " + event.getData().toString());
                        JSONObject tempJson = new JSONObject(event.getData().toString());
                        JSONObject data = tempJson.getJSONObject("data");
                        String info = data.getString("info");
                        mTeacherInfo = new DataConverter<TeacherInfo>().JsonToObject(info, TeacherInfo.class);
                        bindDataToView();
                        initAbout();
                    } catch (Exception e) {
                        // mErrView.StopLoading(event.getCode(), event.getMsg());
                        LogUtils.e(TAG, "解析老师信息异常", e);
                    }
                } else {
                    LogUtils.e(TAG, "获取老师信息失败：" + event.getMsg());
                }
                break;
        }
    }

    private void getTeacherInfo() {
        TeacherHttpUtil.fetchTeacherInfo(this, teacherId, MyApplication.getUser().getUser_id(), liveId, Action.live_teacher_info);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isOnPause && !isSurfaceDestroyed && isLoginSuccess) {
            initAndStartLivePlay();
        }
        isOnPause = false;
    }

    private boolean isOnPause = false;

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null && player.isPlaying()) {
            player.pause();
        }
        qaMap.clear();
        dwLive.stop();
        isPrepared = false;
        isOnPause = true;
        stopLiveTimerTask();
        stopPlaybackTimerTask();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (dwLive != null) {
            dwLive.stop();
        }
        if (handler != null && playHideRunnable != null) {
            handler.removeCallbacks(playHideRunnable);
        }
        if (localBroadcastManager != null && mReceiver != null) {
            localBroadcastManager.unregisterReceiver(mReceiver);
        }
        isFinish = true;
        isLoginSuccess = false;
        super.onDestroy();
    }

    public boolean isLogin(final Context context) {
        User user = MyApplication.getUser();
        if (user == null) {
            LoginPromptDialog loginPromptDialog = new LoginPromptDialog(context);
            loginPromptDialog.setCallBack(new LoginPromptDialog.CallBack() {
                @Override
                public void onCancel(DialogInterface dialog, int which) {

                }

                @Override
                public void onLogin(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }

                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            loginPromptDialog.show();
            return false;
        }
        return true;
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url)) {
                Intent intent = new Intent(PlayLiveActivity.this, WebActivity.class);
                intent.putExtra(Constant.KEY_URL, url);
                startActivity(intent);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

    }

    private void showOverlay() {

    }

    /**
     * hider overlay
     */
    private void hideOverlay() {
        if (mShowing) {
            mShowing = false;
        }
    }

    /**
     * show/hide the overlay
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isPrepared) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (rl_control.getVisibility() == View.VISIBLE) {
                    handler.removeCallbacks(playHideRunnable);
                    handler.sendEmptyMessage(HIDE_CONTROL);
                } else {
                    handler.sendEmptyMessage(SHOW_CONTROL);
                    hidePlayHander();
                }
            }
        }
        if (mIsLocked) {
            // locked, only handle show/hide & ignore all actions
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (!mShowing) {
                    showOverlay();
                } else {
                    hideOverlay();
                }
            }
            return false;
        }

        DisplayMetrics screen = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(screen);

        if (mSurfaceYDisplayRange == 0)
            mSurfaceYDisplayRange = Math.min(screen.widthPixels, screen.heightPixels);

        float y_changed = event.getRawY() - mTouchY;
        float x_changed = event.getRawX() - mTouchX;

        // coef is the gradient's move to determine a neutral zone
        float coef = Math.abs(y_changed / x_changed);
        float xgesturesize = ((x_changed / screen.xdpi) * 2.54f);

        /* Offset for Mouse Events */
        int[] offset = new int[2];
        sv.getLocationOnScreen(offset);
        int xTouch = Math.round((event.getRawX() - offset[0]) * mVideoWidth / sv.getWidth());
        int yTouch = Math.round((event.getRawY() - offset[1]) * mVideoHeight / sv.getHeight());

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // Audio
                mTouchY = event.getRawY();
                mVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mTouchAction = TOUCH_NONE;
                // Seek
                mTouchX = event.getRawX();
                // Mouse events for the core
//                mVideoView.sendMouseEvent(MotionEvent.ACTION_DOWN, 0, xTouch, yTouch);
                break;

            case MotionEvent.ACTION_MOVE:
                if (coef > 2) {
                    // Volume (Up or Down - Right side)
                    if (!mEnableBrightnessGesture || mTouchX > (screen.widthPixels / 2)) {
                        doVolumeTouch(y_changed);
                    }
                    // Brightness (Up or Down - Left side)
                    if (mEnableBrightnessGesture && mTouchX < (screen.widthPixels / 2)) {
                        doBrightnessTouch(y_changed);
                    }
                }
                // Mouse events for the core
//                mVideoView.sendMouseEvent(MotionEvent.ACTION_MOVE, 0, xTouch, yTouch);

                // No volume/brightness action if coef < 2 or a secondary display is connected
                // Seek (Right or Left move)
                doSeekTouch(coef, xgesturesize, false);
                break;

            case MotionEvent.ACTION_UP:
                // Mouse events for the core
//                LibVLC.sendMouseEvent(MotionEvent.ACTION_UP, 0, xTouch, yTouch);
                Log.i("ACTION_UP", "1");
                // Audio or Brightness
                if (mTouchAction == TOUCH_NONE) {
                    if (!mShowing) {
                        Log.i("ACTION_UP", "2");
                        showOverlay();
                    } else {
                        Log.i("ACTION_UP", "3");
                        hideOverlay();
                    }
                }
                // Seek
                doSeekTouch(coef, xgesturesize, true);
                break;
        }
        return mTouchAction != TOUCH_NONE;
    }

    private void doSeekTouch(float coef, float gesturesize, boolean seek) {
        // No seek action if coef > 0.5 and gesturesize < 1cm
        if (coef > 0.5 || Math.abs(gesturesize) < 1)
            return;

        if (mTouchAction != TOUCH_NONE && mTouchAction != TOUCH_SEEK)
            return;
        mTouchAction = TOUCH_SEEK;

        // Always show seekbar when searching
        if (!mShowing) showOverlay();

//        dwLive.getLivePlayedTime();
        long length = liveDuration * 1000;
        long time = getRelTime() * 1000;

        // Size of the jump, 10 minutes max (600000), with a bi-cubic progression, for a 8cm gesture
        int jump = (int) (Math.signum(gesturesize) * ((600000 * Math.pow((gesturesize / 8), 4)) + 3000));

        // Adjust the jump
        if ((jump > 0) && ((time + jump) > length))
            jump = (int) (length - time);
        if ((jump < 0) && ((time + jump) < 0)) {
            jump = (int) -time;
        }

        //Jump !
        if (seek && length > 0) {
            int pr = (int) ((float) seekBarMax * (float) (((int) (time + jump) / 1000)) / (float) liveDuration);
            LogUtils.e("sdsdsd", "time =" + time);
            LogUtils.e("sdsdsd", "jump=" + jump);
            LogUtils.e("sdsdsd", "time + jump=" + (time + jump));
            LogUtils.e("sdsdsd", "pr=" + pr);
            progress = pr;
            seekBar.setProgress(pr);
            try {
                currentPosition = getRelTime();
                dwLive.startPlayedBackPlay(liveDuration - currentPosition);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DWLiveException e) {
                e.printStackTrace();
            }

        }
        if (length > 0) {
            //Show the jump's size
            showInfo(String.format("%s/%s",
                    TimeFormatUtil.getTime(time + jump),
                    TimeFormatUtil.getTime(length)), 1000, -1);
        } else {
            showInfo("播放时不支持定位的流", 1000, -1);
        }

    }


    /**
     * 调整音量
     *
     * @param y_changed
     */
    private void doVolumeTouch(float y_changed) {
        if (mTouchAction != TOUCH_NONE && mTouchAction != TOUCH_VOLUME)
            return;
        int delta = -(int) ((y_changed / mSurfaceYDisplayRange) * mAudioMax);
        int vol = (int) Math.min(Math.max(mVol + delta, 0), mAudioMax);
        if (delta != 0) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
            mTouchAction = TOUCH_VOLUME;
            showInfo(String.format(Locale.CHINA, "声音 %02d", vol), 1000, R.mipmap.ic_volume_up_white_48dp);
        }
    }

    private void initBrightnessTouch() {
        float brightnesstemp = 0.01f;
        // Initialize the layoutParams screen brightness
        try {
            brightnesstemp = Settings.System.getInt(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS) / 255.0f;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightnesstemp;
        getWindow().setAttributes(lp);
        mIsFirstBrightnessGesture = false;
    }

    /**
     * 调整亮度
     *
     * @param changed
     */
    private void doBrightnessTouch(float changed) {
        if (mTouchAction != TOUCH_NONE && mTouchAction != TOUCH_BRIGHTNESS)
            return;
        if (mIsFirstBrightnessGesture) initBrightnessTouch();
        mTouchAction = TOUCH_BRIGHTNESS;

        // Set delta : 0.07f is arbitrary for now, it possibly will change in the future
        float delta = -changed / mSurfaceYDisplayRange * 0.07f;

        // Estimate and adjust Brightness
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = Math.min(Math.max(lp.screenBrightness + delta, 0.01f), 1);

        // Set Brightness
        getWindow().setAttributes(lp);
        showInfo(String.format(Locale.CHINA, "亮度 %02d", Math.round(lp.screenBrightness * 15)), 1000, R.mipmap.ic_wb_sunny_white_48dp);
    }

    /**
     * Show text in the info view for "duration" milliseconds
     *
     * @param text     sate text
     * @param duration duration
     */
    private void showInfo(String text, int duration, int image) {
        rl_mInfo.setVisibility(View.VISIBLE);
        mInfo.setText(text);
        if (image == -1) {
            iv_mInfo.setVisibility(View.GONE);
        } else {
            iv_mInfo.setVisibility(View.VISIBLE);
            ImageDisplayUtil.displayImage(this, iv_mInfo, image);
        }
        handler.removeMessages(FADE_OUT_INFO);
        handler.sendEmptyMessageDelayed(FADE_OUT_INFO, duration);
    }

    /**
     * 隐藏控件
     */
    private void fadeOutInfo() {
        if (rl_mInfo != null) {
            if (rl_mInfo.getVisibility() == View.VISIBLE)
                rl_mInfo.startAnimation(AnimationUtils.loadAnimation(
                        PlayLiveActivity.this, android.R.anim.fade_out));
            rl_mInfo.setVisibility(View.INVISIBLE);
        }
    }

}