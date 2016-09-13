package com.kuainiutv.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.kuainiutv.R;


/**
 * Created by guxuan on 2016/3/22.
 */
public class SelectSharpnessPopupWindow extends PopupWindow {
    private Button btnStandard, btnHigh, btnSuper, btnCancel;
    private View mMenuView;

    @SuppressLint("InflateParams")
    public SelectSharpnessPopupWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        mMenuView = inflater.inflate(R.layout.layout_dialog_sharpness, null);
        btnStandard = (Button) mMenuView.findViewById(R.id.btn_standard);
        btnHigh = (Button) mMenuView.findViewById(R.id.btn_high);
        btnSuper = (Button) mMenuView.findViewById(R.id.btn_super);
        btnCancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        // 设置按钮监听
        btnStandard.setOnClickListener(itemsOnClick);
        btnHigh.setOnClickListener(itemsOnClick);
        btnSuper.setOnClickListener(itemsOnClick);
        btnCancel.setOnClickListener(itemsOnClick);


        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}
