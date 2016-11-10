package tv.kuainiu.widget.tagview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by sirius on 2016/11/10.
 */

public class ThroughScrollView  extends ScrollView{
    public ThroughScrollView(Context context) {
        super(context);
    }

    public ThroughScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThroughScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ThroughScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
