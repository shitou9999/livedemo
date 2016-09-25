package tv.kuainiu.ui.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Locale;
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
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.MatcherUtils;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.SecurityUtils;


/**
 * 修改手机账号第二步
 */
public class UpdatePhone2Activity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_phone) EditText mEtPhone;
    @BindView(R.id.et_check_code) EditText mEtCheckCode;
    @BindView(R.id.btn_push_check_code) Button mBtnPushCheckCode;
    @BindView(R.id.btn_submit) Button mBtnSubmit;

    private String phoneNumber;
    private String checkCode;
    private String postKey;


    private MyCountDownTimer mMyCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone2);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initListener();
        postKey = getIntent().getStringExtra("post_key");
        DebugUtils.dd("post key : " + postKey);

        mMyCountDownTimer = new MyCountDownTimer();
    }


     protected void initListener() {
        mBtnPushCheckCode.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        phoneNumber = mEtPhone.getText().toString().trim();
        checkCode = mEtCheckCode.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btn_push_check_code:
                if (!isForm()) {
                    return;
                }
                OKHttpUtils.getInstance().post(this, Api.UPDATE_PHONE_SENDCODE, prepareParamCheckCode(), Action.update_phone_sendcode);

                mBtnPushCheckCode.setEnabled(false);
                mMyCountDownTimer.start();
                break;
            case R.id.btn_submit:
                if (!isForm()) {
                    return;
                }
                if (TextUtils.isEmpty(checkCode)) {
                    DebugUtils.showToast(this, "验证码不能为空");
                    return;
                }
                OKHttpUtils.getInstance().post(this, Api.UPDATE_PHONE, prepareParamValid(), Action.update_phone);

                break;
            default:
                break;
        }
    }

    private boolean isForm() {
        if (TextUtils.isEmpty(phoneNumber)) {
            DebugUtils.showToast(this, "手机号不能为空");
            return false;
        }
        if (!MatcherUtils.checkPhone(phoneNumber)) {
            DebugUtils.showToast(this, "请输入正确的手机号");
            return false;
        }
        if (!NetUtils.isOnline(this)) {
            DebugUtils.showToast(this, "网络状态不稳定，请稍后重试");
            return false;
        }
        return true;
    }


    private String prepareParamCheckCode() {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phoneNumber);
        map.put("type", "new_phone");
        return ParamUtil.getParam(map);
    }

    private String prepareParamValid() {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phoneNumber);
        map.put("code", checkCode);
        map.put("post_key", postKey);
        return ParamUtil.getParam(map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)

    public void onHttpEvent(HttpEvent event) {
        DebugUtils.dd("action : " + event.getAction() + " code : " + event.getCode());

        if (Action.update_phone_sendcode == event.getAction()) {
            if (event.getCode() == Constant.SUCCEED) {
                DebugUtils.showToastResponse(this, "验证码已发送");

            } else {
                mMyCountDownTimer.onFinish();
                DebugUtils.showToastResponse(this, event.getMsg());
            }
        }

        if (Action.update_phone == event.getAction()) {
            if (event.getAction() == Action.update_phone) {
                DebugUtils.showToastResponse(this, "修改成功");
                PreferencesUtils.putString(this, "phone", SecurityUtils.DESUtil.en(Api.PUBLIC_KEY, phoneNumber));

                Intent intent = new Intent(UpdatePhone2Activity.this, PersonalActivity.class);
                startActivity(intent);
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
        public MyCountDownTimer() {
            super(60000L, 1000L);
        }


        @Override public void onTick(long millisUntilFinished) {
            if (mBtnPushCheckCode != null) {
                long value = millisUntilFinished / 1000;
                mBtnPushCheckCode.setText(String.format(Locale.CHINA, "重新发送(%d)", value));
            }

        }

        @Override public void onFinish() {
            if (mBtnPushCheckCode != null) {
                mBtnPushCheckCode.setEnabled(true);
                mBtnPushCheckCode.setText(getResources().getString(R.string.to_resend));
            }
        }
    }
    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
