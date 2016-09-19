package tv.kuainiu.ui.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import java.util.Locale;
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
import tv.kuainiu.ui.region.Region;
import tv.kuainiu.ui.region.RegionDataHelper;
import tv.kuainiu.ui.region.RegionSelectionActivity;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.util.DebugUtils;
import tv.kuainiu.util.KeyBoardUtil;
import tv.kuainiu.util.MatcherUtils;
import tv.kuainiu.util.NetUtils;


/**
 * 找回密码第一步
 */
public class ForgetPassword1Activity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_region_selector) LinearLayout mRlRegionSelector;
    @BindView(R.id.et_region_code) EditText mEtRegionCode;
    @BindView(R.id.tv_region_name) TextView mTvRegionName;
    @BindView(R.id.et_account) EditText mEtAccount;
    @BindView(R.id.btn_submit) Button mBtnSubmit;

    private MyCountDownTimer mDownTimer;
    private String account;
    private Map<String, Region> mRegionMap;
    private boolean mIsNumber;


    private final Handler mHideHandler = new Handler();

    private Runnable mHidePartRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(ForgetPassword1Activity.this, ForgetPassword2Activity.class);
            intent.putExtra("account", account);
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password1);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mRegionMap = RegionDataHelper.getRegionDataMap(this);
        mDownTimer = new MyCountDownTimer(60000L, 1000L);
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


    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        mDownTimer.cancel();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


    protected void initListener() {
        mBtnSubmit.setOnClickListener(this);
        mRlRegionSelector.setOnClickListener(this);
//        mLlRegionCodeGroup.setOnClickListener(this);

        mEtRegionCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (count > 0 && Character.isLetterOrDigit(s.charAt(before))) {
//                    mEtRegionCode.setEnabled(false);
//                    mRlRegionSelector.setEnabled(false);
////                    mEtRegionCode.setText("86");
////                    mTvRegion.setText("中国");
//                    mIsNumber = false;
//                } else {
//                    mEtRegionCode.setEnabled(true);
//                    mRlRegionSelector.setEnabled(true);
//                    mIsNumber = true;
//                }
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


        });


        mEtRegionCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String account = mEtAccount.getText().toString().trim();
                    if (account.length() > 0 && !MatcherUtils.matcher("[0-9]*", account)) {
                        v.setEnabled(false);
                    } else {
                        v.setEnabled(true);
                    }
                } else {
                    v.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mEtRegionCode.setSelection(mEtRegionCode.length());
        if (500 == requestCode && resultCode == 300) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Region region = (Region) bundle.get("region");
                    if (region != null) {
                        mEtRegionCode.setText(String.valueOf(region.getRegionCode()));
                        mTvRegionName.setText(region.getRegionName());
                    }
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                String area = mEtRegionCode.getText().toString().trim();
                account = mEtAccount.getText().toString().trim();

                if (TextUtils.isEmpty(account)) {
                    DebugUtils.showToast(ForgetPassword1Activity.this, "账号不能为空");
                    return;
                }

                if (MatcherUtils.matcher("[0-9]*", account)) {
                    if (TextUtils.isEmpty(area)) {
                        DebugUtils.showToast(this, "请输入地区号");
                        return;
                    }
                    if (Constant.AREA_CODE_ERROR.equals(mTvRegionName.getText())) {
                        DebugUtils.showToast(this, Constant.AREA_CODE_ERROR);
                        return;
                    }
                }


                if (!NetUtils.isOnline(ForgetPassword1Activity.this)) {
                    DebugUtils.showToast(ForgetPassword1Activity.this, R.string.toast_not_network);
                    return;
                }


                Map<String, String> map = new HashMap<>();
                map.put(Constant.KEY_AREA, area);
                map.put("user", account);
                OKHttpUtils.getInstance().post(ForgetPassword1Activity.this, Api.FORGET_PWD_SENDCODE, ParamUtil.getParam(map), Action.forget_pwd_sendcode);
                KeyBoardUtil.hideSoftInput(ForgetPassword1Activity.this, mEtAccount);
                mBtnSubmit.setEnabled(false);
                break;

            case R.id.ll_region_selector:
                String account = mEtAccount.getText().toString().trim();
                if (account.length() > 0 && !MatcherUtils.matcher("[0-9]*", account)) {
                    return;
                }
                forwordSwitchRegion();
                break;

            default:
                break;
        }
    }

    private void forwordSwitchRegion() {
        Intent intent = new Intent(ForgetPassword1Activity.this, RegionSelectionActivity.class);
        startActivityForResult(intent, 500);
    }


    @Override
    protected void onStop() {
        mDownTimer.cancel();
        mBtnSubmit.setEnabled(true);
        mBtnSubmit.setText(getString(R.string.lable_fetch_check_code));
        super.onStop();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HttpEvent event) {
        if (Action.forget_pwd_sendcode == event.getAction()) {
            DebugUtils.showToastResponse(ForgetPassword1Activity.this, event.getMsg());
            if (event.getCode() == Constant.SUCCEED) {
                mDownTimer.start();
                mHideHandler.removeCallbacks(mHidePartRunnable);
                mHideHandler.postDelayed(mHidePartRunnable, 3000);
            } else {
                mDownTimer.onFinish();
                mBtnSubmit.setText("获取验证码");
//                mBtnSubmit.setEnabled(true);
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


        @Override
        public void onTick(long millisUntilFinished) {
            if (null != mBtnSubmit) {
                long value = millisUntilFinished / 1000;
                mBtnSubmit.setText(String.format(Locale.CHINA, "重新发送(%d)", value));
            }
        }

        @Override
        public void onFinish() {
            if (null != mBtnSubmit) {
                mBtnSubmit.setEnabled(true);
                mBtnSubmit.setText(getResources().getString(R.string.to_resend));
            }
        }
    }
}
