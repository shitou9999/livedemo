package tv.kuainiu.ui.publishing;

import android.os.Bundle;
import android.widget.NumberPicker;

import tv.kuainiu.R;
import tv.kuainiu.ui.BaseActivity;

/**
 * Created by sirius on 2016/10/17.
 */

public class TimePickActivity extends BaseActivity {

    NumberPicker numberPickerYear2;
    NumberPicker numberPickerMonth;
    NumberPicker numberPickerDay;
    NumberPicker numberPickerHour;
    NumberPicker numberPickerMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picktime);

        initView();
        initData();
    }

    private void initView() {
        numberPickerYear2= (NumberPicker) findViewById(R.id._numberPicker_Year);
        numberPickerMonth= (NumberPicker) findViewById(R.id.numberPicker_Month);
        numberPickerDay= (NumberPicker) findViewById(R.id.numberPicker_Day);
        numberPickerHour= (NumberPicker) findViewById(R.id.numberPicker_Hour);
        numberPickerMin= (NumberPicker) findViewById(R.id.numberPicker_Min);
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
        numberPickerYear2.setMaxValue(2026);
        numberPickerYear2.setValue(2016);
        numberPickerYear2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        numberPickerMonth.setMinValue(1);
        numberPickerMonth.setFormatter(mFormatter2);
        numberPickerMonth.setMaxValue(12);
        numberPickerMonth.setValue(10);
        numberPickerMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        numberPickerDay.setMinValue(1);
        numberPickerDay.setFormatter(mFormatter2);
        numberPickerDay.setMaxValue(31);
        numberPickerDay.setValue(10);
        numberPickerDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        numberPickerHour.setMinValue(0);
        numberPickerHour.setFormatter(mFormatter2);
        numberPickerHour.setMaxValue(23);
        numberPickerHour.setValue(10);
        numberPickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        numberPickerMin.setMinValue(0);
        numberPickerMin.setFormatter(mFormatter2);
        numberPickerMin.setMaxValue(59);
        numberPickerMin.setValue(10);
        numberPickerMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });
    }


}
