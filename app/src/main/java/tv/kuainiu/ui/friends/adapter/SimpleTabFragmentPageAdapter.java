package tv.kuainiu.ui.friends.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tv.kuainiu.ui.fragment.TabMajorFragment;

/**
 * Created by jack on 2016/9/20.
 */
public class SimpleTabFragmentPageAdapter extends FragmentPagerAdapter {
    private int PAGE_COUNT = 2;

    public SimpleTabFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public SimpleTabFragmentPageAdapter(FragmentManager fm, int PAGE_COUNT) {
        super(fm);
        this.PAGE_COUNT = PAGE_COUNT;
    }

    @Override public Fragment getItem(int position) {
        return TabMajorFragment.newInstance(position);
    }

    @Override public int getCount() {
        return PAGE_COUNT;
    }
}
