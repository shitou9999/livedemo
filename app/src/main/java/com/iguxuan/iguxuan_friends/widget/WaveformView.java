package com.iguxuan.iguxuan_friends.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author nanck on 2016/8/2.
 */
public class WaveformView extends View {
    private Paint mPaint;
    private TextPaint mTextPaint;
    private int mLineColor;
    private Context mContext;

    public WaveformView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public WaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public WaveformView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    private void init() {
        mPaint = new Paint(); // 打开抗锯齿
        mLineColor = Color.parseColor("#2196F3");
        mPaint.setColor(mLineColor);
        mPaint.setStyle(Paint.Style.FILL);


        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.rgb(43, 43, 43));
        mTextPaint.setTextSize(36f);

    }

    int width = 0;
    int index = 0;
    int height = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float linesPoint1[] = {22f, 22f, 22f, 82f, 14f, 30f, 14f, 74f, 6f, 38f, 6f, 66f};
    private float linesPoint2[] = {40f, 22f, 40f, 82f, 48f, 30f, 48f, 74f, 56f, 38f, 56f, 66f};

    @Override protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
//        canvas.drawLine(22f, 22f, 22f, 60f, mPaint);
//        canvas.drawLines(linesPoint1, mPaint);
//        canvas.drawLines(linesPoint2, mPaint);
//        int X= DensityUtils.dp2px(mContext,)
      /*  index = width / 13;
        for (int j = 0; j < 4; j++) {
            float x = 300f - j * (4 * index);
            float y = 24f + j * index;
            float bY = 136f - j * index;

            for (int i = 0; i < 3; i++) {
                canvas.drawLine(x, y, x, bY, mPaint);
                x = x - index;
                y = y + index;
                bY = bY - index;
            }
        }

        for (int j = 0; j < 4; j++) {
            float x = 424f + j * (4 * index);
            float y = 24f + j * index;
            float bY = 136f - j * index;

            for (int i = 0; i < 3; i++) {
                canvas.drawLine(x, y, x, bY, mPaint);
                x = x + index;
                y = y + index;
                bY = bY - index;
            }
        }*/
        for (int j = 0; j < 4; j++) {
            float x = 300f - j * (4 * 16f);
            float y = 24f + j * 16f;
            float bY = 136f - j * 16f;

            for (int i = 0; i < 3; i++) {
                canvas.drawLine(x, y, x, bY, mPaint);
                x = x - 16f;
                y = y + 16f;
                bY = bY - 16f;
            }
        }

        for (int j = 0; j < 4; j++) {
            float x = 424f + j * (4 * 16f);
            float y = 24f + j * 16f;
            float bY = 136f - j * 16f;

            for (int i = 0; i < 3; i++) {
                canvas.drawLine(x, y, x, bY, mPaint);
                x = x + 16f;
                y = y + 16f;
                bY = bY - 16f;
            }
        }

        canvas.drawText("11:08", 316f, 100f, mTextPaint);
    }
}
