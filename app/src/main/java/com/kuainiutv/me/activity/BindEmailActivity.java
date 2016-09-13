package com.kuainiutv.me.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kuainiutv.IGXApplication;
import com.kuainiutv.R;
import com.kuainiutv.command.http.Api;
import com.kuainiutv.command.http.core.OKHttpUtils;
import com.kuainiutv.command.http.core.ParamUtil;
import com.kuainiutv.event.HttpEvent;
import com.kuainiutv.modle.cons.Action;
import com.kuainiutv.modle.cons.Constant;
import com.kuainiutv.ui.activity.BaseActivity;
import com.kuainiutv.util.DebugUtils;
import com.kuainiutv.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 绑定邮箱
 */
public class BindEmailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_email) EditText mEtEmail;
    @BindView(R.id.et_check_code) EditText mEtCheckCode;
    @BindView(R.id.btn_push_check_code) Button mBtnPushCheckCode;
    @BindView(R.id.btn_submit) Button mBtnSubmit;

    private String checkCode;
    private String email;
    private boolean isBind = false;
    private MyCountDownTimer mDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_email);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (IGXApplication.getInstance().getUser() != null) {
            if (!TextUtils.isEmpty(IGXApplication.getInstance().getUser().getEmail())) {
                isBind = true;
                mEtEmail.setText(IGXApplication.getInstance().getUser().getEmail());
                mBtnSubmit.setText("解除绑定");
            } else {
                isBind = false;
                mEtEmail.setText("");
                mBtnSubmit.setText(getString(R.string.binding));
            }
        }
        initListener();
        mDownTimer = new MyCountDownTimer(60000l, 1000l);
    }



    protected void initListener() {
        mBtnPushCheckCode.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }

    private String emailConfirm;


    @Override protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mDownTimer.cancel();
    }

    /**
     * 3.6 绑定邮箱  account/bind_email_sendcode
     * <p>
     * <p>
     * 固定参数  user_id, time ,sign
     * 业务参数  email
     * 返回值:
     * {"status"="0"}  成功
     * <p>
     * <p>
     * 3.6 绑定邮箱  account/bind_email
     * <p>
     * <p>
     * 固定参数  user_id, time ,sign
     * 业务参数  email,code
     * 返回值:
     * {"status"="0"}  成功
     * <p>
     * <p>
     * <p>
     * 3.7 解除绑定邮箱  account/unbind_email_sendcode
     * <p>
     * <p>
     * 固定参数  user_id, time ,sign
     * 业务参数  email
     * 返回值:
     * {"status"="0"}  成功
     * <p>
     * <p>
     * 3.8 解除绑定邮箱  account/unbind_email
     * <p>
     * <p>
     * 固定参数  user_id, time ,sign
     * 业务参数  email,code
     * 返回值:
     * {"status"="0"}  成功
     *
     * @param v
     */
    @Override public void onClick(View v) {
        checkCode = mEtCheckCode.getText().toString().trim();
        email = mEtEmail.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btn_push_check_code:
                emailConfirm = email;
                if (TextUtils.isEmpty(emailConfirm)) {
                    DebugUtils.showToast(BindEmailActivity.this, "邮箱不能为空");
                    return;
                }
                if (!NetUtils.isOnline(this)) {
                    DebugUtils.showToast(this, R.string.toast_not_network);
                    return;
                }
                mDownTimer.start();
                mBtnPushCheckCode.setEnabled(false);
                if (!isBind) {
                    OKHttpUtils.getInstance().post(BindEmailActivity.this, Api.BIND_EMAIL_SENDCODE, prepareParamCheckCode(), Action.bind_email_sendcode);
                } else {
                    OKHttpUtils.getInstance().post(BindEmailActivity.this, Api.UNBIND_EMAIL_SENDCODE, prepareParamCheckCode(), Action.bind_email_sendcode);
                }
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(emailConfirm)) {
                    emailConfirm = mEtEmail.getText().toString().trim();
                }
                if (TextUtils.isEmpty(emailConfirm)) {
                    DebugUtils.showToast(BindEmailActivity.this, "邮箱不能为空");
                    return;
                }
                if (TextUtils.isEmpty(checkCode)) {
                    DebugUtils.showToast(BindEmailActivity.this, "验证码不能为空");
                    return;
                }
                if (!NetUtils.isOnline(this)) {
                    DebugUtils.showToast(this, R.string.toast_not_network);
                    return;
                }
                mBtnSubmit.setEnabled(false);
                if (!isBind) {
                    // 如果未绑定邮箱则绑定
                    OKHttpUtils.getInstance().post(BindEmailActivity.this, Api.BIND_EMAIL, prepareParamBindEmail(), Action.bind_email);
                } else {
                    // 如果已绑定邮箱则解除绑定
                    OKHttpUtils.getInstance().post(BindEmailActivity.this, Api.UNBIND_EMAIL, prepareParamBindEmail(), Action.bind_email);
                }
                break;
            default:
                break;
        }
    }

    private String prepareParamCheckCode() {
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        return ParamUtil.getParam(map);
    }

    private String prepareParamBindEmail() {
        Map<String, String> map = new HashMap<>();
        map.put("email", emailConfirm);
        map.put("code", checkCode);
        return ParamUtil.getParam(map);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        Action action = event.getAction();
        if (action == Action.bind_email_sendcode) {
            DebugUtils.showToastResponse(this, event.getMsg());
            if (Constant.SUCCEED == event.getCode()) {
                DebugUtils.showToast(BindEmailActivity.this, "验证码已发送");
            }
        }
        if (action == Action.bind_email) {
            mBtnSubmit.setEnabled(true);
            String msg = TextUtils.isEmpty(event.getMsg()) ? "成功" : event.getMsg();
            DebugUtils.showToast(this, msg);
            if (Constant.SUCCEED == event.getCode()) {
                if (!isBind) { // 已绑定则清空
                    IGXApplication.getInstance().getUser().setEmail(emailConfirm);
                } else {
                    IGXApplication.getInstance().getUser().setEmail("");
                }
                finish();
            } else {
                DebugUtils.showToastResponse(this, event.getMsg());
            }

        }
    }

    /**
     * 重新发送验证码倒计时
     */
    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        @Override public void onTick(long millisUntilFinished) {
            mBtnPushCheckCode.setText("" + (millisUntilFinished / 1000));

        }

        @Override public void onFinish() {
            mBtnPushCheckCode.setEnabled(true);
            mBtnPushCheckCode.setText(getResources().getString(R.string.to_resend));
        }
    }
}
