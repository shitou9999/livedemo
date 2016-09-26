package tv.kuainiu.ui.comments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.comments.adapter.SimpleCommentFragmentPagerAdapter;
import tv.kuainiu.ui.comments.fragmet.PostCommentListFragment;

/**
 * 评论列表
 */
public class CommentListActivity extends BaseActivity {
    @BindView(R.id.tab_comment_tag)
    TabLayout mTabLayout;
    @BindView(R.id.vp_post_zone_comment)
    ViewPager mViewPager;
    public static final String TYPE = "CommentListActivity_TYPE";
    private int type = PostCommentListFragment.MODE_ARTICLE;

    public static void intoNewIntent(Context context, int type,String mPostId,String mCatId) {
        Intent intent = new Intent(context, CommentListActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(Constant.KEY_ID,mPostId);
        intent.putExtra(Constant.KEY_CATID,mCatId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra(TYPE, PostCommentListFragment.MODE_ARTICLE);
        initMyListener();
        mViewPager.setAdapter(new SimpleCommentFragmentPagerAdapter(getSupportFragmentManager(), this, type));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(Color.parseColor("#757575"), Color.parseColor(Theme.getCommonColor()));
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor(Theme.getCommonColor()));
    }


    private void initMyListener() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
