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
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.UserHttpRequest;
import tv.kuainiu.command.preferences.UserPreferencesManager;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.event.UserEvent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.me.thridLogin.LoginApi;
import tv.kuainiu.ui.me.thridLogin.OnLoginListener;
import tv.kuainiu.ui.region.Region;
import tv.kuainiu.ui.region.RegionDataHelper;
import tv.kuainiu.ui.region.RegionSelectionActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.KeyBoardUtil;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.SecurityUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.TitleBarView;

import static tv.kuainiu.R.id.et_password;
import static tv.kuainiu.R.id.et_region;
import static tv.kuainiu.R.id.rl_thrid_login;
import static tv.kuainiu.R.id.tbv_title;
import static tv.kuainiu.R.id.tv_forget_password;
import static tv.kuainiu.ui.me.activity.ThridAccountVerifyActivity.PLATFORM_AVATAR;
import static tv.kuainiu.ui.me.activity.ThridAccountVerifyActivity.PLATFORM_ID;
import static tv.kuainiu.ui.me.activity.ThridAccountVerifyActivity.PLATFORM_NAME;


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
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;
    @BindView(R.id.ll_region_selector)
    LinearLayout mRlRegionSelector;
    @BindView(R.id.tv_region_name)
    TextView mTvRegion;
    @BindView(tv_forget_password)
    TextView mTvForgetPassword;
    @BindView(R.id.tv_message_login)
    TextView mTvMessageLogin;
    @BindView(et_region)
    EditText mEtRegion;
    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(et_password)
    EditText mEtPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.imageButton_weibo)
    ImageView mImageButtonWeibo;
    @BindView(R.id.imageButton_wechat)
    ImageView mImageButtonWechat;
    @BindView(R.id.imageButton_qq)
    ImageView mImageButtonQQ;
    @BindView(tbv_title)
    TitleBarView mTbvTitle;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.ivClearText)
    ImageView ivClearText;
    @BindView(R.id.vsAccountBind)
    ViewStub vsAccountBind;
    @BindView(R.id.ll_password)
    LinearLayout llPassword;
    @BindView(R.id.ll_input)
    LinearLayout llInput;
    @BindView(R.id.fl_bottom_label)
    TextView flBottomLabel;
    @BindView(rl_thrid_login)
    RelativeLayout rlThridLogin;
    @BindView(R.id.rl_login_content_root)
    RelativeLayout rlLoginContentRoot;
    CircleImageView ivPlatFromImage;
    private boolean isKeyboardShown;
    private Map<String, Region> mRegionMap;
    private Handler handler;
    /**
     * 第三方平台id
     */
    private String platform_id = "";
    private String platform_avatar = "";
    /**
     * 第三方平台名称
     */
    private String platform_name = "";

    private String area = "";
    private String area_country = "";
    private String account = "";
    private Platform mPlatform;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mTbvTitle.setText("登录");
        handler = new Handler();
        String text = mTvForgetPassword.getText().toString();
        SpannableString span = new SpannableString(text);
        span.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvForgetPassword.setText(span);

        String messageText = mTvMessageLogin.getText().toString().trim();
        SpannableString messageSpan = new SpannableString(messageText);
        messageSpan.setSpan(new UnderlineSpan(), 0, messageText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvMessageLogin.setText(messageSpan);
        platform_name = getIntent().getStringExtra(PLATFORM_NAME);
        platform_id = getIntent().getStringExtra(PLATFORM_ID);
        platform_avatar = getIntent().getStringExtra(PLATFORM_AVATAR);
        area = getIntent().getStringExtra(ThridAccountVerifyActivity.AREA_CODE);
        account = getIntent().getStringExtra(ThridAccountVerifyActivity.ACCOUNT);
        area_country = getIntent().getStringExtra(ThridAccountVerifyActivity.AREA_COUNTRY);
        mRegionMap = RegionDataHelper.getRegionDataMap(this);
        snackbar = Snackbar.make(mBtnLogin, "正在登录......", Snackbar.LENGTH_LONG);
        initAccount();
        initListener();
    }

    // XXX Review this code logic(审查此段代码逻辑)
    private void initAccount() {
        User user = UserPreferencesManager.getUserExtraInfo();
        if (!TextUtils.isEmpty(area_country)) {
            mEtRegion.setText(area);
            mTvRegion.setText(area_country);
        } else if (user != null) {
            area = user.getArea();
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
        if (!TextUtils.isEmpty(account)) {
            mEtAccount.setText(account.trim());
            mEtAccount.setEnabled(false);
        } else {
            mEtAccount.requestFocus();
            mEtAccount.setSelection(mEtAccount.length());
        }
        if (!TextUtils.isEmpty(platform_name)) {
            vsAccountBind.setVisibility(View.VISIBLE);
            mTvForgetPassword.setVisibility(View.INVISIBLE);
            mTvMessageLogin.setVisibility(View.INVISIBLE);
            rlThridLogin.setVisibility(View.GONE);
            tvRegister.setVisibility(View.GONE);
            ivClearText.setVisibility(View.GONE);
            mEtRegion.setEnabled(false);
            ivPlatFromImage = (CircleImageView) findViewById(R.id.ivPlatFromImage);
            ivPlatFromImage.setSelected(true);
            mRlRegionSelector.setEnabled(false);
            mEtPassword.requestFocus();
            mEtPassword.setSelection(mEtPassword.length());
//            if (SinaWeibo.NAME.equals(platform_name)) {
//                ivPlatFromImage.setImageResource(R.drawable.selector_share_sina);
//            }
//            if (QQ.NAME.equals(platform_name)) {
//                ivPlatFromImage.setImageResource(R.drawable.selector_share_qq);
//            }
//            if (Wechat.NAME.equals(platform_name)) {
//                ivPlatFromImage.setImageResource(R.drawable.selector_share_wechat);
//            }
            ImageDisplayUtil.displayImage(this, ivPlatFromImage, platform_avatar);
        }
    }


    @Override
    protected void onStart() {
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
        ivClearText.setOnClickListener(this);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClearText:
                mEtAccount.setText("");
                break;
            case R.id.btn_login:
                login();
                break;

            case tv_forget_password:
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
            case R.id.imageButton_weibo:
                //新浪微博
                login(SinaWeibo.NAME);
                break;
            case R.id.imageButton_wechat:
//                ToastUtils.showToast(LoginActivity.this, "微信登录暂未开放");
                login(Wechat.NAME);
                break;
            case R.id.imageButton_qq:
//                ToastUtils.showToast(LoginActivity.this, "qq登录暂未开放");
                login(QQ.NAME);
                break;
            default:
                break;
        }
    }

    /*
       * 执行第三方登录/注册的方法
       * <p>
       * @param platformName 执行登录/注册的平台名称，如：SinaWeibo.NAME
       */
    private void login(String platformName) {
        snackbar.show();
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(new OnLoginListener() {
            public void onLogin(Platform mPlatform) {
                LoginActivity.this.mPlatform = mPlatform;
                String type = "";
                String platform_name = mPlatform.getName();
                if (SinaWeibo.NAME.equals(platform_name)) {
                    type = "wb";
                }
                if (QQ.NAME.equals(platform_name)) {
                    type = "qq";
                }
                if (Wechat.NAME.equals(platform_name)) {
                    type = "wx";
                }
                String platform_id = mPlatform.getDb().getUserId();
                String platform_token = mPlatform.getDb().getToken();
                String platform_nickname = mPlatform.getDb().getUserName();
                String platform_avatar = mPlatform.getDb().getUserIcon();
                long platform_expires_in = mPlatform.getDb().getExpiresTime() / 1000 + mPlatform.getDb().getExpiresIn();
                UserHttpRequest.thirdLoginCheck(LoginActivity.this, type, platform_token, platform_id, platform_nickname, platform_avatar, platform_expires_in, Action.third_login);
            }

            @Override
            public void error(String errorMsg, Throwable error) {
                snackbar.dismiss();
                ToastUtils.showToast(LoginActivity.this, error == null ? errorMsg : error.getMessage());
            }

            @Override
            public void cancel() {
                snackbar.dismiss();
                ToastUtils.showToast(LoginActivity.this, "取消登录");
            }

        });
        api.login(this);
    }

    private void login() {
        String area = mEtRegion.getText().toString();
        account = mEtAccount.getText().toString();
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
            snackbar.show();
            UserHttpRequest.login(this, area, account, password, platform_id, platform_name);
            mBtnLogin.setEnabled(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case third_login:
                snackbar.dismiss();
                if (Constant.SUCCEED == event.getCode()) {
                    User user = new Gson().fromJson(event.getData().optString("data"), User.class);
                    MyApplication.setUser(user);
                    ToastUtils.showToast(this, "登录成功");
                    EventBus.getDefault().post(new HttpEvent(Action.login, Constant.SUCCEED));
                    finish();
                } else if (-201 == event.getCode()) {
                    ThridAccountVerifyActivity.intoNewActivity(LoginActivity.this, mPlatform);
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "第三方登录失败"));
                }
                break;
            case login_finish:
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLoginMain(UserEvent event) {
        mBtnLogin.setEnabled(true);
        snackbar.dismiss();
        if (Constant.SUCCEED == event.getCode()) {
            KeyBoardUtil.hideSoftInput(LoginActivity.this, mEtPassword);
            User user = event.getUser();
            MyApplication.setUser(user);
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constant.INTENT_ACTION_GET_CUSTOM));
            EventBus.getDefault().post(new HttpEvent(Action.login, Constant.SUCCEED));
            PreferencesUtils.putString(this, "phone", SecurityUtils.DESUtil.en(Api.PUBLIC_KEY, user.getPhone()));
            PreferencesUtils.putString(this, "area", SecurityUtils.DESUtil.en(Api.PUBLIC_KEY, user.getArea()));
            EventBus.getDefault().post(new HttpEvent(Action.login_finish, Constant.SUCCEED));
            finish();
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
