package tv.kuainiu.ui.me.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.MainActivity;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.util.DebugUtils;
import tv.kuainiu.util.NetUtils;
import tv.kuainiu.util.PreferencesUtils;
import tv.kuainiu.util.SecurityUtils;


/**
 * 填写验证码
 */
public class MessageLoginTwoActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_check_code_sms_login2) EditText mEtCheckCode;
    @BindView(R.id.tv_countdown) TextView mTvCountdown;
    @BindView(R.id.tv_phone) TextView mTvPhone;
    @BindView(R.id.button_submit) Button mBtnSubmit;

    private MyCountDownTimer mDownTimer;

    private String mRegionCode;
    private String mPhoneNumber;
    private String mCheckCode;
    private int mSendType;

    private void initData(Intent intent) {
        if (intent != null) {
            mRegionCode = intent.getStringExtra("area");
            mPhoneNumber = intent.getStringExtra("phone");
            mSendType = intent.getIntExtra("type", MessageLoginOneActivity.MESSAGE);
            DebugUtils.dd("area--->" + mRegionCode);
            DebugUtils.dd("phone--->" + mPhoneNumber);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_login_two);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData(getIntent());
        initListener();
        mTvPhone.setText(getString(R.string.value_region_code_and_phone, mRegionCode, mPhoneNumber));

        mDownTimer = new MyCountDownTimer();
        mDownTimer.start();
    }

    @Override protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    protected void initListener() {
        mBtnSubmit.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        String checkCode = mEtCheckCode.getText().toString().trim();
        switch (v.getId()) {
            case R.id.button_submit:
                if (TextUtils.isEmpty(mRegionCode) || TextUtils.isEmpty(mPhoneNumber)) {
                    DebugUtils.showToast(this, "账号可能不正确");
                    return;
                }
                if (TextUtils.isEmpty(checkCode)) {
                    DebugUtils.showToast(MessageLoginTwoActivity.this, "请填写验证码");
                    return;
                }
                if (!NetUtils.isOnline(MessageLoginTwoActivity.this)) {
                    DebugUtils.showToast(MessageLoginTwoActivity.this, R.string.toast_not_network);
                    return;
                }

                mCheckCode = checkCode;

                Map<String, String> map = new HashMap<>();
                map.put("area", mRegionCode);
                map.put("phone", mPhoneNumber);
                map.put("code", checkCode);
                String param = ParamUtil.getParam(map);
                OKHttpUtils.getInstance().post(MessageLoginTwoActivity.this, Api.TEST_DNS_API_HOST_V21, Api.SMS_LOGING, param, Action.sms_login);

                mBtnSubmit.setEnabled(false);
                break;

            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(HttpEvent event) {
        if (Action.sms_login == event.getAction()) {
            mBtnSubmit.setEnabled(true);
            if (99 == event.getCode()) {
                Intent intent = new Intent(this, MessageLoginThreeActivity.class);
                intent.putExtra("area", mRegionCode);
                intent.putExtra("phone", mPhoneNumber);
                intent.putExtra("check_code", mCheckCode);
                startActivity(intent);
                finish();
            } else {
                if (Constant.SUCCEED == event.getCode()) {
                    DebugUtils.dd("user json data : " + event.getData().toString());
                    User user = new Gson().fromJson(event.getData().optString("data"), User.class);
                    DebugUtils.dd("user string : " + user.toString());

                    IGXApplication.setUser(user);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constant.INTENT_ACTION_GET_CUSTOM));

                    PreferencesUtils.putString(this, Constant.KEY_PHONE, SecurityUtils.DESUtil.en(Api.PUBLIC_KEY, user.getPhone()));
                    PreferencesUtils.putString(this, Constant.KEY_AREA, SecurityUtils.DESUtil.en(Api.PUBLIC_KEY, user.getArea()));

                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    DebugUtils.showToastResponse(this, event.getMsg());
                }
            }
        }

        // 短信验证码
        if (Action.sms_login_send_code_two == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                DebugUtils.showToast(this, "验证码已发送");
            } else {
                DebugUtils.showToastResponse(this, event.getMsg());
            }
        }

        // 语音验证码
        if (Action.sms_login_send_code_voice_two == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                DebugUtils.showToast(this, "验证码已发送,请注意接听电话");

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


        @Override
        public void onTick(long millisUntilFinished) {
            if (null != mTvCountdown) {
                long value = millisUntilFinished / 1000;
                mTvCountdown.setText(String.format(Locale.CHINA, "接收验证码大约需要%d秒", value));
            }
        }

        @Override
        public void onFinish() {
            if (mBtnSubmit != null) {
                mBtnSubmit.setEnabled(true);
            }
            if (null != mTvCountdown) {
                String text = "收不到验证码？重新发送";
                SpannableString span = new SpannableString(text);
                span.setSpan(new ClickableSpan() {
                    @Override public void onClick(View widget) {
                        if (mSendType == MessageLoginOneActivity.MESSAGE) {
                            sendMessage();
                        } else {
                            sendVoice();
                        }
                        if (mDownTimer != null) {
                            mDownTimer.start();
                        } else {
                            mDownTimer = new MyCountDownTimer();
                            mDownTimer.start();
                        }
                    }
                }, 7, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(Color.parseColor("#2196F3")), 7, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                mTvCountdown.setText("收不到验证码？");
                mTvCountdown.setMovementMethod(LinkMovementMethod.getInstance());
                mTvCountdown.setText(span);
            }
        }
    }


    /**
     * 2.13 短信登录  发送验证码 login/sms_login_sendcode（v21）
     * <p/>
     * 固定参数  user_id, time ,sign
     * 业务参数  phone，area
     * <p/>
     * {"status"="0","data"="json"}
     */
    private void sendMessage() {
        Map<String, String> map = new HashMap<>();
        map.put(Constant.KEY_PHONE, mPhoneNumber);
        map.put(Constant.KEY_AREA, mRegionCode);
        String param = ParamUtil.getParam(map);

        OKHttpUtils.getInstance().post(this, Api.TEST_DNS_API_HOST_V21, Api.SMS_LOGING_SEND_CODE_MESSAGE, param, Action.sms_login_send_code_two);

    }

    /**
     * 2.17 短信登录  发送语音验证码 login/sms_login_voicecode（v21）
     * <p/>
     * 固定参数  user_id, time ,sign
     * 业务参数  phone
     * <p/>
     * {"status"="0","data"="json"}
     */
    private void sendVoice() {
        Map<String, String> map = new HashMap<>();
        map.put(Constant.KEY_PHONE, mPhoneNumber);
        String param = ParamUtil.getParam(map);

        OKHttpUtils.getInstance().post(this, Api.TEST_DNS_API_HOST_V21, Api.SMS_LOGING_SEND_CODE_VOICE, param, Action.sms_login_send_code_voice_two);
    }
}
