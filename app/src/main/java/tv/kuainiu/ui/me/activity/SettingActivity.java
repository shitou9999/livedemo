package tv.kuainiu.ui.me.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.Constans;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.activity.GuideActivity;
import tv.kuainiu.updatesystem.UpdateManager;
import tv.kuainiu.utils.AppUtils;
import tv.kuainiu.utils.CacheManage;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.ShareUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.SwitchView;
import tv.kuainiu.widget.dialog.SelectSharpnessPopupWindow;

import static tv.kuainiu.app.Constans.APP_DOWN_URL;


/**
 * 设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.switch_allow_notWifi) SwitchView mSwitchView;
    //    @BindView(R.id.rl_settingAllowWifi) RelativeLayout mRlAllowWifi;
    @BindView(R.id.rl_settingSharpness) RelativeLayout mRlGuideSharpness;
    @BindView(R.id.rl_settingGuidePage) RelativeLayout mRlGuidePage;
    @BindView(R.id.rl_settingFeedback) RelativeLayout mRlFeedback;
    @BindView(R.id.rl_settingScore) RelativeLayout mRlScore;
    @BindView(R.id.rl_settingShareApp) RelativeLayout mRlShareApp;
    @BindView(R.id.rl_settingAboutMe) RelativeLayout mRlAboutMe;
    @BindView(R.id.rl_settingCheckUpdate) RelativeLayout mRlCheckUpdate;
    @BindView(R.id.rl_clear_caech) RelativeLayout rl_clear_caech;
    @BindView(R.id.tv_settingSharpness) TextView mTvSharpness;
    @BindView(R.id.tv_version_state) TextView tv_version_state;
    @BindView(R.id.tv_caech_size) TextView tv_caech_size;

    private SelectSharpnessPopupWindow menuWindow;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(getResources().getString(R.string.setting));
    }

    @Override
    protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        //  mRlAllowWifi.setOnClickListener(this);
        mRlGuideSharpness.setOnClickListener(this);
        mRlGuidePage.setOnClickListener(this);
        mRlFeedback.setOnClickListener(this);
        mRlScore.setOnClickListener(this);
        mRlShareApp.setOnClickListener(this);
        mRlCheckUpdate.setOnClickListener(this);
        mRlAboutMe.setOnClickListener(this);
        rl_clear_caech.setOnClickListener(this);

        mSwitchView.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                setAllowNotWifiCache();
            }

            @Override
            public void toggleToOff(View view) {
                mSwitchView.toggleSwitch(false);
                mEditor.putBoolean(Constant.CONFIG_KEY_NOTWIFI_DOWNLOAD, false);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_settingSharpness:
                menuWindow = new SelectSharpnessPopupWindow(this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.mainLayout),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.rl_settingGuidePage:
                Intent guideActivityIntent = new Intent(SettingActivity.this, GuideActivity.class);
                startActivity(guideActivityIntent);
                break;
            case R.id.rl_settingFeedback:
//                Intent feedback = new Intent(SettingActivity.this, FeedbackActivity.class);
//                startActivity(feedback);
                break;
            case R.id.rl_settingScore:
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    DebugUtils.showToast(this, "Couldn't launch the market !");
                }
                break;
            case R.id.rl_settingShareApp:
                ShareUtils.showShare(ShareUtils.APP, SettingActivity.this, "快牛直播", "快牛直播聚合资深商务牛人，为您创建价值资讯空间，线上点播，直播，同步查看数据分析，多维度分析揭秘，多视角透析行情；快牛直播，让价值讯息传递变得更简单！", Constans.LOGO_IMAGE_PATH, APP_DOWN_URL, null);
                break;
            case R.id.rl_settingAboutMe:
                Intent about = new Intent(this, AboutActivity.class);
                startActivity(about);
                break;
            case R.id.rl_settingCheckUpdate:
                CheckAppUpdate(this);
                break;
            case R.id.rl_clear_caech:
                try {
                    CacheManage.getInstance().clearAppCache(this);
                    setCacheSize();
                } catch (Exception e) {
                    LogUtils.e("SettingActivity", "clearAppCache", e);
                }
            default:
                break;
        }

    }

    /**
     * 初始化版本号
     */
    private void initData() {
        mSharedPreferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        boolean allowNotWifiCache = mSharedPreferences.getBoolean(Constant.CONFIG_KEY_NOTWIFI_DOWNLOAD, false);
        if (allowNotWifiCache) {
            mSwitchView.toggleSwitch(true);
        }
        initSharpnessText();
        if (PreferencesUtils.getInt(this, UpdateManager.SERVER_VERSION_CODE, 0) > PreferencesUtils.getInt(this, UpdateManager.CURRENT_VERSION_CODE, 0)) {
            tv_version_state.setText("新版本" + PreferencesUtils.getString(this, UpdateManager.SERVER_VERSION_NAME) + "，立即更新");
            tv_version_state.setTextColor(this.getResources().getColor(R.color.colorRed500));
            isUpdate = true;
        } else {
            tv_version_state.setText(AppUtils.getAppVersionName(this));
            tv_version_state.setTextColor(this.getResources().getColor(R.color.colorGrey600));
            isUpdate = false;
        }
        setCacheSize();
    }

    private void setCacheSize() {
        tv_caech_size.setText(StringUtils.replaceNullToEmpty(CacheManage.getInstance().CalculationCacheSize(this), ""));
    }

    public static void CheckAppUpdate(final Context context) {
        UpdateManager manager = new UpdateManager(context);
        manager.setApkName(context.getResources().getString(R.string.app_name));
        manager.setPackageInfo(AppUtils.getAppPackageName(context));
        manager.setICheckupdate(new UpdateManager.ICheckUpdate() {

            @Override
            public void result(boolean isnew) {
                if (!isnew && context instanceof SettingActivity) {
                    ToastUtils.showToast(context, "已经是最新版本了");
                }
            }
        });
        manager.checkUpdate();
    }

    /**
     * 设置是否允许在非wi_fi网络下缓存
     */
    private void setAllowNotWifiCache() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("可能会产生较高的流量费用，确认要开启吗?");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mSwitchView.toggleSwitch(false);
            }
        });
        builder.setPositiveButton("允许", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mEditor.putBoolean(Constant.CONFIG_KEY_NOTWIFI_DOWNLOAD, true);
                mSwitchView.toggleSwitch(true);
            }
        });
        builder.create();
        builder.show();
    }

    /**
     * 选择视频清晰度
     */
    private void initSharpnessText() {
        int value = mSharedPreferences.getInt(Constant.CONFIG_KEY_VIDEO_SHARPNESS, Constant.VIDEO_SHARPNESS_STANDARD);
        switch (value) {
            case Constant.VIDEO_SHARPNESS_STANDARD:
                mTvSharpness.setText("流畅");
                break;
            case Constant.VIDEO_SHARPNESS_HIGH:
                mTvSharpness.setText("高清");
                break;
            case Constant.VIDEO_SHARPNESS_SUPER:
                mTvSharpness.setText("超清");
                break;
        }
    }


    @Override
    protected void onPause() {
        if (null != mEditor) {
            mEditor.apply();
        }
        super.onPause();
    }


    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 流畅(标清)
                case R.id.btn_standard:
                    mEditor.putInt(Constant.CONFIG_KEY_VIDEO_SHARPNESS, Constant.VIDEO_SHARPNESS_STANDARD);
                    mTvSharpness.setText("流畅");
                    break;
                // 高清
                case R.id.btn_high:
                    mEditor.putInt(Constant.CONFIG_KEY_VIDEO_SHARPNESS, Constant.VIDEO_SHARPNESS_HIGH);
                    mTvSharpness.setText("高清");
                    break;
                // 超清
                case R.id.btn_super:
                    mEditor.putInt(Constant.CONFIG_KEY_VIDEO_SHARPNESS, Constant.VIDEO_SHARPNESS_SUPER);
                    mTvSharpness.setText("超清");
                    break;
                // 取消
                case R.id.btn_cancel:
                    menuWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {

    }
}
