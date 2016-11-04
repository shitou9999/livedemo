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
    int curYear = 0;
    int month = 0;
    int curMonth = 0;
    int day = 0;
    int curDay = 0;
    int hour = 0;
    int curHour = 0;
    int minute = 0;
    int curMinute = 0;
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
        curYear = year;
        isRunnian();
        month = c.get(Calendar.MONTH) + 1;
        curMonth = month;
        day = c.get(Calendar.DAY_OF_MONTH);
        curDay = day;
        hour = c.get(Calendar.HOUR_OF_DAY);
        curHour = hour;
        minute = c.get(Calendar.MINUTE);
        curMinute = minute;
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
                if (year == curYear) {
                    numberPickerMonth.setMinValue(curMonth);
                } else {
                    numberPickerMonth.setMinValue(1);
                    numberPickerDay.setMinValue(1);
                    numberPickerHour.setMinValue(0);
                    numberPickerMin.setMinValue(0);
                }
                isRunnian();
                setDayMax();
            }
        });

        numberPickerMonth.setMinValue(curMonth);
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

        numberPickerDay.setMinValue(curDay);
        numberPickerDay.setFormatter(mFormatter2);
        setDayMax();

        numberPickerDay.setValue(day);
        numberPickerDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                day = newVal;
                if (month == curMonth && day == curDay) {
                    if (curMinute + 30 < 60) {
                        numberPickerHour.setMinValue(curHour);
                        numberPickerMin.setMinValue(curMinute + 30);
                    } else {
                        numberPickerHour.setMinValue(curHour + 1 >= 24 ? 0 : curHour + 1);
                        numberPickerMin.setMinValue(curMinute - 30);
                    }
                } else {
                    numberPickerHour.setMinValue(0);
                    numberPickerMin.setMinValue(0);
                }
            }
        });

        numberPickerHour.setMinValue(curHour);
        numberPickerHour.setFormatter(mFormatter2);
        numberPickerHour.setMaxValue(23);
        numberPickerHour.setValue(hour);
        numberPickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hour = newVal;
                if (year==curYear && month == curMonth && day == curDay && hour == curHour) {
                    if (curMinute + 30 < 60) {
                        numberPickerHour.setMinValue(curHour);
                        numberPickerHour.setValue(curHour);
                        numberPickerMin.setMinValue(curMinute + 30);
                        numberPickerMin.setValue(curMinute + 30);
                    } else {
                        if(curHour + 1 >= 24){
                            numberPickerDay.setMinValue(curDay + 1);
                            numberPickerDay.setValue(curDay + 1);
                        }
                        numberPickerHour.setMinValue(curHour + 1 >= 24 ? 0 : curHour + 1);
                        numberPickerHour.setValue(curHour + 1 >= 24 ? 0 : curHour + 1);
                        numberPickerMin.setMinValue(curMinute - 30);
                        numberPickerMin.setValue(curMinute - 30);
                    }
                } else {
                    numberPickerMin.setMinValue(0);
                }
            }
        });

        numberPickerMin.setMinValue(curMinute);
        numberPickerMin.setFormatter(mFormatter2);
        numberPickerMin.setMaxValue(59);
        numberPickerMin.setValue(minute);
        numberPickerMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minute = newVal;
            }
        });
        if (year==curYear && month == curMonth && day == curDay) {
            if (curMinute + 30 < 60) {
                numberPickerHour.setMinValue(curHour);
                numberPickerHour.setValue(curHour);
                numberPickerMin.setMinValue(curMinute + 30);
                numberPickerMin.setValue(curMinute + 30);
            } else {
                if(curHour + 1 >= 24){
                    numberPickerDay.setMinValue(curDay + 1);
                    numberPickerDay.setValue(curDay + 1);
                }

                numberPickerHour.setMinValue(curHour + 1 >= 24 ? 0 : curHour + 1);
                numberPickerHour.setValue(curHour + 1 >= 24 ? 0 : curHour + 1);
                numberPickerMin.setMinValue(curMinute - 30);
                numberPickerMin.setValue(curMinute - 30);
            }
        } else {
            numberPickerHour.setMinValue(0);
            numberPickerMin.setMinValue(0);
        }
    }

    private void setDayMax() {
        if (year == curYear && month == curMonth) {
            numberPickerDay.setMinValue(curDay);
        } else {
            numberPickerDay.setMinValue(1);
            numberPickerHour.setMinValue(0);
            numberPickerMin.setMinValue(0);
        }
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
                if (textView == null) {
                    return;
                }
                textView.setText(String.format("%d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, numberPickerMin.getValue(), 0));
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
