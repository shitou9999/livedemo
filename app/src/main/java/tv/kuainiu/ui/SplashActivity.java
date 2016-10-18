package tv.kuainiu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import cn.jpush.android.api.JPushInterface;
import tv.kuainiu.R;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.activity.GuideActivity;
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
