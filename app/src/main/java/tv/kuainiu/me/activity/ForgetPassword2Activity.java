package tv.kuainiu.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.util.DebugUtils;
import tv.kuainiu.util.NetUtils;
import tv.kuainiu.util.SMSUtils;
import tv.kuainiu.util.SecurityUtils;
import tv.kuainiu.util.WeakHandler;


/**
 * 找回密码第二步
 */
public class ForgetPassword2Activity extends AbsSMSPermissionActivity implements View.OnClickListener {
    @BindView(R.id.et_check_code) EditText mEtCheckCode;
    @BindView(R.id.et_password) EditText mEtPassword;
    @BindView(R.id.et_affirm_password) EditText mEtAffirmPassword;
    @BindView(R.id.btn_submit) Button mBtnSubmit;
    /**
     * 找回密码成功
     */
    public static final int SUCCESS_CODE = 200;

    private String mAccount;

    private final Handler mHideHandler = new Handler();
    private Runnable mHidePartRunnable = new Runnable() {
        @Override public void run() {
            mBtnSubmit.setEnabled(true);
            finish();
            Intent intent = new Intent(ForgetPassword2Activity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("account", mAccount);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password2);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mAccount = getIntent().getStringExtra("account");
        mBtnSubmit.setOnClickListener(this);
    }


    /**
     * @ClassName: MyHandle
     * @Description: (处理接收数据)
     */
    public static class MyHandle extends WeakHandler<ForgetPassword2Activity> {

        public MyHandle(ForgetPassword2Activity owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            ForgetPassword2Activity rActivity = getOwner();
            switch (msg.what) {
                case SMSUtils.SMS:
                    if (msg.obj != null && rActivity != null && rActivity.mEtCheckCode != null) {
                        rActivity.mEtCheckCode.setText(msg.obj.toString());
                    }
                    break;
            }
        }
    }

    @Override public void onClick(View v) {
        password = mEtPassword.getText().toString().trim();
        checkCode = mEtCheckCode.getText().toString().trim();
        affirmPassword = mEtAffirmPassword.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btn_submit:
                if (TextUtils.isEmpty(checkCode)) {
                    DebugUtils.showToast(ForgetPassword2Activity.this, "请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(affirmPassword)) {
                    DebugUtils.showToast(ForgetPassword2Activity.this, "请确认密码");
                    return;
                }
                if (!affirmPassword.equals(password)) {
                    DebugUtils.showToast(ForgetPassword2Activity.this, "两次输入的密码不一致");
                    return;
                }
                if (!NetUtils.isOnline(ForgetPassword2Activity.this)) {
                    DebugUtils.showToast(ForgetPassword2Activity.this, getString(R.string.toast_not_network));
                    return;
                }
                mBtnSubmit.setEnabled(false);
                OKHttpUtils.getInstance().post(this, Api.FORGET_PWD, prepareParam(), Action.forget_pwd);
                break;
            default:
                break;
        }
    }

    private String password;
    private String checkCode;
    private String affirmPassword;


    private String prepareParam() {
        Map<String, String> map = new HashMap<>();
        map.put("user", mAccount);
        map.put("new_pwd", SecurityUtils.salt(password));
        map.put("code", checkCode);
        return ParamUtil.getParam(map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HttpEvent event) {
        if (Action.forget_pwd == event.getAction()) {
            DebugUtils.showToastResponse(this, event.getMsg());
            mBtnSubmit.setEnabled(true);
            if (Constant.SUCCEED == event.getCode()) {
                //EventBus.getDefault().post(new Forget2Event(RESULT_CANCELED));
                mHideHandler.removeCallbacks(mHidePartRunnable);
                mHideHandler.postDelayed(mHidePartRunnable, 3000);
            }
        }
    }

    @Override protected void awarded() {
        new SMSUtils(ForgetPassword2Activity.this, new MyHandle(this));
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
