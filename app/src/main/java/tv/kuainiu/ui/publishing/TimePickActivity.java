package tv.kuainiu.ui.publishing;

import android.os.Bundle;
import android.widget.NumberPicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.ui.BaseActivity;

/**
 * Created by sirius on 2016/10/17.
 */

public class TimePickActivity extends BaseActivity {

    @BindView(R.id._numberPicker_Year)
    NumberPicker numberPickerYear2;
    @BindView(R.id.numberPicker_Month)
    NumberPicker numberPickerMonth;
    @BindView(R.id.numberPicker_Day)
    NumberPicker numberPickerDay;
    @BindView(R.id.numberPicker_Hour)
    NumberPicker numberPickerHour;
    @BindView(R.id.numberPicker_Min)
    NumberPicker numberPickerMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picktime);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
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
        numberPickerYear2.setMinValue(2016);
        numberPickerYear2.setFormatter(mFormatter);
        numberPickerYear2.setMaxValue(2116);
        numberPickerYear2.setValue(2016);

        numberPickerMonth.setMinValue(1);
        numberPickerMonth.setFormatter(mFormatter2);
        numberPickerMonth.setMaxValue(12);
        numberPickerMonth.setValue(10);

        numberPickerDay.setMinValue(1);
        numberPickerDay.setFormatter(mFormatter2);
        numberPickerDay.setMaxValue(31);
        numberPickerDay.setValue(10);

        numberPickerHour.setMinValue(0);
        numberPickerHour.setFormatter(mFormatter2);
        numberPickerHour.setMaxValue(23);
        numberPickerHour.setValue(10);

        numberPickerMin.setMinValue(0);
        numberPickerMin.setFormatter(mFormatter2);
        numberPickerMin.setMaxValue(59);
        numberPickerMin.setValue(10);
    }

    private void initView() {
    }


}
