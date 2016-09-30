package tv.kuainiu.ui.me;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.me.activity.CollectActivity;
import tv.kuainiu.ui.me.activity.FollowActivity;
import tv.kuainiu.ui.me.activity.LoginActivity;
import tv.kuainiu.ui.me.activity.PersonalActivity;
import tv.kuainiu.ui.me.activity.SettingActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

/**
 * @author nanck on 2016/7/29.
 */
public class MeFragment extends BaseFragment {

    @BindView(R.id.srlRefresh) SwipeRefreshLayout srlRefresh;

    @BindView(R.id.ivSetting) ImageView ivSetting;
    @BindView(R.id.ivMessage) ImageView ivMessage;

    @BindView(R.id.rl_avatar) RelativeLayout mRlAvatar;
    @BindView(R.id.rlLogOut) RelativeLayout rlLogOut;
    @BindView(R.id.rlHomePage) RelativeLayout rlHomePage;
    @BindView(R.id.tv_me_name) TextView mTvMeName;
    @BindView(R.id.mTvFollowCount) TextView mTvFollowCount;
    @BindView(R.id.ci_avatar) CircleImageView ci_avatar;
    @BindView(R.id.tv_me_phone) TextView mTvMePhone;
    @BindView(R.id.tv_me_home) TextView tv_me_home;
    @BindView(R.id.tvFans) TextView tvFans;
    @BindView(R.id.iv_text_vip) ImageView mIvTextVip;
    @BindView(R.id.iv_icon_vip) ImageView mIvIconVip;
    @BindView(R.id.iv_icon_play) ImageView mIvIconPlay;
    @BindView(R.id.iv_icon_video) ImageView mIvIconVideo;
    @BindView(R.id.iv_icon_group) ImageView mIvIconGroup;
    @BindView(R.id.iv_icon_lesson) ImageView mIvIconLesson;
    @BindView(R.id.iv_icon_institution) ImageView mIvIconInstitution;
    @BindView(R.id.iv_institution) ImageView mIvInstitution;
    @BindView(R.id.tv_institution) TextView mTvInstitution;
    @BindView(R.id.iv_institution_right) ImageView mIvInstitutionRight;
    @BindView(R.id.tv_institution_tip) TextView mTvInstitutionTip;
    @BindView(R.id.rl_institution) RelativeLayout mRlInstitution;
    @BindView(R.id.iv_live) ImageView mIvLive;
    @BindView(R.id.tv_live) TextView mTvLive;
    @BindView(R.id.iv_live_right) ImageView mIvLiveRight;
    @BindView(R.id.tv_live_tip) TextView mTvLiveTip;
    @BindView(R.id.rl_live) RelativeLayout mRlLive;
    @BindView(R.id.iv_appointment) ImageView mIvAppointment;
    @BindView(R.id.iv_appointment_right) ImageView mIvAppointmentRight;
    @BindView(R.id.tv_appointment_tip) TextView mTvAppointmentTip;
    @BindView(R.id.rl_appointment) RelativeLayout mRlAppointment;
    @BindView(R.id.fl_tab_frag) LinearLayout mFlTabFrag;
    @BindView(R.id.llJurisdiction) LinearLayout mLlJurisdiction;
    @BindView(R.id.btnPublish) Button mBtnPublish;
    @BindView(R.id.rlFollow) RelativeLayout mRlFollow;
    @BindView(R.id.rlSub) RelativeLayout mRlSub;
    @BindView(R.id.rlDown) RelativeLayout mRlDown;
    @BindView(R.id.rlCollect) RelativeLayout mRlCollect;
    @BindView(R.id.rlRecorder) RelativeLayout mRlRecorder;

