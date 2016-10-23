package tv.kuainiu.widget;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.kuainiu.R;

/**
 * 弹幕布局类
 *
 * @author liufh
 */
public class PublishPickTimeLayout extends RelativeLayout {


    @BindView(R.id._numberPicker_Year)
    NumberPicker NumberPickerYear;
    @BindView(R.id.numberPicker_Month)
    NumberPicker numberPickerMonth;
    @BindView(R.id.numberPicker_Day)
    NumberPicker numberPickerDay;
    @BindView(R.id.numberPicker_Hour)
    NumberPicker numberPickerHour;
    @BindView(R.id.numberPicker_Min)
    NumberPicker numberPickerMin;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    private Context context;
    private View view;
    int year = 0;
    int month = 0;
    int day = 0;
    int hour = 0;
    int minute = 0;
    boolean isRunnian = false;
    private TextView textView;

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

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    private void init(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.picktime, this);
        ButterKnife.bind(this, view);
        initData();
    }

    private void initData() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        isRunnian();
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        NumberPicker.Formatter mFormatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%d", value);
            }
        };
        NumberPicker.Formatter mFormatter2 = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        };
        NumberPickerYear.setMinValue(year);
        NumberPickerYear.setFormatter(mFormatter);
        NumberPickerYear.setMaxValue(year + 9);
        NumberPickerYear.setValue(year);
        NumberPickerYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                year = newVal;
                isRunnian();
                setDayMax();
            }
        });

        numberPickerMonth.setMinValue(1);
        numberPickerMonth.setFormatter(mFormatter2);
        numberPickerMonth.setMaxValue(12);
        numberPickerMonth.setValue(month);
        numberPickerMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                month = newVal;
                setDayMax();
            }
        });

        numberPickerDay.setMinValue(1);
        numberPickerDay.setFormatter(mFormatter2);
        setDayMax();

        numberPickerDay.setValue(day);
        numberPickerDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                day = newVal;
            }
        });

        numberPickerHour.setMinValue(0);
        numberPickerHour.setFormatter(mFormatter2);
        numberPickerHour.setMaxValue(23);
        numberPickerHour.setValue(hour);
        numberPickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hour = newVal;
            }
        });

        numberPickerMin.setMinValue(0);
        numberPickerMin.setFormatter(mFormatter2);
        numberPickerMin.setMaxValue(59);
        numberPickerMin.setValue(minute);
        numberPickerMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minute = newVal;
            }
        });
    }

    private void setDayMax() {
        if (isRunnian && month == 2) {
            numberPickerDay.setMaxValue(29);
        } else if (month == 2) {
            numberPickerDay.setMaxValue(28);
        } else {
            numberPickerDay.setMaxValue(31);
        }
    }

    private void isRunnian() {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            isRunnian = true;
        } else {
            isRunnian = false;
        }
    }

    @OnClick({R.id.ivClose, R.id.tvSure})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSure:
                if(textView==null){
                    return;
                }
                textView.setText(String.format("%d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, 0));
                textView.setTag(String.format("%d-%02d-%02d", year, month, day));
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

    }
}
