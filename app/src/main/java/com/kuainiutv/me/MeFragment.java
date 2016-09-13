package com.kuainiutv.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kuainiutv.IGXApplication;
import com.kuainiutv.R;
import com.kuainiutv.app.Theme;
import com.kuainiutv.command.http.Api;
import com.kuainiutv.command.http.core.OKHttpUtils;
import com.kuainiutv.command.http.core.ParamUtil;
import com.kuainiutv.event.HttpEvent;
import com.kuainiutv.me.activity.LoginActivity;
import com.kuainiutv.me.activity.PersonalActivity;
import com.kuainiutv.modle.User;
import com.kuainiutv.modle.cons.Action;
import com.kuainiutv.modle.cons.Constant;
import com.kuainiutv.ui.fragment.BaseFragment;
import com.kuainiutv.util.DebugUtils;
import com.kuainiutv.util.ImageDisplayUtil;
import com.kuainiutv.util.PreferencesUtils;
import com.kuainiutv.util.StringUtils;
import com.kuainiutv.widget.dialog.LoginPromptDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author nanck on 2016/7/29.
 */
public class MeFragment extends BaseFragment {

    @BindView(R.id.srlRefresh) SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rl_avatar) RelativeLayout mRlAvatar;
    @BindView(R.id.rlHomePage) RelativeLayout rlHomePage;
    @BindView(R.id.tv_me_name) TextView mTvMeName;
    @BindView(R.id.mTvFollowCount) TextView mTvFollowCount;
    @BindView(R.id.ci_avatar) CircleImageView ci_avatar;
    @BindView(R.id.tv_me_phone) TextView mTvMePhone;
    @BindView(R.id.tv_me_home) TextView tv_me_home;
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

    private Context context;

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
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        User user = IGXApplication.getUser();
        uploadUserInfo();
        bindDataForView(user);
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
    }

    @OnClick({R.id.ci_avatar, R.id.rl_institution, R.id.rl_live, R.id.rl_appointment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ci_avatar:
                Intent intent = new Intent();
                Class clazz = !IGXApplication.isLogin() ? LoginActivity.class : PersonalActivity.class;
                intent.setClass(getActivity(), clazz);
                startActivity(intent);
                break;
            case R.id.rl_institution:
                break;
            case R.id.rl_live:
                break;
            case R.id.rl_appointment:
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
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,priority = 100)
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

        } else {
            displayAvatar(user.getAvatar());
            mTvMePhone.setText(StringUtils.getX(user.getPhone()));
            String nickName = TextUtils.isEmpty(user.getNickname()) ? "" : user.getNickname();
            mTvMeName.setText(nickName);

        }
        setFollowAndSubText(user);
    }

    private void setFollowAndSubText(User user) {
        if (user == null) {
            mTvFollowCount.setText(getString(R.string.value_follow_count_default));
        } else {
            mTvFollowCount.setText(StringUtils.getDecimal(user.getFollow_count(), Constant.TEN_THOUSAND, "万", ""));
        }
    }

    private void displayAvatar(String imagePath) {
        if (TextUtils.isEmpty(imagePath) || "".equals(imagePath)) {
            ImageDisplayUtil.displayImage(getActivity(), ci_avatar, R.mipmap.head_nor);
        } else {
            ImageDisplayUtil.displayImage(getActivity(), ci_avatar, StringUtils.replaceNullToEmpty(imagePath), R.mipmap.head_nor);
        }
    }
}
