package tv.kuainiu.updatesystem;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import tv.kuainiu.R;
import tv.kuainiu.modle.AppVersion;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.util.AppUtils;


public class SystemUpdateDialog extends Dialog implements OnClickListener {
	Context context;
	AppVersion appVersion;
	private LeaveMeetingDialogListener listener;

	public SystemUpdateDialog(Context context) {
		super(context);
		this.context = context;
	}

	public SystemUpdateDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public interface LeaveMeetingDialogListener {
		public void onClick(View view);
	}

	public SystemUpdateDialog(Context context, int theme,
							  LeaveMeetingDialogListener listener) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
	}

	public SystemUpdateDialog(Context context, int theme,
							  LeaveMeetingDialogListener listener, AppVersion appVersion) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
		this.appVersion = appVersion;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_system_update_dialog);
		Button dialog_button_cancel = (Button) findViewById(R.id.dialog_button_cancel);
		dialog_button_cancel.setOnClickListener(this);
		Button dialog_button_ok = (Button) findViewById(R.id.dialog_button_ok);
		dialog_button_ok.setOnClickListener(this);
		TextView CurrentTextView = (TextView) findViewById(R.id.update_text_banben);
		TextView ServerTextView = (TextView) findViewById(R.id.update_text_zxbanben);
		TextView UpdateContentTextView = (TextView) findViewById(R.id.update_text_content);
		if (appVersion != null) {
			CurrentTextView.setText("当前版本："
					+ AppUtils.getAppVersionName(context));
			ServerTextView.setText("更新版本：" + appVersion.getVersion_name());
			UpdateContentTextView.append(appVersion.getVersion_dec());
			if (Constant.MUST_UPDATE == appVersion.getIs_force_update()) {// 强制更新
				dialog_button_cancel.setVisibility(View.GONE);
			} else {
				dialog_button_cancel.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View view) {
		listener.onClick(view);

	}

}
