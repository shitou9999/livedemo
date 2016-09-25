package tv.kuainiu.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import tv.kuainiu.utils.LogUtils;


/**
* @ClassName ViewPagerFixed
* @Description 图片缩放
* 解决图片缩放时java.lang.IllegalArgumentException: pointerIndex out of range异常
* http://blog.csdn.net/nnmmbb/article/details/28419779
* @author 网络
*/ 
public class ViewPagerFixed extends ViewPager {
	public ViewPagerFixed(Context context) {
		this(context,null);
	}

	public ViewPagerFixed(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		try {
			return super.onTouchEvent(ev);
		} catch (IllegalArgumentException ex) {
			LogUtils.printStackTrace(ex);
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException ex) {
			LogUtils.printStackTrace(ex);
		}
		return false;
	}
}
