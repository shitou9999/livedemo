package com.kuainiutv.friends.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kuainiutv.R;
import com.kuainiutv.ui.fragment.BaseFragment;
import com.kuainiutv.ui.fragment.TabMajorFragment;
import com.kuainiutv.widget.HeaderTabView;
import com.kuainiutv.widget.MyHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nanck on 2016/7/29.
 */
public class FriendsMainFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "FriendsMainFragment";
    @BindView(R.id.rl_fragment_live_main_top_actionBar) HeaderTabView mHeaderTabView;
    @BindView(R.id.ll_fragment_friends_main_news_info) LinearLayout mLlFragmentFriendsMainNewsInfo;
    @BindView(R.id.vp_fragment_friends_main) ViewPager mViewPager;

    public static FriendsMainFragment newInstance() {
        Bundle args = new Bundle();
        FriendsMainFragment fragment = new FriendsMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_main, container, false);
        ButterKnife.bind(this, view);
        List<MyHeader> list = new ArrayList<>();
        MyHeader header = new MyHeader("名家观点", "#ff0000");
        list.add(header);
        header = new MyHeader("解盘视频", "#ff0000");
        list.add(header);
        header = new MyHeader("订阅主题", "#ff0000");
        list.add(header);
        mHeaderTabView.setData(list);
        mHeaderTabView.setCheckListen(new HeaderTabView.ICheckListen() {
            @Override public void checked(int index) {
                mViewPager.setCurrentItem(index);
            }
        });

        SimpleTabFragmentPageAdapter adapter = new SimpleTabFragmentPageAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(5);

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

        return view;
    }


    @Override public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }

    // ==================================================================================
    // nil  ||
    // ==================================================================================
    class SimpleTabFragmentPageAdapter extends FragmentPagerAdapter {
        private static final int PAGE_COUNT = 3;

        public SimpleTabFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            return TabMajorFragment.newInstance(position);
        }

        @Override public int getCount() {
            return PAGE_COUNT;
        }
    }

}
