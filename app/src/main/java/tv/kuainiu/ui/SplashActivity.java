package tv.kuainiu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.jpush.android.api.JPushInterface;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.UserHttpRequest;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.InitInfo;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.activity.GuideActivity;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.PreferencesUtils;

/**
 * Created by jack on 2016/9/7.
 */
public class SplashActivity extends BaseActivity {
    private static final int PHONE_REQUEST_CODE = 1;
    private static final int STORAGE_REQUEST_CODE = 2;

    private boolean isShowPermissionDialog1 = false;
    private boolean isShowPermissionDialog2 = false;


    private boolean is_first_opened_app = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        UserHttpRequest.initApp(this, Action.client_init);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNextActivity();
            }
        }, 1000);
    }

    private void goToNextActivity() {
        is_first_opened_app = PreferencesUtils
                .getBoolean(SplashActivity.this,
                        Constant.IS_FIRST_OPENED_APP, true);
        Intent intent = new Intent();
        if (is_first_opened_app) {
            intent.setClass(SplashActivity.this,
                    GuideActivity.class);

        } else {
            intent.setClass(SplashActivity.this,
                    MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientInitEvent(HttpEvent event) {
        initClientPost(event.getCode(), this);
        switch (event.getAction()) {
            case client_init:
                if (Constant.SUCCEED == event.getCode()) {
//                    BaseActivity.initClientNumbers = 0;//初始化成功后清空初始化计数
                    LogUtils.i(TAG, "json data : " + event.getData().toString());
                    String tempString = event.getData().optString("data");
                    InitInfo initInfo = new Gson().fromJson(tempString, InitInfo.class);
                    String privateKey = initInfo.getPrivate_key();
                    LogUtils.i(TAG, "privateKey : " + privateKey);
                    if (!TextUtils.isEmpty(privateKey)) {
                        MyApplication.setKey(privateKey);
                    }
                    if (MyApplication.isLogin()) {
                        User user = MyApplication.getUser();
                        user.setIs_teacher(initInfo.getIs_teacher());
                        user.setLive_count(initInfo.getLive_count());
                        user.setFans_count(initInfo.getFans_count());
                        user.setFollow_count(initInfo.getFollow_count());
                        MyApplication.setUser(user);
                    }
                    PreferencesUtils.putInt(this, Constant.MSG_NUM, initInfo.getMsg_num());
                }
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            goToNextActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        isShowPermissionDialog1 = false;
        isShowPermissionDialog2 = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
