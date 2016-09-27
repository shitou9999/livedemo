package tv.kuainiu.ui.liveold.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.liveold.LiveHttpUtil;
import tv.kuainiu.ui.liveold.PlayLiveActivity;
import tv.kuainiu.ui.liveold.model.LivingInfo;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.widget.NetErrAddLoadView;
import tv.kuainiu.widget.dialog.LoginPromptDialog;


public class LiveFragment extends BaseFragment implements View.OnClickListener, LiveTodayFragment.OnRetryRequestListener {
    private static final String TAG_LIFE = "LiveFragment_LIFE";
    private static final long REFILDAYS_TIME = 30 * 60 * 1000;
    private static final long REFILDAYS_TIME_TEST = 60 * 1000;
    public static final String ARG_LIVING = "living";


    @BindView(R.id.tab_live_top) TabLayout mTabLayout;
    @BindView(R.id.vp_live_home) ViewPager mViewPager;
    @BindView(R.id.rl_bottom_group) RelativeLayout mRlBottomGroup;
    @BindView(R.id.ll_live_online_group) LinearLayout mLlOnLineUserGroup;
    @BindView(R.id.tv_live_online_user_count) TextView mTvOnLineUserCount;
    @BindView(R.id.tv_live_into) TextView mTvInto;
    @BindView(R.id.iv_live_play_button) ImageView mImgPlay;
    @BindView(R.id.ib_live_like) ImageButton mImgButtonLike;
    @BindView(R.id.tv_live_title) TextView mTvLiveTitle;
    @BindView(R.id.iv_live_image) ImageView mIvBanner;
    @BindView(R.id.civ_avatar) CircleImageView mCivAuthor;
    @BindView(R.id.tv_live_anchor_name) TextView mTvAnchor;
    @BindView(R.id.tv_live_like_count) TextView mTvLikeCount;
    @BindView(R.id.err_layout) NetErrAddLoadView mErrView;

    private LivingInfo mLivingInfo;
    private boolean isLike = false;

    private TabLayout.OnTabSelectedListener mOnTabSelectedListener =
            new TabLayout.OnTabSelectedListener() {
                @Override public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    mViewPager.setCurrentItem(position);
                }

