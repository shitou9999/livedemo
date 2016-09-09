package com.iguxuan.iguxuan_friends.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iguxuan.iguxuan_friends.MainActivity;
import com.iguxuan.iguxuan_friends.R;
import com.iguxuan.iguxuan_friends.live.fragment.ConsultationFragment;
import com.iguxuan.iguxuan_friends.live.fragment.OpenClassFragment;
import com.iguxuan.iguxuan_friends.live.fragment.ReadingTapeFragment;
import com.iguxuan.iguxuan_friends.ui.fragment.BaseFragment;
import com.iguxuan.iguxuan_friends.widget.HeaderTabView;
import com.iguxuan.iguxuan_friends.widget.MyHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 直播模块：
 * 1.今日看盘（ReadingTapeFragment）；
 * 2.公开课（OpenClassFragment）；
 * 3.咨询（ConsultationFragment）
 */
public class LiveMainFragment extends BaseFragment {
    private static final String TAG = "FriendsMainFragment";
    @BindView(R.id.rl_fragment_live_main_top_actionBar) HeaderTabView mHeaderTabView;
    @BindView(R.id.vp_fragment_live_main) ViewPager mViewPager;

    private List<BaseFragment> mBaseFragments = new ArrayList<>();

    public static LiveMainFragment newInstance() {
        Bundle args = new Bundle();
        LiveMainFragment fragment = new LiveMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_main, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initView() {

        List<MyHeader> list = new ArrayList<>();
        MyHeader header = new MyHeader("直播看盘", "#ff0000");
        list.add(header);
        header = new MyHeader("  公开课  ", "#ff0000");
        list.add(header);
        header = new MyHeader("  咨询  ", "#ff0000");
        list.add(header);
        mHeaderTabView.setData(list);
    }

    private void initData() {
        mBaseFragments.clear();
        mBaseFragments.add(ReadingTapeFragment.newInstance(0));//今日看盘
        mBaseFragments.add(OpenClassFragment.newInstance(0));//公开课
        mBaseFragments.add(ConsultationFragment.newInstance(0));//咨询

        mViewPager.setAdapter(new MainActivity.TabFragmentPagerAdapter(getChildFragmentManager(), mBaseFragments));
//        mViewPager.setOffscreenPageLimit(mBaseFragments.size() - 1);
        switchFragment(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled position : " + position);
            }

            @Override public void onPageSelected(int position) {
                mHeaderTabView.setChecked(position);
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initListener() {
        mHeaderTabView.setCheckListen(new HeaderTabView.ICheckListen() {
            @Override public void checked(int index) {
                switchFragment(index);
            }
        });
    }

    private void switchFragment(int position) {
        mViewPager.setCurrentItem(position, false);
    }
}
