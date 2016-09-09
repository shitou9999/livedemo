package com.iguxuan.iguxuan_friends.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iguxuan.iguxuan_friends.R;
import com.iguxuan.iguxuan_friends.app.Theme;
import com.iguxuan.iguxuan_friends.me.activity.LoginActivity;
import com.iguxuan.iguxuan_friends.ui.fragment.BaseFragment;

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
    @BindView(R.id.tv_me_name) TextView mTvMeName;
    @BindView(R.id.ci_avatar) CircleImageView ci_avatar;
    @BindView(R.id.tv_me_phone) TextView mTvMePhone;
    @BindView(R.id.tv_me_hone) TextView mTvMeHone;
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
        initView();
        return view;
    }

    private void initView() {
        srlRefresh.setColorSchemeColors(Theme.getLoadingColor());
    }

    @OnClick({R.id.ci_avatar, R.id.rl_institution, R.id.rl_live, R.id.rl_appointment})
    public void onClick(View view) {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
        switch (view.getId()) {
            case R.id.ci_avatar:

                break;
            case R.id.rl_institution:
                break;
            case R.id.rl_live:
                break;
            case R.id.rl_appointment:
                break;
        }
    }
}
