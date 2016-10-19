package tv.kuainiu.ui.message.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import tv.kuainiu.R;
import tv.kuainiu.command.http.SystemMessageHttpUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.SystemMessage;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.activity.WebActivity;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.LoadingProgressDialog;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * 系统消息详情
 */
public class MessageDetailsActivity extends BaseActivity {

    public static final String MESSGE_TITLE = "messge_title";
    public static final String MESSAGE_ID = "message_id";
    private String messge_title = "";
    private String message_id = "";

    @BindView(R.id.tv_message_title)
    TextView tv_message_title;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.wv_content)
    WebView wv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_message_details);
        messge_title = getIntent().getStringExtra(MESSGE_TITLE);
        message_id = getIntent().getStringExtra(MESSAGE_ID);
        setMyTitle("消息通知");
        fetcherSystemMessageDetail();
        WebSettings settings = wv_content.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDefaultTextEncodingName("utf-8");
        wv_content.setWebChromeClient(new MyWebChromeClient());
        wv_content.setWebViewClient(new MyWebViewClient());
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
                Intent intent = new Intent(MessageDetailsActivity.this, WebActivity.class);
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

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
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

    @Override
    protected void initListener() {
        super.initListener();
    }

    private void fetcherSystemMessageDetail() {
        LoadingProgressDialog.startProgressDialog(this);
        SystemMessageHttpUtil.fetcherSystemMessageDetail(this, new SystemMessageHttpUtil.ParamBuilder(message_id), Action.fetcherSystemMessageDetail);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HttpEvent event) {
        switch (event.getAction()) {
            case fetcherSystemMessageDetail:
                LoadingProgressDialog.stopProgressDialog();
                if (SUCCEED == event.getCode()) {
                    try {
                        JsonParser parser = new JsonParser();
                        JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                        JsonObject json = tempJson.getAsJsonObject("data");
                        JsonObject info = json.getAsJsonObject("info");
                        DataConverter<SystemMessage> dc = new DataConverter<>();
                        SystemMessage sysTemMessageList = dc.JsonToObject(info.toString(), SystemMessage.class);
                        dataBindView(sysTemMessageList);
                    } catch (Exception e) {
                        LogUtils.e(MessageDetailsActivity.class.getSimpleName(), "json 解析异常", e);
                    }
                }
                break;
        }
    }

    private void dataBindView(SystemMessage sysTemMessageList) {
        tv_message_title.setText(StringUtils.replaceNullToEmpty(sysTemMessageList.getTitle()));
        tv_date.setText(StringUtils.replaceNullToEmpty(sysTemMessageList.getDatetime()));
        wv_content.loadDataWithBaseURL(null,StringUtils.replaceNullToEmpty(sysTemMessageList.getContent()), "text/html", "utf-8",null);
    }
}
