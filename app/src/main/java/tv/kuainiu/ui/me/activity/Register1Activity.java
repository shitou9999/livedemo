package tv.kuainiu.ui.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.region.Region;
import tv.kuainiu.ui.region.RegionDataHelper;
import tv.kuainiu.ui.region.RegionSelectionActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.SMSUtils;
import tv.kuainiu.utils.SecurityUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.utils.WeakHandler;


/**
 * 注册
 */
public class Register1Activity extends AbsSMSPermissionActivity implements View.OnClickListener {
    @BindView(R.id.ll_region_selector)
    LinearLayout mRlRegionSelector;
    @BindView(R.id.tv_region_name)
    TextView mTvRegion;
    @BindView(R.id.et_region)
    EditText mEtRegion;
    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.et_check_code)
    EditText mEtCheckCode;
    @BindView(R.id.et_password1)
    EditText mEtPassword1;
    @BindView(R.id.et_password2)
    EditText mEtPassword2;
    @BindView(R.id.btn_push_check_code)
    Button mBtnPushCheckCode;
    @BindView(R.id.btn_register)
    Button mBtnRegister;
    @BindView(R.id.ivClearText)
    ImageView ivClearText;
    private MyCountDownTimer mDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mRegionMap = RegionDataHelper.getRegionDataMap(this);
        mDownTimer = new MyCountDownTimer();

        mEtRegion.setSelection(mEtRegion.length());

        mEtAccount.requestFocus();
        initListener();
