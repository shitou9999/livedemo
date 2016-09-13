package tv.kuainiu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * @author nanck on 2016/6/20.
 */
public class LinkTextView extends TextView {

    public boolean linkHit;//内部链接是否被点击

    public LinkTextView(Context context) {
        super(context);
    }

    public LinkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean performClick() {
        if (linkHit) {
            return true;
        }
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        linkHit = false;
        return super.onTouchEvent(event);
    }
}
