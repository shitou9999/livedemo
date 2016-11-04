package tv.kuainiu.ui.publishing.pick;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.friends.fragment.TabMajorFragment;
import tv.kuainiu.ui.publishing.pick.fragment.PickArticleFragment;
import tv.kuainiu.ui.publishing.pick.fragment.PickVideoFragment;
import tv.kuainiu.widget.TitleBarView;

/**
 * 选择引用文章
 * Created by sirius on 2016/10/13.
 */

public class PickArticleActivity extends BaseActivity {
    public static final String NEWS_ITEM = "NEWSITEM";
    public static final String SELECTED_INDEX = "selectedIndex";
    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    List<TeacherZoneDynamicsInfo> listArticleList = new ArrayList<>();
    PickArticleAdapter mPickArticleAdapter;
    @BindView(R.id.tab_fragment_major)
    TabLayout tabFragmentMajor;
    @BindView(R.id.nvp_fragment_major)
    ViewPager nvpFragmentMajor;
    private List<BaseFragment> mBaseFragments = new ArrayList<>();
    private static final String[] titles = {"博文观点", "视频解盘"};
    private int selectedIndex = 0;

    public static void intoNewActivity(BaseActivity context, int requestCode, int selectedIndex) {
        Intent intent = new Intent(context, PickArticleActivity.class);
        intent.putExtra(SELECTED_INDEX, selectedIndex);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_article);
        ButterKnife.bind(this);
        selectedIndex = getIntent().getIntExtra(SELECTED_INDEX, 0);
        initFragment();
        nvpFragmentMajor.setAdapter(new TabMajorFragment.SimpleViewPager(getSupportFragmentManager(), mBaseFragments, titles));
        tabFragmentMajor.setupWithViewPager(nvpFragmentMajor);
        tabFragmentMajor.setTabTextColors(Color.parseColor("#757575"), Color.parseColor(Theme.getCommonColor()));
        tabFragmentMajor.setSelectedTabIndicatorColor(Color.parseColor(Theme.getCommonColor()));
        nvpFragmentMajor.setCurrentItem(selectedIndex, false);
    }


    private void initFragment() {
        mBaseFragments.clear();
        mBaseFragments.add(PickArticleFragment.newInstance("", ""));
        mBaseFragments.add(PickVideoFragment.newInstance("", ""));
    }
}
