package com.kuainiutv.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.kuainiutv.R;


/**
 * @ClassName CustomProgressDialog
 * @Description 自定义加载对话框
 */
public class CustomProgressDialog extends Dialog {
	private static CustomProgressDialog customProgressDialog = null;

	public CustomProgressDialog(Context context) {
		super(context);
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static CustomProgressDialog createDialog(Context context) {
		customProgressDialog = new CustomProgressDialog(context,
				R.style.CustomProgressDialog);
		customProgressDialog
				.setContentView(R.layout.common_customprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			dismiss();
			// ((Activity) context).onBackPressed();

			break;
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 
	 * [Summary] setTitile
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public CustomProgressDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	/**
	 * 
	 * [Summary] setMessage
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public CustomProgressDialog setMessage(String strMessage) {
		if (customProgressDialog != null) {
			TextView tvMsg = (TextView) customProgressDialog
					.findViewById(R.id.com_borui_common_loading_text);
			if (tvMsg != null && strMessage != null) {
				tvMsg.setVisibility(View.VISIBLE);
				tvMsg.setText(strMessage);
			} else if (tvMsg != null) {
				tvMsg.setVisibility(View.GONE);
			}
		}

		

		return customProgressDialog;
	}

	public CustomProgressDialog setMessage(String strMessage, int textColor,
										   float textSize) {
		if (customProgressDialog != null) {
			TextView tvMsg = (TextView) customProgressDialog
					.findViewById(R.id.com_borui_common_loading_text);
			if (tvMsg != null) {
				tvMsg.setTextColor(textColor);
				tvMsg.setTextSize(textSize);
				tvMsg.setText(strMessage);
			}
		}

		return customProgressDialog;
	}

	@Override
	public void dismiss() {
		if (customProgressDialog != null) {
			customProgressDialog = null;
		}
		super.dismiss();
	}

}
