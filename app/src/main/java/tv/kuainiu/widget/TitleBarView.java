package tv.kuainiu.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.util.LogUtils;

/**
 * @author nanck on 2016/8/2.
 */
public class TitleBarView extends RelativeLayout {
    @BindView(R.id.tv_title) TextView mTvTitle;
    @BindView(R.id.ivLeft) ImageView ivLeft;
    @BindView(R.id.ivRight) ImageView ivRight;
    @BindView(R.id.rl_title_bar_left) RelativeLayout mRlTitleBarLeft;
    @BindView(R.id.rl_title_bar_right) RelativeLayout mRlTitleBarRight;
    private Context mContext;
    private View view;
    private final static String TAG = "TitleBarView";
    private OnClickListening mICheckListen;

    private int leftVisibility = 1;
    private int rightVisibility = 1;
    private Drawable leftSrc = null;
    private Drawable rightSrc = null;
    private String titleText = "";

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.include_header2, this);
        ButterKnife.bind(view);
        setBackgroundColor(Color.parseColor(Theme.getCommonColor()));
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.title_bar_view,
                defStyleAttr, 0);
        titleText=attributes.getString(R.styleable.title_bar_view_title);
        leftVisibility = attributes.getInt(R.styleable.title_bar_view_left_visibility, View.VISIBLE);
        rightVisibility = attributes.getInt(R.styleable.title_bar_view_right_visibility, View.VISIBLE);
        leftSrc = attributes.getDrawable(R.styleable.title_bar_view_leftSrc);
        rightSrc = attributes.getDrawable(R.styleable.title_bar_view_rightSrc);
        attributes.recycle();
        mRlTitleBarLeft.setVisibility(leftVisibility == View.INVISIBLE ? View.INVISIBLE : View.VISIBLE);
        mRlTitleBarRight.setVisibility(rightVisibility == View.INVISIBLE ? View.INVISIBLE : View.VISIBLE);
        mTvTitle.setText(titleText);
        if (rightSrc != null) {
            ivRight.setImageDrawable(rightSrc);
        }
        if (leftSrc != null) {
            ivLeft.setImageDrawable(leftSrc);
        }
        mTvTitle.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                if (mICheckListen != null) {
                    mICheckListen.titleClick();
                }
            }
        });
        mRlTitleBarLeft.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                if (mICheckListen != null) {
                    mICheckListen.leftClick();
                }else{
                    try {
                        ((Activity) mContext).finish();
                    }catch (Exception e){
                        LogUtils.e("TitleBarView","关闭Activity失败",e);
                    }
                }
            }
        });
        mRlTitleBarRight.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                if (mICheckListen != null) {
                    mICheckListen.rightClick();
                }
            }
        });
    }

    public void setOnClickListening(OnClickListening c) {
        mICheckListen = c;
    }

    public void setText(CharSequence text) {
        mTvTitle.setText(text);
    }

    public TextView getTitle() {
        return mTvTitle;
    }

    public ImageView getLeftImage() {
        return ivLeft;
    }

    public ImageView getRightImage() {
        return ivRight;
    }

    public interface OnClickListening {
        void leftClick();

        void rightClick();

        void titleClick();
    }
}
