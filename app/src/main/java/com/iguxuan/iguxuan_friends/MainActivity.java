package com.iguxuan.iguxuan_friends;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.iguxuan.iguxuan_friends.friends.fragment.FriendsMainFragment;
import com.iguxuan.iguxuan_friends.ui.fragment.BaseFragment;
import com.iguxuan.iguxuan_friends.ui.fragment.MeFragment;
import com.iguxuan.iguxuan_friends.ui.fragment.TabTempFragment;
import com.iguxuan.iguxuan_friends.widget.NoSlideViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    @BindView(R.id.vp_main) NoSlideViewPager mVpMain;
    @BindView(R.id.tv_main_live) TextView mMainLive;
    @BindView(R.id.tv_main_friends) TextView mMainFriends;
    @BindView(R.id.tv_main_teacher) TextView mMainTeacher;
    @BindView(R.id.tv_main_me) TextView mMainMe;

    private List<BaseFragment> mBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        initFragment();
        switchFragment(0);
        mMainLive.setOnClickListener(this);
        mMainFriends.setOnClickListener(this);
        mMainTeacher.setOnClickListener(this);
        mMainMe.setOnClickListener(this);
    }


    private void initFragment() {
        mBaseFragments.clear();
        mBaseFragments.add(TabTempFragment.newInstance(Color.DKGRAY, "直播"));
        mBaseFragments.add(FriendsMainFragment.newInstance());
        mBaseFragments.add(TabTempFragment.newInstance(Color.GRAY, "名师"));
        mBaseFragments.add(MeFragment.newInstance());

        mVpMain.setAdapter(new MainTabFragmentPagerAdapter(getSupportFragmentManager(), mBaseFragments));
        mVpMain.setOffscreenPageLimit(mBaseFragments.size() - 1);
    }

    private void switchFragment(int position) {
        mVpMain.setCurrentItem(position, false);
    }

    @Override public void onClick(View view) {
        mMainLive.setSelected(false);
        mMainFriends.setSelected(false);
        mMainTeacher.setSelected(false);
        mMainMe.setSelected(false);
        switch (view.getId()) {
            case R.id.tv_main_live:
                mMainLive.setSelected(true);
                switchFragment(0);
                break;

            case R.id.tv_main_friends:
                mMainFriends.setSelected(true);
                switchFragment(1);
                break;

            case R.id.tv_main_teacher:
                mMainTeacher.setSelected(true);
                switchFragment(2);
                break;

            case R.id.tv_main_me:
                mMainMe.setSelected(true);
                switchFragment(3);
                break;
        }
    }


    // ==================================================================================
    // nil  ||
    // ==================================================================================
    private static class MainTabFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> mFragments = new ArrayList<>();

        public MainTabFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
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
