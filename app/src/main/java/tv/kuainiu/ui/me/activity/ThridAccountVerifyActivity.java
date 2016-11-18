package tv.kuainiu.ui.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.command.preferences.UserPreferencesManager;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.region.Region;
import tv.kuainiu.ui.region.RegionDataHelper;
import tv.kuainiu.ui.region.RegionSelectionActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.MatcherUtils;
import tv.kuainiu.utils.NetUtils;


/**
 * 找回密码第一步
 */
public class ThridAccountVerifyActivity extends BaseActivity implements View.OnClickListener {
    public static final String PLATFORM_ID = "platformId";
    public static final String PLATFORM_NAME = "platformName";
    public static final String PLATFORM_TOKEN_SECRET = "platform_token_secret";
    public static final String ACCOUNT = "account";
    public static final String AREA_CODE = "area_code";
    public static final String AREA_COUNTRY = "area_country";
    @BindView(R.id.ll_region_selector)
    LinearLayout mRlRegionSelector;
    @BindView(R.id.et_region_code)
    EditText mEtRegionCode;
    @BindView(R.id.tv_region_name)
    TextView mTvRegionName;
    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.ivClearText)
    ImageView ivClearText;

    private String account;
    private Map<String, Region> mRegionMap;
    private boolean mIsNumber;
    /**
     * 第三方平台id
     */
    private String platform_id = "";
    /**
     * 第三方平台名称
     */
    private String platform_name = "";
    /**
     * 第三方平台token
     */
    private String platform_token_secret = "";

    CircleImageView ivPlatFromImage;

    public static void intoNewActivity(Context context, Platform platform) {
        Intent intent = new Intent(context, ThridAccountVerifyActivity.class);
        intent.putExtra(PLATFORM_ID, platform.getDb().getUserId());
        intent.putExtra(PLATFORM_NAME, platform.getName());
        intent.putExtra(PLATFORM_TOKEN_SECRET, platform.getDb().getTokenSecret());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrid_account_verify);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mRegionMap = RegionDataHelper.getRegionDataMap(this);
        platform_id = getIntent().getStringExtra(PLATFORM_ID);
        platform_name = getIntent().getStringExtra(PLATFORM_NAME);
        platform_token_secret = getIntent().getStringExtra(PLATFORM_TOKEN_SECRET);
        initListener();
        initAccount();
    }

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
        ivPlatFromImage = (CircleImageView) findViewById(R.id.ivPlatFromImage);
        ivPlatFromImage.setSelected(true);
        if (SinaWeibo.NAME.equals(platform_name)) {
            ivPlatFromImage.setImageResource(R.drawable.selector_share_sina);
        }
        if (QQ.NAME.equals(platform_name)) {
            ivPlatFromImage.setImageResource(R.drawable.selector_share_qq);
        }
        if (Wechat.NAME.equals(platform_name)) {
            ivPlatFromImage.setImageResource(R.drawable.selector_share_wechat);
        }
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


    protected void initListener() {
        mBtnSubmit.setOnClickListener(this);
        ivClearText.setOnClickListener(this);
        mRlRegionSelector.setOnClickListener(this);
//        mLlRegionCodeGroup.setOnClickListener(this);

        mEtRegionCode.addTextChangedListener(new TextWatcher() {
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
                            mTvRegionName.setText(countries);
                        } else {
                            mTvRegionName.setText(getString(R.string.region_code_error));
                        }
                    }
                }


            }


        });


        mEtRegionCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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
            case R.id.ivClearText:
                mEtAccount.setText("");
                break;
            case R.id.btn_submit:
                String area = mEtRegionCode.getText().toString().trim();
                account = mEtAccount.getText().toString().trim();

                if (TextUtils.isEmpty(account)) {
                    DebugUtils.showToast(ThridAccountVerifyActivity.this, "账号不能为空");
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


                if (!NetUtils.isOnline(ThridAccountVerifyActivity.this)) {
                    DebugUtils.showToast(ThridAccountVerifyActivity.this, R.string.toast_not_network);
                    return;
                }
                //TODO 请求服务器，验证是否注册过会绑定过
                //TODO  1 绑定过直接返回快牛平台账号信息
                //TODO 2 已有账号未绑定，调到登录界面绑定
                //TODO 3没有账号则跳到注册界面注册
                Intent intent = new Intent();
                intent.putExtra(PLATFORM_TOKEN_SECRET, platform_token_secret);
                intent.putExtra(PLATFORM_NAME, platform_name);
                intent.putExtra(ACCOUNT, account);
                intent.putExtra(AREA_CODE, area);
                intent.putExtra(AREA_COUNTRY, mTvRegionName.getText().toString());
                intent.setClass(ThridAccountVerifyActivity.this, LoginActivity.class);
//                intent.setClass(ThridAccountVerifyActivity.this, Register1Activity.class);
                startActivity(intent);
//                Map<String, String> map = new HashMap<>();
//                map.put(Constant.KEY_AREA, area);
//                map.put("user", account);
//                OKHttpUtils.getInstance().post(ThridAccountVerifyActivity.this, Api.FORGET_PWD_SENDCODE, ParamUtil.getParam(map), Action.forget_pwd_sendcode);
//                KeyBoardUtil.hideSoftInput(ThridAccountVerifyActivity.this, mEtAccount);
//                mBtnSubmit.setEnabled(false);
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
        Intent intent = new Intent(ThridAccountVerifyActivity.this, RegionSelectionActivity.class);
        startActivityForResult(intent, 500);
    }


    @Override
    protected void onStop() {
        mBtnSubmit.setEnabled(true);
        super.onStop();
    }


}
