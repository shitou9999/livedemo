package tv.kuainiu.ui.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import tv.kuainiu.command.preferences.UserPreferencesManager;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.region.Region;
import tv.kuainiu.region.RegionDataHelper;
import tv.kuainiu.region.RegionSelectionActivity;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.util.CheckCodePicker;
import tv.kuainiu.util.DebugUtils;


/**
 * 用验证码登录
 */
public class MessageLoginOneActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 100;
    public static final int MESSAGE = 0x1;
    public static final int VOICE = 0x2;

    @BindView(R.id.ll_region_selector) LinearLayout mRlRegionSelector;
    @BindView(R.id.et_region) EditText mEtRegionCode;
    @BindView(R.id.et_account) EditText mEtAccount;
    @BindView(R.id.tv_region_name) TextView mTvRegionName;
    @BindView(R.id.button_submit) Button mBtnNextStep;

    private String mRegionCode;
    private String mPhoneNumber;
    private Map<String, Region> mRegionMap;

    private TextWatcher mEditRegionCodeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override public void afterTextChanged(Editable s) {
            if (mRegionMap != null) {
                if (s.length() > 0) {
                    boolean has = mRegionMap.containsKey(s.toString());
                    if (has) {
                        Region region = mRegionMap.get(s.toString());
                        String countries = region.getRegionName();
                        mTvRegionName.setText(countries);
                    } else {
                        mTvRegionName.setText(getString(R.string.region_code_error));
                    }
                    //mTvRegion.setText("");
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_login_one);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mRegionMap = RegionDataHelper.getRegionDataMap(this);

        mEtRegionCode.setSelection(mEtRegionCode.length());
        initListener();

        initAccount();

    }

    // XXX Review this code logic(审查此段代码逻辑)
    private void initAccount() {
        User user = UserPreferencesManager.getUserExtraInfo();
        if (user != null) {
            String area = user.getArea();
            if (!TextUtils.isEmpty(area) && (mRegionMap != null && mRegionMap.containsKey(area))) {
                mEtRegionCode.setText(area);
                mTvRegionName.setText(mRegionMap.get(area).getRegionName());
            } else {
                mEtRegionCode.setText("86");
                mTvRegionName.setText("中国");
            }

            String phone = user.getPhone();
            if (!TextUtils.isEmpty(phone)) {
                if (phone.length() < 18) {
                    mEtAccount.setText(phone);
                }
            }

        } else {
            mEtRegionCode.setText("86");
            mTvRegionName.setText("中国");
        }

        mEtRegionCode.setSelection(mEtRegionCode.length());

        mEtAccount.requestFocus();
        mEtAccount.setSelection(mEtAccount.length());
    }



  protected void initListener() {
        mRlRegionSelector.setOnClickListener(this);
        mBtnNextStep.setOnClickListener(this);
        mEtRegionCode.addTextChangedListener(mEditRegionCodeTextWatcher);
    }


    @Override protected void onStart() {
        super.onStart();
        mEtAccount.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE == requestCode && 300 == resultCode) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Region region = (Region) bundle.get("region");
                    if (region != null) {
                        mTvRegionName.setText(region.getRegionName());
                        mEtRegionCode.setText(String.valueOf(region.getRegionCode()));
                        mEtRegionCode.setSelection(mEtRegionCode.length());
                    }
                }
            }
        }
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_region_selector:
                Intent intent = new Intent(MessageLoginOneActivity.this, RegionSelectionActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;

            case R.id.button_submit:
                mRegionCode = mEtRegionCode.getText().toString();
                mPhoneNumber = mEtAccount.getText().toString().trim();
                DebugUtils.dd("region code : " + mRegionCode);
                DebugUtils.dd("phone number : " + mPhoneNumber);

                if (TextUtils.isEmpty(mRegionCode)) {
                    DebugUtils.showToast(this, "请输入地区号");
                    return;
                }
                if (Constant.AREA_CODE_ERROR.equals(mTvRegionName.getText())) {
                    DebugUtils.showToast(this, Constant.AREA_CODE_ERROR);
                    return;
                }
                if (TextUtils.isEmpty(mPhoneNumber)) {
                    DebugUtils.showToast(this, "请输入手机号");
                    return;
                }

                if ("86".equals(mRegionCode)) {
                    CheckCodePicker.selectorCheckCode(this, new CheckCodePicker.IDialogButtonOnClickListener() {
                        @Override public void message() {
                            sendMessage();
                            mBtnNextStep.setEnabled(false);
                        }

                        @Override public void call() {
                            sendVoice();
                            mBtnNextStep.setEnabled(false);
                        }
                    });

                } else {
                    sendMessage();
                    mBtnNextStep.setEnabled(false);
                }
                break;

            default:
                break;
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

        OKHttpUtils.getInstance().post(this, Api.TEST_DNS_API_HOST_V21, Api.SMS_LOGING_SEND_CODE_MESSAGE, param, Action.sms_login_send_code);

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

        OKHttpUtils.getInstance().post(this, Api.TEST_DNS_API_HOST_V21, Api.SMS_LOGING_SEND_CODE_VOICE, param, Action.sms_login_send_code_voice);
    }

    private void startMessageLoginTwoActivity(int type) {
        Intent intent = new Intent(this, MessageLoginTwoActivity.class);
        intent.putExtra("type", type);
        intent.putExtra(Constant.KEY_AREA, mRegionCode);
        intent.putExtra(Constant.KEY_PHONE, mPhoneNumber);
        startActivity(intent);
    }

    /**
     * @param event
     * @deprecated
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(HttpEvent event) {
        // 短信验证码
        if (Action.sms_login_send_code == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                DebugUtils.showToast(this, "验证码已发送");
                mBtnNextStep.setEnabled(true);
                startMessageLoginTwoActivity(MESSAGE);
                finish();
            } else {
                DebugUtils.showToastResponse(this, event.getMsg());
                mBtnNextStep.setEnabled(true);
            }
        }

        // 语音验证码
        if (Action.sms_login_send_code_voice == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                DebugUtils.showToast(this, "验证码已发送,请注意接听电话");
                mBtnNextStep.setEnabled(true);
                startMessageLoginTwoActivity(VOICE);
                finish();
            } else {
                DebugUtils.showToastResponse(this, event.getMsg());
                mBtnNextStep.setEnabled(true);
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
