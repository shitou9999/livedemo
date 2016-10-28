package tv.kuainiu.ui.articles.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.CollectionMessageHttpUtil;
import tv.kuainiu.command.http.SupportHttpUtil;
import tv.kuainiu.command.http.TeacherHttpUtil;
import tv.kuainiu.command.http.UserHttpRequest;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.ArticleDetailsEntity2;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.CatalogType;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.activity.WebActivity;
import tv.kuainiu.ui.comments.CommentListActivity;
import tv.kuainiu.ui.comments.fragmet.PostCommentListFragment;
import tv.kuainiu.ui.teachers.activity.TeacherZoneActivity;
import tv.kuainiu.umeng.UMEventManager;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.LoadingProgressDialog;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.ShareUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

import static tv.kuainiu.R.id.ci_avatar;


/**
 * <p>
 * 文章详情页(预留了调整字体大小的代码，已注释。如不需要可以删除)
 * </p>
 * <p>
 * 在该页停至少停留 7 秒算一次有效阅读，向友盟添加一个"阅读文章"的计数事件
 * </p>
 */
public class PostZoneActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "PostZoneActivity";
    private static final String TAG_LIFE = "PostZoneActivity_LIFE";
    private static final int SOURCE_TEACHER = 0x1; // 文章来自老师
    private static final int SOURCE_PROGRAM = 0x2; // 文章来自节目
    @BindView(R.id.tbv_title)
    TitleBarView tbv_title;
    @BindView(R.id.rl_top_navigation)
    RelativeLayout mRlTopNavigation;
    @BindView(ci_avatar)
    CircleImageView mCivTeacherHead;
    @BindView(R.id.tv_follow_button)
    TextView mTVFollowButton;
    @BindView(R.id.ptr_rv_layout)
    SwipeRefreshLayout mContentFrameLayout;
    @BindView(R.id.web_content)
    WebView mWebContent;
    @BindView(R.id.tv_favour)
    TextView mTvFavour;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.tv_share)
    TextView mTvShare;
    @BindView(R.id.tv_collect)
    TextView mTvCollect;
    @BindView(R.id.pb_loading)
    ProgressBar pb_loading;

    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.radioButton1)
    RadioButton mRadioButton1;
    @BindView(R.id.radioButton2)
    RadioButton mRadioButton2;
    @BindView(R.id.radioButton3)
    RadioButton mRadioButton3;
    @BindView(R.id.radioButton4)
    RadioButton mRadioButton4;
    @BindView(R.id.ivIsVip)
    ImageView mIvIsVip;
    @BindView(R.id.tvTheme)
    TextView mTvTheme;
    @BindView(R.id.tvTeacherName)
    TextView mTvTeacherName;
    @BindView(R.id.tv_follow_number)
    TextView mTvFollowNumber;
    @BindView(R.id.line_bottom)
    View lineBottom;
    @BindView(R.id.ll_bottom_bar)
    LinearLayout llBottomBar;
    /**
     * 来源
     */
    private int mSource;
    /**
     * News id
     */
    private String mId;
    /**
     * Cat id
     */
    private String mCatId;
    /**
     * 是否收藏
     */
    private boolean mIsCoolect = false;
    /**
     * 是否赞过
     */
    private boolean mIsFavour = false;
    /**
     * 收到的赞数
     */
    private int mCurFavourCount;
    /**
     * 评论总数
     */
    private int mCurCommentCount;
    /**
     * 文章url
     */
    private String mArticleUrl;
    /**
     * 文章标题
     */
    private String mPostTitle;
    /**
     * 内容简述
     */
    private String mPostDescription;
    /**
     * 封面
     */
    private String mPostThumb;


    String commentid = "";
    String id = "";
    String is_reply = "";

    private Map<String, String> map = new HashMap<>();
    private PopupWindow popupWindow;
    private String session_id;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver mReceiver;
    private ArticleDetailsEntity2 mDetailsEntity;

    /**
     * 停留7秒则算一次有效阅读
     */
    private static final int EFFECTIVE_READING_TIME = 7000;
    private MyCountDownTimer mDownTimer;
    private boolean isReaded = false;
    private String shareUrl;

    public static void intoNewIntent(Context context, String id, String catId) {
        Intent intent = new Intent(context, PostZoneActivity.class);
        intent.putExtra(Constant.KEY_ID, id);
        intent.putExtra(Constant.KEY_CATID, catId);
        context.startActivity(intent);
    }

    private void initData(Intent data) {
        mId = data.getStringExtra(Constant.KEY_ID);
        mCatId = data.getStringExtra(Constant.KEY_CATID);

        int gdqcArticle = CatalogType.CatalogTypeGDQCArticle.type();
        int jqfbArticle = CatalogType.CatalogTypeJQFBArticle.type();

        mSource = (String.valueOf(gdqcArticle).equals(mCatId) || String.valueOf(jqfbArticle).equals(mCatId)) ?
                SOURCE_PROGRAM : SOURCE_TEACHER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_zone);
        ButterKnife.bind(this);
        initData(getIntent());
        mRadioGroup.setVisibility(View.GONE);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mWebContent == null) {
                    return;
                }
                switch (checkedId) {
                    case R.id.radioButton1:
                        mWebContent.getSettings().setTextZoom(50);
                        break;

                    case R.id.radioButton2:
                        mWebContent.getSettings().setTextZoom(75);
                        break;

                    case R.id.radioButton3:
                        mWebContent.getSettings().setTextZoom(100);
                        break;

                    case R.id.radioButton4:
                        mWebContent.getSettings().setTextZoom(125);
                        break;
                }
            }
        });

        mDownTimer = new MyCountDownTimer(EFFECTIVE_READING_TIME, 1000);
        mDownTimer.start();
        initView();
        initListener();
    }


    @Override
    protected void onStop() {
        if (isReaded) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UMEventManager.onEvent(PostZoneActivity.this, UMEventManager.ID_VIEW_ARTICLE);
                }
            }).start();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mWebContent.clearFormData();
        mCurFavourCount = 0;
        mCurCommentCount = 0;
        if (localBroadcastManager != null && mReceiver != null) {
            localBroadcastManager.unregisterReceiver(mReceiver);
        }
        if (mDownTimer != null) {
            mDownTimer.cancel();
        }
        super.onDestroy();
    }

    protected void initView() {
        registerBroadcast();
        mContentFrameLayout.setColorSchemeColors(Theme.getLoadingColor());

        mWebContent.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebContent.getSettings().setJavaScriptEnabled(true);
        mWebContent.getSettings().setTextZoom(100);

        mWebContent.setWebViewClient(new MyWebViewClient());
        mWebContent.setWebChromeClient(new MyWebChromeClient());
        mWebContent.addJavascriptInterface(new JavascriptInterface(this),
                "jslistner");
        mWebContent.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebContent.removeJavascriptInterface("accessibility");
        mWebContent.removeJavascriptInterface("accessibilityTraversal");
        LoadingProgressDialog.startProgressDialog(this);
        fetchPostDetailsInfo();

        switch (mSource) {
            case SOURCE_PROGRAM:
                mTVFollowButton.setText(getString(R.string.subscription_new));
                break;

            case SOURCE_TEACHER:
                mTVFollowButton.setText(getString(R.string.follow_new));
                break;
        }
        mArticleUrl = Api.TEST_DNS_API_HOST + "/news/show_news" + prepareParamFetchContent();
        if (mArticleUrl.contains("?")) {
            mArticleUrl += "&appVersion=1.1.5";
        } else {
            mArticleUrl += "?appVersion=1.1.5";
        }
        loadUrl();
    }

    private void registerBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.INTENT_ACTION_GET_CUSTOM)) {
                    fetchPostDetailsInfo();
                }
            }

        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.INTENT_ACTION_GET_CUSTOM);
        localBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }


    private void autoLogin() {
        User user = MyApplication.getUser();
        if (user != null) {
            UserHttpRequest.authLogin(this, user.getUser_id(), user.getPhone(), Action.AUTO_LOGIN);
        } else {//提示去登录
            new LoginPromptDialog(this).show();
        }
    }

    @OnClick(ci_avatar)
    public void onClick() {
        if (mDetailsEntity != null) {
            TeacherZoneActivity.intoNewIntent(this, mDetailsEntity.getUser_id());
        }
    }

    // js通信接口
    public class JavascriptInterface {

        public Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void showImage(final String imagePath, final int index) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(PostZoneActivity.this, PicturePreviewFullScreenActivity.class);
                    ArrayList<String> imageList = new ArrayList<>();
                    String[] imagePaths = imagePath.split(",");
                    for (int i = 0; i < imagePaths.length; i++) {
                        imageList.add(imagePaths[i]);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(PicturePreviewFullScreenActivity.PICTURE_PREVIEW_KEY, imageList);
                    bundle.putInt(PicturePreviewFullScreenActivity.PICTURE_PREVIEW_INDEX_KEY, index);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }

        /**
         * -101需要重新登陆
         *
         * @param content 内容
         */
        @android.webkit.JavascriptInterface
        public void login(final String content) {
            LogUtils.i("course", "content=" + content);
            if ("-101".equals(content)) {
                autoLogin();
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            boolean flag = false;
            if (!TextUtils.isEmpty(url)) {
                map.clear();
                try {
                    String decodeUrl = URLDecoder.decode(url, "UTF-8");
                    String[] parameters;

                    if (decodeUrl.contains("@@")) {
                        parameters = decodeUrl.split("@@");
                        for (int i = 0; i < parameters.length; i++) {
                            if (parameters[i].contains("=")) {
                                String[] key_value = parameters[i].split("=", 2);
                                map.put(key_value[0], key_value[1]);
                            }
                        }
                    }
                    LogUtils.i("WebView_url", "url=" + url);
                    LogUtils.i("WebView_url", "map=" + map.toString());
                    if (map.containsKey("act") && "hf".equals(map.get("act"))) {
                        flag = true;
                        //回复
                        showCommentPopupWindow();
                    } else if (map.containsKey("act") && "support_comment".equals(map.get("act")) && map.containsKey("commentid")) {
                        //点赞
                        if (map.containsKey("id")) {
                            flag = true;
                            commentid = map.get("id");
                            favourForCommentId(commentid);
                        }

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (!flag) {
                    Intent intent = new Intent(PostZoneActivity.this, WebActivity.class);
                    intent.putExtra(Constant.KEY_URL, url);
                    startActivity(intent);
                }
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        private void showCommentPopupWindow() {
            View contentView = getLayoutInflater().inflate(R.layout.popup_keyboard_edit_comment, null);

            popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            final EditText et_edit_comment = (EditText) contentView.findViewById(R.id.et_edit_comment);
            Button btn_publish = (Button) contentView.findViewById(R.id.btn_publish);
            View v_close = contentView.findViewById(R.id.v_close);
            v_close.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != popupWindow) {
                        popupWindow.dismiss();
                    }
                }
            });

            if (map.containsKey("content")) {
                et_edit_comment.setHint(String.format(Locale.CHINA, "回复：用户%s", map.get("content")));
            }
            if (map.containsKey("news_id")) {
                id = map.get("news_id");
            }
            if (map.containsKey("commentid")) {
                commentid = map.get("commentid");
            }
            btn_publish.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!MyApplication.isLogin()) {
                        LoginPromptDialog loginPromptDialog = new LoginPromptDialog(PostZoneActivity.this);
                        loginPromptDialog.show();
                        return;
                    }
                    String comment = et_edit_comment.getText().toString();
                    if (TextUtils.isEmpty(comment)) {
                        ToastUtils.showToast(PostZoneActivity.this, "评论不能为空");
                    } else {
//                        CommentHttpUtil.addComment(PostZoneActivity.this, mId, mCatId, comment, commentid, StringUtils.replaceNullToEmpty(MyApplication.getUser().getNickname()), is_reply);
                    }
                }
            });
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));
            popupWindow.setAnimationStyle(R.style.PopupAnimation);
            popupWindow.setContentView(contentView);
            popupWindow.setOutsideTouchable(true);//不能在没有焦点的时候使用
            popupWindow.setFocusable(true);
            popupWindow.setAnimationStyle(R.anim.push_bottom_out);
            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            addImageClickListner();
            if (pb_loading != null) {
                pb_loading.setVisibility(View.GONE);
            }
            mContentFrameLayout.setRefreshing(false);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (pb_loading != null) {
                pb_loading.setVisibility(View.VISIBLE);
            }
        }

    }


    // 注入js函数监听
    private void addImageClickListner() {

        // 这段js函数的功能就是，遍历所有的a标签，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
        if (mWebContent == null) {
            return;
        }
        mWebContent.loadUrl("javascript:(function(){"
                + "var imageObjs = document.getElementsByTagName(\"img\");"
                + "var imageObjsALL = [];"
                + "for(var i=0;i<imageObjs.length;i++) {"
                + "imageObjsALL.push(imageObjs[i].src);"
                + "imageObjs[i].index=i;"
                + " imageObjs[i].onclick=function(){" + " window.jslistner.showImage(imageObjsALL.join(\",\"),this.index);" + "return false;  " + "}"
                + "}"
                + " })()");
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (pb_loading != null) {
                pb_loading.setProgress(newProgress);
                if (newProgress + 2 < 100) {
                    pb_loading.setSecondaryProgress(newProgress + 2);
                }
            }
        }
    }

    protected void initListener() {
        mContentFrameLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPostDetailsInfo();//下拉刷新
            }
        });
        mTvFavour.setOnClickListener(this);
        mTvComment.setOnClickListener(this);
        mTvShare.setOnClickListener(this);
        mTvCollect.setOnClickListener(this);
        mTVFollowButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_favour:
                if (!MyApplication.isLogin()) {
                    new LoginPromptDialog(this).show();
                    return;
                }
                SupportHttpUtil.supportVideoDynamics(this, mDetailsEntity.getCat_id(), mDetailsEntity.getId());
                break;

            case R.id.tv_comment:
                CommentListActivity.intoNewIntent(this, PostCommentListFragment.MODE_ARTICLE, mId, mCatId);
