package com.iguxuan.iguxuan_friends.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iguxuan.iguxuan_friends.R;
import com.iguxuan.iguxuan_friends.app.Theme;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nanck on 2016/8/2.
 */
public class TitleBarView extends RelativeLayout {
    @BindView(R.id.tv_title) TextView mTvTitle;
    @BindView(R.id.iv_right_title) ImageView mIvRightTitle;
    @BindView(R.id.title_toolbar_right_button) ImageView mTitleToolbarRightButton;
    @BindView(R.id.rl_title_bar_left) RelativeLayout mRlTitleBarLeft;
    @BindView(R.id.rl_title_bar_right) RelativeLayout mRlTitleBarRight;
    private Context mContext;
    private View view;
    private final static String TAG = "TitleBarView";
    private OnClickListening mICheckListen;

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
        return mTitleToolbarRightButton;
    }

    public ImageView getRightImage() {
        return mIvRightTitle;
    }

    public interface OnClickListening {
        void leftClick();

        void rightClick();

        void titleClick();
    }
}
