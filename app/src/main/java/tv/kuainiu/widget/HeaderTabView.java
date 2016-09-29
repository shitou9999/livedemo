package tv.kuainiu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;

/**
 * @author nanck on 2016/8/2.
 */
public class HeaderTabView extends RelativeLayout {
    private Paint mPaint;
    private TextPaint mTextPaint;
    private int mLineColor;
    private Context mContext;
    private View view;
    private ICheckListen mICheckListen;
    RadioGroup mRadioGroup;
    private List<RadioButton> listRadioButton;
    private final static String TAG = "HeaderTabView";

    public HeaderTabView(Context context) {
        this(context, null);
    }

    public HeaderTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.head_tab_view, this);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_parent);
        ButterKnife.bind(view);
        listRadioButton = new ArrayList<>();

    }

    public void setChecked(int index) {
        if (listRadioButton != null && listRadioButton.size() > 0) {
            listRadioButton.get(index).setChecked(true);
        }
    }

    public void setCheckListen(ICheckListen c) {
        mICheckListen = c;
    }

    public void setData(List<MyHeader> list) {
        if (list == null && list.size() < 1) {
            return;
        }
        mRadioGroup.removeAllViews();
        listRadioButton.clear();
        setBackgroundColor(Color.parseColor(Theme.getCommonColor()));
        int textSize = mContext.getResources().getDimensionPixelSize(R.dimen.head_table_text_size);
        for (int i = 0; i < list.size(); i++) {
            MyHeader header = list.get(i);
            RadioButton mRadioButton = new RadioButton(mContext);
            mRadioButton.setId(i);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            layoutParams.gravity = Gravity.CENTER;
            mRadioButton.setLayoutParams(layoutParams);
            mRadioButton.setText(header.getTitle());
            Bitmap a = null;
            mRadioButton.setButtonDrawable(new BitmapDrawable(a));
            mRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            mRadioButton.setTextColor(Theme.getColorCheckedStateList());
            if (i == 0 && i == (list.size() - 1)) {
                mRadioButton.setBackgroundResource(R.drawable.selector_friends_main_tab_all);
                mRadioButton.setEnabled(false);
            } else if (i == 0) {
                mRadioButton.setBackgroundResource(R.drawable.selector_friends_main_tab0);
            } else if (i < (list.size() - 1)) {
                mRadioButton.setBackgroundResource(R.drawable.selector_friends_main_tab1);
            } else {
                mRadioButton.setBackgroundResource(R.drawable.selector_friends_main_tab2);
            }
            listRadioButton.add(mRadioButton);
            mRadioGroup.addView(mRadioButton);
            if (i == 0) {
                mRadioButton.setChecked(true);
            }
        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (mICheckListen == null) {
                    Log.e(TAG, "空指针异常HeadrtTabView 未设置点击事件监听函数");
                } else {
                    mICheckListen.checked(i);
                }
            }
        });

    }

    public interface ICheckListen {
        void checked(int index);
    }
}
