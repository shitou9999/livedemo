package tv.kuainiu.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * Activity基类，提供以下方法 @see {@link #initContentView(int)}
 * 输出生命周期日志，可通过 @see {@link #setOutLifeEnable(String, boolean)}方法开。默认关闭。
 * 对具有输入文本功能的子类提供点击屏幕隐藏软键盘的接口 @see {@link #setTouchHideInput(boolean)}
 * Created by guxuan on 2016/3/2.
 */
public abstract class BaseActivity extends tv.kuainiu.ui.activity.BaseActivity {
    public static final String CLIENT_INIT_TIME = "client_init_time";
    public static final int CLIENT_INIT_NUMBER = 5;
    public static final int CLIENT_INIT_MINUTE = 5;
    protected static final int HTTP_SUCCEED = SUCCEED;
    public static int initClientNumbers = 0;

    protected @BindView(R.id.toolbar) Toolbar mToolbar;
    protected @BindView(R.id.tv_title) TextView mTitle;
    protected @BindView(R.id.tv_right_title) TextView mRightTitle;
    protected @BindView(R.id.title_toolbar_right_button) ImageButton mRightButton;

    private OnBackClickListener mOnBackClickListener;
    public boolean isUpLoadRegistrationID = false;

    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        mOnBackClickListener = onBackClickListener;
    }

    /**
     * 允许点击屏幕关闭软键盘
     */
    private boolean mIsTouchHideInput = false;

    /**
     * 是否打印生命周期日志
     */
    private boolean mIsOutLife = false;
    private String mTag;

    protected void setTouchHideInput(boolean bool) {
        mIsTouchHideInput = bool;
    }

    protected void setOutLifeEnable(String tag, boolean bool) {
        mIsOutLife = bool;
        mTag = tag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initContentView(R.layout.title_toolbar);
        initListener();
    }

    /**
     * 初始化视图
     *
     * @param layoutId layout id
     */
    protected void initContentView(int layoutId) {
        setContentView(layoutId);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setTitle("");
    }

    /**
     * 初始化事件监听
     */
    protected void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnBackClickListener) {
                    mOnBackClickListener.onBack();
                }
                finish();
            }
        });

        mRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightTitleClick();
            }
        });

        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightTitleClick();
            }
        });
    }

    /**
     * 设置居中标题
     *
     * @param charSequence title
     */
    protected void setMyTitle(CharSequence charSequence) {
        mTitle.setText(charSequence);
    }

    protected void setMyTitle(int resId) {
        mTitle.setText(getString(resId));
    }

    /**
     * 设置右标题
     *
     * @param charSequence
     */
    protected void setRightTitle(CharSequence charSequence) {
        mRightTitle.setText(charSequence);
    }

    protected void setRightTitle(int resId) {
        mRightTitle.setText(getString(resId));
    }


    /**
     * 设置右标题图片
     *
     * @param resId
     */
    protected void setRightButton(int resId) {
        mRightButton.setImageResource(resId);
    }

    /**
     * 设置右标题按钮可见状态
     *
     * @param visibility
     */
    protected void setRightButtonVisibility(int visibility) {
        mRightButton.setVisibility(visibility);
    }


    /**
     * 显示/隐藏右标题。默认隐藏
     *
     * @param type
     */
    protected void setRightTitleVisibility(int type) {
        mRightTitle.setVisibility(type);
    }

    /**
     * 右标题点击事件
     */
    protected void onRightTitleClick() {
    }

    /**
     * 标题栏返回事件
     */
    public interface OnBackClickListener {
        void onBack();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        boolean isReadPhone = PermissionManager.checkPermission(this, Manifest.permission.READ_PHONE_STATE);
//        boolean isStorage = PermissionManager.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (!isReadPhone || !isStorage) {
//            Intent intent = new Intent(this, SplashActivity.class);
//            startActivity(intent);
//            Constant.IS_FIRIST = true;
//            ExitUtil.getInstance().closes();
//        } else {
//            privateResume();
//        }
    }

    private void privateResume() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
//        if (!isUpLoadRegistrationID) {
//            String registrationID = JPushInterface.getRegistrationID(getApplicationContext());
//            JpushUtil.updoadPushId(this, registrationID);
//        }

        MobclickAgent.onResume(this);
    }




}
