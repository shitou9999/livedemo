package tv.kuainiu.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.core.IWheelPicker;

import tv.kuainiu.R;

/**
 * @author AigeStudio 2015-12-08
 */
public class MainDialog extends Dialog implements View.OnClickListener {
    private View root;
    private ViewGroup container;
    private Button btnObtain;

    private IWheelPicker picker;

    private String data;

    public MainDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                container.removeAllViews();
            }
        });
        root = getLayoutInflater().inflate(R.layout.ac_main_dialog, null);
        container = (ViewGroup) root.findViewById(R.id.main_dialog_container);

        btnObtain = (Button) root.findViewById(R.id.btn_obtain);
        btnObtain.setOnClickListener(this);
    }

    @Override
    public void setContentView(View view) {
        if (view instanceof IWheelPicker) {
            picker = (IWheelPicker) view;
            picker.setOnWheelChangeListener(new AbstractWheelPicker.SimpleWheelChangeListener() {
                @Override
                public void onWheelScrollStateChanged(int state) {
                    if (state != AbstractWheelPicker.SCROLL_STATE_IDLE) {
                        btnObtain.setEnabled(false);
                    } else {
                        btnObtain.setEnabled(true);
                    }
                }

                @Override
                public void onWheelSelected(int index, String data) {
                    MainDialog.this.data = data;
                }
            });
        }
        container.addView(view);
        super.setContentView(root);
    }

    public String getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_obtain:
                if (mOnButtonClickListener != null) {
                    mOnButtonClickListener.onButtonClick(v);
                }
                break;
        }
    }

    private OnButtonClickListener mOnButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        mOnButtonClickListener = onButtonClickListener;
    }

    public interface OnButtonClickListener {
        void onButtonClick(View v);
    }
}