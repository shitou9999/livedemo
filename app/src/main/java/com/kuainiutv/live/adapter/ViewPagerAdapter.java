package com.kuainiutv.live.adapter;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 2016/9/9.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> listView = new ArrayList<>();
//    private Context mContext;

    public ViewPagerAdapter(List<View> listView) {
        this.listView = listView;
    }

//    public ViewPagerAdapter(List<View> listView, Context context) {
//        this.listView = listView;
//        mContext = context;
//    }

    @Override public int getCount() {
        return listView.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(listView.get(position));
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        View view = listView.get(position);
        container.addView(view);
        return view;
    }
}
