package tv.kuainiu.ui.friends.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.ui.fragment.BaseFragment;

/**
 * @author nanck on 2016/7/29.
 */
public class TabMajorFragment extends BaseFragment {
    private static final String ARG_POSITION = "ARG_POSITION";
    private static final String[][] titles = {
            {"快讯", "牛人"},
            {"直播", "点播"}};

    @BindView(R.id.tab_fragment_major)
    TabLayout mTabFragmentMajor;
    @BindView(R.id.nvp_fragment_major)
    ViewPager mNvpFragmentMajor;
    @BindView(R.id.sw_fragment_major)
    SwitchCompat mSwitchCompat;

    private int mParentPosition;
    private List<BaseFragment> mBaseFragments = new ArrayList<>();

    public static TabMajorFragment newInstance(int parentPosition) {
        TabMajorFragment fragment = new TabMajorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, parentPosition);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParentPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_tab_major, container, false);

            ButterKnife.bind(this, view);
            initFragment();


            mNvpFragmentMajor.setAdapter(new SimpleViewPager(getChildFragmentManager(), mBaseFragments, titles[mParentPosition]));
//        mNvpFragmentMajor.setOffscreenPageLimit(mBaseFragments.size() - 1);

            mTabFragmentMajor.setupWithViewPager(mNvpFragmentMajor);
            mTabFragmentMajor.setTabTextColors(Color.parseColor("#757575"), Color.parseColor(Theme.getCommonColor()));
            mTabFragmentMajor.setSelectedTabIndicatorColor(Color.parseColor(Theme.getCommonColor()));
            mTabFragmentMajor.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    mNvpFragmentMajor.setCurrentItem(position);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

//        int visibility = mParentPosition == 0 ? View.VISIBLE : View.GONE;
//        mSwitchCompat.setVisibility(visibility);
//        mSwitchCompat.setTextOn("热");
//        mSwitchCompat.setTextOff("");
//
//        mSwitchCompat.setTextColor(Color.RED);
        } else {
            ViewGroup viewgroup = (ViewGroup) view.getParent();
            if (viewgroup != null) {
                viewgroup.removeView(view);
            }
        }
        return view;
    }


    private void initFragment() {
        mBaseFragments.clear();
        switch (mParentPosition) {
            case 0:
                mBaseFragments.add(CustomViewPointFragment.newInstance(false, ""));
                mBaseFragments.add(CustomTeacherFragment.newInstance());
                break;

            case 1:
                mBaseFragments.add(CustomLiveFragment.newInstance(false, ""));
                mBaseFragments.add(CustomVideoFragment.newInstance());
                break;

        }
//
//        mBaseFragments.add(TabTempFragment.newInstance(Color.GRAY, pstr + " : 水的"));
//        mBaseFragments.add(TabTempFragment.newInstance(Color.MAGENTA, pstr + " : N次方"));
    }

    public static class SimpleViewPager extends FragmentPagerAdapter {
        private List<BaseFragment> mFragments;
        private String[] mTitles;

        public SimpleViewPager(FragmentManager fm, List<BaseFragment> fragments, String[] titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }


        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