//                Intent intent = new Intent(this, CommentListActivity.class);
//                intent.putExtra(Constant.KEY_ID, mId);
//                intent.putExtra(Constant.KEY_CATID, mCatId);
//                startActivity(intent);
                break;

            case R.id.tv_share:
                ShareUtils.showShare(ShareUtils.ARTICLE, this, mPostTitle, mPostDescription, mPostThumb, shareUrl, null);
                break;

            case R.id.tv_collect:
                if (!MyApplication.isLogin()) {
                    new LoginPromptDialog(this).show();
                    return;
                }
                if (!mIsCoolect) {
                    addCollect();
                } else {
                    delCollect();
                }
                break;

            case R.id.tv_follow_button:
                if (!MyApplication.isLogin()) {
                    new LoginPromptDialog(PostZoneActivity.this).show();
                } else {
                    if (mDetailsEntity == null) {
                        return;
                    }
                    if (mDetailsEntity.getTeacher_info() != null) {
                        if (Constant.FOLLOWED == mDetailsEntity.getTeacher_info().getIs_follow()) {
                            TeacherHttpUtil.delFollowForTeacherId(PostZoneActivity.this, mDetailsEntity.getTeacher_info().getId(), Action.del_follow);
                        } else {
                            TeacherHttpUtil.addFollowForTeacherID(PostZoneActivity.this, mDetailsEntity.getTeacher_info().getId(), Action.add_follow);
                        }
                    }
                }
                break;


        }
    }

    private void setButtonEnabled(boolean enabled) {
        mTvFavour.setEnabled(enabled);
        mTvComment.setEnabled(enabled);
        mTvShare.setEnabled(enabled);
        mTvCollect.setEnabled(enabled);
    }

    private void fetchPostDetailsInfo() {
        OKHttpUtils.getInstance().syncGet(this, Api.VIDEO_OR_POST_DETAILS + prepareParamFetchContent(), Action.post_details);
    }

    private String prepareParamFetchContent() {
        if (!MyApplication.isLogin()) {
            return "?id=" + mId + "&has_content=no";
        } else {
            return "?id=" + mId + "&user_id=" + MyApplication.getUser().getUser_id() + "&has_content=no";
        }
    }

    private String prepareParamForFavour() {
        StringBuilder sb = new StringBuilder("?catid=");
        sb.append(mCatId);
        sb.append("&news_id=");
        sb.append(mId);
        sb.append("&device=");
        sb.append(MyApplication.getInstance().getDeviceId());
        return sb.toString();
    }


    private void loadUrl() {
        String url = mArticleUrl;
        session_id = PreferencesUtils.getString(this, Constant.SESSION_ID, "");
        if (url.contains("?")) {
            url = url + "&app_type=android&session_id=" + StringUtils.replaceNullToEmpty(session_id);
        } else {
            url = url + "?app_type=android&session_id=" + StringUtils.replaceNullToEmpty(session_id);
        }

        DebugUtils.dd("now url " + url);
        mWebContent.loadUrl(url);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(HttpEvent event) {
        switch (event.getAction()) {
            case AUTO_LOGIN:
                if (Constant.SUCCEED == event.getCode()) {
                    JSONObject jsonObject = event.getData();
                    try {
                        if (jsonObject != null && jsonObject.has("session_id")) {
                            session_id = jsonObject.getString("session_id");
                            PreferencesUtils.putString(this, Constant.SESSION_ID, session_id);
                            loadUrl();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showToast(PostZoneActivity.this, event.getMsg());
                }

                break;
            case post_details:
                getContent(event);
                break;
            case video_favour:
                if (Constant.SUCCEED == event.getCode()) {
                    DebugUtils.showToast(PostZoneActivity.this, "支持成功");
                    mIsFavour = true;
                    mCurFavourCount += 1;
                    bindViewForFavour();
                } else {
                    DebugUtils.showToastResponse(this, event.getMsg());
                }
                break;
            case add_collect:
                if (Constant.SUCCEED == event.getCode()) {
                    mIsCoolect = true;
                    bindViewForCollect();
                    DebugUtils.showToast(this, "收藏成功");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("type", "article");
                    UMEventManager.onEvent(PostZoneActivity.this, UMEventManager.ID_COLLECTION, map);
                } else {
                    DebugUtils.showToastResponse(this, event.getMsg());
                }
                break;
            case del_collect:
                if (Constant.SUCCEED == event.getCode()) {
                    mIsCoolect = false;
                    bindViewForCollect();
                    DebugUtils.showToast(this, "取消收藏成功");
                }
                break;
            case add_comment:
                if (Constant.SUCCEED == event.getCode()) {
                    // mIsCoolect = false;
                    bindViewForCollect();
                    DebugUtils.showToast(this, "评论成功");
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                } else {
                    if (TextUtils.isEmpty(event.getMsg())) {
                        DebugUtils.showToast(this, "评论失败");
                    } else {
                        DebugUtils.showToast(this, event.getMsg());
                    }
                }
                break;
            case favour_comment:
                if (Constant.SUCCEED == event.getCode()) {
                    //  mIsCoolect = false;
                    bindViewForCollect();
                    DebugUtils.showToast(this, "点赞成功");
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                } else {
                    if (TextUtils.isEmpty(event.getMsg())) {
                        DebugUtils.showToast(this, "点赞失败");
                    } else {
                        DebugUtils.showToast(this, event.getMsg());
                    }
                }
                break;


            case add_follow:
                if (Constant.SUCCEED == event.getCode()) {
                    mDetailsEntity.getTeacher_info().setIs_follow(Constant.FAVOURED);
                    bindTopNavigation(mDetailsEntity);
                } else {
                    DebugUtils.showToastResponse(PostZoneActivity.this, event.getMsg());
                }
                break;

            case del_follow:
                if (Constant.SUCCEED == event.getCode()) {
                    mDetailsEntity.getTeacher_info().setIs_follow(Constant.UNFAVOUR);
                    bindTopNavigation(mDetailsEntity);
                } else {
                    DebugUtils.showToastResponse(PostZoneActivity.this, event.getMsg());
                }
                break;

//            case add_subscribe:
//                DebugUtils.dd("entity add_sub response " + event.getCode() + event.getMsg());
//                if (Constant.SUCCEED == event.getCode()) {
//                    mDetailsEntity.getCat_info().setIs_subscibe(Constant.SUBSCRIBEED);
//                    bindTopNavigation(mDetailsEntity);
//                } else {
//                    DebugUtils.showToastResponse(PostZoneActivity.this, event.getMsg());
//                }
//                break;
//
//            case del_subscribe:
//                DebugUtils.dd("entity del_sub response " + event.getCode() + event.getMsg());
//                if (Constant.SUCCEED == event.getCode()) {
//                    mDetailsEntity.getCat_info().setIs_subscibe(Constant.UNSUBSCRIBE);
//                    bindTopNavigation(mDetailsEntity);
//                } else {
//                    DebugUtils.showToastResponse(PostZoneActivity.this, event.getMsg());
//                }
//                break;
        }
    }

    private void getContent(HttpEvent event) {
//        if (Action.post_details == event.getAction()) {
        LoadingProgressDialog.stopProgressDialog();
        setButtonEnabled(0 == event.getCode());
        if (Constant.SUCCEED == event.getCode()) {
            String json = event.getData().optString("data");
            try {
                JSONObject object = new JSONObject(json);
                JSONObject info = object.optJSONObject("info");

                mDetailsEntity = new DataConverter<ArticleDetailsEntity2>().JsonToObject(info.toString(), ArticleDetailsEntity2.class);

                // log
                DebugUtils.dd(info.toString());

                mIsCoolect = Constant.COLLECTED == info.optInt("collected");
                mIsFavour = Constant.FAVOURED == info.optInt("is_support");
                mCurFavourCount = info.optInt("support_num");
                mCurCommentCount = info.optInt("comment_num");
                String catName = info.optString("catname");
                // 文章简述
                mPostDescription = StringUtils.replaceNullToEmpty(info.optString("description"));
                mPostThumb = StringUtils.replaceNullToEmpty(info.optString("thumb"));
                mPostTitle = StringUtils.replaceNullToEmpty(info.optString("title"));

                tbv_title.setText(catName);
                shareUrl =StringUtils.replaceNullToEmpty(mDetailsEntity.getUrl());
                bindViewForCollect();
                bindViewForFavour();
                mTvComment.setText(StringUtils.getDecimal2(mCurCommentCount, Constant.TEN_THOUSAND,
                        String.format(Locale.CHINA, "评论(%d万)", mCurCommentCount), String.format(Locale.CHINA, "评论(%d)", mCurCommentCount)));
//
                bindTopNavigation(mDetailsEntity);


                // log
                DebugUtils.dd("request url : " + Api.TEST_DNS_API_HOST + "/news/show_news" + prepareParamFetchContent());

            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }


    private void bindTopNavigation(String headUrl, String name) {
        ImageDisplayUtil.displayImage(this, mCivTeacherHead, headUrl, R.mipmap.head_nor);
        mTvTeacherName.setText(name);
//        mTVFollowButton.setSelected(isFollow);
    }


    private void bindTopNavigation(ArticleDetailsEntity2 entity) {
        if (entity == null) {
            return;
        }
        // log
        DebugUtils.dd("---------a entity : " + entity.toString());

        if (mDetailsEntity.getTeacher_info() == null) {
            mRlTopNavigation.setVisibility(View.GONE);
        }

        if (mDetailsEntity.getTeacher_info() != null) {
            bindTopNavigation(mDetailsEntity.getTeacher_info().getAvatar(), mDetailsEntity.getTeacher_info().getNickname());
            boolean flag = Constant.FOLLOWED == mDetailsEntity.getTeacher_info().getIs_follow();
            mTVFollowButton.setSelected(flag);
            if (flag) {
                mTVFollowButton.setText(getString(R.string.followed));
            } else {
                mTVFollowButton.setText(getString(R.string.follow_new));
            }
            mTvTheme.setText(StringUtils.replaceNullToEmpty(mDetailsEntity.getTeacher_info().getSlogan()));
        }
//        if (entity.getCat_info() != null && entity.getDaoshi_info() == null) {
//            DebugUtils.dd("entity program info : " + entity.getCat_info().toString());
//            bindTopNavigation(entity.getCat_info().getImage(), entity.getCatname());
//            boolean flag = Constant.SUBSCRIBEED == entity.getCat_info().getIs_subscibe();
//            mTVFollowButton.setSelected(flag);
//            if (flag) {
//                mTVFollowButton.setText(getString(R.string.subscriptioned));
//            } else {
//                mTVFollowButton.setText(getString(R.string.subscription_new));
//            }
//        }
    }


    private void bindViewForCollect() {
        mTvCollect.setSelected(mIsCoolect);
        if (mIsCoolect) {
//            mTvCollect.setTextColor(TextColorUtil.generateColor(R.color.def_DisableTextColor));
            mTvCollect.setText("已收藏");
        } else {
//            mTvCollect.setTextColor(TextColorUtil.generateColor(R.color.colorGrey333333));
            mTvCollect.setText("收藏");
        }
    }

    private void bindViewForFavour() {
        String tempFavour;
        mTvFavour.setSelected(mIsFavour);
        if (mIsFavour) {
//            mTvFavour.setTextColor(TextColorUtil.generateColor(R.color.def_DisableTextColor));
            tempFavour = StringUtils.getDecimal2(mCurFavourCount, Constant.TEN_THOUSAND,
                    String.format(Locale.CHINA, "%d万", mCurFavourCount), String.format(Locale.CHINA, "%d", mCurFavourCount));
        } else {
//            mTvFavour.setTextColor(TextColorUtil.generateColor(R.color.colorGrey333333));
            tempFavour = StringUtils.getDecimal2(mCurFavourCount, Constant.TEN_THOUSAND,
                    String.format(Locale.CHINA, "%d万", mCurFavourCount), String.format(Locale.CHINA, "%d", mCurFavourCount));
        }
        mTvFavour.setText(tempFavour);
    }

    /**
     * 添加收藏
     */
    private void addCollect() {
        CollectionMessageHttpUtil.addCollect(this, mId, String.valueOf(Constant.NEWS_TYPE_ARTICLE));
    }

    /**
     * 取消收藏
     */
    private void delCollect() {
        CollectionMessageHttpUtil.delCollect(this, mId, String.valueOf(Constant.NEWS_TYPE_ARTICLE));
    }

    /**
     * 赞某条评论
     *
     * @param commentId 评论ID
     */
    private void favourForCommentId(String commentId) {
        if (MyApplication.isLogin()) {
            Map<String, String> map = new HashMap<>();
            map.put(Constant.KEY_ID, commentId);
            map.put(Constant.KEY_CATID, mCatId);
            map.put("news_id", mId);
            String param = ParamUtil.getParam(map);
            OKHttpUtils.getInstance().post(this, Api.TEST_DNS_API_HOST_V2, Api.FAVOUR_COMMENT, param, Action.favour_comment);
        } else {
            LoginPromptDialog lpd = new LoginPromptDialog(PostZoneActivity.this);
            lpd.show();
        }
    }


    // ------------------------------------------
    // 调节字形
//    @Override public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_post_zone, menu);
//        return super.onCreateOptionsMenu(menu);
//    }


    /**
     * 计时器
     */
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            DebugUtils.dd("millisUni" + millisUntilFinished);
        }

        @Override
        public void onFinish() {
            isReaded = true;
        }
    }
}

