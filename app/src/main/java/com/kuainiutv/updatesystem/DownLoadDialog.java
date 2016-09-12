package com.kuainiutv.updatesystem;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.kuainiutv.R;


public class DownLoadDialog extends Dialog implements OnClickListener {
	Context context;

	private XDialog listener;
	

	public DownLoadDialog(Context context) {
		super(context);
		this.context = context;
	}

	public DownLoadDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public interface XDialog {
		public void onClick(View view);
	}

	public DownLoadDialog(Context context, int theme,
						  XDialog listener) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.setContentView(R.layout.xzdialog);
		Button button =(Button) findViewById(R.id.dialog_button_qx);
		button.setOnClickListener(this);
		

	}

	@Override
	public void onClick(View view) {
		listener.onClick(view);
	}

	
}
