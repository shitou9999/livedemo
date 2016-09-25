package tv.kuainiu.ui.me.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.UserHttpRequest;
import tv.kuainiu.command.preferences.UserPreferencesManager;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.event.UserEvent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.region.Region;
import tv.kuainiu.ui.region.RegionDataHelper;
import tv.kuainiu.ui.region.RegionSelectionActivity;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.KeyBoardUtil;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.SecurityUtils;
import tv.kuainiu.widget.TitleBarView;


/**
 * 登录
 */
//public class LoginActivity extends BaseActivity implements View.OnClickListener, Handler.Callback, PlatformActionListener { 社交账号登录
public class LoginActivity extends BaseActivity implements View.OnClickListener {
//    private static final int MSG_USERID_FOUND = 1;
//    private static final int MSG_LOGIN = 2;
//    private static final int MSG_AUTH_CANCEL = 3;
//    private static final int MSG_AUTH_ERROR = 4;
//    private static final int MSG_AUTH_COMPLETE = 5;

    @BindView(android.R.id.content) FrameLayout mFlRoot;
    @BindView(R.id.ll_region_selector) LinearLayout mRlRegionSelector;
    @BindView(R.id.tv_region_name) TextView mTvRegion;
    @BindView(R.id.tv_forget_password) TextView mTvForgetPassword;
    @BindView(R.id.tv_message_login) TextView mTvMessageLogin;
    @BindView(R.id.et_region) EditText mEtRegion;
    @BindView(R.id.et_account) EditText mEtAccount;
    @BindView(R.id.et_password) EditText mEtPassword;
    @BindView(R.id.btn_login) Button mBtnLogin;
    @BindView(R.id.imageButton_weibo) ImageButton mImageButtonWeibo;
    @BindView(R.id.imageButton_wechat) ImageButton mImageButtonWechat;
    @BindView(R.id.imageButton_qq) ImageButton mImageButtonQQ;
    @BindView(R.id.tbv_title) TitleBarView mTbvTitle;
    @BindView(R.id.tvRegister) TextView tvRegister;

    private boolean isKeyboardShown;
    private Map<String, Region> mRegionMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mTbvTitle.setText("登录");
        String text = mTvForgetPassword.getText().toString();
        SpannableString span = new SpannableString(text);
        span.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvForgetPassword.setText(span);

        String messageText = mTvMessageLogin.getText().toString().trim();
        SpannableString messageSpan = new SpannableString(messageText);
        messageSpan.setSpan(new UnderlineSpan(), 0, messageText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvMessageLogin.setText(messageSpan);
        initAccount();
        mRegionMap = RegionDataHelper.getRegionDataMap(this);
        initListener();
    }

    // XXX Review this code logic(审查此段代码逻辑)
    private void initAccount() {
        User user = UserPreferencesManager.getUserExtraInfo();
        if (user != null) {
            String area = user.getArea();
            if (!TextUtils.isEmpty(area) && (mRegionMap != null && mRegionMap.containsKey(area))) {
                mEtRegion.setText(area);
                mTvRegion.setText(mRegionMap.get(area).getRegionName());
            } else {
                mEtRegion.setText("86");
                mTvRegion.setText("中国");
            }

            String phone = user.getPhone();
            if (!TextUtils.isEmpty(phone)) {
                if (phone.length() < 18) {
                    mEtAccount.setText(phone);
                }
            }

        } else {
            mEtRegion.setText("86");
            mTvRegion.setText("中国");
        }

        mEtRegion.setSelection(mEtRegion.length());

        mEtAccount.requestFocus();
        mEtAccount.setSelection(mEtAccount.length());
    }


    @Override protected void onStart() {
        super.onStart();
        Intent data = getIntent();
        if (null != data) {
            String account = data.getStringExtra("account");
            if (!TextUtils.isEmpty(account)) {
                mEtAccount.setText(account);
            }
        }
    }


    private void initListener() {
        mBtnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        mTvForgetPassword.setOnClickListener(this);
        mTvMessageLogin.setOnClickListener(this);
        mImageButtonWeibo.setOnClickListener(this);
        mImageButtonWechat.setOnClickListener(this);
        mImageButtonQQ.setOnClickListener(this);
        mRlRegionSelector.setOnClickListener(this);

        mEtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode) {
                    login();
                }
                return false;
            }
        });


        mEtRegion.addTextChangedListener(new TextWatcher() {
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
                            mTvRegion.setText(countries);
                        } else {
                            mTvRegion.setText(getString(R.string.region_code_error));
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mEtAccount.setFocusable(true);
        mEtRegion.setSelection(mEtRegion.length());
        if (400 == requestCode && resultCode == 300) {
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


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;

            case R.id.tv_forget_password:
                Intent forget = new Intent(LoginActivity.this, ForgetPassword1Activity.class);
                startActivity(forget);
                mEtPassword.setText("");
                break;

            case R.id.ll_region_selector:
                Intent intent = new Intent(LoginActivity.this, RegionSelectionActivity.class);
                startActivityForResult(intent, 400);
                break;

            case R.id.tv_message_login:
                Intent messageLoginIntent = new Intent(LoginActivity.this, MessageLoginOneActivity.class);
                messageLoginIntent.putExtra("area", mEtRegion.getText().toString().trim());
                messageLoginIntent.putExtra("phone", mEtAccount.getText().toString().trim());
                startActivity(messageLoginIntent);
                break;
            case R.id.tvRegister:
                Intent registerIntent = new Intent(LoginActivity.this, Register1Activity.class);
                startActivity(registerIntent);
                break;

            default:
                break;
        }
    }

    private void login() {
        String area = mEtRegion.getText().toString();
        String account = mEtAccount.getText().toString();
        String password = mEtPassword.getText().toString();

        if (TextUtils.isEmpty(area)) {
            DebugUtils.showToast(this, "请选择地区");
            return;
        }

        if (Constant.AREA_CODE_ERROR.equals(mTvRegion.getText())) {
            DebugUtils.showToast(this, Constant.AREA_CODE_ERROR);
            return;
        }

        if (TextUtils.isEmpty(account)) {
            DebugUtils.showToast(this, "请填写账号");
        } else if (TextUtils.isEmpty(password)) {
            DebugUtils.showToast(this, "请填写密码");
        } else if (!NetUtils.isOnline(this)) {
            DebugUtils.showToast(this, R.string.toast_not_network);
        } else {
            UserHttpRequest.login(this, area, account, password);
            mBtnLogin.setEnabled(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLoginMain(UserEvent event) {
        mBtnLogin.setEnabled(true);
        if (Constant.SUCCEED == event.getCode()) {
            KeyBoardUtil.hideSoftInput(LoginActivity.this, mEtPassword);
            Snackbar snackbar = Snackbar.make(mBtnLogin, "正在登录......", Snackbar.LENGTH_LONG);
            User user = event.getUser();
            IGXApplication.setUser(user);
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constant.INTENT_ACTION_GET_CUSTOM));
            EventBus.getDefault().post(new HttpEvent(Action.login, Constant.SUCCEED));
            PreferencesUtils.putString(this, "phone", SecurityUtils.DESUtil.en(Api.PUBLIC_KEY, user.getPhone()));
            PreferencesUtils.putString(this, "area", SecurityUtils.DESUtil.en(Api.PUBLIC_KEY, user.getArea()));

            snackbar.show();
            new Handler().postDelayed(new Runnable() {
                @Override public void run() {
                    finish();
                }
            }, 1000);
        } else {
            DebugUtils.showToastResponse(this, event.getMsg());
        }
    }

    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

}
