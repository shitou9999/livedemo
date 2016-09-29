package tv.kuainiu.ui.live;

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
import tv.kuainiu.R;
import tv.kuainiu.ui.MainActivity;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.live.fragment.LivePreviewFragment;
import tv.kuainiu.ui.live.fragment.ReadingTapeFragment;
import tv.kuainiu.widget.HeaderTabView;
import tv.kuainiu.widget.MyHeader;

/**
 * 直播模块：
 * 1.直播看盘（ReadingTapeFragment）；
 * 2.直播预告（ReadingTapeFragment）；
 */
public class LiveMainFragment extends BaseFragment {
    private static final String TAG = "FriendsMainFragment";
    @BindView(R.id.rl_fragment_live_main_top_actionBar) HeaderTabView mHeaderTabView;
    @BindView(R.id.vp_fragment_live_main) ViewPager mViewPager;

    private List<BaseFragment> mBaseFragments = new ArrayList<>();
    List<MyHeader> list = new ArrayList<>();

    public static LiveMainFragment newInstance() {
        Bundle args = new Bundle();
        LiveMainFragment fragment = new LiveMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_live_main, container, false);
            ButterKnife.bind(this, view);
            initView();
            initData();
            initListener();
        }
        ViewGroup viewgroup = (ViewGroup) view.getParent();
        if (viewgroup != null) {
            viewgroup.removeView(view);
        }

        return view;
    }

    @Override public void onStart() {
        super.onStart();
    }

    private void initView() {

        list.clear();
        MyHeader header = new MyHeader("直播看盘", "#ff0000");
        list.add(header);
        header = new MyHeader("直播预告", "#ff0000");
        list.add(header);
        mHeaderTabView.setData(list);
    }

    private void initData() {
        mBaseFragments.clear();
        mBaseFragments.add(ReadingTapeFragment.newInstance(0));//直播看盘
        mBaseFragments.add(LivePreviewFragment.newInstance(1));//直播预告

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
