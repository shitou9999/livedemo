package tv.kuainiu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import tv.kuainiu.R;

/**
 * 弹幕布局类
 *
 * @author liufh
 */
public class PublishPickTimeLayout extends RelativeLayout {


    private Context context;
    private View view;

    public PublishPickTimeLayout(Context context) {
        super(context);
        init(context);
    }

    public PublishPickTimeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PublishPickTimeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.picktime, this);
        ButterKnife.bind(this, view);
    }

    /*@OnClick({R.id.ll_publish_dynamic, R.id.ll_publish_voice, R.id.ll_publish_article, R.id.ll_publish_video, R.id.ivClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                BottomSheetBehavior behavior = BottomSheetBehavior.from(this);
                if (behavior != null) {
                    if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
                break;
        }
    }*/
}
