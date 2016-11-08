package tv.kuainiu.widget.editview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by sirius on 2016/11/8.
 */

public class EditScrollView extends ScrollView {
    public EditScrollView(Context context) {
        super(context);
    }

    public EditScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
