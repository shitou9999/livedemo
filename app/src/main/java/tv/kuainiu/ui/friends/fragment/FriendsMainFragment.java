package tv.kuainiu.ui.friends.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.friends.adapter.SimpleTabFragmentPageAdapter;
import tv.kuainiu.widget.HeaderTabView;
import tv.kuainiu.widget.MyHeader;

/**
 * @author nanck on 2016/7/29.
 */
public class FriendsMainFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "FriendsMainFragment";
    @BindView(R.id.rl_fragment_live_main_top_actionBar)
    HeaderTabView mHeaderTabView;
    @BindView(R.id.ll_fragment_friends_main_news_info)
    LinearLayout mLlFragmentFriendsMainNewsInfo;
    @BindView(R.id.vp_fragment_friends_main)
    ViewPager mViewPager;
    private List<MyHeader> list = new ArrayList<>();

    public static FriendsMainFragment newInstance() {
        Bundle args = new Bundle();
        FriendsMainFragment fragment = new FriendsMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_friends_main, container, false);
            ButterKnife.bind(this, view);

            list.clear();
            MyHeader header = new MyHeader("牛人快讯", "#ff0000");
            list.add(header);
            header = new MyHeader("解盘视频", "#ff0000");
            list.add(header);
//        header = new MyHeader("订阅主题", "#ff0000");
//        list.add(header);
            mHeaderTabView.setData(list);
            mHeaderTabView.setCheckListen(new HeaderTabView.ICheckListen() {
                @Override
                public void checked(int index) {
                    mViewPager.setCurrentItem(index);
                }
            });

            SimpleTabFragmentPageAdapter adapter = new SimpleTabFragmentPageAdapter(getChildFragmentManager());
            mViewPager.setAdapter(adapter);
            mViewPager.setOffscreenPageLimit(2);

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    mHeaderTabView.setChecked(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            ViewGroup viewgroup = (ViewGroup) view.getParent();
            if (viewgroup != null) {
                viewgroup.removeView(view);
            }
        }
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }


}
