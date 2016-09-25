package tv.kuainiu.ui.comments.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tv.kuainiu.ui.comments.fragmet.PostCommentListFragment;

/**
 * @author nanck on 2016/6/17.
 */
public class SimpleCommentFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"全部评论", "老师回复"};
    private Context context;
    private int mode;

    public SimpleCommentFragmentPagerAdapter(FragmentManager fm, Context context,int type) {
        super(fm);
        this.context = context;
        mode = type;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PostCommentListFragment.newInstance(position,mode);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