    private Context context;
    private boolean isShowLoginTip;

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_me, container, false);
            ButterKnife.bind(this, view);
            context = getActivity();
            initView();
        }
        ViewGroup viewgroup = (ViewGroup) view.getParent();
        if (viewgroup != null) {
            viewgroup.removeView(view);
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        uploadUserInfo();

    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    private void initView() {
        srlRefresh.setColorSchemeColors(Theme.getLoadingColor());
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                uploadUserInfo();
            }
        });
    }

    @OnClick({R.id.ivSetting, R.id.rlLogOut, R.id.ci_avatar, R.id.rl_institution, R.id.rl_live, R.id.rl_appointment,
            R.id.rlFollow, R.id.rlSub, R.id.rlDown, R.id.rlCollect, R.id.rlRecorder,R.id.ivEdite,R.id.tv_me_name,
            R.id.tv_me_phone,R.id.rlHomePage})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rlHomePage:
            case R.id.tv_me_phone:
            case R.id.tv_me_name:
            case R.id.ivEdite:
            case R.id.ci_avatar:
                Intent personalIntent = new Intent();
                personalIntent.setClass(getActivity(), PersonalActivity.class);
                startActivity(personalIntent);
                break;
            case R.id.rl_institution:
                break;
            case R.id.rl_live:
                break;
            case R.id.rl_appointment:
                break;
            case R.id.rlLogOut:
                Intent loginIntent = new Intent();
                loginIntent.setClass(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.rlFollow:
                if(!IGXApplication.isLogin()){
                    showLoginTip();
                    return;
                }
                Intent intentFollow = new Intent();
                intentFollow.setClass(getActivity(), FollowActivity.class);
                startActivity(intentFollow);
                break;
            case R.id.rlSub:
                break;
            case R.id.rlDown:
                break;
            case R.id.rlCollect:
                if(!IGXApplication.isLogin()){
                    showLoginTip();
                    return;
                }
                Intent intentCollect = new Intent();
                intentCollect.setClass(getActivity(), CollectActivity.class);
                startActivity(intentCollect);
                break;
            case R.id.rlRecorder:
                break;
            case R.id.ivSetting:
                Intent intentSettingActivity = new Intent();
                intentSettingActivity.setClass(getActivity(), SettingActivity.class);
                startActivity(intentSettingActivity);
                break;
        }
    }

    /**
     * 跳转
     *
     * @param clazz Class type
     */
    private void forward(Class clazz) {
        if (IGXApplication.isLogin()) {
            Intent intent = new Intent(getActivity(), clazz);
            startActivity(intent);
        } else {
            new LoginPromptDialog(getActivity()).show();
        }
    }

    private void uploadUserInfo() {
        if (IGXApplication.isLogin()) {
            OKHttpUtils.getInstance().post(getActivity(), Api.TEST_DNS_API_HOST_V2, Api.FETCH_USERINFO, ParamUtil.getParam(null), Action.fetch_userinfo);
        } else {
            User user = IGXApplication.getUser();
            bindDataForView(user);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,
            priority = 100)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case login:
                if (Constant.SUCCEED == event.getCode()) {
                    User user = IGXApplication.getUser();
                    if (user != null) {
                        bindDataForView(user);
                    } else {
                        uploadUserInfo();
                    }
                }
                break;
            case off_line:
                bindDataForView(IGXApplication.getUser());
                break;
            case fetch_userinfo:
                if (Constant.SUCCEED == event.getCode()) {
                    DebugUtils.dd(event.getData().toString());
                    String json = event.getData().optString("data");
                    User user = new Gson().fromJson(json, User.class);
                    DebugUtils.dd(user.toString());
                    displayAvatar(user.getAvatar());
                    setFollowAndSubText(user);
                    IGXApplication.setUser(user);
                    PreferencesUtils.putInt(context, Constant.MSG_NUM, user.getMsg_num());
//                    localBroadcastManager.sendBroadcast(new Intent(Constant.INTENT_ACTION_ACTIVITY_MSG_NUM));
                }
                bindDataForView(IGXApplication.getUser());
                break;
        }
    }

    private void bindDataForView(User user) {
        if (user == null) {
            String nullValue = getActivity().getResources().getString(R.string.null_value);
            displayAvatar("");
            mTvMePhone.setText(nullValue);
            mTvMeName.setText(nullValue);
            rlLogOut.setVisibility(View.VISIBLE);
            tvFans.setText(nullValue);
        } else {
            displayAvatar(user.getAvatar());
            mTvMePhone.setText(StringUtils.getX(user.getPhone()));
            String nickName = TextUtils.isEmpty(user.getNickname()) ? "" : user.getNickname();
            mTvMeName.setText(nickName);
            tvFans.setText(StringUtils.getDecimal(user.getFans_count(), Constant.TEN_THOUSAND, "万", ""));
            rlLogOut.setVisibility(View.GONE);
        }
        setFollowAndSubText(user);
    }

    private void setFollowAndSubText(User user) {
        if (user == null) {
            mTvFollowCount.setText("-");
        } else {
            mTvFollowCount.setText(StringUtils.getDecimal(user.getFollow_count(), Constant.TEN_THOUSAND, "万", ""));
        }
        srlRefresh.setRefreshing(false);
    }

    private void displayAvatar(String imagePath) {
        if (TextUtils.isEmpty(imagePath) || "".equals(imagePath)) {
            ImageDisplayUtil.displayImage(getActivity(), ci_avatar, R.mipmap.default_avatar);
        } else {
            ImageDisplayUtil.displayImage(getActivity(), ci_avatar, StringUtils.replaceNullToEmpty(imagePath), R.mipmap.default_avatar);
        }
    }
    private void showLoginTip() {
        if (isShowLoginTip) {
            return;
        }
        LoginPromptDialog loginPromptDialog = new LoginPromptDialog(getActivity());
        loginPromptDialog.setCallBack(new LoginPromptDialog.CallBack() {
            @Override
            public void onCancel(DialogInterface dialog, int which) {

            }

            @Override
            public void onLogin(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                isShowLoginTip = true;
            }

            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowLoginTip = false;
            }
        });
        loginPromptDialog.show();
    }

}
