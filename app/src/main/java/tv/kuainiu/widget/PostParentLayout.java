package tv.kuainiu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import tv.kuainiu.R;
import tv.kuainiu.app.Constans;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.ui.friends.model.BasePost;

/**
 * @author nanck on 2016/8/2.
 */
public class PostParentLayout extends RelativeLayout {
    private Context mContext;
    private int mPostType = 0;
    private BasePost mBasePost;
    private TeacherZoneDynamicsInfo  teacherZoneDynamicsInfo;
    public PostParentLayout(Context context) {
        super(context);
        init(context);
    }

    public PostParentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public PostParentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setPostType(TeacherZoneDynamicsInfo teacherZoneDynamicsInfo) {
        this.teacherZoneDynamicsInfo=teacherZoneDynamicsInfo;
        if (this.teacherZoneDynamicsInfo != null) {
            mPostType =Integer.parseInt(this.teacherZoneDynamicsInfo.getType());
            loadPostView();
        } else {
            setVisibility(View.GONE);
        }
    }

    private void init(Context context) {
        mContext = context;

    }

    private void loadPostView() {
        View view;
        switch (mPostType) {
            case Constans.TYPE_POST:
                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_normal, this, false);
                if (getChildAt(0) != null) {
                    removeAllViews();
                }
                addView(view);
                break;

//            case Constans.TYPE_LIVING:
//                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_living, this, false);
//                if (getChildAt(0) != null) {
//                    removeAllViews();
//                }
//                addView(view);
//                break;
//
//            case Constans.TYPE_LIVE:
//                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_live, this, false);
//                if (getChildAt(0) != null) {
//                    removeAllViews();
//                }
//                addView(view);
//                break;

            case Constans.TYPE_VIDEO:
                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_video, this, false);
                if (getChildAt(0) != null) {
                    removeAllViews();
                }
                addView(view);
                break;

            case Constans.TYPE_AUDIO:
                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_audio, this, false);
                if (getChildAt(0) != null) {
                    removeAllViews();
                }
                addView(view);
                break;
// XXX
            default:
//                removeAllViews();
//                this.setVisibility(GONE);
                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_normal, this, false);
                if (getChildAt(0) != null) {
                    removeAllViews();
                }
                addView(view);
                break;
        }
    }
}
