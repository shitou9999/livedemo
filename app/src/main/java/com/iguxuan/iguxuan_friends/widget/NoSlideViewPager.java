package com.iguxuan.iguxuan_friends.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author nanck on 2016/7/29.
 */
public class NoSlideViewPager extends ViewPager {
    public NoSlideViewPager(Context context) {
        super(context);
    }

    public NoSlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }


    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
