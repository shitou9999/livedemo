package tv.kuainiu.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.MainActivity;
import tv.kuainiu.R;
import tv.kuainiu.live.fragment.ConsultationFragment;
import tv.kuainiu.live.fragment.OpenClassFragment;
import tv.kuainiu.live.fragment.ReadingTapeFragment;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.widget.HeaderTabView;
import tv.kuainiu.widget.MyHeader;

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
