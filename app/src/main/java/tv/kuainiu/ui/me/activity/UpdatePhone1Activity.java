package tv.kuainiu.ui.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.util.DebugUtils;
import tv.kuainiu.util.MatcherUtils;
import tv.kuainiu.util.NetUtils;
import tv.kuainiu.util.SMSUtils;
import tv.kuainiu.util.WeakHandler;


/**
 * 修改手机账号第一步
 */
public class UpdatePhone1Activity extends AbsSMSPermissionActivity implements View.OnClickListener {
    @BindView(R.id.et_phone) EditText mEtPhone;
    @BindView(R.id.et_check_code) EditText mEtCheckCode;
    @BindView(R.id.btn_push_check_code) Button mBtnPushCheckCode;
    @BindView(R.id.btn_submit) Button mBtnSubmit;

    private String phoneNumber;
    private String checkCode;
    private MyCountDownTimer mDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone1);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initListener();
        mDownTimer = new MyCountDownTimer();
        bindDataForView();
    }


    @Override protected void onStop() {
        super.onStop();
        if (null != mDownTimer) { // 取消计时
            mDownTimer.cancel();
        }
    }

    private void bindDataForView() {
        User user = IGXApplication.getUser();
        if (user != null) {
            mEtPhone.setText(user.getPhone());
        }
    }

    /**
     * @ClassName: MyHandle
     * @Description: (处理接收数据)
     */
    public static class MyHandle extends WeakHandler<UpdatePhone1Activity> {

        public MyHandle(UpdatePhone1Activity owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            UpdatePhone1Activity rActivity = getOwner();
            switch (msg.what) {
                case SMSUtils.SMS:
                    if (msg.obj != null && rActivity != null && rActivity.mEtCheckCode != null) {
                        rActivity.mEtCheckCode.setText(msg.obj.toString());
                    }
                    break;
            }
        }
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

                mBtnPushCheckCode.setEnabled(false);
                OKHttpUtils.getInstance().post(this, Api.UPDATE_PHONE_SENDCODE, prepareParamCheckCode(), Action.update_phone_sendcode);
                mDownTimer.start();
                break;
            case R.id.btn_submit:
                if (!isForm()) {
                    return;
                }
                if (TextUtils.isEmpty(checkCode)) {
                    DebugUtils.showToast(this, "验证码不能为空");
                    return;
                }
                OKHttpUtils.getInstance().post(this, Api.UPDATE_PHONE_VALID, prepareParamValid(), Action.phone_valid);
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
            DebugUtils.showToast(this, R.string.toast_not_network);
            return false;
        }
        return true;
    }


    private String prepareParamCheckCode() {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phoneNumber);
        map.put("type", "ori_phone");
        return ParamUtil.getParam(map);
    }

    private String prepareParamValid() {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phoneNumber);
        map.put("code", checkCode);
        return ParamUtil.getParam(map);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        if (Action.update_phone_sendcode == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                DebugUtils.showToastResponse(this, "验证码已发送");
                JSONObject object = event.getData();
                DebugUtils.dd("update phone 1 check code : " + object.toString());

            } else {
                DebugUtils.showToastResponse(this, event.getMsg());
                mDownTimer.onFinish();
            }
        }

        if (Action.phone_valid == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                JSONObject object = event.getData().optJSONObject("data");
                String postKey = object.optString("post_key");
                Intent intent = new Intent(UpdatePhone1Activity.this, UpdatePhone2Activity.class);
                intent.putExtra("post_key", postKey);
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

    @Override protected void awarded() {
        new SMSUtils(UpdatePhone1Activity.this, new MyHandle(this));
    }
    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
