package tv.kuainiu.ui.me.appointment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.friends.fragment.TabMajorFragment;
import tv.kuainiu.ui.me.appointment.fragment.MyLiveFragment;
import tv.kuainiu.ui.me.appointment.fragment.MyLiveHistoryFragment;
import tv.kuainiu.widget.TitleBarView;

public class MyLiveActivity extends BaseActivity {
    private static final String[] titles = {"直播计划", "历史回放"};

    TabLayout mTabFragmentMajor;
    ViewPager mNvpFragmentMajor;
    TitleBarView tbvTitle;

    private List<BaseFragment> mBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        ButterKnife.bind(this);
        tbvTitle= (TitleBarView) findViewById(R.id.tbv_title);
        tbvTitle.setText("我的直播");
        mTabFragmentMajor= (TabLayout) findViewById(R.id.tab_fragment_major);
        mNvpFragmentMajor= (ViewPager) findViewById(R.id.nvp_fragment_major);
        initFragment();

        mNvpFragmentMajor.setAdapter(new TabMajorFragment.SimpleViewPager(getSupportFragmentManager(), mBaseFragments, titles));
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
    }

    private void initFragment() {
        mBaseFragments.clear();
        mBaseFragments.add(MyLiveFragment.newInstance(0));
        mBaseFragments.add(MyLiveHistoryFragment.newInstance());
    }
}
