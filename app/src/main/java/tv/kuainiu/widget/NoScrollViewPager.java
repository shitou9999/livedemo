package tv.kuainiu.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
* @ClassName NoScrollViewPager
* @Description 不可滑动的ViewPager
*/
public class NoScrollViewPager extends ViewPager {

	private boolean scrollable = false;
	
	public NoScrollViewPager(Context context) {
		this(context,null);
	}
	
	public NoScrollViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	  }

	public boolean isScrollable() {
		return scrollable;
	}

	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if(!scrollable){
			return false;
		}else{
			return super.onTouchEvent(arg0);			
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if(!scrollable){
			return false;
		}else{
			return super.onInterceptTouchEvent(arg0);
		}
	}
	
	
}