//        new SMSUtils(Register1Activity.this, new MyHandle(this));
    }

    @Override
    protected void onDestroy() {
        if (null != mDownTimer) {
            mDownTimer.cancel();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    /**
     * 处理接收数据
     */
    public static class MyHandle extends WeakHandler<Register1Activity> {

        public MyHandle(Register1Activity owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            Register1Activity rActivity = getOwner();
            switch (msg.what) {
                case SMSUtils.SMS:
                    if (msg.obj != null && rActivity != null && rActivity.mEtCheckCode != null) {
                        rActivity.mEtCheckCode.setText(msg.obj.toString());
                    }
                    break;
            }
        }

    }


    Map<String, Region> mRegionMap;

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void initListener() {
        mBtnPushCheckCode.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        ivClearText.setOnClickListener(this);
        mRlRegionSelector.setOnClickListener(this);

        mEtRegion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mRegionMap != null) {
                    if (s.length() > 0) {
                        boolean has = mRegionMap.containsKey(s.toString());
                        if (has) {
                            Region region = mRegionMap.get(s.toString());
                            String countries = region.getRegionName();
                            mTvRegion.setText(countries);
                        } else {
                            mTvRegion.setText(Constant.AREA_CODE_ERROR);
                        }
                        //mTvRegion.setText("");
                    }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (200 == requestCode && resultCode == 300) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Region region = (Region) bundle.get("region");
                    if (region != null) {
                        mTvRegion.setText(region.getRegionName());
                        mEtRegion.setText(String.valueOf(region.getRegionCode()));
                        mEtRegion.setSelection(mEtRegion.length());


                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClearText:
                mEtAccount.setText("");
                break;
            case R.id.btn_push_check_code:

                String region = mEtRegion.getText().toString();
                if (TextUtils.isEmpty(region)) {
                    DebugUtils.showToast(this, "请输入地区号");
                    return;
                }

                String phoneNumber = mEtAccount.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    mEtAccount.setFocusable(true);
                    mEtAccount.setError("请输入手机号");
                    return;
                } else {
                    mEtAccount.setError(null);
                }
                if (Constant.AREA_CODE_ERROR.equals(mTvRegion.getText())) {
                    DebugUtils.showToast(this, Constant.AREA_CODE_ERROR);
                    return;
                }
                //TODO 屏蔽了语音验证码
//                if ("86".equals(region)) {
//                    CheckCodePicker.selectorCheckCode(this, new CheckCodePicker.IDialogButtonOnClickListener() {
//                        @Override public void message() {
//                            sendMessage();
//                        }
//
//                        @Override public void call() {
//                            sendVoiceCode();
//                        }
//                    });
//
//                } else {
//                    sendMessage();
//                }
                sendMessage();
                break;
            case R.id.btn_register:
                if (checkFormIsCorrect()) {
                    mBtnRegister.setEnabled(false);
                    submitRegister();
                }
                break;

            case R.id.ll_region_selector:
                Intent intent = new Intent(Register1Activity.this, RegionSelectionActivity.class);
                startActivityForResult(intent, 200);
                break;

            default:
                break;
        }
    }

    private void sendMessage() {
        if (!NetUtils.isOnline(this)) {
            ToastUtils.showToast(this, getString(R.string.toast_not_network));
            return;
        }
        mBtnPushCheckCode.setEnabled(false);
        fetchCheckCode();
    }

    private void sendVoiceCode() {
        if (!NetUtils.isOnline(this)) {
            ToastUtils.showToast(this, getString(R.string.toast_not_network));
            return;
        }
        mBtnPushCheckCode.setEnabled(false);
        OKHttpUtils.getInstance().post(this, Api.TEST_DNS_API_HOST_V21, Api.REG_PHONE_SENDCODE_VOICE, prepareParamCheckCode(), Action.reg_phone_sendcode_voice);

        //fetchCheckCode();
    }


    private void fetchCheckCode() {
        OKHttpUtils.getInstance().post(this, Api.REG_PHONE_SENDCODE, prepareParamCheckCode(), Action.reg_phone_sendcode);
    }

    private void submitRegister() {
        OKHttpUtils.getInstance().post(this, Api.REG_PHONE, prepareParamRegister(), Action.reg_phone);
    }

    private String prepareParamCheckCode() {
        Map<String, String> map = new HashMap<>();
        map.put("phone", mEtAccount.getText().toString().trim());
        map.put("area", mEtRegion.getText().toString().trim());
        return ParamUtil.getParam(map);
    }

    private String prepareParamRegister() {
        Map<String, String> map = new HashMap<>();
        map.put("code", mEtCheckCode.getText().toString().trim());
        map.put("password", SecurityUtils.salt(mEtPassword2.getText().toString().trim()));
        map.put("phone", mEtAccount.getText().toString().trim());
        map.put("area", mEtRegion.getText().toString().trim());

        return ParamUtil.getParam(map);
    }

    private boolean checkFormIsCorrect() {
        boolean flag = true;
        String phoneNumber = mEtAccount.getText().toString();
        String checkCode = mEtCheckCode.getText().toString();
        String password = mEtPassword1.getText().toString();
        String passwordAffirm = mEtPassword2.getText().toString();

        if (TextUtils.isEmpty(phoneNumber)) {
            mEtAccount.setError("请输入手机号");
            if (flag) {
                mEtAccount.setFocusable(true);
            }
            flag = false;
        } else {
            mEtAccount.setError(null);
        }
        if (TextUtils.isEmpty(checkCode) || checkCode.length() < 1) {
            mEtCheckCode.setError("请输入验证码");
            if (flag) {
                mEtCheckCode.setFocusable(true);
            }
            flag = false;
        } else {
            mEtCheckCode.setError(null);
        }
        if (TextUtils.isEmpty(password) || password.length() < 1) {
            mEtPassword1.setError("请输入密码");
            if (flag) {
                mEtPassword1.setFocusable(true);
            }
            flag = false;
        } else {
            if (password.length() < 6) {
                mEtPassword1.setError("密码长度不能低于6位");
                if (flag) {
                    mEtPassword1.setFocusable(true);
                }
                flag = false;
            } else {
                mEtPassword1.setError(null);
            }

        }
        if (TextUtils.isEmpty(passwordAffirm) || passwordAffirm.length() < 1) {
            mEtPassword2.setError("请输入确认密码");
            if (flag) {
                mEtPassword2.setFocusable(true);
            }
            flag = false;
        } else {
            mEtPassword2.setError(null);
        }
        if (!passwordAffirm.equals(password)) {
            mEtPassword2.setError("2次密码输入的不一致");
            if (flag) {
                mEtPassword2.setFocusable(true);
            }
            flag = false;
        } else {
            mEtPassword2.setError(null);
        }
        return flag;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterEvent(HttpEvent event) {

        if (Action.reg_phone == event.getAction()) {
            mBtnRegister.setEnabled(true);
            if (Constant.SUCCEED == event.getCode()) {
                try {
                    String tempString = event.getData().optString("data");
                    JSONObject object = new JSONObject(tempString);
                    User user = new User();
                    if (object.has("user_id")) {
                        user.setUser_id(object.optString("user_id"));
                    }
                    if (object.has("phone")) {
                        user.setPhone(object.optString("phone"));
                    }
//                    user.setNickname("");
                    MyApplication.setUser(user);
                    ToastUtils.showToast(this, "注册成功");
                    EventBus.getDefault().post(new HttpEvent(Action.login, Constant.SUCCEED));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                DebugUtils.showToastResponse(this, event.getMsg());
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSendRegisterCheckCode(HttpEvent event) {
        if (Action.reg_phone_sendcode == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                DebugUtils.showToast(this, "验证码已发送");
                mDownTimer.start();
            } else {
                mBtnPushCheckCode.setEnabled(true);
                DebugUtils.showToastResponse(this, event.getMsg());
            }
        }

        if (Action.reg_phone_sendcode_voice == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                DebugUtils.showToast(this, "验证码已发送,请注意接听电话");
                mDownTimer.start();
            } else {
                mBtnPushCheckCode.setEnabled(true);
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
            // mBtnPushCheckCode.setEnabled(false);
        }


        @Override
        public void onTick(long millisUntilFinished) {
            if (null != mBtnPushCheckCode) {
                long value = millisUntilFinished / 1000;
                mBtnPushCheckCode.setText(String.format(Locale.CHINA, "重新发送(%d)", value));
            }
        }

        @Override
        public void onFinish() {
            if (null != mBtnPushCheckCode) {
                mBtnPushCheckCode.setEnabled(true);
                mBtnPushCheckCode.setText(getResources().getString(R.string.to_resend));
            }
        }
    }


    @Override
    protected void awarded() {
        new SMSUtils(Register1Activity.this, new MyHandle(this));
    }

}
