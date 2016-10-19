package tv.kuainiu.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.UserHttpRequest;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.dialog.LoginPromptDialog;


public class WebActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView mWebView;
    TextView tv_title;
    ProgressBar pb_loading;
    private String mUrl;
    private boolean isNeed = false;
    private String session_id;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver;

    public static void intoNewIntent(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Constant.KEY_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        initContentView();
    }

    protected void initContentView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        registerBroadcast();
        mUrl = getIntent().getStringExtra(Constant.KEY_URL);
        isNeed = getIntent().getBooleanExtra(Constant.KEY_ISNEED, false);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setSingleLine();
        tv_title.setEllipsize(TextUtils.TruncateAt.END);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.addJavascriptInterface(new JavascriptInterface(this),
                "jslistner");
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        loadUrl();
    }

    private void registerBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == Constant.INTENT_ACTION_GET_CUSTOM) {
                    loadUrl();
                }
            }

        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.INTENT_ACTION_GET_CUSTOM);
        localBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void loadUrl() {
        String url = mUrl;
        session_id = PreferencesUtils.getString(this, Constant.SESSION_ID, "");
        if (url.contains("?")) {
            url = url + "&app_type=android&session_id=" + StringUtils.replaceNullToEmpty(session_id);
        } else {
            url = url + "?app_type=android&session_id=" + StringUtils.replaceNullToEmpty(session_id);
        }
        mWebView.loadUrl(url);
    }

    // 注入js函数监听
    private void addImageClickListner() {

        // 这段js函数的功能就是，遍历所有的a标签，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
//        mWebView.loadUrl("javascript:(function(){" +
//                "var objs =  document.getElementsByTagName(\"title\")[0];" +
//                "window.jslistner.setName(objs.innerHTML)" +
//                " })()");
    }

    // js通信接口
    public class JavascriptInterface {

        public Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

//        @android.webkit.JavascriptInterface
//        public void setName(final String title) {
//            LogUtils.i("course", "title=" + title);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tv_title.setText(StringUtils.replaceNullToEmpty(title));
//                }
//            });
//        }

        /**
         * @param id    消息id
         * @param state 0未收藏，1收藏
         */
        @android.webkit.JavascriptInterface
        public void collect(String id, int state) {
            Intent intent = new Intent(Constant.INTENT_ACTION_ACTIVITY_COLLECT);
//            intent.putExtra(MessageActivityCollectionActivity.ID, id);
//            intent.putExtra(MessageActivityCollectionActivity.STATE, state);
            localBroadcastManager.sendBroadcast(intent);
        }

        /**
         * -101需要重新登陆
         *
         * @param content
         */
        @android.webkit.JavascriptInterface
        public void login(final String content) {
            LogUtils.i("course", "content=" + content);
            if ("-101".equals(content)) {
                autoLogin();
            }
        }
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url) && url.startsWith("http") && isNeed) {
                Intent intent = new Intent(WebActivity.this, WebActivity.class);
                intent.putExtra(Constant.KEY_URL, url);
                startActivity(intent);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            addImageClickListner();
            pb_loading.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pb_loading.setVisibility(View.VISIBLE);
        }

    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            pb_loading.setProgress(newProgress);
            if (newProgress + 2 < 100) {
                pb_loading.setSecondaryProgress(newProgress + 2);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            tv_title.setText(title);
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            LogUtils.i("tag", "url=" + url);
            LogUtils.i("tag", "userAgent=" + userAgent);
            LogUtils.i("tag", "contentDisposition=" + contentDisposition);
            LogUtils.i("tag", "mimetype=" + mimetype);
            LogUtils.i("tag", "contentLength=" + contentLength);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void autoLogin() {
        User user = MyApplication.getUser();
        if (user != null) {
            UserHttpRequest.authLogin(this, user.getUser_id(), user.getPhone(), Action.AUTO_LOGIN);
        } else {// //提示去登录
            new LoginPromptDialog(this).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(HttpEvent event) {
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
                    ToastUtils.showToast(WebActivity.this, event.getMsg());
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
        }
        mWebView = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (localBroadcastManager != null && mReceiver != null) {
            localBroadcastManager.unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
}
