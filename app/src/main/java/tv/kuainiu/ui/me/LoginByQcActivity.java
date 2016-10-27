package tv.kuainiu.ui.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.CaptureActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.UserHttpRequest;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.me.activity.LoginActivity;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * 通过扫描二维码授权网页登录
 */
public class LoginByQcActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.btn_login) Button btn_login;
    @BindView(R.id.tv_close) TextView tv_close;
    @BindView(R.id.tv_cancel_login) TextView tv_cancel_login;
    @BindView(R.id.tv_error_tip) TextView tv_error_tip;
    @BindView(R.id.tv_qc) TextView tv_qc;
    @BindView(R.id.tv_tip) TextView tv_tip;

    String client_id = "";
    String connect_time = "";
    LoginPromptDialog loginPromptDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.login_qc);
    }

    @Override protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_login_qc);
        client_id = StringUtils.replaceNullToEmpty(getIntent().getStringExtra(Constant.CLIENT_ID));
        connect_time = StringUtils.replaceNullToEmpty(getIntent().getStringExtra(Constant.CONNECT_TIME));
    }


    @Override protected void initListener() {
        super.initListener();
        btn_login.setOnClickListener(this);
        tv_close.setOnClickListener(this);
        tv_cancel_login.setOnClickListener(this);
        tv_qc.setOnClickListener(this);
        tv_tip.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                btn_login.setEnabled(false);
                tv_error_tip.setText("");
                loginByQc();
                break;
            case R.id.tv_close:
            case R.id.tv_cancel_login:
                finish();
                break;
            case R.id.tv_qc:
                Intent rl_zxing = new Intent(this, CaptureActivity.class);
                startActivity(rl_zxing);
                finish();
                break;
        }
    }

    /**
     * 通过二维码登录
     */
    private void loginByQc() {
        User user = MyApplication.getUser();

        if (user != null) {
            if (!TextUtils.isEmpty(user.getSession_id())) {
                UserHttpRequest.qrCodeLogin(this, user.getSession_id(), client_id, connect_time, Action.qr_code_login);
            } else {
                autoLogin();
            }
        } else {
            tipLoginFirst();
        }
    }

    /**
     * 获取session_id
     */
    private void autoLogin() {
        User user = MyApplication.getUser();
        if (user != null) {
            UserHttpRequest.authLogin(this, user.getUser_id(), user.getPhone(), Action.AUTO_LOGIN);
        } else {// //提示去登录
            tipLoginFirst();
        }
    }

    /**
     * 提示先登录客户端
     */
    private void tipLoginFirst() {
        if (loginPromptDialog == null) {
            loginPromptDialog = new LoginPromptDialog(this);
        }
        loginPromptDialog.setCallBack(new LoginPromptDialog.CallBack() {
            @Override public void onCancel(DialogInterface dialog, int which) {

            }

            @Override public void onLogin(DialogInterface dialog, int which) {
                Intent intent = new Intent(LoginByQcActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override public void onDismiss(DialogInterface dialog) {
                btn_login.setEnabled(true);
            }
        });
        loginPromptDialog.show();
    }

    /**
     * 二维码登录失败处理
     *
     * @param msg
     */
    private void loginFailed(String msg) {
        btn_login.setEnabled(true);
        tv_error_tip.setText(StringUtils.replaceNullToEmpty(msg, "登录失败，重试一次"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(HttpEvent event) {
        switch (event.getAction()) {
            case AUTO_LOGIN:
                if (SUCCEED == event.getCode()) {
                    JSONObject jsonObject = event.getData();
                    try {
                        if (jsonObject != null && jsonObject.has("session_id")) {
                            String session_id = jsonObject.getString("session_id");
                            PreferencesUtils.putString(this, Constant.SESSION_ID, session_id);
                            User user = MyApplication.getUser();
                            user.setSession_id(session_id);
                            MyApplication.setUser(user);
                            loginByQc();
                        } else {
                            loginFailed(null);//二维码登录失败处理
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loginFailed(null);//二维码登录失败处理
                    }
                } else {
                    //二维码登录失败处理
                    loginFailed(event.getMsg());
                }
                break;
            case qr_code_login:
                if (SUCCEED == event.getCode()) {
                    LogUtils.i("qcLoin", "qc_login_data:" + event.getData().toString());
                    ToastUtils.showToast(this, "登录成功");
                    finish();
                } else if (event.getCode() == -101) {
                    autoLogin();
                } else {
                    // 二维码登录失败处理
                    loginFailed(event.getMsg());
                }
                break;
        }
    }

}
