package com.iguxuan.iguxuan_friends;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iguxuan.iguxuan_friends.app.Theme;
import com.iguxuan.iguxuan_friends.friends.fragment.FriendsMainFragment;
import com.iguxuan.iguxuan_friends.live.LiveMainFragment;
import com.iguxuan.iguxuan_friends.ui.activity.BaseActivity;
import com.iguxuan.iguxuan_friends.ui.fragment.BaseFragment;
import com.iguxuan.iguxuan_friends.me.MeFragment;
import com.iguxuan.iguxuan_friends.ui.fragment.TabTempFragment;
import com.iguxuan.iguxuan_friends.widget.NoSlideViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.vp_main) NoSlideViewPager mVpMain;
    @BindView(R.id.tv_main_live) TextView mMainLive;
    @BindView(R.id.tv_main_friends) TextView mMainFriends;
    @BindView(R.id.tv_main_teacher) TextView mMainTeacher;
    @BindView(R.id.tv_main_me) TextView mMainMe;

    @BindView(R.id.ll_main_live) LinearLayout ll_main_live;
    @BindView(R.id.ll_main_friends) LinearLayout ll_main_friends;
    @BindView(R.id.ll_main_teacher) LinearLayout ll_main_teacher;
    @BindView(R.id.ll_main_me) LinearLayout ll_main_me;

    private List<BaseFragment> mBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();

    }

    private void initView() {
        mMainLive.setTextColor(Theme.getColorSelectedStateList());
        mMainFriends.setTextColor(Theme.getColorSelectedStateList());
        mMainTeacher.setTextColor(Theme.getColorSelectedStateList());
        mMainMe.setTextColor(Theme.getColorSelectedStateList());
        ll_main_live.setSelected(true);
    }



    private void initData() {
        mBaseFragments.clear();
        mBaseFragments.add(LiveMainFragment.newInstance());
        mBaseFragments.add(FriendsMainFragment.newInstance());
        mBaseFragments.add(TabTempFragment.newInstance(Color.GRAY, "名师"));
        mBaseFragments.add(MeFragment.newInstance());

        mVpMain.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(), mBaseFragments));
//        mVpMain.setOffscreenPageLimit(mBaseFragments.size() - 1);
        switchFragment(0);
    }

    private void switchFragment(int position) {
        ll_main_live.setSelected(position == 0);
        ll_main_friends.setSelected(position == 1);
        ll_main_teacher.setSelected(position == 2);
        ll_main_me.setSelected(position == 3);
        mVpMain.setCurrentItem(position, false);
    }
    @OnClick({R.id.ll_main_live, R.id.ll_main_friends, R.id.ll_main_teacher,R.id.ll_main_me})
    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_main_live:
                switchFragment(0);
                break;

            case R.id.ll_main_friends:
                switchFragment(1);
                break;

            case R.id.ll_main_teacher:
                switchFragment(2);
                break;

            case R.id.ll_main_me:
                switchFragment(3);
                break;
        }
    }


    // ==================================================================================
    // nil  ||
    // ==================================================================================
    public static class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> mFragments = new ArrayList<>();

        public TabFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }
    }

}
