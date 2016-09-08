package com.iguxuan.iguxuan_friends.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iguxuan.iguxuan_friends.R;
import com.iguxuan.iguxuan_friends.modle.cons.Constant;
import com.iguxuan.iguxuan_friends.util.ImageDisplayUtil;
import com.iguxuan.iguxuan_friends.util.LogUtils;
import com.iguxuan.iguxuan_friends.util.NetUtils;
import com.iguxuan.iguxuan_friends.util.StringUtils;
import com.iguxuan.iguxuan_friends.util.ToastUtils;


/**
 * 加载数据时显示的布局
 *
 * @author nanck on 2016/4/12.
 */
public class NetErrAddLoadView extends FrameLayout {
    private TextView mTextPrompt;
    private ProgressBar mProgressBar;
    private ImageView iv_tip_image;
    private OnTryCallBack mOnTryCallBack;
    private Context context;

    public void setOnTryCallBack(OnTryCallBack onTryCallBack) {
        mOnTryCallBack = onTryCallBack;
    }

    private void hide() {
        setVisibility(GONE);
    }

    private void show() {
        setVisibility(VISIBLE);
    }

    public void StartLoading() {
        StartLoading("");
    }

    public void StartLoading(String tip) {
        show();
        mProgressBar.setVisibility(View.VISIBLE);
        iv_tip_image.setVisibility(View.GONE);
        mTextPrompt.setText(StringUtils.replaceNullToEmpty(tip));
    }


    public void StopLoading(int type, String msg) {
        mProgressBar.setVisibility(View.GONE);
        switch (type) {
            case Constant.SUCCEED:
                hide();
                break;
            case Constant.NULL_FOR_DATA:
            case Constant.NULL_FOR_LIST:
                ImageDisplayUtil.displayImage(context, iv_tip_image, R.mipmap.digit);
                iv_tip_image.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mTextPrompt.setText("未获取到数据，请点击屏幕重试");
                break;
            default:
                if (!NetUtils.isOnline(context)) {//如果没有网络，提示网络错误，检查网络
                    mTextPrompt.setText(StringUtils.replaceNullToEmpty("亲，请检查网络"));
                    iv_tip_image.setVisibility(View.VISIBLE);
                    ImageDisplayUtil.displayImage(context, iv_tip_image, R.mipmap.net);
                } else {
                    ImageDisplayUtil.displayImage(context, iv_tip_image, R.mipmap.digit);
                    iv_tip_image.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    if (msg != null && msg.length() > 0) {
                        mTextPrompt.setText(msg+"，请点击屏幕重试");
                    } else {
                        mTextPrompt.setText("获取数据失败，请点击屏幕重试");
                    }
                }
                break;
        }


    }

    private void init(final Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_load_neterror, this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mTextPrompt = (TextView) view.findViewById(R.id.tv_error);
        iv_tip_image = (ImageView) view.findViewById(R.id.iv_tip_image);
        StartLoading();
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //摇摆
//                TranslateAnimation alphaAnimation1 = new TranslateAnimation(0f, 300f, 0f, 0f);
//                alphaAnimation1.setInterpolator(new CycleInterpolator(20.0f));
//                alphaAnimation1.setDuration(200);
//                alphaAnimation1.setRepeatCount(4);
//                alphaAnimation1.setRepeatMode(Animation.REVERSE);
//                mTextPrompt.setAnimation(alphaAnimation1);
//                alphaAnimation1.start();
                if (null != mOnTryCallBack) {
                    if (NetUtils.isOnline(context)) {
                        mOnTryCallBack.callAgain();
                        StartLoading();
                    } else {
//                        mOnTryCallBack.checkNetWork();
                        ToastUtils.showToast(context, "请检查网络");
                    }

                } else {
                    LogUtils.e("NetErrAddLoadView.class", "mOnTryCallBack 不能为null");
                }
            }
        });


    }

    public NetErrAddLoadView(Context context) {
        super(context);
        init(context);
    }

    public NetErrAddLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NetErrAddLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public interface OnTryCallBack {
        void callAgain();//重新请求
//        void checkNetWork();//检查网络
    }


}