                @Override public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override public void onTabReselected(TabLayout.Tab tab) {

                }
            };

    private Handler mHandler = new Handler();
    private Runnable mTimeTaskRunnable = new Runnable() {
        @Override public void run() {
            time += REFILDAYS_TIME_TEST / 1000 / 60;
            DebugUtils.dd("刷新 : " + time + " m");
            initHttp();
            LiveHttpUtil.fetchLiveList(getActivity());
            mHandler.postDelayed(mTimeTaskRunnable, REFILDAYS_TIME_TEST);
        }
    };

    private int time = 0;


    private void initView() {
        LiveChildFragmentAdapter adapter = new LiveChildFragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mHandler.removeCallbacks(mTimeTaskRunnable);
        mHandler.postDelayed(mTimeTaskRunnable, REFILDAYS_TIME_TEST);
    }


    private void initListener() {
        mErrView.setOnTryCallBack(new NetErrAddLoadView.OnTryCallBack() {
            @Override
            public void callAgain() {
                initHttp();
            }
        });
        mTabLayout.setOnTabSelectedListener(mOnTabSelectedListener);
        mImgPlay.setOnClickListener(this);
        mImgButtonLike.setOnClickListener(this);
        mTvInto.setOnClickListener(this);
    }


    private void initHttp() {
        LiveHttpUtil.fetchLiveNowTopInfo(getActivity());
    }


    @Override public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        View view = inflater.inflate(R.layout.fragment_live, container, false);
        ViewGroup viewgroup = (ViewGroup) view.getParent();
        if (viewgroup != null) {
            viewgroup.removeView(view);
        }
        ButterKnife.bind(this, view);
        initView();
        initListener();
        initHttp();
        return view;
    }


    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mTimeTaskRunnable);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_live_into:
            case R.id.iv_live_play_button:
                startPlayingActivity();
                break;
            case R.id.ib_live_like:
                if (!IGXApplication.isLogin()) {
                    new LoginPromptDialog(getActivity()).show();
                    return;
                }
                like();
                break;
            default:
                break;
        }
    }


    private void startPlayingActivity() {
        Activity context = getActivity();
        if (context == null) return;
        if (mLivingInfo == null || mLivingInfo.getLiveing() == null) {
            DebugUtils.showToast(context, R.string.live_toast_not_started);
            return;
        }
        Intent intent = new Intent(context, PlayLiveActivity.class);
        intent.putExtra(ARG_LIVING, mLivingInfo);
        startActivity(intent);
    }


    private void like() {
        if (isLike) {
            DebugUtils.showToast(getActivity(), R.string.live_toast_liked);
        }
        //
        else if (!NetUtils.isOnline(getActivity())) {
            DebugUtils.showToast(getActivity(), R.string.toast_not_network);
        }
        //
        else if (mLivingInfo == null || mLivingInfo.getLiveing() == null) {
            DebugUtils.showToast(getActivity(), R.string.live_toast_not_information);
        }
        //
        else {
            LiveHttpUtil.executeAddLike(getActivity(), mLivingInfo.getLiveing().getId());
        }

    }

    private void BindViewDataToView(LivingInfo info) {
        if (info == null) {
            return;
        }
        // FIXME: 2016/7/21  Temp change info title text
        // info.getLiveing().setTitle("李厚霖夺顶554、圆顶地。。、顶替？ ？。杉矶之战、，‘、，【】霜压顶李昌奎爆棚李昌奎瓠果涯杨万里瓢泼大雨地34234u283r782");
        //////

        ImageDisplayUtil.displayImage(getActivity(), mIvBanner, info.getImg());

        LivingInfo.LiveingEntity entry = info.getLiveing();
        if (entry == null) {
            mRlBottomGroup.setVisibility(View.INVISIBLE);
        } else {
            ImageDisplayUtil.displayImage(getActivity(), mCivAuthor, entry.getThumb(), R.mipmap.ic_def_error);
            mTvLiveTitle.setText(entry.getTitle());
            mTvAnchor.setText(entry.getAnchor());
            mTvLikeCount.setText(String.valueOf(entry.getSupport()));
            mRlBottomGroup.setVisibility(View.VISIBLE);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(HttpEvent event) {
        switch (event.getAction()) {
            case live_fetch_living_info:
                onEventFetchTopInfo(event);
                mErrView.StopLoading(event.getCode(), event.getMsg());
                break;

            case live_add_like:
                onEventAddLike(event);
                break;

            default:
                break;
        }
    }

    public void onEventFetchTopInfo(HttpEvent event) {
        if (Constant.SUCCEED == event.getCode()) {
            try {
                DebugUtils.dd("Live paling info : " + event.getData().toString());
                JsonParser parser = new JsonParser();
                JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                JsonObject json = tempJson.getAsJsonObject("data");
                mLivingInfo = new DataConverter<LivingInfo>().JsonToObject(json.toString(), LivingInfo.class);
                BindViewDataToView(mLivingInfo);
            } catch (Exception e) {
                // mErrView.StopLoading(event.getCode(), event.getMsg());
            }
        }
    }

    public void onEventAddLike(HttpEvent event) {
        DebugUtils.dd("like code : " + event.getCode());
        DebugUtils.dd("like msg : " + event.getMsg());
        if (Constant.SUCCEED == event.getCode()) {
            DebugUtils.showToastResponse(getActivity(), event.getMsg());
            try {
                int likeCount = mLivingInfo.getLiveing().getSupport();
                likeCount = likeCount + 1;
                mLivingInfo.getLiveing().setSupport(likeCount);
                mTvLikeCount.setText(StringUtils.getDecimal(likeCount, Constant.TEN_THOUSAND, "万", ""));
            } catch (NullPointerException e) {
                e.printStackTrace();
            } finally {
                isLike = true;
            }
        } else {
            if (-2 == event.getCode()) {
                isLike = true;
            }
            DebugUtils.showToastResponse(getActivity(), event.getMsg());
        }
    }

    @Override public void request() {
        if (mLivingInfo == null || mLivingInfo.getLiveing() == null) {
            initHttp();
        }
    }

    // ---------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    private class LiveChildFragmentAdapter extends FragmentPagerAdapter {
        private static final int PAGE_COUNT = 2;
        private String[] titles;
        //        ArrayList<BaseFragment> mFragments = Arrays.asList(LiveTodayFragment.newInstance(), LiveHistoryFragment.newInstance());
        ArrayList<BaseFragment> mFragments = new ArrayList<>();

        public LiveChildFragmentAdapter(FragmentManager fm) {
            super(fm);
            titles = getResources().getStringArray(R.array.live_home_tab_titles);
            mFragments.add(LiveTodayFragment.newInstance(LiveFragment.this));
            mFragments.add(LiveHistoryFragment.newInstance());
//            TeacherFragment tf = new TeacherFragment();
//            tf.setLiveChild(true);
//            mFragments.add(tf);
       /*     mFragments.add(LiveTeacherTeamFragment.newInstance());*/
        }

        @Override public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override public int getCount() {
            return PAGE_COUNT;
        }

        @Override public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
